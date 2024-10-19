package net.lax1dude.eaglercraft.v1_8.sp.server.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.MessageEvent;
import org.teavm.jso.typedarrays.ArrayBuffer;

import com.google.common.collect.Collections2;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.Filesystem;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ES6ShimStatus;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EnumES6ShimStatus;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EnumES6Shims;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.MessageChannel;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMBlobURLManager;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class ServerPlatformSingleplayer {

	private static final Logger logger = LogManager.getLogger("ServerPlatformSingleplayer");

	private static final LinkedList<IPCPacketData> messageQueue = new LinkedList<>();

	private static boolean immediateContinueSupport = false;
	private static MessageChannel immediateContinueChannel = null;
	private static Runnable currentContinueHack = null;
	private static final Object immediateContLock = new Object();
	private static final JSString emptyJSString = JSString.valueOf("");
	private static boolean singleThreadMode = false;
	private static Consumer<IPCPacketData> singleThreadCB = null;

	private static IEaglerFilesystem filesystem = null;

	@JSFunctor
	private static interface WorkerBinaryPacketHandler extends JSObject {
		public void onMessage(String channel, ArrayBuffer buf);
	}

	private static class WorkerBinaryPacketHandlerImpl implements WorkerBinaryPacketHandler {
		
		public void onMessage(String channel, ArrayBuffer buf) {
			if(channel == null) {
				logger.error("Recieved IPC packet with null channel");
				return;
			}
			
			if(buf == null) {
				logger.error("Recieved IPC packet with null buffer");
				return;
			}
			
			synchronized(messageQueue) {
				messageQueue.add(new IPCPacketData(channel, TeaVMUtils.wrapByteArrayBuffer(buf)));
			}
		}
		
	}

	@JSBody(params = { "wb" }, script = "__eaglerXOnMessage = function(o) { wb(o.data.ch, o.data.dat); };")
	private static native void registerPacketHandler(WorkerBinaryPacketHandler wb);

	public static void register() {
		registerPacketHandler(new WorkerBinaryPacketHandlerImpl());
	}

	public static void initializeContext() {
		singleThreadMode = false;
		singleThreadCB = null;
		ES6ShimStatus shimStatus = ES6ShimStatus.getRuntimeStatus();
		if(shimStatus != null) {
			EnumES6ShimStatus stat = shimStatus.getStatus();
			switch(stat) {
			case STATUS_ERROR:
			case STATUS_DISABLED_ERRORS:
				logger.error("ES6 Shim Status: {}", stat.statusDesc);
				break;
			case STATUS_ENABLED_ERRORS:
				logger.error("ES6 Shim Status: {}", stat.statusDesc);
				dumpShims(shimStatus.getShims());
				break;
			case STATUS_DISABLED:
			case STATUS_NOT_PRESENT:
				logger.info("ES6 Shim Status: {}", stat.statusDesc);
				break;
			case STATUS_ENABLED:
				logger.info("ES6 Shim Status: {}", stat.statusDesc);
				dumpShims(shimStatus.getShims());
				break;
			default:
				break;
			}
		}
		
		TeaVMBlobURLManager.initialize();
		
		checkImmediateContinueSupport();
		
		filesystem = Filesystem.getHandleFor(getClientConfigAdapter().getWorldsDB());
		VFile2.setPrimaryFilesystem(filesystem);
	}

	public static IEaglerFilesystem getWorldsDatabase() {
		return filesystem;
	}

	public static void initializeContextSingleThread(Consumer<IPCPacketData> packetSendCallback) {
		singleThreadMode = true;
		singleThreadCB = packetSendCallback;
		filesystem = Filesystem.getHandleFor(getClientConfigAdapter().getWorldsDB());
	}

	private static void dumpShims(Set<EnumES6Shims> shims) {
		if(!shims.isEmpty()) {
			logger.info("(Enabled {} shims: {})", shims.size(), String.join(", ", Collections2.transform(shims, (shim) -> shim.shimDesc)));
		}
	}

	@JSBody(params = { "ch", "dat" }, script = "postMessage({ ch: ch, dat : dat });")
	public static native void sendPacketTeaVM(String channel, ArrayBuffer arr);

	public static void sendPacket(IPCPacketData packet) {
		if(singleThreadMode) {
			singleThreadCB.accept(packet);
		}else {
			sendPacketTeaVM(packet.channel, TeaVMUtils.unwrapArrayBuffer(packet.contents));
		}
	}

	public static List<IPCPacketData> recieveAllPacket() {
		synchronized(messageQueue) {
			if(messageQueue.size() == 0) {
				return null;
			}else {
				List<IPCPacketData> ret = new ArrayList<>(messageQueue);
				messageQueue.clear();
				return ret;
			}
		}
	}

	public static IClientConfigAdapter getClientConfigAdapter() {
		return TeaVMClientConfigAdapter.instance;
	}

	private static void checkImmediateContinueSupport() {
		try {
			immediateContinueSupport = false;
			if(!MessageChannel.supported()) {
				logger.error("Fast immediate continue will be disabled for server context due to MessageChannel being unsupported");
				return;
			}
			immediateContinueChannel = MessageChannel.create();
			immediateContinueChannel.getPort1().addEventListener("message", new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					Runnable toRun;
					synchronized(immediateContLock) {
						toRun = currentContinueHack;
						currentContinueHack = null;
					}
					if(toRun != null) {
						toRun.run();
					}
				}
			});
			immediateContinueChannel.getPort1().start();
			immediateContinueChannel.getPort2().start();
			final boolean[] checkMe = new boolean[1];
			checkMe[0] = false;
			currentContinueHack = () -> {
				checkMe[0] = true;
			};
			immediateContinueChannel.getPort2().postMessage(emptyJSString);
			if(checkMe[0]) {
				currentContinueHack = null;
				if(immediateContinueChannel != null) {
					safeShutdownChannel(immediateContinueChannel);
				}
				immediateContinueChannel = null;
				logger.error("Fast immediate continue will be disabled for server context due to actually continuing immediately");
				return;
			}
			EagUtils.sleep(10);
			currentContinueHack = null;
			if(!checkMe[0]) {
				if(immediateContinueChannel != null) {
					safeShutdownChannel(immediateContinueChannel);
				}
				immediateContinueChannel = null;
				logger.error("Fast immediate continue will be disabled for server context due to startup check failing");
			}else {
				immediateContinueSupport = true;
			}
		}catch(Throwable t) {
			logger.error("Fast immediate continue will be disabled for server context due to exceptions");
			immediateContinueSupport = false;
			if(immediateContinueChannel != null) {
				safeShutdownChannel(immediateContinueChannel);
			}
			immediateContinueChannel = null;
		}
	}

	private static void safeShutdownChannel(MessageChannel chan) {
		try {
			chan.getPort1().close();
		}catch(Throwable tt) {
		}
		try {
			chan.getPort2().close();
		}catch(Throwable tt) {
		}
	}

	public static void immediateContinue() {
		if(singleThreadMode) {
			PlatformRuntime.immediateContinue();
		}else {
			if(immediateContinueSupport) {
				immediateContinueTeaVM();
			}else {
				EagUtils.sleep(0);
			}
		}
	}

	@Async
	private static native void immediateContinueTeaVM();
	
	private static void immediateContinueTeaVM(final AsyncCallback<Void> cb) {
		synchronized(immediateContLock) {
			if(currentContinueHack != null) {
				cb.error(new IllegalStateException("Worker thread is already waiting for an immediate continue callback!"));
				return;
			}
			currentContinueHack = () -> {
				cb.complete(null);
			};
			try {
				immediateContinueChannel.getPort2().postMessage(emptyJSString);
			}catch(Throwable t) {
				logger.error("Caught error posting immediate continue, using setTimeout instead");
				Window.setTimeout(() -> cb.complete(null), 0);
			}
		}
	}

	public static boolean isSingleThreadMode() {
		return singleThreadMode;
	}

	public static void recievePacketSingleThreadTeaVM(IPCPacketData pkt) {
		synchronized(messageQueue) {
			messageQueue.add(pkt);
		}
	}

}

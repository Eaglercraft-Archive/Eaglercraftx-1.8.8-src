package net.lax1dude.eaglercraft.v1_8.sp.server.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.teavm.interop.Import;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v1_8.Filesystem;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WASMGCClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.lax1dude.eaglercraft.v1_8.sp.server.IWASMCrashCallback;
import net.lax1dude.eaglercraft.v1_8.sp.server.internal.wasm_gc_teavm.JS_IPCPacketData;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

	private static final List<IPCPacketData> messageQueue = new LinkedList<>();

	private static boolean singleThreadMode = false;
	private static Consumer<IPCPacketData> singleThreadCB = null;

	private static IEaglerFilesystem filesystem = null;

	public static void initializeContext() {
		singleThreadMode = false;
		singleThreadCB = null;
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

	public static void sendPacket(IPCPacketData packet) {
		if(singleThreadMode) {
			singleThreadCB.accept(packet);
		}else {
			ByteBuffer buf = WASMGCDirectArrayConverter.byteArrayToBuffer(packet.contents);
			try {
				sendPacket0(BetterJSStringConverter.stringToJS(packet.channel), WASMGCBufferAllocator.getUnsignedByteBufferView(buf));
			}finally {
				PlatformRuntime.freeByteBuffer(buf);
			}
		}
	}

	@Import(module = "serverPlatformSingleplayer", name = "sendPacket")
	private static native void sendPacket0(JSString channel, Uint8Array arr);

	public static List<IPCPacketData> recieveAllPacket() {
		if(singleThreadMode) {
			if(messageQueue.size() == 0) {
				return null;
			}else {
				List<IPCPacketData> ret = new ArrayList<>(messageQueue);
				messageQueue.clear();
				return ret;
			}
		}else {
			int cnt = getAvailablePackets();
			if(cnt == 0) {
				return null;
			}
			IPCPacketData[] ret = new IPCPacketData[cnt];
			for(int i = 0; i < cnt; ++i) {
				ret[i] = getNextPacket().internalize();
			}
			return Arrays.asList(ret);
		}
	}

	@Import(module = "serverPlatformSingleplayer", name = "getAvailablePackets")
	private static native int getAvailablePackets();

	@Import(module = "serverPlatformSingleplayer", name = "getNextPacket")
	private static native JS_IPCPacketData getNextPacket();

	@Import(module = "platformRuntime", name = "immediateContinue")
	public static native void immediateContinue();

	public static IClientConfigAdapter getClientConfigAdapter() {
		return WASMGCClientConfigAdapter.instance;
	}

	public static boolean isSingleThreadMode() {
		return singleThreadMode;
	}

	public static void recievePacketSingleThreadTeaVM(IPCPacketData pkt) {
		messageQueue.add(pkt);
	}

	public static void setCrashCallbackWASM(IWASMCrashCallback callback) {
		setCrashCallbackWASM0().call(callback != null ? callback::callback : null);
	}

	@JSFunctor
	private static interface JSWASMCrashCallback extends JSObject {
		void callback(String crashReport, boolean terminated);
	}

	private static interface JSWASMCrashCallbackInterface extends JSObject {
		void call(JSWASMCrashCallback callback);
	}

	@Import(module = "serverPlatformSingleplayer", name = "setCrashCallback")
	private static native JSWASMCrashCallbackInterface setCrashCallbackWASM0();

	@Import(module = "serverPlatformSingleplayer", name = "isTabAboutToClose")
	public static native boolean isTabAboutToCloseWASM();

}

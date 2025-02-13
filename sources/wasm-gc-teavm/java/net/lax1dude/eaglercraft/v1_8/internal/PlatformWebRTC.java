/*
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

package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teavm.interop.Import;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.MemoryStack;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANPeerEvent;

public class PlatformWebRTC {

	private static final Logger logger = LogManager.getLogger("PlatformWebRTC");

	private static boolean isSupported = false;

	public static void initialize() {
		isSupported = supported0();
	}

	public static boolean supported() {
		return isSupported;
	}

	@Import(module = "platformWebRTC", name = "supported")
	private static native boolean supported0();

	public static void runScheduledTasks() {
		
	}

	public static void startRTCLANClient() {
		
	}

	@Import(module = "platformWebRTC", name = "clientLANReadyState")
	public static native int clientLANReadyState();

	@Import(module = "platformWebRTC", name = "clientLANCloseConnection")
	public static native void clientLANCloseConnection();

	public static void clientLANSendPacket(byte[] pkt) {
		MemoryStack.push();
		try {
			clientLANSendPacket0(WASMGCDirectArrayConverter.byteArrayToStackU8Array(pkt));
		}finally {
			MemoryStack.pop();
		}
	}

	@Import(module = "platformWebRTC", name = "clientLANSendPacket")
	private static native void clientLANSendPacket0(Uint8Array pkt);

	public static byte[] clientLANReadPacket() {
		Uint8Array arr = clientLANReadPacket0();
		if(arr != null) {
			return WASMGCDirectArrayConverter.externU8ArrayToByteArray(arr);
		}else {
			return null;
		}
	}

	@Import(module = "platformWebRTC", name = "clientLANReadPacket")
	private static native Uint8Array clientLANReadPacket0();

	@Import(module = "platformWebRTC", name = "clientLANAvailable")
	private static native int clientLANAvailable();

	public static List<byte[]> clientLANReadAllPacket() {
		int cnt = clientLANAvailable();
		if(cnt == 0) {
			return null;
		}
		byte[][] ret = new byte[cnt][];
		for(int i = 0; i < cnt; ++i) {
			ret[i] = clientLANReadPacket();
		}
		return Arrays.asList(ret);
	}

	public static void clientLANSetICEServersAndConnect(String[] servers) {
		clientLANSetICEServersAndConnect0(BetterJSStringConverter.stringArrayToJS(servers));
	}

	@Import(module = "platformWebRTC", name = "clientLANSetICEServersAndConnect")
	private static native void clientLANSetICEServersAndConnect0(JSArray<JSString> servers);

	@Import(module = "platformWebRTC", name = "clearLANClientState")
	public static native void clearLANClientState();

	public static String clientLANAwaitICECandidate() {
		return BetterJSStringConverter.stringFromJS(clientLANAwaitICECandidate0());
	}

	@Import(module = "platformWebRTC", name = "clientLANAwaitICECandidate")
	private static native JSString clientLANAwaitICECandidate0();

	public static String clientLANAwaitDescription() {
		return BetterJSStringConverter.stringFromJS(clientLANAwaitDescription0());
	}

	@Import(module = "platformWebRTC", name = "clientLANAwaitDescription")
	private static native JSString clientLANAwaitDescription0();

	@Import(module = "platformWebRTC", name = "clientLANAwaitChannel")
	public static native boolean clientLANAwaitChannel();

	@Import(module = "platformWebRTC", name = "clientLANClosed")
	public static native boolean clientLANClosed();

	public static void clientLANSetICECandidate(String candidate) {
		clientLANSetICECandidate0(BetterJSStringConverter.stringToJS(candidate));
	}

	@Import(module = "platformWebRTC", name = "clientLANSetICECandidate")
	private static native void clientLANSetICECandidate0(JSString candidate);

	public static void clientLANSetDescription(String description) {
		clientLANSetDescription0(BetterJSStringConverter.stringToJS(description));
	}

	@Import(module = "platformWebRTC", name = "clientLANSetDescription")
	private static native void clientLANSetDescription0(JSString description);

	public static void startRTCLANServer() {
		
	}

	public static void serverLANInitializeServer(String[] servers) {
		serverLANInitializeServer0(BetterJSStringConverter.stringArrayToJS(servers));
	}

	@Import(module = "platformWebRTC", name = "serverLANInitializeServer")
	private static native void serverLANInitializeServer0(JSArray<JSString> servers);

	@Import(module = "platformWebRTC", name = "serverLANCloseServer")
	public static native void serverLANCloseServer();

	private static final int EVENT_WEBRTC_ICE = 0;
	private static final int EVENT_WEBRTC_DESC = 1;
	private static final int EVENT_WEBRTC_OPEN = 2;
	private static final int EVENT_WEBRTC_PACKET = 3;
	private static final int EVENT_WEBRTC_CLOSE = 4;

	private interface JSLANPeerEvent extends JSObject {

		@JSProperty
		int getType();

		@JSProperty
		<T> T getData();

	}

	private interface JSLANPeerHandle extends JSObject {

		@JSProperty
		JSString getPeerId();

		int countAvailableEvents();

		JSLANPeerEvent nextEvent();

		void writePacket(Uint8Array arr);

		void handleRemoteICECandidates(JSString iceCandidates);

		void handleRemoteDescription(JSString description);

		void mapIPC(String ipcChannel);

		void disconnect();

	}

	private static class LANPeer {

		private final String peerId;
		private final JSLANPeerHandle handle;
		private boolean dead = false;

		private LANPeer(String peerId, JSLANPeerHandle handle) {
			this.peerId = peerId;
			this.handle = handle;
		}

		private LANPeerEvent getEvent() {
			if(dead) return null;
			JSLANPeerEvent peerEvt = handle.nextEvent();
			switch(peerEvt.getType()) {
				case EVENT_WEBRTC_ICE: {
					return new LANPeerEvent.LANPeerICECandidateEvent(peerId,
							BetterJSStringConverter.stringFromJS(peerEvt.getData()));
				}
				case EVENT_WEBRTC_DESC: {
					return new LANPeerEvent.LANPeerDescriptionEvent(peerId,
							BetterJSStringConverter.stringFromJS(peerEvt.getData()));
				}
				case EVENT_WEBRTC_OPEN: {
					return new LANPeerEvent.LANPeerDataChannelEvent(peerId);
				}
				case EVENT_WEBRTC_PACKET: {
					return new LANPeerEvent.LANPeerPacketEvent(peerId,
							WASMGCDirectArrayConverter.externU8ArrayToByteArray(peerEvt.getData()));
				}
				case EVENT_WEBRTC_CLOSE: {
					return new LANPeerEvent.LANPeerDisconnectEvent(peerId);
				}
				default: {
					throw new IllegalStateException();
				}
			}
		}

		private List<LANPeerEvent> getAllEvent() {
			if(dead) return null;
			int cnt = handle.countAvailableEvents();
			if(cnt == 0) {
				return null;
			}
			LANPeerEvent[] lst = new LANPeerEvent[cnt];
			for(int i = 0; i < cnt; ++i) {
				lst[i] = getEvent();
			}
			return Arrays.asList(lst);
		}

		private void writePacket(byte[] pkt) {
			if(dead) return;
			MemoryStack.push();
			try {
				handle.writePacket(WASMGCDirectArrayConverter.byteArrayToStackU8Array(pkt));
			}finally {
				MemoryStack.pop();
			}
		}

		private void handleRemoteICECandidates(String iceCandidates) {
			if(dead) return;
			handle.handleRemoteICECandidates(BetterJSStringConverter.stringToJS(iceCandidates));
		}

		private void handleRemoteDescription(String description) {
			if(dead) return;
			handle.handleRemoteDescription(BetterJSStringConverter.stringToJS(description));
		}

		private void mapIPC(String ipcChannel) {
			if(dead) return;
			handle.mapIPC(ipcChannel);
		}

		private void disconnect() {
			if(!dead) {
				dead = true;
				handle.disconnect();
			}
		}

	}

	private static final Map<String, LANPeer> lanServerPeers = new HashMap<>();

	public static void serverLANCreatePeer(String peer) {
		JSLANPeerHandle handle = serverLANCreatePeer0(BetterJSStringConverter.stringToJS(peer));
		if(handle != null) {
			lanServerPeers.put(peer, new LANPeer(peer, handle));
		}else {
			logger.error("Failed to create peer for client \"{}\"!", peer);
		}
	}

	@Import(module = "platformWebRTC", name = "serverLANCreatePeer")
	private static native JSLANPeerHandle serverLANCreatePeer0(JSString peer);

	public static LANPeerEvent serverLANGetEvent(String peer) {
		LANPeer lanPeer = lanServerPeers.get(peer);
		if(lanPeer != null) {
			return lanPeer.getEvent();
		}else {
			return null;
		}
	}

	public static List<LANPeerEvent> serverLANGetAllEvent(String peer) {
		LANPeer lanPeer = lanServerPeers.get(peer);
		if(lanPeer != null) {
			return lanPeer.getAllEvent();
		}else {
			return null;
		}
	}

	public static void serverLANWritePacket(String peer, byte[] data) {
		LANPeer lanPeer = lanServerPeers.get(peer);
		if(lanPeer != null) {
			lanPeer.writePacket(data);
		}
	}

	public static void serverLANPeerICECandidates(String peer, String iceCandidates) {
		LANPeer lanPeer = lanServerPeers.get(peer);
		if(lanPeer != null) {
			lanPeer.handleRemoteICECandidates(iceCandidates);
		}
	}

	public static void serverLANPeerDescription(String peer, String description) {
		LANPeer lanPeer = lanServerPeers.get(peer);
		if(lanPeer != null) {
			lanPeer.handleRemoteDescription(description);
		}
	}

	public static void serverLANPeerMapIPC(String peer, String ipcChannel) {
		LANPeer lanPeer = lanServerPeers.get(peer);
		if(lanPeer != null) {
			lanPeer.mapIPC(ipcChannel);
		}
	}

	public static void serverLANDisconnectPeer(String peer) {
		LANPeer lanPeer = lanServerPeers.remove(peer);
		if(lanPeer != null) {
			lanPeer.disconnect();
		}
	}

	public static int countPeers() {
		return lanServerPeers.size();
	}

}
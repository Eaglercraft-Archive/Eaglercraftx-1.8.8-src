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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.webaudio.AnalyserNode;
import org.teavm.jso.webaudio.AudioContext;
import org.teavm.jso.webaudio.AudioNode;
import org.teavm.jso.webaudio.GainNode;
import org.teavm.jso.webaudio.MediaStream;
import org.teavm.jso.webaudio.MediaStreamAudioDestinationNode;
import org.teavm.jso.webaudio.MediaStreamAudioSourceNode;
import org.teavm.jso.webaudio.PannerNode;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelReadyState;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelType;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;

public class PlatformVoiceClient {

	private static final Logger logger = LogManager.getLogger("PlatformVoiceClient");

	static boolean support = false;

	private interface JSVoicePeerHandle extends JSObject {

		@JSProperty
		int getObjId();
		
		void setRemoteDescription(JSString desc);
		
		void addRemoteICECandidate(JSString ice);
		
		void closeHandle();
		
	}

	private interface JSVoicePeerICEEvent extends JSObject {
		
		@JSProperty
		int getObjId();
		
		@JSProperty
		JSString getData();
		
	}

	private interface JSVoicePeerOpenEvent extends JSObject {
		
		@JSProperty
		int getObjId();
		
		@JSProperty
		MediaStream getStream();
		
	}

	private interface JSVoicePeerCloseEvent extends JSObject {
		
		@JSProperty
		int getObjId();
		
	}

	private static final int EVENT_VOICE_ICE = 0;
	private static final int EVENT_VOICE_DESC = 1;
	private static final int EVENT_VOICE_OPEN = 2;
	private static final int EVENT_VOICE_CLOSE = 3;

	static void handleJSEvent(PlatformRuntime.JSEagRuntimeEvent evt) {
		switch(evt.getEventType() & 31) {
			case EVENT_VOICE_ICE: {
				JSVoicePeerICEEvent obj = evt.getEventObj();
				VoicePeer peer = peerListI.get(obj.getObjId());
				if(peer != null) {
					peer.handleEventLocalICECandidate(BetterJSStringConverter.stringFromJS(obj.getData()));
				}
				break;
			}
			case EVENT_VOICE_DESC: {
				JSVoicePeerICEEvent obj = evt.getEventObj();
				VoicePeer peer = peerListI.get(obj.getObjId());
				if(peer != null) {
					peer.handleEventLocalDescription(BetterJSStringConverter.stringFromJS(obj.getData()));
				}
				break;
			}
			case EVENT_VOICE_OPEN: {
				JSVoicePeerOpenEvent obj = evt.getEventObj();
				VoicePeer peer = peerListI.get(obj.getObjId());
				if(peer != null) {
					peer.handleEventOpened(obj.getStream());
				}
				break;
			}
			case EVENT_VOICE_CLOSE: {
				JSVoicePeerCloseEvent obj = evt.getEventObj();
				VoicePeer peer = peerListI.remove(obj.getObjId());
				if(peer != null) {
					peerList.remove(peer.peerId);
					peer.handleEventClosed();
				}
				break;
			}
		}
	}

	static final Map<EaglercraftUUID, VoicePeer> peerList = new HashMap<>();
	static final IntObjectMap<VoicePeer> peerListI = new IntObjectHashMap<>();

	private static class VoicePeer {

		private final int objId;
		private final EaglercraftUUID peerId;
		private final JSVoicePeerHandle jsHandle;

		private MediaStreamAudioSourceNode audioNode = null;

		private MediaStream rawStream = null;
		private AnalyserNode analyser = null;
		private GainNode gain = null;
		private PannerNode panner = null;
		private AudioNode recNode = null;
		
		private boolean dead = false;
		
		private VoicePeer(int objId, EaglercraftUUID peerId, JSVoicePeerHandle jsHandle) {
			this.objId = objId;
			this.peerId = peerId;
			this.jsHandle = jsHandle;
		}

		private void handleEventLocalICECandidate(String data) {
			VoiceClientController.sendPacketICE(peerId, data);
		}

		private void handleEventLocalDescription(String data) {
			VoiceClientController.sendPacketDesc(peerId, data);
		}

		private void handlePacketRemoteDescription(String descJSON) {
			jsHandle.setRemoteDescription(BetterJSStringConverter.stringToJS(descJSON));
		}

		private void handlePacketRemoteICECandidate(String candidate) {
			jsHandle.addRemoteICECandidate(BetterJSStringConverter.stringToJS(candidate));
		}

		private void makeGlobal() {
			if (audioNode == null) {
				return;
			}
			if (panner != null) {
				if (gain != null) {
					panner.disconnect(gain);
				}
				audioNode.disconnect(panner);
				panner = null;
			} else if (gain != null) {
				audioNode.disconnect(gain);
			}
			gain = PlatformAudio.audioctx.createGain();
			gain.getGain().setValue(VoiceClientController.getVoiceListenVolume());
			audioNode.connect(gain);
			gain.connect(PlatformAudio.audioctx.getDestination());
			if(PlatformAudio.gameRecGain != null) {
				gain.connect(PlatformAudio.gameRecGain);
			}
			VoiceClientController.getVoiceListening().add(peerId);
			recNode = gain;
		}

		private void makeProximity() {
			if (audioNode == null) {
				return;
			}
			if (panner != null) {
				if (gain != null) {
					panner.disconnect(gain);
				}
				audioNode.disconnect(panner);
				panner = null;
			} else if (gain != null) {
				audioNode.disconnect(gain);
			}
			panner = PlatformAudio.audioctx.createPanner();
			float vol = VoiceClientController.getVoiceListenVolume();
			PlatformAudio.setupPanner(panner, vol * 2 * VoiceClientController.getVoiceProximity() + 0.1f, 0, 0, 0);
			gain = PlatformAudio.audioctx.createGain();
			gain.getGain().setValue(vol);
			audioNode.connect(gain);
			gain.connect(panner);
			panner.connect(PlatformAudio.audioctx.getDestination());
			if(PlatformAudio.gameRecGain != null) {
				panner.connect(PlatformAudio.gameRecGain);
			}
			VoiceClientController.getVoiceListening().add(peerId);
			recNode = panner;
		}

		private void handleEventOpened(MediaStream stream) {
			rawStream = stream;
			audioNode = PlatformAudio.audioctx.createMediaStreamSource(stream);
			analyser = PlatformAudio.audioctx.createAnalyser();
			analyser.setSmoothingTimeConstant(0f);
			analyser.setFftSize(32);
			audioNode.connect(analyser);
			if (VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.GLOBAL) {
				makeGlobal();
			} else if (VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.PROXIMITY) {
				makeProximity();
			}
			if (VoiceClientController.getVoiceMuted().contains(peerId)) mute(true);
		}

		private void mute(boolean muted) {
			if(rawStream != null) {
				PlatformVoiceClient.mute(rawStream, muted);
			}
		}

		private void handlePacketClosed() {
			closeHandle(true);
		}

		private void handleEventClosed() {
			if(!dead) {
				dead = true;
				cleanup(false);
			}
		}

		private void closeHandle(boolean quiet) {
			if(!dead) {
				dead = true;
				jsHandle.closeHandle();
				peerListI.remove(objId);
				peerList.remove(peerId);
				cleanup(quiet);
			}
		}

		private void cleanup(boolean quiet) {
			if(analyser != null) {
				analyser.disconnect();
				analyser = null;
			}
			if(gain != null) {
				gain.disconnect();
				gain = null;
			}
			if(panner != null) {
				panner.disconnect();
				panner = null;
			}
			if(rawStream != null) {
				rawStream = null;
			}
			VoiceClientController.getVoiceListening().remove(peerId);
			if (!quiet) {
				VoiceClientController.sendPacketDisconnectPeer(peerId);
			}
		}

	}

	static void initialize() {
		support = PlatformWebRTC.supported() && isSupported0();
		peerList.clear();
		peerListI.clear();
	}

	public static boolean isSupported() {
		return support;
	}

	@Import(module = "platformVoiceClient", name = "isSupported")
	private static native boolean isSupported0();

	public static String iceServers = null;
	public static boolean hasInit = false;
	public static MediaStreamAudioDestinationNode localMediaStream;
	public static GainNode localMediaStreamGain;
	public static MediaStream localRawMediaStream;
	public static EnumVoiceChannelReadyState readyState = EnumVoiceChannelReadyState.NONE;
	public static AudioContext microphoneVolumeAudioContext = null;

	public static void setICEServers(String[] urls) {
		if (urls == null || urls.length == 0) {
			iceServers = null;
			return;
		}
		JSONArray arr = new JSONArray();
		for (String url : urls) {
			String[] etr = url.split(";");
			if (etr.length == 1) {
				JSONObject m = new JSONObject();
				m.put("urls", etr[0]);
				arr.put(m);
			} else if (etr.length == 3) {
				JSONObject m = new JSONObject();
				m.put("urls", etr[0]);
				m.put("username", etr[1]);
				m.put("credential", etr[2]);
				arr.put(m);
			}
		}
		iceServers = arr.toString();
	}

	@JSBody(params = { "rawStream", "muted" }, script = "return rawStream.getAudioTracks()[0].enabled = !muted;")
	static native void mute(MediaStream rawStream, boolean muted);

	public static void activateVoice(boolean talk) {
		if (hasInit) {
			mute(localRawMediaStream, !talk);
		}
	}

	public static void initializeDevices() {
		if (!hasInit) {
			localRawMediaStream = PlatformScreenRecord.getMic();
			if (localRawMediaStream == null) {
				readyState = EnumVoiceChannelReadyState.ABORTED;
				return;
			}
			microphoneVolumeAudioContext = new AudioContext();
			mute(localRawMediaStream, true);
			localMediaStream = microphoneVolumeAudioContext.createMediaStreamDestination();
			localMediaStreamGain = microphoneVolumeAudioContext.createGain();
			microphoneVolumeAudioContext.createMediaStreamSource(localRawMediaStream).connect(localMediaStreamGain);
			localMediaStreamGain.connect(localMediaStream);
			localMediaStreamGain.getGain().setValue(1.0F);
			readyState = EnumVoiceChannelReadyState.DEVICE_INITIALIZED;
			hasInit = true;
		} else {
			readyState = EnumVoiceChannelReadyState.DEVICE_INITIALIZED;
		}
	}

	public static void tickVoiceClient() {
		for (VoicePeer voicePlayer : peerList.values()) {
			AnalyserNode analyser = voicePlayer.analyser;
			if(analyser != null) {
				Uint8Array array = new Uint8Array(analyser.getFrequencyBinCount());
				analyser.getByteFrequencyData(array);
				int len = array.getLength();
				for (int i = 0; i < len; i++) {
					if (array.get(i) >= 0.1f) {
						VoiceClientController.getVoiceSpeaking().add(voicePlayer.peerId);
						break;
					}
				}
			}
		}
	}

	public static void setMicVolume(float val) {
		if (hasInit) {
			if(val > 0.5F) val = 0.5F + (val - 0.5F) * 2.0F;
			if(val > 1.5F) val = 1.5F;
			if(val < 0.0F) val = 0.0F;
			localMediaStreamGain.getGain().setValue(val * 2.0F);
		}
	}

	public static EnumVoiceChannelReadyState getReadyState() {
		return readyState;
	}

	public static void signalConnect(EaglercraftUUID peerId, boolean offer) {
		if(iceServers == null) {
			logger.error("No ICE servers provided for {}", peerId);
			return;
		}
		JSVoicePeerHandle peerHandle = createRTCPeerConnection(
				BetterJSStringConverter.stringToJS(iceServers), offer,
				localMediaStream.getStream());
		if(peerHandle != null) {
			int obj = peerHandle.getObjId();
			VoicePeer peer = new VoicePeer(obj, peerId, peerHandle);
			peerList.put(peerId, peer);
			peerListI.put(obj, peer);
		}else {
			logger.error("Could not create peer for {}", peerId);
		}
	}

	@Import(module = "platformVoiceClient", name = "createRTCPeerConnection")
	private static native JSVoicePeerHandle createRTCPeerConnection(JSString iceServers, boolean offer, JSObject localStream);

	public static void signalDescription(EaglercraftUUID peerId, String descJSON) {
		VoicePeer peer = peerList.get(peerId);
		if (peer != null) {
			peer.handlePacketRemoteDescription(descJSON);
		}
	}

	public static void signalDisconnect(EaglercraftUUID peerId, boolean quiet) {
		VoicePeer peer = peerList.remove(peerId);
		if (peer != null) {
			peer.handlePacketClosed();
		}
	}

	public static void setVoiceProximity(int prox) {
		for (VoicePeer player : peerList.values()) {
			if(player.panner != null) {
				player.panner.setMaxDistance(VoiceClientController.getVoiceListenVolume() * 2 * prox + 0.1f);
			}
		}
	}

	public static void updateVoicePosition(EaglercraftUUID uuid, double x, double y, double z) {
		VoicePeer player = peerList.get(uuid);
		if (player != null && player.panner != null) player.panner.setPosition((float) x, (float) y, (float) z);
	}

	public static void mutePeer(EaglercraftUUID peerId, boolean muted) {
		VoicePeer peer = peerList.get(peerId);
		if (peer != null) {
			peer.mute(muted);
		}
	}

	public static void signalICECandidate(EaglercraftUUID peerId, String candidate) {
		VoicePeer peer = peerList.get(peerId);
		if (peer != null) {
			peer.handlePacketRemoteICECandidate(candidate);
		}
	}

	public static void makePeerGlobal(EaglercraftUUID peerId) {
		VoicePeer peer = peerList.get(peerId);
		if (peer != null) {
			peer.makeGlobal();
		}
	}

	public static void makePeerProximity(EaglercraftUUID peerId) {
		VoicePeer peer = peerList.get(peerId);
		if (peer != null) {
			peer.makeProximity();
		}
	}

	static void addRecordingDest(AudioNode destNode) {
		for(VoicePeer peer : peerList.values()) {
			if(peer.recNode != null) {
				peer.recNode.connect(destNode);
			}
		}
	}

	static void removeRecordingDest(AudioNode destNode) {
		for(VoicePeer peer : peerList.values()) {
			try {
				if(peer.recNode != null) {
					peer.recNode.disconnect(destNode);
				}
			}catch(Throwable t) {
			}
		}
	}

	public static void setVoiceListenVolume(float f) {
		for (VoicePeer peer : peerList.values()) {
			if(peer.gain != null) {
				float val = f;
				if(val > 0.5f) val = 0.5f + (val - 0.5f) * 3.0f;
				if(val > 2.0f) val = 2.0f;
				if(val < 0.0f) val = 0.0f;
				peer.gain.getGain().setValue(val * 2.0f);
			}
			if(peer.panner != null) {
				peer.panner.setMaxDistance(f * 2 * VoiceClientController.getVoiceProximity() + 0.1f);
			}
		}
	}

}
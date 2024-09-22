package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelPeerState;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelReadyState;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelType;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLAudioElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.json.JSON;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.webaudio.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2022-2024 ayunami2000. All Rights Reserved.
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
public class PlatformVoiceClient {

	private static final Logger logger = LogManager.getLogger("PlatformVoiceClient");

	@JSBody(params = {}, script = "return typeof navigator.mediaDevices !== \"undefined\" && typeof navigator.mediaDevices.getUserMedia !== \"undefined\";")
	private static native boolean isSupported0();

	private static final int SRC_OBJECT_SUPPORT_NONE = -1;
	private static final int SRC_OBJECT_SUPPORT_CORE = 0;
	private static final int SRC_OBJECT_SUPPORT_MOZ = 1;

	static boolean hasCheckedSupport = false;
	static boolean support = false;
	static int srcObjectSupport = SRC_OBJECT_SUPPORT_NONE;

	public static boolean isSupported() {
		if(!hasCheckedSupport) {
			support = PlatformWebRTC.supported() && isSupported0();
			if(support) {
				srcObjectSupport = checkSrcObjectSupport(PlatformRuntime.doc);
				if(srcObjectSupport == SRC_OBJECT_SUPPORT_NONE) {
					support = false;
				}else if(srcObjectSupport == SRC_OBJECT_SUPPORT_MOZ) {
					logger.info("Using moz- prefix for HTMLMediaElement.srcObject");
				}
			}
			hasCheckedSupport = true;
		}
		return support;
	}

	@JSBody(params = { "item" }, script = "return item.streams[0];")
	static native MediaStream getFirstStream(JSObject item);

	@JSBody(params = { "doc" }, script = "var aud = doc.createElement(\"audio\"); return (typeof aud.srcObject !== \"undefined\") ? 0 : ((typeof aud.mozSrcObject !== \"undefined\") ? 1 : -1);")
	static native int checkSrcObjectSupport(HTMLDocument doc);

	static void setSrcObject(HTMLAudioElement aud, MediaStream stream) {
		switch(srcObjectSupport) {
		case SRC_OBJECT_SUPPORT_CORE:
			setSrcObjectCore(aud, stream);
			break;
		case SRC_OBJECT_SUPPORT_MOZ:
			setMozSrcObject(aud, stream);
			break;
		default:
			throw new IllegalStateException();
		}
	}

	@JSBody(params = { "aud", "stream" }, script = "return aud.srcObject = stream;")
	private static native void setSrcObjectCore(HTMLAudioElement aud, MediaStream stream);

	@JSBody(params = { "aud", "stream" }, script = "return aud.mozSrcObject = stream;")
	private static native void setMozSrcObject(HTMLAudioElement aud, MediaStream stream);

	@JSBody(params = { "pc", "stream" }, script = "return stream.getTracks().forEach(function(track) { pc.addTrack(track, stream); });")
	static native void addStream(JSObject pc, MediaStream stream);

	@JSBody(params = { "rawStream", "muted" }, script = "return rawStream.getAudioTracks()[0].enabled = !muted;")
	static native void mute(MediaStream rawStream, boolean muted);

	@JSBody(params = { "peerConnection", "str" }, script = "return peerConnection.addIceCandidate(new RTCIceCandidate(JSON.parse(str)));")
	static native void addCoreIceCandidate(JSObject peerConnection, String str);

	@JSBody(params = { "peerConnection", "str" }, script = "return peerConnection.addIceCandidate(new mozRTCIceCandidate(JSON.parse(str)));")
	static native void addMozIceCandidate(JSObject peerConnection, String str);

	static void addIceCandidate(JSObject peerConnection, String str) {
		if(!PlatformWebRTC.hasCheckedSupport) PlatformWebRTC.supported();
		switch(PlatformWebRTC.supportedImpl) {
		case PlatformWebRTC.WEBRTC_SUPPORT_CORE:
		case PlatformWebRTC.WEBRTC_SUPPORT_WEBKIT:
			addCoreIceCandidate(peerConnection, str);
			break;
		case PlatformWebRTC.WEBRTC_SUPPORT_MOZ:
			addMozIceCandidate(peerConnection, str);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void disconnect(JSObject peerConnection) {
		PlatformWebRTC.closeIt(peerConnection);
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

	public static class VoicePeer {

		public final EaglercraftUUID peerId;
		public final JSObject peerConnection;
		public MediaStream rawStream;
		
		private AnalyserNode analyser = null;
		private GainNode gain = null;
		private PannerNode panner = null;
		private AudioNode recNode = null;
		
		public VoicePeer(EaglercraftUUID peerId, JSObject peerConnection, boolean offer) {
			this.peerId = peerId;
			this.peerConnection = peerConnection;

			TeaVMUtils.addEventListener(peerConnection, "icecandidate", (EventListener<Event>) evt -> {
				if (PlatformWebRTC.hasCandidate(evt)) {
					JSONObject m = new JSONObject();
					m.put("sdpMLineIndex", "" + PlatformWebRTC.getSdpMLineIndex(evt));
					m.put("candidate", PlatformWebRTC.getCandidate(evt));
					VoiceClientController.sendPacketICE(peerId, m.toString());
				}
			});
			TeaVMUtils.addEventListener(peerConnection, "track", (EventListener<Event>) evt -> {
				rawStream = getFirstStream(evt);
				HTMLAudioElement aud = (HTMLAudioElement) PlatformRuntime.doc.createElement("audio");
				aud.setAutoplay(true);
				aud.setMuted(true);
				setSrcObject(aud, rawStream);
				handlePeerTrack(this, rawStream);
			});

			addStream(peerConnection, localMediaStream.getStream());
			if (offer) {
				PlatformWebRTC.createOffer(peerConnection, desc -> {
					PlatformWebRTC.setLocalDescription(peerConnection, desc, () -> {
						VoiceClientController.sendPacketDesc(peerId, JSON.stringify(desc));
					}, err -> {
						logger.error("Failed to set local description for \"{}\"! {}", peerId, err);
						if (peerStateInitial == EnumVoiceChannelPeerState.LOADING) {
							peerStateInitial = EnumVoiceChannelPeerState.FAILED;
						}
						signalDisconnect(VoicePeer.this, false);
					});
				}, err -> {
					logger.error("Failed to set create offer for \"{}\"! {}", peerId, err);
					if (peerStateInitial == EnumVoiceChannelPeerState.LOADING) {
						peerStateInitial = EnumVoiceChannelPeerState.FAILED;
					}
					signalDisconnect(VoicePeer.this, false);
				});
			}

			TeaVMUtils.addEventListener(peerConnection, "connectionstatechange", (EventListener<Event>) evt -> {
				String cs = PlatformWebRTC.getConnectionState(peerConnection);
				if ("disconnected".equals(cs)) {
					signalDisconnect(VoicePeer.this, false);
				} else if ("connected".equals(cs)) {
					if (peerState != EnumVoiceChannelPeerState.SUCCESS) {
						peerState = EnumVoiceChannelPeerState.SUCCESS;
					}
				} else if ("failed".equals(cs)) {
					if (peerState == EnumVoiceChannelPeerState.LOADING) {
						peerState = EnumVoiceChannelPeerState.FAILED;
					}
					signalDisconnect(VoicePeer.this, false);
				}
			});
		}

		public void disconnect() {
			PlatformVoiceClient.disconnect(peerConnection);
		}

		public void mute(boolean muted) {
			PlatformVoiceClient.mute(rawStream, muted);
		}

		public void setRemoteDescription(String descJSON) {
			try {
				JSONObject remoteDesc = new JSONObject(descJSON);
				PlatformWebRTC.setRemoteDescription2(peerConnection, JSON.parse(descJSON), () -> {
					if (remoteDesc.has("type") && "offer".equals(remoteDesc.getString("type"))) {
						PlatformWebRTC.createAnswer(peerConnection, desc -> {
							PlatformWebRTC.setLocalDescription(peerConnection, desc, () -> {
								VoiceClientController.sendPacketDesc(peerId, JSON.stringify(desc));
								if (peerStateDesc != EnumVoiceChannelPeerState.SUCCESS) peerStateDesc = EnumVoiceChannelPeerState.SUCCESS;
							}, err -> {
								logger.error("Failed to set local description for \"{}\"! {}", peerId, err.getMessage());
								if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
								signalDisconnect(VoicePeer.this, false);
							});
						}, err -> {
							logger.error("Failed to create answer for \"{}\"! {}", peerId, err.getMessage());
							if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
							signalDisconnect(VoicePeer.this, false);
						});
					}
				}, err -> {
					logger.error("Failed to set remote description for \"{}\"! {}", peerId, err.getMessage());
					if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
					signalDisconnect(VoicePeer.this, false);
				});
			} catch (Throwable err) {
				logger.error("Failed to parse remote description for \"{}\"! {}", peerId, err.getMessage());
				if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
				signalDisconnect(VoicePeer.this, false);
			}
		}

		public void addICECandidate(String candidate) {
			try {
				addIceCandidate(peerConnection, candidate);
				if (peerStateIce != EnumVoiceChannelPeerState.SUCCESS) peerStateIce = EnumVoiceChannelPeerState.SUCCESS;
			} catch (Throwable err) {
				logger.error("Failed to parse ice candidate for \"{}\"! {}", peerId, err.getMessage());
				if (peerStateIce == EnumVoiceChannelPeerState.LOADING) peerStateIce = EnumVoiceChannelPeerState.FAILED;
				signalDisconnect(VoicePeer.this, false);
			}
		}
	}

	public static Set<Map<String, String>> iceServers = new HashSet<>();
	public static boolean hasInit = false;
	public static final Map<EaglercraftUUID, VoicePeer> peerList = new HashMap<>();
	public static MediaStreamAudioDestinationNode localMediaStream;
	public static GainNode localMediaStreamGain;
	public static MediaStream localRawMediaStream;
	public static EnumVoiceChannelReadyState readyState = EnumVoiceChannelReadyState.NONE;
	public static EnumVoiceChannelPeerState peerState = EnumVoiceChannelPeerState.LOADING;
	public static EnumVoiceChannelPeerState peerStateConnect = EnumVoiceChannelPeerState.LOADING;
	public static EnumVoiceChannelPeerState peerStateInitial = EnumVoiceChannelPeerState.LOADING;
	public static EnumVoiceChannelPeerState peerStateDesc = EnumVoiceChannelPeerState.LOADING;
	public static EnumVoiceChannelPeerState peerStateIce = EnumVoiceChannelPeerState.LOADING;
	public static AudioContext microphoneVolumeAudioContext = null;

	public static void setICEServers(String[] urls) {
		iceServers.clear();
		if (urls == null) return;
		for (String url : urls) {
			String[] etr = url.split(";");
			if (etr.length == 1) {
				Map<String, String> m = new HashMap<>();
				m.put("urls", etr[0]);
				iceServers.add(m);
			} else if (etr.length == 3) {
				Map<String, String> m = new HashMap<>();
				m.put("urls", etr[0]);
				m.put("username", etr[1]);
				m.put("credential", etr[2]);
				iceServers.add(m);
			}
		}
	}

	public static void activateVoice(boolean talk) {
		if (hasInit) {
			PlatformVoiceClient.mute(localRawMediaStream, !talk);
		}
	}

	public static void initializeDevices() {
		if (!hasInit) {
			localRawMediaStream = PlatformScreenRecord.getMic();
			if (localRawMediaStream == null) {
				readyState = EnumVoiceChannelReadyState.ABORTED;
				return;
			}
			microphoneVolumeAudioContext = AudioContext.create();
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
				Uint8Array array = Uint8Array.create(analyser.getFrequencyBinCount());
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

	public static void resetPeerStates() {
		peerState = peerStateConnect = peerStateInitial = peerStateDesc = peerStateIce = EnumVoiceChannelPeerState.LOADING;
	}

	public static EnumVoiceChannelPeerState getPeerState() {
		return peerState;
	}

	public static EnumVoiceChannelPeerState getPeerStateConnect() {
		return peerStateConnect;
	}

	public static EnumVoiceChannelPeerState getPeerStateInitial() {
		return peerStateInitial;
	}

	public static EnumVoiceChannelPeerState getPeerStateDesc() {
		return peerStateDesc;
	}

	public static EnumVoiceChannelPeerState getPeerStateIce() {
		return peerStateIce;
	}

	public static EnumVoiceChannelReadyState getReadyState() {
		return readyState;
	}

	public static void signalConnect(EaglercraftUUID peerId, boolean offer) {
		if (!hasInit) initializeDevices();
		try {
			JSObject peerConnection = PlatformWebRTC.createRTCPeerConnection(JSONWriter.valueToString(iceServers));
			VoicePeer peerInstance = new VoicePeer(peerId, peerConnection, offer);
			peerList.put(peerId, peerInstance);
			if (peerStateConnect != EnumVoiceChannelPeerState.SUCCESS) peerStateConnect = EnumVoiceChannelPeerState.SUCCESS;
		} catch (Throwable e) {
			if (peerStateConnect == EnumVoiceChannelPeerState.LOADING) peerStateConnect = EnumVoiceChannelPeerState.FAILED;
		}
	}

	public static void signalDescription(EaglercraftUUID peerId, String descJSON) {
		VoicePeer peer = peerList.get(peerId);
		if (peer != null) {
			peer.setRemoteDescription(descJSON);
		}
	}

	public static void signalDisconnect(EaglercraftUUID peerId, boolean quiet) {
		VoicePeer peer = peerList.get(peerId);
		if (peer != null) {
			signalDisconnect(peer, quiet);
		}
	}

	private static void signalDisconnect(VoicePeer peer, boolean quiet) {
		peerList.remove(peer.peerId, peer);
		try {
			peer.disconnect();
		} catch (Throwable ignored) {}
		handlePeerDisconnect(peer, quiet);
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
			peer.addICECandidate(candidate);
		}
	}

	private static void handlePeerTrack(VoicePeer peer, MediaStream audioStream) {
		if (VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.NONE) return;
		MediaStreamAudioSourceNode audioNode = PlatformAudio.audioctx.createMediaStreamSource(audioStream);
		AnalyserNode analyser = PlatformAudio.audioctx.createAnalyser();
		analyser.setSmoothingTimeConstant(0f);
		analyser.setFftSize(32);
		audioNode.connect(analyser);
		if (VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.GLOBAL) {
			GainNode gain = PlatformAudio.audioctx.createGain();
			gain.getGain().setValue(VoiceClientController.getVoiceListenVolume());
			audioNode.connect(gain);
			gain.connect(PlatformAudio.audioctx.getDestination());
			if(PlatformAudio.gameRecGain != null) {
				gain.connect(PlatformAudio.gameRecGain);
			}
			VoiceClientController.getVoiceListening().add(peer.peerId);
			peer.analyser = analyser;
			peer.gain = gain;
			peer.recNode = gain;
		} else if (VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.PROXIMITY) {
			PannerNode panner = PlatformAudio.audioctx.createPanner();
			panner.setRolloffFactor(1f);
			panner.setDistanceModel("linear");
			panner.setPanningModel("HRTF");
			panner.setConeInnerAngle(360f);
			panner.setConeOuterAngle(0f);
			panner.setConeOuterGain(0f);
			panner.setOrientation(0f, 1f, 0f);
			panner.setPosition(0, 0, 0);
			float vol = VoiceClientController.getVoiceListenVolume();
			panner.setMaxDistance(vol * 2 * VoiceClientController.getVoiceProximity() + 0.1f);
			GainNode gain = PlatformAudio.audioctx.createGain();
			gain.getGain().setValue(vol);
			audioNode.connect(gain);
			gain.connect(panner);
			panner.connect(PlatformAudio.audioctx.getDestination());
			if(PlatformAudio.gameRecGain != null) {
				panner.connect(PlatformAudio.gameRecGain);
			}
			VoiceClientController.getVoiceListening().add(peer.peerId);
			peer.analyser = analyser;
			peer.panner = panner;
			peer.gain = gain;
			peer.recNode = panner;
		}
		if (VoiceClientController.getVoiceMuted().contains(peer.peerId)) mutePeer(peer.peerId, true);
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

	private static void handlePeerDisconnect(VoicePeer peer, boolean quiet) {
		if(peer.analyser != null) {
			peer.analyser.disconnect();
			peer.analyser = null;
		}
		if(peer.gain != null) {
			peer.gain.disconnect();
			peer.gain = null;
		}
		if(peer.panner != null) {
			peer.panner.disconnect();
			peer.panner = null;
		}
		VoiceClientController.getVoiceListening().remove(peer.peerId);
		if (!quiet) {
			VoiceClientController.sendPacketDisconnectPeer(peer.peerId);
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

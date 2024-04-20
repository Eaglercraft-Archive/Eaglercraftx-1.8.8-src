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

	private static final HashMap<EaglercraftUUID, AnalyserNode> voiceAnalysers = new HashMap<>();
	private static final HashMap<EaglercraftUUID, GainNode> voiceGains = new HashMap<>();
	private static final HashMap<EaglercraftUUID, PannerNode> voicePanners = new HashMap<>();

	@JSBody(params = {}, script = "return typeof window.RTCPeerConnection !== \"undefined\" && typeof navigator.mediaDevices !== \"undefined\" && typeof navigator.mediaDevices.getUserMedia !== \"undefined\";")
	public static native boolean isSupported();

	@JSBody(params = { "item" }, script = "return item.streams[0];")
	static native MediaStream getFirstStream(JSObject item);

	@JSBody(params = { "aud", "stream" }, script = "return aud.srcObject = stream;")
	static native void setSrcObject(HTMLAudioElement aud, MediaStream stream);

	@JSBody(params = { "aud" }, script = "return aud.remove();")
	static native void removeAud(HTMLAudioElement aud);

	@JSBody(params = { "pc", "stream" }, script = "return stream.getTracks().forEach((track) => { pc.addTrack(track, stream); });")
	static native void addStream(JSObject pc, MediaStream stream);

	@JSBody(params = { "rawStream", "muted" }, script = "return rawStream.getAudioTracks()[0].enabled = !muted;")
	static native void mute(MediaStream rawStream, boolean muted);

	@JSBody(params = { "peerConnection", "str" }, script = "return peerConnection.addIceCandidate(new RTCIceCandidate(JSON.parse(str)));")
	static native void addIceCandidate(JSObject peerConnection, String str);

	public static void disconnect(JSObject peerConnection) {
		PlatformWebRTC.closeIt(peerConnection);
	}

	public static void setVoiceProximity(int prox) {
		for (PannerNode panner : voicePanners.values()) {
			panner.setMaxDistance(VoiceClientController.getVoiceListenVolume() * 2 * prox + 0.1f);
		}
	}

	public static void updateVoicePosition(EaglercraftUUID uuid, double x, double y, double z) {
		if (voicePanners.containsKey(uuid)) voicePanners.get(uuid).setPosition((float) x, (float) y, (float) z);
	}

	public static class VoicePeer {
		public final EaglercraftUUID peerId;
		public final JSObject peerConnection;
		public MediaStream rawStream;
		public VoicePeer(EaglercraftUUID peerId, JSObject peerConnection, boolean offer) {
			this.peerId = peerId;
			this.peerConnection = peerConnection;

			TeaVMUtils.addEventListener(peerConnection, "icecandidate", (EventListener<Event>) evt -> {
				if (PlatformWebRTC.hasCandidate(evt)) {
					Map<String, String> m = new HashMap<>();
					m.put("sdpMLineIndex", "" + PlatformWebRTC.getSdpMLineIndex(evt));
					m.put("candidate", PlatformWebRTC.getCandidate(evt));
					handleIceCandidate(peerId, JSONWriter.valueToString(m));
				}
			});
			TeaVMUtils.addEventListener(peerConnection, "track", (EventListener<Event>) evt -> {
				rawStream = getFirstStream(evt);
				HTMLAudioElement aud = (HTMLAudioElement) HTMLDocument.current().createElement("audio");
				aud.setAutoplay(true);
				aud.setMuted(true);
				TeaVMUtils.addEventListener(aud, "ended", (EventListener<Event>) evt2 -> {
					removeAud(aud);
				});
				setSrcObject(aud, rawStream);
				handlePeerTrack(peerId, rawStream);
			});

			addStream(peerConnection, localMediaStream.getStream());
			if (offer) {
				PlatformWebRTC.createOffer(peerConnection, desc -> {
					PlatformWebRTC.setLocalDescription(peerConnection, desc, () -> {
						handleDescription(peerId, JSON.stringify(desc));
					}, err -> {
						logger.error("Failed to set local description for \"{}\"! {}", peerId, err);
						if (peerStateInitial == EnumVoiceChannelPeerState.LOADING) {
							peerStateInitial = EnumVoiceChannelPeerState.FAILED;
						}
						signalDisconnect(peerId, false);
					});
				}, err -> {
					logger.error("Failed to set create offer for \"{}\"! {}", peerId, err);
					if (peerStateInitial == EnumVoiceChannelPeerState.LOADING) {
						peerStateInitial = EnumVoiceChannelPeerState.FAILED;
					}
					signalDisconnect(peerId, false);
				});
			}

			TeaVMUtils.addEventListener(peerConnection, "connectionstatechange", (EventListener<Event>) evt -> {
				String cs = PlatformWebRTC.getConnectionState(peerConnection);
				if ("disconnected".equals(cs)) {
					signalDisconnect(peerId, false);
				} else if ("connected".equals(cs)) {
					if (peerState != EnumVoiceChannelPeerState.SUCCESS) {
						peerState = EnumVoiceChannelPeerState.SUCCESS;
					}
				} else if ("failed".equals(cs)) {
					if (peerState == EnumVoiceChannelPeerState.LOADING) {
						peerState = EnumVoiceChannelPeerState.FAILED;
					}
					signalDisconnect(peerId, false);
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
				PlatformWebRTC.setRemoteDescription2(peerConnection, descJSON, () -> {
					if (remoteDesc.has("type") && "offer".equals(remoteDesc.getString("type"))) {
						PlatformWebRTC.createAnswer(peerConnection, desc -> {
							PlatformWebRTC.setLocalDescription(peerConnection, desc, () -> {
								handleDescription(peerId, JSON.stringify(desc));
								if (peerStateDesc != EnumVoiceChannelPeerState.SUCCESS) peerStateDesc = EnumVoiceChannelPeerState.SUCCESS;
							}, err -> {
								logger.error("Failed to set local description for \"{}\"! {}", peerId, err.getMessage());
								if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
								signalDisconnect(peerId, false);
							});
						}, err -> {
							logger.error("Failed to create answer for \"{}\"! {}", peerId, err.getMessage());
							if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
							signalDisconnect(peerId, false);
						});
					}
				}, err -> {
					logger.error("Failed to set remote description for \"{}\"! {}", peerId, err.getMessage());
					if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
					signalDisconnect(peerId, false);
				});
			} catch (Throwable err) {
				logger.error("Failed to parse remote description for \"{}\"! {}", peerId, err.getMessage());
				if (peerStateDesc == EnumVoiceChannelPeerState.LOADING) peerStateDesc = EnumVoiceChannelPeerState.FAILED;
				signalDisconnect(peerId, false);
			}
		}

		public void addICECandidate(String candidate) {
			try {
				addIceCandidate(peerConnection, candidate);
				if (peerStateIce != EnumVoiceChannelPeerState.SUCCESS) peerStateIce = EnumVoiceChannelPeerState.SUCCESS;
			} catch (Throwable err) {
				logger.error("Failed to parse ice candidate for \"{}\"! {}", peerId, err.getMessage());
				if (peerStateIce == EnumVoiceChannelPeerState.LOADING) peerStateIce = EnumVoiceChannelPeerState.FAILED;
				signalDisconnect(peerId, false);
			}
		}
	}

	public static Set<Map<String, String>> iceServers = new HashSet<>();
	public static boolean hasInit = false;
	public static Map<EaglercraftUUID, VoicePeer> peerList = new HashMap<>();
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
			localRawMediaStream = PlatformRuntime.getMic();
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
		for (EaglercraftUUID uuid : voiceAnalysers.keySet()) {
			AnalyserNode analyser = voiceAnalysers.get(uuid);
			Uint8Array array = Uint8Array.create(analyser.getFrequencyBinCount());
			analyser.getByteFrequencyData(array);
			int len = array.getLength();
			for (int i = 0; i < len; i++) {
				if (array.get(i) >= 0.1f) {
					VoiceClientController.getVoiceSpeaking().add(uuid);
					break;
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
			peerList.remove(peerId, peer);
			try {
				peer.disconnect();
			} catch (Throwable ignored) {}
			handlePeerDisconnect(peerId, quiet);
		}
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

	public static void handleIceCandidate(EaglercraftUUID peerId, String candidate) {
		VoiceClientController.sendPacketICE(peerId, candidate);
	}

	public static void handleDescription(EaglercraftUUID peerId, String desc) {
		VoiceClientController.sendPacketDesc(peerId, desc);
	}

	public static void handlePeerTrack(EaglercraftUUID peerId, MediaStream audioStream) {
		if (VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.NONE) return;
		MediaStreamAudioSourceNode audioNode = PlatformAudio.audioctx.createMediaStreamSource(audioStream);
		AnalyserNode analyser = PlatformAudio.audioctx.createAnalyser();
		analyser.setSmoothingTimeConstant(0f);
		analyser.setFftSize(32);
		audioNode.connect(analyser);
		voiceAnalysers.put(peerId, analyser);
		if (VoiceClientController.getVoiceChannel() == EnumVoiceChannelType.GLOBAL) {
			GainNode gain = PlatformAudio.audioctx.createGain();
			gain.getGain().setValue(VoiceClientController.getVoiceListenVolume());
			analyser.connect(gain);
			gain.connect(PlatformAudio.audioctx.getDestination());
			gain.connect(PlatformAudio.recDest);
			voiceGains.put(peerId, gain);
			VoiceClientController.getVoiceListening().add(peerId);
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
			analyser.connect(gain);
			gain.connect(panner);
			panner.connect(PlatformAudio.audioctx.getDestination());
			panner.connect(PlatformAudio.recDest);
			voiceGains.put(peerId, gain);
			VoiceClientController.getVoiceListening().add(peerId);
			voicePanners.put(peerId, panner);
		}
		if (VoiceClientController.getVoiceMuted().contains(peerId)) mutePeer(peerId, true);
	}

	public static void handlePeerDisconnect(EaglercraftUUID peerId, boolean quiet) {
		if (voiceAnalysers.containsKey(peerId)) {
			voiceAnalysers.get(peerId).disconnect();
			voiceAnalysers.remove(peerId);
		}
		if (voiceGains.containsKey(peerId)) {
			voiceGains.get(peerId).disconnect();
			voiceGains.remove(peerId);
			VoiceClientController.getVoiceListening().remove(peerId);
		}
		if (voicePanners.containsKey(peerId)) {
			voicePanners.get(peerId).disconnect();
			voicePanners.remove(peerId);
		}
		if (!quiet) {
			VoiceClientController.sendPacketDisconnect(peerId);
		}
	}

	public static void setVoiceListenVolume(float f) {
		for (EaglercraftUUID uuid : voiceGains.keySet()) {
			GainNode gain = voiceGains.get(uuid);
			float val = f;
			if(val > 0.5f) val = 0.5f + (val - 0.5f) * 3.0f;
			if(val > 2.0f) val = 2.0f;
			if(val < 0.0f) val = 0.0f;
			gain.getGain().setValue(val * 2.0f);
			if (voicePanners.containsKey(uuid)) voicePanners.get(uuid).setMaxDistance(f * 2 * VoiceClientController.getVoiceProximity() + 0.1f);
		}
	}
}

package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioBufferSourceNode;
import org.teavm.jso.webaudio.AudioContext;
import org.teavm.jso.webaudio.AudioListener;
import org.teavm.jso.webaudio.DecodeErrorCallback;
import org.teavm.jso.webaudio.DecodeSuccessCallback;
import org.teavm.jso.webaudio.GainNode;
import org.teavm.jso.webaudio.MediaEvent;
import org.teavm.jso.webaudio.MediaStream;
import org.teavm.jso.webaudio.MediaStreamAudioDestinationNode;
import org.teavm.jso.webaudio.PannerNode;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.util.MathHelper;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class PlatformAudio {
	
	static final Logger logger = LogManager.getLogger("BrowserAudio");
	
	private static AudioContext audioctx = null;
	private static MediaStreamAudioDestinationNode recDest = null;
	private static final Map<String, BrowserAudioResource> soundCache = new HashMap();
	
	private static long cacheFreeTimer = 0l;
	
	protected static class BrowserAudioResource implements IAudioResource {
		
		protected AudioBuffer buffer;
		protected long cacheHit = 0l;
		
		protected BrowserAudioResource(AudioBuffer buffer) {
			this.buffer = buffer;
		}
		
	}
	
	protected static class BrowserAudioHandle implements IAudioHandle, EventListener<MediaEvent> {
		
		protected final BrowserAudioResource resource;
		protected AudioBufferSourceNode source;
		protected final PannerNode panner;
		protected final GainNode gain;
		protected float pitch;
		protected boolean isPaused = false;
		protected boolean isEnded = false;
		
		public BrowserAudioHandle(BrowserAudioResource resource, AudioBufferSourceNode source, PannerNode panner,
				GainNode gain, float pitch) {
			this.resource = resource;
			this.source = source;
			this.panner = panner;
			this.gain = gain;
			this.pitch = pitch;
			source.setOnEnded(this);
		}

		@Override
		public void pause(boolean setPaused) {
			if(setPaused) {
				if(!isPaused) {
					isPaused = true;
					source.getPlaybackRate().setValue(0.0f);
				}
			}else {
				if(isPaused) {
					isPaused = false;
					source.getPlaybackRate().setValue(pitch);
				}
			}
		}

		@Override
		public void restart() {
			if(isEnded) {
				isEnded = false;
				AudioBufferSourceNode src = audioctx.createBufferSource();
				resource.cacheHit = System.currentTimeMillis();
				src.setBuffer(resource.buffer);
				src.getPlaybackRate().setValue(pitch);
				source.disconnect();
				src.connect(panner == null ? gain : panner);
				source = src;
				source.start();
			}else {
				source.getPlaybackRate().setValue(pitch);
				source.start(0.0);
			}
		}

		@Override
		public void move(float x, float y, float z) {
			if(panner != null) {
				panner.setPosition(x, y, z);
			}
		}

		@Override
		public void pitch(float f) {
			pitch = f;
			if(!isPaused) {
				source.getPlaybackRate().setValue(pitch);
			}
		}

		@Override
		public void gain(float f) {
			if(panner != null) {
				float v1 = f * 16.0f;
				if(v1 < 16.0f) v1 = 16.0f;
				panner.setMaxDistance(v1);
			}
			float v2 = f;
			if(v2 > 1.0f) v2 = 1.0f;
			gain.getGain().setValue(v2);
		}

		@Override
		public void end() {
			if(!isEnded) {
				isEnded = true;
				source.stop();
			}
		}

		@Override
		public boolean shouldFree() {
			return isEnded;
		}

		@Override
		public void handleEvent(MediaEvent evt) {
			isEnded = true;
		}
		
	}
	
	static void initialize() {
		
		try {
			audioctx = AudioContext.create();
			recDest = audioctx.createMediaStreamDestination();
		}catch(Throwable t) {
			throw new PlatformRuntime.RuntimeInitializationFailureException("Could not initialize audio context!", t);
		}
		
		PlatformInput.clearEvenBuffers();
		
	}

	private static GainNode micGain;

	public static void setMicVol(float vol) {
		if (micGain == null) return;
		micGain.getGain().setValue(vol);
	}

	protected static void initRecDest() {
		AudioBufferSourceNode absn = audioctx.createBufferSource();
		AudioBuffer ab = audioctx.createBuffer(1, 1, 48000);
		ab.copyToChannel(new float[] { 0 }, 0);
		absn.setBuffer(ab);
		absn.setLoop(true);
		absn.start();
		absn.connect(recDest);
		MediaStream mic = PlatformRuntime.getMic();
		if (mic != null) {
			micGain = audioctx.createGain();
			micGain.getGain().setValue(Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.VOICE));
			audioctx.createMediaStreamSource(mic).connect(micGain);
			micGain.connect(recDest);
		}
	}

	protected static MediaStream getRecStream() {
		return recDest.getStream();
	}

	public static IAudioResource loadAudioData(String filename, boolean holdInCache) {
		BrowserAudioResource buffer;
		synchronized(soundCache) {
			buffer = soundCache.get(filename);
		}
		if(buffer == null) {
			byte[] file = PlatformAssets.getResourceBytes(filename);
			if(file == null) return null;
			Uint8Array buf = Uint8Array.create(file.length);
			buf.set(file);
			buffer = new BrowserAudioResource(decodeAudioAsync(buf.getBuffer(), filename));
			if(holdInCache) {
				synchronized(soundCache) {
					soundCache.put(filename, buffer);
				}
			}
		}
		if(buffer.buffer != null) {
			buffer.cacheHit = System.currentTimeMillis();
			return buffer;
		}else {
			return null;
		}
	}

	public static interface IAudioCacheLoader {
		byte[] loadFile(String filename);
	}

	public static IAudioResource loadAudioDataNew(String filename, boolean holdInCache, IAudioCacheLoader loader) {
		BrowserAudioResource buffer;
		synchronized(soundCache) {
			buffer = soundCache.get(filename);
		}
		if(buffer == null) {
			byte[] file = loader.loadFile(filename);
			if(file == null) return null;
			Uint8Array buf = Uint8Array.create(file.length);
			buf.set(file);
			buffer = new BrowserAudioResource(decodeAudioAsync(buf.getBuffer(), filename));
			if(holdInCache) {
				synchronized(soundCache) {
					soundCache.put(filename, buffer);
				}
			}
		}
		if(buffer.buffer != null) {
			buffer.cacheHit = System.currentTimeMillis();
			return buffer;
		}else {
			return null;
		}
	}
	
	@Async
	public static native AudioBuffer decodeAudioAsync(ArrayBuffer buffer, String errorFileName);
	
	private static void decodeAudioAsync(ArrayBuffer buffer, final String errorFileName, final AsyncCallback<AudioBuffer> cb) {
		audioctx.decodeAudioData(buffer, new DecodeSuccessCallback() {
			@Override
			public void onSuccess(AudioBuffer decodedData) {
				cb.complete(decodedData);
			}
		}, new DecodeErrorCallback() {
			@Override
			public void onError(JSObject error) {
				logger.error("Could not load audio: {}", errorFileName);
				cb.complete(null);
			}
		});
	}

	public static void clearAudioCache() {
		long millis = System.currentTimeMillis();
		if(millis - cacheFreeTimer > 30000l) {
			cacheFreeTimer = millis;
			synchronized(soundCache) {
				Iterator<BrowserAudioResource> itr = soundCache.values().iterator();
				while(itr.hasNext()) {
					if(millis - itr.next().cacheHit > 600000l) { // 10 minutes
						itr.remove();
					}
				}
			}
		}
	}

	public static void flushAudioCache() {
		synchronized(soundCache) {
			soundCache.clear();
		}
	}
	
	public static boolean available() {
		return true; // this is not used
	}
	
	public static IAudioHandle beginPlayback(IAudioResource track, float x, float y, float z,
			float volume, float pitch) {
		BrowserAudioResource internalTrack = (BrowserAudioResource) track;
		internalTrack.cacheHit = System.currentTimeMillis();
		
		AudioBufferSourceNode src = audioctx.createBufferSource();
		src.setBuffer(internalTrack.buffer);
		src.getPlaybackRate().setValue(pitch);
		
		PannerNode panner = audioctx.createPanner();
		panner.setPosition(x, y, z);
		float v1 = volume * 16.0f;
		if(v1 < 16.0f) v1 = 16.0f;
		panner.setMaxDistance(v1);
		panner.setRolloffFactor(1.0f);
		panner.setDistanceModel("linear");
		panner.setPanningModel("HRTF");
		panner.setConeInnerAngle(360.0f);
		panner.setConeOuterAngle(0.0f);
		panner.setConeOuterGain(0.0f);
		panner.setOrientation(0.0f, 1.0f, 0.0f);
		
		GainNode gain = audioctx.createGain();
		float v2 = volume;
		if(v2 > 1.0f) v2 = 1.0f;
		gain.getGain().setValue(v2);
		
		src.connect(panner);
		panner.connect(gain);
		gain.connect(audioctx.getDestination());
		gain.connect(recDest);

		src.start();
		
		return new BrowserAudioHandle(internalTrack, src, panner, gain, pitch);
	}

	public static IAudioHandle beginPlaybackStatic(IAudioResource track, float volume, float pitch) {
		BrowserAudioResource internalTrack = (BrowserAudioResource) track;
		internalTrack.cacheHit = System.currentTimeMillis();
		
		AudioBufferSourceNode src = audioctx.createBufferSource();
		src.setBuffer(internalTrack.buffer);
		src.getPlaybackRate().setValue(pitch);
		
		GainNode gain = audioctx.createGain();
		float v2 = volume;
		if(v2 > 1.0f) v2 = 1.0f;
		gain.getGain().setValue(v2);
		
		src.connect(gain);
		gain.connect(audioctx.getDestination());
		gain.connect(recDest);
		
		src.start();
		
		return new BrowserAudioHandle(internalTrack, src, null, gain, pitch);
	}

	public static void setListener(float x, float y, float z, float pitchDegrees, float yawDegrees) {
		float var2 = MathHelper.cos(-yawDegrees * 0.017453292F);
		float var3 = MathHelper.sin(-yawDegrees * 0.017453292F);
		float var4 = -MathHelper.cos(pitchDegrees * 0.017453292F);
		float var5 = MathHelper.sin(pitchDegrees * 0.017453292F);
		AudioListener l = audioctx.getListener();
		l.setPosition(x, y, z);
		l.setOrientation(-var3 * var4, -var5, -var2 * var4, 0.0f, 1.0f, 0.0f);
	}
	
}

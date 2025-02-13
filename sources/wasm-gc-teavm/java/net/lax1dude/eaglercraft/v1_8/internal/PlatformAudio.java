/*
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

package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioBufferSourceNode;
import org.teavm.jso.webaudio.AudioContext;
import org.teavm.jso.webaudio.AudioListener;
import org.teavm.jso.webaudio.GainNode;
import org.teavm.jso.webaudio.MediaStream;
import org.teavm.jso.webaudio.MediaStreamAudioDestinationNode;
import org.teavm.jso.webaudio.PannerNode;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.MemoryStack;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.JOrbisAudioBufferDecoder;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WASMGCClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.util.MathHelper;

public class PlatformAudio {

	static final Logger logger = LogManager.getLogger("BrowserAudio");

	static AudioContext audioctx = null;
	static MediaStreamAudioDestinationNode recDestNode = null;
	static MediaStream recDestMediaStream = null;
	static AudioBuffer silence = null;
	static AudioBufferSourceNode recDestSilenceNode = null;
	static GainNode micRecGain = null;
	static GainNode gameRecGain = null;
	private static final Map<String, BrowserAudioResource> soundCache = new HashMap<>();
	private static final List<BrowserAudioHandle> activeSounds = new LinkedList<>();

	private static long cacheFreeTimer = 0l;
	private static long activeFreeTimer = 0l;
	private static boolean oggSupport = false;

	static void initialize() {
		oggSupport = false;
		
		audioctx = getContext();
		if(audioctx == null) {
			logger.error("Could not initialize audio context!");
			return;
		}
		
		if(((WASMGCClientConfigAdapter)PlatformRuntime.getClientConfigAdapter()).isUseJOrbisAudioDecoderTeaVM()) {
			logger.info("Note: Using embedded JOrbis OGG decoder");
		}else {
			byte[] fileData = EagRuntime.getRequiredResourceBytes("/assets/eagler/audioctx_test_ogg.dat");
			MemoryStack.push();
			try {
				AudioBuffer audioBuffer = decodeAudioBrowserAsync(WASMGCDirectArrayConverter.byteArrayToStackU8Array(fileData),
						BetterJSStringConverter.stringToJS("audioctx_test_ogg.dat"));
				if(audioBuffer != null && audioBuffer.getLength() > 0) {
					oggSupport = true;
				}
			}catch(Throwable t) {
			}finally {
				MemoryStack.pop();
			}
			if(!oggSupport) {
				logger.error("OGG file support detected as false! Using embedded JOrbis OGG decoder");
			}
		}
	}

	@Import(module = "platformAudio", name = "getContext")
	private static native AudioContext getContext();

	protected static class BrowserAudioResource implements IAudioResource {
		
		protected AudioBuffer buffer;
		protected long cacheHit = 0l;
		
		protected BrowserAudioResource(AudioBuffer buffer) {
			this.buffer = buffer;
		}
		
	}

	protected interface JSBrowserAudioHandleCB extends JSObject {

		@JSProperty
		boolean getIsEnded();

		@JSProperty
		void setIsEnded(boolean b);

	}

	@JSBody(script = "return { isEnded: false };")
	protected static native JSBrowserAudioHandleCB createHandleCBInstance();

	protected static class BrowserAudioHandle implements IAudioHandle {

		protected final BrowserAudioResource resource;
		protected AudioBufferSourceNode source;
		protected final PannerNode panner;
		protected final GainNode gain;
		protected float pitch;
		protected boolean repeat;
		protected boolean isPaused = false;
		protected boolean isDisposed = false;
		protected JSBrowserAudioHandleCB isEnded;

		protected BrowserAudioHandle(BrowserAudioResource resource, AudioBufferSourceNode source, PannerNode panner,
				GainNode gain, float pitch, boolean repeat) {
			this.resource = resource;
			this.source = source;
			this.panner = panner;
			this.gain = gain;
			this.pitch = pitch;
			this.repeat = repeat;
			this.isEnded = createHandleCBInstance();
			registerIsEndedHandler(source, isEnded);
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
		public void repeat(boolean en) {
			repeat = en;
			if(!isEnded.getIsEnded()) {
				source.setLoop(en);
			}
		}

		@Override
		public void restart() {
			if(isEnded.getIsEnded()) {
				isEnded.setIsEnded(false);
				registerIsEndedHandler(source, isEnded);
				isPaused = false;
				AudioBufferSourceNode src = audioctx.createBufferSource();
				resource.cacheHit = PlatformRuntime.steadyTimeMillis();
				src.setBuffer(resource.buffer);
				src.getPlaybackRate().setValue(pitch);
				source.disconnect();
				src.connect(panner == null ? gain : panner);
				if(isDisposed) {
					isDisposed = false;
					activeSounds.add(this);
					gain.connect(audioctx.getDestination());
					if(gameRecGain != null) {
						gain.connect(gameRecGain);
					}
				}
				source = src;
				source.start();
			}else {
				isPaused = false;
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
			if(!isEnded.getIsEnded()) {
				isEnded.setIsEnded(true);
				releaseIsEndedHandler(source, isEnded);
				source.stop();
			}
		}

		@Override
		public boolean shouldFree() {
			return isEnded.getIsEnded();
		}

		private void dispose() {
			if(!isDisposed) {
				isDisposed = true;
				gain.disconnect();
			}
		}
	}

	@Import(module = "platformAudio", name = "registerIsEndedHandler")
	protected static native void registerIsEndedHandler(AudioBufferSourceNode source, JSBrowserAudioHandleCB isEnded);

	@Import(module = "platformAudio", name = "releaseIsEndedHandler")
	protected static native void releaseIsEndedHandler(AudioBufferSourceNode source, JSBrowserAudioHandleCB isEnded);

	public static IAudioResource loadAudioData(String filename, boolean holdInCache) {
		BrowserAudioResource buffer = soundCache.get(filename);
		if(buffer == null) {
			byte[] file = PlatformAssets.getResourceBytes(filename);
			if(file == null) return null;
			buffer = new BrowserAudioResource(decodeAudioData(file, filename));
			if(holdInCache) {
				soundCache.put(filename, buffer);
			}
		}
		if(buffer.buffer != null) {
			buffer.cacheHit = PlatformRuntime.steadyTimeMillis();
			return buffer;
		}else {
			return null;
		}
	}

	public static IAudioResource loadAudioDataNew(String filename, boolean holdInCache, IAudioCacheLoader loader) {
		BrowserAudioResource buffer = soundCache.get(filename);
		if(buffer == null) {
			byte[] file = loader.loadFile(filename);
			if(file == null) return null;
			buffer = new BrowserAudioResource(decodeAudioData(file, filename));
			if(holdInCache) {
				soundCache.put(filename, buffer);
			}
		}
		if(buffer.buffer != null) {
			buffer.cacheHit = PlatformRuntime.steadyTimeMillis();
			return buffer;
		}else {
			return null;
		}
	}

	private static AudioBuffer decodeAudioData(byte[] data, String errorFileName) {
		if(data == null) {
			return null;
		}
		if(oggSupport || !(data.length > 4 && data[0] == (byte) 0x4F && data[1] == (byte) 0x67 && data[2] == (byte) 0x67
					&& data[3] == (byte) 0x53)) {
			MemoryStack.push();
			try {
				return decodeAudioBrowserAsync(WASMGCDirectArrayConverter.byteArrayToStackU8Array(data),
						BetterJSStringConverter.stringToJS(errorFileName));
			}finally {
				MemoryStack.pop();
			}
		}else {
			return JOrbisAudioBufferDecoder.decodeAudioJOrbis(data, errorFileName);
		}
	}

	@Import(module = "platformAudio", name = "decodeAudioBrowser")
	public static native AudioBuffer decodeAudioBrowserAsync(Uint8Array array, JSString errorFileName);

	public static AudioBuffer decodeAudioBufferPCMBrowser(Float32Array array, int ch, int len, int rate) {
		AudioBuffer buffer = audioctx.createBuffer(ch, len, rate);
		for(int i = 0; i < ch; ++i) {
			buffer.copyToChannel(new Float32Array(array.getBuffer(), array.getByteOffset() + ((i * len) << 2), len), i);
		}
		return buffer;
	}

	public static void clearAudioCache() {
		long millis = PlatformRuntime.steadyTimeMillis();
		if(millis - cacheFreeTimer > 30000l) {
			cacheFreeTimer = millis;
			Iterator<BrowserAudioResource> itr = soundCache.values().iterator();
			while(itr.hasNext()) {
				if(millis - itr.next().cacheHit > 600000l) { // 10 minutes
					itr.remove();
				}
			}
		}
		if(millis - activeFreeTimer > 700l) {
			activeFreeTimer = millis;
			Iterator<BrowserAudioHandle> itr = activeSounds.iterator();
			while(itr.hasNext()) {
				BrowserAudioHandle h = itr.next();
				if(h.shouldFree()) {
					itr.remove();
					h.dispose();
				}
			}
		}
	}

	public static void flushAudioCache() {
		soundCache.clear();
		Iterator<BrowserAudioHandle> itr = activeSounds.iterator();
		while(itr.hasNext()) {
			itr.next().dispose();
		}
		activeSounds.clear();
	}

	public static boolean available() {
		return audioctx != null;
	}

	@JSBody(params = { "node" }, script = "node.distanceModel = \"linear\";")
	static native void setDistanceModelLinearFast(PannerNode node) ;

	@JSBody(params = { "node" }, script = "node.panningModel = \"HRTF\";")
	static native void setPanningModelHRTFFast(PannerNode node) ;

	public static IAudioHandle beginPlayback(IAudioResource track, float x, float y, float z,
			float volume, float pitch, boolean repeat) {
		BrowserAudioResource internalTrack = (BrowserAudioResource) track;
		internalTrack.cacheHit = PlatformRuntime.steadyTimeMillis();
		
		AudioBufferSourceNode src = audioctx.createBufferSource();
		src.setBuffer(internalTrack.buffer);
		src.getPlaybackRate().setValue(pitch);
		src.setLoop(repeat);
		
		PannerNode panner = audioctx.createPanner();
		panner.setPosition(x, y, z);
		float v1 = volume * 16.0f;
		if(v1 < 16.0f) v1 = 16.0f;
		panner.setMaxDistance(v1);
		panner.setRolloffFactor(1.0f);
		setDistanceModelLinearFast(panner);
		setPanningModelHRTFFast(panner);
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
		if(gameRecGain != null) {
			gain.connect(gameRecGain);
		}

		src.start();
		
		BrowserAudioHandle ret = new BrowserAudioHandle(internalTrack, src, panner, gain, pitch, repeat);
		activeSounds.add(ret);
		return ret;
	}

	public static IAudioHandle beginPlaybackStatic(IAudioResource track, float volume, float pitch, boolean repeat) {
		BrowserAudioResource internalTrack = (BrowserAudioResource) track;
		internalTrack.cacheHit = PlatformRuntime.steadyTimeMillis();
		
		AudioBufferSourceNode src = audioctx.createBufferSource();
		src.setBuffer(internalTrack.buffer);
		src.getPlaybackRate().setValue(pitch);
		src.setLoop(repeat);
		
		GainNode gain = audioctx.createGain();
		float v2 = volume;
		if(v2 > 1.0f) v2 = 1.0f;
		gain.getGain().setValue(v2);
		
		src.connect(gain);
		gain.connect(audioctx.getDestination());
		if(gameRecGain != null) {
			gain.connect(gameRecGain);
		}
		
		src.start();
		
		BrowserAudioHandle ret = new BrowserAudioHandle(internalTrack, src, null, gain, pitch, repeat);
		activeSounds.add(ret);
		return ret;
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

	static MediaStream initRecordingStream(float gameVol, float micVol) {
		if(recDestMediaStream != null) {
			return recDestMediaStream;
		}
		try {
			if(silence == null) {
				silence = audioctx.createBuffer(1, 1, 48000);
				silence.copyToChannel(new Float32Array(1), 0);
			}
			recDestNode = audioctx.createMediaStreamDestination();
			recDestSilenceNode = audioctx.createBufferSource();
			recDestSilenceNode.setBuffer(silence);
			recDestSilenceNode.setLoop(true);
			recDestSilenceNode.start();
			recDestSilenceNode.connect(recDestNode);
			if(micVol > 0.0f) {
				MediaStream mic = PlatformScreenRecord.getMic();
				if (mic != null) {
					micRecGain = audioctx.createGain();
					micRecGain.getGain().setValue(micVol);
					audioctx.createMediaStreamSource(mic).connect(micRecGain);
					micRecGain.connect(recDestNode);
				}
			}
			gameRecGain = audioctx.createGain();
			gameRecGain.getGain().setValue(gameVol);
			for(BrowserAudioHandle handle : activeSounds) {
				if(handle.panner != null) {
					handle.panner.connect(gameRecGain);
				}else {
					handle.gain.connect(gameRecGain);
				}
			}
			PlatformVoiceClient.addRecordingDest(gameRecGain);
			gameRecGain.connect(recDestNode);
			recDestMediaStream = recDestNode.getStream();
			return recDestMediaStream;
		}catch(Throwable t) {
			destroyRecordingStream();
			throw t;
		}
	}

	static void destroyRecordingStream() {
		if(recDestSilenceNode != null) {
			try {
				recDestSilenceNode.disconnect();
			}catch(Throwable t) {
			}
			recDestSilenceNode = null;
		}
		if(micRecGain != null) {
			try {
				micRecGain.disconnect();
			}catch(Throwable t) {
			}
			micRecGain = null;
		}
		if(gameRecGain != null) {
			try {
				gameRecGain.disconnect();
			}catch(Throwable t) {
			}
			for(BrowserAudioHandle handle : activeSounds) {
				try {
					handle.gain.disconnect(gameRecGain);
				}catch(Throwable t) {
				}
			}
			PlatformVoiceClient.removeRecordingDest(gameRecGain);
			gameRecGain = null;
		}
		recDestNode = null;
		recDestMediaStream = null;
	}

}
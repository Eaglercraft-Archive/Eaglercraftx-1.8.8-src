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

import java.util.EnumSet;
import java.util.Set;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.webaudio.MediaStream;

import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.recording.EnumScreenRecordingCodec;

public class PlatformScreenRecord {

	static final Logger logger = LogManager.getLogger("PlatformScreenRecord");

	static Window win;
	static HTMLCanvasElement canvas;
	static boolean support;
	static final Set<EnumScreenRecordingCodec> supportedCodecs = EnumSet.noneOf(EnumScreenRecordingCodec.class);
	static float currentGameVolume = 1.0f;
	static float currentMicVolume = 0.0f;
	static MediaStream recStream = null;
	static HTMLCanvasElement downscaleCanvas = null;
	static CanvasRenderingContext2D downscaleCanvasCtx = null;
	static long lastDownscaleFrameCaptured = 0l;
	static boolean currentMicLock = false;
	static JSObject mediaRec = null;
	static ScreenRecordParameters currentParameters = null;

	static void initContext(Window theWin, HTMLCanvasElement theCanvas) {
		win = theWin;
		canvas = theCanvas;
		supportedCodecs.clear();
		support = hasMediaRecorder(theWin, theCanvas);
		if(support) {
			logger.info("MediaRecorder is supported, checking codecs...");
			EnumScreenRecordingCodec[] allCodecs = EnumScreenRecordingCodec.values();
			for(int i = 0; i < allCodecs.length; ++i) {
				if(checkCodecSupported(theWin, allCodecs[i].mimeType)) {
					supportedCodecs.add(allCodecs[i]);
				}
			}
			if(!supportedCodecs.isEmpty()) {
				logger.info("Found {} codecs that are probably supported!", supportedCodecs.size());
			}else {
				logger.error("No supported codecs found!");
				support = false;
			}
		}
	}

	@Import(module = "platformScreenRecord", name = "getMic")
	public static native MediaStream getMic();

	@JSBody(params = { "win", "canvas" }, script = "return (typeof win.MediaRecorder !== \"undefined\") && (typeof win.MediaRecorder.isTypeSupported === \"function\") && (typeof canvas.captureStream === \"function\");")
	private static native boolean hasMediaRecorder(Window win, HTMLCanvasElement canvas);

	@JSBody(params = { "win", "mime" }, script = "return win.MediaRecorder.isTypeSupported(mime);")
	private static native boolean checkCodecSupported(Window win, String mime);

	public static boolean isSupported() {
		return support;
	}

	public static boolean isCodecSupported(EnumScreenRecordingCodec codec) {
		return supportedCodecs.contains(codec);
	}

	static void captureFrameHook() {
		if(mediaRec != null && currentParameters != null && currentParameters.resolutionDivisior > 1 && downscaleCanvas != null && downscaleCanvasCtx != null) {
			if(currentParameters.captureFrameRate > 0) {
				long curTime = PlatformRuntime.steadyTimeMillis();
				if(curTime - lastDownscaleFrameCaptured < (long)(1000 / currentParameters.captureFrameRate)) {
					return;
				}
				lastDownscaleFrameCaptured = curTime;
			}
			int oldWidth = downscaleCanvas.getWidth();
			int oldHeight = downscaleCanvas.getHeight();
			float divisor = (float)Math.sqrt(1.0 / Math.pow(2.0, currentParameters.resolutionDivisior - 1));
			int newWidth = (int)(PlatformInput.getWindowWidth() * divisor);
			int newHeight = (int)(PlatformInput.getWindowHeight() * divisor);
			if(oldWidth != newWidth || oldHeight != newHeight) {
				downscaleCanvas.setWidth(newWidth);
				downscaleCanvas.setHeight(newHeight);
			}
			downscaleCanvasCtx.drawImage(canvas, 0, 0, newWidth, newHeight);
		}
	}

	public static void setGameVolume(float volume) {
		currentGameVolume = volume;
		if(PlatformAudio.gameRecGain != null) {
			PlatformAudio.gameRecGain.getGain().setValue(volume);
		}
	}

	public static void setMicrophoneVolume(float volume) {
		currentMicVolume = volume;
		if(PlatformAudio.micRecGain != null) {
			PlatformAudio.micRecGain.getGain().setValue(volume);
		}
	}

	@Import(module = "platformScreenRecord", name = "setDataAvailableHandler")
	private static native void setupDataAvailableHandler(JSObject mediaRec, boolean isWebM, JSString nameStr);

	@JSBody(params = { }, script = "return { alpha: false, desynchronized: true };")
	private static native JSObject youEagler();

	@JSBody(params = { "canvas", "fps", "audio" }, script = "var stream = fps <= 0 ? canvas.captureStream() : canvas.captureStream(fps); stream.addTrack(audio.getTracks()[0]); return stream;")
	private static native MediaStream captureStreamAndAddAudio(HTMLCanvasElement canvas, int fps, MediaStream audio);

	@JSBody(params = { "stream", "codec", "videoBitrate", "audioBitrate" }, script = "var rec = new MediaRecorder(stream, { mimeType: codec, videoBitsPerSecond: videoBitrate, audioBitsPerSecond: audioBitrate }); rec.start(); return rec;")
	private static native JSObject createMediaRecorder(MediaStream stream, String codec, int videoBitrate, int audioBitrate);

	@JSBody(params = { "rec" }, script = "rec.stop();")
	private static native void stopRec(JSObject rec);

	public static void startRecording(ScreenRecordParameters params) {
		if(!support) {
			throw new IllegalStateException("Screen recording is not supported");
		}
		if(isRecording()) {
			throw new IllegalStateException("Already recording!");
		}
		if(params.captureFrameRate <= 0 && (!PlatformInput.vsync || !PlatformInput.vsyncSupport)) {
			throw new IllegalStateException("V-Sync is not enabled, please enable it in \"Video Settings\"");
		}
		if(params.resolutionDivisior > 1) {
			float divisor = (float)Math.sqrt(1.0 / Math.pow(2.0, params.resolutionDivisior - 1));
			int newWidth = (int)(PlatformInput.getWindowWidth() * divisor);
			int newHeight = (int)(PlatformInput.getWindowHeight() * divisor);
			if(downscaleCanvas == null) {
				downscaleCanvas = (HTMLCanvasElement) win.getDocument().createElement("canvas");
				downscaleCanvas.setWidth(newWidth);
				downscaleCanvas.setHeight(newHeight);
				downscaleCanvasCtx = (CanvasRenderingContext2D) downscaleCanvas.getContext("2d", youEagler());
				if(downscaleCanvasCtx == null) {
					downscaleCanvas = null;
					throw new IllegalStateException("Could not create downscaler canvas!");
				}
			}else {
				downscaleCanvas.setWidth(newWidth);
				downscaleCanvas.setHeight(newHeight);
			}
		}
		currentMicLock = currentMicVolume <= 0.0f;
		recStream = captureStreamAndAddAudio(params.resolutionDivisior > 1 ? downscaleCanvas : canvas,
				Math.max(params.captureFrameRate, 0),
				PlatformAudio.initRecordingStream(currentGameVolume, currentMicVolume));
		mediaRec = createMediaRecorder(recStream, params.codec.mimeType, params.videoBitsPerSecond * 1000,
				params.audioBitsPerSecond * 1000);
		currentParameters = params;
		setupDataAvailableHandler(mediaRec, "video/webm".equals(params.codec.container),
				BetterJSStringConverter.stringToJS(EaglercraftVersion.screenRecordingFilePrefix + " - " + EaglerProfile.getName()
						+ " - ${date}." + params.codec.fileExt));
	}

	public static void endRecording() {
		if(mediaRec != null) {
			stopRec(mediaRec);
			mediaRec = null;
			PlatformAudio.destroyRecordingStream();
		}
		currentParameters = null;
	}

	public static boolean isRecording() {
		return mediaRec != null;
	}

	public static boolean isMicVolumeLocked() {
		return mediaRec != null && currentMicLock;
	}

	public static boolean isVSyncLocked() {
		return mediaRec != null && currentParameters != null && currentParameters.captureFrameRate == -1;
	}

}
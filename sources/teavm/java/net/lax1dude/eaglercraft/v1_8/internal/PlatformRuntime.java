package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSExceptions;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.core.JSError;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLAnchorElement;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.webaudio.MediaStream;
import org.teavm.jso.webgl.WebGLFramebuffer;

import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.GZIPOutputStream;
import com.jcraft.jzlib.InflaterInputStream;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerArrayBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EPKLoader;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EarlyLoadScreen;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass.EPKFileEntry;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGL2RenderingContext;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;

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
public class PlatformRuntime {
	
	static final Logger logger = LogManager.getLogger("BrowserRuntime");

	public static Window win = null;
	public static HTMLDocument doc = null;
	public static HTMLElement parent = null;
	public static HTMLCanvasElement canvas = null;
	public static WebGL2RenderingContext webgl = null;
	
	static WebGLFramebuffer mainFramebuffer = null;

	public static void create() {
		win = Window.current();
		doc = win.getDocument();
		
		logger.info("Creating main game canvas");
		
		parent = doc.getElementById(MainClass.configRootElement);
		if(parent == null) {
			throw new RuntimeInitializationFailureException("Root element \"" + MainClass.configRootElement + "\" was not found in this document!");
		}

		CSSStyleDeclaration style = parent.getStyle();
		style.setProperty("overflowX", "hidden");
		style.setProperty("overflowY", "hidden");
		
		canvas = (HTMLCanvasElement) doc.createElement("canvas");
		
		style = canvas.getStyle();
		style.setProperty("width", "100%");
		style.setProperty("height", "100%");
		style.setProperty("image-rendering", "pixelated");
		
		double r = win.getDevicePixelRatio();
		int iw = parent.getClientWidth();
		int ih = parent.getClientHeight();
		int sw = (int)(r * iw);
		int sh = (int)(r * ih);

		canvas.setWidth(sw);
		canvas.setHeight(sh);
		
		parent.appendChild(canvas);
		
		try {
			PlatformInput.initHooks(win, canvas);
		}catch(Throwable t) {
			throw new RuntimeInitializationFailureException("Exception while registering window event handlers", t);
		}
		
		try {
			doc.exitPointerLock();
		}catch(Throwable t) {
			throw new PlatformIncompatibleException("Mouse cursor lock is not available on this device!");
		}

		logger.info("Creating WebGL context");
		
		JSObject webgl_ = canvas.getContext("webgl2", youEagler());
		if(webgl_ == null) {
			throw new PlatformIncompatibleException("WebGL 2.0 is not supported on this device!");
		}
		
		webgl = (WebGL2RenderingContext) webgl_;
		PlatformOpenGL.setCurrentContext(webgl);
		
		mainFramebuffer = webgl.createFramebuffer();
		PlatformInput.initFramebuffer(webgl, mainFramebuffer, sw, sh);
		
		EarlyLoadScreen.paintScreen();
		
		EPKFileEntry[] epkFiles = MainClass.configEPKFiles;
		
		for(int i = 0; i < epkFiles.length; ++i) {
			String url = epkFiles[i].url;
			String logURL = url.startsWith("data:") ? "<data: " + url.length() + " chars>" : url;
			
			logger.info("Downloading: {}", logURL);
			
			ArrayBuffer epkFileData = downloadRemoteURI(url);
			
			if(epkFileData == null) {
				throw new RuntimeInitializationFailureException("Could not download EPK file \"" + url + "\"");
			}
			
			logger.info("Decompressing: {}", logURL);
			
			try {
				EPKLoader.loadEPK(epkFileData, epkFiles[i].path, PlatformAssets.assets);
			}catch(Throwable t) {
				throw new RuntimeInitializationFailureException("Could not extract EPK file \"" + url + "\"", t);
			}
		}

		logger.info("Loaded {} resources from EPKs", PlatformAssets.assets.size());

		byte[] finalLoadScreen = PlatformAssets.getResourceBytes("/assets/eagler/eagtek.png");

		logger.info("Initializing sound engine...");

		PlatformInput.pressAnyKeyScreen();

		PlatformAudio.initialize();

		if(finalLoadScreen != null) {
			EarlyLoadScreen.paintFinal(finalLoadScreen);
		}

		logger.info("Platform initialization complete");
	}
	
	@JSBody(params = { }, script = "return {antialias: false, depth: false, powerPreference: \"high-performance\", desynchronized: true, preserveDrawingBuffer: false, premultipliedAlpha: false, alpha: false};")
	public static native JSObject youEagler();
	
	public static class RuntimeInitializationFailureException extends IllegalStateException {

		public RuntimeInitializationFailureException(String message, Throwable cause) {
			super(message, cause);
		}

		public RuntimeInitializationFailureException(String s) {
			super(s);
		}
		
	}
	
	public static class PlatformIncompatibleException extends IllegalStateException {

		public PlatformIncompatibleException(String s) {
			super(s);
		}
		
	}

	public static void destroy() {
		logger.fatal("Game tried to destroy the context! Browser runtime can't do that");
	}

	public static EnumPlatformType getPlatformType() {
		return EnumPlatformType.JAVASCRIPT;
	}

	public static EnumPlatformAgent getPlatformAgent() {
		return EnumPlatformAgent.getFromUA(getUserAgentString());
	}

	@JSBody(params = { }, script = "return window.navigator.userAgent;")
	public static native String getUserAgentString();

	public static EnumPlatformOS getPlatformOS() {
		return EnumPlatformOS.getFromUA(getUserAgentString());
	}

	public static void requestANGLE(EnumPlatformANGLE plaf) {
	}

	public static EnumPlatformANGLE getPlatformANGLE() {
		return EnumPlatformANGLE.fromGLRendererString(getGLRenderer());
	}

	public static String getGLVersion() {
		return PlatformOpenGL._wglGetString(RealOpenGLEnums.GL_VERSION);
	}

	public static String getGLRenderer() {
		return PlatformOpenGL._wglGetString(RealOpenGLEnums.GL_RENDERER);
	}

	public static ByteBuffer allocateByteBuffer(int length) {
		return EaglerArrayBufferAllocator.allocateByteBuffer(length);
	}

	public static IntBuffer allocateIntBuffer(int length) {
		return EaglerArrayBufferAllocator.allocateIntBuffer(length);
	}

	public static FloatBuffer allocateFloatBuffer(int length) {
		return EaglerArrayBufferAllocator.allocateFloatBuffer(length);
	}
	
	public static void freeByteBuffer(ByteBuffer byteBuffer) {
		
	}
	
	public static void freeIntBuffer(IntBuffer intBuffer) {
		
	}
	
	public static void freeFloatBuffer(FloatBuffer floatBuffer) {
		
	}

	public static void downloadRemoteURI(String assetPackageURI, final Consumer<ArrayBuffer> cb) {
		downloadRemoteURI(assetPackageURI, new AsyncCallback<ArrayBuffer>() {
			@Override
			public void complete(ArrayBuffer result) {
				cb.accept(result);
			}

			@Override
			public void error(Throwable e) {
				e.printStackTrace();
				cb.accept(null);
			}
		});
	}
	
	@Async
	public static native ArrayBuffer downloadRemoteURI(String assetPackageURI);

	private static void downloadRemoteURI(String assetPackageURI, final AsyncCallback<ArrayBuffer> cb) {
		final XMLHttpRequest request = XMLHttpRequest.create();
		request.setResponseType("arraybuffer");
		request.open("GET", assetPackageURI, true);
		
		TeaVMUtils.addEventListener(request, "load", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				int stat = request.getStatus();
				if(stat == 0 || (stat >= 200 && stat < 400)) {
					cb.complete((ArrayBuffer)request.getResponse());
				}else {
					cb.complete(null);
				}
			}
		});
		
		TeaVMUtils.addEventListener(request, "error", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				cb.complete(null);
			}
		});
		
		request.send();
	}
	
	public static boolean isDebugRuntime() {
		return false;
	}
	
	public static void writeCrashReport(String crashDump) {
		MainClass.showCrashScreen(crashDump);
	}

	public static void removeEventHandlers() {
		try {
			PlatformInput.removeEventHandlers();
		}catch(Throwable t) {
		}
	}
	
	public static void getStackTrace(Throwable t, Consumer<String> ret) {
		JSObject o = JSExceptions.getJSException(t);
		if(o != null) {
			try {
				JSError err = o.cast();
				String stack = err.getStack();
				if(stack != null) {
					String[] stackElements = stack.split("[\\r\\n]+");
					if(stackElements.length > 0) {
						for(int i = 0; i < stackElements.length; ++i) {
							String str = stackElements[i].trim();
							if(str.startsWith("at ")) {
								str = str.substring(3).trim();
							}
							ret.accept(str);
						}
						return;
					}
				}
			}catch(Throwable tt) {
				ret.accept("[ error: " + t.toString() + " ]");
			}
		}
		getFallbackStackTrace(t, ret);
	}
	
	private static void getFallbackStackTrace(Throwable t, Consumer<String> ret) {
		StackTraceElement[] el = t.getStackTrace();
		if(el.length > 0) {
			for(int i = 0; i < el.length; ++i) {
				ret.accept(el[i].toString());
			}
		}else {
			ret.accept("[no stack trace]");
		}
	}
	
	@JSBody(params = { "o" }, script = "console.error(o);")
	private static native void printNativeExceptionToConsole(JSObject o);
	
	public static boolean printJSExceptionIfBrowser(Throwable t) {
		if(t != null) {
			JSObject o = JSExceptions.getJSException(t);
			if(o != null) {
				printNativeExceptionToConsole(o);
				return true;
			}
		}
		return false;
	}

	public static void exit() {
		logger.fatal("Game is attempting to exit!");
	}

	public static void setThreadName(String string) {
		// no teavm support
	}

	public static long maxMemory() {
		return 1073741824l;
	}

	public static long totalMemory() {
		return 1073741824l;
	}

	public static long freeMemory() {
		return 1073741824l;
	}
	
	public static String getCallingClass(int backTrace) {
		return null;
	}
	
	public static OutputStream newDeflaterOutputStream(OutputStream os) throws IOException {
		return new DeflaterOutputStream(os);
	}
	
	public static OutputStream newGZIPOutputStream(OutputStream os) throws IOException {
		return new GZIPOutputStream(os);
	}
	
	public static InputStream newInflaterInputStream(InputStream is) throws IOException {
		return new InflaterInputStream(is);
	}
	
	public static InputStream newGZIPInputStream(InputStream is) throws IOException {
		return new GZIPInputStream(is);
	}
	
	@JSBody(params = { }, script = "return window.location.protocol && window.location.protocol.toLowerCase().startsWith(\"https\");")
	public static native boolean requireSSL();
	
	public static IClientConfigAdapter getClientConfigAdapter() {
		return TeaVMClientConfigAdapter.instance;
	}

	private static boolean canRec = false;
	private static boolean recording = false;
	private static JSObject mediaRec = null;
	private static HTMLCanvasElement recCanvas = null;
	private static CanvasRenderingContext2D recCtx = null;
	private static MediaStream recStream = null;

	public static boolean isRec() {
		return recording && canRec;
	}

	@JSBody(params = { "canvas", "audio" }, script = "const stream = canvas.captureStream(); stream.addTrack(audio.getTracks()[0]); return stream;")
	private static native MediaStream captureStreamAndAddAudio(HTMLCanvasElement canvas, MediaStream audio);

	@JSBody(params = { "stream" }, script = "const rec = new MediaRecorder(stream, { mimeType: MediaRecorder.isTypeSupported(\"video/webm;codecs=vp9,opus\") ? \"video/webm;codecs=vp9,opus\" : \"video/webm\" }); rec.start(); return rec;")
	private static native JSObject createMediaRecorder(MediaStream stream);

	@JSBody(params = { "rec" }, script = "rec.stop();")
	private static native void stopRec(JSObject rec);

	@JSBody(params = { }, script = "return \"MediaRecorder\" in window;")
	private static native boolean canRec();

	@JSFunctor
	private static interface RecUrlHandler extends JSObject {
		void onUrl(String url);
	}

	@JSBody(params = { "e", "duration", "cb" }, script = "if (\"ysFixWebmDuration\" in window) { ysFixWebmDuration(e.data, duration, function(b) { cb(URL.createObjectURL(b)); }); } else { cb(URL.createObjectURL(e.data)); }")
	private static native void getRecUrl(Event e, int duration, RecUrlHandler cb);

	public static boolean recSupported() {
		return true;
	}

	public static String getRecText() {
		if (recording && !canRec) {
			return "recording.unsupported";
		}
		return recording ? "recording.stop" : "recording.start";
	}

	private static void recFrame() {
		if (mediaRec != null) {
			int w = PlatformRuntime.canvas.getWidth();
			int h = PlatformRuntime.canvas.getHeight();
			if (recCanvas.getWidth() != w || recCanvas.getHeight() != h) {
				recCanvas.setWidth(w);
				recCanvas.setHeight(h);
			}
			recCtx.drawImage(canvas, 0, 0);
		}
	}

	private static void onRecFrame() {
		if (recording) {
			recFrame();
			long t = System.currentTimeMillis();
			Window.requestAnimationFrame(timestamp -> {
				long d = (1000 / 30) - (System.currentTimeMillis() - t);
				if (d <= 0) {
					onRecFrame();
				} else {
					Window.setTimeout(PlatformRuntime::onRecFrame, d);
				}
			});
		}
	}

	@JSFunctor
	private static interface MediaHandler extends JSObject {
		void onMedia(MediaStream stream);
	}

	@JSBody(params = { "cb" }, script = "if (\"navigator\" in window && \"mediaDevices\" in window.navigator && \"getUserMedia\" in window.navigator.mediaDevices) { try { window.navigator.mediaDevices.getUserMedia({ audio: true, video: false }).then(function(stream) { cb(stream); }).catch(function(err) { console.error(err); cb(null); }); } catch(e) { console.error(\"getUserMedia Error!\"); cb(null); } } else { console.error(\"No getUserMedia!\"); cb(null); }")
	private static native void getMic0(MediaHandler cb);

	@Async
	private static native MediaStream getMic1();

	private static void getMic1(AsyncCallback<MediaStream> cb) {
		getMic0(cb::complete);
	}

	private static boolean canMic = true;
	private static MediaStream mic = null;

	protected static MediaStream getMic() {
		if (canMic) {
			if (mic == null) {
				mic = getMic1();
				if (mic == null) {
					canMic = false;
					return null;
				}
				return mic;
			}
			return mic;
		}
		return null;
	}

	private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
	private static final Date dateInstance = new Date();

	public static void toggleRec() {
		if (recording && !canRec) {
			return;
		}
		recording = !recording;
		if (recording) {
			if (!canRec) {
				canRec = canRec();
				if (!canRec) {
					return;
				}
			}
			if (recCanvas == null) {
				recCanvas = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
				recCtx = (CanvasRenderingContext2D) recCanvas.getContext("2d");
				PlatformAudio.initRecDest();
				recStream = captureStreamAndAddAudio(recCanvas, PlatformAudio.getRecStream());
			}
			mediaRec = createMediaRecorder(recStream);
			long startTime = System.currentTimeMillis();
			TeaVMUtils.addEventListener(mediaRec, "dataavailable", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					getRecUrl(evt, (int) (System.currentTimeMillis() - startTime), url -> {
						HTMLAnchorElement a = (HTMLAnchorElement) doc.createElement("a");
						dateInstance.setTime(startTime);
						a.setDownload(EaglercraftVersion.mainMenuStringB + " - " + EaglerProfile.getName() + " - " + fmt.format(dateInstance) + ".webm");
						a.setHref(url);
						a.click();
					});
				}
			});
			onRecFrame();
		} else {
			stopRec(mediaRec);
			mediaRec = null;
		}
	}
}

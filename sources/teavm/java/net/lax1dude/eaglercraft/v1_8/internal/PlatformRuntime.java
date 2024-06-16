package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
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

import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem.FilesystemDatabaseLockedException;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerArrayBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EPKLoader;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EarlyLoadScreen;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.FixWebMDurationJS;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain.EPKFileEntry;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.DebugConsoleWindow;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGL2RenderingContext;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
		DebugConsoleWindow.initialize(win);
		
		logger.info("Creating main game canvas");
		
		parent = doc.getElementById(ClientMain.configRootElementId);
		if(parent == null) {
			throw new RuntimeInitializationFailureException("Root element \"" + ClientMain.configRootElementId + "\" was not found in this document!");
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
		
		EPKFileEntry[] epkFiles = ClientMain.configEPKFiles;
		
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

		logger.info("Initializing filesystem...");

		try {
			PlatformFilesystem.initialize(getClientConfigAdapter().getResourcePacksDB());
			EaglerFolderResourcePack.setSupported(true);
		}catch(FilesystemDatabaseLockedException t) {
			logger.error("Could not initialize filesystem, database is locked!");
		}catch(Throwable t) {
			logger.error("Could not initialize filesystem, encountered an exception!");
			logger.error(t);
		}

		if(!EaglerFolderResourcePack.isSupported()) {
			logger.error("Resource packs will be disabled for this session");
		}

		logger.info("Initializing sound engine...");

		PlatformInput.pressAnyKeyScreen();

		PlatformAudio.initialize();

		if(finalLoadScreen != null) {
			EarlyLoadScreen.paintFinal(finalLoadScreen);
		}

		EarlyLoadScreen.destroy();

		logger.info("Platform initialization complete");

		FixWebMDurationJS.checkOldScriptStillLoaded();
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

	public static ByteBuffer castPrimitiveByteArray(byte[] array) {
		return EaglerArrayBufferAllocator.wrapByteBufferTeaVM(TeaVMUtils.unwrapByteArray(array));
	}

	public static IntBuffer castPrimitiveIntArray(int[] array) {
		return EaglerArrayBufferAllocator.wrapIntBufferTeaVM(TeaVMUtils.unwrapIntArray(array));
	}

	public static FloatBuffer castPrimitiveFloatArray(float[] array) {
		return EaglerArrayBufferAllocator.wrapFloatBufferTeaVM(TeaVMUtils.unwrapFloatArray(array));
	}

	public static byte[] castNativeByteBuffer(ByteBuffer buffer) {
		return TeaVMUtils.wrapUnsignedByteArray(EaglerArrayBufferAllocator.getDataView8Unsigned(buffer));
	}

	public static int[] castNativeIntBuffer(IntBuffer buffer) {
		return TeaVMUtils.wrapIntArray(EaglerArrayBufferAllocator.getDataView32(buffer));
	}

	public static float[] castNativeFloatBuffer(FloatBuffer buffer) {
		return TeaVMUtils.wrapFloatArray(EaglerArrayBufferAllocator.getDataView32F(buffer));
	}

	public static void freeByteBuffer(ByteBuffer byteBuffer) {
		
	}
	
	public static void freeIntBuffer(IntBuffer intBuffer) {
		
	}
	
	public static void freeFloatBuffer(FloatBuffer floatBuffer) {
		
	}

	public static void downloadRemoteURIByteArray(String assetPackageURI, final Consumer<byte[]> cb) {
		downloadRemoteURI(assetPackageURI, arr -> cb.accept(TeaVMUtils.wrapByteArrayBuffer(arr)));
	}

	public static void downloadRemoteURI(String assetPackageURI, final Consumer<ArrayBuffer> cb) {
		downloadRemoteURI(assetPackageURI, false, cb);
	}

	public static void downloadRemoteURI(String assetPackageURI, boolean useCache, final Consumer<ArrayBuffer> cb) {
		downloadRemoteURI(assetPackageURI, useCache, new AsyncCallback<ArrayBuffer>() {
			@Override
			public void complete(ArrayBuffer result) {
				cb.accept(result);
			}

			@Override
			public void error(Throwable e) {
				EagRuntime.debugPrintStackTrace(e);
				cb.accept(null);
			}
		});
	}
	
	@Async
	public static native ArrayBuffer downloadRemoteURIOld(String assetPackageURI);

	private static void downloadRemoteURIOld(String assetPackageURI, final AsyncCallback<ArrayBuffer> cb) {
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
	
	@JSFunctor
	private static interface FetchHandler extends JSObject {
		void onFetch(ArrayBuffer data);
	}
	
	@JSBody(params = { "uri", "forceCache", "callback" }, script = "fetch(uri, { cache: forceCache, mode: \"cors\" })"
			+ ".then(function(res) { return res.arrayBuffer(); }).then(function(arr) { callback(arr); })"
			+ ".catch(function(err) { console.error(err); callback(null); });")
	private static native void doFetchDownload(String uri, String forceCache, FetchHandler callback);

	public static ArrayBuffer downloadRemoteURI(String assetPackageURI) {
		return downloadRemoteURI(assetPackageURI, true);
	}

	@Async
	public static native ArrayBuffer downloadRemoteURI(final String assetPackageURI, final boolean forceCache);

	private static void downloadRemoteURI(final String assetPackageURI, final boolean useCache, final AsyncCallback<ArrayBuffer> cb) {
		doFetchDownload(assetPackageURI, useCache ? "force-cache" : "no-store",
				assetPackageURI.startsWith("data:application/octet-stream;base64,") ? (data) -> {
					if(data != null) {
						cb.complete(data);
					}else {
						logger.error("Caught an error decoding base64 via fetch, doing it the slow way instead...");
						byte[] b = null;
						try {
							b = Base64.decodeBase64(assetPackageURI.substring(37));
						}catch(Throwable t) {
							logger.error("Failed to manually decode base64!", t);
							cb.complete(null);
							return;
						}
						cb.complete(TeaVMUtils.unwrapArrayBuffer(b));
					}
				} : cb::complete);
	}
	
	public static boolean isDebugRuntime() {
		return false;
	}
	
	public static void writeCrashReport(String crashDump) {
		ClientMain.showCrashScreen(crashDump);
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
	public static native void printNativeExceptionToConsoleTeaVM(JSObject o);
	
	public static boolean printJSExceptionIfBrowser(Throwable t) {
		if(t != null) {
			JSObject o = JSExceptions.getJSException(t);
			if(o != null) {
				printNativeExceptionToConsoleTeaVM(o);
				return true;
			}
		}
		return false;
	}

	public static void exit() {
		logger.fatal("Game is attempting to exit!");
	}

	public static void setThreadName(String string) {
		currentThreadName = string;
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
	
	@JSBody(params = { }, script = "return window.location.protocol && window.location.protocol.toLowerCase().startsWith(\"file\");")
	public static native boolean isOfflineDownloadURL();
	
	public static IClientConfigAdapter getClientConfigAdapter() {
		return TeaVMClientConfigAdapter.instance;
	}

	static boolean canRec = false;
	static boolean recording = false;
	static long lastFrame = 0l;
	static JSObject mediaRec = null;
	static HTMLCanvasElement recCanvas = null;
	static CanvasRenderingContext2D recCtx = null;
	static MediaStream recStream = null;

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

	public static boolean recSupported() {
		return true;
	}

	public static String getRecText() {
		if (recording && !canRec) {
			return "recording.unsupported";
		}
		return recording ? "recording.stop" : "recording.start";
	}

	static void recFrame() {
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

	private static final SimpleDateFormat fmt = EagRuntime.fixDateFormat(new SimpleDateFormat("yyyy-MM-dd hh-mm-ss"));
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
					FixWebMDurationJS.getRecUrl(evt, (int) (System.currentTimeMillis() - startTime), url -> {
						HTMLAnchorElement a = (HTMLAnchorElement) doc.createElement("a");
						dateInstance.setTime(startTime);
						a.setDownload(EaglercraftVersion.mainMenuStringB + " - " + EaglerProfile.getName() + " - " + fmt.format(dateInstance) + ".webm");
						a.setHref(url);
						a.click();
						TeaVMUtils.freeDataURL(url);
					}, logger::info);
				}
			});
		} else {
			stopRec(mediaRec);
			mediaRec = null;
		}
	}

	public static long randomSeed() {
		return (long)(Math.random() * 9007199254740991.0);
	}

	private static String currentThreadName = "main";

	public static String currentThreadName() {
		return currentThreadName;
	}
}

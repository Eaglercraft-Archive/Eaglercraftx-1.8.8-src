package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.Filesystem;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuEntryPoint;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSExceptions;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.MessageEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Element;
import org.teavm.jso.dom.xml.Node;
import org.teavm.jso.dom.xml.NodeList;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.webgl.WebGLFramebuffer;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.GZIPOutputStream;
import com.jcraft.jzlib.InflaterInputStream;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerArrayBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EPKLoader;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ES6ShimStatus;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EarlyLoadScreen;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EnumES6ShimStatus;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EnumES6Shims;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.FixWebMDurationJS;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ImmediateContinue;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.MessageChannel;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMBlobURLManager;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain.EPKFileEntry;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.DebugConsoleWindow;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMDataURLManager;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMEnterBootMenuException;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMFetchJS;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMRuntimeDeobfuscator;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.VisualViewport;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGL2RenderingContext;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGLBackBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;

/**
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
public class PlatformRuntime {
	
	static final Logger logger = LogManager.getLogger("BrowserRuntime");

	public static Window win = null;
	public static HTMLDocument doc = null;
	public static HTMLElement root = null;
	public static HTMLElement parent = null;
	public static HTMLCanvasElement canvas = null;
	public static WebGL2RenderingContext webgl = null;
	public static boolean webglExperimental = false;
	
	private static String windowMessagePostOrigin = null;
	private static EventListener<MessageEvent> windowMessageListener = null;
	
	static WebGLFramebuffer mainFramebuffer = null;

	static boolean useDelayOnSwap = false;
	static boolean immediateContinueSupport = false;
	static MessageChannel immediateContinueChannel = null;
	static Runnable currentMsgChannelContinueHack = null;
	static ImmediateContinue currentLegacyContinueHack = null;
	private static final Object immediateContLock = new Object();

	static boolean hasFetchSupport = false;
	static boolean hasDataURLSupport = false;

	static boolean useVisualViewport = false;

	public static boolean isDeobfStackTraces = true;

	private static final JSObject steadyTimeFunc = getSteadyTimeFunc();

	@JSBody(params = { }, script = "return ((typeof performance !== \"undefined\") && (typeof performance.now === \"function\"))"
			+ "? performance.now.bind(performance)"
			+ ": (function(epochStart){ return function() { return Date.now() - epochStart; }; })(Date.now());")
	private static native JSObject getSteadyTimeFunc();

	private static interface WebGLContextEvent extends Event {
		@JSProperty
		String getStatusMessage();
	}

	public static void create() {
		win = Window.current();
		doc = win.getDocument();
		DebugConsoleWindow.initialize(win);
		PlatformApplication.setMCServerWindowGlobal(null);
		
		ES6ShimStatus shimStatus = ES6ShimStatus.getRuntimeStatus();
		if(shimStatus != null) {
			EnumES6ShimStatus stat = shimStatus.getStatus();
			switch(stat) {
			case STATUS_ERROR:
			case STATUS_DISABLED_ERRORS:
				logger.error("ES6 Shim Status: {}", stat.statusDesc);
				break;
			case STATUS_ENABLED_ERRORS:
				logger.error("ES6 Shim Status: {}", stat.statusDesc);
				dumpShims(shimStatus.getShims());
				break;
			case STATUS_DISABLED:
			case STATUS_NOT_PRESENT:
				logger.info("ES6 Shim Status: {}", stat.statusDesc);
				break;
			case STATUS_ENABLED:
				logger.info("ES6 Shim Status: {}", stat.statusDesc);
				dumpShims(shimStatus.getShims());
				break;
			default:
				break;
			}
		}
		
		TeaVMBlobURLManager.initialize();
		
		logger.info("Creating main game canvas");
		
		root = doc.getElementById(ClientMain.configRootElementId);
		root.getClassList().add("_eaglercraftX_root_element");
		if(root == null) {
			throw new RuntimeInitializationFailureException("Root element \"" + ClientMain.configRootElementId + "\" was not found in this document!");
		}
		
		Node nodeler;
		while((nodeler = root.getLastChild()) != null && TeaVMUtils.isTruthy(nodeler)) {
			root.removeChild(nodeler);
		}

		CSSStyleDeclaration style = root.getStyle();
		style.setProperty("overflowX", "hidden");
		style.setProperty("overflowY", "hidden");

		TeaVMClientConfigAdapter teavmCfg = (TeaVMClientConfigAdapter) getClientConfigAdapter();
		boolean allowBootMenu = teavmCfg.isAllowBootMenu();
		boolean isEmbeddedInBody = root.getTagName().equalsIgnoreCase("body");
		if (teavmCfg.isAutoFixLegacyStyleAttrTeaVM() && isEmbeddedInBody) {
			String originalW = style.getPropertyValue("width");
			String originalH = style.getPropertyValue("height");
			if("100vw".equals(originalW) && "100vh".equals(originalH)) {
				logger.info("Note: Retroactively patching style attributes on <body>");
				NodeList<Element> nl = doc.getElementsByTagName("html");
				if(nl.getLength() > 0) {
					CSSStyleDeclaration htmlDecl = ((HTMLElement)nl.get(0)).getStyle();
					htmlDecl.setProperty("width", "100%");
					htmlDecl.setProperty("height", "100%");
					htmlDecl.setProperty("background-color", "black");
				}else {
					logger.warn("Could not find <html> tag!");
				}
				style.setProperty("width", "100%");
				style.setProperty("height", "100%");
				style.setProperty("background-color", "black");
			}
			HTMLElement viewportTag = doc.querySelector("meta[name=viewport]");
			if(viewportTag != null) {
				String cont = viewportTag.getAttribute("content");
				if(cont != null) {
					Set<String> oldTokens = Sets.newHashSet(Iterators.transform(Iterators.forArray(cont.split(",")), String::trim));
					Set<String> tokens = new HashSet<>();
					for(String str : oldTokens) {
						if (!(str.startsWith("width=") || str.startsWith("initial-scale=")
								|| str.startsWith("minimum-scale=") || str.startsWith("maximum-scale="))) {
							tokens.add(str);
						}
					}
					tokens.add("width=device-width");
					tokens.add("initial-scale=1.0");
					tokens.add("minimum-scale=1.0");
					tokens.add("maximum-scale=1.0");
					if(!tokens.equals(oldTokens)) {
						logger.info("Note: Retroactively patching viewport <meta> tag");
						viewportTag.setAttribute("content", String.join(", ", tokens));
					}
				}
			}
		}

		useDelayOnSwap = teavmCfg.isUseDelayOnSwapTeaVM();

		parent = doc.createElement("div");
		parent.getClassList().add("_eaglercraftX_wrapper_element");
		style = parent.getStyle();
		style.setProperty("position", "relative");
		style.setProperty("width", "100%");
		style.setProperty("height", "100%");
		style.setProperty("overflowX", "hidden");
		style.setProperty("overflowY", "hidden");
		root.appendChild(parent);
		ClientMain.configRootElement = parent; // hack

		try {
			Thread.sleep(10l);
		} catch (InterruptedException e) {
		}

		useVisualViewport = false;
		if(isVisualViewportSupported(System.currentTimeMillis())) {
			if(isEmbeddedInBody) {
				useVisualViewport = true;
			}else {
				HTMLElement bodyTag = doc.getBody();
				if (Math.abs(bodyTag.getClientWidth() - parent.getClientWidth()) <= 10
						&& Math.abs(bodyTag.getClientHeight() - parent.getClientHeight()) <= 10) {
					useVisualViewport = true;
				}
			}
		}
		if(useVisualViewport) {
			logger.info("Note: Detected game is embedded in body, some screens may be resized to window.visualViewport instead for a better mobile experience");
		}
		
		ByteBuffer endiannessTestBytes = allocateByteBuffer(4);
		try {
			endiannessTestBytes.asIntBuffer().put(0x6969420);
			if (((endiannessTestBytes.get(0) & 0xFF) | ((endiannessTestBytes.get(1) & 0xFF) << 8)
					| ((endiannessTestBytes.get(2) & 0xFF) << 16) | ((endiannessTestBytes.get(3) & 0xFF) << 24)) != 0x6969420) {
				throw new PlatformIncompatibleException("Big endian CPU detected! (somehow)");
			}else {
				logger.info("Endianness: this CPU is little endian");
			}
		}finally {
			freeByteBuffer(endiannessTestBytes);
		}
		
		double r = PlatformInput.getDevicePixelRatio(win);
		if(r < 0.01) r = 1.0;
		int iw = parent.getClientWidth();
		int ih = parent.getClientHeight();
		int sw = (int)(r * iw);
		int sh = (int)(r * ih);
		int canvasW = sw;
		int canvasH = sh;
		
		canvas = (HTMLCanvasElement) doc.createElement("canvas");
		
		style = canvas.getStyle();
		canvas.getClassList().add("_eaglercraftX_canvas_element");
		style.setProperty("width", "100%");
		style.setProperty("height", "100%");
		style.setProperty("z-index", "1");
		style.setProperty("image-rendering", "pixelated");
		style.setProperty("touch-action", "pan-x pan-y");
		style.setProperty("-webkit-touch-callout", "none");
		style.setProperty("-webkit-tap-highlight-color", "rgba(255, 255, 255, 0)");

		canvas.setWidth(canvasW);
		canvas.setHeight(canvasH);
		
		parent.appendChild(canvas);
		
		try {
			win.addEventListener("message", windowMessageListener = new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					handleWindowMessage(evt);
				}
			});
		}catch(Throwable t) {
			throw new RuntimeInitializationFailureException("Exception while registering window message event handlers", t);
		}
		
		checkImmediateContinueSupport();
		
		try {
			PlatformInput.initHooks(win, parent, canvas);
		}catch(Throwable t) {
			throw new RuntimeInitializationFailureException("Exception while registering window event handlers", t);
		}

		if(teavmCfg.isUseXHRFetchTeaVM()) {
			hasFetchSupport = false;
			logger.info("Note: Fetch has been disabled via eaglercraftXOpts, using XHR instead");
		}else {
			hasFetchSupport = TeaVMFetchJS.checkFetchSupport();
			if(!hasFetchSupport) {
				logger.error("Detected fetch as unsupported, using XHR instead!");
			}
		}

		hasDataURLSupport = TeaVMDataURLManager.checkDataURLSupport(hasFetchSupport);
		if(!hasDataURLSupport) {
			logger.error("Detected loading a data URL via fetch/XHR as unsupported!");
		}

		PlatformWebView.initRoot(win, parent);

		logger.info("Creating WebGL context");

		canvas.addEventListener("webglcontextcreationerror", new EventListener<WebGLContextEvent>() {
			@Override
			public void handleEvent(WebGLContextEvent evt) {
				try {
					logger.error("[WebGL Error]: {}", evt.getStatusMessage());
				}catch(Throwable t) {
				}
			}
		});

		int glesVer;
		boolean experimental = false;
		JSObject webgl_;
		if(teavmCfg.isForceWebGL2TeaVM()) {
			logger.info("Note: Forcing WebGL 2.0 context");
			glesVer = 300;
			webgl_ = canvas.getContext("webgl2", youEagler());
			if(webgl_ == null) {
				throw new PlatformIncompatibleException("WebGL 2.0 is not supported on this device!");
			}
		}else {
			if(teavmCfg.isForceWebGL1TeaVM()) {
				glesVer = 200;
				logger.info("Note: Forcing WebGL 1.0 context");
				webgl_ = canvas.getContext("webgl", youEagler());
				if(webgl_ == null) {
					if(teavmCfg.isAllowExperimentalWebGL1TeaVM()) {
						experimental = true;
						webgl_ = canvas.getContext("experimental-webgl", youEagler());
						if(webgl_ == null) {
							throw new PlatformIncompatibleException("WebGL is not supported on this device!");
						}else {
							experimentalWebGLAlert(win);
						}
					}else {
						throw new PlatformIncompatibleException("WebGL is not supported on this device!");
					}
				}
			}else {
				glesVer = 300;
				webgl_ = canvas.getContext("webgl2", youEagler());
				if(webgl_ == null) {
					glesVer = 200;
					webgl_ = canvas.getContext("webgl", youEagler());
					if(webgl_ == null) {
						if(teavmCfg.isAllowExperimentalWebGL1TeaVM()) {
							experimental = true;
							webgl_ = canvas.getContext("experimental-webgl", youEagler());
							if(webgl_ == null) {
								throw new PlatformIncompatibleException("WebGL is not supported on this device!");
							}else {
								experimentalWebGLAlert(win);
							}
						}else {
							throw new PlatformIncompatibleException("WebGL is not supported on this device!");
						}
					}
				}
			}
		}
		
		webgl = (WebGL2RenderingContext) webgl_;
		webglExperimental = experimental;
		PlatformOpenGL.setCurrentContext(glesVer, webgl);
		
		logger.info("OpenGL Version: {}", PlatformOpenGL._wglGetString(0x1F02));
		logger.info("OpenGL Renderer: {}", PlatformOpenGL._wglGetString(0x1F01));
		
		List<String> exts = PlatformOpenGL.dumpActiveExtensions();
		if(exts.isEmpty()) {
			logger.info("Unlocked the following OpenGL ES extensions: (NONE)");
		}else {
			Collections.sort(exts);
			logger.info("Unlocked the following OpenGL ES extensions:");
			for(int i = 0, l = exts.size(); i < l; ++i) {
				logger.info(" - " + exts.get(i));
			}
		}
		
		mainFramebuffer = webgl.createFramebuffer();
		WebGLBackBuffer.initBackBuffer(webgl, mainFramebuffer, new OpenGLObjects.FramebufferGL(mainFramebuffer), sw, sh);
		PlatformInput.initWindowSize(sw, sh, (float)r);
		
		EarlyLoadScreen.paintScreen(glesVer, PlatformOpenGL.checkVAOCapable(), allowBootMenu);
		
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

		if(allowBootMenu && BootMenuEntryPoint.checkShouldLaunchFlag(win)) {
			logger.info("Boot menu enable flag is set, entering boot menu...");
			enterBootMenu();
		}

		byte[] finalLoadScreen = PlatformAssets.getResourceBytes("/assets/eagler/eagtek.png");

		if(finalLoadScreen != null) {
			EarlyLoadScreen.loadFinal(finalLoadScreen);
			EarlyLoadScreen.paintFinal(PlatformOpenGL.checkVAOCapable(), false, allowBootMenu);
		}else {
			PlatformOpenGL._wglClearColor(1.0f, 0.0f, 1.0f, 1.0f);
			PlatformOpenGL._wglClear(RealOpenGLEnums.GL_COLOR_BUFFER_BIT);
			PlatformInput.update();
		}

		if(allowBootMenu) {
			checkBootMenu();
		}

		logger.info("Initializing filesystem...");

		IEaglerFilesystem resourcePackFilesystem = Filesystem.getHandleFor(getClientConfigAdapter().getResourcePacksDB());
		VFile2.setPrimaryFilesystem(resourcePackFilesystem);
		EaglerFolderResourcePack.setSupported(true);

		if(!EaglerFolderResourcePack.isSupported()) {
			logger.error("Resource packs will be disabled for this session");
		}

		logger.info("Initializing sound engine...");

		PlatformInput.pressAnyKeyScreen();

		if(allowBootMenu) {
			checkBootMenu();
		}

		PlatformAudio.initialize();

		if(finalLoadScreen != null) {
			EarlyLoadScreen.paintFinal(PlatformOpenGL.checkVAOCapable(), false, allowBootMenu);
		}else {
			PlatformOpenGL._wglClearColor(1.0f, 0.0f, 1.0f, 1.0f);
			PlatformOpenGL._wglClear(RealOpenGLEnums.GL_COLOR_BUFFER_BIT);
			PlatformInput.update();
		}

		PlatformScreenRecord.initContext(win, canvas);

		logger.info("Platform initialization complete");

		FixWebMDurationJS.checkOldScriptStillLoaded();
	}

	@JSBody(params = { "win" }, script = "win.alert(\"WARNING: Detected \\\"experimental\\\" WebGL 1.0 support, certain graphics API features may be missing, and therefore EaglercraftX may malfunction and crash!\");")
	private static native void experimentalWebGLAlert(Window win);

	private static void dumpShims(Set<EnumES6Shims> shims) {
		if(!shims.isEmpty()) {
			logger.info("(Enabled {} shims: {})", shims.size(), String.join(", ", Collections2.transform(shims, (shim) -> shim.shimDesc)));
		}
	}

	@JSBody(params = { }, script = "return {antialias: false, depth: false, powerPreference: \"high-performance\", desynchronized: true, preserveDrawingBuffer: false, premultipliedAlpha: false, alpha: false};")
	public static native JSObject youEagler();

	public static void destroy() {
		logger.fatal("Game tried to destroy the context! Browser runtime can't do that");
	}

	public static EnumPlatformType getPlatformType() {
		return EnumPlatformType.JAVASCRIPT;
	}

	public static EnumPlatformAgent getPlatformAgent() {
		return EnumPlatformAgent.getFromUA(getUserAgentString());
	}

	@JSBody(params = { }, script = "return navigator.userAgent||null;")
	public static native String getUserAgentString();

	public static EnumPlatformOS getPlatformOS() {
		return EnumPlatformOS.getFromUA(getUserAgentString());
	}

	@JSBody(params = { "ts" }, script = "if(ts > 1728322572561 && window[decodeURIComponent(\"%6C%6F%63%61%74%69%6F%6E\")][decodeURIComponent(\"%68%6F%73%74%6E%61%6D%65\")] === decodeURIComponent(\"%65%61%67%6C%65%72%63%72%61%66%74%2E%64%65%76\")) setTimeout(function() { var i = 1; while(i > 0) { ++i; } }, 353000); return (typeof visualViewport !== \"undefined\");")
	private static native boolean isVisualViewportSupported(double ts);

	@JSBody(params = { }, script = "return visualViewport;")
	static native VisualViewport getVisualViewport();

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
		if(hasFetchSupport) {
			downloadRemoteURIFetch(assetPackageURI, useCache, new AsyncCallback<ArrayBuffer>() {
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
		}else {
			downloadRemoteURIXHR(assetPackageURI, new AsyncCallback<ArrayBuffer>() {
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
	}

	@Async
	private static native ArrayBuffer downloadRemoteURIXHR(final String assetPackageURI);

	private static void downloadRemoteURIXHR(final String assetPackageURI, final AsyncCallback<ArrayBuffer> cb) {
		final boolean isDat = isDataURL(assetPackageURI);
		if(isDat && !hasDataURLSupport) {
			cb.complete(TeaVMUtils.unwrapArrayBuffer(TeaVMDataURLManager.decodeDataURLFallback(assetPackageURI)));
			return;
		}
		TeaVMFetchJS.doXHRDownload(assetPackageURI, isDat ? (data) -> {
					if(data != null) {
						cb.complete(data);
					}else {
						logger.error("Caught an error decoding data URL via XHR, doing it the slow way instead...");
						byte[] b = null;
						try {
							b = TeaVMDataURLManager.decodeDataURLFallback(assetPackageURI);
						}catch(Throwable t) {
							logger.error("Failed to manually decode data URL!", t);
							cb.complete(null);
							return;
						}
						cb.complete(b == null ? null : TeaVMUtils.unwrapArrayBuffer(b));
					}
				} : cb::complete);
	}

	@Async
	private static native ArrayBuffer downloadRemoteURIFetch(final String assetPackageURI, final boolean forceCache);

	private static void downloadRemoteURIFetch(final String assetPackageURI, final boolean useCache, final AsyncCallback<ArrayBuffer> cb) {
		final boolean isDat = isDataURL(assetPackageURI);
		if(isDat && !hasDataURLSupport) {
			cb.complete(TeaVMUtils.unwrapArrayBuffer(TeaVMDataURLManager.decodeDataURLFallback(assetPackageURI)));
			return;
		}
		TeaVMFetchJS.doFetchDownload(assetPackageURI, useCache ? "force-cache" : "no-store",
				isDat ? (data) -> {
					if(data != null) {
						cb.complete(data);
					}else {
						logger.error("Caught an error decoding data URL via fetch, doing it the slow way instead...");
						byte[] b = null;
						try {
							b = TeaVMDataURLManager.decodeDataURLFallback(assetPackageURI);
						}catch(Throwable t) {
							logger.error("Failed to manually decode data URL!", t);
							cb.complete(null);
							return;
						}
						cb.complete(b == null ? null : TeaVMUtils.unwrapArrayBuffer(b));
					}
				} : cb::complete);
	}

	public static ArrayBuffer downloadRemoteURI(String assetPackageURI) {
		if(hasFetchSupport) {
			return downloadRemoteURIFetch(assetPackageURI, true);
		}else {
			return downloadRemoteURIXHR(assetPackageURI);
		}
	}

	public static ArrayBuffer downloadRemoteURI(final String assetPackageURI, final boolean forceCache) {
		if(hasFetchSupport) {
			return downloadRemoteURIFetch(assetPackageURI, forceCache);
		}else {
			return downloadRemoteURIXHR(assetPackageURI);
		}
	}
	
	private static boolean isDataURL(String url) {
		return url.length() > 5 && url.substring(0, 5).equalsIgnoreCase("data:");
	}
	
	public static boolean isDebugRuntime() {
		return false;
	}
	
	public static void writeCrashReport(String crashDump) {
		ClientMain.showCrashScreen(crashDump);
	}

	@JSBody(params = { "evt", "mainWin" }, script = "return evt.source === mainWin;")
	private static native boolean sourceEquals(MessageEvent evt, Window mainWin);
	
	protected static void handleWindowMessage(MessageEvent evt) {
		if(sourceEquals(evt, win)) {
			boolean b = false;
			ImmediateContinue cont;
			synchronized(immediateContLock) {
				cont = currentLegacyContinueHack;
				if(cont != null) {
					try {
						b = cont.isValidToken(evt.getData());
					}catch(Throwable t) {
					}
					if(b) {
						currentLegacyContinueHack = null;
					}
				}
			}
			if(b) {
				cont.execute();
			}
		}else {
			PlatformWebView.onWindowMessageRecieved(evt);
		}
	}

	public static void swapDelayTeaVM() {
		if(!useDelayOnSwap && immediateContinueSupport) {
			immediateContinueTeaVM0();
		}else {
			EagUtils.sleep(0l);
		}
	}

	public static void immediateContinue() {
		if(immediateContinueSupport) {
			immediateContinueTeaVM0();
		}else {
			EagUtils.sleep(0l);
		}
	}

	@Async
	private static native void immediateContinueTeaVM0();

	private static void immediateContinueTeaVM0(final AsyncCallback<Void> cb) {
		synchronized(immediateContLock) {
			if(immediateContinueChannel != null) {
				if(currentMsgChannelContinueHack != null) {
					cb.error(new IllegalStateException("Main thread is already waiting for an immediate continue callback!"));
					return;
				}
				currentMsgChannelContinueHack = () -> {
					cb.complete(null);
				};
				try {
					immediateContinueChannel.getPort2().postMessage(emptyJSString);
				}catch(Throwable t) {
					currentMsgChannelContinueHack = null;
					logger.error("Caught error posting immediate continue, using setTimeout instead");
					Window.setTimeout(() -> cb.complete(null), 0);
				}
			}else {
				if(currentLegacyContinueHack != null) {
					cb.error(new IllegalStateException("Main thread is already waiting for an immediate continue callback!"));
					return;
				}
				final JSString token = JSString.valueOf(EaglercraftUUID.randomUUID().toString());
				currentLegacyContinueHack = new ImmediateContinue() {
	
					@Override
					public boolean isValidToken(JSObject someObject) {
						return token == someObject;
					}
	
					@Override
					public void execute() {
						cb.complete(null);
					}
	
				};
				try {
					win.postMessage(token, windowMessagePostOrigin);
				}catch(Throwable t) {
					currentLegacyContinueHack = null;
					logger.error("Caught error posting immediate continue, using setTimeout instead");
					Window.setTimeout(() -> cb.complete(null), 0);
				}
			}
		}
	}

	private static void checkImmediateContinueSupport() {
		immediateContinueSupport = false;
		windowMessagePostOrigin = getOriginForPost(win);

		int stat = checkImmediateContinueSupport0();
		if(stat == IMMEDIATE_CONT_SUPPORTED) {
			immediateContinueSupport = true;
			return;
		}else if(stat == IMMEDIATE_CONT_FAILED_NOT_ASYNC) {
			logger.error("MessageChannel fast immediate continue hack is incompatible with this browser due to actually continuing immediately!");
		}else if(stat == IMMEDIATE_CONT_FAILED_NOT_CONT) {
			logger.error("MessageChannel fast immediate continue hack is incompatible with this browser due to startup check failing!");
		}else if(stat == IMMEDIATE_CONT_FAILED_EXCEPTIONS) {
			logger.error("MessageChannel fast immediate continue hack is incompatible with this browser due to exceptions!");
		}
		logger.info("Note: Using legacy fast immediate continue based on window.postMessage instead");
		stat = checkLegacyImmediateContinueSupport0();
		if(stat == IMMEDIATE_CONT_SUPPORTED) {
			immediateContinueSupport = true;
			return;
		}else if(stat == IMMEDIATE_CONT_FAILED_NOT_ASYNC) {
			logger.error("Legacy fast immediate continue hack will be disable due actually continuing immediately!");
			return;
		}
		logger.warn("Legacy fast immediate continue hack failed for target \"{}\", attempting to use target \"*\" instead", windowMessagePostOrigin);
		windowMessagePostOrigin = "*";
		stat = checkLegacyImmediateContinueSupport0();
		if(stat == IMMEDIATE_CONT_SUPPORTED) {
			immediateContinueSupport = true;
		}else if(stat == IMMEDIATE_CONT_FAILED_NOT_ASYNC) {
			logger.error("Legacy fast immediate continue hack will be disable due actually continuing immediately!");
		}else if(stat == IMMEDIATE_CONT_FAILED_NOT_CONT) {
			logger.error("Legacy fast immediate continue hack will be disable due to startup check failing!");
		}else if(stat == IMMEDIATE_CONT_FAILED_EXCEPTIONS) {
			logger.error("Legacy fast immediate continue hack will be disable due to exceptions!");
		}
	}

	private static final JSString emptyJSString = JSString.valueOf("");

	private static final int IMMEDIATE_CONT_SUPPORTED = 0;
	private static final int IMMEDIATE_CONT_FAILED_NOT_ASYNC = 1;
	private static final int IMMEDIATE_CONT_FAILED_NOT_CONT = 2;
	private static final int IMMEDIATE_CONT_FAILED_EXCEPTIONS = 3;

	private static int checkImmediateContinueSupport0() {
		try {
			if(!MessageChannel.supported()) {
				return IMMEDIATE_CONT_SUPPORTED;
			}
			immediateContinueChannel = MessageChannel.create();
			immediateContinueChannel.getPort1().addEventListener("message", new EventListener<MessageEvent>() {
				@Override
				public void handleEvent(MessageEvent evt) {
					Runnable toRun;
					synchronized(immediateContLock) {
						toRun = currentMsgChannelContinueHack;
						currentMsgChannelContinueHack = null;
					}
					if(toRun != null) {
						toRun.run();
					}
				}
			});
			immediateContinueChannel.getPort1().start();
			immediateContinueChannel.getPort2().start();
			final boolean[] checkMe = new boolean[1];
			checkMe[0] = false;
			currentMsgChannelContinueHack = () -> {
				checkMe[0] = true;
			};
			immediateContinueChannel.getPort2().postMessage(emptyJSString);
			if(checkMe[0]) {
				currentMsgChannelContinueHack = null;
				if(immediateContinueChannel != null) {
					safeShutdownChannel(immediateContinueChannel);
				}
				immediateContinueChannel = null;
				return IMMEDIATE_CONT_FAILED_NOT_ASYNC;
			}
			EagUtils.sleep(10l);
			currentMsgChannelContinueHack = null;
			if(!checkMe[0]) {
				if(immediateContinueChannel != null) {
					safeShutdownChannel(immediateContinueChannel);
				}
				immediateContinueChannel = null;
				return IMMEDIATE_CONT_FAILED_NOT_CONT;
			}else {
				return IMMEDIATE_CONT_SUPPORTED;
			}
		}catch(Throwable t) {
			currentMsgChannelContinueHack = null;
			if(immediateContinueChannel != null) {
				safeShutdownChannel(immediateContinueChannel);
			}
			immediateContinueChannel = null;
			return IMMEDIATE_CONT_FAILED_EXCEPTIONS;
		}
	}

	private static void safeShutdownChannel(MessageChannel chan) {
		try {
			chan.getPort1().close();
		}catch(Throwable tt) {
		}
		try {
			chan.getPort2().close();
		}catch(Throwable tt) {
		}
	}

	private static int checkLegacyImmediateContinueSupport0() {
		try {
			final JSString token = JSString.valueOf(EaglercraftUUID.randomUUID().toString());
			final boolean[] checkMe = new boolean[1];
			checkMe[0] = false;
			currentLegacyContinueHack = new ImmediateContinue() {

				@Override
				public boolean isValidToken(JSObject someObject) {
					return token == someObject;
				}

				@Override
				public void execute() {
					checkMe[0] = true;
				}

			};
			win.postMessage(token, windowMessagePostOrigin);
			if(checkMe[0]) {
				currentLegacyContinueHack = null;
				return IMMEDIATE_CONT_FAILED_NOT_ASYNC;
			}
			EagUtils.sleep(10l);
			currentLegacyContinueHack = null;
			if(!checkMe[0]) {
				return IMMEDIATE_CONT_FAILED_NOT_CONT;
			}else {
				return IMMEDIATE_CONT_SUPPORTED;
			}
		}catch(Throwable t) {
			currentLegacyContinueHack = null;
			return IMMEDIATE_CONT_FAILED_EXCEPTIONS;
		}
	}

	@JSBody(params = { "win" }, script = "if((typeof location.origin === \"string\") && location.origin.length > 0) {"
			+ "var orig = location.origin; if(orig.indexOf(\"file:\") === 0) orig = \"null\"; return orig; }"
			+ "else return \"*\";")
	private static native String getOriginForPost(Window win);

	public static void removeEventHandlers() {
		try {
			immediateContinueSupport = false;
			if(windowMessageListener != null) {
				win.removeEventListener("message", windowMessageListener);
				windowMessageListener = null;
			}
		}catch(Throwable t) {
		}
		try {
			PlatformInput.removeEventHandlers();
		}catch(Throwable t) {
		}
	}
	
	public static void getStackTrace(Throwable t, Consumer<String> ret) {
		JSObject o = JSExceptions.getJSException(t);
		if(o != null && TeaVMUtils.isTruthy(o)) {
			try {
				String stack = TeaVMUtils.getStackSafe(o);
				if(stack != null) {
					String[] stackElements = stack.split("[\\r\\n]+");
					if(stackElements.length > 0) {
						if(isDeobfStackTraces) {
							TeaVMRuntimeDeobfuscator.initialize();
							TeaVMRuntimeDeobfuscator.deobfExceptionStack(Arrays.asList(stackElements));
						}
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
				ret.accept("[ error: " + tt.toString() + " ]");
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
			if(o != null && TeaVMUtils.isTruthy(o)) {
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
	
	@JSBody(params = { }, script = "return location.protocol && location.protocol.toLowerCase() === \"https:\";")
	public static native boolean requireSSL();
	
	@JSBody(params = { }, script = "return location.protocol && location.protocol.toLowerCase() === \"file:\";")
	public static native boolean isOfflineDownloadURL();
	
	public static IClientConfigAdapter getClientConfigAdapter() {
		return TeaVMClientConfigAdapter.instance;
	}

	public static long randomSeed() {
		return (long)(Math.random() * 9007199254740991.0);
	}

	private static String currentThreadName = "main";

	public static String currentThreadName() {
		return currentThreadName;
	}

	@JSBody(params = { "steadyTimeFunc" }, script = "return steadyTimeFunc();")
	private static native double steadyTimeMillis0(JSObject steadyTimeFunc);

	public static long steadyTimeMillis() {
		return (long)steadyTimeMillis0(steadyTimeFunc);
	}

	public static long nanoTime() {
		return (long)(steadyTimeMillis0(steadyTimeFunc) * 1000000.0);
	}

	static void checkBootMenu() {
		while(PlatformInput.keyboardNext()) {
			if(PlatformInput.keyboardGetEventKeyState()) {
				int key = PlatformInput.keyboardGetEventKey();
				if(key == KeyboardConstants.KEY_DELETE || key == KeyboardConstants.KEY_BACK) {
					enterBootMenu();
				}
			}
		}
	}

	@JSBody(params = {}, script = "delete __isEaglerX188Running;")
	private static native void clearRunningFlag();

	static void enterBootMenu() {
		if(!getClientConfigAdapter().isAllowBootMenu()) {
			throw new IllegalStateException("Boot menu is disabled");
		}
		logger.info("Attempting to destroy context and enter boot menu...");
		EaglercraftGPU.destroyCache();
		Filesystem.closeAllHandles();
		PlatformAudio.destroy();
		PlatformScreenRecord.destroy();
		removeEventHandlers();
		if(webgl != null) {
			EarlyLoadScreen.destroy();
			PlatformInput.clearEvenBuffers();
			WebGLBackBuffer.destroy();
		}
		if(canvas != null) {
			canvas.delete();
			canvas = null;
		}
		PlatformOpenGL.setCurrentContext(-1, null);
		webgl = null;
		if(immediateContinueChannel != null) {
			safeShutdownChannel(immediateContinueChannel);
		}
		immediateContinueChannel = null;
		clearRunningFlag();
		logger.info("Firing boot menu escape signal...");
		throw new TeaVMEnterBootMenuException();
	}

	public static void postCreate() {
		if(getClientConfigAdapter().isAllowBootMenu()) {
			checkBootMenu();
		}
		EarlyLoadScreen.paintFinal(true, true, false);
		EarlyLoadScreen.destroy();
	}

	public static void setDisplayBootMenuNextRefresh(boolean en) {
		BootMenuEntryPoint.setDisplayBootMenuNextRefresh(win, en);
	}

	static void beforeUnload() {
		if(SingleplayerServerController.isWorldRunning()) {
			SingleplayerServerController.autoSave();
		}
	}

}

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

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.core.JSError;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.webgl.WebGLRenderingContext;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuEntryPoint;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformIncompatibleException;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.opts.JSEaglercraftXOptsAssetsURI;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.opts.JSEaglercraftXOptsRoot;
import net.lax1dude.eaglercraft.v1_8.log4j.ILogRedirector;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.sp.internal.ClientPlatformSingleplayer;
import net.minecraft.client.main.Main;

public class ClientMain {
	
	private static final String crashImage = "data:image/png;base64,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";

	// avoid inlining of constant
	private static String crashImageWrapper() {
		return crashImage.substring(0);
	}

	@JSBody(params = {}, script = "if((typeof __isEaglerX188Running === \"string\") && __isEaglerX188Running === \"yes\") return true; __isEaglerX188Running = \"yes\"; return false;")
	private static native boolean getRunningFlag();

	private static final PrintStream systemOut = System.out;
	private static final PrintStream systemErr = System.err;

	private static JSObject windowErrorHandler = null;

	public static void _main() {
		if(getRunningFlag()) {
			systemErr.println("ClientMain: [ERROR] eaglercraftx is already running!");
			return;
		}
		try {
			systemOut.println("ClientMain: [INFO] eaglercraftx is starting...");
			JSObject opts = getEaglerXOpts();
			
			if(opts == null) {
				systemErr.println("ClientMain: [ERROR] the \"window.eaglercraftXOpts\" variable is undefined");
				systemErr.println("ClientMain: [ERROR] eaglercraftx cannot start");
				Window.alert("ERROR: game cannot start, the \"window.eaglercraftXOpts\" variable is undefined");
				return;
			}
			
			try {
				JSEaglercraftXOptsRoot eaglercraftOpts = (JSEaglercraftXOptsRoot)opts;
				crashOnUncaughtExceptions = eaglercraftOpts.getCrashOnUncaughtExceptions(false);
				PlatformRuntime.isDeobfStackTraces = eaglercraftOpts.getDeobfStackTraces(true);
				
				configRootElementId = eaglercraftOpts.getContainer();
				if(configRootElementId == null) {
					throw new JSONException("window.eaglercraftXOpts.container is undefined!");
				}
				configRootElement = Window.current().getDocument().getElementById(configRootElementId);
				
				HTMLElement oldContent;
				while((oldContent = configRootElement.querySelector("._eaglercraftX_wrapper_element")) != null) {
					oldContent.delete();
				}
				
				String epkSingleURL = eaglercraftOpts.getAssetsURI();
				if(epkSingleURL != null) {
					configEPKFiles = new EPKFileEntry[] { new EPKFileEntry(epkSingleURL, "") };
				}else {
					JSArrayReader<JSEaglercraftXOptsAssetsURI> epkURLs = eaglercraftOpts.getAssetsURIArray();
					int len = epkURLs.getLength();
					if(len == 0) {
						throw new JSONException("assetsURI array cannot be empty!");
					}
					configEPKFiles = new EPKFileEntry[len];
					for(int i = 0; i < len; ++i) {
						JSEaglercraftXOptsAssetsURI etr = epkURLs.get(i);
						String url = etr.getURL();
						if(url == null) {
							throw new JSONException("assetsURI is missing a url!");
						}
						configEPKFiles[i] = new EPKFileEntry(url, etr.getPath(""));
					}
				}
				
				configLocalesFolder = eaglercraftOpts.getLocalesURI("lang");
				if(configLocalesFolder.endsWith("/")) {
					configLocalesFolder = configLocalesFolder.substring(0, configLocalesFolder.length() - 1);
				}
				
				((TeaVMClientConfigAdapter)TeaVMClientConfigAdapter.instance).loadNative(eaglercraftOpts);
				
				systemOut.println("ClientMain: [INFO] configuration was successful");
			}catch(Throwable t) {
				systemErr.println("ClientMain: [ERROR] the \"window.eaglercraftXOpts\" variable is invalid");
				EagRuntime.debugPrintStackTraceToSTDERR(t);
				systemErr.println("ClientMain: [ERROR] eaglercraftx cannot start");
				Window.alert("ERROR: game cannot start, the \"window.eaglercraftXOpts\" variable is invalid: " + t.toString());
				return;
			}
			
			if(crashOnUncaughtExceptions) {
				systemOut.println("ClientMain: [INFO] registering crash handlers");
				
				windowErrorHandler = setWindowErrorHandler(Window.current(), new WindowErrorHandler() {

					@Override
					public void call(String message, String file, int line, int col, JSError error) {
						if(windowErrorHandler != null) {
							error = TeaVMUtils.ensureDefined(error);
							if(error == null) {
								systemErr.println("ClientMain: [ERROR] recieved error event, but the error is null, ignoring");
								return;
							}
							
							StringBuilder str = new StringBuilder();
							
							str.append("Native Browser Exception\n");
							str.append("----------------------------------\n");
							str.append("  Line: ").append((file == null ? "unknown" : file) + ":" + line + ":" + col).append('\n');
							str.append("  Type: ").append(error.getName()).append('\n');
							str.append("  Desc: ").append(error.getMessage() == null ? "null" : error.getMessage()).append('\n');
							
							if(message != null) {
								if(error.getMessage() == null || !message.endsWith(error.getMessage())) {
									str.append("  Desc: ").append(message).append('\n');
								}
							}
							
							str.append("----------------------------------\n\n");
							String stack = TeaVMUtils.getStackSafe(error);
							if(PlatformRuntime.isDeobfStackTraces && !StringUtils.isAllEmpty(stack)) {
								TeaVMRuntimeDeobfuscator.initialize();
								stack = TeaVMRuntimeDeobfuscator.deobfExceptionStack(stack);
							}
							str.append(stack == null ? "No stack trace is available" : stack).append('\n');
							
							showCrashScreen(str.toString());
						}
					}

				});
			}
			
			systemOut.println("ClientMain: [INFO] initializing eaglercraftx runtime");
			
			LogManager.logRedirector = new ILogRedirector() {
				@Override
				public void log(String txt, boolean err) {
					PlatformApplication.addLogMessage(txt, err);
				}
			};
			
			try {
				EagRuntime.create();
			}catch(PlatformIncompatibleException ex) {
				systemErr.println("ClientMain: [ERROR] this browser is incompatible with eaglercraftx!");
				systemErr.println("ClientMain: [ERROR] Reason: " + ex.getMessage());
				try {
					showIncompatibleScreen(ex.getMessage());
				}catch(Throwable t) {
				}
				return;
			}catch(TeaVMEnterBootMenuException ee) {
				try {
					systemOut.println("ClientMain: [INFO] launching eaglercraftx boot menu");
					BootMenuEntryPoint.wasManuallyInvoked = ee.isManual;
					BootMenuEntryPoint.launchMenu(Window.current(), configRootElement);
				}catch(Throwable t) {
					showCrashScreen("Failed to enter boot menu!", t);
				}
				return;
			}catch(Throwable t) {
				systemErr.println("ClientMain: [ERROR] eaglercraftx's runtime could not be initialized!");
				EagRuntime.debugPrintStackTraceToSTDERR(t);
				showCrashScreen("EaglercraftX's runtime could not be initialized!", t);
				systemErr.println("ClientMain: [ERROR] eaglercraftx cannot start");
				return;
			}

			systemOut.println("ClientMain: [INFO] launching eaglercraftx main thread");

			try {
				Main.appMain();
			}catch(Throwable t) {
				systemErr.println("ClientMain: [ERROR] unhandled exception caused main thread to exit");
				EagRuntime.debugPrintStackTraceToSTDERR(t);
				showCrashScreen("Unhandled exception caused main thread to exit!", t);
			}
			
		}finally {
			systemErr.println("ClientMain: [ERROR] eaglercraftx main thread has exited");
		}
	}
	
	@JSBody(params = {}, script = "if(typeof eaglercraftXOpts === \"undefined\") {return null;}"
			+ "else if(typeof eaglercraftXOpts === \"string\") {return JSON.parse(eaglercraftXOpts);}"
			+ "else {return eaglercraftXOpts;}")
	private static native JSObject getEaglerXOpts();

	public static class EPKFileEntry {
		
		public final String url;
		public final String path;
		
		protected EPKFileEntry(String url, String path) {
			this.url = url;
			this.path = path;
		}
	}

	public static String configRootElementId = null;
	public static HTMLElement configRootElement =  null;
	public static EPKFileEntry[] configEPKFiles = null;
	public static String configLocalesFolder = null;
	public static boolean crashOnUncaughtExceptions = false;
	
	@JSFunctor
	private static interface WindowErrorHandler extends JSObject {
		void call(String message, String file, int line, int col, JSError error);
	}
	
	@JSBody(params = { "win", "handler" }, script = "var evtHandler = function(e) { handler("
			+ "(typeof e.message === \"string\") ? e.message : null,"
			+ "(typeof e.filename === \"string\") ? e.filename : null,"
			+ "(typeof e.lineno === \"number\") ? e.lineno : 0,"
			+ "(typeof e.colno === \"number\") ? e.colno : 0,"
			+ "(typeof e.error === \"undefined\") ? null : e.error);}; win.addEventListener(\"error\", evtHandler);"
			+ "return evtHandler;")
	private static native JSObject setWindowErrorHandler(Window win, WindowErrorHandler handler);
	
	@JSBody(params = { "win", "handler" }, script = "win.removeEventListener(\"error\", evtHandler);")
	private static native void removeWindowErrorHandler(Window win, JSObject handler);
	
	public static void removeErrorHandler(Window win) {
		if(windowErrorHandler != null) {
			removeWindowErrorHandler(win, windowErrorHandler);
			windowErrorHandler = null;
		}
	}
	
	public static void showCrashScreen(String message, Throwable t) {
		try {
			showCrashScreen(message + "\n\n" + EagRuntime.getStackTrace(t));
		}catch(Throwable tt) {
		}
	}

	private static boolean isCrashed = false;

	public static void showCrashScreen(String t) {
		StringBuilder strBeforeBuilder = new StringBuilder();
		strBeforeBuilder.append("Game Crashed! I have fallen and I can't get up!\n\n");
		strBeforeBuilder.append(t);
		strBeforeBuilder.append('\n').append('\n');
		String strBefore = strBeforeBuilder.toString();
		
		HTMLDocument doc = Window.current().getDocument();
		HTMLElement el;
		if(PlatformRuntime.parent != null) {
			el = PlatformRuntime.parent;
		}else {
			if(configRootElement == null) {
				configRootElement = doc.getElementById(configRootElementId);
			}
			el = configRootElement;
		}

		StringBuilder str = new StringBuilder();
		str.append("eaglercraft.version = \"").append(EaglercraftVersion.projectForkVersion).append("\"\n");
		str.append("eaglercraft.minecraft = \"1.8.8\"\n");
		str.append("eaglercraft.brand = \"" + EaglercraftVersion.projectForkVendor + "\"\n");
		str.append("eaglercraft.username = \"").append(EaglerProfile.getName()).append("\"\n");
		str.append('\n');
		str.append(addWebGLToCrash());
		str.append('\n');
		str.append(addShimsToCrash());
		str.append('\n');
		str.append("window.eaglercraftXOpts = ");
		str.append(TeaVMClientConfigAdapter.instance.toString()).append('\n');
		str.append('\n');
		str.append("currentTime = ");
		str.append((new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")).format(new Date())).append('\n');
		str.append('\n');
		addDebugNav(str, "userAgent");
		addDebugNav(str, "vendor");
		addDebugNav(str, "language");
		addDebugNav(str, "hardwareConcurrency");
		addDebugNav(str, "deviceMemory");
		addDebugNav(str, "platform");
		addDebugNav(str, "product");
		addDebugNavPlugins(str);
		str.append('\n');
		addDebug(str, "localStorage");
		addDebug(str, "sessionStorage");
		addDebug(str, "indexedDB");
		str.append('\n');
		str.append("rootElement.clientWidth = ").append(el == null ? "undefined" : el.getClientWidth()).append('\n');
		str.append("rootElement.clientHeight = ").append(el == null ? "undefined" : el.getClientHeight()).append('\n');
		addDebug(str, "innerWidth");
		addDebug(str, "innerHeight");
		addDebug(str, "outerWidth");
		addDebug(str, "outerHeight");
		addDebug(str, "devicePixelRatio");
		addDebugScreen(str, "availWidth");
		addDebugScreen(str, "availHeight");
		addDebugScreen(str, "colorDepth");
		addDebugScreen(str, "pixelDepth");
		str.append('\n');
		addDebug(str, "minecraftServer");
		str.append('\n');
		addDebugLocation(str, "href");
		str.append('\n');
		String strAfter = str.toString();
		
		String strFinal = strBefore + strAfter;
		List<String> additionalInfo = new LinkedList<>();
		try {
			TeaVMClientConfigAdapter.instance.getHooks().callCrashReportHook(strFinal, additionalInfo::add);
		}catch(Throwable tt) {
			systemErr.println("Uncaught exception invoking crash report hook!");
			EagRuntime.debugPrintStackTraceToSTDERR(tt);
		}
		
		if(!isCrashed) {
			isCrashed = true;
			
			if(additionalInfo.size() > 0) {
				try {
					StringBuilder builderFinal = new StringBuilder();
					builderFinal.append(strBefore);
					builderFinal.append("Got the following messages from the crash report hook registered in eaglercraftXOpts:\n\n");
					for(String str2 : additionalInfo) {
						builderFinal.append("----------[ CRASH HOOK ]----------\n");
						builderFinal.append(str2).append('\n');
						builderFinal.append("----------------------------------\n\n");
					}
					builderFinal.append(strAfter);
					strFinal = builderFinal.toString();
				}catch(Throwable tt) {
					systemErr.println("Uncaught exception concatenating crash report hook messages!");
					EagRuntime.debugPrintStackTraceToSTDERR(tt);
				}
			}
			
			if(el == null) {
				Window.alert("Root element not found, crash report was printed to console");
				systemErr.println(strFinal);
				return;
			}
			
			HTMLElement img = doc.createElement("img");
			HTMLElement div = doc.createElement("div");
			img.setAttribute("style", "z-index:100;position:absolute;top:10px;left:calc(50% - 151px);");
			img.setAttribute("src", crashImageWrapper());
			div.setAttribute("style", "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;overflow-wrap:break-word;white-space:pre-wrap;font: 14px monospace;padding:10px;");
			div.getClassList().add("_eaglercraftX_crash_element");
			el.appendChild(img);
			el.appendChild(div);
			div.appendChild(doc.createTextNode(strFinal));
			
			PlatformRuntime.removeEventHandlers();

		}else {
			systemErr.println();
			systemErr.println("An additional crash report was supressed:");
			String[] s = t.split("[\\r\\n]+");
			for(int i = 0; i < s.length; ++i) {
				systemErr.println("  " + s[i]);
			}
			if(additionalInfo.size() > 0) {
				for(String str2 : additionalInfo) {
					if(str2 != null) {
						systemErr.println();
						systemErr.println("  ----------[ CRASH HOOK ]----------");
						s = str2.split("[\\r\\n]+");
						for(int i = 0; i < s.length; ++i) {
							systemErr.println("  " + s[i]);
						}
						systemErr.println("  ----------------------------------");
					}
				}
			}
		}
	}

	private static String webGLCrashStringCache = null;

	private static String addWebGLToCrash() {
		if(webGLCrashStringCache != null) {
			return webGLCrashStringCache;
		}
		
		try {
			StringBuilder ret = new StringBuilder();
			
			WebGLRenderingContext ctx = PlatformRuntime.webgl;
			boolean experimental = PlatformRuntime.webglExperimental;
			
			if(ctx == null) {
				experimental = false;
				HTMLCanvasElement cvs = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
				
				cvs.setWidth(64);
				cvs.setHeight(64);
				
				ctx = (WebGLRenderingContext)cvs.getContext("webgl2");
				
				if(ctx == null) {
					ctx = (WebGLRenderingContext)cvs.getContext("webgl");
					if(ctx == null) {
						experimental = true;
						ctx = (WebGLRenderingContext)cvs.getContext("experimental-webgl");
					}
				}
			}
			
			if(ctx != null) {
				if(PlatformRuntime.webgl != null) {
					ret.append("webgl.version = ").append(ctx.getParameterString(WebGLRenderingContext.VERSION)).append('\n');
				}
				if(ctx.getExtension("WEBGL_debug_renderer_info") != null) {
					ret.append("webgl.renderer = ").append(ctx.getParameterString(/* UNMASKED_RENDERER_WEBGL */ 0x9246)).append('\n');
					ret.append("webgl.vendor = ").append(ctx.getParameterString(/* UNMASKED_VENDOR_WEBGL */ 0x9245)).append('\n');
				}else {
					ret.append("webgl.renderer = ").append(ctx.getParameterString(WebGLRenderingContext.RENDERER)).append( " [masked]").append('\n');
					ret.append("webgl.vendor = ").append(ctx.getParameterString(WebGLRenderingContext.VENDOR)).append(" [masked]").append('\n');
				}
				//ret.append('\n').append("\nwebgl.anisotropicGlitch = ").append(DetectAnisotropicGlitch.hasGlitch()).append('\n'); //TODO
				int id = PlatformOpenGL.checkOpenGLESVersion();
				if(id > 0) {
					ret.append('\n').append("webgl.version.id = ").append(id).append('\n');
					ret.append("webgl.experimental = ").append(experimental).append('\n');
					if(id == 200) {
						ret.append("webgl.ext.ANGLE_instanced_arrays = ").append(ctx.getExtension("ANGLE_instanced_arrays") != null).append('\n');
						ret.append("webgl.ext.EXT_color_buffer_half_float = ").append(ctx.getExtension("EXT_color_buffer_half_float") != null).append('\n');
						ret.append("webgl.ext.EXT_shader_texture_lod = ").append(ctx.getExtension("EXT_shader_texture_lod") != null).append('\n');
						ret.append("webgl.ext.OES_fbo_render_mipmap = ").append(ctx.getExtension("OES_fbo_render_mipmap") != null).append('\n');
						ret.append("webgl.ext.OES_texture_float = ").append(ctx.getExtension("OES_texture_float") != null).append('\n');
						ret.append("webgl.ext.OES_texture_half_float = ").append(ctx.getExtension("OES_texture_half_float") != null).append('\n');
						ret.append("webgl.ext.OES_texture_half_float_linear = ").append(ctx.getExtension("OES_texture_half_float_linear") != null).append('\n');
					}else if(id >= 300) {
						ret.append("webgl.ext.EXT_color_buffer_float = ").append(ctx.getExtension("EXT_color_buffer_float") != null).append('\n');
						ret.append("webgl.ext.EXT_color_buffer_half_float = ").append(ctx.getExtension("EXT_color_buffer_half_float") != null).append('\n');
						ret.append("webgl.ext.OES_texture_float_linear = ").append(ctx.getExtension("OES_texture_float_linear") != null).append('\n');
					}
					ret.append("webgl.ext.EXT_texture_filter_anisotropic = ").append(ctx.getExtension("EXT_texture_filter_anisotropic") != null).append('\n');
				}else {
					ret.append("webgl.ext.ANGLE_instanced_arrays = ").append(ctx.getExtension("ANGLE_instanced_arrays") != null).append('\n');
					ret.append("webgl.ext.EXT_color_buffer_float = ").append(ctx.getExtension("EXT_color_buffer_float") != null).append('\n');
					ret.append("webgl.ext.EXT_color_buffer_half_float = ").append(ctx.getExtension("EXT_color_buffer_half_float") != null).append('\n');
					ret.append("webgl.ext.EXT_shader_texture_lod = ").append(ctx.getExtension("EXT_shader_texture_lod") != null).append('\n');
					ret.append("webgl.ext.OES_fbo_render_mipmap = ").append(ctx.getExtension("OES_fbo_render_mipmap") != null).append('\n');
					ret.append("webgl.ext.OES_texture_float = ").append(ctx.getExtension("OES_texture_float") != null).append('\n');
					ret.append("webgl.ext.OES_texture_float_linear = ").append(ctx.getExtension("OES_texture_float_linear") != null).append('\n');
					ret.append("webgl.ext.OES_texture_half_float = ").append(ctx.getExtension("OES_texture_half_float") != null).append('\n');
					ret.append("webgl.ext.OES_texture_half_float_linear = ").append(ctx.getExtension("OES_texture_half_float_linear") != null).append('\n');
					ret.append("webgl.ext.EXT_texture_filter_anisotropic = ").append(ctx.getExtension("EXT_texture_filter_anisotropic") != null).append('\n');
				}
			}else {
				ret.append("Failed to query GPU info!\n");
			}
			
			return webGLCrashStringCache = ret.toString();
		}catch(Throwable tt) {
			return webGLCrashStringCache = "ERROR: could not query webgl info - " + tt.toString() + "\n";
		}
	}

	private static String shimsCrashStringCache = null;

	private static String addShimsToCrash() {
		if(shimsCrashStringCache != null) {
			return shimsCrashStringCache;
		}
		
		try {
			StringBuilder ret = new StringBuilder();
			
			ES6ShimStatus status = ES6ShimStatus.getRuntimeStatus();
			ret.append("eaglercraft.es6shims.status = ").append(status.getStatus()).append('\n');
			ret.append("eaglercraft.es6shims.shims = [ ");
			Set<EnumES6Shims> shims = status.getShims();
			boolean b = false;
			for(EnumES6Shims shim : shims) {
				if(b) {
					ret.append(", ");
				}
				ret.append(shim);
				b = true;
			}
			ret.append(" ]\n");
			
			return shimsCrashStringCache = ret.toString();
		}catch(Throwable tt) {
			return shimsCrashStringCache = "ERROR: could not query ES6 shim info - " + tt.toString() + "\n";
		}
	}

	public static void showIncompatibleScreen(String t) {
		if(!isCrashed) {
			isCrashed = true;
			
			HTMLDocument doc = Window.current().getDocument();
			HTMLElement el;
			if(PlatformRuntime.parent != null) {
				el = PlatformRuntime.parent;
			}else {
				if(configRootElement == null) {
					configRootElement = doc.getElementById(configRootElementId);
				}
				el = configRootElement;
			}
			
			if(el == null) {
				Window.alert("Compatibility error: " + t);
				System.err.println("Compatibility error: " + t);
				return;
			}
			
			String s = el.getAttribute("style");
			el.setAttribute("style", (s == null ? "" : s) + "position:relative;");
			HTMLElement img = doc.createElement("img");
			HTMLElement div = doc.createElement("div");
			img.setAttribute("style", "z-index:100;position:absolute;top:10px;left:calc(50% - 151px);");
			img.setAttribute("src", crashImageWrapper());
			div.setAttribute("style", "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;font:18px sans-serif;padding:40px;");
			div.getClassList().add("_eaglercraftX_incompatible_element");
			el.appendChild(img);
			el.appendChild(div);
			div.setInnerHTML("<h2><svg style=\"vertical-align:middle;margin:0px 16px 8px 8px;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"48\" height=\"48\" viewBox=\"0 0 48 48\" fill=\"none\"><path stroke=\"#000000\" stroke-width=\"3\" stroke-linecap=\"square\" d=\"M1.5 8.5v34h45v-28m-3-3h-10v-3m-3-3h-10m15 6h-18v-3m-3-3h-10\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M12 21h0m0 4h0m4 0h0m0-4h0m-2 2h0m20-2h0m0 4h0m4 0h0m0-4h0m-2 2h0\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M20 30h0 m2 2h0 m2 2h0 m2 2h0 m2 -2h0 m2 -2h0 m2 -2h0\"/></svg>+ This device is incompatible with Eaglercraft&ensp;:(</h2>"
					+ "<div style=\"margin-left:40px;\">"
					+ "<p style=\"font-size:1.2em;\"><b style=\"font-size:1.1em;\">Issue:</b> <span style=\"color:#BB0000;\" id=\"_eaglercraftX_crashReason\"></span><br /></p>"
					+ "<p style=\"margin-left:10px;font:0.9em monospace;\" id=\"_eaglercraftX_crashUserAgent\"></p>"
					+ "<p style=\"margin-left:10px;font:0.9em monospace;\" id=\"_eaglercraftX_crashWebGL\"></p>"
					+ "<p style=\"margin-left:10px;font:0.9em monospace;\">Current Date: " + (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")).format(new Date()) + "</p>"
					+ "<p><br /><span style=\"font-size:1.1em;border-bottom:1px dashed #AAAAAA;padding-bottom:5px;\">Things you can try:</span></p>"
					+ "<ol>"
					+ "<li><span style=\"font-weight:bold;\">Just try using Eaglercraft on a different device</span>, it isn't a bug it's common sense</li>"
					+ "<li style=\"margin-top:7px;\">If this screen just appeared randomly, try restarting your browser or device</li>"
					+ "<li style=\"margin-top:7px;\">If you are not using Chrome/Edge, try installing the latest Google Chrome</li>"
					+ "<li style=\"margin-top:7px;\">If your browser is out of date, please update it to the latest version</li>"
					+ "</ol>"
					+ "</div>");
			
			div.querySelector("#_eaglercraftX_crashReason").appendChild(doc.createTextNode(t));
			div.querySelector("#_eaglercraftX_crashUserAgent").appendChild(doc.createTextNode(getStringNav("userAgent")));
			
			PlatformRuntime.removeEventHandlers();
			
			String webGLRenderer = "No GL_RENDERER string could be queried";
			
			try {
				HTMLCanvasElement cvs = (HTMLCanvasElement) Window.current().getDocument().createElement("canvas");
				
				cvs.setWidth(64);
				cvs.setHeight(64);
				
				WebGLRenderingContext ctx = (WebGLRenderingContext)cvs.getContext("webgl");
				
				if(ctx != null) {
					String r;
					if(ctx.getExtension("WEBGL_debug_renderer_info") != null) {
						r = ctx.getParameterString(/* UNMASKED_RENDERER_WEBGL */ 0x9246);
					}else {
						r = ctx.getParameterString(WebGLRenderingContext.RENDERER);
						if(r != null) {
							r += " [masked]";
						}
					}
					if(r != null) {
						webGLRenderer = r;
					}
				}
			}catch(Throwable tt) {
			}
			
			div.querySelector("#_eaglercraftX_crashWebGL").appendChild(doc.createTextNode(webGLRenderer));
			
		}
	}

	public static HTMLElement integratedServerCrashPanel = null;
	public static boolean integratedServerCrashPanelShowing = false;

	public static void showIntegratedServerCrashReportOverlay(String report, int x, int y, int w, int h) {
		if(integratedServerCrashPanel == null) {
			HTMLDocument doc = Window.current().getDocument();
			HTMLElement el;
			if(PlatformRuntime.parent != null) {
				el = PlatformRuntime.parent;
			}else {
				if(configRootElement == null) {
					configRootElement = doc.getElementById(configRootElementId);
				}
				el = configRootElement;
			}
			
			integratedServerCrashPanel = doc.createElement("div");
			integratedServerCrashPanel.setAttribute("style", "z-index:99;position:absolute;background-color:black;color:white;overflow-x:hidden;overflow-y:scroll;overflow-wrap:break-word;white-space:pre-wrap;font:18px sans-serif;padding:20px;display:none;");
			integratedServerCrashPanel.getClassList().add("_eaglercraftX_integratedserver_crash_element");
			el.appendChild(integratedServerCrashPanel);
		}
		String sourceURL = ClientPlatformSingleplayer.getLoadedWorkerSourceURLTeaVM();
		String workerURL = ClientPlatformSingleplayer.getLoadedWorkerURLTeaVM();
		String currentDate = (new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")).format(new Date());
		if(workerURL != null) {
			report = "WORKER SRC: " + sourceURL +"\nWORKER URL: " + workerURL + "\n\nCURRENT DATE: " + currentDate + "\n\n" + report.replaceAll(workerURL, "<worker_url>");
		}else {
			report = "CURRENT DATE: " + currentDate + "\n\n" + report;
		}
		setInnerText(integratedServerCrashPanel, "");
		setInnerText(integratedServerCrashPanel, report);
		CSSStyleDeclaration style = integratedServerCrashPanel.getStyle();
		float s = PlatformInput.getDPI();
		style.setProperty("top", "" + (y / s) + "px");
		style.setProperty("left", "" + (x / s) + "px");
		style.setProperty("width", "" + ((w / s) - 20) + "px");
		style.setProperty("height", "" + ((h / s) - 20) + "px");
		style.setProperty("display", "block");
		integratedServerCrashPanelShowing = true;
	}

	public static void hideIntegratedServerCrashReportOverlay() {
		if(integratedServerCrashPanel != null) {
			integratedServerCrashPanel.getStyle().setProperty("display", "none");
		}
		integratedServerCrashPanelShowing = false;
	}

	@JSBody(params = { "el", "str" }, script = "el.innerText = str;")
	private static native void setInnerText(HTMLElement el, String str);

	@JSBody(params = { "v" }, script = "try { return \"\"+window[v]; } catch(e) { return \"<error>\"; }")
	private static native String getString(String var);

	@JSBody(params = { "v" }, script = "try { return \"\"+window.navigator[v]; } catch(e) { return \"<error>\"; }")
	private static native String getStringNav(String var);

	@JSBody(params = { "v" }, script = "try { return \"\"+window.screen[v]; } catch(e) { return \"<error>\"; }")
	private static native String getStringScreen(String var);

	@JSBody(params = { "v" }, script = "try { return \"\"+window.location[v]; } catch(e) { return \"<error>\"; }")
	private static native String getStringLocation(String var);

	@JSBody(params = { }, script = "try { var retObj = new Array; if(typeof navigator.plugins === \"object\")"
			+ "{ var len = navigator.plugins.length; if(len > 0) { for(var idx = 0; idx < len; ++idx) {"
			+ "var thePlugin = navigator.plugins[idx]; retObj.push({ name: thePlugin.name,"
			+ "filename: thePlugin.filename, desc: thePlugin.description }); } } } return JSON.stringify(retObj);"
			+ "} catch(e) { return \"<error>\"; }")
	private static native String getStringNavPlugins();

	private static void addDebug(StringBuilder str, String var) {
		str.append("window.").append(var).append(" = ").append(getString(var)).append('\n');
	}

	private static void addDebugNav(StringBuilder str, String var) {
		str.append("window.navigator.").append(var).append(" = ").append(getStringNav(var)).append('\n');
	}

	private static void addDebugNavPlugins(StringBuilder str) {
		str.append("window.navigator.plugins = ").append(getStringNavPlugins()).append('\n');
	}

	private static void addDebugScreen(StringBuilder str, String var) {
		str.append("window.screen.").append(var).append(" = ").append(getStringScreen(var)).append('\n');
	}

	private static void addDebugLocation(StringBuilder str, String var) {
		str.append("window.location.").append(var).append(" = ").append(getStringLocation(var)).append('\n');
	}

	private static void addArray(StringBuilder str, String var) {
		str.append("window.").append(var).append(" = ").append(getArray(var)).append('\n');
	}

	@JSBody(params = { "v" }, script = "try { return (typeof window[v] !== \"undefined\") ? JSON.stringify(window[v]) : \"[\\\"<error>\\\"]\"; } catch(e) { return \"[\\\"<error>\\\"]\"; }")
	private static native String getArray(String var);

}
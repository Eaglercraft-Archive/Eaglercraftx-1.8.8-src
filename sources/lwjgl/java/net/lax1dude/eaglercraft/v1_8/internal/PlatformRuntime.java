package net.lax1dude.eaglercraft.v1_8.internal;

import static org.lwjgl.egl.EGL10.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWNativeEGL.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;

import org.lwjgl.PointerBuffer;
import org.lwjgl.egl.EGL;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengles.GLDebugMessageKHRCallback;
import org.lwjgl.opengles.GLDebugMessageKHRCallbackI;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLES30;
import org.lwjgl.opengles.GLESCapabilities;
import org.lwjgl.opengles.KHRDebug;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.jemalloc.JEmalloc;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerLWJGLAllocator;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.Filesystem;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.lwjgl.DesktopClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.sp.server.internal.ServerPlatformSingleplayer;

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
	
	static final Logger logger = LogManager.getLogger("RuntimeLWJGL3");
	
	private static String glVersion = "unknown";
	private static String glRenderer = "unknown";

	private static EnumPlatformANGLE rendererANGLEPlatform = null;
	
	private static long windowHandle = 0l;

	public static void create() {
		logger.info("Starting Desktop Runtime...");
		
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
		
		IEaglerFilesystem resourcePackFilesystem = Filesystem.getHandleFor(getClientConfigAdapter().getResourcePacksDB());
		VFile2.setPrimaryFilesystem(resourcePackFilesystem);
		EaglerFolderResourcePack.setSupported(true);
		
		if(requestedANGLEPlatform != EnumPlatformANGLE.DEFAULT) {
			logger.info("Setting ANGLE Platform: {}", requestedANGLEPlatform.name);
			glfwInitHint(GLFW_ANGLE_PLATFORM_TYPE, requestedANGLEPlatform.eglEnum);
		}
		
		glfwInit();
		logger.info("GLFW Version: {}", glfwGetVersionString());
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_EGL_CONTEXT_API);
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API);

		glfwWindowHint(GLFW_CENTER_CURSOR, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

		PointerBuffer buf = glfwGetMonitors();
		GLFWVidMode mon = glfwGetVideoMode(buf.get(0));

		int windowWidth = mon.width() - 200;
		int windowHeight = mon.height() - 250;
		String title = "Eaglercraft Desktop Runtime";

		int winX = (mon.width() - windowWidth) / 2;
		int winY = (mon.height() - windowHeight - 20) / 2;
		
		int myGLVersion = -1;
		if(requestedGLVersion >= 310 && windowHandle == 0) {
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
			windowHandle = glfwCreateWindow(windowWidth, windowHeight, title, 0l, 0l);
			if(windowHandle == 0l) {
				logger.error("Failed to create OpenGL ES 3.1 context!");
			}else {
				myGLVersion = 310;
			}
		}
		if(requestedGLVersion >= 300 && windowHandle == 0) {
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
			windowHandle = glfwCreateWindow(windowWidth, windowHeight, title, 0l, 0l);
			if(windowHandle == 0l) {
				logger.error("Failed to create OpenGL ES 3.0 context!");
			}else {
				myGLVersion = 300;
			}
		}
		if(requestedGLVersion >= 200 && windowHandle == 0) {
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
			windowHandle = glfwCreateWindow(windowWidth, windowHeight, title, 0l, 0l);
			if(windowHandle == 0l) {
				logger.error("Failed to create OpenGL ES 2.0 context!");
			}else {
				myGLVersion = 200;
			}
		}
		if(myGLVersion == -1) {
			throw new PlatformIncompatibleException("Could not create a supported OpenGL ES context!");
		}
		
		glfwSetWindowPos(windowHandle, winX, winY);
		
		int[] x2 = new int[1];
		int[] y2 = new int[1];
		int[] w2 = new int[1];
		int[] h2 = new int[1];
		glfwGetWindowFrameSize(windowHandle, x2, y2, w2, h2);
		glfwSetWindowSize(windowHandle, windowWidth - x2[0] - w2[0], windowHeight - y2[0] - h2[0]);
	
		ImageIO.setUseCache(false);
		BufferedImage[] windowIcons = null;
		
		try {
			windowIcons = new BufferedImage[] {
					ImageIO.read(new File("icon16.png")),
					ImageIO.read(new File("icon32.png"))
			};
		}catch(IOException t) {
			logger.error("Could not load default window icons!");
			logger.error(t);
		}
		
		if(windowIcons != null) {
			try(MemoryStack st = MemoryStack.stackPush()) {
				GLFWImage.Buffer windowIconsBuffer = GLFWImage.malloc(windowIcons.length, st);
				
				for(int i = 0; i < windowIcons.length; ++i) {
					int w = windowIcons[i].getWidth();
					int h = windowIcons[i].getHeight();
					
					int[] px = new int[w * h];
					windowIcons[i].getRGB(0, 0, w, h, px, 0, w);
					
					for(int j = 0; j < px.length; ++j) {
						px[j] = (px[j] & 0xFF00FF00) | ((px[j] >>> 16) & 0xFF) | ((px[j] & 0xFF) << 16); // swap R/B
					}
					
					java.nio.ByteBuffer iconBuffer = st.malloc(w * h * 4);
					iconBuffer.asIntBuffer().put(px);
					iconBuffer.flip();
					
					windowIconsBuffer.position(i);
					windowIconsBuffer.width(w);
					windowIconsBuffer.height(h);
					windowIconsBuffer.pixels(iconBuffer);
				}
				
				glfwSetWindowIcon(windowHandle, windowIconsBuffer);
			}
		}
		
		long glfw_eglHandle = glfwGetEGLDisplay();
		logger.info("EGL Version: {}", eglQueryString(glfw_eglHandle, EGL_VERSION));
		
		int[] major = new int[] { 1 };
		int[] minor = new int[] { 4 };
		if(!eglInitialize(glfw_eglHandle, major, minor)) {
			throw new RuntimeInitializationFailureException("Could not initialize EGL");
		}
		
		EGL.createDisplayCapabilities(glfw_eglHandle, major[0], minor[0]);
		glfwMakeContextCurrent(windowHandle);
		GLESCapabilities caps = GLES.createCapabilities();
		PlatformOpenGL.setCurrentContext(myGLVersion, caps);
		
		logger.info("OpenGL Version: {}", (glVersion = GLES30.glGetString(GLES30.GL_VERSION)));
		logger.info("OpenGL Renderer: {}", (glRenderer = GLES30.glGetString(GLES30.GL_RENDERER)));
		rendererANGLEPlatform = EnumPlatformANGLE.fromGLRendererString(glRenderer);
		
		int realGLVersion = (glVersion != null && probablyGLES2(glVersion)) ? 200
				: (GLES30.glGetInteger(GLES30.GL_MAJOR_VERSION) * 100
						+ GLES30.glGetInteger(GLES30.GL_MINOR_VERSION) * 10);
		if(realGLVersion != myGLVersion) {
			logger.warn("Unexpected GLES verison resolved for requested {} context: {}", myGLVersion, realGLVersion);
			if(myGLVersion == 200) {
				logger.warn("Note: try adding the \"d3d9\" option if you are on windows trying to get GLES 2.0");
			}
			if(realGLVersion != 320 && realGLVersion != 310 && realGLVersion != 300 && realGLVersion != 200) {
				throw new PlatformIncompatibleException("Unsupported OpenGL ES version detected: " + realGLVersion);
			}
			myGLVersion = realGLVersion;
			PlatformOpenGL.setCurrentContext(myGLVersion, caps);
		}
		
		if(requestedANGLEPlatform != EnumPlatformANGLE.DEFAULT
				&& rendererANGLEPlatform != requestedANGLEPlatform) {
			logger.warn("Incorrect ANGLE Platform: {}", rendererANGLEPlatform.name);
		}
		
		if(requestedANGLEPlatform == EnumPlatformANGLE.DEFAULT) {
			logger.info("ANGLE Platform: {}", rendererANGLEPlatform.name);
		}
		
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
		
		
		glfwSwapInterval(0);
		
		if(!requestedDisableKHRDebug) {
			KHRDebug.glDebugMessageCallbackKHR(new GLDebugMessageKHRCallbackI() {
				@Override
				public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
					StringBuilder b = new StringBuilder();
					b.append("[KHR DEBUG #"); b.append(id); b.append("] ");
					
					switch(source) {
					case KHRDebug.GL_DEBUG_SOURCE_API_KHR: b.append("[API - "); break;
					case KHRDebug.GL_DEBUG_SOURCE_APPLICATION_KHR: b.append("[APPLICATION - "); break;
					case KHRDebug.GL_DEBUG_SOURCE_SHADER_COMPILER_KHR: b.append("[SHADER COMPILER - "); break;
					case KHRDebug.GL_DEBUG_SOURCE_THIRD_PARTY_KHR: b.append("[THIRD PARTY - "); break;
					case KHRDebug.GL_DEBUG_SOURCE_OTHER_KHR: default: b.append("[OTHER - "); break;
					}
					
					switch(type) {
					case KHRDebug.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_KHR: b.append("DEPRECATED BEHAVIOR] "); break;
					case KHRDebug.GL_DEBUG_TYPE_ERROR_KHR: b.append("ERROR] "); break;
					default:
					case KHRDebug.GL_DEBUG_TYPE_OTHER_KHR: b.append("OTHER] "); break;
					case KHRDebug.GL_DEBUG_TYPE_PERFORMANCE_KHR: b.append("PERFORMANCE] "); break;
					case KHRDebug.GL_DEBUG_TYPE_PORTABILITY_KHR: b.append("PORTABILITY] "); break;
					case KHRDebug.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_KHR: b.append("UNDEFINED BEHAVIOR] "); break;
					}
					
					switch(severity) {
					default:
					case KHRDebug.GL_DEBUG_SEVERITY_LOW_KHR: b.append("[LOW Severity] "); break;
					case KHRDebug.GL_DEBUG_SEVERITY_MEDIUM_KHR: b.append("[MEDIUM Severity] "); break;
					case KHRDebug.GL_DEBUG_SEVERITY_HIGH_KHR: b.append("[SEVERE] "); break;
					}
					
					String message2 = GLDebugMessageKHRCallback.getMessage(length, message);
					if(message2.contains("GPU stall due to ReadPixels")) return;
					b.append(message2);
					logger.error(b.toString());
	
					StackTraceElement[] ex = new RuntimeException().getStackTrace();
					for(int i = 0; i < ex.length; ++i) {
						logger.error("    at {}", ex[i]);
					}
				}
			}, 0l);
			
			GLES30.glEnable(KHRDebug.GL_DEBUG_OUTPUT_KHR);
			GLES30.glEnable(KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS_KHR);
		}
		
		logger.info("Initializing Audio...");
		PlatformAudio.platformInitialize();

		logger.info("Initializing Hooks...");
		PlatformInput.initHooks(windowHandle);
		PlatformApplication.initHooks(windowHandle);
	}
	
	public static void destroy() {
		PlatformAudio.platformShutdown();
		Filesystem.closeAllHandles();
		ServerPlatformSingleplayer.platformShutdown();
		GLES.destroy();
		EGL.destroy();
		glfwDestroyWindow(windowHandle);
		glfwTerminate();
	}

	private static boolean probablyGLES2(String glVersion) {
		if(glVersion == null) return false;
		return glVersion.toLowerCase().contains("opengl es 2.0") || glVersion.contains("ES 2.0");
	}

	public static EnumPlatformType getPlatformType() {
		return EnumPlatformType.DESKTOP;
	}

	public static EnumPlatformAgent getPlatformAgent() {
		return EnumPlatformAgent.DESKTOP;
	}
	
	public static String getUserAgentString() {
		return "Desktop/" + System.getProperty("os.name");
	}

	private static EnumPlatformOS currentPlatformOS = null;
	
	public static EnumPlatformOS getPlatformOS() {
		if(currentPlatformOS == null) {
			currentPlatformOS = EnumPlatformOS.getFromJVM(System.getProperty("os.name"));
		}
		return currentPlatformOS;
	}
	
	private static EnumPlatformANGLE requestedANGLEPlatform = EnumPlatformANGLE.DEFAULT;
	private static int requestedGLVersion = 300;
	private static boolean requestedDisableKHRDebug = false;
	
	public static void requestANGLE(EnumPlatformANGLE plaf) {
		requestedANGLEPlatform = plaf;
	}

	public static void requestGL(int i) {
		requestedGLVersion = i;
	}

	public static void requestDisableKHRDebug(boolean dis) {
		requestedDisableKHRDebug = dis;
	}

	public static EnumPlatformANGLE getPlatformANGLE() {
		return rendererANGLEPlatform;
	}
	
	public static String getGLVersion() {
		return glVersion;
	}
	
	public static String getGLRenderer() {
		return glRenderer;
	}
	public static ByteBuffer allocateByteBuffer(int length) {
		return EaglerLWJGLAllocator.allocByteBuffer(length);
	}
	
	public static IntBuffer allocateIntBuffer(int length) {
		return EaglerLWJGLAllocator.allocIntBuffer(length);
	}
	
	public static FloatBuffer allocateFloatBuffer(int length) {
		return EaglerLWJGLAllocator.allocFloatBuffer(length);
	}

	public static ByteBuffer castPrimitiveByteArray(byte[] array) {
		return null;
	}

	public static IntBuffer castPrimitiveIntArray(int[] array) {
		return null;
	}

	public static FloatBuffer castPrimitiveFloatArray(float[] array) {
		return null;
	}

	public static byte[] castNativeByteBuffer(ByteBuffer buffer) {
		return null;
	}

	public static int[] castNativeIntBuffer(IntBuffer buffer) {
		return null;
	}

	public static float[] castNativeFloatBuffer(FloatBuffer buffer) {
		return null;
	}

	public static void freeByteBuffer(ByteBuffer byteBuffer) {
		EaglerLWJGLAllocator.freeByteBuffer(byteBuffer);
	}

	public static void freeIntBuffer(IntBuffer intBuffer) {
		EaglerLWJGLAllocator.freeIntBuffer(intBuffer);
	}

	public static void freeFloatBuffer(FloatBuffer floatBuffer) {
		EaglerLWJGLAllocator.freeFloatBuffer(floatBuffer);
	}
	
	public static class NativeNIO {
		
		public static java.nio.ByteBuffer allocateByteBuffer(int length) {
			long ret = JEmalloc.nje_malloc(length);
			if(ret == 0l) {
				throw new OutOfMemoryError("Native je_malloc call returned null pointer!");
			}
			return MemoryUtil.memByteBuffer(ret, length);
		}
		
		public static java.nio.IntBuffer allocateIntBuffer(int length) {
			long ret = JEmalloc.nje_malloc(length << 2);
			if(ret == 0l) {
				throw new OutOfMemoryError("Native je_malloc call returned null pointer!");
			}
			return MemoryUtil.memIntBuffer(ret, length);
		}
		
		public static java.nio.FloatBuffer allocateFloatBuffer(int length) {
			long ret = JEmalloc.nje_malloc(length << 2);
			if(ret == 0l) {
				throw new OutOfMemoryError("Native je_malloc call returned null pointer!");
			}
			return MemoryUtil.memFloatBuffer(ret, length);
		}
	
		public static java.nio.IntBuffer getIntBuffer(java.nio.ByteBuffer byteBuffer) {
			return MemoryUtil.memIntBuffer(MemoryUtil.memAddress(byteBuffer), byteBuffer.capacity() >> 2);
		}
	
		public static java.nio.FloatBuffer getFloatBuffer(java.nio.ByteBuffer byteBuffer) {
			return MemoryUtil.memFloatBuffer(MemoryUtil.memAddress(byteBuffer), byteBuffer.capacity() >> 2);
		}
	
		public static java.nio.ByteBuffer getAsByteBuffer(java.nio.IntBuffer intBuffer) {
			return MemoryUtil.memByteBuffer(MemoryUtil.memAddress(intBuffer), intBuffer.capacity() << 2);
		}
	
		public static java.nio.ByteBuffer getAsByteBuffer(java.nio.FloatBuffer floatBuffer) {
			return MemoryUtil.memByteBuffer(MemoryUtil.memAddress(floatBuffer), floatBuffer.capacity() << 2);
		}
		
		public static void freeByteBuffer(java.nio.ByteBuffer byteBuffer) {
			JEmalloc.nje_free(MemoryUtil.memAddress(byteBuffer));
		}
	
		public static void freeIntBuffer(java.nio.IntBuffer intBuffer) {
			JEmalloc.nje_free(MemoryUtil.memAddress(intBuffer));
		}
	
		public static void freeFloatBuffer(java.nio.FloatBuffer floatBuffer) {
			JEmalloc.nje_free(MemoryUtil.memAddress(floatBuffer));
		}
		
	}
	
	public static boolean isDebugRuntime() {
		return true;
	}
	
	public static void writeCrashReport(String crashDump) {
		File file1 = new File("./crash-reports");
		if(!file1.exists()) {
			if(!file1.mkdirs()) {
				PlatformRuntime.logger.fatal("Could not create crash report directory: {}", file1.getAbsolutePath());
				return;
			}
		}
		File file2 = new File(file1,
				"crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
		try(FileOutputStream os = new FileOutputStream(file2)) {
			os.write(crashDump.getBytes(StandardCharsets.UTF_8));
		}catch(IOException ex) {
			PlatformRuntime.logger.fatal("Could not write crash report: {}", file2.getAbsolutePath());
			PlatformRuntime.logger.fatal(ex);
			return;
		}
		PlatformRuntime.logger.fatal("Crash report was written to: {}", file2.getAbsolutePath());
	}
	
	public static void getStackTrace(Throwable t, Consumer<String> ret) {
		StackTraceElement[] stackTrace = t.getStackTrace();
		for(int i = 0; i < stackTrace.length; ++i) {
			ret.accept(stackTrace[i].toString());
		}
	}
	
	public static boolean printJSExceptionIfBrowser(Throwable t) {
		return false;
	}
	
	public static void exit() {
		System.exit(0);
	}
	
	public static void setThreadName(String string) {
		Thread.currentThread().setName(string);
	}

	public static long maxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	public static long totalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	public static long freeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
	
	public static String getCallingClass(int backTrace) {
		StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
		StackTraceElement stacktraceelement = astacktraceelement[Math.min(backTrace + 1, astacktraceelement.length)];
		return "" + stacktraceelement.getFileName() + ":" + stacktraceelement.getLineNumber();
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
	
	public static void downloadRemoteURIByteArray(String assetPackageURI, final Consumer<byte[]> cb) {
		logger.info("Downloading: {}");
		try(InputStream is = (new URL(assetPackageURI)).openStream()) {
			EaglerOutputStream bao = new EaglerOutputStream();
			byte[] copyBuffer = new byte[16384];
			int i;
			while((i = is.read(copyBuffer, 0, copyBuffer.length)) != -1) {
				bao.write(copyBuffer, 0, i);
			}
			cb.accept(bao.toByteArray());
		}catch(IOException ex) {
			logger.error("Failed to download file!");
			logger.error(ex);
		}
	}
	
	public static boolean requireSSL() {
		return false;
	}
	
	public static boolean isOfflineDownloadURL() {
		return false;
	}
	
	public static IClientConfigAdapter getClientConfigAdapter() {
		return DesktopClientConfigAdapter.instance;
	}

	private static final Random seedProvider = new Random();

	public static long randomSeed() {
		synchronized(seedProvider) {
			return seedProvider.nextLong();
		}
	}

	public static void getWindowXY(int[] x, int[] y) {
		glfwGetWindowPos(windowHandle, x, y);
	}

	public static String currentThreadName() {
		return Thread.currentThread().getName();
	}

	public static long getWindowHandle() {
		return windowHandle;
	}

	public static long steadyTimeMillis() {
		return System.nanoTime() / 1000000l;
	}

	public static long nanoTime() {
		return System.nanoTime();
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	public static void postCreate() {
		
	}

	public static void setDisplayBootMenuNextRefresh(boolean en) {
		
	}

	public static void immediateContinue() {
		// nope
	}

}

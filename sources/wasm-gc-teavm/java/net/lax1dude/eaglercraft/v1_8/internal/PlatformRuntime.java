package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.GZIPInputStream;
import com.jcraft.jzlib.GZIPOutputStream;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.InflaterInputStream;

import net.lax1dude.eaglercraft.v1_8.Filesystem;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.EarlyLoadScreen;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WASMGCClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WebGLBackBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;

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

	static final Logger logger = LogManager.getLogger("RuntimeWASMGC");

	public static Window win = null;
	public static HTMLDocument doc = null;
	public static HTMLElement root = null;
	public static HTMLElement parent = null;
	public static HTMLCanvasElement canvas = null;
	public static boolean webglExperimental = false;

	public static void create() {
		win = Window.current();
		doc = win.getDocument();
		root = getRootElement();
		parent = getParentElement();
		canvas = getCanvasElement();
		PlatformApplication.setMCServerWindowGlobal(null);
		PlatformOpenGL.initContext();
		PlatformInput.initContext(win, parent, canvas);

		// Should contain an event to update the initial screen size
		pollJSEventsAfterSleep();

		WebGLBackBuffer.initBackBuffer(PlatformInput.getWindowWidth(), PlatformInput.getWindowHeight());

		HTMLElement el = parent.querySelector("._eaglercraftX_early_splash_element");
		if(el != null) {
			el.delete();
		}

		EarlyLoadScreen.extractingAssetsScreen();
		sleep(20);

		PlatformAssets.readAssetsTeaVM();

		byte[] finalLoadScreen = PlatformAssets.getResourceBytes("/assets/eagler/eagtek.png");

		if(finalLoadScreen != null) {
			EarlyLoadScreen.loadFinal(finalLoadScreen);
			EarlyLoadScreen.paintFinal(false);
		}else {
			PlatformOpenGL._wglClearColor(1.0f, 0.0f, 1.0f, 1.0f);
			PlatformOpenGL._wglClear(RealOpenGLEnums.GL_COLOR_BUFFER_BIT);
			PlatformInput.update();
		}
		sleep(20);

		EarlyLoadScreen.destroy();

		logger.info("Initializing filesystem...");

		IEaglerFilesystem resourcePackFilesystem = Filesystem.getHandleFor(getClientConfigAdapter().getResourcePacksDB());
		VFile2.setPrimaryFilesystem(resourcePackFilesystem);
		EaglerFolderResourcePack.setSupported(true);

		logger.info("Initializing sound engine...");

		PlatformAudio.initialize();
		PlatformScreenRecord.initContext(win, canvas);

		PlatformWebRTC.initialize();
		PlatformVoiceClient.initialize();
		PlatformWebView.initialize();
	}

	@Import(module = "platformRuntime", name = "getRootElement")
	private static native HTMLElement getRootElement();

	@Import(module = "platformRuntime", name = "getParentElement")
	private static native HTMLElement getParentElement();

	@Import(module = "platformRuntime", name = "getCanvasElement")
	private static native HTMLCanvasElement getCanvasElement();

	public static void destroy() {
		logger.fatal("Game tried to destroy the context! Browser runtime can't do that");
	}

	public static EnumPlatformType getPlatformType() {
		return EnumPlatformType.WASM_GC;
	}

	public static EnumPlatformAgent getPlatformAgent() {
		return EnumPlatformAgent.getFromUA(getUserAgentString());
	}

	@JSBody(params = { }, script = "return navigator.userAgent||null;")
	public static native String getUserAgentString();

	public static EnumPlatformOS getPlatformOS() {
		return EnumPlatformOS.getFromUA(getUserAgentString());
	}

	@JSBody(params = { }, script = "return location.protocol && location.protocol.toLowerCase() === \"https:\";")
	public static native boolean requireSSL();

	@JSBody(params = { }, script = "return location.protocol && location.protocol.toLowerCase() === \"file:\";")
	public static native boolean isOfflineDownloadURL();

	public static void requestANGLE(EnumPlatformANGLE plaf) {
	}

	public static EnumPlatformANGLE getPlatformANGLE() {
		return EnumPlatformANGLE.fromGLRendererString(getGLRenderer());
	}

	public static String getGLVersion() {
		String ret = PlatformOpenGL._wglGetString(RealOpenGLEnums.GL_VERSION);
		if(ret != null) {
			return ret;
		}else {
			return "null";
		}
	}

	public static String getGLRenderer() {
		String ret = PlatformOpenGL._wglGetString(RealOpenGLEnums.GL_RENDERER);
		if(ret != null) {
			return ret;
		}else {
			return "null";
		}
	}

	public static ByteBuffer allocateByteBuffer(int length) {
		return WASMGCBufferAllocator.allocateByteBuffer(length);
	}
	
	public static IntBuffer allocateIntBuffer(int length) {
		return WASMGCBufferAllocator.allocateIntBuffer(length);
	}
	
	public static FloatBuffer allocateFloatBuffer(int length) {
		return WASMGCBufferAllocator.allocateFloatBuffer(length);
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
		WASMGCBufferAllocator.freeByteBuffer(byteBuffer);
	}

	public static void freeIntBuffer(IntBuffer intBuffer) {
		WASMGCBufferAllocator.freeIntBuffer(intBuffer);
	}

	public static void freeFloatBuffer(FloatBuffer floatBuffer) {
		WASMGCBufferAllocator.freeFloatBuffer(floatBuffer);
	}

	private interface JSAsyncDownloadEvent extends JSObject {

		@JSProperty
		int getRequestId();

		@JSProperty
		ArrayBuffer getArrayBuffer();

	}

	private static final int EVENT_TYPE_INPUT = 0;
	private static final int EVENT_TYPE_RUNTIME = 1;
	private static final int EVENT_TYPE_VOICE = 2;
	private static final int EVENT_TYPE_WEBVIEW = 3;

	public interface JSEagRuntimeEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		<T> T getEventObj();

	}

	static void pollJSEvents() {
		int cnt = getEventCount();
		while(cnt-- > 0) {
			JSEagRuntimeEvent evt = getNextEvent();
			if(evt != null) {
				switch(evt.getEventType() >>> 5) {
				case EVENT_TYPE_INPUT:
					PlatformInput.handleJSEvent(evt);
					break;
				case EVENT_TYPE_RUNTIME:
					handleJSEvent(evt);
					break;
				case EVENT_TYPE_VOICE:
					PlatformVoiceClient.handleJSEvent(evt);
					break;
				case EVENT_TYPE_WEBVIEW:
					PlatformWebView.handleJSEvent(evt);
					break;
				default:
					break;
				}
			}else {
				break;
			}
		}
		pollAsyncCallbacksTeaVM();
	}

	private static boolean isWakingUpFromSleep = false;

	static void pollJSEventsAfterSleep() {
		if(!isWakingUpFromSleep) {
			try {
				isWakingUpFromSleep = true;
				pollJSEvents();
			}finally {
				isWakingUpFromSleep = false;
			}
		}
	}

	private static final int EVENT_RUNTIME_ASYNC_DOWNLOAD = 0;

	private static void handleJSEvent(PlatformRuntime.JSEagRuntimeEvent evt) {
		switch(evt.getEventType() & 31) {
			case EVENT_RUNTIME_ASYNC_DOWNLOAD: {
				JSAsyncDownloadEvent obj = evt.getEventObj();
				int id = obj.getRequestId();
				Consumer<ArrayBuffer> handler = waitingAsyncDownloads.get(id);
				if(handler != null) {
					handler.accept(obj.getArrayBuffer());
				}else {
					logger.warn("Ignoring unknown async download result #{}", id);
				}
				break;
			}
		}
	}

	@Import(module = "teavm", name = "pollAsyncCallbacks")
	private static native JSEagRuntimeEvent pollAsyncCallbacksTeaVM();

	@Import(module = "platformRuntime", name = "getEventCount")
	private static native int getEventCount();

	@Import(module = "platformRuntime", name = "getNextEvent")
	private static native JSEagRuntimeEvent getNextEvent();

	private static final IntObjectMap<Consumer<ArrayBuffer>> waitingAsyncDownloads = new IntObjectHashMap<>();
	private static int asyncDownloadID = 0;

	private static void queueAsyncDownload(String uri, boolean forceCache, Consumer<ArrayBuffer> cb) {
		int id = ++asyncDownloadID;
		waitingAsyncDownloads.put(id, cb);
		queueAsyncDownload0(BetterJSStringConverter.stringToJS(uri), forceCache, id);
	}

	public static void downloadRemoteURIByteArray(String assetPackageURI, final Consumer<byte[]> cb) {
		downloadRemoteURIByteArray(assetPackageURI, false, cb);
	}

	public static void downloadRemoteURIByteArray(String assetPackageURI, boolean useCache, final Consumer<byte[]> cb) {
		queueAsyncDownload(assetPackageURI, useCache, arr -> {
			if(arr == null) {
				cb.accept(null);
			}else {
				cb.accept(WASMGCDirectArrayConverter.externU8ArrayToByteArray(new Uint8Array(arr)));
			}
		});
	}

	public static void downloadRemoteURI(String assetPackageURI, Consumer<ArrayBuffer> cb) {
		queueAsyncDownload(assetPackageURI, false, cb);
	}

	public static void downloadRemoteURI(String assetPackageURI, boolean useCache, Consumer<ArrayBuffer> cb) {
		queueAsyncDownload(assetPackageURI, useCache, cb);
	}

	public static byte[] downloadRemoteURIByteArray(String assetPackageURI) {
		return downloadRemoteURIByteArray(assetPackageURI, false);
	}

	public static byte[] downloadRemoteURIByteArray(String assetPackageURI, boolean forceCache) {
		ArrayBuffer arrayBuffer = downloadSync(BetterJSStringConverter.stringToJS(assetPackageURI), forceCache);
		pollJSEventsAfterSleep();
		if(arrayBuffer == null) {
			return null;
		}
		return WASMGCDirectArrayConverter.externU8ArrayToByteArray(new Uint8Array(arrayBuffer));
	}

	public static ArrayBuffer downloadRemoteURI(String assetPackageURI) {
		ArrayBuffer ret = downloadSync(BetterJSStringConverter.stringToJS(assetPackageURI), false);
		pollJSEventsAfterSleep();
		return ret;
	}

	public static ArrayBuffer downloadRemoteURI(String assetPackageURI, boolean forceCache) {
		ArrayBuffer ret = downloadSync(BetterJSStringConverter.stringToJS(assetPackageURI), forceCache);
		pollJSEventsAfterSleep();
		return ret;
	}

	@Import(module = "platformRuntime", name = "queueAsyncDownload")
	private static native void queueAsyncDownload0(JSString uri, boolean forceCache, int id);

	@Import(module = "platformRuntime", name = "download")
	private static native ArrayBuffer downloadSync(JSString uri, boolean forceCache);

	public static boolean isDebugRuntime() {
		return false;
	}

	public static void writeCrashReport(String crashDump) {
		writeCrashReport0(BetterJSStringConverter.stringToJS(crashDump));
	}

	@Import(module = "platformRuntime", name = "writeCrashReport")
	private static native void writeCrashReport0(JSString crashDump);

	public static void getStackTrace(Throwable t, Consumer<String> ret) {
		StackTraceElement[] el = t.getStackTrace();
		if(el.length > 0) {
			for(int i = 0; i < el.length; ++i) {
				ret.accept(el[i].toString());
			}
		}else {
			ret.accept("[no stack trace]");
		}
	}

	public static boolean printJSExceptionIfBrowser(Throwable t) {
		return false;
	}

	private static String currentThreadName = "main";

	public static String currentThreadName() {
		return currentThreadName;
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

	public static String getCallingClass(int i) {
		return null;
	}

	public static long randomSeed() {
		return (long)(Math.random() * 9007199254740991.0);
	}

	public static void exit() {
		logger.fatal("Game is attempting to exit!");
	}

	public static IClientConfigAdapter getClientConfigAdapter() {
		return WASMGCClientConfigAdapter.instance;
	}

	@Import(module = "platformRuntime", name = "steadyTimeMillis")
	static native double steadyTimeMillis0();

	public static long steadyTimeMillis() {
		return (long)steadyTimeMillis0();
	}

	public static long nanoTime() {
		return (long)(steadyTimeMillis0() * 1000000.0);
	}

	public static void sleep(int millis) {
		sleep0(millis);
		pollJSEventsAfterSleep();
	}

	@Import(module = "platformRuntime", name = "sleep")
	private static native void sleep0(int millis);

	public static void immediateContinue() {
		immediateContinue0();
		pollJSEventsAfterSleep();
	}

	@Import(module = "platformRuntime", name = "immediateContinue")
	private static native void immediateContinue0();

	@Import(module = "platformRuntime", name = "immediateContinueSupported")
	public static native boolean immediateContinueSupported();

	public static void postCreate() {
		
	}

	public static void setDisplayBootMenuNextRefresh(boolean en) {
		
	}

	public static OutputStream newDeflaterOutputStream(OutputStream os) throws IOException {
		return new DeflaterOutputStream(os);
	}

	@SuppressWarnings("deprecation")
	public static int deflateFull(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff,
			int outputLen) throws IOException {
		Deflater df = new Deflater();
		df.setInput(input, inputOff, inputLen, false);
		df.setOutput(output, outputOff, outputLen);
		df.init(5);
		int c;
		do {
			c = df.deflate(4);
			if(c != 0 && c != 1) {
				throw new IOException("Deflater failed! Code " + c);
			}
		}while(c != 1);
		return (int)df.getTotalOut();
	}

	public static OutputStream newGZIPOutputStream(OutputStream os) throws IOException {
		return new GZIPOutputStream(os);
	}

	public static InputStream newInflaterInputStream(InputStream is) throws IOException {
		return new InflaterInputStream(is);
	}

	@SuppressWarnings("deprecation")
	public static int inflateFull(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff,
			int outputLen) throws IOException {
		Inflater df = new Inflater();
		df.setInput(input, inputOff, inputLen, false);
		df.setOutput(output, outputOff, outputLen);
		int c;
		do {
			c = df.inflate(0);
			if(c != 0 && c != 1) {
				throw new IOException("Inflater failed! Code " + c);
			}
		}while(c != 1);
		return (int)df.getTotalOut();
	}

	public static InputStream newGZIPInputStream(InputStream is) throws IOException {
		return new GZIPInputStream(is);
	}

}

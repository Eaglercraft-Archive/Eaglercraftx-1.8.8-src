/*
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

package net.lax1dude.eaglercraft.v1_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.internal.EaglerMissingResourceException;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformANGLE;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformAgent;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformOS;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAssets;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.recording.ScreenRecordingController;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;

public class EagRuntime {

	private static final Logger logger = LogManager.getLogger("EagRuntime");
	private static final Logger exceptionLogger = LogManager.getLogger("Exception");
	private static boolean ssl = false;
	private static boolean offlineDownloadURL = false;
	private static EnumPlatformAgent userAgent = null;
	private static String userAgentString = null;
	private static EnumPlatformOS operatingSystem = null;
	private static EnumPlatformANGLE angleBackend = null;
	
	public static String getVersion() {
		return "EagRuntimeX 1.0";
	}
	
	public static void create() {
		logger.info("Version: {}", getVersion());
		PlatformRuntime.create();
		ssl = PlatformRuntime.requireSSL();
		offlineDownloadURL = PlatformRuntime.isOfflineDownloadURL();
		userAgent = PlatformRuntime.getPlatformAgent();
		userAgentString = PlatformRuntime.getUserAgentString();
		operatingSystem = PlatformRuntime.getPlatformOS();
		angleBackend = PlatformRuntime.getPlatformANGLE();
		UpdateService.initialize();
		EaglerXBungeeVersion.initialize();
		EaglercraftGPU.warmUpCache();
		ScreenRecordingController.initialize();
		PlatformRuntime.postCreate();
	}

	public static void destroy() {
		PlatformRuntime.destroy();
	}

	public static EnumPlatformType getPlatformType() {
		return PlatformRuntime.getPlatformType();
	}

	public static EnumPlatformAgent getPlatformAgent() {
		return userAgent;
	}

	public static String getUserAgentString() {
		return userAgentString;
	}

	public static EnumPlatformOS getPlatformOS() {
		return operatingSystem;
	}

	public static EnumPlatformANGLE getPlatformANGLE() {
		return angleBackend;
	}

	public static ByteBuffer allocateByteBuffer(int length) {
		return PlatformRuntime.allocateByteBuffer(length);
	}

	public static IntBuffer allocateIntBuffer(int length) {
		return PlatformRuntime.allocateIntBuffer(length);
	}

	public static FloatBuffer allocateFloatBuffer(int length) {
		return PlatformRuntime.allocateFloatBuffer(length);
	}

	public static void freeByteBuffer(ByteBuffer floatBuffer) {
		PlatformRuntime.freeByteBuffer(floatBuffer);
	}

	public static void freeIntBuffer(IntBuffer intBuffer) {
		PlatformRuntime.freeIntBuffer(intBuffer);
	}

	public static void freeFloatBuffer(FloatBuffer byteBuffer) {
		PlatformRuntime.freeFloatBuffer(byteBuffer);
	}

	public static boolean getResourceExists(String path) {
		return PlatformAssets.getResourceExists(path);
	}

	public static byte[] getResourceBytes(String path) {
		return PlatformAssets.getResourceBytes(path);
	}

	public static byte[] getRequiredResourceBytes(String path) {
		byte[] ret = PlatformAssets.getResourceBytes(path);
		if(ret == null) {
			throw new EaglerMissingResourceException("Could not load required resource from EPK: " + path);
		}
		return ret;
	}

	public static InputStream getResourceStream(String path) {
		byte[] b = PlatformAssets.getResourceBytes(path);
		if(b != null) {
			return new EaglerInputStream(b);
		}else {
			return null;
		}
	}

	public static InputStream getRequiredResourceStream(String path) {
		byte[] ret = PlatformAssets.getResourceBytes(path);
		if(ret == null) {
			throw new EaglerMissingResourceException("Could not load required resource from EPK: " + path);
		}
		return new EaglerInputStream(ret);
	}

	public static String getResourceString(String path) {
		byte[] bytes = PlatformAssets.getResourceBytes(path);
		return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
	}

	public static String getRequiredResourceString(String path) {
		byte[] ret = PlatformAssets.getResourceBytes(path);
		if(ret == null) {
			throw new EaglerMissingResourceException("Could not load required resource from EPK: " + path);
		}
		return new String(ret, StandardCharsets.UTF_8);
	}

	public static List<String> getResourceLines(String path) {
		byte[] bytes = PlatformAssets.getResourceBytes(path);
		if(bytes != null) {
			List<String> ret = new ArrayList<>();
			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(new EaglerInputStream(bytes), StandardCharsets.UTF_8));
				String s;
				while((s = rd.readLine()) != null) {
					ret.add(s);
				}
			}catch(IOException ex) {
				// ??
				return null;
			}
			return ret;
		}else {
			return null;
		}
	}

	public static List<String> getRequiredResourceLines(String path) {
		List<String> ret = getResourceLines(path);
		if(ret == null) {
			throw new EaglerMissingResourceException("Could not load required resource from EPK: " + path);
		}
		return ret;
	}

	public static void debugPrintStackTraceToSTDERR(Throwable t) {
		debugPrintStackTraceToSTDERR0("", t);
		Throwable c = t.getCause();
		while(c != null) {
			debugPrintStackTraceToSTDERR0("Caused by: ", c);
			c = c.getCause();
		}
	}

	private static void debugPrintStackTraceToSTDERR0(String pfx, Throwable t) {
		System.err.println(pfx + t.toString());
		if(!PlatformRuntime.printJSExceptionIfBrowser(t)) {
			getStackTrace(t, (s) -> {
				System.err.println("    at " + s);
			});
		}
	}
	
	public static void getStackTrace(Throwable t, Consumer<String> ret) {
		if(t == null) return;
		PlatformRuntime.getStackTrace(t, ret);
	}
	
	public static String[] getStackTraceElements(Throwable t) {
		if(t == null) return new String[0];
		List<String> lst = new ArrayList<>();
		PlatformRuntime.getStackTrace(t, (s) -> {
			lst.add(s);
		});
		return lst.toArray(new String[lst.size()]);
	}
	
	public static String getStackTrace(Throwable t) {
		if(t == null) {
			return "[null]";
		}
		StringBuilder sb = new StringBuilder();
		getStackTrace0(t, sb);
		Throwable c = t.getCause();
		while(c != null) {
			sb.append("\nCaused by: ");
			getStackTrace0(c, sb);
			c = c.getCause();
		}
		return sb.toString();
	}
	
	private static void getStackTrace0(Throwable t, StringBuilder sb) {
		sb.append(t.toString());
		getStackTrace(t, (s) -> {
			sb.append('\n').append("    at ").append(s);
		});
	}
	
	public static void debugPrintStackTrace(Throwable t) {
		exceptionLogger.error(t);
	}
	
	public static void dumpStack() {
		try {
			throw new Exception("Stack Trace");
		}catch(Exception ex) {
			exceptionLogger.error(ex);
		}
	}
	
	public static void exit() {
		PlatformRuntime.exit();
	}

	/**
	 * Note to skids: This doesn't do anything in javascript runtime!
	 */
	public static long maxMemory() {
		return PlatformRuntime.maxMemory();
	}

	/**
	 * Note to skids: This doesn't do anything in javascript runtime!
	 */
	public static long totalMemory() {
		return PlatformRuntime.totalMemory();
	}

	/**
	 * Note to skids: This doesn't do anything in javascript runtime!
	 */
	public static long freeMemory() {
		return PlatformRuntime.freeMemory();
	}

	public static boolean requireSSL() {
		return ssl;
	}

	public static boolean isOfflineDownloadURL() {
		return offlineDownloadURL;
	}

	public static void showPopup(String msg) {
		PlatformApplication.showPopup(msg);
	}

	public static String getClipboard() {
		return PlatformApplication.getClipboard();
	}

	public static void setClipboard(String text) {
		PlatformApplication.setClipboard(text);
	}

	public static void openLink(String url) {
		PlatformApplication.openLink(url);
	}

	public static void displayFileChooser(String mime, String ext) {
		PlatformApplication.displayFileChooser(mime, ext);
	}

	public static boolean fileChooserHasResult() {
		return PlatformApplication.fileChooserHasResult();
	}

	public static FileChooserResult getFileChooserResult() {
		return PlatformApplication.getFileChooserResult();
	}

	public static void clearFileChooserResult() {
		PlatformApplication.clearFileChooserResult();
	}

	public static void setStorage(String name, byte[] data) {
		PlatformApplication.setLocalStorage(name, data);
	}

	public static byte[] getStorage(String data) {
		return PlatformApplication.getLocalStorage(data);
	}

	public static IClientConfigAdapter getConfiguration() {
		return PlatformRuntime.getClientConfigAdapter();
	}

	public static void openCreditsPopup(String text) {
		PlatformApplication.openCreditsPopup(text);
	}

	public static void downloadFileWithName(String fileName, byte[] fileContents) {
		PlatformApplication.downloadFileWithName(fileName, fileContents);
	}

	public static String currentThreadName() {
		return PlatformRuntime.currentThreadName();
	}

	public static void showDebugConsole() {
		PlatformApplication.showDebugConsole();
	}

	public static void setDisplayBootMenuNextRefresh(boolean en) {
		PlatformRuntime.setDisplayBootMenuNextRefresh(en);
	}

	public static void setMCServerWindowGlobal(String url) {
		PlatformApplication.setMCServerWindowGlobal(url);
	}

	public static long steadyTimeMillis() {
		return PlatformRuntime.steadyTimeMillis();
	}

	public static long nanoTime() {
		return PlatformRuntime.nanoTime();
	}

	public static void immediateContinue() {
		PlatformRuntime.immediateContinue();
	}

	public static boolean immediateContinueSupported() {
		return PlatformRuntime.immediateContinueSupported();
	}

}
package net.lax1dude.eaglercraft.v1_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
public class EagRuntime {

	private static final Logger logger = LogManager.getLogger("EagRuntime");
	private static final Logger exceptionLogger = LogManager.getLogger("Exception");
	private static boolean ssl = false;
	
	public static String getVersion() {
		return "EagRuntimeX 1.0";
	}
	
	public static void create() {
		logger.info("Version: {}", getVersion());
		PlatformRuntime.create();
		ssl = PlatformRuntime.requireSSL();
		EaglercraftGPU.warmUpCache();
	}

	public static void destroy() {
		PlatformRuntime.destroy();
	}

	public static EnumPlatformType getPlatformType() {
		return PlatformRuntime.getPlatformType();
	}

	public static EnumPlatformAgent getPlatformAgent() {
		return PlatformRuntime.getPlatformAgent();
	}
	
	public static String getUserAgentString() {
		return PlatformRuntime.getUserAgentString();
	}

	public static EnumPlatformOS getPlatformOS() {
		return PlatformRuntime.getPlatformOS();
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
	
	public static byte[] getResourceBytes(String path) {
		return PlatformAssets.getResourceBytes(path);
	}
	
	public static InputStream getResourceStream(String path) {
		byte[] b = PlatformAssets.getResourceBytes(path);
		if(b != null) {
			return new EaglerInputStream(b);
		}else {
			return null;
		}
	}
	
	public static String getResourceString(String path) {
		byte[] bytes = PlatformAssets.getResourceBytes(path);
		return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
	}
	
	public static List<String> getResourceLines(String path) {
		byte[] bytes = PlatformAssets.getResourceBytes(path);
		if(bytes != null) {
			List<String> ret = new ArrayList();
			try {
				BufferedReader rd = new BufferedReader(new StringReader(path));
				String s;
				while((s = rd.readLine()) != null) {
					ret.add(s);
				}
			}catch(IOException ex) {
				// ??
			}
			return ret;
		}else {
			return null;
		}
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
		PlatformRuntime.getStackTrace(t, ret);
	}
	
	public static String[] getStackTraceElements(Throwable t) {
		List<String> lst = new ArrayList();
		PlatformRuntime.getStackTrace(t, (s) -> {
			lst.add(s);
		});
		return lst.toArray(new String[lst.size()]);
	}
	
	public static String getStackTrace(Throwable t) {
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

	public static long maxMemory() {
		return PlatformRuntime.maxMemory();
	}

	public static long totalMemory() {
		return PlatformRuntime.totalMemory();
	}

	public static long freeMemory() {
		return PlatformRuntime.freeMemory();
	}
	
	public static boolean requireSSL() {
		return ssl;
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

	public static void setStorage(String name, byte[] data) {
		PlatformApplication.setLocalStorage(name, data);
	}

	public static byte[] getStorage(String data) {
		return PlatformApplication.getLocalStorage(data);
	}

	public static IClientConfigAdapter getConfiguration() {
		return PlatformRuntime.getClientConfigAdapter();
	}

	public static String getRecText() {
		return PlatformRuntime.getRecText();
	}

	public static void toggleRec() {
		PlatformRuntime.toggleRec();
	}

	public static boolean recSupported() {
		return PlatformRuntime.recSupported();
	}

	public static void openCreditsPopup(String text) {
		PlatformApplication.openCreditsPopup(text);
	}
}

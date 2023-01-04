package net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries;

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
public class TeaVMBridge {

	private static URLClassLoader classLoader = null;

	/**
	 * <h3>List of required options:</h3>
	 * <table>
	 * <tr><td><b>classPathEntries</b></td><td>-&gt; BuildStrategy.setClassPathEntries(List&lt;String&gt;)</td></tr>
	 * <tr><td><b>entryPointName</b></td><td>-&gt; BuildStrategy.setEntryPointName(String)</td></tr>
	 * <tr><td><b>mainClass</b></td><td>-&gt; BuildStrategy.setMainClass(String)</td></tr>
	 * <tr><td><b>minifying</b></td><td>-&gt; BuildStrategy.setMinifying(boolean)</td></tr>
	 * <tr><td><b>optimizationLevel</b></td><td>-&gt; BuildStrategy.setOptimizationLevel(TeaVMOptimizationLevel)</td></tr>
	 * <tr><td><b>generateSourceMaps</b></td><td>-&gt; BuildStrategy.setSourceMapsFileGenerated(boolean)</td></tr>
	 * <tr><td><b>targetDirectory</b></td><td>-&gt; BuildStrategy.setTargetDirectory(String)</td></tr>
	 * <tr><td><b>targetFileName</b></td><td>-&gt; BuildStrategy.setTargetFileName(String)</td></tr>
	 * </table>
	 * <br>
	 */
	public static boolean compileTeaVM(Map<String, Object> options) throws TeaVMClassLoadException, TeaVMRuntimeException {
		File[] cp = TeaVMBinaries.getTeaVMCompilerClasspath();
		URL[] urls = new URL[cp.length];
		
		for(int i = 0; i < cp.length; ++i) {
			try {
				urls[i] = cp[i].toURI().toURL();
			} catch (MalformedURLException e) {
				throw new TeaVMClassLoadException("Could not resolve URL for: " + cp[i].getAbsolutePath(), e);
			}
		}
		
		Method found = null;
		
		try {
			if(classLoader == null) {
				classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
			}
			Class c = classLoader.loadClass("net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridgeImpl");
			Method[] methods = c.getDeclaredMethods();
			for(int i = 0; i < methods.length; ++i) {
				Method m = methods[i];
				if(m.getName().equals("compileTeaVM")) {
					found = m;
				}
			}
			if(found == null) {
				throw new NoSuchMethodException("compileTeaVM");
			}
		}catch(TeaVMClassLoadException | NoSuchMethodException | ClassNotFoundException t) {
			throw new TeaVMClassLoadException("Could not link TeaVM compiler!", t);
		}catch(RuntimeException t) {
			String msg = t.getMessage();
			if(msg.startsWith("[TeaVMBridge]")) {
				throw new TeaVMRuntimeException(msg.substring(13).trim(), t.getCause());
			}else {
				throw new TeaVMRuntimeException("Uncaught exception was thrown!", t);
			}
		}catch(Throwable t) {
			throw new TeaVMRuntimeException("Uncaught exception was thrown!", t);
		}
		
		try {
			Object ret = found.invoke(null, options);
			return ret != null && (ret instanceof Boolean) && ((Boolean)ret).booleanValue();
		}catch(InvocationTargetException ex) {
			throw new TeaVMRuntimeException("Uncaught exception was thrown!", ex.getCause());
		} catch (Throwable t) {
			throw new TeaVMRuntimeException("Failed to invoke 'compileTeaVM'!", t);
		}
		
	}

	public static class TeaVMClassLoadException extends RuntimeException {
		public TeaVMClassLoadException(String message, Throwable cause) {
			super(message, cause);
		}
		public TeaVMClassLoadException(String message) {
			super(message);
		}
	}

	public static class TeaVMRuntimeException extends RuntimeException {
		public TeaVMRuntimeException(String message, Throwable cause) {
			super(message, cause);
		}
		public TeaVMRuntimeException(String message) {
			super(message);
		}
	}

	public static void free() {
		if(classLoader != null) {
			try {
				classLoader.close();
				classLoader = null;
			} catch (IOException e) {
				System.err.println("Memory leak, failed to release TeaVM JAR ClassLoader!");
				e.printStackTrace();
			}
		}
	}

}

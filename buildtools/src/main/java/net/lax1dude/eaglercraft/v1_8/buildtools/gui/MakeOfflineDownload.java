package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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
public class MakeOfflineDownload {

	private static File currentJarFile = null;
	private static URLClassLoader classLoader = null;
	private static Method mainMethod = null;

	public static void compilerMain(File jarFile, String[] args) throws InvocationTargetException {
		if(currentJarFile != null && !currentJarFile.equals(jarFile)) {
			throw new IllegalArgumentException("Cannot load two different MakeOfflineDownload versions into the same runtime");
		}
		if(mainMethod == null) {
			currentJarFile = jarFile;
			try {
				if(classLoader == null) {
					classLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() }, ClassLoader.getSystemClassLoader());
				}
				Class epkCompilerMain = classLoader.loadClass("net.lax1dude.eaglercraft.v1_8.buildtools.workspace.MakeOfflineDownload");
				mainMethod = epkCompilerMain.getDeclaredMethod("main", String[].class);
			} catch (MalformedURLException | SecurityException e) {
				throw new IllegalArgumentException("Illegal MakeOfflineDownload JAR path!", e);
			} catch (ClassNotFoundException | NoSuchMethodException e) {
				throw new IllegalArgumentException("MakeOfflineDownload JAR does not contain main class: 'net.lax1dude.eaglercraft.v1_8.buildtools.workspace.MakeOfflineDownload'", e);
			}
		}
		try {
			mainMethod.invoke(null, new Object[] { args });
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalArgumentException("MakeOfflineDownload JAR does not contain valid 'main' method", e);
		}
	}

	public static void free() {
		if(classLoader != null) {
			try {
				classLoader.close();
				classLoader = null;
			} catch (IOException e) {
				System.err.println("Memory leak, failed to release MakeOfflineDownload ClassLoader!");
				e.printStackTrace();
			}
		}
	}

}

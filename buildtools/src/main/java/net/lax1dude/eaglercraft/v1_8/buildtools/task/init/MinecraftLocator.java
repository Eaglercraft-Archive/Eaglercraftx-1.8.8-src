package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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
public class MinecraftLocator {
	
	private static boolean hasTriedToFind = false;
	private static File directory = null;
	
	private static File locateOrCopyFile(String name, String copyPath) {
		File f = new File("./mcp918/" + name);
		if(f.isFile()) {
			return f;
		}
		if(!hasTriedToFind) {
			hasTriedToFind = true;
			String var0 = System.getProperty("os.name").toLowerCase();
			if(var0.contains("win")) {
				String ad = System.getenv("APPDATA");
				if(ad != null) {
					directory = new File(ad, ".minecraft");
				}else {
					directory = new File(System.getProperty("user.home"), ".minecraft");
				}
			}else if(var0.contains("mac")) {
				directory = new File(System.getProperty("user.home"), "Library/Application Support/minecraft");
			}else {
				directory = new File(System.getProperty("user.home"), ".minecraft");
			}
			if(!directory.isDirectory()) {
				directory = new File(System.getProperty("user.home"), "minecraft");
				if(!directory.isDirectory()) {
					directory = null;
				}
			}
		}
		if(directory == null) {
			return null;
		}else {
			File f2 = new File(directory, copyPath);
			if(f2.isFile()) {
				try {
					System.out.println("Copying '" + copyPath + "' from your .minecraft directory into './mcp918'...");
					FileUtils.copyFile(f2, f, true);
					return f;
				} catch (IOException e) {
					System.err.println("ERROR: failed to copy '" + copyPath + "' from your .minecraft directory into './mcp918'!");
					e.printStackTrace();
					return null;
				}
			}else {
				return null;
			}
		}
	}

	public static File locateMinecraftVersionJar(String name) {
		return locateOrCopyFile(name + ".jar", "versions/" + name + "/" + name + ".jar");
	}

	public static File locateMinecraftVersionAssets(String name) {
		return locateOrCopyFile(name + ".json", "assets/indexes/" + name + ".json");
	}
	
}

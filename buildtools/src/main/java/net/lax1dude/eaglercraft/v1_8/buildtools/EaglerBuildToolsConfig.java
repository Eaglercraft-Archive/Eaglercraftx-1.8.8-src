package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

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
public class EaglerBuildToolsConfig {

	public static File temporary_directory = new File(System.getProperty("user.home"), ".eaglercraft_1.8_buildtools");
	private static boolean temporary_directory_isInit = false;
	private static boolean temporary_directory_mentioned = false;
	
	public static File workspace_directory = new File("../eaglercraft_1.8_workspace");
	private static boolean workspace_directory_isInit = false;
	private static boolean workspace_directory_mentioned = false;
	
	private static boolean config_file_loaded = false;
	
	public static final File configFile = new File("./buildtools_config.json");
	
	public static void load() {
		if(configFile.exists()) {
			try(FileInputStream is = new FileInputStream(configFile)) {
				byte[] r = new byte[(int)configFile.length()];
				is.read(r);
				is.close();
				String jsonTxt = new String(r, StandardCharsets.UTF_8);
				JSONObject obj = new JSONObject(jsonTxt);
				String path = obj.optString("temporary_directory", null);
				if(path != null) {
					temporary_directory = new File(path);
					temporary_directory_isInit = true;
				}
				path = obj.optString("workspace_directory", null);
				if(path != null) {
					workspace_directory = new File(path);
					workspace_directory_isInit = true;
				}
			}catch(Throwable ex) {
				System.err.println("Failed to read config!");
				ex.printStackTrace();
			}
		}
	}

	public static void save() {
		JSONObject obj = new JSONObject();
		if(temporary_directory_isInit) obj.put("temporary_directory", temporary_directory.getAbsolutePath());
		if(workspace_directory_isInit) obj.put("workspace_directory", workspace_directory.getAbsoluteFile());
		try(FileOutputStream os = new FileOutputStream(configFile)) {
			os.write(obj.toString(4).getBytes(StandardCharsets.UTF_8));
			os.close();
		}catch(IOException e) {
			System.err.println("Failed to write config!");
			e.printStackTrace();
		}
	}
	
	private static void mentionConfigPath() {
		System.out.println("Edit '" + configFile.getName() + "' to change");
	}
	
	public static File getTemporaryDirectory() {
		if(!config_file_loaded) {
			load();
			config_file_loaded = true;
		}
		if(!temporary_directory_isInit) {
			File f = temporary_directory;
			System.out.println();
			System.out.println("Using temporary directory: " + f.getAbsolutePath());
			temporary_directory_mentioned = true;
			f = askIfChangeIsWanted(f);
			temporary_directory = f;
			temporary_directory_isInit = true;
			while(!temporary_directory.isDirectory() && !temporary_directory.mkdirs()) {
				System.err.println("Failed to create: " + f.getAbsolutePath());
				temporary_directory = askIfChangeIsWanted(f);
			}
			save();
			System.out.println();
			return temporary_directory;
		}else {
			if(!temporary_directory_mentioned) {
				System.out.println("Using temporary directory: " + temporary_directory.getAbsolutePath());
				temporary_directory_mentioned = true;
				while(!temporary_directory.isDirectory() && !temporary_directory.mkdirs()) {
					System.err.println("Failed to create: " + temporary_directory.getAbsolutePath());
					temporary_directory = askIfChangeIsWanted(temporary_directory);
				}
				mentionConfigPath();
			}
			return temporary_directory;
		}
	}
	
	public static File getWorkspaceDirectory() {
		if(!config_file_loaded) {
			load();
			config_file_loaded = true;
		}
		if(!workspace_directory_isInit) {
			File f = workspace_directory;
			System.out.println();
			System.out.println("Using workspace directory: " + f.getAbsolutePath());
			workspace_directory_mentioned = true;
			f = askIfChangeIsWanted(f);
			workspace_directory = f;
			workspace_directory_isInit = true;
			while(!workspace_directory.isDirectory() && !workspace_directory.mkdirs()) {
				System.err.println("Failed to create: " + f.getAbsolutePath());
				workspace_directory = askIfChangeIsWanted(f);
			}
			save();
			System.out.println();
			return workspace_directory;
		}else {
			if(!workspace_directory_mentioned) {
				System.out.println("Using workspace directory: " + workspace_directory.getAbsolutePath());
				workspace_directory_mentioned = true;
				while(!workspace_directory.isDirectory() && !workspace_directory.mkdirs()) {
					System.err.println("Failed to create: " + workspace_directory.getAbsolutePath());
					workspace_directory = askIfChangeIsWanted(workspace_directory);
				}
				mentionConfigPath();
			}
			return workspace_directory;
		}
	}
	
	public static File askIfChangeIsWanted(File in) {
		System.out.println("Would you like to change this directory?");
		System.out.println("Enter 'Y' for yes or 'N' for no: ");
		String l = "N";
		
		try {
			l = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		} catch (IOException e) {
		}
		
		if(l != null && ((l = l.trim()).equalsIgnoreCase("y") || l.equalsIgnoreCase("yes"))) {
			System.out.println();
			System.out.println("Type a new filename or hit 'Enter' to browse: ");
			try {
				l = (new BufferedReader(new InputStreamReader(System.in))).readLine();
			} catch (IOException e) {
			}
			if(l != null && (l = l.trim()).length() > 0) {
				in = new File(l);
			}else {
				File f = FileChooserTool.load(true);
				if(f == null) {
					System.out.println("You hit cancel on the file chooser, the directory '" + in.getAbsolutePath() + "' will be used.");
					in = askIfChangeIsWanted(in);
				}else {
					in = f;
				}
			}
		}
		
		return in;
	}
	
}

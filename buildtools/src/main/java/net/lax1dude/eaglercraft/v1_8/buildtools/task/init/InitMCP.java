package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

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
public class InitMCP {
	
	public static boolean initTask(File f, File mcpDataTMP) throws Throwable {
		File mcpUnifiedJar = new File(mcpDataTMP, "runtime.jar");
		String[] jarsToUnify = new String[] { "mcinjector.jar", "specialsource.jar" }; //, "retroguard.jar" };
		boolean[] jarsFound = new boolean[jarsToUnify.length];
		String[] configToCopy = new String[] { "exceptor.json", "fields.csv", "joined.exc",
				"joined.srg", "methods.csv", "params.csv", "fernflower.jar" };
		boolean[] configFound = new boolean[configToCopy.length];
		Set<String> copiedFiles = new HashSet();
		
		System.out.println();
		System.out.println("Extracting \"" + f.getAbsolutePath() + "\" to \"" + mcpDataTMP.getAbsolutePath() + "\"...");
		
		try(ZipInputStream is = new ZipInputStream(new FileInputStream(f)); 
			ZipOutputStream os = new ZipOutputStream(new FileOutputStream(mcpUnifiedJar))) {
		
			os.setLevel(0);
			os.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			os.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
			
			ZipEntry e;
			entry_read: while((e = is.getNextEntry()) != null) {
				String zn = e.getName();
				if(zn.startsWith("/")) {
					zn = zn.substring(1);
				}
				for(int ii = 0; ii < jarsToUnify.length; ++ii) {
					if(zn.endsWith(jarsToUnify[ii])) {
						System.out.println("   " + jarsToUnify[ii] + " -> " + mcpUnifiedJar.getName());
						ZipInputStream iis = new ZipInputStream(is);
						ZipEntry e2;
						while((e2 = iis.getNextEntry()) != null) {
							if(e2.isDirectory()) {
								continue;
							}
							String n = e2.getName();
							int i = n.indexOf("META-INF");
							if(i == 0 || i == 1) {
								continue;
							}
							if(copiedFiles.add(n)) {
								ZipEntry e3 = new ZipEntry(e2.getName());
								os.putNextEntry(e3);
								IOUtils.copy(iis, os, 4096);
							}
						}
						jarsFound[ii] = true;
						continue entry_read;
					}
				}
				
				for(int ii = 0; ii < configToCopy.length; ++ii) {
					if(zn.endsWith(configToCopy[ii])) {
						System.out.println("   " + configToCopy[ii] + " -> " + configToCopy[ii]);
						try(OutputStream oss = new FileOutputStream(new File(mcpDataTMP, configToCopy[ii]))) {
							IOUtils.copy(is, oss, 32768);
						}
						configFound[ii] = true;
						continue entry_read;
					}
				}
			}
		}catch(IOException ex) {
			System.err.println("ERROR: failed to extract \"" + f.getAbsolutePath() + "\" to \"" + mcpDataTMP.getAbsolutePath() + "\"!");
			ex.printStackTrace();
			return false;
		}
		
		boolean err = false;
		for(int ii = 0; ii < jarsToUnify.length; ++ii) {
			if(!jarsFound[ii]) {
				err = true;
				System.err.println("JAR not found: \"" + jarsToUnify[ii] + "\"!");
			}
		}
		for(int ii = 0; ii < configToCopy.length; ++ii) {
			if(!configFound[ii]) {
				err = true;
				System.err.println("Config not found: \"" + configToCopy[ii] + "\"!");
			}
		}
		
		if(err) {
			System.err.println("ERROR: Could not extract all required MCP files from \"" + f.getName() + "\"!");
			return false;
		}
		
		CSVMappings mappings = new CSVMappings();
		
		File srgsOut = new File(mcpDataTMP, "minecraft.srg");
		if(!GenerateSRGs.generate(mcpDataTMP, srgsOut, mappings)) {
			System.err.println("ERROR: could not generate joined \"minecraft.srg\" file from conf in \"" + mcpDataTMP.getAbsolutePath() + "\"!");
			return false;
		}
		
		File excsOut = new File(mcpDataTMP, "minecraft.exc");
		if(!GenerateEXCs.generateEXCs(mcpDataTMP, excsOut, mappings)) {
			System.err.println("ERROR: could not generate joined \"minecraft.exc\" file from conf in \"" + mcpDataTMP.getAbsolutePath() + "\"!");
			return false;
		}
		
		return true;
	}
	
}

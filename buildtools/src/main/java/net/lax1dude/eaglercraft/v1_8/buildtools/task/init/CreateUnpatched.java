package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;

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
public class CreateUnpatched {

	public static boolean createUnpatched() {
		try {
			return createUnpatched0();
		}catch(Throwable t) {
			System.err.println();
			System.err.println("Exception encountered while running task 'unpatched'!");
			t.printStackTrace();
			return false;
		}
		
	}

	private static boolean createUnpatched0() throws Throwable {
		File tmpDirectory = EaglerBuildToolsConfig.getTemporaryDirectory();
		File mcpDir = new File(tmpDirectory, "ModCoderPack");
		File minecraftSrc = new File(tmpDirectory, "MinecraftSrc/minecraft_src.jar");
		File minecraftRes = new File(tmpDirectory, "MinecraftSrc/minecraft_res.jar");
		File outputFile = new File("./MinecraftSrc.zip");
		
		if(outputFile.exists()) {
			System.err.println("ERROR: The file 'MinecraftSrc.zip' already exists in this directory!");
			System.err.println("Delete it and re-run 'unpatched' to try again");
			return false;
		}
		
		if(!mcpDir.isDirectory()) {
			System.err.println("The '" + mcpDir.getName() + "' directory was not found in the temporary directory!");
			System.err.println("Please run the 'init' command to create it");
			return false;
		}
		
		if(!minecraftSrc.isFile()) {
			System.err.println("The '" + minecraftSrc.getName() + "' file was not found in the temporary directory!");
			System.err.println("Please run the 'init' command to create it");
			return false;
		}
		
		if(!minecraftRes.isFile()) {
			System.err.println("The '" + minecraftRes.getName() + "' file was not found in the temporary directory!");
			System.err.println("Please run the 'init' command to create it");
			return false;
		}
		
		File tmpJavadocOut = new File(tmpDirectory, "MinecraftSrc/minecraft_unpatched_javadoc.jar");
		System.out.println();
		System.out.println("Preparing source in '" + minecraftSrc.getName() + "'...");
		System.out.println();
		CSVMappings mp = new CSVMappings();
		InsertJavaDoc.processSource(minecraftSrc, tmpJavadocOut, mcpDir, mp, false);
		
		try(ZipOutputStream zot = new ZipOutputStream(new FileOutputStream(outputFile))) {
			zot.setLevel(0);
			int tl;
			System.out.println("Extracting '" + tmpJavadocOut.getName() + "' into '" + outputFile.getName() + "'...");
			try(FileInputStream fin = new FileInputStream(tmpJavadocOut)) {
				tl = extractZipTo(new ZipInputStream(fin), zot, "src");
			}
			System.out.println("Extracted " + tl + " files.");
			System.out.println();
			System.out.println("Extracting '" + minecraftRes.getName() + "' into '" + outputFile.getName() + "'...");
			try(FileInputStream fin = new FileInputStream(minecraftRes)) {
				tl = extractZipTo(new ZipInputStream(fin), zot, "res");
			}
			System.out.println("Extracted " + tl + " files.");
		}
		
		if(!tmpJavadocOut.delete()) {
			System.err.println();
			System.err.println("ERROR: failed to delete '" + tmpJavadocOut.getName() + "' from temporary directory!");
		}
		
		return true;
		
	}
	
	private static int extractZipTo(ZipInputStream zin, ZipOutputStream zout, String pfx) throws IOException {
		int cnt = 0;
		ZipEntry in;
		while((in = zin.getNextEntry()) != null) {
			if(in.isDirectory()) {
				continue;
			}
			String n = in.getName();
			if(n.startsWith("/")) {
				n = n.substring(1);
			}
			if(n.startsWith("META-INF")) {
				continue;
			}
			ZipEntry out = new ZipEntry(pfx + "/" + n);
			zout.putNextEntry(out);
			IOUtils.copy(zin, zout, 8192);
			++cnt;
		}
		return cnt;
	}
	
}

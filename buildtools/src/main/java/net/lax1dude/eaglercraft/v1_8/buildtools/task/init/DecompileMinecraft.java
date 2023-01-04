package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.diff.ApplyPatchesToZip;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.formatter.EclipseFormatter;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.JARSubprocess;

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
public class DecompileMinecraft {

	public static boolean decompileMinecraft(File mcpDataTMP, File minecraftJar, File minecraftSrc, File assetsJson, boolean writeJavaDoc) throws Throwable {
		
		File filterOut = new File(minecraftSrc, "minecraft_classes.jar");
		System.out.println();
		System.out.println("Extracting '" + minecraftJar.getAbsolutePath() + "\" to \"" + filterOut.getAbsolutePath() + "\"...");
		
		int xt = 0;
		try(ZipInputStream jarIn = new ZipInputStream(new FileInputStream(minecraftJar)); 
				ZipOutputStream jarOut = new ZipOutputStream(new FileOutputStream(filterOut))) {
			jarOut.setLevel(0);
			jarOut.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			jarOut.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
			ZipEntry et;
			String nm;
			while((et = jarIn.getNextEntry()) != null) {
				if(!et.isDirectory() && (nm = et.getName()).endsWith(".class")) {
					ZipEntry z2 = new ZipEntry(nm);
					jarOut.putNextEntry(z2);
					IOUtils.copy(jarIn, jarOut);
					++xt;
				}
			}
		}catch(IOException ex) {
			System.err.println("ERROR: failed to extract \"" + minecraftJar.getAbsolutePath() + "\" to \"" + filterOut.getAbsolutePath() + "\"!");
			ex.printStackTrace();
			if(filterOut.exists()) {
				filterOut.delete();
			}
			return false;
		}
		System.out.println("Extracted " + xt + " class files.");

		File deobfOut = new File(minecraftSrc, "minecraft_specialsource.jar");
		
		System.out.println();
		System.out.println("Running SpecialSource...");
		int ex = JARSubprocess.runJava(mcpDataTMP, new String[] {
				"-cp", filterOut.getAbsolutePath() + JARSubprocess.classPathSeperator + "runtime.jar",
				"net.md_5.specialsource.SpecialSource", "-i", filterOut.getAbsolutePath(), "-o",
				deobfOut.getAbsolutePath(), "-m", "minecraft.srg", "--kill-source"
		}, "   [SpecialSource]");
		
		filterOut.delete();
		
		if(ex == 0) {
			System.out.println("SpecialSource completed successfully.");
		}else {
			System.err.println("ERROR: MCP SpecialSource execution failed!");
			return false;
		}
		System.out.println();

		File deobfOut2 = new File(minecraftSrc, "minecraft_mcinjector.jar");
		
		System.out.println("Running MCInjector...");
		ex = JARSubprocess.runJava(mcpDataTMP, new String[] {
				"-cp", filterOut.getAbsolutePath() + JARSubprocess.classPathSeperator + "runtime.jar",
				"de.oceanlabs.mcp.mcinjector.MCInjector", "--jarIn", deobfOut.getAbsolutePath(), "--jarOut",
				deobfOut2.getAbsolutePath(), "--mapIn", "minecraft.exc", "--jsonIn", "exceptor.json",
				"--lvt", "STRIP"
		}, "   [MCInjector]");
		
		deobfOut.delete();
		
		if(ex == 0) {
			System.out.println("MCInjector completed successfully.");
		}else {
			System.err.println("ERROR: MCP MCInjector execution failed!");
			return false;
		}
		System.out.println();
		
		File ffOut = new File(minecraftSrc, "fernflower.tmp");
		
		if(ffOut.isFile()) {
			ffOut.delete();
		}else if(ffOut.isDirectory()) {
			FileUtils.deleteDirectory(ffOut);
		}
		
		if(!ffOut.mkdir()) {
			System.err.println("ERROR: Could not create Fernflower output directory!");
			return false;
		}
		
		System.out.println("Decompiling with Fernflower...");
		System.out.println("This will take a while, go get a drink or something lol.");
		System.out.println();
		
		System.out.println("Staying hydrated is important when u work on a fucked up project that");
		System.out.println("will make you angry enough put your fists through your bedroom wall");
		System.out.println();
		
		ex = JARSubprocess.runJava(mcpDataTMP, new String[] {
				"-jar", "fernflower.jar", "-din=1", "-rbr=1", "-dgs=1", "-asc=1", "-rsy=1", "-iec=1",
				"-ren=0", "-jvn=1", "-udv=1", "-ump=1", "-log=WARN", deobfOut2.getAbsolutePath(),
				ffOut.getAbsolutePath()
		}, "   [Fernflower]");
		
		deobfOut2.delete();
		
		if(ex == 0) {
			System.out.println("Decompiler completed successfully.");
		}else {
			System.err.println("ERROR: Fernflower decompiler failed!");
			return false;
		}
		System.out.println();
		
		File[] ff = ffOut.listFiles();
		File decomp = null;
		for(int i = 0; i < ff.length; ++i) {
			if(ff[i].getName().endsWith(".jar")) {
				if(ff[i].getName().equalsIgnoreCase("minecraft_mcinjector.jar")) {
					decomp = ff[i];
				}else {
					if(decomp == null) {
						decomp = ff[i];
					}
				}
			}
		}
		
		if(decomp == null) {
			System.err.println("Could not find Fernflower output jar! (in " + ffOut.getAbsolutePath() + ")");
			return false;
		}
		
		File formatOut = new File(minecraftSrc, "minecraft_src.jar");

		System.out.println("Formatting source for patches...");
		System.out.println("   (Using default Eclipse format)");
		System.out.print("   ");
		xt = 0;
		try(ZipInputStream jarIn = new ZipInputStream(new FileInputStream(decomp)); 
				ZipOutputStream jarOut = new ZipOutputStream(new FileOutputStream(formatOut))) {
			jarOut.setLevel(5);
			jarOut.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			jarOut.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
			ZipEntry et;
			String nm;
			while((et = jarIn.getNextEntry()) != null) {
				if((nm = et.getName()).endsWith(".java")) {
					String txt = IOUtils.toString(jarIn, "UTF-8");
					txt = EclipseFormatter.processSource(txt, "\n");
					ZipEntry z2 = new ZipEntry(nm);
					jarOut.putNextEntry(z2);
					IOUtils.write(txt, jarOut, "UTF-8");
					++xt;
					if(xt % 75 == 74) {
						System.out.print(".");
					}
				}else {
					if(!nm.startsWith("META-INF")) {
						ZipEntry z2 = new ZipEntry(nm);
						jarOut.putNextEntry(z2);
						IOUtils.copy(jarIn, jarOut, 4096);
					}
				}
			}
		}
		
		System.out.println();
		System.out.println("Formatted " + xt + " classes.");
		System.out.println();
		
		try {
			FileUtils.deleteDirectory(ffOut);
		}catch(IOException exx) {
		}

		File patchOut = new File(minecraftSrc, "minecraft_src_patch.jar");
		
		try {
			ApplyPatchesToZip.applyPatches(formatOut, null, new File(EaglerBuildTools.repositoryRoot, "patches/minecraft"), patchOut, true, true);
		}catch(Throwable t) {
			System.err.println("ERROR: Could not apply 'patches' directory to: " + patchOut.getName());
			t.printStackTrace();
			return false;
		}
		
		File javadocOut = new File(minecraftSrc, "minecraft_src_javadoc.jar");
		CSVMappings comments = writeJavaDoc ? new CSVMappings() : null;
		if(!InsertJavaDoc.processSource(patchOut, javadocOut, mcpDataTMP, comments)) {
			System.err.println("ERROR: Could not create javadoc!");
			return false;
		}
		
		File resourcesOut = new File(minecraftSrc, "minecraft_res.jar");
		if(!LoadResources.loadResources(minecraftJar, assetsJson, resourcesOut, mcpDataTMP, new File(minecraftSrc, "minecraft_languages.zip"))) {
			System.err.println("ERROR: Could not copy resources!");
			return false;
		}

		File patchResourcesOut = new File(minecraftSrc, "minecraft_res_patch.jar");
		
		try {
			ApplyPatchesToZip.applyPatches(resourcesOut, null, new File(EaglerBuildTools.repositoryRoot, "patches/resources"), patchResourcesOut, true, true);
		}catch(Throwable t) {
			System.err.println("ERROR: Could not apply 'patches' directory to: " + patchResourcesOut.getName());
			t.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}

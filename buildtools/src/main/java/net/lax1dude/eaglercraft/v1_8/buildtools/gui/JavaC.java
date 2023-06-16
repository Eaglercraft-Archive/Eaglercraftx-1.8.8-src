package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
public class JavaC {

	public static final boolean windows;

	public static File jdkHome;

	public static final List<String> compilerFlags = Arrays.asList(
			"-Xlint:-unchecked", "-Xlint:-options", "-Xlint:-deprecation",
			"-source", "1.8", "-target", "1.8"
	);

	private static int debugSourceFileCount = 0;

	public static int runJavaC(File mcSourceJar, File outputDirectory, File tmpDirectory, String[] teavmClasspath,
			File... eaglerSourceDirs) throws IOException {
		
		if(!outputDirectory.exists() && !outputDirectory.mkdirs()) {
			throw new IOException("Could not create output directory: " + outputDirectory.getAbsolutePath());
		}
		
		if(!tmpDirectory.exists() && !tmpDirectory.mkdirs()) {
			throw new IOException("Could not create temporary directory: " + outputDirectory.getAbsolutePath());
		}
		
		File minecraftSrcTmp = new File(tmpDirectory, "MinecraftSrc/src_javadoc_tmp");
		
		if(!minecraftSrcTmp.exists() && !minecraftSrcTmp.mkdirs()) {
			throw new IOException("Could not create temporary directory: " + minecraftSrcTmp.getAbsolutePath());
		}
		
		debugSourceFileCount = 0;
		
		File argFile = new File(tmpDirectory, "sourceFiles.txt");
		try(PrintWriter writer = new PrintWriter(new FileWriter(argFile))) {
			
			System.out.println("Extracting decompiled source...");
			
			byte[] copyBuffer = new byte[16384];
			int copyBufferLen;
			try(ZipInputStream zis = new ZipInputStream(new FileInputStream(mcSourceJar))) {
				ZipEntry etr;
				while((etr = zis.getNextEntry()) != null && !etr.isDirectory()) {
					String n = etr.getName();
					if(n.endsWith(".java")) {
						File writeTo = new File(minecraftSrcTmp, n);
						File parent = writeTo.getParentFile();
						if(!parent.exists() && !parent.mkdirs()) {
							throw new IOException("Could not create temporary directory: " + parent.getAbsolutePath());
						}
						try(OutputStream os = new FileOutputStream(writeTo)) {
							while((copyBufferLen = zis.read(copyBuffer)) != -1) {
								os.write(copyBuffer, 0, copyBufferLen);
							}
						}
						writer.println("\"" + writeTo.getAbsolutePath().replace('\\', '/') + "\"");
						++debugSourceFileCount;
					}
				}
			}
			
			System.out.println("Scanning source folder paths...");
			
			for(int i = 0; i < eaglerSourceDirs.length; ++i) {
				discoverSourceFiles(eaglerSourceDirs[i], writer);
			}
			
		}
		
		List<String> commandBuilder = new ArrayList();
		
		if(windows) {
			commandBuilder.add((new File(jdkHome, "bin/javac.exe")).getAbsolutePath());
		}else {
			commandBuilder.add((new File(jdkHome, "bin/javac")).getAbsolutePath());
		}
		
		commandBuilder.addAll(compilerFlags);
		
		String pathSeparator = System.getProperty("path.separator");
		
		commandBuilder.add("-classpath");
		commandBuilder.add(String.join(pathSeparator, teavmClasspath));

		commandBuilder.add("-sourcepath");
		
		StringBuilder sourcePathBuilder = new StringBuilder();
		sourcePathBuilder.append(mcSourceJar.getAbsolutePath());
		
		for(int i = 0; i < eaglerSourceDirs.length; ++i) {
			sourcePathBuilder.append(pathSeparator).append(eaglerSourceDirs[i].getAbsolutePath());
		}

		commandBuilder.add(sourcePathBuilder.toString());

		commandBuilder.add("-d");
		commandBuilder.add(outputDirectory.getAbsolutePath());
		
		commandBuilder.add("@" + argFile.getAbsolutePath());

		System.out.println();
		for(int i = 0, l = commandBuilder.size(); i < l; ++i) {
			String e = commandBuilder.get(i);
			if(e.indexOf(' ') != -1) {
				System.out.print("\"" + e + "\"");
			}else {
				System.out.print(e);
			}
			System.out.print(' ');
		}
		System.out.println();
		System.out.println();
		System.out.println("Compiling " + debugSourceFileCount + " source files...");
		
		ProcessBuilder procBuilder = new ProcessBuilder(commandBuilder);
		procBuilder.directory(tmpDirectory);
		Process javacProcess = procBuilder.start();

		InputStream stdout = javacProcess.getInputStream();
		InputStream stderr = javacProcess.getErrorStream();
		byte[] readBuffer = new byte[128];
		int j;
		boolean tick;
		
		do {
			tick = false;
			
			j = stdout.available();
			if(j > 0) {
				if(j > 128) {
					j = 128;
				}
				stdout.read(readBuffer, 0, j);
				System.out.write(readBuffer, 0, j);
				tick = true;
			}
			
			j = stderr.available();
			if(j > 0) {
				if(j > 128) {
					j = 128;
				}
				stderr.read(readBuffer, 0, j);
				System.err.write(readBuffer, 0, j);
				tick = true;
			}
			
			if(!tick) {
				try {
					Thread.sleep(10l);
				} catch (InterruptedException e) {
				}
			}
			
		}while(javacProcess.isAlive());
		
		while(true) {
			try {
				return javacProcess.waitFor();
			} catch (InterruptedException e) {
			}
		}
	}

	private static void discoverSourceFiles(File folder, PrintWriter printWriter) throws IOException {
		File[] files = folder.listFiles();
		for(int i = 0; i < files.length; ++i) {
			File f = files[i];
			String name = f.getAbsolutePath();
			if(f.isDirectory()) {
				discoverSourceFiles(f, printWriter);
			}else {
				if(name.endsWith(".java")) {
					printWriter.println("\"" + name.replace('\\', '/') + "\"");
					++debugSourceFileCount;
				}
			}
		}
	}

	static {
		windows = System.getProperty("os.name").toLowerCase().contains("windows");
		String javac = windows ? "javac.exe" : "javac";
		File jdkHomeProp = new File(System.getProperty("java.home"));
		if((new File(jdkHomeProp, "bin/" + javac)).isFile()) {
			jdkHome = jdkHomeProp;
		}else if((new File(jdkHomeProp, "../bin/" + javac)).isFile()) {
			jdkHome = jdkHomeProp.getParentFile();
		}else {
			jdkHome = null;
		}
	}

	
}

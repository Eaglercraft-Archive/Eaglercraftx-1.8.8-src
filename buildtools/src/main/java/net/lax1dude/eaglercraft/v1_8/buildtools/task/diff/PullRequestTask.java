package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.zip.CRC32;

import org.apache.commons.io.FileUtils;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.formatter.EclipseFormatter;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InsertJavaDoc;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileWriterUTF;

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
public class PullRequestTask {
	
	public static boolean pullRequest() {
		try {
			return pullRequest0();
		}catch(Throwable t) {
			System.err.println();
			System.err.println("Exception encountered while running task 'pullrequest'!");
			t.printStackTrace();
			return false;
		}
	}
	
	private static boolean pullRequest0() throws Throwable {
		File originalUnpatchedSourceMainJar = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_src.jar");
		File originalSourceMainJar = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_src_patch.jar");
		File minecraftJavadocTmp = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_src_javadoc.jar");
		File originalSourceMain = new File(EaglerBuildTools.repositoryRoot, "sources/main/java");
		File originalSourceTeaVM = new File(EaglerBuildTools.repositoryRoot, "sources/teavm/java");
		File originalSourceLWJGL = new File(EaglerBuildTools.repositoryRoot, "sources/lwjgl/java");
		File originalUnpatchedSourceResourcesJar = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_res.jar");
		File originalSourceResourcesJar = new File(EaglerBuildToolsConfig.getTemporaryDirectory(), "MinecraftSrc/minecraft_res_patch.jar");
		File originalSourceResources = new File(EaglerBuildTools.repositoryRoot, "sources/resources");
		File diffFromMain = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "src/main/java");
		File diffFromTeaVM = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "src/teavm/java");
		File diffFromLWJGL = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "src/lwjgl/java");
		File diffFromResources = new File(EaglerBuildToolsConfig.getWorkspaceDirectory(), "desktopRuntime/resources");
		File pullRequestTo = new File(EaglerBuildTools.repositoryRoot, "pullrequest");
		
		boolean prExist = pullRequestTo.exists();
		if(prExist && !(pullRequestTo.isDirectory() && pullRequestTo.list().length == 0)) {
			System.out.println();
			System.out.print("Warning: The 'pullrequest' folder already exists in your repository. Overwrite? [Y/n]: ");
			String ret = "n";
			try {
				ret = (new BufferedReader(new InputStreamReader(System.in))).readLine();
			}catch(IOException ex) {
				// ?
			}
			ret = ret.toLowerCase();
			if(!ret.startsWith("y")) {
				System.out.println();
				System.out.println("The pull request was cancelled.");
				return true;
			}else {
				try {
					FileUtils.deleteDirectory(pullRequestTo);
					prExist = false;
				}catch(IOException ex) {
					System.err.println("ERROR: Could not delete \"" + pullRequestTo.getAbsolutePath() + "\"!");
					ex.printStackTrace();
					return false;
				}
			}
		}
		
		if(!prExist && !pullRequestTo.mkdirs()) {
			System.err.println("ERROR: Could not create folder \"" + pullRequestTo.getAbsolutePath() + "\"!");
		}
		
		File pullRequestToMain = new File(pullRequestTo, "source");
		File pullRequestToResources = new File(pullRequestTo, "resources");

		boolean flag = false;
		int i = copyAllModified(diffFromTeaVM, originalSourceTeaVM);
		if(i > 0) {
			flag = true;
		}
		System.out.println("Found " + i + " changed files in /src/teavm/java/");
		
		i = copyAllModified(diffFromLWJGL, originalSourceLWJGL);
		if(i > 0) {
			flag = true;
		}
		System.out.println("Found " + i + " changed files in /src/lwjgl/java/");
		
		i = createDiffFiles(originalSourceMain, minecraftJavadocTmp, originalUnpatchedSourceMainJar, 
				originalSourceMainJar, diffFromMain, pullRequestToMain, true);
		if(i > 0) {
			flag = true;
		}
		System.out.println("Found " + i + " changed files in /src/main/java/");
		
		i = createDiffFiles(originalSourceResources, originalSourceResourcesJar, originalUnpatchedSourceResourcesJar, 
				null, diffFromResources, pullRequestToResources, false);
		if(i > 0) {
			flag = true;
		}
		System.out.println("Found " + i + " changed files in /desktopRuntime/resources/");
		
		if(!flag) {
			System.out.println("ERROR: No modified files were found!");
			if(pullRequestTo.exists()) {
				pullRequestTo.delete();
			}
		}
		
		return true;
	}
	
	private static int createDiffFiles(File folderOriginal, File jarOriginal, File jarOriginalUnpatched, File originalJarNoJavadoc,
			File folderEdited, File folderOut, boolean isJava) throws Throwable {
		if(!folderEdited.isDirectory()) {
			return 0;
		}
		boolean createdFolderOut = folderOut.isDirectory();
		int cnt = 0;
		Collection<File> workspaceFiles = FileUtils.listFiles(folderEdited, null, true);
		Map<String,byte[]> jarEntriesUnpatched;
		if(jarOriginalUnpatched != null) {
			System.out.println("Loading files from '" + jarOriginalUnpatched.getName() + "'...");
			try(FileInputStream is = new FileInputStream(jarOriginalUnpatched)) {
				jarEntriesUnpatched = JARMemoryCache.loadJAR(is);
			}
		}else {
			jarEntriesUnpatched = new WeakHashMap();
		}
		Map<String,byte[]> jarEntriesPatched;
		if(jarOriginal != null) {
			System.out.println("Loading files from '" + jarOriginal.getName() + "'...");
			try(FileInputStream is = new FileInputStream(jarOriginal)) {
				jarEntriesPatched = JARMemoryCache.loadJAR(is);
			}
		}else {
			jarEntriesPatched = new WeakHashMap();
		}
		Map<String,byte[]> jarEntries = new HashMap();
		jarEntries.putAll(jarEntriesUnpatched);
		jarEntries.putAll(jarEntriesPatched);
		Map<String,byte[]> jarEntriesNoJavadoc;
		if(originalJarNoJavadoc != null) {
			System.out.println("Loading files from '" + originalJarNoJavadoc.getName() + "'...");
			try(FileInputStream is = new FileInputStream(originalJarNoJavadoc)) {
				jarEntriesNoJavadoc = JARMemoryCache.loadJAR(is);
			}
		}else {
			jarEntriesNoJavadoc = new WeakHashMap();
		}
		System.out.println("Comparing...");
		System.out.println("(this may take a while)");
		String editedPrefix = folderEdited.getAbsolutePath();
		Set<String> filesReplaced = new HashSet();
		for(File wf : workspaceFiles) {
			String newPath = wf.getAbsolutePath().replace(editedPrefix, "");
			if(newPath.indexOf('\\') != -1) {
				newPath = newPath.replace('\\', '/');
			}
			if(newPath.startsWith("/")) {
				newPath = newPath.substring(1);
			}
			File orig = new File(folderOriginal, newPath);
			byte[] jarData = null;
			boolean replacedFileExists = orig.exists();
			if(replacedFileExists) {
				filesReplaced.add(newPath);
				if(copyFileIfChanged(wf, orig)) {
					++cnt;
				}
			}else if((jarData = jarEntries.get(newPath)) != null) {
				filesReplaced.add(newPath);
				byte [] o = jarData;
				byte [] n = FileUtils.readFileToByteArray(wf);
				boolean changed = false;
				if(o.length != n.length) {
					if(!createdFolderOut) {
						if(!folderOut.mkdirs()) {
							throw new IOException("Could not create folder: \"" + folderOut.getAbsolutePath() + "\"!");
						}
						createdFolderOut = true;
					}
					String noJavaDocString = null;
					byte[] noJavaDoc = jarEntriesNoJavadoc.get(newPath);
					if(noJavaDoc != null) {
						noJavaDocString = new String(noJavaDoc, StandardCharsets.UTF_8);
					}
					if(writeDiff(o, n, folderOut, newPath, isJava, noJavaDocString)) {
						changed = true;
						++cnt;
					}
				}else {
					for(int i = 0; i < o.length; ++i) {
						if(o[i] != n[i]) {
							if(!createdFolderOut) {
								if(!folderOut.mkdirs()) {
									throw new IOException("Could not create folder: \"" + folderOut.getAbsolutePath() + "\"!");
								}
								createdFolderOut = true;
							}
							String noJavaDocString = null;
							byte[] noJavaDoc = jarEntriesNoJavadoc.get(newPath);
							if(noJavaDoc != null) {
								noJavaDocString = new String(noJavaDoc, StandardCharsets.UTF_8);
							}
							if(writeDiff(o, n, folderOut, newPath, isJava, noJavaDocString)) {
								changed = true;
								++cnt;
							}
							break;
						}
					}
				}
				if(!changed && !jarEntriesPatched.containsKey(newPath)) {
					FileUtils.writeByteArrayToFile(new File(folderOut, makeName(newPath, "recreate")), jarData);
					++cnt;
				}
			}else {
				filesReplaced.add(newPath);
				FileUtils.copyFile(wf, orig);
				++cnt;
			}
		}
		
		if(jarEntriesPatched.size() > 0) {
			for(Entry<String,byte[]> etr : jarEntriesPatched.entrySet()) {
				if(filesReplaced.contains(etr.getKey())) {
					continue;
				}
				if(!(new File(folderEdited, etr.getKey())).exists()) {
					if(!createdFolderOut) {
						if(!folderOut.mkdirs()) {
							throw new IOException("Could not create folder: \"" + folderOut.getAbsolutePath() + "\"!");
						}
						createdFolderOut = true;
					}
					FileUtils.writeStringToFile(new File(folderOut, makeName(etr.getKey(), "delete")), 
							"#hash: " + getCRC32(etr.getValue()), "UTF-8");
					++cnt;
				}
			}
		}
		
		return cnt;
	}
	
	private static boolean writeDiff(byte[] old, byte[] _new, File outDir, String outName, boolean isJava, String javaNotJavadoc) throws IOException {
		String oldStr = toStringIfValid(old);
		String newStr = oldStr == null ? null : toStringIfValid(_new);
		if(oldStr == null || newStr == null) {
			FileUtils.writeByteArrayToFile(new File(outDir, makeName(outName, "replace")), _new);
		}else {
			if(javaNotJavadoc != null) {
				oldStr = javaNotJavadoc;
			}
			//oldStr = stripJavadocAndFormat(oldStr);
			newStr = stripJavadocAndFormat(newStr);
			List<String> oldLines = Lines.linesList(oldStr);
			List<String> newLines = Lines.linesList(newStr);
			Patch<String> deltas = DiffUtils.diff(oldLines, newLines);
			
			List<String> diffFile = UnifiedDiffUtils.generateUnifiedDiff(outName, outName, oldLines, deltas, ApplyPatchesToZip.patchContextLength);
			
			if(diffFile.size() == 0) {
				return false;
			}
			
			File fout = new File(outDir, makeName(outName, "edit"));
			File p = fout.getParentFile();
			if(!p.isDirectory()) {
				if(!p.mkdirs()) {
					throw new IOException("Failed to create directory \"" + p.getAbsolutePath() + "\"!");
				}
			}
			try(PrintWriter foutStream = new PrintWriter(new FileWriterUTF(fout))) {
				for(int i = 0, l = diffFile.size(); i < l; ++i) {
					foutStream.println(diffFile.get(i));
				}
			}
		}
		return true;
	}
	
	private static String stripJavadocAndFormat(String input) {
		input = InsertJavaDoc.stripDocForDiff(input);
		input = EclipseFormatter.processSource(input, System.lineSeparator());
		return input;
	}
	
	private static int copyAllModified(File inDir, File outDir) throws IOException {
		if(!inDir.isDirectory()) {
			return 0;
		}
		int cnt = 0;
		Collection<File> workspaceFiles = FileUtils.listFiles(inDir, null, true);
		String editedPrefix = inDir.getAbsolutePath();
		for(File wf : workspaceFiles) {
			String newPath = wf.getAbsolutePath().replace(editedPrefix, "");
			if(newPath.indexOf('\\') != -1) {
				newPath = newPath.replace('\\', '/');
			}
			if(newPath.startsWith("/")) {
				newPath = newPath.substring(1);
			}
			File orig = new File(outDir, newPath);
			if(copyFileIfChanged(wf, orig)) {
				++cnt;
			}
		}
		return cnt;
	}
	
	private static String makeName(String input, String type) {
		int lastSlash = input.lastIndexOf('/');
		int lastDot = input.lastIndexOf('.');
		if(lastDot > lastSlash + 1) {
			return input.substring(0, lastDot) + "." + type + input.substring(lastDot);
		}else {
			return input + "." + type;
		}
	}
	
	private static final CharsetDecoder utf8Decoder = StandardCharsets.UTF_8.newDecoder();
	
	private static String toStringIfValid(byte[] in) {
		ByteBuffer inn = ByteBuffer.wrap(in);
		CharBuffer cb;
		try {
			cb = utf8Decoder.decode(inn);
		}catch(Throwable t) {
			return null;
		}
		return cb.toString();
	}
	
	private static final String hex = "0123456789ABCDEF";
	
	private static String hex32(long in) {
		char[] ret = new char[8];
		for(int i = 7; i >= 0; --i) {
			ret[i] = hex.charAt((int)((in >> (i << 2)) & 0xF));
		}
		return new String(ret);
	}
	
	private static String getCRC32(File in) throws IOException {
		CRC32 crc = new CRC32();
		crc.update(FileUtils.readFileToByteArray(in));
		return hex32(crc.getValue());
	}
	
	private static String getCRC32(byte[] in) {
		CRC32 crc = new CRC32();
		crc.update(in);
		return hex32(crc.getValue());
	}
	
	private static boolean checkCRC32(File in1, File in2) throws IOException {
		CRC32 crc = new CRC32();
		crc.update(FileUtils.readFileToByteArray(in1));
		long v1 = crc.getValue();
		crc.reset();
		crc.update(FileUtils.readFileToByteArray(in2));
		return v1 != crc.getValue();
	}
	
	private static boolean copyFileIfChanged(File in1, File in2) throws IOException {
		if(!in2.exists()) {
			FileUtils.copyFile(in1, in2);
			return true;
		}
		if(in1.lastModified() == in2.lastModified()) {
			return false;
		}
		CRC32 crc = new CRC32();
		byte[] f1 = FileUtils.readFileToByteArray(in1);
		crc.update(f1);
		long v1 = crc.getValue();
		crc.reset();
		byte[] f2 = FileUtils.readFileToByteArray(in2);
		crc.update(f2);
		if(v1 != crc.getValue()) {
			//System.out.println("changed: " + in1.getAbsolutePath());
			FileUtils.writeByteArrayToFile(in2, f1);
			return true;
		}else {
			return false;
		}
	}
	
}

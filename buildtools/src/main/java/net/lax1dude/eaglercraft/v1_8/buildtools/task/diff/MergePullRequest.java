package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.CSVMappings;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InsertJavaDoc;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.SetupWorkspace;

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
public class MergePullRequest {
	
	public static boolean mergeTask() {
		try {
			return mergeTask0();
		}catch(Throwable t) {
			System.err.println();
			System.err.println("Exception encountered while running task 'merge'!");
			t.printStackTrace();
			return false;
		}
	}
	
	private static boolean mergeTask0() throws Throwable {
		File pullRequestDir = new File("pullrequest");
		
		if(!pullRequestDir.isDirectory() || FileUtils.isEmptyDirectory(pullRequestDir)) {
			System.err.println("ERROR: the 'pullrequest' directory does not exist or is empty, aborting merge because there's nothing to merge");
			return false;
		}
		
		if((new File(pullRequestDir, "merged.txt")).exists()) {
			System.err.println("ERROR: the 'pullrequest' directory has already been merged, aborting merge because there's nothing to merge");
			System.err.println("To override, delete 'merged.txt' from the folder.");
			return false;
		}

		System.out.println();
		System.out.println("Warning: running 'merge' is a command only intended to be used");
		System.out.println("by the repository's owner, it will perminantly incorporate all");
		System.out.println("changes in the 'pullrequest' directory into this repository's");
		System.out.println("patch file directory!");
		System.out.println();
		System.out.println("Doing so will make it impossible to reliably create any future");
		System.out.println("pull requests back to this project's main repository, unless the");
		System.out.println("main repository has merged the same pull request into it's patch");
		System.out.println("file directory too.");
		System.out.println();
		System.out.println("Back up the current state of the patch file directory in a local");
		System.out.println("commit or branch to allow you to undo any unintentional changes");
		System.out.println("made to the directory as a result of running this command.");
		System.out.println();
		System.out.print("Do you really want to do this? [Y/n]: ");

		String ret = "n";
		try {
			ret = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		}catch(IOException ex) {
			// ?
		}
		ret = ret.toLowerCase();
		if(!ret.startsWith("y")) {
			System.out.println();
			System.out.println("OKAY THANK GOD, crisis averted!");
			System.out.println();
			System.out.println("Thank the author of this tool kindly for providing this check.");
			return true;
		}

		System.out.println();
		System.out.println("Warning: close all programs that may have files or folders open");
		System.out.println("in the repository or the merge could fail catastrophically");
		System.out.println();
		System.out.println("This folder: " + (new File(".")).getAbsolutePath());
		System.out.println();
		System.out.println("Check for any file explorer windows displaying the contents of a");
		System.out.println("file or folder in this directory.");
		System.out.println();
		System.out.println("Close any programs with files open someplace in this folder.");
		System.out.println();
		System.out.println("If merging fails, revert all changes in this directory with git");
		System.out.println("or a backup, re-run 'init', then run 'pullrequest' and 'merge'");
		System.out.println();
		System.out.print("Did you close everything? [Y/n]: ");

		ret = "n";
		try {
			ret = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		}catch(IOException ex) {
			// ?
		}
		ret = ret.toLowerCase();
		if(!ret.startsWith("y")) {
			System.out.println();
			System.out.println("OKAY THANK GOD, crisis averted!");
			System.out.println();
			System.out.println("Thank the author of this tool kindly for providing this check.");
			return true;
		}

		System.out.println();
		File temporaryDirectory = EaglerBuildToolsConfig.getTemporaryDirectory();
		System.out.println();

		File pullRequestToSrc = new File(pullRequestDir, "source");
		File pullRequestToRes = new File(pullRequestDir, "resources");

		boolean prSrcExist = pullRequestToSrc.isDirectory() && !FileUtils.isEmptyDirectory(pullRequestToSrc);
		boolean prResExist = pullRequestToRes.isDirectory() && !FileUtils.isEmptyDirectory(pullRequestToRes);
		
		if(!prSrcExist && !prResExist) {
			System.err.println("ERROR: the 'pullrequest' directory does not exist or is empty, aborting merge because there's nothing to merge");
			return false;
		}
		
		if(prSrcExist) {
			File tmpOriginalUnpatched = new File(temporaryDirectory, "MinecraftSrc/minecraft_src.jar");
			if(!tmpOriginalUnpatched.isFile()) {
				System.err.println("ERROR: file '" + tmpOriginalUnpatched.getName() + "' was not found!");
				System.err.println("Run the 'init' task again to re-generate it");
				return false;
			}
			
			File tmpOriginal = new File(temporaryDirectory, "MinecraftSrc/minecraft_src_patch.jar");
			if(!tmpOriginal.isFile()) {
				System.err.println("ERROR: file '" + tmpOriginal.getName() + "' was not found!");
				System.err.println("Run the 'init' task again to re-generate it");
				return false;
			}
			
			File tmpMerged = new File(temporaryDirectory, "MinecraftSrc/minecraft_src_merge.jar");
			File tmpMergedDiffs = new File(temporaryDirectory, "MinecraftSrc/minecraft_src_merge_diffs.zip");

			System.out.println("Applying pull request to '" + tmpOriginal.getName() + "'...");
			System.out.println();
			ApplyPatchesToZip.applyPatches(tmpOriginal, tmpOriginalUnpatched, pullRequestToSrc, tmpMerged, true, false);
			
			try {
				createMergeDiffs(tmpMerged, tmpOriginalUnpatched, tmpMergedDiffs);
			}catch(Throwable t) {
				tmpMerged.delete();
				throw t;
			}
			
			System.out.println();

			File patchOut = new File("./patches/minecraft");
			File patchTmpOut = new File("./patches.bak/minecraft");
			if(patchOut.exists()) {
				System.out.println("Backing up '" + patchOut.getAbsolutePath() + "'...");
				try {
					FileUtils.deleteDirectory(patchTmpOut);
					FileUtils.moveDirectory(patchOut, patchTmpOut);
				}catch(Throwable t) {
					tmpMerged.delete();
					throw t;
				}
			}

			FileUtils.copyFile(new File(patchTmpOut, "output_license.txt"), new File(patchOut, "output_license.txt"));

			System.out.println("Extracting '" + tmpMergedDiffs + "' to 'patches/minecraft'...");
			int cnt = SetupWorkspace.extractJarTo(tmpMergedDiffs, patchOut);

			if(!tmpMergedDiffs.delete()) {
				System.err.println("ERROR: could not delete '" + tmpMergedDiffs.getName() + "'!");
			}
			
			System.out.println("Wrote " + cnt + " files.");
			
			System.out.println("Copying '" + tmpMerged.getName() + "' to '" + tmpOriginal.getName() + "'...");
			
			if((tmpOriginal.exists() && !tmpOriginal.delete()) || !tmpMerged.renameTo(tmpOriginal)) {
				System.err.println("ERROR: could not copy '" + tmpMerged.getName() + "' to '" + tmpOriginal.getName() + "'!");
				System.err.println("Run the 'init' task again before proceeding");
				tmpOriginal.delete();
			}else {
				File javadocOut = new File(temporaryDirectory, "MinecraftSrc/minecraft_src_javadoc.jar");
				CSVMappings comments = new CSVMappings();
				if(!InsertJavaDoc.processSource(tmpOriginal, javadocOut, new File(temporaryDirectory, "ModCoderPack"), comments)) {
					System.err.println();
					System.err.println("ERROR: Could not create javadoc!");
					return false;
				}
			}
			
			if(tmpMerged.exists()) {
				tmpMerged.delete();
			}
			
			System.out.println("Deleting backup folder...");
			
			try {
				FileUtils.deleteDirectory(patchTmpOut);
			}catch(Throwable t) {
				System.err.println("ERROR: could not delete 'patches.bak/minecraft'!");
				System.err.println(t.toString());
			}
			System.out.println();
			
		}
		
		if(prResExist) {
			File tmpOriginalUnpatched = new File(temporaryDirectory, "MinecraftSrc/minecraft_res.jar");
			if(!tmpOriginalUnpatched.isFile()) {
				System.err.println("ERROR: file '" + tmpOriginalUnpatched.getName() + "' was not found!");
				System.err.println("Run the 'init' task again to re-generate it");
				return false;
			}
			
			File tmpOriginal = new File(temporaryDirectory, "MinecraftSrc/minecraft_res_patch.jar");
			if(!tmpOriginal.isFile()) {
				System.err.println("ERROR: file '" + tmpOriginal.getName() + "' was not found!");
				System.err.println("Run the 'init' task again to re-generate it");
				return false;
			}
			
			File tmpMerged = new File(temporaryDirectory, "MinecraftSrc/minecraft_res_merge.jar");
			File tmpMergedDiffs = new File(temporaryDirectory, "MinecraftSrc/minecraft_res_merge_diffs.zip");

			System.out.println("Applying pull request to '" + tmpOriginal.getName() + "'...");
			System.out.println();
			ApplyPatchesToZip.applyPatches(tmpOriginal, tmpOriginalUnpatched, pullRequestToRes, tmpMerged, true, false);
			
			try {
				createMergeDiffs(tmpMerged, tmpOriginalUnpatched, tmpMergedDiffs);
			}catch(Throwable t) {
				tmpMerged.delete();
				throw t;
			}
			
			System.out.println();

			File patchOut = new File("./patches/resources");
			File patchTmpOut = new File("./patches.bak/resources");
			if(patchOut.exists()) {
				System.out.println("Backing up '" + patchOut.getAbsolutePath() + "'...");
				try {
					FileUtils.deleteDirectory(patchTmpOut);
					FileUtils.moveDirectory(patchOut, patchTmpOut);
				}catch(Throwable t) {
					tmpMerged.delete();
					throw t;
				}
			}

			System.out.println("Extracting '" + tmpMergedDiffs + "' to 'patches/resources'...");
			int cnt = SetupWorkspace.extractJarTo(tmpMergedDiffs, patchOut);

			if(!tmpMergedDiffs.delete()) {
				System.err.println("ERROR: could not delete '" + tmpMergedDiffs.getName() + "'!");
			}

			System.out.println("Wrote " + cnt + " files.");
			
			System.out.println("Copying '" + tmpMerged.getName() + "' to '" + tmpOriginal.getName() + "'...");
			
			if((tmpOriginal.exists() && !tmpOriginal.delete()) || !tmpMerged.renameTo(tmpOriginal)) {
				System.err.println("ERROR: could not copy '" + tmpMerged.getName() + "' to '" + tmpOriginal.getName() + "'!");
				System.err.println("Run the 'init' task again before proceeding");
				tmpOriginal.delete();
			}

			if(tmpMerged.exists()) {
				tmpMerged.delete();
			}
			
			System.out.println("Deleting backup folder...");
			try {
				FileUtils.deleteDirectory(patchTmpOut);
			}catch(Throwable t) {
				System.err.println("ERROR: could not delete 'patches.bak/resources'!");
				System.err.println(t.getMessage());
			}
			System.out.println();
			
		}
		
		(new File("./patches.bak")).delete();

		System.out.println("Successfully merged pullrequest directory!");
		
		try {
			SimpleDateFormat fmt1 = new SimpleDateFormat("MM-dd-yy");
			SimpleDateFormat fmt2 = new SimpleDateFormat("kk:mm:ss");
			Date dt = new Date();
			FileUtils.writeStringToFile(new File(pullRequestDir, "merged.txt"), "This pullrequest was merged on " +
					fmt1.format(new Date()) + " at " + fmt2.format(dt) + ".", "UTF-8");
		}catch(IOException ex) {
			System.err.println("ERROR: could not write 'merged.txt' in pullrequest directory!");
			System.err.println("Creating a file called 'merged.txt' is important to tell buildtools that the");
			System.err.println("existing pullrequest has already been merged! Do not try to merge it again!");
		}
		
		System.out.println("Backing up to 'pullrequest_merged_backup'...");
		
		String pth = pullRequestDir.getAbsolutePath();
		if(pth.endsWith("/") || pth.endsWith("\\")) {
			pth = pth.substring(0, pth.length() - 1);
		}

		File m0 = new File(pth + "_merged_backup");
		
		if(m0.exists() && !FileUtils.deleteQuietly(m0)) {
			System.err.println("Could not delete old backup!");
			m0 = new File(pth + "_merged_backup1");
			if(m0.exists() && !FileUtils.deleteQuietly(m0)) {
				System.err.println("Could not delete 2nd old backup!");
				return true;
			}
		}
		
		try {
			FileUtils.moveDirectory(pullRequestDir, m0);
		}catch(IOException ex) {
			System.err.println("Could not create backup!");
		}
		
		return true;
	}
	
	private static void createMergeDiffs(File tmpMerged, File tmpOriginalUnpatched, File tmpMergedDiffs) throws Throwable {
		System.out.println("Creating patches from '" + tmpMerged.getName() + "'...");
		
		System.out.println("Loading files from '" + tmpOriginalUnpatched.getName() + "'...");
		Map<String, byte[]> memoryCacheUnpatched;
		try(InputStream is = new FileInputStream(tmpOriginalUnpatched)) {
			memoryCacheUnpatched = JARMemoryCache.loadJAR(is);
		}
		
		if(memoryCacheUnpatched == null) {
			throw new IOException("Failed to load JAR into memory: '" + tmpOriginalUnpatched.getName());
		}
		
		System.out.println("Loading files from '" + tmpMerged.getName() + "'...");
		Map<String, byte[]> memoryCacheMerged;
		try(InputStream is = new FileInputStream(tmpMerged)) {
			memoryCacheMerged = JARMemoryCache.loadJAR(is);
		}
		
		if(memoryCacheMerged == null) {
			throw new IOException("Failed to load JAR into memory: '" + tmpMerged.getName());
		}
		
		Set<String> deleteList = new HashSet();
		deleteList.addAll(memoryCacheUnpatched.keySet());

		System.out.println("Generating patch files..");
		System.out.println("(Writing to: " + tmpMergedDiffs.getName() + ")");
		System.out.println("(this may take a while)");
		System.out.print("   ");
		int cnt = 0;
		try(ZipOutputStream mgd = new ZipOutputStream(new FileOutputStream(tmpMergedDiffs))) {
			mgd.setLevel(5);
			for(Entry<String,byte[]> met : memoryCacheMerged.entrySet()) {
				String n = met.getKey();
				byte[] orig = memoryCacheUnpatched.get(n);
				if(orig == null) {
					System.err.println("Error: tried to patch file '" + n + "' that doesn't exist in the minecraft source");
					continue;
				}
				deleteList.remove(n);
				if(writeDiff(orig, met.getValue(), n, mgd)) {
					++cnt;
					if(cnt % 75 == 74) {
						System.out.print(".");
					}
				}
			}
			System.out.println();
			
			System.out.println("Wrote " + cnt + " patch files.");
			
			mgd.putNextEntry(new ZipEntry("delete.txt"));
			PrintWriter delWriter = new PrintWriter(mgd);
			delWriter.println("# " + deleteList.size() + " files to delete:");
			for(String s : deleteList) {
				delWriter.println(s);
			}
			delWriter.flush();
			
			System.out.println("Wrote " + deleteList.size() + " deletes.");
			
		}
		
	}
	
	private static boolean writeDiff(byte[] old, byte[] _new, String outName, ZipOutputStream output) throws IOException {
		if(Arrays.equals(old, _new)) {
			return false;
		}
		
		String oldStr = toStringIfValid(old);
		String newStr = oldStr == null ? null : toStringIfValid(_new);
		
		if(oldStr == null || newStr == null) {
			output.putNextEntry(new ZipEntry(makeName(outName, "replace")));
			IOUtils.write(_new, output);
			return true;
		}else {
			List<String> oldLines = Lines.linesList(oldStr);
			List<String> newLines = Lines.linesList(newStr);
			Patch<String> deltas = DiffUtils.diff(oldLines, newLines);

//			List<String> diffFile = UnifiedDiffUtils.generateUnifiedDiff(outName, outName, oldLines, deltas, ApplyPatchesToZip.patchContextLength);
//			
//			if(diffFile.size() == 0) {
//				return false;
//			}
//			
//			output.putNextEntry(new ZipEntry(makeName(outName, "edit")));
//			PrintWriter foutStream = new PrintWriter(output);
//			for(int i = 0, l = diffFile.size(); i < l; ++i) {
//				foutStream.println(diffFile.get(i));
//			}
//			foutStream.flush();

			output.putNextEntry(new ZipEntry(makeName(outName, "edit")));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
			EaglerContextRedacted.writeContextRedacted(deltas, writer);
			writer.flush();
			
			return true;
		}
		
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
	
	public static boolean mergeDirect() {
		try {
			return mergeDirect0();
		}catch(Throwable t) {
			System.err.println();
			System.err.println("Exception encountered while running task 'merge_direct'!");
			t.printStackTrace();
			return false;
		}
	}

	private static boolean mergeDirect0() throws Throwable {
		
		if(!PullRequestTask.pullRequest()) {
			System.err.println();
			System.err.println("Error: could not create merge_direct pull request!");
			return false;
		}
		
		try {
			if(!mergeTask0()) {
				System.err.println();
				System.err.println("Exception encountered while running task 'merge_direct'!");
				return false;
			}
		}catch(Throwable t) {
			System.err.println();
			System.err.println("Exception encountered while running task 'merge_direct'!");
			t.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}

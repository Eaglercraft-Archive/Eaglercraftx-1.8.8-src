package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

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
public class DiffSet {
	
	public static class DeleteFunction {
		private DeleteFunction() {
		}
	}
	
	public static class ReplaceFunction {
		public final byte[] file;
		private ReplaceFunction(byte[] file) {
			this.file = file;
		}
	}
	
	public static final DeleteFunction deleteFunction = new DeleteFunction();

	public final Map<String,Object> diffs;
	public final Map<String,byte[]> recreate;
	
	public DiffSet() {
		diffs = new HashMap();
		recreate = new HashMap();
	}
	
	private static final Pattern editPattern = Pattern.compile(".*\\.edit(\\.[^\\.\\/\\\\]+)?$");
	private static final Pattern replacePattern = Pattern.compile(".*\\.replace(\\.[^\\.\\/\\\\]+)?$");
	private static final Pattern deletePattern = Pattern.compile(".*\\.delete(\\.[^\\.\\/\\\\]+)?$");
	private static final Pattern recreatePattern = Pattern.compile(".*\\.recreate(\\.[^\\.\\/\\\\]+)?$");
	
	public int loadFolder(File pathIn, boolean useECR, SourceProvider ecrContextProvider) throws IOException {
		String baseAbsolutePath = pathIn.getAbsolutePath();
		int total = 0;
		
		File del = new File(pathIn, "delete.txt");
		if(del.isFile()) {
			Collection<String> cl = FileUtils.readLines(del, "UTF-8");
			for(String s : cl) {
				s = s.trim();
				s = s.replace('\\', '/');
				if(!s.startsWith("#")) {
					if(s.startsWith("/")) {
						s = s.substring(1);
					}
					diffs.put(s, deleteFunction);
				}
			}
		}
		
		Collection<File> fl = FileUtils.listFiles(pathIn, null, true);
		Iterator<File> fi = fl.iterator();
		while(fi.hasNext()) {
			File f = fi.next();
			String fName = f.getAbsolutePath().replace(baseAbsolutePath, "").replace('\\', '/');
			if(fName.startsWith("/")) {
				fName = fName.substring(1);
			}
			if(editPattern.matcher(fName).matches()) {
				try {
					String nm = removeExt(fName, "edit");
					Patch<String> pth;
					if(useECR) {
						try (BufferedReader reader = new BufferedReader(
								new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
							pth = EaglerContextRedacted.readContextRestricted(ecrContextProvider.getSource(nm), reader);
						}
					}else {
						List<String> phile = FileUtils.readLines(f, "UTF-8");
						pth = UnifiedDiffUtils.parseUnifiedDiff(phile);
					}
					if(pth == null) {
						throw new IOException("Invalid DIFF file!");
					}
					diffs.put(nm, pth);
					++total;
				}catch(Throwable ex) {
					System.err.println("ERROR: could not read '" + fName + "'!");
				}
			}else if(replacePattern.matcher(fName).matches()) {
				try {
					diffs.put(removeExt(fName, "replace"), new ReplaceFunction(FileUtils.readFileToByteArray(f)));
					++total;
				}catch(Throwable ex) {
					System.err.println("ERROR: could not read '" + fName + "'!");
				}
			}else if(deletePattern.matcher(fName).matches()) {
				diffs.put(removeExt(fName, "delete"), deleteFunction);
				++total;
			}else if(recreatePattern.matcher(fName).matches()) {
				try {
					String str = removeExt(fName, "recreate");
					recreate.put(str, FileUtils.readFileToByteArray(f));
					diffs.remove(str);
					++total;
				}catch(Throwable ex) {
					System.err.println("ERROR: could not read '" + fName + "'!");
				}
			}
		}
		
		return total;
	}
	
	private static String removeExt(String fn, String ext) {
		int end = fn.lastIndexOf("." + ext);
		if(end != -1) {
			return fn.substring(0, end) + fn.substring(end + ext.length() + 1, fn.length());
		}else {
			return fn;
		}
	}
	
	public static interface SourceProvider {
		List<String> getSource(String filename) throws IOException;
	}
}

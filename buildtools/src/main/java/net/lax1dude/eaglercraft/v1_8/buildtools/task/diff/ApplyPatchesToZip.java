package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;

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
public class ApplyPatchesToZip {
	
	public static final int patchContextLength = 3;

	public static void applyPatches(File zipIn, File unpatchedZipIn, File patchesIn, File zipOut, boolean compress, boolean useECR) throws Throwable {
		if(!patchesIn.isDirectory()) {
			FileUtils.copyFile(zipIn, zipOut);
			return;
		}
		Map<String,byte[]> jarEntriesUnpatched;
		if(unpatchedZipIn != null) {
			System.out.println("Loading files from '" + unpatchedZipIn.getName() + "'...");
			try(FileInputStream is = new FileInputStream(unpatchedZipIn)) {
				jarEntriesUnpatched = JARMemoryCache.loadJAR(is);
			}
		}else {
			jarEntriesUnpatched = new WeakHashMap();
		}
		Map<String,byte[]> jarEntriesPatched;
		if(zipIn != null) {
			System.out.println("Loading files from '" + zipIn.getName() + "'...");
			try(FileInputStream is = new FileInputStream(zipIn)) {
				jarEntriesPatched = JARMemoryCache.loadJAR(is);
			}
		}else {
			jarEntriesPatched = new WeakHashMap();
		}
		System.out.println("Patching files in '" + zipIn.getName() + "'...");
		final Map<String,byte[]> jarEntries = new HashMap();
		jarEntries.putAll(jarEntriesUnpatched);
		jarEntries.putAll(jarEntriesPatched);
		DiffSet diffs = new DiffSet();
		int totalLoad = diffs.loadFolder(patchesIn, useECR, useECR ? new DiffSet.SourceProvider() {
			@Override
			public List<String> getSource(String filename) throws IOException {
				byte[] etr = jarEntries.get(filename);
				if(etr == null) {
					throw new FileNotFoundException("Could not find source for: " + filename);
				}
				return Lines.linesList(new String(etr, StandardCharsets.UTF_8));
			}
		} : null);
		System.out.println("   loaded " + totalLoad + " patch files from the repo");
		System.out.println("   patching files...");
		System.out.print("   ");
		
		int cnt = 0;
		int crtCnt = 0;
		int delCnt = 0;
		int repCnt = 0;
		int pthCnt = 0;
		try(ZipOutputStream jarOut = new ZipOutputStream(new FileOutputStream(zipOut))) {
			jarOut.setLevel(compress ? 5 : 0);
			jarOut.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			jarOut.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
			String nm;
			for(Entry<String,byte[]> et : jarEntries.entrySet()) {
				nm = et.getKey();
				if(!nm.startsWith("META-INF")) {
					Object op = diffs.diffs.get(nm);
					if(op != null) {
						if(op instanceof DiffSet.DeleteFunction) {
							++delCnt;
							continue;
						}else if(op instanceof DiffSet.ReplaceFunction) {
							jarOut.putNextEntry(new ZipEntry(nm));
							IOUtils.write(((DiffSet.ReplaceFunction)op).file, jarOut);
							++repCnt;
						}else if(op instanceof Patch<?>) {
							jarOut.putNextEntry(new ZipEntry(nm));
							List<String> lines = Lines.linesList(new String(et.getValue(), "UTF-8"));
							try {
								lines = ((Patch<String>)op).applyTo(lines);
							}catch(PatchFailedException ptch) {
								throw new IOException("Could not patch file \"" + nm + "\"!", ptch);
							}
							IOUtils.writeLines(lines, null, jarOut, "UTF-8");
							++pthCnt;
						}else {
							// ?
						}
						++cnt;
						if(cnt % 75 == 74) {
							System.out.print(".");
						}
					}else {
						if(jarEntriesPatched.containsKey(nm)) {
							jarOut.putNextEntry(new ZipEntry(nm));
							IOUtils.write(et.getValue(), jarOut);
							++cnt;
							if(cnt % 75 == 74) {
								System.out.print(".");
							}
						}
					}
				}
			}
			for(Entry<String,byte[]> etr : diffs.recreate.entrySet()) {
				jarOut.putNextEntry(new ZipEntry(etr.getKey()));
				IOUtils.write(etr.getValue(), jarOut);
				++crtCnt;
				++cnt;
				if(cnt % 75 == 74) {
					System.out.print(".");
				}
			}
		}

		System.out.println();
		System.out.println("Patched " + pthCnt + " files");
		System.out.println("Restored " + crtCnt + " files");
		System.out.println("Replaced " + repCnt + " files");
		System.out.println("Deleted " + delCnt + " files");
		System.out.println();
	}

}

package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;

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
public class LoadResources {

	public static boolean loadResources(File minecraftJarIn, File assetsIndexIn, File assetsJarOut, File tmpDir, File languagesZipOut) {
		System.out.println("Copying resources from '" + minecraftJarIn.getName() + "' into '" + assetsJarOut.getName() + "'");
		try(ZipOutputStream os = new ZipOutputStream(new FileOutputStream(assetsJarOut))) {
			os.setLevel(5);
			os.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			os.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
			try(ZipInputStream is = new ZipInputStream(new FileInputStream(minecraftJarIn))) {
				ZipEntry e;
				while((e = is.getNextEntry()) != null) {
					if(e.isDirectory()) {
						continue;
					}
					String zn = e.getName();
					if(zn.startsWith("/")) {
						zn = zn.substring(1);
					}
					if(zn.startsWith("META-INF") || zn.endsWith(".class")) {
						continue;
					}
					os.putNextEntry(e);
					IOUtils.copy(is, os, 4096);
				}
			}

			System.out.println();
			System.out.println("Reading 'assetsIndexTransformer.json'...");
			
			ResourceRulesList rules;
			
			try {
				rules = ResourceRulesList.loadResourceRules(new File(EaglerBuildTools.repositoryRoot, "mcp918/assetsIndexTransformer.json"));
			}catch(IOException ex) {
				System.err.println();
				System.err.println("ERROR: failed to read 'mcp918/assetsIndexTransformer.json'!");
				ex.printStackTrace();
				return false;
			}
			
			System.out.println();
			System.out.println("Reading asset index '" + assetsIndexIn.getAbsolutePath() + "'...");

			try(ZipOutputStream os2 = new ZipOutputStream(new FileOutputStream(languagesZipOut))) {
				os2.setLevel(5);
					
				try {
					JSONObject json = (new JSONObject(FileUtils.readFileToString(assetsIndexIn, StandardCharsets.UTF_8))).getJSONObject("objects");
					Iterator<String> itr = json.keys();
		
					System.out.println("Downloading assets from 'https://resources.download.minecraft.net/'...");
					
					while(itr.hasNext()) {
						String name = itr.next();
						JSONObject obj = json.getJSONObject(name);
						
						
						ResourceRulesList.ResourceRule r = rules.get(name);
						if(r.action == ResourceRulesList.Action.EXCLUDE) {
							System.out.println("Skipping file '" + name + "'");
							continue;
						}
						
						String hash = obj.getString("hash");
						int len = obj.getInt("size");
		
						System.out.println("Downloading '" + name + "' (" + formatByteLength(len) + ") ...");
						
						URL url;
						try {
							url = new URL("https://resources.download.minecraft.net/" + hash.substring(0, 2) + "/" + hash);
						}catch(MalformedURLException ex) {
							System.err.println("Resource file '" + name + "' had an invalid URL!");
							ex.printStackTrace();
							continue;
						}
						
						byte[] downloadedFile = new byte[len];
						
						try(InputStream is = url.openStream()) {
							int dl = 0;
							int i = 0;
							while(dl != len && (i = is.read(downloadedFile, dl, len - dl)) > 0) {
								dl += i;
							}
							int a = is.available();
							if(dl != len || a > 0) {
								throw new IOException("File '" + url.toString() + "' was the wrong length! " + (a > 0 ? "" + a + " bytes remaining" : "" + (len - dl) + " bytes missing"));
							}
						}catch(IOException ex) {
							System.err.println("Resource file '" + url.toString() + "' could not be downloaded!");
							ex.printStackTrace();
							continue;
						}
						
						if(r.action == ResourceRulesList.Action.ENCODE) {
							try {
								System.out.println(" - encoding ogg: " + (r.ffmpegSamples / 1000) + "kHz, " + r.ffmpegBitrate + "kbps, " + (r.ffmpegStereo ? "stereo" : "mono"));
								downloadedFile = FFMPEG.encodeOgg(tmpDir, downloadedFile, r.ffmpegSamples, r.ffmpegBitrate, r.ffmpegStereo);
							}catch(IOException ex) {
								System.err.println("Resource file '" + name + "' could not be encoded!");
								ex.printStackTrace();
								continue;
							}
						}else if(r.action == ResourceRulesList.Action.LANGUAGES_ZIP) {
							int j = name.lastIndexOf('/');
							if(j != -1) {
								name = name.substring(j + 1);
							}
							System.out.println(" - writing language '" + name + "' to '" + languagesZipOut.getName() + "'");
							os2.putNextEntry(new ZipEntry(name));
							os2.write(downloadedFile);
							continue;
						}
	
						os.putNextEntry(new ZipEntry("assets/" + name));
						os.write(downloadedFile);
					}
				}catch(IOException | JSONException ex) {
					System.err.println("ERROR: failed to download additional assets from '" + assetsIndexIn.getName() + "'!");
					ex.printStackTrace();
					return false;
				}
			}
		
		}catch(IOException ex) {
			System.err.println("ERROR: failed to copy from '" + minecraftJarIn.getName() + "' -> '" +assetsJarOut.getName() + "'!");
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private static String formatByteLength(int len) {
		if(len < 4096) {
			return "" + len;
		}else if(len < 1024 * 4096) {
			return "" + (len / 1024) + "k";
		}else  {
			return "" + (len / 1024 / 1024) + "M";
		}
	}
	
}

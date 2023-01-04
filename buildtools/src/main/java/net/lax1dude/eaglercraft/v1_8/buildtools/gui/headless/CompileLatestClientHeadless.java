package net.lax1dude.eaglercraft.v1_8.buildtools.gui.headless;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.LicensePrompt;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.EPKCompiler;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.JavaC;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.MakeOfflineDownload;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.CompileLatestClientGUI.CompileFailureException;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries.MissingJARsException;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.DecompileMinecraft;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.FFMPEG;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InitMCP;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge.TeaVMClassLoadException;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge.TeaVMRuntimeException;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileReaderUTF;
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
public class CompileLatestClientHeadless {

	public static void main(String[] args) throws Throwable {
		
		System.out.println();
		System.out.println("Launching client compiler...");
		System.out.println("Copyright (c) 2022-2023 lax1dude");
		System.out.println();
		
		boolean yes = false;
		String configPath = null;
		
		if(args.length == 1) {
			configPath = args[0];
		}else if(args.length == 2 && (yes = args[0].equalsIgnoreCase("-y"))) {
			configPath = args[1];
		}else {
			System.err.println("Usage: java -jar BuildTools.jar [-y] <config file>");
			System.err.println();
			System.exit(-1);
			return;
		}
		
		System.out.println("Loading config file: " + configPath);
		System.out.println();
		
		File configFile = new File(configPath);
		String configSrc;
		try {
			configSrc = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
			System.err.println();
			System.err.println("ERROR: File '" + configFile.getAbsolutePath() + "' does not exist!");
			System.err.println();
			System.exit(-1);
			return;
		}
		
		JSONObject configJSON;
		try {
			configJSON = new JSONObject(configSrc);
		}catch(JSONException ex) {
			System.err.println("ERROR: Could not parse '" + configFile.getName() + "' as JSON!");
			System.err.println();
			System.err.println(ex.toString());
			System.err.println();
			System.exit(-1);
			return;
		}
		
		File repositoryFolder;
		File modCoderPack;
		File minecraftJar;
		File assetsIndex;
		File outputDirectory;
		File temporaryDirectory;
		String ffmpeg = "ffmpeg";
		String mavenURL = null;
		File mavenLocal = null;
		File productionIndex = null;
		File productionFavicon = null;
		List<String> addScripts = null;
		List<String> removeScripts = null;
		List<String> injectInOfflineScripts = null;
		boolean generateOffline;
		File offlineTemplate = null;
		boolean keepTemporaryFiles;
		boolean writeSourceMap = false;
		boolean minifying = true;
		try {
			repositoryFolder = new File(configJSON.optString("repositoryFolder", "."));
			modCoderPack = new File(configJSON.getString("modCoderPack"));
			minecraftJar = new File(configJSON.getString("minecraftJar"));
			assetsIndex = new File(configJSON.getString("assetsIndex"));
			outputDirectory = new File(configJSON.getString("outputDirectory"));
			String tmpDir = configJSON.optString("temporaryDirectory");
			temporaryDirectory = tmpDir == null ? new File(outputDirectory, "build") : new File(tmpDir);
			ffmpeg = configJSON.optString("ffmpeg", ffmpeg);
			if(ffmpeg.length() == 0) {
				ffmpeg = "ffmpeg";
			}
			String prodIndex = configJSON.optString("productionIndex");
			if(prodIndex != null) {
				productionIndex = new File(prodIndex);
				String prodFavicon = configJSON.optString("productionFavicon");
				if(prodFavicon != null) {
					productionFavicon = new File(prodFavicon);
				}
				JSONArray scripts = configJSON.optJSONArray("addScripts");
				if(scripts != null) {
					int l = scripts.length();
					if(l > 0) {
						addScripts = new ArrayList(l);
						for(int i = 0; i < l; ++i) {
							addScripts.add(scripts.getString(i));
						}
					}
				}
				scripts = configJSON.optJSONArray("removeScripts");
				if(scripts != null) {
					int l = scripts.length();
					if(l > 0) {
						removeScripts = new ArrayList(l);
						for(int i = 0; i < l; ++i) {
							removeScripts.add(scripts.getString(i));
						}
					}
				}
				scripts = configJSON.optJSONArray("injectInOffline");
				if(scripts != null) {
					int l = scripts.length();
					if(l > 0) {
						injectInOfflineScripts = new ArrayList(l);
						for(int i = 0; i < l; ++i) {
							injectInOfflineScripts.add(scripts.getString(i));
						}
					}
				}
			}
			mavenURL = configJSON.optString("mavenURL");
			mavenLocal = new File(configJSON.getString("mavenLocal"));
			generateOffline = configJSON.optBoolean("generateOfflineDownload", false);
			if(generateOffline) {
				offlineTemplate = new File(configJSON.getString("offlineDownloadTemplate"));
			}
			keepTemporaryFiles = configJSON.optBoolean("keepTemporaryFiles", false);
			writeSourceMap = configJSON.optBoolean("writeSourceMap", false);
			minifying = configJSON.optBoolean("minifying", true);
		}catch(JSONException ex) {
			System.err.println("CONFIG ERROR: " + ex.toString());
			System.err.println();
			System.exit(-1);
			return;
		}
		
		System.out.println("Loaded config successfully:");
		System.out.println();
		System.out.println(" - Repository Folder: " + repositoryFolder.getAbsolutePath().replace('\\', '/'));
		System.out.println(" - Mod Coder Pack: " + modCoderPack.getAbsolutePath().replace('\\', '/'));
		System.out.println(" - Minecraft 1.8.8: " + minecraftJar.getAbsolutePath().replace('\\', '/'));
		System.out.println(" - Assets Index 1.8: " + assetsIndex.getAbsolutePath().replace('\\', '/'));
		System.out.println(" - Temporary Directory: " + temporaryDirectory.getAbsolutePath().replace('\\', '/'));
		System.out.println(" - Output Directory: " + outputDirectory.getAbsolutePath().replace('\\', '/'));
		System.out.println(" - FFmpeg Executable: " + ffmpeg.replace('\\', '/'));
		System.out.println(" - Maven Repo URL: " + mavenURL);
		System.out.println(" - Maven Local Dir: " + mavenLocal.getAbsolutePath().replace('\\', '/'));
		System.out.println(" - Production Index: " + (productionIndex == null ? "null" : productionIndex.getAbsolutePath().replace('\\', '/')));
		System.out.println(" - Production Favicon: " + (productionFavicon == null ? "null" : productionFavicon.getAbsolutePath().replace('\\', '/')));
		System.out.println(" - Generate Offline: " + generateOffline);
		System.out.println(" - Offline Template: " + (offlineTemplate == null ? "null" : offlineTemplate.getAbsolutePath().replace('\\', '/')));
		System.out.println(" - Inject in Offline: " + (injectInOfflineScripts == null ? "[ ]" : "[ " + String.join(", ", injectInOfflineScripts).replace('\\', '/') + " ]"));
		System.out.println(" - Minifying: " + minifying);
		System.out.println(" - Write Source Map: " + writeSourceMap);
		System.out.println(" - Keep Temp Files: " + keepTemporaryFiles);
		System.out.println(" - Add Scripts: " + (addScripts == null ? "[ ]" : "[ " + String.join(", ", addScripts).replace('\\', '/') + " ]"));
		System.out.println(" - Remove Scripts: " + (removeScripts == null ? "[ ]" : "[ " + String.join(", ", removeScripts).replace('\\', '/') + " ]"));
		System.out.println();
		
		if(!yes) {
			System.out.println();
			LicensePrompt.display();
			System.out.println();
		}
		
		EaglerBuildTools.repositoryRoot = repositoryFolder;
		
		try {
			if(!outputDirectory.isDirectory() && !outputDirectory.mkdirs()) {
				throw new CompileFailureException("Could not create output directory!");
			}
			
			File[] existingOutput = outputDirectory.listFiles();
			if(existingOutput.length > 0) {
				
				if(!yes) {
					System.out.print("Output directory has existing files, would you like to delete them? [y/n] ");
					String str = (new BufferedReader(new InputStreamReader(System.in))).readLine();
					System.out.println();
					if(!str.equalsIgnoreCase("y") && !str.equalsIgnoreCase("yes")) {
						System.out.println("Build cancelled.");
						System.out.println();
						System.exit(-1);
						return;
					}
				}
				
				System.out.println("Deleting existing files from the output directory...");
				
				try {
					for(int i = 0; i < existingOutput.length; ++i) {
						File f = existingOutput[i];
						if(f.isDirectory()) {
							FileUtils.deleteDirectory(f);
						}else {
							if(!f.delete()) {
								throw new IOException("Could not delete: " + f.getAbsolutePath());
							}
						}
					}
				}catch(IOException t) {
					throw new CompileFailureException("Could not delete old output directory: " + t.getMessage());
				}
			}
			
			File mcpDataTMP = new File(temporaryDirectory, "ModCoderPack");
			File minecraftSrcTmp = new File(temporaryDirectory, "MinecraftSrc");
			
			if(ffmpeg.length() == 0) {
				FFMPEG.foundFFMPEG = "ffmpeg";
			}else {
				FFMPEG.foundFFMPEG = ffmpeg;
			}
			
			if(!mcpDataTMP.isDirectory() && !mcpDataTMP.mkdirs()) {
				throw new CompileFailureException("Error: failed to create \"" + mcpDataTMP.getAbsolutePath() + "\"!");
			}
			
			if(!InitMCP.initTask(modCoderPack, mcpDataTMP)) {
				throw new CompileFailureException("Error: could not initialize MCP from \"" + modCoderPack.getAbsolutePath() + "\"!");
			}
			
			if(!minecraftSrcTmp.isDirectory() && !minecraftSrcTmp.mkdirs()) {
				throw new CompileFailureException("Error: failed to create \"" + minecraftSrcTmp.getAbsolutePath() + "\"!");
			}
			
			if(!DecompileMinecraft.decompileMinecraft(mcpDataTMP, minecraftJar, minecraftSrcTmp, assetsIndex, false)) {
				throw new CompileFailureException("Error: could not decompile and patch 1.8.8.jar from \"" + minecraftJar.getAbsolutePath() + "\"!");
			}
			
			try {
				FileUtils.copyFile(new File(repositoryFolder, "patches/minecraft/output_license.txt"), new File(temporaryDirectory, "MinecraftSrc/LICENSE"));
			}catch(IOException ex) {
				System.err.println("Error: failed to write LICENSE in temporary directory!");
				ex.printStackTrace();
			}
			
			System.out.println();
			
			if(mavenURL == null) {
				System.out.println("TeaVM JARs will be loaded from: " + mavenLocal.getAbsolutePath());
				System.out.println();
				try {
					TeaVMBinaries.loadFromDirectory(mavenLocal);
				}catch(MissingJARsException ex) {
					throw new CompileFailureException(ex.getMessage());
				}
			}else {
				System.out.println("TeaVM JARs will be downloaded from repository: " + mavenURL);
				System.out.println();
				try {
					TeaVMBinaries.downloadFromMaven(mavenURL, mavenLocal);
				}catch(MissingJARsException ex) {
					throw new CompileFailureException(ex.getMessage());
				}
				System.out.println();
				System.out.println("Notice: make sure to delete \"" + mavenLocal.getAbsolutePath() + "\" when the compiler is finished, it will not be deleted automatically");
				System.out.println();
			}
			
			int compileResultCode;
			File compiledResultClasses = new File(temporaryDirectory, "classes");

			try {
				try {
					compileResultCode = JavaC.runJavaC(new File(minecraftSrcTmp, "minecraft_src_javadoc.jar"),
							compiledResultClasses, temporaryDirectory, TeaVMBinaries.getTeaVMRuntimeClasspath(),
						new File(repositoryFolder, "sources/main/java"), new File(repositoryFolder, "sources/teavm/java"));
				}catch(IOException ex) {
					throw new CompileFailureException("failed to run javac compiler! " + ex.toString(), ex);
				}
		
				System.out.println();
				
				if(compileResultCode == 0) {
					System.out.println("Java compiler completed successfully");
				}else {
					throw new CompileFailureException("failed to run javac compiler! exit code " + compileResultCode + ", check log");
				}
			}finally {
				File extractedSrcTmp = new File(temporaryDirectory, "MinecraftSrc/src_javadoc_tmp");
				if(extractedSrcTmp.exists()) {
					System.out.println();
					System.out.println("Deleting temporary directory: " + extractedSrcTmp.getAbsolutePath());
					try {
						FileUtils.deleteDirectory(extractedSrcTmp);
					}catch(IOException ex) {
						System.err.println("Failed to delete temporary directory!");
						ex.printStackTrace();
					}
				}
			}
			
			System.out.println();
			System.out.println("Preparing arguments for TeaVM...");
			
			if(!TeaVMBinaries.tryLoadTeaVMBridge()) {
				System.err.println("Failed to locate TeaVMBridge.jar, you can specify it's path manually by adding the JVM argument \"-Deaglercraft.TeaVMBridge=<path>\"");
				throw new CompileFailureException("Failed to locate TeaVMBridge.jar!");
			}
			
			Map<String, Object> teavmArgs = new HashMap();
			
			List<String> teavmClassPath = new ArrayList();
			teavmClassPath.add(compiledResultClasses.getAbsolutePath());
			teavmClassPath.addAll(Arrays.asList(TeaVMBinaries.getTeaVMRuntimeClasspath()));
			teavmArgs.put("classPathEntries", teavmClassPath);

			teavmArgs.put("entryPointName", "main");
			teavmArgs.put("mainClass", "net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass");
			teavmArgs.put("minifying", minifying);
			teavmArgs.put("optimizationLevel", "ADVANCED");
			teavmArgs.put("targetDirectory", outputDirectory.getAbsolutePath());
			teavmArgs.put("generateSourceMaps", writeSourceMap);
			teavmArgs.put("targetFileName", "classes.js");
			
			System.out.println();
			
			boolean teavmStatus;
			try {
				teavmStatus = TeaVMBridge.compileTeaVM(teavmArgs);
			}catch(TeaVMClassLoadException ex) {
				throw new CompileFailureException("Failed to link TeaVM jar files! Did you select the wrong jar?", ex);
			}catch(TeaVMRuntimeException ex) {
				throw new CompileFailureException("Failed to run TeaVM! Check log", ex);
			}
			
			if(!teavmStatus) {
				System.out.println("TeaVM reported problems, check the log");
				System.out.println();
				System.exit(-1);
				return;
			}
			
			File epkCompiler = new File(repositoryFolder, "sources/setup/workspace_template/desktopRuntime/CompileEPK.jar");
			
			if(!epkCompiler.exists()) {
				throw new CompileFailureException("EPKCompiler JAR file is missing: " + epkCompiler.getAbsolutePath());
			}

			System.out.println();
			System.out.println("Writing index.html...");
			System.out.println();
			
			String faviconExt = null;
			if(productionFavicon != null) {
				faviconExt = productionFavicon.getName();
				int i = faviconExt.lastIndexOf('.');
				if(i != -1) {
					faviconExt = faviconExt.substring(i + 1);
				}
			}
			
			try(BufferedReader indexReader = new BufferedReader(new FileReaderUTF(productionIndex));
					PrintWriter indexWriter = new PrintWriter(new FileWriterUTF(new File(outputDirectory, "index.html")))) {
				String line;
				while((line = indexReader.readLine()) != null) {
					String trim = line.trim();
					if(trim.startsWith("<link")) {
						if(trim.contains("rel=\"shortcut icon\"")) {
							if(faviconExt != null) {
								String contentType = "image/png";
								switch(faviconExt) {
								case "png":
									break;
								case "jpg":
								case "jpeg":
									contentType = "image/jpeg";
									break;
								case "ico":
									contentType = "image/x-icon";
									break;
								case "gif":
									contentType = "image/gif";
									break;
								case "bmp":
									contentType = "image/bmp";
									break;
								case "webp":
									contentType = "image/webp";
									break;
								default:
									System.err.println();
									System.err.println("WARNING: favicon extension '" + faviconExt + "' is unknown, defaulting to image/png MIME type");
									System.err.println();
									break;
								}
								indexWriter.println(line.replace("favicon.png", "favicon." + faviconExt).replace("image/png", contentType));
								System.out.println("Setting favicon <link> href to \"favicon." + faviconExt + "\", MIME type \"" + contentType + "\" in index.html");
							}else {
								System.out.println("Removed favicon <link> from index.html, no favicon configured");
							}
							continue;
						}
					}
					if(trim.startsWith("<meta")) {
						if(trim.contains("property=\"og:image\"")) {
							if(faviconExt != null) {
								indexWriter.println(line.replace("favicon.png", "favicon." + faviconExt));
								System.out.println("Setting og:image <link> href to \"favicon." + faviconExt + "\"");
							}else {
								System.out.println("Removed og:image <meta> tag in index.html, no favicon configured");
							}
							continue;
						}
					}
					if(trim.startsWith("<script")) {
						int idx = line.indexOf("src=\"");
						int idx2 = line.indexOf('"', idx + 5);
						String srcSubStr = line.substring(idx + 5, idx2);
						if(addScripts != null && srcSubStr.equals("classes.js")) {
							for(int i = 0, l = addScripts.size(); i < l; ++i) {
								String addSrc = addScripts.get(i);
								indexWriter.println(line.replace("classes.js", addSrc));
								System.out.println("Added <script> tag with src \"" + addSrc + "\" to index.html");
							}
						}
						if(removeScripts != null && removeScripts.contains(srcSubStr)) {
							System.out.println("Removed <script> tag with src \"" + srcSubStr + "\" from index.html");
							continue;
						}
					}
					indexWriter.println(line);
				}
			}
			
			System.out.println();
			
			if(productionFavicon != null) {
				FileUtils.copyFile(productionFavicon, new File(outputDirectory, "favicon." + faviconExt));
			}
			
			FileUtils.copyFile(new File(repositoryFolder, "sources/setup/workspace_template/javascript/fix-webm-duration.js"), new File(outputDirectory, "fix-webm-duration.js"));
			
			System.out.println();
			System.out.println("Running EPKCompiler on assets...");
			
			EPKCompiler.compilerMain(epkCompiler, new String[] {
					((new File(minecraftSrcTmp, "minecraft_res_patch.jar")).getAbsolutePath() + System.getProperty("path.separator") +
					(new File(repositoryFolder, "sources/resources")).getAbsolutePath()), (new File(outputDirectory, "assets.epk")).getAbsolutePath() });
			
			System.out.println();
			System.out.println("Running EPKCompiler on languages.zip...");
			
			EPKCompiler.compilerMain(epkCompiler, new String[] {
					(new File(minecraftSrcTmp, "minecraft_languages.zip")).getAbsolutePath(),
					(new File(temporaryDirectory, "languages.epk")).getAbsolutePath() });

			System.out.println();
			System.out.println("Creating languages directory...");
			File langDirectory = new File(outputDirectory, "lang");
			
			byte[] copyBuffer = new byte[16384];
			int i;
			try(ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(minecraftSrcTmp, "minecraft_languages.zip")))) {
				ZipEntry etr;
				while((etr = zis.getNextEntry()) != null) {
					if(!etr.isDirectory()) {
						File phile = new File(langDirectory, etr.getName());
						File parent = phile.getParentFile();
						if(!parent.exists() && !parent.mkdirs()) {
							throw new IOException("Could not create directory: " + parent.getAbsolutePath());
						}
						try(FileOutputStream os = new FileOutputStream(phile)) {
							while((i = zis.read(copyBuffer)) != -1) {
								os.write(copyBuffer, 0, i);
							}
						}
					}
				}
			}
			
			System.out.println();
			
			if(generateOffline) {
				System.out.println("Running offline download generator...");
				System.out.println();
				File offlineTemplateArg = offlineTemplate;
				if(injectInOfflineScripts != null) {
					offlineTemplateArg = new File(temporaryDirectory, "offline_download_template.txt");
					try(BufferedReader indexReader = new BufferedReader(new FileReaderUTF(offlineTemplate));
							PrintWriter indexWriter = new PrintWriter(new FileWriterUTF(offlineTemplateArg))) {
						String line;
						while((line = indexReader.readLine()) != null) {
							if(line.contains("${classes_js}")) {
								for(int j = 0, l = injectInOfflineScripts.size(); j < l; ++j) {
									File injectFile = new File(injectInOfflineScripts.get(j));
									String injectName = injectFile.getAbsolutePath();
									String injectNameName = injectFile.getName();
									System.out.println("Adding file to offline download template: " + injectName);
									indexWriter.println("// %%%%%%%%% " + injectNameName + " %%%%%%%%%");
									indexWriter.println();
									try(BufferedReader insertReader = new BufferedReader(new FileReaderUTF(injectFile))) {
										String line2;
										while((line2 = insertReader.readLine()) != null) {
											indexWriter.println(line2);
										}
									}
									indexWriter.println();
									char[] percents = new char[20 + injectNameName.length()];
									for(int k = 0; k < percents.length; ++k) {
										percents[k] = '%';
									}
									indexWriter.print("// ");
									indexWriter.println(percents);
									indexWriter.println();
									indexWriter.println();
								}
								System.out.println();
							}
							indexWriter.println(line);
						}
					}
				}
				File offlineDownloadGenerator = new File(repositoryFolder, "sources/setup/workspace_template/desktopRuntime/MakeOfflineDownload.jar");
				MakeOfflineDownload.compilerMain(offlineDownloadGenerator, new String[] {
						offlineTemplateArg.getAbsolutePath(),
						(new File(outputDirectory, "classes.js")).getAbsolutePath() + System.getProperty("path.separator")
							+ (new File(outputDirectory, "fix-webm-duration.js")).getAbsolutePath(),
						(new File(outputDirectory, "assets.epk")).getAbsolutePath(),
						(new File(outputDirectory, "EaglercraftX_1.8_Offline_en_US.html")).getAbsolutePath(),
						(new File(outputDirectory, "EaglercraftX_1.8_Offline_International.html")).getAbsolutePath(), 
						(new File(temporaryDirectory, "languages.epk")).getAbsolutePath()
				});
			}

			System.out.println("Releasing external ClassLoader(s)...");
			System.out.println();

			TeaVMBridge.free();
			EPKCompiler.free();
			
			if(generateOffline) {
				MakeOfflineDownload.free();
			}
			
			if(!keepTemporaryFiles) {
				System.out.println("Cleaning up temporary files...");
				try {
					FileUtils.deleteDirectory(temporaryDirectory);
				}catch(IOException ex) {
					System.err.println("Failed to delete temporary directory: " + temporaryDirectory.getAbsolutePath());
					ex.printStackTrace();
				}
			}
			
			System.out.println();
			System.out.println("Client build successful! Check the output directory for your files");
			
		}catch(CompileFailureException ex) {
			System.out.println();
			System.err.println("COMPILATION FAILED: " + ex.getMessage());
			System.out.println();
			System.exit(-1);
		}
	}

}

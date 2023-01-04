package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries.MissingJARsException;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.DecompileMinecraft;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.FFMPEG;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InitMCP;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge.TeaVMClassLoadException;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridge.TeaVMRuntimeException;

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
public class CompileLatestClientGUI {

	public static CompileLatestClientFrame frame = null;

	public static void main(String[] args) {
		System.out.println();
		System.out.println("Launching client compiler wizard...");
		System.out.println("Copyright (c) 2022-2023 lax1dude");
		System.out.println();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					System.err.println("Could not set system look and feel: " + e.toString());
				}
				if(!System.getProperty("eaglercraft.isJava11", "false").equalsIgnoreCase("true")) {
					try {
						if (!(boolean) Class
								.forName("net.lax1dude.eaglercraft.v1_8.buildtools.Java11Check", true,
										new URLClassLoader(new URL[] { (new File("buildtools/Java11Check.jar")).toURI().toURL() }))
								.getMethod("classLoadCheck").invoke(null)) {
							throw new RuntimeException("wtf?");
						}
					}catch(Throwable t) {
						JOptionPane.showMessageDialog(null, "Error: Java 11 is required to run this program", "Unsupported JRE", JOptionPane.ERROR_MESSAGE);
						System.exit(-1);
						return;
					}
				}
				frame = new CompileLatestClientFrame();
				frame.frmCompileLatestClient.setLocationRelativeTo(null);
				frame.frmCompileLatestClient.setVisible(true);
				System.out.println("you eagler");
				System.out.println();
				frame.launchLogUpdateThread();
				System.setOut(new PrintStream(new ConsoleRedirector(false)));
				System.setErr(new PrintStream(new ConsoleRedirector(true)));
				if(JavaC.jdkHome == null) {
					if(JOptionPane.showConfirmDialog(frame.frmCompileLatestClient, "Error: A JDK is required to run this program!\nYou are currently running on a JRE\nDo you have a JDK installed that you would like to use instead?", "Unsupported JRE", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						JOptionPane.showMessageDialog(frame.frmCompileLatestClient, "You need at least JDK 8 to compile EaglercraftX 1.8!\nSelect the path to the installation you want to use", "Unsupported JRE", JOptionPane.INFORMATION_MESSAGE);
						JFileChooser fileChooser = new JFileChooser((new File(System.getProperty("java.home"))).getParentFile());
						fileChooser.setMultiSelectionEnabled(false);
						fileChooser.setFileHidingEnabled(false);
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						while(true) {
							if(fileChooser.showOpenDialog(frame.frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
								File f = fileChooser.getSelectedFile();
								if(JavaC.windows ? (new File(f, "bin/javac.exe")).exists() : (new File(f, "bin/javac")).canExecute()) {
									break;
								}else {
									if(JOptionPane.showConfirmDialog(frame.frmCompileLatestClient, "Could not find a java compiler in this directory!\nWould you like to try again?", "Unsupported JRE", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
										continue;
									}
								}
							}
							JOptionPane.showMessageDialog(frame.frmCompileLatestClient, "Please install JDK 8 or newer to continue", "Unsupported JRE", JOptionPane.ERROR_MESSAGE);
							System.exit(-1);
							return;
						}
						JavaC.jdkHome = fileChooser.getSelectedFile();
						JOptionPane.showMessageDialog(frame.frmCompileLatestClient, "The JDK \"" + JavaC.jdkHome.getAbsolutePath() + "\" will be used to compile EaglercraftX", "Unsupported JRE", JOptionPane.INFORMATION_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(frame.frmCompileLatestClient, "Please install a JDK and re-launch this program", "Unsupported JRE", JOptionPane.ERROR_MESSAGE);
						System.exit(-1);
					}
				}
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						frame.scrollPane_LicenseText.getVerticalScrollBar().setValue(0);
					}
				});
			}
		});
	}

	public static class CompileFailureException extends RuntimeException {
		public CompileFailureException(String msg) {
			super(msg);
		}
		public CompileFailureException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}

	public static void runCompiler() {
		try {
			runCompiler0();
		}catch(CompileFailureException t) {
			System.out.println();
			System.err.println("Error: " + t.getMessage());
			t.printStackTrace();
			frame.finishCompiling(true, t.getMessage());
			return;
		}catch(Throwable t) {
			System.out.println();
			System.err.println("Error: unhandled exception caught while compiling!");
			t.printStackTrace();
			frame.finishCompiling(true, t.toString());
			return;
		}
		if(!frame.finished) {
			System.out.println();
			System.err.println("Error: compilation finished with unknown status!");
			frame.finishCompiling(true, "Compilation finished with unknown status");
		}
	}

	private static void runCompiler0() throws Throwable {
		File repositoryFolder = new File(frame.textField_RepositoryPath.getText().trim());
		EaglerBuildTools.repositoryRoot = repositoryFolder;
		File modCoderPack = new File(frame.textField_ModCoderPack.getText().trim());
		File minecraftJar = new File(frame.textField_JarFilePath.getText().trim());
		File assetsIndex = new File(frame.textField_AssetsIndexJSON.getText().trim());
		File outputDirectory = new File(frame.textField_OutputDirectory.getText().trim());
		File temporaryDirectory = new File(outputDirectory, "build");
		
		File[] existingOutput = outputDirectory.listFiles();
		if(existingOutput.length > 0) {
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
		
		String ffmpeg = frame.chckbxUsePathFFmpeg.isSelected() ? "" : frame.textField_pathToFFmpeg.getText().trim();
		if(ffmpeg.length() == 0) {
			FFMPEG.foundFFMPEG = "ffmpeg";
		}else {
			FFMPEG.foundFFMPEG = ffmpeg;
		}
		
		String mavenRepositoryURL = frame.getRepositoryURL();
		File mavenRepositoryFolder = null;
		if(mavenRepositoryURL == null) {
			mavenRepositoryFolder = new File(frame.textField_MavenRepoLocal.getText().trim());
		}

		boolean generateOfflineDownload = frame.chckbxOutputOfflineDownload.isSelected();
		boolean keepTemporaryFiles = frame.chckbxKeepTemporaryFiles.isSelected();
		
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
		
		if(frame.rdbtnMavenRepoLocal.isSelected()) {
			System.out.println("TeaVM JARs will be loaded from: " + frame.textField_MavenRepoLocal.getText());
		}else {
			String url = frame.getRepositoryURL();
			System.out.println("TeaVM JARs will be downloaded from repository: " + url);
			System.out.println();
			try {
				TeaVMBinaries.downloadFromMaven(url, new File("##TEAVM.TMP##"));
			}catch(MissingJARsException ex) {
				throw new CompileFailureException(ex.getMessage());
			}
		}

		System.out.println();
		
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
		teavmArgs.put("minifying", true);
		teavmArgs.put("optimizationLevel", "ADVANCED");
		teavmArgs.put("targetDirectory", outputDirectory.getAbsolutePath());
		teavmArgs.put("generateSourceMaps", true);
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
			frame.finishCompiling(true, "TeaVM reported problems, check the log");
			return;
		}
		
		File epkCompiler = new File(repositoryFolder, "sources/setup/workspace_template/desktopRuntime/CompileEPK.jar");
		
		if(!epkCompiler.exists()) {
			throw new CompileFailureException("EPKCompiler JAR file is missing: " + epkCompiler.getAbsolutePath());
		}

		System.out.println();
		System.out.println("Writing default index.html...");

		FileUtils.copyFile(new File(repositoryFolder, "buildtools/production-index.html"), new File(outputDirectory, "index.html"));
		FileUtils.copyFile(new File(repositoryFolder, "buildtools/production-favicon.png"), new File(outputDirectory, "favicon.png"));
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
		
		if(generateOfflineDownload) {
			System.out.println("Running offline download generator...");
			System.out.println();
			File offlineDownloadGenerator = new File(repositoryFolder, "sources/setup/workspace_template/desktopRuntime/MakeOfflineDownload.jar");
			MakeOfflineDownload.compilerMain(offlineDownloadGenerator, new String[] {
					(new File(repositoryFolder, "sources/setup/workspace_template/javascript/OfflineDownloadTemplate.txt")).getAbsolutePath(),
					(new File(outputDirectory, "classes.js")).getAbsolutePath() + System.getProperty("path.separator")
						+ (new File(outputDirectory, "fix-webm-duration.js")).getAbsolutePath(),
					(new File(outputDirectory, "assets.epk")).getAbsolutePath(),
					(new File(outputDirectory, "EaglercraftX_1.8_Offline_en_US.html")).getAbsolutePath(),
					(new File(outputDirectory, "EaglercraftX_1.8_Offline_International.html")).getAbsolutePath(), 
					(new File(outputDirectory, "build/languages.epk")).getAbsolutePath()
			});
		}

		System.out.println("Releasing external ClassLoader(s)...");
		System.out.println();

		TeaVMBridge.free();
		EPKCompiler.free();
		
		if(generateOfflineDownload) {
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
		
		try {
			Desktop.getDesktop().open(outputDirectory);
		}catch(Throwable t) {
		}
		
		frame.finishCompiling(false, "");
	}
}

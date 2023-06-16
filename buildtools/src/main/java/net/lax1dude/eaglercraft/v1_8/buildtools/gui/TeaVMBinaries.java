package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

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
public class TeaVMBinaries {

	public static final String teavmCoreJar = "teavm-core-0.6.1.jar";
	public static final String teavmCoreMaven = "org/teavm/teavm-core/0.6.1/teavm-core-0.6.1.jar";
	public static File teavmCore = null;

	public static final String teavmCliJar = "teavm-cli-0.6.1.jar";
	public static final String teavmCliMaven = "org/teavm/teavm-cli/0.6.1/teavm-cli-0.6.1.jar";
	public static File teavmCli = null;

	public static final String teavmToolingJar = "teavm-tooling-0.6.1.jar";
	public static final String teavmToolingMaven = "org/teavm/teavm-tooling/0.6.1/teavm-tooling-0.6.1.jar";
	public static File teavmTooling = null;

	public static final String teavmPlatformJar = "teavm-platform-0.6.1.jar";
	public static final String teavmPlatformMaven = "org/teavm/teavm-platform/0.6.1/teavm-platform-0.6.1.jar";
	public static File teavmPlatform = null;

	public static final String teavmClasslibJar = "teavm-classlib-0.6.1.jar";
	public static final String teavmClasslibMaven = "org/teavm/teavm-classlib/0.6.1/teavm-classlib-0.6.1.jar";
	public static File teavmClasslib = null;

	public static final String teavmInteropJar = "teavm-interop-0.6.1.jar";
	public static final String teavmInteropMaven = "org/teavm/teavm-interop/0.6.1/teavm-interop-0.6.1.jar";
	public static File teavmInterop = null;

	public static final String teavmJSOJar = "teavm-jso-0.6.1.jar";
	public static final String teavmJSOMaven = "org/teavm/teavm-jso/0.6.1/teavm-jso-0.6.1.jar";
	public static File teavmJSO = null;

	public static final String teavmJSOApisJar = "teavm-jso-apis-0.6.1.jar";
	public static final String teavmJSOApisMaven = "org/teavm/teavm-jso-apis/0.6.1/teavm-jso-apis-0.6.1.jar";
	public static File teavmJSOApis = null;

	public static final String teavmJSOImplJar = "teavm-jso-impl-0.6.1.jar";
	public static final String teavmJSOImplMaven = "org/teavm/teavm-jso-impl/0.6.1/teavm-jso-impl-0.6.1.jar";
	public static File teavmJSOImpl = null;

	public static final String teavmMetaprogrammingAPIJar = "teavm-metaprogramming-api-0.6.1.jar";
	public static final String teavmMetaprogrammingAPIMaven = "org/teavm/teavm-metaprogramming-api/0.6.1/teavm-metaprogramming-api-0.6.1.jar";
	public static File teavmMetaprogrammingAPI = null;

	public static final String teavmMetaprogrammingImplJar = "teavm-metaprogramming-impl-0.6.1.jar";
	public static final String teavmMetaprogrammingImplMaven = "org/teavm/teavm-metaprogramming-impl/0.6.1/teavm-metaprogramming-impl-0.6.1.jar";
	public static File teavmMetaprogrammingImpl = null;

	public static final String teavmJodaTimeJar = "joda-time-2.7.jar";
	public static final String teavmJodaTimeMaven = "joda-time/joda-time/2.7/joda-time-2.7.jar";
	public static File teavmJodaTime = null;

	public static final String teavmJZLIBJar = "jzlib-1.1.3.jar";
	public static final String teavmJZLIBMaven = "com/jcraft/jzlib/1.1.3/jzlib-1.1.3.jar";
	public static File teavmJZLIB = null;

	public static File teavmBridge = null;

	public static class MissingJARsException extends RuntimeException {
		
		public final List<String> jars;
		
		public MissingJARsException(String msg, List<String> jars) {
			super(msg);
			this.jars = jars;
		}
		
		public MissingJARsException(List<String> jars) {
			this("The following JAR files were not found: " + String.join(", ", jars), jars);
		}
		
	}

	public static void downloadFromMaven(String url, File outputDir) throws MissingJARsException {
		teavmCore = teavmPlatform = teavmClasslib = teavmInterop = teavmJSO = 
		teavmJSOApis = teavmJSOImpl = teavmMetaprogrammingAPI = teavmMetaprogrammingImpl =
		teavmJodaTime = teavmJZLIB = teavmTooling = teavmCli = null;
		
		if(url.lastIndexOf('/') != url.length() - 1) {
			url += "/";
		}
		
		String urlConc = url + teavmCoreMaven;
		try {
			File f = new File(outputDir, teavmCoreJar);
			copyURLToFileCheck404(urlConc, f);
			teavmCore = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmCliMaven;
		try {
			File f = new File(outputDir, teavmCliJar);
			copyURLToFileCheck404(urlConc, f);
			teavmCli = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmToolingMaven;
		try {
			File f = new File(outputDir, teavmToolingJar);
			copyURLToFileCheck404(urlConc, f);
			teavmTooling = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmPlatformMaven;
		try {
			File f = new File(outputDir, teavmPlatformJar);
			copyURLToFileCheck404(urlConc, f);
			teavmPlatform = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmClasslibMaven;
		try {
			File f = new File(outputDir, teavmClasslibJar);
			copyURLToFileCheck404(urlConc, f);
			teavmClasslib = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmInteropMaven;
		try {
			File f = new File(outputDir, teavmInteropJar);
			copyURLToFileCheck404(urlConc, f);
			teavmInterop = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmJSOMaven;
		try {
			File f = new File(outputDir, teavmJSOJar);
			copyURLToFileCheck404(urlConc, f);
			teavmJSO = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmJSOApisMaven;
		try {
			File f = new File(outputDir, teavmJSOApisJar);
			copyURLToFileCheck404(urlConc, f);
			teavmJSOApis = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmJSOImplMaven;
		try {
			File f = new File(outputDir, teavmJSOImplJar);
			copyURLToFileCheck404(urlConc, f);
			teavmJSOImpl = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmMetaprogrammingAPIMaven;
		try {
			File f = new File(outputDir, teavmMetaprogrammingAPIJar);
			copyURLToFileCheck404(urlConc, f);
			teavmMetaprogrammingAPI = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmMetaprogrammingImplMaven;
		try {
			File f = new File(outputDir, teavmMetaprogrammingImplJar);
			copyURLToFileCheck404(urlConc, f);
			teavmMetaprogrammingImpl = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmJodaTimeMaven;
		try {
			File f = new File(outputDir, teavmJodaTimeJar);
			copyURLToFileCheck404(urlConc, f);
			teavmJodaTime = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
		urlConc = url + teavmJZLIBMaven;
		try {
			File f = new File(outputDir, teavmJZLIBJar);
			copyURLToFileCheck404(urlConc, f);
			teavmJZLIB = f;
		}catch(IOException ex) {
			System.err.println("Could not download JAR: " + urlConc);
			ex.printStackTrace();
			throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
		}
		
	}

	public static void loadFromDirectory(File directory) throws MissingJARsException {
		teavmCore = teavmPlatform = teavmClasslib = teavmInterop = teavmJSO = 
		teavmJSOApis = teavmJSOImpl = teavmMetaprogrammingAPI = teavmMetaprogrammingImpl =
		teavmJodaTime = teavmJZLIB = teavmTooling = teavmCli = null;
		discoverJars(directory);
		List<String> missingJars = new ArrayList();
		if(teavmCore == null) {
			missingJars.add(teavmCoreJar);
		}
		if(teavmCli == null) {
			missingJars.add(teavmCliJar);
		}
		if(teavmTooling == null) {
			missingJars.add(teavmToolingJar);
		}
		if(teavmPlatform == null) {
			missingJars.add(teavmPlatformJar);
		}
		if(teavmClasslib == null) {
			missingJars.add(teavmClasslibJar);
		}
		if(teavmInterop == null) {
			missingJars.add(teavmInteropJar);
		}
		if(teavmJSO == null) {
			missingJars.add(teavmJSOJar);
		}
		if(teavmJSOApis == null) {
			missingJars.add(teavmJSOApisJar);
		}
		if(teavmJSOImpl == null) {
			missingJars.add(teavmJSOImplJar);
		}
		if(teavmMetaprogrammingAPI == null) {
			missingJars.add(teavmMetaprogrammingAPIJar);
		}
		if(teavmMetaprogrammingImpl == null) {
			missingJars.add(teavmMetaprogrammingImplJar);
		}
		if(teavmJodaTime == null) {
			missingJars.add(teavmJodaTimeJar);
		}
		if(teavmJZLIB == null) {
			missingJars.add(teavmJZLIBJar);
		}
		if(missingJars.size() > 0) {
			throw new MissingJARsException(missingJars);
		}
	}

	private static void discoverJars(File dir) {
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; ++i) {
			File f = files[i];
			if(f.isDirectory()) {
				discoverJars(f);
			}else {
				String n = f.getName();
				switch(n) {
				case teavmCoreJar:
					teavmCore = f;
					break;
				case teavmCliJar:
					teavmCli = f;
					break;
				case teavmToolingJar:
					teavmTooling = f;
					break;
				case teavmPlatformJar:
					teavmPlatform = f;
					break;
				case teavmClasslibJar:
					teavmClasslib = f;
					break;
				case teavmInteropJar:
					teavmInterop = f;
					break;
				case teavmJSOJar:
					teavmJSO = f;
					break;
				case teavmJSOApisJar:
					teavmJSOApis = f;
					break;
				case teavmJSOImplJar:
					teavmJSOImpl = f;
					break;
				case teavmMetaprogrammingAPIJar:
					teavmMetaprogrammingAPI = f;
					break;
				case teavmMetaprogrammingImplJar:
					teavmMetaprogrammingImpl = f;
					break;
				case teavmJodaTimeJar:
					teavmJodaTime = f;
					break;
				case teavmJZLIBJar:
					teavmJZLIB = f;
					break;
				default:
					break;
				}
			}
		}
	}

	private static void copyURLToFileCheck404(String urlIn, File fileOut) throws IOException {
		System.out.println("downloading: " + urlIn);
		URL url;
		try {
			url = new URL(urlIn);
		}catch(MalformedURLException ex) {
			throw new IOException("Invalid URL: " + urlIn, ex);
		}
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        int respCode = connection.getResponseCode();
        if(respCode != 200) {
        	connection.disconnect();
        	throw new IOException("Recieved response code: " + respCode);
        }
        try (InputStream stream = connection.getInputStream()) {
            FileUtils.copyInputStreamToFile(stream, fileOut);
        }finally {
        	connection.disconnect(); // is this required?
        }
	}

	public static boolean tryLoadTeaVMBridge() {
		String override = System.getProperty("eaglercraft.TeaVMBridge");
		File teavmBridgeCheck;
		if(override != null) {
			teavmBridgeCheck = new File(override);
		}else {
			try {
				teavmBridgeCheck = new File(new File(URLDecoder.decode(
						TeaVMBinaries.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),
						"UTF-8")).getParent(), "TeaVMBridge.jar");
			} catch (URISyntaxException | UnsupportedEncodingException e) {
				System.err.println("Failed to locate TeaVMBridge.jar relative to BuildTools jar!");
				e.printStackTrace();
				return false;
			}
		}
		if(teavmBridgeCheck.exists()) {
			teavmBridge = teavmBridgeCheck;
			return true;
		}else {
			System.err.println("File does not exist: " + teavmBridgeCheck.getAbsolutePath());
			return false;
		}
	}

	public static File[] getTeaVMCompilerClasspath() {
		return new File[] { teavmCore, teavmCli, teavmTooling, teavmInterop, teavmMetaprogrammingAPI, teavmBridge };
	}

	public static String[] getTeaVMRuntimeClasspath() {
		return new String[] {
				teavmJodaTime.getAbsolutePath(), teavmJZLIB.getAbsolutePath(), teavmClasslib.getAbsolutePath(),
				teavmInterop.getAbsolutePath(), teavmJSO.getAbsolutePath(), teavmJSOApis.getAbsolutePath(),
				teavmJSOImpl.getAbsolutePath(), teavmMetaprogrammingAPI.getAbsolutePath(),
				teavmMetaprogrammingImpl.getAbsolutePath(), teavmPlatform.getAbsolutePath()
		};
	}

}

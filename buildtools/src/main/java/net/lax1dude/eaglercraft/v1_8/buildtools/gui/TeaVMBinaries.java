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
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class TeaVMBinaries {

	public static class MavenJAREntry {
		public final String jar;
		public final String maven;
		public File file;
		private MavenJAREntry(String maven) {
			this.jar = maven.substring(maven.lastIndexOf('/') + 1);
			this.maven = maven;
		}
	}

	public static final MavenJAREntry teavmCore = new MavenJAREntry("org/teavm/teavm-core/0.9.2/teavm-core-0.9.2.jar");
	public static final MavenJAREntry teavmCli = new MavenJAREntry("org/teavm/teavm-cli/0.9.2/teavm-cli-0.9.2.jar");
	public static final MavenJAREntry teavmTooling = new MavenJAREntry("org/teavm/teavm-tooling/0.9.2/teavm-tooling-0.9.2.jar");
	public static final MavenJAREntry teavmPlatform = new MavenJAREntry("org/teavm/teavm-platform/0.9.2/teavm-platform-0.9.2.jar");
	public static final MavenJAREntry teavmClasslib = new MavenJAREntry("org/teavm/teavm-classlib/0.9.2/teavm-classlib-0.9.2.jar");
	public static final MavenJAREntry teavmInterop = new MavenJAREntry("org/teavm/teavm-interop/0.9.2/teavm-interop-0.9.2.jar");
	public static final MavenJAREntry teavmJSO = new MavenJAREntry("org/teavm/teavm-jso/0.9.2/teavm-jso-0.9.2.jar");
	public static final MavenJAREntry teavmJSOApis = new MavenJAREntry("org/teavm/teavm-jso-apis/0.9.2/teavm-jso-apis-0.9.2.jar");
	public static final MavenJAREntry teavmJSOImpl = new MavenJAREntry("org/teavm/teavm-jso-impl/0.9.2/teavm-jso-impl-0.9.2.jar");
	public static final MavenJAREntry teavmRelocatedLibsASM = new MavenJAREntry("org/teavm/teavm-relocated-libs-asm/0.9.2/teavm-relocated-libs-asm-0.9.2.jar");
	public static final MavenJAREntry teavmRelocatedLibsASMAnalysis = new MavenJAREntry("org/teavm/teavm-relocated-libs-asm-analysis/0.9.2/teavm-relocated-libs-asm-analysis-0.9.2.jar");
	public static final MavenJAREntry teavmRelocatedLibsASMCommons = new MavenJAREntry("org/teavm/teavm-relocated-libs-asm-commons/0.9.2/teavm-relocated-libs-asm-commons-0.9.2.jar");
	public static final MavenJAREntry teavmRelocatedLibsASMTree = new MavenJAREntry("org/teavm/teavm-relocated-libs-asm-tree/0.9.2/teavm-relocated-libs-asm-tree-0.9.2.jar");
	public static final MavenJAREntry teavmRelocatedLibsASMUtil = new MavenJAREntry("org/teavm/teavm-relocated-libs-asm-util/0.9.2/teavm-relocated-libs-asm-util-0.9.2.jar");
	public static final MavenJAREntry teavmRelocatedLibsHPPC = new MavenJAREntry("org/teavm/teavm-relocated-libs-hppc/0.9.2/teavm-relocated-libs-hppc-0.9.2.jar");
	public static final MavenJAREntry teavmRelocatedLibsRhino = new MavenJAREntry("org/teavm/teavm-relocated-libs-rhino/0.9.2/teavm-relocated-libs-rhino-0.9.2.jar");
	public static final MavenJAREntry teavmMetaprogrammingAPI = new MavenJAREntry("org/teavm/teavm-metaprogramming-api/0.9.2/teavm-metaprogramming-api-0.9.2.jar");
	public static final MavenJAREntry teavmMetaprogrammingImpl = new MavenJAREntry("org/teavm/teavm-metaprogramming-impl/0.9.2/teavm-metaprogramming-impl-0.9.2.jar");
	public static final MavenJAREntry teavmJodaTime = new MavenJAREntry("joda-time/joda-time/2.12.2/joda-time-2.12.2.jar");
	public static final MavenJAREntry teavmJZLIB = new MavenJAREntry("com/jcraft/jzlib/1.1.3/jzlib-1.1.3.jar");
	public static final MavenJAREntry depJOrbis = new MavenJAREntry("org/jcraft/jorbis/0.0.17/jorbis-0.0.17.jar");
	public static final MavenJAREntry depLang3 = new MavenJAREntry("org/apache/commons/commons-lang3/3.6/commons-lang3-3.6.jar");
	public static final MavenJAREntry depHPPC = new MavenJAREntry("com/carrotsearch/hppc/0.10.0/hppc-0.10.0.jar");
	public static final MavenJAREntry depJSR305 = new MavenJAREntry("com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar");

	private static final MavenJAREntry[] jarsList = new MavenJAREntry[] { teavmCore, teavmCli, teavmTooling,
			teavmPlatform, teavmClasslib, teavmInterop, teavmJSO, teavmJSOApis, teavmJSOImpl, teavmRelocatedLibsASM,
			teavmRelocatedLibsASMAnalysis, teavmRelocatedLibsASMCommons, teavmRelocatedLibsASMTree,
			teavmRelocatedLibsASMUtil, teavmRelocatedLibsHPPC, teavmRelocatedLibsRhino, teavmMetaprogrammingAPI,
			teavmMetaprogrammingImpl, teavmJodaTime, teavmJZLIB, depJOrbis, depLang3, depHPPC, depJSR305 };

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
		for(int i = 0; i < jarsList.length; ++i) {
			jarsList[i].file = null;
		}
		
		if(url.lastIndexOf('/') != url.length() - 1) {
			url += "/";
		}
		
		for(int i = 0; i < jarsList.length; ++i) {
			MavenJAREntry jar = jarsList[i];
			String urlConc = url + jar.maven;
			try {
				File f = new File(outputDir, jar.jar);
				copyURLToFileCheck404(urlConc, f);
				jar.file = f;
			}catch(IOException ex) {
				System.err.println("Could not download JAR: " + urlConc);
				ex.printStackTrace();
				throw new MissingJARsException("The following JAR file could not be downloaded: " + urlConc, Arrays.asList(urlConc));
			}
		}
	}

	public static void loadFromDirectory(File directory) throws MissingJARsException {
		for(int i = 0; i < jarsList.length; ++i) {
			jarsList[i].file = null;
		}
		discoverJars(directory);
		List<String> missingJars = new ArrayList();
		for(int i = 0; i < jarsList.length; ++i) {
			MavenJAREntry jar = jarsList[i];
			if(jar.file == null) {
				missingJars.add(jar.jar);
			}
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
				for(int j = 0; j < jarsList.length; ++j) {
					if(n.equals(jarsList[j].jar)) {
						jarsList[j].file = f;
					}
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
		return new File[] { teavmCore.file, teavmCli.file, teavmTooling.file, teavmInterop.file,
				teavmRelocatedLibsASM.file, teavmRelocatedLibsASMAnalysis.file, teavmRelocatedLibsASMCommons.file,
				teavmRelocatedLibsASMTree.file, teavmRelocatedLibsASMUtil.file, teavmRelocatedLibsHPPC.file,
				teavmRelocatedLibsRhino.file, teavmMetaprogrammingAPI.file, teavmBridge };
	}

	public static String[] getTeaVMRuntimeClasspath() {
		return new String[] { teavmJodaTime.file.getAbsolutePath(), teavmJZLIB.file.getAbsolutePath(),
				teavmClasslib.file.getAbsolutePath(), teavmInterop.file.getAbsolutePath(),
				teavmJSO.file.getAbsolutePath(), teavmJSOApis.file.getAbsolutePath(),
				teavmJSOImpl.file.getAbsolutePath(), teavmMetaprogrammingAPI.file.getAbsolutePath(),
				teavmMetaprogrammingImpl.file.getAbsolutePath(), teavmPlatform.file.getAbsolutePath(),
				teavmCore.file.getAbsolutePath(), depJOrbis.file.getAbsolutePath(), depLang3.file.getAbsolutePath(),
				depHPPC.file.getAbsolutePath(), depJSR305.file.getAbsolutePath() };
	}

}

package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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
public class MakeOfflineDownload {

	private static File currentJarFile = null;
	private static URLClassLoader classLoader = null;
	private static Method mainMethod = null;

	public static void compilerMain(File jarFile, String[] args) throws InvocationTargetException {
		if(currentJarFile != null && !currentJarFile.equals(jarFile)) {
			throw new IllegalArgumentException("Cannot load two different MakeOfflineDownload versions into the same runtime");
		}
		if(mainMethod == null) {
			currentJarFile = jarFile;
			try {
				if(classLoader == null) {
					classLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() }, ClassLoader.getSystemClassLoader());
				}
				Class epkCompilerMain = classLoader.loadClass("net.lax1dude.eaglercraft.v1_8.buildtools.workspace.MakeOfflineDownload");
				mainMethod = epkCompilerMain.getDeclaredMethod("main", String[].class);
			} catch (MalformedURLException | SecurityException e) {
				throw new IllegalArgumentException("Illegal MakeOfflineDownload JAR path!", e);
			} catch (ClassNotFoundException | NoSuchMethodException e) {
				throw new IllegalArgumentException("MakeOfflineDownload JAR does not contain main class: 'net.lax1dude.eaglercraft.v1_8.buildtools.workspace.MakeOfflineDownload'", e);
			}
		}
		try {
			mainMethod.invoke(null, new Object[] { args });
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalArgumentException("MakeOfflineDownload JAR does not contain valid 'main' method", e);
		}
	}

	public static void free() {
		if(classLoader != null) {
			try {
				classLoader.close();
				classLoader = null;
			} catch (IOException e) {
				System.err.println("Memory leak, failed to release MakeOfflineDownload ClassLoader!");
				e.printStackTrace();
			}
		}
	}

}

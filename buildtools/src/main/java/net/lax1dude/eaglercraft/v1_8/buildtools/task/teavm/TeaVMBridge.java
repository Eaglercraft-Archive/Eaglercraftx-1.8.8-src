package net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries;

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
public class TeaVMBridge {

	private static URLClassLoader classLoader = null;

	/**
	 * <h3>List of required options:</h3>
	 * <table>
	 * <tr><td><b>classPathEntries</b></td><td>-&gt; BuildStrategy.setClassPathEntries(List&lt;String&gt;)</td></tr>
	 * <tr><td><b>entryPointName</b></td><td>-&gt; BuildStrategy.setEntryPointName(String)</td></tr>
	 * <tr><td><b>mainClass</b></td><td>-&gt; BuildStrategy.setMainClass(String)</td></tr>
	 * <tr><td><b>minifying</b></td><td>-&gt; BuildStrategy.setMinifying(boolean)</td></tr>
	 * <tr><td><b>optimizationLevel</b></td><td>-&gt; BuildStrategy.setOptimizationLevel(TeaVMOptimizationLevel)</td></tr>
	 * <tr><td><b>generateSourceMaps</b></td><td>-&gt; BuildStrategy.setSourceMapsFileGenerated(boolean)</td></tr>
	 * <tr><td><b>targetDirectory</b></td><td>-&gt; BuildStrategy.setTargetDirectory(String)</td></tr>
	 * <tr><td><b>targetFileName</b></td><td>-&gt; BuildStrategy.setTargetFileName(String)</td></tr>
	 * </table>
	 * <br>
	 */
	public static boolean compileTeaVM(Map<String, Object> options) throws TeaVMClassLoadException, TeaVMRuntimeException {
		File[] cp = TeaVMBinaries.getTeaVMCompilerClasspath();
		URL[] urls = new URL[cp.length];
		
		for(int i = 0; i < cp.length; ++i) {
			try {
				urls[i] = cp[i].toURI().toURL();
			} catch (MalformedURLException e) {
				throw new TeaVMClassLoadException("Could not resolve URL for: " + cp[i].getAbsolutePath(), e);
			}
		}
		
		Method found = null;
		
		try {
			if(classLoader == null) {
				classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
			}
			Class c = classLoader.loadClass("net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm.TeaVMBridgeImpl");
			Method[] methods = c.getDeclaredMethods();
			for(int i = 0; i < methods.length; ++i) {
				Method m = methods[i];
				if(m.getName().equals("compileTeaVM")) {
					found = m;
				}
			}
			if(found == null) {
				throw new NoSuchMethodException("compileTeaVM");
			}
		}catch(TeaVMClassLoadException | NoSuchMethodException | ClassNotFoundException t) {
			throw new TeaVMClassLoadException("Could not link TeaVM compiler!", t);
		}catch(RuntimeException t) {
			String msg = t.getMessage();
			if(msg.startsWith("[TeaVMBridge]")) {
				throw new TeaVMRuntimeException(msg.substring(13).trim(), t.getCause());
			}else {
				throw new TeaVMRuntimeException("Uncaught exception was thrown!", t);
			}
		}catch(Throwable t) {
			throw new TeaVMRuntimeException("Uncaught exception was thrown!", t);
		}
		
		try {
			Object ret = found.invoke(null, options);
			return ret != null && (ret instanceof Boolean) && ((Boolean)ret).booleanValue();
		}catch(InvocationTargetException ex) {
			throw new TeaVMRuntimeException("Uncaught exception was thrown!", ex.getCause());
		} catch (Throwable t) {
			throw new TeaVMRuntimeException("Failed to invoke 'compileTeaVM'!", t);
		}
		
	}

	public static class TeaVMClassLoadException extends RuntimeException {
		public TeaVMClassLoadException(String message, Throwable cause) {
			super(message, cause);
		}
		public TeaVMClassLoadException(String message) {
			super(message);
		}
	}

	public static class TeaVMRuntimeException extends RuntimeException {
		public TeaVMRuntimeException(String message, Throwable cause) {
			super(message, cause);
		}
		public TeaVMRuntimeException(String message) {
			super(message);
		}
	}

	public static void free() {
		if(classLoader != null) {
			try {
				classLoader.close();
				classLoader = null;
			} catch (IOException e) {
				System.err.println("Memory leak, failed to release TeaVM JAR ClassLoader!");
				e.printStackTrace();
			}
		}
	}

}

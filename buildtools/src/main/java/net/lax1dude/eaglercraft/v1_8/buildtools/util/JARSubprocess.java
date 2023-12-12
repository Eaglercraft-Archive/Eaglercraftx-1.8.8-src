package net.lax1dude.eaglercraft.v1_8.buildtools.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
public class JARSubprocess {
	
	public static final char classPathSeperator;
	
	static {
		classPathSeperator = System.getProperty("os.name").toLowerCase().contains("windows") ? ';' : ':';
	}

	private static final List<Process> activeProcesses = new ArrayList();

	private static boolean shutdownThreadStarted = false;

	public static int runJava(File directory, String[] javaExeArguments, String logPrefix) throws IOException {
		if(logPrefix.length() > 0 && !logPrefix.endsWith(" ")) {
			logPrefix = logPrefix + " ";
		}
		String javaHome = System.getProperty("java.home");
		if(classPathSeperator == ';') {
			File javaExe = new File(javaHome, "bin/java.exe");
			if(!javaExe.isFile()) {
				javaExe = new File(javaHome, "java.exe");
				if(!javaExe.isFile()) {
					throw new IOException("Could not find /bin/java.exe equivelant on java.home! (java.home=" + javaHome + ")");
				}
			}
			javaHome = javaExe.getAbsolutePath();
		}else {
			File javaExe = new File(javaHome, "bin/java");
			if(!javaExe.isFile()) {
				javaExe = new File(javaHome, "java");
				if(!javaExe.isFile()) {
					throw new IOException("Could not find /bin/java equivelant on java.home! (java.home=" + javaHome + ")");
				}
			}
			javaHome = javaExe.getAbsolutePath();
		}
		
		String[] fullArgs = new String[javaExeArguments.length + 1];
		fullArgs[0] = javaHome;
		System.arraycopy(javaExeArguments, 0, fullArgs, 1, javaExeArguments.length);
		
		ProcessBuilder exec = new ProcessBuilder(fullArgs);
		exec.directory(directory);
		
		Process ps = exec.start();
		
		synchronized(activeProcesses) {
			if(!shutdownThreadStarted) {
				Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
					public void run() {
						synchronized(activeProcesses) {
							for(Process proc : activeProcesses) {
								try {
									if(proc.isAlive()) {
										proc.destroy();
									}
								}catch(Throwable t) {
								}
							}
						}
					}
				}, "Subprocess Exit Thread"));
				shutdownThreadStarted = true;
			}
			activeProcesses.add(ps);
		}
		
		InputStream is = ps.getInputStream();
		InputStream ise = ps.getErrorStream();
		BufferedReader isb = new BufferedReader(new InputStreamReader(is));
		BufferedReader iseb = new BufferedReader(new InputStreamReader(ise));
		
		String isbl = "";
		String isebl = "";
		int maxReadPerLoop = 128;
		int c = 0;
		do {
			boolean tick = false;
			c = 0;
			while(isb.ready() && (!iseb.ready() || ++c < maxReadPerLoop)) {
				char cc = (char)isb.read();
				if(cc != '\r') {
					if(cc == '\n') {
						System.out.println(logPrefix + isbl);
						isbl = "";
					}else {
						isbl += cc;
					}
				}
				tick = true;
			}
			c = 0;
			while(iseb.ready() && (!isb.ready() || ++c < maxReadPerLoop)) {
				char cc = (char)iseb.read();
				if(cc != '\r') {
					if(cc == '\n') {
						System.err.println(logPrefix + isebl);
						isebl = "";
					}else {
						isebl += cc;
					}
				}
				tick = true;
			}
			if(!tick) {
				try {
					Thread.sleep(10l);
				} catch (InterruptedException e) {
				}
			}
		} while(ps.isAlive());
		
		while(true) {
			try {
				return ps.waitFor();
			} catch (InterruptedException e) {
			}
		}
	}
	
}

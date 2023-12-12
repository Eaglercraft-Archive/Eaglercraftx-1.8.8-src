package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

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
public class FFMPEG {
	
	public static final boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");
	
	public static String foundFFMPEG = null;
	
	public static void confirmFFMPEG() {
		if(checkFFMPEGOnPath()) {
			foundFFMPEG = "ffmpeg";
			return;
		}
		if(windows) {
			File f = new File("mcp918/ffmpeg.exe");
			if(!f.isFile()) {
				System.out.println();
				System.out.println("ERROR: 'ffmpeg.exe' wasn't found in the 'mcp918' folder!");
				System.out.println();
				System.out.println("Please visit one of the following URLs to download it:");
				System.out.println(" - https://www.gyan.dev/ffmpeg/builds/");
				System.out.println(" - https://github.com/BtbN/FFmpeg-Builds/releases");
				System.out.println();
				System.out.println("Locate 'bin/ffmpeg.exe' in the .zip file you download and");
				System.out.println("place it in the 'mcp918' folder and press enter to continue");
				System.out.println();
				try {
					while(System.in.read() != '\n') {
						try {
							Thread.sleep(20l);
						} catch (InterruptedException e) {
						}
					}
				}catch(IOException ex) {
				}
				confirmFFMPEG();
			}else {
				foundFFMPEG = f.getAbsolutePath();
			}
		}else {
			do {
				File f = new File("mcp918/ffmpeg");
				if(f.isFile() && f.canExecute()) {
					foundFFMPEG = f.getAbsolutePath();
					return;
				}
				System.out.println();
				System.out.println("ERROR: ffmpeg is not installed on this system!");
				System.out.println();
				System.out.println("Please install it to continue, you can use the package");
				System.out.println("manager on most distros to do this automatically:");
				System.out.println(" - Debian: apt install ffmpeg");
				System.out.println(" - Ubuntu: apt install ffmpeg");
				System.out.println(" - Fedora: dnf install ffmpeg");
				System.out.println(" - Arch: pacman -S ffmpeg");
				System.out.println();
				System.out.println("Alternatively, place the 'ffmpeg' executable in the");
				System.out.println("'mcp918' folder of this repository");
				System.out.println();
				System.out.println("Make sure it has chmod +x");
				System.out.println();
				System.out.println("Press enter to continue once it has installed");
				System.out.println();
				try {
					while(System.in.read() != '\n') {
						try {
							Thread.sleep(20l);
						} catch (InterruptedException e) {
						}
					}
				}catch(IOException ex) {
				}
			}while(!checkFFMPEGOnPath());
			foundFFMPEG = "ffmpeg";
		}
	}

	public static int run(File rundir, String... args) {
		String[] e = new String[args.length + 1];
		System.arraycopy(args, 0, e, 1, args.length);
		
		if(foundFFMPEG == null) {
			confirmFFMPEG();
		}
		
		e[0] = foundFFMPEG;
		
		ProcessBuilder pb = new ProcessBuilder(e);
		pb.directory(rundir);
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		
		try {
			Process p = pb.start();
			while(true) {
				try {
					return p.waitFor();
				} catch (InterruptedException ee) {
				}
			}
		}catch(IOException ex) {
			System.err.println("Could not start ffmpeg process!");
			ex.printStackTrace();
			return -1;
		}
		
	}
	
	public static byte[] encodeOgg(File temporaryDir, byte[] bytesInput, int samples, int bitrate, boolean stereo) throws IOException {
		File src = new File(temporaryDir, "temp.src.ogg");
		FileUtils.writeByteArrayToFile(src, bytesInput);
		
		File dst = new File(temporaryDir, "temp.dst.ogg");

		int i;
		if (stereo) {
			i = run(temporaryDir, "-y", "-v", "error", "-i", "temp.src.ogg", "-c:a", "libvorbis", "-ac", "2",
					"-apply_phase_inv", "1", "-b:a", "" + bitrate + "k", "-ar", "" + samples, "temp.dst.ogg");
		} else {
			i = run(temporaryDir, "-y", "-v", "error", "-i", "temp.src.ogg", "-c:a", "libvorbis", "-ac", "1",
					"-apply_phase_inv", "0", "-b:a", "" + bitrate + "k", "-ar", "" + samples, "temp.dst.ogg");
		}
		
		src.delete();
		
		if(i != 0) {
			throw new IOException("FFMPEG returned error code: " + i);
		}
		
		byte[] read = FileUtils.readFileToByteArray(dst);
		
		dst.delete();
		
		return read;
	}

	public static boolean checkFFMPEGOnPath() {
		ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-version");
		Process proc;
		try {
			proc = pb.start();
		}catch(IOException ex) {
			return false;
		}
		int exitCode;
		while(true) {
			try {
				exitCode = proc.waitFor();
				break;
			} catch (InterruptedException e) {
			}
		}
		return exitCode == 0;
	}

}

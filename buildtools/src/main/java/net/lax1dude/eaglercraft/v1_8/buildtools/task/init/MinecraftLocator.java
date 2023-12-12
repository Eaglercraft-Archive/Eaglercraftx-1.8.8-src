package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class MinecraftLocator {
	
	private static boolean hasTriedToFind = false;
	private static File directory = null;
	
	private static File locateOrCopyFile(String name, String copyPath) {
		File f = new File("./mcp918/" + name);
		if(f.isFile()) {
			return f;
		}
		if(!hasTriedToFind) {
			hasTriedToFind = true;
			String var0 = System.getProperty("os.name").toLowerCase();
			if(var0.contains("win")) {
				String ad = System.getenv("APPDATA");
				if(ad != null) {
					directory = new File(ad, ".minecraft");
				}else {
					directory = new File(System.getProperty("user.home"), ".minecraft");
				}
			}else if(var0.contains("mac")) {
				directory = new File(System.getProperty("user.home"), "Library/Application Support/minecraft");
			}else {
				directory = new File(System.getProperty("user.home"), ".minecraft");
			}
			if(!directory.isDirectory()) {
				directory = new File(System.getProperty("user.home"), "minecraft");
				if(!directory.isDirectory()) {
					directory = null;
				}
			}
		}
		if(directory == null) {
			return null;
		}else {
			File f2 = new File(directory, copyPath);
			if(f2.isFile()) {
				try {
					System.out.println("Copying '" + copyPath + "' from your .minecraft directory into './mcp918'...");
					FileUtils.copyFile(f2, f, true);
					return f;
				} catch (IOException e) {
					System.err.println("ERROR: failed to copy '" + copyPath + "' from your .minecraft directory into './mcp918'!");
					e.printStackTrace();
					return null;
				}
			}else {
				return null;
			}
		}
	}

	public static File locateMinecraftVersionJar(String name) {
		return locateOrCopyFile(name + ".jar", "versions/" + name + "/" + name + ".jar");
	}

	public static File locateMinecraftVersionAssets(String name) {
		return locateOrCopyFile(name + ".json", "assets/indexes/" + name + ".json");
	}
	
}

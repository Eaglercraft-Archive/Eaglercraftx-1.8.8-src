package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;

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
public class InitTask {
	
	private static File locatedMCPZip = null;
	private static File locatedMinecraftJar = null;
	private static File locatedAssetsJson = null;
	
	public static boolean initTask() {
		try {
			return initTask0();
		}catch(Throwable t) {
			System.err.println();
			System.err.println("Exception encountered while running task 'init'!");
			t.printStackTrace();
			return false;
		}
	}

	private static boolean initTask0() throws Throwable {
		System.out.println("Scanning 'mcp918' folder...");
		File mcp918dir = new File(EaglerBuildTools.repositoryRoot, "mcp918");
		
		if(!mcp918dir.isDirectory()) {
			System.err.println("ERROR: \"" + mcp918dir.getAbsolutePath() + "\" is not a directory!");
			return false;
		}
		
		for(File f : mcp918dir.listFiles()) {
			if(f.getName().equalsIgnoreCase("mcp918.zip")) {
				locatedMCPZip = f;
			}
			if(locatedMCPZip == null && f.getName().endsWith(".zip")) {
				locatedMCPZip = f;
			}
			if(f.getName().equalsIgnoreCase("1.8.8.jar")) {
				locatedMinecraftJar = f;
			}
			if(locatedMinecraftJar == null && f.getName().endsWith(".jar")) {
				locatedMinecraftJar = f;
			}
			if(f.getName().equalsIgnoreCase("1.8.json")) {
				locatedAssetsJson = f;
			}
			if(locatedAssetsJson == null && f.getName().endsWith(".json")) {
				locatedAssetsJson = f;
			}
		}

		if(locatedMCPZip == null) {
			System.err.println("ERROR: could not find ./mcp918/mcp918.zip! Please locate it and copy it into the 'mcp918' folder.");
			return false;
		}
		if(locatedMinecraftJar == null) {
			locatedMinecraftJar = MinecraftLocator.locateMinecraftVersionJar("1.8.8");
			if(locatedMinecraftJar == null) {
				System.err.println("ERROR: could not find ./mcp918/1.8.8.jar! Please locate it and copy it into the 'mcp918' folder.");
				return false;
			}
		}
		if(locatedAssetsJson == null) {
			locatedAssetsJson = MinecraftLocator.locateMinecraftVersionAssets("1.8");
			if(locatedAssetsJson == null) {
				System.err.println("ERROR: could not find ./mcp918/1.8.json! Please locate it and copy it into the 'mcp918' folder.");
				return false;
			}
		}
		
		FFMPEG.confirmFFMPEG();
		
		File buildToolsTmp = EaglerBuildToolsConfig.getTemporaryDirectory();
		boolean btExist = buildToolsTmp.exists();
		if(btExist && !(buildToolsTmp.isDirectory() && buildToolsTmp.list().length == 0)) {
			System.out.println();
			System.out.println("Notice: BuildTools is already initialized.");
			System.out.println();
			System.out.println("you must revert all changes in the 'patches' directory of");
			System.out.println("this repo back to the main repository's current commits,");
			System.out.println("otherwise the 'pullrequest' command wll not work properly");
			System.out.println();
			System.out.print("Do you want to re-initialize? [Y/n]: ");
			
			String ret = "n";
			try {
				ret = (new BufferedReader(new InputStreamReader(System.in))).readLine();
			}catch(IOException ex) {
				// ?
			}
			ret = ret.toLowerCase();
			if(!ret.startsWith("y")) {
				System.out.println();
				System.out.println("Ok nice, the re-init will be cancelled. (thank god)");
				return true;
			}else {
				try {
					FileUtils.deleteDirectory(buildToolsTmp);
					btExist = false;
				}catch(IOException ex) {
					System.err.println("ERROR: Could not delete \"" + buildToolsTmp.getAbsolutePath() + "\"!");
					ex.printStackTrace();
					return false;
				}
			}
		}
		
		File mcpDataTMP = new File(buildToolsTmp, "ModCoderPack");
		if(!mcpDataTMP.isDirectory() && !mcpDataTMP.mkdirs()) {
			System.err.println("ERROR: failed to create \"" + mcpDataTMP.getAbsolutePath() + "\"!");
			return false;
		}
		
		boolean skipMCP = false;
		
		if(!skipMCP && !InitMCP.initTask(locatedMCPZip, mcpDataTMP)) {
			System.err.println("ERROR: could not initialize MCP from \"" + locatedMCPZip.getAbsolutePath() + "\"!");
			return false;
		}
		
		File minecraftSrcTmp = new File(buildToolsTmp, "MinecraftSrc");
		if(!minecraftSrcTmp.isDirectory() && !minecraftSrcTmp.mkdirs()) {
			System.err.println("ERROR: failed to create \"" + minecraftSrcTmp.getAbsolutePath() + "\"!");
			return false;
		}
		
		if(!DecompileMinecraft.decompileMinecraft(mcpDataTMP, locatedMinecraftJar, minecraftSrcTmp, locatedAssetsJson, true)) {
			System.err.println("ERROR: could not decompile and patch 1.8.8.jar from \"" + locatedMinecraftJar.getAbsolutePath() + "\"!");
			return false;
		}
		
		
		
		return true;
	}
	
}

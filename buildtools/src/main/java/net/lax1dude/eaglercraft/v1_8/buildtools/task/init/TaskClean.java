package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildToolsConfig;

public class TaskClean {
	
	public static boolean taskClean() {
		try {
			return taskClean0();
		}catch(Throwable t) {
			System.err.println();
			System.err.println("Exception encountered while running task 'clean'!");
			t.printStackTrace();
			return false;
		}
		
	}

	private static boolean taskClean0() throws Throwable {
		File buildToolsTmp = EaglerBuildToolsConfig.getTemporaryDirectory();
		File pullRequestTo = new File("pullrequest");
		boolean btExist = buildToolsTmp.exists();
		boolean prExist = pullRequestTo.exists();
		if((btExist && !(buildToolsTmp.isDirectory() && buildToolsTmp.list().length == 0)) ||
				(prExist && !(pullRequestTo.isDirectory() && pullRequestTo.list().length == 0))) {
			System.out.println();
			System.out.println("Notice: Clean will delete the init directory and also");
			System.out.println("all of the files in the current pull request");
			System.out.println();
			System.out.println("you must revert all changes in the 'patches' directory of");
			System.out.println("this repo back to the main repository's current commits,");
			System.out.println("otherwise the 'pullrequest' command wll not work properly");
			System.out.println();
			System.out.print("Do you want to clean? [Y/n]: ");
			
			String ret = "n";
			try {
				ret = (new BufferedReader(new InputStreamReader(System.in))).readLine();
			}catch(IOException ex) {
				// ?
			}
			ret = ret.toLowerCase();
			if(!ret.startsWith("y")) {
				System.out.println();
				System.out.println("Ok nice, the clean will be cancelled. (thank god)");
				return true;
			}else {
				try {
					if(prExist) {
						System.out.println();
						System.out.println("Deleting pull request...");
						FileUtils.deleteDirectory(pullRequestTo);
						prExist = false;
					}
				}catch(IOException ex) {
					System.err.println("ERROR: Could not delete \"" + pullRequestTo.getAbsolutePath() + "\"!");
					ex.printStackTrace();
					return false;
				}
				try {
					if(btExist) {
						System.out.println();
						System.out.println("Deleting init directory...");
						FileUtils.deleteDirectory(buildToolsTmp);
						btExist = false;
					}
				}catch(IOException ex) {
					System.err.println("ERROR: Could not delete \"" + buildToolsTmp.getAbsolutePath() + "\"!");
					ex.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
}

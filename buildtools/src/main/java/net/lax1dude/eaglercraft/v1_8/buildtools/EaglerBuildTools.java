package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import net.lax1dude.eaglercraft.v1_8.buildtools.task.diff.MergePullRequest;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.diff.PullRequestTask;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.CreateUnpatched;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.InitTask;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.SetupWorkspace;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.TaskClean;

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
public class EaglerBuildTools {

	public static File repositoryRoot = new File(".");

	public static void main(String[] args) {
		System.out.println("Eaglercraft 1.8 Build Tools");
		System.out.println("Copyright (c) 2022-2025 lax1dude");
		System.out.println();
		
		if(!System.getProperty("eaglercraft.isJava11", "false").equalsIgnoreCase("true")) {
			try {
				if (!(boolean) Class
						.forName("net.lax1dude.eaglercraft.v1_8.buildtools.Java11Check", true,
								new URLClassLoader(new URL[] { (new File("buildtools/Java11Check.jar")).toURI().toURL() }))
						.getMethod("classLoadCheck").invoke(null)) {
					throw new RuntimeException("wtf?");
				}
			}catch(Throwable t) {
				System.err.println("ERROR: A minimum of Java 11 is required to run this tool!");
				System.err.println();
				System.err.println("You are using Java " + System.getProperty("java.version"));
				System.err.println();
				return;
			}
		}
		
		if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			System.out.println("Options:");
			System.out.println("  help - displays this message");
			System.out.println("  init - decompiles 1.8.8 and applies the main repo's patch files");
			System.out.println("  workspace - creates a dev workspace with a gradle project to compile the source");
			System.out.println("  pullrequest - scans changes in the dev workspace and creates patch files for pull requests");
			System.out.println("  pullrequest_test - makes new workspace and re-applies the patches in 'pullrequest'");
			System.out.println("  unpatched - creates a zip file with the vanilla minecraft source without patches");
			System.out.println("  merge - merges the patch files in the pullrequest folder with the repo's main patch files");
			System.out.println("  merge_direct - merges changes in the dev workspace with the repo's main patch files");
			System.out.println("  clean - delete init and pullrequest directories, keeps dev workspace");
			System.out.println();
		}else if(args.length == 1 && args[0].equalsIgnoreCase("init")) {
			LicensePrompt.display();
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(InitTask.initTask()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else if(args.length == 1 && args[0].equalsIgnoreCase("workspace")) {
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(SetupWorkspace.setupWorkspace()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else if(args.length == 1 && args[0].equalsIgnoreCase("pullrequest")) {
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(PullRequestTask.pullRequest()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else if(args.length == 1 && args[0].equalsIgnoreCase("pullrequest_test")) {
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(SetupWorkspace.pullRequestTest()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else if(args.length == 1 && args[0].equalsIgnoreCase("unpatched")) {
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(CreateUnpatched.createUnpatched()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else if(args.length == 1 && args[0].equalsIgnoreCase("merge")) {
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(MergePullRequest.mergeTask()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else if(args.length == 1 && args[0].equalsIgnoreCase("merge_direct")) {
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(MergePullRequest.mergeDirect()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else if(args.length == 1 && args[0].equalsIgnoreCase("clean")) {
			System.out.println("Running task '" + args[0] + "':");
			System.out.println();
			if(TaskClean.taskClean()) {
				System.out.println();
				System.out.println("Task Complete.");
				System.out.println();
			}else {
				System.err.println();
				System.err.println("Task Failed!");
				System.err.println();
			}
		}else {
			System.err.println("Invalid arguments!");
		}
	}

}

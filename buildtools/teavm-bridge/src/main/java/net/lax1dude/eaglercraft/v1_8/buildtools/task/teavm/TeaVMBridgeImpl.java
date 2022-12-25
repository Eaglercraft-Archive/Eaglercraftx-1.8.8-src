package net.lax1dude.eaglercraft.v1_8.buildtools.task.teavm;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.teavm.diagnostics.Problem;
import org.teavm.diagnostics.ProblemProvider;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.builder.BuildException;
import org.teavm.tooling.builder.BuildResult;
import org.teavm.tooling.builder.BuildStrategy;
import org.teavm.tooling.builder.ClassLoaderFactory;
import org.teavm.tooling.builder.InProcessBuildStrategy;
import org.teavm.vm.TeaVMOptimizationLevel;
import org.teavm.vm.TeaVMPhase;
import org.teavm.vm.TeaVMProgressFeedback;
import org.teavm.vm.TeaVMProgressListener;

/**
 * Copyright (c) 2022 LAX1DUDE. All Rights Reserved.
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
public class TeaVMBridgeImpl {

	static {
		System.out.println("[TeaVMBridge] Class was loaded");
	}

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
	public static boolean compileTeaVM(Map<String, Object> options) throws RuntimeException {
		
		System.out.println();
		System.out.println("[TeaVMBridge] Configuring InProcessBuildStrategy:");
		
		for(Entry<String, Object> etr : options.entrySet()) {
			System.out.println("[TeaVMBridge]     " + etr.getKey() + " = " + etr.getValue());
		}
		
		System.out.println();
		
		final List<URLClassLoader> openedClassLoaders = new ArrayList();
		
		BuildStrategy buildStrategy = new InProcessBuildStrategy(new ClassLoaderFactory() {
			@Override
			public ClassLoader create(URL[] var1, ClassLoader var2) {
				URLClassLoader classLoader = new URLClassLoader(var1, var2);
				synchronized(openedClassLoaders) {
					openedClassLoaders.add(classLoader);
				}
				return classLoader;
			}
		});
		
		long start = System.currentTimeMillis();
		BuildResult result = null;
		
		try {
			buildStrategy.setClassPathEntries((List<String>)options.get("classPathEntries"));
			buildStrategy.setDebugInformationGenerated(false);
			buildStrategy.setEntryPointName((String)options.get("entryPointName"));
			buildStrategy.setMainClass((String)options.get("mainClass"));
			buildStrategy.setMaxTopLevelNames(10000); // TODO: what does this do? sounds important
			buildStrategy.setMinifying(((Boolean)options.get("minifying")).booleanValue());
			buildStrategy.setOptimizationLevel(TeaVMOptimizationLevel.valueOf((String)options.get("optimizationLevel")));
			buildStrategy.setSourceFilesCopied(false);
			buildStrategy.setSourceMapsFileGenerated(((Boolean)options.get("generateSourceMaps")).booleanValue());
			buildStrategy.setTargetDirectory((String)options.get("targetDirectory"));
			buildStrategy.setTargetFileName((String)options.get("targetFileName"));
			buildStrategy.setTargetType(TeaVMTargetType.JAVASCRIPT);
			
			buildStrategy.setProgressListener(new TeaVMProgressListener() {
				
				@Override
				public TeaVMProgressFeedback progressReached(int var1) {
					return TeaVMProgressFeedback.CONTINUE;
				}
				
				@Override
				public TeaVMProgressFeedback phaseStarted(TeaVMPhase var1, int var2) {
					if(var1 == TeaVMPhase.DEPENDENCY_ANALYSIS) {
						System.out.println("[TeaVMBridge] Analyzing dependencies...");
					}else if(var1 == TeaVMPhase.COMPILING) {
						System.out.println("[TeaVMBridge] Running compiler...");
					}
					return TeaVMProgressFeedback.CONTINUE;
				}
			});
			
			try {
				result = buildStrategy.build();
			}catch(BuildException ex) {
				throw new RuntimeException("[TeaVMBridge] BuildException thrown while building!", ex.getCause());
			}catch(Throwable t) {
				throw new RuntimeException("[TeaVMBridge] Unhandled exception thrown while building!", t);
			}
		}finally {
			for(int i = 0, l = openedClassLoaders.size(); i < l; ++i) {
				URLClassLoader cl = openedClassLoaders.get(i);
				try {
					cl.close();
				}catch(Throwable t) {
					System.out.println("[TeaVMBridge] ERROR: could not free classloader " + cl + ": " + t.toString());
				}
			}
		}

		System.out.println();
		System.out.println("[TeaVMBridge] Build complete! Took " + ((System.currentTimeMillis() - start) / 1000l) + " seconds");
		
		boolean returnError = false;
		ProblemProvider prov = result.getProblems();
		if(prov != null) {
			List<Problem> problems = prov.getProblems();
			if(problems != null && problems.size() > 0) {
				returnError = true;
				System.err.println("[TeaVMBridge] Encountered " + problems.size() + " problems while building:");
				for(int i = 0, l = problems.size(); i < l; ++i) {
					Problem p = problems.get(i);
					System.err.println("[TeaVMBridge]     - " + p.getSeverity() + ": " + p.getClass() + " : " + p.getLocation() + " - " + p.getText() + " - params: " + collectionToString(p.getParams()));
				}
				System.err.println();
			}
			
			List<Problem> severeProblems = prov.getSevereProblems();
			if(severeProblems != null && severeProblems.size() > 0) {
				returnError = true;
				System.err.println("[TeaVMBridge] Encountered " + severeProblems.size() + " high-severity problems while building:");
				for(int i = 0, l = severeProblems.size(); i < l; ++i) {
					Problem p = severeProblems.get(i);
					System.err.println("[TeaVMBridge]     - " + p.getSeverity() + ": " + p.getClass() + " : " + p.getLocation() + " - " + p.getText() + " - params: " + collectionToString(p.getParams()));
				}
				System.err.println();
			}
		}
		
		return !returnError;
	}

	private static String collectionToString(Object[] params) {
		if(params.length == 0) {
			return "[ ]";
		}
		StringBuilder ret = new StringBuilder();
		ret.append("[ ");
		for(int i = 0; i < params.length; ++i) {
			if(i > 0) {
				ret.append(" , ");
			}
			ret.append(params[i]);
		}
		ret.append(" ]");
		return ret.toString();
	}

}

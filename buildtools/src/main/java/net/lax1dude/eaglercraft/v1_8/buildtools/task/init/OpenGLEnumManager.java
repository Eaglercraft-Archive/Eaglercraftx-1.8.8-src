package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
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
public class OpenGLEnumManager {
	
	private static boolean hasLoaded = false;
	public static final Set<String> classNames = new HashSet();
	public static final Map<String,String> enumsForGLStateManager = new HashMap();
	public static final Map<String,Map<Integer,String>> enumsForFunctionKV = new HashMap();
	public static final Map<String,Map<String,Integer>> enumsForFunctionVK = new HashMap();
	
	public static boolean loadEnumMap() {
		if(hasLoaded) {
			return true;
		}
		hasLoaded = true;
		try {
			
			String enumsPath = "/lang/enums.json";
			System.out.println("Loading OpenGL enums: " + enumsPath);

			int fcnt = 0;
			int ecnt = 0;
			
			String jsonData;
			try(InputStream is = OpenGLEnumManager.class.getResourceAsStream(enumsPath)) {
				if(is == null) {
					throw new FileNotFoundException("classpath:/" + enumsPath);
				}
				jsonData = IOUtils.toString(is, "UTF-8");
			}
			
			JSONArray enumJSON = (new JSONObject(jsonData)).getJSONArray("enums");
			for(Object o : enumJSON.toList()) {
				List<Object> enumData = (List<Object>) o;
				
				List<String> functionsToAdd = new ArrayList();
				Map<Integer,String> enumsToAddKV = new HashMap();
				Map<String,Integer> enumsToAddVK = new HashMap();
				
				Map<String,Object> functionSet = (Map<String,Object>)enumData.get(0);
				
				for(Entry<String,Object> etr : functionSet.entrySet()) {
					classNames.add(etr.getKey());
					List<Object> functionArr = (List<Object>)etr.getValue();
					for(Object func : functionArr) {
						functionsToAdd.add(etr.getKey() + "." + (String)func);
					}
				}
				
				Map<String,Object> enumSet = (Map<String,Object>)enumData.get(1);
				
				for(Entry<String,Object> etr : enumSet.entrySet()) {
					Map<String,Object> enumEnums = (Map<String,Object>)etr.getValue();
					for(Entry<String,Object> etr2 : enumEnums.entrySet()) {
						Integer intg = Integer.parseInt(etr2.getKey());
						enumsToAddKV.put(intg, (String)etr2.getValue());
						enumsToAddVK.put((String)etr2.getValue(), intg);
						++ecnt;
					}
				}
				
				for(String fn : functionsToAdd) {
					if(!enumsForFunctionKV.containsKey(fn)) {
						++fcnt;
						enumsForFunctionKV.put(fn, enumsToAddKV);
						enumsForFunctionVK.put(fn, enumsToAddVK);
					}
				}
				
			}

			String glStateEnumsPath = "/lang/statemgr.json";
			System.out.println("Loading OpenGL enums: " + glStateEnumsPath);
			
			try(InputStream is = OpenGLEnumManager.class.getResourceAsStream(glStateEnumsPath)) {
				if(is == null) {
					throw new FileNotFoundException("classpath:/" + glStateEnumsPath);
				}
				jsonData = IOUtils.toString(is, "UTF-8");
			}
			
			JSONObject enumStateJSON = (new JSONObject(jsonData)).getJSONObject("statemgr_mappings");
			for(Entry<String,Object> etr : enumStateJSON.toMap().entrySet()) {
				String f = etr.getKey();
				String m = (String)etr.getValue();
				enumsForGLStateManager.put(f, m);
				if(!enumsForFunctionKV.containsKey(f) && enumsForFunctionKV.containsKey(m)) {
					enumsForFunctionKV.put(f, enumsForFunctionKV.get(m));
					enumsForFunctionVK.put(f, enumsForFunctionVK.get(m));
					++fcnt;
				}
			}
			
			for(String str : enumsForGLStateManager.keySet()) {
				int idx = str.indexOf('.');
				if(idx != -1) {
					classNames.add(str.substring(0, idx));
				}
			}
			
			System.out.println("Loaded " + ecnt + " enums for " + fcnt + " functions");
			
			return true;
		}catch(Throwable ex) {
			System.err.println("ERROR: could not load opengl enum map!");
			ex.printStackTrace();
			return false;
		}
	}
	
	public static String insertIntoLine(String input, Consumer<Integer> progressCallback) {
		int idx1 = input.indexOf('.');
		if(idx1 != -1) {
			String pfx = input.substring(0, idx1);
			String p2 = pfx.trim();
			if(classNames.contains(p2)) {
				String fn = input.substring(idx1 + 1);
				int idx2 = fn.indexOf('(');
				if(idx2 != -1) {
					String argz = fn.substring(idx2 + 1);
					fn = fn.substring(0, idx2);
					int idx3 = argz.lastIndexOf(')');
					String pofx = "";
					if(idx3 == -1) {
						idx3 = argz.length();
					}else {
						pofx = argz.substring(idx3);
					}
					Map<Integer,String> repValues = enumsForFunctionKV.get(p2 + "." + fn);
					if(repValues != null) {
						argz = argz.substring(0, idx3);
						String[] args = argz.split(", ");
						int cnt = 0;
						for(int i = 0; i < args.length; ++i) {
							Integer j;
							try {
								j = Integer.valueOf(args[i]);
							}catch(NumberFormatException ex) {
								continue;
							}
							String estr = repValues.get(j);
							if(estr != null) {
								args[i] = estr;
								++cnt;
							}
						}
						if(cnt > 0) {
							input = pfx + "." + fn + "(" + String.join(", ", args) + pofx;
							if(progressCallback != null) {
								progressCallback.accept(cnt);
							}
						}
					}
				}
			}
		}
		return input;
	}
	
	public static String stripFromLine(String input) {
		int idx1 = input.indexOf('.');
		if(idx1 != -1) {
			String pfx = input.substring(0, idx1);
			String p2 = pfx.trim();
			if(classNames.contains(p2)) {
				String fn = input.substring(idx1 + 1);
				int idx2 = fn.indexOf('(');
				if(idx2 != -1) {
					String argz = fn.substring(idx2 + 1);
					fn = fn.substring(0, idx2);
					int idx3 = argz.lastIndexOf(')');
					String pofx = "";
					if(idx3 == -1) {
						idx3 = argz.length();
					}else {
						pofx = argz.substring(idx3);
					}
					Map<String,Integer> repValues = enumsForFunctionVK.get(p2 + "." + fn);
					if(repValues != null) {
						argz = argz.substring(0, idx3);
						String[] args = argz.split(", ");
						int cnt = 0;
						for(int i = 0; i < args.length; ++i) {
							Integer estr = repValues.get(args[i]);
							if(estr != null) {
								args[i] = estr.toString();
								++cnt;
							}
						}
						if(cnt > 0) {
							return pfx + "." + fn + "(" + String.join(", ", args) + pofx;
						}
					}
				}
			}
		}
		return null;
	}
	
}

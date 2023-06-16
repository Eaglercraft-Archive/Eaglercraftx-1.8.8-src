package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
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
public class ResourceRulesList {
	
	public static ResourceRulesList loadResourceRules(File conf) throws IOException {
		List<ResourceRule> list = new ArrayList();
		
		try {
			JSONArray rulesArray = new JSONObject(FileUtils.readFileToString(conf, StandardCharsets.UTF_8)).getJSONArray("rules");
			for(int i = 0, l = rulesArray.length(); i < l; ++i) {
				JSONObject obj = rulesArray.getJSONObject(i);
				
				Iterator<String> itr = obj.keys();
				while(itr.hasNext()) {
					String name = itr.next();
					JSONObject a = obj.getJSONObject(name);
					boolean wildcard = name.endsWith("*");
					
					if(wildcard) {
						name = name.substring(0, name.length() - 1);
					}
					
					Action action = Action.valueOf(a.getString("action").toUpperCase());
					
					int ffmpegSamples = 16000;
					int ffmpegBitrate = 48;
					boolean ffmpegStereo = false;
					
					if(action == Action.ENCODE) {
						JSONObject ffmpegObj = a.optJSONObject("ffmpeg", null);
						if(ffmpegObj != null) {
							ffmpegSamples = ffmpegObj.optInt("samples", ffmpegSamples);
							ffmpegBitrate = ffmpegObj.optInt("bitrate", ffmpegBitrate);
							ffmpegStereo = ffmpegObj.optBoolean("stereo", ffmpegStereo);
						}
					}
					
					list.add(new ResourceRule(name, wildcard, action, ffmpegSamples, ffmpegBitrate, ffmpegStereo));
				}
			}
		}catch(JSONException ex) {
			throw new IOException("Invalid JSON file: " + conf.getAbsolutePath(), ex);
		}
		
		return new ResourceRulesList(list);
	}
	
	private final List<ResourceRule> list;
	
	private ResourceRulesList(List<ResourceRule> list) {
		this.list = list;
	}
	
	public ResourceRule get(String str) {
		
		for(int i = 0, l = list.size(); i < l; ++i) {
			ResourceRule r = list.get(i);
			if(r.wildcard) {
				if(str.startsWith(r.path)) {
					return r;
				}
			}else {
				if(str.equals(r.path)) {
					return r;
				}
			}
		}
		
		return defaultRule;
	}
	
	private static final ResourceRule defaultRule = new ResourceRule("", true, Action.EXCLUDE, 16000, 48, false);
	
	public static class ResourceRule {
		
		private final String path;
		private final boolean wildcard;
		public final Action action;
		
		public final int ffmpegSamples;
		public final int ffmpegBitrate;
		public final boolean ffmpegStereo;
		
		protected ResourceRule(String path, boolean wildcard, Action action, int ffmpegSamples, int ffmpegBitrate,
				boolean ffmpegStereo) {
			this.path = path;
			this.wildcard = wildcard;
			this.action = action;
			this.ffmpegSamples = ffmpegSamples;
			this.ffmpegBitrate = ffmpegBitrate;
			this.ffmpegStereo = ffmpegStereo;
		}
		
	}
	
	public static enum Action {
		INCLUDE, EXCLUDE, ENCODE, LANGUAGES_ZIP
	}

}

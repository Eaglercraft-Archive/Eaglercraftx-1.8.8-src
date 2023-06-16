package net.lax1dude.eaglercraft.v1_8.internal;

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
public class QueryResponse {

	public final String responseType;
	private final Object responseData;
	public final String serverVersion;
	public final String serverBrand;
	public final String serverName;
	public final long serverTime;
	public final long clientTime;
	public final boolean serverCracked;
	public final long ping;
	
	public QueryResponse(JSONObject obj, long ping) {
		this.responseType = obj.getString("type").toLowerCase();
		this.ping = ping;
		this.responseData = obj.get("data");
		this.serverVersion = obj.getString("vers");
		this.serverBrand = obj.getString("brand");
		this.serverName = obj.getString("name");
		this.serverTime = obj.getLong("time");
		this.clientTime = System.currentTimeMillis();
		this.serverCracked = obj.optBoolean("cracked", false);
	}
	
	public boolean isResponseString() {
		return responseData instanceof String;
	}
	
	public boolean isResponseJSON() {
		return responseData instanceof JSONObject;
	}
	
	public String getResponseString() {
		return (String)responseData;
	}
	
	public JSONObject getResponseJSON() {
		return (JSONObject)responseData;
	}

}

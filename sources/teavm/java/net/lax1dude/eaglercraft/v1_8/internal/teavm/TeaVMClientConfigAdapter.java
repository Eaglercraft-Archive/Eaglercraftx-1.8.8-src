package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;

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
public class TeaVMClientConfigAdapter implements IClientConfigAdapter {

	public static final IClientConfigAdapter instance = new TeaVMClientConfigAdapter();

	private String defaultLocale = "en_US";
	private boolean hideDownDefaultServers = false;
	private List<DefaultServer> defaultServers = new ArrayList();
	private String serverToJoin = null;   

	void loadJSON(JSONObject eaglercraftOpts) {
		defaultLocale = eaglercraftOpts.optString("lang", "en_US");
		serverToJoin = eaglercraftOpts.optString("joinServer", null);
		JSONArray serversArray = eaglercraftOpts.optJSONArray("servers");
		if(serversArray != null) {
			for(int i = 0, l = serversArray.length(); i < l; ++i) {
				JSONObject serverEntry = serversArray.getJSONObject(i);
				String serverAddr = serverEntry.optString("addr", null);
				if(serverAddr != null) {
					String serverName = serverEntry.optString("name", "Default Server #" + i);
					defaultServers.add(new DefaultServer(serverName, serverAddr));
				}
			}
		}
	}

	@Override
	public String getDefaultLocale() {
		return defaultLocale;
	}

	@Override
	public List<DefaultServer> getDefaultServerList() {
		return defaultServers;
	}

	@Override
	public String getServerToJoin() {
		return serverToJoin;
	}

}

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query.EaglerQueryHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query.EaglerQuerySimpleHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query.MOTDConnection;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.MOTDCacheConfiguration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class MOTDQueryHandler extends EaglerQuerySimpleHandler implements MOTDConnection {

	private long creationTime = 0l;

	private String line1;
	private String line2;
	private List<String> players;
	private int[] bitmap;
	private int onlinePlayers;
	private int maxPlayers;
	private boolean hasIcon;
	private boolean iconDirty;
	private String subType;
	private String returnType;

	@Override
	protected void begin(String queryType) {
		creationTime = System.currentTimeMillis();
		subType = queryType;
		returnType = "MOTD";
		EaglerListenerConfig listener = getListener();
		String[] lns = listener.getMotd().split("\n");
		if(lns.length >= 1) {
			line1 = lns[0];
		}
		if(lns.length >= 2) {
			line2 = lns[1];
		}
		maxPlayers = listener.getMaxPlayers();
		onlinePlayers = ProxyServer.getInstance().getOnlineCount();
		players = new ArrayList();
		for(ProxiedPlayer pp : ProxyServer.getInstance().getPlayers()) {
			players.add(pp.getDisplayName());
			if(players.size() >= 9) {
				players.add("" + ChatColor.GRAY + ChatColor.ITALIC + "(" + (onlinePlayers - players.size()) + " more)");
				break;
			}
		}
		bitmap = new int[4096];
		int i = queryType.indexOf('.');
		if(i > 0) {
			subType = queryType.substring(i + 1);
			if(subType.length() == 0) {
				subType = "motd";
			}
		}else {
			subType = "motd";
		}
		if(!subType.startsWith("noicon") && !subType.startsWith("cache.noicon")) {
			int[] maybeIcon = listener.getServerIconPixels();
			iconDirty = hasIcon = maybeIcon != null;
			if(hasIcon) {
				System.arraycopy(maybeIcon, 0, bitmap, 0, 4096);
			}
		}
	}

	@Override
	public long getConnectionTimestamp() {
		return creationTime;
	}

	@Override
	public void sendToUser() {
		if(!isClosed()) {
			JsonObject obj = new JsonObject();
			if(subType.startsWith("cache.anim")) {
				obj.addProperty("unsupported", true);
				sendJsonResponseAndClose(returnType, obj);
				return;
			}else if(subType.startsWith("cache")) {
				JsonArray cacheControl = new JsonArray();
				MOTDCacheConfiguration cc = getListener().getMOTDCacheConfig();
				if(cc.cacheServerListAnimation) {
					cacheControl.add("animation");
				}
				if(cc.cacheServerListResults) {
					cacheControl.add("results");
				}
				if(cc.cacheServerListTrending) {
					cacheControl.add("trending");
				}
				if(cc.cacheServerListPortfolios) {
					cacheControl.add("portfolio");
				}
				obj.add("cache", cacheControl);
				obj.addProperty("ttl", cc.cacheTTL);
			}else {
				MOTDCacheConfiguration cc = getListener().getMOTDCacheConfig();;
				obj.addProperty("cache", cc.cacheServerListAnimation || cc.cacheServerListResults ||
						cc.cacheServerListTrending || cc.cacheServerListPortfolios);
			}
			boolean noIcon = subType.startsWith("noicon") || subType.startsWith("cache.noicon");
			JsonArray motd = new JsonArray();
			if(line1 != null && line1.length() > 0) motd.add(line1);
			if(line2 != null && line2.length() > 0) motd.add(line2);
			obj.add("motd", motd);
			obj.addProperty("icon", hasIcon && !noIcon);
			obj.addProperty("online", onlinePlayers);
			obj.addProperty("max", maxPlayers);
			JsonArray playerz = new JsonArray();
			for(String s : players) {
				playerz.add(s);
			}
			obj.add("players", playerz);
			sendJsonResponse(returnType, obj);
			if(hasIcon && !noIcon && iconDirty && bitmap != null) {
				byte[] iconPixels = new byte[16384];
				for(int i = 0, j; i < 4096; ++i) {
					j = i << 2;
					iconPixels[j] = (byte)((bitmap[i] >> 16) & 0xFF);
					iconPixels[j + 1] = (byte)((bitmap[i] >> 8) & 0xFF);
					iconPixels[j + 2] = (byte)(bitmap[i] & 0xFF);
					iconPixels[j + 3] = (byte)((bitmap[i] >> 24) & 0xFF);
				}
				sendBinaryResponse(iconPixels);
				iconDirty = false;
			}
			if(subType.startsWith("cache")) {
				close();
			}
		}
	}

	@Override
	public String getLine1() {
		return line1;
	}

	@Override
	public String getLine2() {
		return line2;
	}

	@Override
	public List<String> getPlayerList() {
		return players;
	}

	@Override
	public int[] getBitmap() {
		return bitmap;
	}

	@Override
	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	@Override
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	@Override
	public String getSubType() {
		return subType;
	}

	@Override
	public void setLine1(String p) {
		line1 = p;
	}

	@Override
	public void setLine2(String p) {
		line2 = p;
	}

	@Override
	public void setPlayerList(List<String> p) {
		players = p;
	}

	@Override
	public void setPlayerList(String... p) {
		players = Arrays.asList(p);
	}

	@Override
	public void setBitmap(int[] p) {
		iconDirty = hasIcon = true;
		bitmap = p;
	}

	@Override
	public void setOnlinePlayers(int i) {
		onlinePlayers = i;
	}

	@Override
	public void setMaxPlayers(int i) {
		maxPlayers = i;
	}

}

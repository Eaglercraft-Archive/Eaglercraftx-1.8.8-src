package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpContentType;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpWebServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.config.Configuration;

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
public class EaglerListenerConfig extends ListenerInfo {

	static EaglerListenerConfig loadConfig(Configuration config, Map<String, HttpContentType> contentTypes) {
		
		String host = config.getString("address", "0.0.0.0:8081");
		InetSocketAddress hostv4 = null;
		if(host != null && !host.equalsIgnoreCase("null") && !host.equalsIgnoreCase("none")) {
			int i = host.lastIndexOf(':');
			if(i == -1) {
				throw new IllegalArgumentException("Invalid address: " + host + "! Must be an ipv4:port combo");
			}
			hostv4 = new InetSocketAddress(host.substring(0, i), Integer.parseInt(host.substring(i + 1)));
		}
		
		String hostV6 = config.getString("address_v6", "null");
		InetSocketAddress hostv6 = null;
		if(hostV6 != null && !hostV6.equalsIgnoreCase("null") && !hostV6.equalsIgnoreCase("none") && hostV6.length() > 0) {
			int i = hostV6.lastIndexOf(':');
			if(i == -1) {
				throw new IllegalArgumentException("Invalid address: " + host + "! Must be an ipv6:port combo");
			}
			hostv6 = new InetSocketAddress(hostV6.substring(0, i), Integer.parseInt(hostV6.substring(i + 1)));
		}
		
		if(hostv4 == null && hostv6 == null) {
			throw new IllegalArgumentException("Invalid host specifies no addresses, both v4 and v6 address are null");
		}
		
		int maxPlayer = config.getInt("max_players", 60);
		String tabListType = config.getString("tab_list", "GLOBAL_PING");
		String defaultServer = config.getString("default_server", "lobby");
		boolean forceDefaultServer = config.getBoolean("force_default_server", false);
		boolean forwardIp = config.getBoolean("forward_ip", false);
		String forwardIpHeader = config.getString("forward_ip_header", "X-Real-IP");
		String redirectLegacyClientsTo = config.getString("redirect_legacy_clients_to", "null");
		if(redirectLegacyClientsTo != null && (redirectLegacyClientsTo.equalsIgnoreCase("null") || redirectLegacyClientsTo.length() == 0)) {
			redirectLegacyClientsTo = null;
		}
		String serverIcon = config.getString("server_icon", "server-icon.png");
		List<String> serverMOTD = (List<String>) config.getList("server_motd", Arrays.asList("&6An EaglercraftX server"));
		for(int i = 0, l = serverMOTD.size(); i < l; ++i) {
			serverMOTD.set(i, ChatColor.translateAlternateColorCodes('&', serverMOTD.get(i)));
		}
		boolean allowMOTD = config.getBoolean("allow_motd", false);
		boolean allowQuery = config.getBoolean("allow_query", false);
		
		int cacheTTL = 7200;
		boolean cacheAnimation = false;
		boolean cacheResults = true;
		boolean cacheTrending = true;
		boolean cachePortfolios = false;
		
		Configuration cacheConf = config.getSection("request_motd_cache");
		if(cacheConf != null) {
			cacheTTL = cacheConf.getInt("cache_ttl", 7200);
			cacheAnimation = cacheConf.getBoolean("online_server_list_animation", false);
			cacheResults = cacheConf.getBoolean("online_server_list_results", true);
			cacheTrending = cacheConf.getBoolean("online_server_list_trending", true);
			cachePortfolios = cacheConf.getBoolean("online_server_list_portfolios", false);
		}
		
		HttpWebServer httpServer = null;
		Configuration httpServerConf = config.getSection("http_server");
		
		if(httpServerConf != null && httpServerConf.getBoolean("enabled", false)) {
			String rootDirectory = httpServerConf.getString("root", "web");
			String page404 = httpServerConf.getString("page_404_not_found", "default");
			if(page404 != null && (page404.length() == 0 || page404.equalsIgnoreCase("null") || page404.equalsIgnoreCase("default"))) {
				page404 = null;
			}
			List<String> defaultIndex = Arrays.asList("index.html", "index.htm");
			List indexPageRaw = httpServerConf.getList("page_index_name", defaultIndex);
			List<String> indexPage = new ArrayList(indexPageRaw.size());
			
			for(int i = 0, l = indexPageRaw.size(); i < l; ++i) {
				Object o = indexPageRaw.get(i);
				if(o instanceof String) {
					indexPage.add((String)o);
				}
			}
			
			if(indexPage.size() == 0) {
				indexPage.addAll(defaultIndex);
			}
			
			httpServer = new HttpWebServer(new File(EaglerXBungee.getEagler().getDataFolder(), rootDirectory),
					contentTypes, indexPage, page404);
		}
		
		EaglerRateLimiter ratelimitIp = null;
		EaglerRateLimiter ratelimitLogin = null;
		EaglerRateLimiter ratelimitMOTD = null;
		EaglerRateLimiter ratelimitQuery = null;
		
		Configuration rateLimitConfig = config.getSection("ratelimit");
		if(rateLimitConfig != null) {
			Configuration ratelimitIpConfig = rateLimitConfig.getSection("ip");
			if(ratelimitIpConfig != null && ratelimitIpConfig.getBoolean("enable", false)) {
				ratelimitIp = EaglerRateLimiter.loadConfig(ratelimitIpConfig);
			}
			Configuration ratelimitLoginConfig = rateLimitConfig.getSection("login");
			if(ratelimitLoginConfig != null && ratelimitLoginConfig.getBoolean("enable", false)) {
				ratelimitLogin = EaglerRateLimiter.loadConfig(ratelimitLoginConfig);
			}
			Configuration ratelimitMOTDConfig = rateLimitConfig.getSection("motd");
			if(ratelimitMOTDConfig != null && ratelimitMOTDConfig.getBoolean("enable", false)) {
				ratelimitMOTD = EaglerRateLimiter.loadConfig(ratelimitMOTDConfig);
			}
			Configuration ratelimitQueryConfig = rateLimitConfig.getSection("query");
			if(ratelimitQueryConfig != null && ratelimitQueryConfig.getBoolean("enable", false)) {
				ratelimitQuery = EaglerRateLimiter.loadConfig(ratelimitQueryConfig);
			}
		}
		
		MOTDCacheConfiguration cacheConfig = new MOTDCacheConfiguration(cacheTTL, cacheAnimation, cacheResults,
				cacheTrending, cachePortfolios);
		return new EaglerListenerConfig(hostv4, hostv6, maxPlayer, tabListType, defaultServer, forceDefaultServer,
				forwardIp, forwardIpHeader, redirectLegacyClientsTo, serverIcon, serverMOTD, allowMOTD, allowQuery,
				cacheConfig, httpServer, ratelimitIp, ratelimitLogin, ratelimitMOTD, ratelimitQuery);
	}

	private final InetSocketAddress address;
	private final InetSocketAddress addressV6;
	private final int maxPlayer;
	private final String tabListType;
	private final String defaultServer;
	private final boolean forceDefaultServer;
	private final boolean forwardIp;
	private final String forwardIpHeader;
	private final String redirectLegacyClientsTo;
	private final String serverIcon;
	private final List<String> serverMOTD;
	private final boolean allowMOTD;
	private final boolean allowQuery;
	private final MOTDCacheConfiguration motdCacheConfig;
	private final HttpWebServer webServer;
	private boolean serverIconSet = false;
	private int[] serverIconPixels = null;
	private final EaglerRateLimiter ratelimitIp;
	private final EaglerRateLimiter ratelimitLogin;
	private final EaglerRateLimiter ratelimitMOTD;
	private final EaglerRateLimiter ratelimitQuery;
	
	public EaglerListenerConfig(InetSocketAddress address, InetSocketAddress addressV6, int maxPlayer,
			String tabListType, String defaultServer, boolean forceDefaultServer, boolean forwardIp,
			String forwardIpHeader, String redirectLegacyClientsTo, String serverIcon, List<String> serverMOTD,
			boolean allowMOTD, boolean allowQuery, MOTDCacheConfiguration motdCacheConfig, HttpWebServer webServer,
			EaglerRateLimiter ratelimitIp, EaglerRateLimiter ratelimitLogin, EaglerRateLimiter ratelimitMOTD,
			EaglerRateLimiter ratelimitQuery) {
		super(address, String.join("\n", serverMOTD), maxPlayer, 60, Arrays.asList(defaultServer), forceDefaultServer,
				Collections.emptyMap(), tabListType, false, false, 0, false, false);
		this.address = address;
		this.addressV6 = addressV6;
		this.maxPlayer = maxPlayer;
		this.tabListType = tabListType;
		this.defaultServer = defaultServer;
		this.forceDefaultServer = forceDefaultServer;
		this.forwardIp = forwardIp;
		this.forwardIpHeader = forwardIpHeader;
		this.redirectLegacyClientsTo = redirectLegacyClientsTo;
		this.serverIcon = serverIcon;
		this.serverMOTD = serverMOTD;
		this.allowMOTD = allowMOTD;
		this.allowQuery = allowQuery;
		this.motdCacheConfig = motdCacheConfig;
		this.webServer = webServer;
		this.ratelimitIp = ratelimitIp;
		this.ratelimitLogin = ratelimitLogin;
		this.ratelimitMOTD = ratelimitMOTD;
		this.ratelimitQuery = ratelimitQuery;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public InetSocketAddress getAddressV6() {
		return addressV6;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public String getTabListType() {
		return tabListType;
	}

	public String getDefaultServer() {
		return defaultServer;
	}

	public boolean isForceDefaultServer() {
		return forceDefaultServer;
	}

	public boolean isForwardIp() {
		return forwardIp;
	}

	public String getForwardIpHeader() {
		return forwardIpHeader;
	}

	public String getServerIconName() {
		return serverIcon;
	}

	public int[] getServerIconPixels() {
		if(!serverIconSet) {
			if(serverIcon != null) {
				File f = new File(serverIcon);
				if(f.isFile()) {
					serverIconPixels = ServerIconLoader.createServerIcon(f);
					if(serverIconPixels == null) {
						EaglerXBungee.logger().warning("Server icon could not be loaded: " + f.getAbsolutePath());
					}
				}else {
					EaglerXBungee.logger().warning("Server icon is not a file: " + f.getAbsolutePath());
				}
			}
			serverIconSet = true;
		}
		return serverIconPixels;
	}

	public List<String> getServerMOTD() {
		return serverMOTD;
	}

	public boolean isAllowMOTD() {
		return allowMOTD;
	}

	public boolean isAllowQuery() {
		return allowQuery;
	}
	
	public HttpWebServer getWebServer() {
		return webServer;
	}

	public MOTDCacheConfiguration getMOTDCacheConfig() {
		return motdCacheConfig;
	}
	
	public boolean blockRequest(HttpRequest request) {
		return false;
	}

	public String redirectLegacyClientsTo() {
		return redirectLegacyClientsTo;
	}

	public EaglerRateLimiter getRatelimitIp() {
		return ratelimitIp;
	}

	public EaglerRateLimiter getRatelimitLogin() {
		return ratelimitLogin;
	}

	public EaglerRateLimiter getRatelimitMOTD() {
		return ratelimitMOTD;
	}

	public EaglerRateLimiter getRatelimitQuery() {
		return ratelimitQuery;
	}

}

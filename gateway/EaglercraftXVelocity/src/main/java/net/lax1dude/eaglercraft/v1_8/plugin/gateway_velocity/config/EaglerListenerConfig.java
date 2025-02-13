/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.bungee.ChatColor;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.bungee.Configuration;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.web.HttpContentType;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.web.HttpWebServer;

public class EaglerListenerConfig {

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
		boolean allowMOTD = config.getBoolean("allow_motd", true);
		boolean allowQuery = config.getBoolean("allow_query", true);
		int minMCProtocol = config.getInt("min_minecraft_protocol", 47);
		int maxMCProtocol = config.getInt("max_minecraft_protocol", 340);
		boolean allowV3 = config.getBoolean("allow_protocol_v3", true);
		boolean allowV4 = config.getBoolean("allow_protocol_v4", true);
		if(!allowV3 && !allowV4) {
			throw new IllegalArgumentException("Both v3 and v4 protocol are disabled!");
		}
		int defragSendDelay = config.getInt("protocol_v4_defrag_send_delay", 10);
		boolean haproxyProtocol = config.getBoolean("use_haproxy_protocol", false);
		
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
			List<?> indexPageRaw = httpServerConf.getList("page_index_name", defaultIndex);
			List<String> indexPage = new ArrayList<>(indexPageRaw.size());
			
			for(int i = 0, l = indexPageRaw.size(); i < l; ++i) {
				Object o = indexPageRaw.get(i);
				if(o instanceof String) {
					indexPage.add((String)o);
				}
			}
			
			if(indexPage.size() == 0) {
				indexPage.addAll(defaultIndex);
			}
			
			httpServer = new HttpWebServer(new File(EaglerXVelocity.getEagler().getDataFolder(), rootDirectory),
					contentTypes, indexPage, page404);
		}
		
		boolean enableVoiceChat = config.getBoolean("allow_voice", false);
		
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
		return new EaglerListenerConfig(hostv4, hostv6, maxPlayer,
				forwardIp, forwardIpHeader, redirectLegacyClientsTo, serverIcon, serverMOTD, allowMOTD, allowQuery,
				minMCProtocol, maxMCProtocol, allowV3, allowV4, defragSendDelay, haproxyProtocol, cacheConfig,
				httpServer, enableVoiceChat, ratelimitIp, ratelimitLogin, ratelimitMOTD, ratelimitQuery);
	}

	private final InetSocketAddress address;
	private final InetSocketAddress addressV6;
	private final int maxPlayer;
	private final boolean forwardIp;
	private final String forwardIpHeader;
	private final String redirectLegacyClientsTo;
	private final String serverIcon;
	private final List<String> serverMOTD;
	private final boolean allowMOTD;
	private final boolean allowQuery;
	private final int minMCProtocol;
	private final int maxMCProtocol;
	private final boolean allowV3;
	private final boolean allowV4;
	private final int defragSendDelay;
	private final boolean haproxyProtocol;
	private final MOTDCacheConfiguration motdCacheConfig;
	private final HttpWebServer webServer;
	private boolean serverIconSet = false;
	private int[] serverIconPixels = null;
	private final boolean enableVoiceChat;
	private final EaglerRateLimiter ratelimitIp;
	private final EaglerRateLimiter ratelimitLogin;
	private final EaglerRateLimiter ratelimitMOTD;
	private final EaglerRateLimiter ratelimitQuery;
	
	public EaglerListenerConfig(InetSocketAddress address, InetSocketAddress addressV6, int maxPlayer,
			boolean forwardIp, String forwardIpHeader, String redirectLegacyClientsTo, String serverIcon,
			List<String> serverMOTD, boolean allowMOTD, boolean allowQuery, int minMCProtocol, int maxMCProtocol,
			boolean allowV3, boolean allowV4, int defragSendDelay, boolean haproxyProtocol,
			MOTDCacheConfiguration motdCacheConfig, HttpWebServer webServer, boolean enableVoiceChat,
			EaglerRateLimiter ratelimitIp, EaglerRateLimiter ratelimitLogin, EaglerRateLimiter ratelimitMOTD,
			EaglerRateLimiter ratelimitQuery) {
		this.address = address;
		this.addressV6 = addressV6;
		this.maxPlayer = maxPlayer;
		this.forwardIp = forwardIp;
		this.forwardIpHeader = forwardIpHeader;
		this.redirectLegacyClientsTo = redirectLegacyClientsTo;
		this.serverIcon = serverIcon;
		this.serverMOTD = serverMOTD;
		this.allowMOTD = allowMOTD;
		this.allowQuery = allowQuery;
		this.minMCProtocol = minMCProtocol;
		this.maxMCProtocol = maxMCProtocol;
		this.allowV3 = allowV3;
		this.allowV4 = allowV4;
		this.defragSendDelay = defragSendDelay;
		this.haproxyProtocol = haproxyProtocol;
		this.motdCacheConfig = motdCacheConfig;
		this.webServer = webServer;
		this.enableVoiceChat = enableVoiceChat;
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
						EaglerXVelocity.logger().warn("Server icon could not be loaded: {}", f.getAbsolutePath());
					}
				}else {
					EaglerXVelocity.logger().warn("Server icon is not a file: {}", f.getAbsolutePath());
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

	public int getMinMCProtocol() {
		return minMCProtocol;
	}

	public int getMaxMCProtocol() {
		return maxMCProtocol;
	}

	public boolean isAllowV3() {
		return allowV3;
	}

	public boolean isAllowV4() {
		return allowV4;
	}

	public int getDefragSendDelay() {
		return defragSendDelay;
	}

	public boolean isHAProxyProtocol() {
		return haproxyProtocol;
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

	public boolean getEnableVoiceChat() {
		return enableVoiceChat;
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
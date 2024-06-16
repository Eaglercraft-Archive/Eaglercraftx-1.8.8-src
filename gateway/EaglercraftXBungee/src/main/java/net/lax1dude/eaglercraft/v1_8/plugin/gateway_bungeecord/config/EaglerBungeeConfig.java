package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpContentType;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.protocol.Property;

/**
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
public class EaglerBungeeConfig {
	
	public static EaglerBungeeConfig loadConfig(File directory) throws IOException {
		Map<String, HttpContentType> contentTypes = new HashMap();
		
		try(InputStream is = new FileInputStream(getConfigFile(directory, "http_mime_types.json"))) {
			loadMimeTypes(is, contentTypes);
		}catch(Throwable t) {
			try(InputStream is = EaglerBungeeConfig.class.getResourceAsStream("default_http_mime_types.json")) {
				loadMimeTypes(is, contentTypes);
			}catch(IOException ex) {
				EaglerXBungee.logger().severe("Could not load default_http_mime_types.json!");
				throw new RuntimeException(ex);
			}
		}
		
		directory.mkdirs();
		ConfigurationProvider prov = ConfigurationProvider.getProvider(YamlConfiguration.class);
		
		Configuration configYml = prov.load(getConfigFile(directory, "settings.yml"));
		String serverName = configYml.getString("server_name", "EaglercraftXBungee Server");
		String serverUUIDString = configYml.getString("server_uuid", null);
		if(serverUUIDString == null) {
			throw new IOException("You must specify a server_uuid!");
		}
		
		UUID serverUUID = null;
		try {
			serverUUID = UUID.fromString(serverUUIDString);
		}catch(Throwable t) {
		}
		
		if(serverUUID == null) {
			throw new IOException("The server_uuid \"" + serverUUIDString + "\" is invalid!");
		}
		
		Configuration listenerYml = prov.load(getConfigFile(directory, "listeners.yml"));
		Iterator<String> listeners = listenerYml.getKeys().iterator();
		Map<String, EaglerListenerConfig> serverListeners = new HashMap();
		boolean voiceChat = false;
		
		while(listeners.hasNext()) {
			String name = listeners.next();
			EaglerListenerConfig conf = EaglerListenerConfig.loadConfig(listenerYml.getSection(name), contentTypes);
			if(conf != null) {
				serverListeners.put(name, conf);
				voiceChat |= conf.getEnableVoiceChat();
			}else {
				EaglerXBungee.logger().severe("Invalid listener config: " + name);
			}
		}
		
		if(serverListeners.size() == 0) {
			EaglerXBungee.logger().severe("No Listeners Configured!");
		}
		
		Configuration authserivceYml = prov.load(getConfigFile(directory, "authservice.yml"));
		EaglerAuthConfig authConfig = EaglerAuthConfig.loadConfig(authserivceYml);
		
		Configuration updatesYml = prov.load(getConfigFile(directory, "updates.yml"));
		EaglerUpdateConfig updatesConfig = EaglerUpdateConfig.loadConfig(updatesYml);
		
		Configuration iceServersYml = prov.load(getConfigFile(directory, "ice_servers.yml"));
		Collection<String> iceServers = loadICEServers(iceServersYml);
		
		if(authConfig.isEnableAuthentication()) {
			for(EaglerListenerConfig lst : serverListeners.values()) {
				if(lst.getRatelimitLogin() != null) lst.getRatelimitLogin().setDivisor(2);
				if(lst.getRatelimitIp() != null) lst.getRatelimitIp().setDivisor(2);
			}
		}
		
		long websocketKeepAliveTimeout = configYml.getInt("websocket_connection_timeout", 15000);
		long websocketHandshakeTimeout = configYml.getInt("websocket_handshake_timeout", 5000);
		int websocketCompressionLevel = configYml.getInt("http_websocket_compression_level", 6);
		
		boolean downloadVanillaSkins = configYml.getBoolean("download_vanilla_skins_to_clients", false);
		Collection<String> validSkinUrls = (Collection<String>)configYml.getList("valid_skin_download_urls");
		int uuidRateLimitPlayer = configYml.getInt("uuid_lookup_ratelimit_player", 50);
		int uuidRateLimitGlobal = configYml.getInt("uuid_lookup_ratelimit_global", 175);
		int skinRateLimitPlayer = configYml.getInt("skin_download_ratelimit_player", 1000);
		int skinRateLimitGlobal = configYml.getInt("skin_download_ratelimit_global", 30000);
		
		String skinCacheURI = configYml.getString("skin_cache_db_uri", "jdbc:sqlite:eaglercraft_skins_cache.db");
		int keepObjectsDays = configYml.getInt("skin_cache_keep_objects_days", 45);
		int keepProfilesDays = configYml.getInt("skin_cache_keep_profiles_days", 7);
		int maxObjects = configYml.getInt("skin_cache_max_objects", 32768);
		int maxProfiles = configYml.getInt("skin_cache_max_profiles", 32768);
		int antagonistsRateLimit = configYml.getInt("skin_cache_antagonists_ratelimit", 15);
		String sqliteDriverClass = configYml.getString("sql_driver_class", "internal");
		String sqliteDriverPath = configYml.getString("sql_driver_path", null);
		String eaglerPlayersVanillaSkin = configYml.getString("eagler_players_vanilla_skin", null);
		if(eaglerPlayersVanillaSkin != null && eaglerPlayersVanillaSkin.length() == 0) {
			eaglerPlayersVanillaSkin = null;
		}
		boolean enableIsEaglerPlayerProperty = configYml.getBoolean("enable_is_eagler_player_property", true);
		Set<String> disableVoiceOnServers = new HashSet((Collection<String>)configYml.getList("disable_voice_chat_on_servers"));
		boolean disableFNAWSkinsEverywhere = configYml.getBoolean("disable_fnaw_skins_everywhere", false);
		Set<String> disableFNAWSkinsOnServers = new HashSet((Collection<String>)configYml.getList("disable_fnaw_skins_on_servers"));
		
		final EaglerBungeeConfig ret = new EaglerBungeeConfig(serverName, serverUUID, websocketKeepAliveTimeout,
				websocketHandshakeTimeout, websocketCompressionLevel, serverListeners, contentTypes,
				downloadVanillaSkins, validSkinUrls, uuidRateLimitPlayer, uuidRateLimitGlobal, skinRateLimitPlayer,
				skinRateLimitGlobal, skinCacheURI, keepObjectsDays, keepProfilesDays, maxObjects, maxProfiles,
				antagonistsRateLimit, sqliteDriverClass, sqliteDriverPath, eaglerPlayersVanillaSkin,
				enableIsEaglerPlayerProperty, authConfig, updatesConfig, iceServers, voiceChat,
				disableVoiceOnServers, disableFNAWSkinsEverywhere, disableFNAWSkinsOnServers);
		
		if(eaglerPlayersVanillaSkin != null) {
			VanillaDefaultSkinProfileLoader.lookupVanillaSkinUser(ret);
		}
		
		return ret;
	}
	
	private static File getConfigFile(File directory, String fileName) throws IOException {
		File file = new File(directory, fileName);
		if(!file.isFile()) {
			try (BufferedReader is = new BufferedReader(new InputStreamReader(
					EaglerBungeeConfig.class.getResourceAsStream("default_" + fileName), StandardCharsets.UTF_8));
					PrintWriter os = new PrintWriter(new FileWriter(file))) {
				String line;
				while((line = is.readLine()) != null) {
					if(line.contains("${")) {
						line = line.replace("${random_uuid}", UUID.randomUUID().toString());
					}
					os.println(line);
				}
			}
		}
		return file;
	}
	
	private static void loadMimeTypes(InputStream file, Map<String, HttpContentType> contentTypes) throws IOException {
		JsonObject obj = parseJsonObject(file);
		for(Entry<String, JsonElement> etr : obj.entrySet()) {
			String mime = etr.getKey();
			try {
				JsonObject e = etr.getValue().getAsJsonObject();
				JsonArray arr = e.getAsJsonArray("files");
				if(arr == null || arr.size() == 0) {
					EaglerXBungee.logger().warning("MIME type '" + mime + "' defines no extensions!");
					continue;
				}
				HashSet<String> exts = new HashSet();
				for(int i = 0, l = arr.size(); i < l; ++i) {
					exts.add(arr.get(i).getAsString());
				}
				long expires = 0l;
				JsonElement ex = e.get("expires");
				if(ex != null) {
					expires = ex.getAsInt() * 1000l;
				}
				String charset = null;
				ex = e.get("charset");
				if(ex != null) {
					charset = ex.getAsString();
				}
				HttpContentType typeObj = new HttpContentType(exts, mime, charset, expires);
				for(String s : exts) {
					contentTypes.put(s, typeObj);
				}
			}catch(Throwable t) {
				EaglerXBungee.logger().warning("Exception parsing MIME type '" + mime + "' - " + t.toString());
			}
		}
	}
	
	private static Collection<String> loadICEServers(Configuration config) {
		Collection<String> ret = new ArrayList(config.getList("voice_stun_servers"));
		Configuration turnServers = config.getSection("voice_turn_servers");
		Iterator<String> turnItr = turnServers.getKeys().iterator();
		while(turnItr.hasNext()) {
			String name = turnItr.next();
			Configuration turnSvr = turnServers.getSection(name);
			ret.add(turnSvr.getString("url") + ";" + turnSvr.getString("username") + ";" + turnSvr.getString("password"));
		}
		return ret;
	}
	
	private static JsonObject parseJsonObject(InputStream file) throws IOException {
		try {
			return JsonParser.parseReader(new InputStreamReader(file, StandardCharsets.UTF_8)).getAsJsonObject();
		}catch(JsonSyntaxException ex) {
			throw new IOException("Invalid JSONObject", ex);
		}
	}

	public static final Property isEaglerProperty = new Property("isEaglerPlayer", "true");

	private final String serverName;
	private final UUID serverUUID;
	private final long websocketKeepAliveTimeout;
	private final long websocketHandshakeTimeout;
	private final int httpWebsocketCompressionLevel;
	private final Map<String, EaglerListenerConfig> serverListeners;
	private final Map<String, HttpContentType> contentTypes;
	private final boolean downloadVanillaSkins;
	private final Collection<String> validSkinUrls;
	private final int uuidRateLimitPlayer;
	private final int uuidRateLimitGlobal;
	private final int skinRateLimitPlayer;
	private final int skinRateLimitGlobal;
	private final String skinCacheURI;
	private final int keepObjectsDays;
	private final int keepProfilesDays;
	private final int maxObjects;
	private final int maxProfiles;
	private final int antagonistsRateLimit;
	private final String sqliteDriverClass;
	private final String sqliteDriverPath;
	private final String eaglerPlayersVanillaSkin;
	private final boolean enableIsEaglerPlayerProperty;
	private final EaglerAuthConfig authConfig;
	private final EaglerUpdateConfig updateConfig;
	private final Collection<String> iceServers;
	private final boolean enableVoiceChat;
	private final Set<String> disableVoiceOnServers;
	private final boolean disableFNAWSkinsEverywhere;
	private final Set<String> disableFNAWSkinsOnServers;
	private boolean isCrackedFlag;
	Property[] eaglerPlayersVanillaSkinCached = new Property[] { isEaglerProperty };

	public String getServerName() {
		return serverName;
	}

	public UUID getServerUUID() {
		return serverUUID;
	}

	public long getWebsocketKeepAliveTimeout() {
		return websocketKeepAliveTimeout;
	}
	
	public long getWebsocketHandshakeTimeout() {
		return websocketHandshakeTimeout;
	}
	
	public int getHttpWebsocketCompressionLevel() {
		return httpWebsocketCompressionLevel;
	}

	public Collection<EaglerListenerConfig> getServerListeners() {
		return serverListeners.values();
	}

	public EaglerListenerConfig getServerListenersByName(String name) {
		return serverListeners.get(name);
	}

	public Map<String, HttpContentType> getContentType() {
		return contentTypes;
	}

	public Map<String, HttpContentType> getContentTypes() {
		return contentTypes;
	}

	public boolean getDownloadVanillaSkins() {
		return downloadVanillaSkins;
	}

	public Collection<String> getValidSkinUrls() {
		return validSkinUrls;
	}

	public boolean isValidSkinHost(String host) {
		host = host.toLowerCase();
		for(String str : validSkinUrls) {
			if(str.length() > 0) {
				str = str.toLowerCase();
				if(str.charAt(0) == '*') {
					if(host.endsWith(str.substring(1))) {
						return true;
					}
				}else {
					if(host.equals(str)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int getUuidRateLimitPlayer() {
		return uuidRateLimitPlayer;
	}

	public int getUuidRateLimitGlobal() {
		return uuidRateLimitGlobal;
	}

	public int getSkinRateLimitPlayer() {
		return skinRateLimitPlayer;
	}

	public int getSkinRateLimitGlobal() {
		return skinRateLimitGlobal;
	}

	public String getSkinCacheURI() {
		return skinCacheURI;
	}

	public String getSQLiteDriverClass() {
		return sqliteDriverClass;
	}

	public String getSQLiteDriverPath() {
		return sqliteDriverPath;
	}

	public int getKeepObjectsDays() {
		return keepObjectsDays;
	}

	public int getKeepProfilesDays() {
		return keepProfilesDays;
	}

	public int getMaxObjects() {
		return maxObjects;
	}

	public int getMaxProfiles() {
		return maxProfiles;
	}

	public int getAntagonistsRateLimit() {
		return antagonistsRateLimit;
	}

	public String getEaglerPlayersVanillaSkin() {
		return eaglerPlayersVanillaSkin;
	}

	public boolean getEnableIsEaglerPlayerProperty() {
		return enableIsEaglerPlayerProperty;
	}

	public Property[] getEaglerPlayersVanillaSkinProperties() {
		return eaglerPlayersVanillaSkinCached;
	}

	public boolean isCracked() {
		return isCrackedFlag;
	}

	public void setCracked(boolean cracked) {
		isCrackedFlag = cracked;
	}

	public EaglerAuthConfig getAuthConfig() {
		return authConfig;
	}

	public EaglerUpdateConfig getUpdateConfig() {
		return updateConfig;
	}

	public Collection<String> getICEServers() {
		return iceServers;
	}

	public boolean getEnableVoiceChat() {
		return enableVoiceChat;
	}

	public Set<String> getDisableVoiceOnServersSet() {
		return disableVoiceOnServers;
	}

	public boolean getDisableFNAWSkinsEverywhere() {
		return disableFNAWSkinsEverywhere;
	}

	public Set<String> getDisableFNAWSkinsOnServersSet() {
		return disableFNAWSkinsOnServers;
	}

	private EaglerBungeeConfig(String serverName, UUID serverUUID, long websocketKeepAliveTimeout,
			long websocketHandshakeTimeout, int httpWebsocketCompressionLevel,
			Map<String, EaglerListenerConfig> serverListeners, Map<String, HttpContentType> contentTypes,
			boolean downloadVanillaSkins, Collection<String> validSkinUrls, int uuidRateLimitPlayer,
			int uuidRateLimitGlobal, int skinRateLimitPlayer, int skinRateLimitGlobal, String skinCacheURI,
			int keepObjectsDays, int keepProfilesDays, int maxObjects, int maxProfiles, int antagonistsRateLimit,
			String sqliteDriverClass, String sqliteDriverPath, String eaglerPlayersVanillaSkin,
			boolean enableIsEaglerPlayerProperty, EaglerAuthConfig authConfig, EaglerUpdateConfig updateConfig,
			Collection<String> iceServers, boolean enableVoiceChat, Set<String> disableVoiceOnServers,
			boolean disableFNAWSkinsEverywhere, Set<String> disableFNAWSkinsOnServers) {
		this.serverName = serverName;
		this.serverUUID = serverUUID;
		this.serverListeners = serverListeners;
		this.websocketHandshakeTimeout = websocketHandshakeTimeout;
		this.websocketKeepAliveTimeout = websocketKeepAliveTimeout;
		this.httpWebsocketCompressionLevel = httpWebsocketCompressionLevel;
		this.contentTypes = contentTypes;
		this.downloadVanillaSkins = downloadVanillaSkins;
		this.validSkinUrls = validSkinUrls;
		this.uuidRateLimitPlayer = uuidRateLimitPlayer;
		this.uuidRateLimitGlobal = uuidRateLimitGlobal;
		this.skinRateLimitPlayer = skinRateLimitPlayer;
		this.skinRateLimitGlobal = skinRateLimitGlobal;
		this.skinCacheURI = skinCacheURI;
		this.keepObjectsDays = keepObjectsDays;
		this.keepProfilesDays = keepProfilesDays;
		this.maxObjects = maxObjects;
		this.maxProfiles = maxProfiles;
		this.antagonistsRateLimit = antagonistsRateLimit;
		this.sqliteDriverClass = sqliteDriverClass;
		this.sqliteDriverPath = sqliteDriverPath;
		this.eaglerPlayersVanillaSkin = eaglerPlayersVanillaSkin;
		this.enableIsEaglerPlayerProperty = enableIsEaglerPlayerProperty;
		this.authConfig = authConfig;
		this.updateConfig = updateConfig;
		this.iceServers = iceServers;
		this.enableVoiceChat = enableVoiceChat;
		this.disableVoiceOnServers = disableVoiceOnServers;
		this.disableFNAWSkinsEverywhere = disableFNAWSkinsEverywhere;
		this.disableFNAWSkinsOnServers = disableFNAWSkinsOnServers;
	}

}

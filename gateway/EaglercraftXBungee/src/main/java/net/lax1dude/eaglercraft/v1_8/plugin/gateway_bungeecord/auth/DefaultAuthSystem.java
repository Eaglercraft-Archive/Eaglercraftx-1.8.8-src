package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftHandleAuthPasswordEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftIsAuthRequiredEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftIsAuthRequiredEvent.AuthMethod;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftIsAuthRequiredEvent.AuthResponse;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.sqlite.EaglerDrivers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.protocol.Property;

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
public class DefaultAuthSystem {

	public static class AuthSystemException extends RuntimeException {

		public AuthSystemException() {
		}

		public AuthSystemException(String message, Throwable cause) {
			super(message, cause);
		}

		public AuthSystemException(String message) {
			super(message);
		}

		public AuthSystemException(Throwable cause) {
			super(cause);
		}

	}

	protected final String uri;
	protected final Connection databaseConnection;
	protected final String passwordPromptScreenText;
	protected final String wrongPasswordScreenText;
	protected final String notRegisteredScreenText;
	protected final String eaglerCommandName;
	protected final String useRegisterCommandText;
	protected final String useChangeCommandText;
	protected final String commandSuccessText;
	protected final String lastEaglerLoginMessage;
	protected final String tooManyRegistrationsMessage;
	protected final String needVanillaToRegisterMessage;
	protected final boolean overrideEaglerToVanillaSkins;
	protected final int maxRegistrationsPerIP;

	protected final SecureRandom secureRandom;

	public static DefaultAuthSystem initializeAuthSystem(EaglerAuthConfig config) throws AuthSystemException {
		String databaseURI = config.getDatabaseURI();
		Connection conn;
		try {
			conn = EaglerDrivers.connectToDatabase(databaseURI, config.getDriverClass(), config.getDriverPath(), new Properties());
			if(conn == null) {
				throw new IllegalStateException("Connection is null");
			}
		}catch(Throwable t) {
			throw new AuthSystemException("Could not initialize '" + databaseURI + "'!", t);
		}
		EaglerXBungee.logger().info("Connected to database: " + databaseURI);
		try {
			try(Statement stmt = conn.createStatement()) {
				stmt.execute("CREATE TABLE IF NOT EXISTS \"eaglercraft_accounts\" ("
						+ "\"Version\"	TINYINT NOT NULL,"
						+ "\"MojangUUID\"	TEXT(32) NOT NULL,"
						+ "\"MojangUsername\"	TEXT(16) NOT NULL,"
						+ "\"HashBase\"	BLOB NOT NULL,"
						+ "\"HashSalt\"	BLOB NOT NULL,"
						+ "\"MojangTextures\"	BLOB,"
						+ "\"Registered\"	DATETIME NOT NULL,"
						+ "\"RegisteredIP\"	VARCHAR(42) NOT NULL,"
						+ "\"LastLogin\"	DATETIME,"
						+ "\"LastLoginIP\"	VARCHAR(42),"
						+ "PRIMARY KEY(\"MojangUUID\"))");
				stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS \"MojangUsername\" ON "
						+ "\"eaglercraft_accounts\" (\"MojangUsername\")");
			}
			return new DefaultAuthSystem(databaseURI, conn, config.getPasswordPromptScreenText(),
					config.getWrongPasswordScreenText(), config.getNotRegisteredScreenText(),
					config.getEaglerCommandName(), config.getUseRegisterCommandText(), config.getUseChangeCommandText(),
					config.getCommandSuccessText(), config.getLastEaglerLoginMessage(),
					config.getTooManyRegistrationsMessage(), config.getNeedVanillaToRegisterMessage(),
					config.getOverrideEaglerToVanillaSkins(), config.getMaxRegistrationsPerIP());
		}catch(AuthSystemException ex) {
			try {
				conn.close();
			}catch(SQLException exx) {
			}
			throw ex;
		}catch(Throwable t) {
			try {
				conn.close();
			}catch(SQLException exx) {
			}
			throw new AuthSystemException("Could not initialize '" + databaseURI + "'!", t);
		}
	}

	protected final PreparedStatement registerUser;
	protected final PreparedStatement isRegisteredUser;
	protected final PreparedStatement pruneUsers;
	protected final PreparedStatement updatePassword;
	protected final PreparedStatement updateMojangUsername;
	protected final PreparedStatement getRegistrationsOnIP;
	protected final PreparedStatement checkRegistrationByUUID;
	protected final PreparedStatement checkRegistrationByName;
	protected final PreparedStatement setLastLogin;
	protected final PreparedStatement updateTextures;

	protected class AccountLoader implements AuthLoadingCache.CacheLoader<String, CachedAccountInfo> {

		@Override
		public CachedAccountInfo load(String key) {
			try {
				CachedAccountInfo cachedInfo = null;
				synchronized(checkRegistrationByName) {
					checkRegistrationByName.setString(1, key);
					try(ResultSet res = checkRegistrationByName.executeQuery()) {
						if (res.next()) {
							cachedInfo = new CachedAccountInfo(res.getInt(1), parseMojangUUID(res.getString(2)), key,
									res.getBytes(3), res.getBytes(4), res.getBytes(5), res.getDate(6), res.getString(7),
									res.getDate(8), res.getString(9));
						}
					}
				}
				return cachedInfo;
			}catch(SQLException ex) {
				throw new AuthException("Failed to query database!", ex);
			}
		}

	}

	protected class CachedAccountInfo {

		protected int version;
		protected UUID mojangUUID;
		protected String mojangUsername;
		protected byte[] texturesProperty;
		protected byte[] hashBase;
		protected byte[] hashSalt;
		protected long registered;
		protected String registeredIP;
		protected long lastLogin;
		protected String lastLoginIP;

		protected CachedAccountInfo(int version, UUID mojangUUID, String mojangUsername, byte[] texturesProperty,
				byte[] hashBase, byte[] hashSalt, Date registered, String registeredIP, Date lastLogin,
				String lastLoginIP) {
			this(version, mojangUUID, mojangUsername, texturesProperty, hashBase, hashSalt,
					registered == null ? 0l : registered.getTime(), registeredIP,
					lastLogin == null ? 0l : lastLogin.getTime(), lastLoginIP);
		}

		protected CachedAccountInfo(int version, UUID mojangUUID, String mojangUsername, byte[] texturesProperty,
				byte[] hashBase, byte[] hashSalt, long registered, String registeredIP, long lastLogin,
				String lastLoginIP) {
			this.version = version;
			this.mojangUUID = mojangUUID;
			this.mojangUsername = mojangUsername;
			this.texturesProperty = texturesProperty;
			this.hashBase = hashBase;
			this.hashSalt = hashSalt;
			this.registered = registered;
			this.registeredIP = registeredIP;
			this.lastLogin = lastLogin;
			this.lastLoginIP = lastLoginIP;
		}

	}

	protected final AuthLoadingCache<String, CachedAccountInfo> authLoadingCache;

	protected DefaultAuthSystem(String uri, Connection databaseConnection, String passwordPromptScreenText,
			String wrongPasswordScreenText, String notRegisteredScreenText, String eaglerCommandName,
			String useRegisterCommandText, String useChangeCommandText, String commandSuccessText,
			String lastEaglerLoginMessage, String tooManyRegistrationsMessage, String needVanillaToRegisterMessage,
			boolean overrideEaglerToVanillaSkins, int maxRegistrationsPerIP) throws SQLException {
		this.uri = uri;
		this.databaseConnection = databaseConnection;
		this.passwordPromptScreenText = passwordPromptScreenText;
		this.wrongPasswordScreenText = wrongPasswordScreenText;
		this.notRegisteredScreenText = notRegisteredScreenText;
		this.eaglerCommandName = eaglerCommandName;
		this.useRegisterCommandText = useRegisterCommandText;
		this.useChangeCommandText = useChangeCommandText;
		this.commandSuccessText = commandSuccessText;
		this.lastEaglerLoginMessage = lastEaglerLoginMessage;
		this.tooManyRegistrationsMessage = tooManyRegistrationsMessage;
		this.needVanillaToRegisterMessage = needVanillaToRegisterMessage;
		this.overrideEaglerToVanillaSkins = overrideEaglerToVanillaSkins;
		this.maxRegistrationsPerIP = maxRegistrationsPerIP;
		this.registerUser = databaseConnection.prepareStatement("INSERT INTO eaglercraft_accounts (Version, MojangUUID, MojangUsername, MojangTextures, HashBase, HashSalt, Registered, RegisteredIP) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		this.isRegisteredUser = databaseConnection.prepareStatement("SELECT COUNT(MojangUUID) AS total_accounts FROM eaglercraft_accounts WHERE MojangUUID = ?");
		this.pruneUsers = databaseConnection.prepareStatement("DELETE FROM eaglercraft_accounts WHERE LastLogin < ?");
		this.updatePassword = databaseConnection.prepareStatement("UPDATE eaglercraft_accounts SET HashBase = ?, HashSalt = ? WHERE MojangUUID = ?");
		this.updateMojangUsername = databaseConnection.prepareStatement("UPDATE eaglercraft_accounts SET MojangUsername = ? WHERE MojangUUID = ?");
		this.getRegistrationsOnIP = databaseConnection.prepareStatement("SELECT COUNT(MojangUUID) AS total_accounts FROM eaglercraft_accounts WHERE RegisteredIP = ?");
		this.checkRegistrationByUUID = databaseConnection.prepareStatement("SELECT Version, MojangUsername, LastLogin, LastLoginIP FROM eaglercraft_accounts WHERE MojangUUID = ?");
		this.checkRegistrationByName = databaseConnection.prepareStatement("SELECT Version, MojangUUID, MojangTextures, HashBase, HashSalt, Registered, RegisteredIP, LastLogin, LastLoginIP FROM eaglercraft_accounts WHERE MojangUsername = ?");
		this.setLastLogin = databaseConnection.prepareStatement("UPDATE eaglercraft_accounts SET LastLogin = ?, LastLoginIP = ? WHERE MojangUUID = ?");
		this.updateTextures = databaseConnection.prepareStatement("UPDATE eaglercraft_accounts SET MojangTextures = ? WHERE MojangUUID = ?");
		this.authLoadingCache = new AuthLoadingCache(new AccountLoader(), 120000l);
		this.secureRandom = new SecureRandom();
	}

	public void handleIsAuthRequiredEvent(EaglercraftIsAuthRequiredEvent event) {
		String username = new String(event.getAuthUsername(), StandardCharsets.US_ASCII);
		
		String usrs = username.toString();
		if(!usrs.equals(usrs.replaceAll("[^A-Za-z0-9_]", "_").trim())) {
			event.kickUser("Invalid characters in username");
			return;
		}
		
		if(username.length() < 3) {
			event.kickUser("Username must be at least 3 characters");
			return;
		}
		
		if(username.length() > 16) {
			event.kickUser("Username must be under 16 characters");
			return;
		}
		
		CachedAccountInfo info = authLoadingCache.get(username);
		if(info == null) {
			event.kickUser(notRegisteredScreenText);
			return;
		}
		
		event.setAuthAttachment(info);
		event.setAuthRequired(AuthResponse.REQUIRE);
		event.setAuthMessage(passwordPromptScreenText);
		event.setUseAuthMethod(AuthMethod.EAGLER_SHA256);
		
		byte[] randomBytes = new byte[32];
		Random rng;
		synchronized(secureRandom) {
			rng = new Random(secureRandom.nextLong());
		}
		
		rng.nextBytes(randomBytes);
		
		byte[] saltingData = new byte[64];
		System.arraycopy(info.hashSalt, 0, saltingData, 0, 32);
		System.arraycopy(randomBytes, 0, saltingData, 32, 32);
		
		event.setSaltingData(saltingData);
	}

	public void handleAuthPasswordEvent(EaglercraftHandleAuthPasswordEvent event) {
		CachedAccountInfo info = event.getAuthAttachment();
		
		if(info == null) {
			event.setLoginDenied(notRegisteredScreenText);
			return;
		}
		
		
		byte[] responseHash = event.getAuthPasswordDataResponse();
		
		if(responseHash.length != 32) {
			event.setLoginDenied("Wrong number of bits in checksum!");
			return;
		}
		
		byte[] saltingData = event.getAuthSaltingData();
		
		SHA256Digest digest = new SHA256Digest(); 
		
		digest.update(info.hashBase, 0, 32);
		digest.update(saltingData, 32, 32);
		digest.update(HashUtils.EAGLER_SHA256_SALT_BASE, 0, 32);
		
		byte[] hashed = new byte[32];
		digest.doFinal(hashed, 0);
		
		if(!Arrays.equals(hashed, responseHash)) {
			event.setLoginDenied(wrongPasswordScreenText);
			EaglerXBungee.logger().warning("User \"" + info.mojangUsername + "\" entered the wrong password while logging in from: " + event.getRemoteAddress().getHostAddress());
			return;
		}
		
		try {
			synchronized(setLastLogin) {
				setLastLogin.setDate(1, new Date(System.currentTimeMillis()));
				setLastLogin.setString(2, event.getRemoteAddress().getHostAddress());
				setLastLogin.setString(3, getMojangUUID(info.mojangUUID));
				if(setLastLogin.executeUpdate() == 0) {
					throw new SQLException("Query did not alter the database");
				}
			}
		}catch(SQLException ex) {
			EaglerXBungee.logger().log(Level.SEVERE, "Could not update last login for \"" + info.mojangUUID.toString() + "\"", ex);
		}
		
		event.setLoginAllowed();
		event.setProfileUsername(info.mojangUsername);
		event.setProfileUUID(info.mojangUUID);
		
		byte[] texturesProp = info.texturesProperty;
		if(texturesProp != null) {
			try {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(texturesProp));
				int valueLen = dis.readInt();
				int sigLen = dis.readInt();
				byte[] valueBytes = new byte[valueLen];
				dis.read(valueBytes);
				String valueB64 = Base64.encodeBase64String(valueBytes);
				String sigB64 = null;
				if(sigLen > 0) {
					valueBytes = new byte[sigLen];
					dis.read(valueBytes);
					sigB64 = Base64.encodeBase64String(valueBytes);
				}
				event.applyTexturesProperty(valueB64, sigB64);
				event.setOverrideEaglerToVanillaSkins(overrideEaglerToVanillaSkins);
			}catch(IOException ex) {
			}
		}
	}

	public void processSetPassword(ProxiedPlayer player, String password) throws TooManyRegisteredOnIPException, AuthException {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			throw new AuthException("Cannot register from an eaglercraft account!");
		}else if(!conn.isOnlineMode()) {
			throw new AuthException("Cannot register without online mode enabled!");
		}else {
			try {
				String uuid = getMojangUUID(player.getUniqueId());
				synchronized(registerUser) {
					int cnt;
					synchronized(isRegisteredUser) {
						isRegisteredUser.setString(1, uuid);
						try(ResultSet set = isRegisteredUser.executeQuery()) {
							if(set.next()) {
								cnt = set.getInt(1);
							}else {
								throw new SQLException("Empty ResultSet recieved while checking if user exists");
							}
						}
					}
					
					SHA256Digest digest = new SHA256Digest(); 
					
					int passLen = password.length();
					
					digest.update((byte)((passLen >> 8) & 0xFF));
					digest.update((byte)(passLen & 0xFF));
					for(int i = 0; i < passLen; ++i) {
						char codePoint = password.charAt(i);
						digest.update((byte)((codePoint >> 8) & 0xFF));
						digest.update((byte)(codePoint & 0xFF));
					}
					
					digest.update(HashUtils.EAGLER_SHA256_SALT_SAVE, 0, 32);
					
					byte[] hashed = new byte[32];
					digest.doFinal(hashed, 0);
					
					byte[] randomBytes = new byte[32];
					synchronized(secureRandom) {
						secureRandom.nextBytes(randomBytes);
					}
					
					digest.reset();
					
					digest.update(hashed, 0, 32);
					digest.update(randomBytes, 0, 32);
					digest.update(HashUtils.EAGLER_SHA256_SALT_BASE, 0, 32);

					digest.doFinal(hashed, 0);
					
					String username = player.getName();
					authLoadingCache.evict(username);
					
					if(cnt > 0) {
						synchronized(updatePassword) {
							updatePassword.setBytes(1, hashed);
							updatePassword.setBytes(2, randomBytes);
							updatePassword.setString(3, uuid);
							if(updatePassword.executeUpdate() <= 0) {
								throw new AuthException("Update password query did not alter the database!");
							}
						}
					}else {
						String sockAddr = sockAddrToString(player.getSocketAddress());
						if(maxRegistrationsPerIP > 0) {
							if(countUsersOnIP(sockAddr) >= maxRegistrationsPerIP) {
								throw new TooManyRegisteredOnIPException(sockAddr);
							}
						}
						Date nowDate = new Date(System.currentTimeMillis());
						registerUser.setInt(1, 1);
						registerUser.setString(2, uuid);
						registerUser.setString(3, username);
						LoginResult res = ((InitialHandler)player.getPendingConnection()).getLoginProfile();
						if(res != null) {
							registerUser.setBytes(4, getTexturesProperty(res));
						}else {
							registerUser.setBytes(4, null);
						}
						registerUser.setBytes(5, hashed);
						registerUser.setBytes(6, randomBytes);
						registerUser.setDate(7, nowDate);
						registerUser.setString(8, sockAddr);
						if(registerUser.executeUpdate() <= 0) {
							throw new AuthException("Registration query did not alter the database!");
						}
					}
				}
			}catch(SQLException ex) {
				throw new AuthException("Failed to query database!", ex);
			}
		}
	}

	private static byte[] getTexturesProperty(LoginResult profile) {
		try {
			Property[] props = profile.getProperties();
			for(int i = 0; i < props.length; ++i) {
				Property prop = props[i];
				if("textures".equals(prop.getName())) {
					byte[] texturesData = Base64.decodeBase64(prop.getValue());
					byte[] signatureData = prop.getSignature() == null ? new byte[0] : Base64.decodeBase64(prop.getSignature());
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					DataOutputStream dao = new DataOutputStream(bao);
					dao.writeInt(texturesData.length);
					dao.writeInt(signatureData.length);
					dao.write(texturesData);
					dao.write(signatureData);
					return bao.toByteArray();
				}
			}
		}catch(Throwable t) {
		}
		return null;
	}

	public int pruneUsers(long before) throws AuthException {
		try {
			authLoadingCache.flush();
			synchronized(pruneUsers) {
				pruneUsers.setDate(1, new Date(before));
				return pruneUsers.executeUpdate();
			}
		}catch(SQLException ex) {
			throw new AuthException("Failed to query database!", ex);
		}
	}

	public int countUsersOnIP(String addr) throws AuthException {
		synchronized(getRegistrationsOnIP) {
			try {
				getRegistrationsOnIP.setString(1, addr);
				try(ResultSet set = getRegistrationsOnIP.executeQuery()) {
					if(set.next()) {
						return set.getInt(1);
					}else {
						throw new SQLException("Empty ResultSet recieved while counting accounts");
					}
				}
			}catch(SQLException ex) {
				throw new AuthException("Failed to query database!", ex);
			}
		}
	}

	public void handleVanillaLogin(PostLoginEvent loginEvent) {
		ProxiedPlayer player = loginEvent.getPlayer();
		PendingConnection con = player.getPendingConnection();
		if(!(con instanceof EaglerInitialHandler)) {
			Date lastLogin = null;
			String lastLoginIP = null;
			boolean isRegistered = false;
			synchronized(checkRegistrationByUUID) {
				UUID uuid = player.getUniqueId();
				try {
					String uuidString = getMojangUUID(uuid);
					checkRegistrationByUUID.setString(1, getMojangUUID(player.getUniqueId()));
					try(ResultSet res = checkRegistrationByUUID.executeQuery()) {
						if(res.next()) {
							isRegistered = true;
							int vers = res.getInt(1);
							String username = res.getString(2);
							lastLogin = res.getDate(3);
							lastLoginIP = res.getString(4);
							String playerName = player.getName();
							if(!playerName.equals(username)) {
								EaglerXBungee.logger().info("Player \"" + uuid.toString() + "\" changed their username from \"" + username
												+ " to \"" + playerName + "\", updating authentication database...");
								synchronized(updateMojangUsername) {
									updateMojangUsername.setString(1, playerName);
									updateMojangUsername.setString(2, uuidString);
									if(updateMojangUsername.executeUpdate() == 0) {
										throw new SQLException("Failed to update username to \"" + playerName + "\"");
									}
								}
							}
						}
					}
					byte[] texProperty = getTexturesProperty(((InitialHandler)con).getLoginProfile());
					if(texProperty != null) {
						synchronized(updateTextures) {
							updateTextures.setBytes(1, texProperty);
							updateTextures.setString(2, uuidString);
							updateTextures.executeUpdate();
						}
					}
				}catch(SQLException ex) {
					EaglerXBungee.logger().log(Level.SEVERE, "Could not look up UUID \"" + uuid.toString() + "\" in auth database!", ex);
				}
			}
			if(isRegistered) {
				if(lastLogin != null) {
					String dateStr;
					java.util.Date juLastLogin = new java.util.Date(lastLogin.getTime());
					Calendar calendar = Calendar.getInstance();
					int yearToday = calendar.get(Calendar.YEAR);
					calendar.setTime(juLastLogin);
					if(calendar.get(Calendar.YEAR) != yearToday) {
						dateStr = (new SimpleDateFormat("EE, MMM d, yyyy, HH:mm z")).format(juLastLogin);
					}else {
						dateStr = (new SimpleDateFormat("EE, MMM d, HH:mm z")).format(juLastLogin);
					}
					TextComponent comp = new TextComponent(lastEaglerLoginMessage.replace("$date", dateStr).replace("$ip", "" + lastLoginIP));
					comp.setColor(ChatColor.GREEN);
					player.sendMessage(comp);
				}
				player.sendMessage(new TextComponent(useChangeCommandText));
			}else {
				player.sendMessage(new TextComponent(useRegisterCommandText));
			}
		}
	}

	private void destroyStatement(Statement stmt) {
		try {
			stmt.close();
		} catch (SQLException e) {
		}
	}

	public void flush() {
		authLoadingCache.flush();
	}

	public void destroy() {
		destroyStatement(registerUser);
		destroyStatement(isRegisteredUser);
		destroyStatement(pruneUsers);
		destroyStatement(updatePassword);
		destroyStatement(updateMojangUsername);
		destroyStatement(getRegistrationsOnIP);
		destroyStatement(checkRegistrationByUUID);
		destroyStatement(checkRegistrationByName);
		destroyStatement(setLastLogin);
		destroyStatement(updateTextures);
		try {
			databaseConnection.close();
			EaglerXBungee.logger().info("Successfully disconnected from database '" + uri + "'");
		} catch (SQLException e) {
			EaglerXBungee.logger().log(Level.WARNING, "Exception disconnecting from database '" + uri + "'!", e);
		}
	}

	public static class AuthException extends RuntimeException {
		
		public AuthException(String msg) {
			super(msg);
		}
		
		public AuthException(Throwable t) {
			super(t);
		}
		
		public AuthException(String msg, Throwable t) {
			super(msg, t);
		}
		
	}

	public static class TooManyRegisteredOnIPException extends AuthException {
		
		public TooManyRegisteredOnIPException(String ip) {
			super(ip);
		}
		
	}

	private static final String hexString = "0123456789abcdef";

	private static final char[] HEX = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};

	public static String getMojangUUID(UUID uuid) {
		char[] ret = new char[32];
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		for(int i = 0, j; i < 16; ++i) {
			j = (15 - i) << 2;
			ret[i] = HEX[(int)((msb >> j) & 15l)];
			ret[i + 16] = HEX[(int)((lsb >> j) & 15l)];
		}
		return new String(ret);
	}

	public static UUID parseMojangUUID(String uuid) {
		long msb = 0l;
		long lsb = 0l;
		for(int i = 0, j; i < 16; ++i) {
			j = (15 - i) << 2;
			msb |= ((long)hexString.indexOf(uuid.charAt(i)) << j);
			lsb |= ((long)hexString.indexOf(uuid.charAt(i + 16)) << j);
		}
		return new UUID(msb, lsb);
	}

	private static String sockAddrToString(SocketAddress addr) {
		if(addr instanceof InetSocketAddress) {
			return ((InetSocketAddress)addr).getAddress().getHostAddress();
		}else {
			return "127.0.0.1";
		}
	}

}

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.sqlite.EaglerDrivers;

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
public class JDBCCacheProvider implements ICacheProvider {

	public static JDBCCacheProvider initialize(String uri, String driverClass, String driverPath, int keepObjectsDays,
			int keepProfilesDays, int maxObjects, int maxProfiles) throws CacheException {
		Connection conn;
		try {
			conn = EaglerDrivers.connectToDatabase(uri, driverClass, driverPath, new Properties());
			if(conn == null) {
				throw new IllegalStateException("Connection is null");
			}
		}catch(Throwable t) {
			throw new CacheException("Could not initialize '" + uri + "'!", t);
		}
		EaglerXBungee.logger().info("Connected to database: " + uri);
		try {
			try(Statement stmt = conn.createStatement()) {
				stmt.execute("CREATE TABLE IF NOT EXISTS "
						+ "\"eaglercraft_skins_objects\" ("
						+ "\"TextureUUID\" TEXT(32) NOT NULL,"
						+ "\"TextureURL\" VARCHAR(256) NOT NULL,"
						+ "\"TextureTime\" DATETIME NOT NULL,"
						+ "\"TextureData\" BLOB,"
						+ "\"TextureLength\" INT(24) NOT NULL,"
						+ "PRIMARY KEY(\"TextureUUID\"))");
				stmt.execute("CREATE TABLE IF NOT EXISTS "
						+ "\"eaglercraft_skins_profiles\" ("
						+ "\"ProfileUUID\" TEXT(32) NOT NULL,"
						+ "\"ProfileName\" TEXT(16) NOT NULL,"
						+ "\"ProfileTime\" DATETIME NOT NULL,"
						+ "\"ProfileTexture\" VARCHAR(256),"
						+ "\"ProfileModel\" VARCHAR(16) NOT NULL,"
						+ "PRIMARY KEY(\"ProfileUUID\"))");
				stmt.execute("CREATE INDEX IF NOT EXISTS \"profile_name_index\" "
						+ "ON \"eaglercraft_skins_profiles\" (\"ProfileName\")");
			}
			JDBCCacheProvider cacheProvider = new JDBCCacheProvider(conn, uri, keepObjectsDays, keepProfilesDays, maxObjects, maxProfiles);
			cacheProvider.flush();
			return cacheProvider;
		}catch(CacheException ex) {
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
			throw new CacheException("Could not initialize '" + uri + "'!", t);
		}
	}

	protected final Connection connection;
	protected final String uri;

	protected final PreparedStatement discardExpiredObjects;
	protected final PreparedStatement discardExpiredProfiles;
	protected final PreparedStatement getTotalObjects;
	protected final PreparedStatement getTotalProfiles;
	protected final PreparedStatement deleteSomeOldestObjects;
	protected final PreparedStatement deleteSomeOldestProfiles;
	protected final PreparedStatement querySkinByUUID;
	protected final PreparedStatement queryProfileByUUID;
	protected final PreparedStatement queryProfileByUsername;
	protected final PreparedStatement cacheNewSkin;
	protected final PreparedStatement cacheNewProfile;
	protected final PreparedStatement cacheHasSkin;
	protected final PreparedStatement cacheHasProfile;
	protected final PreparedStatement cacheUpdateSkin;
	protected final PreparedStatement cacheUpdateProfile;
	
	protected long lastFlush;

	protected int keepObjectsDays;
	protected int keepProfilesDays;
	protected int maxObjects;
	protected int maxProfiles;

	protected JDBCCacheProvider(Connection conn, String uri, int keepObjectsDays, int keepProfilesDays, int maxObjects,
			int maxProfiles) throws SQLException {
		this.connection = conn;
		this.uri = uri;
		this.lastFlush = 0l;
		this.keepObjectsDays = keepObjectsDays;
		this.keepProfilesDays = keepProfilesDays;
		this.maxObjects = maxObjects;
		this.maxProfiles = maxProfiles;
		
		this.discardExpiredObjects = connection.prepareStatement("DELETE FROM eaglercraft_skins_objects WHERE textureTime < ?");
		this.discardExpiredProfiles = connection.prepareStatement("DELETE FROM eaglercraft_skins_profiles WHERE profileTime < ?");
		this.getTotalObjects = connection.prepareStatement("SELECT COUNT(*) AS total_objects FROM eaglercraft_skins_objects");
		this.getTotalProfiles = connection.prepareStatement("SELECT COUNT(*) AS total_profiles FROM eaglercraft_skins_profiles");
		this.deleteSomeOldestObjects = connection.prepareStatement("DELETE FROM eaglercraft_skins_objects WHERE TextureUUID IN (SELECT TextureUUID FROM eaglercraft_skins_objects ORDER BY TextureTime ASC LIMIT ?)");
		this.deleteSomeOldestProfiles = connection.prepareStatement("DELETE FROM eaglercraft_skins_profiles WHERE ProfileUUID IN (SELECT ProfileUUID FROM eaglercraft_skins_profiles ORDER BY ProfileTime ASC LIMIT ?)");
		this.querySkinByUUID = connection.prepareStatement("SELECT TextureURL,TextureData,TextureLength FROM eaglercraft_skins_objects WHERE TextureUUID = ? LIMIT 1");
		this.queryProfileByUUID = connection.prepareStatement("SELECT ProfileName,ProfileTexture,ProfileModel FROM eaglercraft_skins_profiles WHERE ProfileUUID = ? LIMIT 1");
		this.queryProfileByUsername = connection.prepareStatement("SELECT ProfileUUID,ProfileTexture,ProfileModel FROM eaglercraft_skins_profiles WHERE ProfileName = ? LIMIT 1");
		this.cacheNewSkin = connection.prepareStatement("INSERT INTO eaglercraft_skins_objects (TextureUUID, TextureURL, TextureTime, TextureData, TextureLength) VALUES(?, ?, ?, ?, ?)");
		this.cacheNewProfile = connection.prepareStatement("INSERT INTO eaglercraft_skins_profiles (ProfileUUID, ProfileName, ProfileTime, ProfileTexture, ProfileModel) VALUES(?, ?, ?, ?, ?)");
		this.cacheHasSkin = connection.prepareStatement("SELECT COUNT(TextureUUID) AS has_object FROM eaglercraft_skins_objects WHERE TextureUUID = ? LIMIT 1");
		this.cacheHasProfile = connection.prepareStatement("SELECT COUNT(ProfileUUID) AS has_profile FROM eaglercraft_skins_profiles WHERE ProfileUUID = ? LIMIT 1");
		this.cacheUpdateSkin = connection.prepareStatement("UPDATE eaglercraft_skins_objects SET TextureURL = ?, TextureTime = ?, TextureData = ?, TextureLength = ? WHERE TextureUUID = ?");
		this.cacheUpdateProfile = connection.prepareStatement("UPDATE eaglercraft_skins_profiles SET ProfileName = ?, ProfileTime = ?, ProfileTexture = ?, ProfileModel = ? WHERE ProfileUUID = ?");
	}

	public CacheLoadedSkin loadSkinByUUID(UUID uuid) throws CacheException {
		String uuidString = SkinService.getMojangUUID(uuid);
		String queriedUrls;
		byte[] queriedTexture;
		int queriedLength;
		try {
			synchronized(querySkinByUUID) {
				querySkinByUUID.setString(1, uuidString);
				try(ResultSet resultSet = querySkinByUUID.executeQuery()) {
					if(resultSet.next()) {
						queriedUrls = resultSet.getString(1);
						queriedTexture = resultSet.getBytes(2);
						queriedLength = resultSet.getInt(3);
					}else {
						return null;
					}
				}
			}
		}catch(SQLException ex) {
			throw new CacheException("SQL query failure while loading cached skin", ex);
		}
		if(queriedLength == 0) {
			return new CacheLoadedSkin(uuid, queriedUrls, new byte[0]);
		}else {
			byte[] decompressed = new byte[queriedLength];
			try {
				GZIPInputStream is = new GZIPInputStream(new ByteArrayInputStream(queriedTexture));
				int i = 0, j = 0;
				while(j < queriedLength && (i = is.read(decompressed, j, queriedLength - j)) != -1) {
					j += i;
				}
			}catch(IOException ex) {
				throw new CacheException("SQL query failure while loading cached skin");
			}
			return new CacheLoadedSkin(uuid, queriedUrls, decompressed);
		}
	}

	public void cacheSkinByUUID(UUID uuid, String url, byte[] textureBlob) throws CacheException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			GZIPOutputStream deflateOut = new GZIPOutputStream(bao);
			deflateOut.write(textureBlob);
			deflateOut.close();
		}catch(IOException ex) {
			throw new CacheException("Skin compression error", ex);
		}
		int len;
		byte[] textureBlobCompressed;
		if(textureBlob == null || textureBlob.length == 0) {
			len = 0;
			textureBlobCompressed = null;
		}else {
			len = textureBlob.length;
			textureBlobCompressed = bao.toByteArray();
		}
		try {
			String uuidString = SkinService.getMojangUUID(uuid);
			synchronized(cacheNewSkin) {
				boolean has;
				cacheHasSkin.setString(1, uuidString);
				try(ResultSet resultSet = cacheHasSkin.executeQuery()) {
					if(resultSet.next()) {
						has = resultSet.getInt(1) > 0;
					}else {
						has = false; // ??
					}
				}
				if(has) {
					cacheUpdateSkin.setString(1, url);
					cacheUpdateSkin.setDate(2, new Date(System.currentTimeMillis()));
					cacheUpdateSkin.setBytes(3, textureBlobCompressed);
					cacheUpdateSkin.setInt(4, len);
					cacheUpdateSkin.setString(5, uuidString);
					cacheUpdateSkin.executeUpdate();
				}else {
					cacheNewSkin.setString(1, uuidString);
					cacheNewSkin.setString(2, url);
					cacheNewSkin.setDate(3, new Date(System.currentTimeMillis()));
					cacheNewSkin.setBytes(4, textureBlobCompressed);
					cacheNewSkin.setInt(5, len);
					cacheNewSkin.executeUpdate();
				}
			}
		}catch(SQLException ex) {
			throw new CacheException("SQL query failure while caching new skin", ex);
		}
	}

	public CacheLoadedProfile loadProfileByUUID(UUID uuid) throws CacheException {
		try {
			String uuidString = SkinService.getMojangUUID(uuid);
			synchronized(queryProfileByUUID) {
				queryProfileByUUID.setString(1, uuidString);
				try(ResultSet resultSet = queryProfileByUUID.executeQuery()) {
					if(resultSet.next()) {
						String profileName = resultSet.getString(1);
						String profileTexture = resultSet.getString(2);
						String profileModel = resultSet.getString(3);
						return new CacheLoadedProfile(uuid, profileName, profileTexture, profileModel);
					}else {
						return null;
					}
				}
			}
		}catch(SQLException ex) {
			throw new CacheException("SQL query failure while loading profile by uuid", ex);
		}
	}

	public CacheLoadedProfile loadProfileByUsername(String username) throws CacheException {
		try {
			synchronized(queryProfileByUsername) {
				queryProfileByUsername.setString(1, username);
				try(ResultSet resultSet = queryProfileByUsername.executeQuery()) {
					if(resultSet.next()) {
						UUID profileUUID = SkinService.parseMojangUUID(resultSet.getString(1));
						String profileTexture = resultSet.getString(2);
						String profileModel = resultSet.getString(3);
						return new CacheLoadedProfile(profileUUID, username, profileTexture, profileModel);
					}else {
						return null;
					}
				}
			}
		}catch(SQLException ex) {
			throw new CacheException("SQL query failure while loading profile by username", ex);
		}
	}

	public void cacheProfileByUUID(UUID uuid, String username, String texture, String model) throws CacheException {
		try {
			String uuidString = SkinService.getMojangUUID(uuid);
			synchronized(cacheNewProfile) {
				boolean has;
				cacheHasProfile.setString(1, uuidString);
				try(ResultSet resultSet = cacheHasProfile.executeQuery()) {
					if(resultSet.next()) {
						has = resultSet.getInt(1) > 0;
					}else {
						has = false; // ??
					}
				}
				if(has) {
					cacheUpdateProfile.setString(1, username);
					cacheUpdateProfile.setDate(2, new Date(System.currentTimeMillis()));
					cacheUpdateProfile.setString(3, texture);
					cacheUpdateProfile.setString(4, model);
					cacheUpdateProfile.setString(5, uuidString);
					cacheUpdateProfile.executeUpdate();
				}else {
					cacheNewProfile.setString(1, uuidString);
					cacheNewProfile.setString(2, username);
					cacheNewProfile.setDate(3, new Date(System.currentTimeMillis()));
					cacheNewProfile.setString(4, texture);
					cacheNewProfile.setString(5, model);
					cacheNewProfile.executeUpdate();
				}
			}
		}catch(SQLException ex) {
			throw new CacheException("SQL query failure while caching new profile", ex);
		}
	}

	@Override
	public void flush() {
		long millis = System.currentTimeMillis();
		if(millis - lastFlush > 1200000l) { // 30 minutes
			lastFlush = millis;
			try {
				Date expiryObjects = new Date(millis - keepObjectsDays * 86400000l);
				Date expiryProfiles = new Date(millis - keepProfilesDays * 86400000l);
				
				synchronized(discardExpiredObjects) {
					discardExpiredObjects.setDate(1, expiryObjects);
					discardExpiredObjects.execute();
				}
				synchronized(discardExpiredProfiles) {
					discardExpiredProfiles.setDate(1, expiryProfiles);
					discardExpiredProfiles.execute();
				}
				
				int totalObjects, totalProfiles;
				
				synchronized(getTotalObjects) {
					try(ResultSet resultSet = getTotalObjects.executeQuery()) {
						if(resultSet.next()) {
							totalObjects = resultSet.getInt(1);
						}else {
							throw new SQLException("Empty ResultSet recieved when checking \"eaglercraft_skins_objects\" row count");
						}
					}
				}
				
				synchronized(getTotalProfiles) {
					try(ResultSet resultSet = getTotalProfiles.executeQuery()) {
						if(resultSet.next()) {
							totalProfiles = resultSet.getInt(1);
						}else {
							throw new SQLException("Empty ResultSet recieved when checking \"eaglercraft_skins_profiles\" row count");
						}
					}
				}
				
				if(totalObjects > maxObjects) {
					int deleteCount = totalObjects - maxObjects + (maxObjects >> 3);
					EaglerXBungee.logger().warning("Skin object cache has passed " + maxObjects + " skins in size ("
							+ totalObjects + "), deleting " + deleteCount + " skins from the cache to free space");
					synchronized(deleteSomeOldestObjects) {
						deleteSomeOldestObjects.setInt(1, deleteCount);
						deleteSomeOldestObjects.executeUpdate();
					}
				}
				
				if(totalProfiles > maxProfiles) {
					int deleteCount = totalProfiles - maxProfiles + (maxProfiles >> 3);
					EaglerXBungee.logger().warning("Skin profile cache has passed " + maxProfiles + " profiles in size ("
							+ totalProfiles + "), deleting " + deleteCount + " profiles from the cache to free space");
					synchronized(deleteSomeOldestProfiles) {
						deleteSomeOldestProfiles.setInt(1, deleteCount);
						deleteSomeOldestProfiles.executeUpdate();
					}
				}
				
			}catch(SQLException ex) {
				throw new CacheException("SQL query failure while flushing cache!", ex);
			}
		}
	}

	private void destroyStatement(Statement stmt) {
		try {
			stmt.close();
		} catch (SQLException e) {
		}
	}

	@Override
	public void destroy() {
		destroyStatement(discardExpiredObjects);
		destroyStatement(discardExpiredProfiles);
		destroyStatement(getTotalObjects);
		destroyStatement(getTotalProfiles);
		destroyStatement(deleteSomeOldestObjects);
		destroyStatement(deleteSomeOldestProfiles);
		destroyStatement(querySkinByUUID);
		destroyStatement(queryProfileByUUID);
		destroyStatement(queryProfileByUsername);
		destroyStatement(cacheNewSkin);
		destroyStatement(cacheNewProfile);
		destroyStatement(cacheHasSkin);
		destroyStatement(cacheHasProfile);
		destroyStatement(cacheUpdateSkin);
		destroyStatement(cacheUpdateProfile);
		try {
			connection.close();
			EaglerXBungee.logger().info("Successfully disconnected from database '" + uri + "'");
		} catch (SQLException e) {
			EaglerXBungee.logger().log(Level.WARNING, "Exception disconnecting from database '" + uri + "'!", e);
		}
	}

}

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.io.IOException;
import java.util.UUID;

import net.md_5.bungee.UserConnection;

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
public interface ISkinService {

	void init(String uri, String driverClass, String driverPath, int keepObjectsDays, int keepProfilesDays,
			int maxObjects, int maxProfiles);

	void processGetOtherSkin(final UUID searchUUID, final UserConnection sender);

	void processGetOtherSkin(UUID searchUUID, String skinURL, UserConnection sender);

	void registerEaglercraftPlayer(UUID clientUUID, byte[] generatedPacket, int modelId) throws IOException;

	void unregisterPlayer(UUID clientUUID);

	default void registerTextureToPlayerAssociation(String textureURL, UUID playerUUID) {
		registerTextureToPlayerAssociation(SkinPackets.createEaglerURLSkinUUID(textureURL), playerUUID);
	}

	void registerTextureToPlayerAssociation(UUID textureUUID, UUID playerUUID);

	void flush();

	void shutdown();

}

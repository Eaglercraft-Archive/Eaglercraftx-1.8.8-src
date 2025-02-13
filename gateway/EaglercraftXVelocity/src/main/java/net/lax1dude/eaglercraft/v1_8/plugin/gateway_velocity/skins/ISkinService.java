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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins;

import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

public interface ISkinService {

	void init(String uri, String driverClass, String driverPath, int keepObjectsDays, int keepProfilesDays,
			int maxObjects, int maxProfiles);

	void processGetOtherSkin(final UUID searchUUID, final EaglerPlayerData sender);

	void processGetOtherSkin(UUID searchUUID, String skinURL, EaglerPlayerData sender);

	void registerEaglercraftPlayer(UUID clientUUID, SkinPacketVersionCache generatedPacket, int modelId);

	void unregisterPlayer(UUID clientUUID);

	default void registerTextureToPlayerAssociation(String textureURL, UUID playerUUID) {
		registerTextureToPlayerAssociation(SkinPackets.createEaglerURLSkinUUID(textureURL), playerUUID);
	}

	void registerTextureToPlayerAssociation(UUID textureUUID, UUID playerUUID);

	void flush();

	void shutdown();

	void processForceSkin(UUID playerUUID, EaglerPlayerData initialHandler);

	SkinPacketVersionCache getSkin(UUID playerUUID);

}
/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.ServerInfoTemplateParser;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.CapeServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.ISkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinRescaler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketEnableFNAWSkinsEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketInvalidatePlayerCacheV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeShowV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapeCustomEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapePresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV3EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinPresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketServerInfoDataChunkV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketUnforceClientV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

public class EaglerXVelocityAPIHelper {

	public static EaglerXVelocity getEaglerXVelocity() {
		return EaglerXVelocity.getEagler();
	}

	public static EaglerPlayerData getEaglerHandle(Player player) {
		return player != null ? EaglerPipeline.getEaglerHandle(player) : null;
	}

	public static boolean isEaglerPlayer(Player player) {
		return player != null && (EaglerPipeline.getEaglerHandle(player) != null);
	}

	public static ConnectedPlayer getPlayer(EaglerPlayerData conn) {
		return conn != null ? conn.getPlayerObj() : null;
	}

	public static final UUID BRAND_NULL_UUID = new UUID(0l, 0l);
	public static final UUID BRAND_PENDING_UUID = new UUID(0x6969696969696969l, 0x6969696969696969l);
	public static final UUID BRAND_VANILLA_UUID = new UUID(0x1DCE015CD384374El, 0x85030A4DE95E5736l);

	public static final UUID BRAND_EAGLERCRAFTX_V4_UUID = makeClientBrandUUID("EaglercraftX");
	public static final UUID BRAND_EAGLERCRAFTX_LEGACY_UUID = makeClientBrandUUIDLegacy("EaglercraftX");

	public static UUID makeClientBrandUUID(String name) {
		return UUID.nameUUIDFromBytes(("EaglercraftXClient:" + name).getBytes(StandardCharsets.UTF_8));
	}

	public static UUID makeClientBrandUUIDLegacy(String name) {
		return UUID.nameUUIDFromBytes(("EaglercraftXClientOld:" + name).getBytes(StandardCharsets.UTF_8));
	}

	public static UUID getClientBrandUUID(Player player) {
		if(player == null) {
			return BRAND_NULL_UUID;
		}
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		if(conn != null) {
			UUID ret = conn.getClientBrandUUID();
			if(ret != null) {
				return ret;
			}else {
				return BRAND_NULL_UUID;
			}
		}else {
			return BRAND_VANILLA_UUID;
		}
	}

	public static UUID getClientBrandUUID(EaglerPlayerData conn) {
		if(conn == null) {
			return BRAND_NULL_UUID;
		}
		UUID ret = conn.getClientBrandUUID();
		if(ret != null) {
			return ret;
		}else {
			return BRAND_NULL_UUID;
		}
	}

	public static String getClientVersionString(Player player) {
		if(player == null) {
			return null;
		}
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		return conn != null ? conn.getEaglerVersionString() : null;
	}

	public static String getClientVersionString(EaglerPlayerData conn) {
		return conn != null ? conn.getEaglerVersionString() : null;
	}

	public static String getClientBrandString(Player player) {
		if(player == null) {
			return null;
		}
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		return conn != null ? conn.getEaglerBrandString() : null;
	}

	public static String getClientBrandString(EaglerPlayerData conn) {
		return conn != null ? conn.getEaglerBrandString() : null;
	}

	public static String getClientOrigin(Player player) {
		if(player == null) {
			return null;
		}
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		return conn != null ? conn.getOrigin() : null;
	}

	public static String getClientOrigin(EaglerPlayerData conn) {
		return conn != null ? conn.getOrigin() : null;
	}

	public static String getClientUserAgent(Player player) {
		if(player == null) {
			return null;
		}
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		return conn != null ? conn.getUserAgent() : null;
	}

	public static String getClientUserAgent(EaglerPlayerData conn) {
		return conn != null ? conn.getUserAgent() : null;
	}

	public static boolean getCookieAllowed(Player player) {
		if(player == null) {
			return false;
		}
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		return conn != null ? conn.getCookieAllowed() : null;
	}

	public static boolean getCookieAllowed(EaglerPlayerData conn) {
		return conn != null ? conn.getCookieAllowed() : null;
	}

	public static byte[] getCookieData(Player player) {
		if(player == null) {
			return null;
		}
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		return conn != null ? conn.getCookieData() : null;
	}

	public static byte[] getCookieData(EaglerPlayerData conn) {
		return conn != null ? conn.getCookieData() : null;
	}

	public static String getCookieDataString(Player player) {
		byte[] ret = getCookieData(player);
		if(ret == null) {
			return null;
		}
		return new String(ret, StandardCharsets.UTF_8);
	}

	public static String getCookieDataString(EaglerPlayerData conn) {
		byte[] ret = getCookieData(conn);
		if(ret == null) {
			return null;
		}
		return new String(ret, StandardCharsets.UTF_8);
	}

	public static void setCookieDataString(Player player, String data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(player, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieDataString(Player player, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(player, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieDataString(Player player, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(player, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieDataString(Player player, String data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			setCookieData(eaglerHandle, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
		}else {
			throw new UnsupportedOperationException("Can't set cookies on vanilla players!");
		}
	}

	public static void setCookieDataString(EaglerPlayerData conn, String data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieDataString(EaglerPlayerData conn, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieDataString(EaglerPlayerData conn, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieDataString(EaglerPlayerData conn, String data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieData(Player player, byte[] data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(player, data, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieData(Player player, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(player, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieData(Player player, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(player, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieData(Player player, byte[] data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			setCookieData(eaglerHandle, data, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
		}else {
			throw new UnsupportedOperationException("Can't set cookies on vanilla players!");
		}
	}

	public static void setCookieData(EaglerPlayerData conn, byte[] data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(conn, data, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieData(EaglerPlayerData conn, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(conn, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieData(EaglerPlayerData conn, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(conn, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieData(EaglerPlayerData conn, byte[] data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		conn.setCookieData(data, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void clearCookieData(Player player) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			setCookieData(eaglerHandle, (byte[])null, 0, false, false);
		}else {
			throw new UnsupportedOperationException("Can't set cookies on vanilla players!");
		}
	}

	public static void clearCookieData(EaglerPlayerData conn) {
		setCookieData(conn, (byte[])null, 0, false, false);
	}

	public static boolean getRedirectSupported(Player player) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null && eaglerHandle.redirectToWebSocketSupported();
	}

	public static boolean getRedirectSupported(EaglerPlayerData conn) {
		return conn != null && conn.redirectToWebSocketSupported();
	}

	public static void redirectPlayerToWebSocket(Player player, String serverAddress) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.redirectPlayerToWebSocket(serverAddress);
		}else {
			throw new UnsupportedOperationException("Can't redirect vanilla players to websocket addresses!");
		}
	}

	public static void redirectPlayerToWebSocket(EaglerPlayerData conn, String serverAddress) {
		conn.redirectPlayerToWebSocket(serverAddress);
	}

	public static byte[] loadCustomSkinFromImage(File phile) throws IOException {
		try(FileInputStream is = new FileInputStream(phile)) {
			return loadCustomSkinFromImage(is);
		}
	}

	public static byte[] loadCustomCapeFromImage(File phile) throws IOException {
		try(FileInputStream is = new FileInputStream(phile)) {
			return loadCustomCapeFromImage(is);
		}
	}

	public static byte[] loadCustomSkinFromImage(InputStream phile) throws IOException {
		return loadCustomSkinFromImage(ImageIO.read(phile));
	}

	public static byte[] loadCustomCapeFromImage(InputStream phile) throws IOException {
		return loadCustomCapeFromImage(ImageIO.read(phile));
	}

	public static byte[] loadCustomSkinFromImage(BufferedImage img) throws IOException {
		if(img.getWidth() != 64 || (img.getHeight() != 32 && img.getHeight() != 64)) {
			throw new IOException("Invalid image dimensions!");
		}
		byte[] ret = new byte[16384];
		if(img.getHeight() == 32) {
			int[] pixels = new int[2048];
			img.getRGB(0, 0, 64, 32, pixels, 0, 64);
			SkinRescaler.convert64x32To64x64(pixels, ret);
		}else {
			int[] pixels = new int[4096];
			img.getRGB(0, 0, 64, 64, pixels, 0, 64);
			SkinRescaler.convertToBytes(pixels, ret);
		}
		return ret;
	}

	public static byte[] loadCustomCapeFromImage(BufferedImage img) throws IOException {
		if(img.getHeight() != 32 || (img.getWidth() != 32 && img.getWidth() != 64)) {
			throw new IOException("Invalid image dimensions!");
		}
		int[] pixels = new int[1024];
		img.getRGB(0, 0, 32, 32, pixels, 0, 32);
		byte[] ret = new byte[4096];
		SkinRescaler.convertToBytes(pixels, ret);
		return ret;
	}

	public static void changePlayerSkinPreset(Player player, int presetId, boolean notifyOtherPlayers) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			UUID uuid = eaglerHandle.getUniqueId();
			ISkinService svc = EaglerXVelocity.getEagler().getSkinService();
			if(eaglerHandle.originalSkin == null) {
				eaglerHandle.originalSkin = svc.getSkin(uuid);
			}
			svc.unregisterPlayer(uuid);
			GameMessagePacket replacement = new SPacketOtherSkinPresetEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), presetId);
			svc.registerEaglercraftPlayer(uuid, new SkinPacketVersionCache(replacement, replacement), -1);
			if(eaglerHandle.getEaglerProtocol().ver >= 4) {
				svc.processForceSkin(uuid, eaglerHandle);
			}
			if(notifyOtherPlayers) {
				Optional<ServerConnection> svr = player.getCurrentServer();
				if(svr.isPresent()) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(true, false,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(Player otherPlayer : svr.get().getServer().getPlayersConnected()) {
						if(player != otherPlayer) {
							EaglerPlayerData otherConn = EaglerPipeline.getEaglerHandle(otherPlayer);
							if(otherConn != null) {
								if(otherConn.getEaglerProtocol().ver >= 4) {
									otherConn.sendEaglerMessage(pkt);
								}
							}
						}
					}
				}
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void changePlayerSkinCustom(Player player, int modelId, byte[] texture, boolean notifyOtherPlayers) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			UUID uuid = eaglerHandle.getUniqueId();
			ISkinService svc = EaglerXVelocity.getEagler().getSkinService();
			if(eaglerHandle.originalSkin == null) {
				eaglerHandle.originalSkin = svc.getSkin(uuid);
			}
			svc.unregisterPlayer(uuid);
			GameMessagePacket replacement = new SPacketOtherSkinCustomV3EAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), modelId, texture);
			svc.registerEaglercraftPlayer(uuid, new SkinPacketVersionCache(replacement, null), modelId);
			if(eaglerHandle.getEaglerProtocol().ver >= 4) {
				svc.processForceSkin(uuid, eaglerHandle);
			}
			if(notifyOtherPlayers) {
				Optional<ServerConnection> svr = player.getCurrentServer();
				if(svr.isPresent()) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(true, false,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(Player otherPlayer : svr.get().getServer().getPlayersConnected()) {
						if(player != otherPlayer) {
							EaglerPlayerData otherConn = EaglerPipeline.getEaglerHandle(otherPlayer);
							if(otherConn != null) {
								if(otherConn.getEaglerProtocol().ver >= 4) {
									otherConn.sendEaglerMessage(pkt);
								}
							}
						}
					}
				}
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void changePlayerCapePreset(Player player, int presetId, boolean notifyOtherPlayers) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			UUID uuid = eaglerHandle.getUniqueId();
			CapeServiceOffline svc = EaglerXVelocity.getEagler().getCapeService();
			if(eaglerHandle.originalCape == null) {
				eaglerHandle.originalCape = svc.getCape(uuid);
			}
			svc.unregisterPlayer(uuid);
			GameMessagePacket replacement = new SPacketOtherCapePresetEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), presetId);
			svc.registerEaglercraftPlayer(uuid, replacement);
			if(eaglerHandle.getEaglerProtocol().ver >= 4) {
				svc.processForceCape(uuid, eaglerHandle);
			}
			if(notifyOtherPlayers) {
				Optional<ServerConnection> svr = player.getCurrentServer();
				if(svr.isPresent()) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(false, true,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(Player otherPlayer : svr.get().getServer().getPlayersConnected()) {
						if(player != otherPlayer) {
							EaglerPlayerData otherConn = EaglerPipeline.getEaglerHandle(otherPlayer);
							if(otherConn != null) {
								if(otherConn.getEaglerProtocol().ver >= 4) {
									otherConn.sendEaglerMessage(pkt);
								}
							}
						}
					}
				}
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void changePlayerCapeCustom(Player player, byte[] texture, boolean notifyOtherPlayers) {
		changePlayerCapeCustom(player, texture, false, notifyOtherPlayers);
	}

	public static void changePlayerCapeCustom(Player player, byte[] texture, boolean compressedFormat, boolean notifyOtherPlayers) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			UUID uuid = eaglerHandle.getUniqueId();
			CapeServiceOffline svc = EaglerXVelocity.getEagler().getCapeService();
			if(eaglerHandle.originalCape == null) {
				eaglerHandle.originalCape = svc.getCape(uuid);
			}
			svc.unregisterPlayer(uuid);
			byte[] capeTex;
			if(compressedFormat) {
				capeTex = texture;
			}else {
				capeTex = new byte[1173];
				convertCape32x32RGBAto23x17RGB(texture, capeTex);
			}
			GameMessagePacket replacement = new SPacketOtherCapeCustomEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), capeTex);
			svc.registerEaglercraftPlayer(uuid, replacement);
			if(eaglerHandle.getEaglerProtocol().ver >= 4) {
				svc.processForceCape(uuid, eaglerHandle);
			}
			if(notifyOtherPlayers) {
				Optional<ServerConnection> svr = player.getCurrentServer();
				if(svr.isPresent()) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(false, true,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(Player otherPlayer : svr.get().getServer().getPlayersConnected()) {
						if(player != otherPlayer) {
							EaglerPlayerData otherConn = EaglerPipeline.getEaglerHandle(otherPlayer);
							if(otherConn != null) {
								if(otherConn.getEaglerProtocol().ver >= 4) {
									otherConn.sendEaglerMessage(pkt);
								}
							}
						}
					}
				}
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void setEnableFNAWSkins(Player player, boolean enable) {
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		if(conn != null) {
			setEnableFNAWSkins(conn, enable);
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void setEnableFNAWSkins(EaglerPlayerData conn, boolean enable) {
		if(enable != conn.currentFNAWSkinEnableStatus.getAndSet(enable)) {
			conn.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(enable, conn.currentFNAWSkinForceStatus.get()));
		}
	}

	public static void setForceFNAWSkins(Player player, boolean force) {
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		if(conn != null) {
			setForceFNAWSkins(conn, force);
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void setForceFNAWSkins(EaglerPlayerData conn, boolean force) {
		if(force != conn.currentFNAWSkinForceStatus.getAndSet(force)) {
			conn.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(conn.currentFNAWSkinEnableStatus.get(), force));
		}
	}

	public static void setEnableForceFNAWSkins(Player player, boolean enable, boolean force) {
		EaglerPlayerData conn = EaglerPipeline.getEaglerHandle(player);
		if(conn != null) {
			setForceFNAWSkins(conn, force);
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void setEnableForceFNAWSkins(EaglerPlayerData conn, boolean enable, boolean force) {
		if ((enable != conn.currentFNAWSkinEnableStatus.getAndSet(enable))
				| (force != conn.currentFNAWSkinForceStatus.getAndSet(force))) {
			conn.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(enable, force));
		}
	}

	public static void convertCape32x32RGBAto23x17RGB(byte[] skinIn, byte[] skinOut) {
		convertCape32x32RGBAto23x17RGB(skinIn, 0, skinOut, 0);
	}

	public static void convertCape32x32RGBAto23x17RGB(byte[] skinIn, int inOffset, byte[] skinOut, int outOffset) {
		int i, j;
		for(int y = 0; y < 17; ++y) {
			for(int x = 0; x < 22; ++x) {
				i = inOffset + ((y * 32 + x) << 2);
				j = outOffset + ((y * 23 + x) * 3);
				skinOut[j] = skinIn[i + 1];
				skinOut[j + 1] = skinIn[i + 2];
				skinOut[j + 2] = skinIn[i + 3];
			}
		}
		for(int y = 0; y < 11; ++y) {
			i = inOffset + (((y + 11) * 32 + 22) << 2);
			j = outOffset + (((y + 6) * 23 + 22) * 3);
			skinOut[j] = skinIn[i + 1];
			skinOut[j + 1] = skinIn[i + 2];
			skinOut[j + 2] = skinIn[i + 3];
		}
	}

	public static void convertCape23x17RGBto32x32RGBA(byte[] skinIn, byte[] skinOut) {
		convertCape23x17RGBto32x32RGBA(skinIn, 0, skinOut, 0);
	}

	public static void convertCape23x17RGBto32x32RGBA(byte[] skinIn, int inOffset, byte[] skinOut, int outOffset) {
		int i, j;
		for(int y = 0; y < 17; ++y) {
			for(int x = 0; x < 22; ++x) {
				i = outOffset + ((y * 32 + x) << 2);
				j = inOffset + ((y * 23 + x) * 3);
				skinOut[i] = (byte)0xFF;
				skinOut[i + 1] = skinIn[j];
				skinOut[i + 2] = skinIn[j + 1];
				skinOut[i + 3] = skinIn[j + 2];
			}
		}
		for(int y = 0; y < 11; ++y) {
			i = outOffset + (((y + 11) * 32 + 22) << 2);
			j = inOffset + (((y + 6) * 23 + 22) * 3);
			skinOut[i] = (byte)0xFF;
			skinOut[i + 1] = skinIn[j];
			skinOut[i + 2] = skinIn[j + 1];
			skinOut[i + 3] = skinIn[j + 2];
		}
	}

	public static void resetPlayerSkin(Player player, boolean notifyOtherPlayers) {
		resetPlayerMulti(player, true, false, false, notifyOtherPlayers);
	}

	public static void resetPlayerCape(Player player, boolean notifyOtherPlayers) {
		resetPlayerMulti(player, false, true, false, notifyOtherPlayers);
	}

	public static void resetPlayerSkinAndCape(Player player, boolean notifyOtherPlayers) {
		resetPlayerMulti(player, true, true, false, notifyOtherPlayers);
	}

	public static void resetPlayerMulti(Player player, boolean resetSkin, boolean resetCape, boolean resetForceFNAWSkins, boolean notifyOtherPlayers) {
		EaglerPlayerData eaglerHandler = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandler != null) {
			UUID uuid = eaglerHandler.getUniqueId();
			boolean notifyResetSkin = false;
			boolean notifyResetCape = false;
			if(resetSkin) {
				if(eaglerHandler.originalSkin != null) {
					notifyResetSkin = true;
					ISkinService svc = EaglerXVelocity.getEagler().getSkinService();
					svc.unregisterPlayer(uuid);
					svc.registerEaglercraftPlayer(uuid, eaglerHandler.originalSkin, eaglerHandler.originalSkin.getModelId());
				}
			}
			if(resetCape) {
				if(eaglerHandler.originalCape != null) {
					notifyResetCape = true;
					CapeServiceOffline svc = EaglerXVelocity.getEagler().getCapeService();
					svc.unregisterPlayer(uuid);
					svc.registerEaglercraftPlayer(uuid, eaglerHandler.originalCape);
				}
			}
			if(resetForceFNAWSkins) {
				eaglerHandler.currentFNAWSkinForceStatus.set(false);
			}
			if(eaglerHandler.getEaglerProtocol().ver >= 4) {
				eaglerHandler.sendEaglerMessage(new SPacketUnforceClientV4EAG(resetSkin, resetCape, resetForceFNAWSkins));
			}
			if(notifyOtherPlayers && (notifyResetSkin || notifyResetCape)) {
				Optional<ServerConnection> svr = player.getCurrentServer();
				if(svr != null) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(notifyResetSkin, notifyResetCape,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(Player otherPlayer : svr.get().getServer().getPlayersConnected()) {
						if(player != otherPlayer) {
							EaglerPlayerData otherConn = EaglerPipeline.getEaglerHandle(otherPlayer);
							if(otherConn != null) {
								if(otherConn.getEaglerProtocol().ver >= 4) {
									otherConn.sendEaglerMessage(pkt);
								}
							}
						}
					}
				}
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static EnumVoiceState getClientVoiceState(Player player) {
		if(player == null) {
			return EnumVoiceState.SERVER_DISABLE;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			VoiceService svc = EaglerXVelocity.getEagler().getVoiceService();
			if(svc != null && eaglerHandle.getEaglerListenerConfig().getEnableVoiceChat()) {
				Optional<ServerConnection> svr = player.getCurrentServer();
				if(svr.isPresent()) {
					return svc.getPlayerVoiceState(eaglerHandle.getUniqueId(), svr.get().getServerInfo());
				}else {
					return EnumVoiceState.SERVER_DISABLE;
				}
			}else {
				return EnumVoiceState.SERVER_DISABLE;
			}
		}else {
			return EnumVoiceState.SERVER_DISABLE;
		}
	}

	public static List<SPacketServerInfoDataChunkV4EAG> convertServerInfoToChunks(String rawData, byte[] hashReturn) throws IOException {
		return convertServerInfoToChunks(rawData.getBytes(StandardCharsets.UTF_8), hashReturn, 24576);
	}

	public static List<SPacketServerInfoDataChunkV4EAG> convertServerInfoToChunks(byte[] rawData, byte[] hashReturn) throws IOException {
		return convertServerInfoToChunks(rawData, hashReturn, 24576);
	}

	public static List<SPacketServerInfoDataChunkV4EAG> convertServerInfoToChunks(byte[] rawData, byte[] hashReturn, int chunkSize) throws IOException {
		if(hashReturn.length != 20) {
			throw new IllegalArgumentException("Hash return array must be 20 bytes long!");
		}
		
		SHA1Digest digest = new SHA1Digest();
		digest.update(rawData, 0, rawData.length);
		digest.doFinal(hashReturn, 0);
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bao.write((rawData.length >>> 24) & 0xFF);
		bao.write((rawData.length >>> 16) & 0xFF);
		bao.write((rawData.length >>> 8) & 0xFF);
		bao.write(rawData.length & 0xFF);
		
		try(OutputStream os = new GZIPOutputStream(bao)) {
			os.write(rawData);
		}
		
		byte[] toFragment = bao.toByteArray();
		
		int i = 0, j = 0, k = 0;
		List<SPacketServerInfoDataChunkV4EAG> ret = new ArrayList<>();
		while(i < toFragment.length) {
			j = Math.min(toFragment.length - i, chunkSize);
			byte[] chunk = new byte[j];
			System.arraycopy(toFragment, i, chunk, 0, j);
			ret.add(new SPacketServerInfoDataChunkV4EAG(i + j == toFragment.length, k++, hashReturn, toFragment.length, chunk));
			i += j;
		}
		
		return ret;
	}

	public static String loadFileToStringServerInfo(File serverInfoFile) throws IOException {
		return new String(loadFileToByteArrayServerInfo(serverInfoFile), StandardCharsets.UTF_8);
	}

	public static byte[] loadFileToByteArrayServerInfo(File serverInfoFile) throws IOException {
		long ll = serverInfoFile.length();
		if(ll > 0x2000000l) {
			throw new IOException("File is too large: " + serverInfoFile.getAbsolutePath());
		}
		int expectLen = (int)ll;
		if(expectLen <= 0) {
			expectLen = 1024;
		}
		ByteArrayOutputStream bao = new ByteArrayOutputStream(expectLen);
		byte[] copyBuffer = new byte[Math.min(expectLen, 1024)];
		try(InputStream is = new FileInputStream(serverInfoFile)) {
			int i;
			while((i = is.read(copyBuffer)) != -1) {
				bao.write(copyBuffer, 0, i);
			}
		}
		return bao.toByteArray();
	}

	public static String loadServerInfoTemplateEagler(File serverInfoFile, File baseDir, boolean allowEvalMacro) throws IOException {
		return loadServerInfoTemplateEagler(serverInfoFile, serverInfoFile.getParentFile(), allowEvalMacro);
	}

	public static String loadServerInfoTemplateEagler(File serverInfoFile, boolean allowEvalMacro) throws IOException {
		return loadServerInfoTemplateEagler(serverInfoFile, serverInfoFile.getParentFile(), allowEvalMacro);
	}

	public static String loadServerInfoTemplateEagler(String serverInfoFile, File baseDir, boolean allowEvalMacro) throws IOException {
		return ServerInfoTemplateParser.loadTemplate(serverInfoFile, baseDir, allowEvalMacro, templateGlobals);
	}

	private static final Map<String, String> templateGlobals = new ConcurrentHashMap<>();

	public static Map<String, String> getTemplateGlobals() {
		return templateGlobals;
	}

	public static boolean getWebViewSupport(Player player) {
		if(player == null) {
			return false;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			return eaglerHandle.getWebViewSupport();
		}else {
			return false;
		}
	}

	public static boolean getWebViewSupport(EaglerPlayerData conn) {
		return conn != null && conn.getWebViewSupport();
	}

	public static boolean getWebViewMessageChannelOpen(Player player) {
		if(player == null) {
			return false;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null && eaglerHandle.getWebViewMessageChannelOpen();
	}

	public static boolean getWebViewMessageChannelOpen(EaglerPlayerData conn) {
		return conn != null && conn.getWebViewMessageChannelOpen();
	}

	public static String getWebViewMessageChannelName(Player player) {
		if(player == null) {
			return null;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null ? eaglerHandle.getWebViewMessageChannelName() : null;
	}

	public static String getWebViewMessageChannelName(EaglerPlayerData conn) {
		return conn != null ? conn.getWebViewMessageChannelName() : null;
	}

	public static boolean checkCurrentWebViewChannelIsOpen(Player player, String channelName) {
		if(player == null) {
			return false;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null && eaglerHandle.getWebViewMessageChannelOpen() && channelName.equals(eaglerHandle.getWebViewMessageChannelName());
	}

	public static boolean checkCurrentWebViewChannelIsOpen(EaglerPlayerData conn, String channelName) {
		return conn != null && conn.getWebViewMessageChannelOpen() && channelName.equals(conn.getWebViewMessageChannelName());
	}

	public static void sendWebViewMessagePacket(Player player, String str) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.sendWebViewMessage(str);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void sendWebViewMessagePacket(EaglerPlayerData conn, String str) {
		conn.sendWebViewMessage(str);
	}

	public static void sendWebViewMessagePacket(Player player, byte[] data) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.sendWebViewMessage(data);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void sendWebViewMessagePacket(EaglerPlayerData conn, byte[] data) {
		conn.sendWebViewMessage(data);
	}

	public static EnumWebViewState getWebViewState(Player player) {
		if(player == null) {
			return EnumWebViewState.NOT_SUPPORTED;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null ? eaglerHandle.getWebViewState() : EnumWebViewState.NOT_SUPPORTED;
	}

	public static EnumWebViewState getWebViewState(EaglerPlayerData conn) {
		return conn != null ? conn.getWebViewState() : EnumWebViewState.NOT_SUPPORTED;
	}

	public static PacketImageData loadPacketImageData(File img) throws IOException {
		return loadPacketImageData(ImageIO.read(img), 255, 255);
	}

	public static PacketImageData loadPacketImageData(File img, int maxWidth, int maxHeight) throws IOException {
		return loadPacketImageData(ImageIO.read(img), maxWidth, maxHeight);
	}

	public static PacketImageData loadPacketImageData(BufferedImage img) {
		return loadPacketImageData(img, 255, 255);
	}

	public static PacketImageData loadPacketImageData(BufferedImage img, int maxWidth, int maxHeight) {
		int w = img.getWidth();
		int h = img.getHeight();
		if(w > maxWidth || h > maxHeight) {
			float aspectRatio = (float)w / (float)h;
			int nw, nh;
			if(aspectRatio >= 1.0f) {
				nw = (int)(maxWidth / aspectRatio);
				nh = maxHeight;
			}else {
				nw = maxWidth;
				nh = (int)(maxHeight * aspectRatio);
			}
			BufferedImage resized = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) resized.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setBackground(new Color(0, true));
			g.clearRect(0, 0, nw, nh);
			g.drawImage(img, 0, 0, nw, nh, 0, 0, w, h, null);
			g.dispose();
			img = resized;
		}
		int[] pixels = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);
		return new PacketImageData(w, h, pixels);
	}

	public static boolean canSendNotificationsTo(Player player) {
		if(player == null) {
			return false;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null && eaglerHandle.notificationSupported();
	}

	public static boolean canSendNotificationsTo(EaglerPlayerData conn) {
		return conn.notificationSupported();
	}

	public static void registerNotificationIcon(Player player, UUID uuid, PacketImageData imageData) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.registerNotificationIcon(uuid, imageData);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void registerNotificationIcon(EaglerPlayerData conn, UUID uuid, PacketImageData imageData) {
		conn.registerNotificationIcon(uuid, imageData);
	}

	public static void registerNotificationIcons(Player player, Map<UUID,PacketImageData> imageDatas) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.registerNotificationIcons(imageDatas);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void registerNotificationIcons(EaglerPlayerData conn, Map<UUID,PacketImageData> imageDatas) {
		conn.registerNotificationIcons(imageDatas);
	}

	public static void showNotificationBadge(Player player, NotificationBadgeBuilder badgeBuilder) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.showNotificationBadge(badgeBuilder);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void showNotificationBadge(EaglerPlayerData conn, NotificationBadgeBuilder badgeBuilder) {
		conn.showNotificationBadge(badgeBuilder);
	}

	public static void showNotificationBadge(Player player, SPacketNotifBadgeShowV4EAG badgePacket) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.showNotificationBadge(badgePacket);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void showNotificationBadge(EaglerPlayerData conn, SPacketNotifBadgeShowV4EAG badgePacket) {
		conn.showNotificationBadge(badgePacket);
	}

	public static void hideNotificationBadge(Player player, UUID badgeUUID) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.hideNotificationBadge(badgeUUID);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void hideNotificationBadge(EaglerPlayerData conn, UUID badgeUUID) {
		conn.hideNotificationBadge(badgeUUID);
	}

	public static void releaseNotificationIcon(Player player, UUID uuid) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.releaseNotificationIcon(uuid);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void releaseNotificationIcon(EaglerPlayerData conn, UUID uuid) {
		conn.releaseNotificationIcon(uuid);
	}

	public static void releaseNotificationIcons(Player player, Collection<UUID> uuids) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(eaglerHandle != null) {
			eaglerHandle.releaseNotificationIcons(uuids);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void releaseNotificationIcons(EaglerPlayerData conn, Collection<UUID> uuids) {
		conn.releaseNotificationIcons(uuids);
	}

	public static int getEaglerProtocolVersion(Player player) {
		if(player == null) {
			return -1;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null ? eaglerHandle.getEaglerProtocolHandshake() : -1;
	}

	public static int getEaglerProtocolVersion(EaglerPlayerData conn) {
		return conn != null ? conn.getEaglerProtocolHandshake() : -1;
	}

	public static GamePluginMessageProtocol getMessageProtocolVersion(Player player) {
		if(player == null) {
			return null;
		}
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		return eaglerHandle != null ? eaglerHandle.getEaglerProtocol() : null;
	}

	public static GamePluginMessageProtocol getMessageProtocolVersion(EaglerPlayerData conn) {
		return conn.getEaglerProtocol();
	}

	public static void sendEaglerMessagePacket(Player player, GameMessagePacket packet) {
		EaglerPlayerData eaglerHandle = EaglerPipeline.getEaglerHandle(player);
		if(player != null) {
			eaglerHandle.sendEaglerMessage(packet);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void sendEaglerMessagePacket(EaglerPlayerData conn, GameMessagePacket packet) {
		conn.sendEaglerMessage(packet);
	}

	public static long steadyTimeMillis() {
		return System.nanoTime() / 1000000l;
	}

}
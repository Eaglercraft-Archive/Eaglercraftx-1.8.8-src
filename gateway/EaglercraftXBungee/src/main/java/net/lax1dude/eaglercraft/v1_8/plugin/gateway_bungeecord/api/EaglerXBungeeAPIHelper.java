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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api;

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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.ServerInfoTemplateParser;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.protocol.GameProtocolMessageController;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.CapeServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.ISkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinRescaler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice.VoiceService;
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
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class EaglerXBungeeAPIHelper {

	public static EaglerXBungee getEaglerXBungee() {
		return EaglerXBungee.getEagler();
	}

	public static EaglerInitialHandler getEaglerHandle(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? (EaglerInitialHandler)conn : null;
	}

	public static EaglerInitialHandler getEaglerHandle(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? (EaglerInitialHandler)conn : null;
	}

	public static boolean isEaglerPlayer(ProxiedPlayer player) {
		return player.getPendingConnection() instanceof EaglerInitialHandler;
	}

	public static boolean isEaglerPlayer(PendingConnection conn) {
		return conn instanceof EaglerInitialHandler;
	}

	public static ProxiedPlayer getPlayer(PendingConnection conn) {
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			GameProtocolMessageController controller = initialHandler.getEaglerMessageController();
			if(controller != null) {
				return controller.getUserConnection();
			}else {
				return initialHandler.ch.getHandle().attr(EaglerPipeline.CONNECTION_INSTANCE).get().userConnection;
			}
		}else {
			return null;
		}
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

	public static UUID getClientBrandUUID(ProxiedPlayer player) {
		if(player == null) {
			return BRAND_NULL_UUID;
		}
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			UUID ret = ((EaglerInitialHandler)conn).getClientBrandUUID();
			if(ret != null) {
				return ret;
			}else {
				return BRAND_NULL_UUID;
			}
		}else {
			return BRAND_VANILLA_UUID;
		}
	}

	public static UUID getClientBrandUUID(PendingConnection conn) {
		if(conn == null) {
			return BRAND_NULL_UUID;
		}
		if(conn instanceof EaglerInitialHandler) {
			UUID ret = ((EaglerInitialHandler)conn).getClientBrandUUID();
			if(ret != null) {
				return ret;
			}else {
				return BRAND_NULL_UUID;
			}
		}else {
			return BRAND_VANILLA_UUID;
		}
	}

	public static String getClientVersionString(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerVersionString() : null;
	}

	public static String getClientVersionString(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerVersionString() : null;
	}

	public static String getClientBrandString(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerBrandString() : null;
	}

	public static String getClientBrandString(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerBrandString() : null;
	}

	public static String getClientOrigin(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getOrigin() : null;
	}

	public static String getClientOrigin(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getOrigin() : null;
	}

	public static String getClientUserAgent(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getUserAgent() : null;
	}

	public static String getClientUserAgent(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getUserAgent() : null;
	}

	public static boolean getCookieAllowed(ProxiedPlayer player) {
		if(player == null) {
			return false;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) && ((EaglerInitialHandler)conn).getCookieAllowed();
	}

	public static boolean getCookieAllowed(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) && ((EaglerInitialHandler)conn).getCookieAllowed();
	}

	public static byte[] getCookieData(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getCookieData() : null;
	}

	public static byte[] getCookieData(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getCookieData() : null;
	}

	public static String getCookieDataString(ProxiedPlayer player) {
		byte[] ret = getCookieData(player);
		return ret != null ? new String(ret, StandardCharsets.UTF_8) : null;
	}

	public static String getCookieDataString(PendingConnection conn) {
		byte[] ret = getCookieData(conn);
		return ret != null ? new String(ret, StandardCharsets.UTF_8) : null;
	}

	public static void setCookieDataString(ProxiedPlayer player, String data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(player, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieDataString(ProxiedPlayer player, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(player, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieDataString(ProxiedPlayer player, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(player, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieDataString(ProxiedPlayer player, String data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(player.getPendingConnection(), data != null ? data.getBytes(StandardCharsets.UTF_8) : null, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieDataString(PendingConnection conn, String data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieDataString(PendingConnection conn, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieDataString(PendingConnection conn, String data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieDataString(PendingConnection conn, String data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(conn, data != null ? data.getBytes(StandardCharsets.UTF_8) : null, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieData(ProxiedPlayer player, byte[] data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(player, data, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieData(ProxiedPlayer player, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(player, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieData(ProxiedPlayer player, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(player, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieData(ProxiedPlayer player, byte[] data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(player.getPendingConnection(), data, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieData(PendingConnection conn, byte[] data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(conn, data, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public static void setCookieData(PendingConnection conn, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(conn, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public static void setCookieData(PendingConnection conn, byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(conn, data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public static void setCookieData(PendingConnection conn, byte[] data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).setCookieData(data, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk);
		}else {
			throw new UnsupportedOperationException("Can't set cookies on vanilla players!");
		}
	}

	public static void clearCookieData(ProxiedPlayer player) {
		setCookieData(player.getPendingConnection(), (byte[])null, 0, false, false);
	}

	public static void clearCookieData(PendingConnection conn) {
		setCookieData(conn, (byte[])null, 0, false, false);
	}

	public static boolean getRedirectSupported(ProxiedPlayer player) {
		return getRedirectSupported(player.getPendingConnection());
	}

	public static boolean getRedirectSupported(PendingConnection player) {
		return (player instanceof EaglerInitialHandler) && ((EaglerInitialHandler)player).redirectToWebSocketSupported();
	}

	public static void redirectPlayerToWebSocket(ProxiedPlayer player, String serverAddress) {
		redirectPlayerToWebSocket(player.getPendingConnection(), serverAddress);
	}

	public static void redirectPlayerToWebSocket(PendingConnection conn, String serverAddress) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).redirectPlayerToWebSocket(serverAddress);
		}else {
			throw new UnsupportedOperationException("Can't redirect vanilla players to websocket addresses!");
		}
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

	public static void changePlayerSkinPreset(ProxiedPlayer player, int presetId, boolean notifyOtherPlayers) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			UUID uuid = initialHandler.getUniqueId();
			ISkinService svc = EaglerXBungee.getEagler().getSkinService();
			if(initialHandler.originalSkin == null) {
				initialHandler.originalSkin = svc.getSkin(uuid);
			}
			svc.unregisterPlayer(uuid);
			GameMessagePacket replacement = new SPacketOtherSkinPresetEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), presetId);
			svc.registerEaglercraftPlayer(uuid, new SkinPacketVersionCache(replacement, replacement), -1);
			if(initialHandler.getEaglerProtocol().ver >= 4) {
				svc.processForceSkin(uuid, initialHandler);
			}
			if(notifyOtherPlayers) {
				Server svr = player.getServer();
				if(svr != null) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(true, false,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(ProxiedPlayer otherPlayer : svr.getInfo().getPlayers()) {
						if(player != otherPlayer) {
							PendingConnection otherConn = otherPlayer.getPendingConnection();
							if(otherConn instanceof EaglerInitialHandler) {
								EaglerInitialHandler otherConnEag = (EaglerInitialHandler)otherConn;
								if(otherConnEag.getEaglerProtocol().ver >= 4) {
									otherConnEag.sendEaglerMessage(pkt);
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

	public static void changePlayerSkinCustom(ProxiedPlayer player, int modelId, byte[] texture, boolean notifyOtherPlayers) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			UUID uuid = initialHandler.getUniqueId();
			ISkinService svc = EaglerXBungee.getEagler().getSkinService();
			if(initialHandler.originalSkin == null) {
				initialHandler.originalSkin = svc.getSkin(uuid);
			}
			svc.unregisterPlayer(uuid);
			GameMessagePacket replacement = new SPacketOtherSkinCustomV3EAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), modelId, texture);
			svc.registerEaglercraftPlayer(uuid, new SkinPacketVersionCache(replacement, null), modelId);
			if(initialHandler.getEaglerProtocol().ver >= 4) {
				svc.processForceSkin(uuid, initialHandler);
			}
			if(notifyOtherPlayers) {
				Server svr = player.getServer();
				if(svr != null) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(true, false,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(ProxiedPlayer otherPlayer : svr.getInfo().getPlayers()) {
						if(player != otherPlayer) {
							PendingConnection otherConn = otherPlayer.getPendingConnection();
							if(otherConn instanceof EaglerInitialHandler) {
								EaglerInitialHandler otherConnEag = (EaglerInitialHandler)otherConn;
								if(otherConnEag.getEaglerProtocol().ver >= 4) {
									otherConnEag.sendEaglerMessage(pkt);
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

	public static void changePlayerCapePreset(ProxiedPlayer player, int presetId, boolean notifyOtherPlayers) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			UUID uuid = initialHandler.getUniqueId();
			CapeServiceOffline svc = EaglerXBungee.getEagler().getCapeService();
			if(initialHandler.originalCape == null) {
				initialHandler.originalCape = svc.getCape(uuid);
			}
			svc.unregisterPlayer(uuid);
			GameMessagePacket replacement = new SPacketOtherCapePresetEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), presetId);
			svc.registerEaglercraftPlayer(uuid, replacement);
			if(initialHandler.getEaglerProtocol().ver >= 4) {
				svc.processForceCape(uuid, initialHandler);
			}
			if(notifyOtherPlayers) {
				Server svr = player.getServer();
				if(svr != null) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(false, true,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(ProxiedPlayer otherPlayer : svr.getInfo().getPlayers()) {
						if(player != otherPlayer) {
							PendingConnection otherConn = otherPlayer.getPendingConnection();
							if(otherConn instanceof EaglerInitialHandler) {
								EaglerInitialHandler otherConnEag = (EaglerInitialHandler)otherConn;
								if(otherConnEag.getEaglerProtocol().ver >= 4) {
									otherConnEag.sendEaglerMessage(pkt);
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

	public static void changePlayerCapeCustom(ProxiedPlayer player, byte[] texture, boolean notifyOtherPlayers) {
		changePlayerCapeCustom(player, texture, false, notifyOtherPlayers);
	}

	public static void changePlayerCapeCustom(ProxiedPlayer player, byte[] texture, boolean compressedFormat, boolean notifyOtherPlayers) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			UUID uuid = initialHandler.getUniqueId();
			CapeServiceOffline svc = EaglerXBungee.getEagler().getCapeService();
			if(initialHandler.originalCape == null) {
				initialHandler.originalCape = svc.getCape(uuid);
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
			if(initialHandler.getEaglerProtocol().ver >= 4) {
				svc.processForceCape(uuid, initialHandler);
			}
			if(notifyOtherPlayers) {
				Server svr = player.getServer();
				if(svr != null) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(false, true,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(ProxiedPlayer otherPlayer : svr.getInfo().getPlayers()) {
						if(player != otherPlayer) {
							PendingConnection otherConn = otherPlayer.getPendingConnection();
							if(otherConn instanceof EaglerInitialHandler) {
								EaglerInitialHandler otherConnEag = (EaglerInitialHandler)otherConn;
								if(otherConnEag.getEaglerProtocol().ver >= 4) {
									otherConnEag.sendEaglerMessage(pkt);
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

	public static void setEnableFNAWSkins(ProxiedPlayer player, boolean enable) {
		setEnableFNAWSkins(player.getPendingConnection(), enable);
	}

	public static void setEnableFNAWSkins(PendingConnection conn, boolean enable) {
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			if(enable != initialHandler.currentFNAWSkinEnableStatus.getAndSet(enable)) {
				initialHandler.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(enable, initialHandler.currentFNAWSkinForceStatus.get()));
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void setForceFNAWSkins(ProxiedPlayer player, boolean force) {
		setForceFNAWSkins(player.getPendingConnection(), force);
	}

	public static void setForceFNAWSkins(PendingConnection conn, boolean force) {
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			if(force != initialHandler.currentFNAWSkinForceStatus.getAndSet(force)) {
				initialHandler.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(initialHandler.currentFNAWSkinEnableStatus.get(), force));
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
		}
	}

	public static void setEnableForceFNAWSkins(ProxiedPlayer player, boolean enable, boolean force) {
		setForceFNAWSkins(player.getPendingConnection(), force);
	}

	public static void setEnableForceFNAWSkins(PendingConnection conn, boolean enable, boolean force) {
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			if ((enable != initialHandler.currentFNAWSkinEnableStatus.getAndSet(enable))
					| (force != initialHandler.currentFNAWSkinForceStatus.getAndSet(force))) {
				initialHandler.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(enable, force));
			}
		}else {
			throw new UnsupportedOperationException("Can't send eagler packets to vanilla players!");
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

	public static void resetPlayerSkin(ProxiedPlayer player, boolean notifyOtherPlayers) {
		resetPlayerMulti(player, true, false, false, notifyOtherPlayers);
	}

	public static void resetPlayerCape(ProxiedPlayer player, boolean notifyOtherPlayers) {
		resetPlayerMulti(player, false, true, false, notifyOtherPlayers);
	}

	public static void resetPlayerSkinAndCape(ProxiedPlayer player, boolean notifyOtherPlayers) {
		resetPlayerMulti(player, true, true, false, notifyOtherPlayers);
	}

	public static void resetPlayerMulti(ProxiedPlayer player, boolean resetSkin, boolean resetCape, boolean resetForceFNAWSkins, boolean notifyOtherPlayers) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			UUID uuid = initialHandler.getUniqueId();
			boolean notifyResetSkin = false;
			boolean notifyResetCape = false;
			if(resetSkin) {
				if(initialHandler.originalSkin != null) {
					notifyResetSkin = true;
					ISkinService svc = EaglerXBungee.getEagler().getSkinService();
					svc.unregisterPlayer(uuid);
					svc.registerEaglercraftPlayer(uuid, initialHandler.originalSkin, initialHandler.originalSkin.getModelId());
				}
			}
			if(resetCape) {
				if(initialHandler.originalCape != null) {
					notifyResetCape = true;
					CapeServiceOffline svc = EaglerXBungee.getEagler().getCapeService();
					svc.unregisterPlayer(uuid);
					svc.registerEaglercraftPlayer(uuid, initialHandler.originalCape);
				}
			}
			if(resetForceFNAWSkins) {
				initialHandler.currentFNAWSkinForceStatus.set(false);
			}
			if(initialHandler.getEaglerProtocol().ver >= 4) {
				initialHandler.sendEaglerMessage(new SPacketUnforceClientV4EAG(resetSkin, resetCape, resetForceFNAWSkins));
			}
			if(notifyOtherPlayers && (notifyResetSkin || notifyResetCape)) {
				Server svr = player.getServer();
				if(svr != null) {
					SPacketInvalidatePlayerCacheV4EAG pkt = new SPacketInvalidatePlayerCacheV4EAG(notifyResetSkin, notifyResetCape,
							uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
					for(ProxiedPlayer otherPlayer : svr.getInfo().getPlayers()) {
						if(player != otherPlayer) {
							PendingConnection otherConn = otherPlayer.getPendingConnection();
							if(otherConn instanceof EaglerInitialHandler) {
								EaglerInitialHandler otherConnEag = (EaglerInitialHandler)otherConn;
								if(otherConnEag.getEaglerProtocol().ver >= 4) {
									otherConnEag.sendEaglerMessage(pkt);
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

	public static EnumVoiceState getClientVoiceState(ProxiedPlayer player) {
		if(player == null) {
			return EnumVoiceState.SERVER_DISABLE;
		}
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler initialHandler = (EaglerInitialHandler)conn;
			VoiceService svc = EaglerXBungee.getEagler().getVoiceService();
			if(svc != null && initialHandler.getEaglerListenerConfig().getEnableVoiceChat()) {
				Server svr = player.getServer();
				if(svr != null) {
					return svc.getPlayerVoiceState(initialHandler.getUniqueId(), svr.getInfo());
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

	public static boolean getWebViewSupport(ProxiedPlayer player) {
		if(player == null) {
			return false;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getWebViewSupport() : null;
	}

	public static boolean getWebViewSupport(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) && ((EaglerInitialHandler)conn).getWebViewSupport();
	}

	public static boolean getWebViewMessageChannelOpen(ProxiedPlayer player) {
		if(player == null) {
			return false;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) && ((EaglerInitialHandler)conn).getWebViewMessageChannelOpen();
	}

	public static boolean getWebViewMessageChannelOpen(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) && ((EaglerInitialHandler)conn).getWebViewMessageChannelOpen();
	}

	public static String getWebViewMessageChannelName(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getWebViewMessageChannelName() : null;
	}

	public static String getWebViewMessageChannelName(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getWebViewMessageChannelName() : null;
	}

	public static boolean checkCurrentWebViewChannelIsOpen(ProxiedPlayer player, String channelName) {
		if(player == null) {
			return false;
		}
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)conn;
			return eaglerHandler.getWebViewMessageChannelOpen() && channelName.equals(eaglerHandler.getWebViewMessageChannelName());
		}else {
			return false;
		}
	}

	public static boolean checkCurrentWebViewChannelIsOpen(PendingConnection conn, String channelName) {
		if(conn == null) {
			return false;
		}
		if(conn instanceof EaglerInitialHandler) {
			EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)conn;
			return eaglerHandler.getWebViewMessageChannelOpen() && channelName.equals(eaglerHandler.getWebViewMessageChannelName());
		}else {
			return false;
		}
	}

	public static void sendWebViewMessagePacket(ProxiedPlayer player, String str) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).sendWebViewMessage(str);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void sendWebViewMessagePacket(PendingConnection conn, String str) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).sendWebViewMessage(str);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void sendWebViewMessagePacket(ProxiedPlayer player, byte[] data) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).sendWebViewMessage(data);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void sendWebViewMessagePacket(PendingConnection conn, byte[] data) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).sendWebViewMessage(data);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static EnumWebViewState getWebViewState(ProxiedPlayer player) {
		if(player == null) {
			return EnumWebViewState.NOT_SUPPORTED;
		}
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			return ((EaglerInitialHandler)conn).getWebViewState();
		}else {
			return EnumWebViewState.NOT_SUPPORTED;
		}
	}

	public static EnumWebViewState getWebViewState(PendingConnection conn) {
		if(conn == null) {
			return EnumWebViewState.NOT_SUPPORTED;
		}
		if(conn instanceof EaglerInitialHandler) {
			return ((EaglerInitialHandler)conn).getWebViewState();
		}else {
			return EnumWebViewState.NOT_SUPPORTED;
		}
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

	public static boolean canSendNotificationsTo(ProxiedPlayer player) {
		if(player == null) {
			return false;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) && ((EaglerInitialHandler)conn).notificationSupported();
	}

	public static boolean canSendNotificationsTo(PendingConnection conn) {
		return conn != null && (conn instanceof EaglerInitialHandler) && ((EaglerInitialHandler)conn).notificationSupported();
	}

	public static void registerNotificationIcon(ProxiedPlayer player, UUID uuid, PacketImageData imageData) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).registerNotificationIcon(uuid, imageData);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void registerNotificationIcon(PendingConnection conn, UUID uuid, PacketImageData imageData) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).registerNotificationIcon(uuid, imageData);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void registerNotificationIcons(ProxiedPlayer player, Map<UUID,PacketImageData> imageDatas) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).registerNotificationIcons(imageDatas);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void registerNotificationIcons(PendingConnection conn, Map<UUID,PacketImageData> imageDatas) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).registerNotificationIcons(imageDatas);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void showNotificationBadge(ProxiedPlayer player, NotificationBadgeBuilder badgeBuilder) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).showNotificationBadge(badgeBuilder);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void showNotificationBadge(PendingConnection conn, NotificationBadgeBuilder badgeBuilder) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).showNotificationBadge(badgeBuilder);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void showNotificationBadge(ProxiedPlayer player, SPacketNotifBadgeShowV4EAG badgePacket) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).showNotificationBadge(badgePacket);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void showNotificationBadge(PendingConnection conn, SPacketNotifBadgeShowV4EAG badgePacket) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).showNotificationBadge(badgePacket);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void hideNotificationBadge(ProxiedPlayer player, UUID badgeUUID) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler) conn).hideNotificationBadge(badgeUUID);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void hideNotificationBadge(PendingConnection conn, UUID badgeUUID) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler) conn).hideNotificationBadge(badgeUUID);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void releaseNotificationIcon(ProxiedPlayer player, UUID uuid) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler) conn).releaseNotificationIcon(uuid);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void releaseNotificationIcon(PendingConnection conn, UUID uuid) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler) conn).releaseNotificationIcon(uuid);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void releaseNotificationIcons(ProxiedPlayer player, Collection<UUID> uuids) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler) conn).releaseNotificationIcons(uuids);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void releaseNotificationIcons(PendingConnection conn, Collection<UUID> uuids) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler) conn).releaseNotificationIcons(uuids);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static int getEaglerProtocolVersion(ProxiedPlayer player) {
		if(player == null) {
			return -1;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerProtocolHandshake() : -1;
	}

	public static int getEaglerProtocolVersion(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerProtocolHandshake() : -1;
	}

	public static GamePluginMessageProtocol getMessageProtocolVersion(ProxiedPlayer player) {
		if(player == null) {
			return null;
		}
		PendingConnection conn = player.getPendingConnection();
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerProtocol() : null;
	}

	public static GamePluginMessageProtocol getMessageProtocolVersion(PendingConnection conn) {
		return (conn instanceof EaglerInitialHandler) ? ((EaglerInitialHandler)conn).getEaglerProtocol() : null;
	}

	public static void sendEaglerMessagePacket(ProxiedPlayer player, GameMessagePacket packet) {
		PendingConnection conn = player.getPendingConnection();
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).sendEaglerMessage(packet);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static void sendEaglerMessagePacket(PendingConnection conn, GameMessagePacket packet) {
		if(conn instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)conn).sendEaglerMessage(packet);
		}else {
			throw new UnsupportedOperationException("Can't send eagler message packets to vanilla players!");
		}
	}

	public static long steadyTimeMillis() {
		return System.nanoTime() / 1000000l;
	}

}
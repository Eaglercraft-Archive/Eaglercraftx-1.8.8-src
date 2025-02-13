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

package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.EaglerBackendRPCPacket;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.CPacketRPCNotifBadgeShow;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.CPacketRPCSetPauseMenuCustom;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util.NotificationBadgeBuilder;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util.PacketImageData;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util.SkinPacketHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.event.IEaglerRPCEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.ResponseBytes;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.ResponseCookie;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.ResponseString;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.ResponseUUID;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.ResponseVoiceStatus;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.ResponseWebViewStatus;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl.EaglerXBukkitImpl;

public interface IEaglerXBukkitAPI {

	public static IEaglerRPCFuture<IEaglerXBukkitAPI> getAPI(Player player) {
		IEaglerRPCFuture<IEaglerXBukkitAPI> futureRet = EaglerXBukkitImpl.getAPI(player);
		futureRet.setExpiresMSFromNow(EaglerXBukkitImpl.DEFAULT_TIMEOUT);
		return futureRet;
	}

	public static IEaglerRPCFuture<IEaglerXBukkitAPI> getAPI(Player player, int timeoutMS) {
		IEaglerRPCFuture<IEaglerXBukkitAPI> futureRet = EaglerXBukkitImpl.getAPI(player);
		futureRet.setExpiresMSFromNow(timeoutMS);
		return futureRet;
	}

	EaglerBackendRPCProtocol getRPCProtocolVersion();

	int getEaglerProtocolVersion();

	boolean isOpen();

	void closeAPI();

	Player getPlayer();

	void sendRPCPacket(EaglerBackendRPCPacket packet);

	default void subscribeEvents(EnumSubscribeEvents...events) {
		int bits = 0;
		for(int i = 0; i < events.length; ++i) {
			bits |= events[i].bit;
		}
		subscribeEvents(bits);
	}

	void subscribeEvents(int events);

	default void unsubscribeEvents(EnumSubscribeEvents...events) {
		int bits = 0;
		for(int i = 0; i < events.length; ++i) {
			bits |= events[i].bit;
		}
		unsubscribeEvents(bits);
	}

	void unsubscribeEvents(int events);

	void unsubscribeAllEvents();

	int getSubscribedEventsBits();

	default Set<EnumSubscribeEvents> getSubscribedEvents() {
		Set<EnumSubscribeEvents> ret = new HashSet<>(4);
		int bits = getSubscribedEventsBits();
		EnumSubscribeEvents[] enums = EnumSubscribeEvents._VALUES;
		for(int i = 0; i < enums.length; ++i) {
			if((bits & enums[i].bit) != 0) {
				ret.add(enums[i]);
			}
		}
		return ret;
	}

	void addEventListener(EnumSubscribeEvents eventType, IEaglerRPCEventHandler<? extends IEaglerRPCEvent> handler);

	void removeEventListener(EnumSubscribeEvents eventType, IEaglerRPCEventHandler<? extends IEaglerRPCEvent> handler);

	void removeEventListeners(EnumSubscribeEvents eventType);

	void addCloseListener(IEaglerRPCCloseHandler handler);

	void removeCloseListener(IEaglerRPCCloseHandler handler);

	void removeCloseListeners();

	boolean redirectPlayerSupported();

	void redirectPlayerToWebSocket(String webSocketURI);

	void setBaseRequestTimeout(int seconds);

	IEaglerRPCFuture<ResponseUUID> requestPlayerProfileUUID();

	IEaglerRPCFuture<ResponseString> requestPlayerRealIP();

	IEaglerRPCFuture<ResponseString> requestPlayerOrigin();

	IEaglerRPCFuture<ResponseString> requestPlayerUserAgent();

	IEaglerRPCFuture<ResponseBytes> requestPlayerSkinData();

	IEaglerRPCFuture<ResponseBytes> requestPlayerCapeData();

	IEaglerRPCFuture<ResponseCookie> requestPlayerCookieData();

	IEaglerRPCFuture<ResponseString> requestPlayerClientBrandStr();

	IEaglerRPCFuture<ResponseString> requestPlayerClientVersionStr();

	IEaglerRPCFuture<ResponseString> requestPlayerClientBrandAndVersionStr();

	IEaglerRPCFuture<ResponseUUID> requestPlayerClientBrandUUID();

	IEaglerRPCFuture<ResponseVoiceStatus> requestPlayerVoiceStatus();

	IEaglerRPCFuture<ResponseWebViewStatus> requestPlayerWebViewStatus();

	void sendRawCustomPayloadPacket(String channel, byte[] data);

	default void sendRawEaglerPacketV4(byte[] data) {
		sendRawCustomPayloadPacket("EAG|1.8", data);
	}

	boolean pauseMenuCustomizationSupported();

	void setPauseMenuCustomizationState(CPacketRPCSetPauseMenuCustom packet);

	void sendWebViewMessageString(String channelName, String data);

	void sendWebViewMessageBytes(String channelName, byte[] data);

	void forcePlayerSkin(byte[] skinData, boolean notifyOthers);

	default void forcePlayerSkinPreset(int presetID, boolean notifyOthers) {
		forcePlayerSkin(SkinPacketHelper.writePresetSkinPacket(presetID), notifyOthers);
	}

	default void forcePlayerSkinCustom(int modelId, byte[] texture64x64, boolean notifyOthers) {
		forcePlayerSkin(SkinPacketHelper.writeCustomSkinPacket(modelId, texture64x64), notifyOthers);
	}

	void forcePlayerCape(byte[] capeData, boolean notifyOthers);

	default void forcePlayerCapePreset(int presetID, boolean notifyOthers) {
		forcePlayerCape(SkinPacketHelper.writePresetCapePacket(presetID), notifyOthers);
	}

	default void forcePlayerCapeCustom(byte[] texture32x32, boolean notifyOthers) {
		forcePlayerCape(SkinPacketHelper.writeCustomCapePacket(texture32x32), notifyOthers);
	}

	void setCookieData(byte[] cookieData, int expiresAfterSec, boolean revokeQuerySupported, boolean saveToDisk);

	default void setCookieData(byte[] cookieData, int expiresAfter, TimeUnit expiresTimeUnit, boolean revokeQuerySupported, boolean saveToDisk) {
		setCookieData(cookieData, (int)expiresTimeUnit.toSeconds(expiresAfter), revokeQuerySupported, saveToDisk);
	}

	default void setCookieData(byte[] cookieData, int expiresAfterSec, boolean revokeQuerySupported) {
		setCookieData(cookieData, expiresAfterSec, revokeQuerySupported, true);
	}

	default void setCookieData(byte[] cookieData, int expiresAfter, TimeUnit expiresTimeUnit, boolean revokeQuerySupported) {
		setCookieData(cookieData, (int)expiresTimeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	default void setCookieData(byte[] cookieData, int expiresAfterSec) {
		setCookieData(cookieData, expiresAfterSec, false, true);
	}

	default void setCookieData(byte[] cookieData, int expiresAfter, TimeUnit expiresTimeUnit) {
		setCookieData(cookieData, (int)expiresTimeUnit.toSeconds(expiresAfter), false, true);
	}

	default void clearCookieData() {
		setCookieData(null, 0, false, false);
	}

	void setFNAWSkinsEnabled(boolean enabled, boolean force);

	void setFNAWSkinsEnabled(boolean enabled);

	void resetForcedMulti(boolean resetSkin, boolean resetCape, boolean resetFNAWForce, boolean notifyOtherPlayers);

	void resetForcedSkin(boolean notifyOtherPlayers);

	void resetForcedCape(boolean notifyOtherPlayers);

	void resetForcedFNAW();

	boolean notifSupported();

	void notifIconRegister(Map<UUID, PacketImageData> iconsToRegister);

	void notifIconRegister(UUID iconUUID, PacketImageData imageData);

	void notifIconRelease(Collection<UUID> iconsToRelease);

	void notifIconRelease(UUID iconUUID);

	void notifBadgeShow(CPacketRPCNotifBadgeShow packet);

	default void notifBadgeShow(NotificationBadgeBuilder packet) {
		notifBadgeShow(packet.buildPacket());
	}

	void notifBadgeHide(UUID badgeUUID);

	<T> void setMeta(String key, T value);

	<T> T getMeta(String key);

}
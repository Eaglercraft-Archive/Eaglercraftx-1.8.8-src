/*
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.socket.protocol.client;

import net.lax1dude.eaglercraft.v1_8.ClientUUIDLoadingCache;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.PauseMenuCustomizeState;
import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.skin_cache.ServerTextureCache;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.WrongPacketException;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
import net.lax1dude.eaglercraft.v1_8.webview.ServerInfoCache;
import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;

public class ClientV4MessageHandler extends ClientV3MessageHandler {

	public ClientV4MessageHandler(NetHandlerPlayClient netHandler) {
		super(netHandler);
	}

	public void handleServer(SPacketOtherSkinCustomV3EAG packet) {
		throw new WrongPacketException();
	}

	public void handleServer(SPacketOtherSkinCustomV4EAG packet) {
		netHandler.getTextureCache().handlePacket(packet);
	}

	public void handleServer(SPacketVoiceSignalConnectV3EAG packet) {
		throw new WrongPacketException();
	}

	public void handleServer(SPacketVoiceSignalConnectV4EAG packet) {
		if (VoiceClientController.isClientSupported()) {
			VoiceClientController.handleVoiceSignalPacketTypeConnect(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), packet.offer);
		}
	}

	public void handleServer(SPacketVoiceSignalConnectAnnounceV4EAG packet) {
		if (VoiceClientController.isClientSupported()) {
			VoiceClientController.handleVoiceSignalPacketTypeConnectAnnounce(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
		}
	}

	public void handleServer(SPacketForceClientSkinPresetV4EAG packet) {
		EaglerProfile.handleForceSkinPreset(packet.presetSkin);
	}

	public void handleServer(SPacketForceClientSkinCustomV4EAG packet) {
		EaglerProfile.handleForceSkinCustom(packet.modelID, SkinPacketVersionCache.convertToV3Raw(packet.customSkin));
	}

	public void handleServer(SPacketSetServerCookieV4EAG packet) {
		if(!netHandler.isClientInEaglerSingleplayerOrLAN() && Minecraft.getMinecraft().getCurrentServerData().enableCookies) {
			ServerCookieDataStore.saveCookie(netHandler.getNetworkManager().getAddress(), packet.expires, packet.data,
					packet.revokeQuerySupported, packet.saveCookieToDisk);
		}
	}

	public void handleServer(SPacketRedirectClientV4EAG packet) {
		Minecraft.getMinecraft().handleReconnectPacket(packet.redirectURI);
	}

	public void handleServer(SPacketOtherPlayerClientUUIDV4EAG packet) {
		ClientUUIDLoadingCache.handleResponse(packet.requestId, new EaglercraftUUID(packet.clientUUIDMost, packet.clientUUIDLeast));
	}

	public void handleServer(SPacketForceClientCapePresetV4EAG packet) {
		EaglerProfile.handleForceCapePreset(packet.presetCape);
	}

	public void handleServer(SPacketForceClientCapeCustomV4EAG packet) {
		EaglerProfile.handleForceCapeCustom(packet.customCape);
	}

	public void handleServer(SPacketInvalidatePlayerCacheV4EAG packet) {
		if(packet.players != null && packet.players.size() > 0) {
			ServerTextureCache textureCache = this.netHandler.getTextureCache();
			for(SPacketInvalidatePlayerCacheV4EAG.InvalidateRequest req : packet.players) {
				textureCache.dropPlayer(new EaglercraftUUID(req.uuidMost, req.uuidLeast),
						req.invalidateSkin, req.invalidateCape);
			}
		}
	}

	public void handleServer(SPacketUnforceClientV4EAG packet) {
		if(packet.resetSkin) {
			EaglerProfile.isServerSkinOverride = false;
		}
		if(packet.resetCape) {
			EaglerProfile.isServerCapeOverride = false;
		}
		if(packet.resetFNAW) {
			netHandler.currentFNAWSkinForcedState = false;
			Minecraft.getMinecraft().getRenderManager().setEnableFNAWSkins(
					netHandler.currentFNAWSkinAllowedState && Minecraft.getMinecraft().gameSettings.enableFNAWSkins);
		}
	}

	public void handleServer(SPacketCustomizePauseMenuV4EAG packet) {
		PauseMenuCustomizeState.loadPacket(packet);
	}

	public void handleServer(SPacketServerInfoDataChunkV4EAG packet) {
		ServerInfoCache.handleChunk(packet);
	}

	public void handleServer(SPacketWebViewMessageV4EAG packet) {
		WebViewOverlayController.handleMessagePacket(packet);
	}

	public void handleServer(SPacketNotifIconsRegisterV4EAG packet) {
		netHandler.getNotifManager().processPacketAddIcons(packet);
	}

	public void handleServer(SPacketNotifIconsReleaseV4EAG packet) {
		netHandler.getNotifManager().processPacketRemIcons(packet);
	}

	public void handleServer(SPacketNotifBadgeShowV4EAG packet) {
		netHandler.getNotifManager().processPacketShowBadge(packet);
	}

	public void handleServer(SPacketNotifBadgeHideV4EAG packet) {
		netHandler.getNotifManager().processPacketHideBadge(packet);
	}

}
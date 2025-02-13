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

package net.lax1dude.eaglercraft.v1_8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerSkinTexture;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketCustomizePauseMenuV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class PauseMenuCustomizeState {

	private static final Logger logger = LogManager.getLogger("PauseMenuCustomizeState");

	public static ResourceLocation icon_title_L;
	public static float icon_title_L_aspect = 1.0f;
	public static ResourceLocation icon_title_R;
	public static float icon_title_R_aspect = 1.0f;
	public static ResourceLocation icon_backToGame_L;
	public static float icon_backToGame_L_aspect = 1.0f;
	public static ResourceLocation icon_backToGame_R;
	public static float icon_backToGame_R_aspect = 1.0f;
	public static ResourceLocation icon_achievements_L;
	public static float icon_achievements_L_aspect = 1.0f;
	public static ResourceLocation icon_achievements_R;
	public static float icon_achievements_R_aspect = 1.0f;
	public static ResourceLocation icon_statistics_L;
	public static float icon_statistics_L_aspect = 1.0f;
	public static ResourceLocation icon_statistics_R;
	public static float icon_statistics_R_aspect = 1.0f;
	public static ResourceLocation icon_serverInfo_L;
	public static float icon_serverInfo_L_aspect = 1.0f;
	public static ResourceLocation icon_serverInfo_R;
	public static float icon_serverInfo_R_aspect = 1.0f;
	public static ResourceLocation icon_options_L;
	public static float icon_options_L_aspect = 1.0f;
	public static ResourceLocation icon_options_R;
	public static float icon_options_R_aspect = 1.0f;
	public static ResourceLocation icon_discord_L;
	public static float icon_discord_L_aspect = 1.0f;
	public static ResourceLocation icon_discord_R;
	public static float icon_discord_R_aspect = 1.0f;
	public static ResourceLocation icon_disconnect_L;
	public static float icon_disconnect_L_aspect = 1.0f;
	public static ResourceLocation icon_disconnect_R;
	public static float icon_disconnect_R_aspect = 1.0f;
	public static ResourceLocation icon_background_pause;
	public static float icon_background_pause_aspect = 1.0f;
	public static ResourceLocation icon_background_all;
	public static float icon_background_all_aspect = 1.0f;
	public static ResourceLocation icon_watermark_pause;
	public static float icon_watermark_pause_aspect = 1.0f;
	public static ResourceLocation icon_watermark_all;
	public static float icon_watermark_all_aspect = 1.0f;

	public static final int SERVER_INFO_MODE_NONE = SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_NONE;
	public static final int SERVER_INFO_MODE_EXTERNAL_URL = SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_EXTERNAL_URL;
	public static final int SERVER_INFO_MODE_SHOW_EMBED_OVER_HTTP = SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_HTTP;
	public static final int SERVER_INFO_MODE_SHOW_EMBED_OVER_WS = SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_WS;

	public static final int SERVER_INFO_EMBED_PERMS_JAVASCRIPT = SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_JAVASCRIPT;
	public static final int SERVER_INFO_EMBED_PERMS_MESSAGE_API = SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_MESSAGE_API;
	public static final int SERVER_INFO_EMBED_PERMS_STRICT_CSP = SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_STRICT_CSP;

	public static final int DISCORD_MODE_NONE = SPacketCustomizePauseMenuV4EAG.DISCORD_MODE_NONE;
	public static final int DISCORD_MODE_INVITE_URL = SPacketCustomizePauseMenuV4EAG.DISCORD_MODE_INVITE_URL;

	public static int serverInfoMode;
	public static int serverInfoEmbedPerms = SERVER_INFO_EMBED_PERMS_STRICT_CSP;
	public static String serverInfoEmbedTitle;
	public static String serverInfoButtonText;
	public static String serverInfoURL;
	public static byte[] serverInfoHash;

	public static int discordButtonMode;
	public static String discordButtonText;
	public static String discordInviteURL;

	private static final List<PauseMenuSprite> toFree = new ArrayList<>();

	private static int textureId = 0;

	private static class PauseMenuSprite {
		
		private final ResourceLocation loc;
		private final EaglerSkinTexture tex;
		
		public PauseMenuSprite(EaglerSkinTexture tex) {
			this.loc = newLoc();
			this.tex = tex;
		}
		
	}

	public static void loadPacket(SPacketCustomizePauseMenuV4EAG packet) {
		reset();
		
		serverInfoMode = packet.serverInfoMode;
		switch(packet.serverInfoMode) {
		case SERVER_INFO_MODE_NONE:
		default:
			serverInfoButtonText = null;
			serverInfoURL = null;
			serverInfoHash = null;
			break;
		case SERVER_INFO_MODE_EXTERNAL_URL:
			serverInfoButtonText = packet.serverInfoButtonText;
			serverInfoURL = packet.serverInfoURL;
			break;
		case SERVER_INFO_MODE_SHOW_EMBED_OVER_HTTP:
			serverInfoButtonText = packet.serverInfoButtonText;
			serverInfoEmbedPerms = packet.serverInfoEmbedPerms;
			serverInfoURL = packet.serverInfoURL;
			serverInfoEmbedTitle = packet.serverInfoEmbedTitle;
			break;
		case SERVER_INFO_MODE_SHOW_EMBED_OVER_WS:
			serverInfoButtonText = packet.serverInfoButtonText;
			serverInfoEmbedPerms = packet.serverInfoEmbedPerms;
			serverInfoHash = packet.serverInfoHash;
			serverInfoEmbedTitle = packet.serverInfoEmbedTitle;
			break;
		}
		
		discordButtonMode = packet.discordButtonMode;
		switch(packet.discordButtonMode) {
		case DISCORD_MODE_NONE:
		default:
			discordButtonText = null;
			serverInfoURL = null;
			serverInfoHash = null;
			break;
		case DISCORD_MODE_INVITE_URL:
			discordButtonText = packet.discordButtonText;
			discordInviteURL = packet.discordInviteURL;
			break;
		}
		
		if(packet.imageMappings != null) {
			Map<Integer, PauseMenuSprite> spriteCache = new HashMap<>();
			icon_title_L = cacheLoadHelperFunction("icon_title_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_title_L_aspect = a);
			icon_title_R = cacheLoadHelperFunction("icon_title_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_title_R_aspect = a);
			icon_backToGame_L = cacheLoadHelperFunction("icon_backToGame_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_backToGame_L_aspect = a);
			icon_backToGame_R = cacheLoadHelperFunction("icon_backToGame_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_backToGame_R_aspect = a);
			icon_achievements_L = cacheLoadHelperFunction("icon_achievements_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_achievements_L_aspect = a);
			icon_achievements_R = cacheLoadHelperFunction("icon_achievements_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_achievements_R_aspect = a);
			icon_statistics_L = cacheLoadHelperFunction("icon_statistics_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_statistics_L_aspect = a);
			icon_statistics_R = cacheLoadHelperFunction("icon_statistics_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_statistics_R_aspect = a);
			icon_serverInfo_L = cacheLoadHelperFunction("icon_serverInfo_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_serverInfo_L_aspect = a);
			icon_serverInfo_R = cacheLoadHelperFunction("icon_serverInfo_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_serverInfo_R_aspect = a);
			icon_options_L = cacheLoadHelperFunction("icon_options_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_options_L_aspect = a);
			icon_options_R = cacheLoadHelperFunction("icon_options_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_options_R_aspect = a);
			icon_discord_L = cacheLoadHelperFunction("icon_discord_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_discord_L_aspect = a);
			icon_discord_R = cacheLoadHelperFunction("icon_discord_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_discord_R_aspect = a);
			icon_disconnect_L = cacheLoadHelperFunction("icon_disconnect_L", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_disconnect_L_aspect = a);
			icon_disconnect_R = cacheLoadHelperFunction("icon_disconnect_R", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_disconnect_R_aspect = a);
			icon_background_pause = cacheLoadHelperFunction("icon_background_pause", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_background_pause_aspect = a);
			icon_background_all = cacheLoadHelperFunction("icon_background_all", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_background_all_aspect = a);
			icon_watermark_pause = cacheLoadHelperFunction("icon_watermark_pause", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_watermark_pause_aspect = a);
			icon_watermark_all = cacheLoadHelperFunction("icon_watermark_all", packet.imageMappings, spriteCache, packet.imageData, (a) -> icon_watermark_all_aspect = a);
		}
	}

	private static ResourceLocation cacheLoadHelperFunction(String name, Map<String, Integer> lookup,
			Map<Integer, PauseMenuSprite> spriteCache,
			List<PacketImageData> sourceData, Consumer<Float> aspectCB) {
		Integer i = lookup.get(name);
		if(i == null) {
			return null;
		}
		PauseMenuSprite ret = spriteCache.get(i);
		if(ret != null) {
			if(name.startsWith("icon_background_") && ImageData.isNPOTStatic(ret.tex.getWidth(), ret.tex.getHeight())) {
				logger.warn("An NPOT (non-power-of-two) texture was used for \"{}\", this texture's width and height must be powers of two for this texture to display properly on all hardware");
			}
			aspectCB.accept((float)ret.tex.getWidth() / ret.tex.getHeight());
			return ret.loc;
		}
		int ii = i.intValue();
		if(ii < 0 || ii >= sourceData.size()) {
			return null;
		}
		PacketImageData data = sourceData.get(ii);
		ret = new PauseMenuSprite(new EaglerSkinTexture(ImageData.swapRB(data.rgba), data.width, data.height));
		Minecraft.getMinecraft().getTextureManager().loadTexture(ret.loc, ret.tex);
		spriteCache.put(i, ret);
		toFree.add(ret);
		aspectCB.accept((float)data.width / data.height);
		return ret.loc;
	}

	private static ResourceLocation newLoc() {
		return new ResourceLocation("eagler:gui/server/custom_pause_menu/tex_" + textureId++);
	}

	public static void reset() {
		icon_title_L = icon_title_R = null;
		icon_backToGame_L = icon_backToGame_R = null;
		icon_achievements_L = icon_achievements_R = null;
		icon_statistics_L = icon_statistics_R = null;
		icon_serverInfo_L = icon_serverInfo_R = null;
		icon_options_L = icon_options_R = null;
		icon_discord_L = icon_discord_R = null;
		icon_disconnect_L = icon_disconnect_R = null;
		icon_background_pause = icon_background_all = null;
		icon_watermark_pause = icon_watermark_all = null;
		icon_title_L_aspect = icon_title_R_aspect = 1.0f;
		icon_backToGame_L_aspect = icon_backToGame_R_aspect = 1.0f;
		icon_achievements_L_aspect = icon_achievements_R_aspect = 1.0f;
		icon_statistics_L_aspect = icon_statistics_R_aspect = 1.0f;
		icon_serverInfo_L_aspect = icon_serverInfo_R_aspect = 1.0f;
		icon_options_L_aspect = icon_options_R_aspect = 1.0f;
		icon_discord_L_aspect = icon_discord_R_aspect = 1.0f;
		icon_disconnect_L_aspect = icon_disconnect_R_aspect = 1.0f;
		icon_background_pause_aspect = icon_background_all_aspect = 1.0f;
		icon_watermark_pause_aspect = icon_watermark_all_aspect = 1.0f;
		serverInfoMode = 0;
		serverInfoEmbedPerms = SERVER_INFO_EMBED_PERMS_STRICT_CSP;
		serverInfoButtonText = null;
		serverInfoURL = null;
		serverInfoHash = null;
		serverInfoEmbedTitle = null;
		discordButtonMode = 0;
		discordButtonText = null;
		discordInviteURL = null;
		if(!toFree.isEmpty()) {
			TextureManager mgr = Minecraft.getMinecraft().getTextureManager();
			for(PauseMenuSprite rc : toFree) {
				mgr.deleteTexture(rc.loc);
			}
			toFree.clear();
		}
	}

}
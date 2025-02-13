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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.bungee.Configuration;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketCustomizePauseMenuV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketServerInfoDataChunkV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SimpleOutputBufferImpl;

public class EaglerPauseMenuConfig {

	private boolean enableCustomPauseMenu;
	private SPacketCustomizePauseMenuV4EAG customPauseMenuPacket;
	private byte[] serverInfoHash;
	private List<SPacketServerInfoDataChunkV4EAG> serverInfoChunks;
	private int infoSendRate;

	static EaglerPauseMenuConfig loadConfig(Configuration conf, File baseDir) throws IOException {
		boolean enabled = conf.getBoolean("enable_custom_pause_menu", false);
		if(!enabled) {
			return new EaglerPauseMenuConfig(false, null, null, 1);
		}

		Configuration server_info_button = conf.getSection("server_info_button");
		boolean enableInfoButton = server_info_button.getBoolean("enable_button", false);
		String infoButtonText = server_info_button.getString("button_text", "Server Info");
		boolean infoButtonModeNewTab = server_info_button.getBoolean("button_mode_open_new_tab", false);
		String infoButtonEmbedURL = server_info_button.getString("server_info_embed_url", "");
		boolean infoButtonModeEmbedFile = server_info_button.getBoolean("button_mode_embed_file", true);
		String infoButtonEmbedFile = server_info_button.getString("server_info_embed_file", "server_info.html");
		String infoButtonEmbedScreenTitle = server_info_button.getString("server_info_embed_screen_title", "Server Info");
		int infoSendRate = server_info_button.getInt("server_info_embed_send_chunk_rate", 1);
		int infoChunkSize = server_info_button.getInt("server_info_embed_send_chunk_size", 24576);
		if(infoChunkSize > 32720) {
			throw new IOException("Chunk size " +infoChunkSize + " is too large! Max is 32720 bytes");
		}
		boolean infoButtonEnableTemplateMacros = server_info_button.getBoolean("enable_template_macros", true);
		Configuration globals = server_info_button.getSection("server_info_embed_template_globals");
		for(String s : globals.getKeys()) {
			EaglerXVelocityAPIHelper.getTemplateGlobals().put(s, globals.getString(s));
		}
		boolean infoButtonAllowTemplateEvalMacro = server_info_button.getBoolean("allow_embed_template_eval_macro", false);
		boolean infoButtonEnableWebviewJavascript = server_info_button.getBoolean("enable_webview_javascript", false);
		boolean infoButtonEnableWebviewMessageAPI = server_info_button.getBoolean("enable_webview_message_api", false);
		boolean infoButtonEnableWebviewStrictCSP = server_info_button.getBoolean("enable_webview_strict_csp", true);

		Configuration discord_button = conf.getSection("discord_button");
		boolean enableDiscordButton = discord_button.getBoolean("enable_button", false);
		String discordButtonText = discord_button.getString("button_text", "Discord");
		String discordButtonURL = discord_button.getString("button_url", "https://invite url here");

		int infoButtonMode = enableInfoButton
				? (infoButtonModeEmbedFile
						? (infoButtonEmbedFile.length() > 0
								? SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_WS
								: SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_NONE)
						: (infoButtonEmbedURL.length() > 0
								? (infoButtonModeNewTab ? SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_EXTERNAL_URL
										: SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_HTTP)
								: SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_NONE))
				: SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_NONE;

		int discordButtonMode = (enableDiscordButton && discordButtonURL.length() > 0)
				? SPacketCustomizePauseMenuV4EAG.DISCORD_MODE_INVITE_URL
				: SPacketCustomizePauseMenuV4EAG.DISCORD_MODE_NONE;

		int webviewPerms = (infoButtonEnableWebviewJavascript ? SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_JAVASCRIPT : 0) |
				(infoButtonEnableWebviewMessageAPI ? SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_MESSAGE_API : 0) |
				(infoButtonEnableWebviewStrictCSP ? SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_STRICT_CSP : 0);

		Map<String,String> imagesToActuallyLoad = new WeakHashMap<>();

		Configuration custom_images = conf.getSection("custom_images");
		for(String s : custom_images.getKeys()) {
			String fileName = custom_images.getString(s, "");
			if(fileName.length() > 0) {
				imagesToActuallyLoad.put(s, fileName);
			}
		}

		Map<String,Integer> imageMappings = null;
		List<PacketImageData> customImageDatas = null;

		if(imagesToActuallyLoad.size() > 0) {
			Map<String,PacketImageData> imageLoadingCache = new HashMap<>();
			Int2ObjectMap<PacketImageData> imageDumbHashTable = new Int2ObjectOpenHashMap<>();

			imageMappings = new HashMap<>();
			customImageDatas = new ArrayList<>();

			outer_loop: for(Entry<String,String> etr : imagesToActuallyLoad.entrySet()) {
				String key = etr.getKey();
				String value = etr.getValue();
				PacketImageData existing = imageLoadingCache.get(value);
				if(existing != null) {
					for(int i = 0, l = customImageDatas.size(); i < l; ++i) {
						if(customImageDatas.get(i) == existing) {
							imageMappings.put(key, i);
							continue outer_loop;
						}
					}
					imageMappings.put(key, customImageDatas.size());
					customImageDatas.add(existing);
					continue outer_loop;
				}else {
					PacketImageData img = EaglerXVelocityAPIHelper.loadPacketImageData(new File(baseDir, value), 64, 64);
					int hashCode = Arrays.hashCode(img.rgba);
					PacketImageData possibleClone = imageDumbHashTable.get(hashCode);
					if (possibleClone != null && possibleClone.width == img.width && possibleClone.height == img.height
							&& Arrays.equals(img.rgba, possibleClone.rgba)) {
						for(int i = 0, l = customImageDatas.size(); i < l; ++i) {
							if(customImageDatas.get(i) == possibleClone) {
								imageMappings.put(key, i);
								continue outer_loop;
							}
						}
						imageMappings.put(key, customImageDatas.size());
						customImageDatas.add(possibleClone);
						continue outer_loop;
					}else {
						imageMappings.put(key, customImageDatas.size());
						customImageDatas.add(img);
						imageDumbHashTable.put(hashCode, img);
						continue outer_loop;
					}
				}
			}
		}

		SPacketCustomizePauseMenuV4EAG pausePacket = new SPacketCustomizePauseMenuV4EAG();
		List<SPacketServerInfoDataChunkV4EAG> serverInfoChunks = null;

		pausePacket.serverInfoMode = infoButtonMode;
		switch(infoButtonMode) {
		case SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_NONE:
		default:
			break;
		case SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_EXTERNAL_URL:
			pausePacket.serverInfoButtonText = infoButtonText;
			pausePacket.serverInfoURL = infoButtonEmbedURL;
			break;
		case SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_HTTP:
			pausePacket.serverInfoButtonText = infoButtonText;
			pausePacket.serverInfoURL = infoButtonEmbedURL;
			pausePacket.serverInfoEmbedPerms = webviewPerms;
			pausePacket.serverInfoEmbedTitle = infoButtonEmbedScreenTitle;
			break;
		case SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_WS:
			pausePacket.serverInfoButtonText = infoButtonText;
			byte[] hash = new byte[20];
			String rawData = EaglerXVelocityAPIHelper.loadFileToStringServerInfo(new File(baseDir, infoButtonEmbedFile));
			if(infoButtonEnableTemplateMacros) {
				rawData = EaglerXVelocityAPIHelper.loadServerInfoTemplateEagler(rawData, baseDir, infoButtonAllowTemplateEvalMacro);
			}
			serverInfoChunks = EaglerXVelocityAPIHelper.convertServerInfoToChunks(rawData.getBytes(StandardCharsets.UTF_8), hash, infoChunkSize);
			if(!serverInfoChunks.isEmpty()) {
				SPacketServerInfoDataChunkV4EAG pk = serverInfoChunks.get(0);
				EaglerXVelocity.logger().info("Total server info embed size: {} bytes {}", pk.finalSize, serverInfoChunks.size() > 1 ? (" (" + serverInfoChunks.size() + " chunks)") : "");
			}
			pausePacket.serverInfoEmbedPerms = webviewPerms;
			pausePacket.serverInfoEmbedTitle = infoButtonEmbedScreenTitle;
			pausePacket.serverInfoHash = hash;
			break;
		}

		pausePacket.discordButtonMode = discordButtonMode;
		switch(discordButtonMode) {
		case SPacketCustomizePauseMenuV4EAG.DISCORD_MODE_NONE:
		default:
			break;
		case SPacketCustomizePauseMenuV4EAG.DISCORD_MODE_INVITE_URL:
			pausePacket.discordButtonMode = discordButtonMode;
			pausePacket.discordButtonText = discordButtonText;
			pausePacket.discordInviteURL = discordButtonURL;
			break;
		}

		pausePacket.imageMappings = imageMappings;
		pausePacket.imageData = customImageDatas;

		SimpleOutputBufferImpl ob = new SimpleOutputBufferImpl(new TestOutputStream());
		pausePacket.writePacket(ob);
		int cnt = ob.size();
		
		EaglerXVelocity.logger().info("Total pause menu packet size: {} bytes", cnt);
		if(cnt > 32760) {
			throw new IOException("Pause menu packet is " + (cnt - 32760) + " bytes too large! Try making the images smaller or reusing the same image file for multiple icons!");
		}

		return new EaglerPauseMenuConfig(enabled, pausePacket, serverInfoChunks, infoSendRate);
	}

	private EaglerPauseMenuConfig(boolean enableCustomPauseMenu, SPacketCustomizePauseMenuV4EAG customPauseMenuPacket,
			List<SPacketServerInfoDataChunkV4EAG> serverInfoChunks, int infoSendRate) {
		this.enableCustomPauseMenu = enableCustomPauseMenu;
		this.customPauseMenuPacket = customPauseMenuPacket;
		this.serverInfoHash = customPauseMenuPacket != null ? customPauseMenuPacket.serverInfoHash : null;
		this.serverInfoChunks = serverInfoChunks;
		this.infoSendRate = infoSendRate;
	}

	public boolean getEnabled() {
		return enableCustomPauseMenu;
	}

	public SPacketCustomizePauseMenuV4EAG getPacket() {
		return customPauseMenuPacket;
	}

	public byte[] getServerInfoHash() {
		return serverInfoHash;
	}

	public List<SPacketServerInfoDataChunkV4EAG> getServerInfo() {
		return serverInfoChunks;
	}

	public int getInfoSendRate() {
		return infoSendRate;
	}

}
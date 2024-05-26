package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.handlers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.GameProfile.Property;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import net.kyori.adventure.text.Component;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.Base64;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.CapePackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.CapeServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinPackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice.VoiceSignalPackets;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EaglerPacketEventListener {

	public static final ChannelIdentifier FNAW_SKIN_ENABLE_CHANNEL = new LegacyChannelIdentifier("EAG|FNAWSEn-1.8");
	public static final ChannelIdentifier GET_DOMAIN_CHANNEL = new LegacyChannelIdentifier("EAG|GetDomain");
	
	public final EaglerXVelocity plugin;
	
	public EaglerPacketEventListener(EaglerXVelocity plugin) {
		this.plugin = plugin;
	}

	@Subscribe(order = PostOrder.FIRST)
	public void onPluginMessage(final PluginMessageEvent event) {
		if(event.getSource() instanceof ConnectedPlayer) {
			final ConnectedPlayer player = (ConnectedPlayer)event.getSource();
			EaglerPlayerData eagPlayer = EaglerPipeline.getEaglerHandle(player);
			if(eagPlayer != null) {
				if(SkinService.CHANNEL.equals(event.getIdentifier())) {
					EaglerXVelocity.proxy().getScheduler().buildTask(plugin, new Runnable() {
						@Override
						public void run() {
							try {
								SkinPackets.processPacket(event.getData(), player, plugin.getSkinService());
							} catch (IOException e) {
								player.disconnect(Component.text("Skin packet error!"));
								EaglerXVelocity.logger().error("Eagler user \"{}\" raised an exception handling skins!", player.getUsername(), e);
							}
						}
					}).schedule();
				}else if(CapeServiceOffline.CHANNEL.equals(event.getIdentifier())) {
					try {
						CapePackets.processPacket(event.getData(), player, plugin.getCapeService());
					} catch (IOException e) {
						player.disconnect(Component.text("Cape packet error!"));
						EaglerXVelocity.logger().error("Eagler user \"{}\" raised an exception handling capes!", player.getUsername(), e);
					}
				}else if(VoiceService.CHANNEL.equals(event.getIdentifier())) {
					VoiceService svc = plugin.getVoiceService();
					if(svc != null && eagPlayer.getEaglerListenerConfig().getEnableVoiceChat()) {
						try {
							VoiceSignalPackets.processPacket(event.getData(), player, svc);
						} catch (IOException e) {
							player.disconnect(Component.text("Voice signal packet error!"));
							EaglerXVelocity.logger().error("Eagler user \"{}\" raised an exception handling voice signals!", player.getUsername(), e);
						}
					}
				}
			}
		}else if(event.getSource() instanceof ServerConnection && event.getTarget() instanceof ConnectedPlayer) {
			ConnectedPlayer player = (ConnectedPlayer)event.getTarget();
			if(GET_DOMAIN_CHANNEL.equals(event.getIdentifier())) {
				EaglerPlayerData eagPlayerData = EaglerPipeline.getEaglerHandle(player);
				if(eagPlayerData != null) {
					String domain = eagPlayerData.getOrigin();
					if(domain == null) {
						((ServerConnection)event.getSource()).sendPluginMessage(GET_DOMAIN_CHANNEL, new byte[] { 0 });
					}else {
						((ServerConnection)event.getSource()).sendPluginMessage(GET_DOMAIN_CHANNEL, domain.getBytes(StandardCharsets.UTF_8));
					}
				}
			}
		}
	}

	@Subscribe
	public void onPostLogin(PostLoginEvent event) {
		Player p = event.getPlayer();
		if(p instanceof ConnectedPlayer) {
			ConnectedPlayer player = (ConnectedPlayer)p;
			GameProfile res = player.getGameProfile();
			if(res != null) {
				List<Property> props = res.getProperties();
				if(props.size() > 0) {
					for(int i = 0, l = props.size(); i < l; ++i) {
						Property pp = props.get(i);
						if(pp.getName().equals("textures")) {
							try {
								String jsonStr = SkinPackets.bytesToAscii(Base64.decodeBase64(pp.getValue()));
								JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
								JsonObject skinObj = json.getAsJsonObject("SKIN");
								if(skinObj != null) {
									JsonElement url = json.get("url");
									if(url != null) {
										String urlStr = SkinService.sanitizeTextureURL(url.getAsString());
										plugin.getSkinService().registerTextureToPlayerAssociation(urlStr, player.getUniqueId());
									}
								}
							}catch(Throwable t) {
							}
						}
					}
				}
			}
			EaglerAuthConfig authConf = plugin.getConfig().getAuthConfig();
			if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
				DefaultAuthSystem srv = plugin.getAuthService();
				if(srv != null) {
					srv.handleVanillaLogin(event);
				}
			}
		}
	}

	@Subscribe
	public void onConnectionLost(DisconnectEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		plugin.getSkinService().unregisterPlayer(uuid);
		plugin.getCapeService().unregisterPlayer(uuid);
		if(event.getPlayer() instanceof ConnectedPlayer) {
			ConnectedPlayer player = (ConnectedPlayer)event.getPlayer();
			EaglerPlayerData eagData = EaglerPipeline.getEaglerHandle(player);
			if(eagData != null && eagData.getEaglerListenerConfig().getEnableVoiceChat()) {
				plugin.getVoiceService().handlePlayerLoggedOut(player);
			}
		}
	}

	@Subscribe
	public void onServerConnected(ServerConnectedEvent event) {
		if(event.getPlayer() instanceof ConnectedPlayer) {
			ConnectedPlayer player = (ConnectedPlayer)event.getPlayer();
			EaglerPlayerData eagData = EaglerPipeline.getEaglerHandle(player);
			if(eagData != null) {
				ServerInfo sv = event.getServer().getServerInfo();
				boolean fnawSkins = !plugin.getConfig().getDisableFNAWSkinsEverywhere()
						&& !plugin.getConfig().getDisableFNAWSkinsOnServersSet().contains(sv.getName());
				if(fnawSkins != eagData.currentFNAWSkinEnableStatus) {
					eagData.currentFNAWSkinEnableStatus = fnawSkins;
					player.sendPluginMessage(FNAW_SKIN_ENABLE_CHANNEL, new byte[] { fnawSkins ? (byte)1 : (byte)0 });
				}
				if(eagData.getEaglerListenerConfig().getEnableVoiceChat()) {
					plugin.getVoiceService().handleServerConnected(player, sv);
				}
			}
		}
	}

	@Subscribe
	public void onServerDisconnected(ServerPreConnectEvent event) {
		if(event.getPreviousServer() != null && (event.getPlayer() instanceof ConnectedPlayer)) {
			ConnectedPlayer player = (ConnectedPlayer)event.getPlayer();
			EaglerPlayerData eagData = EaglerPipeline.getEaglerHandle(player);
			if(eagData != null && eagData.getEaglerListenerConfig().getEnableVoiceChat()) {
				plugin.getVoiceService().handleServerDisconnected(player, event.getPreviousServer().getServerInfo());
			}
		}
	}
}

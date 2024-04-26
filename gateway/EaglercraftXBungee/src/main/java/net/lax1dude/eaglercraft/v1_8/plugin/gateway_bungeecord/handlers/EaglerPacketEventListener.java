package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.handlers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.CapePackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.CapeServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinPackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice.VoiceSignalPackets;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Property;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class EaglerPacketEventListener implements Listener {

	public static final String FNAW_SKIN_ENABLE_CHANNEL = "EAG|FNAWSEn-1.8";
	public static final String GET_DOMAIN_CHANNEL = "EAG|GetDomain";
	
	public final EaglerXBungee plugin;
	
	public EaglerPacketEventListener(EaglerXBungee plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPluginMessage(final PluginMessageEvent event) {
		if(event.getSender() instanceof UserConnection) {
			final UserConnection player = (UserConnection)event.getSender();
			if(player.getPendingConnection() instanceof EaglerInitialHandler) {
				if(SkinService.CHANNEL.equals(event.getTag())) {
					event.setCancelled(true);
					ProxyServer.getInstance().getScheduler().runAsync(plugin, new Runnable() {
						@Override
						public void run() {
							try {
								SkinPackets.processPacket(event.getData(), player, plugin.getSkinService());
							} catch (IOException e) {
								event.getSender().disconnect(new TextComponent("Skin packet error!"));
								EaglerXBungee.logger().log(Level.SEVERE, "Eagler user \"" + player.getName() + "\" raised an exception handling skins!", e);
							}
						}
					});
				}else if(CapeServiceOffline.CHANNEL.equals(event.getTag())) {
					event.setCancelled(true);
					try {
						CapePackets.processPacket(event.getData(), player, plugin.getCapeService());
					} catch (IOException e) {
						event.getSender().disconnect(new TextComponent("Cape packet error!"));
						EaglerXBungee.logger().log(Level.SEVERE, "Eagler user \"" + player.getName() + "\" raised an exception handling capes!", e);
					}
				}else if(VoiceService.CHANNEL.equals(event.getTag())) {
					event.setCancelled(true);
					VoiceService svc = plugin.getVoiceService();
					if(svc != null && ((EaglerInitialHandler)player.getPendingConnection()).getEaglerListenerConfig().getEnableVoiceChat()) {
						try {
							VoiceSignalPackets.processPacket(event.getData(), player, svc);
						} catch (IOException e) {
							event.getSender().disconnect(new TextComponent("Voice signal packet error!"));
							EaglerXBungee.logger().log(Level.SEVERE, "Eagler user \"" + player.getName() + "\" raised an exception handling voice signals!", e);
						}
					}
				}
			}
		}else if(event.getSender() instanceof Server && event.getReceiver() instanceof UserConnection) {
			UserConnection player = (UserConnection)event.getReceiver();
			if(GET_DOMAIN_CHANNEL.equals(event.getTag()) && player.getPendingConnection() instanceof EaglerInitialHandler) {
				event.setCancelled(true);
				String domain = ((EaglerInitialHandler)player.getPendingConnection()).getOrigin();
				if(domain == null) {
					((Server)event.getSender()).sendData("EAG|Domain", new byte[] { 0 });
				}else {
					((Server)event.getSender()).sendData("EAG|Domain", domain.getBytes(StandardCharsets.UTF_8));
				}
			}
		}
	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		ProxiedPlayer p = event.getPlayer();
		if(p instanceof UserConnection) {
			UserConnection player = (UserConnection)p;
			InitialHandler handler = player.getPendingConnection();
			LoginResult res = handler.getLoginProfile();
			if(res != null) {
				Property[] props = res.getProperties();
				if(props.length > 0) {
					for(int i = 0; i < props.length; ++i) {
						Property pp = props[i];
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

	@EventHandler
	public void onConnectionLost(PlayerDisconnectEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		plugin.getSkinService().unregisterPlayer(uuid);
		plugin.getCapeService().unregisterPlayer(uuid);
		if(event.getPlayer() instanceof UserConnection) {
			UserConnection player = (UserConnection)event.getPlayer();
			if((player.getPendingConnection() instanceof EaglerInitialHandler)
					&& ((EaglerInitialHandler) player.getPendingConnection()).getEaglerListenerConfig()
							.getEnableVoiceChat()) {
				plugin.getVoiceService().handlePlayerLoggedOut(player);
			}
		}
	}

	@EventHandler
	public void onServerConnected(ServerConnectedEvent event) {
		if(event.getPlayer() instanceof UserConnection) {
			UserConnection player = (UserConnection)event.getPlayer();
			if(player.getPendingConnection() instanceof EaglerInitialHandler) {
				EaglerInitialHandler handler = (EaglerInitialHandler) player.getPendingConnection();
				ServerInfo sv = event.getServer().getInfo();
				boolean fnawSkins = !plugin.getConfig().getDisableFNAWSkinsEverywhere() && !plugin.getConfig().getDisableFNAWSkinsOnServersSet().contains(sv.getName());
				if(fnawSkins != handler.currentFNAWSkinEnableStatus) {
					handler.currentFNAWSkinEnableStatus = fnawSkins;
					player.sendData(FNAW_SKIN_ENABLE_CHANNEL, new byte[] { fnawSkins ? (byte)1 : (byte)0 });
				}
				if(handler.getEaglerListenerConfig().getEnableVoiceChat()) {
					plugin.getVoiceService().handleServerConnected(player, sv);
				}
			}
		}
	}

	@EventHandler
	public void onServerDisconnected(ServerDisconnectEvent event) {
		if(event.getPlayer() instanceof UserConnection) {
			UserConnection player = (UserConnection)event.getPlayer();
			if((player.getPendingConnection() instanceof EaglerInitialHandler)
					&& ((EaglerInitialHandler) player.getPendingConnection()).getEaglerListenerConfig()
							.getEnableVoiceChat()) {
				plugin.getVoiceService().handleServerDisconnected(player, event.getTarget());
			}
		}
	}
}

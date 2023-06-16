package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.handlers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinPackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinService;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Property;

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
public class EaglerPacketEventListener implements Listener {
	
	public final EaglerXBungee plugin;
	
	public EaglerPacketEventListener(EaglerXBungee plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPluginMessage(final PluginMessageEvent event) {
		if(event.getSender() instanceof UserConnection) {
			if(SkinService.CHANNEL.equals(event.getTag())) {
				event.setCancelled(true);
				final UserConnection sender = (UserConnection)event.getSender();
				if(sender.getPendingConnection() instanceof EaglerInitialHandler) {
					ProxyServer.getInstance().getScheduler().runAsync(plugin, new Runnable() {
						@Override
						public void run() {
							try {
								SkinPackets.processPacket(event.getData(), sender, plugin.getSkinService());
							} catch (IOException e) {
								event.getSender().disconnect(new TextComponent("Skin packet error!"));
								EaglerXBungee.logger().log(Level.SEVERE, "Eagler user \"" + sender.getName() + "\" raised an exception handling skins!", e);
							}
						}
					});
				}else {
					event.getSender().disconnect(new TextComponent("Cannot send \"" + SkinService.CHANNEL + "\" on a non-eagler connection!"));
				}
			}
		}else if(event.getSender() instanceof Server && event.getReceiver() instanceof UserConnection) {
			UserConnection player = (UserConnection)event.getReceiver();
			if("EAG|GetDomain".equals(event.getTag()) && player.getPendingConnection() instanceof EaglerInitialHandler) {
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
	@SuppressWarnings("deprecation")
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
								JsonObject json = (new JsonParser()).parse(jsonStr).getAsJsonObject();
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
			EaglerAuthConfig authConf = EaglerXBungee.getEagler().getConfig().getAuthConfig();
			if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
				DefaultAuthSystem srv = EaglerXBungee.getEagler().getAuthService();
				if(srv != null) {
					srv.handleVanillaLogin(event);
				}
			}
		}
	}

	@EventHandler
	public void onConnectionLost(PlayerDisconnectEvent event) {
		plugin.getSkinService().unregisterPlayer(event.getPlayer().getUniqueId());
	}
}

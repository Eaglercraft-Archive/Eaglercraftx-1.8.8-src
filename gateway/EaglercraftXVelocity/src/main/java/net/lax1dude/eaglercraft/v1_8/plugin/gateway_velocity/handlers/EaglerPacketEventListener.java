/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.handlers;

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
import com.velocitypowered.api.event.connection.PluginMessageEvent.ForwardResult;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.GameProfile.Property;
import com.velocitypowered.proxy.connection.backend.VelocityServerConnection;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import net.kyori.adventure.text.Component;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol.BackendRPCSessionHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.protocol.GameProtocolMessageController;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.Base64;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinPackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketEnableFNAWSkinsEAG;

public class EaglerPacketEventListener {

	public static final ChannelIdentifier GET_DOMAIN_CHANNEL = new LegacyChannelIdentifier("EAG|GetDomain");
	
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	
	public final EaglerXVelocity plugin;
	
	public EaglerPacketEventListener(EaglerXVelocity plugin) {
		this.plugin = plugin;
	}

	@Subscribe(order = PostOrder.FIRST)
	public void onPluginMessage(final PluginMessageEvent event) {
		ChannelIdentifier tagObj = event.getIdentifier();
		String tag = tagObj.getId();
		if(event.getSource() instanceof ConnectedPlayer) {
			final ConnectedPlayer player = (ConnectedPlayer)event.getSource();
			EaglerPlayerData eagPlayer = EaglerPipeline.getEaglerHandle(player);
			if(eagPlayer != null) {
				GameProtocolMessageController msgController = eagPlayer.getEaglerMessageController();
				if(msgController != null) {
					try {
						if(msgController.handlePacket(tag, event.getData())) {
							event.setResult(ForwardResult.handled());
							return;
						}
					} catch (Throwable e) {
						player.disconnect(Component.text("Eaglercraft packet error!"));
						event.setResult(ForwardResult.handled());
						return;
					}
				}
				if(EaglerBackendRPCProtocol.CHANNEL_NAME.equals(tag) || EaglerBackendRPCProtocol.CHANNEL_NAME_MODERN.equals(tag)) {
					player.disconnect(Component.text("Nope!"));
					event.setResult(ForwardResult.handled());
					return;
				}
				if(EaglerBackendRPCProtocol.CHANNEL_NAME_READY.equals(tag) || EaglerBackendRPCProtocol.CHANNEL_NAME_READY_MODERN.equals(tag)) {
					event.setResult(ForwardResult.handled());
					return;
				}
			}
		}else if(event.getSource() instanceof ServerConnection && event.getTarget() instanceof ConnectedPlayer) {
			ConnectedPlayer player = (ConnectedPlayer)event.getTarget();
			EaglerPlayerData eagPlayerData = EaglerPipeline.getEaglerHandle(player);
			if(eagPlayerData != null) {
				if(EaglerBackendRPCProtocol.CHANNEL_NAME.equals(tag) || EaglerBackendRPCProtocol.CHANNEL_NAME_MODERN.equals(tag)) {
					event.setResult(ForwardResult.handled());
					try {
						eagPlayerData.handleBackendRPCPacket((ServerConnection)event.getSource(), event.getData());
					}catch(Throwable t) {
						EaglerXVelocity.logger().error("[{}]: Caught an exception handling backend RPC packet!", player.getUsername(), t);
					}
				}else if("EAG|GetDomain".equals(tag)) {
					event.setResult(ForwardResult.handled());
					String domain = eagPlayerData.getOrigin();
					if(domain == null) {
						((ServerConnection)event.getSource()).sendPluginMessage(GET_DOMAIN_CHANNEL, new byte[] { 0 });
					}else {
						((ServerConnection)event.getSource()).sendPluginMessage(GET_DOMAIN_CHANNEL, domain.getBytes(StandardCharsets.UTF_8));
					}
				}
			}else {
				if(EaglerBackendRPCProtocol.CHANNEL_NAME.equals(tag) || EaglerBackendRPCProtocol.CHANNEL_NAME_MODERN.equals(tag)) {
					event.setResult(ForwardResult.handled());
					try {
						BackendRPCSessionHandler.handlePacketOnVanilla((ServerConnection)event.getSource(), player, event.getData());
					}catch(Throwable t) {
						EaglerXVelocity.logger().error("[{}]: Caught an exception handling backend RPC packet!", player.getUsername(), t);
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
				plugin.getVoiceService().handlePlayerLoggedOut(eagData);
			}
		}
	}

	@Subscribe
	public void onServerConnected(ServerPostConnectEvent event) {
		try {
			ConnectedPlayer player = (ConnectedPlayer) event.getPlayer();
			ServerConnection server = player.getConnectedServer();
			BackendRPCSessionHandler.sendPluginMessage(server,
					BackendRPCSessionHandler.getReadyChNameFor((VelocityServerConnection) server), EMPTY_BYTE_ARRAY);
			EaglerPlayerData playerObj = EaglerPipeline.getEaglerHandle(player);
			if(playerObj != null) {
				ServerInfo sv = server.getServerInfo();
				boolean fnawSkins = !plugin.getConfig().getDisableFNAWSkinsEverywhere()
						&& !plugin.getConfig().getDisableFNAWSkinsOnServersSet().contains(sv.getName());
				if(fnawSkins != playerObj.currentFNAWSkinEnableStatus.getAndSet(fnawSkins)) {
					playerObj.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(fnawSkins, false));
				}
				if(playerObj.getEaglerListenerConfig().getEnableVoiceChat()) {
					plugin.getVoiceService().handleServerConnected(playerObj, sv);
				}
			}
		}catch(Throwable t) {
			EaglerXVelocity.logger().error("Failed to process server connection ready handler for player \"{}\"",
					event.getPlayer().getUsername(), t);
		}
	}

	@Subscribe
	public void onServerDisconnected(ServerPreConnectEvent event) {
		if(event.getPreviousServer() != null && (event.getPlayer() instanceof ConnectedPlayer)) {
			ConnectedPlayer player = (ConnectedPlayer)event.getPlayer();
			EaglerPlayerData eagData = EaglerPipeline.getEaglerHandle(player);
			if(eagData != null) {
				BackendRPCSessionHandler rpcHandler = eagData.getRPCSessionHandler();
				if(rpcHandler != null) {
					rpcHandler.handleConnectionLost();
				}
				if(eagData.getEaglerListenerConfig().getEnableVoiceChat()) {
					plugin.getVoiceService().handleServerDisconnected(eagData, event.getPreviousServer().getServerInfo());
				}
			}
		}
	}

}
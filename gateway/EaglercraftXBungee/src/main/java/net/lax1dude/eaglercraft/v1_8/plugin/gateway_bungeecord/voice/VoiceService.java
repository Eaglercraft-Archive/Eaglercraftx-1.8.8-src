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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import gnu.trove.map.TMap;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventToggledVoice;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EnumVoiceState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftVoiceStatusChangeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalAllowedEAG;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;

public class VoiceService {

	private final Map<String, VoiceServerImpl> serverMap = new HashMap<>();
	private final GameMessagePacket disableVoicePacket;

	public VoiceService(EaglerBungeeConfig conf) {
		this.disableVoicePacket = new SPacketVoiceSignalAllowedEAG(false, null);
		String[] iceServers = conf.getICEServers().toArray(new String[conf.getICEServers().size()]);
		SPacketVoiceSignalAllowedEAG iceServersPacket = new SPacketVoiceSignalAllowedEAG(true, iceServers);
		TMap<String,ServerInfo> servers = BungeeCord.getInstance().config.getServers();
		Set<String> keySet = new HashSet<>(servers.keySet());
		keySet.removeAll(conf.getDisableVoiceOnServersSet());
		for(String s : keySet) {
			serverMap.put(s, new VoiceServerImpl(servers.get(s), iceServersPacket));
		}
	}

	public void handlePlayerLoggedIn(UserConnection player) {
		
	}

	public void handlePlayerLoggedOut(UserConnection player) {
		
	}

	public void handleServerConnected(UserConnection player, ServerInfo server) {
		VoiceServerImpl svr = serverMap.get(server.getName());
		if(svr != null) {
			svr.handlePlayerLoggedIn(player);
		}else {
			EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)player.getPendingConnection();
			eaglerHandler.sendEaglerMessage(disableVoicePacket);
			eaglerHandler.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.SERVER_DISABLE);
			if(eaglerHandler.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
				eaglerHandler.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_SERVER_DISABLE);
			}
		}
	}

	public void handleServerDisconnected(UserConnection player, ServerInfo server) {
		VoiceServerImpl svr = serverMap.get(server.getName());
		if(svr != null) {
			svr.handlePlayerLoggedOut(player);
		}
	}

	public void handleVoiceSignalPacketTypeRequest(UUID player, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeRequest(player, sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeConnect(UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeConnect(sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeICE(UUID player, byte[] str, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeICE(player, str, sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeDesc(UUID player, byte[] str, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDesc(player, str, sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeDisconnect(UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDisconnect(sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeDisconnectPeer(UUID player, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDisconnectPeer(player, sender);
			}
		}
	}

	public EnumVoiceState getPlayerVoiceState(UUID player, ServerInfo info) {
		VoiceServerImpl svr = serverMap.get(info.getName());
		if(svr != null) {
			return svr.getPlayerVoiceState(player);
		}else {
			return EnumVoiceState.SERVER_DISABLE;
		}
	}

}
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventToggledVoice;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EnumVoiceState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftVoiceStatusChangeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalAllowedEAG;

public class VoiceService {

	private final Map<String, VoiceServerImpl> serverMap = new HashMap<>();
	private final GameMessagePacket disableVoicePacket;

	public VoiceService(EaglerVelocityConfig conf) {
		this.disableVoicePacket = new SPacketVoiceSignalAllowedEAG(false, null);
		String[] iceServers = conf.getICEServers().toArray(new String[conf.getICEServers().size()]);
		SPacketVoiceSignalAllowedEAG iceServersPacket = new SPacketVoiceSignalAllowedEAG(true, iceServers);
		Collection<RegisteredServer> servers = EaglerXVelocity.proxy().getAllServers();
		for(RegisteredServer s : servers) {
			ServerInfo inf = s.getServerInfo();
			if(!conf.getDisableVoiceOnServersSet().contains(inf.getName())) {
				serverMap.put(inf.getName(), new VoiceServerImpl(inf, iceServersPacket));
			}
		}
	}

	public void handlePlayerLoggedIn(EaglerPlayerData player) {
		
	}

	public void handlePlayerLoggedOut(EaglerPlayerData player) {
		for(VoiceServerImpl svr : serverMap.values()) {
			svr.handlePlayerLoggedOut(player);
		}
	}

	public void handleServerConnected(EaglerPlayerData player, ServerInfo server) {
		VoiceServerImpl svr = serverMap.get(server.getName());
		if(svr != null) {
			svr.handlePlayerLoggedIn(player);
		}else {
			player.sendEaglerMessage(disableVoicePacket);
			player.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.SERVER_DISABLE);
			if(player.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
				player.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_SERVER_DISABLE);
			}
		}
	}

	public void handleServerDisconnected(EaglerPlayerData player, ServerInfo server) {
		VoiceServerImpl svr = serverMap.get(server.getName());
		if(svr != null) {
			svr.handlePlayerLoggedOut(player);
		}
	}

	public void handleVoiceSignalPacketTypeRequest(UUID player, EaglerPlayerData sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeRequest(player, sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeConnect(EaglerPlayerData sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeConnect(sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeICE(UUID player, byte[] str, EaglerPlayerData sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeICE(player, str, sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeDesc(UUID player, byte[] str, EaglerPlayerData sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDesc(player, str, sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeDisconnect(EaglerPlayerData sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDisconnect(sender);
			}
		}
	}

	public void handleVoiceSignalPacketTypeDisconnectPeer(UUID player, EaglerPlayerData sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
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
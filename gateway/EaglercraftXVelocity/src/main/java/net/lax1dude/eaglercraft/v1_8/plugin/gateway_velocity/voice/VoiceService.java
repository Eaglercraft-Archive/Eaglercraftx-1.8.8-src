package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;

/**
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
public class VoiceService {

	public static final ChannelIdentifier CHANNEL = new LegacyChannelIdentifier("EAG|Voice-1.8");

	private final Map<String, VoiceServerImpl> serverMap = new HashMap();
	private final byte[] disableVoicePacket;

	public VoiceService(EaglerVelocityConfig conf) {
		this.disableVoicePacket = VoiceSignalPackets.makeVoiceSignalPacketAllowed(false, null);
		String[] iceServers = conf.getICEServers().toArray(new String[conf.getICEServers().size()]);
		byte[] iceServersPacket = VoiceSignalPackets.makeVoiceSignalPacketAllowed(true, iceServers);
		Collection<RegisteredServer> servers = EaglerXVelocity.proxy().getAllServers();
		for(RegisteredServer s : servers) {
			ServerInfo inf = s.getServerInfo();
			if(!conf.getDisableVoiceOnServersSet().contains(inf.getName())) {
				serverMap.put(inf.getName(), new VoiceServerImpl(inf, iceServersPacket));
			}
		}
	}

	public void handlePlayerLoggedIn(ConnectedPlayer player) {
		
	}

	public void handlePlayerLoggedOut(ConnectedPlayer player) {
		for(VoiceServerImpl svr : serverMap.values()) {
			svr.handlePlayerLoggedOut(player);
		}
	}

	public void handleServerConnected(ConnectedPlayer player, ServerInfo server) {
		VoiceServerImpl svr = serverMap.get(server.getName());
		if(svr != null) {
			svr.handlePlayerLoggedIn(player);
		}else {
			player.sendPluginMessage(CHANNEL, disableVoicePacket);
		}
	}

	public void handleServerDisconnected(ConnectedPlayer player, ServerInfo server) {
		VoiceServerImpl svr = serverMap.get(server.getName());
		if(svr != null) {
			svr.handlePlayerLoggedOut(player);
		}
	}

	void handleVoiceSignalPacketTypeRequest(UUID player, ConnectedPlayer sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeRequest(player, sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeConnect(ConnectedPlayer sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeConnect(sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeICE(UUID player, String str, ConnectedPlayer sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeICE(player, str, sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeDesc(UUID player, String str, ConnectedPlayer sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDesc(player, str, sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeDisconnect(UUID player, ConnectedPlayer sender) {
		if(sender.getConnectedServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getConnectedServer().getServerInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDisconnect(player, sender);
			}
		}
	}

}

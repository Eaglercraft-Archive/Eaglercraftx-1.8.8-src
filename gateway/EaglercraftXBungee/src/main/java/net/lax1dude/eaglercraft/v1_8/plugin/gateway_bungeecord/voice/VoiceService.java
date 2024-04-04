package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import gnu.trove.map.TMap;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;

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

	public static final String CHANNEL = "EAG|Voice-1.8";

	private final Map<String, VoiceServerImpl> serverMap = new HashMap();
	private final byte[] disableVoicePacket;

	public VoiceService(EaglerBungeeConfig conf) {
		this.disableVoicePacket = VoiceSignalPackets.makeVoiceSignalPacketAllowed(false, null);
		String[] iceServers = conf.getICEServers().toArray(new String[conf.getICEServers().size()]);
		byte[] iceServersPacket = VoiceSignalPackets.makeVoiceSignalPacketAllowed(true, iceServers);
		TMap<String,ServerInfo> servers = BungeeCord.getInstance().config.getServers();
		Set<String> keySet = new HashSet(servers.keySet());
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
			player.sendData(CHANNEL, disableVoicePacket);
		}
	}

	public void handleServerDisconnected(UserConnection player, ServerInfo server) {
		VoiceServerImpl svr = serverMap.get(server.getName());
		if(svr != null) {
			svr.handlePlayerLoggedOut(player);
		}
	}

	void handleVoiceSignalPacketTypeRequest(UUID player, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeRequest(player, sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeConnect(UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeConnect(sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeICE(UUID player, String str, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeICE(player, str, sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeDesc(UUID player, String str, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDesc(player, str, sender);
			}
		}
	}

	void handleVoiceSignalPacketTypeDisconnect(UUID player, UserConnection sender) {
		if(sender.getServer() != null) {
			VoiceServerImpl svr = serverMap.get(sender.getServer().getInfo().getName());
			if(svr != null) {
				svr.handleVoiceSignalPacketTypeDisconnect(player, sender);
			}
		}
	}

}

package net.lax1dude.eaglercraft.v1_8.socket.protocol;

import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;

import static net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public enum GamePluginMessageProtocol {
	V3(3,
			define(V3_SKIN_CHANNEL, 0x03, CLIENT_TO_SERVER, CPacketGetOtherSkinEAG.class),
			define(V3_SKIN_CHANNEL, 0x04, SERVER_TO_CLIENT, SPacketOtherSkinPresetEAG.class),
			define(V3_SKIN_CHANNEL, 0x05, SERVER_TO_CLIENT, SPacketOtherSkinCustomV3EAG.class),
			define(V3_SKIN_CHANNEL, 0x06, CLIENT_TO_SERVER, CPacketGetSkinByURLEAG.class),
			define(V3_SKIN_CHANNEL, 0x07, CLIENT_TO_SERVER, CPacketInstallSkinSPEAG.class),
			define(V3_CAPE_CHANNEL, 0x03, CLIENT_TO_SERVER, CPacketGetOtherCapeEAG.class),
			define(V3_CAPE_CHANNEL, 0x04, SERVER_TO_CLIENT, SPacketOtherCapePresetEAG.class),
			define(V3_CAPE_CHANNEL, 0x05, SERVER_TO_CLIENT, SPacketOtherCapeCustomEAG.class),
			define(V3_VOICE_CHANNEL, 0x00, SERVER_TO_CLIENT, SPacketVoiceSignalAllowedEAG.class),
			define(V3_VOICE_CHANNEL, 0x00, CLIENT_TO_SERVER, CPacketVoiceSignalRequestEAG.class),
			define(V3_VOICE_CHANNEL, 0x01, CLIENT_TO_SERVER, CPacketVoiceSignalConnectEAG.class),
			define(V3_VOICE_CHANNEL, 0x01, SERVER_TO_CLIENT, SPacketVoiceSignalConnectV3EAG.class),
			define(V3_VOICE_CHANNEL, 0x02, CLIENT_TO_SERVER, CPacketVoiceSignalDisconnectV3EAG.class),
			define(V3_VOICE_CHANNEL, 0x02, SERVER_TO_CLIENT, SPacketVoiceSignalDisconnectPeerEAG.class),
			define(V3_VOICE_CHANNEL, 0x03, CLIENT_TO_SERVER, CPacketVoiceSignalICEEAG.class),
			define(V3_VOICE_CHANNEL, 0x03, SERVER_TO_CLIENT, SPacketVoiceSignalICEEAG.class),
			define(V3_VOICE_CHANNEL, 0x04, CLIENT_TO_SERVER, CPacketVoiceSignalDescEAG.class),
			define(V3_VOICE_CHANNEL, 0x04, SERVER_TO_CLIENT, SPacketVoiceSignalDescEAG.class),
			define(V3_VOICE_CHANNEL, 0x05, SERVER_TO_CLIENT, SPacketVoiceSignalGlobalEAG.class),
			define(V3_UPDATE_CHANNEL, -1, SERVER_TO_CLIENT, SPacketUpdateCertEAG.class),
			define(V3_FNAW_EN_CHANNEL, -1, SERVER_TO_CLIENT, SPacketEnableFNAWSkinsEAG.class)
	), V4(4,
			define(V4_CHANNEL, 0x01, CLIENT_TO_SERVER, CPacketGetOtherSkinEAG.class),
			define(V4_CHANNEL, 0x02, SERVER_TO_CLIENT, SPacketOtherSkinPresetEAG.class),
			define(V4_CHANNEL, 0x03, SERVER_TO_CLIENT, SPacketOtherSkinCustomV4EAG.class),
			define(V4_CHANNEL, 0x04, CLIENT_TO_SERVER, CPacketGetSkinByURLEAG.class),
			define(V4_CHANNEL, 0x05, CLIENT_TO_SERVER, CPacketInstallSkinSPEAG.class),
			define(V4_CHANNEL, 0x06, CLIENT_TO_SERVER, CPacketGetOtherCapeEAG.class),
			define(V4_CHANNEL, 0x07, SERVER_TO_CLIENT, SPacketOtherCapePresetEAG.class),
			define(V4_CHANNEL, 0x08, SERVER_TO_CLIENT, SPacketOtherCapeCustomEAG.class),
			define(V4_CHANNEL, 0x09, SERVER_TO_CLIENT, SPacketVoiceSignalAllowedEAG.class),
			define(V4_CHANNEL, 0x0A, CLIENT_TO_SERVER, CPacketVoiceSignalRequestEAG.class),
			define(V4_CHANNEL, 0x0B, CLIENT_TO_SERVER, CPacketVoiceSignalConnectEAG.class),
			define(V4_CHANNEL, 0x0C, SERVER_TO_CLIENT, SPacketVoiceSignalConnectV4EAG.class),
			define(V4_CHANNEL, 0x0D, SERVER_TO_CLIENT, SPacketVoiceSignalConnectAnnounceV4EAG.class),
			define(V4_CHANNEL, 0x0E, CLIENT_TO_SERVER, CPacketVoiceSignalDisconnectV4EAG.class),
			define(V4_CHANNEL, 0x0F, CLIENT_TO_SERVER, CPacketVoiceSignalDisconnectPeerV4EAG.class),
			define(V4_CHANNEL, 0x10, SERVER_TO_CLIENT, SPacketVoiceSignalDisconnectPeerEAG.class),
			define(V4_CHANNEL, 0x11, CLIENT_TO_SERVER, CPacketVoiceSignalICEEAG.class),
			define(V4_CHANNEL, 0x12, SERVER_TO_CLIENT, SPacketVoiceSignalICEEAG.class),
			define(V4_CHANNEL, 0x13, CLIENT_TO_SERVER, CPacketVoiceSignalDescEAG.class),
			define(V4_CHANNEL, 0x14, SERVER_TO_CLIENT, SPacketVoiceSignalDescEAG.class),
			define(V4_CHANNEL, 0x15, SERVER_TO_CLIENT, SPacketVoiceSignalGlobalEAG.class),
			define(V4_CHANNEL, 0x16, SERVER_TO_CLIENT, SPacketUpdateCertEAG.class),
			define(V4_CHANNEL, 0x17, SERVER_TO_CLIENT, SPacketEnableFNAWSkinsEAG.class),
			define(V4_CHANNEL, 0x18, SERVER_TO_CLIENT, SPacketForceClientSkinPresetV4EAG.class),
			define(V4_CHANNEL, 0x19, SERVER_TO_CLIENT, SPacketForceClientSkinCustomV4EAG.class),
			define(V4_CHANNEL, 0x1A, SERVER_TO_CLIENT, SPacketSetServerCookieV4EAG.class),
			define(V4_CHANNEL, 0x1B, SERVER_TO_CLIENT, SPacketRedirectClientV4EAG.class),
			define(V4_CHANNEL, 0x1C, CLIENT_TO_SERVER, CPacketGetOtherClientUUIDV4EAG.class),
			define(V4_CHANNEL, 0x1D, SERVER_TO_CLIENT, SPacketOtherPlayerClientUUIDV4EAG.class),
			define(V4_CHANNEL, 0x1E, SERVER_TO_CLIENT, SPacketForceClientCapePresetV4EAG.class),
			define(V4_CHANNEL, 0x1F, SERVER_TO_CLIENT, SPacketForceClientCapeCustomV4EAG.class),
			define(V4_CHANNEL, 0x20, SERVER_TO_CLIENT, SPacketInvalidatePlayerCacheV4EAG.class),
			define(V4_CHANNEL, 0x21, SERVER_TO_CLIENT, SPacketUnforceClientV4EAG.class),
			define(V4_CHANNEL, 0x22, SERVER_TO_CLIENT, SPacketCustomizePauseMenuV4EAG.class),
			define(V4_CHANNEL, 0x23, CLIENT_TO_SERVER, CPacketRequestServerInfoV4EAG.class),
			define(V4_CHANNEL, 0x24, SERVER_TO_CLIENT, SPacketServerInfoDataChunkV4EAG.class),
			define(V4_CHANNEL, 0x25, CLIENT_TO_SERVER, CPacketWebViewMessageEnV4EAG.class),
			define(V4_CHANNEL, 0x26, CLIENT_TO_SERVER, CPacketWebViewMessageV4EAG.class),
			define(V4_CHANNEL, 0x27, SERVER_TO_CLIENT, SPacketWebViewMessageV4EAG.class),
			define(V4_CHANNEL, 0x28, SERVER_TO_CLIENT, SPacketNotifIconsRegisterV4EAG.class),
			define(V4_CHANNEL, 0x29, SERVER_TO_CLIENT, SPacketNotifIconsReleaseV4EAG.class),
			define(V4_CHANNEL, 0x2A, SERVER_TO_CLIENT, SPacketNotifBadgeShowV4EAG.class),
			define(V4_CHANNEL, 0x2B, SERVER_TO_CLIENT, SPacketNotifBadgeHideV4EAG.class)
	);

	public final int ver;

	private final Map<String, Object>[] channelMap;
	private final Map<Class<? extends GameMessagePacket>, PacketDef>[] classMap;
	private final Set<String> notChannelMap = new HashSet<>(); // populated in clinit

	private static final GamePluginMessageProtocol[] PROTOCOLS_MAP = new GamePluginMessageProtocol[5];
	private static final Set<String> allChannels = new HashSet<>();

	private GamePluginMessageProtocol(int versionInt, PacketDef... packets) {
		ver = versionInt;
		channelMap = new Map[] { new HashMap<>(), new HashMap<>() };
		classMap = new Map[] { new HashMap<>(), new HashMap<>() };
		for(int i = 0; i < packets.length; ++i) {
			PacketDef pkt = packets[i];
			classMap[pkt.dir].put(pkt.clazz, pkt);
			if(pkt.id == -1) {
				channelMap[pkt.dir].put(pkt.channel, pkt);
			}else {
				PacketDef[] map = (PacketDef[])channelMap[pkt.dir].get(pkt.channel);
				if(map == null || map.length <= pkt.id) {
					PacketDef[] newMap = new PacketDef[((pkt.id + 1) & 0xF0) + 0x0F];
					if(map != null) {
						System.arraycopy(map, 0, newMap, 0, map.length);
					}
					map = newMap;
					channelMap[pkt.dir].put(pkt.channel, map);
				}
				map[pkt.id] = pkt;
			}
		}
	}

	private static PacketDef define(String channel, int id, int dir, Class<? extends GameMessagePacket> clazz) {
		return new PacketDef(channel, id, dir, clazz);
	}

	private static class PacketDef {
		
		private final String channel;
		private final int id;
		private final int dir;
		private final Class<? extends GameMessagePacket> clazz;
		
		private PacketDef(String channel, int id, int dir, Class<? extends GameMessagePacket> clazz) {
			this.channel = channel;
			this.id = id;
			this.dir = dir;
			this.clazz = clazz;
		}
		
	}

	public GameMessagePacket readPacket(String channel, int direction, GamePacketInputBuffer buffer) throws IOException {
		Object obj = channelMap[direction].get(channel);
		if(obj == null) {
			return null;
		}
		PacketDef toRead;
		if(obj instanceof PacketDef) {
			toRead = (PacketDef)obj;
		}else {
			int pktId = buffer.readUnsignedByte();
			PacketDef[] pkts = (PacketDef[])obj;
			if(pktId < 0 || pktId >= pkts.length || (toRead = pkts[pktId]) == null) {
				throw new IOException("[" + channel + "] Unknown packet ID: " + pktId);
			}
		}
		GameMessagePacket ret;
		try {
			ret = toRead.clazz.newInstance();
		}catch(Throwable t) {
			throw new RuntimeException("Reflection failed to call packet constructor! (Is it defined?)", t);
		}
		ret.readPacket(buffer);
		return ret;
	}

	public String writePacket(int direction, GamePacketOutputBuffer stream, GameMessagePacket packet) throws IOException {
		Class<? extends GameMessagePacket> clazz = packet.getClass();
		PacketDef def = classMap[direction].get(clazz);
		if(def == null) {
			throw new IOException("Unknown packet type or wrong direction: " + clazz);
		}
		if(def.id != -1) {
			stream.writeByte(def.id);
		}
		packet.writePacket(stream);
		return def.channel;
	}

	public List<String> filterProtocols(Collection<String> data) {
		List<String> ret = new ArrayList<>(data.size());
		for(String str : data) {
			if(!notChannelMap.contains(str)) {
				ret.add(str);
			}
		}
		return ret;
	}

	public static GamePluginMessageProtocol getByVersion(int ver) {
		if(ver < 0 || ver > PROTOCOLS_MAP.length) {
			return null;
		}
		return PROTOCOLS_MAP[ver];
	}

	public static Set<String> getAllChannels() {
		return allChannels;
	}

	static {
		GamePluginMessageProtocol[] _values = values();
		PROTOCOLS_MAP[2] = V3;
		for(int i = 0; i < _values.length; ++i) {
			GamePluginMessageProtocol protocol = _values[i];
			PROTOCOLS_MAP[protocol.ver] = protocol;
			allChannels.addAll(protocol.channelMap[CLIENT_TO_SERVER].keySet());
			allChannels.addAll(protocol.channelMap[SERVER_TO_CLIENT].keySet());
		}
		for(int i = 0; i < _values.length; ++i) {
			GamePluginMessageProtocol protocol = _values[i];
			protocol.notChannelMap.addAll(allChannels);
			protocol.notChannelMap.removeAll(protocol.channelMap[CLIENT_TO_SERVER].keySet());
			protocol.notChannelMap.removeAll(protocol.channelMap[SERVER_TO_CLIENT].keySet());
		}
	}
}

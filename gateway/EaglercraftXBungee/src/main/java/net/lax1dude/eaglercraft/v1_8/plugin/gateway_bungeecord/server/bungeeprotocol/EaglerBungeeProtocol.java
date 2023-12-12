package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.md_5.bungee.protocol.BadPacketException;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.packet.*;

import java.util.function.Supplier;

/**
 * The original net.md_5.bungee.protocol.Protocol is inaccessible due to java
 * security rules
 */

public enum EaglerBungeeProtocol {

	// Undef
	HANDSHAKE {

		{
			TO_SERVER.registerPacket(Handshake.class, Handshake::new, map(ProtocolConstants.MINECRAFT_1_8, 0x00));
		}
	},
	// 0
	GAME {

		{
			TO_CLIENT.registerPacket(KeepAlive.class, KeepAlive::new, map(ProtocolConstants.MINECRAFT_1_8, 0x00));
			TO_CLIENT.registerPacket(Login.class, Login::new, map(ProtocolConstants.MINECRAFT_1_8, 0x01));
			TO_CLIENT.registerPacket(Chat.class, Chat::new, map(ProtocolConstants.MINECRAFT_1_8, 0x02));
			TO_CLIENT.registerPacket(Respawn.class, Respawn::new, map(ProtocolConstants.MINECRAFT_1_8, 0x07));
			TO_CLIENT.registerPacket(PlayerListItem.class, // PlayerInfo
					PlayerListItem::new, map(ProtocolConstants.MINECRAFT_1_8, 0x38));
			TO_CLIENT.registerPacket(TabCompleteResponse.class, TabCompleteResponse::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x3A));
			TO_CLIENT.registerPacket(ScoreboardObjective.class, ScoreboardObjective::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x3B));
			TO_CLIENT.registerPacket(ScoreboardScore.class, ScoreboardScore::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x3C));
			TO_CLIENT.registerPacket(ScoreboardDisplay.class, ScoreboardDisplay::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x3D));
			TO_CLIENT.registerPacket(Team.class, Team::new, map(ProtocolConstants.MINECRAFT_1_8, 0x3E));
			TO_CLIENT.registerPacket(PluginMessage.class, PluginMessage::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x3F));
			TO_CLIENT.registerPacket(Kick.class, Kick::new, map(ProtocolConstants.MINECRAFT_1_8, 0x40));
			TO_CLIENT.registerPacket(Title.class, Title::new, map(ProtocolConstants.MINECRAFT_1_8, 0x45));
			TO_CLIENT.registerPacket(PlayerListHeaderFooter.class, PlayerListHeaderFooter::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x47));
			TO_CLIENT.registerPacket(EntityStatus.class, EntityStatus::new, map(ProtocolConstants.MINECRAFT_1_8, 0x1A));

			TO_SERVER.registerPacket(KeepAlive.class, KeepAlive::new, map(ProtocolConstants.MINECRAFT_1_8, 0x00));
			TO_SERVER.registerPacket(Chat.class, Chat::new, map(ProtocolConstants.MINECRAFT_1_8, 0x01));
			TO_SERVER.registerPacket(TabCompleteRequest.class, TabCompleteRequest::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x14));
			TO_SERVER.registerPacket(ClientSettings.class, ClientSettings::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x15));
			TO_SERVER.registerPacket(PluginMessage.class, PluginMessage::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x17));
		}
	},
	// 1
	STATUS {

		{
			TO_CLIENT.registerPacket(StatusResponse.class, StatusResponse::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x00));
			TO_CLIENT.registerPacket(PingPacket.class, PingPacket::new, map(ProtocolConstants.MINECRAFT_1_8, 0x01));

			TO_SERVER.registerPacket(StatusRequest.class, StatusRequest::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x00));
			TO_SERVER.registerPacket(PingPacket.class, PingPacket::new, map(ProtocolConstants.MINECRAFT_1_8, 0x01));
		}
	},
	// 2
	LOGIN {

		{
			TO_CLIENT.registerPacket(Kick.class, Kick::new, map(ProtocolConstants.MINECRAFT_1_8, 0x00));
			TO_CLIENT.registerPacket(EncryptionRequest.class, EncryptionRequest::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x01));
			TO_CLIENT.registerPacket(LoginSuccess.class, LoginSuccess::new, map(ProtocolConstants.MINECRAFT_1_8, 0x02));
			TO_CLIENT.registerPacket(SetCompression.class, SetCompression::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x03));

			TO_SERVER.registerPacket(LoginRequest.class, LoginRequest::new, map(ProtocolConstants.MINECRAFT_1_8, 0x00));
			TO_SERVER.registerPacket(EncryptionResponse.class, EncryptionResponse::new,
					map(ProtocolConstants.MINECRAFT_1_8, 0x01));
		}
	},
	// 3
	CONFIGURATION {

	};

	/* ======================================================================== */
	public static final int MAX_PACKET_ID = 0xFF;
	/* ======================================================================== */
	public final DirectionData TO_SERVER = new DirectionData(this, ProtocolConstants.Direction.TO_SERVER);
	public final DirectionData TO_CLIENT = new DirectionData(this, ProtocolConstants.Direction.TO_CLIENT);

	public static void main(String[] args) {
		for (int version : ProtocolConstants.SUPPORTED_VERSION_IDS) {
			dump(version);
		}
	}

	private static void dump(int version) {
		for (EaglerBungeeProtocol protocol : EaglerBungeeProtocol.values()) {
			dump(version, protocol);
		}
	}

	private static void dump(int version, EaglerBungeeProtocol protocol) {
		dump(version, protocol.TO_CLIENT);
		dump(version, protocol.TO_SERVER);
	}

	private static void dump(int version, DirectionData data) {
		for (int id = 0; id < MAX_PACKET_ID; id++) {
			DefinedPacket packet = data.createPacket(id, version);
			if (packet != null) {
				System.out.println(version + " " + data.protocolPhase + " " + data.direction + " " + id + " "
						+ packet.getClass().getSimpleName());
			}
		}
	}

	private static class ProtocolData {

		private final int protocolVersion;
		private final TObjectIntMap<Class<? extends DefinedPacket>> packetMap = new TObjectIntHashMap<>(MAX_PACKET_ID);
		@SuppressWarnings("unchecked")
		private final Supplier<? extends DefinedPacket>[] packetConstructors = new Supplier[MAX_PACKET_ID];

		private ProtocolData(int protocolVersion) {
			this.protocolVersion = protocolVersion;
		}
	}

	private static class ProtocolMapping {

		private final int protocolVersion;
		private final int packetID;

		private ProtocolMapping(int protocolVersion, int packetID) {
			this.protocolVersion = protocolVersion;
			this.packetID = packetID;
		}
	}

	// Helper method
	private static ProtocolMapping map(int protocol, int id) {
		return new ProtocolMapping(protocol, id);
	}

	public static final class DirectionData {

		private final TIntObjectMap<ProtocolData> protocols = new TIntObjectHashMap<>();
		//
		private final EaglerBungeeProtocol protocolPhase;
		private final ProtocolConstants.Direction direction;

		public DirectionData(EaglerBungeeProtocol protocolPhase, ProtocolConstants.Direction direction) {
			this.protocolPhase = protocolPhase;
			this.direction = direction;

			for (int protocol : ProtocolConstants.SUPPORTED_VERSION_IDS) {
				protocols.put(protocol, new ProtocolData(protocol));
			}
		}

		private ProtocolData getProtocolData(int version) {
			ProtocolData protocol = protocols.get(version);
			if (protocol == null && (protocolPhase != EaglerBungeeProtocol.GAME)) {
				protocol = Iterables.getFirst(protocols.valueCollection(), null);
			}
			return protocol;
		}

		public final DefinedPacket createPacket(int id, int version) {
			ProtocolData protocolData = getProtocolData(version);
			if (protocolData == null) {
				throw new BadPacketException("Unsupported protocol version " + version);
			}
			if (id > MAX_PACKET_ID || id < 0) {
				throw new BadPacketException("Packet with id " + id + " outside of range");
			}

			Supplier<? extends DefinedPacket> constructor = protocolData.packetConstructors[id];
			return (constructor == null) ? null : constructor.get();
		}

		private void registerPacket(Class<? extends DefinedPacket> packetClass,
				Supplier<? extends DefinedPacket> constructor, ProtocolMapping... mappings) {
			int mappingIndex = 0;
			ProtocolMapping mapping = mappings[mappingIndex];
			for (int protocol : ProtocolConstants.SUPPORTED_VERSION_IDS) {
				if (protocol < mapping.protocolVersion) {
					// This is a new packet, skip it till we reach the next protocol
					continue;
				}

				if (mapping.protocolVersion < protocol && mappingIndex + 1 < mappings.length) {
					// Mapping is non current, but the next one may be ok
					ProtocolMapping nextMapping = mappings[mappingIndex + 1];

					if (nextMapping.protocolVersion == protocol) {
						Preconditions.checkState(nextMapping.packetID != mapping.packetID,
								"Duplicate packet mapping (%s, %s)", mapping.protocolVersion,
								nextMapping.protocolVersion);

						mapping = nextMapping;
						mappingIndex++;
					}
				}

				if (mapping.packetID < 0) {
					break;
				}

				ProtocolData data = protocols.get(protocol);
				data.packetMap.put(packetClass, mapping.packetID);
				data.packetConstructors[mapping.packetID] = constructor;
			}
		}

		public boolean hasPacket(Class<? extends DefinedPacket> packet, int version) {
			ProtocolData protocolData = getProtocolData(version);
			if (protocolData == null) {
				throw new BadPacketException("Unsupported protocol version");
			}

			return protocolData.packetMap.containsKey(packet);
		}

		final int getId(Class<? extends DefinedPacket> packet, int version) {

			ProtocolData protocolData = getProtocolData(version);
			if (protocolData == null) {
				throw new BadPacketException("Unsupported protocol version");
			}
			Preconditions.checkArgument(protocolData.packetMap.containsKey(packet),
					"Cannot get ID for packet %s in phase %s with direction %s", packet, protocolPhase, direction);

			return protocolData.packetMap.get(packet);
		}
	}
}
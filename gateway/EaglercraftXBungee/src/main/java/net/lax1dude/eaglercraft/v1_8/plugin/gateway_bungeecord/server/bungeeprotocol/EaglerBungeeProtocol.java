package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.TIntObjectMap;
import java.util.Arrays;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.TObjectIntMap;
import net.md_5.bungee.protocol.packet.LoginPayloadResponse;
import net.md_5.bungee.protocol.packet.EncryptionResponse;
import net.md_5.bungee.protocol.packet.LoginRequest;
import net.md_5.bungee.protocol.packet.LoginPayloadRequest;
import net.md_5.bungee.protocol.packet.SetCompression;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.StatusRequest;
import net.md_5.bungee.protocol.packet.PingPacket;
import net.md_5.bungee.protocol.packet.StatusResponse;
import net.md_5.bungee.protocol.packet.ClientSettings;
import net.md_5.bungee.protocol.packet.TabCompleteRequest;
import net.md_5.bungee.protocol.packet.ClientChat;
import net.md_5.bungee.protocol.packet.ClientCommand;
import net.md_5.bungee.protocol.packet.ServerData;
import net.md_5.bungee.protocol.packet.ViewDistance;
import net.md_5.bungee.protocol.packet.GameState;
import net.md_5.bungee.protocol.packet.Commands;
import net.md_5.bungee.protocol.packet.EntityStatus;
import net.md_5.bungee.protocol.packet.PlayerListHeaderFooter;
import net.md_5.bungee.protocol.packet.SystemChat;
import net.md_5.bungee.protocol.packet.TitleTimes;
import net.md_5.bungee.protocol.packet.Subtitle;
import net.md_5.bungee.protocol.packet.ClearTitles;
import net.md_5.bungee.protocol.packet.Title;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.Team;
import net.md_5.bungee.protocol.packet.ScoreboardDisplay;
import net.md_5.bungee.protocol.packet.ScoreboardScore;
import net.md_5.bungee.protocol.packet.ScoreboardObjective;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.BadPacketException;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.packet.BossBar;
import net.md_5.bungee.protocol.packet.Respawn;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.KeepAlive;
import java.util.function.Supplier;
import net.md_5.bungee.protocol.packet.Handshake;

/**
 * 
 * The original net.md_5.bungee.protocol.Protocol is inaccessible due to java security rules
 * 
 * This is the decompiled source of it, I was too lazy to locate the original
 *
 */
public enum EaglerBungeeProtocol {
	HANDSHAKE(0) {
		{
			this.TO_SERVER.registerPacket(Handshake.class, Handshake::new, new ProtocolMapping[]{map(47, 0)});
		}
	},
	GAME(1) {
		{
			this.TO_CLIENT.registerPacket(KeepAlive.class, KeepAlive::new,
					new ProtocolMapping[]{map(47, 0), map(107, 31), map(393, 33), map(477, 32), map(573, 33),
							map(735, 32), map(751, 31), map(755, 33), map(759, 30), map(760, 32)});
			this.TO_CLIENT.registerPacket(Login.class, Login::new, new ProtocolMapping[]{map(47, 1), map(107, 35),
					map(393, 37), map(573, 38), map(735, 37), map(751, 36), map(755, 38), map(759, 35), map(760, 37)});
			this.TO_CLIENT.registerPacket(Chat.class, Chat::new, new ProtocolMapping[]{map(47, 2), map(107, 15),
					map(393, 14), map(573, 15), map(735, 14), map(755, 15), map(759, -1)});
			this.TO_CLIENT.registerPacket(Respawn.class, Respawn::new,
					new ProtocolMapping[]{map(47, 7), map(107, 51), map(335, 52), map(338, 53), map(393, 56),
							map(477, 58), map(573, 59), map(735, 58), map(751, 57), map(755, 61), map(759, 59),
							map(760, 62)});
			this.TO_CLIENT.registerPacket(BossBar.class, BossBar::new,
					new ProtocolMapping[]{map(107, 12), map(573, 13), map(735, 12), map(755, 13), map(759, 10)});
			this.TO_CLIENT.registerPacket(PlayerListItem.class, PlayerListItem::new,
					new ProtocolMapping[]{map(47, 56), map(107, 45), map(338, 46), map(393, 48), map(477, 51),
							map(573, 52), map(735, 51), map(751, 50), map(755, 54), map(759, 52), map(760, 55)});
			this.TO_CLIENT.registerPacket(TabCompleteResponse.class, TabCompleteResponse::new,
					new ProtocolMapping[]{map(47, 58), map(107, 14), map(393, 16), map(573, 17), map(735, 16),
							map(751, 15), map(755, 17), map(759, 14)});
			this.TO_CLIENT.registerPacket(ScoreboardObjective.class, ScoreboardObjective::new,
					new ProtocolMapping[]{map(47, 59), map(107, 63), map(335, 65), map(338, 66), map(393, 69),
							map(477, 73), map(573, 74), map(755, 83), map(760, 86)});
			this.TO_CLIENT.registerPacket(ScoreboardScore.class, ScoreboardScore::new,
					new ProtocolMapping[]{map(47, 60), map(107, 66), map(335, 68), map(338, 69), map(393, 72),
							map(477, 76), map(573, 77), map(755, 86), map(760, 89)});
			this.TO_CLIENT.registerPacket(ScoreboardDisplay.class, ScoreboardDisplay::new,
					new ProtocolMapping[]{map(47, 61), map(107, 56), map(335, 58), map(338, 59), map(393, 62),
							map(477, 66), map(573, 67), map(755, 76), map(760, 79)});
			this.TO_CLIENT.registerPacket(Team.class, Team::new, new ProtocolMapping[]{map(47, 62), map(107, 65),
					map(335, 67), map(338, 68), map(393, 71), map(477, 75), map(573, 76), map(755, 85), map(760, 88)});
			this.TO_CLIENT.registerPacket(PluginMessage.class, PluginMessage::new,
					new ProtocolMapping[]{map(47, 63), map(107, 24), map(393, 25), map(477, 24), map(573, 25),
							map(735, 24), map(751, 23), map(755, 24), map(759, 21), map(760, 22)});
			this.TO_CLIENT.registerPacket(Kick.class, Kick::new,
					new ProtocolMapping[]{map(47, 64), map(107, 26), map(393, 27), map(477, 26), map(573, 27),
							map(735, 26), map(751, 25), map(755, 26), map(759, 23), map(760, 25)});
			this.TO_CLIENT.registerPacket(Title.class, Title::new,
					new ProtocolMapping[]{map(47, 69), map(335, 71), map(338, 72), map(393, 75), map(477, 79),
							map(573, 80), map(735, 79), map(755, 89), map(757, 90), map(760, 93)});
			this.TO_CLIENT.registerPacket(ClearTitles.class, ClearTitles::new,
					new ProtocolMapping[]{map(755, 16), map(759, 13)});
			this.TO_CLIENT.registerPacket(Subtitle.class, Subtitle::new,
					new ProtocolMapping[]{map(755, 87), map(757, 88), map(760, 91)});
			this.TO_CLIENT.registerPacket(TitleTimes.class, TitleTimes::new,
					new ProtocolMapping[]{map(755, 90), map(757, 91), map(760, 94)});
			this.TO_CLIENT.registerPacket(SystemChat.class, SystemChat::new,
					new ProtocolMapping[]{map(759, 95), map(760, 98)});
			this.TO_CLIENT.registerPacket(PlayerListHeaderFooter.class, PlayerListHeaderFooter::new,
					new ProtocolMapping[]{map(47, 71), map(107, 72), map(110, 71), map(335, 73), map(338, 74),
							map(393, 78), map(477, 83), map(573, 84), map(735, 83), map(755, 94), map(757, 95),
							map(759, 96), map(760, 99)});
			this.TO_CLIENT.registerPacket(EntityStatus.class, EntityStatus::new,
					new ProtocolMapping[]{map(47, 26), map(107, 27), map(393, 28), map(477, 27), map(573, 28),
							map(735, 27), map(751, 26), map(755, 27), map(759, 24), map(760, 26)});
			this.TO_CLIENT.registerPacket(Commands.class, Commands::new, new ProtocolMapping[]{map(393, 17),
					map(573, 18), map(735, 17), map(751, 16), map(755, 18), map(759, 15)});
			this.TO_CLIENT.registerPacket(GameState.class, GameState::new, new ProtocolMapping[]{map(573, 31),
					map(735, 30), map(751, 29), map(755, 30), map(759, 27), map(760, 29)});
			this.TO_CLIENT.registerPacket(ViewDistance.class, ViewDistance::new, new ProtocolMapping[]{map(477, 65),
					map(573, 66), map(735, 65), map(755, 74), map(759, 73), map(760, 76)});
			this.TO_CLIENT.registerPacket(ServerData.class, ServerData::new,
					new ProtocolMapping[]{map(759, 63), map(760, 66)});
			this.TO_SERVER.registerPacket(KeepAlive.class, KeepAlive::new,
					new ProtocolMapping[]{map(47, 0), map(107, 11), map(335, 12), map(338, 11), map(393, 14),
							map(477, 15), map(735, 16), map(755, 15), map(759, 17), map(760, 18)});
			this.TO_SERVER.registerPacket(Chat.class, Chat::new, new ProtocolMapping[]{map(47, 1), map(107, 2),
					map(335, 3), map(338, 2), map(477, 3), map(759, -1)});
			this.TO_SERVER.registerPacket(ClientCommand.class, ClientCommand::new,
					new ProtocolMapping[]{map(759, 3), map(760, 4)});
			this.TO_SERVER.registerPacket(ClientChat.class, ClientChat::new,
					new ProtocolMapping[]{map(759, 4), map(760, 5)});
			this.TO_SERVER.registerPacket(TabCompleteRequest.class, TabCompleteRequest::new,
					new ProtocolMapping[]{map(47, 20), map(107, 1), map(335, 2), map(338, 1), map(393, 5), map(477, 6),
							map(759, 8), map(760, 9)});
			this.TO_SERVER.registerPacket(ClientSettings.class, ClientSettings::new, new ProtocolMapping[]{map(47, 21),
					map(107, 4), map(335, 5), map(338, 4), map(477, 5), map(759, 7), map(760, 8)});
			this.TO_SERVER.registerPacket(PluginMessage.class, PluginMessage::new,
					new ProtocolMapping[]{map(47, 23), map(107, 9), map(335, 10), map(338, 9), map(393, 10),
							map(477, 11), map(755, 10), map(759, 12), map(760, 13)});
		}
	},
	STATUS(2) {
		{
			this.TO_CLIENT.registerPacket(StatusResponse.class, StatusResponse::new, new ProtocolMapping[]{map(47, 0)});
			this.TO_CLIENT.registerPacket(PingPacket.class, PingPacket::new, new ProtocolMapping[]{map(47, 1)});
			this.TO_SERVER.registerPacket(StatusRequest.class, StatusRequest::new, new ProtocolMapping[]{map(47, 0)});
			this.TO_SERVER.registerPacket(PingPacket.class, PingPacket::new, new ProtocolMapping[]{map(47, 1)});
		}
	},
	LOGIN(3) {
		{
			this.TO_CLIENT.registerPacket(Kick.class, Kick::new, new ProtocolMapping[]{map(47, 0)});
			this.TO_CLIENT.registerPacket(EncryptionRequest.class, EncryptionRequest::new,
					new ProtocolMapping[]{map(47, 1)});
			this.TO_CLIENT.registerPacket(LoginSuccess.class, LoginSuccess::new, new ProtocolMapping[]{map(47, 2)});
			this.TO_CLIENT.registerPacket(SetCompression.class, SetCompression::new, new ProtocolMapping[]{map(47, 3)});
			this.TO_CLIENT.registerPacket(LoginPayloadRequest.class, LoginPayloadRequest::new,
					new ProtocolMapping[]{map(393, 4)});
			this.TO_SERVER.registerPacket(LoginRequest.class, LoginRequest::new, new ProtocolMapping[]{map(47, 0)});
			this.TO_SERVER.registerPacket(EncryptionResponse.class, EncryptionResponse::new,
					new ProtocolMapping[]{map(47, 1)});
			this.TO_SERVER.registerPacket(LoginPayloadResponse.class, LoginPayloadResponse::new,
					new ProtocolMapping[]{map(393, 2)});
		}
	};

	public static final int MAX_PACKET_ID = 255;
	final DirectionData TO_SERVER;
	final DirectionData TO_CLIENT;

	private EaglerBungeeProtocol(int id) {
		this.TO_SERVER = new DirectionData(this, ProtocolConstants.Direction.TO_SERVER);
		this.TO_CLIENT = new DirectionData(this, ProtocolConstants.Direction.TO_CLIENT);
	}

	private static ProtocolMapping map(final int protocol, final int id) {
		return new ProtocolMapping(protocol, id);
	}

	public static class ProtocolData {
		private final int protocolVersion;
		private final TObjectIntMap<Class<? extends DefinedPacket>> packetMap;
		private final Supplier<? extends DefinedPacket>[] packetConstructors;

		public ProtocolData(final int protocolVersion) {
			this.packetMap = (TObjectIntMap<Class<? extends DefinedPacket>>) new TObjectIntHashMap(255);
			this.packetConstructors = (Supplier<? extends DefinedPacket>[]) new Supplier[255];
			this.protocolVersion = protocolVersion;
		}

		public int getProtocolVersion() {
			return this.protocolVersion;
		}

		public TObjectIntMap<Class<? extends DefinedPacket>> getPacketMap() {
			return this.packetMap;
		}

		public Supplier<? extends DefinedPacket>[] getPacketConstructors() {
			return this.packetConstructors;
		}

		@Override
		public boolean equals(final Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof ProtocolData)) {
				return false;
			}
			final ProtocolData other = (ProtocolData) o;
			if (!other.canEqual(this)) {
				return false;
			}
			if (this.getProtocolVersion() != other.getProtocolVersion()) {
				return false;
			}
			final Object this$packetMap = this.getPacketMap();
			final Object other$packetMap = other.getPacketMap();
			if (this$packetMap == null) {
				if (other$packetMap == null) {
					return Arrays.deepEquals(this.getPacketConstructors(), other.getPacketConstructors());
				}
			} else if (this$packetMap.equals(other$packetMap)) {
				return Arrays.deepEquals(this.getPacketConstructors(), other.getPacketConstructors());
			}
			return false;
		}

		protected boolean canEqual(final Object other) {
			return other instanceof ProtocolData;
		}

		@Override
		public int hashCode() {
			final int PRIME = 59;
			int result = 1;
			result = result * 59 + this.getProtocolVersion();
			final Object $packetMap = this.getPacketMap();
			result = result * 59 + (($packetMap == null) ? 43 : $packetMap.hashCode());
			result = result * 59 + Arrays.deepHashCode(this.getPacketConstructors());
			return result;
		}

		@Override
		public String toString() {
			return "Protocol.ProtocolData(protocolVersion=" + this.getProtocolVersion() + ", packetMap="
					+ this.getPacketMap() + ", packetConstructors=" + Arrays.deepToString(this.getPacketConstructors())
					+ ")";
		}
	}

	public static class ProtocolMapping {
		private final int protocolVersion;
		private final int packetID;

		public ProtocolMapping(final int protocolVersion, final int packetID) {
			this.protocolVersion = protocolVersion;
			this.packetID = packetID;
		}

		public int getProtocolVersion() {
			return this.protocolVersion;
		}

		public int getPacketID() {
			return this.packetID;
		}

		@Override
		public boolean equals(final Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof ProtocolMapping)) {
				return false;
			}
			final ProtocolMapping other = (ProtocolMapping) o;
			return other.canEqual(this) && this.getProtocolVersion() == other.getProtocolVersion()
					&& this.getPacketID() == other.getPacketID();
		}

		protected boolean canEqual(final Object other) {
			return other instanceof ProtocolMapping;
		}

		@Override
		public int hashCode() {
			final int PRIME = 59;
			int result = 1;
			result = result * 59 + this.getProtocolVersion();
			result = result * 59 + this.getPacketID();
			return result;
		}

		@Override
		public String toString() {
			return "Protocol.ProtocolMapping(protocolVersion=" + this.getProtocolVersion() + ", packetID="
					+ this.getPacketID() + ")";
		}
	}

	public static final class DirectionData {
		private final TIntObjectMap<ProtocolData> protocols;
		private final EaglerBungeeProtocol protocolPhase;
		private final ProtocolConstants.Direction direction;

		public DirectionData(final EaglerBungeeProtocol protocolPhase, final ProtocolConstants.Direction direction) {
			this.protocols = (TIntObjectMap<ProtocolData>) new TIntObjectHashMap();
			this.protocolPhase = protocolPhase;
			this.direction = direction;
			for (final int protocol : ProtocolConstants.SUPPORTED_VERSION_IDS) {
				this.protocols.put(protocol, new ProtocolData(protocol));
			}
		}

		private ProtocolData getProtocolData(final int version) {
			ProtocolData protocol = (ProtocolData) this.protocols.get(version);
			if (protocol == null && this.protocolPhase != EaglerBungeeProtocol.GAME) {
				protocol = (ProtocolData) Iterables.getFirst((Iterable) this.protocols.valueCollection(),
						(Object) null);
			}
			return protocol;
		}

		public final DefinedPacket createPacket(final int id, final int version) {
			final ProtocolData protocolData = this.getProtocolData(version);
			if (protocolData == null) {
				throw new BadPacketException("Unsupported protocol version " + version);
			}
			if (id > 255 || id < 0) {
				throw new BadPacketException("Packet with id " + id + " outside of range");
			}
			final Supplier<? extends DefinedPacket> constructor = protocolData.packetConstructors[id];
			return (constructor == null) ? null : ((DefinedPacket) constructor.get());
		}

		private void registerPacket(final Class<? extends DefinedPacket> packetClass,
				final Supplier<? extends DefinedPacket> constructor, final ProtocolMapping... mappings) {
			int mappingIndex = 0;
			ProtocolMapping mapping = mappings[mappingIndex];
			for (final int protocol : ProtocolConstants.SUPPORTED_VERSION_IDS) {
				if (protocol < mapping.protocolVersion) {
					continue;
				}
				if (mapping.protocolVersion < protocol && mappingIndex + 1 < mappings.length) {
					final ProtocolMapping nextMapping = mappings[mappingIndex + 1];
					if (nextMapping.protocolVersion == protocol) {
						Preconditions.checkState(nextMapping.packetID != mapping.packetID,
								"Duplicate packet mapping (%s, %s)", mapping.protocolVersion,
								nextMapping.protocolVersion);
						mapping = nextMapping;
						++mappingIndex;
					}
				}
				if (mapping.packetID < 0) {
					break;
				}
				final ProtocolData data = (ProtocolData) this.protocols.get(protocol);
				data.packetMap.put(packetClass, mapping.packetID);
				data.packetConstructors[mapping.packetID] = constructor;
			}
		}

		final int getId(final Class<? extends DefinedPacket> packet, final int version) {
			final ProtocolData protocolData = this.getProtocolData(version);
			if (protocolData == null) {
				throw new BadPacketException("Unsupported protocol version");
			}
			Preconditions.checkArgument(protocolData.packetMap.containsKey((Object) packet),
					"Cannot get ID for packet %s in phase %s with direction %s", (Object) packet,
					(Object) this.protocolPhase, (Object) this.direction);
			return protocolData.packetMap.get((Object) packet);
		}

		public ProtocolConstants.Direction getDirection() {
			return this.direction;
		}
	}
}
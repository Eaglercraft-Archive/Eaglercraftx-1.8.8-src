package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol;

import net.md_5.bungee.protocol.DefinedPacket;

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
public class EaglerProtocolAccessProxy {
	
	public static int getPacketId(EaglerBungeeProtocol protocol, int protocolVersion, DefinedPacket pkt, boolean server) {
		final EaglerBungeeProtocol.DirectionData prot = server ? protocol.TO_CLIENT : protocol.TO_SERVER;
		return prot.getId((Class) pkt.getClass(), protocolVersion);
	}

	public static DefinedPacket createPacket(EaglerBungeeProtocol protocol, int protocolVersion, int packetId, boolean server) {
		final EaglerBungeeProtocol.DirectionData prot = server ? protocol.TO_CLIENT : protocol.TO_SERVER;
		return prot.createPacket(packetId, protocolVersion);
	}
	
}

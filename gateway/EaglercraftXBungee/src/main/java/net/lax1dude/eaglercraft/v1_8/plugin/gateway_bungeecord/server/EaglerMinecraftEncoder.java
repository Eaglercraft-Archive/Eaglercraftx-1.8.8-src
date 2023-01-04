package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol.EaglerBungeeProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol.EaglerProtocolAccessProxy;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

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
public class EaglerMinecraftEncoder extends MessageToMessageEncoder<DefinedPacket> {
	
	private EaglerBungeeProtocol protocol;
	private boolean server;
	private int protocolVersion;
	
	@Override
	protected void encode(ChannelHandlerContext ctx, DefinedPacket msg, List<Object> out) throws Exception {
		ByteBuf buf = Unpooled.buffer();
		int pk = EaglerProtocolAccessProxy.getPacketId(protocol, protocolVersion, msg, server);
		DefinedPacket.writeVarInt(pk, buf);
		msg.write(buf, server ? Direction.TO_CLIENT : Direction.TO_SERVER, protocolVersion);
		out.add(new BinaryWebSocketFrame(buf));
	}

	public EaglerMinecraftEncoder(final EaglerBungeeProtocol protocol, final boolean server, final int protocolVersion) {
		this.protocol = protocol;
		this.server = server;
		this.protocolVersion = protocolVersion;
	}

	public void setProtocol(final EaglerBungeeProtocol protocol) {
		this.protocol = protocol;
	}

	public void setProtocolVersion(final int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
}

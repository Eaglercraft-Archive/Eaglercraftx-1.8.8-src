package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class EaglerMinecraftDecoder extends MessageToMessageDecoder<WebSocketFrame> {
	private Protocol protocol;
	private final boolean server;
	private int protocolVersion;
	private static Constructor<PacketWrapper> packetWrapperConstructor = null;

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
		if(!ctx.channel().isActive()) {
			return;
		}
		EaglerConnectionInstance con = ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get();
		long millis = EaglerXBungeeAPIHelper.steadyTimeMillis();
		if(frame instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame in = (BinaryWebSocketFrame) frame;
			ByteBuf buf = in.content();
			buf.markReaderIndex();
			int pktId = DefinedPacket.readVarInt(buf);
			DefinedPacket pkt = EaglerProtocolAccessProxy.createPacket(protocol, protocolVersion, pktId, server);
			if(pkt != null) {
				pkt.read(buf, server ? Direction.TO_CLIENT : Direction.TO_SERVER, protocolVersion);
				if(buf.isReadable()) {
					EaglerXBungee.logger().severe("[DECODER][" + ctx.channel().remoteAddress() + "] Packet "  +
							pkt.getClass().getSimpleName() + " had extra bytes! (" + buf.readableBytes() + ")");
				}else {
					buf.resetReaderIndex();
					out.add(this.wrapPacket(pkt, buf, protocol));
				}
			}else {
				buf.resetReaderIndex();
				out.add(this.wrapPacket(null, buf, protocol));
			}
		}else if(frame instanceof PingWebSocketFrame) {
			if(millis - con.lastClientPingPacket > 500l) {
				ctx.write(new PongWebSocketFrame());
				con.lastClientPingPacket = millis;
			}
		}else if(frame instanceof PongWebSocketFrame) {
			con.lastClientPongPacket = millis;
		}else {
			ctx.close();
		}
	}

	public EaglerMinecraftDecoder(Protocol protocol, boolean server, int protocolVersion) {
		this.protocol = protocol;
		this.server = server;
		this.protocolVersion = protocolVersion;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	private PacketWrapper wrapPacket(DefinedPacket packet, ByteBuf buf, Protocol protocol) {
		boolean isInputDirect = buf.isDirect();
		if(!isInputDirect) {
			int len = buf.readableBytes();
			ByteBuf newBuf = buf.alloc().directBuffer(len);
			buf.readBytes(newBuf, len);
			buf = newBuf;
		}
		try {
			PacketWrapper pkt;
			if (packetWrapperConstructor != null) {
				try {
					pkt = packetWrapperConstructor.newInstance(packet, buf);
					buf.retain();
					return pkt;
				} catch (IllegalAccessException | InvocationTargetException | InstantiationException var14) {
					throw new RuntimeException(var14);
				}
			}
	
			try {
				pkt = new PacketWrapper(packet, buf, protocol);
				buf.retain();
				return pkt;
			} catch (NoSuchMethodError var15) {
				try {
					packetWrapperConstructor = PacketWrapper.class.getDeclaredConstructor(DefinedPacket.class, ByteBuf.class);
					pkt = packetWrapperConstructor.newInstance(packet, buf);
					buf.retain();
					return pkt;
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var13) {
					throw new RuntimeException(var13);
				}
			}
		}finally {
			if(!isInputDirect) {
				buf.release();
			}
		}
	}
}

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.lang.reflect.Method;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol.EaglerBungeeProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol.EaglerProtocolAccessProxy;
import net.md_5.bungee.protocol.DefinedPacket;
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
public class EaglerMinecraftEncoder extends MessageToMessageEncoder<DefinedPacket> {
	
	private EaglerBungeeProtocol protocol;
	private boolean server;
	private int protocolVersion;
	private static Method meth = null;
	
	@Override
	protected void encode(ChannelHandlerContext ctx, DefinedPacket msg, List<Object> out) throws Exception {
		Protocol bungeeProtocol = null;
		switch(this.protocol) {
			case GAME:
				bungeeProtocol = Protocol.GAME;
				break;
			case HANDSHAKE:
				bungeeProtocol = Protocol.HANDSHAKE;
				break;
			case LOGIN:
				bungeeProtocol = Protocol.LOGIN;
				break;
			case STATUS:
				bungeeProtocol = Protocol.STATUS;
		}
		ByteBuf buf = ctx.alloc().buffer();
		int pk = EaglerProtocolAccessProxy.getPacketId(protocol, protocolVersion, msg, server);
		DefinedPacket.writeVarInt(pk, buf);
		try {
			msg.write(buf, bungeeProtocol, server ? Direction.TO_CLIENT : Direction.TO_SERVER, protocolVersion);
		} catch (NoSuchMethodError e) {
			try {
				if (meth == null) {
					meth = DefinedPacket.class.getDeclaredMethod("write", ByteBuf.class, Direction.class, int.class);
				}
				meth.invoke(msg, buf, server ? Direction.TO_CLIENT : Direction.TO_SERVER, protocolVersion);
			} catch (Exception e1) {
				buf.release();
				throw new RuntimeException("Could not call DefinedPacket write method!", e1);
			}
		} catch (Exception e) {
			buf.release();
			throw new RuntimeException("Could not call DefinedPacket write method!", e);
		}
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

	public EaglerBungeeProtocol getProtocol() {
		return this.protocol;
	}

}

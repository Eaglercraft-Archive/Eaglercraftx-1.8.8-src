package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import io.netty.channel.ChannelHandlerContext;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol.EaglerBungeeProtocol;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.Protocol;

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
public class EaglerChannelWrapper extends ChannelWrapper {

	public EaglerChannelWrapper(ChannelHandlerContext ctx) {
		super(ctx);
	}

	public void setProtocol(EaglerBungeeProtocol protocol) {
		getHandle().pipeline().get(EaglerMinecraftEncoder.class).setProtocol(protocol);
		getHandle().pipeline().get(EaglerMinecraftDecoder.class).setProtocol(protocol);
	}
	
	public void setVersion(int protocol) {
		getHandle().pipeline().get(EaglerMinecraftEncoder.class).setProtocolVersion(protocol);
		getHandle().pipeline().get(EaglerMinecraftDecoder.class).setProtocolVersion(protocol);
	}

	private Protocol lastProtocol = null;

	public Protocol getEncodeProtocol() {
		EaglerMinecraftEncoder enc;
		if (this.getHandle() == null || (enc = this.getHandle().pipeline().get(EaglerMinecraftEncoder.class)) == null) return lastProtocol;
		EaglerBungeeProtocol eaglerProtocol = enc.getProtocol();
		switch(eaglerProtocol) {
			case GAME:
				return (lastProtocol = Protocol.GAME);
			case HANDSHAKE:
				return (lastProtocol = Protocol.HANDSHAKE);
			case LOGIN:
				return (lastProtocol = Protocol.LOGIN);
			case STATUS:
				return (lastProtocol = Protocol.STATUS);
			default:
				return lastProtocol;
		}
	}
	
	public void close(Object o) {
		super.close(o);
		EaglerPipeline.closeChannel(getHandle());
	}
	
}

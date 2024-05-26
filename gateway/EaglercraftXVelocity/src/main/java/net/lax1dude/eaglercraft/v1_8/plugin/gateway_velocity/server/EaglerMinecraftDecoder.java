package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) {
		if(!ctx.channel().isActive()) {
			return;
		}
		EaglerConnectionInstance con = ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get();
		long millis = System.currentTimeMillis();
		if(frame instanceof BinaryWebSocketFrame) {
			out.add(frame.content().retain());
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
}

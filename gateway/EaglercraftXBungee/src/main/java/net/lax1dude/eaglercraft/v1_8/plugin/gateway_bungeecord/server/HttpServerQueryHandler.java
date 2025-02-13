/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query.QueryManager;

public abstract class HttpServerQueryHandler extends ChannelInboundHandlerAdapter {

	public static class UnexpectedDataException extends RuntimeException {
		public UnexpectedDataException() {
		}
		public UnexpectedDataException(String message, Throwable cause) {
			super(message, cause);
		}
		public UnexpectedDataException(String message) {
			super(message);
		}
		public UnexpectedDataException(Throwable cause) {
			super(cause);
		}
	}

	private static final InetAddress localhost;
	
	static {
		try {
			localhost = InetAddress.getLocalHost();
		}catch(Throwable t) {
			throw new RuntimeException("localhost doesn't exist?!", t);
		}
	}

	private EaglerListenerConfig conf;
	private ChannelHandlerContext context;
	private String accept;
	private boolean acceptTextPacket = false;
	private boolean acceptBinaryPacket = false;
	private boolean hasClosed = false;
	private boolean keepAlive = false;
	private long maxAge = -1l;

	public void beginHandleQuery(EaglerListenerConfig conf, ChannelHandlerContext context, String accept) {
		this.conf = conf;
		this.context = context;
		this.accept = accept;
		begin(accept);
	}

	protected void acceptText() {
		acceptText(true);
	}

	protected void acceptText(boolean bool) {
		acceptTextPacket = bool;
	}

	protected void acceptBinary() {
		acceptBinary(true);
	}

	protected void acceptBinary(boolean bool) {
		acceptBinaryPacket = bool;
	}

	public void close() {
		context.close();
		hasClosed = true;
	}

	public boolean isClosed() {
		return hasClosed;
	}

	public InetAddress getAddress() {
		InetAddress addr = context.channel().attr(EaglerPipeline.REAL_ADDRESS).get();
		if(addr != null) {
			return addr;
		}else {
			SocketAddress sockAddr = context.channel().remoteAddress();
			return sockAddr instanceof InetSocketAddress ? ((InetSocketAddress) sockAddr).getAddress() : localhost;
		}
	}

	public ChannelHandlerContext getContext() {
		return context;
	}

	public EaglerListenerConfig getListener() {
		return conf;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if(msg instanceof WebSocketFrame) {
				if(msg instanceof BinaryWebSocketFrame) {
					handleBinary(ctx, ((BinaryWebSocketFrame)msg).content());
				}else if(msg instanceof TextWebSocketFrame) {
					handleText(ctx, ((TextWebSocketFrame)msg).text());
				}else if(msg instanceof PingWebSocketFrame) {
					ctx.writeAndFlush(new PongWebSocketFrame());
				}else if(msg instanceof CloseWebSocketFrame) {
					ctx.close();
				}
			}else if(msg instanceof HAProxyMessage) {
				EaglerXBungee.logger().warning("[" + ctx.channel().remoteAddress() + "]: Ignoring HAProxyMessage because the WebSocket connection has already been established");
			}else {
				EaglerXBungee.logger().severe("Unexpected Packet: " + msg.getClass().getSimpleName());
			}
		}finally {
			ReferenceCountUtil.release(msg);
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (ctx.channel().isActive()) {
			EaglerXBungee.logger().warning("[" + ctx.channel().remoteAddress() + "]: Exception Caught: " + cause.toString());
		}
	}

	private void handleBinary(ChannelHandlerContext ctx, ByteBuf buffer) {
		if(!acceptBinaryPacket) {
			ctx.close();
			return;
		}
		byte[] packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		processBytes(packet);
	}

	private void handleText(ChannelHandlerContext ctx, String str) {
		if(!acceptTextPacket) {
			ctx.close();
			return;
		}
		JsonObject obj = null;
		if(str.indexOf('{') == 0) {
			try {
				obj = JsonParser.parseString(str).getAsJsonObject();
			}catch(JsonParseException ex) {
			}
		}
		if(obj != null) {
			processJson(obj);
		}else {
			processString(str);
		}
	}

	public void channelInactive(ChannelHandlerContext ctx) {
		EaglerPipeline.closeChannel(ctx.channel());
		hasClosed = true;
		closed();
	}

	public String getAccept() {
		return accept;
	}

	public String getOrigin() {
		return context.channel().attr(EaglerPipeline.ORIGIN).get();
	}

	public String getUserAgent() {
		return context.channel().attr(EaglerPipeline.USER_AGENT).get();
	}

	public void sendStringResponse(String type, String str) {
		context.writeAndFlush(new TextWebSocketFrame(QueryManager.createStringResponse(accept, str).toString()));
	}

	public void sendStringResponseAndClose(String type, String str) {
		context.writeAndFlush(new TextWebSocketFrame(QueryManager.createStringResponse(accept, str).toString())).addListener(ChannelFutureListener.CLOSE);
	}

	public void sendJsonResponse(String type, JsonObject obj) {
		context.writeAndFlush(new TextWebSocketFrame(QueryManager.createJsonObjectResponse(accept, obj).toString()));
	}

	public void sendJsonResponseAndClose(String type, JsonObject obj) {
		context.writeAndFlush(new TextWebSocketFrame(QueryManager.createJsonObjectResponse(accept, obj).toString())).addListener(ChannelFutureListener.CLOSE);
	}

	public void sendBinaryResponse(byte[] bytes) {
		context.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(bytes)));
	}

	public void sendBinaryResponseAndClose(byte[] bytes) {
		context.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(bytes))).addListener(ChannelFutureListener.CLOSE);
	}

	public void setKeepAlive(boolean enable) {
		keepAlive = enable;
	}

	public boolean shouldKeepAlive() {
		return keepAlive;
	}

	public long getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(long millis) {
		this.maxAge = millis;
	}

	protected abstract void begin(String queryType);

	protected abstract void processString(String str);

	protected abstract void processJson(JsonObject obj);

	protected abstract void processBytes(byte[] bytes);

	protected abstract void closed();

}
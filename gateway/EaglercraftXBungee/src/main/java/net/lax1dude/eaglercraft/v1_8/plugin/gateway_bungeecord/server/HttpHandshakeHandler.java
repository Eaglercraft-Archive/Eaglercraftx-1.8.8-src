package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerRateLimiter;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.RateLimitStatus;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpMemoryCache;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpWebServer;

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
public class HttpHandshakeHandler extends ChannelInboundHandlerAdapter {
	
	private static final byte[] error429Bytes = "<h3>429 Too Many Requests<br /><small>(Try again later)</small></h3>".getBytes(StandardCharsets.UTF_8);
	
	private final EaglerListenerConfig conf;
	
	public HttpHandshakeHandler(EaglerListenerConfig conf) {
		this.conf = conf;
	}
	
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			EaglerConnectionInstance pingTracker = ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get();
			HttpRequest req = (HttpRequest) msg;
			HttpHeaders headers = req.headers();
			
			String rateLimitHost = null;
			
			if(conf.isForwardIp()) {
				String str = headers.get(conf.getForwardIpHeader());
				if(str != null) {
					rateLimitHost = str.split(",", 2)[0];
					try {
						ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).set(InetAddress.getByName(rateLimitHost));
					}catch(UnknownHostException ex) {
						EaglerXBungee.logger().warning("[" + ctx.channel().remoteAddress() + "]: Connected with an invalid '" + conf.getForwardIpHeader() + "' header, disconnecting...");
						ctx.close();
						return;
					}
				}else {
					EaglerXBungee.logger().warning("[" + ctx.channel().remoteAddress() + "]: Connected without a '" + conf.getForwardIpHeader() + "' header, disconnecting...");
					ctx.close();
					return;
				}
			}else {
				SocketAddress addr = ctx.channel().remoteAddress();
				if(addr instanceof InetSocketAddress) {
					rateLimitHost = ((InetSocketAddress) addr).getAddress().getHostAddress();
				}
			}
			
			EaglerRateLimiter ipRateLimiter = conf.getRatelimitIp();
			RateLimitStatus ipRateLimit = RateLimitStatus.OK;
			
			if(ipRateLimiter != null && rateLimitHost != null) {
				ipRateLimit = ipRateLimiter.rateLimit(rateLimitHost);
			}
			
			if(ipRateLimit == RateLimitStatus.LOCKED_OUT) {
				ctx.close();
				return;
			}
			
			if(headers.get(HttpHeaderNames.CONNECTION) != null && headers.get(HttpHeaderNames.CONNECTION).toLowerCase().contains("upgrade") &&
					"websocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {
				
				String origin = headers.get(HttpHeaderNames.ORIGIN);
				if(origin != null) {
					ctx.channel().attr(EaglerPipeline.ORIGIN).set(origin);
				}
				
				//TODO: origin blacklist
				
				if(ipRateLimit == RateLimitStatus.OK) {
					ctx.channel().attr(EaglerPipeline.HOST).set(headers.get(HttpHeaderNames.HOST));
					ctx.pipeline().replace(this, "HttpWebSocketHandler", new HttpWebSocketHandler(conf));
				}
				
				WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
						"ws://" + headers.get(HttpHeaderNames.HOST) + req.uri(), null, true, 0xFFFFF);
				WebSocketServerHandshaker hs = wsFactory.newHandshaker(req);
				if(hs != null) {
					pingTracker.isWebSocket = true;
					ChannelFuture future = hs.handshake(ctx.channel(), req);
					if(ipRateLimit != RateLimitStatus.OK) {
						final RateLimitStatus rateLimitTypeFinal = ipRateLimit;
						future.addListener(new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture paramF) throws Exception {
								ctx.writeAndFlush(new TextWebSocketFrame(
										rateLimitTypeFinal == RateLimitStatus.LIMITED_NOW_LOCKED_OUT ? "LOCKED" : "BLOCKED"))
										.addListener(ChannelFutureListener.CLOSE);
							}
						});
					}
				}else {
					WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel()).addListener(ChannelFutureListener.CLOSE);
				}
			}else {
				if(ipRateLimit != RateLimitStatus.OK) {
					ByteBuf error429Buffer = ctx.alloc().buffer(error429Bytes.length, error429Bytes.length);
					error429Buffer.writeBytes(error429Bytes);
					DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TOO_MANY_REQUESTS, error429Buffer);
					ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
					return;
				}
				pingTracker.isRegularHttp = true;
				HttpWebServer srv = conf.getWebServer();
				if(srv != null) {
					String uri = req.uri();
					if(uri.startsWith("/")) {
						uri = uri.substring(1);
					}
					int j = uri.indexOf('?');
					if(j != -1) {
						uri = uri.substring(0, j);
					}
					HttpMemoryCache ch = srv.retrieveFile(uri);
					if(ch != null) {
						ctx.writeAndFlush(ch.createHTTPResponse()).addListener(ChannelFutureListener.CLOSE);
					}else {
						ctx.writeAndFlush(HttpWebServer.getWebSocket404().createHTTPResponse(HttpResponseStatus.NOT_FOUND))
							.addListener(ChannelFutureListener.CLOSE);
					}
				}else {
					ctx.writeAndFlush(HttpWebServer.getWebSocket404().createHTTPResponse(HttpResponseStatus.NOT_FOUND))
						.addListener(ChannelFutureListener.CLOSE);
				}
			}
		}else {
			ctx.close();
		}
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (ctx.channel().isActive()) {
			EaglerXBungee.logger().log(Level.WARNING, "[Pre][" + ctx.channel().remoteAddress() + "]: Exception Caught: " + cause.toString(), cause);
			ctx.close();
		}
	}
	
	private static String formatAddressFor404(String str) {
		return "<span style=\"font-family:monospace;font-weight:bold;background-color:#EEEEEE;padding:3px 4px;\">" + str.replace("<", "&lt;").replace(">", "&gt;") + "</span>";
	}
	
	public void channelInactive(ChannelHandlerContext ctx) {
		EaglerPipeline.closeChannel(ctx.channel());
	}
	
}

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.ReferenceCountUtil;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftWebSocketOpenEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerRateLimiter;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.RateLimitStatus;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.web.HttpMemoryCache;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.web.HttpWebServer;

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
public class HttpHandshakeHandler extends ChannelInboundHandlerAdapter {
	
	private static final byte[] error429Bytes = "<h3>429 Too Many Requests<br /><small>(Try again later)</small></h3>".getBytes(StandardCharsets.UTF_8);
	
	private final EaglerListenerConfig conf;
	private boolean logExceptions;
	private boolean healthCheck;
	private InetSocketAddress haproxyRemoteAddr;
	
	public HttpHandshakeHandler(EaglerListenerConfig conf) {
		this.conf = conf;
	}
	
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg instanceof HttpRequest) {
				logExceptions = true;
				EaglerConnectionInstance pingTracker = ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get();
				HttpRequest req = (HttpRequest) msg;
				HttpHeaders headers = req.headers();

				String rateLimitHost = null;

				SocketAddress addr;
				if (conf.isForwardIp()) {
					String str = headers.get(conf.getForwardIpHeader());
					if (str != null) {
						rateLimitHost = str.split(",", 2)[0];
						try {
							InetAddress inetAddr = InetAddress.getByName(rateLimitHost);
							if(haproxyRemoteAddr != null) {
								addr = new InetSocketAddress(inetAddr, haproxyRemoteAddr.getPort());
							}else {
								addr = ctx.channel().remoteAddress();
								if(addr instanceof InetSocketAddress) {
									addr = new InetSocketAddress(inetAddr, ((InetSocketAddress)addr).getPort());
								}else {
									addr = new InetSocketAddress(inetAddr, 0);
								}
							}
							ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).set(inetAddr);
						} catch (UnknownHostException ex) {
							EaglerXVelocity.logger().warn("[{}]: Connected with an invalid '{}' header, disconnecting...", ctx.channel().remoteAddress(), conf.getForwardIpHeader());
							ctx.close();
							return;
						}
					} else {
						EaglerXVelocity.logger().warn("[{}]: Connected without a '{}' header, disconnecting...", ctx.channel().remoteAddress(), conf.getForwardIpHeader());
						ctx.close();
						return;
					}
				} else if(haproxyRemoteAddr != null) {
					addr = haproxyRemoteAddr;
					ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).set(haproxyRemoteAddr.getAddress());
				} else {
					addr = ctx.channel().remoteAddress();
					if (addr instanceof InetSocketAddress) {
						rateLimitHost = ((InetSocketAddress) addr).getAddress().getHostAddress();
					}
				}

				EaglerRateLimiter ipRateLimiter = conf.getRatelimitIp();
				RateLimitStatus ipRateLimit = RateLimitStatus.OK;

				if (ipRateLimiter != null && rateLimitHost != null) {
					ipRateLimit = ipRateLimiter.rateLimit(rateLimitHost);
				}

				if (ipRateLimit == RateLimitStatus.LOCKED_OUT) {
					ctx.close();
					return;
				}

				if (headers.get(HttpHeaderNames.CONNECTION) != null && headers.get(HttpHeaderNames.CONNECTION).toLowerCase().contains("upgrade") &&
						"websocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {

					String origin = headers.get(HttpHeaderNames.ORIGIN);
					if (origin != null) {
						ctx.channel().attr(EaglerPipeline.ORIGIN).set(origin);
					}
					String userAgent = headers.get(HttpHeaderNames.USER_AGENT);
					if(userAgent != null) {
						ctx.channel().attr(EaglerPipeline.USER_AGENT).set(userAgent);
					}

					if (ipRateLimit == RateLimitStatus.OK) {
						EaglercraftWebSocketOpenEvent evt = new EaglercraftWebSocketOpenEvent(ctx.channel(), conf, rateLimitHost, origin, userAgent);
						try {
							evt = EaglerXVelocity.proxy().getEventManager().fire(evt).join();
						}catch(Throwable t) {
							ctx.close();
							return;
						}
						if(evt.isCancelled()) {
							ctx.close();
							return;
						}
						ctx.channel().attr(EaglerPipeline.HOST).set(headers.get(HttpHeaderNames.HOST));
						ctx.pipeline().replace(this, "HttpWebSocketHandler", new HttpWebSocketHandler(conf));
					}

					WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
							"ws://" + headers.get(HttpHeaderNames.HOST) + req.uri(), null, true, 0xFFFFF);
					WebSocketServerHandshaker hs = wsFactory.newHandshaker(req);
					if (hs != null) {
						pingTracker.isWebSocket = true;
						ChannelFuture future = hs.handshake(ctx.channel(), req);
						if (ipRateLimit != RateLimitStatus.OK) {
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
					} else {
						WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel()).addListener(ChannelFutureListener.CLOSE);
					}
				} else {
					if (ipRateLimit != RateLimitStatus.OK) {
						ByteBuf error429Buffer = ctx.alloc().buffer(error429Bytes.length, error429Bytes.length);
						error429Buffer.writeBytes(error429Bytes);
						DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TOO_MANY_REQUESTS, error429Buffer);
						ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
						return;
					}
					pingTracker.isRegularHttp = true;
					HttpWebServer srv = conf.getWebServer();
					if (srv != null) {
						String uri = req.uri();
						if (uri.startsWith("/")) {
							uri = uri.substring(1);
						}
						int j = uri.indexOf('?');
						if (j != -1) {
							uri = uri.substring(0, j);
						}
						HttpMemoryCache ch = srv.retrieveFile(uri);
						if (ch != null) {
							ctx.writeAndFlush(ch.createHTTPResponse()).addListener(ChannelFutureListener.CLOSE);
						} else {
							ctx.writeAndFlush(HttpWebServer.getWebSocket404().createHTTPResponse(HttpResponseStatus.NOT_FOUND))
									.addListener(ChannelFutureListener.CLOSE);
						}
					} else {
						ctx.writeAndFlush(HttpWebServer.getWebSocket404().createHTTPResponse(HttpResponseStatus.NOT_FOUND))
								.addListener(ChannelFutureListener.CLOSE);
					}
				}
			} else if (msg instanceof HAProxyMessage) {
				logExceptions = true;
				HAProxyMessage proxy = (HAProxyMessage) msg;
				if(proxy.sourceAddress() != null) {
					if(!conf.isForwardIp()) {
						try {
							haproxyRemoteAddr = new InetSocketAddress(proxy.sourceAddress(), proxy.sourcePort());
						}catch(IllegalArgumentException t) {
						}
					}
				}else {
					healthCheck = true;
				}
			} else {
				ctx.close();
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (ctx.channel().isActive()) {
			if(logExceptions && !healthCheck) {
				EaglerXVelocity.logger().warn("[Pre][{}]: Exception Caught: {}", ctx.channel().remoteAddress(), cause.toString(), cause);
			}
			ctx.close();
		}
	}
	
	public void channelInactive(ChannelHandlerContext ctx) {
		EaglerPipeline.closeChannel(ctx.channel());
	}
	
}

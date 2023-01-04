package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.net.ssl.SSLEngine;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.md_5.bungee.netty.PipelineUtils;

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
public class BinaryHttpClient {

	public static class Response {

		public final int code;
		public final byte[] data;
		public final Throwable exception;

		public Response(int code, byte[] data) {
			this.code = code;
			this.data = data;
			this.exception = null;
		}

		public Response(Throwable exception) {
			this.code = -1;
			this.data = null;
			this.exception = exception;
		}

	}

	private static class NettyHttpChannelFutureListener implements ChannelFutureListener {

		protected final String method;
		protected final URI requestURI;
		protected final Consumer<Response> responseCallback;

		protected NettyHttpChannelFutureListener(String method, URI requestURI, Consumer<Response> responseCallback) {
			this.method = method;
			this.requestURI = requestURI;
			this.responseCallback = responseCallback;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if (future.isSuccess()) {
				String path = requestURI.getRawPath()
						+ ((requestURI.getRawQuery() == null) ? "" : ("?" + requestURI.getRawQuery()));
				HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
						HttpMethod.valueOf(method), path);
				request.headers().set(HttpHeaderNames.HOST, (Object) requestURI.getHost());
				request.headers().set(HttpHeaderNames.USER_AGENT, "Mozilla/5.0 EaglerXBungee/" + EaglerXBungee.getEagler().getDescription().getVersion());
				future.channel().writeAndFlush(request);
			} else {
				addressCache.invalidate(requestURI.getHost());
				responseCallback.accept(new Response(new IOException("Connection failed")));
			}
		}

	}

	private static class NettyHttpChannelInitializer extends ChannelInitializer<Channel> {

		protected final Consumer<Response> responseCallback;
		protected final boolean ssl;
		protected final String host;
		protected final int port;

		protected NettyHttpChannelInitializer(Consumer<Response> responseCallback, boolean ssl, String host, int port) {
			this.responseCallback = responseCallback;
			this.ssl = ssl;
			this.host = host;
			this.port = port;
		}

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ch.pipeline().addLast("timeout", new ReadTimeoutHandler(5L, TimeUnit.SECONDS));
			if (this.ssl) {
				SSLEngine engine = SslContextBuilder.forClient().build().newEngine(ch.alloc(), host, port);
				ch.pipeline().addLast("ssl", new SslHandler(engine));
			}

			ch.pipeline().addLast("http", new HttpClientCodec());
			ch.pipeline().addLast("handler", new NettyHttpResponseHandler(responseCallback));
		}

	}
	
	private static class NettyHttpResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

		protected final Consumer<Response> responseCallback;
		protected int responseCode = -1;
		protected ByteBuf buffer = null;

		protected NettyHttpResponseHandler(Consumer<Response> responseCallback) {
			this.responseCallback = responseCallback;
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
			if (msg instanceof HttpResponse) {
				HttpResponse response = (HttpResponse) msg;
				responseCode = response.status().code();
				if (responseCode == HttpResponseStatus.NO_CONTENT.code()) {
					this.done(ctx);
					return;
				}
			}
			if (msg instanceof HttpContent) {
				HttpContent content = (HttpContent) msg;
				if(buffer == null) {
					buffer = ctx.alloc().buffer();
				}
				this.buffer.writeBytes(content.content());
				if (msg instanceof LastHttpContent) {
					this.done(ctx);
				}
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			responseCallback.accept(new Response(cause));
		}

		private void done(ChannelHandlerContext ctx) {
			try {
				byte[] array;
				if(buffer != null) {
					array = new byte[buffer.readableBytes()];
					buffer.readBytes(array);
					buffer.release();
				}else {
					array = new byte[0];
				}
				responseCallback.accept(new Response(responseCode, array));
			}finally {
				ctx.channel().pipeline().remove(this);
				ctx.channel().close();
			}
		}

	}

	private static final Cache<String, InetAddress> addressCache = CacheBuilder.newBuilder().expireAfterWrite(15L, TimeUnit.MINUTES).build();
	private static EventLoopGroup eventLoop = null;

	public static void asyncRequest(String method, URI uri, Consumer<Response> responseCallback) {
		EventLoopGroup eventLoop = getEventLoopGroup();
		
		int port = uri.getPort();
		boolean ssl = false;
		String scheme = uri.getScheme();
		switch(scheme) {
		case "http":
			if(port == -1) {
				port = 80;
			}
			break;
		case "https":
			if(port == -1) {
				port = 443;
			}
			ssl = true;
			break;
		default:
			responseCallback.accept(new Response(new UnsupportedOperationException("Unsupported scheme: " + scheme)));
			return;
		}
		
		String host = uri.getHost();
		InetAddress inetHost = addressCache.getIfPresent(host);
		if (inetHost == null) {
			try {
				inetHost = InetAddress.getByName(host);
			} catch (UnknownHostException ex) {
				responseCallback.accept(new Response(ex));
				return;
			}
			addressCache.put(host, inetHost);
		}
		
		(new Bootstrap()).channel(PipelineUtils.getChannel(null)).group(eventLoop)
				.handler(new NettyHttpChannelInitializer(responseCallback, ssl, host, port))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000).option(ChannelOption.TCP_NODELAY, true)
				.remoteAddress(inetHost, port).connect()
				.addListener(new NettyHttpChannelFutureListener(method, uri, responseCallback));
	}

	private static EventLoopGroup getEventLoopGroup() {
		if(eventLoop == null) {
			eventLoop = PipelineUtils.newEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Skin Download Thread #%1$d").build());
		}
		return eventLoop;
	}

	public static void killEventLoop() {
		if(eventLoop != null) {
			EaglerXBungee.logger().info("Stopping skin cache HTTP client...");
			eventLoop.shutdownGracefully();
			try {
				eventLoop.awaitTermination(30l, TimeUnit.SECONDS);
			} catch (InterruptedException var13) {
				;
			}
			eventLoop = null;
		}
	}

}

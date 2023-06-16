package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.logging.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.DeflateFrameServerExtensionHandshaker;
import io.netty.handler.codec.http.websocketx.extensions.compression.PerMessageDeflateServerExtensionHandshaker;
import io.netty.util.AttributeKey;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
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
public class EaglerPipeline {

	public static final AttributeKey<EaglerListenerConfig> LISTENER = AttributeKey.valueOf("ListenerInfo");
	public static final AttributeKey<InetSocketAddress> LOCAL_ADDRESS = AttributeKey.valueOf("LocalAddress");
	public static final AttributeKey<EaglerConnectionInstance> CONNECTION_INSTANCE = AttributeKey.valueOf("EaglerConnectionInstance");
	public static final AttributeKey<InetAddress> REAL_ADDRESS = AttributeKey.valueOf("RealAddress");
	public static final AttributeKey<String> HOST = AttributeKey.valueOf("Host");
	public static final AttributeKey<String> ORIGIN = AttributeKey.valueOf("Origin");

	public static final Collection<Channel> openChannels = new LinkedList();

	public static final TimerTask closeInactive = new TimerTask() {

		@Override
		public void run() {
			Logger log = EaglerXBungee.logger();
			EaglerBungeeConfig conf = EaglerXBungee.getEagler().getConfig();
			long handshakeTimeout = conf.getWebsocketHandshakeTimeout();
			long keepAliveTimeout = conf.getWebsocketKeepAliveTimeout();
			synchronized(openChannels) {
				long millis = System.currentTimeMillis();
				Iterator<Channel> channelIterator = openChannels.iterator();
				while(channelIterator.hasNext()) {
					Channel c = channelIterator.next();
					final EaglerConnectionInstance i = c.attr(EaglerPipeline.CONNECTION_INSTANCE).get();
					long handshakeTimeoutForConnection = 500l;
					if(i.isRegularHttp) handshakeTimeoutForConnection = 10000l;
					if(i.isWebSocket) handshakeTimeoutForConnection = handshakeTimeout;
					if(i == null || (!i.hasBeenForwarded && millis - i.creationTime > handshakeTimeoutForConnection)
							|| millis - i.lastClientPongPacket > keepAliveTimeout || !c.isActive()) {
						if(c.isActive()) {
							c.close();
						}
						channelIterator.remove();
					}else {
						long pingRate = 5000l;
						if(pingRate + 700l > keepAliveTimeout) {
							pingRate = keepAliveTimeout - 500l;
							if(pingRate < 500l) {
								keepAliveTimeout = 500l;
							}
						}
						if(millis - i.lastServerPingPacket > pingRate) {
							i.lastServerPingPacket = millis;
							c.write(new PingWebSocketFrame());
						}
					}
				}
			}
			for(EaglerListenerConfig lst : conf.getServerListeners()) {
				HttpWebServer srv = lst.getWebServer();
				if(srv != null) {
					try {
						srv.flushCache();
					}catch(Throwable t) {
						log.severe("Failed to flush web server cache for: " + lst.getAddress().toString());
						t.printStackTrace();
					}
				}
			}
		}
		
	};
	
	public static final ChannelInitializer<Channel> SERVER_CHILD = new ChannelInitializer<Channel>() {

		@Override
		protected void initChannel(Channel channel) throws Exception {
			ChannelPipeline pipeline = channel.pipeline();
			pipeline.addLast("HttpServerCodec", new HttpServerCodec());
			pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(65535));
			int compressionLevel = EaglerXBungee.getEagler().getConfig().getHttpWebsocketCompressionLevel();
			if(compressionLevel > 0) {
				if(compressionLevel > 9) {
					compressionLevel = 9;
				}
				DeflateFrameServerExtensionHandshaker deflateExtensionHandshaker = new DeflateFrameServerExtensionHandshaker(
						compressionLevel);
				PerMessageDeflateServerExtensionHandshaker perMessageDeflateExtensionHandshaker = new PerMessageDeflateServerExtensionHandshaker(
						compressionLevel, ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(),
						PerMessageDeflateServerExtensionHandshaker.MAX_WINDOW_SIZE, false, false);
				pipeline.addLast("HttpCompressionHandler", new WebSocketServerExtensionHandler(deflateExtensionHandshaker,
						perMessageDeflateExtensionHandshaker));
			}
			pipeline.addLast("HttpHandshakeHandler", new HttpHandshakeHandler(channel.attr(LISTENER).get()));
			channel.attr(CONNECTION_INSTANCE).set(new EaglerConnectionInstance(channel));
			synchronized(openChannels) {
				openChannels.add(channel);
			}
		}
		
	};

	public static void closeChannel(Channel channel) {
		synchronized(openChannels) {
			openChannels.remove(channel);
		}
	}
	
}

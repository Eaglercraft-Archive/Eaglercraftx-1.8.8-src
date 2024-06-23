package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.WriteBufferWaterMark;
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
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler.ClientCertificateHolder;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpWebServer;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class EaglerPipeline {

	public static final AttributeKey<EaglerListenerConfig> LISTENER = AttributeKey.valueOf("ListenerInfo");
	public static final AttributeKey<InetSocketAddress> LOCAL_ADDRESS = AttributeKey.valueOf("LocalAddress");
	public static final AttributeKey<EaglerConnectionInstance> CONNECTION_INSTANCE = AttributeKey.valueOf("EaglerConnectionInstance");
	public static final AttributeKey<InetAddress> REAL_ADDRESS = AttributeKey.valueOf("RealAddress");
	public static final AttributeKey<String> HOST = AttributeKey.valueOf("Host");
	public static final AttributeKey<String> ORIGIN = AttributeKey.valueOf("Origin");
	public static final int LOW_MARK = Integer.getInteger("net.md_5.bungee.low_mark", 524288);
	public static final int HIGH_MARK = Integer.getInteger("net.md_5.bungee.high_mark", 2097152);
	public static final WriteBufferWaterMark MARK = new WriteBufferWaterMark(LOW_MARK, HIGH_MARK);

	public static final Collection<Channel> openChannels = new LinkedList();

	public static final String UPDATE_CERT_CHANNEL = "EAG|UpdateCert-1.8";

	public static final TimerTask closeInactive = new TimerTask() {

		@Override
		public void run() {
			Logger log = EaglerXBungee.logger();
			try {
				EaglerBungeeConfig conf = EaglerXBungee.getEagler().getConfig();
				long handshakeTimeout = conf.getWebsocketHandshakeTimeout();
				long keepAliveTimeout = conf.getWebsocketKeepAliveTimeout();
				long httpTimeout = conf.getBuiltinHttpServerTimeout();
				List<Channel> channelsList;
				synchronized(openChannels) {
					long millis = System.currentTimeMillis();
					Iterator<Channel> channelIterator = openChannels.iterator();
					while(channelIterator.hasNext()) {
						Channel c = channelIterator.next();
						final EaglerConnectionInstance i = c.attr(EaglerPipeline.CONNECTION_INSTANCE).get();
						long handshakeTimeoutForConnection = 500l;
						if(i.isRegularHttp) handshakeTimeoutForConnection = httpTimeout;
						else if(i.isWebSocket) handshakeTimeoutForConnection = handshakeTimeout;
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
					channelsList = new ArrayList(openChannels);
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
				if(!conf.getUpdateConfig().isBlockAllClientUpdates()) {
					int sizeTracker = 0;
					for(Channel c : channelsList) {
						EaglerConnectionInstance conn = c.attr(EaglerPipeline.CONNECTION_INSTANCE).get();
						if(conn.userConnection == null) {
							continue;
						}
						EaglerInitialHandler i = (EaglerInitialHandler)conn.userConnection.getPendingConnection();
						ClientCertificateHolder certHolder = null;
						synchronized(i.certificatesToSend) {
							if(i.certificatesToSend.size() > 0) {
								Iterator<ClientCertificateHolder> itr = i.certificatesToSend.iterator();
								certHolder = itr.next();
								itr.remove();
							}
						}
						if(certHolder != null) {
							int identityHash = certHolder.hashCode();
							boolean bb;
							synchronized(i.certificatesSent) {
								bb = i.certificatesSent.add(identityHash);
							}
							if(bb) {
								conn.userConnection.sendData(UPDATE_CERT_CHANNEL, certHolder.data);
								sizeTracker += certHolder.data.length;
								if(sizeTracker > (conf.getUpdateConfig().getCertPacketDataRateLimit() / 4)) {
									break;
								}
							}
						}
					}
					EaglerUpdateSvc.updateTick();	
				}
			}catch(Throwable t) {
				log.severe("Exception in thread \"" + Thread.currentThread().getName() + "\"! " + t.toString());
				t.printStackTrace();
			}
		}
	};
	
	public static final ChannelInitializer<Channel> SERVER_CHILD = new ChannelInitializer<Channel>() {

		@Override
		protected void initChannel(Channel channel) throws Exception {
			channel.config().setAllocator(PooledByteBufAllocator.DEFAULT).setWriteBufferWaterMark(MARK);
			try {
				channel.config().setOption(ChannelOption.IP_TOS, 24);
			} catch (ChannelException var3) {
			}
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

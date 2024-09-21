package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import com.velocitypowered.proxy.scheduler.VelocityScheduler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
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
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData.ClientCertificateHolder;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.web.HttpWebServer;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketUpdateCertEAG;

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
public class EaglerPipeline {

	public static final WriteBufferWaterMark SERVER_WRITE_MARK = new WriteBufferWaterMark(1048576, 2097152);
	public static final AttributeKey<EaglerListenerConfig> LISTENER = AttributeKey.valueOf("ListenerInfo");
	public static final AttributeKey<InetSocketAddress> LOCAL_ADDRESS = AttributeKey.valueOf("LocalAddress");
	public static final AttributeKey<EaglerConnectionInstance> CONNECTION_INSTANCE = AttributeKey.valueOf("EaglerConnectionInstance");
	public static final AttributeKey<InetAddress> REAL_ADDRESS = AttributeKey.valueOf("RealAddress");
	public static final AttributeKey<String> HOST = AttributeKey.valueOf("Host");
	public static final AttributeKey<String> ORIGIN = AttributeKey.valueOf("Origin");
	public static final AttributeKey<String> USER_AGENT = AttributeKey.valueOf("UserAgent");

	public static final Collection<Channel> openChannels = new LinkedList<>();

	public static final TimerTask closeInactive = new TimerTask() {

		@Override
		public void run() {
			Logger log = EaglerXVelocity.logger();
			try {
				EaglerVelocityConfig conf = EaglerXVelocity.getEagler().getConfig();
				long handshakeTimeout = conf.getWebsocketHandshakeTimeout();
				long keepAliveTimeout = conf.getWebsocketKeepAliveTimeout();
				long httpTimeout = conf.getBuiltinHttpServerTimeout();
				List<Channel> channelsList;
				synchronized(openChannels) {
					long millis = EaglerXVelocityAPIHelper.steadyTimeMillis();
					Iterator<Channel> channelIterator = openChannels.iterator();
					while(channelIterator.hasNext()) {
						Channel c = channelIterator.next();
						final EaglerConnectionInstance i = c.attr(EaglerPipeline.CONNECTION_INSTANCE).get();
						long handshakeTimeoutForConnection = 500l;
						if(i.isRegularHttp) handshakeTimeoutForConnection = httpTimeout;
						else if(i.isWebSocket) handshakeTimeoutForConnection = handshakeTimeout;
						boolean hasTimeout = !i.hasBeenForwarded;
						if(i.queryHandler != null) {
							long l = i.queryHandler.getMaxAge();
							hasTimeout = l != -1l;
							if(hasTimeout) {
								handshakeTimeoutForConnection = l;
							}
						}
						if((hasTimeout && millis - i.creationTime > handshakeTimeoutForConnection)
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
					channelsList = new ArrayList<>(openChannels);
				}
				for(EaglerListenerConfig lst : conf.getServerListeners()) {
					HttpWebServer srv = lst.getWebServer();
					if(srv != null) {
						try {
							srv.flushCache();
						}catch(Throwable t) {
							log.error("Failed to flush web server cache for: {}", lst.getAddress());
							t.printStackTrace();
						}
					}
				}
				final int serverInfoSendRate = Math.max(conf.getPauseMenuConf().getInfoSendRate(), 1);
				boolean blockAllClientUpdates = conf.getUpdateConfig().isBlockAllClientUpdates();
				final AtomicInteger sizeTracker = blockAllClientUpdates ? null : new AtomicInteger(0);
				final int rateLimitParam = conf.getUpdateConfig().getCertPacketDataRateLimit() / 4;
				VelocityScheduler sched = EaglerXVelocity.proxy().getScheduler();
				EaglerXVelocity plugin = EaglerXVelocity.getEagler();
				for(Channel c : channelsList) {
					final EaglerConnectionInstance conn = c.attr(EaglerPipeline.CONNECTION_INSTANCE).get();
					if(conn.userConnection == null) {
						continue;
					}
					final EaglerPlayerData i = conn.eaglerData;
					boolean certToSend = false;
					if(!blockAllClientUpdates) {
						synchronized(i.certificatesToSend) {
							if(!i.certificatesToSend.isEmpty()) {
								certToSend = true;
							}
						}
					}
					boolean serverInfoToSend = false;
					synchronized(i.serverInfoSendBuffer) {
						if(!i.serverInfoSendBuffer.isEmpty()) {
							serverInfoToSend = true;
						}
					}
					if(certToSend || serverInfoToSend) {
						final boolean do_certToSend = certToSend;
						final boolean do_serverInfoToSend = serverInfoToSend;
						sched.buildTask(plugin, () -> {
							if(do_certToSend) {
								ClientCertificateHolder certHolder = null;
								synchronized(i.certificatesToSend) {
									if(i.certificatesToSend.size() > 0) {
										Iterator<ClientCertificateHolder> itr = i.certificatesToSend.iterator();
										certHolder = itr.next();
										itr.remove();
									}
								}
								if(certHolder != null && sizeTracker.getAndAdd(certHolder.data.length) < rateLimitParam) {
									int identityHash = certHolder.hashCode();
									boolean bb;
									synchronized(i.certificatesSent) {
										bb = i.certificatesSent.add(identityHash);
									}
									if(bb) {
										conn.eaglerData.sendEaglerMessage(new SPacketUpdateCertEAG(certHolder.data));
									}
								}
							}
							if(do_serverInfoToSend) {
								List<GameMessagePacket> toSend = i.serverInfoSendBuffer;
								synchronized(toSend) {
									if(!toSend.isEmpty()) {
										try {
											if(serverInfoSendRate == 1) {
												i.getEaglerMessageController().sendPacketImmediately(toSend.remove(0));
											}else {
												for(int j = 0; j < serverInfoSendRate; ++j) {
													if(!toSend.isEmpty()) {
														i.getEaglerMessageController().sendPacketImmediately(toSend.remove(0));
													}else {
														break;
													}
												}
											}
										}catch(Throwable t) {
											log.error("Exception in thread  \"{}\"!", Thread.currentThread().getName(), t);
										}
									}
								}
							}
						}).schedule();
					}
				}
			}catch(Throwable t) {
				log.error("Exception in thread \"{}\"!", Thread.currentThread().getName(), t);
			}
		}
	};
	
	public static final ChannelInitializer<Channel> SERVER_CHILD = new ChannelInitializer<Channel>() {

		@Override
		protected void initChannel(Channel channel) throws Exception {
			ChannelPipeline pipeline = channel.pipeline();
			pipeline.addLast("HttpServerCodec", new HttpServerCodec());
			pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(65535));
			int compressionLevel = EaglerXVelocity.getEagler().getConfig().getHttpWebsocketCompressionLevel();
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
	
	public static EaglerPlayerData getEaglerHandle(Channel channel) {
		EaglerConnectionInstance i = channel.attr(EaglerPipeline.CONNECTION_INSTANCE).get();
		return i == null ? null : i.eaglerData;
	}
	
	public static EaglerPlayerData getEaglerHandle(Player player) {
		if(!(player instanceof ConnectedPlayer)) return null;
		return getEaglerHandle(((ConnectedPlayer)player).getConnection().getChannel());
	}
}

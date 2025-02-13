/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.DeflateFrameServerExtensionHandshaker;
import io.netty.handler.codec.http.websocketx.extensions.compression.PerMessageDeflateServerExtensionHandshaker;
import io.netty.util.AttributeKey;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler.ClientCertificateHolder;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.backend_rpc_protocol.BackendRPCSessionHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpWebServer;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketEnableFNAWSkinsEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketUpdateCertEAG;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.scheduler.BungeeScheduler;

public class EaglerPipeline {

	public static final AttributeKey<EaglerListenerConfig> LISTENER = AttributeKey.valueOf("ListenerInfo");
	public static final AttributeKey<InetSocketAddress> LOCAL_ADDRESS = AttributeKey.valueOf("LocalAddress");
	public static final AttributeKey<EaglerConnectionInstance> CONNECTION_INSTANCE = AttributeKey.valueOf("EaglerConnectionInstance");
	public static final AttributeKey<InetAddress> REAL_ADDRESS = AttributeKey.valueOf("RealAddress");
	public static final AttributeKey<String> HOST = AttributeKey.valueOf("Host");
	public static final AttributeKey<String> ORIGIN = AttributeKey.valueOf("Origin");
	public static final AttributeKey<String> USER_AGENT = AttributeKey.valueOf("UserAgent");
	public static final int LOW_MARK = Integer.getInteger("net.md_5.bungee.low_mark", 524288);
	public static final int HIGH_MARK = Integer.getInteger("net.md_5.bungee.high_mark", 2097152);
	public static final WriteBufferWaterMark MARK = new WriteBufferWaterMark(LOW_MARK, HIGH_MARK);

	public static final Collection<Channel> openChannels = new LinkedList<>();
	public static final Set<UserConnection> waitingServerConnections = new HashSet<>();

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
					long millis = EaglerXBungeeAPIHelper.steadyTimeMillis();
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
				List<UserConnection> readyServerConnections = null;
				synchronized(waitingServerConnections) {
					Iterator<UserConnection> connIterator = waitingServerConnections.iterator();
					while(connIterator.hasNext()) {
						UserConnection userCon = connIterator.next();
						if(userCon.isConnected()) {
							ServerConnection serverCon = userCon.getServer();
							if(serverCon != null) {
								if(readyServerConnections == null) {
									readyServerConnections = new ArrayList<>(4);
								}
								readyServerConnections.add(userCon);
								connIterator.remove();
							}
						}else {
							connIterator.remove();
						}
					}
				}
				if(readyServerConnections != null) {
					for(int i = 0, l = readyServerConnections.size(); i < l; ++i) {
						handleServerConnectionReady(readyServerConnections.get(i));
					}
				}
				boolean updateLoop = !conf.getUpdateConfig().isBlockAllClientUpdates();
				final AtomicInteger sizeTracker = updateLoop ? new AtomicInteger(0) : null;
				final int rateLimitParam = conf.getUpdateConfig().getCertPacketDataRateLimit() / 4;
				final int serverInfoSendRate = Math.max(conf.getPauseMenuConf().getInfoSendRate(), 1);
				BungeeScheduler sched = BungeeCord.getInstance().getScheduler();
				for(Channel c : channelsList) {
					EaglerConnectionInstance conn = c.attr(EaglerPipeline.CONNECTION_INSTANCE).get();
					if(conn.userConnection == null) {
						continue;
					}
					final EaglerInitialHandler i = (EaglerInitialHandler)conn.userConnection.getPendingConnection();
					boolean certToSend = false;
					if(updateLoop) {
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
						sched.runAsync(EaglerXBungee.getEagler(), () -> {
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
										i.sendEaglerMessage(new SPacketUpdateCertEAG(certHolder.data));
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
											log.log(Level.SEVERE, "Exception in thread  \"" + Thread.currentThread().getName() + "\"!", t);
										}
									}
								}
							}
						});
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
			EaglerListenerConfig listener = channel.attr(LISTENER).get();
			ChannelPipeline pipeline = channel.pipeline();
			if(listener.isHAProxyProtocol()) {
				pipeline.addLast("HAProxyMessageDecoder", new HAProxyMessageDecoder());
			}
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
			pipeline.addLast("HttpHandshakeHandler", new HttpHandshakeHandler(listener));
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

	public static void addServerConnectListener(UserConnection player) {
		synchronized(waitingServerConnections) {
			waitingServerConnections.add(player);
		}
	}

	private static void handleServerConnectionReady(UserConnection userConnection) {
		try {
			ServerConnection server = userConnection.getServer();
			server.sendData(BackendRPCSessionHandler.getReadyChNameFor(server), ArrayUtils.EMPTY_BYTE_ARRAY);
			if(userConnection.getPendingConnection() instanceof EaglerInitialHandler) {
				EaglerInitialHandler handler = (EaglerInitialHandler) userConnection.getPendingConnection();
				ServerInfo sv = server.getInfo();
				EaglerXBungee plugin = EaglerXBungee.getEagler();
				boolean fnawSkins = !plugin.getConfig().getDisableFNAWSkinsEverywhere()
						&& !plugin.getConfig().getDisableFNAWSkinsOnServersSet().contains(sv.getName());
				if(fnawSkins != handler.currentFNAWSkinEnableStatus.getAndSet(fnawSkins)) {
					handler.sendEaglerMessage(new SPacketEnableFNAWSkinsEAG(fnawSkins, false));
				}
				if(handler.getEaglerListenerConfig().getEnableVoiceChat()) {
					plugin.getVoiceService().handleServerConnected(userConnection, sv);
				}
			}
		}catch(Throwable t) {
			EaglerXBungee.logger().log(Level.SEVERE, "Failed to process server connection ready handler for player \""
					+ userConnection.getName() + "\"", t);
		}
	}

}
package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.event.connection.*;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.player.GameProfileRequestEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.network.HandshakeIntent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.crypto.IdentifiedKey;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.GameProfile.Property;
import com.velocitypowered.proxy.VelocityServer;
import com.velocitypowered.proxy.config.PlayerInfoForwarding;
import com.velocitypowered.proxy.connection.ConnectionType;
import com.velocitypowered.proxy.connection.ConnectionTypes;
import com.velocitypowered.proxy.connection.MinecraftConnection;
import com.velocitypowered.proxy.connection.client.*;
import com.velocitypowered.proxy.network.Connections;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import com.velocitypowered.proxy.protocol.StateRegistry;
import com.velocitypowered.proxy.protocol.netty.*;
import com.velocitypowered.proxy.protocol.packet.HandshakePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.ReferenceCountUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocityVersion;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.JSONLegacySerializer;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftClientBrandEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftHandleAuthCookieEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftHandleAuthPasswordEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftIsAuthRequiredEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftMOTDEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftRegisterCapeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftRegisterSkinEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftIsAuthRequiredEvent.AuthMethod;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftIsAuthRequiredEvent.AuthResponse;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command.CommandConfirmCode;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.*;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.protocol.GameProtocolMessageController;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.query.MOTDQueryHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.query.QueryManager;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.CapePackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinPackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketCustomizePauseMenuV4EAG;

import static com.velocitypowered.proxy.network.Connections.*;

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
public class HttpWebSocketHandler extends ChannelInboundHandlerAdapter {

	private static final Constructor<InitialInboundConnection> stupidConstructor;
	private static final Field remoteAddressField;
	private static final Field stateField;
	private static final Field protocolVersionField;
	private static final Constructor<LoginInboundConnection> stupid2Constructor;
	private static final Method loginEventFiredMethod;
	private static final Constructor<ConnectedPlayer> stupid3Constructor;
	private static final Constructor<ConnectedPlayer> stupid3Constructor_new;
	private static final Method setPermissionFunctionMethod;
	private static final Field defaultPermissionsField;
	private static final Constructor<InitialConnectSessionHandler> stupid4Constructor;
	static {
		try {
			stupidConstructor = InitialInboundConnection.class.getDeclaredConstructor(MinecraftConnection.class, String.class, HandshakePacket.class);
			stupidConstructor.setAccessible(true);
			remoteAddressField = MinecraftConnection.class.getDeclaredField("remoteAddress");
			remoteAddressField.setAccessible(true);
			stateField = MinecraftConnection.class.getDeclaredField("state");
			stateField.setAccessible(true);
			protocolVersionField = MinecraftConnection.class.getDeclaredField("protocolVersion");
			protocolVersionField.setAccessible(true);
			stupid2Constructor = LoginInboundConnection.class.getDeclaredConstructor(InitialInboundConnection.class);
			stupid2Constructor.setAccessible(true);
			loginEventFiredMethod = LoginInboundConnection.class.getDeclaredMethod("loginEventFired", Runnable.class);
			loginEventFiredMethod.setAccessible(true);
			Constructor<ConnectedPlayer> c3 = null;
			Constructor<ConnectedPlayer> c3_new = null;
			try {
				c3_new = ConnectedPlayer.class.getDeclaredConstructor(VelocityServer.class, GameProfile.class, MinecraftConnection.class, InetSocketAddress.class, String.class, boolean.class, IdentifiedKey.class);
				c3_new.setAccessible(true);
			} catch (NoSuchMethodException e) {
				c3 = ConnectedPlayer.class.getDeclaredConstructor(VelocityServer.class, GameProfile.class, MinecraftConnection.class, InetSocketAddress.class, boolean.class, IdentifiedKey.class);
				c3.setAccessible(true);
				c3_new = null;
			}
			stupid3Constructor = c3;
			stupid3Constructor_new = c3_new;
			setPermissionFunctionMethod = ConnectedPlayer.class.getDeclaredMethod("setPermissionFunction", PermissionFunction.class);
			setPermissionFunctionMethod.setAccessible(true);
			defaultPermissionsField = ConnectedPlayer.class.getDeclaredField("DEFAULT_PERMISSIONS");
			defaultPermissionsField.setAccessible(true);
			stupid4Constructor = InitialConnectSessionHandler.class.getDeclaredConstructor(ConnectedPlayer.class, VelocityServer.class);
			stupid4Constructor.setAccessible(true);
		} catch (NoSuchMethodException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	private final EaglerListenerConfig conf;

	private int clientLoginState = HandshakePacketTypes.STATE_OPENED;
	private int clientProtocolVersion = -1;
	private boolean isProtocolExchanged = false;
	private int gameProtocolVersion = -1;
	private String clientBrandString;
	private String clientVersionString;
	private String clientUsername;
	private UUID clientUUID;
	private String clientRequestedServer;
	private boolean clientAuth;
	private byte[] clientAuthUsername;
	private byte[] clientAuthPassword;
	private boolean clientEnableCookie;
	private byte[] clientCookieData;
	private EaglercraftIsAuthRequiredEvent authRequireEvent;
	private final Map<String, byte[]> profileData = new HashMap<>();
	private boolean hasFirstPacket = false;
	private boolean hasBinaryConnection = false;
	private boolean connectionClosed = false;
	private InetAddress remoteAddress;
	private String localAddrString; 
	private Property texturesOverrideProperty;
	private boolean overrideEaglerToVanillaSkins;

	private static final Set<String> profileDataStandard = Sets.newHashSet(
			"skin_v1", "skin_v2", "cape_v1", "update_cert_v1", "brand_uuid_v1");

	public HttpWebSocketHandler(EaglerListenerConfig conf) {
		this.conf = conf;
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg instanceof WebSocketFrame) {
				if (msg instanceof BinaryWebSocketFrame) {
					handleBinary(ctx, ((BinaryWebSocketFrame) msg).content());
				} else if (msg instanceof TextWebSocketFrame) {
					handleText(ctx, ((TextWebSocketFrame) msg).text());
				} else if (msg instanceof PingWebSocketFrame) {
					ctx.writeAndFlush(new PongWebSocketFrame());
				} else if (msg instanceof CloseWebSocketFrame) {
					ctx.close();
				}
			} else {
				EaglerXVelocity.logger().error("Unexpected Packet: {}", msg.getClass().getSimpleName());
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (ctx.channel().isActive()) {
			EaglerXVelocity.logger().warn("[Yee][{}]: Exception Caught: {}", ctx.channel().remoteAddress(), cause.toString());
		}
	}
	
	private void handleBinary(ChannelHandlerContext ctx, ByteBuf buffer) {
		if(connectionClosed) {
			return;
		}
		if(!hasFirstPacket) {
			if(buffer.readableBytes() >= 2) {
				if(buffer.getByte(0) == (byte)2 && buffer.getByte(1) == (byte)69) {
					handleLegacyClient(ctx, buffer);
					return;
				}
			}
			hasFirstPacket = true;
			hasBinaryConnection = true;
			
			VelocityServer bungus = EaglerXVelocity.proxy();
			
			if(conf.getMaxPlayer() > 0) {
				int i = 0;
				for(Player p : bungus.getAllPlayers()) {
					EaglerPlayerData d = EaglerPipeline.getEaglerHandle(p);
					if(d != null && d.getEaglerListenerConfig() == conf) {
						++i;
					}
				}
				
				if (i >= conf.getMaxPlayer()) {
					sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Proxy is full")
							.addListener(ChannelFutureListener.CLOSE);
					return;
				}
			}
			
			SocketAddress localSocketAddr = ctx.channel().remoteAddress();
			InetAddress addr = ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).get();
			
			String limiterAddress = null;
			RateLimitStatus loginRateLimit = RateLimitStatus.OK;
			if(addr != null) {
				remoteAddress = addr;
				limiterAddress = addr.getHostAddress();
			}else {
				if(localSocketAddr instanceof InetSocketAddress) {
					remoteAddress = ((InetSocketAddress)localSocketAddr).getAddress();
					limiterAddress = remoteAddress.getHostAddress();
				}else {
					remoteAddress = InetAddress.getLoopbackAddress();
				}
			}
			
			EaglerRateLimiter limiter = conf.getRatelimitLogin();
			if(limiterAddress != null && limiter != null) {
				loginRateLimit = limiter.rateLimit(limiterAddress);
			}
			
			if(loginRateLimit == RateLimitStatus.LOCKED_OUT) {
				connectionClosed = true;
				ctx.close();
				return;
			}
			
			if (loginRateLimit != RateLimitStatus.OK) {
				sendErrorCode(ctx,
						loginRateLimit == RateLimitStatus.LIMITED_NOW_LOCKED_OUT
								? HandshakePacketTypes.SERVER_ERROR_RATELIMIT_LOCKED
								: HandshakePacketTypes.SERVER_ERROR_RATELIMIT_BLOCKED,
						"Too many logins!").addListener(ChannelFutureListener.CLOSE);
				return;
			}
			
			localAddrString = localSocketAddr.toString();
			EaglerXVelocity.logger().info("[{}]: Connected via websocket", localAddrString);
			if(addr != null) {
				EaglerXVelocity.logger().info("[{}]: Real address is {}", localAddrString, addr.getHostAddress());
			}
			String origin = ctx.channel().attr(EaglerPipeline.ORIGIN).get();
			if(origin != null) {
				EaglerXVelocity.logger().info("[{}]: Origin header is {}", localAddrString, origin);
			}else {
				EaglerXVelocity.logger().info("[{}]: No origin header is present!", localAddrString);
			}
		}else if(!hasBinaryConnection) {
			connectionClosed = true;
			ctx.close();
			return;
		}
		int op = -1;
		try {
			op = buffer.readUnsignedByte();
			switch(op) {
			case HandshakePacketTypes.PROTOCOL_CLIENT_VERSION: {
				if(clientLoginState == HandshakePacketTypes.STATE_OPENED) {
					clientLoginState = HandshakePacketTypes.STATE_STALLING;
					EaglerXVelocity eaglerXBungee = EaglerXVelocity.getEagler();
					EaglerAuthConfig authConfig = eaglerXBungee.getConfig().getAuthConfig();
					
					final int minecraftProtocolVersion = 47;
					
					int eaglerLegacyProtocolVersion = buffer.readUnsignedByte();
					
					if(eaglerLegacyProtocolVersion == 1) {
						if(authConfig.isEnableAuthentication()) {
							sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Please update your client to register on this server!")
										.addListener(ChannelFutureListener.CLOSE);
							return;
						}else if(buffer.readUnsignedByte() != minecraftProtocolVersion || !conf.isAllowV3()) {
							clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
							connectionClosed = true;
							ByteBuf buf = ctx.alloc().buffer();
							buf.writeByte(HandshakePacketTypes.PROTOCOL_VERSION_MISMATCH);
							buf.writeByte(1);
							buf.writeByte(1);
							buf.writeByte(eaglerLegacyProtocolVersion);
							String str = "Outdated Client";
							buf.writeByte(str.length());
							buf.writeCharSequence(str, StandardCharsets.US_ASCII);
							ctx.writeAndFlush(new BinaryWebSocketFrame(buf)).addListener(ChannelFutureListener.CLOSE);
							return;
						}
					}else if(eaglerLegacyProtocolVersion == 2) {
						//make sure to update VersionQueryHandler too
						int minServerSupported = conf.isAllowV3() ? 2 : 4;
						int maxServerSupported = conf.isAllowV4() ? 4 : 3;
						int minAvailableProtVers = Integer.MAX_VALUE;
						int maxAvailableProtVers = Integer.MIN_VALUE;
						int minSupportedProtVers = Integer.MAX_VALUE;
						int maxSupportedProtVers = Integer.MIN_VALUE;
						
						int cnt = buffer.readUnsignedShort();
						for(int i = 0; i < cnt; ++i) {
							int j = buffer.readUnsignedShort();
							if(j > maxAvailableProtVers) {
								maxAvailableProtVers = j;
							}
							if(j < minAvailableProtVers) {
								minAvailableProtVers = j;
							}
							if(j >= minServerSupported && j <= maxServerSupported) {
								if(j > maxSupportedProtVers) {
									maxSupportedProtVers = j;
								}
								if(j < minSupportedProtVers) {
									minSupportedProtVers = j;
								}
							}
						}
						
						int minGameVers = Integer.MAX_VALUE;
						int maxGameVers = -1;
						boolean has47InList = false;
						
						cnt = buffer.readUnsignedShort();
						for(int i = 0; i < cnt; ++i) {
							int j = buffer.readUnsignedShort();
							if(j == minecraftProtocolVersion) {
								has47InList = true;
							}
							if(j > maxGameVers) {
								maxGameVers = j;
							}
							if(j < minGameVers) {
								minGameVers = j;
							}
						}
						
						if(maxAvailableProtVers == Integer.MIN_VALUE || maxGameVers == Integer.MIN_VALUE) {
							throw new IOException();
						}
						
						boolean versMisMatch = false;
						boolean isServerProbablyOutdated = false;
						boolean isClientProbablyOutdated = false;
						if(maxSupportedProtVers == Integer.MIN_VALUE) {
							clientProtocolVersion = maxAvailableProtVers < 3 ? 2 : 3;
							versMisMatch = true;
							isServerProbablyOutdated = minAvailableProtVers > maxServerSupported && maxAvailableProtVers > maxServerSupported;
							isClientProbablyOutdated = minAvailableProtVers < minServerSupported && maxAvailableProtVers < minServerSupported;
						}else if(!has47InList) {
							clientProtocolVersion = 3;
							versMisMatch = true;
							isServerProbablyOutdated = minGameVers > minecraftProtocolVersion && maxGameVers > minecraftProtocolVersion;
							isClientProbablyOutdated = minGameVers < minecraftProtocolVersion && maxGameVers < minecraftProtocolVersion;
						}else {
							clientProtocolVersion = maxSupportedProtVers;
						}
						
						if(versMisMatch) {
							clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
							connectionClosed = true;
							ByteBuf buf = ctx.alloc().buffer();
							buf.writeByte(HandshakePacketTypes.PROTOCOL_VERSION_MISMATCH);
							
							buf.writeShort((conf.isAllowV3() ? 2 : 0) + (conf.isAllowV4() ? 1 : 0));
							if(conf.isAllowV3()) {
								buf.writeShort(2);
								buf.writeShort(3);
							}
							if(conf.isAllowV4()) {
								buf.writeShort(4);
							}
							
							buf.writeShort(1);
							buf.writeShort(minecraftProtocolVersion); // want game version 47
							
							String str = isClientProbablyOutdated ? "Outdated Client" : (isServerProbablyOutdated ? "Outdated Server" : "Unsupported Client Version");
							buf.writeByte(str.length());
							buf.writeCharSequence(str, StandardCharsets.US_ASCII);
							ctx.writeAndFlush(new BinaryWebSocketFrame(buf)).addListener(ChannelFutureListener.CLOSE);
							return;
						}
					}else {
						sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Legacy protocol version should always be '2' on post-snapshot clients")
								.addListener(ChannelFutureListener.CLOSE);
						return;
					}
					
					int strlen = buffer.readUnsignedByte();
					String eaglerBrand = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII).toString();
					strlen = buffer.readUnsignedByte();
					String eaglerVersionString = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII).toString();
					
					if(eaglerLegacyProtocolVersion >= 2) {
						clientAuth = buffer.readBoolean();
						strlen = buffer.readUnsignedByte();
						clientAuthUsername = new byte[strlen];
						buffer.readBytes(clientAuthUsername);
					}
					if(buffer.isReadable()) {
						throw new IllegalArgumentException("Packet too long");
					}
					
					boolean useSnapshotFallbackProtocol = false;
					if(eaglerLegacyProtocolVersion == 1 && !authConfig.isEnableAuthentication()) {
						clientProtocolVersion = 2;
						useSnapshotFallbackProtocol = true;
						clientAuth = false;
						clientAuthUsername = null;
					}
						
					InetAddress addr = ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).get();
					if(addr == null) {
						SocketAddress remoteSocketAddr = ctx.channel().remoteAddress();
						if(remoteSocketAddr instanceof InetSocketAddress) {
							addr = ((InetSocketAddress)remoteSocketAddr).getAddress();
						}else {
							addr = InetAddress.getLoopbackAddress();
						}
					}

					EaglercraftClientBrandEvent brandEvent = new EaglercraftClientBrandEvent(eaglerBrand, eaglerVersionString,
							ctx.channel().attr(EaglerPipeline.ORIGIN).get(), clientProtocolVersion, addr);
					brandEvent = eaglerXBungee.getProxy().getEventManager().fire(brandEvent).join();
					if(brandEvent.isCancelled()) {
						Component kickReason = brandEvent.getMessage();
						if(kickReason == null) {
							kickReason = Component.text("End of stream");
						}
						sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, kickReason)
								.addListener(ChannelFutureListener.CLOSE);
						return;
					}

					final boolean final_useSnapshotFallbackProtocol = useSnapshotFallbackProtocol;
					Runnable continueThread = () -> {
						clientLoginState = HandshakePacketTypes.STATE_CLIENT_VERSION;
						gameProtocolVersion = 47;
						clientBrandString = eaglerBrand;
						clientVersionString = eaglerVersionString;
						
						ByteBuf buf = ctx.alloc().buffer();
						buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_VERSION);
						
						if(final_useSnapshotFallbackProtocol) {
							buf.writeByte(1);
						}else {
							buf.writeShort(clientProtocolVersion);
							buf.writeShort(minecraftProtocolVersion);
						}
						
						String brandStr = EaglerXVelocityVersion.NAME;
						buf.writeByte(brandStr.length());
						buf.writeCharSequence(brandStr, StandardCharsets.US_ASCII);
						
						String versStr = EaglerXVelocityVersion.VERSION;
						buf.writeByte(versStr.length());
						buf.writeCharSequence(versStr, StandardCharsets.US_ASCII);

						if(!authConfig.isEnableAuthentication() || !clientAuth) {
							buf.writeByte(0);
							buf.writeShort(0);
						}else {
							int meth = getAuthMethodId(authRequireEvent.getUseAuthType());
							
							if(meth == -1) {
								sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Unsupported authentication method resolved")
										.addListener(ChannelFutureListener.CLOSE);
								EaglerXVelocity.logger().error("[{}]: Disconnecting, unsupported AuthMethod: {}", localAddrString, authRequireEvent.getUseAuthType());
								return;
							}
							
							buf.writeByte(meth);
							
							byte[] saltingData = authRequireEvent.getSaltingData();
							if(saltingData != null) {
								buf.writeShort(saltingData.length);
								buf.writeBytes(saltingData);
							}else {
								buf.writeShort(0);
							}
						}
						
						ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
						isProtocolExchanged = true;
					};
					
					authRequireEvent = null;
					if(authConfig.isEnableAuthentication()) {
						String origin = ctx.channel().attr(EaglerPipeline.ORIGIN).get();
						try {
							authRequireEvent = new EaglercraftIsAuthRequiredEvent(conf, remoteAddress, origin,
									clientAuth, clientAuthUsername, (reqAuthEvent) -> {
								if(authRequireEvent.shouldKickUser()) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, authRequireEvent.getKickMessage())
											.addListener(ChannelFutureListener.CLOSE);
									return;
								}
								
								AuthResponse resp = authRequireEvent.getAuthRequired();
								if(resp == null) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "IsAuthRequiredEvent was not handled")
											.addListener(ChannelFutureListener.CLOSE);
									EaglerXVelocity.logger().error("[{}]: Disconnecting, no installed authentication system handled: {}", localAddrString, authRequireEvent);
									return;
								}
								
								if(resp == AuthResponse.DENY) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, authRequireEvent.getKickMessage())
											.addListener(ChannelFutureListener.CLOSE);
									return;
								}
								
								AuthMethod type = authRequireEvent.getUseAuthType();
								if(type == null) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "IsAuthRequiredEvent was not fully handled")
											.addListener(ChannelFutureListener.CLOSE);
									EaglerXVelocity.logger().error("[{}]: Disconnecting, no authentication method provided by handler", localAddrString);
									return;
								}
								
								int typeId = getAuthMethodId(type);
								if(typeId == -1) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Unsupported authentication method resolved")
											.addListener(ChannelFutureListener.CLOSE);
									EaglerXVelocity.logger().error("[{}]: Disconnecting, unsupported AuthMethod: {}", localAddrString, type);
									return;
								}
								
								if(!clientAuth && resp == AuthResponse.REQUIRE) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_AUTHENTICATION_REQUIRED,
											HandshakePacketTypes.AUTHENTICATION_REQUIRED + " [" + typeId + "] " + authRequireEvent.getAuthMessage())
													.addListener(ChannelFutureListener.CLOSE);
									EaglerXVelocity.logger().info("[{}]: Displaying authentication screen", localAddrString);
									return;
								}else {
									if(authRequireEvent.getUseAuthType() == null) {
										sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "IsAuthRequiredEvent was not fully handled")
												.addListener(ChannelFutureListener.CLOSE);
										EaglerXVelocity.logger().error("[{}]: Disconnecting, no authentication method provided by handler", localAddrString);
										return;
									}
								}
								continueThread.run();
							});
							
							if(authConfig.isUseBuiltInAuthentication()) {
								DefaultAuthSystem authSystem = eaglerXBungee.getAuthService();
								if(authSystem != null) {
									authSystem.handleIsAuthRequiredEvent(authRequireEvent);
								}
							}else {
								authRequireEvent = eaglerXBungee.getProxy().getEventManager().fire(authRequireEvent).join();
							}
							
							if(!authRequireEvent.isAsyncContinue()) {
								authRequireEvent.doDirectContinue();
							}
						}catch(Throwable t) {
							throw new EventException(t);
						}
					}else {
						continueThread.run();
					}
				}else {
					clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
					sendErrorWrong(ctx, op, "STATE_OPENED").addListener(ChannelFutureListener.CLOSE);
				}
			}
			break;
			case HandshakePacketTypes.PROTOCOL_CLIENT_REQUEST_LOGIN: {
				if(clientLoginState == HandshakePacketTypes.STATE_CLIENT_VERSION) {
					clientLoginState = HandshakePacketTypes.STATE_STALLING;
					
					int strlen = buffer.readUnsignedByte();
					clientUsername = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII).toString();
					
					if(!clientUsername.equals(clientUsername.replaceAll("[^A-Za-z0-9_]", "_"))) {
						sendLoginDenied(ctx, "Invalid characters in username")
								.addListener(ChannelFutureListener.CLOSE);
						return;
					}
					
					if(clientUsername.length() < 3) {
						sendLoginDenied(ctx, "Username must be at least 3 characters")
								.addListener(ChannelFutureListener.CLOSE);
						return;
					}
					
					if(clientUsername.length() > 16) {
						sendLoginDenied(ctx, "Username must be under 16 characters")
								.addListener(ChannelFutureListener.CLOSE);
						return;
					}
					
					if(clientAuthUsername == null) {
						clientAuthUsername = new byte[strlen];
						for(int i = 0; i < strlen; ++i) {
							clientAuthUsername[i] = (byte)clientUsername.charAt(i);
						}
					}
					
					String offlinePlayerStr = "OfflinePlayer:";
					byte[] uuidHashGenerator = new byte[offlinePlayerStr.length() + clientAuthUsername.length];
					System.arraycopy(offlinePlayerStr.getBytes(StandardCharsets.US_ASCII), 0, uuidHashGenerator, 0, offlinePlayerStr.length());
					System.arraycopy(clientAuthUsername, 0, uuidHashGenerator, offlinePlayerStr.length(), clientAuthUsername.length);
					clientUUID = UUID.nameUUIDFromBytes(uuidHashGenerator);
					
					strlen = buffer.readUnsignedByte();
					clientRequestedServer = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII).toString();
					strlen = buffer.readUnsignedByte();
					clientAuthPassword = new byte[strlen];
					buffer.readBytes(clientAuthPassword);
					
					if(clientProtocolVersion >= 4) {
						clientEnableCookie = buffer.readBoolean();
						strlen = buffer.readUnsignedByte();
						if(clientEnableCookie && strlen > 0) {
							clientCookieData = new byte[strlen];
							buffer.readBytes(clientCookieData);
						}else {
							if(strlen > 0) {
								throw new IllegalArgumentException("Unexpected cookie");
							}
							clientCookieData = null;
						}
					}else {
						clientEnableCookie = false;
						clientCookieData = null;
					}
					
					if(buffer.isReadable()) {
						throw new IllegalArgumentException("Packet too long");
					}

					Runnable continueThread = () -> {
						
						final VelocityServer bungee = EaglerXVelocity.proxy();
						if (bungee.getPlayer(clientUsername).isPresent()) {
							sendLoginDenied(ctx, LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render(Component.translatable("velocity.error.already-connected-proxy"), Locale.getDefault())))
									.addListener(ChannelFutureListener.CLOSE);
							return;
						}
						
						clientLoginState = HandshakePacketTypes.STATE_CLIENT_LOGIN;
						ByteBuf buf = ctx.alloc().buffer();
						buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_ALLOW_LOGIN);
						buf.writeByte(clientUsername.length());
						buf.writeCharSequence(clientUsername, StandardCharsets.US_ASCII);
						buf.writeLong(clientUUID.getMostSignificantBits());
						buf.writeLong(clientUUID.getLeastSignificantBits());
						ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
					};

					EaglerXVelocity eaglerXBungee = EaglerXVelocity.getEagler();
					EaglerAuthConfig authConfig = eaglerXBungee.getConfig().getAuthConfig();
					
					if(authConfig.isEnableAuthentication()) {
						if(clientAuth && clientAuthPassword.length > 0) {
							EaglercraftHandleAuthPasswordEvent handleEvent = new EaglercraftHandleAuthPasswordEvent(
									conf, remoteAddress, authRequireEvent.getOriginHeader(), clientAuthUsername,
									authRequireEvent.getSaltingData(), clientUsername, clientUUID,
									clientAuthPassword, clientEnableCookie, clientCookieData,
									authRequireEvent.getUseAuthType(), authRequireEvent.getAuthMessage(),
									(Object) authRequireEvent.getAuthAttachment(), clientRequestedServer,
									(handleAuthEvent) -> {
								
								if(handleAuthEvent.getLoginAllowed() != EaglercraftHandleAuthPasswordEvent.AuthResponse.ALLOW) {
									sendLoginDenied(ctx, handleAuthEvent.getLoginDeniedMessage()).addListener(ChannelFutureListener.CLOSE);
									return;
								}
								
								clientUsername = handleAuthEvent.getProfileUsername();
								clientUUID = handleAuthEvent.getProfileUUID();
								
								String texPropOverrideValue = handleAuthEvent.getApplyTexturesPropertyValue();
								if(texPropOverrideValue != null) {
									String texPropOverrideSig = handleAuthEvent.getApplyTexturesPropertySignature();
									texturesOverrideProperty = new Property("textures", texPropOverrideValue, texPropOverrideSig);
								}
								
								overrideEaglerToVanillaSkins = handleAuthEvent.isOverrideEaglerToVanillaSkins();
								
								continueThread.run();
							});
							
							if(authConfig.isUseBuiltInAuthentication()) {
								DefaultAuthSystem authSystem = eaglerXBungee.getAuthService();
								if(authSystem != null) {
									authSystem.handleAuthPasswordEvent(handleEvent);
								}
							}else {
								eaglerXBungee.getProxy().getEventManager().fire(handleEvent).join();
							}
							
							if(!handleEvent.isAsyncContinue()) {
								handleEvent.doDirectContinue();
							}
						}else if(authRequireEvent.getEnableCookieAuth()) {
							EaglercraftHandleAuthCookieEvent handleEvent = new EaglercraftHandleAuthCookieEvent(
									conf, remoteAddress, authRequireEvent.getOriginHeader(), clientAuthUsername,
									clientUsername, clientUUID, clientEnableCookie, clientCookieData,
									authRequireEvent.getUseAuthType(), authRequireEvent.getAuthMessage(),
									(Object) authRequireEvent.getAuthAttachment(),
									clientRequestedServer, (handleAuthEvent) -> {
								
								EaglercraftHandleAuthCookieEvent.AuthResponse resp = handleAuthEvent.getLoginAllowed();
								
								if(resp == null) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "EaglercraftHandleAuthCookieEvent was not handled")
											.addListener(ChannelFutureListener.CLOSE);
											EaglerXVelocity.logger().error(
													"[{}]: Disconnecting, no installed authentication system handled: {}",
													localAddrString, handleAuthEvent.toString());
											return;
								}
								
								if(resp == EaglercraftHandleAuthCookieEvent.AuthResponse.DENY) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, handleAuthEvent.getLoginDeniedMessage())
											.addListener(ChannelFutureListener.CLOSE);
									return;
								}
								
								clientUsername = handleAuthEvent.getProfileUsername();
								clientUUID = handleAuthEvent.getProfileUUID();
								
								String texPropOverrideValue = handleAuthEvent.getApplyTexturesPropertyValue();
								if(texPropOverrideValue != null) {
									String texPropOverrideSig = handleAuthEvent.getApplyTexturesPropertySignature();
									texturesOverrideProperty = new Property("textures", texPropOverrideValue, texPropOverrideSig);
								}
								
								overrideEaglerToVanillaSkins = handleAuthEvent.isOverrideEaglerToVanillaSkins();
								
								if(resp == EaglercraftHandleAuthCookieEvent.AuthResponse.ALLOW) {
									continueThread.run();
									return;
								}
								
								if(!clientAuth && resp == EaglercraftHandleAuthCookieEvent.AuthResponse.REQUIRE_AUTH) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_AUTHENTICATION_REQUIRED, HandshakePacketTypes.AUTHENTICATION_REQUIRED
											+ " [" + getAuthMethodId(authRequireEvent.getUseAuthType()) + "] " + authRequireEvent.getAuthMessage())
												.addListener(ChannelFutureListener.CLOSE);
									EaglerXVelocity.logger().info("[{}]: Displaying authentication screen", localAddrString);
									return;
								}else {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Failed to handle authentication!")
												.addListener(ChannelFutureListener.CLOSE);
									return;
								}
							});
							
							handleEvent = eaglerXBungee.getProxy().getEventManager().fire(handleEvent).join();
							
							if(!handleEvent.isAsyncContinue()) {
								handleEvent.doDirectContinue();
							}
						}else {
							if(authRequireEvent.getAuthRequired() != EaglercraftIsAuthRequiredEvent.AuthResponse.SKIP) {
								sendLoginDenied(ctx, "Client provided no authentication code").addListener(ChannelFutureListener.CLOSE);
								return;
							}else {
								continueThread.run();
							}
						}
					}else {
						continueThread.run();
					}
					
				}else {
					clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
					sendErrorWrong(ctx, op, "STATE_CLIENT_VERSION")
							.addListener(ChannelFutureListener.CLOSE);
				}
			}
			break;
			case HandshakePacketTypes.PROTOCOL_CLIENT_PROFILE_DATA: {
				if(clientLoginState == HandshakePacketTypes.STATE_CLIENT_LOGIN) {
					if(clientProtocolVersion <= 3) {
						if(profileData.size() >= 12) {
							sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_EXCESSIVE_PROFILE_DATA, "Too many profile data packets recieved")
									.addListener(ChannelFutureListener.CLOSE);
							return;
						}
						int strlen = buffer.readUnsignedByte();
						String dataType = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII).toString();
						strlen = buffer.readUnsignedShort();
						byte[] readData = new byte[strlen];
						buffer.readBytes(readData);
						
						if(buffer.isReadable()) {
							throw new IllegalArgumentException("Packet too long");
						}
						
						if(!profileData.containsKey(dataType)) {
							profileData.put(dataType, readData);
						}else {
							sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_DUPLICATE_PROFILE_DATA, "Multiple profile data packets of the same type recieved")
									.addListener(ChannelFutureListener.CLOSE);
							return;
						}
					}else {
						int count = buffer.readUnsignedByte();
						if(profileData.size() + count > 12) {
							sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_EXCESSIVE_PROFILE_DATA, "Too many profile data packets recieved")
									.addListener(ChannelFutureListener.CLOSE);
							return;
						}
						for(int i = 0; i < count; ++i) {
							int strlen = buffer.readUnsignedByte();
							String dataType = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII).toString();
							strlen = buffer.readUnsignedShort();
							byte[] readData = new byte[strlen];
							buffer.readBytes(readData);
							if(!profileData.containsKey(dataType)) {
								profileData.put(dataType, readData);
							}else {
								sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_DUPLICATE_PROFILE_DATA, "Multiple profile data packets of the same type recieved")
										.addListener(ChannelFutureListener.CLOSE);
								return;
							}
						}
						
						if(buffer.isReadable()) {
							throw new IllegalArgumentException("Packet too long");
						}
					}
					
				}else {
					clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
					sendErrorWrong(ctx, op, "STATE_CLIENT_LOGIN").addListener(ChannelFutureListener.CLOSE);
				}
			}
			break;
			case HandshakePacketTypes.PROTOCOL_CLIENT_FINISH_LOGIN: {
				if(clientLoginState == HandshakePacketTypes.STATE_CLIENT_LOGIN) {
					clientLoginState = HandshakePacketTypes.STATE_STALLING;
					if(buffer.isReadable()) {
						throw new IllegalArgumentException("Packet too long");
					}
					
					finish(ctx);
					
					clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
				}else {
					sendErrorWrong(ctx, op, "STATE_CLIENT_LOGIN").addListener(ChannelFutureListener.CLOSE);
				}
			}
			break;
			default:
				clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
				sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_UNKNOWN_PACKET, "Unknown Packet #" + op)
						.addListener(ChannelFutureListener.CLOSE);
				break;
			}
		}catch(Throwable ex) {
			ex.printStackTrace();
			if(ex instanceof EventException) {
				EaglerXVelocity.logger().error("[{}]: Hanshake packet {} caught an exception", localAddrString, op, ex.getCause());
			}
			clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_INVALID_PACKET, op == -1 ?
					"Invalid Packet" : "Invalid Packet #" + op)
					.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	private void finish(final ChannelHandlerContext ctx) {
		final VelocityServer bungee = EaglerXVelocity.proxy();

		if(conf.getMaxPlayer() > 0) {
			int i = 0;
			for(Player p : bungee.getAllPlayers()) {
				EaglerPlayerData playerData = EaglerPipeline.getEaglerHandle(p);
				if(playerData != null && playerData.getEaglerListenerConfig() == conf) {
					++i;
				}
			}
			
			if (i >= conf.getMaxPlayer()) {
				sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Proxy is full")
						.addListener(ChannelFutureListener.CLOSE);
				connectionClosed = true;
				return;
			}
		}

		final String usernameStr = clientUsername.toString();
		if (bungee.getPlayer(usernameStr).isPresent()) {
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
					LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render(Component.translatable("velocity.error.already-connected-proxy"), Locale.getDefault())))
							.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		InetSocketAddress baseAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		InetAddress addr = ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).get();
		if(addr != null) {
			baseAddress = new InetSocketAddress(addr, baseAddress.getPort());
		}
		final InetSocketAddress final_baseAddress = baseAddress;

		ProtocolVersion protocolVers = ProtocolVersion.getProtocolVersion(gameProtocolVersion);
		if(!protocolVers.isSupported()) {
			//TODO: localize somehow
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Outdated Client!").addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		InetSocketAddress localAddress = (InetSocketAddress)ctx.channel().localAddress();
		String hostName = localAddress.getHostString();
		HandshakePacket fakeHandshake = new HandshakePacket();
		fakeHandshake.setIntent(HandshakeIntent.LOGIN);
		fakeHandshake.setProtocolVersion(protocolVers);
		fakeHandshake.setServerAddress(hostName);
		fakeHandshake.setPort(localAddress.getPort());

		final MinecraftConnection con = new MinecraftConnection(ctx.channel(), bungee);

		try {
			remoteAddressField.set(con, baseAddress);
			stateField.set(con, StateRegistry.LOGIN);
			protocolVersionField.set(con, protocolVers);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		con.setType(ConnectionTypes.VANILLA);

		EaglerVelocityConfig eaglerConf = EaglerXVelocity.getEagler().getConfig();

		EaglerUpdateConfig updateconf = eaglerConf.getUpdateConfig();
		boolean blockUpdate = updateconf.isBlockAllClientUpdates();
		EaglerPlayerData.ClientCertificateHolder mycert = null;
		if(!blockUpdate && !updateconf.isDiscardLoginPacketCerts()) {
			byte[] b = profileData.get("update_cert_v1");
			if(b != null && b.length < 32759) {
				EaglerUpdateSvc.sendCertificateToPlayers(mycert = EaglerUpdateSvc.tryMakeHolder(b));
			}
		}
		final EaglerPlayerData.ClientCertificateHolder cert = mycert;

		UUID clientBrandUUID = null;
		String clientBrandAsString = clientBrandString.toString();
		byte[] brandUUIDBytes = profileData.get("brand_uuid_v1");
		if(brandUUIDBytes != null) {
			if(brandUUIDBytes.length == 16) {
				ByteBuf buf = Unpooled.wrappedBuffer(brandUUIDBytes);
				clientBrandUUID = new UUID(buf.readLong(), buf.readLong());
				if (clientBrandUUID.equals(EaglerXVelocityAPIHelper.BRAND_NULL_UUID)
						|| clientBrandUUID.equals(EaglerXVelocityAPIHelper.BRAND_PENDING_UUID)
						|| clientBrandUUID.equals(EaglerXVelocityAPIHelper.BRAND_VANILLA_UUID)) {
					clientBrandUUID = null;
				}
			}
		}else {
			clientBrandUUID = EaglerXVelocityAPIHelper.makeClientBrandUUIDLegacy(clientBrandAsString);
		}
		final UUID final_clientBrandUUID = clientBrandUUID;
		final Map<String,byte[]> otherProfileData = new HashMap<>();
		for(Entry<String,byte[]> etr2 : profileData.entrySet()) {
			String str = etr2.getKey();
			if(!profileDataStandard.contains(str)) {
				otherProfileData.put(str, etr2.getValue());
			}
		}
		InitialInboundConnection inboundCon;
		try {
			inboundCon = stupidConstructor.newInstance(con, cleanVhost(hostName), fakeHandshake);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		if (!bungee.getIpAttemptLimiter().attempt(baseAddress.getAddress())) {
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
					LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render(Component.translatable("velocity.error.logging-in-too-fast", NamedTextColor.RED), Locale.getDefault())))
							.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		if (bungee.getConfiguration().getPlayerInfoForwardingMode() == PlayerInfoForwarding.MODERN
				&& protocolVers.lessThan(ProtocolVersion.MINECRAFT_1_13)) {
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
					LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render(Component.translatable("velocity.error.modern-forwarding-needs-new-client", NamedTextColor.RED), Locale.getDefault())))
							.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		LoginInboundConnection lic;
		try {
			lic = stupid2Constructor.newInstance(inboundCon);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		bungee.getEventManager().fireAndForget(new ConnectionHandshakeEvent(inboundCon, fakeHandshake.getIntent()));

		PreLoginEvent event1 = new PreLoginEvent(lic, usernameStr, clientUUID);
		bungee.getEventManager().fire(event1).thenRunAsync(() -> {
			if (con.isClosed()) {
				return;
			}

			PreLoginEvent.PreLoginComponentResult result = event1.getResult();
			Optional<Component> disconnectReason = result.getReasonComponent();
			if (disconnectReason.isPresent()) {
				sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
						GlobalTranslator.render(disconnectReason.get(), Locale.getDefault()))
						.addListener(ChannelFutureListener.CLOSE);
				return;
			}

			try {
				loginEventFiredMethod.invoke(lic, (Runnable) () -> {
					if (con.isClosed()) {
						return;
					}

					con.eventLoop().execute(() -> {
						List<Property> daprops;
						if(eaglerConf.getEnableIsEaglerPlayerProperty()) {
							daprops = Arrays.asList(EaglerVelocityConfig.isEaglerProperty);
						}else {
							daprops = new ArrayList<>(0);
						}
						GameProfile profile = con.getType().addGameProfileTokensIfRequired(new GameProfile(clientUUID, usernameStr, daprops), bungee.getConfiguration().getPlayerInfoForwardingMode());
						GameProfileRequestEvent profileRequestEvent = new GameProfileRequestEvent(lic, profile, false);
						bungee.getEventManager().fire(profileRequestEvent).thenComposeAsync((Function<GameProfileRequestEvent, CompletableFuture<Void>>) (profileEvent) -> {
							if (con.isClosed()) {
								return CompletableFuture.completedFuture(null);
							} else {
								GameProfile gp = profileRequestEvent.getGameProfile();
								if(eaglerConf.getEnableIsEaglerPlayerProperty()) {
									gp = gp.addProperty(EaglerVelocityConfig.isEaglerProperty);
								}

								ConnectedPlayer player;
								if(stupid3Constructor_new != null) {
									try {
										player = stupid3Constructor_new.newInstance(bungee, gp, con, lic.getVirtualHost().orElse(null), lic.getRawVirtualHost().orElse(null), false, lic.getIdentifiedKey());
									} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
										throw new RuntimeException(e);
									}
								}else {
									try {
										player = stupid3Constructor.newInstance(bungee, gp, con, lic.getVirtualHost().orElse(null), false, lic.getIdentifiedKey());
									} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
										throw new RuntimeException(e);
									}
								}

								con.setAssociation(player);

								if (!bungee.canRegisterConnection(player)) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
											LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render(Component.translatable("velocity.error.already-connected-proxy"), Locale.getDefault())))
													.addListener(ChannelFutureListener.CLOSE);
									return CompletableFuture.completedFuture(null);
								} else {
									boolean doRegisterSkins = true;
									boolean doForceSkins = false;
	
									EaglercraftRegisterSkinEvent registerSkinEvent = bungee.getEventManager()
											.fire(new EaglercraftRegisterSkinEvent(usernameStr, clientUUID,
											authRequireEvent != null ? authRequireEvent.getAuthAttachment() : null)).join();

									Property prop = registerSkinEvent.getForceUseMojangProfileProperty();
									boolean useExistingProp = registerSkinEvent.getForceUseLoginResultObjectTextures();
									if(prop != null) {
										texturesOverrideProperty = prop;
										overrideEaglerToVanillaSkins = true;
										if(clientProtocolVersion >= 4 && (EaglerXVelocity.getEagler().getSkinService() instanceof SkinService)) {
											doForceSkins = true;
										}
									}else {
										if(useExistingProp) {
											overrideEaglerToVanillaSkins = true;
										}else {
											byte[] custom = registerSkinEvent.getForceSetUseCustomPacket();
											if(custom != null) {
												profileData.remove("skin_v2");
												profileData.put("skin_v1", custom);
												overrideEaglerToVanillaSkins = false;
												if(clientProtocolVersion >= 4) {
													doForceSkins = true;
												}
											}
										}
									}
	
									if(texturesOverrideProperty != null) {
										gp = gp.addProperties(Arrays.asList(texturesOverrideProperty, EaglerVelocityConfig.isEaglerProperty));
										player.setGameProfileProperties(gp.getProperties());
									}else {
										if(!useExistingProp) {
											String vanillaSkin = eaglerConf.getEaglerPlayersVanillaSkin();
											if(vanillaSkin != null) {
												gp = gp.addProperties(Arrays.asList(eaglerConf.getEaglerPlayersVanillaSkinProperties()));
												player.setGameProfileProperties(gp.getProperties());
											}
										}
									}
	
									if(overrideEaglerToVanillaSkins) {
										List<Property> props = gp.getProperties();
										if(props != null) {
											for(int i = 0; i < props.size(); ++i) {
												if("textures".equals(props.get(i).getName())) {
													try {
														String jsonStr = SkinPackets.bytesToAscii(Base64.getDecoder().decode(props.get(i).getValue()));
														JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
														JsonObject skinObj = json.getAsJsonObject("SKIN");
														if(skinObj != null) {
															JsonElement url = json.get("url");
															if(url != null) {
																String urlStr = SkinService.sanitizeTextureURL(url.getAsString());
																EaglerXVelocity.getEagler().getSkinService().registerTextureToPlayerAssociation(urlStr, gp.getId());
															}
														}
														doRegisterSkins = false;
														if(clientProtocolVersion >= 4) {
															doForceSkins = true;
														}
													}catch(Throwable t) {
													}
													break;
												}
											}
										}
									}
	
									if(doRegisterSkins) {
										if(clientProtocolVersion >= 4 && profileData.containsKey("skin_v2")) {
											try {
												SkinPackets.registerEaglerPlayer(clientUUID, profileData.get("skin_v2"),
														EaglerXVelocity.getEagler().getSkinService(), 4);
											} catch (Throwable ex) {
												SkinPackets.registerEaglerPlayerFallback(clientUUID, EaglerXVelocity.getEagler().getSkinService());
												EaglerXVelocity.logger().info("[{}]: Invalid skin packet: {}", ctx.channel().remoteAddress(), ex.toString());
											}
										}else if(profileData.containsKey("skin_v1")) {
											try {
												SkinPackets.registerEaglerPlayer(clientUUID, profileData.get("skin_v1"),
														EaglerXVelocity.getEagler().getSkinService(), 3);
											} catch (Throwable ex) {
												SkinPackets.registerEaglerPlayerFallback(clientUUID, EaglerXVelocity.getEagler().getSkinService());
												EaglerXVelocity.logger().info("[{}]: Invalid skin packet: {}", ctx.channel().remoteAddress(), ex.toString());
											}
										}else {
											SkinPackets.registerEaglerPlayerFallback(clientUUID, EaglerXVelocity.getEagler().getSkinService());
										}
									}
	
									EaglercraftRegisterCapeEvent registerCapeEvent = bungee.getEventManager().fire(new EaglercraftRegisterCapeEvent(usernameStr,
											clientUUID, authRequireEvent != null ? authRequireEvent.getAuthAttachment() : null)).join();
	
									byte[] forceCape = registerCapeEvent.getForceSetUseCustomPacket();
									if(forceCape != null) {
										profileData.put("cape_v1", forceCape);
									}
	
									if(profileData.containsKey("cape_v1")) {
										try {
											CapePackets.registerEaglerPlayer(clientUUID, profileData.get("cape_v1"),
													EaglerXVelocity.getEagler().getCapeService());
										} catch (Throwable ex) {
											CapePackets.registerEaglerPlayerFallback(clientUUID, EaglerXVelocity.getEagler().getCapeService());
											EaglerXVelocity.logger().info("[{}]: Invalid cape packet: {}", ctx.channel().remoteAddress(), ex.toString());
										}
									}else {
										CapePackets.registerEaglerPlayerFallback(clientUUID, EaglerXVelocity.getEagler().getCapeService());
									}

									EaglerConnectionInstance connInstance = ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get();
									EaglerPlayerData epd = connInstance.eaglerData = new EaglerPlayerData(connInstance,
											conf, clientProtocolVersion, gameProtocolVersion, clientBrandString,
											clientVersionString, final_clientBrandUUID, clientUsername, clientUUID,
											final_baseAddress, ctx.channel().attr(EaglerPipeline.ORIGIN).get(),
											ctx.channel().attr(EaglerPipeline.USER_AGENT).get(), cert,
											clientEnableCookie, clientCookieData, otherProfileData, player.getGameProfile());

									epd.messageProtocolController = new GameProtocolMessageController(player,
											GamePluginMessageProtocol.getByVersion(clientProtocolVersion),
											GameProtocolMessageController.createServerHandler(clientProtocolVersion, player,
													epd, EaglerXVelocity.getEagler()), conf.getDefragSendDelay());

									if(doForceSkins) {
										EaglerXVelocity.getEagler().getSkinService().processForceSkin(clientUUID, epd);
									}
									if(forceCape != null && clientProtocolVersion >= 4) {
										EaglerXVelocity.getEagler().getCapeService().processForceCape(clientUUID, epd);
									}

									EaglerXVelocity.logger().info("{} has connected", player);
									if(conf.getEnableVoiceChat()) {
										EaglerXVelocity.getEagler().getVoiceService().handlePlayerLoggedIn(epd);
									}
									
									if(clientProtocolVersion >= 4) {
										SPacketCustomizePauseMenuV4EAG pauseMenuPkt = EaglerXVelocity.getEagler().getConfig().getPauseMenuConf().getPacket();
										if(pauseMenuPkt != null) {
											epd.sendEaglerMessage(pauseMenuPkt);
										}
									}
									
									if(!blockUpdate) {
										List<EaglerPlayerData.ClientCertificateHolder> set = EaglerUpdateSvc.getCertList();
										synchronized(set) {
											epd.certificatesToSend.addAll(set);
										}
										for (Player p : bungee.getAllPlayers()) {
											EaglerPlayerData ppp = EaglerPipeline.getEaglerHandle(p);
											if(ppp != null && ppp.clientCertificate != null && ppp.clientCertificate != cert) {
												epd.certificatesToSend.add(ppp.clientCertificate);
											}
										}
									}

									PermissionProvider prov;
									try {
										prov = (PermissionProvider) defaultPermissionsField.get(null);
									} catch (IllegalAccessException e) {
										throw new RuntimeException(e);
									}
									return bungee.getEventManager().fire(new PermissionsSetupEvent(player, prov)).thenAcceptAsync((event) -> {
										if (!con.isClosed()) {
											PermissionFunction function = event.createFunction(player);
											if (function == null) {
												EaglerXVelocity.logger().error("A plugin permission provider {} provided an invalid permission function for player {}. This is a bug in the plugin, not in Velocity. Falling back to the default permission function.", event.getProvider().getClass().getName(), player.getUsername());
											} else {
												try {
													setPermissionFunctionMethod.invoke(player, function);
												} catch (IllegalAccessException | InvocationTargetException e) {
													throw new RuntimeException(e);
												}
											}
											bungee.getEventManager().fire(new LoginEvent(player)).thenAcceptAsync(event3 -> {
												if (con.isClosed()) {
													// The player was disconnected
													bungee.getEventManager().fireAndForget(new DisconnectEvent(player, DisconnectEvent.LoginStatus.CANCELLED_BY_USER_BEFORE_COMPLETE));
													return;
												}

												Optional<Component> reason = event3.getResult().getReasonComponent();
												if (reason.isPresent()) {
													bungee.getEventManager().fireAndForget(new DisconnectEvent(player, DisconnectEvent.LoginStatus.CANCELLED_BY_PROXY));
													sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
															GlobalTranslator.render(reason.get(), Locale.getDefault()))
															.addListener(ChannelFutureListener.CLOSE);
													return;
												} else {
													if (!bungee.registerConnection(player)) {
														bungee.getEventManager().fireAndForget(new DisconnectEvent(player, DisconnectEvent.LoginStatus.CONFLICTING_LOGIN));
														sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
																LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render(Component.translatable("velocity.error.already-connected-proxy"), Locale.getDefault())))
																		.addListener(ChannelFutureListener.CLOSE);
														return;
													}

													ByteBuf buf = Unpooled.buffer();
													buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_FINISH_LOGIN);
													ctx.channel().writeAndFlush(new BinaryWebSocketFrame(buf)).addListener(future -> {
														ChannelPipeline pp = ctx.channel().pipeline();

														pp.remove(HttpWebSocketHandler.this);

														pp
																.addLast("EaglerMinecraftByteBufDecoder", new EaglerMinecraftDecoder())
																.addLast(LEGACY_PING_DECODER, new LegacyPingDecoder())
																.addLast(READ_TIMEOUT,
																		new ReadTimeoutHandler(bungee.getConfiguration().getReadTimeout(),
																				TimeUnit.MILLISECONDS))
																.addLast("EaglerMinecraftByteBufEncoder", new EaglerMinecraftEncoder())
																.addLast(LEGACY_PING_ENCODER, LegacyPingEncoder.INSTANCE)
																.addLast(MINECRAFT_DECODER, new MinecraftDecoder(ProtocolUtils.Direction.SERVERBOUND))
																.addLast(MINECRAFT_ENCODER, new MinecraftEncoder(ProtocolUtils.Direction.CLIENTBOUND));
														
														pp.addLast(Connections.HANDLER, con);
														
														con.setProtocolVersion(protocolVers);
														con.setState(StateRegistry.PLAY);
														con.setActiveSessionHandler(StateRegistry.PLAY, stupid4Constructor.newInstance(player, bungee));
														
														bungee.getEventManager().fire(new PostLoginEvent(player)).thenCompose((ignored) -> {
															EaglerConnectionInstance conInstance = ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get();
															conInstance.userConnection = player;
															conInstance.hasBeenForwarded = true;
															Optional<RegisteredServer> initialFromConfig = player.getNextServerToTry();
															PlayerChooseInitialServerEvent event2 = new PlayerChooseInitialServerEvent(player, initialFromConfig.orElse(null));
															return bungee.getEventManager().fire(event2).thenRunAsync(() -> {
																Optional<RegisteredServer> toTry = event2.getInitialServer();
																if (toTry.isEmpty()) {
																	player.disconnect0(Component.translatable("velocity.error.no-available-servers", NamedTextColor.RED), true);
																} else {
																	player.createConnectionRequest(toTry.get()).fireAndForget();
																}
															}, con.eventLoop());
														}).exceptionally((ex) -> {
															EaglerXVelocity.logger().error("Exception while connecting {} to initial server", player, ex);
															return null;
														});
													});
												}
											}, con.eventLoop()).exceptionally((ex) -> {
												EaglerXVelocity.logger().error("Exception while completing login initialisation phase for {}", player, ex);
												return null;
											});
										}

									}, con.eventLoop());
								}
							}
						}, con.eventLoop()).exceptionally((ex) -> {
							EaglerXVelocity.logger().error("Exception during connection of {}", profile, ex);
							return null;
						});
					});
				});
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}, con.eventLoop()).exceptionally((ex) -> {
			EaglerXVelocity.logger().error("Exception in pre-login stage", ex);
			return null;
		});
		
	}
	
	private static String cleanVhost(String hostname) {
		String cleaned = hostname;
		int zeroIdx = hostname.indexOf(0);
		if (zeroIdx > -1) {
			cleaned = hostname.substring(0, zeroIdx);
		}

		if (!cleaned.isEmpty() && cleaned.charAt(cleaned.length() - 1) == '.') {
			cleaned = cleaned.substring(0, cleaned.length() - 1);
		}

		return cleaned;
	}

	private static class EventException extends RuntimeException {
		public EventException(Throwable t) {
			super(t.toString(), t);
		}
	}

	private void handleText(ChannelHandlerContext ctx, String str) {
		if(connectionClosed) {
			return;
		}
		if (!hasFirstPacket && (conf.isAllowMOTD() || conf.isAllowQuery()) && str.length() < 128
				&& (str = str.toLowerCase()).startsWith("accept:")) {
			str = str.substring(7).trim();
			hasFirstPacket = true;
			hasBinaryConnection = false;
			
			if(CommandConfirmCode.confirmHash != null && str.equalsIgnoreCase(CommandConfirmCode.confirmHash)) {
				connectionClosed = true;
				ctx.writeAndFlush(new TextWebSocketFrame("OK")).addListener(ChannelFutureListener.CLOSE);
				CommandConfirmCode.confirmHash = null;
				return;
			}
			
			boolean isMOTD = str.startsWith("motd");
			
			SocketAddress localSocketAddr = ctx.channel().remoteAddress();
			InetAddress addr = ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).get();
			
			String limiterAddress = null;
			RateLimitStatus queryRateLimit = RateLimitStatus.OK;
			if(addr != null) {
				limiterAddress = addr.getHostAddress();
			}else {
				if(localSocketAddr instanceof InetSocketAddress) {
					limiterAddress = ((InetSocketAddress)localSocketAddr).getAddress().getHostAddress();
				}
			}
			
			EaglerRateLimiter limiter = isMOTD ? conf.getRatelimitMOTD() : conf.getRatelimitQuery();
			if(limiterAddress != null && limiter != null) {
				queryRateLimit = limiter.rateLimit(limiterAddress);
			}
			
			if(queryRateLimit == RateLimitStatus.LOCKED_OUT) {
				connectionClosed = true;
				ctx.close();
				return;
			}
			
			if(queryRateLimit != RateLimitStatus.OK) {
				connectionClosed = true;
				final RateLimitStatus rateLimitTypeFinal = queryRateLimit;
				ctx.writeAndFlush(new TextWebSocketFrame(
						rateLimitTypeFinal == RateLimitStatus.LIMITED_NOW_LOCKED_OUT ? "{\"type\":\"locked\"}" : "{\"type\":\"blocked\"}"))
						.addListener(ChannelFutureListener.CLOSE);
				return;
			}
			
			HttpServerQueryHandler handler = null;
			if(isMOTD) {
				if(conf.isAllowMOTD()) {
					handler = new MOTDQueryHandler();
				}
			}else if(conf.isAllowQuery()) {
				handler = QueryManager.createQueryHandler(str);
			}
			if(handler != null) {
				ctx.pipeline().replace(HttpWebSocketHandler.this, "HttpServerQueryHandler", handler);
				ctx.pipeline().addBefore("HttpServerQueryHandler", "WriteTimeoutHandler", new WriteTimeoutHandler(5l, TimeUnit.SECONDS));
				ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get().hasBeenForwarded = true;
				handler.beginHandleQuery(conf, ctx, str);
				if(handler instanceof MOTDQueryHandler) {
					EaglercraftMOTDEvent evt = new EaglercraftMOTDEvent((MOTDQueryHandler)handler);
					final HttpServerQueryHandler handlerF = handler;
					EaglerXVelocity.proxy().getEventManager().fire(evt).thenAccept(evt2 -> {
						if(!handlerF.isClosed()) {
							((MOTDQueryHandler)handlerF).sendToUser();
							if(!handlerF.isClosed() && !handlerF.shouldKeepAlive()) {
								handlerF.close();
							}
						}
					}).exceptionally((excep) -> {
						EaglerXVelocity.logger().error("Failed to handle EaglercraftMOTDEvent!", excep);
						if(!handlerF.isClosed()) {
							handlerF.close();
						}
						return null;
					});
				}else if(!handler.isClosed() && !handler.shouldKeepAlive()) {
					handler.close();
				}
			}else {
				connectionClosed = true;
				ctx.close();
			}
		}else {
			connectionClosed = true;
			ctx.close();
			return;
		}
	}
	
	private int getAuthMethodId(AuthMethod meth) {
		switch(meth) {
		case PLAINTEXT:
			return 255; // plaintext authentication
		case EAGLER_SHA256:
			return 1; // eagler_sha256 authentication
		case AUTHME_SHA256:
			return 2; // authme_sha256 authentication
		default:
			return -1;
		}
	}
	
	private ChannelFuture sendLoginDenied(ChannelHandlerContext ctx, String reason) {
		if((!isProtocolExchanged || clientProtocolVersion == 2) && reason.length() > 255) {
			reason = reason.substring(0, 255);
		}else if(reason.length() > 65535) {
			reason = reason.substring(0, 65535);
		}
		clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
		connectionClosed = true;
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_DENY_LOGIN);
		byte[] msg = reason.getBytes(StandardCharsets.UTF_8);
		if(!isProtocolExchanged || clientProtocolVersion == 2) {
			buf.writeByte(msg.length);
		}else {
			buf.writeShort(msg.length);
		}
		buf.writeBytes(msg);
		return ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
	}

	private ChannelFuture sendErrorWrong(ChannelHandlerContext ctx, int op, String state) {
		return sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_WRONG_PACKET, "Wrong Packet #" + op + " in state '" + state + "'");
	}
	
	private ChannelFuture sendErrorCode(ChannelHandlerContext ctx, int code, String str) {
		if((!isProtocolExchanged || clientProtocolVersion == 2) && str.length() > 255) {
			str = str.substring(0, 255);
		}else if(str.length() > 65535) {
			str = str.substring(0, 65535);
		}
		clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
		connectionClosed = true;
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_ERROR);
		buf.writeByte(code);
		byte[] msg = str.getBytes(StandardCharsets.UTF_8);
		if(!isProtocolExchanged || clientProtocolVersion == 2) {
			buf.writeByte(msg.length);
		}else {
			buf.writeShort(msg.length);
		}
		buf.writeBytes(msg);
		return ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
	}
	
	private ChannelFuture sendErrorCode(ChannelHandlerContext ctx, int code, Component comp) {
		if((!isProtocolExchanged || clientProtocolVersion == 2)) {
			return sendErrorCode(ctx, code, LegacyComponentSerializer.legacySection().serialize(comp));
		}else {
			return sendErrorCode(ctx, code, JSONLegacySerializer.instance.serialize(comp));
		}
	}
	
	public void channelInactive(ChannelHandlerContext ctx) {
		connectionClosed = true;
		EaglerPipeline.closeChannel(ctx.channel());
	}
	
	private void handleLegacyClient(ChannelHandlerContext ctx, ByteBuf buffer) {
		connectionClosed = true;
		ByteBuf kickMsg = ctx.alloc().buffer();
		final String redir = conf.redirectLegacyClientsTo();
		if(redir != null) {
			writeLegacyRedirect(kickMsg, redir);
		}else {
			writeLegacyKick(kickMsg, "This is an EaglercraftX 1.8 server, it is not compatible with 1.5.2!");
		}
		ctx.writeAndFlush(new BinaryWebSocketFrame(kickMsg)).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture var1) throws Exception {
				ctx.channel().eventLoop().schedule(new Runnable() {
					@Override
					public void run() {
						ctx.close();
					}
				}, redir != null ? 100l : 500l, TimeUnit.MILLISECONDS);
			}
		});
	}

	public static void writeLegacyKick(ByteBuf buffer, String message) {
		buffer.writeByte(0xFF);
		buffer.writeShort(message.length());
		for(int i = 0, l = message.length(), j; i < l; ++i) {
			j = message.charAt(i);
			buffer.writeByte((j >>> 8) & 0xFF);
			buffer.writeByte(j & 0xFF);
		}
	}

	public static void writeLegacyRedirect(ByteBuf buffer, String redirect) {
		buffer.writeBytes(legacyRedirectHeader);
		byte[] redirect_ = redirect.getBytes(StandardCharsets.UTF_8);
		buffer.writeByte((redirect_.length >>> 8) & 0xFF);
		buffer.writeByte(redirect_.length & 0xFF);
		buffer.writeBytes(redirect_);
	}

	private static final byte[] legacyRedirectHeader;

	static {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bao);
		try {
			// Packet1Login
			dos.writeByte(0x01);
			dos.writeInt(0);
			dos.writeShort(0);
			dos.writeByte(0);
			dos.writeByte(0);
			dos.writeByte(0xFF);
			dos.writeByte(0);
			dos.writeByte(0);
			// Packet250CustomPayload
			dos.writeByte(0xFA);
			String channel = "EAG|Reconnect";
			int cl = channel.length();
			dos.writeShort(cl);
			for(int i = 0; i < cl; ++i) {
				dos.writeChar(channel.charAt(i));
			}
		}catch(IOException ex) {
			throw new ExceptionInInitializerError(ex);
		}
		legacyRedirectHeader = bao.toByteArray();
	}

}

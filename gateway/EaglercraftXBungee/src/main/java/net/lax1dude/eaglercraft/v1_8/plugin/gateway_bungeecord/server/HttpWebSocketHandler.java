package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftHandleAuthPasswordEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftIsAuthRequiredEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftIsAuthRequiredEvent.AuthMethod;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftIsAuthRequiredEvent.AuthResponse;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftMOTDEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftRegisterSkinEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command.CommandConfirmCode;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerRateLimiter;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.RateLimitStatus;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol.EaglerBungeeProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query.MOTDQueryHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query.QueryManager;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinPackets;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinService;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.protocol.Property;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;

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
public class HttpWebSocketHandler extends ChannelInboundHandlerAdapter {
	
	private final EaglerListenerConfig conf;

	private int clientLoginState = HandshakePacketTypes.STATE_OPENED;
	private int clientProtocolVersion = -1;
	private boolean isProtocolExchanged = false;
	private int gameProtocolVersion = -1;
	private CharSequence clientBrandString;
	private CharSequence clientVersionString;
	private CharSequence clientUsername;
	private UUID clientUUID;
	private CharSequence clientRequestedServer;
	private boolean clientAuth;
	private byte[] clientAuthUsername;
	private byte[] clientAuthPassword;
	private EaglercraftIsAuthRequiredEvent authRequireEvent;
	private final Map<String, byte[]> profileData = new HashMap();
	private boolean hasFirstPacket = false;
	private boolean hasBinaryConnection = false;
	private boolean connectionClosed = false;
	private InetAddress remoteAddress;
	private String localAddrString; 
	private Property texturesOverrideProperty;
	private boolean overrideEaglerToVanillaSkins;

	public HttpWebSocketHandler(EaglerListenerConfig conf) {
		this.conf = conf;
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
		}else {
			EaglerXBungee.logger().severe("Unexpected Packet: " + msg.getClass().getSimpleName());
		}
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (ctx.channel().isActive()) {
			EaglerXBungee.logger().warning("[Yee][" + ctx.channel().remoteAddress() + "]: Exception Caught: " + cause.toString());
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
			
			BungeeCord bungus = BungeeCord.getInstance();
			int limit = bungus.config.getPlayerLimit();
			if (limit > 0 && bungus.getOnlineCount() >= limit) {
				sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, bungus.getTranslation("proxy_full"))
					.addListener(ChannelFutureListener.CLOSE);
				connectionClosed = true;
				return;
			}
			
			if(conf.getMaxPlayer() > 0) {
				int i = 0;
				for(ProxiedPlayer p : bungus.getPlayers()) {
					if(p.getPendingConnection().getListener() == conf) {
						++i;
					}
				}
				
				if (i >= conf.getMaxPlayer()) {
					sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, bungus.getTranslation("proxy_full"))
						.addListener(ChannelFutureListener.CLOSE);
					connectionClosed = true;
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
				ctx.close();
				connectionClosed = true;
				return;
			}
			
			if (loginRateLimit != RateLimitStatus.OK) {
				sendErrorCode(ctx,
						loginRateLimit == RateLimitStatus.LIMITED_NOW_LOCKED_OUT
								? HandshakePacketTypes.SERVER_ERROR_RATELIMIT_LOCKED
								: HandshakePacketTypes.SERVER_ERROR_RATELIMIT_BLOCKED,
						"Too many logins!").addListener(ChannelFutureListener.CLOSE);
				connectionClosed = true;
				return;
			}
			
			localAddrString = localSocketAddr.toString();
			EaglerXBungee.logger().info("[" + localAddrString + "]: Connected via websocket");
			if(addr != null) {
				EaglerXBungee.logger().info("[" + localAddrString + "]: Real address is " + addr.getHostAddress());
			}
			String origin = ctx.channel().attr(EaglerPipeline.ORIGIN).get();
			if(origin != null) {
				EaglerXBungee.logger().info("[" + localAddrString + "]: Origin header is " + origin);
			}else {
				EaglerXBungee.logger().info("[" + localAddrString + "]: No origin header is present!");
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
					EaglerXBungee eaglerXBungee = EaglerXBungee.getEagler();
					EaglerAuthConfig authConfig = eaglerXBungee.getConfig().getAuthConfig();
					
					final int minecraftProtocolVersion = 47;
					
					int eaglerLegacyProtocolVersion = buffer.readUnsignedByte();
					
					if(eaglerLegacyProtocolVersion == 1) {
						if(authConfig.isEnableAuthentication()) {
							sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Please update your client to register on this server!")
								.addListener(ChannelFutureListener.CLOSE);
							return;
						}else if(buffer.readUnsignedByte() != minecraftProtocolVersion) {
							clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
							ByteBuf buf = Unpooled.buffer();
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
						int minProtVers = Integer.MAX_VALUE;
						int maxProtVers = -1;
						boolean hasV2InList = false;
						boolean hasV3InList = false;
						
						int minGameVers = Integer.MAX_VALUE;
						int maxGameVers = -1;
						boolean has47InList = false;
						
						int cnt = buffer.readUnsignedShort();
						for(int i = 0; i < cnt; ++i) {
							int j = buffer.readUnsignedShort();
							if(j == 2) {
								hasV2InList = true;
							}
							if(j == 3) {
								hasV3InList = true;
							}
							if(j > maxProtVers) {
								maxProtVers = j;
							}
							if(j < minProtVers) {
								minProtVers = j;
							}
						}
						
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
						
						if(minProtVers == Integer.MAX_VALUE || minGameVers == Integer.MAX_VALUE) {
							throw new IOException();
						}
						
						boolean versMisMatch = false;
						boolean isServerProbablyOutdated = false;
						boolean isClientProbablyOutdated = false;
						if(!hasV2InList && !hasV3InList) {
							versMisMatch = true;
							isServerProbablyOutdated = minProtVers > 3 && maxProtVers > 3; //make sure to update VersionQueryHandler too
							isClientProbablyOutdated = minProtVers < 2 && maxProtVers < 2;
						}else if(!has47InList) {
							versMisMatch = true;
							isServerProbablyOutdated = minGameVers > minecraftProtocolVersion && maxGameVers > minecraftProtocolVersion;
							isClientProbablyOutdated = minGameVers < minecraftProtocolVersion && maxGameVers < minecraftProtocolVersion;
						}
						
						clientProtocolVersion = hasV3InList ? 3 : 2;
						
						if(versMisMatch) {
							clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
							ByteBuf buf = Unpooled.buffer();
							buf.writeByte(HandshakePacketTypes.PROTOCOL_VERSION_MISMATCH);
							
							buf.writeShort(2);
							buf.writeShort(2); // want v2 or v3
							buf.writeShort(3);
							
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
					CharSequence eaglerBrand = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII);
					strlen = buffer.readUnsignedByte();
					CharSequence eaglerVersionString = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII);
					
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

					final boolean final_useSnapshotFallbackProtocol = useSnapshotFallbackProtocol;
					Runnable continueThread = () -> {
						clientLoginState = HandshakePacketTypes.STATE_CLIENT_VERSION;
						gameProtocolVersion = 47;
						clientBrandString = eaglerBrand;
						clientVersionString = eaglerVersionString;
						
						ByteBuf buf = Unpooled.buffer();
						buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_VERSION);
						
						if(final_useSnapshotFallbackProtocol) {
							buf.writeByte(1);
						}else {
							buf.writeShort(clientProtocolVersion);
							buf.writeShort(minecraftProtocolVersion);
						}
						
						String brandStr = eaglerXBungee.getDescription().getName();
						buf.writeByte(brandStr.length());
						buf.writeCharSequence(brandStr, StandardCharsets.US_ASCII);
						
						String versStr = eaglerXBungee.getDescription().getVersion();
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
								EaglerXBungee.logger().severe("[" + localAddrString + "]: Disconnecting, unsupported AuthMethod: " + authRequireEvent.getUseAuthType());
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
									EaglerXBungee.logger().severe("[" + localAddrString + "]: Disconnecting, no installed authentication system handled: " + authRequireEvent.toString());
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
									EaglerXBungee.logger().severe("[" + localAddrString + "]: Disconnecting, no authentication method provided by handler");
									return;
								}
								
								int typeId = getAuthMethodId(type);
								if(typeId == -1) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "Unsupported authentication method resolved")
										.addListener(ChannelFutureListener.CLOSE);
									EaglerXBungee.logger().severe("[" + localAddrString + "]: Disconnecting, unsupported AuthMethod: " + type);
									return;
								}
								
								if(!clientAuth && resp == AuthResponse.REQUIRE) {
									sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_AUTHENTICATION_REQUIRED,
											HandshakePacketTypes.AUTHENTICATION_REQUIRED + " [" + typeId + "] " + authRequireEvent.getAuthMessage())
													.addListener(ChannelFutureListener.CLOSE);
									EaglerXBungee.logger().info("[" + localAddrString + "]: Displaying authentication screen");
									return;
								}else {
									if(authRequireEvent.getUseAuthType() == null) {
										sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, "IsAuthRequiredEvent was not fully handled")
											.addListener(ChannelFutureListener.CLOSE);
										EaglerXBungee.logger().severe("[" + localAddrString + "]: Disconnecting, no authentication method provided by handler");
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
								eaglerXBungee.getProxy().getPluginManager().callEvent(authRequireEvent);
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
					clientUsername = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII);
					
					String usrs = clientUsername.toString();
					if(!usrs.equals(usrs.replaceAll("[^A-Za-z0-9_]", "_").trim())) {
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
					clientRequestedServer = buffer.readCharSequence(strlen, StandardCharsets.US_ASCII);
					strlen = buffer.readUnsignedByte();
					clientAuthPassword = new byte[strlen];
					buffer.readBytes(clientAuthPassword);
					
					if(buffer.isReadable()) {
						throw new IllegalArgumentException("Packet too long");
					}

					Runnable continueThread = () -> {
						
						final BungeeCord bungee = BungeeCord.getInstance();
						String usernameStr = clientUsername.toString();
						final ProxiedPlayer oldName = bungee.getPlayer(usernameStr);
						if (oldName != null) {
							sendLoginDenied(ctx, bungee.getTranslation("already_connected_proxy", new Object[0]))
								.addListener(ChannelFutureListener.CLOSE);
							return;
						}
						
						clientLoginState = HandshakePacketTypes.STATE_CLIENT_LOGIN;
						ByteBuf buf = Unpooled.buffer();
						buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_ALLOW_LOGIN);
						buf.writeByte(clientUsername.length());
						buf.writeCharSequence(clientUsername, StandardCharsets.US_ASCII);
						buf.writeLong(clientUUID.getMostSignificantBits());
						buf.writeLong(clientUUID.getLeastSignificantBits());
						ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
					};

					EaglerXBungee eaglerXBungee = EaglerXBungee.getEagler();
					EaglerAuthConfig authConfig = eaglerXBungee.getConfig().getAuthConfig();
					
					if(authConfig.isEnableAuthentication() && clientAuth) {
						if(clientAuthPassword.length == 0) {
							sendLoginDenied(ctx, "Client provided no authentication code")
								.addListener(ChannelFutureListener.CLOSE);
							return;
						}else {
							try {
								EaglercraftHandleAuthPasswordEvent handleEvent = new EaglercraftHandleAuthPasswordEvent(
										conf, remoteAddress, authRequireEvent.getOriginHeader(), clientAuthUsername,
										authRequireEvent.getSaltingData(), clientUsername, clientUUID, clientAuthPassword,
										authRequireEvent.getUseAuthType(), authRequireEvent.getAuthMessage(),
										(Object) authRequireEvent.getAuthAttachment(), clientRequestedServer.toString(),
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
									eaglerXBungee.getProxy().getPluginManager().callEvent(handleEvent);
								}
								
								if(!handleEvent.isAsyncContinue()) {
									handleEvent.doDirectContinue();
								}
							}catch(Throwable t) {
								throw new EventException(t);
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
					
					if(profileData.size() > 12) {
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
			if(ex instanceof EventException) {
				EaglerXBungee.logger().log(Level.SEVERE, "[" + localAddrString + "]: Hanshake packet " + op + " caught an exception", ex.getCause());
			}
			clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_INVALID_PACKET, op == -1 ?
					"Invalid Packet" : "Invalid Packet #" + op)
				.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	private void finish(final ChannelHandlerContext ctx) {
		final BungeeCord bungee = BungeeCord.getInstance();
		
		// verify player counts a second time after handshake just to be safe
		int limit = bungee.config.getPlayerLimit();
		if (limit > 0 && bungee.getOnlineCount() >= limit) {
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, bungee.getTranslation("proxy_full"))
				.addListener(ChannelFutureListener.CLOSE);
			connectionClosed = true;
			return;
		}
		
		if(conf.getMaxPlayer() > 0) {
			int i = 0;
			for(ProxiedPlayer p : bungee.getPlayers()) {
				if(p.getPendingConnection().getListener() == conf) {
					++i;
				}
			}
			
			if (i >= conf.getMaxPlayer()) {
				sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE, bungee.getTranslation("proxy_full"))
					.addListener(ChannelFutureListener.CLOSE);
				connectionClosed = true;
				return;
			}
		}
		
		final String usernameStr = clientUsername.toString();
		final ProxiedPlayer oldName = bungee.getPlayer(usernameStr);
		if (oldName != null) {
			sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
					bungee.getTranslation("already_connected_proxy", new Object[0]))
							.addListener(ChannelFutureListener.CLOSE);
			return;
		}
		final ChannelWrapper ch = new EaglerChannelWrapper(ctx);
		InetSocketAddress baseAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		InetAddress addr = ctx.channel().attr(EaglerPipeline.REAL_ADDRESS).get();
		if(addr != null) {
			baseAddress = new InetSocketAddress(addr, baseAddress.getPort());
			ch.setRemoteAddress(baseAddress);
		}
		final EaglerInitialHandler initialHandler = new EaglerInitialHandler(bungee, conf, ch, gameProtocolVersion,
				usernameStr, clientUUID, baseAddress, ctx.channel().attr(EaglerPipeline.HOST).get(),
				ctx.channel().attr(EaglerPipeline.ORIGIN).get());
		final Callback<LoginEvent> complete = (Callback<LoginEvent>) new Callback<LoginEvent>() {
			public void done(final LoginEvent result, final Throwable error) {
				if (result.isCancelled()) {
					final BaseComponent[] reason = result.getCancelReasonComponents();
					sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
							ComponentSerializer.toString(reason != null ? reason
									: TextComponent.fromLegacyText(bungee.getTranslation("kick_message", new Object[0]))))
							.addListener(ChannelFutureListener.CLOSE);
					return;
				}
				if (!ctx.channel().isActive()) {
					return;
				}
				
				ByteBuf buf = Unpooled.buffer();
				buf.writeByte(HandshakePacketTypes.PROTOCOL_SERVER_FINISH_LOGIN);
				ctx.writeAndFlush(new BinaryWebSocketFrame(buf)).addListener(new GenericFutureListener<Future<Void>>() {

					@Override
					public void operationComplete(Future<Void> var1) throws Exception {
						EaglerConnectionInstance eaglerCon = ctx.channel().attr(EaglerPipeline.CONNECTION_INSTANCE).get();
						
						EaglerXBungee.logger().info("[" + ctx.channel().remoteAddress() + "]: Logged in as '" + usernameStr + "'");
						
						final UserConnection userCon = eaglerCon.userConnection = new UserConnection(bungee, ch, usernameStr, initialHandler);
						userCon.setCompressionThreshold(-1);
						userCon.init();
						
						ChannelPipeline pp = ctx.channel().pipeline();
						
						HandlerBoss handler = new HandlerBoss() {
							public void channelInactive(ChannelHandlerContext ctx) throws Exception {
								super.channelInactive(ctx);
								EaglerPipeline.closeChannel(ctx.channel());
							}
						};
						handler.setHandler(new UpstreamBridge(bungee, userCon));
						try {
							handler.channelActive(ctx);
						} catch (Exception e) {
						}
						
						pp.replace(HttpWebSocketHandler.this, "HandlerBoss", handler);
						
						pp.addBefore("HandlerBoss", "ReadTimeoutHandler", new ReadTimeoutHandler((BungeeCord.getInstance()).config.getTimeout(), TimeUnit.MILLISECONDS));
						
						pp.addBefore("HandlerBoss", "EaglerMinecraftDecoder", new EaglerMinecraftDecoder(
								EaglerBungeeProtocol.GAME, false, gameProtocolVersion));
						
						pp.addBefore("HandlerBoss", "EaglerMinecraftByteBufEncoder", new EaglerMinecraftByteBufEncoder());
						
						pp.addBefore("HandlerBoss", "EaglerMinecraftWrappedEncoder", new EaglerMinecraftWrappedEncoder());
						
						pp.addBefore("HandlerBoss", "EaglerMinecraftEncoder", new EaglerMinecraftEncoder(
								EaglerBungeeProtocol.GAME, true, gameProtocolVersion));
						
						boolean doRegisterSkins = true;
						
						EaglercraftRegisterSkinEvent registerSkinEvent = new EaglercraftRegisterSkinEvent(usernameStr, clientUUID);
						
						bungee.getPluginManager().callEvent(registerSkinEvent);
						
						Property prop = registerSkinEvent.getForceUseMojangProfileProperty();
						boolean useExistingProp = registerSkinEvent.getForceUseLoginResultObjectTextures();
						if(prop != null) {
							texturesOverrideProperty = prop;
							overrideEaglerToVanillaSkins = true;
						}else {
							if(useExistingProp) {
								overrideEaglerToVanillaSkins = true;
							}else {
								byte[] custom = registerSkinEvent.getForceSetUseCustomPacket();
								if(custom != null) {
									profileData.put("skin_v1", custom);
									overrideEaglerToVanillaSkins = false;
								}else {
									String customUrl = registerSkinEvent.getForceSetUseURL();
									if(customUrl != null) {
										EaglerXBungee.getEagler().getSkinService().registerTextureToPlayerAssociation(customUrl, initialHandler.getUniqueId());
										doRegisterSkins = false;
										overrideEaglerToVanillaSkins = false;
									}
								}
							}
						}
						
						EaglerBungeeConfig eaglerConf = EaglerXBungee.getEagler().getConfig();
						
						
						if(texturesOverrideProperty != null) {
							LoginResult oldProfile = initialHandler.getLoginProfile();
							if(oldProfile == null) {
								oldProfile = new LoginResult(initialHandler.getUniqueId().toString(), initialHandler.getName(), null);
								initialHandler.setLoginProfile(oldProfile);
							}
							oldProfile.setProperties(new Property[] { texturesOverrideProperty, EaglerBungeeConfig.isEaglerProperty });
						}else {
							if(!useExistingProp) {
								String vanillaSkin = eaglerConf.getEaglerPlayersVanillaSkin();
								if(vanillaSkin != null) {
									LoginResult oldProfile = initialHandler.getLoginProfile();
									if(oldProfile == null) {
										oldProfile = new LoginResult(initialHandler.getUniqueId().toString(), initialHandler.getName(), null);
										initialHandler.setLoginProfile(oldProfile);
									}
									oldProfile.setProperties(eaglerConf.getEaglerPlayersVanillaSkinProperties());
								}
							}
						}
						
						if(overrideEaglerToVanillaSkins) {
							LoginResult res = initialHandler.getLoginProfile();
							if(res != null) {
								Property[] props = res.getProperties();
								if(props != null) {
									for(int i = 0; i < props.length; ++i) {
										if("textures".equals(props[i].getName())) {
											try {
												String jsonStr = SkinPackets.bytesToAscii(Base64.decodeBase64(props[i].getValue()));
												JsonObject json = (new JsonParser()).parse(jsonStr).getAsJsonObject();
												JsonObject skinObj = json.getAsJsonObject("SKIN");
												if(skinObj != null) {
													JsonElement url = json.get("url");
													if(url != null) {
														String urlStr = SkinService.sanitizeTextureURL(url.getAsString());
														EaglerXBungee.getEagler().getSkinService().registerTextureToPlayerAssociation(urlStr, initialHandler.getUniqueId());
													}
												}
												doRegisterSkins = false;
											}catch(Throwable t) {
											}
											break;
										}
									}
								}
							}
						}
						
						if(doRegisterSkins) {
							if(profileData.containsKey("skin_v1")) {
								try {
									SkinPackets.registerEaglerPlayer(clientUUID, profileData.get("skin_v1"),
											EaglerXBungee.getEagler().getSkinService());
								} catch (Throwable ex) {
									SkinPackets.registerEaglerPlayerFallback(clientUUID, EaglerXBungee.getEagler().getSkinService());
									EaglerXBungee.logger().info("[" + ctx.channel().remoteAddress() + "]: Invalid skin packet: " + ex.toString());
								}
							}else {
								SkinPackets.registerEaglerPlayerFallback(clientUUID, EaglerXBungee.getEagler().getSkinService());
							}
						}
						
						bungee.getPluginManager().callEvent(new PostLoginEvent(userCon));
						
						ServerInfo server;
						if (bungee.getReconnectHandler() != null) {
							server = bungee.getReconnectHandler().getServer((ProxiedPlayer) userCon);
						} else {
							server = AbstractReconnectHandler.getForcedHost(initialHandler);
						}

						if (server == null) {
							server = bungee.getServerInfo(conf.getDefaultServer());
						}
						
						eaglerCon.hasBeenForwarded = true;
						userCon.connect(server, null, true, ServerConnectEvent.Reason.JOIN_PROXY);
					}
					
				});
			}
		};
		final Callback<PreLoginEvent> completePre = new Callback<PreLoginEvent>() {
			public void done(PreLoginEvent var1, Throwable var2) {
				if (var1.isCancelled()) {
					final BaseComponent[] reason = var1.getCancelReasonComponents();
					sendErrorCode(ctx, HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE,
							ComponentSerializer.toString(reason != null ? reason
									: TextComponent.fromLegacyText(bungee.getTranslation("kick_message", new Object[0]))))
							.addListener(ChannelFutureListener.CLOSE);
				}else {
					bungee.getPluginManager().callEvent(new LoginEvent(initialHandler, complete));
				}
			}
		};
		bungee.getPluginManager().callEvent(new PreLoginEvent(initialHandler, completePre));
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
		if(!hasFirstPacket && (conf.isAllowMOTD() || conf.isAllowQuery()) && (str = str.toLowerCase()).startsWith("accept:")) {
			str = str.substring(7).trim();
			hasFirstPacket = true;
			hasBinaryConnection = false;
			
			if(CommandConfirmCode.confirmHash != null && str.equalsIgnoreCase(CommandConfirmCode.confirmHash)) {
				ctx.writeAndFlush(new TextWebSocketFrame("OK")).addListener(ChannelFutureListener.CLOSE);
				CommandConfirmCode.confirmHash = null;
				connectionClosed = true;
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
				ctx.close();
				connectionClosed = true;
				return;
			}
			
			if(queryRateLimit != RateLimitStatus.OK) {
				final RateLimitStatus rateLimitTypeFinal = queryRateLimit;
				ctx.writeAndFlush(new TextWebSocketFrame(
						rateLimitTypeFinal == RateLimitStatus.LIMITED_NOW_LOCKED_OUT ? "{\"type\":\"locked\"}" : "{\"type\":\"blocked\"}"))
						.addListener(ChannelFutureListener.CLOSE);
				connectionClosed = true;
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
				handler.beginHandleQuery(conf, ctx, str);
				if(handler instanceof MOTDQueryHandler) {
					EaglercraftMOTDEvent evt = new EaglercraftMOTDEvent((MOTDQueryHandler)handler);
					BungeeCord.getInstance().getPluginManager().callEvent(evt);
					if(!handler.isClosed()) {
						((MOTDQueryHandler)handler).sendToUser();
					}
				}
				if(!handler.isClosed() && !handler.shouldKeepAlive()) {
					connectionClosed = true;
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
			reason = reason.substring(0, 256);
		}else if(reason.length() > 65535) {
			reason = reason.substring(0, 65536);
		}
		clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
		connectionClosed = true;
		ByteBuf buf = Unpooled.buffer();
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
			str = str.substring(0, 256);
		}else if(str.length() > 65535) {
			str = str.substring(0, 65536);
		}
		clientLoginState = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
		connectionClosed = true;
		ByteBuf buf = Unpooled.buffer();
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
			buffer.writeByte((j >> 8) & 0xFF);
			buffer.writeByte(j & 0xFF);
		}
	}

	public static void writeLegacyRedirect(ByteBuf buffer, String redirect) {
		buffer.writeBytes(legacyRedirectHeader);
		byte[] redirect_ = redirect.getBytes(StandardCharsets.UTF_8);
		buffer.writeByte((redirect_.length >> 8) & 0xFF);
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

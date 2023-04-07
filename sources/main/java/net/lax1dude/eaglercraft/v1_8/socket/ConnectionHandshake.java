package net.lax1dude.eaglercraft.v1_8.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.crypto.SHA256Digest;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.profile.GuiAuthenticationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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
public class ConnectionHandshake {

	private static final long baseTimeout = 15000l;

	private static final int protocolV2 = 2;
	private static final int protocolV3 = 3;
	
	private static final Logger logger = LogManager.getLogger();
	
	public static boolean attemptHandshake(Minecraft mc, GuiConnecting connecting, GuiScreen ret, String password, boolean allowPlaintext) {
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			DataOutputStream d = new DataOutputStream(bao);
			
			d.writeByte(HandshakePacketTypes.PROTOCOL_CLIENT_VERSION);
			
			d.writeByte(2); // legacy protocol version
			
			d.writeShort(2); // supported eagler protocols count
			d.writeShort(protocolV2); // client supports v2
			d.writeShort(protocolV3); // client supports v3

			d.writeShort(1); // supported game protocols count
			d.writeShort(47); // client supports 1.8 protocol
			
			String clientBrand = EaglercraftVersion.projectForkName;
			d.writeByte(clientBrand.length());
			d.writeBytes(clientBrand);
			
			String clientVers = EaglercraftVersion.projectOriginVersion;
			d.writeByte(clientVers.length());
			d.writeBytes(clientVers);
			
			d.writeBoolean(password != null);
			
			String username = mc.getSession().getProfile().getName();
			d.writeByte(username.length());
			d.writeBytes(username);
			
			PlatformNetworking.writePlayPacket(bao.toByteArray());
			
			byte[] read = awaitNextPacket(baseTimeout);
			if(read == null) {
				logger.error("Read timed out while waiting for server protocol response!");
				return false;
			}
			
			DataInputStream di = new DataInputStream(new EaglerInputStream(read));
			
			int type = di.read();
			if(type == HandshakePacketTypes.PROTOCOL_VERSION_MISMATCH) {
				
				StringBuilder protocols = new StringBuilder();
				int c = di.readShort();
				for(int i = 0; i < c; ++i) {
					if(i > 0) {
						protocols.append(", ");
					}
					protocols.append("v").append(di.readShort());
				}
				
				StringBuilder games = new StringBuilder();
				c = di.readShort();
				for(int i = 0; i < c; ++i) {
					if(i > 0) {
						games.append(", ");
					}
					games.append("mc").append(di.readShort());
				}
				
				logger.info("Incompatible client: v2 & mc47");
				logger.info("Server supports: {}", protocols);
				logger.info("Server supports: {}", games);
				
				int msgLen = di.read();
				byte[] dat = new byte[msgLen];
				di.read(dat);
				String msg = new String(dat, StandardCharsets.UTF_8);
				
				mc.displayGuiScreen(new GuiDisconnected(ret, "connect.failed", new ChatComponentText(msg)));
				
				return false;
			}else if(type == HandshakePacketTypes.PROTOCOL_SERVER_VERSION) {
				int serverVers = di.readShort();
				
				if(serverVers != protocolV2 && serverVers != protocolV3) {
					logger.info("Incompatible server version: {}", serverVers);
					mc.displayGuiScreen(new GuiDisconnected(ret, "connect.failed", new ChatComponentText(serverVers < protocolV2 ? "Outdated Server" : "Outdated Client")));
					return false;
				}
				
				int gameVers = di.readShort();
				if(gameVers != 47) {
					logger.info("Incompatible minecraft protocol version: {}", gameVers);
					mc.displayGuiScreen(new GuiDisconnected(ret, "connect.failed", new ChatComponentText("This server does not support 1.8!")));
					return false;
				}
				
				logger.info("Server protocol: {}", serverVers);
				
				int msgLen = di.read();
				byte[] dat = new byte[msgLen];
				di.read(dat);
				String brandStr = ArrayUtils.asciiString(dat);
				
				msgLen = di.read();
				dat = new byte[msgLen];
				di.read(dat);
				String versionStr = ArrayUtils.asciiString(dat);
				
				logger.info("Server version: {}", versionStr);
				logger.info("Server brand: {}", brandStr);
				
				if(versionStr.equals("1.0.0") || versionStr.equals("1.0.1") || versionStr.equals("1.0.2")
						|| versionStr.equals("1.0.3") || versionStr.equals("1.0.4") || versionStr.equals("1.0.5")) {
					mc.bungeeOutdatedMsgTimer = 80;
					mc.bungeeOutdatedMsgVer = versionStr;
				}else {
					mc.bungeeOutdatedMsgTimer = 0;
				}
				
				int authType = di.read();
				int saltLength = (int)di.readShort() & 0xFFFF;
				
				byte[] salt = new byte[saltLength];
				di.read(salt);
				
				bao.reset();
				d.writeByte(HandshakePacketTypes.PROTOCOL_CLIENT_REQUEST_LOGIN);
				
				d.writeByte(username.length());
				d.writeBytes(username);
				
				String requestedServer = "default";
				d.writeByte(requestedServer.length());
				d.writeBytes(requestedServer);
				
				if(authType != 0 && password != null && password.length() > 0) {
					if(authType == HandshakePacketTypes.AUTH_METHOD_PLAINTEXT) {
						if(allowPlaintext) {
							logger.warn("Server is using insecure plaintext authentication");
							d.writeByte(password.length() << 1);
							d.writeChars(password);
						}else {
							logger.error("Plaintext authentication was attempted but no user confirmation has been given to proceed");
							mc.displayGuiScreen(new GuiDisconnected(ret, "connect.failed",
									new ChatComponentText(EnumChatFormatting.RED + "Plaintext authentication was attempted but no user confirmation has been given to proceed")));
							return false;
						}
					}else if(authType == HandshakePacketTypes.AUTH_METHOD_EAGLER_SHA256) {
						SHA256Digest digest = new SHA256Digest();
						
						int passLen = password.length();
						
						digest.update((byte)((passLen >> 8) & 0xFF));
						digest.update((byte)(passLen & 0xFF));
						
						for(int i = 0; i < passLen; ++i) {
							char codePoint = password.charAt(i);
							digest.update((byte)((codePoint >> 8) & 0xFF));
							digest.update((byte)(codePoint & 0xFF));
						}
						
						digest.update(HandshakePacketTypes.EAGLER_SHA256_SALT_SAVE, 0, 32);
						
						byte[] hashed = new byte[32];
						digest.doFinal(hashed, 0);
						
						digest.reset();
						
						digest.update(hashed, 0, 32);
						digest.update(salt, 0, 32);
						digest.update(HandshakePacketTypes.EAGLER_SHA256_SALT_BASE, 0, 32);
						
						digest.doFinal(hashed, 0);
						
						digest.reset();
						
						digest.update(hashed, 0, 32);
						digest.update(salt, 32, 32);
						digest.update(HandshakePacketTypes.EAGLER_SHA256_SALT_BASE, 0, 32);
						
						digest.doFinal(hashed, 0);
						
						d.writeByte(32);
						d.write(hashed);
					}else if(authType == HandshakePacketTypes.AUTH_METHOD_AUTHME_SHA256) {
						SHA256Digest digest = new SHA256Digest();
						
						byte[] passwd = password.getBytes(StandardCharsets.UTF_8);
						digest.update(passwd, 0, passwd.length);
						
						byte[] hashed = new byte[32];
						digest.doFinal(hashed, 0);
						
						byte[] toHexAndSalt = new byte[64];
						for(int i = 0; i < 32; ++i) {
							toHexAndSalt[i << 1] = HEX[(hashed[i] >> 4) & 0xF];
							toHexAndSalt[(i << 1) + 1] = HEX[hashed[i] & 0xF];
						}
						
						digest.reset();
						digest.update(toHexAndSalt, 0, 64);
						digest.update(salt, 0, salt.length);
						
						digest.doFinal(hashed, 0);
						
						for(int i = 0; i < 32; ++i) {
							toHexAndSalt[i << 1] = HEX[(hashed[i] >> 4) & 0xF];
							toHexAndSalt[(i << 1) + 1] = HEX[hashed[i] & 0xF];
						}

						d.writeByte(64);
						d.write(toHexAndSalt);
					}else {
						logger.error("Unsupported authentication type: {}", authType);
						mc.displayGuiScreen(new GuiDisconnected(ret, "connect.failed",
								new ChatComponentText(EnumChatFormatting.RED + "Unsupported authentication type: " + authType + "\n\n" + EnumChatFormatting.GRAY + "(Use a newer version of the client)")));
						return false;
					}
				}else {
					d.writeByte(0);
				}
				
				PlatformNetworking.writePlayPacket(bao.toByteArray());
				
				read = awaitNextPacket(baseTimeout);
				if(read == null) {
					logger.error("Read timed out while waiting for login negotiation response!");
					return false;
				}
				
				di = new DataInputStream(new EaglerInputStream(read));
				type = di.read();
				if(type == HandshakePacketTypes.PROTOCOL_SERVER_ALLOW_LOGIN) {
					msgLen = di.read();
					dat = new byte[msgLen];
					di.read(dat);
					
					String serverUsername = ArrayUtils.asciiString(dat);
					
					Minecraft.getMinecraft().getSession().update(serverUsername, new EaglercraftUUID(di.readLong(), di.readLong()));
					
					bao.reset();
					d.writeByte(HandshakePacketTypes.PROTOCOL_CLIENT_PROFILE_DATA);
					String profileDataType = "skin_v1";
					d.writeByte(profileDataType.length());
					d.writeBytes(profileDataType);
					byte[] packetSkin = EaglerProfile.getSkinPacket();
					if(packetSkin.length > 0xFFFF) {
						throw new IOException("Skin packet is too long: " + packetSkin.length);
					}
					d.writeShort(packetSkin.length);
					d.write(packetSkin);
					PlatformNetworking.writePlayPacket(bao.toByteArray());
					
					bao.reset();
					d.writeByte(HandshakePacketTypes.PROTOCOL_CLIENT_FINISH_LOGIN);
					PlatformNetworking.writePlayPacket(bao.toByteArray());
					
					read = awaitNextPacket(baseTimeout);
					if(read == null) {
						logger.error("Read timed out while waiting for login confirmation response!");
						return false;
					}
					
					di = new DataInputStream(new EaglerInputStream(read));
					type = di.read();
					if(type == HandshakePacketTypes.PROTOCOL_SERVER_FINISH_LOGIN) {
						return true;
					}else if(type == HandshakePacketTypes.PROTOCOL_SERVER_ERROR) {
						showError(mc, connecting, ret, di, serverVers == protocolV2);
						return false;
					}else {
						return false;
					}
				}else if(type == HandshakePacketTypes.PROTOCOL_SERVER_DENY_LOGIN) {
					if(serverVers == protocolV2) {
						msgLen = di.read();
					}else {
						msgLen = di.readUnsignedShort();
					}
					dat = new byte[msgLen];
					di.read(dat);
					String errStr = new String(dat, StandardCharsets.UTF_8);
					mc.displayGuiScreen(new GuiDisconnected(ret, "connect.failed", IChatComponent.Serializer.jsonToComponent(errStr)));
					return false;
				}else if(type == HandshakePacketTypes.PROTOCOL_SERVER_ERROR) {
					showError(mc, connecting, ret, di, serverVers == protocolV2);
					return false;
				}else {
					return false;
				}
			}else if(type == HandshakePacketTypes.PROTOCOL_SERVER_ERROR) {
				showError(mc, connecting, ret, di, true);
				return false;
			}else {
				return false;
			}
		}catch(Throwable t) {
			logger.error("Exception in handshake");
			logger.error(t);
			return false;
		}
		
	}
	
	private static byte[] awaitNextPacket(long timeout) {
		long millis = System.currentTimeMillis();
		byte[] b;
		while((b = PlatformNetworking.readPlayPacket()) == null) {
			if(PlatformNetworking.playConnectionState().isClosed()) {
				return null;
			}
			try {
				Thread.sleep(50l);
			} catch (InterruptedException e) {
			}
			if(System.currentTimeMillis() - millis > timeout) {
				PlatformNetworking.playDisconnect();
				return null;
			}
		}
		return b;
	}
	
	private static void showError(Minecraft mc, GuiConnecting connecting, GuiScreen scr, DataInputStream err, boolean v2) throws IOException {
		int errorCode = err.read();
		int msgLen = v2 ? err.read() : err.readUnsignedShort();
		byte[] dat = new byte[msgLen];
		err.read(dat);
		String errStr = new String(dat, StandardCharsets.UTF_8);
		logger.info("Server Error Code {}: {}", errorCode, errStr);
		if(errorCode == HandshakePacketTypes.SERVER_ERROR_RATELIMIT_BLOCKED) {
			RateLimitTracker.registerBlock(PlatformNetworking.getCurrentURI());
			mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(scr));
		}else if(errorCode == HandshakePacketTypes.SERVER_ERROR_RATELIMIT_LOCKED) {
			RateLimitTracker.registerLockOut(PlatformNetworking.getCurrentURI());
			mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(scr));
		}else if(errorCode == HandshakePacketTypes.SERVER_ERROR_CUSTOM_MESSAGE) {
			mc.displayGuiScreen(new GuiDisconnected(scr, "connect.failed", IChatComponent.Serializer.jsonToComponent(errStr)));
		}else if(connecting != null && errorCode == HandshakePacketTypes.SERVER_ERROR_AUTHENTICATION_REQUIRED) {
			mc.displayGuiScreen(new GuiAuthenticationScreen(connecting, scr, errStr));
		}else {
			mc.displayGuiScreen(new GuiDisconnected(scr, "connect.failed", new ChatComponentText("Server Error Code " + errorCode + "\n" + errStr)));
		}
	}
	
	public static GuiScreen displayAuthProtocolConfirm(int protocol, GuiScreen no, GuiScreen yes) {
		if(protocol == HandshakePacketTypes.AUTH_METHOD_PLAINTEXT) {
			return new GuiHandshakeApprove("plaintext", no, yes);
		}else if(protocol != HandshakePacketTypes.AUTH_METHOD_EAGLER_SHA256 && protocol != HandshakePacketTypes.AUTH_METHOD_AUTHME_SHA256) {
			return new GuiHandshakeApprove("unsupportedAuth", no);
		}else {
			return null;
		}
	}
	
	private static final byte[] HEX = new byte[] {
		(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
		(byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
	};
}

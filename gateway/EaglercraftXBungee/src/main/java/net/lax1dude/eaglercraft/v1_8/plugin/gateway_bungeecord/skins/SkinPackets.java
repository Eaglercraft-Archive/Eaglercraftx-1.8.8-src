package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.md_5.bungee.UserConnection;

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
public class SkinPackets {

	public static final int PACKET_MY_SKIN_PRESET = 0x01;
	public static final int PACKET_MY_SKIN_CUSTOM = 0x02;
	public static final int PACKET_GET_OTHER_SKIN = 0x03;
	public static final int PACKET_OTHER_SKIN_PRESET = 0x04;
	public static final int PACKET_OTHER_SKIN_CUSTOM = 0x05;
	public static final int PACKET_GET_SKIN_BY_URL = 0x06;
	
	public static void processPacket(byte[] data, UserConnection sender, ISkinService skinService) throws IOException {
		if(data.length == 0) {
			throw new IOException("Zero-length packet recieved");
		}
		int packetId = (int)data[0] & 0xFF;
		try {
			switch(packetId) {
			case PACKET_GET_OTHER_SKIN:
				processGetOtherSkin(data, sender, skinService);
				break;
			case PACKET_GET_SKIN_BY_URL:
				processGetOtherSkinByURL(data, sender, skinService);
				break;
			default:
				throw new IOException("Unknown packet type " + packetId);
			}
		}catch(IOException ex) {
			throw ex;
		}catch(Throwable t) {
			throw new IOException("Unhandled exception handling packet type " + packetId, t);
		}
	}
	
	private static void processGetOtherSkin(byte[] data, UserConnection sender, ISkinService skinService) throws IOException {
		if(data.length != 17) {
			throw new IOException("Invalid length " + data.length + " for skin request packet");
		}
		UUID searchUUID = bytesToUUID(data, 1);
		skinService.processGetOtherSkin(searchUUID, sender);
	}
	
	private static void processGetOtherSkinByURL(byte[] data, UserConnection sender, ISkinService skinService) throws IOException {
		if(data.length < 20) {
			throw new IOException("Invalid length " + data.length + " for skin request packet");
		}
		UUID searchUUID = bytesToUUID(data, 1);
		int urlLength = (data[17] << 8) | data[18];
		if(data.length < 19 + urlLength) {
			throw new IOException("Invalid length " + data.length + " for skin request packet with " + urlLength + " length URL");
		}
		String urlStr = bytesToAscii(data, 19, urlLength);
		urlStr = SkinService.sanitizeTextureURL(urlStr);
		if(urlStr == null) {
			throw new IOException("Invalid URL for skin request packet");
		}
		URL url;
		try {
			url = new URL(urlStr);
		}catch(MalformedURLException t) {
			throw new IOException("Invalid URL for skin request packet", t);
		}
		String host = url.getHost();
		if(EaglerXBungee.getEagler().getConfig().isValidSkinHost(host)) {
			UUID validUUID = createEaglerURLSkinUUID(urlStr);
			if(!searchUUID.equals(validUUID)) {
				throw new IOException("Invalid generated UUID from skin URL");
			}
			skinService.processGetOtherSkin(searchUUID, urlStr, sender);
		}else {
			throw new IOException("Invalid host in skin packet: " + host);
		}
	}
	
	public static void registerEaglerPlayer(UUID clientUUID, byte[] bs, ISkinService skinService) throws IOException {
		if(bs.length == 0) {
			throw new IOException("Zero-length packet recieved");
		}
		byte[] generatedPacket;
		int skinModel = -1;
		int packetType = (int)bs[0] & 0xFF;
		switch(packetType) {
		case PACKET_MY_SKIN_PRESET:
			if(bs.length != 5) {
				throw new IOException("Invalid length " + bs.length + " for preset skin packet");
			}
			generatedPacket = SkinPackets.makePresetResponse(clientUUID, (bs[1] << 24) | (bs[2] << 16) | (bs[3] << 8) | (bs[4] & 0xFF));
			break;
		case PACKET_MY_SKIN_CUSTOM:
			byte[] pixels = new byte[16384];
			if(bs.length != 2 + pixels.length) {
				throw new IOException("Invalid length " + bs.length + " for custom skin packet");
			}
			setAlphaForChest(pixels, (byte)255);
			System.arraycopy(bs, 2, pixels, 0, pixels.length);
			generatedPacket = SkinPackets.makeCustomResponse(clientUUID, (skinModel = (int)bs[1] & 0xFF), pixels);
			break;
		default:
			throw new IOException("Unknown skin packet type: " + packetType);
		}
		skinService.registerEaglercraftPlayer(clientUUID, generatedPacket, skinModel);
	}
	
	public static void registerEaglerPlayerFallback(UUID clientUUID, ISkinService skinService) throws IOException {
		int skinModel = (clientUUID.hashCode() & 1) != 0 ? 1 : 0;
		byte[] generatedPacket = SkinPackets.makePresetResponse(clientUUID, skinModel);
		skinService.registerEaglercraftPlayer(clientUUID, generatedPacket, skinModel);
	}
	
	public static void setAlphaForChest(byte[] skin64x64, byte alpha) {
		if(skin64x64.length != 16384) {
			throw new IllegalArgumentException("Skin is not 64x64!");
		}
		for(int y = 20; y < 32; ++y) {
			for(int x = 16; x < 40; ++x) {
				skin64x64[(y << 8) | (x << 2)] = alpha;
			}
		}
	}
	
	public static byte[] makePresetResponse(UUID uuid) {
		return makePresetResponse(uuid, (uuid.hashCode() & 1) != 0 ? 1 : 0);
	}
	
	public static byte[] makePresetResponse(UUID uuid, int presetId) {
		byte[] ret = new byte[1 + 16 + 4];
		ret[0] = (byte)PACKET_OTHER_SKIN_PRESET;
		UUIDToBytes(uuid, ret, 1);
		ret[17] = (byte)(presetId >> 24);
		ret[18] = (byte)(presetId >> 16);
		ret[19] = (byte)(presetId >> 8);
		ret[20] = (byte)(presetId & 0xFF);
		return ret;
	}
	
	public static byte[] makeCustomResponse(UUID uuid, int model, byte[] pixels) {
		byte[] ret = new byte[1 + 16 + 1 + pixels.length];
		ret[0] = (byte)PACKET_OTHER_SKIN_CUSTOM;
		UUIDToBytes(uuid, ret, 1);
		ret[17] = (byte)model;
		System.arraycopy(pixels, 0, ret, 18, pixels.length);
		return ret;
	}
	
	public static UUID bytesToUUID(byte[] bytes, int off) {
		long msb = (((long) bytes[off] & 0xFFl) << 56l) | (((long) bytes[off + 1] & 0xFFl) << 48l)
				| (((long) bytes[off + 2] & 0xFFl) << 40l) | (((long) bytes[off + 3] & 0xFFl) << 32l)
				| (((long) bytes[off + 4] & 0xFFl) << 24l) | (((long) bytes[off + 5] & 0xFFl) << 16l)
				| (((long) bytes[off + 6] & 0xFFl) << 8l) | ((long) bytes[off + 7] & 0xFFl);
		long lsb = (((long) bytes[off + 8] & 0xFFl) << 56l) | (((long) bytes[off + 9] & 0xFFl) << 48l)
				| (((long) bytes[off + 10] & 0xFFl) << 40l) | (((long) bytes[off + 11] & 0xFFl) << 32l)
				| (((long) bytes[off + 12] & 0xFFl) << 24l) | (((long) bytes[off + 13] & 0xFFl) << 16l)
				| (((long) bytes[off + 14] & 0xFFl) << 8l) | ((long) bytes[off + 15] & 0xFFl);
		return new UUID(msb, lsb);
	}
	
	private static final String hex = "0123456789abcdef";
	
	public static String bytesToString(byte[] bytes, int off, int len) {
		char[] ret = new char[len << 1];
		for(int i = 0; i < len; ++i) {
			ret[i * 2] = hex.charAt((bytes[off + i] >> 4) & 0xF);
			ret[i * 2 + 1] = hex.charAt(bytes[off + i] & 0xF);
		}
		return new String(ret);
	}
	
	public static String bytesToAscii(byte[] bytes, int off, int len) {
		char[] ret = new char[len];
		for(int i = 0; i < len; ++i) {
			ret[i] = (char)((int)bytes[off + i] & 0xFF);
		}
		return new String(ret);
	}

	public static String bytesToAscii(byte[] bytes) {
		return bytesToAscii(bytes, 0, bytes.length);
	}
	
	public static void UUIDToBytes(UUID uuid, byte[] bytes, int off) {
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		bytes[off] = (byte)(msb >> 56l);
		bytes[off + 1] = (byte)(msb >> 48l);
		bytes[off + 2] = (byte)(msb >> 40l);
		bytes[off + 3] = (byte)(msb >> 32l);
		bytes[off + 4] = (byte)(msb >> 24l);
		bytes[off + 5] = (byte)(msb >> 16l);
		bytes[off + 6] = (byte)(msb >> 8l);
		bytes[off + 7] = (byte)(msb & 0xFFl);
		bytes[off + 8] = (byte)(lsb >> 56l);
		bytes[off + 9] = (byte)(lsb >> 48l);
		bytes[off + 10] = (byte)(lsb >> 40l);
		bytes[off + 11] = (byte)(lsb >> 32l);
		bytes[off + 12] = (byte)(lsb >> 24l);
		bytes[off + 13] = (byte)(lsb >> 16l);
		bytes[off + 14] = (byte)(lsb >> 8l);
		bytes[off + 15] = (byte)(lsb & 0xFFl);
	}
	
	public static byte[] asciiString(String string) {
		byte[] str = new byte[string.length()];
		for(int i = 0; i < str.length; ++i) {
			str[i] = (byte)string.charAt(i);
		}
		return str;
	}
	
	public static UUID createEaglerURLSkinUUID(String skinUrl) {
		return UUID.nameUUIDFromBytes(asciiString("EaglercraftSkinURL:" + skinUrl));
	}

	public static int getModelId(String modelName) {
		return "slim".equalsIgnoreCase(modelName) ? 1 : 0;
	}

	public static byte[] rewriteUUID(UUID newUUID, byte[] pkt) {
		byte[] ret = new byte[pkt.length];
		System.arraycopy(pkt, 0, ret, 0, pkt.length);
		UUIDToBytes(newUUID, ret, 1);
		return ret;
	}

	public static byte[] rewriteUUIDModel(UUID newUUID, byte[] pkt, int model) {
		byte[] ret = new byte[pkt.length];
		System.arraycopy(pkt, 0, ret, 0, pkt.length);
		UUIDToBytes(newUUID, ret, 1);
		if(ret[0] == (byte)PACKET_OTHER_SKIN_CUSTOM) {
			ret[17] = (byte)model;
		}
		return ret;
	}

}

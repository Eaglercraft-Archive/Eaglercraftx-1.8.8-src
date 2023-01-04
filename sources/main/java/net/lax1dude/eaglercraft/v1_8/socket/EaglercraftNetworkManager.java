package net.lax1dude.eaglercraft.v1_8.socket;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentTranslation;
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
public class EaglercraftNetworkManager {
	
	private final String address;
	private INetHandler nethandler = null;
	private EnumConnectionState packetState = EnumConnectionState.HANDSHAKING;
	private final PacketBuffer temporaryBuffer;
	private int debugPacketCounter = 0;
	
	public static final Logger logger = LogManager.getLogger("NetworkManager");

	public EaglercraftNetworkManager(String address) {
		this.address = address;
		this.temporaryBuffer = new PacketBuffer(Unpooled.buffer(0x1FFFF));
	}
	
	public void connect() {
		PlatformNetworking.startPlayConnection(address);
	}
	
	public EnumEaglerConnectionState getConnectStatus() {
		return PlatformNetworking.playConnectionState();
	}
	
	public void closeChannel(IChatComponent reason) {
		PlatformNetworking.playDisconnect();
		if(nethandler != null) {
			nethandler.onDisconnect(reason);
		}
		clientDisconnected = true;
	}
	
	public void setConnectionState(EnumConnectionState state) {
		packetState = state;
	}
	
	public void processReceivedPackets() throws IOException {
		if(nethandler == null) return;
		byte[] next;

		while((next = PlatformNetworking.readPlayPacket()) != null) {
			++debugPacketCounter;
			try {
				ByteBuf nettyBuffer = Unpooled.buffer(next, next.length);
				nettyBuffer.writerIndex(next.length);
				PacketBuffer input = new PacketBuffer(nettyBuffer);
				int pktId = input.readVarIntFromBuffer();
				
				Packet pkt;
				try {
					pkt = packetState.getPacket(EnumPacketDirection.CLIENTBOUND, pktId);
				}catch(IllegalAccessException | InstantiationException ex) {
					throw new IOException("Recieved a packet with type " + pktId + " which is invalid!");
				}
				
				try {
					pkt.readPacketData(input);
				}catch(Throwable t) {
					throw new IOException("Failed to read packet type '" + pkt.getClass().getSimpleName() + "'", t);
				}
				
				try {
					pkt.processPacket(nethandler);
				}catch(Throwable t) {
					logger.error("Failed to process {}! It'll be skipped for debug purposes.", pkt.getClass().getSimpleName());
					logger.error(t);
				}
				
			}catch(Throwable t) {
				logger.error("Failed to process websocket frame {}! It'll be skipped for debug purposes.", debugPacketCounter);
				logger.error(t);
			}
		}
	}

	public void sendPacket(Packet pkt) {
		if(!isChannelOpen()) {
			logger.error("Packet was sent on a closed connection: {}", pkt.getClass().getSimpleName());
			return;
		}
		
		int i;
		try {
			i = packetState.getPacketId(EnumPacketDirection.SERVERBOUND, pkt);
		}catch(Throwable t) {
			logger.error("Incorrect packet for state: {}", pkt.getClass().getSimpleName());
			return;
		}
		
		temporaryBuffer.clear();
		temporaryBuffer.writeVarIntToBuffer(i);
		try {
			pkt.writePacketData(temporaryBuffer);
		}catch(IOException ex) {
			logger.error("Failed to write packet {}!", pkt.getClass().getSimpleName());
			return;
		}
		
		int len = temporaryBuffer.writerIndex();
		byte[] bytes = new byte[len];
		temporaryBuffer.getBytes(0, bytes);
		
		PlatformNetworking.writePlayPacket(bytes);
	}
	
	public void setNetHandler(INetHandler nethandler) {
		this.nethandler = nethandler;
	}
	
	public boolean isLocalChannel() {
		return false;
	}
	
	public boolean isChannelOpen() {
		return getConnectStatus() == EnumEaglerConnectionState.CONNECTED;
	}

	public boolean getIsencrypted() {
		return false;
	}

	public void setCompressionTreshold(int compressionTreshold) {
		throw new CompressionNotSupportedException();
	}

	public boolean checkDisconnected() {
		if(PlatformNetworking.playConnectionState().isClosed()) {
			try {
				processReceivedPackets(); // catch kick message
			} catch (IOException e) {
			}
			doClientDisconnect(new ChatComponentTranslation("disconnect.endOfStream"));
			return true;
		}else {
			return false;
		}
	}
	
	private boolean clientDisconnected = false;
	
	private void doClientDisconnect(IChatComponent msg) {
		if(!clientDisconnected) {
			clientDisconnected = true;
			if(nethandler != null) {
				this.nethandler.onDisconnect(msg);
			}
		}
	}
	
}

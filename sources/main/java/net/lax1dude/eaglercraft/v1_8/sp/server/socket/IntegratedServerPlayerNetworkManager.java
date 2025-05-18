/*
 * Copyright (c) 2022-2025 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.sp.server.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.socket.CompressionNotSupportedException;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.message.InjectedMessageController;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.lax1dude.eaglercraft.v1_8.sp.server.internal.ServerPlatformSingleplayer;

public class IntegratedServerPlayerNetworkManager {

	private INetHandler nethandler = null;
	public final String playerChannel;
	private EnumConnectionState packetState = EnumConnectionState.HANDSHAKING;
	private static PacketBuffer temporaryBuffer;
	private static byte[] compressedPacketTmp;
	private int debugPacketCounter = 0;
	private final List<byte[]> recievedPacketBuffer = new LinkedList<>();
	private final boolean enableSendCompression;
	protected InjectedMessageController injectedController = null;

	private boolean firstPacket = true;

	private List<byte[]> fragmentedPacket = new ArrayList<>();

	public static final int fragmentSize = 0xFF00;
	public static final int compressionThreshold = 1024;
	
	public static final Logger logger = LogManager.getLogger("NetworkManager");

	public IntegratedServerPlayerNetworkManager(String playerChannel) {
		if(temporaryBuffer == null) {
			temporaryBuffer = new PacketBuffer(Unpooled.buffer(0x1FFFF));
		}
		this.playerChannel = playerChannel;
		this.enableSendCompression = !SingleplayerServerController.PLAYER_CHANNEL.equals(playerChannel);
	}

	public void setInjectedMessageController(InjectedMessageController controller) {
		injectedController = controller;
	}
	
	public void connect() {
		fragmentedPacket.clear();
		firstPacket = true;
	}
	
	public EnumEaglerConnectionState getConnectStatus() {
		return EaglerIntegratedServerWorker.getChannelExists(playerChannel) ? EnumEaglerConnectionState.CONNECTED : EnumEaglerConnectionState.CLOSED;
	}
	
	public void closeChannel(IChatComponent reason) {
		EaglerIntegratedServerWorker.closeChannel(playerChannel);
		if(nethandler != null) {
			nethandler.onDisconnect(reason);
		}
	}
	
	public void setConnectionState(EnumConnectionState state) {
		packetState = state;
	}

	public void addRecievedPacket(byte[] next) {
		recievedPacketBuffer.add(next);
	}

	public void processReceivedPackets() {
		if(nethandler == null) return;

		while(!recievedPacketBuffer.isEmpty()) {
			byte[] data = recievedPacketBuffer.remove(0);
			byte[] fullData;

			if(enableSendCompression) {
				if(firstPacket) {
					if(data.length > 2 && data[0] == (byte)0x02 && data[1] == (byte)0x3D) {
						EaglerOutputStream kickPacketBAO = new EaglerOutputStream();
						try {
							DataOutputStream kickDAO = new DataOutputStream(kickPacketBAO);
							kickDAO.write(0);
							kickDAO.write(0xFF);
							String msg = "This is an EaglercraftX 1.8 LAN world!";
							kickDAO.write(0x00);
							kickDAO.write(msg.length());
							for(int j = 0, l = msg.length(); j < l; ++j) {
								kickDAO.writeChar(msg.charAt(j));
							}
						}catch(IOException ex) {
							throw new RuntimeException(ex);
						}
						ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, kickPacketBAO.toByteArray()));
						closeChannel(new ChatComponentText("Recieved unsuppoorted connection from an Eaglercraft 1.5.2 client!"));
						firstPacket = false;
						return;
					}
					firstPacket = false;
				}
				if (data[0] == 0) {
					if(fragmentedPacket.isEmpty()) {
						fullData = new byte[data.length - 1];
						System.arraycopy(data, 1, fullData, 0, fullData.length);
					}else {
						fragmentedPacket.add(data);
						int len = 0;
						int fragCount = fragmentedPacket.size();
						for(int j = 0; j < fragCount; ++j) {
							len += fragmentedPacket.get(j).length - 1;
						}
						fullData = new byte[len];
						len = 0;
						for(int j = 0; j < fragCount; ++j) {
							byte[] f = fragmentedPacket.get(j);
							System.arraycopy(f, 1, fullData, len, f.length - 1);
							len += f.length - 1;
						}
						fragmentedPacket.clear();
					}
				} else if (data[0] == 1) {
					fragmentedPacket.add(data);
					continue;
				} else {
					logger.error("Recieved {} byte fragment of unknown type: {}", data.length, ((int)data[0] & 0xFF));
					continue;
				}
				
			}else {
				fullData = data;
			}
			
			++debugPacketCounter;
			try {
				if(injectedController != null && injectedController.handlePacket(fullData, 0)) {
					continue;
				}
				
				ByteBuf nettyBuffer = Unpooled.buffer(fullData, fullData.length);
				nettyBuffer.writerIndex(fullData.length);
				PacketBuffer input = new PacketBuffer(nettyBuffer);
				int pktId = input.readVarIntFromBuffer();
				
				Packet pkt;
				try {
					pkt = packetState.getPacket(EnumPacketDirection.SERVERBOUND, pktId);
				}catch(IllegalAccessException | InstantiationException ex) {
					throw new IOException("Recieved a packet with type " + pktId + " which is invalid!");
				}

				if(pkt == null) {
					throw new IOException("Recieved packet type " + pktId + " which is undefined in state " + packetState);
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
				logger.error("Failed to process socket frame {}! It'll be skipped for debug purposes.", debugPacketCounter);
				logger.error(t);
			}
		}
	}

	public void sendPacket(Packet pkt) {
		if(!isChannelOpen()) {
			return;
		}
		
		int i;
		try {
			i = packetState.getPacketId(EnumPacketDirection.CLIENTBOUND, pkt);
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
		
		int len = temporaryBuffer.readableBytes();
		if(enableSendCompression) {
			if(len > compressionThreshold) {
				if(compressedPacketTmp == null || compressedPacketTmp.length < len) {
					compressedPacketTmp = new byte[len];
				}
				int cmpLen;
				try {
					cmpLen = EaglerZLIB.deflateFull(temporaryBuffer.array(), 0, len, compressedPacketTmp, 0, compressedPacketTmp.length);
				}catch(IOException ex) {
					logger.error("Failed to compress packet {}!", pkt.getClass().getSimpleName());
					logger.error(ex);
					return;
				}
				byte[] compressedData = new byte[5 + cmpLen];
				compressedData[0] = (byte)2;
				compressedData[1] = (byte)((len >>> 24) & 0xFF);
				compressedData[2] = (byte)((len >>> 16) & 0xFF);
				compressedData[3] = (byte)((len >>> 8) & 0xFF);
				compressedData[4] = (byte)(len & 0xFF);
				System.arraycopy(compressedPacketTmp, 0, compressedData, 5, cmpLen);
				if(compressedData.length > fragmentSize) {
					int fragmentSizeN1 = fragmentSize - 1;
					for (int j = 1; j < compressedData.length; j += fragmentSizeN1) {
						byte[] fragData = new byte[((j + fragmentSizeN1 > (compressedData.length - 1)) ? ((compressedData.length - 1) % fragmentSizeN1) : fragmentSizeN1) + 1];
						System.arraycopy(compressedData, j, fragData, 1, fragData.length - 1);
						fragData[0] = (j + fragmentSizeN1 < compressedData.length) ? (byte) 1 : (byte) 2;
						ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, fragData));
					}
				}else {
					ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, compressedData));
				}
			}else {
				int fragmentSizeN1 = fragmentSize - 1;
				if(len > fragmentSizeN1) {
					do {
						int readLen = len > fragmentSizeN1 ? fragmentSizeN1 : len;
						byte[] frag = new byte[readLen + 1];
						temporaryBuffer.readBytes(frag, 1, readLen);
						frag[0] = temporaryBuffer.readableBytes() == 0 ? (byte)0 : (byte)1;
						ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, frag));
					}while((len = temporaryBuffer.readableBytes()) > 0);
				}else {
					byte[] bytes = new byte[len + 1];
					bytes[0] = 0;
					temporaryBuffer.readBytes(bytes, 1, len);
					ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, bytes));
				}
			}
		}else {
			byte[] bytes = new byte[len];
			temporaryBuffer.readBytes(bytes, 0, len);
			ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, bytes));
		}
	}
	
	public void injectRawFrame(byte[] data) {
		if(!isChannelOpen()) {
			return;
		}
		if(enableSendCompression) {
			int len = data.length;
			if(len > compressionThreshold) {
				if(compressedPacketTmp == null || compressedPacketTmp.length < len) {
					compressedPacketTmp = new byte[len];
				}
				int cmpLen;
				try {
					cmpLen = EaglerZLIB.deflateFull(data, 0, len, compressedPacketTmp, 0, compressedPacketTmp.length);
				}catch(IOException ex) {
					logger.error("Failed to compress injected frame!");
					logger.error(ex);
					return;
				}
				byte[] compressedData = new byte[5 + cmpLen];
				compressedData[0] = (byte)2;
				compressedData[1] = (byte)((len >>> 24) & 0xFF);
				compressedData[2] = (byte)((len >>> 16) & 0xFF);
				compressedData[3] = (byte)((len >>> 8) & 0xFF);
				compressedData[4] = (byte)(len & 0xFF);
				System.arraycopy(compressedPacketTmp, 0, compressedData, 5, cmpLen);
				if(compressedData.length > fragmentSize) {
					int fragmentSizeN1 = fragmentSize - 1;
					for (int j = 1; j < compressedData.length; j += fragmentSizeN1) {
						byte[] fragData = new byte[((j + fragmentSizeN1 > (compressedData.length - 1)) ? ((compressedData.length - 1) % fragmentSizeN1) : fragmentSizeN1) + 1];
						System.arraycopy(compressedData, j, fragData, 1, fragData.length - 1);
						fragData[0] = (j + fragmentSizeN1 < compressedData.length) ? (byte) 1 : (byte) 2;
						ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, fragData));
					}
				}else {
					ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, compressedData));
				}
			}else {
				int fragmentSizeN1 = fragmentSize - 1;
				if(len > fragmentSizeN1) {
					int idx = 0;
					do {
						int readLen = len > fragmentSizeN1 ? fragmentSizeN1 : len;
						byte[] frag = new byte[readLen + 1];
						System.arraycopy(data, idx, frag, 1, readLen);
						idx += readLen;
						len -= readLen;
						frag[0] = len == 0 ? (byte)0 : (byte)1;
						ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, frag));
					}while(len > 0);
				}else {
					byte[] bytes = new byte[len + 1];
					System.arraycopy(data, 0, bytes, 1, len);
					ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, bytes));
				}
			}
		}else {
			ServerPlatformSingleplayer.sendPacket(new IPCPacketData(playerChannel, data));
		}
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

	public void tick() {
		processReceivedPackets();
		if(nethandler instanceof ITickable) {
			((ITickable)nethandler).update();
		}
	}
}
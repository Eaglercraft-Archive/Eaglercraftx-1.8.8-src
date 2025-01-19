package net.lax1dude.eaglercraft.v1_8.socket.protocol.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePacketOutputBuffer;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.sp.server.socket.protocol.ServerV3MessageHandler;
import net.lax1dude.eaglercraft.v1_8.sp.server.socket.protocol.ServerV4MessageHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class GameProtocolMessageController {

	private static final Logger logger = LogManager.getLogger("GameProtocolMessageController");

	public final GamePluginMessageProtocol protocol;
	public final int sendDirection;
	public final int receiveDirection;
	private final PacketBufferInputWrapper inputStream = new PacketBufferInputWrapper(null);
	private final PacketBufferOutputWrapper outputStream = new PacketBufferOutputWrapper(null);
	private final GameMessageHandler handler;
	private final IPluginMessageSendFunction sendFunction;
	private final List<PacketBuffer> sendQueueV4;
	private final boolean noDelay;

	public GameProtocolMessageController(GamePluginMessageProtocol protocol, int sendDirection, GameMessageHandler handler,
			IPluginMessageSendFunction sendCallback) {
		this.protocol = protocol;
		this.sendDirection = sendDirection;
		this.receiveDirection = GamePluginMessageConstants.oppositeDirection(sendDirection);
		this.handler = handler;
		this.sendFunction = sendCallback;
		this.noDelay = protocol.ver < 4 || EagRuntime.getConfiguration().isEaglerNoDelay();
		this.sendQueueV4 = !noDelay ? new LinkedList<>() : null;
	}

	public boolean handlePacket(String channel, PacketBuffer data) throws IOException {
		GameMessagePacket pkt;
		if(protocol.ver >= 4 && data.readableBytes() > 0 && data.getByte(data.readerIndex()) == (byte) 0xFF
				&& channel.equals(GamePluginMessageConstants.V4_CHANNEL)) {
			data.readByte();
			inputStream.buffer = data;
			int count = inputStream.readVarInt();
			for(int i = 0, j, k; i < count; ++i) {
				j = data.readVarIntFromBuffer();
				k = data.readerIndex() + j;
				if(j > data.readableBytes()) {
					throw new IOException("Packet fragment is too long: " + j + " > " + data.readableBytes());
				}
				pkt = protocol.readPacket(channel, receiveDirection, inputStream);
				if(pkt != null) {
					try {
						pkt.handlePacket(handler);
					}catch(Throwable t) {
						logger.error("Failed to handle packet {} in direction {} using handler {}!", pkt.getClass().getSimpleName(),
								GamePluginMessageConstants.getDirectionString(receiveDirection), handler);
						logger.error(t);
					}
				}else {
					logger.warn("Could not read packet fragment {} of {}, unknown packet", count, i);
				}
				if(data.readerIndex() != k) {
					logger.warn("Packet fragment {} was the wrong length: {} != {}",
							(pkt != null ? pkt.getClass().getSimpleName() : "unknown"), j + data.readerIndex() - k, j);
					data.readerIndex(k);
				}
			}
			if(data.readableBytes() > 0) {
				logger.warn("Leftover data after reading multi-packet! ({} bytes)", data.readableBytes());
			}
			inputStream.buffer = null;
			return true;
		}
		inputStream.buffer = data;
		pkt = protocol.readPacket(channel, receiveDirection, inputStream);
		if(pkt != null && inputStream.available() > 0) {
			logger.warn("Leftover data after reading packet {}! ({} bytes)", pkt.getClass().getSimpleName(), inputStream.available());
		}
		inputStream.buffer = null;
		if(pkt != null) {
			try {
				pkt.handlePacket(handler);
			}catch(Throwable t) {
				logger.error("Failed to handle packet {} in direction {} using handler {}!", pkt.getClass().getSimpleName(),
						GamePluginMessageConstants.getDirectionString(receiveDirection), handler);
				logger.error(t);
			}
			return true;
		}else {
			return false;
		}
	}

	public void sendPacket(GameMessagePacket packet) throws IOException {
		int len = packet.length() + 1;
		PacketBuffer buf = new PacketBuffer(len != 0 ? Unpooled.buffer(len) : Unpooled.buffer(64));
		outputStream.buffer = buf;
		String chan = protocol.writePacket(sendDirection, outputStream, packet);
		outputStream.buffer = null;
		int j = buf.writerIndex();
		if(len != 0 && j != len && j + 1 != len) {
			logger.warn("Packet {} was expected to be {} bytes but was serialized to {} bytes!",
					packet.getClass().getSimpleName(), len, j);
		}
		if(sendQueueV4 != null && chan.equals(GamePluginMessageConstants.V4_CHANNEL)) {
			sendQueueV4.add(buf);
		}else {
			sendFunction.sendPluginMessage(chan, buf);
		}
	}

	public void flush() {
		if(sendQueueV4 != null) {
			int queueLen = sendQueueV4.size();
			PacketBuffer pkt;
			if(queueLen == 0) {
				return;
			}else if(queueLen == 1) {
				pkt = sendQueueV4.remove(0);
				sendFunction.sendPluginMessage(GamePluginMessageConstants.V4_CHANNEL, pkt);
			}else {
				int i, j, sendCount, totalLen;
				PacketBuffer sendBuffer;
				while(sendQueueV4.size() > 0) {
					sendCount = 0;
					totalLen = 0;
					Iterator<PacketBuffer> itr = sendQueueV4.iterator();
					do {
						i = itr.next().readableBytes();
						totalLen += GamePacketOutputBuffer.getVarIntSize(i) + i;
						++sendCount;
					}while(totalLen < 32760 && itr.hasNext());
					if(totalLen >= 32760) {
						--sendCount;
					}
					if(sendCount <= 1) {
						pkt = sendQueueV4.remove(0);
						sendFunction.sendPluginMessage(GamePluginMessageConstants.V4_CHANNEL, pkt);
						continue;
					}
					sendBuffer = new PacketBuffer(Unpooled.buffer(1 + totalLen + GamePacketOutputBuffer.getVarIntSize(sendCount))); 
					sendBuffer.writeByte(0xFF);
					sendBuffer.writeVarIntToBuffer(sendCount);
					for(j = 0; j < sendCount; ++j) {
						pkt = sendQueueV4.remove(0);
						sendBuffer.writeVarIntToBuffer(pkt.readableBytes());
						sendBuffer.writeBytes(pkt);
					}
					sendFunction.sendPluginMessage(GamePluginMessageConstants.V4_CHANNEL, sendBuffer);
				}
			}
		}
	}

	public static GameMessageHandler createClientHandler(int protocolVersion, NetHandlerPlayClient netHandler) {
		switch(protocolVersion) {
		case 2:
		case 3:
			return new ClientV3MessageHandler(netHandler);
		case 4:
			return new ClientV4MessageHandler(netHandler);
		default:
			throw new IllegalArgumentException("Unknown protocol verison: " + protocolVersion);
		}
	}

	public static GameMessageHandler createServerHandler(int protocolVersion, NetHandlerPlayServer netHandler) {
		switch(protocolVersion) {
		case 2:
		case 3:
			return new ServerV3MessageHandler(netHandler);
		case 4:
			return new ServerV4MessageHandler(netHandler);
		default:
			throw new IllegalArgumentException("Unknown protocol verison: " + protocolVersion);
		}
	}
}

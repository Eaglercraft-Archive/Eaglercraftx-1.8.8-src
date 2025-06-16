/*
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.socket.protocol.message;

import java.io.IOException;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePacketOutputBuffer;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.minecraft.network.PacketBuffer;

public class LegacyMessageController extends MessageController {

	private static final Logger logger = LogManager.getLogger("LegacyMessageController");

	public interface IPluginMessageSendFunction {
		void sendPluginMessage(String channel, PacketBuffer contents);
	}

	private final PacketBufferInputWrapper inputStream = new PacketBufferInputWrapper(null);
	private final PacketBufferOutputWrapper outputStream = new PacketBufferOutputWrapper(null);
	private final IPluginMessageSendFunction send;

	public LegacyMessageController(GamePluginMessageProtocol protocol, GameMessageHandler handler, int direction,
			IPluginMessageSendFunction send) {
		super(protocol, handler, direction);
		this.send = send;
	}

	public boolean handlePacket(String channel, PacketBuffer data) throws IOException {
		GameMessagePacket pkt;
		if(protocol.ver >= 4 && data.readableBytes() > 0 && data.getByte(data.readerIndex()) == (byte) 0xFF
				&& channel.equals(GamePluginMessageConstants.V4_CHANNEL)) {
			data.readByte();
			inputStream.buffer = data;
			int count = inputStream.readVarInt();
			for(int i = 0, j, k, l; i < count; ++i) {
				j = data.readVarIntFromBuffer();
				k = data.readerIndex() + j;
				l = data.writerIndex();
				if(j < 0 || k > l) {
					throw new IOException("Packet fragment is too long: " + j + " > " + data.readableBytes());
				}
				data.writerIndex(k);
				pkt = protocol.readPacket(channel, receiveDirection, inputStream);
				if(pkt != null) {
					handlePacket(pkt);
				}else {
					logger.warn("Could not read packet fragment {} of {}, unknown packet", count, i);
				}
				if(data.readerIndex() != k) {
					logger.warn("Packet fragment {} was the wrong length: {} != {}",
							(pkt != null ? pkt.getClass().getSimpleName() : "unknown"), j + data.readerIndex() - k, j);
					data.readerIndex(k);
				}
				data.writerIndex(l);
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
			handlePacket(pkt);
			return true;
		}else {
			return false;
		}
	}

	@Override
	protected void writePacket(GameMessagePacket packet) throws IOException {
		int len = packet.length() + 1;
		PacketBuffer buf = new PacketBuffer(len != 0 ? Unpooled.buffer(len) : Unpooled.buffer(64));
		outputStream.buffer = buf;
		String chan = protocol.writePacket(sendDirection, outputStream, packet);
		outputStream.buffer = null;
		int j = buf.writerIndex();
		if(len != 0 && j != len && (protocol.ver > 3 || j + 1 != len)) {
			logger.warn("Packet {} was expected to be {} bytes but was serialized to {} bytes!",
					packet.getClass().getSimpleName(), len, j);
		}
		send.sendPluginMessage(chan, buf);
	}

	@Override
	protected void writeMultiPacket(List<GameMessagePacket> packets) throws IOException {
		int total = packets.size();
		PacketBuffer[] toSend = new PacketBuffer[total];
		for(int i = 0; i < total; ++i) {
			GameMessagePacket packet = packets.get(i);
			int len = packet.length() + 1;
			PacketBuffer buf = new PacketBuffer(len != 0 ? Unpooled.buffer(len) : Unpooled.buffer(64));
			outputStream.buffer = buf;
			protocol.writePacket(sendDirection, outputStream, packet);
			outputStream.buffer = null;
			int j = buf.writerIndex();
			if(len != 0 && j != len && (protocol.ver > 3 || j + 1 != len)) {
				logger.warn("Packet {} was expected to be {} bytes but was serialized to {} bytes!",
						packet.getClass().getSimpleName(), len, j);
			}
			toSend[i] = buf;
		}
		int start = 0;
		int i, j, sendCount, totalLen, lastLen;
		while(total > start) {
			sendCount = 0;
			totalLen = 0;
			do {
				i = toSend[start + sendCount].readableBytes();
				lastLen = GamePacketOutputBuffer.getVarIntSize(i) + i;
				totalLen += lastLen;
				++sendCount;
			}while(totalLen < 32760 && sendCount < total - start && sendCount < maxMultiPacket);
			if(totalLen >= 32760) {
				--sendCount;
				totalLen -= lastLen;
			}
			if(sendCount <= 1) {
				send.sendPluginMessage(GamePluginMessageConstants.V4_CHANNEL, toSend[start++]);
				continue;
			}
			PacketBuffer sendBuffer = new PacketBuffer(
					Unpooled.buffer(1 + totalLen + GamePacketOutputBuffer.getVarIntSize(sendCount)));
			sendBuffer.writerIndex(0);
			sendBuffer.writeByte(0xFF);
			sendBuffer.writeVarIntToBuffer(sendCount);
			for(j = 0; j < sendCount; ++j) {
				PacketBuffer dat = toSend[start++];
				sendBuffer.writeVarIntToBuffer(dat.readableBytes());
				sendBuffer.writeBytes(dat);
			}
			send.sendPluginMessage(GamePluginMessageConstants.V4_CHANNEL, sendBuffer);
		}
	}

}

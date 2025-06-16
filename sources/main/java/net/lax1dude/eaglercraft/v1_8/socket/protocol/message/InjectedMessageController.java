/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePacketOutputBuffer;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayInputStream;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayOutputStream;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SimpleInputBufferImpl;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SimpleOutputBufferImpl;

public class InjectedMessageController extends MessageController {

	private static final Logger logger = LogManager.getLogger("InjectedMessageController");

	private final ReusableByteArrayInputStream byteInputStreamSingleton = new ReusableByteArrayInputStream();
	private final ReusableByteArrayOutputStream byteOutputStreamSingleton = new ReusableByteArrayOutputStream();
	private final SimpleInputBufferImpl inputStreamSingleton = new SimpleInputBufferImpl(byteInputStreamSingleton);
	private final SimpleOutputBufferImpl outputStreamSingleton = new SimpleOutputBufferImpl(byteOutputStreamSingleton);

	public interface IBinarySendFunction {
		void sendBinaryFrame(byte[] contents);
	}

	private final IBinarySendFunction send;

	public InjectedMessageController(GamePluginMessageProtocol protocol, GameMessageHandler handler, int direction,
			IBinarySendFunction send) {
		super(protocol, handler, direction);
		this.send = send;
	}

	public boolean handlePacket(byte[] data, int offset) throws IOException {
		if(data.length - offset > 1 && data[offset] == (byte) 0xEE) {
			GameMessagePacket pkt;
			byteInputStreamSingleton.feedBuffer(data, offset);
			inputStreamSingleton.readByte();
			if(data[offset + 1] == (byte) 0xFF) {
				if(inputStreamSingleton.available() > 32768) {
					throw new IOException("Impossible large multi-packet received: " + inputStreamSingleton.available());
				}
				inputStreamSingleton.readByte();
				int count = inputStreamSingleton.readVarInt();
				for(int i = 0, j, k; i < count; ++i) {
					j = inputStreamSingleton.readVarInt();
					inputStreamSingleton.setToByteArrayReturns(j - 1);
					k = byteInputStreamSingleton.getReaderIndex() + j;
					if(j < 0 || j > inputStreamSingleton.available()) {
						throw new IOException("Packet fragment is too long: " + j + " > " + inputStreamSingleton.available());
					}
					pkt = protocol.readPacketV5(receiveDirection, inputStreamSingleton);
					if(byteInputStreamSingleton.getReaderIndex() != k) {
						throw new IOException("Packet fragment was the wrong length: " + (j + byteInputStreamSingleton.getReaderIndex() - k) + " != " + j);
					}
					handlePacket(pkt);
				}
				if(inputStreamSingleton.available() > 0) {
					throw new IOException("Leftover data after reading multi-packet! (" + inputStreamSingleton.available() + " bytes)");
				}
				byteOutputStreamSingleton.feedBuffer(null);
				return true;
			}
			inputStreamSingleton.setToByteArrayReturns(data.length - offset - 2);
			pkt = protocol.readPacketV5(receiveDirection, inputStreamSingleton);
			if(byteInputStreamSingleton.available() != 0) {
				throw new IOException("Packet was the wrong length: " + pkt.getClass().getSimpleName());
			}
			byteOutputStreamSingleton.feedBuffer(null);
			handlePacket(pkt);
			return true;
		}
		return false;
	}

	@Override
	protected void writePacket(GameMessagePacket packet) throws IOException {
		int len = packet.length() + 2;
		byteOutputStreamSingleton.feedBuffer(len == 1 ? new byte[64] : new byte[len]);
		byteOutputStreamSingleton.write(0xEE);
		protocol.writePacketV5(sendDirection, outputStreamSingleton, packet);
		byte[] data = byteOutputStreamSingleton.returnBuffer();
		byteOutputStreamSingleton.feedBuffer(null);
		if(len != 1 && data.length != len) {
			logger.warn("Packet " + packet.getClass().getSimpleName() + " was the wrong length after serialization, "
					+ data.length + " != " + len);
		}
		send.sendBinaryFrame(data);
	}

	@Override
	protected void writeMultiPacket(List<GameMessagePacket> packets) throws IOException {
		int total = packets.size();
		byte[][] buffer = new byte[total][];
		byte[] dat;
		for(int i = 0; i < total; ++i) {
			GameMessagePacket packet = packets.get(i);
			int len = packet.length() + 2;
			byteOutputStreamSingleton.feedBuffer(len == 1 ? new byte[64] : new byte[len]);
			byteOutputStreamSingleton.write(0xEE);
			protocol.writePacketV5(sendDirection, outputStreamSingleton, packet);
			dat = byteOutputStreamSingleton.returnBuffer();
			byteOutputStreamSingleton.feedBuffer(null);
			if(len != 1 && dat.length != len) {
				logger.warn("Packet " + packet.getClass().getSimpleName()
						+ " was the wrong length after serialization, " + dat.length + " != " + len);
			}
			buffer[i] = dat;
		}
		int start = 0;
		int i, j, sendCount, totalLen, lastLen;
		while(total > start) {
			sendCount = 0;
			totalLen = 0;
			do {
				i = buffer[start + sendCount].length - 1;
				lastLen = GamePacketOutputBuffer.getVarIntSize(i) + i;
				totalLen += lastLen;
				++sendCount;
			}while(totalLen < 32760 && sendCount < total - start && sendCount < maxMultiPacket);
			if(totalLen >= 32760) {
				--sendCount;
				totalLen -= lastLen;
			}
			if(sendCount <= 1) {
				send.sendBinaryFrame(buffer[start++]);
				continue;
			}
			byteOutputStreamSingleton.feedBuffer(new byte[2 + totalLen + GamePacketOutputBuffer.getVarIntSize(sendCount)]);
			outputStreamSingleton.writeShort(0xEEFF);
			outputStreamSingleton.writeVarInt(sendCount);
			for(j = 0; j < sendCount; ++j) {
				dat = buffer[start++];
				i = dat.length - 1;
				outputStreamSingleton.writeVarInt(i);
				outputStreamSingleton.write(dat, 1, i);
			}
			send.sendBinaryFrame(byteOutputStreamSingleton.returnBuffer());
			byteOutputStreamSingleton.feedBuffer(null);
		}
	}

}

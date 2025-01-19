package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.protocol;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.ScheduledFuture;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePacketOutputBuffer;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayInputStream;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayOutputStream;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SimpleInputBufferImpl;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SimpleOutputBufferImpl;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.protocol.DefinedPacket;

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

	public final GamePluginMessageProtocol protocol;
	public final GameMessageHandler handler;
	private final ReusableByteArrayInputStream byteInputStreamSingleton = new ReusableByteArrayInputStream();
	private final ReusableByteArrayOutputStream byteOutputStreamSingleton = new ReusableByteArrayOutputStream();
	private final SimpleInputBufferImpl inputStreamSingleton = new SimpleInputBufferImpl(byteInputStreamSingleton);
	private final SimpleOutputBufferImpl outputStreamSingleton = new SimpleOutputBufferImpl(byteOutputStreamSingleton);
	private final ReentrantLock inputStreamLock = new ReentrantLock();
	private final ReentrantLock outputStreamLock = new ReentrantLock();
	private final UserConnection owner;
	private int defagSendDelay;

	private final List<byte[]> sendQueueV4 = new LinkedList<>();
	private volatile int sendQueueByteLengthV4 = 0;
	private volatile Callable<Void> futureSendCallableV4 = null;
	private volatile ScheduledFuture<Void> futureSendTaskV4 = null;

	public GameProtocolMessageController(UserConnection owner, GamePluginMessageProtocol protocol,
			GameMessageHandler handler, int defagSendDelay) {
		this.owner = owner;
		this.protocol = protocol;
		this.handler = handler;
		this.defagSendDelay = defagSendDelay;
	}

	public boolean handlePacket(String channel, byte[] data) throws IOException {
		GameMessagePacket pkt;
		if(inputStreamLock.tryLock()) {
			try {
				byteInputStreamSingleton.feedBuffer(data);
				if(protocol.ver >= 4 && data.length > 0 && data[0] == (byte)0xFF && channel.equals(GamePluginMessageConstants.V4_CHANNEL)) {
					inputStreamSingleton.readByte();
					int count = inputStreamSingleton.readVarInt();
					for(int i = 0, j, k; i < count; ++i) {
						j = inputStreamSingleton.readVarInt();
						k = byteInputStreamSingleton.getReaderIndex() + j;
						if(j > inputStreamSingleton.available()) {
							throw new IOException("Packet fragment is too long: " + j + " > " + inputStreamSingleton.available());
						}
						pkt = protocol.readPacket(channel, GamePluginMessageConstants.CLIENT_TO_SERVER, inputStreamSingleton);
						if(pkt != null) {
							pkt.handlePacket(handler);
						}else {
							throw new IOException("Unknown packet type in fragment!");
						}
						if(byteInputStreamSingleton.getReaderIndex() != k) {
							throw new IOException("Packet fragment was the wrong length: " + (j + byteInputStreamSingleton.getReaderIndex() - k) + " != " + j);
						}
					}
					if(inputStreamSingleton.available() > 0) {
						throw new IOException("Leftover data after reading multi-packet! (" + inputStreamSingleton.available() + " bytes)");
					}
					return true;
				}
				inputStreamSingleton.setToByteArrayReturns(data);
				pkt = protocol.readPacket(channel, GamePluginMessageConstants.CLIENT_TO_SERVER, inputStreamSingleton);
				if(pkt != null && byteInputStreamSingleton.available() != 0) {
					throw new IOException("Packet was the wrong length: " + pkt.getClass().getSimpleName());
				}
			}finally {
				byteInputStreamSingleton.feedBuffer(null);
				inputStreamSingleton.setToByteArrayReturns(null);
				inputStreamLock.unlock();
			}
		}else {
			// slow version that makes multiple new objects
			ReusableByteArrayInputStream inputStream = new ReusableByteArrayInputStream();
			inputStream.feedBuffer(data);
			SimpleInputBufferImpl inputBuffer = new SimpleInputBufferImpl(inputStream, data);
			if(protocol.ver >= 4 && channel.equals(GamePluginMessageConstants.V4_CHANNEL)) {
				inputBuffer.setToByteArrayReturns(null);
				inputBuffer.readByte();
				int count = inputBuffer.readVarInt();
				for(int i = 0, j, k; i < count; ++i) {
					j = inputBuffer.readVarInt();
					k = inputStream.getReaderIndex() + j;
					if(j > inputBuffer.available()) {
						throw new IOException("Packet fragment is too long: " + j + " > " + inputBuffer.available());
					}
					pkt = protocol.readPacket(channel, GamePluginMessageConstants.CLIENT_TO_SERVER, inputBuffer);
					if(pkt != null) {
						pkt.handlePacket(handler);
					}else {
						throw new IOException("Unknown packet type in fragment!");
					}
					if(inputStream.getReaderIndex() != k) {
						throw new IOException("Packet fragment was the wrong length: " + (j + inputStream.getReaderIndex() - k) + " != " + j);
					}
				}
				if(inputBuffer.available() > 0) {
					throw new IOException("Leftover data after reading multi-packet! (" + inputBuffer.available() + " bytes)");
				}
				return true;
			}
			pkt = protocol.readPacket(channel, GamePluginMessageConstants.CLIENT_TO_SERVER, inputBuffer);
			if(pkt != null && inputStream.available() != 0) {
				throw new IOException("Packet was the wrong length: " + pkt.getClass().getSimpleName());
			}
		}
		if(pkt != null) {
			pkt.handlePacket(handler);
			return true;
		}else {
			return false;
		}
	}

	public void sendPacketImmediately(GameMessagePacket packet) throws IOException {
		sendPacket(packet, true);
	}

	public void sendPacket(GameMessagePacket packet) throws IOException {
		sendPacket(packet, false);
	}

	protected void sendPacket(GameMessagePacket packet, boolean immediately) throws IOException {
		int len = packet.length() + 1;
		String chan;
		byte[] data;
		if(outputStreamLock.tryLock()) {
			try {
				byteOutputStreamSingleton.feedBuffer(new byte[len == 0 ? 64 : len]);
				chan = protocol.writePacket(GamePluginMessageConstants.SERVER_TO_CLIENT, outputStreamSingleton, packet);
				data = byteOutputStreamSingleton.returnBuffer();
			}finally {
				byteOutputStreamSingleton.feedBuffer(null);
				outputStreamLock.unlock();
			}
		}else {
			// slow version that makes multiple new objects
			ReusableByteArrayOutputStream bao = new ReusableByteArrayOutputStream();
			bao.feedBuffer(new byte[len == 0 ? 64 : len]);
			SimpleOutputBufferImpl outputStream = new SimpleOutputBufferImpl(bao);
			chan = protocol.writePacket(GamePluginMessageConstants.SERVER_TO_CLIENT, outputStream, packet);
			data = bao.returnBuffer();
		}
		if(len != 0 && data.length != len && data.length + 1 != len) {
			EaglerXBungee.logger().warning("Packet " + packet.getClass().getSimpleName()
					+ " was the wrong length after serialization, " + data.length + " != " + len);
		}
		if(defagSendDelay > 0 && protocol.ver >= 4 && chan.equals(GamePluginMessageConstants.V4_CHANNEL)) {
			synchronized(sendQueueV4) {
				int varIntLen = GamePacketOutputBuffer.getVarIntSize(data.length);
				if(immediately || sendQueueByteLengthV4 + data.length + varIntLen > 32760) {
					if(futureSendTaskV4 != null && !futureSendTaskV4.isDone()) {
						futureSendTaskV4.cancel(false);
						futureSendTaskV4 = null;
						futureSendCallableV4 = null;
					}
					if(!sendQueueV4.isEmpty()) {
						flushQueue();
					}
				}
				if(immediately) {
					owner.sendData(chan, data);
				}else {
					sendQueueV4.add(data);
					if(futureSendTaskV4 == null || futureSendTaskV4.isDone()) {
						futureSendTaskV4 = ((EaglerInitialHandler) owner.getPendingConnection()).ch.getHandle()
								.eventLoop().schedule(futureSendCallableV4 = new Callable<Void>() {
							@Override
							public Void call() throws Exception {
								synchronized (sendQueueV4) {
									if (futureSendCallableV4 != this) {
										return null;
									}
									futureSendTaskV4 = null;
									futureSendCallableV4 = null;
									if(!sendQueueV4.isEmpty()) {
										flushQueue();
									}
								}
								return null;
							}
						}, defagSendDelay, TimeUnit.MILLISECONDS);
					}
				}
			}
		}else {
			owner.sendData(chan, data);
		}
	}

	private void flushQueue() {
		if(!owner.isConnected()) {
			sendQueueV4.clear();
			return;
		}
		byte[] pkt;
		if(sendQueueV4.size() == 1) {
			pkt = sendQueueV4.remove(0);
			owner.sendData(GamePluginMessageConstants.V4_CHANNEL, pkt);
		}else {
			int i, j, sendCount, totalLen;
			while(!sendQueueV4.isEmpty()) {
				sendCount = 0;
				totalLen = 0;
				Iterator<byte[]> itr = sendQueueV4.iterator();
				do {
					i = itr.next().length;
					totalLen += GamePacketOutputBuffer.getVarIntSize(i) + i;
					++sendCount;
				}while(totalLen < 32760 && itr.hasNext());
				if(totalLen >= 32760) {
					--sendCount;
				}
				if(sendCount <= 1) {
					pkt = sendQueueV4.remove(0);
					owner.sendData(GamePluginMessageConstants.V4_CHANNEL, pkt);
					continue;
				}
				byte[] toSend = new byte[1 + totalLen + GamePacketOutputBuffer.getVarIntSize(sendCount)];
				ByteBuf sendBuffer = Unpooled.wrappedBuffer(toSend);
				sendBuffer.writerIndex(0);
				sendBuffer.writeByte(0xFF);
				DefinedPacket.writeVarInt(sendCount, sendBuffer);
				for(j = 0; j < sendCount; ++j) {
					pkt = sendQueueV4.remove(0);
					DefinedPacket.writeVarInt(pkt.length, sendBuffer);
					sendBuffer.writeBytes(pkt);
				}
				owner.sendData(GamePluginMessageConstants.V4_CHANNEL, toSend);
			}
		}
	}

	public static GameMessageHandler createServerHandler(int protocolVersion, UserConnection conn, EaglerXBungee plugin) {
		switch(protocolVersion) {
		case 2:
		case 3:
			return new ServerV3MessageHandler(conn, plugin);
		case 4:
			return new ServerV4MessageHandler(conn, plugin);
		default:
			throw new IllegalArgumentException("Unknown protocol verison: " + protocolVersion);
		}
	}

	public static void sendHelper(UserConnection conn, GameMessagePacket packet) {
		PendingConnection p = conn.getPendingConnection();
		if(p instanceof EaglerInitialHandler) {
			((EaglerInitialHandler)p).sendEaglerMessage(packet);
		}else {
			throw new UnsupportedOperationException("Tried to send eagler packet on a non-eagler connection!");
		}
	}

	public UserConnection getUserConnection() {
		return owner;
	}

}

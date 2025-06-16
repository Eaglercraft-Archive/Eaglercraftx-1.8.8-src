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
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;

public abstract class MessageController {

	private static final Logger logger = LogManager.getLogger("MessageController");

	protected final GamePluginMessageProtocol protocol;
	protected final GameMessageHandler handler;
	protected final int sendDirection;
	protected final int receiveDirection;
	protected List<GameMessagePacket> sendQueue;
	protected int maxMultiPacket = 64;

	public MessageController(GamePluginMessageProtocol protocol, GameMessageHandler handler, int direction) {
		this.protocol = protocol;
		this.handler = handler;
		this.sendDirection = direction;
		this.receiveDirection = direction == GamePluginMessageConstants.CLIENT_TO_SERVER
				? GamePluginMessageConstants.SERVER_TO_CLIENT
				: GamePluginMessageConstants.CLIENT_TO_SERVER;
		this.sendQueue = protocol.ver >= 4 && !EagRuntime.getConfiguration().isEaglerNoDelay()
				? new ArrayList<>() : null;
	}

	public GamePluginMessageProtocol getProtocol() {
		return protocol;
	}

	public boolean isSendQueueEnabled() {
		return sendQueue != null;
	}

	public void setMaxMultiPacket(int max) {
		this.maxMultiPacket = max;
	}

	public void sendPacket(GameMessagePacket packet) {
		if(sendQueue != null) {
			sendQueue.add(packet);
		}else {
			try {
				writePacket(packet);
			} catch (IOException ex) {
				throw new RuntimeException("Failed to serialize packet: " + packet.getClass().getSimpleName(), ex);
			}
		}
	}

	protected abstract void writePacket(GameMessagePacket packet) throws IOException;

	public void flush() {
		if(sendQueue != null && !sendQueue.isEmpty()) {
			try {
				writeMultiPacket(sendQueue);
			} catch (IOException ex) {
				throw new RuntimeException("Failed to serialize packet multi-packet!", ex);
			}
			if(sendQueue.size() < 64) {
				sendQueue.clear();
			}else {
				sendQueue = new ArrayList<>();
			}
		}
	}

	protected abstract void writeMultiPacket(List<GameMessagePacket> packets) throws IOException;

	protected void handlePacket(GameMessagePacket packet) {
		try {
			packet.handlePacket(handler);
		}catch(Throwable t) {
			logger.error("Failed to handle packet {} in direction {} using handler {}!", packet.getClass().getSimpleName(),
					GamePluginMessageConstants.getDirectionString(receiveDirection), handler);
			logger.error(t);
		}
	}

}

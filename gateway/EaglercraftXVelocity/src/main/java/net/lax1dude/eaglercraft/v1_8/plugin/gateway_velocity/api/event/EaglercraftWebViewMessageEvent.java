/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event;

import java.nio.charset.StandardCharsets;

import com.velocitypowered.api.proxy.Player;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketWebViewMessageV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketWebViewMessageV4EAG;

public class EaglercraftWebViewMessageEvent {

	public static enum MessageType {
		STRING(SPacketWebViewMessageV4EAG.TYPE_STRING), BINARY(SPacketWebViewMessageV4EAG.TYPE_BINARY);
		
		private final int id;
		
		private MessageType(int id) {
			this.id = id;
		}
		
		private static MessageType fromId(int id) {
			switch(id) {
			case CPacketWebViewMessageV4EAG.TYPE_STRING:
				return STRING;
			default:
			case CPacketWebViewMessageV4EAG.TYPE_BINARY:
				return BINARY;
			}
		}
	}

	private final Player player;
	private final EaglerListenerConfig listener;
	private final String currentChannel;
	private final EaglerPlayerData eaglerHandler;
	private final MessageType type;
	private final byte[] data;
	private String asString;

	public EaglercraftWebViewMessageEvent(Player player, EaglerListenerConfig listener, String currentChannel, MessageType type, byte[] data) {
		this.player = player;
		this.listener = listener;
		this.currentChannel = currentChannel;
		this.eaglerHandler = EaglerXVelocityAPIHelper.getEaglerHandle(player);
		this.type = type;
		this.data = data;
	}

	public EaglercraftWebViewMessageEvent(Player player, EaglerListenerConfig listener, String currentChannel, CPacketWebViewMessageV4EAG packet) {
		this.player = player;
		this.listener = listener;
		this.currentChannel = currentChannel;
		this.eaglerHandler = EaglerXVelocityAPIHelper.getEaglerHandle(player);
		this.type = MessageType.fromId(packet.type);
		this.data = packet.data;
	}

	public Player getPlayer() {
		return player;
	}

	public EaglerListenerConfig getListener() {
		return listener;
	}

	public EaglerPlayerData getEaglerHandle() {
		return eaglerHandler;
	}

	public void sendResponse(MessageType type, byte[] data) {
		eaglerHandler.sendEaglerMessage(new SPacketWebViewMessageV4EAG(type.id, data));
	}

	public void sendResponse(String str) {
		sendResponse(MessageType.STRING, str.getBytes(StandardCharsets.UTF_8));
	}

	public void sendResponse(byte[] data) {
		sendResponse(MessageType.BINARY, data);
	}

	public MessageType getType() {
		return type;
	}

	public byte[] getAsBinary() {
		return data;
	}

	public String getAsString() {
		if(asString == null) {
			asString = new String(data, StandardCharsets.UTF_8);
		}
		return asString;
	}

	public String getChannelName() {
		return currentChannel;
	}

}
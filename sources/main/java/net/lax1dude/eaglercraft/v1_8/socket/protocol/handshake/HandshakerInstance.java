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

package net.lax1dude.eaglercraft.v1_8.socket.protocol.handshake;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.carrotsearch.hppc.ObjectByteMap;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.socket.HandshakePacketTypes;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class HandshakerInstance {

	protected final HandshakerHandler handler;
	protected String pluginBrand;
	protected String pluginVersion;
	protected String username;
	protected EaglercraftUUID uuid;
	protected int state = HandshakePacketTypes.STATE_NEW;
	protected int serverStandardCaps;
	protected byte[] serverStandardCapVers;
	protected ObjectByteMap<EaglercraftUUID> extendedCaps;

	public HandshakerInstance(HandshakerHandler handler) {
		this.handler = handler;
	}

	protected void begin(String pluginBrand, String pluginVersion, int authType, byte[] salt) {
		this.pluginBrand = pluginBrand;
		this.pluginVersion = pluginVersion;
		byte[] password = null;
		if (handler.password != null) {
			switch(authType) {
			case HandshakePacketTypes.AUTH_METHOD_NONE:
				break;
			case HandshakePacketTypes.AUTH_METHOD_EAGLER_SHA256:
				password = AuthTypes.applyEaglerSHA256(handler.password, salt);
				break;
			case HandshakePacketTypes.AUTH_METHOD_AUTHME_SHA256:
				password = AuthTypes.applyAuthMeSHA256(handler.password, salt);
				break;
			case HandshakePacketTypes.AUTH_METHOD_PLAINTEXT:
				if(!handler.allowPlaintext) {
					handleError("disconnect.loginFailed", new ChatComponentText("Server attempted insecure plaintext authentication without user consent!"));
					return;
				}
				password = handler.password.getBytes(StandardCharsets.UTF_8);
				if(password.length > 255) {
					handleError("disconnect.loginFailed", new ChatComponentText("Password is too long!"));
					return;
				}
				break;
			default:
				handleError("disconnect.loginFailed", new ChatComponentText("Unknown auth method #" + authType + " requested"));
				return;
			}
		}

		sendClientRequestLogin(handler.username, "default", password, handler.enableCookies, handler.cookieData);

		state = HandshakePacketTypes.STATE_CLIENT_LOGIN;
	}

	protected abstract int getVersion();

	protected abstract void sendClientRequestLogin(String username, String requestedServer, byte[] password,
			boolean enableCookies, byte[] cookie);

	protected void tick() {
		IWebSocketFrame frame;
		while (state != HandshakePacketTypes.STATE_FINISHED
				&& (frame = handler.websocket.getNextBinaryFrame()) != null) {
			handleInboundPacket(frame.getByteArray());
		}
		if(handler.websocket.isClosed()) {
			handleError("Connection Closed", (IChatComponent) null);
		}
	}

	protected void handleInboundPacket(byte[] data) {
		try {
			PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(data, data.length).writerIndex(data.length));
			int pktId = buffer.readUnsignedByte();
			switch(pktId) {
			case HandshakePacketTypes.PROTOCOL_SERVER_ALLOW_LOGIN:
				handleInboundServerAllowLogin(buffer);
				break;
			case HandshakePacketTypes.PROTOCOL_SERVER_DENY_LOGIN:
				handleInboundServerDenyLogin(buffer);
				break;
			case HandshakePacketTypes.PROTOCOL_SERVER_FINISH_LOGIN:
				handleServerFinishLogin();
				break;
			case HandshakePacketTypes.PROTOCOL_SERVER_REDIRECT_TO:
				handleInboundServerRedirectTo(buffer);
				break;
			case HandshakePacketTypes.PROTOCOL_SERVER_ERROR:
				handleInboundServerError(buffer);
				break;
			default:
				handleError("connect.failed", "Unknown packet type " + pktId + " received");
				break;
			}
		}catch(Exception ex) {
			handler.handleError("connect.failed", new ChatComponentText("Invalid packet received"));
			HandshakerHandler.logger.error("Invalid packet received");
			HandshakerHandler.logger.error(ex);
		}
	}

	protected void handleError(String message, String detail) {
		state = HandshakePacketTypes.STATE_FINISHED;
		handler.handleError(message, IChatComponent.Serializer.jsonToComponent(detail));
	}

	protected void handleError(String message, IChatComponent detail) {
		state = HandshakePacketTypes.STATE_FINISHED;
		handler.handleError(message, detail);
	}

	protected abstract void handleInboundServerAllowLogin(PacketBuffer buffer);

	protected abstract void handleInboundServerDenyLogin(PacketBuffer buffer);

	protected void handleServerAllowLogin(String username, EaglercraftUUID uuid, int serverStandardCaps,
			byte[] serverStandardCapVers, ObjectByteMap<EaglercraftUUID> extendedCaps) {
		if(state != HandshakePacketTypes.STATE_CLIENT_LOGIN) {
			handleError("connect.failed", "Unexpected allow login packet in state " + state);
			return;
		}

		this.username = username;
		this.uuid = uuid;
		this.serverStandardCaps = serverStandardCaps;
		this.serverStandardCapVers = serverStandardCapVers;
		this.extendedCaps = extendedCaps;

		Map<String, byte[]> profileDataToSend = new HashMap<>();

		if(getVersion() >= 4) {
			byte[] arr = new byte[16];
			ByteBuf buf = Unpooled.buffer(arr, 16);
			buf.writeLong(EaglercraftVersion.clientBrandUUID.msb);
			buf.writeLong(EaglercraftVersion.clientBrandUUID.lsb);
			profileDataToSend.put("brand_uuid_v1", arr);
		}

		byte[] packetSkin = EaglerProfile.getSkinPacket(getVersion());
		if(packetSkin.length > 0xFFFF) {
			handleError("connect.failed", new ChatComponentText("Skin packet is too long: " + packetSkin.length));
			return;
		}
		profileDataToSend.put(getVersion() >= 4 ? "skin_v2" : "skin_v1", packetSkin);

		byte[] packetCape = EaglerProfile.getCapePacket();
		if(packetCape.length > 0xFFFF) {
			handleError("connect.failed", new ChatComponentText("Cape packet is too long: " + packetCape.length));
			return;
		}
		profileDataToSend.put("cape_v1", packetCape);

		byte[] packetSignatureData = UpdateService.getClientSignatureData();
		if(packetSignatureData != null) {
			profileDataToSend.put("update_cert_v1", packetSignatureData);
		}

		sendClientProfileData(profileDataToSend);

		sendFinishLogin();

		state = HandshakePacketTypes.STATE_CLIENT_COMPLETE;
	}

	protected abstract void sendClientProfileData(Map<String, byte[]> profileDataToSend);

	protected abstract void sendFinishLogin();

	protected void handleServerFinishLogin() {
		if(state != HandshakePacketTypes.STATE_CLIENT_COMPLETE) {
			handleError("connect.failed", "Unexpected finish login packet in state " + state);
			return;
		}
		
		state = HandshakePacketTypes.STATE_FINISHED;
		
		handler.handleSuccess();
	}

	protected abstract void handleInboundServerRedirectTo(PacketBuffer buffer);

	protected void handleServerRedirectTo(String address) {
		state = HandshakePacketTypes.STATE_FINISHED;
		
		handler.handleServerRedirectTo(address);
	}

	protected abstract void handleInboundServerError(PacketBuffer buffer);

}

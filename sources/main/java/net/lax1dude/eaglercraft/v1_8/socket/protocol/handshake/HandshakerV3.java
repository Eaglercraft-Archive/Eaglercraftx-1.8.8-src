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
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.socket.HandshakePacketTypes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;

public class HandshakerV3 extends HandshakerInstance {

	public HandshakerV3(HandshakerHandler handler) {
		super(handler);
	}

	@Override
	protected int getVersion() {
		return 3;
	}

	@Override
	protected void sendClientRequestLogin(String username, String requestedServer, byte[] password,
			boolean enableCookies, byte[] cookie) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		buffer.writeByte(HandshakePacketTypes.PROTOCOL_CLIENT_REQUEST_LOGIN);
		buffer.writeByte(username.length());
		HandshakerHandler.writeASCII(buffer, username);
		buffer.writeByte(requestedServer.length());
		HandshakerHandler.writeASCII(buffer, requestedServer);
		if(password != null) {
			buffer.writeByte(password.length);
			buffer.writeBytes(password);
		}else {
			buffer.writeByte(0);
		}
		handler.websocket.send(buffer.toBytes());
	}

	@Override
	protected void handleInboundServerAllowLogin(PacketBuffer buffer) {
		byte[] username = new byte[buffer.readUnsignedByte()];
		buffer.readBytes(username);
		EaglercraftUUID uuid = new EaglercraftUUID(buffer.readLong(), buffer.readLong());
		handleServerAllowLogin(ArrayUtils.asciiString(username), uuid, ServerCapabilities.VIRTUAL_V3_SERVER_CAPS,
				ServerCapabilities.VIRTUAL_V3_SERVER_CAPS_VERS, null);
	}

	@Override
	protected void handleInboundServerDenyLogin(PacketBuffer buffer) {
		byte[] dat = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(dat);
		handleError("disconnect.loginFailed",
				IChatComponent.Serializer.jsonToComponent(new String(dat, StandardCharsets.UTF_8)));
	}

	@Override
	protected void sendClientProfileData(Map<String, byte[]> profileDataToSend) {
		for(Map.Entry<String, byte[]> etr : profileDataToSend.entrySet()) {
			PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
			buffer.writeByte(HandshakePacketTypes.PROTOCOL_CLIENT_PROFILE_DATA);
			String profileDataType = etr.getKey();
			buffer.writeByte(profileDataType.length());
			HandshakerHandler.writeASCII(buffer, profileDataType);
			byte[] data = etr.getValue();
			buffer.writeShort(data.length);
			buffer.writeBytes(data);
			handler.websocket.send(buffer.toBytes());
		}
	}

	@Override
	protected void sendFinishLogin() {
		handler.websocket.send(new byte[] { (byte) HandshakePacketTypes.PROTOCOL_CLIENT_FINISH_LOGIN });
	}

	@Override
	protected void handleInboundServerRedirectTo(PacketBuffer buffer) {
		handleError("disconnect.loginFailed", "Unexpected login redirect packet");
	}

	@Override
	protected void handleInboundServerError(PacketBuffer buffer) {
		state = HandshakePacketTypes.STATE_FINISHED;
		handler.handleServerError(buffer, true);
	}

}

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

import com.carrotsearch.hppc.ObjectByteHashMap;
import com.carrotsearch.hppc.ObjectByteMap;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.socket.HandshakePacketTypes;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.handshake.ClientCapabilities.ExtCapability;
import net.minecraft.network.PacketBuffer;

public class HandshakerV5 extends HandshakerV4 {

	public HandshakerV5(HandshakerHandler handler) {
		super(handler);
	}

	@Override
	protected int getVersion() {
		return 5;
	}

	@Override
	protected void sendClientRequestLogin(String username, String requestedServer, byte[] password,
			boolean enableCookies, byte[] cookie) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		buffer.writeByte(HandshakePacketTypes.PROTOCOL_CLIENT_REQUEST_LOGIN);
		if(handler.nicknameSelection) {
			buffer.writeByte(username.length());
			HandshakerHandler.writeASCII(buffer, username);
		}else {
			buffer.writeByte(0);
		}
		buffer.writeByte(requestedServer.length());
		HandshakerHandler.writeASCII(buffer, requestedServer);
		if(password != null) {
			buffer.writeByte(password.length);
			buffer.writeBytes(password);
		}else {
			buffer.writeByte(0);
		}
		buffer.writeBoolean(enableCookies);
		if(enableCookies && cookie != null) {
			buffer.writeByte(cookie.length);
			buffer.writeBytes(cookie);
		}else {
			buffer.writeByte(0);
		}
		ClientCapabilities caps = ClientCapabilities.createCapabilities(enableCookies);
		buffer.writeVarIntToBuffer(caps.getStandardCaps());
		int[] vers = caps.getStandardCapsVers();
		for(int i = 0; i < vers.length; ++i) {
			buffer.writeVarIntToBuffer(vers[i]);
		}
		ExtCapability[] extVers = caps.getExtendedCaps();
		buffer.writeByte(extVers.length);
		for(int i = 0; i < extVers.length; ++i) {
			ExtCapability extCap = extVers[i];
			buffer.writeLong(extCap.uuid.msb);
			buffer.writeLong(extCap.uuid.lsb);
			buffer.writeVarIntToBuffer(extCap.vers);
		}
		handler.websocket.send(buffer.toBytes());
	}

	@Override
	protected void handleInboundServerAllowLogin(PacketBuffer buffer) {
		byte[] username = new byte[buffer.readUnsignedByte()];
		buffer.readBytes(username);
		EaglercraftUUID uuid = new EaglercraftUUID(buffer.readLong(), buffer.readLong());
		int standardCaps = buffer.readVarIntFromBuffer();
		byte[] standardCapsVers = new byte[Integer.bitCount(standardCaps)];
		buffer.readBytes(standardCapsVers);
		int extCaps = buffer.readUnsignedByte();
		ObjectByteMap<EaglercraftUUID> extCapsMap = null;
		if(extCaps > 0) {
			extCapsMap = new ObjectByteHashMap<>(extCaps);
			for (int i = 0; i < extCaps; ++i) {
				extCapsMap.put(new EaglercraftUUID(buffer.readLong(), buffer.readLong()), buffer.readByte());
			}
		}
		handleServerAllowLogin(ArrayUtils.asciiString(username), uuid, standardCaps, standardCapsVers, extCapsMap);
	}

	@Override
	protected void handleInboundServerRedirectTo(PacketBuffer buffer) {
		byte[] urlLen = new byte[buffer.readShort()];
		buffer.readBytes(urlLen);
		handleServerRedirectTo(new String(urlLen, StandardCharsets.UTF_8));
	}

}

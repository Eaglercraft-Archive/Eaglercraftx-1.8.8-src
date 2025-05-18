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

package net.lax1dude.eaglercraft.v1_8.sp.server.socket.protocol;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.WrongPacketException;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.minecraft.network.NetHandlerPlayServer;

public class ServerV5MessageHandler extends ServerV4MessageHandler {

	public ServerV5MessageHandler(NetHandlerPlayServer netHandler) {
		super(netHandler);
	}

	public void handleClient(CPacketGetOtherCapeEAG packet) {
		throw new WrongPacketException();
	}

	public void handleClient(CPacketGetOtherSkinEAG packet) {
		throw new WrongPacketException();
	}

	public void handleClient(CPacketGetSkinByURLEAG packet) {
		throw new WrongPacketException();
	}

	public void handleClient(CPacketGetOtherCapeV5EAG packet) {
		server.getTextureService().handleRequestPlayerCapeV5(netHandler.playerEntity, packet.requestId,
				new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
	}

	public void handleClient(CPacketGetOtherSkinV5EAG packet) {
		server.getTextureService().handleRequestPlayerSkinV5(netHandler.playerEntity, packet.requestId,
				new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
	}

	public void handleClient(CPacketGetSkinByURLV5EAG packet) {
		server.getTextureService().handleRequestSkinByURLV5(netHandler.playerEntity, packet.requestId, packet.url);
	}

	public void handleClient(CPacketGetOtherTexturesV5EAG packet) {
		server.getTextureService().handleRequestPlayerTexturesV5(netHandler.playerEntity, packet.requestId,
				new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
	}

}

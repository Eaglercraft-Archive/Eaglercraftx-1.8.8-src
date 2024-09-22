package net.lax1dude.eaglercraft.v1_8.sp.server.socket.protocol;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
import net.lax1dude.eaglercraft.v1_8.sp.server.voice.IntegratedVoiceService;
import net.minecraft.network.NetHandlerPlayServer;

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
public class ServerV3MessageHandler implements GameMessageHandler {

	private final NetHandlerPlayServer netHandler;
	private final EaglerMinecraftServer server;

	public ServerV3MessageHandler(NetHandlerPlayServer netHandler) {
		this.netHandler = netHandler;
		this.server = (EaglerMinecraftServer)netHandler.serverController;
	}

	public void handleClient(CPacketGetOtherCapeEAG packet) {
		server.getCapeService().processGetOtherCape(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), netHandler.playerEntity);
	}

	public void handleClient(CPacketGetOtherSkinEAG packet) {
		server.getSkinService().processPacketGetOtherSkin(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), netHandler.playerEntity);
	}

	public void handleClient(CPacketGetSkinByURLEAG packet) {
		server.getSkinService().processPacketGetOtherSkin(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), packet.url, netHandler.playerEntity);
	}

	public void handleClient(CPacketInstallSkinSPEAG packet) {
		server.getSkinService().processPacketInstallNewSkin(packet.customSkin, netHandler.playerEntity);
	}

	public void handleClient(CPacketVoiceSignalConnectEAG packet) {
		IntegratedVoiceService voiceSvc = server.getVoiceService();
		if(voiceSvc != null) {
			voiceSvc.handleVoiceSignalPacketTypeConnect(netHandler.playerEntity);
		}
	}

	public void handleClient(CPacketVoiceSignalDescEAG packet) {
		IntegratedVoiceService voiceSvc = server.getVoiceService();
		if(voiceSvc != null) {
			voiceSvc.handleVoiceSignalPacketTypeDesc(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), packet.desc, netHandler.playerEntity);
		}
	}

	public void handleClient(CPacketVoiceSignalDisconnectV3EAG packet) {
		IntegratedVoiceService voiceSvc = server.getVoiceService();
		if(voiceSvc != null) {
			if(packet.isPeerType) {
				voiceSvc.handleVoiceSignalPacketTypeDisconnectPeer(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), netHandler.playerEntity);
			}else {
				voiceSvc.handleVoiceSignalPacketTypeDisconnect(netHandler.playerEntity);
			}
		}
	}

	public void handleClient(CPacketVoiceSignalICEEAG packet) {
		IntegratedVoiceService voiceSvc = server.getVoiceService();
		if(voiceSvc != null) {
			voiceSvc.handleVoiceSignalPacketTypeICE(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), packet.ice, netHandler.playerEntity);
		}
	}

	public void handleClient(CPacketVoiceSignalRequestEAG packet) {
		IntegratedVoiceService voiceSvc = server.getVoiceService();
		if(voiceSvc != null) {
			voiceSvc.handleVoiceSignalPacketTypeRequest(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), netHandler.playerEntity);
		}
	}

}

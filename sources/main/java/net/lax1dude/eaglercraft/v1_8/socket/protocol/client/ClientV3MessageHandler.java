package net.lax1dude.eaglercraft.v1_8.socket.protocol.client;

import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;
import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;

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
public class ClientV3MessageHandler implements GameMessageHandler {

	private final NetHandlerPlayClient netHandler;

	public ClientV3MessageHandler(NetHandlerPlayClient netHandler) {
		this.netHandler = netHandler;
	}

	public void handleServer(SPacketEnableFNAWSkinsEAG packet) {
		netHandler.currentFNAWSkinAllowedState = packet.enableSkins;
		netHandler.currentFNAWSkinForcedState = packet.enableSkins;
		Minecraft.getMinecraft().getRenderManager().setEnableFNAWSkins(netHandler.currentFNAWSkinForcedState
				|| (netHandler.currentFNAWSkinAllowedState && Minecraft.getMinecraft().gameSettings.enableFNAWSkins));
	}

	public void handleServer(SPacketOtherCapeCustomEAG packet) {
		netHandler.getCapeCache().cacheCapeCustom(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast),
				packet.customCape);
	}

	public void handleServer(SPacketOtherCapePresetEAG packet) {
		netHandler.getCapeCache().cacheCapePreset(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast),
				packet.presetCape);
	}

	public void handleServer(SPacketOtherSkinCustomV3EAG packet) {
		EaglercraftUUID responseUUID = new EaglercraftUUID(packet.uuidMost, packet.uuidLeast);
		SkinModel modelId;
		if(packet.modelID == (byte)0xFF) {
			modelId = this.netHandler.getSkinCache().getRequestedSkinType(responseUUID);
		}else {
			modelId = SkinModel.getModelFromId(packet.modelID & 0x7F);
			if((packet.modelID & 0x80) != 0 && modelId.sanitize) {
				modelId = SkinModel.STEVE;
			}
		}
		if(modelId.highPoly != null) {
			modelId = SkinModel.STEVE;
		}
		this.netHandler.getSkinCache().cacheSkinCustom(responseUUID, packet.customSkin, modelId);
	}

	public void handleServer(SPacketOtherSkinPresetEAG packet) {
		this.netHandler.getSkinCache().cacheSkinPreset(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast),
				packet.presetSkin);
	}

	public void handleServer(SPacketUpdateCertEAG packet) {
		if (EagRuntime.getConfiguration().allowUpdateSvc()) {
			UpdateService.addCertificateToSet(packet.updateCert);
		}
	}

	public void handleServer(SPacketVoiceSignalAllowedEAG packet) {
		if (VoiceClientController.isClientSupported()) {
			VoiceClientController.handleVoiceSignalPacketTypeAllowed(packet.allowed, packet.iceServers);
		}
	}

	public void handleServer(SPacketVoiceSignalConnectV3EAG packet) {
		if (VoiceClientController.isClientSupported()) {
			if (packet.isAnnounceType) {
				VoiceClientController.handleVoiceSignalPacketTypeConnectAnnounce(
						new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
			} else {
				VoiceClientController.handleVoiceSignalPacketTypeConnect(
						new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), packet.offer);
			}
		}
	}

	public void handleServer(SPacketVoiceSignalDescEAG packet) {
		if (VoiceClientController.isClientSupported()) {
			VoiceClientController.handleVoiceSignalPacketTypeDescription(
					new EaglercraftUUID(packet.uuidMost, packet.uuidLeast),
					new String(packet.desc, StandardCharsets.UTF_8));
		}
	}

	public void handleServer(SPacketVoiceSignalDisconnectPeerEAG packet) {
		if (VoiceClientController.isClientSupported()) {
			VoiceClientController.handleVoiceSignalPacketTypeDisconnect(
					new EaglercraftUUID(packet.uuidMost, packet.uuidLeast));
		}
	}

	public void handleServer(SPacketVoiceSignalGlobalEAG packet) {
		if (VoiceClientController.isClientSupported()) {
			VoiceClientController.handleVoiceSignalPacketTypeGlobalNew(packet.users);
		}
	}

	public void handleServer(SPacketVoiceSignalICEEAG packet) {
		if (VoiceClientController.isClientSupported()) {
			VoiceClientController.handleVoiceSignalPacketTypeICECandidate(
					new EaglercraftUUID(packet.uuidMost, packet.uuidLeast),
					new String(packet.ice, StandardCharsets.UTF_8));
		}
	}

}

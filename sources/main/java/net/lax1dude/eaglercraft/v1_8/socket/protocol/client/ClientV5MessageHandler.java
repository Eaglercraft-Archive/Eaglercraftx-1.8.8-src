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

package net.lax1dude.eaglercraft.v1_8.socket.protocol.client;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.WrongPacketException;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenPhishingWarning;
import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenRecieveServerInfo;
import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenRequestDisplay;
import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenServerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;

public class ClientV5MessageHandler extends ClientV4MessageHandler {

	public ClientV5MessageHandler(NetHandlerPlayClient netHandler) {
		super(netHandler);
	}

	public void handleServer(SPacketOtherSkinPresetEAG packet) {
		throw new WrongPacketException();
	}

	public void handleServer(SPacketOtherSkinCustomV4EAG packet) {
		throw new WrongPacketException();
	}

	public void handleServer(SPacketOtherCapePresetEAG packet) {
		throw new WrongPacketException();
	}

	public void handleServer(SPacketOtherCapeCustomEAG packet) {
		throw new WrongPacketException();
	}

	public void handleServer(SPacketOtherSkinPresetV5EAG packet) {
		netHandler.getTextureCache().handlePacket(packet);
	}

	public void handleServer(SPacketOtherSkinCustomV5EAG packet) {
		netHandler.getTextureCache().handlePacket(packet);
	}

	public void handleServer(SPacketOtherCapePresetV5EAG packet) {
		netHandler.getTextureCache().handlePacket(packet);
	}

	public void handleServer(SPacketOtherCapeCustomV5EAG packet) {
		netHandler.getTextureCache().handlePacket(packet);
	}

	public void handleServer(SPacketOtherTexturesV5EAG packet) {
		netHandler.getTextureCache().handlePacket(packet);
	}

	public void handleServer(SPacketClientStateFlagV5EAG packet) {
		StateFlags.setFlag(new EaglercraftUUID(packet.uuidMost, packet.uuidLeast), packet.state);
	}

	public void handleServer(SPacketDisplayWebViewURLV5EAG packet) {
		if (netHandler.allowedDisplayWebview && !netHandler.allowedDisplayWebviewYes) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen screen = GuiScreenServerInfo.createForDisplayRequest(mc.currentScreen, packet.flags,
				packet.embedTitle, packet.embedURL);
		if (!mc.gameSettings.hasHiddenPhishWarning && !GuiScreenPhishingWarning.hasShownMessage) {
			screen = new GuiScreenPhishingWarning(screen);
		}
		if (!netHandler.allowedDisplayWebview) {
			mc.displayGuiScreen(new GuiScreenRequestDisplay(screen, mc.currentScreen, netHandler));
		} else {
			mc.displayGuiScreen(screen);
		}
	}

	public void handleServer(SPacketDisplayWebViewBlobV5EAG packet) {
		if (netHandler.allowedDisplayWebview && !netHandler.allowedDisplayWebviewYes) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen screen = new GuiScreenRecieveServerInfo(mc.currentScreen, packet.embedHash,
				(parent, blob, permissionsOriginUUID) -> {
			return GuiScreenServerInfo.createForDisplayRequest(parent, packet.flags, packet.embedTitle, blob,
					permissionsOriginUUID);
		});
		if (!mc.gameSettings.hasHiddenPhishWarning && !GuiScreenPhishingWarning.hasShownMessage) {
			screen = new GuiScreenPhishingWarning(screen);
		}
		if (!netHandler.allowedDisplayWebview) {
			mc.displayGuiScreen(new GuiScreenRequestDisplay(screen, mc.currentScreen, netHandler));
		} else {
			mc.displayGuiScreen(screen);
		}
	}

}

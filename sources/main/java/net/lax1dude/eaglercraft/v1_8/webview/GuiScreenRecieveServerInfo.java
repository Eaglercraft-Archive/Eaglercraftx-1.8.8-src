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

package net.lax1dude.eaglercraft.v1_8.webview;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.crypto.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.internal.WebViewOptions;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketRequestServerInfoV4EAG;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;

public class GuiScreenRecieveServerInfo extends GuiScreen {

	private static final Logger logger = LogManager.getLogger("GuiScreenRecieveServerInfo");

	protected final GuiScreen parent;
	protected final byte[] expectHash;
	protected int timer;
	protected int timer2;
	protected String statusString = "recieveServerInfo.checkingCache";

	public GuiScreenRecieveServerInfo(GuiScreen parent, byte[] expectHash) {
		this.parent = parent;
		this.expectHash = expectHash;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 6 + 106, I18n.format("gui.cancel")));
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, I18n.format("recieveServerInfo.title"), this.width / 2, 70, 11184810);
		this.drawCenteredString(fontRendererObj, I18n.format(statusString), this.width / 2, 90, 16777215);
		if(Arrays.equals(ServerInfoCache.chunkRecieveHash, expectHash) && ServerInfoCache.chunkFinalSize > 0) {
			int progress = ServerInfoCache.chunkCurrentSize * 100 / ServerInfoCache.chunkFinalSize;
			if(progress < 0) progress = 0;
			if(progress > 100) progress = 100;
			if(ServerInfoCache.hasLastChunk) {
				progress = 100;
			}
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			byte b0 = 100;
			byte b1 = 2;
			int i1 = width / 2 - b0 / 2;
			int j1 = 103;
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			worldrenderer.pos((double) i1, (double) j1, 0.0D).color(128, 128, 128, 255).endVertex();
			worldrenderer.pos((double) i1, (double) (j1 + b1), 0.0D).color(128, 128, 128, 255).endVertex();
			worldrenderer.pos((double) (i1 + b0), (double) (j1 + b1), 0.0D).color(128, 128, 128, 255)
					.endVertex();
			worldrenderer.pos((double) (i1 + b0), (double) j1, 0.0D).color(128, 128, 128, 255).endVertex();
			worldrenderer.pos((double) i1, (double) j1, 0.0D).color(128, 255, 128, 255).endVertex();
			worldrenderer.pos((double) i1, (double) (j1 + b1), 0.0D).color(128, 255, 128, 255).endVertex();
			worldrenderer.pos((double) (i1 + progress), (double) (j1 + b1), 0.0D).color(128, 255, 128, 255)
					.endVertex();
			worldrenderer.pos((double) (i1 + progress), (double) j1, 0.0D).color(128, 255, 128, 255)
					.endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
		}
		super.drawScreen(par1, par2, par3);
	}

	public void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			mc.displayGuiScreen(parent);
		}
	}

	public void updateScreen() {
		if(mc.thePlayer == null) {
			mc.displayGuiScreen(parent);
			return;
		}
		++timer;
		if(timer == 1) {
			byte[] data = ServerInfoCache.loadFromCache(expectHash);
			if(data != null) {
				mc.displayGuiScreen(GuiScreenServerInfo.createForCurrentState(parent, data, WebViewOptions.getEmbedOriginUUID(expectHash)));
			}else {
				byte[] b = mc.thePlayer.sendQueue.cachedServerInfoData;
				if(b != null) {
					if(b.length == 0) {
						mc.displayGuiScreen(new GuiScreenGenericErrorMessage("serverInfoFailure.title", "serverInfoFailure.desc", parent));
					}else {
						ServerInfoCache.storeInCache(expectHash, b);
						mc.displayGuiScreen(GuiScreenServerInfo.createForCurrentState(parent, b, WebViewOptions.getEmbedOriginUUID(expectHash)));
					}
				}else {
					statusString = "recieveServerInfo.contactingServer";
					if(!mc.thePlayer.sendQueue.hasRequestedServerInfo) {
						if(!ServerInfoCache.hasLastChunk || !Arrays.equals(ServerInfoCache.chunkRecieveHash, expectHash)) {
							ServerInfoCache.clearDownload();
							mc.thePlayer.sendQueue.sendEaglerMessage(new CPacketRequestServerInfoV4EAG(expectHash));
							mc.thePlayer.sendQueue.hasRequestedServerInfo = true;
						}
					}
				}
			}
		}else if(timer > 1) {
			if(Arrays.equals(ServerInfoCache.chunkRecieveHash, expectHash)) {
				if(ServerInfoCache.hasLastChunk) {
					statusString = "recieveServerInfo.decompressing";
					++timer2;
					if(timer2 == 2) {
						byte[] finalData = new byte[ServerInfoCache.chunkCurrentSize];
						int i = 0;
						for(byte[] b : ServerInfoCache.chunkRecieveBuffer) {
							System.arraycopy(b, 0, finalData, i, b.length);
							i += b.length;
						}
						if(i != ServerInfoCache.chunkCurrentSize) {
							logger.error("An unknown error occured!");
							mc.thePlayer.sendQueue.cachedServerInfoData = new byte[0];
							mc.displayGuiScreen(new GuiScreenGenericErrorMessage("serverInfoFailure.title", "serverInfoFailure.desc", parent));
							return;
						}
						ServerInfoCache.clearDownload();
						try {
							EaglerInputStream bis = new EaglerInputStream(finalData);
							int finalSize = (new DataInputStream(bis)).readInt();
							if(finalSize < 0) {
								logger.error("The response data was corrupt, decompressed size is negative!");
								mc.thePlayer.sendQueue.cachedServerInfoData = new byte[0];
								mc.displayGuiScreen(new GuiScreenGenericErrorMessage("serverInfoFailure.title", "serverInfoFailure.desc", parent));
								return;
							}
							if(finalSize > ServerInfoCache.CACHE_MAX_SIZE * 2) {
								logger.error("Failed to decompress/verify server info response! Size is massive, {} " + finalSize + " bytes reported!");
								logger.error("Aborting decompression. Rejoin the server to try again.");
								mc.thePlayer.sendQueue.cachedServerInfoData = new byte[0];
								mc.displayGuiScreen(new GuiScreenGenericErrorMessage("serverInfoFailure.title", "serverInfoFailure.desc", parent));
								return;
							}
							byte[] decompressed = new byte[finalSize];
							try(InputStream is = EaglerZLIB.newGZIPInputStream(bis)) {
								IOUtils.readFully(is, decompressed);
							}
							SHA1Digest digest = new SHA1Digest();
							digest.update(decompressed, 0, decompressed.length);
							byte[] csum = new byte[20];
							digest.doFinal(csum, 0);
							if(Arrays.equals(csum, expectHash)) {
								ServerInfoCache.storeInCache(csum, decompressed);
								mc.thePlayer.sendQueue.cachedServerInfoData = decompressed;
								mc.displayGuiScreen(GuiScreenServerInfo.createForCurrentState(parent, decompressed, WebViewOptions.getEmbedOriginUUID(expectHash)));
							}else {
								logger.error("The data recieved from the server did not have the correct SHA1 checksum! Rejoin the server to try again.");
								mc.thePlayer.sendQueue.cachedServerInfoData = new byte[0];
								mc.displayGuiScreen(new GuiScreenGenericErrorMessage("serverInfoFailure.title", "serverInfoFailure.desc", parent));
							}
						}catch(IOException ex) {
							logger.error("Failed to decompress/verify server info response! Rejoin the server to try again.");
							logger.error(ex);
							mc.thePlayer.sendQueue.cachedServerInfoData = new byte[0];
							mc.displayGuiScreen(new GuiScreenGenericErrorMessage("serverInfoFailure.title", "serverInfoFailure.desc", parent));
						}
					}
				}else {
					statusString = "recieveServerInfo.recievingData";
				}
			}else {
				statusString = "recieveServerInfo.contactingServer";
			}
		}
	}

	protected boolean isPartOfPauseMenu() {
		return true;
	}
}
package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayQuery;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
class GuiSlotRelay extends GuiSlot {

	private static final ResourceLocation eaglerGuiTex = new ResourceLocation("eagler:gui/eagler_gui.png");

	final GuiScreenRelay screen;
	final RelayManager relayManager;

	public GuiSlotRelay(GuiScreenRelay screen) {
		super(GuiScreenRelay.getMinecraft(screen), screen.width, screen.height, 32, screen.height - 64, 26);
		this.screen = screen;
		this.relayManager = RelayManager.relayManager;
	}

	@Override
	protected int getSize() {
		return relayManager.count();
	}

	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		screen.selected = var1;
		screen.updateButtons();
	}

	@Override
	protected boolean isSelected(int var1) {
		return screen.selected == var1;
	}

	@Override
	protected void drawBackground() {
		screen.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int id, int xx, int yy, int width, int height, int ii) {
		if(id < relayManager.count()) {
			this.mc.getTextureManager().bindTexture(Gui.icons);
			RelayServer srv = relayManager.get(id);
			String comment = srv.comment;
			int var15 = 0;
			int var16 = 0;
			String str = null;
			int h = 12;
			long ping = srv.getPing();
			if(ping == 0l) {
				var16 = 5;
				str = "No Connection";
			}else if(ping < 0l) {
				var15 = 1;
				var16 = (int) (Minecraft.getSystemTime() / 100L + (long) (id * 2) & 7L);
				if (var16 > 4) {
					var16 = 8 - var16;
				}
				str = "Polling...";
			}else {
				RelayQuery.VersionMismatch vm = srv.getPingCompatible();
				if(!vm.isCompatible()) {
					var16 = 5;
					switch(vm) {
						case CLIENT_OUTDATED:
							str = "Outdated Client!";
							break;
						case RELAY_OUTDATED:
							str = "Outdated Relay!";
							break;
						default:
						case UNKNOWN:
							str = "Incompatible Relay!";
							break;
					}
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					GlStateManager.pushMatrix();
					GlStateManager.translate(xx + 205, yy + 11, 0.0f);
					GlStateManager.scale(0.6f, 0.6f, 0.6f);
					screen.drawTexturedModalRect(0, 0, 0, 144, 16, 16);
					GlStateManager.popMatrix();
					h += 10;
				}else {
					String pingComment = srv.getPingComment().trim();
					if(pingComment.length() > 0) {
						comment = pingComment;
					}
					str = "" + ping + "ms";
					if (ping < 150L) {
						var16 = 0;
					} else if (ping < 300L) {
						var16 = 1;
					} else if (ping < 600L) {
						var16 = 2;
					} else if (ping < 1000L) {
						var16 = 3;
					} else {
						var16 = 4;
					}
				}
			}

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			screen.drawTexturedModalRect(xx + 205, yy, 0 + var15 * 10, 176 + var16 * 8, 10, 8);
			if(srv.isPrimary()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(xx + 4, yy + 5, 0.0f);
				GlStateManager.scale(0.8f, 0.8f, 0.8f);
				this.mc.getTextureManager().bindTexture(eaglerGuiTex);
				screen.drawTexturedModalRect(0, 0, 48, 0, 16, 16);
				GlStateManager.popMatrix();
			}

			screen.drawString(mc.fontRendererObj, comment, xx + 22, yy + 2, 0xFFFFFFFF);
			screen.drawString(mc.fontRendererObj, srv.address, xx + 22, yy + 12, 0xFF999999);

			if(str != null) {
				int mx = screen.getFrameMouseX();
				int my = screen.getFrameMouseY();
				int rx = xx + 202;
				if(mx > rx && mx < rx + 13 && my > yy - 1 && my < yy + h) {
					screen.setToolTip(str);
				}
			}
		}
	}

}
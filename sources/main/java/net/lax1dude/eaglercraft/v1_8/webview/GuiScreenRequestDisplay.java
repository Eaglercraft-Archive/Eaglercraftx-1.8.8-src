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

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiScreenRequestDisplay extends GuiScreen {

	private static final ResourceLocation beaconGuiTexture = new ResourceLocation("textures/gui/container/beacon.png");

	private GuiScreen cont;
	private GuiScreen back;
	private NetHandlerPlayClient netHandler;
	private boolean mouseOverCheck;
	private boolean hasCheckedBox;

	public GuiScreenRequestDisplay(GuiScreen cont, GuiScreen back, NetHandlerPlayClient netHandler) {
		this.cont = cont;
		this.back = back;
		this.netHandler = netHandler;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 + 2, this.height / 6 + 122, 148, 20, I18n.format("webviewPhishingWarning.continue")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 150, this.height / 6 + 122, 148, 20, I18n.format("gui.cancel")));
	}

	public void drawScreen(int mx, int my, float pt) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, EnumChatFormatting.BOLD + I18n.format("webviewDisplayWarning.title"), this.width / 2, 70, 0xFF4444);
		this.drawCenteredString(fontRendererObj, I18n.format("webviewDisplayWarning.text0"), this.width / 2, 90, 16777215);
		this.drawCenteredString(fontRendererObj, I18n.format("webviewDisplayWarning.text1"), this.width / 2, 102, 16777215);
		
		String dontShowAgain = I18n.format("webviewPhishingWarning.dontShowAgain");
		int w = fontRendererObj.getStringWidth(dontShowAgain) + 20;
		int ww = (this.width - w) / 2;
		this.drawString(fontRendererObj, dontShowAgain, ww + 20, 125, 0xCCCCCC);
		
		mouseOverCheck = ww < mx && ww + 17 > mx && 121 < my && 138 > my;
		
		if(mouseOverCheck) {
			GlStateManager.color(0.7f, 0.7f, 1.0f, 1.0f);
		}else {
			GlStateManager.color(0.6f, 0.6f, 0.6f, 1.0f);
		}
		
		mc.getTextureManager().bindTexture(beaconGuiTexture);
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75f, 0.75f, 0.75f);
		drawTexturedModalRect(ww * 4 / 3, 121 * 4 / 3, 22, 219, 22, 22);
		GlStateManager.popMatrix();
		
		if(hasCheckedBox) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1.1f, 1.1f, 1.1f, 1.0f);
			GlStateManager.translate(0.5f, 0.5f, 0.0f);
			drawTexturedModalRect(ww, 121, 90, 222, 16, 16);
			GlStateManager.popMatrix();
		}
		
		super.drawScreen(mx, my, pt);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			if(hasCheckedBox) {
				netHandler.allowedDisplayWebview = true;
				netHandler.allowedDisplayWebviewYes = true;
			}
			mc.displayGuiScreen(cont);
		}else if (par1GuiButton.id == 1) {
			if(hasCheckedBox) {
				netHandler.allowedDisplayWebview = true;
				netHandler.allowedDisplayWebviewYes = false;
			}
			mc.displayGuiScreen(back);
		}
	}

	@Override
	protected void mouseClicked(int mx, int my, int btn) {
		if(btn == 0 && mouseOverCheck) {
			hasCheckedBox = !hasCheckedBox;
			mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			return;
		}
		super.mouseClicked(mx, my, btn);
	}

}
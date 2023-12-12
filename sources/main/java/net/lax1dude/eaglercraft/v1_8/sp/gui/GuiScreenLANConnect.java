package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

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
public class GuiScreenLANConnect extends GuiScreen {

	private final GuiScreen parent;
	private GuiTextField codeTextField;
	private final GuiNetworkSettingsButton relaysButton;

	private static String lastCode = "";

	public GuiScreenLANConnect(GuiScreen parent) {
		this.parent = parent;
		this.relaysButton = new GuiNetworkSettingsButton(this);
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("directConnect.lanWorldJoin")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
		this.codeTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 27, 200, 20);
		this.codeTextField.setMaxStringLength(48);
		this.codeTextField.setFocused(true);
		this.codeTextField.setText(lastCode);
		this.buttonList.get(0).enabled = this.codeTextField.getText().trim().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		lastCode = this.codeTextField.getText().trim();
	}

	protected void keyTyped(char par1, int par2) {
		if (this.codeTextField.textboxKeyTyped(par1, par2)) {
			((GuiButton) this.buttonList.get(0)).enabled = this.codeTextField.getText().trim().length() > 0;
		} else if (par2 == 28) {
			this.actionPerformed(this.buttonList.get(0));
		}
	}

	public void updateScreen() {
		this.codeTextField.updateCursorCounter();
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.codeTextField.mouseClicked(par1, par2, par3);
		this.relaysButton.mouseClicked(par1, par2, par3);
	}

	public void drawScreen(int xx, int yy, float pt) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("selectServer.direct"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(this.fontRendererObj, I18n.format("directConnect.lanWorldCode"), this.width / 2 - 100, this.height / 4 + 12, 10526880);
		this.drawCenteredString(this.fontRendererObj, I18n.format("directConnect.networkSettingsNote"), this.width / 2, this.height / 4 + 63, 10526880);
		this.drawCenteredString(this.fontRendererObj, I18n.format("directConnect.ipGrabNote"), this.width / 2, this.height / 4 + 77, 10526880);
		this.codeTextField.drawTextBox();
		super.drawScreen(xx, yy, pt);
		this.relaysButton.drawScreen(xx, yy);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(parent);
		}else if(par1GuiButton.id == 0) {
			mc.displayGuiScreen(new GuiScreenLANConnecting(parent, this.codeTextField.getText().trim()));
		}
	}

}
package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
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
public class GuiScreenAddRelay extends GuiScreen {

	/** This GUI's parent GUI. */
	private GuiScreenRelay parentGui;
	private GuiTextField serverAddress;
	private GuiTextField serverName;

	public GuiScreenAddRelay(GuiScreenRelay par1GuiScreen) {
		this.parentGui = par1GuiScreen;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.serverName.updateCursorCounter();
		this.serverAddress.updateCursorCounter();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.parentGui.addNewName = RelayManager.relayManager.makeNewRelayName();
		this.parentGui.addNewAddr = "";
		this.parentGui.addNewPrimary = RelayManager.relayManager.count() == 0;
		int sslOff = EagRuntime.requireSSL() ? 36 : 0;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12 + sslOff, I18n.format("addRelay.add")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12 + sslOff, I18n.format("gui.cancel")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 142, I18n.format("addRelay.primary") + ": " + (this.parentGui.addNewPrimary ? I18n.format("gui.yes") : I18n.format("gui.no"))));
		this.serverName = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 100, 106, 200, 20);
		this.serverAddress = new GuiTextField(4, this.fontRendererObj, this.width / 2 - 100, 66, 200, 20);
		this.serverAddress.setMaxStringLength(128);
		this.serverAddress.setFocused(true);
		((GuiButton) this.buttonList.get(0)).enabled = this.serverAddress.getText().length() > 0 && this.serverAddress.getText().split(":").length > 0 && this.serverName.getText().length() > 0;
		this.serverName.setText(this.parentGui.addNewName);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 1) {
				this.parentGui.confirmClicked(false, 0);
			} else if (par1GuiButton.id == 0) {
				this.parentGui.addNewName = this.serverName.getText();
				this.parentGui.addNewAddr = this.serverAddress.getText();
				this.parentGui.confirmClicked(true, 0);
			} else if (par1GuiButton.id == 2) {
				this.parentGui.addNewPrimary = !this.parentGui.addNewPrimary;
				((GuiButton) this.buttonList.get(2)).displayString = I18n.format("addRelay.primary") + ": " + (this.parentGui.addNewPrimary ? I18n.format("gui.yes") : I18n.format("gui.no"));
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		this.serverName.textboxKeyTyped(par1, par2);
		this.serverAddress.textboxKeyTyped(par1, par2);

		if (par1 == 9) {
			if (this.serverName.isFocused()) {
				this.serverName.setFocused(false);
				this.serverAddress.setFocused(true);
			} else {
				this.serverName.setFocused(true);
				this.serverAddress.setFocused(false);
			}
		}

		if (par1 == 13) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}

		((GuiButton) this.buttonList.get(0)).enabled = this.serverAddress.getText().length() > 0 && this.serverAddress.getText().split(":").length > 0 && this.serverName.getText().length() > 0;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.serverAddress.mouseClicked(par1, par2, par3);
		this.serverName.mouseClicked(par1, par2, par3);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawBackground(0);
		this.drawCenteredString(this.fontRendererObj, I18n.format("addRelay.title"), this.width / 2, 17, 16777215);
		this.drawString(this.fontRendererObj, I18n.format("addRelay.address"), this.width / 2 - 100, 53, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("addRelay.name"), this.width / 2 - 100, 94, 10526880);
		if(EagRuntime.requireSSL()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn1"), this.width / 2, 169, 0xccccff);
			this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.SSLWarn2"), this.width / 2, 181, 0xccccff);
		}
		this.serverName.drawTextBox();
		this.serverAddress.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	public boolean blockPTTKey() {
		return this.serverName.isFocused() || this.serverAddress.isFocused();
	}
}
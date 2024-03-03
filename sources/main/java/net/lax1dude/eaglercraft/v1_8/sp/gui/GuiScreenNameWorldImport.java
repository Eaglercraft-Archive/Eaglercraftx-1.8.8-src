package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class GuiScreenNameWorldImport extends GuiScreen {
	private GuiScreen parentGuiScreen;
	private GuiTextField theGuiTextField;
	private GuiButton loadSpawnChunksBtn;
	private GuiButton enhancedGameRulesBtn;
	private int importFormat;
	private FileChooserResult world;
	private String name;
	private boolean timeToImport = false;
	private boolean definetlyTimeToImport = false;
	private boolean isImporting = false;
	private boolean loadSpawnChunks = false;
	private boolean enhancedGameRules = true;

	public GuiScreenNameWorldImport(GuiScreen menu, FileChooserResult world, int format) {
		this.parentGuiScreen = menu;
		this.importFormat = format;
		this.world = world;
		this.name = world.fileName;
		if(name.length() > 4 && (name.endsWith(".epk") || name.endsWith(".zip"))) {
			name = name.substring(0, name.length() - 4);
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if(!timeToImport) {
			this.theGuiTextField.updateCursorCounter();
		}
		if(definetlyTimeToImport && !isImporting) {
			isImporting = true;
			SingleplayerServerController.importWorld(GuiCreateWorld.func_146317_a(mc.getSaveLoader(), this.theGuiTextField.getText().trim()), world.fileData, importFormat, (byte) ((loadSpawnChunks ? 2 : 0) | (enhancedGameRules ? 1 : 0)));
			mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(parentGuiScreen, "singleplayer.busy.importing." + (importFormat + 1), "singleplayer.failed.importing." + (importFormat + 1), SingleplayerServerController::isReady));
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		if(!timeToImport) {
			Keyboard.enableRepeatEvents(true);
			this.buttonList.clear();
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("singleplayer.import.continue")));
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
			this.theGuiTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 3, 200, 20);
			this.theGuiTextField.setFocused(true);
			this.theGuiTextField.setText(name);
			this.buttonList.add(loadSpawnChunksBtn = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 24 + 12, I18n.format("singleplayer.import.loadSpawnChunks", loadSpawnChunks ? I18n.format("gui.yes") : I18n.format("gui.no"))));
			this.buttonList.add(enhancedGameRulesBtn = new GuiButton(3, this.width / 2 - 100, this.height / 4 + 48 + 12, I18n.format("singleplayer.import.enhancedGameRules", enhancedGameRules ? I18n.format("gui.yes") : I18n.format("gui.no"))));
		}
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
				EagRuntime.clearFileChooserResult();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			} else if (par1GuiButton.id == 0) {
				this.buttonList.clear();
				timeToImport = true;
			} else if (par1GuiButton.id == 2) {
				loadSpawnChunks = !loadSpawnChunks;
				loadSpawnChunksBtn.displayString = I18n.format("singleplayer.import.loadSpawnChunks", loadSpawnChunks ? I18n.format("gui.yes") : I18n.format("gui.no"));
			} else if (par1GuiButton.id == 3) {
				enhancedGameRules = !enhancedGameRules;
				enhancedGameRulesBtn.displayString = I18n.format("singleplayer.import.enhancedGameRules", enhancedGameRules ? I18n.format("gui.yes") : I18n.format("gui.no"));
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		this.theGuiTextField.textboxKeyTyped(par1, par2);
		((GuiButton) this.buttonList.get(0)).enabled = this.theGuiTextField.getText().trim().length() > 0;

		if (par1 == 13) {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if(!timeToImport) {
			this.theGuiTextField.mouseClicked(par1, par2, par3);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		if(!timeToImport) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.import.title"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
			this.drawString(this.fontRendererObj, I18n.format("singleplayer.import.enterName"), this.width / 2 - 100, this.height / 4 - 60 + 50, 10526880);
			this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.seedNote"), this.width / 2, this.height / 4 + 90, -6250336);
			this.theGuiTextField.drawTextBox();
		}else {
			definetlyTimeToImport = true;
			long dots = (System.currentTimeMillis() / 500l) % 4l;
			String str = I18n.format("singleplayer.import.reading", world.fileName);
			this.drawString(fontRendererObj, str + (dots > 0 ? "." : "") + (dots > 1 ? "." : "") + (dots > 2 ? "." : ""), (this.width - this.fontRendererObj.getStringWidth(str)) / 2, this.height / 3 + 10, 0xFFFFFF);
		}
		super.drawScreen(par1, par2, par3);
	}
}

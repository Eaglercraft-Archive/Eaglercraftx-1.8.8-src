/*
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

package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.resources.I18n;

public class GuiScreenConnectOption extends GuiScreen {

	private final GuiMultiplayer guiScreen;
	private String title;
	private String prompt;

	private final GuiNetworkSettingsButton relaysButton;

	public GuiScreenConnectOption(GuiMultiplayer guiScreen) {
		this.guiScreen = guiScreen;
		this.relaysButton = new GuiNetworkSettingsButton(this);
	}

	public void initGui() {
		title = I18n.format("selectServer.direct");
		prompt = I18n.format("directConnect.prompt");
		buttonList.clear();
		buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 - 60 + 90, I18n.format("directConnect.serverJoin")));
		buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 - 60 + 115, I18n.format("directConnect.lanWorld")));
		buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 - 60 + 155, I18n.format("gui.cancel")));
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			guiScreen.cancelDirectConnect();
			mc.displayGuiScreen(guiScreen);
		}else if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(new GuiScreenServerList(guiScreen, guiScreen.getSelectedServer()));
		}else if(par1GuiButton.id == 2) {
			if(LANServerController.supported()) {
				guiScreen.cancelDirectConnect();
				mc.displayGuiScreen(GuiScreenLANInfo.showLANInfoScreen(new GuiScreenLANConnect(guiScreen)));
			}else {
				mc.displayGuiScreen(new GuiScreenLANNotSupported(this));
			}
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, title, this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawCenteredString(this.fontRendererObj, prompt, this.width / 2, this.height / 4 - 60 + 55, 0x999999);
		super.drawScreen(par1, par2, par3);
		relaysButton.drawScreen(par1, par2);
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		relaysButton.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}

}
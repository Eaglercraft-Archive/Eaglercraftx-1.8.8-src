package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.demo.DemoWorldServer;

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
public class GuiScreenDemoPlayWorldSelection extends GuiScreen {

	private GuiScreen mainmenu;
	private GuiButton playWorld = null;
	private GuiButton joinWorld = null;
	
	public GuiScreenDemoPlayWorldSelection(GuiScreen mainmenu) {
		this.mainmenu = mainmenu;
	}
	
	public void initGui() {
		this.buttonList.add(playWorld = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 40, I18n.format("singleplayer.demo.create.create")));
		this.buttonList.add(joinWorld = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 65, I18n.format("singleplayer.demo.create.join")));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 130, I18n.format("gui.cancel")));
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		
		this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.demo.create.title"), this.width / 2, this.height / 4, 16777215);
		
		int toolTipColor = 0xDDDDAA;
		if(playWorld.isMouseOver()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.demo.create.create.tooltip"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}else if(joinWorld.isMouseOver()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.demo.create.join.tooltip"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}
		
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(mainmenu);
		}else if(par1GuiButton.id == 1) {
			this.mc.gameSettings.hasCreatedDemoWorld = true;
			this.mc.gameSettings.saveOptions();
			this.mc.launchIntegratedServer("Demo World", "Demo World", DemoWorldServer.demoWorldSettings);
		}else if(par1GuiButton.id == 2) {
			if(LANServerController.supported()) {
				this.mc.displayGuiScreen(GuiScreenLANInfo.showLANInfoScreen(new GuiScreenLANConnect(mainmenu)));
			}else {
				this.mc.displayGuiScreen(new GuiScreenLANNotSupported(mainmenu));
			}
		}
	}

}

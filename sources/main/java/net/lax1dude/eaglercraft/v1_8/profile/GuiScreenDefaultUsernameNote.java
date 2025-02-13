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

package net.lax1dude.eaglercraft.v1_8.profile;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiScreenDefaultUsernameNote extends GuiScreen {

	private final GuiScreen back;
	private final GuiScreen cont;

	public GuiScreenDefaultUsernameNote(GuiScreen back, GuiScreen cont) {
		this.back = back;
		this.cont = cont;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 6 + 112, I18n.format("defaultUsernameDetected.changeUsername")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 6 + 142, I18n.format("defaultUsernameDetected.continueAnyway")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 6 + 172, I18n.format("defaultUsernameDetected.doNotShow")));
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, I18n.format("defaultUsernameDetected.title"), this.width / 2, 70, 11184810);
		this.drawCenteredString(fontRendererObj, I18n.format("defaultUsernameDetected.text0", EaglerProfile.getName()), this.width / 2, 90, 16777215);
		this.drawCenteredString(fontRendererObj, I18n.format("defaultUsernameDetected.text1"), this.width / 2, 105, 16777215);
		this.drawCenteredString(fontRendererObj, I18n.format("defaultUsernameDetected.text2"), this.width / 2, 120, 16777215);
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 0) {
			this.mc.displayGuiScreen(back);
		}else if(parGuiButton.id == 1) {
			this.mc.displayGuiScreen(cont);
		}else if(parGuiButton.id == 2) {
			this.mc.gameSettings.hideDefaultUsernameWarning = true;
			this.mc.gameSettings.saveOptions();
			this.mc.displayGuiScreen(cont);
		}
	}

}
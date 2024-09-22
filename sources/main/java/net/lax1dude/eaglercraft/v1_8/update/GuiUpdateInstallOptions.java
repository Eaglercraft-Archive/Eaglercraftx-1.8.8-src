package net.lax1dude.eaglercraft.v1_8.update;

import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
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
public class GuiUpdateInstallOptions extends GuiScreen {

	protected final GuiScreen parent;
	protected final GuiScreen onDone;
	protected final UpdateDataObj updateData;
	protected boolean makeDefault;
	protected boolean enableCountdown;
	protected GuiButton makeDefaultBtn;
	protected GuiButton enableCountdownBtn;

	public GuiUpdateInstallOptions(GuiScreen parent, GuiScreen onDone, UpdateDataObj updateData) {
		this.parent = parent;
		this.onDone = onDone;
		this.updateData = updateData;
		makeDefault = updateData.clientSignature.bundleVersionInteger > EaglercraftVersion.updateBundlePackageVersionInt;
		enableCountdown = makeDefault;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(makeDefaultBtn = new GuiButton(0, this.width / 2 - 100, this.height / 6 + 46,
				I18n.format("updateInstall.setDefault") + ": " + I18n.format(makeDefault ? "gui.yes" : "gui.no")));
		this.buttonList.add(enableCountdownBtn = new GuiButton(1, this.width / 2 - 100, this.height / 6 + 76,
				I18n.format("updateInstall.setCountdown") + ": "
						+ I18n.format(enableCountdown ? "gui.yes" : "gui.no")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 6 + 110, I18n.format("updateInstall.install")));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 6 + 140, I18n.format("gui.cancel")));
		
	}

	public void actionPerformed(GuiButton btn) {
		if(btn.id == 0) {
			makeDefault = !makeDefault;
			makeDefaultBtn.displayString = I18n.format("updateInstall.setDefault") + ": " + I18n.format(makeDefault ? "gui.yes" : "gui.no");
		}else if(btn.id == 1) {
			enableCountdown = !enableCountdown;
			enableCountdownBtn.displayString = I18n.format("updateInstall.setCountdown") + ": " + I18n.format(enableCountdown ? "gui.yes" : "gui.no");
		}else if(btn.id == 2) {
			mc.loadingScreen.eaglerShow(I18n.format("updateSuccess.installing"), null);
			try {
				UpdateService.installSignedClient(updateData.clientSignature, updateData.clientBundle, makeDefault, enableCountdown);
			}catch(Throwable t) {
				mc.displayGuiScreen(new GuiScreenGenericErrorMessage("installFailed.title", t.toString(), onDone));
				return;
			}
			mc.displayGuiScreen(onDone);
		}else if(btn.id == 3) {
			mc.displayGuiScreen(parent);
		}
	}

	public void drawScreen(int mx, int my, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, I18n.format("updateInstall.title"), this.width / 2, 40, 11184810);
		this.drawCenteredString(fontRendererObj,
				updateData.clientSignature.bundleDisplayName + " " + updateData.clientSignature.bundleDisplayVersion,
				this.width / 2, 60, 0xFFFFAA);
		super.drawScreen(mx, my, partialTicks);
	}

}

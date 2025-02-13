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

package net.lax1dude.eaglercraft.v1_8.update;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiUpdateDownloadSuccess extends GuiScreen {

	protected final GuiScreen parent;
	protected final UpdateDataObj updateData;

	public GuiUpdateDownloadSuccess(GuiScreen parent, UpdateDataObj updateData) {
		this.parent = parent;
		this.updateData = updateData;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 6 + 56, I18n.format("updateSuccess.downloadOffline")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 6 + 86, I18n.format("updateSuccess.installToBootMenu")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 6 + 130, I18n.format("gui.cancel")));
	}

	public void actionPerformed(GuiButton btn) {
		if(btn.id == 0) {
			this.mc.loadingScreen.eaglerShow(I18n.format("updateSuccess.downloading"), null);
			UpdateService.quine(updateData.clientSignature, updateData.clientBundle);
			this.mc.displayGuiScreen(parent);
		}else if(btn.id == 1) {
			this.mc.displayGuiScreen(new GuiUpdateInstallOptions(this, parent, updateData));
		}else if(btn.id == 2) {
			this.mc.displayGuiScreen(parent);
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, I18n.format("updateSuccess.title"), this.width / 2, 50, 11184810);
		this.drawCenteredString(fontRendererObj,
				updateData.clientSignature.bundleDisplayName + " " + updateData.clientSignature.bundleDisplayVersion,
				this.width / 2, 70, 0xFFFFAA);
		super.drawScreen(par1, par2, par3);
	}

}
package net.lax1dude.eaglercraft.v1_8.update;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
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
public class GuiUpdateVersionList extends GuiScreen {

	final GuiScreen back;
	GuiUpdateVersionSlot slots;
	int selected;
	GuiButton downloadButton;
	int mx = 0;
	int my = 0;
	String tooltip = null;

	public GuiUpdateVersionList(GuiScreen back) {
		this.back = back;
	}

	public void initGui() {
		selected = -1;
		buttonList.clear();
		buttonList.add(new GuiButton(0, this.width / 2 + 54, this.height - 28, 100, 20, I18n.format("gui.done")));
		buttonList.add(downloadButton = new GuiButton(1, this.width / 2 - 50, this.height - 28, 100, 20, I18n.format("updateList.download")));
		buttonList.add(new GuiButton(2, this.width / 2 - 154, this.height - 28, 100, 20, I18n.format("updateList.refresh")));
		slots = new GuiUpdateVersionSlot(this);
		updateButtons();
	}

	void updateButtons() {
		downloadButton.enabled = selected != -1;
	}

	static Minecraft getMinecraft(GuiUpdateVersionList screen) {
		return screen.mc;
	}

	public void actionPerformed(GuiButton btn) {
		switch(btn.id) {
		case 1:
			if(selected != -1) {
				UpdateService.startClientUpdateFrom(slots.certList.get(selected));
			}
		case 0:
		default:
			mc.displayGuiScreen(back);
			break;
		case 2:
			this.initGui();
			break;
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		mx = par1;
		my = par2;
		slots.drawScreen(par1, par2, par3);
		this.drawCenteredString(fontRendererObj, I18n.format("updateList.title"), this.width / 2, 16, 16777215);
		this.drawCenteredString(fontRendererObj, I18n.format("updateList.note.0"), this.width / 2, this.height - 55, 0x888888);
		this.drawCenteredString(fontRendererObj, I18n.format("updateList.note.1"), this.width / 2, this.height - 45, 0x888888);
		super.drawScreen(par1, par2, par3);
		if(tooltip != null) {
			drawHoveringText(mc.fontRendererObj.listFormattedStringToWidth(tooltip, 180), par1, par2);
			tooltip = null;
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		slots.handleMouseInput();
	}
}

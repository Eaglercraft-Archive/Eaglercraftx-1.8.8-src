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

package net.lax1dude.eaglercraft.v1_8.minecraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiScreenVideoSettingsWarning extends GuiScreen {

	private final GuiScreen cont;
	private final int mask;
	private final List<String> messages = new ArrayList<>();
	private int top = 0;

	public static final int WARNING_VSYNC = 1;
	public static final int WARNING_RENDER_DISTANCE = 2;
	public static final int WARNING_FRAME_LIMIT = 4;

	public GuiScreenVideoSettingsWarning(GuiScreen cont, int mask) {
		this.cont = cont;
		this.mask = mask;
	}

	public void initGui() {
		messages.clear();
		messages.add(EnumChatFormatting.RED + I18n.format("options.badVideoSettingsDetected.title"));
		messages.add(null);
		messages.add(EnumChatFormatting.GRAY + I18n.format("options.badVideoSettingsDetected.0"));
		messages.add(EnumChatFormatting.GRAY + I18n.format("options.badVideoSettingsDetected.1"));
		if((mask & WARNING_VSYNC) != 0) {
			messages.add(null);
			messages.add(I18n.format("options.badVideoSettingsDetected.vsync.0"));
		}
		if((mask & WARNING_RENDER_DISTANCE) != 0) {
			messages.add(null);
			messages.add(I18n.format("options.badVideoSettingsDetected.renderDistance.0", mc.gameSettings.renderDistanceChunks));
			messages.add(I18n.format("options.badVideoSettingsDetected.renderDistance.1"));
			messages.add(I18n.format("options.badVideoSettingsDetected.renderDistance.2"));
		}
		if((mask & WARNING_FRAME_LIMIT) != 0) {
			messages.add(null);
			messages.add(I18n.format("options.badVideoSettingsDetected.frameLimit.0", mc.gameSettings.limitFramerate));
		}
		int j = 0;
		for(int i = 0, l = messages.size(); i < l; ++i) {
			if(messages.get(i) != null) {
				j += 9;
			}else {
				j += 5;
			}
		}
		top = this.height / 6 + j / -12;
		j += top;
		buttonList.clear();
		buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 16, I18n.format("options.badVideoSettingsDetected.fixSettings")));
		buttonList.add(new GuiButton(1, this.width / 2 - 100, j + 40, I18n.format("options.badVideoSettingsDetected.continueAnyway")));
		buttonList.add(new GuiButton(2, this.width / 2 - 100, j + 64, I18n.format("options.badVideoSettingsDetected.doNotShowAgain")));
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		int j = 0;
		for(int i = 0, l = messages.size(); i < l; ++i) {
			String str = messages.get(i);
			if(str != null) {
				this.drawCenteredString(fontRendererObj, str, this.width / 2, top + j, 16777215);
				j += 9;
			}else {
				j += 5;
			}
		}
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			mc.gameSettings.fixBadVideoSettings();
			mc.gameSettings.saveOptions();
			if((mask & WARNING_RENDER_DISTANCE) != 0) {
				mc.renderGlobal.loadRenderers();
			}
			mc.displayGuiScreen(cont);
		}else if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(cont);
		}else if(par1GuiButton.id == 2) {
			mc.gameSettings.hideVideoSettingsWarning = true;
			mc.gameSettings.saveOptions();
			mc.displayGuiScreen(cont);
		}
	}

}
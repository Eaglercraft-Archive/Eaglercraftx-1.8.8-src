package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui;

import java.io.IOException;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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
public class GuiShaderConfig extends GuiScreen {

	private static final Logger logger = LogManager.getLogger();

	boolean shaderStartState = false;

	private final GuiScreen parent;
	private GuiShaderConfigList listView;

	private String title;
	private GuiButton enableDisableButton;

	public GuiShaderConfig(GuiScreen parent) {
		this.parent = parent;
		this.shaderStartState = Minecraft.getMinecraft().gameSettings.shaders;
	}

	public void initGui() {
		this.title = I18n.format("shaders.gui.title");
		this.buttonList.clear();
		this.buttonList.add(enableDisableButton = new GuiButton(0, width / 2 - 155, height - 30, 150, 20, I18n.format("shaders.gui.enable")
				+ ": " + (mc.gameSettings.shaders ? I18n.format("gui.yes") : I18n.format("gui.no"))));
		this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 30, 150, 20, I18n.format("gui.done")));
		if(listView == null) {
			this.listView = new GuiShaderConfigList(this, mc);
		}else {
			this.listView.resize();
		}
	}

	protected void actionPerformed(GuiButton btn) {
		if(btn.id == 0) {
			mc.gameSettings.shaders = !mc.gameSettings.shaders;
			listView.setAllDisabled(!mc.gameSettings.shaders);
			enableDisableButton.displayString = I18n.format("shaders.gui.enable") + ": "
					+ (mc.gameSettings.shaders ? I18n.format("gui.yes") : I18n.format("gui.no"));
		}else if(btn.id == 1) {
			mc.displayGuiScreen(parent);
		}
	}

	public void onGuiClosed() {
		if(shaderStartState != mc.gameSettings.shaders || listView.isDirty()) {
			mc.gameSettings.saveOptions();
			if(shaderStartState != mc.gameSettings.shaders) {
				mc.loadingScreen.eaglerShowRefreshResources();
				mc.refreshResources();
			}else {
				logger.info("Reloading shaders...");
				try {
					mc.gameSettings.deferredShaderConf.reloadShaderPackInfo(mc.getResourceManager());
				}catch(IOException ex) {
					logger.info("Could not reload shader pack info!");
					logger.info(ex);
					logger.info("Shaders have been disabled");
					mc.gameSettings.shaders = false;
					mc.refreshResources();
					return;
				}

				if(mc.gameSettings.shaders) {
					ShaderSource.clearCache();
				}

				if (mc.renderGlobal != null) {
					mc.renderGlobal.loadRenderers();
				}
			}
		}
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		listView.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		listView.handleTouchInput();
	}

	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		listView.mouseClicked(parInt1, parInt2, parInt3);
	}

	protected void mouseReleased(int i, int j, int k) {
		super.mouseReleased(i, j, k);
		listView.mouseReleased(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		this.drawBackground(0);
		listView.drawScreen(i, j, f);
		drawCenteredString(this.fontRendererObj, title, this.width / 2, 15, 16777215);
		super.drawScreen(i, j, f);
		listView.postRender(i, j, f);
	}

	void renderTooltip(List<String> txt, int x, int y) {
		drawHoveringText(txt, x, y);
	}

	FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

	Minecraft getMinecraft() {
		return mc;
	}
}

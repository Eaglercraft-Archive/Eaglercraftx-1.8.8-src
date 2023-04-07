package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class GuiShadersNotSupported extends GuiScreen {

	private GuiScreen parent;
	private String reason;

	public GuiShadersNotSupported(GuiScreen parent, String reason) {
		this.parent = parent;
		this.reason = reason;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 10, I18n.format("gui.back")));
	}

	public void drawScreen(int i, int j, float var3) {
		this.drawBackground(0);
		drawCenteredString(fontRendererObj, I18n.format("shaders.gui.unsupported.title"), width / 2, height / 2 - 30, 0xFFFFFF);
		drawCenteredString(fontRendererObj, reason, width / 2, height / 2 - 10, 11184810);
		super.drawScreen(i, j, var3);
	}

	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 0) {
			mc.displayGuiScreen(parent);
		}
	}

}

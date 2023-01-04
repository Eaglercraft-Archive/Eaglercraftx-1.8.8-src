package net.lax1dude.eaglercraft.v1_8.socket;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
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
public class GuiHandshakeApprove extends GuiScreen {

	protected String message;
	protected GuiScreen no;
	protected GuiScreen yes;

	protected String titleString;
	protected List<String> bodyLines;

	protected int bodyY;

	public GuiHandshakeApprove(String message, GuiScreen no, GuiScreen yes) {
		this.message = message;
		this.no = no;
		this.yes = yes;
	}

	public GuiHandshakeApprove(String message, GuiScreen back) {
		this(message, back, null);
	}

	public void initGui() {
		this.buttonList.clear();
		titleString = I18n.format("handshakeApprove." + message + ".title");
		bodyLines = new ArrayList();
		int i = 0;
		boolean wasNull = true;
		while(true) {
			String line = getI18nOrNull("handshakeApprove." + message + ".body." + (i++));
			if(line == null) {
				if(wasNull) {
					break;
				}else {
					bodyLines.add("");
					wasNull = true;
				}
			}else {
				bodyLines.add(line);
				wasNull = false;
			}
		}
		int totalHeight = 10 + 10 + bodyLines.size() * 10 + 10 + 20;
		bodyY = (height - totalHeight) / 2 - 15;
		int buttonY = bodyY + totalHeight - 20;
		if(yes != null) {
			this.buttonList.add(new GuiButton(0, width / 2 + 3, buttonY, 100, 20, I18n.format("gui.no")));
			this.buttonList.add(new GuiButton(1, width / 2 - 103, buttonY, 100, 20, I18n.format("gui.yes")));
		}else {
			this.buttonList.add(new GuiButton(0, width / 2 - 100, buttonY, 200, 20, I18n.format("gui.back")));
		}
	}

	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 0) {
			mc.displayGuiScreen(no);
		}else if(parGuiButton.id == 1) {
			mc.displayGuiScreen(yes);
		}
	}

	public void drawScreen(int xx, int yy, float partialTicks) {
		drawBackground(0);
		drawCenteredString(fontRendererObj, titleString, width / 2, bodyY, 16777215);
		for(int i = 0, l = bodyLines.size(); i < l; ++i) {
			String s = bodyLines.get(i);
			if(s.length() > 0) {
				drawCenteredString(fontRendererObj, s, width / 2, bodyY + 20 + i * 10, 16777215);
			}
		}
		super.drawScreen(xx, yy, partialTicks);		
	}

	private String getI18nOrNull(String key) {
		String ret = I18n.format(key);
		if(key.equals(ret)) {
			return null;
		}else {
			return ret;
		}
	}

}

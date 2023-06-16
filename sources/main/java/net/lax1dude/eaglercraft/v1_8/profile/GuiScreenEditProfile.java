package net.lax1dude.eaglercraft.v1_8.profile;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.io.IOException;

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
public class GuiScreenEditProfile extends GuiScreen {

	private final GuiScreen parent;
	private GuiTextField usernameField;

	private boolean dropDownOpen = false;
	private String[] dropDownOptions;
	private int slotsVisible = 0;
	private int selectedSlot = 0;
	private int scrollPos = -1;
	private int skinsHeight = 0;
	private boolean dragging = false;
	private int mousex = 0;
	private int mousey = 0;

	private boolean newSkinWaitSteveOrAlex = false;

	private static final ResourceLocation eaglerGui = new ResourceLocation("eagler:gui/eagler_gui.png");

	protected String screenTitle = "Edit Profile";

	public GuiScreenEditProfile(GuiScreen parent) {
		this.parent = parent;
		updateOptions();
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		screenTitle = I18n.format("editProfile.title");
		usernameField = new GuiTextField(0, fontRendererObj, width / 2 - 20 + 1, height / 6 + 24 + 1, 138, 20);
		usernameField.setFocused(true);
		usernameField.setText(EaglerProfile.getName());
		selectedSlot = EaglerProfile.presetSkinId == -1 ? EaglerProfile.customSkinId : (EaglerProfile.presetSkinId + EaglerProfile.customSkins.size());
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 - 21, height / 6 + 110, 71, 20, I18n.format("editProfile.addSkin")));
		buttonList.add(new GuiButton(2, width / 2 - 21 + 71, height / 6 + 110, 72, 20, I18n.format("editProfile.clearSkin")));
	}

	private void updateOptions() {
		int numCustom = EaglerProfile.customSkins.size();
		String[] n = new String[numCustom + DefaultSkins.defaultSkinsMap.length];
		for(int i = 0; i < numCustom; ++i) {
			n[i] = EaglerProfile.customSkins.get(i).name;
		}
		int numDefault = DefaultSkins.defaultSkinsMap.length;
		for(int j = 0; j < numDefault; ++j) {
			n[numCustom + j] = DefaultSkins.defaultSkinsMap[j].name;
		}
		dropDownOptions = n;
	}

	public void drawScreen(int mx, int my, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, screenTitle, width / 2, 15, 16777215);
		drawString(fontRendererObj, I18n.format("editProfile.username"), width / 2 - 20, height / 6 + 8, 10526880);
		drawString(fontRendererObj, I18n.format("editProfile.playerSkin"), width / 2 - 20, height / 6 + 66, 10526880);
		
		mousex = mx;
		mousey = my;
		
		int skinX = width / 2 - 120;
		int skinY = height / 6 + 8;
		int skinWidth = 80;
		int skinHeight = 130;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, 0xFFA0A0A0);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, 0xFF000015);
		
		int skid = selectedSlot - EaglerProfile.customSkins.size();
		if(skid < 0) {
			skid = 0;
		}
		
		usernameField.drawTextBox();
		if(dropDownOpen ||  newSkinWaitSteveOrAlex) {
			super.drawScreen(0, 0, partialTicks);
		}else {
			super.drawScreen(mx, my, partialTicks);
		}
		
		skinX = width / 2 - 20;
		skinY = height / 6 + 82;
		skinWidth = 140;
		skinHeight = 22;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 21, skinY + skinHeight - 1, -16777216);
		drawRect(skinX + skinWidth - 20, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		mc.getTextureManager().bindTexture(eaglerGui);
		drawTexturedModalRect(skinX + skinWidth - 18, skinY + 3, 0, 0, 16, 16);
		
		drawString(fontRendererObj, dropDownOptions[selectedSlot], skinX + 5, skinY + 7, 14737632);
		
		skinX = width / 2 - 20;
		skinY = height / 6 + 103;
		skinWidth = 140;
		skinHeight = (height - skinY - 10);
		slotsVisible = (skinHeight / 10);
		if(slotsVisible > dropDownOptions.length) slotsVisible = dropDownOptions.length;
		skinHeight = slotsVisible * 10 + 7;
		skinsHeight = skinHeight;
		if(scrollPos == -1) {
			scrollPos = selectedSlot - 2;
		}
		if(scrollPos > (dropDownOptions.length - slotsVisible)) {
			scrollPos = (dropDownOptions.length - slotsVisible);
		}
		if(scrollPos < 0) {
			scrollPos = 0;
		}
		if(dropDownOpen) {
			drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
			drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
			for(int i = 0; i < slotsVisible; i++) {
				if(i + scrollPos < dropDownOptions.length) {
					if(selectedSlot == i + scrollPos) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x77ffffff);
					}else if(mx >= skinX && mx < (skinX + skinWidth - 10) && my >= (skinY + i*10 + 5) && my < (skinY + i*10 + 15)) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x55ffffff);
					}
					drawString(fontRendererObj, dropDownOptions[i + scrollPos], skinX + 5, skinY + 5 + i*10, 14737632);
				}
			}
			int scrollerSize = skinHeight * slotsVisible / dropDownOptions.length;
			int scrollerPos = skinHeight * scrollPos / dropDownOptions.length;
			drawRect(skinX + skinWidth - 4, skinY + scrollerPos + 1, skinX + skinWidth - 1, skinY + scrollerPos + scrollerSize, 0xff888888);
		}

		int xx = width / 2 - 80;
		int yy = height / 6 + 130;
		int numberOfCustomSkins = EaglerProfile.customSkins.size();
		
		if(newSkinWaitSteveOrAlex && selectedSlot < numberOfCustomSkins) {
			skinWidth = 70;
			skinHeight = 120;
			
			CustomSkin newSkin = EaglerProfile.customSkins.get(selectedSlot);
			
			GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
			
			skinX = width / 2 - 90;
			skinY = height / 4;
			xx = skinX + 35;
			yy = skinY + 117;
			
			boolean mouseOver = mx >= skinX && my >= skinY && mx < skinX + skinWidth && my < skinY + skinHeight;
			int cc = mouseOver ? 0xFFDDDD99 : 0xFF555555;
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			drawRect(0, 0, width, height, 0xbb000000);
			drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, 0xbb000000);
			GlStateManager.disableBlend();
			
			drawRect(skinX, skinY, skinX + 1, skinY + skinHeight, cc);
			drawRect(skinX, skinY, skinX + skinWidth, skinY + 1, cc);
			drawRect(skinX + skinWidth - 1, skinY, skinX + skinWidth, skinY + skinHeight, cc);
			drawRect(skinX, skinY + skinHeight - 1, skinX + skinWidth, skinY + skinHeight, cc);
			
			if(mouseOver) {
				drawCenteredString(fontRendererObj, "Steve", skinX + skinWidth / 2, skinY + skinHeight + 6, cc);
			}
			
			mc.getTextureManager().bindTexture(newSkin.getResource());
			SkinPreviewRenderer.renderBiped(xx, yy, mx, my, SkinModel.STEVE);
			
			skinX = width / 2 + 20;
			skinY = height / 4;
			xx = skinX + 35;
			yy = skinY + 117;
			
			mouseOver = mx >= skinX && my >= skinY && mx < skinX + skinWidth && my < skinY + skinHeight;
			cc = mouseOver ? 0xFFDDDD99 : 0xFF555555;
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, 0xbb000000);
			GlStateManager.disableBlend();
			
			drawRect(skinX, skinY, skinX + 1, skinY + skinHeight, cc);
			drawRect(skinX, skinY, skinX + skinWidth, skinY + 1, cc);
			drawRect(skinX + skinWidth - 1, skinY, skinX + skinWidth, skinY + skinHeight, cc);
			drawRect(skinX, skinY + skinHeight - 1, skinX + skinWidth, skinY + skinHeight, cc);
			
			if(mouseOver) {
				drawCenteredString(fontRendererObj, "Alex", skinX + skinWidth / 2, skinY + skinHeight + 8, cc);
			}
			
			mc.getTextureManager().bindTexture(newSkin.getResource());
			SkinPreviewRenderer.renderBiped(xx, yy, mx, my, SkinModel.ALEX);
		}else {
			skinX = this.width / 2 - 120;
			skinY = this.height / 6 + 8;
			skinWidth = 80;
			skinHeight = 130;
			
			ResourceLocation texture;
			SkinModel model;
			if(selectedSlot < numberOfCustomSkins) {
				CustomSkin customSkin = EaglerProfile.customSkins.get(selectedSlot);
				texture = customSkin.getResource();
				model = customSkin.model;
			}else {
				DefaultSkins defaultSkin = DefaultSkins.defaultSkinsMap[selectedSlot - numberOfCustomSkins];
				texture = defaultSkin.location;
				model = defaultSkin.model;
			}

			mc.getTextureManager().bindTexture(texture);
			SkinPreviewRenderer.renderBiped(xx, yy, newSkinWaitSteveOrAlex ? width / 2 : mx, newSkinWaitSteveOrAlex ? height / 2 : my, model);
		}
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		if(dropDownOpen) {
			int var1 = Mouse.getEventDWheel();
			if(var1 < 0) {
				scrollPos += 3;
			}
			if(var1 > 0) {
				scrollPos -= 3;
				if(scrollPos < 0) {
					scrollPos = 0;
				}
			}
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(!dropDownOpen) {
			if(par1GuiButton.id == 0) {
				safeProfile();
				this.mc.displayGuiScreen((GuiScreen) parent);
			}else if(par1GuiButton.id == 1) {
				EagRuntime.displayFileChooser("image/png", "png");
			}else if(par1GuiButton.id == 2) {
				EaglerProfile.clearCustomSkins();
				safeProfile();
				updateOptions();
				selectedSlot = 0;
			}
		}
	}

	public void updateScreen() {
		usernameField.updateCursorCounter();
		if(EagRuntime.fileChooserHasResult()) {
			FileChooserResult result = EagRuntime.getFileChooserResult();
			if(result != null) {
				ImageData loadedSkin = ImageData.loadImageFile(result.fileData);
				if(loadedSkin != null) {
					boolean isLegacy = loadedSkin.width == 64 && loadedSkin.height == 32;
					boolean isModern = loadedSkin.width == 64 && loadedSkin.height == 64;
					if(isLegacy) {
						ImageData newSkin = new ImageData(64, 64, true);
						SkinConverter.convert64x32to64x64(loadedSkin, newSkin);
						loadedSkin = newSkin;
						isModern = true;
					}
					if(isModern) {
						byte[] rawSkin = new byte[16384];
						for(int i = 0, j, k; i < 4096; ++i) {
							j = i << 2;
							k = loadedSkin.pixels[i];
							rawSkin[j] = (byte)(k >> 24);
							rawSkin[j + 1] = (byte)(k >> 16);
							rawSkin[j + 2] = (byte)(k >> 8);
							rawSkin[j + 3] = (byte)(k & 0xFF);
						}
						for(int y = 20; y < 32; ++y) {
							for(int x = 16; x < 40; ++x) {
								rawSkin[(y << 8) | (x << 2)] = (byte)0xff;
							}
						}
						int k;
						if((k = EaglerProfile.addCustomSkin(result.fileName, rawSkin)) != -1) {
							selectedSlot = k;
							newSkinWaitSteveOrAlex = true;
							updateOptions();
							safeProfile();
						}
					}else {
						EagRuntime.showPopup("The selected image '" + result.fileName + "' is not the right size!\nEaglercraft only supports 64x32 or 64x64 skins");
					}
				}else {
					EagRuntime.showPopup("The selected file '" + result.fileName + "' is not a PNG file!");
				}
			}
		}
		if(dropDownOpen) {
			if(Mouse.isButtonDown(0)) {
				int skinX = width / 2 - 20;
				int skinY = height / 6 + 103;
				int skinWidth = 140;
				if(mousex >= (skinX + skinWidth - 10) && mousex < (skinX + skinWidth) && mousey >= skinY && mousey < (skinY + skinsHeight)) {
					dragging = true;
				}
				if(dragging) {
					int scrollerSize = skinsHeight * slotsVisible / dropDownOptions.length;
					scrollPos = (mousey - skinY - (scrollerSize / 2)) * dropDownOptions.length / skinsHeight;
				}
			}else {
				dragging = false;
			}
		}else {
			dragging = false;
		}
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void keyTyped(char c, int k) {
		usernameField.textboxKeyTyped(c, k);
		
		String text = usernameField.getText();
		if(text.length() > 16) text = text.substring(0, 16);
		text = text.replaceAll("[^A-Za-z0-9]", "_");
		usernameField.updateText(text);
		
		if(k == 200 && selectedSlot > 0) {
			--selectedSlot;
			scrollPos = selectedSlot - 2;
		}
		if(k == 208 && selectedSlot < (dropDownOptions.length - 1)) {
			++selectedSlot;
			scrollPos = selectedSlot - 2;
		}
	}
	
	protected void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);
		usernameField.mouseClicked(mx, my, button);
		if (button == 0) {
			if(newSkinWaitSteveOrAlex) {
				int skinX = width / 2 - 90;
				int skinY = height / 4;
				int skinWidth = 70;
				int skinHeight = 120;
				if(mx >= skinX && my >= skinY && mx < skinX + skinWidth && my < skinY + skinHeight) {
					if(selectedSlot < EaglerProfile.customSkins.size()) {
						newSkinWaitSteveOrAlex = false;
						EaglerProfile.customSkins.get(selectedSlot).model = SkinModel.STEVE;
						safeProfile();
					}
					return;
				}
				skinX = width / 2 + 20;
				skinY = height / 4;
				if(mx >= skinX && my >= skinY && mx < skinX + skinWidth && my < skinY + skinHeight) {
					if(selectedSlot < EaglerProfile.customSkins.size()) {
						EaglerProfile.customSkins.get(selectedSlot).model = SkinModel.ALEX;
						newSkinWaitSteveOrAlex = false;
						safeProfile();
					}
				}
				return;
			}else if(selectedSlot < EaglerProfile.customSkins.size()) {
				int skinX = width / 2 - 120;
				int skinY = height / 6 + 18;
				int skinWidth = 80;
				int skinHeight = 120;
				if(mx >= skinX && my >= skinY && mx < skinX + skinWidth && my < skinY + skinHeight) {
					if(selectedSlot < EaglerProfile.customSkins.size()) {
						newSkinWaitSteveOrAlex = true;
						return;
					}
				}
			}
			int skinX = width / 2 + 140 - 40;
			int skinY = height / 6 + 82;
		
			if(mx >= skinX && mx < (skinX + 20) && my >= skinY && my < (skinY + 22)) {
				dropDownOpen = !dropDownOpen;
				return;
			}
			
			skinX = width / 2 - 20;
			skinY = height / 6 + 82;
			int skinWidth = 140;
			int skinHeight = skinsHeight;
			
			if(!(mx >= skinX && mx < (skinX + skinWidth) && my >= skinY && my < (skinY + skinHeight + 22))) {
				dropDownOpen = false;
				dragging = false;
				return;
			}
			
			skinY += 21;
			
			if(dropDownOpen && !dragging) {
				for(int i = 0; i < slotsVisible; i++) {
					if(i + scrollPos < dropDownOptions.length) {
						if(selectedSlot != i + scrollPos) {
							if(mx >= skinX && mx < (skinX + skinWidth - 10) && my >= (skinY + i * 10 + 5) && my < (skinY + i * 10 + 15) && selectedSlot != i + scrollPos) {
								selectedSlot = i + scrollPos;
								dropDownOpen = false;
								dragging = false;
							}
						}
					}
				}
			}
		}
	}
	
	protected void safeProfile() {
		int customLen = EaglerProfile.customSkins.size();
		if(selectedSlot < customLen) {
			EaglerProfile.presetSkinId = -1;
			EaglerProfile.customSkinId = selectedSlot;
		}else {
			EaglerProfile.presetSkinId = selectedSlot - customLen;
			EaglerProfile.customSkinId = -1;
		}
		String name = usernameField.getText().trim();
		while(name.length() < 3) {
			name = name + "_";
		}
		if(name.length() > 16) {
			name = name.substring(0, 16);
		}
		EaglerProfile.setName(name);
		EaglerProfile.write();
	}

}

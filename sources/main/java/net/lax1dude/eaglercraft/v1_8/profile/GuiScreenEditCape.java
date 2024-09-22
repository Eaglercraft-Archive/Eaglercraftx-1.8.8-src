package net.lax1dude.eaglercraft.v1_8.profile;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

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
public class GuiScreenEditCape extends GuiScreen {

	private final GuiScreenEditProfile parent;

	private boolean dropDownOpen = false;
	private String[] dropDownOptions;
	private int slotsVisible = 0;
	protected int selectedSlot = 0;
	private int scrollPos = -1;
	private int skinsHeight = 0;
	private boolean dragging = false;
	private int mousex = 0;
	private int mousey = 0;

	private static final ResourceLocation eaglerGui = new ResourceLocation("eagler:gui/eagler_gui.png");

	protected String screenTitle = "Edit Cape";

	public GuiScreenEditCape(GuiScreenEditProfile parent) {
		this.parent = parent;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		screenTitle = I18n.format("editCape.title");
		selectedSlot = EaglerProfile.presetCapeId == -1 ? EaglerProfile.customCapeId : (EaglerProfile.presetCapeId + EaglerProfile.customCapes.size());
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 - 21, height / 6 + 80, 71, 20, I18n.format("editCape.addCape")));
		buttonList.add(new GuiButton(2, width / 2 - 21 + 71, height / 6 + 80, 72, 20, I18n.format("editCape.clearCape")));
		updateOptions();
	}

	private void updateOptions() {
		int numCustom = EaglerProfile.customCapes.size();
		String[] n = new String[numCustom + DefaultCapes.defaultCapesMap.length];
		for(int i = 0; i < numCustom; ++i) {
			n[i] = EaglerProfile.customCapes.get(i).name;
		}
		int numDefault = DefaultCapes.defaultCapesMap.length;
		for(int j = 0; j < numDefault; ++j) {
			n[numCustom + j] = DefaultCapes.defaultCapesMap[j].name;
		}
		dropDownOptions = n;
	}

	public void drawScreen(int mx, int my, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, screenTitle, width / 2, 15, 16777215);
		drawString(fontRendererObj, I18n.format("editCape.playerCape"), width / 2 - 20, height / 6 + 36, 10526880);
		
		mousex = mx;
		mousey = my;
		
		int skinX = width / 2 - 120;
		int skinY = height / 6 + 8;
		int skinWidth = 80;
		int skinHeight = 130;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, 0xFFA0A0A0);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, 0xFF000015);
		
		int skid = selectedSlot - EaglerProfile.customCapes.size();
		if(skid < 0) {
			skid = 0;
		}
		
		if(dropDownOpen) {
			super.drawScreen(0, 0, partialTicks);
		}else {
			super.drawScreen(mx, my, partialTicks);
		}

		int numberOfCustomSkins = EaglerProfile.customSkins.size();
		int numberOfCustomCapes = EaglerProfile.customCapes.size();
		ResourceLocation skinTexture;
		SkinModel model;
		if(parent.selectedSlot < numberOfCustomSkins) {
			CustomSkin customSkin = EaglerProfile.customSkins.get(parent.selectedSlot);
			skinTexture = customSkin.getResource();
			model = customSkin.model;
		}else {
			DefaultSkins defaultSkin = DefaultSkins.getSkinFromId(parent.selectedSlot - numberOfCustomSkins);
			skinTexture = defaultSkin.location;
			model = defaultSkin.model;
		}
		
		if(model.highPoly != null) {
			drawCenteredString(fontRendererObj, I18n.format(this.mc.gameSettings.enableFNAWSkins ? "editProfile.disableFNAW" : "editProfile.enableFNAW"), width / 2, height / 6 + 150, 10526880);
		}
		
		skinX = width / 2 - 20;
		skinY = height / 6 + 52;
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
		skinY = height / 6 + 73;
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

		if(!EagRuntime.getConfiguration().isDemo()) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			String text = I18n.format("editProfile.importExport");
			
			int w = mc.fontRendererObj.getStringWidth(text);
			boolean hover = mx > 1 && my > 1 && mx < (w * 3 / 4) + 7 && my < 12;
			if(hover) {
				Mouse.showCursor(EnumCursorType.HAND);
			}
	
			drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, 5, 5, hover ? 0xFFEEEE22 : 0xFFCCCCCC);
			
			GlStateManager.popMatrix();
		}

		int xx = width / 2 - 80;
		int yy = height / 6 + 130;
		
		skinX = this.width / 2 - 120;
		skinY = this.height / 6 + 8;
		skinWidth = 80;
		skinHeight = 130;

		ResourceLocation capeTexture;
		if(selectedSlot < numberOfCustomCapes) {
			capeTexture = EaglerProfile.customCapes.get(selectedSlot).getResource();
		}else {
			capeTexture = DefaultCapes.getCapeFromId(selectedSlot - numberOfCustomCapes).location;
		}

		SkinPreviewRenderer.renderPreview(xx, yy, mx, my, true, model, skinTexture, capeTexture);
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
				EaglerProfile.clearCustomCapes();
				safeProfile();
				updateOptions();
				selectedSlot = 0;
			}
		}
	}

	public void updateScreen() {
		if(EagRuntime.fileChooserHasResult()) {
			FileChooserResult result = EagRuntime.getFileChooserResult();
			if(result != null) {
				ImageData loadedCape = ImageData.loadImageFile(result.fileData, ImageData.getMimeFromType(result.fileName));
				if(loadedCape != null) {
					if((loadedCape.width == 32 || loadedCape.width == 64) && loadedCape.height == 32) {
						byte[] resized = new byte[1173];
						SkinConverter.convertCape32x32RGBAto23x17RGB(loadedCape, resized);
						int k;
						if((k = EaglerProfile.addCustomCape(result.fileName, resized)) != -1) {
							selectedSlot = k;
							updateOptions();
							safeProfile();
						}
					}else {
						EagRuntime.showPopup("The selected image '" + result.fileName + "' is not the right size!\nEaglercraft only supports 32x32 or 64x32 capes");
					}
				}else {
					EagRuntime.showPopup("The selected file '" + result.fileName + "' is not a supported format!");
				}
			}
		}
		if(dropDownOpen) {
			if(PointerInputAbstraction.getVCursorButtonDown(0)) {
				int skinX = width / 2 - 20;
				int skinY = height / 6 + 73;
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
		if (button == 0) {
			if(!EagRuntime.getConfiguration().isDemo()) {
				int w = mc.fontRendererObj.getStringWidth(I18n.format("editProfile.importExport"));
				if(mx > 1 && my > 1 && mx < (w * 3 / 4) + 7 && my < 12) {
					safeProfile();
					mc.displayGuiScreen(new GuiScreenImportExportProfile(parent));
					mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
					return;
				}
			}
			
			int skinX = width / 2 + 140 - 40;
			int skinY = height / 6 + 52;
		
			if(mx >= skinX && mx < (skinX + 20) && my >= skinY && my < (skinY + 22)) {
				dropDownOpen = !dropDownOpen;
				return;
			}
			
			skinX = width / 2 - 20;
			skinY = height / 6 + 52;
			int skinWidth = 140;
			int skinHeight = skinsHeight;
			
			if(!(mx >= skinX && mx < (skinX + skinWidth) && my >= skinY && my < (skinY + skinHeight + 22))) {
				dragging = false;
				if(dropDownOpen) {
					dropDownOpen = false;
					return;
				}
			}else if(dropDownOpen && !dragging) {
				skinY += 21;
				for(int i = 0; i < slotsVisible; i++) {
					if(i + scrollPos < dropDownOptions.length) {
						if(mx >= skinX && mx < (skinX + skinWidth - 10) && my >= (skinY + i * 10 + 5) && my < (skinY + i * 10 + 15) && selectedSlot != i + scrollPos) {
							selectedSlot = i + scrollPos;
							dropDownOpen = false;
							dragging = false;
							return;
						}
					}
				}
			}
		}
		super.mouseClicked(mx, my, button);
	}
	
	protected void safeProfile() {
		int customLen = EaglerProfile.customCapes.size();
		if(selectedSlot < customLen) {
			EaglerProfile.presetCapeId = -1;
			EaglerProfile.customCapeId = selectedSlot;
		}else {
			EaglerProfile.presetCapeId = selectedSlot - customLen;
			EaglerProfile.customCapeId = -1;
		}
	}

}

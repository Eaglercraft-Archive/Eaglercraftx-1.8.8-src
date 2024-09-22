
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 12  @  2

+ import java.util.ArrayList;
+ import java.util.Collection;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Display;
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
+ import net.lax1dude.eaglercraft.v1_8.Touch;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ 

> CHANGE  3 : 9  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchControls;
~ import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchOverlayRenderer;

> CHANGE  2 : 3  @  2 : 11

~ import net.minecraft.client.gui.inventory.GuiInventory;

> DELETE  2  @  2 : 3

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.renderer.entity.RenderManager;

> INSERT  13 : 14  @  13

+ import net.minecraft.network.play.client.C16PacketClientStatus;

> INSERT  11 : 12  @  11

+ import net.minecraft.util.MovingObjectPosition.MovingObjectType;

> CHANGE  8 : 9  @  8 : 9

~ 	private final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  3  @  3 : 4

> CHANGE  7 : 8  @  7 : 8

~ 	public final GuiOverlayDebug overlayDebug;

> DELETE  19  @  19 : 20

> CHANGE  11 : 12  @  11 : 12

~ 		ScaledResolution scaledresolution = mc.scaledResolution;

> CHANGE  4 : 7  @  4 : 9

~ 		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
~ 		GlStateManager.enableDepth();
~ 		GlStateManager.disableLighting();

> INSERT  15 : 17  @  15

+ 		onBeginHotbarDraw();
+ 

> DELETE  6  @  6 : 7

> DELETE  1  @  1 : 8

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 22

> INSERT  1 : 2  @  1

+ 		GlStateManager.disableBlend();

> INSERT  7 : 9  @  7

+ 		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 		GlStateManager.disableBlend();

> CHANGE  6 : 8  @  6 : 14

~ 		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
~ 		GlStateManager.disableBlend();

> DELETE  1  @  1 : 2

> INSERT  21 : 22  @  21

+ 		}

> CHANGE  1 : 20  @  1 : 2

~ 		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
~ 		drawEaglerInteractButton(scaledresolution);
~ 
~ 		onEndHotbarDraw();
~ 
~ 		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
~ 		if (this.mc.thePlayer.getSleepTimer() > 0) {
~ 			GlStateManager.disableDepth();
~ 			GlStateManager.disableAlpha();
~ 			int j1 = this.mc.thePlayer.getSleepTimer();
~ 			float f1 = (float) j1 / 100.0F;
~ 			if (f1 > 1.0F) {
~ 				f1 = 1.0F - (float) (j1 - 100) / 10.0F;
~ 			}
~ 
~ 			int k = (int) (220.0F * f1) << 24 | 1052704;
~ 			drawRect(0, 0, i, j, k);
~ 			GlStateManager.enableAlpha();
~ 			GlStateManager.enableDepth();

> INSERT  2 : 8  @  2

+ 		if (this.mc.isDemo()) {
+ 			this.renderDemo(scaledresolution);
+ 		}
+ 
+ 		this.overlayDebug.renderDebugInfo(scaledresolution);
+ 

> DELETE  1  @  1 : 2

> DELETE  33  @  33 : 35

> INSERT  18 : 22  @  18

+ 		if (this.mc.currentScreen == null) {
+ 			this.mc.voiceOverlay.drawOverlay();
+ 		}
+ 

> INSERT  4 : 9  @  4

+ 		if (this.mc.gameSettings.hudWorld && (mc.currentScreen == null || !(mc.currentScreen instanceof GuiChat))) {
+ 			j -= 10;
+ 		}
+ 		j -= (this.mc.displayHeight - (Display.getVisualViewportX() + Display.getVisualViewportH())) * j
+ 				/ this.mc.displayHeight;

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> INSERT  11 : 12  @  11

+ 

> INSERT  4 : 15  @  4

+ 	public void renderGameOverlayCrosshairs(int scaledResWidth, int scaledResHeight) {
+ 		if (this.showCrosshair()) {
+ 			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 			this.mc.getTextureManager().bindTexture(icons);
+ 			GlStateManager.enableBlend();
+ 			GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
+ 			GlStateManager.enableAlpha();
+ 			this.drawTexturedModalRect(scaledResWidth / 2 - 7, scaledResHeight / 2 - 7, 0, 0, 16, 16);
+ 		}
+ 	}
+ 

> INSERT  9 : 26  @  9

+ 
+ 			if (PointerInputAbstraction.isTouchMode()) {
+ 				this.mc.getTextureManager().bindTexture(TouchOverlayRenderer.spriteSheet);
+ 				this.drawTexturedModalRect(i + 89, sr.getScaledHeight() - 22, 234, 0, 22, 22);
+ 				int areaHAdd = 12;
+ 				hotbarAreaX = (i - 91) * mc.displayWidth / sr.getScaledWidth();
+ 				hotbarAreaY = (sr.getScaledHeight() - 22 - areaHAdd) * mc.displayHeight / sr.getScaledHeight();
+ 				hotbarAreaW = 203 * mc.displayWidth / sr.getScaledWidth();
+ 				hotbarAreaH = (22 + areaHAdd) * mc.displayHeight / sr.getScaledHeight();
+ 			} else {
+ 				hotbarAreaX = -1;
+ 				hotbarAreaY = -1;
+ 				hotbarAreaW = -1;
+ 				hotbarAreaH = -1;
+ 			}
+ 
+ 			this.mc.getTextureManager().bindTexture(widgetsTexPath);

> INSERT  16 : 17  @  16

+ 

> DELETE  5  @  5 : 6

> DELETE  9  @  9 : 11

> DELETE  3  @  3 : 4

> DELETE  12  @  12 : 13

> DELETE  1  @  1 : 2

> DELETE  10  @  10 : 11

> DELETE  5  @  5 : 6

> CHANGE  1 : 2  @  1 : 2

~ 			String s = this.highlightingItemStack.getDisplayNameProfanityFilter();

> DELETE  25  @  25 : 26

> DELETE  3  @  3 : 4

> DELETE  11  @  11 : 12

> DELETE  25  @  25 : 29

> CHANGE  17 : 19  @  17 : 18

~ 		for (int m = 0, n = arraylist1.size(); m < n; ++m) {
~ 			Score score = (Score) arraylist1.get(m);

> CHANGE  12 : 14  @  12 : 13

~ 		for (int m = 0, n = arraylist1.size(); m < n; ++m) {
~ 			Score score1 = (Score) arraylist1.get(m);

> CHANGE  7 : 9  @  7 : 9

~ 			this.getFontRenderer().drawString(s1, k1, k, 0xFFFFFFFF);
~ 			this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 0xFFFFFFFF);

> CHANGE  5 : 6  @  5 : 6

~ 						k - this.getFontRenderer().FONT_HEIGHT, 0xFFFFFFFF);

> INSERT  25 : 27  @  25

+ 			GlStateManager.enableBlend();
+ 			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

> DELETE  23  @  23 : 25

> DELETE  17  @  17 : 19

> DELETE  61  @  61 : 63

> DELETE  39  @  39 : 40

> DELETE  36  @  36 : 37

> DELETE  14  @  14 : 15

> CHANGE  6 : 7  @  6 : 9

~ 			int i = mc.scaledResolution.getScaledWidth();

> CHANGE  40 : 41  @  40 : 41

~ 	public void renderVignette(float parFloat1, int scaledWidth, int scaledHeight) {

> CHANGE  29 : 32  @  29 : 33

~ 		worldrenderer.pos(0.0D, (double) scaledHeight, -90.0D).tex(0.0D, 1.0D).endVertex();
~ 		worldrenderer.pos((double) scaledWidth, scaledHeight, -90.0D).tex(1.0D, 1.0D).endVertex();
~ 		worldrenderer.pos((double) scaledWidth, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();

> CHANGE  21 : 22  @  21 : 22

~ 		EaglerTextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes()

> DELETE  57  @  57 : 58

> INSERT  60 : 113  @  60

+ 	public void drawEaglerPlayerOverlay(int x, int y, float partialTicks) {
+ 		Entity e = mc.getRenderViewEntity();
+ 		if (e != null && e instanceof EntityLivingBase) {
+ 			EntityLivingBase ent = (EntityLivingBase) e;
+ 			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 			GlStateManager.enableDepth();
+ 			GlStateManager.enableColorMaterial();
+ 			GlStateManager.pushMatrix();
+ 			GlStateManager.translate((float) x - 10, (float) y + 36, 50.0F);
+ 			GlStateManager.scale(-17.0F, 17.0F, 17.0F);
+ 			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
+ 			float f = ent.renderYawOffset;
+ 			float f1 = ent.rotationYaw;
+ 			float f2 = ent.prevRotationYaw;
+ 			float f3 = ent.prevRotationYawHead;
+ 			float f4 = ent.rotationYawHead;
+ 			float f5 = ent.prevRenderYawOffset;
+ 			GlStateManager.rotate(115.0F, 0.0F, 1.0F, 0.0F);
+ 			RenderHelper.enableStandardItemLighting();
+ 			float f6 = ent.prevRenderYawOffset + (ent.renderYawOffset - ent.prevRenderYawOffset) * partialTicks;
+ 			ent.rotationYawHead -= f6;
+ 			ent.prevRotationYawHead -= f6;
+ 			ent.rotationYawHead *= 0.5f;
+ 			ent.prevRotationYawHead *= 0.5f;
+ 			ent.renderYawOffset = 0.0f;
+ 			ent.prevRenderYawOffset = 0.0f;
+ 			ent.prevRotationYaw = 0.0f;
+ 			ent.rotationYaw = 0.0f;
+ 			GlStateManager.rotate(-135.0F
+ 					- (ent.prevRotationYawHead + (ent.rotationYawHead - ent.prevRotationYawHead) * partialTicks) * 0.5F,
+ 					0.0F, 1.0F, 0.0F);
+ 			GlStateManager.rotate(ent.rotationPitch * 0.2f, 1.0F, 0.0F, 0.0F);
+ 			RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
+ 			rendermanager.setPlayerViewY(180.0F);
+ 			rendermanager.setRenderShadow(false);
+ 			rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
+ 			rendermanager.setRenderShadow(true);
+ 			ent.renderYawOffset = f;
+ 			ent.rotationYaw = f1;
+ 			ent.prevRotationYaw = f2;
+ 			ent.prevRotationYawHead = f3;
+ 			ent.rotationYawHead = f4;
+ 			ent.prevRenderYawOffset = f5;
+ 			GlStateManager.popMatrix();
+ 			RenderHelper.disableStandardItemLighting();
+ 			GlStateManager.disableDepth();
+ 			GlStateManager.disableRescaleNormal();
+ 			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 			GlStateManager.disableTexture2D();
+ 			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 		}
+ 	}
+ 

> INSERT  27 : 243  @  27

+ 
+ 	private int hotbarAreaX = -1;
+ 	private int hotbarAreaY = -1;
+ 	private int hotbarAreaW = -1;
+ 	private int hotbarAreaH = -1;
+ 	private int currentHotbarSlotTouch = -1;
+ 	private long hotbarSlotTouchStart = -1l;
+ 	private boolean hotbarSlotTouchAlreadySelected = false;
+ 	private int interactButtonX = -1;
+ 	private int interactButtonY = -1;
+ 	private int interactButtonW = -1;
+ 	private int interactButtonH = -1;
+ 	private int touchVPosX = -1;
+ 	private int touchVPosY = -1;
+ 	private int touchEventUID = -1;
+ 
+ 	private void drawEaglerInteractButton(ScaledResolution parScaledResolution) {
+ 		if (PointerInputAbstraction.isTouchMode() && mc.objectMouseOver != null
+ 				&& mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY) {
+ 			int scale = parScaledResolution.getScaleFactor();
+ 			interactButtonW = 118 * scale;
+ 			interactButtonH = 20 * scale;
+ 			int xx = (parScaledResolution.getScaledWidth() - 118) / 2;
+ 			int yy = parScaledResolution.getScaledHeight() - 70;
+ 			interactButtonX = xx * scale;
+ 			interactButtonY = yy * scale;
+ 			mc.getTextureManager().bindTexture(TouchOverlayRenderer.spriteSheet);
+ 			boolean hover = touchVPosX >= interactButtonX && touchVPosY >= interactButtonY
+ 					&& touchVPosX < interactButtonX + interactButtonW && touchVPosY < interactButtonY + interactButtonH;
+ 			float f = MathHelper.clamp_float(mc.gameSettings.touchControlOpacity, 0.0f, 1.0f);
+ 			if (f > 0.0f) {
+ 				GlStateManager.color(1.0f, 1.0f, 1.0f, f);
+ 				drawTexturedModalRect(xx, yy, 0, hover ? 216 : 236, 118, 20);
+ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 				drawCenteredString(mc.fontRendererObj, I18n.format("touch.interact.entity"),
+ 						parScaledResolution.getScaledWidth() / 2, yy + 6,
+ 						(hover ? 16777120 : 14737632) | ((int) (f * 255.0f) << 24));
+ 			}
+ 		} else {
+ 			interactButtonX = -1;
+ 			interactButtonY = -1;
+ 			interactButtonW = -1;
+ 			interactButtonH = -1;
+ 		}
+ 	}
+ 
+ 	private int applyTouchHotbarTransformX(int posX, boolean scaled) {
+ 		if (scaled) {
+ 			return (posX + mc.scaledResolution.getScaledWidth() / 4) * 2 / 3;
+ 		} else {
+ 			return (posX + mc.displayWidth / 4) * 2 / 3;
+ 		}
+ 	}
+ 
+ 	private int applyTouchHotbarTransformY(int posY, boolean scaled) {
+ 		if (scaled) {
+ 			return (posY + mc.scaledResolution.getScaledHeight() / 2) * 2 / 3;
+ 		} else {
+ 			return (posY + mc.displayHeight / 2) * 2 / 3;
+ 		}
+ 	}
+ 
+ 	private void onBeginHotbarDraw() {
+ 		if (PointerInputAbstraction.isTouchMode()) {
+ 			GlStateManager.pushMatrix();
+ 			ScaledResolution res = mc.scaledResolution;
+ 			GlStateManager.translate(res.getScaledWidth() / -4, res.getScaledHeight() / -2, field_175199_z);
+ 			GlStateManager.scale(1.5f, 1.5f, 1.5f);
+ 		}
+ 	}
+ 
+ 	private void onEndHotbarDraw() {
+ 		if (PointerInputAbstraction.isTouchMode()) {
+ 			GlStateManager.popMatrix();
+ 		}
+ 	}
+ 
+ 	private int getHotbarSlotTouched(int pointX) {
+ 		int xx = pointX - hotbarAreaX - 2;
+ 		xx /= 20 * mc.scaledResolution.getScaleFactor();
+ 		if (xx < 0)
+ 			xx = 0;
+ 		if (xx > 9)
+ 			xx = 9;
+ 		return xx;
+ 	}
+ 
+ 	public boolean handleTouchBeginEagler(int uid, int pointX, int pointY) {
+ 		if (mc.thePlayer == null) {
+ 			return false;
+ 		}
+ 		if (touchEventUID == -1) {
+ 			pointX = applyTouchHotbarTransformX(pointX, false);
+ 			pointY = applyTouchHotbarTransformY(pointY, false);
+ 			if (pointX >= hotbarAreaX && pointY >= hotbarAreaY && pointX < hotbarAreaX + hotbarAreaW
+ 					&& pointY < hotbarAreaY + hotbarAreaH) {
+ 				touchEventUID = uid;
+ 				currentHotbarSlotTouch = getHotbarSlotTouched(pointX);
+ 				hotbarSlotTouchStart = EagRuntime.steadyTimeMillis();
+ 				if (currentHotbarSlotTouch >= 0 && currentHotbarSlotTouch < 9) {
+ 					if (mc.thePlayer.isSpectator()) {
+ 						hotbarSlotTouchAlreadySelected = false;
+ 						mc.ingameGUI.getSpectatorGui().func_175260_a(currentHotbarSlotTouch);
+ 					} else {
+ 						hotbarSlotTouchAlreadySelected = (mc.thePlayer.inventory.currentItem == currentHotbarSlotTouch);
+ 						mc.thePlayer.inventory.currentItem = currentHotbarSlotTouch;
+ 					}
+ 				} else if (currentHotbarSlotTouch == 9) {
+ 					hotbarSlotTouchAlreadySelected = false;
+ 					currentHotbarSlotTouch = 69;
+ 					if (mc.playerController.isRidingHorse()) {
+ 						mc.thePlayer.sendHorseInventory();
+ 					} else {
+ 						mc.getNetHandler().addToSendQueue(
+ 								new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
+ 						mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
+ 					}
+ 				}
+ 				return true;
+ 			}
+ 			if (pointX >= interactButtonX && pointY >= interactButtonY && pointX < interactButtonX + interactButtonW
+ 					&& pointY < interactButtonY + interactButtonH) {
+ 				touchEventUID = uid;
+ 				mc.rightClickMouse();
+ 				return true;
+ 			}
+ 		}
+ 		return false;
+ 	}
+ 
+ 	public boolean handleTouchEndEagler(int uid, int pointX, int pointY) {
+ 		if (uid == touchEventUID) {
+ 			if (hotbarSlotTouchStart != -1l && currentHotbarSlotTouch != 69) {
+ 				if (EagRuntime.steadyTimeMillis() - hotbarSlotTouchStart < 350l) {
+ 					if (hotbarSlotTouchAlreadySelected) {
+ 						if (mc.thePlayer != null) {
+ 							mc.thePlayer.dropOneItem(false);
+ 						}
+ 					}
+ 				}
+ 			}
+ 			touchVPosX = -1;
+ 			touchVPosY = -1;
+ 			touchEventUID = -1;
+ 			currentHotbarSlotTouch = -1;
+ 			hotbarSlotTouchStart = -1l;
+ 			hotbarSlotTouchAlreadySelected = false;
+ 			return true;
+ 		}
+ 		return false;
+ 	}
+ 
+ 	public void updateTouchEagler(boolean screenTouched) {
+ 		if (screenTouched) {
+ 			int pointCount = Touch.touchPointCount();
+ 			for (int i = 0; i < pointCount; ++i) {
+ 				int uid = Touch.touchPointUID(i);
+ 				if (TouchControls.touchControls.containsKey(uid)) {
+ 					continue;
+ 				}
+ 				if (touchEventUID == -1 || touchEventUID == uid) {
+ 					touchVPosX = applyTouchHotbarTransformX(Touch.touchPointX(i), false);
+ 					touchVPosY = applyTouchHotbarTransformY(mc.displayHeight - Touch.touchPointY(i) - 1, false);
+ 					long millis = EagRuntime.steadyTimeMillis();
+ 					if (touchEventUID != -1 && hotbarSlotTouchStart != -1l) {
+ 						if (currentHotbarSlotTouch != 69) {
+ 							int slot = getHotbarSlotTouched(touchVPosX);
+ 							if (slot != currentHotbarSlotTouch) {
+ 								hotbarSlotTouchAlreadySelected = false;
+ 								currentHotbarSlotTouch = slot;
+ 								hotbarSlotTouchStart = millis;
+ 								if (slot >= 0 && slot < 9) {
+ 									if (mc.thePlayer.isSpectator()) {
+ 										mc.ingameGUI.getSpectatorGui().func_175260_a(slot);
+ 									} else {
+ 										mc.thePlayer.inventory.currentItem = slot;
+ 									}
+ 								}
+ 							} else {
+ 								if (millis - hotbarSlotTouchStart > 1200l) {
+ 									if (!mc.thePlayer.isSpectator()) {
+ 										hotbarSlotTouchStart = millis;
+ 										this.mc.thePlayer.dropOneItem(true);
+ 									}
+ 								}
+ 							}
+ 						}
+ 					}
+ 					return;
+ 				}
+ 			}
+ 		}
+ 		if (touchEventUID != -1) {
+ 			handleTouchEndEagler(touchEventUID, touchVPosX, touchVPosY);
+ 		}
+ 		touchVPosX = -1;
+ 		touchVPosY = -1;
+ 		touchEventUID = -1;
+ 		currentHotbarSlotTouch = -1;
+ 		hotbarSlotTouchStart = -1l;
+ 		hotbarSlotTouchAlreadySelected = false;
+ 	}
+ 
+ 	public boolean isTouchOverlapEagler(int uid, int tx, int ty) {
+ 		if (touchEventUID == uid) {
+ 			return true;
+ 		}
+ 		ty = mc.displayHeight - ty - 1;
+ 		tx = applyTouchHotbarTransformX(tx, false);
+ 		ty = applyTouchHotbarTransformY(ty, false);
+ 		return (tx >= hotbarAreaX && ty >= hotbarAreaY && tx < hotbarAreaX + hotbarAreaW
+ 				&& ty < hotbarAreaY + hotbarAreaH)
+ 				|| (tx >= interactButtonX && ty >= interactButtonY && tx < interactButtonX + interactButtonW
+ 						&& ty < interactButtonY + interactButtonH);
+ 	}
+ 

> EOF

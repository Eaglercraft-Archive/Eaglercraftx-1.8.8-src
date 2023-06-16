
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 8  @  2

+ import java.util.ArrayList;
+ import java.util.Collection;
+ import java.util.List;
+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ 

> CHANGE  3 : 7  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  2  @  2 : 11

> DELETE  2  @  2 : 3

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.renderer.entity.RenderManager;

> CHANGE  32 : 33  @  32 : 33

~ 	private final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  3  @  3 : 4

> DELETE  27  @  27 : 28

> CHANGE  16 : 17  @  16 : 21

~ 		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

> DELETE  21  @  21 : 22

> DELETE  1  @  1 : 8

> CHANGE  44 : 45  @  44 : 47

~ 		this.overlayDebug.renderDebugInfo(scaledresolution);

> INSERT  87 : 90  @  87

+ 		if (this.mc.gameSettings.hudWorld && (mc.currentScreen == null || !(mc.currentScreen instanceof GuiChat))) {
+ 			j -= 10;
+ 		}

> INSERT  19 : 30  @  19

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

> DELETE  147  @  147 : 151

> CHANGE  17 : 18  @  17 : 18

~ 		for (Score score : (List<Score>) arraylist1) {

> CHANGE  12 : 13  @  12 : 13

~ 		for (Score score1 : (List<Score>) arraylist1) {

> CHANGE  7 : 9  @  7 : 9

~ 			this.getFontRenderer().drawString(s1, k1, k, 0xFFFFFFFF);
~ 			this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 0xFFFFFFFF);

> CHANGE  5 : 6  @  5 : 6

~ 						k - this.getFontRenderer().FONT_HEIGHT, 0xFFFFFFFF);

> INSERT  25 : 27  @  25

+ 			GlStateManager.enableBlend();
+ 			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

> CHANGE  248 : 249  @  248 : 249

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

> EOF

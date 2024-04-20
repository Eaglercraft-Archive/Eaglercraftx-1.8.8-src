
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 9  @  1

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.profile.RenderHighPoly;

> INSERT  3 : 4  @  3

+ import net.minecraft.client.Minecraft;

> DELETE  13  @  13 : 15

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 55

> INSERT  81 : 82  @  81

+ 	private RenderPlayer eaglerRenderer;

> CHANGE  82 : 88  @  82 : 83

~ 		this.skinMap.put("slim", new RenderPlayer(this, true, false));
~ 		this.skinMap.put("zombie", new RenderPlayer(this, false, true));
~ 		this.eaglerRenderer = new RenderHighPoly(this, this.playerRenderer.getMainModel(),
~ 				this.playerRenderer.shadowSize);
~ 		this.skinMap.put("eagler",
~ 				Minecraft.getMinecraft().gameSettings.enableFNAWSkins ? this.eaglerRenderer : this.playerRenderer);

> INSERT  2 : 6  @  2

+ 	public void setEnableFNAWSkins(boolean en) {
+ 		this.skinMap.put("eagler", en ? this.eaglerRenderer : this.playerRenderer);
+ 	}
+ 

> CHANGE  9 : 10  @  9 : 10

~ 			render = this.getEntityClassRenderObject((Class<? extends Entity>) parClass1.getSuperclass());

> CHANGE  6 : 7  @  6 : 7

~ 	public <T extends Entity> Render getEntityRenderObject(Entity entityIn) {

> INSERT  85 : 86  @  85

+ 			DeferredStateManager.setEmissionConstant(1.0f);

> CHANGE  7 : 13  @  7 : 9

~ 		try {
~ 			return this.doRenderEntity(entity, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ, f,
~ 					partialTicks, parFlag);
~ 		} finally {
~ 			DeferredStateManager.setEmissionConstant(0.0f);
~ 		}

> INSERT  2 : 13  @  2

+ 	public static void setupLightmapCoords(Entity entity, float partialTicks) {
+ 		int i = entity.getBrightnessForRender(partialTicks);
+ 		if (entity.isBurning()) {
+ 			DeferredStateManager.setEmissionConstant(1.0f);
+ 			i = 15728880;
+ 		}
+ 		int j = i % 65536;
+ 		int k = i / 65536;
+ 		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
+ 	}
+ 

> CHANGE  18 : 23  @  18 : 19

~ 		try {
~ 			return this.doRenderEntity(entityIn, x, y, z, entityYaw, partialTicks, false);
~ 		} finally {
~ 			DeferredStateManager.setEmissionConstant(0.0f);
~ 		}

> INSERT  14 : 17  @  14

+ 					RenderItem.renderPosX = (float) x;
+ 					RenderItem.renderPosY = (float) y + entity.height * 0.5f;
+ 					RenderItem.renderPosZ = (float) z;

> CHANGE  14 : 16  @  14 : 15

~ 				if (this.debugBoundingBox && !entity.isInvisible() && !parFlag
~ 						&& !DeferredStateManager.isDeferredRenderer()) {

> EOF

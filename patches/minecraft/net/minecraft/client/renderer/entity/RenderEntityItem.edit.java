
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
~ import net.minecraft.client.Minecraft;

> DELETE  1  @  1 : 4

> CHANGE  10 : 11  @  10 : 11

~ 	private EaglercraftRandom field_177079_e = new EaglercraftRandom();

> CHANGE  28 : 30  @  28 : 30

~ 				float f6 = 0.0F * (float) (i - 1) * 0.5F;
~ 				float f4 = 0.0F * (float) (i - 1) * 0.5F;

> INSERT  25 : 27  @  25

+ 		boolean emissive = entityitem.eaglerEmissiveFlag;
+ 		entityitem.eaglerEmissiveFlag = false;

> INSERT  8 : 11  @  8

+ 		if (emissive) {
+ 			DeferredStateManager.setEmissionConstant(1.0f);
+ 		}

> INSERT  43 : 46  @  43

+ 		if (emissive) {
+ 			DeferredStateManager.setEmissionConstant(0.0f);
+ 		}

> EOF

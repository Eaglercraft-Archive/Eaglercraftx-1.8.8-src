
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  3 : 8  @  5 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  10  @  10 : 11

> DELETE  23  @  24 : 25

> CHANGE  31 : 32  @  33 : 34

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  61 : 62  @  63 : 64

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  87 : 90  @  89 : 92

~ 		GlStateManager.viewport((scaledresolution.getScaledWidth() - 290 - 110) / 2 * scaledresolution.getScaleFactor(),
~ 				(scaledresolution.getScaledHeight() - 220 + 60) / 2 * scaledresolution.getScaleFactor(),
~ 				290 * scaledresolution.getScaleFactor(), 220 * scaledresolution.getScaleFactor());

> CHANGE  91 : 92  @  93 : 94

~ 		GlStateManager.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);

> INSERT  128 : 129  @  130

+ 		GlStateManager.enableDepth();

> INSERT  130 : 131  @  131

+ 		GlStateManager.disableDepth();

> EOF

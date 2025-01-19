
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  1 : 8  @  1 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  2  @  2 : 3

> DELETE  13  @  13 : 14

> CHANGE  8 : 9  @  8 : 9

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  29 : 30  @  29 : 30

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  24 : 28  @  24 : 28

~ 		ScaledResolution scaledresolution = mc.scaledResolution;
~ 		GlStateManager.viewport((scaledresolution.getScaledWidth() - 290 - 12) / 2 * scaledresolution.getScaleFactor(),
~ 				(scaledresolution.getScaledHeight() - 220 + 10) / 2 * scaledresolution.getScaleFactor(),
~ 				290 * scaledresolution.getScaleFactor(), 220 * scaledresolution.getScaleFactor());

> CHANGE  1 : 2  @  1 : 2

~ 		GlStateManager.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);

> INSERT  36 : 37  @  36

+ 		GlStateManager.enableDepth();

> INSERT  1 : 2  @  1

+ 		GlStateManager.disableDepth();

> INSERT  105 : 106  @  105

+ 				Mouse.showCursor(EnumCursorType.HAND);

> EOF

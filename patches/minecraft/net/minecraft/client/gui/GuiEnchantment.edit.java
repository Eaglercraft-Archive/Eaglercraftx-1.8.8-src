
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  1 : 6  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  7  @  5 : 6

> DELETE  13  @  14 : 15

> CHANGE  8 : 9  @  9 : 10

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  30 : 31  @  30 : 31

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  26 : 29  @  26 : 29

~ 		GlStateManager.viewport((scaledresolution.getScaledWidth() - 290 - 110) / 2 * scaledresolution.getScaleFactor(),
~ 				(scaledresolution.getScaledHeight() - 220 + 60) / 2 * scaledresolution.getScaleFactor(),
~ 				290 * scaledresolution.getScaleFactor(), 220 * scaledresolution.getScaleFactor());

> CHANGE  4 : 5  @  4 : 5

~ 		GlStateManager.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);

> INSERT  37 : 38  @  37

+ 		GlStateManager.enableDepth();

> INSERT  2 : 3  @  1

+ 		GlStateManager.disableDepth();

> EOF

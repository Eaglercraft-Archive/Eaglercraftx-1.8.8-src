
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

> CHANGE  3 : 6  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  2  @  2 : 11

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 2

> CHANGE  32 : 33  @  32 : 33

~ 	private final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  3  @  3 : 4

> DELETE  27  @  27 : 28

> CHANGE  95 : 96  @  95 : 98

~ 		this.overlayDebug.renderDebugInfo(scaledresolution, partialTicks);

> INSERT  87 : 90  @  87

+ 		if (this.mc.gameSettings.hudWorld && (mc.currentScreen == null || !(mc.currentScreen instanceof GuiChat))) {
+ 			j -= 10;
+ 		}

> DELETE  166  @  166 : 170

> CHANGE  17 : 18  @  17 : 18

~ 		for (Score score : (List<Score>) arraylist1) {

> CHANGE  12 : 13  @  12 : 13

~ 		for (Score score1 : (List<Score>) arraylist1) {

> CHANGE  343 : 344  @  343 : 344

~ 		EaglerTextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes()

> DELETE  57  @  57 : 58

> EOF

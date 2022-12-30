
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 8  @  2

+ import java.util.ArrayList;
+ import java.util.Collection;
+ import java.util.List;
+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ 

> CHANGE  9 : 12  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  5  @  5 : 14

> DELETE  2  @  11 : 12

> DELETE  1  @  2 : 3

> CHANGE  32 : 33  @  33 : 34

~ 	private final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  4  @  4 : 5

> DELETE  27  @  28 : 29

> CHANGE  95 : 96  @  96 : 99

~ 		this.overlayDebug.renderDebugInfo(scaledresolution, partialTicks);

> INSERT  88 : 91  @  90

+ 		if (this.mc.gameSettings.hudWorld && (mc.currentScreen == null || !(mc.currentScreen instanceof GuiChat))) {
+ 			j -= 10;
+ 		}

> DELETE  169  @  166 : 170

> CHANGE  17 : 18  @  21 : 22

~ 		for (Score score : (List<Score>) arraylist1) {

> CHANGE  13 : 14  @  13 : 14

~ 		for (Score score1 : (List<Score>) arraylist1) {

> CHANGE  344 : 345  @  344 : 345

~ 		EaglerTextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes()

> DELETE  58  @  58 : 59

> EOF

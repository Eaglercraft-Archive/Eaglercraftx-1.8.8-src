
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

> CHANGE  11 : 14  @  5 : 8

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  16  @  10 : 19

> DELETE  18  @  21 : 22

> DELETE  19  @  23 : 24

> CHANGE  51 : 52  @  56 : 57

~ 	private final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  55  @  60 : 61

> DELETE  82  @  88 : 89

> CHANGE  177 : 178  @  184 : 187

~ 		this.overlayDebug.renderDebugInfo(scaledresolution, partialTicks);

> INSERT  265 : 268  @  274

+ 		if (this.mc.gameSettings.hudWorld && (mc.currentScreen == null || !(mc.currentScreen instanceof GuiChat))) {
+ 			j -= 10;
+ 		}

> DELETE  434  @  440 : 444

> CHANGE  451 : 452  @  461 : 462

~ 		for (Score score : (List<Score>) arraylist1) {

> CHANGE  464 : 465  @  474 : 475

~ 		for (Score score1 : (List<Score>) arraylist1) {

> CHANGE  808 : 809  @  818 : 819

~ 		EaglerTextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes()

> DELETE  866  @  876 : 877

> EOF

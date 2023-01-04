
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  6  @  6 : 7

> DELETE  1  @  1 : 2

> DELETE  10  @  10 : 11

> CHANGE  43 : 44  @  43 : 44

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  8 : 10  @  8 : 16

~ 	protected int getCloseKey() {
~ 		return this.mc.gameSettings.keyBindInventory.getKeyCode();

> CHANGE  76 : 77  @  76 : 77

~ 			GlStateManager.disableLighting();

> INSERT  61 : 65  @  61

+ 		GlStateManager.enableDepth();
+ 		GlStateManager.clearDepth(0.0f);
+ 		GlStateManager.clear(256);
+ 		GlStateManager.clearDepth(1.0f);

> CHANGE  17 : 18  @  17 : 18

~ 		EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  8 : 10  @  8 : 9

~ 				random.setSeed(
~ 						(long) (this.mc.getSession().getProfile().getId().hashCode() + k1 + l2 + (l1 + k2) * 16));

> CHANGE  1 : 2  @  1 : 2

~ 				EaglerTextureAtlasSprite textureatlassprite = this.func_175371_a(Blocks.sand);

> DELETE  26  @  26 : 27

> CHANGE  158 : 159  @  158 : 159

~ 		GlStateManager.disableBlend();

> CHANGE  3 : 4  @  3 : 4

~ 	private EaglerTextureAtlasSprite func_175371_a(Block parBlock) {

> EOF

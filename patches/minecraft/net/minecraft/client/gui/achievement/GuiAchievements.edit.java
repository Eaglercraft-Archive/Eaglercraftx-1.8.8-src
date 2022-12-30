
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  11  @  8 : 9

> DELETE  1  @  2 : 3

> DELETE  10  @  11 : 12

> CHANGE  43 : 44  @  44 : 45

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  9 : 11  @  9 : 17

~ 	protected int getCloseKey() {
~ 		return this.mc.gameSettings.keyBindInventory.getKeyCode();

> CHANGE  78 : 79  @  84 : 85

~ 			GlStateManager.disableLighting();

> INSERT  62 : 66  @  62

+ 		GlStateManager.enableDepth();
+ 		GlStateManager.clearDepth(0.0f);
+ 		GlStateManager.clear(256);
+ 		GlStateManager.clearDepth(1.0f);

> CHANGE  21 : 22  @  17 : 18

~ 		EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  9 : 11  @  9 : 10

~ 				random.setSeed(
~ 						(long) (this.mc.getSession().getProfile().getId().hashCode() + k1 + l2 + (l1 + k2) * 16));

> CHANGE  3 : 4  @  2 : 3

~ 				EaglerTextureAtlasSprite textureatlassprite = this.func_175371_a(Blocks.sand);

> DELETE  27  @  27 : 28

> CHANGE  158 : 159  @  159 : 160

~ 		GlStateManager.disableBlend();

> CHANGE  4 : 5  @  4 : 5

~ 	private EaglerTextureAtlasSprite func_175371_a(Block parBlock) {

> EOF


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

> DELETE  13  @  10 : 11

> DELETE  14  @  12 : 13

> DELETE  24  @  23 : 24

> CHANGE  67 : 68  @  67 : 68

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  76 : 78  @  76 : 84

~ 	protected int getCloseKey() {
~ 		return this.mc.gameSettings.keyBindInventory.getKeyCode();

> CHANGE  154 : 155  @  160 : 161

~ 			GlStateManager.disableLighting();

> INSERT  216 : 220  @  222

+ 		GlStateManager.enableDepth();
+ 		GlStateManager.clearDepth(0.0f);
+ 		GlStateManager.clear(256);
+ 		GlStateManager.clearDepth(1.0f);

> CHANGE  237 : 238  @  239 : 240

~ 		EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  246 : 248  @  248 : 249

~ 				random.setSeed(
~ 						(long) (this.mc.getSession().getProfile().getId().hashCode() + k1 + l2 + (l1 + k2) * 16));

> CHANGE  249 : 250  @  250 : 251

~ 				EaglerTextureAtlasSprite textureatlassprite = this.func_175371_a(Blocks.sand);

> DELETE  276  @  277 : 278

> CHANGE  434 : 435  @  436 : 437

~ 		GlStateManager.disableBlend();

> CHANGE  438 : 439  @  440 : 441

~ 	private EaglerTextureAtlasSprite func_175371_a(Block parBlock) {

> EOF

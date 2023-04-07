
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> CHANGE  47 : 48  @  47 : 48

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom random, int i) {

> INSERT  101 : 105  @  101

+ 	public boolean eaglerShadersShouldRenderGlassHighlights() {
+ 		return this == Blocks.glass_pane && DeferredStateManager.isRenderingGlassHighlights();
+ 	}
+ 

> EOF

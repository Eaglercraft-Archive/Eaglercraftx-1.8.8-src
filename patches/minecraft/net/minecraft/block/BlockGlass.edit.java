
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> CHANGE  10 : 11  @  10 : 11

~ 	public int quantityDropped(EaglercraftRandom var1) {

> INSERT  14 : 18  @  14

+ 
+ 	public boolean eaglerShadersShouldRenderGlassHighlights() {
+ 		return DeferredStateManager.isRenderingGlassHighlights();
+ 	}

> EOF

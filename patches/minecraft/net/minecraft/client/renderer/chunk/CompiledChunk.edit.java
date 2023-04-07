
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 3

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> CHANGE  15 : 16  @  15 : 16

~ 			return true;

> INSERT  8 : 9  @  8

+ 	private WorldRenderer.State stateWater;

> INSERT  45 : 53  @  45

+ 
+ 	public WorldRenderer.State getStateRealisticWater() {
+ 		return this.stateWater;
+ 	}
+ 
+ 	public void setStateRealisticWater(WorldRenderer.State stateIn) {
+ 		this.stateWater = stateIn;
+ 	}

> EOF

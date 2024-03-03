
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> CHANGE  3 : 4  @  3 : 4

~ 	private final WorldRenderer[] worldRenderers = new WorldRenderer[EnumWorldBlockLayer._VALUES.length];

> INSERT  6 : 8  @  6

+ 		this.worldRenderers[EnumWorldBlockLayer.REALISTIC_WATER.ordinal()] = new WorldRenderer(262145);
+ 		this.worldRenderers[EnumWorldBlockLayer.GLASS_HIGHLIGHTS.ordinal()] = new WorldRenderer(131072);

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  2 : 6  @  3 : 5

~ 
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> CHANGE  20 : 21  @  18 : 19

~ 	public EaglerTextureAtlasSprite getParticleIcon(Item item) {

> CHANGE  4 : 5  @  4 : 5

~ 	public EaglerTextureAtlasSprite getParticleIcon(Item item, int meta) {

> CHANGE  50 : 51  @  50 : 51

~ 			this.simpleShapesCache.put((Integer) entry.getKey(),

> EOF

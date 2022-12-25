
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  4 : 8  @  5 : 7

~ 
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> CHANGE  24 : 25  @  23 : 24

~ 	public EaglerTextureAtlasSprite getParticleIcon(Item item) {

> CHANGE  28 : 29  @  27 : 28

~ 	public EaglerTextureAtlasSprite getParticleIcon(Item item, int meta) {

> CHANGE  78 : 79  @  77 : 78

~ 			this.simpleShapesCache.put((Integer) entry.getKey(),

> EOF

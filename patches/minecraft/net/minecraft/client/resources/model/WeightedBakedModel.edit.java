
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  2 : 7  @  2

+ 
+ import com.google.common.collect.ComparisonChain;
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> DELETE  2  @  2 : 4

> CHANGE  34 : 35  @  34 : 35

~ 	public EaglerTextureAtlasSprite getParticleTexture() {

> CHANGE  47 : 50  @  47 : 49

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			for (int j = 0; j < facings.length; ++j) {
~ 				i += this.model.getFaceQuads(facings[j]).size();

> EOF

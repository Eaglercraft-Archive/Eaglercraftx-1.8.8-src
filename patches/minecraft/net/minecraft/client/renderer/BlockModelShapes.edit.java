
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 7  @  3

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> DELETE  45  @  45 : 46

> CHANGE  21 : 22  @  21 : 22

~ 	public EaglerTextureAtlasSprite getTexture(IBlockState state) {

> CHANGE  54 : 55  @  54 : 55

~ 			this.bakedModelStore.put((IBlockState) entry.getKey(),

> CHANGE  175 : 176  @  175 : 176

~ 				String s = BlockDirt.VARIANT.getName((BlockDirt.DirtType) linkedhashmap.remove(BlockDirt.VARIANT));

> CHANGE  10 : 12  @  10 : 11

~ 				String s = BlockStoneSlab.VARIANT
~ 						.getName((BlockStoneSlab.EnumType) linkedhashmap.remove(BlockStoneSlab.VARIANT));

> CHANGE  9 : 10  @  9 : 10

~ 						.getName((BlockStoneSlabNew.EnumType) linkedhashmap.remove(BlockStoneSlabNew.VARIANT));

> EOF

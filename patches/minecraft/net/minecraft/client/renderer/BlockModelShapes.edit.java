
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 7  @  4

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> DELETE  49  @  45 : 46

> CHANGE  21 : 22  @  22 : 23

~ 	public EaglerTextureAtlasSprite getTexture(IBlockState state) {

> CHANGE  55 : 56  @  55 : 56

~ 			this.bakedModelStore.put((IBlockState) entry.getKey(),

> CHANGE  176 : 177  @  176 : 177

~ 				String s = BlockDirt.VARIANT.getName((BlockDirt.DirtType) linkedhashmap.remove(BlockDirt.VARIANT));

> CHANGE  11 : 13  @  11 : 12

~ 				String s = BlockStoneSlab.VARIANT
~ 						.getName((BlockStoneSlab.EnumType) linkedhashmap.remove(BlockStoneSlab.VARIANT));

> CHANGE  11 : 12  @  10 : 11

~ 						.getName((BlockStoneSlabNew.EnumType) linkedhashmap.remove(BlockStoneSlabNew.VARIANT));

> EOF

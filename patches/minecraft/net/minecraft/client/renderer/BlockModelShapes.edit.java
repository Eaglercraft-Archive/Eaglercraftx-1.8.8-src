
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  5 : 9  @  6

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> DELETE  54  @  51 : 52

> CHANGE  75 : 76  @  73 : 74

~ 	public EaglerTextureAtlasSprite getTexture(IBlockState state) {

> CHANGE  130 : 131  @  128 : 129

~ 			this.bakedModelStore.put((IBlockState) entry.getKey(),

> CHANGE  306 : 307  @  304 : 305

~ 				String s = BlockDirt.VARIANT.getName((BlockDirt.DirtType) linkedhashmap.remove(BlockDirt.VARIANT));

> CHANGE  317 : 319  @  315 : 316

~ 				String s = BlockStoneSlab.VARIANT
~ 						.getName((BlockStoneSlab.EnumType) linkedhashmap.remove(BlockStoneSlab.VARIANT));

> CHANGE  328 : 329  @  325 : 326

~ 						.getName((BlockStoneSlabNew.EnumType) linkedhashmap.remove(BlockStoneSlabNew.VARIANT));

> EOF

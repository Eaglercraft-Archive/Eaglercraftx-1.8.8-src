
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  17 : 18  @  17 : 19

~ 	public static PropertyEnum<BlockStoneSlab.EnumType> VARIANT;

> CHANGE  14 : 19  @  14 : 15

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockStoneSlab.EnumType>create("variant", BlockStoneSlab.EnumType.class);
~ 	}
~ 
~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  21 : 24  @  21 : 22

~ 			BlockStoneSlab.EnumType[] types = BlockStoneSlab.EnumType.META_LOOKUP;
~ 			for (int i = 0; i < types.length; ++i) {
~ 				BlockStoneSlab.EnumType blockstoneslab$enumtype = types[i];

> CHANGE  55 : 56  @  55 : 56

~ 		public static final BlockStoneSlab.EnumType[] META_LOOKUP = new BlockStoneSlab.EnumType[8];

> CHANGE  45 : 48  @  45 : 47

~ 			BlockStoneSlab.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF

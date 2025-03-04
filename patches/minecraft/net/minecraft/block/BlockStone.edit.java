
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  14 : 15  @  14 : 16

~ 	public static PropertyEnum<BlockStone.EnumType> VARIANT;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStone.EnumType>create("variant", BlockStone.EnumType.class);
+ 	}
+ 

> CHANGE  9 : 10  @  9 : 10

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> CHANGE  9 : 12  @  9 : 11

~ 		BlockStone.EnumType[] types = BlockStone.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  24 : 25  @  24 : 25

~ 		public static final BlockStone.EnumType[] META_LOOKUP = new BlockStone.EnumType[7];

> CHANGE  45 : 48  @  45 : 47

~ 			BlockStone.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  22 : 24  @  22 : 26

~ 	public static PropertyEnum<BlockDoublePlant.EnumPlantType> VARIANT;
~ 	public static PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF;

> INSERT  13 : 18  @  13

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockDoublePlant.EnumPlantType>create("variant", BlockDoublePlant.EnumPlantType.class);
+ 		HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);
+ 	}
+ 

> CHANGE  60 : 61  @  60 : 61

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom random, int var3) {

> CHANGE  98 : 101  @  98 : 100

~ 		BlockDoublePlant.EnumPlantType[] types = BlockDoublePlant.EnumPlantType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMeta()));

> CHANGE  14 : 15  @  14 : 15

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState var4) {

> CHANGE  50 : 51  @  50 : 51

~ 		private static final BlockDoublePlant.EnumPlantType[] META_LOOKUP = new BlockDoublePlant.EnumPlantType[6];

> CHANGE  39 : 42  @  39 : 41

~ 			BlockDoublePlant.EnumPlantType[] types = BlockDoublePlant.EnumPlantType.values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMeta()] = types[i];

> EOF

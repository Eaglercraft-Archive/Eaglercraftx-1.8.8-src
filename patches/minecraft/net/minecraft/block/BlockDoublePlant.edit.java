
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  17  @  21 : 23

> CHANGE  25 : 27  @  31 : 35

~ 	public static PropertyEnum<BlockDoublePlant.EnumPlantType> VARIANT;
~ 	public static PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF;

> INSERT  40 : 45  @  48

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockDoublePlant.EnumPlantType>create("variant", BlockDoublePlant.EnumPlantType.class);
+ 		HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);
+ 	}
+ 

> CHANGE  105 : 106  @  108 : 109

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom random, int var3) {

> DELETE  145  @  148 : 158

> DELETE  155  @  168 : 176

> DELETE  169  @  190 : 206

> CHANGE  186 : 187  @  223 : 224

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  190 : 191  @  227 : 228

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState var4) {

> EOF

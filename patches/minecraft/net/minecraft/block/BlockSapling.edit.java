
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 10

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  11  @  16 : 17

> DELETE  16  @  22 : 31

> CHANGE  18 : 19  @  33 : 35

~ 	public static PropertyEnum<BlockPlanks.EnumType> TYPE;

> INSERT  29 : 33  @  45

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  38 : 39  @  50 : 58

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> DELETE  41  @  60 : 161

> CHANGE  69 : 70  @  189 : 190

~ 	public boolean canUseBonemeal(World world, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> DELETE  73  @  193 : 197

> EOF

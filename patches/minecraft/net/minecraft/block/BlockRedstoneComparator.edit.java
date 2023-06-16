
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.base.Predicate;
~ 

> CHANGE  25 : 26  @  25 : 27

~ 	public static PropertyEnum<BlockRedstoneComparator.Mode> MODE;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		MODE = PropertyEnum.<BlockRedstoneComparator.Mode>create("mode", BlockRedstoneComparator.Mode.class);
+ 	}
+ 

> CHANGE  4 : 5  @  4 : 5

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  148 : 149  @  148 : 149

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {

> EOF

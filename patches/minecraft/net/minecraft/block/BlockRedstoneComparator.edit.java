
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 7  @  4 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.base.Predicate;
~ 

> CHANGE  32 : 33  @  33 : 35

~ 	public static PropertyEnum<BlockRedstoneComparator.Mode> MODE;

> INSERT  42 : 46  @  44

+ 	public static void bootstrapStates() {
+ 		MODE = PropertyEnum.<BlockRedstoneComparator.Mode>create("mode", BlockRedstoneComparator.Mode.class);
+ 	}
+ 

> CHANGE  50 : 51  @  48 : 49

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  199 : 200  @  197 : 198

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {

> EOF

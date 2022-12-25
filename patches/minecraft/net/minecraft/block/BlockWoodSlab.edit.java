
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  19 : 20  @  20 : 22

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  32 : 36  @  34

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  40 : 41  @  38 : 39

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF

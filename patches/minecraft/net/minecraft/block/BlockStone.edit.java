
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  19 : 20  @  19 : 21

~ 	public static PropertyEnum<BlockStone.EnumType> VARIANT;

> INSERT  27 : 31  @  28

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStone.EnumType>create("variant", BlockStone.EnumType.class);
+ 	}
+ 

> CHANGE  40 : 41  @  37 : 38

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF

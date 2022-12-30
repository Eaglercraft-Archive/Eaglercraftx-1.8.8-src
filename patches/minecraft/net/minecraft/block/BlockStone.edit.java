
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  16 : 17  @  16 : 18

~ 	public static PropertyEnum<BlockStone.EnumType> VARIANT;

> INSERT  8 : 12  @  9

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStone.EnumType>create("variant", BlockStone.EnumType.class);
+ 	}
+ 

> CHANGE  13 : 14  @  9 : 10

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF

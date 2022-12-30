
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  8  @  11 : 12

> CHANGE  14 : 15  @  15 : 16

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		TYPE = BlockPistonExtension.TYPE;
+ 	}
+ 

> CHANGE  49 : 50  @  45 : 56

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> DELETE  4  @  14 : 24

> DELETE  4  @  14 : 21

> EOF

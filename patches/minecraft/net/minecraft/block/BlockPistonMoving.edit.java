
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  6  @  6 : 7

> CHANGE  14 : 15  @  14 : 15

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  8 : 12  @  8

+ 	public static void bootstrapStates() {
+ 		TYPE = BlockPistonExtension.TYPE;
+ 	}
+ 

> CHANGE  45 : 46  @  45 : 56

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> DELETE  3  @  3 : 13

> DELETE  4  @  4 : 11

> EOF

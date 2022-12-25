
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  10  @  13 : 14

> CHANGE  24 : 25  @  28 : 29

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  33 : 37  @  37

+ 	public static void bootstrapStates() {
+ 		TYPE = BlockPistonExtension.TYPE;
+ 	}
+ 

> CHANGE  82 : 83  @  82 : 93

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> DELETE  86  @  96 : 106

> DELETE  90  @  110 : 117

> EOF

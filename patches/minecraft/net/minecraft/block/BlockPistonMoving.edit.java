
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  21 : 22  @  21 : 22

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  8 : 12  @  8

+ 	public static void bootstrapStates() {
+ 		TYPE = BlockPistonExtension.TYPE;
+ 	}
+ 

> CHANGE  55 : 56  @  55 : 56

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> DELETE  21  @  21 : 22

> EOF

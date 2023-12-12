
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  32 : 33  @  32 : 33

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  8 : 12  @  8

+ 	public static void bootstrapStates() {
+ 		TYPE = BlockPistonExtension.TYPE;
+ 	}
+ 

> CHANGE  47 : 48  @  47 : 48

~ 		if (world.getTileEntity(blockpos) == null) {

> CHANGE  7 : 8  @  7 : 8

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  4 : 5  @  4 : 5

~ 		{

> CHANGE  13 : 14  @  13 : 17

~ 		world.getTileEntity(blockpos);

> EOF

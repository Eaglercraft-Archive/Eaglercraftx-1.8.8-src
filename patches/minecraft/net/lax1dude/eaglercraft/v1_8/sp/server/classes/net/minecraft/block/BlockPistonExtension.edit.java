
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  29 : 30  @  29 : 31

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  11 : 16  @  11

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPistonExtension.EnumPistonType>create("type",
+ 				BlockPistonExtension.EnumPistonType.class);
+ 	}
+ 

> CHANGE  44 : 45  @  44 : 45

~ 	public int quantityDropped(EaglercraftRandom var1) {

> EOF

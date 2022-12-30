
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  22 : 23  @  23 : 25

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  12 : 17  @  13

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPistonExtension.EnumPistonType>create("type",
+ 				BlockPistonExtension.EnumPistonType.class);
+ 	}
+ 

> CHANGE  49 : 50  @  44 : 45

~ 	public int quantityDropped(EaglercraftRandom var1) {

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  25 : 26  @  26 : 28

~ 	public static PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE;

> INSERT  37 : 42  @  39

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPistonExtension.EnumPistonType>create("type",
+ 				BlockPistonExtension.EnumPistonType.class);
+ 	}
+ 

> CHANGE  86 : 87  @  83 : 84

~ 	public int quantityDropped(EaglercraftRandom var1) {

> EOF

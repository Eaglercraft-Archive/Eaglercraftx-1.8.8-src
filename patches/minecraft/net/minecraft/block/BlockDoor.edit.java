
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  25 : 26  @  25 : 27

~ 	public static PropertyEnum<BlockDoor.EnumHingePosition> HINGE;

> CHANGE  1 : 2  @  1 : 3

~ 	public static PropertyEnum<BlockDoor.EnumDoorHalf> HALF;

> INSERT  8 : 13  @  8

+ 	public static void bootstrapStates() {
+ 		HINGE = PropertyEnum.<BlockDoor.EnumHingePosition>create("hinge", BlockDoor.EnumHingePosition.class);
+ 		HALF = PropertyEnum.<BlockDoor.EnumDoorHalf>create("half", BlockDoor.EnumDoorHalf.class);
+ 	}
+ 

> CHANGE  135 : 136  @  135 : 140

~ 			if (!flag1) {

> CHANGE  15 : 16  @  15 : 16

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF

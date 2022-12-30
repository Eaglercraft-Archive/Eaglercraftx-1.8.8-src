
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  27 : 28  @  27 : 29

~ 	public static PropertyEnum<BlockDoor.EnumHingePosition> HINGE;

> CHANGE  2 : 3  @  3 : 5

~ 	public static PropertyEnum<BlockDoor.EnumDoorHalf> HALF;

> INSERT  9 : 14  @  10

+ 	public static void bootstrapStates() {
+ 		HINGE = PropertyEnum.<BlockDoor.EnumHingePosition>create("hinge", BlockDoor.EnumHingePosition.class);
+ 		HALF = PropertyEnum.<BlockDoor.EnumDoorHalf>create("half", BlockDoor.EnumDoorHalf.class);
+ 	}
+ 

> CHANGE  140 : 141  @  135 : 140

~ 			if (!flag1) {

> CHANGE  16 : 17  @  20 : 21

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF

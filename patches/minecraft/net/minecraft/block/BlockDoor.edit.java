
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  29 : 30  @  29 : 31

~ 	public static PropertyEnum<BlockDoor.EnumHingePosition> HINGE;

> CHANGE  31 : 32  @  32 : 34

~ 	public static PropertyEnum<BlockDoor.EnumDoorHalf> HALF;

> INSERT  40 : 45  @  42

+ 	public static void bootstrapStates() {
+ 		HINGE = PropertyEnum.<BlockDoor.EnumHingePosition>create("hinge", BlockDoor.EnumHingePosition.class);
+ 		HALF = PropertyEnum.<BlockDoor.EnumDoorHalf>create("half", BlockDoor.EnumDoorHalf.class);
+ 	}
+ 

> CHANGE  180 : 181  @  177 : 182

~ 			if (!flag1) {

> CHANGE  196 : 197  @  197 : 198

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF

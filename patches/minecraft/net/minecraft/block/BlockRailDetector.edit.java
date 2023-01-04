
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 3

~ 
~ import com.google.common.base.Predicate;
~ 

> CHANGE  18 : 19  @  18 : 27

~ 	public static PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE;

> INSERT  9 : 21  @  9

+ 	public static void bootstrapStates() {
+ 		SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class,
+ 				new Predicate<BlockRailBase.EnumRailDirection>() {
+ 					public boolean apply(BlockRailBase.EnumRailDirection blockrailbase$enumraildirection) {
+ 						return blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_EAST
+ 								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_WEST
+ 								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_EAST
+ 								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_WEST;
+ 					}
+ 				});
+ 	}
+ 

> DELETE  8  @  8 : 25

> EOF

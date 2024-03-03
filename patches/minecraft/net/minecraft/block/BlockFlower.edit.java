
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import java.util.Collection;
+ import java.util.List;
+ 

> CHANGE  3 : 4  @  3 : 7

~ 

> CHANGE  24 : 27  @  24 : 27

~ 		BlockFlower.EnumFlowerType[] flowerTypes = BlockFlower.EnumFlowerType.getTypes(this.getBlockType());
~ 		for (int i = 0; i < flowerTypes.length; ++i) {
~ 			list.add(new ItemStack(item, 1, flowerTypes[i].getMeta()));

> CHANGE  56 : 59  @  56 : 58

~ 		public static final BlockFlower.EnumFlowerType[] _VALUES = EnumFlowerType.values();
~ 
~ 		private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[_VALUES.length][];

> CHANGE  50 : 53  @  50 : 51

~ 			BlockFlower.EnumFlowerColor[] colors = BlockFlower.EnumFlowerColor.values();
~ 			for (int i = 0; i < colors.length; ++i) {
~ 				final BlockFlower.EnumFlowerColor blockflower$enumflowercolor = colors[i];

> EOF

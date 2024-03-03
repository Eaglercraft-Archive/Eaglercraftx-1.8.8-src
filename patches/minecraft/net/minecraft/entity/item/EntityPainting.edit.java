
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Lists;
+ 

> CHANGE  21 : 24  @  21 : 23

~ 		EntityPainting.EnumArt[] types = EntityPainting.EnumArt._VALUES;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			this.art = types[i];

> CHANGE  2 : 3  @  2 : 3

~ 				arraylist.add(types[i]);

> CHANGE  13 : 16  @  13 : 14

~ 		EntityPainting.EnumArt[] types = EntityPainting.EnumArt._VALUES;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			EntityPainting.EnumArt entitypainting$enumart = types[i];

> CHANGE  17 : 20  @  17 : 18

~ 		EntityPainting.EnumArt[] types = EntityPainting.EnumArt._VALUES;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			EntityPainting.EnumArt entitypainting$enumart = types[i];

> INSERT  56 : 58  @  56

+ 		public static final EnumArt[] _VALUES = values();
+ 

> EOF

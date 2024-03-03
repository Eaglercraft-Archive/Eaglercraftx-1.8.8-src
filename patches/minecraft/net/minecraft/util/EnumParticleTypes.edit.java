
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  3 : 6  @  3

+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> INSERT  17 : 19  @  17

+ 	public static final EnumParticleTypes[] _VALUES = values();
+ 

> CHANGE  49 : 52  @  49 : 50

~ 		EnumParticleTypes[] types = values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			EnumParticleTypes enumparticletypes = types[i];

> EOF

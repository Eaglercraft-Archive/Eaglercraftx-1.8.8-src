
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> INSERT  1 : 5  @  1

+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ import com.google.common.collect.Lists;
+ 

> INSERT  17 : 19  @  17

+ 	public static final EnumParticleTypes[] _VALUES = values();
+ 

> CHANGE  4 : 5  @  4 : 5

~ 	private static final IntObjectMap<EnumParticleTypes> PARTICLES = new IntObjectHashMap<>();

> CHANGE  38 : 39  @  38 : 39

~ 		return PARTICLES.get(Integer.valueOf(particleId));

> CHANGE  5 : 8  @  5 : 6

~ 		EnumParticleTypes[] types = values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			EnumParticleTypes enumparticletypes = types[i];

> EOF

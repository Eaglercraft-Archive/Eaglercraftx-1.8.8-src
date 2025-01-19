
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  6 : 7  @  6 : 7

~ 	protected EaglercraftRandom rand;

> INSERT  2 : 10  @  2

+ 	public MapGenBase() {
+ 		this(true);
+ 	}
+ 
+ 	public MapGenBase(boolean scramble) {
+ 		rand = new EaglercraftRandom(scramble);
+ 	}
+ 

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.base.Predicate;
~ 

> DELETE  12  @  9 : 10

> CHANGE  90 : 92  @  91 : 93

~ 	public EaglercraftRandom getRandomWithSeed(long seed) {
~ 		return new EaglercraftRandom(this.getWorld().getSeed() + (long) (this.xPosition * this.xPosition * 4987142)

> EOF

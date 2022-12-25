
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 7  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.base.Predicate;
~ 

> DELETE  15  @  13 : 14

> CHANGE  105 : 107  @  104 : 106

~ 	public EaglercraftRandom getRandomWithSeed(long seed) {
~ 		return new EaglercraftRandom(this.getWorld().getSeed() + (long) (this.xPosition * this.xPosition * 4987142)

> EOF

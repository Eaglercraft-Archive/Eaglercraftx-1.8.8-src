
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.base.Predicate;
~ 

> DELETE  8  @  8 : 9

> CHANGE  90 : 96  @  90 : 94

~ 	public EaglercraftRandom getRandomWithSeed(long seed) {
~ 		return new EaglercraftRandom(
~ 				this.getWorld().getSeed() + (long) (this.xPosition * this.xPosition * 4987142)
~ 						+ (long) (this.xPosition * 5947611) + (long) (this.zPosition * this.zPosition) * 4392871L
~ 						+ (long) (this.zPosition * 389711) ^ seed,
~ 				!this.getWorld().getWorldInfo().isOldEaglercraftRandom());

> EOF

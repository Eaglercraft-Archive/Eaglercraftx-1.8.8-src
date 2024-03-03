
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ 

> CHANGE  4 : 5  @  4 : 5

~ 	private final EaglercraftRandom rnd = new EaglercraftRandom();

> CHANGE  16 : 18  @  16 : 18

~ 		for (int j = 0, l = this.soundPool.size(); j < l; ++j) {
~ 			i += this.soundPool.get(j).getWeight();

> CHANGE  10 : 12  @  10 : 11

~ 			for (int k = 0, l = this.soundPool.size(); k < l; ++k) {
~ 				ISoundEventAccessor isoundeventaccessor = this.soundPool.get(k);

> EOF

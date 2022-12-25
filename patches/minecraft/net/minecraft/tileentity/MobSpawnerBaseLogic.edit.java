
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  4

+ 
+ import com.google.common.collect.Lists;
+ 

> DELETE  15  @  13 : 14

> CHANGE  62 : 70  @  61 : 131

~ 			double d3 = (double) ((float) blockpos.getX() + this.getSpawnerWorld().rand.nextFloat());
~ 			double d4 = (double) ((float) blockpos.getY() + this.getSpawnerWorld().rand.nextFloat());
~ 			double d5 = (double) ((float) blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat());
~ 			this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D,
~ 					new int[0]);
~ 			this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
~ 			if (this.spawnDelay > 0) {
~ 				--this.spawnDelay;

> INSERT  72 : 74  @  133

+ 			this.prevMobRotation = this.mobRotation;
+ 			this.mobRotation = (this.mobRotation + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0D;

> CHANGE  230 : 231  @  289 : 290

~ 		if (delay == 1) {

> EOF

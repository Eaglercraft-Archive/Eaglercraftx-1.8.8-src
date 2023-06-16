
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Lists;
+ 

> DELETE  9  @  9 : 10

> CHANGE  47 : 55  @  47 : 117

~ 			double d3 = (double) ((float) blockpos.getX() + this.getSpawnerWorld().rand.nextFloat());
~ 			double d4 = (double) ((float) blockpos.getY() + this.getSpawnerWorld().rand.nextFloat());
~ 			double d5 = (double) ((float) blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat());
~ 			this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D,
~ 					new int[0]);
~ 			this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
~ 			if (this.spawnDelay > 0) {
~ 				--this.spawnDelay;

> INSERT  2 : 4  @  2

+ 			this.prevMobRotation = this.mobRotation;
+ 			this.mobRotation = (this.mobRotation + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0D;

> CHANGE  156 : 157  @  156 : 157

~ 		if (delay == 1) {

> EOF

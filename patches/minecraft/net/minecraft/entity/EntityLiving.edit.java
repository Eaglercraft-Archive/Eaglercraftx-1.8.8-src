
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 

> DELETE  1  @  1 : 13

> DELETE  17  @  17 : 20

> DELETE  7  @  7 : 8

> DELETE  4  @  4 : 7

> DELETE  1  @  1 : 4

> DELETE  1  @  1 : 2

> DELETE  10  @  10 : 16

> DELETE  1  @  1 : 8

> DELETE  7  @  7 : 31

> CHANGE  61 : 71  @  61 : 76

~ 		for (int i = 0; i < 20; ++i) {
~ 			double d0 = this.rand.nextGaussian() * 0.02D;
~ 			double d1 = this.rand.nextGaussian() * 0.02D;
~ 			double d2 = this.rand.nextGaussian() * 0.02D;
~ 			double d3 = 10.0D;
~ 			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
~ 					this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * d3,
~ 					this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * d3,
~ 					this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3,
~ 					d0, d1, d2, new int[0]);

> DELETE  1  @  1 : 2

> DELETE  11  @  11 : 19

> DELETE  113  @  113 : 129

> DELETE  95  @  95 : 123

> DELETE  328  @  328 : 336

> DELETE  19  @  19 : 24

> CHANGE  5 : 7  @  5 : 6

~ 				EaglercraftUUID uuid = new EaglercraftUUID(this.leashNBTTag.getLong("UUIDMost"),
~ 						this.leashNBTTag.getLong("UUIDLeast"));

> EOF

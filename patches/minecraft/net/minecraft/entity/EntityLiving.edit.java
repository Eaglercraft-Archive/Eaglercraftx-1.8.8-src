
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 

> DELETE  3  @  2 : 14

> DELETE  17  @  29 : 32

> DELETE  7  @  10 : 11

> DELETE  4  @  5 : 8

> DELETE  1  @  4 : 7

> DELETE  1  @  4 : 5

> DELETE  10  @  11 : 17

> DELETE  1  @  7 : 14

> DELETE  7  @  14 : 38

> CHANGE  61 : 71  @  85 : 100

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

> DELETE  11  @  16 : 17

> DELETE  11  @  12 : 20

> DELETE  113  @  121 : 137

> DELETE  95  @  111 : 139

> DELETE  328  @  356 : 364

> DELETE  19  @  27 : 32

> CHANGE  5 : 7  @  10 : 11

~ 				EaglercraftUUID uuid = new EaglercraftUUID(this.leashNBTTag.getLong("UUIDMost"),
~ 						this.leashNBTTag.getLong("UUIDLeast"));

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 

> DELETE  5  @  4 : 16

> DELETE  22  @  33 : 36

> DELETE  29  @  43 : 44

> DELETE  33  @  48 : 51

> DELETE  34  @  52 : 55

> DELETE  35  @  56 : 57

> DELETE  45  @  67 : 73

> DELETE  46  @  74 : 81

> DELETE  53  @  88 : 112

> CHANGE  114 : 124  @  173 : 188

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

> DELETE  125  @  189 : 190

> DELETE  136  @  201 : 209

> DELETE  249  @  322 : 338

> DELETE  344  @  433 : 461

> DELETE  672  @  789 : 797

> DELETE  691  @  816 : 821

> CHANGE  696 : 698  @  826 : 827

~ 				EaglercraftUUID uuid = new EaglercraftUUID(this.leashNBTTag.getLong("UUIDMost"),
~ 						this.leashNBTTag.getLong("UUIDLeast"));

> EOF

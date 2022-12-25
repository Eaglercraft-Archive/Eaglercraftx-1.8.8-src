
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 13

> DELETE  20  @  28 : 35

> CHANGE  84 : 91  @  99 : 116

~ 		for (int i = 0; i < 2; ++i) {
~ 			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
~ 					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					this.posY + this.rand.nextDouble() * (double) this.height,
~ 					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);

> DELETE  92  @  117 : 118

> EOF

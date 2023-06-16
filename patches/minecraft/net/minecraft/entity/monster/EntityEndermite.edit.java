
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 13

> DELETE  15  @  15 : 22

> CHANGE  64 : 71  @  64 : 81

~ 		for (int i = 0; i < 2; ++i) {
~ 			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
~ 					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					this.posY + this.rand.nextDouble() * (double) this.height,
~ 					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);

> DELETE  1  @  1 : 2

> EOF

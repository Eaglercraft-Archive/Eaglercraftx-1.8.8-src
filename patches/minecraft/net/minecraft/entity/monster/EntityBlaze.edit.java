
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  4  @  4 : 14

> DELETE  2  @  2 : 3

> DELETE  2  @  2 : 3

> DELETE  10  @  10 : 17

> CHANGE  39 : 43  @  39 : 44

~ 		if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
~ 			this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire",
~ 					1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
~ 		}

> CHANGE  1 : 6  @  1 : 8

~ 		for (int i = 0; i < 2; ++i) {
~ 			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
~ 					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					this.posY + this.rand.nextDouble() * (double) this.height,
~ 					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D, new int[0]);

> DELETE  67  @  67 : 145

> EOF

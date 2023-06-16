
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  24  @  24 : 47

> CHANGE  12 : 13  @  12 : 13

~ 		return this.dataWatcher.getWatchableObjectByte(12);

> CHANGE  52 : 59  @  52 : 63

~ 		if (this.field_175503_c > 0) {
~ 			if (this.field_175503_c % 4 == 0) {
~ 				this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
~ 						this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
~ 						this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
~ 						this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D,
~ 						0.0D, 0.0D, new int[0]);

> CHANGE  2 : 3  @  2 : 15

~ 			--this.field_175503_c;

> INSERT  2 : 3  @  2

+ 		this.setScaleForAge(this.isChild());

> EOF

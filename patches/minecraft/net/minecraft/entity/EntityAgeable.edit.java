
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  24  @  26 : 49

> CHANGE  12 : 13  @  35 : 36

~ 		return this.dataWatcher.getWatchableObjectByte(12);

> CHANGE  53 : 60  @  53 : 64

~ 		if (this.field_175503_c > 0) {
~ 			if (this.field_175503_c % 4 == 0) {
~ 				this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
~ 						this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
~ 						this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
~ 						this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D,
~ 						0.0D, 0.0D, new int[0]);

> CHANGE  9 : 10  @  13 : 26

~ 			--this.field_175503_c;

> INSERT  3 : 4  @  15

+ 		this.setScaleForAge(this.isChild());

> EOF

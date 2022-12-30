
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  1 : 2  @  4 : 7

~ 

> DELETE  3  @  5 : 6

> DELETE  2  @  3 : 5

> DELETE  1  @  3 : 4

> DELETE  2  @  3 : 6

> DELETE  2  @  5 : 6

> DELETE  2  @  3 : 5

> CHANGE  71 : 76  @  73 : 80

~ 		float f = MathHelper.cos(this.animTime * 3.1415927F * 2.0F);
~ 		float f1 = MathHelper.cos(this.prevAnimTime * 3.1415927F * 2.0F);
~ 		if (f1 <= -0.3F && f >= -0.3F && !this.isSilent()) {
~ 			this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F,
~ 					0.8F + this.rand.nextFloat() * 0.3F, false);

> CHANGE  43 : 56  @  45 : 139

~ 				if (this.newPosRotationIncrements > 0) {
~ 					double d10 = this.posX + (this.newPosX - this.posX) / (double) this.newPosRotationIncrements;
~ 					double d0 = this.posY + (this.newPosY - this.posY) / (double) this.newPosRotationIncrements;
~ 					double d1 = this.posZ + (this.newPosZ - this.posZ) / (double) this.newPosRotationIncrements;
~ 					double d2 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double) this.rotationYaw);
~ 					this.rotationYaw = (float) ((double) this.rotationYaw
~ 							+ d2 / (double) this.newPosRotationIncrements);
~ 					this.rotationPitch = (float) ((double) this.rotationPitch
~ 							+ (this.newRotationPitch - (double) this.rotationPitch)
~ 									/ (double) this.newPosRotationIncrements);
~ 					--this.newPosRotationIncrements;
~ 					this.setPosition(d10, d0, d1);
~ 					this.setRotation(this.rotationYaw, this.rotationPitch);

> DELETE  42  @  123 : 133

> DELETE  37  @  47 : 52

> DELETE  7  @  12 : 17

> CHANGE  13 : 14  @  18 : 19

~ 			for (EntityEnderCrystal entityendercrystal1 : (List<EntityEnderCrystal>) list) {

> DELETE  13  @  13 : 75

> DELETE  4  @  66 : 107

> DELETE  45  @  86 : 104

> DELETE  2  @  20 : 23

> DELETE  1  @  4 : 16

> DELETE  2  @  14 : 57

> EOF

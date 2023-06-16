
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  1 : 2  @  1 : 4

~ 

> DELETE  2  @  2 : 3

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 5

> DELETE  2  @  2 : 3

> DELETE  2  @  2 : 4

> CHANGE  71 : 76  @  71 : 78

~ 		float f = MathHelper.cos(this.animTime * 3.1415927F * 2.0F);
~ 		float f1 = MathHelper.cos(this.prevAnimTime * 3.1415927F * 2.0F);
~ 		if (f1 <= -0.3F && f >= -0.3F && !this.isSilent()) {
~ 			this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F,
~ 					0.8F + this.rand.nextFloat() * 0.3F, false);

> CHANGE  38 : 51  @  38 : 132

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

> DELETE  29  @  29 : 39

> DELETE  37  @  37 : 42

> DELETE  7  @  7 : 12

> CHANGE  13 : 14  @  13 : 14

~ 			for (EntityEnderCrystal entityendercrystal1 : (List<EntityEnderCrystal>) list) {

> DELETE  12  @  12 : 74

> DELETE  4  @  4 : 45

> DELETE  45  @  45 : 63

> DELETE  2  @  2 : 5

> DELETE  1  @  1 : 13

> DELETE  2  @  2 : 45

> EOF

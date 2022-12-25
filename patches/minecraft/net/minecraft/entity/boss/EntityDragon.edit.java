
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  3 : 4  @  6 : 9

~ 

> DELETE  6  @  11 : 12

> DELETE  8  @  14 : 16

> DELETE  9  @  17 : 18

> DELETE  11  @  20 : 23

> DELETE  13  @  25 : 26

> DELETE  15  @  28 : 30

> CHANGE  86 : 91  @  101 : 108

~ 		float f = MathHelper.cos(this.animTime * 3.1415927F * 2.0F);
~ 		float f1 = MathHelper.cos(this.prevAnimTime * 3.1415927F * 2.0F);
~ 		if (f1 <= -0.3F && f >= -0.3F && !this.isSilent()) {
~ 			this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F,
~ 					0.8F + this.rand.nextFloat() * 0.3F, false);

> CHANGE  129 : 142  @  146 : 240

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

> DELETE  171  @  269 : 279

> DELETE  208  @  316 : 321

> DELETE  215  @  328 : 333

> CHANGE  228 : 229  @  346 : 347

~ 			for (EntityEnderCrystal entityendercrystal1 : (List<EntityEnderCrystal>) list) {

> DELETE  241  @  359 : 421

> DELETE  245  @  425 : 466

> DELETE  290  @  511 : 529

> DELETE  292  @  531 : 534

> DELETE  293  @  535 : 547

> DELETE  295  @  549 : 592

> EOF

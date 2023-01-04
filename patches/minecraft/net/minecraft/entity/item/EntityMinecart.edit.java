
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Maps;
+ 

> DELETE  8  @  8 : 15

> DELETE  4  @  4 : 5

> DELETE  11  @  11 : 12

> CHANGE  85 : 86  @  85 : 112

~ 		return true;

> CHANGE  42 : 53  @  42 : 97

~ 		if (this.turnProgress > 0) {
~ 			double d4 = this.posX + (this.minecartX - this.posX) / (double) this.turnProgress;
~ 			double d5 = this.posY + (this.minecartY - this.posY) / (double) this.turnProgress;
~ 			double d6 = this.posZ + (this.minecartZ - this.posZ) / (double) this.turnProgress;
~ 			double d1 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double) this.rotationYaw);
~ 			this.rotationYaw = (float) ((double) this.rotationYaw + d1 / (double) this.turnProgress);
~ 			this.rotationPitch = (float) ((double) this.rotationPitch
~ 					+ (this.minecartPitch - (double) this.rotationPitch) / (double) this.turnProgress);
~ 			--this.turnProgress;
~ 			this.setPosition(d4, d5, d6);
~ 			this.setRotation(this.rotationYaw, this.rotationPitch);

> CHANGE  1 : 2  @  1 : 41

~ 			this.setPosition(this.posX, this.posY, this.posZ);

> DELETE  1  @  1 : 18

> DELETE  351  @  351 : 361

> DELETE  1  @  1 : 68

> EOF

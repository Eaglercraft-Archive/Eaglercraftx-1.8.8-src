
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  4

+ 
+ import com.google.common.collect.Maps;
+ 

> DELETE  14  @  12 : 19

> DELETE  18  @  23 : 24

> DELETE  29  @  35 : 36

> CHANGE  114 : 115  @  121 : 148

~ 		return true;

> CHANGE  157 : 168  @  190 : 245

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

> CHANGE  169 : 170  @  246 : 286

~ 			this.setPosition(this.posX, this.posY, this.posZ);

> DELETE  171  @  287 : 304

> DELETE  522  @  655 : 665

> DELETE  523  @  666 : 733

> EOF

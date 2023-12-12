
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  15 : 16  @  15 : 16

~ import net.minecraft.nbt.NBTTagCompound;

> INSERT  147 : 149  @  147

+ 	public boolean isChair = false;
+ 

> INSERT  2 : 9  @  2

+ 		if (isChair) {
+ 			if (!(riddenByEntity instanceof EntityPlayer)) {
+ 				isChair = false;
+ 				setDead();
+ 			}
+ 			return;
+ 		}

> CHANGE  109 : 110  @  109 : 112

~ 							entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);

> CHANGE  167 : 168  @  167 : 168

~ 		if (this.inGround && this.arrowShake <= 0) {

> EOF

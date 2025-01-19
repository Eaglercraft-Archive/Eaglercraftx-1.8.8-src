
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  156 : 158  @  156

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

> EOF

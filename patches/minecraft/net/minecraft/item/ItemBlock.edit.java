
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  9  @  9 : 11

> INSERT  120 : 124  @  120

+ 
+ 	public float getHeldItemBrightnessEagler(ItemStack itemStack) {
+ 		return this.block.getLightValue() * 0.06667f;
+ 	}

> EOF

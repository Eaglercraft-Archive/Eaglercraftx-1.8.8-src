
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 12

> CHANGE  23 : 25  @  23 : 24

~ 		for (int k = 0; k < 4; ++k) {
~ 			final int k2 = k;

> CHANGE  9 : 10  @  9 : 10

~ 											? ((ItemArmor) itemstack.getItem()).armorType == k2

> CHANGE  1 : 2  @  1 : 2

~ 													&& itemstack.getItem() != Items.skull ? false : k2 == 0));

> CHANGE  3 : 4  @  3 : 4

~ 							return ItemArmor.EMPTY_SLOT_NAMES[k2];

> EOF

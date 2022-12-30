
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 12

> CHANGE  23 : 25  @  29 : 30

~ 		for (int k = 0; k < 4; ++k) {
~ 			final int k2 = k;

> CHANGE  11 : 12  @  10 : 11

~ 											? ((ItemArmor) itemstack.getItem()).armorType == k2

> CHANGE  2 : 3  @  2 : 3

~ 													&& itemstack.getItem() != Items.skull ? false : k2 == 0));

> CHANGE  4 : 5  @  4 : 5

~ 							return ItemArmor.EMPTY_SLOT_NAMES[k2];

> EOF

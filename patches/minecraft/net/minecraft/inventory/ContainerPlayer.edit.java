
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 12

> CHANGE  29 : 31  @  35 : 36

~ 		for (int k = 0; k < 4; ++k) {
~ 			final int k2 = k;

> CHANGE  40 : 41  @  45 : 46

~ 											? ((ItemArmor) itemstack.getItem()).armorType == k2

> CHANGE  42 : 43  @  47 : 48

~ 													&& itemstack.getItem() != Items.skull ? false : k2 == 0));

> CHANGE  46 : 47  @  51 : 52

~ 							return ItemArmor.EMPTY_SLOT_NAMES[k2];

> EOF

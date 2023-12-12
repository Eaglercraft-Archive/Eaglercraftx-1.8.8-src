
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  42 : 44  @  42 : 43

~ 		for (int k = 0; k < 4; ++k) {
~ 			final int kk = k;

> CHANGE  1 : 2  @  1 : 2

~ 					new Slot(playerInventory, playerInventory.getSizeInventory() - 1 - kk, 8, 8 + kk * 18) {

> CHANGE  7 : 8  @  7 : 8

~ 											? ((ItemArmor) itemstack.getItem()).armorType == kk

> CHANGE  1 : 2  @  1 : 2

~ 													&& itemstack.getItem() != Items.skull ? false : kk == 0));

> CHANGE  3 : 4  @  3 : 4

~ 							return ItemArmor.EMPTY_SLOT_NAMES[kk];

> EOF

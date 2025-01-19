
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  17  @  17 : 21

> CHANGE  172 : 174  @  172 : 174

~ 		for (int i = 0; i < this.inventory.length; ++i) {
~ 			if (this.inventory[i] != null) {

> CHANGE  8 : 10  @  8 : 9

~ 		for (int i = 0; i < this.inventory.length; ++i) {
~ 			ItemStack itemstack = this.inventory[i];

> CHANGE  111 : 115  @  111 : 114

~ 			List<EntityItem> list = func_181556_a(parIHopper.getWorld(), parIHopper.getXPos(),
~ 					parIHopper.getYPos() + 1.0D, parIHopper.getZPos());
~ 			for (int i = 0, l = list.size(); i < l; ++i) {
~ 				if (putDropInInventoryAllSlots(parIHopper, list.get(i))) {

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;

> DELETE  3  @  3 : 5

> CHANGE  10 : 12  @  10 : 11

~ 		if (entityplayer.capabilities.isCreativeMode && world.isRemote
~ 				&& !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {

> CHANGE  2 : 5  @  2 : 3

~ 			if (!entityplayer.capabilities.isCreativeMode) {
~ 				--itemstack.stackSize;
~ 			}

> INSERT  9 : 13  @  9

+ 
+ 	public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
+ 		return true;
+ 	}

> EOF

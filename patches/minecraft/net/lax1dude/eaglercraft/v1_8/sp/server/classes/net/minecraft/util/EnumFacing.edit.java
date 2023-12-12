
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 6  @  5

+ 

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> INSERT  4 : 6  @  4

+ import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;
+ 

> INSERT  8 : 12  @  8

+ 	static {
+ 		__checkIntegratedContextValid("net/minecraft/util/EnumFacing");
+ 	}
+ 

> CHANGE  162 : 163  @  162 : 163

~ 	public static EnumFacing random(EaglercraftRandom rand) {

> CHANGE  140 : 141  @  140 : 141

~ 		public EnumFacing random(EaglercraftRandom rand) {

> EOF

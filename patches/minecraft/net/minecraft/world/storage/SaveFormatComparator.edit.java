
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.minecraft.nbt.NBTTagCompound;

> INSERT  11 : 12  @  11

+ 	public final NBTTagCompound levelDat;

> CHANGE  3 : 4  @  3 : 4

~ 			boolean cheatsEnabledIn, NBTTagCompound levelDat) {

> INSERT  8 : 9  @  8

+ 		this.levelDat = levelDat;

> EOF

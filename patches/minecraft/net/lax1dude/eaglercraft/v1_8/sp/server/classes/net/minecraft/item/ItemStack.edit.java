
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 6  @  5

+ 

> INSERT  2 : 3  @  2

+ import java.util.Collection;

> CHANGE  1 : 4  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ 

> CHANGE  18 : 21  @  18 : 21

~ import net.minecraft.nbt.NBTBase;
~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagList;

> CHANGE  189 : 190  @  189 : 190

~ 	public boolean attemptDamageItem(int amount, EaglercraftRandom rand) {

> CHANGE  249 : 250  @  249 : 250

~ 				s = s + HString.format("#%04d/%d%s",

> CHANGE  2 : 3  @  2 : 3

~ 				s = s + HString.format("#%04d%s", new Object[] { Integer.valueOf(i), s1 });

> CHANGE  56 : 57  @  56 : 57

~ 			for (Entry entry : (Collection<Entry>) multimap.entries()) {

> EOF

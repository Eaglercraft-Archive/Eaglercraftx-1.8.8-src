
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  12 : 14  @  12

+ import net.lax1dude.eaglercraft.v1_8.HString;
+ 

> INSERT  39 : 42  @  39

+ 	public static final int eaglerVersionCurrent = 1;
+ 	private int eaglerVersion = eaglerVersionCurrent;
+ 

> INSERT  116 : 117  @  116

+ 		this.eaglerVersion = nbt.getInteger("eaglerVersionSerial");

> INSERT  102 : 103  @  102

+ 		nbt.setInteger("eaglerVersionSerial", this.eaglerVersion);

> INSERT  274 : 288  @  274

+ 	public int getEaglerVersion() {
+ 		return this.eaglerVersion;
+ 	}
+ 
+ 	public boolean isOldEaglercraftRandom() {
+ 		return this.eaglerVersion == 0;
+ 	}
+ 
+ 	public static void initEaglerVersion(NBTTagCompound compound) {
+ 		if (!compound.hasKey("eaglerVersionSerial", 99)) {
+ 			compound.setInteger("eaglerVersionSerial", eaglerVersionCurrent);
+ 		}
+ 	}
+ 

> CHANGE  8 : 9  @  8 : 9

~ 				return HString.format("ID %02d - %s, ver %d. Features enabled: %b",

> CHANGE  19 : 21  @  19 : 21

~ 				return HString.format("%d game time, %d day time", new Object[] {
~ 						Long.valueOf(WorldInfo.this.totalTime), Long.valueOf(WorldInfo.this.worldTime) });

> CHANGE  23 : 24  @  23 : 24

~ 				return HString.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.this.saveVersion), s });

> CHANGE  4 : 5  @  4 : 5

~ 				return HString.format("Rain time: %d (now: %b), thunder time: %d (now: %b)",

> CHANGE  7 : 8  @  7 : 8

~ 				return HString.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] {

> EOF

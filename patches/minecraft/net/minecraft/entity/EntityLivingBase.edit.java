
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  4 : 11  @  7 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.base.Predicates;
~ import com.google.common.collect.Maps;
~ 

> DELETE  9  @  4 : 5

> DELETE  1  @  2 : 6

> DELETE  5  @  9 : 12

> DELETE  1  @  4 : 6

> DELETE  9  @  11 : 14

> DELETE  13  @  16 : 17

> CHANGE  2 : 4  @  3 : 4

~ 	private static final EaglercraftUUID sprintingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

> DELETE  95  @  94 : 111

> CHANGE  25 : 26  @  42 : 45

~ 		this.extinguish();

> DELETE  23  @  25 : 28

> CHANGE  14 : 15  @  17 : 18

~ 		if (this.hurtResistantTime > 0) {

> DELETE  42  @  42 : 45

> DELETE  1  @  4 : 12

> CHANGE  33 : 34  @  41 : 42

~ 	public EaglercraftRandom getRNG() {

> DELETE  75  @  75 : 78

> CHANGE  37 : 38  @  40 : 46

~ 			if (potioneffect.onUpdate(this) && potioneffect.getDuration() % 600 == 0) {

> CHANGE  5 : 6  @  10 : 14

~ 		this.potionsNeedUpdate = false;

> DELETE  2  @  5 : 8

> DELETE  47  @  50 : 51

> DELETE  1  @  2 : 11

> DELETE  62  @  71 : 76

> DELETE  4  @  9 : 16

> DELETE  4  @  11 : 16

> CHANGE  19 : 20  @  24 : 117

~ 		return false;

> DELETE  34  @  126 : 131

> DELETE  1  @  6 : 15

> DELETE  190  @  199 : 203

> DELETE  59  @  63 : 64

> CHANGE  154 : 157  @  155 : 160

~ 					if (!this.worldObj.isBlockLoaded(new BlockPos((int) this.posX, 0, (int) this.posZ))
~ 							|| !this.worldObj.getChunkFromBlockCoords(new BlockPos((int) this.posX, 0, (int) this.posZ))
~ 									.isLoaded()) {

> DELETE  90  @  92 : 128

> DELETE  158  @  194 : 198

> DELETE  29  @  33 : 37

> DELETE  31  @  35 : 41

> DELETE  1  @  7 : 18

> CHANGE  31 : 32  @  42 : 43

~ 		return false;

> EOF

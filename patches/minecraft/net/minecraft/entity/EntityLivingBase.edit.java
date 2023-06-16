
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  4 : 11  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.base.Predicates;
~ import com.google.common.collect.Maps;
~ 

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 5

> DELETE  5  @  5 : 8

> DELETE  1  @  1 : 3

> DELETE  9  @  9 : 12

> DELETE  13  @  13 : 14

> CHANGE  2 : 4  @  2 : 3

~ 	private static final EaglercraftUUID sprintingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

> DELETE  93  @  93 : 110

> CHANGE  25 : 26  @  25 : 28

~ 		this.extinguish();

> DELETE  22  @  22 : 25

> CHANGE  14 : 15  @  14 : 15

~ 		if (this.hurtResistantTime > 0) {

> DELETE  41  @  41 : 44

> DELETE  1  @  1 : 9

> CHANGE  33 : 34  @  33 : 34

~ 	public EaglercraftRandom getRNG() {

> DELETE  74  @  74 : 77

> CHANGE  37 : 38  @  37 : 43

~ 			if (potioneffect.onUpdate(this) && potioneffect.getDuration() % 600 == 0) {

> CHANGE  4 : 5  @  4 : 8

~ 		this.potionsNeedUpdate = false;

> DELETE  1  @  1 : 4

> DELETE  47  @  47 : 48

> DELETE  1  @  1 : 10

> DELETE  62  @  62 : 67

> DELETE  4  @  4 : 11

> DELETE  4  @  4 : 9

> CHANGE  19 : 20  @  19 : 112

~ 		return false;

> DELETE  33  @  33 : 38

> DELETE  1  @  1 : 10

> DELETE  190  @  190 : 194

> DELETE  59  @  59 : 60

> CHANGE  154 : 157  @  154 : 159

~ 					if (!this.worldObj.isBlockLoaded(new BlockPos((int) this.posX, 0, (int) this.posZ))
~ 							|| !this.worldObj.getChunkFromBlockCoords(new BlockPos((int) this.posX, 0, (int) this.posZ))
~ 									.isLoaded()) {

> DELETE  87  @  87 : 123

> DELETE  158  @  158 : 162

> DELETE  29  @  29 : 33

> DELETE  31  @  31 : 37

> DELETE  1  @  1 : 12

> CHANGE  31 : 32  @  31 : 32

~ 		return false;

> EOF

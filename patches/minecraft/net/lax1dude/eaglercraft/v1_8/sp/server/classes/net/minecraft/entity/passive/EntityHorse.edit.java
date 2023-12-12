
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  30 : 32  @  30 : 32

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagList;

> DELETE  2  @  2 : 3

> CHANGE  287 : 288  @  287 : 288

~ 		if (this.isChested()) {

> CHANGE  64 : 65  @  64 : 65

~ 		{

> DELETE  5  @  5 : 6

> CHANGE  214 : 215  @  214 : 216

~ 		if ((this.riddenByEntity == null || this.riddenByEntity == playerEntity) && this.isTame()) {

> CHANGE  149 : 150  @  149 : 153

~ 		player.mountEntity(this);

> CHANGE  35 : 36  @  35 : 39

~ 		this.dropChestItems();

> CHANGE  8 : 9  @  8 : 9

~ 		{

> DELETE  24  @  24 : 25

> DELETE  4  @  4 : 9

> CHANGE  5 : 6  @  5 : 6

~ 		if (this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {

> CHANGE  60 : 62  @  60 : 65

~ 		this.openMouthCounter = 1;
~ 		this.setHorseWatchableBoolean(128, true);

> CHANGE  24 : 26  @  24 : 29

~ 		this.jumpRearingCounter = 1;
~ 		this.setRearing(true);

> CHANGE  17 : 18  @  17 : 18

~ 		if (animalChestIn != null) {

> CHANGE  56 : 59  @  56 : 61

~ 			this.setAIMoveSpeed(
~ 					(float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
~ 			super.moveEntityWithHeading(f, f1);

> CHANGE  33 : 34  @  33 : 34

~ 		nbttagcompound.setString("Owner", this.getOwnerId());

> CHANGE  37 : 39  @  37 : 42

~ 		if (nbttagcompound.hasKey("Owner", 8)) {
~ 			s = nbttagcompound.getString("Owner");

> EOF

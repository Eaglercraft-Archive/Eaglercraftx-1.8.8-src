
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 6  @  5

+ 

> CHANGE  4 : 6  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> CHANGE  23 : 28  @  23 : 28

~ import net.minecraft.nbt.NBTBase;
~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagFloat;
~ import net.minecraft.nbt.NBTTagList;
~ import net.minecraft.nbt.NBTTagShort;

> CHANGE  26 : 28  @  26 : 27

~ 	private static final EaglercraftUUID sprintingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

> CHANGE  93 : 94  @  93 : 94

~ 		if (this.fallDistance > 3.0F && flag) {

> CHANGE  41 : 42  @  41 : 42

~ 		if (this.isImmuneToFire()) {

> CHANGE  24 : 25  @  24 : 25

~ 				if (this.isRiding() && this.ridingEntity instanceof EntityLivingBase) {

> CHANGE  58 : 59  @  58 : 59

~ 			if ((this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot()

> CHANGE  44 : 45  @  44 : 45

~ 	public EaglercraftRandom getRNG() {

> CHANGE  74 : 75  @  74 : 75

~ 		if (nbttagcompound.hasKey("Attributes", 9) && this.worldObj != null) {

> CHANGE  40 : 41  @  40 : 41

~ 				{

> CHANGE  9 : 10  @  9 : 10

~ 			{

> CHANGE  58 : 59  @  58 : 59

~ 			{

> CHANGE  67 : 68  @  67 : 68

~ 		{

> CHANGE  8 : 9  @  8 : 9

~ 		if (flag) {

> CHANGE  10 : 11  @  10 : 11

~ 		{

> DELETE  25  @  25 : 27

> CHANGE  122 : 123  @  122 : 123

~ 		{

> CHANGE  422 : 423  @  422 : 436

~ 					this.motionY -= 0.08D;

> CHANGE  78 : 79  @  78 : 79

~ 		{

> CHANGE  193 : 194  @  193 : 196

~ 		this.collideWithNearbyEntities();

> CHANGE  30 : 31  @  30 : 34

~ 			this.dismountEntity(this.ridingEntity);

> CHANGE  31 : 32  @  31 : 32

~ 		if (!entity.isDead) {

> CHANGE  48 : 49  @  48 : 49

~ 		return true;

> EOF

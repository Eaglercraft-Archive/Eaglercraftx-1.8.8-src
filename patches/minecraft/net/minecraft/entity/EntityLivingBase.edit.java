
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  6 : 13  @  9 : 11

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.base.Predicates;
~ import com.google.common.collect.Maps;
~ 

> DELETE  15  @  13 : 14

> DELETE  16  @  15 : 19

> DELETE  21  @  24 : 27

> DELETE  22  @  28 : 30

> DELETE  31  @  39 : 42

> DELETE  44  @  55 : 56

> CHANGE  46 : 48  @  58 : 59

~ 	private static final EaglercraftUUID sprintingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

> DELETE  141  @  152 : 169

> CHANGE  166 : 167  @  194 : 197

~ 		this.extinguish();

> DELETE  189  @  219 : 222

> CHANGE  203 : 204  @  236 : 237

~ 		if (this.hurtResistantTime > 0) {

> DELETE  245  @  278 : 281

> DELETE  246  @  282 : 290

> CHANGE  279 : 280  @  323 : 324

~ 	public EaglercraftRandom getRNG() {

> DELETE  354  @  398 : 401

> CHANGE  391 : 392  @  438 : 444

~ 			if (potioneffect.onUpdate(this) && potioneffect.getDuration() % 600 == 0) {

> CHANGE  396 : 397  @  448 : 452

~ 		this.potionsNeedUpdate = false;

> DELETE  398  @  453 : 456

> DELETE  445  @  503 : 504

> DELETE  446  @  505 : 514

> DELETE  508  @  576 : 581

> DELETE  512  @  585 : 592

> DELETE  516  @  596 : 601

> CHANGE  535 : 536  @  620 : 713

~ 		return false;

> DELETE  569  @  746 : 751

> DELETE  570  @  752 : 761

> DELETE  760  @  951 : 955

> DELETE  819  @  1014 : 1015

> CHANGE  973 : 976  @  1169 : 1174

~ 					if (!this.worldObj.isBlockLoaded(new BlockPos((int) this.posX, 0, (int) this.posZ))
~ 							|| !this.worldObj.getChunkFromBlockCoords(new BlockPos((int) this.posX, 0, (int) this.posZ))
~ 									.isLoaded()) {

> DELETE  1063  @  1261 : 1297

> DELETE  1221  @  1455 : 1459

> DELETE  1250  @  1488 : 1492

> DELETE  1281  @  1523 : 1529

> DELETE  1282  @  1530 : 1541

> CHANGE  1313 : 1314  @  1572 : 1573

~ 		return false;

> EOF

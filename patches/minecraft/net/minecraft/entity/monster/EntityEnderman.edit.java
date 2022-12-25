
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  3 : 7  @  8 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.collect.Sets;
~ 

> DELETE  8  @  10 : 11

> DELETE  12  @  15 : 23

> DELETE  13  @  24 : 27

> DELETE  14  @  28 : 29

> DELETE  17  @  32 : 33

> DELETE  23  @  39 : 40

> CHANGE  27 : 29  @  44 : 45

~ 	private static final EaglercraftUUID attackingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("020E0DFB-87AE-4653-9556-831010E291A0");

> DELETE  38  @  54 : 69

> DELETE  76  @  107 : 123

> CHANGE  81 : 88  @  128 : 137

~ 		for (int i = 0; i < 2; ++i) {
~ 			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
~ 					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
~ 					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);

> DELETE  89  @  138 : 139

> DELETE  232  @  282 : 285

> CHANGE  234 : 235  @  287 : 293

~ 					this.isAggressive = true;

> CHANGE  267 : 268  @  325 : 326

~ 	public static void bootstrap() {

> DELETE  284  @  342 : 493

> EOF

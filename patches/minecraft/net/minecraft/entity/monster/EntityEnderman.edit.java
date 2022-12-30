
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  1 : 5  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.collect.Sets;
~ 

> DELETE  5  @  2 : 3

> DELETE  4  @  5 : 13

> DELETE  1  @  9 : 12

> DELETE  1  @  4 : 5

> DELETE  3  @  4 : 5

> DELETE  6  @  7 : 8

> CHANGE  4 : 6  @  5 : 6

~ 	private static final EaglercraftUUID attackingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("020E0DFB-87AE-4653-9556-831010E291A0");

> DELETE  11  @  10 : 25

> DELETE  38  @  53 : 69

> CHANGE  5 : 12  @  21 : 30

~ 		for (int i = 0; i < 2; ++i) {
~ 			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
~ 					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
~ 					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);

> DELETE  8  @  10 : 11

> DELETE  143  @  144 : 147

> CHANGE  2 : 3  @  5 : 11

~ 					this.isAggressive = true;

> CHANGE  33 : 34  @  38 : 39

~ 	public static void bootstrap() {

> DELETE  17  @  17 : 168

> EOF

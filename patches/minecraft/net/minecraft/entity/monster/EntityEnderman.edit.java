
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  1 : 5  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.google.common.collect.Sets;
~ 

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 12

> DELETE  1  @  1 : 4

> DELETE  1  @  1 : 2

> DELETE  3  @  3 : 4

> DELETE  6  @  6 : 7

> CHANGE  4 : 6  @  4 : 5

~ 	private static final EaglercraftUUID attackingSpeedBoostModifierUUID = EaglercraftUUID
~ 			.fromString("020E0DFB-87AE-4653-9556-831010E291A0");

> DELETE  9  @  9 : 24

> DELETE  38  @  38 : 54

> CHANGE  5 : 12  @  5 : 14

~ 		for (int i = 0; i < 2; ++i) {
~ 			this.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
~ 					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
~ 					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
~ 					(this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);

> DELETE  1  @  1 : 2

> DELETE  143  @  143 : 146

> CHANGE  2 : 3  @  2 : 8

~ 					this.isAggressive = true;

> CHANGE  32 : 33  @  32 : 33

~ 	public static void bootstrap() {

> DELETE  16  @  16 : 167

> EOF

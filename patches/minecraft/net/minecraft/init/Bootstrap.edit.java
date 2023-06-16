
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  22 : 24  @  22

+ import net.minecraft.entity.monster.EntityEnderman;
+ import net.minecraft.entity.passive.EntityVillager;

> DELETE  5  @  5 : 7

> INSERT  2 : 3  @  2

+ import net.minecraft.item.ItemAxe;

> INSERT  3 : 4  @  3

+ import net.minecraft.item.ItemPickaxe;

> INSERT  1 : 2  @  1

+ import net.minecraft.item.ItemSpade;

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  3  @  3 : 4

> CHANGE  1 : 2  @  1 : 3

~ import net.minecraft.world.biome.BiomeGenBase;

> CHANGE  102 : 103  @  102 : 103

~ 				EaglercraftRandom random = world.rand;

> CHANGE  138 : 139  @  138 : 143

~ 					if (!ItemDye.applyBonemeal(itemstack, world, blockpos)) {

> CHANGE  40 : 41  @  40 : 74

~ 				if (!(world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, itemstack))) {

> DELETE  25  @  25 : 29

> INSERT  27 : 29  @  27

+ 			Blocks.doBootstrap();
+ 			BiomeGenBase.bootstrap();

> INSERT  1 : 5  @  1

+ 			EntityEnderman.bootstrap();
+ 			ItemAxe.bootstrap();
+ 			ItemPickaxe.bootstrap();
+ 			ItemSpade.bootstrap();

> INSERT  1 : 3  @  1

+ 			Items.doBootstrap();
+ 			EntityVillager.bootstrap();

> CHANGE  6 : 8  @  6 : 8

~ 		System.setErr(new LoggingPrintStream("STDERR", true, System.err));
~ 		System.setOut(new LoggingPrintStream("STDOUT", false, SYSOUT));

> EOF

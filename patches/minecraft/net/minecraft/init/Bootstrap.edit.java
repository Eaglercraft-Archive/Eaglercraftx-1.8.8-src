
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  26 : 28  @  24

+ import net.minecraft.entity.monster.EntityEnderman;
+ import net.minecraft.entity.passive.EntityVillager;

> DELETE  7  @  5 : 7

> INSERT  2 : 3  @  4

+ import net.minecraft.item.ItemAxe;

> INSERT  4 : 5  @  3

+ import net.minecraft.item.ItemPickaxe;

> INSERT  2 : 3  @  1

+ import net.minecraft.item.ItemSpade;

> DELETE  2  @  1 : 3

> DELETE  1  @  3 : 4

> DELETE  1  @  2 : 3

> DELETE  3  @  4 : 5

> CHANGE  1 : 2  @  2 : 4

~ import net.minecraft.world.biome.BiomeGenBase;

> CHANGE  103 : 104  @  104 : 105

~ 				EaglercraftRandom random = world.rand;

> CHANGE  139 : 140  @  139 : 144

~ 					if (!ItemDye.applyBonemeal(itemstack, world, blockpos)) {

> CHANGE  41 : 42  @  45 : 79

~ 				if (!(world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, itemstack))) {

> DELETE  26  @  59 : 63

> INSERT  27 : 29  @  31

+ 			Blocks.doBootstrap();
+ 			BiomeGenBase.bootstrap();

> INSERT  3 : 7  @  1

+ 			EntityEnderman.bootstrap();
+ 			ItemAxe.bootstrap();
+ 			ItemPickaxe.bootstrap();
+ 			ItemSpade.bootstrap();

> INSERT  5 : 7  @  1

+ 			Items.doBootstrap();
+ 			EntityVillager.bootstrap();

> CHANGE  8 : 10  @  6 : 8

~ 		System.setErr(new LoggingPrintStream("STDERR", true, System.err));
~ 		System.setOut(new LoggingPrintStream("STDOUT", false, SYSOUT));

> EOF

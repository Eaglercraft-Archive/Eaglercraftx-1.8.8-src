
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 7  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  29 : 31  @  28

+ import net.minecraft.entity.monster.EntityEnderman;
+ import net.minecraft.entity.passive.EntityVillager;

> DELETE  36  @  33 : 35

> INSERT  38 : 39  @  37

+ import net.minecraft.item.ItemAxe;

> INSERT  42 : 43  @  40

+ import net.minecraft.item.ItemPickaxe;

> INSERT  44 : 45  @  41

+ import net.minecraft.item.ItemSpade;

> DELETE  46  @  42 : 44

> DELETE  47  @  45 : 46

> DELETE  48  @  47 : 48

> DELETE  51  @  51 : 52

> CHANGE  52 : 53  @  53 : 55

~ import net.minecraft.world.biome.BiomeGenBase;

> CHANGE  155 : 156  @  157 : 158

~ 				EaglercraftRandom random = world.rand;

> CHANGE  294 : 295  @  296 : 301

~ 					if (!ItemDye.applyBonemeal(itemstack, world, blockpos)) {

> CHANGE  335 : 336  @  341 : 375

~ 				if (!(world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, itemstack))) {

> DELETE  361  @  400 : 404

> INSERT  388 : 390  @  431

+ 			Blocks.doBootstrap();
+ 			BiomeGenBase.bootstrap();

> INSERT  391 : 395  @  432

+ 			EntityEnderman.bootstrap();
+ 			ItemAxe.bootstrap();
+ 			ItemPickaxe.bootstrap();
+ 			ItemSpade.bootstrap();

> INSERT  396 : 398  @  433

+ 			Items.doBootstrap();
+ 			EntityVillager.bootstrap();

> CHANGE  404 : 406  @  439 : 441

~ 		System.setErr(new LoggingPrintStream("STDERR", true, System.err));
~ 		System.setOut(new LoggingPrintStream("STDOUT", false, SYSOUT));

> EOF

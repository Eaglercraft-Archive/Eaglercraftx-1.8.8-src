
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  1 : 3  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  22 : 24  @  22

+ import net.minecraft.entity.monster.EntityEnderman;
+ import net.minecraft.entity.passive.EntityVillager;

> INSERT  9 : 10  @  9

+ import net.minecraft.item.ItemAxe;

> INSERT  3 : 4  @  3

+ import net.minecraft.item.ItemPickaxe;

> INSERT  1 : 2  @  1

+ import net.minecraft.item.ItemSpade;

> INSERT  3 : 4  @  3

+ import net.minecraft.util.LoggingPrintStream;

> DELETE  6  @  6 : 7

> CHANGE  2 : 5  @  2 : 4

~ import net.minecraft.world.biome.BiomeGenBase;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  102 : 103  @  102 : 103

~ 				EaglercraftRandom random = world.rand;

> CHANGE  139 : 140  @  139 : 142

~ 						world.playAuxSFX(2005, blockpos, 0);

> CHANGE  42 : 43  @  42 : 43

~ 					{

> CHANGE  14 : 15  @  14 : 15

~ 											gameprofile = new GameProfile((EaglercraftUUID) null, s);

> CHANGE  42 : 43  @  42 : 46

~ 							world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);

> INSERT  27 : 29  @  27

+ 			Blocks.doBootstrap();
+ 			BiomeGenBase.doBootstrap();

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

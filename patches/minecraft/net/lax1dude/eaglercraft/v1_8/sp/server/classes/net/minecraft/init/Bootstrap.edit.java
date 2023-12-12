
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  1 : 3  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  22 : 23  @  22

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.monster.EntityEnderman;

> INSERT  9 : 10  @  9

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemAxe;

> INSERT  3 : 4  @  3

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemPickaxe;

> INSERT  1 : 2  @  1

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemSpade;

> CHANGE  1 : 4  @  1 : 3

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTUtil;
~ import net.minecraft.util.LoggingPrintStream;

> DELETE  6  @  6 : 7

> CHANGE  2 : 5  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.biome.BiomeGenBase;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  109 : 110  @  109 : 110

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

+ 			EntityEnderman.doBootstrap();
+ 			ItemAxe.doBootstrap();
+ 			ItemPickaxe.doBootstrap();
+ 			ItemSpade.doBootstrap();

> INSERT  1 : 2  @  1

+ 			Items.doBootstrap();

> CHANGE  6 : 8  @  6 : 8

~ 		System.setErr(new LoggingPrintStream("STDERR", true, System.err));
~ 		System.setOut(new LoggingPrintStream("STDOUT", false, SYSOUT));

> EOF

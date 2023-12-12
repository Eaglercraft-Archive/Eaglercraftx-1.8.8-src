
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 7

> CHANGE  4 : 5  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  2 : 5  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.CrashReportHelper;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> CHANGE  4 : 6  @  4 : 6

~ import net.minecraft.crash.CrashReport;
~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  59 : 61  @  59 : 61

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  15 : 16  @  15 : 16

~ 	private final Map<EaglercraftUUID, Entity> entitiesByUuid = Maps.newHashMap();

> CHANGE  176 : 177  @  176 : 177

~ 		if (this.allPlayersSleeping) {

> INSERT  105 : 106  @  105

+ 									++EaglerMinecraftServer.counterTileUpdate;

> INSERT  47 : 48  @  47

+ 						++EaglerMinecraftServer.counterTileUpdate;

> INSERT  95 : 96  @  95

+ 								++EaglerMinecraftServer.counterTileUpdate;

> CHANGE  5 : 7  @  5 : 7

~ 								CrashReportHelper.addIntegratedServerBlockInfo(crashreportcategory,
~ 										nextticklistentry1.position, iblockstate);

> CHANGE  153 : 154  @  153 : 154

~ 			EaglercraftRandom random = new EaglercraftRandom(this.getSeed());

> CHANGE  268 : 269  @  268 : 269

~ 	public Entity getEntityFromUuid(EaglercraftUUID uuid) {

> CHANGE  3 : 5  @  3 : 5

~ 	public void addScheduledTask(Runnable runnable) {
~ 		this.mcServer.addScheduledTask(runnable);

> EOF

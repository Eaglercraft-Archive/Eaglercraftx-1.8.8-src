
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
~ 

> CHANGE  6 : 8  @  6 : 8

~ import net.minecraft.crash.CrashReport;
~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.nbt.NBTTagCompound;

> CHANGE  14 : 15  @  14 : 15

~ 	private static final SimpleDateFormat timestampFormat = EagRuntime.fixDateFormat(new SimpleDateFormat("HH:mm:ss"));

> DELETE  59  @  59 : 63

> CHANGE  1 : 2  @  1 : 2

~ 		if (minecraftserver != null && minecraftserver.isCommandBlockEnabled()) {

> CHANGE  39 : 40  @  39 : 40

~ 		if (this.trackOutput && this.getEntityWorld() != null) {

> CHANGE  9 : 10  @  9 : 10

~ 		return minecraftserver == null

> DELETE  29  @  29 : 33

> EOF

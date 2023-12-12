
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> INSERT  2 : 4  @  2

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.server.CrashReportHelper;

> CHANGE  3 : 4  @  3 : 4

~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.nbt.NBTTagCompound;

> CHANGE  23 : 26  @  23 : 25

~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  65 : 67  @  65 : 66

~ 			logger.error("Caught exception calling tile entity constructor!");
~ 			logger.error(exception);

> CHANGE  88 : 90  @  88 : 89

~ 			CrashReportHelper.addIntegratedServerBlockInfo(reportCategory, this.pos, this.getBlockType(),
~ 					this.getBlockMetadata());

> CHANGE  6 : 7  @  6 : 7

~ 						return HString.format("ID #%d (%s // %s)",

> CHANGE  14 : 16  @  14 : 16

~ 						String s = HString.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
~ 						return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });

> EOF

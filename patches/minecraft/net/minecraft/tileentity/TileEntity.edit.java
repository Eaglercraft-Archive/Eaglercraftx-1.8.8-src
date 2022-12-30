
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 8  @  3

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  13  @  7 : 27

> DELETE  2  @  22 : 24

> CHANGE  58 : 59  @  60 : 61

~ 			logger.error("Could not create TileEntity", exception);

> CHANGE  85 : 86  @  85 : 86

~ 						+ TileEntity.this.getClass().getName();

> CHANGE  11 : 12  @  11 : 12

~ 						return HString.format("ID #%d (%s // %s)",

> CHANGE  2 : 3  @  2 : 3

~ 										Block.getBlockById(i).getClass().getName() });

> CHANGE  13 : 15  @  13 : 15

~ 						String s = HString.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
~ 						return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });

> EOF

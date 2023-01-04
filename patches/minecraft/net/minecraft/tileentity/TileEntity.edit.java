
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 8  @  2

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  7  @  7 : 27

> DELETE  2  @  2 : 4

> CHANGE  58 : 59  @  58 : 59

~ 			logger.error("Could not create TileEntity", exception);

> CHANGE  84 : 85  @  84 : 85

~ 						+ TileEntity.this.getClass().getName();

> CHANGE  10 : 11  @  10 : 11

~ 						return HString.format("ID #%d (%s // %s)",

> CHANGE  1 : 2  @  1 : 2

~ 										Block.getBlockById(i).getClass().getName() });

> CHANGE  12 : 14  @  12 : 14

~ 						String s = HString.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
~ 						return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });

> EOF

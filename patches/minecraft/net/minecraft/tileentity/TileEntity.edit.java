
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  4 : 10  @  5

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  17  @  12 : 32

> DELETE  19  @  34 : 36

> CHANGE  77 : 78  @  94 : 95

~ 			logger.error("Could not create TileEntity", exception);

> CHANGE  162 : 163  @  179 : 180

~ 						+ TileEntity.this.getClass().getName();

> CHANGE  173 : 174  @  190 : 191

~ 						return HString.format("ID #%d (%s // %s)",

> CHANGE  175 : 176  @  192 : 193

~ 										Block.getBlockById(i).getClass().getName() });

> CHANGE  188 : 190  @  205 : 207

~ 						String s = HString.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
~ 						return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });

> EOF

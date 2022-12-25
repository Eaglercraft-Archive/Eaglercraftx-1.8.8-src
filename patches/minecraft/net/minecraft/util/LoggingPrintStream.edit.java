
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  4  @  4 : 6

> INSERT  5 : 9  @  7

+ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ 

> DELETE  10  @  8 : 9

> INSERT  11 : 13  @  10

+ 	private final Logger logger;
+ 	private final boolean err;

> CHANGE  14 : 15  @  11 : 12

~ 	public LoggingPrintStream(String domainIn, boolean err, OutputStream outStream) {

> INSERT  17 : 19  @  14

+ 		this.logger = LogManager.getLogger(domainIn);
+ 		this.err = err;

> CHANGE  30 : 44  @  25 : 29

~ 		String callingClass = PlatformRuntime.getCallingClass(3);
~ 		if (callingClass == null) {
~ 			if (err) {
~ 				logger.error(string);
~ 			} else {
~ 				logger.info(string);
~ 			}
~ 		} else {
~ 			if (err) {
~ 				logger.error("@({}): {}", new Object[] { callingClass, string });
~ 			} else {
~ 				logger.info("@({}): {}", new Object[] { callingClass, string });
~ 			}
~ 		}

> EOF

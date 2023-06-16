
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  4  @  4 : 6

> INSERT  1 : 5  @  1

+ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ 

> DELETE  1  @  1 : 2

> INSERT  1 : 3  @  1

+ 	private final Logger logger;
+ 	private final boolean err;

> CHANGE  1 : 2  @  1 : 2

~ 	public LoggingPrintStream(String domainIn, boolean err, OutputStream outStream) {

> INSERT  2 : 4  @  2

+ 		this.logger = LogManager.getLogger(domainIn);
+ 		this.err = err;

> CHANGE  11 : 25  @  11 : 15

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

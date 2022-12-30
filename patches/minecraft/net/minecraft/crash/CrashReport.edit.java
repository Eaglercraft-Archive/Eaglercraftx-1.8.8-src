
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  2  @  5 : 7

> CHANGE  4 : 13  @  6 : 7

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  10  @  2 : 7

> DELETE  7  @  12 : 13

> CHANGE  1 : 2  @  2 : 3

~ 	private String[] stacktrace;

> INSERT  5 : 6  @  5

+ 		this.stacktrace = EagRuntime.getStackTraceElements(causeThrowable);

> CHANGE  27 : 38  @  26 : 54

~ 		if (EagRuntime.getPlatformType() != EnumPlatformType.JAVASCRIPT) {
~ 			this.theReportCategory.addCrashSectionCallable("Memory", new Callable<String>() {
~ 				public String call() {
~ 					long i = EagRuntime.maxMemory();
~ 					long j = EagRuntime.totalMemory();
~ 					long k = EagRuntime.freeMemory();
~ 					long l = i / 1024L / 1024L;
~ 					long i1 = j / 1024L / 1024L;
~ 					long j1 = k / 1024L / 1024L;
~ 					return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l
~ 							+ " MB)";

> CHANGE  12 : 14  @  29 : 38

~ 			});
~ 		}

> CHANGE  14 : 15  @  21 : 22

~ 			this.stacktrace = (String[]) ArrayUtils

> CHANGE  8 : 9  @  8 : 9

~ 			for (String stacktraceelement : this.stacktrace) {

> CHANGE  17 : 18  @  17 : 28

~ 		StringBuilder stackTrace = new StringBuilder();

> CHANGE  2 : 9  @  12 : 13

~ 		if ((this.cause.getMessage() == null || this.cause.getMessage().length() == 0)
~ 				&& ((this.cause instanceof NullPointerException) || (this.cause instanceof StackOverflowError)
~ 						|| (this.cause instanceof OutOfMemoryError))) {
~ 			stackTrace.append(this.cause.getClass().getName()).append(": ");
~ 			stackTrace.append(this.description).append('\n');
~ 		} else {
~ 			stackTrace.append(this.cause.toString()).append('\n');

> CHANGE  9 : 12  @  3 : 4

~ 		EagRuntime.getStackTrace(this.cause, (s) -> {
~ 			stackTrace.append("\tat ").append(s).append('\n');
~ 		});

> CHANGE  4 : 5  @  2 : 13

~ 		return stackTrace.toString();

> DELETE  28  @  38 : 63

> CHANGE  12 : 15  @  37 : 40

~ 			String[] astacktraceelement = EagRuntime.getStackTraceElements(cause);
~ 			String stacktraceelement = null;
~ 			String stacktraceelement1 = null;

> CHANGE  24 : 25  @  24 : 25

~ 				this.stacktrace = new String[j];

> CHANGE  12 : 13  @  12 : 31

~ 		return "eagler";

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  2  @  2 : 4

> CHANGE  4 : 13  @  4 : 5

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  1  @  1 : 6

> DELETE  7  @  7 : 8

> CHANGE  1 : 2  @  1 : 2

~ 	private String[] stacktrace;

> INSERT  4 : 5  @  4

+ 		this.stacktrace = EagRuntime.getStackTraceElements(causeThrowable);

> CHANGE  26 : 37  @  26 : 54

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

> CHANGE  1 : 3  @  1 : 10

~ 			});
~ 		}

> CHANGE  12 : 13  @  12 : 13

~ 			this.stacktrace = (String[]) ArrayUtils

> CHANGE  7 : 8  @  7 : 8

~ 			for (String stacktraceelement : this.stacktrace) {

> CHANGE  16 : 17  @  16 : 27

~ 		StringBuilder stackTrace = new StringBuilder();

> CHANGE  1 : 8  @  1 : 2

~ 		if ((this.cause.getMessage() == null || this.cause.getMessage().length() == 0)
~ 				&& ((this.cause instanceof NullPointerException) || (this.cause instanceof StackOverflowError)
~ 						|| (this.cause instanceof OutOfMemoryError))) {
~ 			stackTrace.append(this.cause.getClass().getName()).append(": ");
~ 			stackTrace.append(this.description).append('\n');
~ 		} else {
~ 			stackTrace.append(this.cause.toString()).append('\n');

> CHANGE  2 : 5  @  2 : 3

~ 		EagRuntime.getStackTrace(this.cause, (s) -> {
~ 			stackTrace.append("\tat ").append(s).append('\n');
~ 		});

> CHANGE  1 : 2  @  1 : 12

~ 		return stackTrace.toString();

> DELETE  27  @  27 : 52

> CHANGE  12 : 15  @  12 : 15

~ 			String[] astacktraceelement = EagRuntime.getStackTraceElements(cause);
~ 			String stacktraceelement = null;
~ 			String stacktraceelement1 = null;

> CHANGE  21 : 22  @  21 : 22

~ 				this.stacktrace = new String[j];

> CHANGE  11 : 12  @  11 : 30

~ 		return "eagler";

> EOF

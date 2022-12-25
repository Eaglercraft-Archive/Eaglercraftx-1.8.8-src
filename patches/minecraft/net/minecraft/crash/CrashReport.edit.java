
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  4  @  7 : 9

> CHANGE  8 : 17  @  13 : 14

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  18  @  15 : 20

> DELETE  25  @  27 : 28

> CHANGE  26 : 27  @  29 : 30

~ 	private String[] stacktrace;

> INSERT  31 : 32  @  34

+ 		this.stacktrace = EagRuntime.getStackTraceElements(causeThrowable);

> CHANGE  58 : 69  @  60 : 88

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

> CHANGE  70 : 72  @  89 : 98

~ 			});
~ 		}

> CHANGE  84 : 85  @  110 : 111

~ 			this.stacktrace = (String[]) ArrayUtils

> CHANGE  92 : 93  @  118 : 119

~ 			for (String stacktraceelement : this.stacktrace) {

> CHANGE  109 : 110  @  135 : 146

~ 		StringBuilder stackTrace = new StringBuilder();

> CHANGE  111 : 118  @  147 : 148

~ 		if ((this.cause.getMessage() == null || this.cause.getMessage().length() == 0)
~ 				&& ((this.cause instanceof NullPointerException) || (this.cause instanceof StackOverflowError)
~ 						|| (this.cause instanceof OutOfMemoryError))) {
~ 			stackTrace.append(this.cause.getClass().getName()).append(": ");
~ 			stackTrace.append(this.description).append('\n');
~ 		} else {
~ 			stackTrace.append(this.cause.toString()).append('\n');

> CHANGE  120 : 123  @  150 : 151

~ 		EagRuntime.getStackTrace(this.cause, (s) -> {
~ 			stackTrace.append("\tat ").append(s).append('\n');
~ 		});

> CHANGE  124 : 125  @  152 : 163

~ 		return stackTrace.toString();

> DELETE  152  @  190 : 215

> CHANGE  164 : 167  @  227 : 230

~ 			String[] astacktraceelement = EagRuntime.getStackTraceElements(cause);
~ 			String stacktraceelement = null;
~ 			String stacktraceelement1 = null;

> CHANGE  188 : 189  @  251 : 252

~ 				this.stacktrace = new String[j];

> CHANGE  200 : 201  @  263 : 282

~ 		return "eagler";

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 7  @  2

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.HString;

> DELETE  2  @  2 : 3

> CHANGE  6 : 7  @  6 : 7

~ 	private String[] stackTrace = new String[0];

> CHANGE  7 : 8  @  7 : 8

~ 		return HString.format("%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(x), Double.valueOf(y),

> CHANGE  10 : 11  @  10 : 11

~ 			stringbuilder.append(HString.format("World: (%d,%d,%d)",

> CHANGE  17 : 18  @  17 : 18

~ 			stringbuilder.append(HString.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)",

> CHANGE  21 : 22  @  21 : 22

~ 					HString.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)",

> CHANGE  28 : 30  @  28 : 30

~ 		String[] astacktraceelement = EagRuntime.getStackTraceElements(new Exception());
~ 		if (astacktraceelement.length - 3 - size <= 0) {

> CHANGE  2 : 3  @  2 : 3

~ 			this.stackTrace = new String[astacktraceelement.length - 3 - size];

> CHANGE  5 : 6  @  5 : 6

~ 	public boolean firstTwoElementsOfStackTraceMatch(String s1, String s2) {

> CHANGE  1 : 3  @  1 : 6

~ 			String stacktraceelement = this.stackTrace[0];
~ 			if (s1.equals(stacktraceelement)) {

> CHANGE  17 : 18  @  17 : 18

~ 		String[] astacktraceelement = new String[this.stackTrace.length - amount];

> CHANGE  18 : 19  @  18 : 19

~ 			for (String stacktraceelement : this.stackTrace) {

> CHANGE  1 : 2  @  1 : 2

~ 				builder.append(stacktraceelement);

> CHANGE  5 : 6  @  5 : 6

~ 	public String[] getStackTrace() {

> CHANGE  9 : 11  @  9 : 11

~ 					return HString.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i),
~ 							blockIn.getUnlocalizedName(), blockIn.getClass().getName() });

> CHANGE  10 : 11  @  10 : 11

~ 					String s = HString.format("%4s", new Object[] { Integer.toBinaryString(blockData) }).replace(" ",

> CHANGE  1 : 2  @  1 : 2

~ 					return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(blockData), s });

> CHANGE  33 : 34  @  33 : 34

~ 				this.value = "~~ERROR~~ " + throwable.getClass().getName() + ": " + throwable.getMessage();

> EOF

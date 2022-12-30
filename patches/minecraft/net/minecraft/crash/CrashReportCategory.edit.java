
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 7  @  3

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.HString;

> DELETE  7  @  2 : 3

> CHANGE  6 : 7  @  7 : 8

~ 	private String[] stackTrace = new String[0];

> CHANGE  8 : 9  @  8 : 9

~ 		return HString.format("%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(x), Double.valueOf(y),

> CHANGE  11 : 12  @  11 : 12

~ 			stringbuilder.append(HString.format("World: (%d,%d,%d)",

> CHANGE  18 : 19  @  18 : 19

~ 			stringbuilder.append(HString.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)",

> CHANGE  22 : 23  @  22 : 23

~ 					HString.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)",

> CHANGE  29 : 31  @  29 : 31

~ 		String[] astacktraceelement = EagRuntime.getStackTraceElements(new Exception());
~ 		if (astacktraceelement.length - 3 - size <= 0) {

> CHANGE  4 : 5  @  4 : 5

~ 			this.stackTrace = new String[astacktraceelement.length - 3 - size];

> CHANGE  6 : 7  @  6 : 7

~ 	public boolean firstTwoElementsOfStackTraceMatch(String s1, String s2) {

> CHANGE  2 : 4  @  2 : 7

~ 			String stacktraceelement = this.stackTrace[0];
~ 			if (s1.equals(stacktraceelement)) {

> CHANGE  19 : 20  @  22 : 23

~ 		String[] astacktraceelement = new String[this.stackTrace.length - amount];

> CHANGE  19 : 20  @  19 : 20

~ 			for (String stacktraceelement : this.stackTrace) {

> CHANGE  2 : 3  @  2 : 3

~ 				builder.append(stacktraceelement);

> CHANGE  6 : 7  @  6 : 7

~ 	public String[] getStackTrace() {

> CHANGE  10 : 12  @  10 : 12

~ 					return HString.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i),
~ 							blockIn.getUnlocalizedName(), blockIn.getClass().getName() });

> CHANGE  12 : 13  @  12 : 13

~ 					String s = HString.format("%4s", new Object[] { Integer.toBinaryString(blockData) }).replace(" ",

> CHANGE  2 : 3  @  2 : 3

~ 					return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(blockData), s });

> CHANGE  34 : 35  @  34 : 35

~ 				this.value = "~~ERROR~~ " + throwable.getClass().getName() + ": " + throwable.getMessage();

> EOF

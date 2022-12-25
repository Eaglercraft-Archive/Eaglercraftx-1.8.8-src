
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  4 : 9  @  5

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.HString;

> DELETE  11  @  7 : 8

> CHANGE  17 : 18  @  14 : 15

~ 	private String[] stackTrace = new String[0];

> CHANGE  25 : 26  @  22 : 23

~ 		return HString.format("%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(x), Double.valueOf(y),

> CHANGE  36 : 37  @  33 : 34

~ 			stringbuilder.append(HString.format("World: (%d,%d,%d)",

> CHANGE  54 : 55  @  51 : 52

~ 			stringbuilder.append(HString.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)",

> CHANGE  76 : 77  @  73 : 74

~ 					HString.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)",

> CHANGE  105 : 107  @  102 : 104

~ 		String[] astacktraceelement = EagRuntime.getStackTraceElements(new Exception());
~ 		if (astacktraceelement.length - 3 - size <= 0) {

> CHANGE  109 : 110  @  106 : 107

~ 			this.stackTrace = new String[astacktraceelement.length - 3 - size];

> CHANGE  115 : 116  @  112 : 113

~ 	public boolean firstTwoElementsOfStackTraceMatch(String s1, String s2) {

> CHANGE  117 : 119  @  114 : 119

~ 			String stacktraceelement = this.stackTrace[0];
~ 			if (s1.equals(stacktraceelement)) {

> CHANGE  136 : 137  @  136 : 137

~ 		String[] astacktraceelement = new String[this.stackTrace.length - amount];

> CHANGE  155 : 156  @  155 : 156

~ 			for (String stacktraceelement : this.stackTrace) {

> CHANGE  157 : 158  @  157 : 158

~ 				builder.append(stacktraceelement);

> CHANGE  163 : 164  @  163 : 164

~ 	public String[] getStackTrace() {

> CHANGE  173 : 175  @  173 : 175

~ 					return HString.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i),
~ 							blockIn.getUnlocalizedName(), blockIn.getClass().getName() });

> CHANGE  185 : 186  @  185 : 186

~ 					String s = HString.format("%4s", new Object[] { Integer.toBinaryString(blockData) }).replace(" ",

> CHANGE  187 : 188  @  187 : 188

~ 					return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(blockData), s });

> CHANGE  221 : 222  @  221 : 222

~ 				this.value = "~~ERROR~~ " + throwable.getClass().getName() + ": " + throwable.getMessage();

> EOF

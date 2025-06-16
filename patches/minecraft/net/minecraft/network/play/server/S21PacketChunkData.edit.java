
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 5  @  2

+ 
+ import com.google.common.collect.Lists;
+ 

> INSERT  19 : 20  @  19

+ 		chunkIn.alfheim$getLightingEngine().processLightUpdates();

> CHANGE  9 : 10  @  9 : 10

~ 		this.extractedData.data = parPacketBuffer.readByteArray(0x1000000);

> CHANGE  30 : 31  @  30 : 31

~ 		ArrayList<ExtendedBlockStorage> arraylist = Lists.newArrayList();

> INSERT  13 : 16  @  13

+ 		int l = arraylist.size();
+ 		for (int k = 0; k < l; ++k) {
+ 			char[] achar = arraylist.get(k).getData();

> CHANGE  1 : 3  @  1 : 5

~ 			for (int m = 0; m < achar.length; ++m) {
~ 				char c0 = achar[m];

> CHANGE  5 : 7  @  5 : 8

~ 		for (int k = 0; k < l; ++k) {
~ 			j = func_179757_a(arraylist.get(k).getBlocklightArray().getData(), s21packetchunkdata$extracted.data, j);

> CHANGE  3 : 5  @  3 : 6

~ 			for (int k = 0; k < l; ++k) {
~ 				j = func_179757_a(arraylist.get(k).getSkylightArray().getData(), s21packetchunkdata$extracted.data, j);

> EOF

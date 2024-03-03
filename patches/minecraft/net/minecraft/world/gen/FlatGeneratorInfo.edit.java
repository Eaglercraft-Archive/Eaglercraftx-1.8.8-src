
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  5 : 10  @  5

+ import java.util.Set;
+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  4  @  4 : 5

> CHANGE  25 : 27  @  25 : 26

~ 		for (int j = 0, l = this.flatLayers.size(); j < l; ++j) {
~ 			FlatLayerInfo flatlayerinfo = this.flatLayers.get(j);

> CHANGE  36 : 37  @  36 : 37

~ 					for (Entry entry1 : (Set<Entry>) map.entrySet()) {

> CHANGE  88 : 90  @  88 : 90

~ 			for (int j = 0; j < astring.length; ++j) {
~ 				FlatLayerInfo flatlayerinfo = func_180715_a(parInt1, astring[j], i);

> CHANGE  36 : 38  @  36 : 38

~ 						for (int m = 0; m < astring1.length; ++m) {
~ 							String[] astring2 = astring1[m].split("\\(", 2);

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  11 : 13  @  11 : 12

~ 
~ 	private static final int NUM_RESULT_TYPES = CommandResultStats.Type._VALUES.length;

> CHANGE  79 : 82  @  79 : 80

~ 			CommandResultStats.Type[] types = CommandResultStats.Type.values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				CommandResultStats.Type commandresultstats$type = types[i];

> CHANGE  15 : 18  @  15 : 16

~ 		CommandResultStats.Type[] types = CommandResultStats.Type.values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			CommandResultStats.Type commandresultstats$type = types[i];

> CHANGE  36 : 39  @  36 : 37

~ 			CommandResultStats.Type[] types = CommandResultStats.Type.values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				CommandResultStats.Type commandresultstats$type = types[i];

> CHANGE  16 : 19  @  16 : 17

~ 		CommandResultStats.Type[] types = CommandResultStats.Type.values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			CommandResultStats.Type commandresultstats$type = types[i];

> INSERT  11 : 13  @  11

+ 		public static final Type[] _VALUES = values();
+ 

> CHANGE  17 : 18  @  17 : 18

~ 			String[] astring = new String[_VALUES.length];

> CHANGE  2 : 5  @  2 : 4

~ 			CommandResultStats.Type[] types = _VALUES;
~ 			for (int j = 0; j < types.length; ++j) {
~ 				astring[i++] = types[j].getTypeName();

> CHANGE  6 : 9  @  6 : 7

~ 			CommandResultStats.Type[] types = _VALUES;
~ 			for (int i = 0; i < types.length; ++i) {
~ 				CommandResultStats.Type commandresultstats$type = types[i];

> EOF

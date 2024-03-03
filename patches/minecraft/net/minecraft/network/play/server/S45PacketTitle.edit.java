
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> CHANGE  88 : 91  @  88 : 89

~ 			S45PacketTitle.Type[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				S45PacketTitle.Type s45packettitle$type = types[i];

> CHANGE  9 : 11  @  9 : 11

~ 			S45PacketTitle.Type[] types = values();
~ 			String[] astring = new String[types.length];

> CHANGE  1 : 3  @  1 : 3

~ 			for (int i = 0; i < types.length; ++i) {
~ 				astring[i] = types[i].name().toLowerCase();

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  64 : 65  @  64 : 65

~ 			for (Entity entity : (ArrayList<Entity>) list) {

> CHANGE  20 : 21  @  20 : 21

~ 				for (World world : (ArrayList<World>) list) {

> CHANGE  49 : 50  @  49 : 50

~ 		String s = func_179651_b(parMap, "type");

> INSERT  16 : 17  @  16

+ 			final String ss = s;

> CHANGE  2 : 3  @  2 : 3

~ 					return EntityList.isStringEntityName(entity, ss) != flag;

> CHANGE  49 : 50  @  49 : 50

~ 		String s = func_179651_b(parMap, "team");

> INSERT  6 : 7  @  6

+ 			final String ss = s;

> CHANGE  8 : 9  @  8 : 9

~ 						return s1.equals(ss) != flag;

> CHANGE  16 : 17  @  16 : 17

~ 					for (Entry entry : (Set<Entry>) map.entrySet()) {

> CHANGE  39 : 40  @  39 : 40

~ 		String s = func_179651_b(parMap, "name");

> INSERT  6 : 7  @  6

+ 			final String ss = s;

> CHANGE  2 : 3  @  2 : 3

~ 					return entity.getName().equals(ss) != flag;

> CHANGE  135 : 136  @  135 : 136

~ 			parList = (List<T>) Lists.newArrayList(entity);

> EOF

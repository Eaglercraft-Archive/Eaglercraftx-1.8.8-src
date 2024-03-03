
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  18  @  18 : 20

> CHANGE  38 : 39  @  38 : 39

~ 		List<Entity> list = matchEntities(sender, token, Entity.class);

> CHANGE  5 : 7  @  5 : 7

~ 			for (int i = 0, l = list.size(); i < l; ++i) {
~ 				arraylist.add(list.get(i).getDisplayName());

> CHANGE  16 : 17  @  16 : 17

~ 				List<World> list = getWorlds(sender, map);

> CHANGE  2 : 4  @  2 : 3

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					World world = list.get(i);

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

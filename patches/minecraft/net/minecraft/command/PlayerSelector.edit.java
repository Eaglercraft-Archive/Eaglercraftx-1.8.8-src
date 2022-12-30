
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  6  @  12 : 13

> INSERT  1 : 2  @  2

+ import java.util.Set;

> CHANGE  3 : 12  @  2 : 4

~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.base.Predicates;
~ import com.google.common.collect.ComparisonChain;
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 
~ import net.minecraft.client.Minecraft;

> DELETE  13  @  6 : 7

> DELETE  4  @  5 : 6

> DELETE  17  @  18 : 22

> CHANGE  13 : 14  @  17 : 18

~ 			for (Entity entity : (List<Entity>) list) {

> CHANGE  4 : 5  @  4 : 5

~ 			return IChatComponent.join(arraylist);

> CHANGE  17 : 18  @  17 : 18

~ 				for (World world : (List<World>) list) {

> CHANGE  27 : 31  @  27 : 28

~ 			Minecraft mc = Minecraft.getMinecraft();
~ 			if (mc.theWorld != null) {
~ 				arraylist.add(mc.thePlayer);
~ 			}

> CHANGE  26 : 28  @  23 : 25

~ 		String ss = func_179651_b(parMap, "type");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  3 : 4  @  3 : 4

~ 			ss = ss.substring(1);

> INSERT  2 : 3  @  2

+ 		final String s = ss;

> CHANGE  30 : 31  @  29 : 36

~ 					return false;

> CHANGE  14 : 15  @  20 : 26

~ 					return false;

> CHANGE  10 : 12  @  15 : 17

~ 		String ss = func_179651_b(parMap, "team");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  3 : 4  @  3 : 4

~ 			ss = ss.substring(1);

> INSERT  2 : 3  @  2

+ 		final String s = ss;

> CHANGE  22 : 23  @  21 : 22

~ 		final Map<String, Integer> map = func_96560_a(parMap);

> CHANGE  4 : 5  @  4 : 5

~ 					Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();

> CHANGE  15 : 16  @  15 : 17

~ 						String s1 = entity instanceof EntityPlayer ? entity.getName() : entity.getUniqueID().toString();

> CHANGE  26 : 28  @  27 : 29

~ 		String ss = func_179651_b(parMap, "name");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  3 : 4  @  3 : 4

~ 			ss = ss.substring(1);

> INSERT  2 : 3  @  2

+ 		final String s = ss;

> CHANGE  141 : 142  @  140 : 141

~ 			parList = (List<T>) Lists.newArrayList(new Entity[] { entity });

> INSERT  134 : 135  @  134

+ 

> EOF

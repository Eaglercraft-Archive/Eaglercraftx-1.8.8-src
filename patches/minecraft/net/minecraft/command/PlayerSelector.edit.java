
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  6  @  6 : 7

> INSERT  1 : 2  @  1

+ import java.util.Set;

> CHANGE  2 : 11  @  2 : 4

~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.base.Predicates;
~ import com.google.common.collect.ComparisonChain;
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 
~ import net.minecraft.client.Minecraft;

> DELETE  4  @  4 : 5

> DELETE  4  @  4 : 5

> DELETE  17  @  17 : 21

> CHANGE  13 : 14  @  13 : 14

~ 			for (Entity entity : (List<Entity>) list) {

> CHANGE  3 : 4  @  3 : 4

~ 			return IChatComponent.join(arraylist);

> CHANGE  16 : 17  @  16 : 17

~ 				for (World world : (List<World>) list) {

> CHANGE  26 : 30  @  26 : 27

~ 			Minecraft mc = Minecraft.getMinecraft();
~ 			if (mc.theWorld != null) {
~ 				arraylist.add(mc.thePlayer);
~ 			}

> CHANGE  22 : 24  @  22 : 24

~ 		String ss = func_179651_b(parMap, "type");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  1 : 2  @  1 : 2

~ 			ss = ss.substring(1);

> INSERT  1 : 2  @  1

+ 		final String s = ss;

> CHANGE  29 : 30  @  29 : 36

~ 					return false;

> CHANGE  13 : 14  @  13 : 19

~ 					return false;

> CHANGE  9 : 11  @  9 : 11

~ 		String ss = func_179651_b(parMap, "team");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  1 : 2  @  1 : 2

~ 			ss = ss.substring(1);

> INSERT  1 : 2  @  1

+ 		final String s = ss;

> CHANGE  21 : 22  @  21 : 22

~ 		final Map<String, Integer> map = func_96560_a(parMap);

> CHANGE  3 : 4  @  3 : 4

~ 					Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();

> CHANGE  14 : 15  @  14 : 16

~ 						String s1 = entity instanceof EntityPlayer ? entity.getName() : entity.getUniqueID().toString();

> CHANGE  25 : 27  @  25 : 27

~ 		String ss = func_179651_b(parMap, "name");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  1 : 2  @  1 : 2

~ 			ss = ss.substring(1);

> INSERT  1 : 2  @  1

+ 		final String s = ss;

> CHANGE  140 : 141  @  140 : 141

~ 			parList = (List<T>) Lists.newArrayList(new Entity[] { entity });

> INSERT  133 : 134  @  133

+ 

> EOF

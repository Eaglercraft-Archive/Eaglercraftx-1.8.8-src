
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  8  @  14 : 15

> INSERT  9 : 10  @  16

+ import java.util.Set;

> CHANGE  12 : 21  @  18 : 20

~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.base.Predicates;
~ import com.google.common.collect.ComparisonChain;
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 
~ import net.minecraft.client.Minecraft;

> DELETE  25  @  24 : 25

> DELETE  29  @  29 : 30

> DELETE  46  @  47 : 51

> CHANGE  59 : 60  @  64 : 65

~ 			for (Entity entity : (List<Entity>) list) {

> CHANGE  63 : 64  @  68 : 69

~ 			return IChatComponent.join(arraylist);

> CHANGE  80 : 81  @  85 : 86

~ 				for (World world : (List<World>) list) {

> CHANGE  107 : 111  @  112 : 113

~ 			Minecraft mc = Minecraft.getMinecraft();
~ 			if (mc.theWorld != null) {
~ 				arraylist.add(mc.thePlayer);
~ 			}

> CHANGE  133 : 135  @  135 : 137

~ 		String ss = func_179651_b(parMap, "type");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  136 : 137  @  138 : 139

~ 			ss = ss.substring(1);

> INSERT  138 : 139  @  140

+ 		final String s = ss;

> CHANGE  168 : 169  @  169 : 176

~ 					return false;

> CHANGE  182 : 183  @  189 : 195

~ 					return false;

> CHANGE  192 : 194  @  204 : 206

~ 		String ss = func_179651_b(parMap, "team");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  195 : 196  @  207 : 208

~ 			ss = ss.substring(1);

> INSERT  197 : 198  @  209

+ 		final String s = ss;

> CHANGE  219 : 220  @  230 : 231

~ 		final Map<String, Integer> map = func_96560_a(parMap);

> CHANGE  223 : 224  @  234 : 235

~ 					Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();

> CHANGE  238 : 239  @  249 : 251

~ 						String s1 = entity instanceof EntityPlayer ? entity.getName() : entity.getUniqueID().toString();

> CHANGE  264 : 266  @  276 : 278

~ 		String ss = func_179651_b(parMap, "name");
~ 		final boolean flag = ss != null && ss.startsWith("!");

> CHANGE  267 : 268  @  279 : 280

~ 			ss = ss.substring(1);

> INSERT  269 : 270  @  281

+ 		final String s = ss;

> CHANGE  410 : 411  @  421 : 422

~ 			parList = (List<T>) Lists.newArrayList(new Entity[] { entity });

> INSERT  544 : 545  @  555

+ 

> EOF

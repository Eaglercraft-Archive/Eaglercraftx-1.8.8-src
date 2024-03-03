
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  2 : 8  @  2

+ 
+ import com.google.common.collect.ComparisonChain;
+ import com.google.common.collect.Ordering;
+ 
+ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 3

> DELETE  2  @  2 : 3

> CHANGE  46 : 48  @  46 : 47

~ 		for (int m = 0, n = list.size(); m < n; ++m) {
~ 			NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo) list.get(m);

> CHANGE  20 : 21  @  20 : 22

~ 		boolean flag = true;

> CHANGE  15 : 17  @  15 : 17

~ 		List<String> list1 = null;
~ 		List<String> list2 = null;

> CHANGE  3 : 5  @  3 : 5

~ 			for (int m = 0, n = list1.size(); m < n; ++m) {
~ 				l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(list1.get(m)));

> CHANGE  6 : 8  @  6 : 8

~ 			for (int m = 0, n = list2.size(); m < n; ++m) {
~ 				l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(list2.get(m)));

> CHANGE  7 : 9  @  7 : 8

~ 			for (int m = 0, n = list1.size(); m < n; ++m) {
~ 				String s3 = list1.get(m);

> CHANGE  32 : 33  @  32 : 33

~ 					if (entityplayer == null || entityplayer.isWearing(EnumPlayerModelParts.HAT)) {

> CHANGE  33 : 35  @  33 : 34

~ 			for (int m = 0, n = list2.size(); m < n; ++m) {
~ 				String s4 = list2.get(m);

> EOF

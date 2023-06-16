
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

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

> CHANGE  46 : 47  @  46 : 47

~ 		for (NetworkPlayerInfo networkplayerinfo : (List<NetworkPlayerInfo>) list) {

> CHANGE  20 : 21  @  20 : 22

~ 		boolean flag = true;

> CHANGE  20 : 21  @  20 : 21

~ 			for (String s : (List<String>) list1) {

> CHANGE  7 : 8  @  7 : 8

~ 			for (String s2 : (List<String>) list2) {

> CHANGE  8 : 9  @  8 : 9

~ 			for (String s3 : (List<String>) list1) {

> CHANGE  32 : 33  @  32 : 33

~ 					if (entityplayer == null || entityplayer.isWearing(EnumPlayerModelParts.HAT)) {

> CHANGE  33 : 34  @  33 : 34

~ 			for (String s4 : (List<String>) list2) {

> EOF

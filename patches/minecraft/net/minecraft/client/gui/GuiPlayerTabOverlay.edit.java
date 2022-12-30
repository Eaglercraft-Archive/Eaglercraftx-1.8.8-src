
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  2 : 8  @  5

+ 
+ import com.google.common.collect.ComparisonChain;
+ import com.google.common.collect.Ordering;
+ 
+ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  7  @  1 : 3

> DELETE  2  @  4 : 5

> CHANGE  46 : 47  @  47 : 48

~ 		for (NetworkPlayerInfo networkplayerinfo : (List<NetworkPlayerInfo>) list) {

> CHANGE  21 : 22  @  21 : 23

~ 		boolean flag = true;

> CHANGE  21 : 22  @  22 : 23

~ 			for (String s : (List<String>) list1) {

> CHANGE  8 : 9  @  8 : 9

~ 			for (String s2 : (List<String>) list2) {

> CHANGE  9 : 10  @  9 : 10

~ 			for (String s3 : (List<String>) list1) {

> CHANGE  33 : 34  @  33 : 34

~ 					if (entityplayer == null || entityplayer.isWearing(EnumPlayerModelParts.HAT)) {

> CHANGE  34 : 35  @  34 : 35

~ 			for (String s4 : (List<String>) list2) {

> EOF

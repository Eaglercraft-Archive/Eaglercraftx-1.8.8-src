
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  4 : 10  @  7

+ 
+ import com.google.common.collect.ComparisonChain;
+ import com.google.common.collect.Ordering;
+ 
+ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  11  @  8 : 10

> DELETE  13  @  12 : 13

> CHANGE  59 : 60  @  59 : 60

~ 		for (NetworkPlayerInfo networkplayerinfo : (List<NetworkPlayerInfo>) list) {

> CHANGE  80 : 81  @  80 : 82

~ 		boolean flag = true;

> CHANGE  101 : 102  @  102 : 103

~ 			for (String s : (List<String>) list1) {

> CHANGE  109 : 110  @  110 : 111

~ 			for (String s2 : (List<String>) list2) {

> CHANGE  118 : 119  @  119 : 120

~ 			for (String s3 : (List<String>) list1) {

> CHANGE  151 : 152  @  152 : 153

~ 					if (entityplayer == null || entityplayer.isWearing(EnumPlayerModelParts.HAT)) {

> CHANGE  185 : 186  @  186 : 187

~ 			for (String s4 : (List<String>) list2) {

> EOF

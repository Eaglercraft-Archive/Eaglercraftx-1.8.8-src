
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  4 : 10  @  4

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 2

> CHANGE  72 : 73  @  72 : 73

~ 					long i = EagRuntime.steadyTimeMillis();

> CHANGE  22 : 25  @  22 : 25

~ 				for (int i = 0, l = list1.size(); i < l; ++i) {
~ 					arraylist.add("textures/entity/banner/"
~ 							+ ((TileEntityBanner.EnumBannerPattern) list1.get(i)).getPatternName() + ".png");

> CHANGE  10 : 11  @  10 : 11

~ 			tileentitybannerrenderer$timedbannertexture.systemTime = EagRuntime.steadyTimeMillis();

> EOF

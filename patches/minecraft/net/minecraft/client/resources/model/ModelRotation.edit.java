
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 6  @  1

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;

> DELETE  2  @  2 : 4

> CHANGE  75 : 78  @  75 : 77

~ 		ModelRotation[] lst = values();
~ 		for (int i = 0; i < lst.length; ++i) {
~ 			mapRotations.put(Integer.valueOf(lst[i].combinedXY), lst[i]);

> EOF

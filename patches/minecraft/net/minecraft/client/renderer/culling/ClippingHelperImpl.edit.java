
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  5  @  2 : 4

> DELETE  4  @  6 : 9

> INSERT  3 : 4  @  6

+ 		instance.destroy();

> DELETE  14  @  13 : 18

> CHANGE  2 : 4  @  7 : 11

~ 		GlStateManager.getFloat(2983, afloat);
~ 		GlStateManager.getFloat(2982, afloat1);

> INSERT  71 : 75  @  73

+ 
+ 	public void destroy() {
+ 	}
+ 

> EOF

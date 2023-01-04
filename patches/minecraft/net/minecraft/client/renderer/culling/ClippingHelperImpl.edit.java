
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 3

> DELETE  4  @  4 : 7

> INSERT  3 : 4  @  3

+ 		instance.destroy();

> DELETE  13  @  13 : 18

> CHANGE  2 : 4  @  2 : 6

~ 		GlStateManager.getFloat(2983, afloat);
~ 		GlStateManager.getFloat(2982, afloat1);

> INSERT  69 : 73  @  69

+ 
+ 	public void destroy() {
+ 	}
+ 

> EOF

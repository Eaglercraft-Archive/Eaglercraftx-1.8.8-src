
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  7  @  4 : 6

> DELETE  11  @  10 : 13

> INSERT  14 : 15  @  16

+ 		instance.destroy();

> DELETE  28  @  29 : 34

> CHANGE  30 : 32  @  36 : 40

~ 		GlStateManager.getFloat(2983, afloat);
~ 		GlStateManager.getFloat(2982, afloat1);

> INSERT  101 : 105  @  109

+ 
+ 	public void destroy() {
+ 	}
+ 

> EOF

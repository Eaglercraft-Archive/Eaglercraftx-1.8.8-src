
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  3  @  3 : 4

> DELETE  1  @  1 : 2

> CHANGE  17 : 19  @  17 : 18

~ 			boolean flag = DeferredStateManager.isEnableShadowRender();
~ 			GlStateManager.cullFace(flag ? GL_BACK : GL_FRONT);

> CHANGE  23 : 24  @  23 : 24

~ 			GlStateManager.cullFace(flag ? GL_FRONT : GL_BACK);

> EOF

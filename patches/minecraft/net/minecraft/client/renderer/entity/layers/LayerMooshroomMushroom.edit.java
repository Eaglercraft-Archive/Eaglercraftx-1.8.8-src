
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  3  @  3 : 4

> DELETE  1  @  1 : 2

> INSERT  17 : 19  @  17

+ 			// boolean flag = DeferredStateManager.isEnableShadowRender();
+ 			// GlStateManager.cullFace(flag ? GL_BACK : GL_FRONT);

> INSERT  24 : 25  @  24

+ 			// GlStateManager.cullFace(flag ? GL_FRONT : GL_BACK);

> EOF

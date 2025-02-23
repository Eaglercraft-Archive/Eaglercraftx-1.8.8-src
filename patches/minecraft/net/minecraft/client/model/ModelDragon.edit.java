
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> INSERT  154 : 155  @  154

+ 		// boolean flag = DeferredStateManager.isEnableShadowRender();

> INSERT  18 : 19  @  18

+ 				// GlStateManager.cullFace(flag ? GL_BACK : GL_FRONT);

> INSERT  5 : 6  @  5

+ 		// GlStateManager.cullFace(flag ? GL_FRONT : GL_BACK);

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 5

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> INSERT  154 : 155  @  154

+ 		boolean flag = DeferredStateManager.isEnableShadowRender();

> CHANGE  18 : 19  @  18 : 19

~ 				GlStateManager.cullFace(flag ? GL_BACK : GL_FRONT);

> CHANGE  4 : 5  @  4 : 5

~ 		GlStateManager.cullFace(flag ? GL_FRONT : GL_BACK);

> EOF

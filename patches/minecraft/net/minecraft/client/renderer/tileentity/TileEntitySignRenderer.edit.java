
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;
+ import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
+ 

> INSERT  1 : 6  @  1

+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;

> DELETE  4  @  4 : 6

> DELETE  4  @  4 : 5

> CHANGE  55 : 56  @  55 : 56

~ 		EaglercraftGPU.glNormal3f(0.0F, 0.0F, -1.0F * f3);

> INSERT  3 : 8  @  3

+ 			if (DeferredStateManager.isInDeferredPass()) {
+ 				_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
+ 				GlStateManager.colorMask(true, true, true, false);
+ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 			}

> INSERT  15 : 19  @  15

+ 			if (DeferredStateManager.isInDeferredPass()) {
+ 				_wglDrawBuffers(EaglerDeferredPipeline.instance.gBufferDrawBuffers);
+ 				GlStateManager.colorMask(true, true, true, true);
+ 			}

> EOF

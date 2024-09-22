
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

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

> INSERT  5 : 7  @  5

+ 	public static boolean disableProfanityFilter = false;
+ 

> CHANGE  50 : 51  @  50 : 51

~ 		EaglercraftGPU.glNormal3f(0.0F, 0.0F, -1.0F * f3);

> CHANGE  3 : 13  @  3 : 6

~ 			if (DeferredStateManager.isInDeferredPass()) {
~ 				_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
~ 				GlStateManager.colorMask(true, true, true, false);
~ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
~ 			}
~ 			IChatComponent[] signText = disableProfanityFilter ? tileentitysign.signText
~ 					: tileentitysign.getSignTextProfanityFilter();
~ 			for (int j = 0; j < signText.length; ++j) {
~ 				if (signText[j] != null) {
~ 					IChatComponent ichatcomponent = signText[j];

> CHANGE  4 : 6  @  4 : 6

~ 						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - signText.length * 5,
~ 								b0);

> CHANGE  1 : 3  @  1 : 3

~ 						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - signText.length * 5,
~ 								b0);

> INSERT  3 : 7  @  3

+ 			if (DeferredStateManager.isInDeferredPass()) {
+ 				_wglDrawBuffers(EaglerDeferredPipeline.instance.gBufferDrawBuffers);
+ 				GlStateManager.colorMask(true, true, true, true);
+ 			}

> EOF

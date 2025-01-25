
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  1 : 7  @  1 : 4

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 3

> CHANGE  126 : 127  @  126 : 129

~ 					GlStateManager.rotateZYXRad(this.rotateAngleX, this.rotateAngleY, this.rotateAngleZ);

> DELETE  1  @  1 : 9

> DELETE  25  @  25 : 28

> CHANGE  1 : 3  @  1 : 4

~ 				// note: vanilla order for this function is YXZ not ZYX for some reason
~ 				GlStateManager.rotateZYXRad(this.rotateAngleX, this.rotateAngleY, this.rotateAngleZ);

> DELETE  1  @  1 : 5

> CHANGE  21 : 22  @  21 : 32

~ 					GlStateManager.rotateZYXRad(this.rotateAngleX, this.rotateAngleY, this.rotateAngleZ);

> CHANGE  7 : 9  @  7 : 9

~ 		this.displayList = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.displayList, GL_COMPILE);

> CHANGE  6 : 7  @  6 : 7

~ 		EaglercraftGPU.glEndList();

> EOF

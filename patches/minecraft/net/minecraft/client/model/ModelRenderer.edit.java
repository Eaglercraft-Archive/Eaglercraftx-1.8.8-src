
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

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

> CHANGE  214 : 216  @  214 : 216

~ 		this.displayList = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.displayList, GL_COMPILE);

> CHANGE  6 : 7  @  6 : 7

~ 		EaglercraftGPU.glEndList();

> EOF

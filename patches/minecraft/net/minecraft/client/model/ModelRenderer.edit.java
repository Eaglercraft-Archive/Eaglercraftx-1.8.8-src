
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  3 : 9  @  2 : 5

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  7  @  4 : 5

> DELETE  1  @  2 : 4

> CHANGE  214 : 216  @  216 : 218

~ 		this.displayList = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.displayList, GL_COMPILE);

> CHANGE  8 : 9  @  8 : 9

~ 		EaglercraftGPU.glEndList();

> EOF

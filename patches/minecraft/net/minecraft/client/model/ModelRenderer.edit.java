
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  5 : 11  @  4 : 7

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  12  @  8 : 9

> DELETE  13  @  10 : 12

> CHANGE  227 : 229  @  226 : 228

~ 		this.displayList = GLAllocation.generateDisplayLists();
~ 		EaglercraftGPU.glNewList(this.displayList, GL_COMPILE);

> CHANGE  235 : 236  @  234 : 235

~ 		EaglercraftGPU.glEndList();

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 7  @  2

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  22  @  16 : 18

> DELETE  2  @  4 : 5

> DELETE  1  @  2 : 56

> CHANGE  163 : 165  @  217 : 218

~ 		this.skinMap.put("slim", new RenderPlayer(this, true, false));
~ 		this.skinMap.put("zombie", new RenderPlayer(this, false, true));

> CHANGE  13 : 14  @  12 : 13

~ 			render = this.getEntityClassRenderObject((Class<? extends Entity>) parClass1.getSuperclass());

> CHANGE  7 : 8  @  7 : 8

~ 	public <T extends Entity> Render getEntityRenderObject(Entity entityIn) {

> EOF

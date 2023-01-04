
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 7  @  1

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  16  @  16 : 18

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 55

> CHANGE  163 : 165  @  163 : 164

~ 		this.skinMap.put("slim", new RenderPlayer(this, true, false));
~ 		this.skinMap.put("zombie", new RenderPlayer(this, false, true));

> CHANGE  11 : 12  @  11 : 12

~ 			render = this.getEntityClassRenderObject((Class<? extends Entity>) parClass1.getSuperclass());

> CHANGE  6 : 7  @  6 : 7

~ 	public <T extends Entity> Render getEntityRenderObject(Entity entityIn) {

> EOF

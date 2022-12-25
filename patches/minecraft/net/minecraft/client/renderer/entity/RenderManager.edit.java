
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 9  @  4

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  25  @  20 : 22

> DELETE  27  @  24 : 25

> DELETE  28  @  26 : 80

> CHANGE  191 : 193  @  243 : 244

~ 		this.skinMap.put("slim", new RenderPlayer(this, true, false));
~ 		this.skinMap.put("zombie", new RenderPlayer(this, false, true));

> CHANGE  204 : 205  @  255 : 256

~ 			render = this.getEntityClassRenderObject((Class<? extends Entity>) parClass1.getSuperclass());

> CHANGE  211 : 212  @  262 : 263

~ 	public <T extends Entity> Render getEntityRenderObject(Entity entityIn) {

> EOF

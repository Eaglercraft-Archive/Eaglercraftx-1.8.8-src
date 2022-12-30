
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 8  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  9  @  5 : 7

> DELETE  6  @  8 : 10

> CHANGE  2 : 6  @  4 : 8

~ 	private static final int[] VIEWPORT = new int[4];
~ 	private static final float[] MODELVIEW = new float[16];
~ 	private static final float[] PROJECTION = new float[16];
~ 	private static final float[] OBJECTCOORDS = new float[3];

> CHANGE  14 : 19  @  14 : 19

~ 		EaglercraftGPU.glGetInteger(GL_VIEWPORT, VIEWPORT);
~ 		float f = (float) ((VIEWPORT[0] + VIEWPORT[2]) / 2);
~ 		float f1 = (float) ((VIEWPORT[1] + VIEWPORT[3]) / 2);
~ 		GlStateManager.gluUnProject(f, f1, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
~ 		position = new Vec3((double) OBJECTCOORDS[0], (double) OBJECTCOORDS[1], (double) OBJECTCOORDS[2]);

> CHANGE  17 : 18  @  17 : 18

~ 		double d1 = parEntity.prevPosY + (parEntity.posY - parEntity.prevPosY) * parDouble1 + parEntity.getEyeHeight();

> EOF

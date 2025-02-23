
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldVertexBufferUploader;

> CHANGE  3 : 12  @  3 : 4

~ 
~ 	public static final int GL_TRIANGLES = RealOpenGLEnums.GL_TRIANGLES;
~ 	public static final int GL_TRIANGLE_STRIP = RealOpenGLEnums.GL_TRIANGLE_STRIP;
~ 	public static final int GL_TRIANGLE_FAN = RealOpenGLEnums.GL_TRIANGLE_FAN;
~ 	public static final int GL_QUADS = RealOpenGLEnums.GL_QUADS;
~ 	public static final int GL_LINES = RealOpenGLEnums.GL_LINES;
~ 	public static final int GL_LINE_STRIP = RealOpenGLEnums.GL_LINE_STRIP;
~ 	public static final int GL_LINE_LOOP = RealOpenGLEnums.GL_LINE_LOOP;
~ 

> CHANGE  12 : 13  @  12 : 13

~ 		WorldVertexBufferUploader.func_181679_a(this.worldRenderer);

> INSERT  2 : 7  @  2

+ 	public void uploadDisplayList(int displayList) {
+ 		this.worldRenderer.finishDrawing();
+ 		WorldVertexBufferUploader.uploadDisplayList(displayList, this.worldRenderer);
+ 	}
+ 

> EOF

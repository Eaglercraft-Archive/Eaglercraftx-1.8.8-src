package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.webgl.WebGLBuffer;
import org.teavm.jso.webgl.WebGLProgram;
import org.teavm.jso.webgl.WebGLRenderingContext;
import org.teavm.jso.webgl.WebGLTexture;
import org.teavm.jso.webgl.WebGLUniformLocation;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public interface WebGL2RenderingContext extends WebGLRenderingContext {

	int TEXTURE_MAX_LEVEL              = 0x0000813D;
	int TEXTURE_MAX_ANISOTROPY_EXT     = 0x000084FE;
	int UNSIGNED_INT_24_8              = 0x000084FA;
	int ANY_SAMPLES_PASSED             = 0x00008D6A; 
	int QUERY_RESULT                   = 0x00008866;
	int QUERY_RESULT_AVAILABLE         = 0x00008867;
	int DEPTH24_STENCIL8               = 0x000088F0;
	int DEPTH_COMPONENT24              = 0x000081A6;
	int DEPTH_COMPONENT32F             = 0x00008CAC;
	int READ_FRAMEBUFFER               = 0x00008CA8;
	int DRAW_FRAMEBUFFER               = 0x00008CA9;
	int RGB8                           = 0x00008051;
	int RGBA8                          = 0x00008058;
	int R8                             = 0x00008229;
	int RED                            = 0x00001903;
	
	WebGLQuery createQuery();

	void beginQuery(int p1, WebGLQuery obj);

	void endQuery(int p1);

	void deleteQuery(WebGLQuery obj);

	int getQueryParameter(WebGLQuery obj, int p2);

	WebGLVertexArray createVertexArray();

	void deleteVertexArray(WebGLVertexArray obj);  

	void bindVertexArray(WebGLVertexArray obj); 
	
	void renderbufferStorageMultisample(int p1, int p2, int p3, int p4, int p5);
	
	void blitFramebuffer(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10);

	void drawBuffers(int[] p1);
	
	void readBuffer(int p1);

	void vertexAttribDivisor(int p1, int p2);

	void drawArraysInstanced(int p1, int p2, int p3, int p4);

	void drawElementsInstanced(int p1, int p2, int p3, int p4, int p5);

	int getUniformBlockIndex(WebGLProgram p1, String p2);

	void bindBufferRange(int p1, int p2, WebGLBuffer p3, int p4, int p5);

	void uniformBlockBinding(WebGLProgram p1, int p2, int p3);

	void uniformMatrix3x2fv(WebGLUniformLocation location, boolean transpose, Float32Array value);

	void uniformMatrix4x2fv(WebGLUniformLocation location, boolean transpose, Float32Array value);

	void uniformMatrix4x3fv(WebGLUniformLocation location, boolean transpose, Float32Array value);

	void texStorage2D(int target, int levels, int internalFormat, int width, int height);

	void texImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format,
			int type, ArrayBufferView pixels);

	void framebufferTextureLayer(int target, int attachment, WebGLTexture texture, int level, int layer);

}

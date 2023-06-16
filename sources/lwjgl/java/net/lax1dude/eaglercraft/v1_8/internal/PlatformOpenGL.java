package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerLWJGLAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

import static org.lwjgl.opengles.GLES30.*;

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
public class PlatformOpenGL {

	public static final void _wglEnable(int glEnum) {
		glEnable(glEnum);
	}

	public static final void _wglDisable(int glEnum) {
		glDisable(glEnum);
	}

	public static final void _wglClearColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}

	public static final void _wglClearDepth(float f) {
		glClearDepthf(f);
	}

	public static final void _wglClear(int bits) {
		glClear(bits);
	}

	public static final void _wglDepthFunc(int glEnum) {
		glDepthFunc(glEnum);
	}

	public static final void _wglDepthMask(boolean mask) {
		glDepthMask(mask);
	}

	public static final void _wglCullFace(int glEnum) {
		glCullFace(glEnum);
	}

	public static final void _wglViewport(int x, int y, int w, int h) {
		glViewport(x, y, w, h);
	}

	public static final void _wglBlendFunc(int src, int dst) {
		glBlendFunc(src, dst);
	}

	public static final void _wglBlendFuncSeparate(int srcColor, int dstColor, int srcAlpha, int dstAlpha) {
		glBlendFuncSeparate(srcColor, dstColor, srcAlpha, dstAlpha);
	}

	public static final void _wglBlendEquation(int glEnum) {
		glBlendEquation(glEnum);
	}

	public static final void _wglColorMask(boolean r, boolean g, boolean b, boolean a) {
		glColorMask(r, g, b, a);
	}

	public static final void _wglDrawBuffers(int buffer) {
		glDrawBuffers(buffer);
	}

	public static final void _wglDrawBuffers(int[] buffers) {
		glDrawBuffers(buffers);
	}

	public static final void _wglReadBuffer(int buffer) {
		glReadBuffer(buffer);
	}

	public static final void _wglPolygonOffset(float f1, float f2) {
		glPolygonOffset(f1, f2);
	}

	public static final void _wglLineWidth(float width) {
		glLineWidth(width);
	}

	public static final IBufferGL _wglGenBuffers() {
		return new OpenGLObjects.BufferGL(glGenBuffers());
	}

	public static final ITextureGL _wglGenTextures() {
		return new OpenGLObjects.TextureGL(glGenTextures());
	}

	public static final IBufferArrayGL _wglGenVertexArrays() {
		return new OpenGLObjects.BufferArrayGL(glGenVertexArrays());
	}

	public static final IProgramGL _wglCreateProgram() {
		return new OpenGLObjects.ProgramGL(glCreateProgram());
	}

	public static final IShaderGL _wglCreateShader(int type) {
		return new OpenGLObjects.ShaderGL(glCreateShader(type));
	}

	public static final IFramebufferGL _wglCreateFramebuffer() {
		return new OpenGLObjects.FramebufferGL(glGenFramebuffers());
	}

	public static final IRenderbufferGL _wglCreateRenderbuffer() {
		return new OpenGLObjects.RenderbufferGL(glGenRenderbuffers());
	}

	public static final IQueryGL _wglGenQueries() {
		return new OpenGLObjects.QueryGL(glGenQueries());
	}

	public static final void _wglDeleteBuffers(IBufferGL obj) {
		glDeleteBuffers(((OpenGLObjects.BufferGL) obj).ptr);
	}

	public static final void _wglDeleteTextures(ITextureGL obj) {
		glDeleteTextures(((OpenGLObjects.TextureGL) obj).ptr);
	}

	public static final void _wglDeleteVertexArrays(IBufferArrayGL obj) {
		glDeleteVertexArrays(((OpenGLObjects.BufferArrayGL) obj).ptr);
	}

	public static final void _wglDeleteProgram(IProgramGL obj) {
		glDeleteProgram(((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static final void _wglDeleteShader(IShaderGL obj) {
		glDeleteShader(((OpenGLObjects.ShaderGL) obj).ptr);
	}

	public static final void _wglDeleteFramebuffer(IFramebufferGL obj) {
		glDeleteFramebuffers(((OpenGLObjects.FramebufferGL) obj).ptr);
	}

	public static final void _wglDeleteRenderbuffer(IRenderbufferGL obj) {
		glDeleteRenderbuffers(((OpenGLObjects.RenderbufferGL) obj).ptr);
	}

	public static final void _wglDeleteQueries(IQueryGL obj) {
		glDeleteQueries(((OpenGLObjects.QueryGL) obj).ptr);
	}

	public static final void _wglBindBuffer(int target, IBufferGL obj) {
		glBindBuffer(target, obj == null ? 0 : ((OpenGLObjects.BufferGL) obj).ptr);
	}

	public static final void _wglBufferData(int target, ByteBuffer data, int usage) {
		nglBufferData(target, data == null ? 0 : data.remaining(),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data), usage);
	}

	public static final void _wglBufferData(int target, IntBuffer data, int usage) {
		nglBufferData(target, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data), usage);
	}

	public static final void _wglBufferData(int target, FloatBuffer data, int usage) {
		nglBufferData(target, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data), usage);
	}

	public static final void _wglBufferData(int target, int size, int usage) {
		glBufferData(target, size, usage);
	}

	public static final void _wglBufferSubData(int target, int offset, ByteBuffer data) {
		nglBufferSubData(target, offset, data == null ? 0 : data.remaining(),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglBufferSubData(int target, int offset, IntBuffer data) {
		nglBufferSubData(target, offset, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglBufferSubData(int target, int offset, FloatBuffer data) {
		nglBufferSubData(target, offset, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglBindVertexArray(IBufferArrayGL obj) {
		glBindVertexArray(obj == null ? 0 : ((OpenGLObjects.BufferArrayGL) obj).ptr);
	}

	public static final void _wglEnableVertexAttribArray(int index) {
		glEnableVertexAttribArray(index);
	}

	public static final void _wglDisableVertexAttribArray(int index) {
		glDisableVertexAttribArray(index);
	}

	public static final void _wglVertexAttribPointer(int index, int size, int type, boolean normalized, int stride,
			int offset) {
		glVertexAttribPointer(index, size, type, normalized, stride, offset);
	}

	public static final void _wglVertexAttribDivisor(int index, int divisor) {
		glVertexAttribDivisor(index, divisor);
	}

	public static final void _wglActiveTexture(int texture) {
		glActiveTexture(texture);
	}

	public static final void _wglBindTexture(int target, ITextureGL obj) {
		glBindTexture(target, obj == null ? 0 : ((OpenGLObjects.TextureGL) obj).ptr);
	}

	public static final void _wglTexParameterf(int target, int param, float value) {
		glTexParameterf(target, param, value);
	}

	public static final void _wglTexParameteri(int target, int param, int value) {
		glTexParameteri(target, param, value);
	}

	public static final void _wglTexImage2D(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, ByteBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglTexImage2D(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, IntBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglTexImage2D(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, FloatBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, ByteBuffer data) {
		glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, IntBuffer data) {
		glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, FloatBuffer data) {
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static final void _wglCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y,
			int width, int height) {
		glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}

	public static final void _wglPixelStorei(int pname, int value) {
		glPixelStorei(pname, value);
	}

	public static final void _wglGenerateMipmap(int target) {
		glGenerateMipmap(target);
	}

	public static final void _wglShaderSource(IShaderGL obj, String source) {
		glShaderSource(((OpenGLObjects.ShaderGL) obj).ptr, source);
	}

	public static final void _wglCompileShader(IShaderGL obj) {
		glCompileShader(((OpenGLObjects.ShaderGL) obj).ptr);
	}

	public static final int _wglGetShaderi(IShaderGL obj, int param) {
		return glGetShaderi(((OpenGLObjects.ShaderGL) obj).ptr, param);
	}

	public static final String _wglGetShaderInfoLog(IShaderGL obj) {
		return glGetShaderInfoLog(((OpenGLObjects.ShaderGL) obj).ptr);
	}

	public static final void _wglUseProgram(IProgramGL obj) {
		glUseProgram(obj == null ? 0 : ((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static final void _wglAttachShader(IProgramGL obj, IShaderGL shader) {
		glAttachShader(((OpenGLObjects.ProgramGL) obj).ptr, ((OpenGLObjects.ShaderGL) shader).ptr);
	}

	public static final void _wglDetachShader(IProgramGL obj, IShaderGL shader) {
		glDetachShader(((OpenGLObjects.ProgramGL) obj).ptr, ((OpenGLObjects.ShaderGL) shader).ptr);
	}

	public static final void _wglLinkProgram(IProgramGL obj) {
		glLinkProgram(((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static final int _wglGetProgrami(IProgramGL obj, int param) {
		return glGetProgrami(((OpenGLObjects.ProgramGL) obj).ptr, param);
	}

	public static final String _wglGetProgramInfoLog(IProgramGL obj) {
		return glGetProgramInfoLog(((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static final void _wglBindAttribLocation(IProgramGL obj, int index, String name) {
		glBindAttribLocation(((OpenGLObjects.ProgramGL) obj).ptr, index, name);
	}

	public static final int _wglGetAttribLocation(IProgramGL obj, String name) {
		return glGetAttribLocation(((OpenGLObjects.ProgramGL) obj).ptr, name);
	}

	public static final void _wglDrawArrays(int mode, int first, int count) {
		glDrawArrays(mode, first, count);
	}

	public static final void _wglDrawArraysInstanced(int mode, int first, int count, int instanced) {
		glDrawArraysInstanced(mode, first, count, instanced);
	}

	public static final void _wglDrawElements(int mode, int count, int type, int offset) {
		glDrawElements(mode, count, type, offset);
	}

	public static final void _wglDrawElementsInstanced(int mode, int count, int type, int offset, int instanced) {
		glDrawElementsInstanced(mode, count, type, offset, instanced);
	}

	public static final IUniformGL _wglGetUniformLocation(IProgramGL obj, String name) {
		int loc = glGetUniformLocation(((OpenGLObjects.ProgramGL) obj).ptr, name);
		return loc < 0 ? null : new OpenGLObjects.UniformGL(loc);
	}

	public static final void _wglUniform1f(IUniformGL obj, float x) {
		if (obj != null)
			glUniform1f(((OpenGLObjects.UniformGL) obj).ptr, x);
	}

	public static final void _wglUniform2f(IUniformGL obj, float x, float y) {
		if (obj != null)
			glUniform2f(((OpenGLObjects.UniformGL) obj).ptr, x, y);
	}

	public static final void _wglUniform3f(IUniformGL obj, float x, float y, float z) {
		if (obj != null)
			glUniform3f(((OpenGLObjects.UniformGL) obj).ptr, x, y, z);
	}

	public static final void _wglUniform4f(IUniformGL obj, float x, float y, float z, float w) {
		if (obj != null)
			glUniform4f(((OpenGLObjects.UniformGL) obj).ptr, x, y, z, w);
	}

	public static final void _wglUniform1i(IUniformGL obj, int x) {
		if (obj != null)
			glUniform1i(((OpenGLObjects.UniformGL) obj).ptr, x);
	}

	public static final void _wglUniform2i(IUniformGL obj, int x, int y) {
		if (obj != null)
			glUniform2i(((OpenGLObjects.UniformGL) obj).ptr, x, y);
	}

	public static final void _wglUniform3i(IUniformGL obj, int x, int y, int z) {
		if (obj != null)
			glUniform3i(((OpenGLObjects.UniformGL) obj).ptr, x, y, z);
	}

	public static final void _wglUniform4i(IUniformGL obj, int x, int y, int z, int w) {
		if (obj != null)
			glUniform4i(((OpenGLObjects.UniformGL) obj).ptr, x, y, z, w);
	}

	public static final void _wglUniformMatrix2fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix2fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() >> 2, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static final void _wglUniformMatrix3fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix3fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() / 9, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static final void _wglUniformMatrix4fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix4fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() >> 4, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static final void _wglBindFramebuffer(int target, IFramebufferGL framebuffer) {
		glBindFramebuffer(target, framebuffer == null ? 0 : ((OpenGLObjects.FramebufferGL) framebuffer).ptr);
	}

	public static final int _wglCheckFramebufferStatus(int target) {
		return glCheckFramebufferStatus(target);
	}

	public static final void _wglFramebufferTexture2D(int target, int attachment, int texTarget, ITextureGL texture,
			int level) {
		glFramebufferTexture2D(target, attachment, texTarget, ((OpenGLObjects.TextureGL) texture).ptr, level);
	}

	public static final void _wglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0,
			int dstX1, int dstY1, int bits, int filter) {
		glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, bits, filter);
	}

	public static final void _wglBindRenderbuffer(int target, IRenderbufferGL renderbuffer) {
		glBindRenderbuffer(target, renderbuffer == null ? 0 : ((OpenGLObjects.RenderbufferGL) renderbuffer).ptr);
	}

	public static final void _wglRenderbufferStorage(int target, int internalformat, int width, int height) {
		glRenderbufferStorage(target, internalformat, width, height);
	}

	public static final void _wglFramebufferRenderbuffer(int target, int attachment, int renderbufferTarget,
			IRenderbufferGL renderbuffer) {
		glFramebufferRenderbuffer(target, attachment, renderbufferTarget,
				((OpenGLObjects.RenderbufferGL) renderbuffer).ptr);
	}

	public static final String _wglGetString(int param) {
		return glGetString(param);
	}

	public static final int _wglGetInteger(int param) {
		return glGetInteger(param);
	}

	public static final int _wglGetError() {
		return glGetError();
	}

}

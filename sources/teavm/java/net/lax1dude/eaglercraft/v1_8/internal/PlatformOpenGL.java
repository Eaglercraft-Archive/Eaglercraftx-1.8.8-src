package net.lax1dude.eaglercraft.v1_8.internal;

import org.teavm.jso.webgl.WebGLUniformLocation;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerArrayBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGL2RenderingContext;
import net.lax1dude.eaglercraft.v1_8.log4j.Level;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

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
	
	private static final Logger logger = LogManager.getLogger("PlatformOpenGL");
	
	static WebGL2RenderingContext ctx = null;

	static boolean hasDebugRenderInfoExt = false;
	static boolean hasFramebufferHDR16FSupport = false;
	static boolean hasFramebufferHDR32FSupport = false;
	
	static void setCurrentContext(WebGL2RenderingContext context) {
		ctx = context;
		hasDebugRenderInfoExt = ctx.getExtension("WEBGL_debug_renderer_info") != null;
		hasFramebufferHDR16FSupport = ctx.getExtension("EXT_color_buffer_half_float") != null;
		hasFramebufferHDR32FSupport = ctx.getExtension("EXT_color_buffer_float") != null;
		_wglClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static final void _wglEnable(int glEnum) {
		ctx.enable(glEnum);
	}
	
	public static final void _wglDisable(int glEnum) {
		ctx.disable(glEnum);
	}
	
	public static final void _wglClearColor(float r, float g, float b, float a) {
		ctx.clearColor(r, g, b, a);
	}
	
	public static final void _wglClearDepth(float f) {
		ctx.clearDepth(f);
	}
	
	public static final void _wglClear(int bits) {
		ctx.clear(bits);
	}
	
	public static final void _wglDepthFunc(int glEnum) {
		ctx.depthFunc(glEnum);
	}
	
	public static final void _wglDepthMask(boolean mask) {
		ctx.depthMask(mask);
	}
	
	public static final void _wglCullFace(int glEnum) {
		ctx.cullFace(glEnum);
	}
	
	public static final void _wglViewport(int x, int y, int w, int h) {
		ctx.viewport(x, y, w, h);
	}
	
	public static final void _wglBlendFunc(int src, int dst) {
		ctx.blendFunc(src, dst);
	}
	
	public static final void _wglBlendFuncSeparate(int srcColor, int dstColor,
			int srcAlpha, int dstAlpha) {
		ctx.blendFuncSeparate(srcColor, dstColor, srcAlpha, dstAlpha);
	}
	
	public static final void _wglBlendEquation(int glEnum) {
		ctx.blendEquation(glEnum);
	}
	
	public static final void _wglBlendColor(float r, float g, float b, float a) {
		ctx.blendColor(r, g, b, a);
	}
	
	public static final void _wglColorMask(boolean r, boolean g, boolean b, boolean a) {
		ctx.colorMask(r, g, b, a);
	}
	
	public static final void _wglDrawBuffers(int buffer) {
		ctx.drawBuffers(new int[] { buffer });
	}
	
	public static final void _wglDrawBuffers(int[] buffers) {
		ctx.drawBuffers(buffers);
	}
	
	public static final void _wglReadBuffer(int buffer) {
		ctx.readBuffer(buffer);
	}
	
	public static final void _wglPolygonOffset(float f1, float f2) {
		ctx.polygonOffset(f1, f2);
	}
	
	public static final void _wglLineWidth(float width) {
		ctx.lineWidth(width);
	}
	
	public static final IBufferGL _wglGenBuffers() {
		return new OpenGLObjects.BufferGL(ctx.createBuffer());
	}
	
	public static final ITextureGL _wglGenTextures() {
		return new OpenGLObjects.TextureGL(ctx.createTexture());
	}
	
	public static final IBufferArrayGL _wglGenVertexArrays() {
		return new OpenGLObjects.BufferArrayGL(ctx.createVertexArray());
	}
	
	public static final IProgramGL _wglCreateProgram() {
		return new OpenGLObjects.ProgramGL(ctx.createProgram());
	}
	
	public static final IShaderGL _wglCreateShader(int type) {
		return new OpenGLObjects.ShaderGL(ctx.createShader(type));
	}
	
	public static final IFramebufferGL _wglCreateFramebuffer() {
		return new OpenGLObjects.FramebufferGL(ctx.createFramebuffer());
	}
	
	public static final IRenderbufferGL _wglCreateRenderbuffer() {
		return new OpenGLObjects.RenderbufferGL(ctx.createRenderbuffer());
	}
	
	public static final IQueryGL _wglGenQueries() {
		return new OpenGLObjects.QueryGL(ctx.createQuery());
	}
	
	public static final void _wglDeleteBuffers(IBufferGL obj) {
		ctx.deleteBuffer(obj == null ? null : ((OpenGLObjects.BufferGL)obj).ptr);
	}
	
	public static final void _wglDeleteTextures(ITextureGL obj) {
		ctx.deleteTexture(obj == null ? null : ((OpenGLObjects.TextureGL)obj).ptr);
	}
	
	public static final void _wglDeleteVertexArrays(IBufferArrayGL obj) {
		ctx.deleteVertexArray(obj == null ? null : ((OpenGLObjects.BufferArrayGL)obj).ptr);
	}
	
	public static final void _wglDeleteProgram(IProgramGL obj) {
		ctx.deleteProgram(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr);
	}
	
	public static final void _wglDeleteShader(IShaderGL obj) {
		ctx.deleteShader(obj == null ? null : ((OpenGLObjects.ShaderGL)obj).ptr);
	}
	
	public static final void _wglDeleteFramebuffer(IFramebufferGL obj) {
		ctx.deleteFramebuffer(obj == null ? null : ((OpenGLObjects.FramebufferGL)obj).ptr);
	}
	
	public static final void _wglDeleteRenderbuffer(IRenderbufferGL obj) {
		ctx.deleteRenderbuffer(obj == null ? null : ((OpenGLObjects.RenderbufferGL)obj).ptr);
	}
	
	public static final void _wglDeleteQueries(IQueryGL obj) {
		ctx.deleteQuery(obj == null ? null : ((OpenGLObjects.QueryGL)obj).ptr);
	}
	
	public static final void _wglBindBuffer(int target, IBufferGL obj) {
		ctx.bindBuffer(target, obj == null ? null : ((OpenGLObjects.BufferGL)obj).ptr);
	}
	
	public static final void _wglBufferData(int target, ByteBuffer data, int usage) {
		ctx.bufferData(target, data == null ? null : EaglerArrayBufferAllocator.getDataView(data), usage);
	}
	
	public static final void _wglBufferData(int target, IntBuffer data, int usage) {
		ctx.bufferData(target, data == null ? null : EaglerArrayBufferAllocator.getDataView(data), usage);
	}
	
	public static final void _wglBufferData(int target, FloatBuffer data, int usage) {
		ctx.bufferData(target, data == null ? null : EaglerArrayBufferAllocator.getDataView(data), usage);
	}
	
	public static final void _wglBufferData(int target, int size, int usage) {
		ctx.bufferData(target, size, usage);
	}
	
	public static final void _wglBufferSubData(int target, int offset, ByteBuffer data) {
		ctx.bufferSubData(target, offset, data == null ? null : EaglerArrayBufferAllocator.getDataView(data));
	}
	
	public static final void _wglBufferSubData(int target, int offset, IntBuffer data) {
		ctx.bufferSubData(target, offset, data == null ? null : EaglerArrayBufferAllocator.getDataView(data));
	}
	
	public static final void _wglBufferSubData(int target, int offset, FloatBuffer data) {
		ctx.bufferSubData(target, offset, data == null ? null : EaglerArrayBufferAllocator.getDataView(data));
	}
	
	public static final void _wglBindVertexArray(IBufferArrayGL obj) {
		ctx.bindVertexArray(obj == null ? null : ((OpenGLObjects.BufferArrayGL)obj).ptr);
	}
	
	public static final void _wglEnableVertexAttribArray(int index) {
		ctx.enableVertexAttribArray(index);
	}
	
	public static final void _wglDisableVertexAttribArray(int index) {
		ctx.disableVertexAttribArray(index);
	}
	
	public static final void _wglVertexAttribPointer(int index, int size, int type,
			boolean normalized, int stride, int offset) {
		ctx.vertexAttribPointer(index, size, type, normalized, stride, offset);
	}

	public static final void _wglVertexAttribDivisor(int index, int divisor) {
		ctx.vertexAttribDivisor(index, divisor);
	}
	
	public static final void _wglActiveTexture(int texture) {
		ctx.activeTexture(texture);
	}
	
	public static final void _wglBindTexture(int target, ITextureGL obj) {
		ctx.bindTexture(target, obj == null ? null : ((OpenGLObjects.TextureGL)obj).ptr);
	}
	
	public static final void _wglTexParameterf(int target, int param, float value) {
		ctx.texParameterf(target, param, value);
	}
	
	public static final void _wglTexParameteri(int target, int param, int value) {
		ctx.texParameteri(target, param, value);
	}

	public static final void _wglTexImage3D(int target, int level, int internalFormat, int width, int height, int depth,
			int border, int format, int type, ByteBuffer data) {
		ctx.texImage3D(target, level, internalFormat, width, height, depth, border, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid(data));
	}
	
	public static final void _wglTexImage2D(int target, int level, int internalFormat, int width,
			int height, int border, int format, int type, ByteBuffer data) {
		ctx.texImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid(data));
	}
	
	public static final void _wglTexImage2Du16(int target, int level, int internalFormat, int width,
			int height, int border, int format, int type, ByteBuffer data) {
		ctx.texImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid16(data));
	}
	
	public static final void _wglTexImage2D(int target, int level, int internalFormat, int width,
			int height, int border, int format, int type, IntBuffer data) {
		ctx.texImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid(data));
	}
	
	public static final void _wglTexImage2D(int target, int level, int internalFormat, int width,
			int height, int border, int format, int type, FloatBuffer data) {
		ctx.texImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid(data));
	}
	
	public static final void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, ByteBuffer data) {
		ctx.texSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid(data));
	}
	
	public static final void _wglTexSubImage2Du16(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, ByteBuffer data) {
		ctx.texSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid16(data));
	}
	
	public static final void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, IntBuffer data) {
		ctx.texSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid(data));
	}
	
	public static final void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, FloatBuffer data) {
		ctx.texSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? null : EaglerArrayBufferAllocator.getDataViewStupid(data));
	}
	
	public static final void _wglCopyTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int x, int y, int width, int height) {
		ctx.copyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}

	public static final void _wglTexStorage2D(int target, int levels, int internalFormat, int w, int h) {
		ctx.texStorage2D(target, levels, internalFormat, w, h);
	}

	public static final void _wglPixelStorei(int pname, int value) {
		ctx.pixelStorei(pname, value);
	}
	
	public static final void _wglGenerateMipmap(int target) {
		ctx.generateMipmap(target);
	}
	
	public static final void _wglShaderSource(IShaderGL obj, String source) {
		ctx.shaderSource(obj == null ? null : ((OpenGLObjects.ShaderGL)obj).ptr, source);
	}
	
	public static final void _wglCompileShader(IShaderGL obj) {
		ctx.compileShader(obj == null ? null : ((OpenGLObjects.ShaderGL)obj).ptr);
	}
	
	public static final int _wglGetShaderi(IShaderGL obj, int param) {
		return ctx.getShaderParameteri(obj == null ? null : ((OpenGLObjects.ShaderGL)obj).ptr, param);
	}
	
	public static final String _wglGetShaderInfoLog(IShaderGL obj) {
		return ctx.getShaderInfoLog(obj == null ? null : ((OpenGLObjects.ShaderGL)obj).ptr);
	}
	
	public static final void _wglUseProgram(IProgramGL obj) {
		ctx.useProgram(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr);
	}
	
	public static final void _wglAttachShader(IProgramGL obj, IShaderGL shader) {
		ctx.attachShader(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr,
				shader == null ? null : ((OpenGLObjects.ShaderGL)shader).ptr);
	}
	
	public static final void _wglDetachShader(IProgramGL obj, IShaderGL shader) {
		ctx.detachShader(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr,
				shader == null ? null : ((OpenGLObjects.ShaderGL)shader).ptr);
	}
	
	public static final void _wglLinkProgram(IProgramGL obj) {
		ctx.linkProgram(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr);
	}
	
	public static final int _wglGetProgrami(IProgramGL obj, int param) {
		return ctx.getProgramParameteri(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr, param);
	}
	
	public static final String _wglGetProgramInfoLog(IProgramGL obj) {
		return ctx.getProgramInfoLog(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr);
	}
	
	public static final void _wglBindAttribLocation(IProgramGL obj, int index, String name) {
		ctx.bindAttribLocation(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr, index, name);
	}
	
	public static final int _wglGetAttribLocation(IProgramGL obj, String name) {
		return ctx.getAttribLocation(obj == null ? null : ((OpenGLObjects.ProgramGL)obj).ptr, name);
	}
	
	public static final void _wglDrawArrays(int mode, int first, int count) {
		ctx.drawArrays(mode, first, count);
		//checkErr("_wglDrawArrays(" + mode + ", " + first + ", " + count + ");");
	}

	public static final void _wglDrawArraysInstanced(int mode, int first, int count, int instanced) {
		ctx.drawArraysInstanced(mode, first, count, instanced);
		//checkErr("_wglDrawArraysInstanced(" + mode + ", " + first + ", " + count + ", " + instanced + ");");
	}
	
	public static final void _wglDrawElements(int mode, int count, int type, int offset) {
		ctx.drawElements(mode, count, type, offset);
		//checkErr("_wglDrawElements(" + mode + ", " + count + ", " + type + ", " + offset + ");");
	}
	
	public static final void _wglDrawElementsInstanced(int mode, int count, int type, int offset, int instanced) {
		ctx.drawElementsInstanced(mode, count, type, offset, instanced);
		//checkErr("_wglDrawElementsInstanced(" + mode + ", " + count + ", " + type + ", " + offset + ", " + instanced + ");");
	}
	
	public static final IUniformGL _wglGetUniformLocation(IProgramGL obj, String name) {
		WebGLUniformLocation loc = ctx.getUniformLocation(((OpenGLObjects.ProgramGL)obj).ptr, name);
		if(loc != null) {
			return new OpenGLObjects.UniformGL(loc);
		}else {
			return null;
		}
	}
	
	public static final int _wglGetUniformBlockIndex(IProgramGL obj, String name) {
		int i = ctx.getUniformBlockIndex(((OpenGLObjects.ProgramGL)obj).ptr, name);
		if(i == 0xFFFFFFFFl) {
			i = -1;
		}
		return i;
	}
	
	public static final void _wglBindBufferRange(int target, int index, IBufferGL buffer, int offset, int size) {
		ctx.bindBufferRange(target, index, ((OpenGLObjects.BufferGL)buffer).ptr, offset, size);
	}

	public static final void _wglUniformBlockBinding(IProgramGL obj, int blockIndex, int bufferIndex) {
		ctx.uniformBlockBinding(((OpenGLObjects.ProgramGL)obj).ptr, blockIndex, bufferIndex);
	}
	
	public static final void _wglUniform1f(IUniformGL obj, float x) {
		if(obj != null) ctx.uniform1f(((OpenGLObjects.UniformGL)obj).ptr, x);
	}
	
	public static final void _wglUniform2f(IUniformGL obj, float x, float y) {
		if(obj != null) ctx.uniform2f(((OpenGLObjects.UniformGL)obj).ptr, x, y);
	}
	
	public static final void _wglUniform3f(IUniformGL obj, float x, float y, float z) {
		if(obj != null) ctx.uniform3f(((OpenGLObjects.UniformGL)obj).ptr, x, y, z);
	}
	
	public static final void _wglUniform4f(IUniformGL obj, float x, float y, float z, float w) {
		if(obj != null) ctx.uniform4f(((OpenGLObjects.UniformGL)obj).ptr, x, y, z, w);
	}
	
	public static final void _wglUniform1i(IUniformGL obj, int x) {
		if(obj != null) ctx.uniform1i(((OpenGLObjects.UniformGL)obj).ptr, x);
	}
	
	public static final void _wglUniform2i(IUniformGL obj, int x, int y) {
		if(obj != null) ctx.uniform2i(((OpenGLObjects.UniformGL)obj).ptr, x, y);
	}
	
	public static final void _wglUniform3i(IUniformGL obj, int x, int y, int z) {
		if(obj != null) ctx.uniform3i(((OpenGLObjects.UniformGL)obj).ptr, x, y, z);
	}
	
	public static final void _wglUniform4i(IUniformGL obj, int x, int y, int z, int w) {
		if(obj != null) ctx.uniform4i(((OpenGLObjects.UniformGL)obj).ptr, x, y, z, w);
	}
	
	public static final void _wglUniformMatrix2fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if(obj != null) ctx.uniformMatrix2fv(((OpenGLObjects.UniformGL)obj).ptr, transpose,
				mat == null ? null : EaglerArrayBufferAllocator.getFloatArrayStupid(mat));
	}
	
	public static final void _wglUniformMatrix3fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if(obj != null) ctx.uniformMatrix3fv(((OpenGLObjects.UniformGL)obj).ptr, transpose,
				mat == null ? null : EaglerArrayBufferAllocator.getFloatArrayStupid(mat));
	}
	
	public static final void _wglUniformMatrix3x2fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if(obj != null) ctx.uniformMatrix3x2fv(((OpenGLObjects.UniformGL)obj).ptr, transpose,
				mat == null ? null : EaglerArrayBufferAllocator.getFloatArrayStupid(mat));
	}
	
	public static final void _wglUniformMatrix4fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if(obj != null) ctx.uniformMatrix4fv(((OpenGLObjects.UniformGL)obj).ptr, transpose,
				mat == null ? null : EaglerArrayBufferAllocator.getFloatArrayStupid(mat));
	}
	
	public static final void _wglUniformMatrix4x2fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if(obj != null) ctx.uniformMatrix4x2fv(((OpenGLObjects.UniformGL)obj).ptr, transpose,
				mat == null ? null : EaglerArrayBufferAllocator.getFloatArrayStupid(mat));
	}
	
	public static final void _wglUniformMatrix4x3fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if(obj != null) ctx.uniformMatrix4x3fv(((OpenGLObjects.UniformGL)obj).ptr, transpose,
				mat == null ? null : EaglerArrayBufferAllocator.getFloatArrayStupid(mat));
	}
	
	public static final void _wglBindFramebuffer(int target, IFramebufferGL framebuffer) {
		if(framebuffer == null) {
			ctx.bindFramebuffer(target, PlatformRuntime.mainFramebuffer);
			ctx.drawBuffers(new int[] { WebGL2RenderingContext.COLOR_ATTACHMENT0 });
		}else {
			ctx.bindFramebuffer(target, ((OpenGLObjects.FramebufferGL) framebuffer).ptr);
		}
	}
	
	public static final int _wglCheckFramebufferStatus(int target) {
		return ctx.checkFramebufferStatus(target);
	}
	
	public static final void _wglFramebufferTexture2D(int target, int attachment, int texTarget,
			ITextureGL texture, int level) {
		ctx.framebufferTexture2D(target, attachment, texTarget,
				texture == null ? null : ((OpenGLObjects.TextureGL)texture).ptr, level);
	}
	
	public static final void _wglFramebufferTextureLayer(int target, int attachment, ITextureGL texture, int level, int layer) {
		ctx.framebufferTextureLayer(target, attachment,
				texture == null ? null : ((OpenGLObjects.TextureGL) texture).ptr, level, layer);
	}
	
	public static final void _wglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1,
			int dstX0, int dstY0, int dstX1, int dstY1, int bits, int filter) {
		ctx.blitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, bits, filter);
	}
	
	public static final void _wglBindRenderbuffer(int target, IRenderbufferGL renderbuffer) {
		ctx.bindRenderbuffer(target,
				renderbuffer == null ? null : ((OpenGLObjects.RenderbufferGL)renderbuffer).ptr);
	}
	
	public static final void _wglRenderbufferStorage(int target, int internalformat,
			int width, int height) {
		ctx.renderbufferStorage(target, internalformat, width, height);
	}
	
	public static final void _wglFramebufferRenderbuffer(int target, int attachment,
			int renderbufferTarget, IRenderbufferGL renderbuffer) {
		ctx.framebufferRenderbuffer(target, attachment, renderbufferTarget,
				((OpenGLObjects.RenderbufferGL)renderbuffer).ptr);
	}
	
	public static final String _wglGetString(int param) {
		if(hasDebugRenderInfoExt) {
			String s;
			switch(param) {
			case 0x1f00: // VENDOR
				s = ctx.getParameterString(0x9245); // UNMASKED_VENDOR_WEBGL
				if(s == null) {
					s = ctx.getParameterString(0x1f00); // VENDOR
				}
				return s;
			case 0x1f01: // RENDERER
				s = ctx.getParameterString(0x9246); // UNMASKED_RENDERER_WEBGL
				if(s == null) {
					s = ctx.getParameterString(0x1f01); // RENDERER
				}
				return s;
			default:
				return ctx.getParameterString(param);
			}
		}else {
			return ctx.getParameterString(param);
		}
	}
	
	public static final int _wglGetInteger(int param) {
		return ctx.getParameteri(param);
	}
	
	public static final int _wglGetError() {
		return ctx.getError();
	}
	
	public static final boolean checkHDRFramebufferSupport(int bits) {
		switch(bits) {
		case 16:
			return hasFramebufferHDR16FSupport;
		case 32:
			return hasFramebufferHDR32FSupport;
		default:
			return false;
		}
	}

	private static final void checkErr(String name) {
		int i = ctx.getError();
		if(i != 0) {
			logger.error("########## GL ERROR ##########");
			logger.error("@ {}", name);
			do {
				logger.error("#{} - {}", i, EaglercraftGPU.gluErrorString(i));
			}while((i = ctx.getError()) != 0);
			try {
				throw new RuntimeException("GL Error Detected!");
			}catch(Throwable t) {
				logger.log(Level.ERROR, t);
			}
			logger.error("##############################");
		}
	}
}

/*
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerLWJGLAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

import static org.lwjgl.opengles.GLES30.*;
import static org.lwjgl.opengles.ANGLEInstancedArrays.*;
import static org.lwjgl.opengles.EXTInstancedArrays.*;
import static org.lwjgl.opengles.EXTTextureStorage.*;
import static org.lwjgl.opengles.OESVertexArrayObject.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengles.GLESCapabilities;

public class PlatformOpenGL {

	private static int glesVers = -1;

	private static boolean hasANGLEInstancedArrays = false;
	private static boolean hasEXTColorBufferFloat = false;
	private static boolean hasEXTColorBufferHalfFloat = false;
	private static boolean hasEXTGPUShader5 = false;
	private static boolean hasEXTInstancedArrays = false;
	private static boolean hasEXTShaderTextureLOD = false;
	private static boolean hasEXTTextureStorage = false;
	private static boolean hasOESFBORenderMipmap = false;
	private static boolean hasOESGPUShader5 = false;
	private static boolean hasOESVertexArrayObject = false;
	private static boolean hasOESTextureFloat = false;
	private static boolean hasOESTextureFloatLinear = false;
	private static boolean hasOESTextureHalfFloat = false;
	private static boolean hasOESTextureHalfFloatLinear = false;
	private static boolean hasEXTTextureFilterAnisotropic = false;

	private static boolean hasFBO16FSupport = false;
	private static boolean hasFBO32FSupport = false;
	private static boolean hasLinearHDR16FSupport = false;
	private static boolean hasLinearHDR32FSupport = false;

	private static final int VAO_IMPL_NONE = -1;
	private static final int VAO_IMPL_CORE = 0;
	private static final int VAO_IMPL_OES = 1;
	private static int vertexArrayImpl = VAO_IMPL_NONE;

	private static final int INSTANCE_IMPL_NONE = -1;
	private static final int INSTANCE_IMPL_CORE = 0;
	private static final int INSTANCE_IMPL_ANGLE = 1;
	private static final int INSTANCE_IMPL_EXT = 2;
	private static int instancingImpl = INSTANCE_IMPL_NONE;

	private static final int TEX_STORAGE_IMPL_NONE = -1;
	private static final int TEX_STORAGE_IMPL_CORE = 0;
	private static final int TEX_STORAGE_IMPL_EXT = 1;
	private static int texStorageImpl = TEX_STORAGE_IMPL_NONE;

	static void setCurrentContext(int glesVersIn, GLESCapabilities caps) {
		glesVers = glesVersIn;

		hasANGLEInstancedArrays = glesVersIn == 200 && caps.GL_ANGLE_instanced_arrays;
		hasOESTextureFloat = glesVersIn == 200 && caps.GL_OES_texture_float;
		hasOESTextureFloatLinear = glesVersIn >= 300 && caps.GL_OES_texture_float_linear;
		hasOESTextureHalfFloat = glesVersIn == 200 && caps.GL_OES_texture_half_float;
		hasOESTextureHalfFloatLinear = glesVersIn == 200 && caps.GL_OES_texture_half_float_linear;
		hasEXTColorBufferFloat = (glesVersIn == 310 || glesVersIn == 300) && caps.GL_EXT_color_buffer_float;
		hasEXTColorBufferHalfFloat = !hasEXTColorBufferFloat
				&& (glesVersIn == 310 || glesVersIn == 300 || glesVersIn == 200) && caps.GL_EXT_color_buffer_half_float;
		hasEXTInstancedArrays = !hasANGLEInstancedArrays && glesVersIn == 200 && caps.GL_EXT_instanced_arrays;
		hasEXTShaderTextureLOD = glesVersIn == 200 && caps.GL_EXT_shader_texture_lod;
		hasEXTTextureStorage = glesVersIn == 200 && caps.GL_EXT_texture_storage;
		hasOESGPUShader5 = glesVersIn == 310 && caps.GL_OES_gpu_shader5;
		hasEXTGPUShader5 = !hasOESGPUShader5 && glesVersIn == 310 && caps.GL_EXT_gpu_shader5;
		hasOESFBORenderMipmap = glesVersIn == 200 && caps.GL_OES_fbo_render_mipmap;
		hasOESVertexArrayObject = glesVersIn == 200 && caps.GL_OES_vertex_array_object;
		hasLinearHDR32FSupport = caps.GL_OES_texture_float_linear;
		hasEXTTextureFilterAnisotropic = caps.GL_EXT_texture_filter_anisotropic;
		
		hasFBO16FSupport = glesVersIn >= 320 || ((glesVersIn >= 300 || hasOESTextureFloat) && (hasEXTColorBufferFloat || hasEXTColorBufferHalfFloat));
		hasFBO32FSupport = glesVersIn >= 320 || ((glesVersIn >= 300 || hasOESTextureHalfFloat) && hasEXTColorBufferFloat);
		hasLinearHDR16FSupport = glesVersIn >= 300 || hasOESTextureHalfFloatLinear;
		hasLinearHDR32FSupport = glesVersIn >= 300 && hasOESTextureFloatLinear;
		
		if(glesVersIn >= 300) {
			vertexArrayImpl = VAO_IMPL_CORE;
			instancingImpl = INSTANCE_IMPL_CORE;
			texStorageImpl = TEX_STORAGE_IMPL_CORE;
		}else if(glesVersIn == 200) {
			vertexArrayImpl = hasOESVertexArrayObject ? VAO_IMPL_OES : VAO_IMPL_NONE;
			instancingImpl = hasANGLEInstancedArrays ? INSTANCE_IMPL_ANGLE : (hasEXTInstancedArrays ? INSTANCE_IMPL_EXT : INSTANCE_IMPL_NONE);
			texStorageImpl = hasEXTTextureStorage ? TEX_STORAGE_IMPL_EXT : TEX_STORAGE_IMPL_NONE;
		}else {
			vertexArrayImpl = VAO_IMPL_NONE;
			instancingImpl = INSTANCE_IMPL_NONE;
			texStorageImpl = TEX_STORAGE_IMPL_NONE;
		}
	}

	public static List<String> dumpActiveExtensions() {
		List<String> exts = new ArrayList<>();
		if(hasANGLEInstancedArrays) exts.add("ANGLE_instanced_arrays");
		if(hasEXTColorBufferFloat) exts.add("EXT_color_buffer_float");
		if(hasEXTColorBufferHalfFloat) exts.add("EXT_color_buffer_half_float");
		if(hasEXTGPUShader5) exts.add("EXT_gpu_shader5");
		if(hasEXTInstancedArrays) exts.add("EXT_instanced_arrays");
		if(hasEXTTextureStorage) exts.add("EXT_texture_storage");
		if(hasOESFBORenderMipmap) exts.add("OES_fbo_render_mipmap");
		if(hasOESGPUShader5) exts.add("OES_gpu_shader5");
		if(hasOESVertexArrayObject) exts.add("OES_vertex_array_object");
		if(hasOESTextureFloat) exts.add("OES_texture_float");
		if(hasOESTextureFloatLinear) exts.add("OES_texture_float_linear");
		if(hasOESTextureHalfFloat) exts.add("OES_texture_half_float");
		if(hasOESTextureHalfFloatLinear) exts.add("OES_texture_half_float_linear");
		if(hasEXTTextureFilterAnisotropic) exts.add("EXT_texture_filter_anisotropic");
		return exts;
	}

	public static void _wglEnable(int glEnum) {
		glEnable(glEnum);
	}

	public static void _wglDisable(int glEnum) {
		glDisable(glEnum);
	}

	public static void _wglClearColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}

	public static void _wglClearDepth(float f) {
		glClearDepthf(f);
	}

	public static void _wglClear(int bits) {
		glClear(bits);
	}

	public static void _wglDepthFunc(int glEnum) {
		glDepthFunc(glEnum);
	}

	public static void _wglDepthMask(boolean mask) {
		glDepthMask(mask);
	}

	public static void _wglCullFace(int glEnum) {
		glCullFace(glEnum);
	}

	public static void _wglViewport(int x, int y, int w, int h) {
		glViewport(x, y, w, h);
	}

	public static void _wglBlendFunc(int src, int dst) {
		glBlendFunc(src, dst);
	}

	public static void _wglBlendFuncSeparate(int srcColor, int dstColor, int srcAlpha, int dstAlpha) {
		glBlendFuncSeparate(srcColor, dstColor, srcAlpha, dstAlpha);
	}

	public static void _wglBlendEquation(int glEnum) {
		glBlendEquation(glEnum);
	}

	public static void _wglBlendColor(float r, float g, float b, float a) {
		glBlendColor(r, g, b, a);
	}

	public static void _wglColorMask(boolean r, boolean g, boolean b, boolean a) {
		glColorMask(r, g, b, a);
	}

	public static void _wglDrawBuffers(int buffer) {
		if(glesVers == 200) {
			if(buffer != 0x8CE0) { // GL_COLOR_ATTACHMENT0
				throw new UnsupportedOperationException();
			}
		}else {
			glDrawBuffers(buffer);
		}
	}

	public static void _wglDrawBuffers(int[] buffers) {
		if(glesVers == 200) {
			if(buffers.length != 1 || buffers[0] != 0x8CE0) { // GL_COLOR_ATTACHMENT0
				throw new UnsupportedOperationException();
			}
		}else {
			glDrawBuffers(buffers);
		}
	}

	public static void _wglReadBuffer(int buffer) {
		glReadBuffer(buffer);
	}

	public static void _wglReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer data) {
		nglReadPixels(x, y, width, height, format, type, EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglReadPixels_u16(int x, int y, int width, int height, int format, int type, ByteBuffer data) {
		nglReadPixels(x, y, width, height, format, type, EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer data) {
		nglReadPixels(x, y, width, height, format, type, EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglReadPixels(int x, int y, int width, int height, int format, int type, FloatBuffer data) {
		nglReadPixels(x, y, width, height, format, type, EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglPolygonOffset(float f1, float f2) {
		glPolygonOffset(f1, f2);
	}

	public static void _wglLineWidth(float width) {
		glLineWidth(width);
	}

	public static IBufferGL _wglGenBuffers() {
		return new OpenGLObjects.BufferGL(glGenBuffers());
	}

	public static ITextureGL _wglGenTextures() {
		return new OpenGLObjects.TextureGL(glGenTextures());
	}

	public static IVertexArrayGL _wglGenVertexArrays() {
		switch(vertexArrayImpl) {
		case VAO_IMPL_CORE:
			return new OpenGLObjects.VertexArrayGL(glGenVertexArrays());
		case VAO_IMPL_OES:
			return new OpenGLObjects.VertexArrayGL(glGenVertexArraysOES());
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static IProgramGL _wglCreateProgram() {
		return new OpenGLObjects.ProgramGL(glCreateProgram());
	}

	public static IShaderGL _wglCreateShader(int type) {
		return new OpenGLObjects.ShaderGL(glCreateShader(type));
	}

	public static IFramebufferGL _wglCreateFramebuffer() {
		return new OpenGLObjects.FramebufferGL(glGenFramebuffers());
	}

	public static IRenderbufferGL _wglCreateRenderbuffer() {
		return new OpenGLObjects.RenderbufferGL(glGenRenderbuffers());
	}

	public static IQueryGL _wglGenQueries() {
		return new OpenGLObjects.QueryGL(glGenQueries());
	}

	public static void _wglDeleteBuffers(IBufferGL obj) {
		glDeleteBuffers(((OpenGLObjects.BufferGL) obj).ptr);
	}

	public static void _wglDeleteTextures(ITextureGL obj) {
		glDeleteTextures(((OpenGLObjects.TextureGL) obj).ptr);
	}

	public static void _wglDeleteVertexArrays(IVertexArrayGL obj) {
		int ptr = ((OpenGLObjects.VertexArrayGL) obj).ptr;
		switch(vertexArrayImpl) {
		case VAO_IMPL_CORE:
			glDeleteVertexArrays(ptr);
			break;
		case VAO_IMPL_OES:
			glDeleteVertexArraysOES(ptr);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void _wglDeleteProgram(IProgramGL obj) {
		glDeleteProgram(((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static void _wglDeleteShader(IShaderGL obj) {
		glDeleteShader(((OpenGLObjects.ShaderGL) obj).ptr);
	}

	public static void _wglDeleteFramebuffer(IFramebufferGL obj) {
		glDeleteFramebuffers(((OpenGLObjects.FramebufferGL) obj).ptr);
	}

	public static void _wglDeleteRenderbuffer(IRenderbufferGL obj) {
		glDeleteRenderbuffers(((OpenGLObjects.RenderbufferGL) obj).ptr);
	}

	public static void _wglDeleteQueries(IQueryGL obj) {
		glDeleteQueries(((OpenGLObjects.QueryGL) obj).ptr);
	}

	public static void _wglBindBuffer(int target, IBufferGL obj) {
		glBindBuffer(target, obj == null ? 0 : ((OpenGLObjects.BufferGL) obj).ptr);
	}

	public static void _wglBufferData(int target, ByteBuffer data, int usage) {
		nglBufferData(target, data == null ? 0 : data.remaining(),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data), usage);
	}

	public static void _wglBufferData(int target, IntBuffer data, int usage) {
		nglBufferData(target, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data), usage);
	}

	public static void _wglBufferData(int target, FloatBuffer data, int usage) {
		nglBufferData(target, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data), usage);
	}

	public static void _wglBufferData(int target, int size, int usage) {
		glBufferData(target, size, usage);
	}

	public static void _wglBufferSubData(int target, int offset, ByteBuffer data) {
		nglBufferSubData(target, offset, data == null ? 0 : data.remaining(),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglBufferSubData(int target, int offset, IntBuffer data) {
		nglBufferSubData(target, offset, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglBufferSubData(int target, int offset, FloatBuffer data) {
		nglBufferSubData(target, offset, data == null ? 0 : (data.remaining() << 2),
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglBindVertexArray(IVertexArrayGL obj) {
		int ptr = obj == null ? 0 : ((OpenGLObjects.VertexArrayGL) obj).ptr;
		switch(vertexArrayImpl) {
		case VAO_IMPL_CORE:
			glBindVertexArray(ptr);
			break;
		case VAO_IMPL_OES:
			glBindVertexArrayOES(ptr);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void _wglEnableVertexAttribArray(int index) {
		glEnableVertexAttribArray(index);
	}

	public static void _wglDisableVertexAttribArray(int index) {
		glDisableVertexAttribArray(index);
	}

	public static void _wglVertexAttribPointer(int index, int size, int type, boolean normalized, int stride,
			int offset) {
		glVertexAttribPointer(index, size, type, normalized, stride, offset);
	}

	public static void _wglVertexAttribDivisor(int index, int divisor) {
		switch(instancingImpl) {
		case INSTANCE_IMPL_CORE:
			glVertexAttribDivisor(index, divisor);
			break;
		case INSTANCE_IMPL_ANGLE:
			glVertexAttribDivisorANGLE(index, divisor);
			break;
		case INSTANCE_IMPL_EXT:
			glVertexAttribDivisorEXT(index, divisor);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void _wglActiveTexture(int texture) {
		glActiveTexture(texture);
	}

	public static void _wglBindTexture(int target, ITextureGL obj) {
		glBindTexture(target, obj == null ? 0 : ((OpenGLObjects.TextureGL) obj).ptr);
	}

	public static void _wglTexParameterf(int target, int param, float value) {
		glTexParameterf(target, param, value);
	}

	public static void _wglTexParameteri(int target, int param, int value) {
		glTexParameteri(target, param, value);
	}

	public static void _wglTexImage3D(int target, int level, int internalFormat, int width, int height, int depth,
			int border, int format, int type, ByteBuffer data) {
		nglTexImage3D(target, level, internalFormat, width, height, depth, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexImage2D(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, ByteBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexImage2D(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, IntBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexImage2Df32(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, FloatBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexImage2Du16(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, ByteBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexImage2Df32(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, ByteBuffer data) {
		nglTexImage2D(target, level, internalFormat, width, height, border, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, ByteBuffer data) {
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, IntBuffer data) {
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexSubImage2Df32(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, FloatBuffer data) {
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglTexSubImage2Du16(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, ByteBuffer data) {
		nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data == null ? 0l : EaglerLWJGLAllocator.getAddress(data));
	}

	public static void _wglCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y,
			int width, int height) {
		glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}

	public static void _wglTexStorage2D(int target, int levels, int internalFormat, int w, int h) {
		switch(texStorageImpl) {
		case TEX_STORAGE_IMPL_CORE:
			glTexStorage2D(target, levels, internalFormat, w, h);
			break;
		case TEX_STORAGE_IMPL_EXT:
			glTexStorage2DEXT(target, levels, internalFormat, w, h);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void _wglPixelStorei(int pname, int value) {
		glPixelStorei(pname, value);
	}

	public static void _wglGenerateMipmap(int target) {
		glGenerateMipmap(target);
	}

	public static void _wglShaderSource(IShaderGL obj, String source) {
		glShaderSource(((OpenGLObjects.ShaderGL) obj).ptr, source);
	}

	public static void _wglCompileShader(IShaderGL obj) {
		glCompileShader(((OpenGLObjects.ShaderGL) obj).ptr);
	}

	public static int _wglGetShaderi(IShaderGL obj, int param) {
		return glGetShaderi(((OpenGLObjects.ShaderGL) obj).ptr, param);
	}

	public static String _wglGetShaderInfoLog(IShaderGL obj) {
		return glGetShaderInfoLog(((OpenGLObjects.ShaderGL) obj).ptr);
	}

	public static void _wglUseProgram(IProgramGL obj) {
		glUseProgram(obj == null ? 0 : ((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static void _wglAttachShader(IProgramGL obj, IShaderGL shader) {
		glAttachShader(((OpenGLObjects.ProgramGL) obj).ptr, ((OpenGLObjects.ShaderGL) shader).ptr);
	}

	public static void _wglDetachShader(IProgramGL obj, IShaderGL shader) {
		glDetachShader(((OpenGLObjects.ProgramGL) obj).ptr, ((OpenGLObjects.ShaderGL) shader).ptr);
	}

	public static void _wglLinkProgram(IProgramGL obj) {
		glLinkProgram(((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static int _wglGetProgrami(IProgramGL obj, int param) {
		return glGetProgrami(((OpenGLObjects.ProgramGL) obj).ptr, param);
	}

	public static String _wglGetProgramInfoLog(IProgramGL obj) {
		return glGetProgramInfoLog(((OpenGLObjects.ProgramGL) obj).ptr);
	}

	public static void _wglBindAttribLocation(IProgramGL obj, int index, String name) {
		glBindAttribLocation(((OpenGLObjects.ProgramGL) obj).ptr, index, name);
	}

	public static int _wglGetAttribLocation(IProgramGL obj, String name) {
		return glGetAttribLocation(((OpenGLObjects.ProgramGL) obj).ptr, name);
	}

	public static void _wglDrawArrays(int mode, int first, int count) {
		glDrawArrays(mode, first, count);
	}

	public static void _wglDrawArraysInstanced(int mode, int first, int count, int instanced) {
		switch(instancingImpl) {
		case INSTANCE_IMPL_CORE:
			glDrawArraysInstanced(mode, first, count, instanced);
			break;
		case INSTANCE_IMPL_ANGLE:
			glDrawArraysInstancedANGLE(mode, first, count, instanced);
			break;
		case INSTANCE_IMPL_EXT:
			glDrawArraysInstancedEXT(mode, first, count, instanced);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void _wglDrawElements(int mode, int count, int type, int offset) {
		glDrawElements(mode, count, type, offset);
	}

	public static void _wglDrawRangeElements(int mode, int start, int end, int count, int type, int offset) {
		glDrawRangeElements(mode, start, end, count, type, offset);
	}

	public static void _wglDrawElementsInstanced(int mode, int count, int type, int offset, int instanced) {
		switch(instancingImpl) {
		case INSTANCE_IMPL_CORE:
			glDrawElementsInstanced(mode, count, type, offset, instanced);
			break;
		case INSTANCE_IMPL_ANGLE:
			glDrawElementsInstancedANGLE(mode, count, type, offset, instanced);
			break;
		case INSTANCE_IMPL_EXT:
			glDrawElementsInstancedEXT(mode, count, type, offset, instanced);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static IUniformGL _wglGetUniformLocation(IProgramGL obj, String name) {
		int loc = glGetUniformLocation(((OpenGLObjects.ProgramGL) obj).ptr, name);
		return loc < 0 ? null : new OpenGLObjects.UniformGL(loc);
	}

	public static int _wglGetUniformBlockIndex(IProgramGL obj, String name) {
		return glGetUniformBlockIndex(((OpenGLObjects.ProgramGL) obj).ptr, name);
	}

	public static void _wglBindBufferRange(int target, int index, IBufferGL buffer, int offset, int size) {
		glBindBufferRange(target, index, ((OpenGLObjects.BufferGL) buffer).ptr, offset, size);
	}

	public static void _wglUniformBlockBinding(IProgramGL obj, int blockIndex, int bufferIndex) {
		glUniformBlockBinding(((OpenGLObjects.ProgramGL) obj).ptr, blockIndex, bufferIndex);
	}

	public static void _wglUniform1f(IUniformGL obj, float x) {
		if (obj != null)
			glUniform1f(((OpenGLObjects.UniformGL) obj).ptr, x);
	}

	public static void _wglUniform2f(IUniformGL obj, float x, float y) {
		if (obj != null)
			glUniform2f(((OpenGLObjects.UniformGL) obj).ptr, x, y);
	}

	public static void _wglUniform3f(IUniformGL obj, float x, float y, float z) {
		if (obj != null)
			glUniform3f(((OpenGLObjects.UniformGL) obj).ptr, x, y, z);
	}

	public static void _wglUniform4f(IUniformGL obj, float x, float y, float z, float w) {
		if (obj != null)
			glUniform4f(((OpenGLObjects.UniformGL) obj).ptr, x, y, z, w);
	}

	public static void _wglUniform1i(IUniformGL obj, int x) {
		if (obj != null)
			glUniform1i(((OpenGLObjects.UniformGL) obj).ptr, x);
	}

	public static void _wglUniform2i(IUniformGL obj, int x, int y) {
		if (obj != null)
			glUniform2i(((OpenGLObjects.UniformGL) obj).ptr, x, y);
	}

	public static final void _wglUniform3i(IUniformGL obj, int x, int y, int z) {
		if (obj != null)
			glUniform3i(((OpenGLObjects.UniformGL) obj).ptr, x, y, z);
	}

	public static void _wglUniform4i(IUniformGL obj, int x, int y, int z, int w) {
		if (obj != null)
			glUniform4i(((OpenGLObjects.UniformGL) obj).ptr, x, y, z, w);
	}

	public static void _wglUniformMatrix2fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix2fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() >> 2, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static void _wglUniformMatrix3fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix3fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() / 9, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static void _wglUniformMatrix3x2fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix3x2fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() / 6, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static void _wglUniformMatrix4fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix4fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() >> 4, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static void _wglUniformMatrix4x2fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix4x2fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() >> 3, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static void _wglUniformMatrix4x3fv(IUniformGL obj, boolean transpose, FloatBuffer mat) {
		if (obj != null)
			nglUniformMatrix4x3fv(((OpenGLObjects.UniformGL) obj).ptr, mat.remaining() / 12, transpose,
					EaglerLWJGLAllocator.getAddress(mat));
	}

	public static void _wglBindFramebuffer(int target, IFramebufferGL framebuffer) {
		if(framebuffer == null) {
			glBindFramebuffer(target, 0);
		}else {
			glBindFramebuffer(target, ((OpenGLObjects.FramebufferGL) framebuffer).ptr);
		}
	}

	public static int _wglCheckFramebufferStatus(int target) {
		return glCheckFramebufferStatus(target);
	}

	public static void _wglFramebufferTexture2D(int target, int attachment, int texTarget, ITextureGL texture,
			int level) {
		glFramebufferTexture2D(target, attachment, texTarget, ((OpenGLObjects.TextureGL) texture).ptr, level);
	}

	public static void _wglFramebufferTextureLayer(int target, int attachment, ITextureGL texture, int level, int layer) {
		glFramebufferTextureLayer(target, attachment, ((OpenGLObjects.TextureGL) texture).ptr, level, layer);
	}

	public static void _wglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0,
			int dstX1, int dstY1, int bits, int filter) {
		glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, bits, filter);
	}

	public static void _wglBindRenderbuffer(int target, IRenderbufferGL renderbuffer) {
		glBindRenderbuffer(target, renderbuffer == null ? 0 : ((OpenGLObjects.RenderbufferGL) renderbuffer).ptr);
	}

	public static void _wglRenderbufferStorage(int target, int internalformat, int width, int height) {
		glRenderbufferStorage(target, internalformat, width, height);
	}

	public static void _wglFramebufferRenderbuffer(int target, int attachment, int renderbufferTarget,
			IRenderbufferGL renderbuffer) {
		glFramebufferRenderbuffer(target, attachment, renderbufferTarget,
				((OpenGLObjects.RenderbufferGL) renderbuffer).ptr);
	}

	public static String _wglGetString(int param) {
		return glGetString(param);
	}

	public static int _wglGetInteger(int param) {
		return glGetInteger(param);
	}

	public static int _wglGetError() {
		return glGetError();
	}

	public static int checkOpenGLESVersion() {
		return glesVers;
	}

	public static boolean checkEXTGPUShader5Capable() {
		return hasEXTGPUShader5;
	}

	public static boolean checkOESGPUShader5Capable() {
		return hasOESGPUShader5;
	}

	public static boolean checkFBORenderMipmapCapable() {
		return hasOESFBORenderMipmap;
	}

	public static boolean checkVAOCapable() {
		return vertexArrayImpl != VAO_IMPL_NONE;
	}

	public static boolean checkInstancingCapable() {
		return instancingImpl != INSTANCE_IMPL_NONE;
	}

	public static boolean checkTexStorageCapable() {
		return texStorageImpl != TEX_STORAGE_IMPL_NONE;
	}

	public static boolean checkTextureLODCapable() {
		return glesVers >= 300 || hasEXTShaderTextureLOD;
	}

	public static boolean checkNPOTCapable() {
		return glesVers >= 300;
	}

	public static boolean checkHDRFramebufferSupport(int bits) {
		switch(bits) {
		case 16:
			return hasFBO16FSupport;
		case 32:
			return hasFBO32FSupport;
		default:
			return false;
		}
	}

	public static boolean checkLinearHDRFilteringSupport(int bits) {
		switch(bits) {
		case 16:
			return hasLinearHDR16FSupport;
		case 32:
			return hasLinearHDR32FSupport;
		default:
			return false;
		}
	}

	// legacy
	public static boolean checkLinearHDR32FSupport() {
		return hasLinearHDR32FSupport;
	}

	public static boolean checkAnisotropicFilteringSupport() {
		return hasEXTTextureFilterAnisotropic;
	}

	public static String[] getAllExtensions() {
		return glGetString(GL_EXTENSIONS).split(" ");
	}

	public static void enterVAOEmulationHook() {
		
	}

}
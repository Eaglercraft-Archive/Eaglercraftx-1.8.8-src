/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

import java.util.Arrays;
import java.util.List;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.webgl.WebGLBuffer;
import org.teavm.jso.webgl.WebGLFramebuffer;
import org.teavm.jso.webgl.WebGLProgram;
import org.teavm.jso.webgl.WebGLRenderbuffer;
import org.teavm.jso.webgl.WebGLShader;
import org.teavm.jso.webgl.WebGLTexture;
import org.teavm.jso.webgl.WebGLUniformLocation;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WebGLBackBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WebGLQuery;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WebGLVertexArray;

public class PlatformOpenGL {

	static int glesVers = -1;

	static final int VAO_IMPL_NONE = -1;
	static final int VAO_IMPL_CORE = 0;
	static final int VAO_IMPL_OES = 1;
	static int vertexArrayImpl = VAO_IMPL_NONE;

	static final int INSTANCE_IMPL_NONE = -1;
	static final int INSTANCE_IMPL_CORE = 0;
	static final int INSTANCE_IMPL_ANGLE = 1;
	static int instancingImpl = INSTANCE_IMPL_NONE;

	static final int CAP_A_BIT_EXT_GPU_SHADER5 = 1;
	static final int CAP_A_BIT_OES_GPU_SHADER5 = 2;
	static final int CAP_A_BIT_FBO_RENDER_MIPMAP = 4;
	static final int CAP_A_BIT_TEXTURE_LOD_CAPABLE = 8;
	static final int CAP_A_BIT_NPOT_CAPABLE = 16;
	static final int CAP_A_BIT_HDR_FBO16F = 32;
	static final int CAP_A_BIT_HDR_FBO32F = 64;
	static final int CAP_A_BIT_ANISOTROPIC = 128;
	static int capABits = 0;

	static final int CAP_B_BIT_HDR_LINEAR16F = 1;
	static final int CAP_B_BIT_HDR_LINEAR32F = 2;
	static int capBBits = 0;

	static void initContext() {
		glesVers = getCapBits(0);
		vertexArrayImpl = getCapBits(1);
		instancingImpl = getCapBits(2);
		capABits = getCapBits(3);
		capBBits = getCapBits(4);
		_wglClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Import(module = "platformOpenGL", name = "getCapBits")
	static native int getCapBits(int idx);

	@Import(module = "platformOpenGL", name = "glEnable")
	public static native void _wglEnable(int glEnum);

	@Import(module = "platformOpenGL", name = "glDisable")
	public static native void _wglDisable(int glEnum);

	@Import(module = "platformOpenGL", name = "glClearColor")
	public static native void _wglClearColor(float r, float g, float b, float a);

	@Import(module = "platformOpenGL", name = "glClearDepth")
	public static native void _wglClearDepth(float f);

	@Import(module = "platformOpenGL", name = "glClear")
	public static native void _wglClear(int bits);

	@Import(module = "platformOpenGL", name = "glDepthFunc")
	public static native void _wglDepthFunc(int glEnum);

	@Import(module = "platformOpenGL", name = "glDepthMask")
	public static native void _wglDepthMask(boolean mask);

	@Import(module = "platformOpenGL", name = "glCullFace")
	public static native void _wglCullFace(int glEnum);

	@Import(module = "platformOpenGL", name = "glViewport")
	public static native void _wglViewport(int x, int y, int w, int h);

	@Import(module = "platformOpenGL", name = "glBlendFunc")
	public static native void _wglBlendFunc(int src, int dst);

	@Import(module = "platformOpenGL", name = "glBlendFuncSeparate")
	public static native void _wglBlendFuncSeparate(int srcColor, int dstColor, int srcAlpha, int dstAlpha);

	@Import(module = "platformOpenGL", name = "glBlendEquation")
	public static native void _wglBlendEquation(int glEnum);

	@Import(module = "platformOpenGL", name = "glBlendColor")
	public static native void _wglBlendColor(float r, float g, float b, float a);

	@Import(module = "platformOpenGL", name = "glColorMask")
	public static native void _wglColorMask(boolean r, boolean g, boolean b, boolean a);

	private static final JSArray<JSNumber> drawBuffers = new JSArray<>();

	@JSBody(params = { "arr", "idx", "num" }, script = "arr[idx] = num;")
	private static native void setArrayInt(JSArray<JSNumber> arr, int idx, int num);

	public static void _wglDrawBuffers(int buffer) {
		if(glesVers == 200) {
			if(buffer != 0x8CE0) { // GL_COLOR_ATTACHMENT0
				throw new UnsupportedOperationException();
			}
		}else {
			drawBuffers.setLength(1);
			setArrayInt(drawBuffers, 0, buffer);
			_wglDrawBuffersN(drawBuffers);
		}
	}

	public static void _wglDrawBuffers(int[] buffers) {
		if(glesVers == 200) {
			if(buffers.length != 1 || buffers[0] != 0x8CE0) { // GL_COLOR_ATTACHMENT0
				throw new UnsupportedOperationException();
			}
		}else {
			int cnt = buffers.length;
			drawBuffers.setLength(cnt);
			for(int i = 0; i < cnt; ++i) {
				setArrayInt(drawBuffers, i, buffers[i]);
			}
			_wglDrawBuffersN(drawBuffers);
		}
	}

	@Import(module = "platformOpenGL", name = "glDrawBuffers")
	private static native void _wglDrawBuffersN(JSArray<JSNumber> buffers);

	@Import(module = "platformOpenGL", name = "glReadBuffer")
	public static native void _wglReadBuffer(int glEnum);

	public static void _wglReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer buffer) {
		_wglReadPixelsN(x, y, width, height, format, type, WASMGCBufferAllocator.getUnsignedByteBufferView(buffer));
	}

	public static void _wglReadPixels_u16(int x, int y, int width, int height, int format, int type, ByteBuffer buffer) {
		_wglReadPixelsN(x, y, width, height, format, type, WASMGCBufferAllocator.getUnsignedShortBufferView(buffer));
	}

	public static void _wglReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer buffer) {
		_wglReadPixelsN(x, y, width, height, format, type, WASMGCBufferAllocator.getIntBufferView(buffer));
	}

	public static void _wglReadPixels(int x, int y, int width, int height, int format, int type, FloatBuffer buffer) {
		_wglReadPixelsN(x, y, width, height, format, type, WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glReadPixels")
	static native void _wglReadPixelsN(int x, int y, int width, int height, int format, int type, ArrayBufferView array);

	@Import(module = "platformOpenGL", name = "glPolygonOffset")
	public static native void _wglPolygonOffset(float f1, float f2);

	@Import(module = "platformOpenGL", name = "glLineWidth")
	public static native void _wglLineWidth(float width);

	public static IBufferGL _wglGenBuffers() {
		return new OpenGLObjects.BufferGL(_wglGenBuffersN());
	}

	@Import(module = "platformOpenGL", name = "glGenBuffers")
	static native WebGLBuffer _wglGenBuffersN();

	public static ITextureGL _wglGenTextures() {
		return new OpenGLObjects.TextureGL(_wglGenTexturesN());
	}

	@Import(module = "platformOpenGL", name = "glGenTextures")
	static native WebGLTexture _wglGenTexturesN();

	public static IVertexArrayGL _wglGenVertexArrays() {
		return new OpenGLObjects.VertexArrayGL(_wglGenVertexArraysN());
	}

	@Import(module = "platformOpenGL", name = "glGenVertexArrays")
	public static native WebGLVertexArray _wglGenVertexArraysN();

	public static IProgramGL _wglCreateProgram() {
		return new OpenGLObjects.ProgramGL(_wglCreateProgramN());
	}

	@Import(module = "platformOpenGL", name = "glCreateProgram")
	static native WebGLProgram _wglCreateProgramN();

	public static IShaderGL _wglCreateShader(int type) {
		return new OpenGLObjects.ShaderGL(_wglCreateShaderN(type));
	}

	@Import(module = "platformOpenGL", name = "glCreateShader")
	static native WebGLShader _wglCreateShaderN(int type);

	public static IFramebufferGL _wglCreateFramebuffer() {
		return new OpenGLObjects.FramebufferGL(_wglCreateFramebufferN());
	}

	@Import(module = "platformOpenGL", name = "glCreateFramebuffer")
	static native WebGLFramebuffer _wglCreateFramebufferN();

	public static IRenderbufferGL _wglCreateRenderbuffer() {
		return new OpenGLObjects.RenderbufferGL(_wglCreateRenderbufferN());
	}

	@Import(module = "platformOpenGL", name = "glCreateRenderbuffer")
	static native WebGLRenderbuffer _wglCreateRenderbufferN();

	public static IQueryGL _wglGenQueries() {
		return new OpenGLObjects.QueryGL(_wglGenQueriesN());
	}

	@Import(module = "platformOpenGL", name = "glGenQueries")
	static native WebGLQuery _wglGenQueriesN();

	public static void _wglDeleteBuffers(IBufferGL objId) {
		_wglDeleteBuffersN(((OpenGLObjects.BufferGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteBuffers")
	static native void _wglDeleteBuffersN(WebGLBuffer objId);

	public static void _wglDeleteTextures(ITextureGL objId) {
		_wglDeleteTexturesN(((OpenGLObjects.TextureGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteTextures")
	static native void _wglDeleteTexturesN(WebGLTexture objId);

	public static void _wglDeleteVertexArrays(IVertexArrayGL objId) {
		_wglDeleteVertexArraysN(((OpenGLObjects.VertexArrayGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteVertexArrays")
	static native void _wglDeleteVertexArraysN(WebGLVertexArray objId);

	public static void _wglDeleteProgram(IProgramGL objId) {
		_wglDeleteProgramN(((OpenGLObjects.ProgramGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteProgram")
	static native void _wglDeleteProgramN(WebGLProgram objId);

	public static void _wglDeleteShader(IShaderGL objId) {
		_wglDeleteShaderN(((OpenGLObjects.ShaderGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteShader")
	static native void _wglDeleteShaderN(WebGLShader objId);

	public static void _wglDeleteFramebuffer(IFramebufferGL objId) {
		_wglDeleteFramebufferN(((OpenGLObjects.FramebufferGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteFramebuffer")
	static native void _wglDeleteFramebufferN(WebGLFramebuffer objId);

	public static void _wglDeleteRenderbuffer(IRenderbufferGL objId) {
		_wglDeleteRenderbufferN(((OpenGLObjects.RenderbufferGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteRenderbuffer")
	static native void _wglDeleteRenderbufferN(WebGLRenderbuffer objId);

	public static void _wglDeleteQueries(IQueryGL objId) {
		_wglDeleteQueriesN(((OpenGLObjects.QueryGL)objId).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDeleteQueries")
	static native void _wglDeleteQueriesN(WebGLQuery objId);

	public static void _wglBindBuffer(int target, IBufferGL bufObj) {
		_wglBindBufferN(target, bufObj != null ? ((OpenGLObjects.BufferGL)bufObj).ptr : null);
	}

	@Import(module = "platformOpenGL", name = "glBindBuffer")
	static native void _wglBindBufferN(int target, WebGLBuffer bufObj);

	@Import(module = "platformOpenGL", name = "glBufferData")
	public static native void _wglBufferData(int target, int size, int usage);

	public static void _wglBufferData(int target, ByteBuffer buffer, int usage) {
		_wglBufferDataN(target, WASMGCBufferAllocator.getUnsignedByteBufferView(buffer), usage);
	}

	public static void _wglBufferData(int target, IntBuffer buffer, int usage) {
		_wglBufferDataN(target, WASMGCBufferAllocator.getIntBufferView(buffer), usage);
	}

	public static void _wglBufferData(int target, FloatBuffer buffer, int usage) {
		_wglBufferDataN(target, WASMGCBufferAllocator.getFloatBufferView(buffer), usage);
	}

	@Import(module = "platformOpenGL", name = "glBufferData")
	static native void _wglBufferDataN(int target, ArrayBufferView typedArray, int usage);

	public static void _wglBufferSubData(int target, int dstOffset, ByteBuffer buffer) {
		_wglBufferSubDataN(target, dstOffset, WASMGCBufferAllocator.getUnsignedByteBufferView(buffer));
	}

	public static void _wglBufferSubData(int target, int dstOffset, IntBuffer buffer) {
		_wglBufferSubDataN(target, dstOffset, WASMGCBufferAllocator.getIntBufferView(buffer));
	}

	public static void _wglBufferSubData(int target, int dstOffset, FloatBuffer buffer) {
		_wglBufferSubDataN(target, dstOffset, WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glBufferSubData")
	static native void _wglBufferSubDataN(int target, int dstOffset, ArrayBufferView typedArray);

	public static void _wglBindVertexArray(IVertexArrayGL objId) {
		_wglBindVertexArrayN(objId != null ? ((OpenGLObjects.VertexArrayGL)objId).ptr : null);
	}

	@Import(module = "platformOpenGL", name = "glBindVertexArray")
	static native void _wglBindVertexArrayN(WebGLVertexArray objId);

	@Import(module = "platformOpenGL", name = "glEnableVertexAttribArray")
	public static native void _wglEnableVertexAttribArray(int index);

	@Import(module = "platformOpenGL", name = "glDisableVertexAttribArray")
	public static native void _wglDisableVertexAttribArray(int index);

	@Import(module = "platformOpenGL", name = "glVertexAttribPointer")
	public static native void _wglVertexAttribPointer(int index, int size, int type,
			boolean normalized, int stride, int offset);

	@Import(module = "platformOpenGL", name = "glVertexAttribDivisor")
	public static native void _wglVertexAttribDivisor(int index, int divisor);

	@Import(module = "platformOpenGL", name = "glActiveTexture")
	public static native void _wglActiveTexture(int texture);

	public static void _wglBindTexture(int target, ITextureGL objId) {
		_wglBindTextureN(target, objId != null ? ((OpenGLObjects.TextureGL)objId).ptr : null);
	}

	@Import(module = "platformOpenGL", name = "glBindTexture")
	static native void _wglBindTextureN(int target, WebGLTexture objId);

	@Import(module = "platformOpenGL", name = "glTexParameterf")
	public static native void _wglTexParameterf(int target, int param, float value);

	@Import(module = "platformOpenGL", name = "glTexParameteri")
	public static native void _wglTexParameteri(int target, int param, int value);

	public static void _wglTexImage3D(int target, int level, int internalFormat, int width, int height, int depth,
			int border, int format, int type, ByteBuffer data) {
		_wglTexImage3DN(target, level, internalFormat, width, height, depth, border, format, type,
				data != null ? WASMGCBufferAllocator.getUnsignedByteBufferView(data) : null);
	}

	@Import(module = "platformOpenGL", name = "glTexImage3D")
	static native void _wglTexImage3DN(int target, int level, int internalFormat, int width, int height, int depth,
			int border, int format, int type, ArrayBufferView typedArray);

	public static void _wglTexImage2D(int target, int level, int internalFormat, int width, int height, int border,
			int format, int type, ByteBuffer data) {
		_wglTexImage2DN(target, level, internalFormat, width, height, border, format, type,
				data != null ? WASMGCBufferAllocator.getUnsignedByteBufferView(data) : null);
	}

	public static void _wglTexImage2Du16(int target, int level, int internalFormat, int width, int height, int border,
			int format, int type, ByteBuffer data) {
		_wglTexImage2DN(target, level, internalFormat, width, height, border, format, type,
				data != null ? WASMGCBufferAllocator.getUnsignedShortBufferView(data) : null);
	}

	public static void _wglTexImage2Df32(int target, int level, int internalFormat, int width, int height, int border,
			int format, int type, ByteBuffer data) {
		_wglTexImage2DN(target, level, internalFormat, width, height, border, format, type,
				data != null ? WASMGCBufferAllocator.getFloatBufferView(data) : null);
	}

	public static void _wglTexImage2D(int target, int level, int internalFormat, int width,
			int height, int border, int format, int type, IntBuffer data) {
		_wglTexImage2DN(target, level, internalFormat, width, height, border, format, type,
				data != null ? WASMGCBufferAllocator.getUnsignedByteBufferView(data) : null);
	}

	public static void _wglTexImage2Df32(int target, int level, int internalFormat, int width,
			int height, int border, int format, int type, FloatBuffer data) {
		_wglTexImage2DN(target, level, internalFormat, width, height, border, format, type,
				data != null ? WASMGCBufferAllocator.getFloatBufferView(data) : null);
	}

	@Import(module = "platformOpenGL", name = "glTexImage2D")
	static native void _wglTexImage2DN(int target, int level, int internalFormat, int width, int height, int border,
			int format, int type, ArrayBufferView typedArray);

	public static void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, ByteBuffer data) {
		_wglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data != null ? WASMGCBufferAllocator.getUnsignedByteBufferView(data) : null);
	}

	public static void _wglTexSubImage2Du16(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, ByteBuffer data) {
		_wglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data != null ? WASMGCBufferAllocator.getUnsignedShortBufferView(data) : null);
	}

	public static void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, IntBuffer data) {
		_wglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data != null ? WASMGCBufferAllocator.getUnsignedByteBufferView(data) : null);
	}

	public static void _wglTexSubImage2Df32(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, FloatBuffer data) {
		_wglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
				data != null ? WASMGCBufferAllocator.getFloatBufferView(data) : null);
	}

	@Import(module = "platformOpenGL", name = "glTexSubImage2D")
	static native void _wglTexSubImage2D(int target, int level, int offsetx, int offsety, int width, int height,
			int format, int type, ArrayBufferView typedArray);

	@Import(module = "platformOpenGL", name = "glCopyTexSubImage2D")
	public static native void _wglCopyTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int x, int y, int width, int height);

	@Import(module = "platformOpenGL", name = "glTexStorage2D")
	public static native void _wglTexStorage2D(int target, int levels, int internalFormat, int w, int h);

	@Import(module = "platformOpenGL", name = "glPixelStorei")
	public static native void _wglPixelStorei(int pname, int value);

	@Import(module = "platformOpenGL", name = "glGenerateMipmap")
	public static native void _wglGenerateMipmap(int target);

	public static void _wglShaderSource(IShaderGL shader, String str) {
		_wglShaderSourceN(((OpenGLObjects.ShaderGL)shader).ptr, BetterJSStringConverter.stringToJS(str));
	}

	@Import(module = "platformOpenGL", name = "glShaderSource")
	static native void _wglShaderSourceN(WebGLShader shader, JSString str);

	public static void _wglCompileShader(IShaderGL shader) {
		_wglCompileShaderN(((OpenGLObjects.ShaderGL)shader).ptr);
	}

	@Import(module = "platformOpenGL", name = "glCompileShader")
	static native void _wglCompileShaderN(WebGLShader shader);

	public static int _wglGetShaderi(IShaderGL shader, int param) {
		return _wglGetShaderiN(((OpenGLObjects.ShaderGL)shader).ptr, param);
	}

	@Import(module = "platformOpenGL", name = "glGetShaderi")
	static native int _wglGetShaderiN(WebGLShader shader, int param);

	public static String _wglGetShaderInfoLog(IShaderGL shader) {
		return BetterJSStringConverter.stringFromJS(_wglGetShaderInfoLogN(((OpenGLObjects.ShaderGL)shader).ptr));
	}

	@Import(module = "platformOpenGL", name = "glGetShaderInfoLog")
	static native JSString _wglGetShaderInfoLogN(WebGLShader shader);

	public static void _wglUseProgram(IProgramGL prog) {
		_wglUseProgramN(prog != null ? ((OpenGLObjects.ProgramGL)prog).ptr : null);
	}

	@Import(module = "platformOpenGL", name = "glUseProgram")
	static native void _wglUseProgramN(WebGLProgram prog);

	public static void _wglAttachShader(IProgramGL prog, IShaderGL shader) {
		_wglAttachShaderN(((OpenGLObjects.ProgramGL)prog).ptr, ((OpenGLObjects.ShaderGL)shader).ptr);
	}

	@Import(module = "platformOpenGL", name = "glAttachShader")
	static native void _wglAttachShaderN(WebGLProgram prog, WebGLShader shader);

	public static void _wglDetachShader(IProgramGL prog, IShaderGL shader) {
		_wglDetachShaderN(((OpenGLObjects.ProgramGL)prog).ptr, ((OpenGLObjects.ShaderGL)shader).ptr);
	}

	@Import(module = "platformOpenGL", name = "glDetachShader")
	public static native void _wglDetachShaderN(WebGLProgram prog, WebGLShader shader);

	public static void _wglLinkProgram(IProgramGL prog) {
		_wglLinkProgramN(((OpenGLObjects.ProgramGL)prog).ptr);
	}

	@Import(module = "platformOpenGL", name = "glLinkProgram")
	static native void _wglLinkProgramN(WebGLProgram prog);

	public static int _wglGetProgrami(IProgramGL prog, int param) {
		return _wglGetProgramiN(((OpenGLObjects.ProgramGL)prog).ptr, param);
	}

	@Import(module = "platformOpenGL", name = "glGetProgrami")
	static native int _wglGetProgramiN(WebGLProgram prog, int param);

	public static String _wglGetProgramInfoLog(IProgramGL prog) {
		return BetterJSStringConverter.stringFromJS(_wglGetProgramInfoLogN(((OpenGLObjects.ProgramGL)prog).ptr));
	}

	@Import(module = "platformOpenGL", name = "glGetProgramInfoLog")
	static native JSString _wglGetProgramInfoLogN(WebGLProgram prog);

	@Import(module = "platformOpenGL", name = "glDrawArrays")
	public static native void _wglDrawArrays(int mode, int first, int count);

	@Import(module = "platformOpenGL", name = "glDrawElements")
	public static native void _wglDrawElements(int mode, int count, int type, int offset);

	@Import(module = "platformOpenGL", name = "glDrawArraysInstanced")
	public static native void _wglDrawArraysInstanced(int mode, int first, int count, int instanced);

	@Import(module = "platformOpenGL", name = "glDrawElementsInstanced")
	public static native void _wglDrawElementsInstanced(int mode, int count, int type, int offset, int instanced);

	public static void _wglBindAttribLocation(IProgramGL prog, int index, String str) {
		_wglBindAttribLocationN(((OpenGLObjects.ProgramGL)prog).ptr, index, BetterJSStringConverter.stringToJS(str));
	}

	@Import(module = "platformOpenGL", name = "glBindAttribLocation")
	static native void _wglBindAttribLocationN(WebGLProgram prog, int index, JSString str);

	public static int _wglGetAttribLocation(IProgramGL prog, String str) {
		return _wglGetAttribLocationN(((OpenGLObjects.ProgramGL)prog).ptr, BetterJSStringConverter.stringToJS(str));
	}

	@Import(module = "platformOpenGL", name = "glGetAttribLocation")
	static native int _wglGetAttribLocationN(WebGLProgram prog, JSString str);

	public static IUniformGL _wglGetUniformLocation(IProgramGL prog, String str) {
		WebGLUniformLocation ret = _wglGetUniformLocationN(((OpenGLObjects.ProgramGL)prog).ptr, BetterJSStringConverter.stringToJS(str));
		return ret != null ? new OpenGLObjects.UniformGL(ret) : null;
	}

	@Import(module = "platformOpenGL", name = "glGetUniformLocation")
	static native WebGLUniformLocation _wglGetUniformLocationN(WebGLProgram prog, JSString str);

	public static int _wglGetUniformBlockIndex(IProgramGL prog, String str) {
		return _wglGetUniformBlockIndexN(((OpenGLObjects.ProgramGL)prog).ptr, BetterJSStringConverter.stringToJS(str));
	}

	@Import(module = "platformOpenGL", name = "glGetUniformBlockIndex")
	static native int _wglGetUniformBlockIndexN(WebGLProgram prog, JSString str);

	public static void _wglBindBufferRange(int target, int index, IBufferGL bufferId, int offset, int size) {
		_wglBindBufferRangeN(target, index, ((OpenGLObjects.BufferGL)bufferId).ptr, offset, size);
	}

	@Import(module = "platformOpenGL", name = "glBindBufferRange")
	static native void _wglBindBufferRangeN(int target, int index, WebGLBuffer bufferId, int offset, int size);

	public static void _wglUniformBlockBinding(IProgramGL prog, int blockIndex, int bufferIndex) {
		_wglUniformBlockBindingN(((OpenGLObjects.ProgramGL)prog).ptr, blockIndex, bufferIndex);
	}

	@Import(module = "platformOpenGL", name = "glUniformBlockBinding")
	static native void _wglUniformBlockBindingN(WebGLProgram prog, int blockIndex, int bufferIndex);

	public static void _wglUniform1f(IUniformGL uniformIndex, float x) {
		if(uniformIndex != null) _wglUniform1fN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x);
	}

	@Import(module = "platformOpenGL", name = "glUniform1f")
	public static native void _wglUniform1fN(WebGLUniformLocation uniformIndex, float x);

	public static void _wglUniform2f(IUniformGL uniformIndex, float x, float y) {
		if(uniformIndex != null) _wglUniform2fN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x, y);
	}

	@Import(module = "platformOpenGL", name = "glUniform2f")
	public static native void _wglUniform2fN(WebGLUniformLocation uniformIndex, float x, float y);

	public static void _wglUniform3f(IUniformGL uniformIndex, float x, float y, float z) {
		if(uniformIndex != null) _wglUniform3fN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x, y, z);
	}

	@Import(module = "platformOpenGL", name = "glUniform3f")
	public static native void _wglUniform3fN(WebGLUniformLocation uniformIndex, float x, float y, float z);

	public static void _wglUniform4f(IUniformGL uniformIndex, float x, float y, float z, float w) {
		if(uniformIndex != null) _wglUniform4fN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x, y, z, w);
	}

	@Import(module = "platformOpenGL", name = "glUniform4f")
	public static native void _wglUniform4fN(WebGLUniformLocation uniformIndex, float x, float y, float z, float w);

	public static void _wglUniform1i(IUniformGL uniformIndex, int x) {
		if(uniformIndex != null) _wglUniform1iN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x);
	}

	@Import(module = "platformOpenGL", name = "glUniform1i")
	public static native void _wglUniform1iN(WebGLUniformLocation uniformIndex, int x);

	public static void _wglUniform2i(IUniformGL uniformIndex, int x, int y) {
		if(uniformIndex != null) _wglUniform2iN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x, y);
	}

	@Import(module = "platformOpenGL", name = "glUniform2i")
	public static native void _wglUniform2iN(WebGLUniformLocation uniformIndex, int x, int y);

	public static void _wglUniform3i(IUniformGL uniformIndex, int x, int y, int z) {
		if(uniformIndex != null) _wglUniform3iN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x, y, z);
	}

	@Import(module = "platformOpenGL", name = "glUniform3i")
	public static native void _wglUniform3iN(WebGLUniformLocation uniformIndex, int x, int y, int z);

	public static void _wglUniform4i(IUniformGL uniformIndex, int x, int y, int z, int w) {
		if(uniformIndex != null) _wglUniform4iN(((OpenGLObjects.UniformGL)uniformIndex).ptr, x, y, z, w);
	}

	@Import(module = "platformOpenGL", name = "glUniform4i")
	public static native void _wglUniform4iN(WebGLUniformLocation uniformIndex, int x, int y, int z, int w);

	public static void _wglUniformMatrix2fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer) {
		if (uniformIndex != null)
			_wglUniformMatrix2fvN(((OpenGLObjects.UniformGL) uniformIndex).ptr, transpose,
					WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glUniformMatrix2fv")
	static native void _wglUniformMatrix2fvN(WebGLUniformLocation uniformIndex, boolean transpose, Float32Array typedArray);

	public static void _wglUniformMatrix3fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer) {
		if (uniformIndex != null)
			_wglUniformMatrix3fvN(((OpenGLObjects.UniformGL) uniformIndex).ptr, transpose,
					WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glUniformMatrix3fv")
	static native void _wglUniformMatrix3fvN(WebGLUniformLocation uniformIndex, boolean transpose, Float32Array typedArray);

	public static void _wglUniformMatrix4fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer) {
		if (uniformIndex != null)
			_wglUniformMatrix4fvN(((OpenGLObjects.UniformGL) uniformIndex).ptr, transpose,
					WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glUniformMatrix4fv")
	static native void _wglUniformMatrix4fvN(WebGLUniformLocation uniformIndex, boolean transpose, Float32Array typedArray);

	public static void _wglUniformMatrix3x2fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer) {
		if (uniformIndex != null)
			_wglUniformMatrix3x2fvN(((OpenGLObjects.UniformGL) uniformIndex).ptr, transpose,
					WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glUniformMatrix3x2fv")
	static native void _wglUniformMatrix3x2fvN(WebGLUniformLocation uniformIndex, boolean transpose, Float32Array typedArray);

	public static void _wglUniformMatrix4x2fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer) {
		if (uniformIndex != null)
			_wglUniformMatrix4x2fvN(((OpenGLObjects.UniformGL) uniformIndex).ptr, transpose,
					WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glUniformMatrix4x2fv")
	static native void _wglUniformMatrix4x2fvN(WebGLUniformLocation uniformIndex, boolean transpose, Float32Array typedArray);

	public static void _wglUniformMatrix4x3fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer) {
		if (uniformIndex != null)
			_wglUniformMatrix4x3fvN(((OpenGLObjects.UniformGL) uniformIndex).ptr, transpose,
					WASMGCBufferAllocator.getFloatBufferView(buffer));
	}

	@Import(module = "platformOpenGL", name = "glUniformMatrix4x3fv")
	static native void _wglUniformMatrix4x3fvN(WebGLUniformLocation uniformIndex, boolean transpose, Float32Array typedArray);

	public static void _wglBindFramebuffer(int target, IFramebufferGL framebuffer) {
		if(framebuffer == null) {
			framebuffer = WebGLBackBuffer.getBackBuffer();
		}
		_wglBindFramebufferN(target, ((OpenGLObjects.FramebufferGL)framebuffer).ptr);
	}

	public static void _wglBindFramebufferLow(int target, IFramebufferGL framebuffer) {
		_wglBindFramebufferN(target, framebuffer != null ? ((OpenGLObjects.FramebufferGL)framebuffer).ptr : null);
	}

	@Import(module = "platformOpenGL", name = "glBindFramebuffer")
	static native void _wglBindFramebufferN(int target, WebGLFramebuffer framebuffer);

	@Import(module = "platformOpenGL", name = "glCheckFramebufferStatus")
	public static native int _wglCheckFramebufferStatus(int target);

	@Import(module = "platformOpenGL", name = "glBlitFramebuffer")
	public static native void _wglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0,
			int dstX1, int dstY1, int bits, int filter);

	@Import(module = "platformOpenGL", name = "glRenderbufferStorage")
	public static native void _wglRenderbufferStorage(int target, int internalformat, int width, int height);

	public static void _wglFramebufferTexture2D(int target, int attachment, int texTarget, ITextureGL texObj, int level) {
		_wglFramebufferTexture2DN(target, attachment, texTarget, ((OpenGLObjects.TextureGL)texObj).ptr, level);
	}

	@Import(module = "platformOpenGL", name = "glFramebufferTexture2D")
	static native void _wglFramebufferTexture2DN(int target, int attachment, int texTarget, WebGLTexture texObj, int level);

	public static void _wglFramebufferTextureLayer(int target, int attachment, ITextureGL texObj, int level, int layer) {
		_wglFramebufferTextureLayerN(target, attachment, ((OpenGLObjects.TextureGL)texObj).ptr, level, layer);
	}

	@Import(module = "platformOpenGL", name = "glFramebufferTextureLayer")
	static native void _wglFramebufferTextureLayerN(int target, int attachment, WebGLTexture texObj, int level, int layer);

	public static void _wglBindRenderbuffer(int target, IRenderbufferGL renderbuffer) {
		_wglBindRenderbufferN(target, renderbuffer != null ? ((OpenGLObjects.RenderbufferGL)renderbuffer).ptr : null);
	}

	@Import(module = "platformOpenGL", name = "glBindRenderbuffer")
	static native void _wglBindRenderbufferN(int target, WebGLRenderbuffer renderbuffer);

	public static void _wglFramebufferRenderbuffer(int target, int attachment, int renderbufferTarget,
			IRenderbufferGL renderbufferId) {
		_wglFramebufferRenderbufferN(target, attachment, renderbufferTarget,
				renderbufferId != null ? ((OpenGLObjects.RenderbufferGL) renderbufferId).ptr : null);
	}

	@Import(module = "platformOpenGL", name = "glFramebufferRenderbuffer")
	static native void _wglFramebufferRenderbufferN(int target, int attachment, int renderbufferTarget, WebGLRenderbuffer renderbufferId);

	public static String _wglGetString(int param) {
		return BetterJSStringConverter.stringFromJS(_wglGetStringN(param));
	}

	@Import(module = "platformOpenGL", name = "glGetString")
	static native JSString _wglGetStringN(int param);

	@Import(module = "platformOpenGL", name = "glGetInteger")
	public static native int _wglGetInteger(int param);

	@Import(module = "platformOpenGL", name = "glGetError")
	public static native int _wglGetError();

	public static int checkOpenGLESVersion() {
		return glesVers;
	}

	public static boolean checkEXTGPUShader5Capable() {
		return (capABits & CAP_A_BIT_EXT_GPU_SHADER5) != 0;
	}

	public static boolean checkOESGPUShader5Capable() {
		return (capABits & CAP_A_BIT_OES_GPU_SHADER5) != 0;
	}

	public static boolean checkFBORenderMipmapCapable() {
		return (capABits & CAP_A_BIT_FBO_RENDER_MIPMAP) != 0;
	}

	public static boolean checkVAOCapable() {
		return vertexArrayImpl != VAO_IMPL_NONE;
	}

	public static boolean checkInstancingCapable() {
		return instancingImpl != INSTANCE_IMPL_NONE;
	}

	public static boolean checkTexStorageCapable() {
		return glesVers >= 300;
	}

	public static boolean checkTextureLODCapable() {
		return (capABits & CAP_A_BIT_TEXTURE_LOD_CAPABLE) != 0;
	}

	public static boolean checkNPOTCapable() {
		return (capABits & CAP_A_BIT_NPOT_CAPABLE) != 0;
	}

	public static boolean checkHDRFramebufferSupport(int bits) {
		switch(bits) {
		case 16:
			return (capABits & CAP_A_BIT_HDR_FBO16F) != 0;
		case 32:
			return (capABits & CAP_A_BIT_HDR_FBO32F) != 0;
		default:
			return false;
		}
	}

	public static boolean checkLinearHDRFilteringSupport(int bits) {
		switch(bits) {
		case 16:
			return (capBBits & CAP_B_BIT_HDR_LINEAR16F) != 0;
		case 32:
			return (capBBits & CAP_B_BIT_HDR_LINEAR32F) != 0;
		default:
			return false;
		}
	}

	// legacy
	public static boolean checkLinearHDR32FSupport() {
		return (capBBits & CAP_B_BIT_HDR_LINEAR32F) != 0;
	}

	public static boolean checkAnisotropicFilteringSupport() {
		return (capABits & CAP_A_BIT_ANISOTROPIC) != 0;
	}

	public static String[] getAllExtensions() {
		return BetterJSStringConverter.stringArrayFromJS(getAllExtensions0());
	}

	@Import(module = "platformOpenGL", name = "getAllExtensions")
	static native JSArray<JSString> getAllExtensions0();

	public static List<String> dumpActiveExtensions() {
		return Arrays.asList(BetterJSStringConverter.stringArrayFromJS(dumpActiveExtensions0()));
	}

	@Import(module = "platformOpenGL", name = "dumpActiveExtensions")
	static native JSArray<JSString> dumpActiveExtensions0();

	public static void enterVAOEmulationHook() {
		WebGLBackBuffer.enterVAOEmulationPhase();
	}

}
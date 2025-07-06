/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

public class PlatformOpenGL {

	public static native void _wglEnable(int glEnum);

	public static native void _wglDisable(int glEnum);

	public static native void _wglClearColor(float r, float g, float b, float a);

	public static native void _wglClearDepth(float f);

	public static native void _wglClear(int bits);

	public static native void _wglDepthFunc(int glEnum);

	public static native void _wglDepthMask(boolean mask);

	public static native void _wglCullFace(int glEnum);

	public static native void _wglViewport(int x, int y, int w, int h);

	public static native void _wglBlendFunc(int src, int dst);

	public static native void _wglBlendFuncSeparate(int srcColor, int dstColor, int srcAlpha, int dstAlpha);

	public static native void _wglBlendEquation(int glEnum);

	public static native void _wglBlendColor(float r, float g, float b, float a);

	public static native void _wglColorMask(boolean r, boolean g, boolean b, boolean a);

	public static native void _wglDrawBuffers(int buffer);

	public static native void _wglDrawBuffers(int[] buffers);

	public static native void _wglReadBuffer(int glEnum);

	public static native void _wglReadPixels(int x, int y, int width, int height, int format, int type,
			ByteBuffer buffer);

	public static native void _wglReadPixels_u16(int x, int y, int width, int height, int format, int type,
			ByteBuffer buffer);

	public static native void _wglReadPixels(int x, int y, int width, int height, int format, int type,
			IntBuffer buffer);

	public static native void _wglReadPixels(int x, int y, int width, int height, int format, int type,
			FloatBuffer buffer);

	public static native void _wglPolygonOffset(float f1, float f2);

	public static native void _wglLineWidth(float width);

	public static native IBufferGL _wglGenBuffers();

	public static native ITextureGL _wglGenTextures();

	public static native IVertexArrayGL _wglGenVertexArrays();

	public static native IProgramGL _wglCreateProgram();

	public static native IShaderGL _wglCreateShader(int type);

	public static native IFramebufferGL _wglCreateFramebuffer();

	public static native IRenderbufferGL _wglCreateRenderbuffer();

	public static native IQueryGL _wglGenQueries();

	public static native void _wglDeleteBuffers(IBufferGL objId);

	public static native void _wglDeleteTextures(ITextureGL objId);

	public static native void _wglDeleteVertexArrays(IVertexArrayGL objId);

	public static native void _wglDeleteProgram(IProgramGL objId);

	public static native void _wglDeleteShader(IShaderGL objId);

	public static native void _wglDeleteFramebuffer(IFramebufferGL objId);

	public static native void _wglDeleteRenderbuffer(IRenderbufferGL objId);

	public static native void _wglDeleteQueries(IQueryGL objId);

	public static native void _wglBindBuffer(int target, IBufferGL bufObj);

	public static native void _wglBufferData(int target, int size, int usage);

	public static native void _wglBufferData(int target, ByteBuffer buffer, int usage);

	public static native void _wglBufferData(int target, IntBuffer buffer, int usage);

	public static native void _wglBufferData(int target, FloatBuffer buffer, int usage);

	public static native void _wglBufferSubData(int target, int dstOffset, ByteBuffer buffer);

	public static native void _wglBufferSubData(int target, int dstOffset, IntBuffer buffer);

	public static native void _wglBufferSubData(int target, int dstOffset, FloatBuffer buffer);

	public static native void _wglBindVertexArray(IVertexArrayGL objId);

	public static native void _wglEnableVertexAttribArray(int index);

	public static native void _wglDisableVertexAttribArray(int index);

	public static native void _wglVertexAttribPointer(int index, int size, int type, boolean normalized, int stride,
			int offset);

	public static native void _wglVertexAttribDivisor(int index, int divisor);

	public static native void _wglActiveTexture(int texture);

	public static native void _wglBindTexture(int target, ITextureGL objId);

	public static native void _wglTexParameterf(int target, int param, float value);

	public static native void _wglTexParameteri(int target, int param, int value);

	public static native void _wglTexImage3D(int target, int level, int internalFormat, int width, int height,
			int depth, int border, int format, int type, ByteBuffer data);

	public static native void _wglTexImage2D(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, ByteBuffer data);

	public static native void _wglTexImage2Du16(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, ByteBuffer data);

	public static native void _wglTexImage2Df32(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, ByteBuffer data);

	public static native void _wglTexImage2D(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, IntBuffer data);

	public static native void _wglTexImage2Df32(int target, int level, int internalFormat, int width, int height,
			int border, int format, int type, FloatBuffer data);

	public static native void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, ByteBuffer data);

	public static native void _wglTexSubImage2Du16(int target, int level, int xoffset, int yoffset, int width,
			int height, int format, int type, ByteBuffer data);

	public static native void _wglTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, IntBuffer data);

	public static native void _wglTexSubImage2Df32(int target, int level, int xoffset, int yoffset, int width,
			int height, int format, int type, FloatBuffer data);

	public static native void _wglCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y,
			int width, int height);

	public static native void _wglTexStorage2D(int target, int levels, int internalFormat, int w, int h);

	public static native void _wglPixelStorei(int pname, int value);

	public static native void _wglGenerateMipmap(int target);

	public static native void _wglShaderSource(IShaderGL shader, String str);

	public static native void _wglCompileShader(IShaderGL shader);

	public static native int _wglGetShaderi(IShaderGL shader, int param);

	public static native String _wglGetShaderInfoLog(IShaderGL shader);

	public static native void _wglUseProgram(IProgramGL prog);

	public static native void _wglAttachShader(IProgramGL prog, IShaderGL shader);

	public static native void _wglDetachShader(IProgramGL prog, IShaderGL shader);

	public static native void _wglLinkProgram(IProgramGL prog);

	public static native int _wglGetProgrami(IProgramGL prog, int param);

	public static native String _wglGetProgramInfoLog(IProgramGL prog);

	public static native void _wglDrawArrays(int mode, int first, int count);

	public static native void _wglDrawElements(int mode, int count, int type, int offset);

	public static native void _wglDrawRangeElements(int mode, int start, int end, int count, int type, int offset);

	public static native void _wglDrawArraysInstanced(int mode, int first, int count, int instanced);

	public static native void _wglDrawElementsInstanced(int mode, int count, int type, int offset, int instanced);

	public static native void _wglBindAttribLocation(IProgramGL prog, int index, String str);

	public static native int _wglGetAttribLocation(IProgramGL prog, String str);

	public static native IUniformGL _wglGetUniformLocation(IProgramGL prog, String str);

	public static native int _wglGetUniformBlockIndex(IProgramGL prog, String str);

	public static native void _wglBindBufferRange(int target, int index, IBufferGL bufferId, int offset, int size);

	public static native void _wglUniformBlockBinding(IProgramGL prog, int blockIndex, int bufferIndex);

	public static native void _wglUniform1f(IUniformGL uniformIndex, float x);

	public static native void _wglUniform2f(IUniformGL uniformIndex, float x, float y);

	public static native void _wglUniform3f(IUniformGL uniformIndex, float x, float y, float z);

	public static native void _wglUniform4f(IUniformGL uniformIndex, float x, float y, float z, float w);

	public static native void _wglUniform1i(IUniformGL uniformIndex, int x);

	public static native void _wglUniform2i(IUniformGL uniformIndex, int x, int y);

	public static native void _wglUniform3i(IUniformGL uniformIndex, int x, int y, int z);

	public static native void _wglUniform4i(IUniformGL uniformIndex, int x, int y, int z, int w);

	public static native void _wglUniformMatrix2fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer);

	public static native void _wglUniformMatrix3fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer);

	public static native void _wglUniformMatrix4fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer);

	public static native void _wglUniformMatrix3x2fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer);

	public static native void _wglUniformMatrix4x2fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer);

	public static native void _wglUniformMatrix4x3fv(IUniformGL uniformIndex, boolean transpose, FloatBuffer buffer);

	public static native void _wglBindFramebuffer(int target, IFramebufferGL framebuffer);

	public static native int _wglCheckFramebufferStatus(int target);

	public static native void _wglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0,
			int dstX1, int dstY1, int bits, int filter);

	public static native void _wglRenderbufferStorage(int target, int internalformat, int width, int height);

	public static native void _wglFramebufferTexture2D(int target, int attachment, int texTarget, ITextureGL texObj,
			int level);

	public static native void _wglFramebufferTextureLayer(int target, int attachment, ITextureGL texObj, int level,
			int layer);

	public static native void _wglBindRenderbuffer(int target, IRenderbufferGL renderbuffer);

	public static native void _wglFramebufferRenderbuffer(int target, int attachment, int renderbufferTarget,
			IRenderbufferGL renderbufferId);

	public static native String _wglGetString(int param);

	public static native int _wglGetInteger(int param);

	public static native int _wglGetError();

	public static native int checkOpenGLESVersion();

	public static native boolean checkEXTGPUShader5Capable();

	public static native boolean checkOESGPUShader5Capable();

	public static native boolean checkFBORenderMipmapCapable();

	public static native boolean checkVAOCapable();

	public static native boolean checkInstancingCapable();

	public static native boolean checkTexStorageCapable();

	public static native boolean checkTextureLODCapable();

	public static native boolean checkNPOTCapable();

	public static native boolean checkHDRFramebufferSupport(int bits);

	public static native boolean checkLinearHDRFilteringSupport(int bits);

	// legacy
	public static native boolean checkLinearHDR32FSupport();

	public static native boolean checkAnisotropicFilteringSupport();

	public static native String[] getAllExtensions();

	public static native List<String> dumpActiveExtensions();

	public static native void enterVAOEmulationHook();

}
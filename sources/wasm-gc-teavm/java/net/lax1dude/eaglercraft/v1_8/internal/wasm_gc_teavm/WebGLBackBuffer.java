/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IRenderbufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.ITextureGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.MemoryStack;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

public class WebGLBackBuffer {

	private static int glesVers = -1;

	private static IFramebufferGL eagFramebuffer;
	private static int width;
	private static int height;

	// GLES 3.0+
	private static IRenderbufferGL gles3ColorRenderbuffer;
	private static IRenderbufferGL gles3DepthRenderbuffer;

	// GLES 2.0
	private static ITextureGL gles2ColorTexture;
	private static IRenderbufferGL gles2DepthRenderbuffer;
	private static IProgramGL gles2BlitProgram;
	private static IBufferArrayGL gles2BlitVAO;
	private static IBufferGL gles2BlitVBO;

	private static boolean isVAOCapable = false;
	private static boolean isEmulatedVAOPhase = false;

	private static final int _GL_FRAMEBUFFER = 0x8D40;
	private static final int _GL_RENDERBUFFER = 0x8D41;
	private static final int _GL_COLOR_ATTACHMENT0 = 0x8CE0;
	private static final int _GL_DEPTH_ATTACHMENT = 0x8D00;
	private static final int _GL_DEPTH_COMPONENT16 = 0x81A5;
	private static final int _GL_DEPTH_COMPONENT32F = 0x8CAC;
	private static final int _GL_READ_FRAMEBUFFER = 0x8CA8;
	private static final int _GL_DRAW_FRAMEBUFFER = 0x8CA9;

	public static void initBackBuffer(int sw, int sh) {
		glesVers = checkOpenGLESVersion();
		eagFramebuffer = _wglCreateFramebuffer();
		isVAOCapable = checkVAOCapable();
		isEmulatedVAOPhase = false;
		width = sw;
		height = sh;
		if(glesVers >= 300) {
			gles3ColorRenderbuffer = _wglCreateRenderbuffer();
			gles3DepthRenderbuffer = _wglCreateRenderbuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, eagFramebuffer);
			_wglBindRenderbuffer(_GL_RENDERBUFFER, gles3ColorRenderbuffer);
			_wglRenderbufferStorage(_GL_RENDERBUFFER, GL_RGBA8, sw, sh);
			_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, _GL_RENDERBUFFER, gles3ColorRenderbuffer);
			_wglBindRenderbuffer(_GL_RENDERBUFFER, gles3DepthRenderbuffer);
			_wglRenderbufferStorage(_GL_RENDERBUFFER, _GL_DEPTH_COMPONENT32F, sw, sh);
			_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, _GL_RENDERBUFFER, gles3DepthRenderbuffer);
			_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);
		}else {
			gles2ColorTexture = _wglGenTextures();
			gles2DepthRenderbuffer = _wglCreateRenderbuffer();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, eagFramebuffer);
			_wglBindTexture(GL_TEXTURE_2D, gles2ColorTexture);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sw, sh, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, gles2ColorTexture, 0);
			_wglBindRenderbuffer(_GL_RENDERBUFFER, gles2DepthRenderbuffer);
			_wglRenderbufferStorage(_GL_RENDERBUFFER, _GL_DEPTH_COMPONENT16, sw, sh);
			_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, _GL_RENDERBUFFER, gles2DepthRenderbuffer);

			MemoryStack.push();
			try {
				ByteBuffer upload = MemoryStack.mallocByteBuffer(48);
				upload.putFloat(0.0f); upload.putFloat(0.0f);
				upload.putFloat(1.0f); upload.putFloat(0.0f);
				upload.putFloat(0.0f); upload.putFloat(1.0f);
				upload.putFloat(1.0f); upload.putFloat(0.0f);
				upload.putFloat(1.0f); upload.putFloat(1.0f);
				upload.putFloat(0.0f); upload.putFloat(1.0f);
				upload.flip();
				
				gles2BlitVBO = _wglGenBuffers();
				EaglercraftGPU.bindVAOGLArrayBufferNow(gles2BlitVBO);
				_wglBufferData(GL_ARRAY_BUFFER, upload, GL_STATIC_DRAW);
			}finally {
				MemoryStack.pop();
			}
			
			if(isVAOCapable) {
				gles2BlitVAO = _wglGenVertexArrays();
				_wglBindVertexArray(gles2BlitVAO);
				_wglEnableVertexAttribArray(0);
				_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
			}

			IShaderGL vertShader = _wglCreateShader(GL_VERTEX_SHADER);
			_wglShaderSource(vertShader, "#version 100\nprecision mediump float; attribute vec2 a_pos2f; varying vec2 v_tex2f; void main() { v_tex2f = a_pos2f; gl_Position = vec4(a_pos2f * 2.0 - 1.0, 0.0, 1.0); }");
			_wglCompileShader(vertShader);
			
			IShaderGL fragShader = _wglCreateShader(GL_FRAGMENT_SHADER);
			_wglShaderSource(fragShader, checkTextureLODCapable()
					? "#version 100\n#extension GL_EXT_shader_texture_lod : enable\nprecision mediump float; precision mediump sampler2D; varying vec2 v_tex2f; uniform sampler2D u_samplerTex; void main() { gl_FragColor = vec4(texture2DLodEXT(u_samplerTex, v_tex2f, 0.0).rgb, 1.0); }"
					: "#version 100\nprecision mediump float; precision mediump sampler2D; varying vec2 v_tex2f; uniform sampler2D u_samplerTex; void main() { gl_FragColor = vec4(texture2D(u_samplerTex, v_tex2f).rgb, 1.0); }");
			_wglCompileShader(fragShader);
			
			gles2BlitProgram = _wglCreateProgram();
			
			_wglAttachShader(gles2BlitProgram, vertShader);
			_wglAttachShader(gles2BlitProgram, fragShader);
			
			_wglBindAttribLocation(gles2BlitProgram, 0, "a_pos2f");
			
			_wglLinkProgram(gles2BlitProgram);
			
			_wglDetachShader(gles2BlitProgram, vertShader);
			_wglDetachShader(gles2BlitProgram, fragShader);

			_wglDeleteShader(vertShader);
			_wglDeleteShader(fragShader);
			
			_wglUseProgram(gles2BlitProgram);
			
			_wglUniform1i(_wglGetUniformLocation(gles2BlitProgram, "u_samplerTex"), 0);
		}
	}

	public static IFramebufferGL getBackBuffer() {
		return eagFramebuffer;
	}

	public static void enterVAOEmulationPhase() {
		if(glesVers < 300) {
			if(!isEmulatedVAOPhase) {
				if(isVAOCapable) {
					_wglDeleteVertexArrays(gles2BlitVAO);
				}
				gles2BlitVAO = EaglercraftGPU.createGLBufferArray();
				EaglercraftGPU.bindGLBufferArray(gles2BlitVAO);
				EaglercraftGPU.bindVAOGLArrayBuffer(gles2BlitVBO);
				EaglercraftGPU.enableVertexAttribArray(0);
				EaglercraftGPU.vertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
				isEmulatedVAOPhase = true;
			}
		}
	}

	private static void drawBlitQuad() {
		if(isEmulatedVAOPhase) {
			EaglercraftGPU.bindGLBufferArray(gles2BlitVAO);
			EaglercraftGPU.doDrawArrays(GL_TRIANGLES, 0, 6);
		}else {
			if(isVAOCapable) {
				_wglBindVertexArray(gles2BlitVAO);
				_wglDrawArrays(GL_TRIANGLES, 0, 6);
			}else {
				EaglercraftGPU.bindGLArrayBuffer(gles2BlitVBO);
				_wglEnableVertexAttribArray(0);
				_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
				_wglDrawArrays(GL_TRIANGLES, 0, 6);
			}
		}
	}

	public static void flipBuffer(int windowWidth, int windowHeight) {
		if(glesVers >= 300) {
			_wglBindFramebufferLow(_GL_READ_FRAMEBUFFER, eagFramebuffer);
			_wglBindFramebufferLow(_GL_DRAW_FRAMEBUFFER, null);
			_wglBlitFramebuffer(0, 0, width, height, 0, 0, windowWidth, windowHeight, GL_COLOR_BUFFER_BIT, GL_NEAREST);
			
			_wglBindFramebufferLow(_GL_FRAMEBUFFER, eagFramebuffer);
			
			if(windowWidth != width || windowHeight != height) {
				width = windowWidth;
				height = windowHeight;
				
				_wglBindRenderbuffer(_GL_RENDERBUFFER, gles3ColorRenderbuffer);
				_wglRenderbufferStorage(_GL_RENDERBUFFER, GL_RGBA8, windowWidth, windowHeight);
				
				_wglBindRenderbuffer(_GL_RENDERBUFFER, gles3DepthRenderbuffer);
				_wglRenderbufferStorage(_GL_RENDERBUFFER, _GL_DEPTH_COMPONENT32F, windowWidth, windowHeight);
			}
		}else {
			_wglBindFramebufferLow(_GL_FRAMEBUFFER, null);
			_wglActiveTexture(GL_TEXTURE0);
			_wglBindTexture(GL_TEXTURE_2D, gles2ColorTexture);
			
			int[] viewportStash = null;
			if(isEmulatedVAOPhase) {
				viewportStash = new int[4];
				EaglercraftGPU.glGetInteger(GL_VIEWPORT, viewportStash);
				GlStateManager.viewport(0, 0, windowWidth, windowHeight);
				GlStateManager.eagPushStateForGLES2BlitHack();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
			}else {
				_wglViewport(0, 0, windowWidth, windowHeight);
				_wglDisable(GL_DEPTH_TEST);
				_wglDisable(GL_BLEND);
			}

			EaglercraftGPU.clearCurrentBinding(EaglercraftGPU.CLEAR_BINDING_SHADER_PROGRAM | EaglercraftGPU.CLEAR_BINDING_ARRAY_BUFFER);

			EaglercraftGPU.bindGLShaderProgram(gles2BlitProgram);

			drawBlitQuad();

			if(windowWidth != width || windowHeight != height) {
				width = windowWidth;
				height = windowHeight;
				
				_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, windowWidth, windowHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
				
				_wglBindRenderbuffer(_GL_RENDERBUFFER, gles2DepthRenderbuffer);
				_wglRenderbufferStorage(_GL_RENDERBUFFER, _GL_DEPTH_COMPONENT16, windowWidth, windowHeight);
			}

			if(isEmulatedVAOPhase) {
				EaglercraftGPU.clearCurrentBinding(EaglercraftGPU.CLEAR_BINDING_TEXTURE0 | EaglercraftGPU.CLEAR_BINDING_ACTIVE_TEXTURE | EaglercraftGPU.CLEAR_BINDING_SHADER_PROGRAM);
				if(viewportStash[2] > 0) {
					GlStateManager.viewport(viewportStash[0], viewportStash[1], viewportStash[2], viewportStash[3]);
				}
				GlStateManager.eagPopStateForGLES2BlitHack();
			}else {
				EaglercraftGPU.clearCurrentBinding(EaglercraftGPU.CLEAR_BINDING_TEXTURE0 | EaglercraftGPU.CLEAR_BINDING_ACTIVE_TEXTURE | EaglercraftGPU.CLEAR_BINDING_SHADER_PROGRAM | EaglercraftGPU.CLEAR_BINDING_BUFFER_ARRAY);
			}

			_wglBindFramebuffer(_GL_FRAMEBUFFER, eagFramebuffer);
		}
	}

	public static void destroy() {
		if(eagFramebuffer != null) {
			_wglDeleteFramebuffer(eagFramebuffer);
			eagFramebuffer = null;
		}
		if(gles3ColorRenderbuffer != null) {
			_wglDeleteRenderbuffer(gles3ColorRenderbuffer);
			gles3ColorRenderbuffer = null;
		}
		if(gles3DepthRenderbuffer != null) {
			_wglDeleteRenderbuffer(gles3DepthRenderbuffer);
			gles3DepthRenderbuffer = null;
		}
		if(gles2ColorTexture != null) {
			_wglDeleteTextures(gles2ColorTexture);
			gles2ColorTexture = null;
		}
		if(gles2DepthRenderbuffer != null) {
			_wglDeleteRenderbuffer(gles2DepthRenderbuffer);
			gles2DepthRenderbuffer = null;
		}
		if(gles2BlitProgram != null) {
			_wglDeleteProgram(gles2BlitProgram);
			gles2BlitProgram = null;
		}
		if(gles2BlitVAO != null) {
			if(isEmulatedVAOPhase) {
				EaglercraftGPU.destroyGLBufferArray(gles2BlitVAO);
			}else if(isVAOCapable) {
				_wglDeleteVertexArrays(gles2BlitVAO);
			}
			gles2BlitVAO = null;
		}
		if(gles2BlitVBO != null) {
			_wglDeleteBuffers(gles2BlitVBO);
			gles2BlitVBO = null;
		}
		width = 0;
		height = 0;
		isVAOCapable = false;
		isEmulatedVAOPhase = false;
	}

}
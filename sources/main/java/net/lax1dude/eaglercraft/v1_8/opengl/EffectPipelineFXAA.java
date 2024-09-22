package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IRenderbufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class EffectPipelineFXAA {

	private static final Logger logger = LogManager.getLogger("EffectPipelineFXAA");

	public static final String fragmentShaderPath = "/assets/eagler/glsl/post_fxaa.fsh";
	public static final String fragmentShaderPrecision = "precision lowp int;\nprecision mediump float;\nprecision mediump sampler2D;\n";

	private static final int _GL_FRAMEBUFFER = 0x8D40;
	private static final int _GL_RENDERBUFFER = 0x8D41;
	private static final int _GL_COLOR_ATTACHMENT0 = 0x8CE0;
	private static final int _GL_DEPTH_ATTACHMENT = 0x8D00;
	private static final int _GL_DEPTH_COMPONENT16 = 0x81A5;
	private static final int _GL_DEPTH_COMPONENT32F = 0x8CAC;

	private static IProgramGL shaderProgram = null;
	private static IUniformGL u_screenSize2f = null;

	private static IFramebufferGL framebuffer = null;
	private static int framebufferColor = -1;
	private static IRenderbufferGL framebufferDepth = null;

	private static int currentWidth = -1;
	private static int currentHeight = -1;

	static void initialize() {
		String fragmentSource = EagRuntime.getRequiredResourceString(fragmentShaderPath);

		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(frag, GLSLHeader.getFragmentHeaderCompat(fragmentSource, fragmentShaderPrecision));
		_wglCompileShader(frag);

		if(_wglGetShaderi(frag, GL_COMPILE_STATUS) != GL_TRUE) {
			logger.error("Failed to compile GL_FRAGMENT_SHADER \"" + fragmentShaderPath + "\" for EffectPipelineFXAA!");
			String log = _wglGetShaderInfoLog(frag);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[FRAG] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Fragment shader \"" + fragmentShaderPath + "\" could not be compiled!");
		}

		shaderProgram = _wglCreateProgram();

		_wglAttachShader(shaderProgram, DrawUtils.vshLocal);
		_wglAttachShader(shaderProgram, frag);

		if(EaglercraftGPU.checkOpenGLESVersion() == 200) {
			VSHInputLayoutParser.applyLayout(shaderProgram, DrawUtils.vshLocalLayout);
		}

		_wglLinkProgram(shaderProgram);

		_wglDetachShader(shaderProgram, DrawUtils.vshLocal);
		_wglDetachShader(shaderProgram, frag);

		_wglDeleteShader(frag);

		if(_wglGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
			logger.error("Failed to link shader program for EffectPipelineFXAA!");
			String log = _wglGetProgramInfoLog(shaderProgram);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[LINK] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Shader program for EffectPipelineFXAA could not be linked!");
		}

		u_screenSize2f = _wglGetUniformLocation(shaderProgram, "u_screenSize2f");

		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_screenTexture"), 0);

		framebuffer = _wglCreateFramebuffer();
		framebufferColor = GlStateManager.generateTexture();

		GlStateManager.bindTexture(framebufferColor);

		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		framebufferDepth = _wglCreateRenderbuffer();
		_wglBindRenderbuffer(_GL_RENDERBUFFER, framebufferDepth);

		_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);
		_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(framebufferColor), 0);
		_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, _GL_RENDERBUFFER, framebufferDepth);

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
	}

	public static void begin(int width, int height) {
		if(currentWidth != width || currentHeight != height) {
			currentWidth = width;
			currentHeight = height;

			GlStateManager.bindTexture(framebufferColor);
			EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);

			_wglBindRenderbuffer(_GL_RENDERBUFFER, framebufferDepth);
			_wglRenderbufferStorage(_GL_RENDERBUFFER, EaglercraftGPU.checkOpenGLESVersion() == 200 ? _GL_DEPTH_COMPONENT16 : _GL_DEPTH_COMPONENT32F, width, height);
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);

		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	}

	public static void end() {
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);

		EaglercraftGPU.bindGLShaderProgram(shaderProgram);

		GlStateManager.bindTexture(framebufferColor);

		_wglUniform2f(u_screenSize2f, 1.0f / currentWidth, 1.0f / currentHeight);

		DrawUtils.drawStandardQuad2D();
	}

	public static void destroy() {
		if(shaderProgram != null) {
			_wglDeleteProgram(shaderProgram);
			shaderProgram = null;
		}
		u_screenSize2f = null;
		if(framebuffer != null) {
			_wglDeleteFramebuffer(framebuffer);
			framebuffer = null;
		}
		if(framebufferColor != -1) {
			GlStateManager.deleteTexture(framebufferColor);
			framebufferColor = -2;
		}
		if(framebufferDepth != null) {
			_wglDeleteRenderbuffer(framebufferDepth);
			framebufferDepth = null;
		}
	}

}

package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IRenderbufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionConstants;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

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
public class EffectPipelineFXAA {

	private static final Logger logger = LogManager.getLogger("EffectPipelineFXAA");

	public static final String fragmentShaderPath = "/assets/eagler/glsl/post_fxaa.fsh";

	private static final int _GL_FRAMEBUFFER = 0x8D40;
	private static final int _GL_RENDERBUFFER = 0x8D41;
	private static final int _GL_COLOR_ATTACHMENT0 = 0x8CE0;
	private static final int _GL_DEPTH_ATTACHMENT = 0x8D00;
	private static final int _GL_DEPTH_COMPONENT32F = 0x8CAC;

	private static IProgramGL shaderProgram = null;
	private static IUniformGL u_screenSize2f = null;

	private static IFramebufferGL framebuffer = null;
	private static int framebufferColor = -1;
	private static IRenderbufferGL framebufferDepth = null;

	private static int currentWidth = -1;
	private static int currentHeight = -1;

	static void initialize() {
		String fragmentSource = EagRuntime.getResourceString(fragmentShaderPath);
		if(fragmentSource == null) {
			throw new RuntimeException("EffectPipelineFXAA shader \"" + fragmentShaderPath + "\" is missing!");
		}

		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(frag, FixedFunctionConstants.VERSION + "\n" + fragmentSource);
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

		_wglAttachShader(shaderProgram, SpriteLevelMixer.vshLocal);
		_wglAttachShader(shaderProgram, frag);

		_wglLinkProgram(shaderProgram);

		_wglDetachShader(shaderProgram, SpriteLevelMixer.vshLocal);
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
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);

			_wglBindRenderbuffer(_GL_RENDERBUFFER, framebufferDepth);
			_wglRenderbufferStorage(_GL_RENDERBUFFER, _GL_DEPTH_COMPONENT32F, width, height);
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);

		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	}

	public static void end() {
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);

		EaglercraftGPU.bindGLBufferArray(SpriteLevelMixer.vertexArray);
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);

		GlStateManager.bindTexture(framebufferColor);

		_wglUniform2f(u_screenSize2f, 1.0f / currentWidth, 1.0f / currentHeight);

		_wglDrawArrays(GL_TRIANGLES, 0, 6);
	}

}

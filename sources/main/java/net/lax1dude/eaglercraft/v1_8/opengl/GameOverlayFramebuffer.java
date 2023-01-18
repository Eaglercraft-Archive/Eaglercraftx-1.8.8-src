package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IRenderbufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
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
public class GameOverlayFramebuffer {

	private static final int _GL_FRAMEBUFFER = 0x8D40;
	private static final int _GL_RENDERBUFFER = 0x8D41;
	private static final int _GL_COLOR_ATTACHMENT0 = 0x8CE0;
	private static final int _GL_DEPTH_ATTACHMENT = 0x8D00;
	private static final int _GL_DEPTH_COMPONENT16 = 0x81A5;

	private long age = -1l;

	private int currentWidth = -1;
	private int currentHeight = -1;

	private IFramebufferGL framebuffer = null;
	private IRenderbufferGL depthBuffer = null;

	private int framebufferColor = -1;

	public void beginRender(int width, int height) {
		if(framebuffer == null) {
			framebuffer = _wglCreateFramebuffer();
			depthBuffer = _wglCreateRenderbuffer();
			framebufferColor = GlStateManager.generateTexture();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);
			GlStateManager.bindTexture(framebufferColor);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(framebufferColor), 0);
			_wglBindRenderbuffer(_GL_RENDERBUFFER, depthBuffer);
			_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, _GL_RENDERBUFFER, depthBuffer);
		}

		if(currentWidth != width || currentHeight != height) {
			currentWidth = width;
			currentHeight = height;
			GlStateManager.bindTexture(framebufferColor);
			_wglTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			_wglBindRenderbuffer(_GL_RENDERBUFFER, depthBuffer);
			_wglRenderbufferStorage(_GL_RENDERBUFFER, _GL_DEPTH_COMPONENT16, width, height);
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);
	}

	public void endRender() {
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		age = System.currentTimeMillis();
	}

	public long getAge() {
		return age == -1l ? -1l : (System.currentTimeMillis() - age);
	}

	public int getTexture() {
		return framebufferColor;
	}

	public void destroy() {
		if(framebuffer != null) {
			_wglDeleteFramebuffer(framebuffer);
			_wglDeleteRenderbuffer(depthBuffer);
			GlStateManager.deleteTexture(framebufferColor);
			framebuffer = null;
			depthBuffer = null;
			framebufferColor = -1;
			age = -1l;
			_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		}
	}

}

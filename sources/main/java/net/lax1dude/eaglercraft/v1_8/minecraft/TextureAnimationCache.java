package net.lax1dude.eaglercraft.v1_8.minecraft;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IRenderbufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.ITextureGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.SpriteLevelMixer;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix3f;
import net.minecraft.client.renderer.GLAllocation;

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
public class TextureAnimationCache {

	public static final int _GL_FRAMEBUFFER = 0x8D40; // enum not defined in RealOpenGLEnums
	public static final int _GL_RENDERBUFFER = 0x8D41;
	public static final int _GL_COLOR_ATTACHMENT0 = 0x8CE0;

	public final int width;
	public final int height;
	public final int mipLevels;

	private int frameCount = 1;

	private int[] cacheTextures = null;
	private IFramebufferGL[] cacheFramebuffers = null;

	private IFramebufferGL interpolateFramebuffer = null;
	private IRenderbufferGL interpolateRenderbuffer = null;

	public TextureAnimationCache(int width, int height, int mipLevels) {
		this.width = width;
		this.height = height;
		this.mipLevels = mipLevels;
	}

	public void initialize(List<int[][]> frames, boolean enableInterpolation) {
		boolean init = cacheTextures == null;
		if(init) {
			cacheTextures = new int[mipLevels];
			for(int i = 0; i < cacheTextures.length; ++i) {
				cacheTextures[i] = GlStateManager.generateTexture();
				GlStateManager.bindTexture(cacheTextures[i]);
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			}

			if(enableInterpolation) {
				interpolateFramebuffer = _wglCreateFramebuffer();
				_wglBindFramebuffer(_GL_FRAMEBUFFER, interpolateFramebuffer);
				interpolateRenderbuffer = _wglCreateRenderbuffer();
				_wglBindRenderbuffer(_GL_RENDERBUFFER, interpolateRenderbuffer);
				_wglRenderbufferStorage(_GL_RENDERBUFFER, GL_RGBA8, width, height);
				_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0,
						_GL_RENDERBUFFER, interpolateRenderbuffer);
				_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
			}
		}
		
		frameCount = frames.size();
		IntBuffer pixels = GLAllocation.createDirectIntBuffer(width * height * frameCount);
		
		try {
			for(int i = 0; i < mipLevels; ++i) {
				pixels.clear();
				
				int lw = width >> i;
				int lh = height >> i;
				int tileLength = lw * lh;
				
				for(int j = 0; j < frameCount; ++j) {
					int[][] frame = frames.get(j);
					if(frame.length <= i) {
						throw new IllegalArgumentException("Frame #" + j + " only has " + frame.length + " mipmap levels! (" + mipLevels + " were expected)");
					}
					
					int[] frameLevel = frame[i];
					if(frameLevel.length != tileLength) {
						throw new IllegalArgumentException("Frame #" + j + " level " + i + " is " + frameLevel.length + " pixels large! (" + tileLength + " expected)");
					}
					
					pixels.put(frameLevel);
				}
				
				pixels.flip();
				
				GlStateManager.bindTexture(cacheTextures[i]);
				EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, lw, lh * frameCount, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			}
		}finally {
			EagRuntime.freeIntBuffer(pixels);
		}
		
		if(init) {
			cacheFramebuffers = new IFramebufferGL[mipLevels];
			for(int i = 0; i < mipLevels; ++i) {
				GlStateManager.bindTexture(cacheTextures[i]);
				IFramebufferGL fbo = _wglCreateFramebuffer();
				_wglBindFramebuffer(_GL_FRAMEBUFFER, fbo);
				_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
						EaglercraftGPU.getNativeTexture(cacheTextures[i]), 0);
				cacheFramebuffers[i] = fbo;
			}
			_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		}
	}

	public void free() {
		if(cacheTextures != null) {
			if(interpolateFramebuffer != null) {
				_wglDeleteFramebuffer(interpolateFramebuffer);
				interpolateFramebuffer = null;
			}
			if(interpolateRenderbuffer != null) {
				_wglDeleteRenderbuffer(interpolateRenderbuffer);
				interpolateRenderbuffer = null;
			}
			if(cacheFramebuffers != null) {
				for(int i = 0; i < mipLevels; ++i) {
					_wglDeleteFramebuffer(cacheFramebuffers[i]);
				}
				cacheFramebuffers = null;
			}
			for(int i = 0; i < cacheTextures.length; ++i) {
				GlStateManager.deleteTexture(cacheTextures[i]);
			}
			cacheTextures = null;
		}
	}

	public void copyFrameLevelsToTex2D(int animationFrame, int dx, int dy, int w, int h) {
		copyFrameLevelsToTex2D(animationFrame, mipLevels, dx, dy, w, h);
	}

	public void copyFrameLevelsToTex2D(int animationFrame, int levels, int dx, int dy, int w, int h) {
		for(int i = 0; i < levels; ++i) {
			copyFrameToTex2D(animationFrame, i, dx >> i, dy >> i, w >> i, h >> i);
		}
	}

	public void copyFrameToTex2D(int animationFrame, int level, int dx, int dy, int w, int h) {
		if(cacheTextures == null) {
			throw new IllegalStateException("Cannot copy from uninitialized TextureAnimationCache");
		}
		_wglBindFramebuffer(_GL_FRAMEBUFFER, cacheFramebuffers[level]);
		_wglReadBuffer(_GL_COLOR_ATTACHMENT0);
		_wglCopyTexSubImage2D(GL_TEXTURE_2D, level, dx, dy, 0, h * animationFrame, w, h);
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
	}

	public void copyInterpolatedFrameLevelsToTex2D(int animationFrameFrom, int animationFrameTo, float factor, int dx,
			int dy, int w, int h) {
		copyInterpolatedFrameLevelsToTex2D(animationFrameFrom, animationFrameTo, factor, mipLevels, dx, dy, w, h);
	}

	public void copyInterpolatedFrameLevelsToTex2D(int animationFrameFrom, int animationFrameTo, float factor,
			int levels, int dx, int dy, int w, int h) {
		for(int i = 0; i < levels; ++i) {
			copyInterpolatedFrameToTex2D(animationFrameFrom, animationFrameTo, factor, i, dx >> i, dy >> i, w >> i, h >> i);
		}
	}

	public void copyInterpolatedFrameToTex2D(int animationFrameFrom, int animationFrameTo, float factor, int level,
			int dx, int dy, int w, int h) {
		if(cacheTextures == null) {
			throw new IllegalStateException("Cannot copy from uninitialized TextureAnimationCache");
		}
		
		int storeTexture = GlStateManager.getTextureBound();
		
		_wglBindFramebuffer(_GL_FRAMEBUFFER, interpolateFramebuffer);
		GlStateManager.bindTexture(cacheTextures[level]);
		
		int[] storeViewport = new int[4];
		EaglercraftGPU.glGetInteger(GL_VIEWPORT, storeViewport);
		GlStateManager.viewport(0, 0, w, h);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GlStateManager.clear(GL_COLOR_BUFFER_BIT);
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_ONE, GL_ONE);
		
		Matrix3f matrix = new Matrix3f();
		matrix.m11 = 1.0f / frameCount;
		matrix.m21 = matrix.m11 * animationFrameFrom;
		
		SpriteLevelMixer.setMatrix3f(matrix);
		SpriteLevelMixer.setBlendColor(factor, factor, factor, factor);
		SpriteLevelMixer.setBiasColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		SpriteLevelMixer.drawSprite(0);
		
		matrix.m21 = matrix.m11 * animationFrameTo;
		SpriteLevelMixer.setMatrix3f(matrix);
		float fac1 = 1.0f - factor;
		SpriteLevelMixer.setBlendColor(fac1, fac1, fac1, fac1);
		
		SpriteLevelMixer.drawSprite(0);
		
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		
		GlStateManager.bindTexture(storeTexture);
		GlStateManager.viewport(storeViewport[0], storeViewport[1], storeViewport[2], storeViewport[3]);
		GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		_wglReadBuffer(_GL_COLOR_ATTACHMENT0);
		_wglCopyTexSubImage2D(GL_TEXTURE_2D, level, dx, dy, 0, 0, w, h);
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
	}

	public int getFrameCount() {
		return frameCount;
	}

}

package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IRenderbufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

/**
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

	private final boolean enableDepth;

	public GameOverlayFramebuffer() {
		this(true);
	}

	public GameOverlayFramebuffer(boolean enableDepth) {
		this.enableDepth = enableDepth;
	}

	public boolean beginRender(int width, int height) {
		if(framebuffer == null) {
			framebuffer = _wglCreateFramebuffer();
			depthBuffer = enableDepth ? _wglCreateRenderbuffer() : null;
			framebufferColor = GlStateManager.generateTexture();
			_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);
			GlStateManager.bindTexture(framebufferColor);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			_wglFramebufferTexture2D(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, EaglercraftGPU.getNativeTexture(framebufferColor), 0);
			if(enableDepth) {
				_wglBindRenderbuffer(_GL_RENDERBUFFER, depthBuffer);
				_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_DEPTH_ATTACHMENT, _GL_RENDERBUFFER, depthBuffer);
			}
		}

		boolean resized = currentWidth != width || currentHeight != height;
		if(resized) {
			currentWidth = width;
			currentHeight = height;
			GlStateManager.bindTexture(framebufferColor);
			EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			if(enableDepth) {
				_wglBindRenderbuffer(_GL_RENDERBUFFER, depthBuffer);
				_wglRenderbufferStorage(_GL_RENDERBUFFER, _GL_DEPTH_COMPONENT16, width, height);
			}
		}

		_wglBindFramebuffer(_GL_FRAMEBUFFER, framebuffer);
		return resized;
	}

	public void endRender() {
		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		age = EagRuntime.steadyTimeMillis();
	}

	public long getAge() {
		return age == -1l ? -1l : (EagRuntime.steadyTimeMillis() - age);
	}

	public int getTexture() {
		return framebufferColor;
	}

	public void destroy() {
		if(framebuffer != null) {
			_wglDeleteFramebuffer(framebuffer);
			if(enableDepth) {
				_wglDeleteRenderbuffer(depthBuffer);
			}
			GlStateManager.deleteTexture(framebufferColor);
			framebuffer = null;
			depthBuffer = null;
			framebufferColor = -1;
			age = -1l;
			_wglBindFramebuffer(_GL_FRAMEBUFFER, null);
		}
	}

}

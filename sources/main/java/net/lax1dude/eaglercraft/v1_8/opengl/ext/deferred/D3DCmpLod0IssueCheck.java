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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ExtGLEnums.*;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IRenderbufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.DrawUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GLSLHeader;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.VSHInputLayoutParser;

public class D3DCmpLod0IssueCheck {

	private static final Logger logger = LogManager.getLogger("D3DCmpLod0IssueCheck");

	public static boolean test() {
		String rendererString = EaglercraftGPU.glGetString(GL_RENDERER);
		if (rendererString == null || !rendererString.contains(" Direct3D11 ")) {
			return false;
		}

		logger.info("Checking for D3D compiler issue...");

		String vshLocalSrc = EagRuntime.getRequiredResourceString("/assets/eagler/glsl/local.vsh");
		List<VSHInputLayoutParser.ShaderInput> vertLayout = VSHInputLayoutParser.getShaderInputs(vshLocalSrc);
		IShaderGL vert = _wglCreateShader(GL_VERTEX_SHADER);
		_wglShaderSource(vert, GLSLHeader.getVertexHeaderCompat(vshLocalSrc, DrawUtils.vertexShaderPrecision));
		_wglCompileShader(vert);
		if (_wglGetShaderi(vert, GL_COMPILE_STATUS) != GL_TRUE) {
			Display.checkContextLost();
			_wglDeleteShader(vert);
			logger.error("Failed to compile vertex shader! This should not happen!");
			logger.error("Don't know how to proceed");
			return false;
		}

		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);
		_wglShaderSource(frag, GLSLHeader.getHeader()
				+ EagRuntime.getRequiredResourceString("/assets/eagler/glsl/deferred/check_d3d_cmplod0.fsh"));
		_wglCompileShader(frag);
		if (_wglGetShaderi(frag, GL_COMPILE_STATUS) != GL_TRUE) {
			Display.checkContextLost();
			logger.info(_wglGetShaderInfoLog(frag));
			_wglDeleteShader(vert);
			_wglDeleteShader(frag);
			logger.error("Failed to compile fragment shader! This should not happen!");
			logger.error("D3D compiler workarounds will be enabled");
			return true;
		}

		IProgramGL program = _wglCreateProgram();

		_wglAttachShader(program, vert);
		_wglAttachShader(program, frag);

		if (EaglercraftGPU.checkOpenGLESVersion() == 200) {
			VSHInputLayoutParser.applyLayout(program, vertLayout);
		}

		_wglLinkProgram(program);

		_wglDetachShader(program, vert);
		_wglDetachShader(program, frag);
		_wglDeleteShader(vert);
		_wglDeleteShader(frag);

		if (_wglGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
			Display.checkContextLost();
			_wglDeleteProgram(program);
			logger.error("Failed to link program! This should not happen!");
			logger.error("D3D compiler workarounds will be enabled");
			return true;
		}

		EaglercraftGPU.bindGLShaderProgram(program);
		_wglUniform1i(_wglGetUniformLocation(program, "u_inputTexture"), 0);

		int emptyTexture = GlStateManager.generateTexture();
		GlStateManager.bindTexture(emptyTexture);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_FUNC, GL_GREATER);
		_wglTexParameteri(GL_TEXTURE_2D, _GL_TEXTURE_COMPARE_MODE, _GL_COMPARE_REF_TO_TEXTURE);
		_wglTexImage2D(GL_TEXTURE_2D, 0, _GL_DEPTH_COMPONENT24, 256, 256, 0, _GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, (ByteBuffer)null);

		IFramebufferGL fbo = _wglCreateFramebuffer();
		_wglBindFramebuffer(_GL_FRAMEBUFFER, fbo);
		IRenderbufferGL rbo = _wglCreateRenderbuffer();
		_wglBindRenderbuffer(_GL_RENDERBUFFER, rbo);
		_wglRenderbufferStorage(_GL_RENDERBUFFER, GL_RGBA8, 256, 256);
		_wglFramebufferRenderbuffer(_GL_FRAMEBUFFER, _GL_COLOR_ATTACHMENT0, _GL_RENDERBUFFER, rbo);
		_wglDrawBuffers(_GL_COLOR_ATTACHMENT0);

		int err = _wglGetError();
		if (err != 0) {
			logger.error("Ignored OpenGL error while clearing error state: ", EaglercraftGPU.gluErrorString(err));
		}

		DrawUtils.drawStandardQuad2D();

		err = _wglGetError();

		GlStateManager.deleteTexture(emptyTexture);
		_wglDeleteProgram(program);
		_wglDeleteRenderbuffer(rbo);
		_wglDeleteFramebuffer(fbo);

		_wglBindFramebuffer(_GL_FRAMEBUFFER, null);

		if (err != 0) {
			logger.error("Using the test shader generated error: {}", EaglercraftGPU.gluErrorString(err));
			logger.error("D3D compiler workarounds will be enabled");
			return true;
		} else {
			return false;
		}
	}

}

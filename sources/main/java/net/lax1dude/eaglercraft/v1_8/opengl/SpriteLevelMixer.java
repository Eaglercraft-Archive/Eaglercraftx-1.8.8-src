/*
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.List;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.VSHInputLayoutParser.ShaderInput;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix3f;

public class SpriteLevelMixer {

	private static final Logger LOGGER = LogManager.getLogger("SpriteLevelMixer");

	public static final String fragmentShaderPath = "/assets/eagler/glsl/texture_mix.fsh";
	public static final String fragmentShaderPrecision = "precision lowp int;\nprecision highp float;\nprecision highp sampler2D;\n";

	private static IProgramGL shaderProgram = null;

	private static IUniformGL u_textureLod1f = null;
	private static IUniformGL u_blendFactor4f = null;
	private static IUniformGL u_blendBias4f = null;
	private static IUniformGL u_srcCoords4f = null;
	private static IUniformGL u_dstCoords4f = null;

	private static FloatBuffer matrixCopyBuffer = null;

	private static boolean blendColorChanged = true;
	private static float blendColorR = 1.0f;
	private static float blendColorG = 1.0f;
	private static float blendColorB = 1.0f;
	private static float blendColorA = 1.0f;

	private static boolean biasColorChanged = true;
	private static float biasColorR = 0.0f;
	private static float biasColorG = 0.0f;
	private static float biasColorB = 0.0f;
	private static float biasColorA = 0.0f;

	private static float srcViewW = 100.0f;
	private static float srcViewH = 100.0f;

	private static float dstViewW = 50.0f;
	private static float dstViewH = 50.0f;

	private static final Matrix3f identityMatrix = new Matrix3f();

	static void initialize(IShaderGL vertexShader, List<ShaderInput> vshSourceLayout) {
		String fragmentSource = EagRuntime.getRequiredResourceString(fragmentShaderPath);

		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(frag, GLSLHeader.getFragmentHeaderCompat(fragmentSource, fragmentShaderPrecision));
		_wglCompileShader(frag);

		if(_wglGetShaderi(frag, GL_COMPILE_STATUS) != GL_TRUE) {
			LOGGER.error("Failed to compile GL_FRAGMENT_SHADER \"" + fragmentShaderPath + "\" for SpriteLevelMixer!");
			String log = _wglGetShaderInfoLog(frag);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					LOGGER.error("[FRAG] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Fragment shader \"" + fragmentShaderPath + "\" could not be compiled!");
		}

		shaderProgram = _wglCreateProgram();

		_wglAttachShader(shaderProgram, vertexShader);
		_wglAttachShader(shaderProgram, frag);

		if(EaglercraftGPU.checkOpenGLESVersion() == 200) {
			VSHInputLayoutParser.applyLayout(shaderProgram, vshSourceLayout);
		}

		_wglLinkProgram(shaderProgram);

		_wglDetachShader(shaderProgram, vertexShader);
		_wglDetachShader(shaderProgram, frag);

		_wglDeleteShader(frag);

		if(_wglGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
			LOGGER.error("Failed to link shader program for SpriteLevelMixer!");
			String log = _wglGetProgramInfoLog(shaderProgram);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					LOGGER.error("[LINK] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Shader program for SpriteLevelMixer could not be linked!");
		}

		matrixCopyBuffer = EagRuntime.allocateFloatBuffer(9);

		EaglercraftGPU.bindGLShaderProgram(shaderProgram);

		u_textureLod1f = _wglGetUniformLocation(shaderProgram, "u_textureLod1f");
		u_blendFactor4f = _wglGetUniformLocation(shaderProgram, "u_blendFactor4f");
		u_blendBias4f = _wglGetUniformLocation(shaderProgram, "u_blendBias4f");
		u_srcCoords4f = _wglGetUniformLocation(shaderProgram, "u_srcCoords4f");
		u_dstCoords4f = _wglGetUniformLocation(shaderProgram, "u_dstCoords4f");

		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_inputTexture"), 0);

	}

	public static void setBlendColor(float r, float g, float b, float a) {
		if(r != blendColorR || g != blendColorG || b != blendColorB || a != blendColorA) {
			blendColorChanged = true;
			blendColorR = r;
			blendColorG = g;
			blendColorB = b;
			blendColorA = a;
		}
	}

	public static void setBiasColor(float r, float g, float b, float a) {
		if(r != biasColorR || g != biasColorG || b != biasColorB || a != biasColorA) {
			biasColorChanged = true;
			biasColorR = r;
			biasColorG = g;
			biasColorB = b;
			biasColorA = a;
		}
	}

	public static void srcSize(int w, int h) {
		srcViewW = w;
		srcViewH = h;
	}

	public static void dstSize(int w, int h) {
		dstViewW = w * 0.5f;
		dstViewH = h * 0.5f;
	}

	public static void srcDstSize(int w, int h) {
		srcViewW = w;
		srcViewH = h;
		dstViewW = w * 0.5f;
		dstViewH = h * 0.5f;
	}

	public static void drawSprite(int lvl, int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		
		if(EaglercraftGPU.checkTextureLODCapable()) {
			_wglUniform1f(u_textureLod1f, lvl);
		}else {
			if(lvl != 0) {
				LOGGER.error("Tried to copy from mipmap level {}, but this GPU does not support textureLod!", lvl);
			}
			_wglUniform1f(u_textureLod1f, 0.0f);
		}
		
		if(blendColorChanged) {
			_wglUniform4f(u_blendFactor4f, blendColorR, blendColorG, blendColorB, blendColorA);
			blendColorChanged = false;
		}
		
		if(biasColorChanged) {
			_wglUniform4f(u_blendBias4f, biasColorR, biasColorG, biasColorB, biasColorA);
			biasColorChanged = false;
		}
		
		_wglUniform4f(u_srcCoords4f, (float)srcX / srcViewW, (float)srcY / srcViewH, (float)srcW / srcViewW, (float)srcH / srcViewH);
		_wglUniform4f(u_dstCoords4f, (float) dstX / dstViewW - 1.0f, (float) dstY / dstViewH - 1.0f,
				(float) dstW / dstViewW, (float) dstH / dstViewH);
		
		DrawUtils.drawStandardQuad2D();
	}

	public static void destroy() {
		if(matrixCopyBuffer != null) {
			EagRuntime.freeFloatBuffer(matrixCopyBuffer);
			matrixCopyBuffer = null;
		}
		if(shaderProgram != null) {
			_wglDeleteProgram(shaderProgram);
			shaderProgram = null;
		}
		u_textureLod1f = null;
		u_blendFactor4f = null;
		u_blendBias4f = null;
		u_srcCoords4f = null;
		u_dstCoords4f = null;
	}

}
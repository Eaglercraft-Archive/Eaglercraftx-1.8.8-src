package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionConstants;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix3f;

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
public class SpriteLevelMixer {

	private static final Logger LOGGER = LogManager.getLogger("SpriteLevelMixer");

	public static final String vertexShaderPath = "/assets/eagler/glsl/local.vsh";
	public static final String fragmentShaderPath = "/assets/eagler/glsl/texture_mix.fsh";

	public static IShaderGL vshLocal = null;

	private static IBufferGL vertexBuffer = null;
	public static IBufferArrayGL vertexArray = null;
	private static IProgramGL shaderProgram = null;

	private static IUniformGL u_textureLod1f = null;
	private static IUniformGL u_blendFactor4f = null;
	private static IUniformGL u_blendBias4f = null;
	private static IUniformGL u_matrixTransform = null;

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

	private static boolean matrixChanged = true;
	private static final Matrix3f transformMatrix = new Matrix3f();

	private static final Matrix3f identityMatrix = new Matrix3f();

	static void initialize() {

		String vertexSource = EagRuntime.getResourceString(vertexShaderPath);
		if(vertexSource == null) {
			throw new RuntimeException("SpriteLevelMixer shader \"" + vertexShaderPath + "\" is missing!");
		}

		String fragmentSource = EagRuntime.getResourceString(fragmentShaderPath);
		if(fragmentSource == null) {
			throw new RuntimeException("SpriteLevelMixer shader \"" + fragmentShaderPath + "\" is missing!");
		}

		vshLocal = _wglCreateShader(GL_VERTEX_SHADER);
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(vshLocal, FixedFunctionConstants.VERSION + "\n" + vertexSource);
		_wglCompileShader(vshLocal);

		if(_wglGetShaderi(vshLocal, GL_COMPILE_STATUS) != GL_TRUE) {
			LOGGER.error("Failed to compile GL_VERTEX_SHADER \"" + vertexShaderPath + "\" for SpriteLevelMixer!");
			String log = _wglGetShaderInfoLog(vshLocal);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					LOGGER.error("[VERT] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Vertex shader \"" + vertexShaderPath + "\" could not be compiled!");
		}

		_wglShaderSource(frag, FixedFunctionConstants.VERSION + "\n" + fragmentSource);
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

		_wglAttachShader(shaderProgram, vshLocal);
		_wglAttachShader(shaderProgram, frag);

		_wglLinkProgram(shaderProgram);

		_wglDetachShader(shaderProgram, vshLocal);
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
		u_matrixTransform = _wglGetUniformLocation(shaderProgram, "u_matrixTransform");

		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_inputTexture"), 0);

		vertexArray = _wglGenVertexArrays();
		vertexBuffer = _wglGenBuffers();

		FloatBuffer verts = EagRuntime.allocateFloatBuffer(12);
		verts.put(new float[] {
				0.0f, 0.0f,  1.0f, 0.0f,  0.0f, 1.0f,
				1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f
		});
		verts.flip();

		EaglercraftGPU.bindGLBufferArray(vertexArray);

		EaglercraftGPU.bindGLArrayBuffer(vertexBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);

		EagRuntime.freeFloatBuffer(verts);

		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);

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

	public static void setIdentityMatrix() {
		setMatrix3f(identityMatrix);
	}

	public static void setMatrix3f(Matrix3f matrix) {
		if(!matrix.equals(transformMatrix)) {
			matrixChanged = true;
			transformMatrix.load(matrix);
		}
	}

	public static void drawSprite(float level) {
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		
		_wglUniform1f(u_textureLod1f, level);
		
		if(blendColorChanged) {
			_wglUniform4f(u_blendFactor4f, blendColorR, blendColorG, blendColorB, blendColorA);
			blendColorChanged = false;
		}		
		
		if(biasColorChanged) {
			_wglUniform4f(u_blendBias4f, biasColorR, biasColorG, biasColorB, biasColorA);
			biasColorChanged = false;
		}
		
		if(matrixChanged) {
			matrixCopyBuffer.clear();
			transformMatrix.store(matrixCopyBuffer);
			matrixCopyBuffer.flip();
			_wglUniformMatrix3fv(u_matrixTransform, false, matrixCopyBuffer);
			matrixChanged = false;
		}
		
		EaglercraftGPU.bindGLBufferArray(vertexArray);
		
		_wglDrawArrays(GL_TRIANGLES, 0, 6);
	}

}

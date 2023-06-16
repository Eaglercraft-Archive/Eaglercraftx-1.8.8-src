package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionConstants;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

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
public class InstancedParticleRenderer {

	private static final Logger logger = LogManager.getLogger("InstancedParticleRenderer");

	public static final String vertexShaderPath = "/assets/eagler/glsl/accel_particle.vsh";
	public static final String fragmentShaderPath = "/assets/eagler/glsl/accel_particle.fsh";

	private static ByteBuffer particleBuffer = null;
	private static int particleCount = 0;
	private static boolean particlesHasOverflowed = false;

	private static final int BYTES_PER_PARTICLE = 24;
	private static final int PARTICLE_LIMIT = 5461;

	private static IProgramGL shaderProgram = null;
	private static IUniformGL u_matrixTransform = null;
	private static FloatBuffer matrixCopyBuffer = null;
	private static IUniformGL u_texCoordSize2f_particleSize1f = null;
	private static IUniformGL u_transformParam_1_2_3_4_f = null;
	private static IUniformGL u_transformParam_5_f = null;
	private static IUniformGL u_color4f = null;

	private static IBufferArrayGL vertexArray = null;
	private static IBufferGL vertexBuffer = null;

	private static IBufferGL instancesBuffer = null;

	private static float stateColorR = -999.0f;
	private static float stateColorG = -999.0f;
	private static float stateColorB = -999.0f;
	private static float stateColorA = -999.0f;
	private static int stateColorSerial = -1;
	
	private static final Matrix4f tmpMatrix = new Matrix4f();
	private static int stateModelMatrixSerial = -1;
	private static int stateProjectionMatrixSerial = -1;

	private static float stateTexCoordWidth = -999.0f;
	private static float stateTexCoordHeight = -999.0f;
	private static float stateParticleCoordSize = -999.0f;

	private static float stateTransformParam1 = -999.0f;
	private static float stateTransformParam2 = -999.0f;
	private static float stateTransformParam3 = -999.0f;
	private static float stateTransformParam4 = -999.0f;
	private static float stateTransformParam5 = -999.0f;

	static void initialize() {
		String vertexSource = EagRuntime.getResourceString(vertexShaderPath);
		if(vertexSource == null) {
			throw new RuntimeException("InstancedParticleRenderer shader \"" + vertexShaderPath + "\" is missing!");
		}

		String fragmentSource = EagRuntime.getResourceString(fragmentShaderPath);
		if(fragmentSource == null) {
			throw new RuntimeException("InstancedParticleRenderer shader \"" + fragmentShaderPath + "\" is missing!");
		}

		IShaderGL vert = _wglCreateShader(GL_VERTEX_SHADER);
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(vert, FixedFunctionConstants.VERSION + "\n" + vertexSource);
		_wglCompileShader(vert);

		if(_wglGetShaderi(vert, GL_COMPILE_STATUS) != GL_TRUE) {
			logger.error("Failed to compile GL_VERTEX_SHADER \"" + vertexShaderPath + "\" for InstancedParticleRenderer!");
			String log = _wglGetShaderInfoLog(vert);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[VERT] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Vertex shader \"" + vertexShaderPath + "\" could not be compiled!");
		}

		_wglShaderSource(frag, FixedFunctionConstants.VERSION + "\n" + fragmentSource);
		_wglCompileShader(frag);

		if(_wglGetShaderi(frag, GL_COMPILE_STATUS) != GL_TRUE) {
			logger.error("Failed to compile GL_FRAGMENT_SHADER \"" + fragmentShaderPath + "\" for InstancedParticleRenderer!");
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

		_wglAttachShader(shaderProgram, vert);
		_wglAttachShader(shaderProgram, frag);

		_wglLinkProgram(shaderProgram);

		_wglDetachShader(shaderProgram, vert);
		_wglDetachShader(shaderProgram, frag);

		_wglDeleteShader(vert);
		_wglDeleteShader(frag);

		if(_wglGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
			logger.error("Failed to link shader program for InstancedParticleRenderer!");
			String log = _wglGetProgramInfoLog(shaderProgram);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[LINK] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Shader program for InstancedParticleRenderer could not be linked!");
		}

		matrixCopyBuffer = EagRuntime.allocateFloatBuffer(16);
		particleBuffer = EagRuntime.allocateByteBuffer(PARTICLE_LIMIT * BYTES_PER_PARTICLE);

		EaglercraftGPU.bindGLShaderProgram(shaderProgram);

		u_matrixTransform = _wglGetUniformLocation(shaderProgram, "u_matrixTransform");
		u_texCoordSize2f_particleSize1f = _wglGetUniformLocation(shaderProgram, "u_texCoordSize2f_particleSize1f");
		u_transformParam_1_2_3_4_f = _wglGetUniformLocation(shaderProgram, "u_transformParam_1_2_3_4_f");
		u_transformParam_5_f = _wglGetUniformLocation(shaderProgram, "u_transformParam_5_f");
		u_color4f = _wglGetUniformLocation(shaderProgram, "u_color4f");

		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_inputTexture"), 0);
		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_lightmapTexture"), 1);

		vertexArray = _wglGenVertexArrays();
		vertexBuffer = _wglGenBuffers();
		instancesBuffer = _wglGenBuffers();

		FloatBuffer verts = EagRuntime.allocateFloatBuffer(12);
		verts.put(new float[] {
				-1.0f, -1.0f,  -1.0f,  1.0f,   1.0f, -1.0f,
				-1.0f,  1.0f,   1.0f,  1.0f,   1.0f, -1.0f
		});
		verts.flip();

		EaglercraftGPU.bindGLBufferArray(vertexArray);

		EaglercraftGPU.bindGLArrayBuffer(vertexBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);

		EagRuntime.freeFloatBuffer(verts);

		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
		_wglVertexAttribDivisor(0, 0);

		EaglercraftGPU.bindGLArrayBuffer(instancesBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, particleBuffer.remaining(), GL_STATIC_DRAW);

		_wglEnableVertexAttribArray(1);
		_wglVertexAttribPointer(1, 3, GL_FLOAT, false, 24, 0);
		_wglVertexAttribDivisor(1, 1);

		_wglEnableVertexAttribArray(2);
		_wglVertexAttribPointer(2, 2, GL_UNSIGNED_SHORT, false, 24, 12);
		_wglVertexAttribDivisor(2, 1);

		_wglEnableVertexAttribArray(3);
		_wglVertexAttribPointer(3, 2, GL_UNSIGNED_BYTE, true, 24, 16);
		_wglVertexAttribDivisor(3, 1);

		_wglEnableVertexAttribArray(4);
		_wglVertexAttribPointer(4, 2, GL_UNSIGNED_BYTE, false, 24, 18);
		_wglVertexAttribDivisor(4, 1);

		_wglEnableVertexAttribArray(5);
		_wglVertexAttribPointer(5, 4, GL_UNSIGNED_BYTE, true, 24, 20);
		_wglVertexAttribDivisor(5, 1);

	}

	public static void begin() {
		particleBuffer.clear();
		particleCount = 0;
		particlesHasOverflowed = false;
	}

	public static void appendParticle(float posX, float posY, float posZ, int particleTextureX, int particleTextureY,
			int lightMapX, int lightMapY, int particleSize, int particleTexSize, float r, float g, float b, float a) {
		int color = ((int)(a * 255.0f) << 24) | ((int)(r * 255.0f) << 16) | ((int)(g * 255.0f) << 8) | (int)(b * 255.0f);
		appendParticle(posX, posY, posZ, particleTextureX, particleTextureY, lightMapX, lightMapY, particleSize, particleTexSize, color);
	}

	public static void appendParticle(float posX, float posY, float posZ, int particleTextureX, int particleTextureY,
			int lightMapX, int lightMapY, int particleSize, int particleTexSize, int rgba) {
		if(particlesHasOverflowed) {
			return;
		}
		if(particleCount >= PARTICLE_LIMIT) {
			particlesHasOverflowed = true;
			logger.error("Particle buffer has overflowed! Exceeded {} particles, no more particles will be rendered.", PARTICLE_LIMIT);
			return;
		}
		++particleCount;
		ByteBuffer buf = particleBuffer;
		buf.putFloat(posX);
		buf.putFloat(posY);
		buf.putFloat(posZ);
		buf.putShort((short)particleTextureX);
		buf.putShort((short)particleTextureY);
		buf.put((byte)lightMapX);
		buf.put((byte)lightMapY);
		buf.put((byte)particleSize);
		buf.put((byte)particleTexSize);
		buf.putInt(rgba);
	}

	public static void render(float texCoordWidth, float texCoordHeight, float particleCoordSize, float transformParam1,
			float transformParam2, float transformParam3, float transformParam4, float transformParam5) {
		if(particleCount == 0) {
			return;
		}
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);

		if (texCoordWidth != stateTexCoordWidth || texCoordHeight != stateTexCoordHeight
				|| particleCoordSize != stateParticleCoordSize) {
			_wglUniform3f(u_texCoordSize2f_particleSize1f, texCoordWidth, texCoordHeight, particleCoordSize);
			stateTexCoordWidth = texCoordWidth;
			stateTexCoordHeight = texCoordHeight;
			stateParticleCoordSize = particleCoordSize;
		}

		if (transformParam1 != stateTransformParam1 || transformParam2 != stateTransformParam2
				|| transformParam3 != stateTransformParam3 || transformParam4 != stateTransformParam4) {
			_wglUniform4f(u_transformParam_1_2_3_4_f, transformParam1, transformParam2, transformParam3, transformParam4);
			stateTransformParam1 = transformParam1;
			stateTransformParam2 = transformParam2;
			stateTransformParam3 = transformParam3;
			stateTransformParam4 = transformParam4;
		}

		if (transformParam5 != stateTransformParam5) {
			_wglUniform1f(u_transformParam_5_f, transformParam5);
			stateTransformParam5 = transformParam5;
		}

		int serial = GlStateManager.stateColorSerial;
		if(stateColorSerial != serial) {
			stateColorSerial = serial;
			float r = GlStateManager.stateColorR;
			float g = GlStateManager.stateColorG;
			float b = GlStateManager.stateColorB;
			float a = GlStateManager.stateColorA;
			if(stateColorR != r || stateColorG != g ||
				stateColorB != b || stateColorA != a) {
				_wglUniform4f(u_color4f, r, g, b, a);
				stateColorR = r;
				stateColorG = g;
				stateColorB = b;
				stateColorA = a;
			}
		}

		int ptr1 = GlStateManager.modelMatrixStackPointer;
		int serial1 = GlStateManager.modelMatrixStackAccessSerial[ptr1];
		int ptr2 = GlStateManager.projectionMatrixStackPointer;
		int serial2 = GlStateManager.projectionMatrixStackAccessSerial[ptr2];
		if(stateModelMatrixSerial != serial1 || stateProjectionMatrixSerial != serial2) {
			stateModelMatrixSerial = serial1;
			stateProjectionMatrixSerial = serial2;
			Matrix4f.mul(GlStateManager.projectionMatrixStack[ptr2], GlStateManager.modelMatrixStack[ptr1], tmpMatrix);
			matrixCopyBuffer.clear();
			tmpMatrix.store(matrixCopyBuffer);
			matrixCopyBuffer.flip();
			_wglUniformMatrix4fv(u_matrixTransform, false, matrixCopyBuffer);
		}

		EaglercraftGPU.bindGLArrayBuffer(instancesBuffer);
		EaglercraftGPU.bindGLBufferArray(vertexArray);
		
		int p = particleBuffer.position();
		int l = particleBuffer.limit();

		particleBuffer.flip();
		_wglBufferSubData(GL_ARRAY_BUFFER, 0, particleBuffer);

		particleBuffer.position(p);
		particleBuffer.limit(l);

		_wglDrawArraysInstanced(GL_TRIANGLES, 0, 6, particleCount);
	}

}

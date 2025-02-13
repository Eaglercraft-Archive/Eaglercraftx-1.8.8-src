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
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

public class InstancedParticleRenderer {

	private static final Logger logger = LogManager.getLogger("InstancedParticleRenderer");

	public static final String vertexShaderPath = "/assets/eagler/glsl/accel_particle.vsh";
	public static final String vertexShaderPrecision = "precision lowp int;\nprecision highp float;\nprecision mediump sampler2D;\n";

	public static final String fragmentShaderPath = "/assets/eagler/glsl/accel_particle.fsh";
	public static final String fragmentShaderPrecision = "precision lowp int;\nprecision mediump float;\nprecision mediump sampler2D;\n";

	private static ByteBuffer particleBuffer = null;
	private static int particleCount = 0;
	private static boolean particlesHasOverflowed = false;

	private static final int BYTES_PER_PARTICLE = 24;
	private static final int PARTICLE_LIMIT = 5461;

	private static IProgramGL shaderProgram = null;
	private static IUniformGL u_matrixTransform = null;
	private static FloatBuffer matrixCopyBuffer = null;
	private static IUniformGL u_texCoordSize2f_particleSize1f = null;
	private static IUniformGL u_transformParam_1_2_5_f = null;
	private static IUniformGL u_transformParam_3_4_f = null;
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
		String vertexSource = EagRuntime.getRequiredResourceString(vertexShaderPath);
		String fragmentSource = EagRuntime.getRequiredResourceString(fragmentShaderPath);

		IShaderGL vert = _wglCreateShader(GL_VERTEX_SHADER);
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(vert, GLSLHeader.getVertexHeaderCompat(vertexSource, vertexShaderPrecision));
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

		_wglShaderSource(frag, GLSLHeader.getFragmentHeaderCompat(fragmentSource, fragmentShaderPrecision));
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

		if(EaglercraftGPU.checkOpenGLESVersion() == 200) {
			VSHInputLayoutParser.applyLayout(shaderProgram, VSHInputLayoutParser.getShaderInputs(vertexSource));
		}

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
		u_transformParam_1_2_5_f = _wglGetUniformLocation(shaderProgram, "u_transformParam_1_2_5_f");
		u_transformParam_3_4_f = _wglGetUniformLocation(shaderProgram, "u_transformParam_3_4_f");
		u_color4f = _wglGetUniformLocation(shaderProgram, "u_color4f");

		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_inputTexture"), 0);
		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_lightmapTexture"), 1);

		vertexArray = EaglercraftGPU.createGLBufferArray();
		vertexBuffer = _wglGenBuffers();
		instancesBuffer = _wglGenBuffers();

		FloatBuffer verts = EagRuntime.allocateFloatBuffer(12);
		verts.put(new float[] {
				-1.0f, -1.0f,  -1.0f,  1.0f,   1.0f, -1.0f,
				-1.0f,  1.0f,   1.0f,  1.0f,   1.0f, -1.0f
		});
		verts.flip();

		EaglercraftGPU.bindGLBufferArray(vertexArray);

		EaglercraftGPU.bindVAOGLArrayBufferNow(vertexBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);

		EagRuntime.freeFloatBuffer(verts);

		EaglercraftGPU.enableVertexAttribArray(0);
		EaglercraftGPU.vertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
		EaglercraftGPU.vertexAttribDivisor(0, 0);

		EaglercraftGPU.bindVAOGLArrayBufferNow(instancesBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, particleBuffer.remaining(), GL_STREAM_DRAW);

		EaglercraftGPU.enableVertexAttribArray(1);
		EaglercraftGPU.vertexAttribPointer(1, 3, GL_FLOAT, false, 24, 0);
		EaglercraftGPU.vertexAttribDivisor(1, 1);

		EaglercraftGPU.enableVertexAttribArray(2);
		EaglercraftGPU.vertexAttribPointer(2, 2, GL_UNSIGNED_SHORT, false, 24, 12);
		EaglercraftGPU.vertexAttribDivisor(2, 1);

		EaglercraftGPU.enableVertexAttribArray(3);
		EaglercraftGPU.vertexAttribPointer(3, 2, GL_UNSIGNED_BYTE, true, 24, 16);
		EaglercraftGPU.vertexAttribDivisor(3, 1);

		EaglercraftGPU.enableVertexAttribArray(4);
		EaglercraftGPU.vertexAttribPointer(4, 2, GL_UNSIGNED_BYTE, false, 24, 18);
		EaglercraftGPU.vertexAttribDivisor(4, 1);

		EaglercraftGPU.enableVertexAttribArray(5);
		EaglercraftGPU.vertexAttribPointer(5, 4, GL_UNSIGNED_BYTE, true, 24, 20);
		EaglercraftGPU.vertexAttribDivisor(5, 1);

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
				|| transformParam5 != stateTransformParam5) {
			_wglUniform3f(u_transformParam_1_2_5_f, transformParam1, transformParam2, transformParam5);
			stateTransformParam1 = transformParam1;
			stateTransformParam2 = transformParam2;
			stateTransformParam5 = transformParam5;
		}

		if (transformParam3 != stateTransformParam3 || transformParam4 != stateTransformParam4) {
			_wglUniform2f(u_transformParam_3_4_f, transformParam3, transformParam4);
			stateTransformParam3 = transformParam3;
			stateTransformParam4 = transformParam4;
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

		EaglercraftGPU.doDrawArraysInstanced(GL_TRIANGLES, 0, 6, particleCount);
	}

	public static void stupidColorSetHack(IUniformGL color4f) {
		_wglUniform4f(color4f, GlStateManager.stateColorR, GlStateManager.stateColorG, GlStateManager.stateColorB, GlStateManager.stateColorA);
	}

	public static void destroy() {
		if(particleBuffer != null) {
			EagRuntime.freeByteBuffer(particleBuffer);
			particleBuffer = null;
		}
		if(shaderProgram != null) {
			_wglDeleteProgram(shaderProgram);
			shaderProgram = null;
		}
		if(matrixCopyBuffer != null) {
			EagRuntime.freeFloatBuffer(matrixCopyBuffer);
			matrixCopyBuffer = null;
		}
		u_matrixTransform = null;
		u_texCoordSize2f_particleSize1f = null;
		u_transformParam_1_2_5_f = null;
		u_transformParam_3_4_f = null;
		u_color4f = null;
		if(vertexArray != null) {
			EaglercraftGPU.destroyGLBufferArray(vertexArray);
			vertexArray = null;
		}
		if(vertexBuffer != null) {
			_wglDeleteBuffers(vertexBuffer);
			vertexBuffer = null;
		}
		if(instancesBuffer != null) {
			_wglDeleteBuffers(instancesBuffer);
			instancesBuffer = null;
		}
	}

}
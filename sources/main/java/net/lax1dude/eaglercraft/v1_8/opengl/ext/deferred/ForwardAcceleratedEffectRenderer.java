package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderAccelParticleForward;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.PipelineShaderAccelParticleGBuffer;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

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
public class ForwardAcceleratedEffectRenderer extends AbstractAcceleratedEffectRenderer {

	private static final Logger logger = LogManager.getLogger("ForwardAcceleratedEffectRenderer");

	private ByteBuffer particleBuffer = null;
	private int particleCount = 0;
	private boolean particlesHasOverflowed = false;

	private static final int BYTES_PER_PARTICLE = 24;
	private static final int PARTICLE_LIMIT = 5461;

	private PipelineShaderAccelParticleForward shaderProgram = null;

	private IBufferArrayGL vertexArray = null;
	private IBufferGL vertexBuffer = null;

	private IBufferGL instancesBuffer = null;

	private static final Matrix4f tmpMatrix = new Matrix4f();

	private float f1;
	private float f2;
	private float f3;
	private float f4;
	private float f5;

	public static boolean isMaterialNormalTexture = false;

	public void initialize(boolean dynamicLights, int sunShadows) {
		destroy();
	
		shaderProgram = PipelineShaderAccelParticleForward.compile(dynamicLights, sunShadows);
		shaderProgram.loadUniforms();

		particleBuffer = EagRuntime.allocateByteBuffer(PARTICLE_LIMIT * BYTES_PER_PARTICLE);

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

	@Override
	public void draw(float texCoordWidth, float texCoordHeight) {
		if(particleCount == 0) {
			return;
		}
	
		shaderProgram.useProgram();

		_wglUniform3f(shaderProgram.uniforms.u_texCoordSize2f_particleSize1f, texCoordWidth, texCoordHeight, 0.0625f);
		_wglUniform4f(shaderProgram.uniforms.u_transformParam_1_2_3_4_f, f1, f5, f2, f3);
		_wglUniform1f(shaderProgram.uniforms.u_transformParam_5_f, f4);
		if(isMaterialNormalTexture) {
			_wglUniform2f(shaderProgram.uniforms.u_textureYScale2f, 0.5f, 0.5f);
		}else {
			_wglUniform2f(shaderProgram.uniforms.u_textureYScale2f, 1.0f, 0.0f);
		}

		EaglerDeferredPipeline.uniformMatrixHelper(shaderProgram.uniforms.u_modelViewMatrix4f, DeferredStateManager.passViewMatrix);
		EaglerDeferredPipeline.uniformMatrixHelper(shaderProgram.uniforms.u_projectionMatrix4f, DeferredStateManager.passProjMatrix);
		EaglerDeferredPipeline.uniformMatrixHelper(shaderProgram.uniforms.u_inverseViewMatrix4f, DeferredStateManager.passInverseViewMatrix);

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

	@Override
	public void begin(float partialTicks) {
		this.partialTicks = partialTicks;

		particleBuffer.clear();
		particleCount = 0;
		particlesHasOverflowed = false;

		Entity et = Minecraft.getMinecraft().getRenderViewEntity();
		if(et != null) {
			f1 = MathHelper.cos(et.rotationYaw * 0.017453292F);
			f2 = MathHelper.sin(et.rotationYaw * 0.017453292F);
			f3 = -f2 * MathHelper.sin(et.rotationPitch * 0.017453292F);
			f4 = f1 * MathHelper.sin(et.rotationPitch * 0.017453292F);
			f5 = MathHelper.cos(et.rotationPitch * 0.017453292F);
		}
	}

	@Override
	public void drawParticle(float posX, float posY, float posZ, int particleIndexX, int particleIndexY,
			int lightMapData, int texSize, float particleSize, int rgba) {
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
		buf.putShort((short)particleIndexX);
		buf.putShort((short)particleIndexY);
		buf.put((byte)(lightMapData & 0xFF));
		buf.put((byte)((lightMapData >> 16) & 0xFF));
		buf.put((byte)(particleSize * 16.0f));
		buf.put((byte)texSize);
		buf.putInt(rgba);
	}

	public void destroy() {
		if(particleBuffer != null) {
			EagRuntime.freeByteBuffer(particleBuffer);
			particleBuffer = null;
		}
		if(shaderProgram != null) {
			shaderProgram.destroy();
			shaderProgram = null;
		}
		if(vertexArray != null) {
			_wglDeleteVertexArrays(vertexArray);
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

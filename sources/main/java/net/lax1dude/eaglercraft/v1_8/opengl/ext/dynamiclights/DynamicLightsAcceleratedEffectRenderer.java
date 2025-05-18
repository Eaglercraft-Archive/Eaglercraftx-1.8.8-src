/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IVertexArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.InstancedParticleRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.AbstractAcceleratedEffectRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.program.DynamicLightsAccelParticleShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class DynamicLightsAcceleratedEffectRenderer extends AbstractAcceleratedEffectRenderer {

	private static final Logger logger = LogManager.getLogger("DynamicLightsAcceleratedEffectRenderer");

	private ByteBuffer particleBuffer = null;
	private int particleCount = 0;
	private boolean particlesHasOverflowed = false;

	private static final int BYTES_PER_PARTICLE = 24;
	private static final int PARTICLE_LIMIT = 5461;

	private DynamicLightsAccelParticleShader shaderProgram = null;

	private IVertexArrayGL vertexArray = null;
	private IBufferGL vertexBuffer = null;

	private IBufferGL instancesBuffer = null;

	private float f1;
	private float f2;
	private float f3;
	private float f4;
	private float f5;

	public static boolean isMaterialNormalTexture = false;

	public void initialize() {
		destroy();

		if(DynamicLightsPipelineCompiler.matrixCopyBuffer == null) {
			DynamicLightsPipelineCompiler.matrixCopyBuffer = GLAllocation.createDirectFloatBuffer(16);
		}

		shaderProgram = DynamicLightsAccelParticleShader.compile();
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

		EaglercraftGPU.bindGLVertexArray(vertexArray);

		EaglercraftGPU.bindGLArrayBuffer(vertexBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);

		EagRuntime.freeFloatBuffer(verts);

		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 8, 0);
		_wglVertexAttribDivisor(0, 0);

		EaglercraftGPU.bindGLArrayBuffer(instancesBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, particleBuffer.capacity(), GL_STREAM_DRAW);

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
		_wglUniform3f(shaderProgram.uniforms.u_transformParam_1_2_5_f, f1, f5, f4);
		_wglUniform2f(shaderProgram.uniforms.u_transformParam_3_4_f, f2, f3);
		InstancedParticleRenderer.stupidColorSetHack(shaderProgram.uniforms.u_color4f);

		FloatBuffer buf = DynamicLightsPipelineCompiler.matrixCopyBuffer;
		buf.clear();
		GlStateManager.getFloat(GL_MODELVIEW_MATRIX, buf);
		buf.flip();
		_wglUniformMatrix4fv(shaderProgram.uniforms.u_modelViewMatrix4f, false, buf);
		buf.clear();
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, buf);
		buf.flip();
		_wglUniformMatrix4fv(shaderProgram.uniforms.u_projectionMatrix4f, false, buf);
		buf.clear();
		DynamicLightsStateManager.inverseViewMatrix.store(buf);
		buf.flip();
		_wglUniformMatrix4fv(shaderProgram.uniforms.u_inverseViewMatrix4f, false, buf);

		EaglercraftGPU.bindGLArrayBuffer(instancesBuffer);
		EaglercraftGPU.bindGLVertexArray(vertexArray);

		int p = particleBuffer.position();
		int l = particleBuffer.limit();

		particleBuffer.flip();
		_wglBufferData(GL_ARRAY_BUFFER, particleBuffer.capacity(), GL_STREAM_DRAW);
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
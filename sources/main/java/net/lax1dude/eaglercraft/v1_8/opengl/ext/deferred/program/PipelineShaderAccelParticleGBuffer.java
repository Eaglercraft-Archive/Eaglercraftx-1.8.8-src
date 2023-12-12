package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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
public class PipelineShaderAccelParticleGBuffer extends ShaderProgram<PipelineShaderAccelParticleGBuffer.Uniforms> {

	public static PipelineShaderAccelParticleGBuffer compile() {
		IShaderGL accelParticleVSH = ShaderCompiler.compileShader("accel_particle_gbuffer", GL_VERTEX_SHADER,
				ShaderSource.accel_particle_vsh, "COMPILE_GBUFFER_VSH");
		IShaderGL accelParticleFSH = null;
		try {
			accelParticleFSH = ShaderCompiler.compileShader("accel_particle_gbuffer", GL_FRAGMENT_SHADER,
					ShaderSource.accel_particle_gbuffer_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("accel_particle_gbuffer", accelParticleVSH, accelParticleFSH);
			return new PipelineShaderAccelParticleGBuffer(prog);
		}finally {
			if(accelParticleVSH != null) {
				accelParticleVSH.free();
			}
			if(accelParticleFSH != null) {
				accelParticleFSH.free();
			}
		}
	}

	private PipelineShaderAccelParticleGBuffer(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_matrixTransform = null;
		public IUniformGL u_texCoordSize2f_particleSize1f = null;
		public IUniformGL u_transformParam_1_2_3_4_f = null;
		public IUniformGL u_transformParam_5_f = null;
		public IUniformGL u_textureYScale2f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_matrixTransform = _wglGetUniformLocation(prog, "u_matrixTransform");
			u_texCoordSize2f_particleSize1f = _wglGetUniformLocation(prog, "u_texCoordSize2f_particleSize1f");
			u_transformParam_1_2_3_4_f = _wglGetUniformLocation(prog, "u_transformParam_1_2_3_4_f");
			u_transformParam_5_f = _wglGetUniformLocation(prog, "u_transformParam_5_f");
			u_textureYScale2f = _wglGetUniformLocation(prog, "u_textureYScale2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_diffuseTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_samplerNormalMaterial"), 2);
		}

	}

}

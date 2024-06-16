package net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.IProgramUniforms;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderCompiler;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderProgram;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderSource;

/**
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
public class DynamicLightsAccelParticleShader extends ShaderProgram<DynamicLightsAccelParticleShader.Uniforms> {

	public static DynamicLightsAccelParticleShader compile() {
		IShaderGL accelParticleVSH = ShaderCompiler.compileShader("accel_particle_dynamiclights", GL_VERTEX_SHADER,
				ShaderSource.accel_particle_dynamiclights_vsh);
		IShaderGL accelParticleFSH = null;
		try {
			accelParticleFSH = ShaderCompiler.compileShader("accel_particle_dynamiclights", GL_FRAGMENT_SHADER,
					ShaderSource.accel_particle_dynamiclights_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("accel_particle_dynamiclights", accelParticleVSH, accelParticleFSH);
			return new DynamicLightsAccelParticleShader(prog);
		}finally {
			if(accelParticleVSH != null) {
				accelParticleVSH.free();
			}
			if(accelParticleFSH != null) {
				accelParticleFSH.free();
			}
		}
	}

	private DynamicLightsAccelParticleShader(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_color4f = null;
		public IUniformGL u_modelViewMatrix4f = null;
		public IUniformGL u_projectionMatrix4f = null;
		public IUniformGL u_inverseViewMatrix4f = null;
		public IUniformGL u_texCoordSize2f_particleSize1f = null;
		public IUniformGL u_transformParam_1_2_5_f = null;
		public IUniformGL u_transformParam_3_4_f = null;

		public int u_chunkLightingDataBlockBinding = -1;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_modelViewMatrix4f = _wglGetUniformLocation(prog, "u_modelViewMatrix4f");
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_texCoordSize2f_particleSize1f = _wglGetUniformLocation(prog, "u_texCoordSize2f_particleSize1f");
			u_transformParam_1_2_5_f = _wglGetUniformLocation(prog, "u_transformParam_1_2_5_f");
			u_transformParam_3_4_f = _wglGetUniformLocation(prog, "u_transformParam_3_4_f");
			u_color4f = _wglGetUniformLocation(prog, "u_color4f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lightmapTexture"), 1);
			int blockIndex = _wglGetUniformBlockIndex(prog, "u_chunkLightingData");
			if(blockIndex != -1) {
				_wglUniformBlockBinding(prog, blockIndex, 0);
				u_chunkLightingDataBlockBinding = 0;
			}else {
				u_chunkLightingDataBlockBinding = -1;
			}
		}

	}

}

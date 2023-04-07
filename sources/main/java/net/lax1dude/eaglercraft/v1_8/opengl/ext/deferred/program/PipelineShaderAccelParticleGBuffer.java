package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

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

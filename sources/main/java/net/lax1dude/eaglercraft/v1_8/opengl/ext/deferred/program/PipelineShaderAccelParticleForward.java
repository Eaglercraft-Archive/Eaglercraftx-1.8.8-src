package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

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
public class PipelineShaderAccelParticleForward extends ShaderProgram<PipelineShaderAccelParticleForward.Uniforms> {

	public static PipelineShaderAccelParticleForward compile(boolean dynamicLights, int sunShadows) {
		IShaderGL accelParticleVSH = ShaderCompiler.compileShader("accel_particle_forward", GL_VERTEX_SHADER,
				ShaderSource.accel_particle_vsh, "COMPILE_FORWARD_VSH");
		IShaderGL accelParticleFSH = null;
		try {
			List<String> lst = new ArrayList(2);
			if(dynamicLights) {
				lst.add("COMPILE_DYNAMIC_LIGHTS");
			}
			if(sunShadows > 0) {
				int lods = sunShadows - 1;
				if(lods > 2) {
					lods = 2;
				}
				lst.add("COMPILE_SUN_SHADOW_LOD" + lods);
			}
			accelParticleFSH = ShaderCompiler.compileShader("accel_particle_forward", GL_FRAGMENT_SHADER,
					ShaderSource.accel_particle_forward_fsh, lst);
			IProgramGL prog = ShaderCompiler.linkProgram("accel_particle_forward", accelParticleVSH, accelParticleFSH);
			return new PipelineShaderAccelParticleForward(prog);
		}finally {
			if(accelParticleVSH != null) {
				accelParticleVSH.free();
			}
			if(accelParticleFSH != null) {
				accelParticleFSH.free();
			}
		}
	}

	private PipelineShaderAccelParticleForward(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_modelViewMatrix4f = null;
		public IUniformGL u_projectionMatrix4f = null;
		public IUniformGL u_inverseViewMatrix4f = null;
		public IUniformGL u_texCoordSize2f_particleSize1f = null;
		public IUniformGL u_transformParam_1_2_3_4_f = null;
		public IUniformGL u_transformParam_5_f = null;
		public IUniformGL u_textureYScale2f = null;

		public int u_chunkLightingDataBlockBinding = -1;
		public int u_worldLightingDataBlockBinding = -1;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_modelViewMatrix4f = _wglGetUniformLocation(prog, "u_modelViewMatrix4f");
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_texCoordSize2f_particleSize1f = _wglGetUniformLocation(prog, "u_texCoordSize2f_particleSize1f");
			u_transformParam_1_2_3_4_f = _wglGetUniformLocation(prog, "u_transformParam_1_2_3_4_f");
			u_transformParam_5_f = _wglGetUniformLocation(prog, "u_transformParam_5_f");
			u_textureYScale2f = _wglGetUniformLocation(prog, "u_textureYScale2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_diffuseTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_samplerNormalMaterial"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_metalsLUT"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowDepthTexture"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_irradianceMap"), 10);
			int blockIndex = _wglGetUniformBlockIndex(prog, "u_worldLightingData");
			if(blockIndex != -1) {
				_wglUniformBlockBinding(prog, blockIndex, 0);
				u_worldLightingDataBlockBinding = 0;
			}else {
				u_worldLightingDataBlockBinding = -1;
			}
			blockIndex = _wglGetUniformBlockIndex(prog, "u_chunkLightingData");
			if(blockIndex != -1) {
				_wglUniformBlockBinding(prog, blockIndex, 1);
				u_chunkLightingDataBlockBinding = 1;
			}else {
				u_chunkLightingDataBlockBinding = -1;
			}
		}

	}

}

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
public class PipelineShaderCloudsSample extends ShaderProgram<PipelineShaderCloudsSample.Uniforms> {

	public static PipelineShaderCloudsSample compile() {
		IShaderGL cloudsSample = ShaderCompiler.compileShader("clouds_sample", GL_FRAGMENT_SHADER,
				ShaderSource.clouds_sample_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("clouds_sample", SharedPipelineShaders.deferred_local, cloudsSample);
			return new PipelineShaderCloudsSample(prog);
		}finally {
			if(cloudsSample != null) {
				cloudsSample.free();
			}
		}
	}

	private PipelineShaderCloudsSample(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_rainStrength1f = null;
		public IUniformGL u_densityModifier4f = null;
		public IUniformGL u_sampleStep1f = null;
		public IUniformGL u_cloudTimer1f = null;
		public IUniformGL u_cloudOffset3f = null;
		public IUniformGL u_sunDirection3f = null;
		public IUniformGL u_sunColor3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_rainStrength1f = _wglGetUniformLocation(prog, "u_rainStrength1f");
			u_densityModifier4f = _wglGetUniformLocation(prog, "u_densityModifier4f");
			u_sampleStep1f = _wglGetUniformLocation(prog, "u_sampleStep1f");
			u_cloudTimer1f = _wglGetUniformLocation(prog, "u_cloudTimer1f");
			u_cloudOffset3f = _wglGetUniformLocation(prog, "u_cloudOffset3f");
			u_sunDirection3f = _wglGetUniformLocation(prog, "u_sunDirection3f");
			u_sunColor3f = _wglGetUniformLocation(prog, "u_sunColor3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_noiseTexture3D"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_skyIrradianceMap"), 1);
		}

	}

}

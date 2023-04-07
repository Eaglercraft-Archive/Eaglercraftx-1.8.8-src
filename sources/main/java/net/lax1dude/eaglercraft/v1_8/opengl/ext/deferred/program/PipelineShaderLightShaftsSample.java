package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Arrays;

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
public class PipelineShaderLightShaftsSample extends ShaderProgram<PipelineShaderLightShaftsSample.Uniforms> {

	public static PipelineShaderLightShaftsSample compile(int shadowsSun) {
		if(shadowsSun == 0) {
			throw new IllegalStateException("Enable shadows to compile this shader");
		}
		int lods = shadowsSun - 1;
		if(lods > 2) {
			lods = 2;
		}
		IShaderGL lightShaftsSample = ShaderCompiler.compileShader("light_shafts_sample", GL_FRAGMENT_SHADER,
				ShaderSource.light_shafts_sample_fsh, Arrays.asList("COMPILE_SUN_SHADOW_LOD" + lods));
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("light_shafts_sample", SharedPipelineShaders.deferred_local, lightShaftsSample);
			return new PipelineShaderLightShaftsSample(prog);
		}finally {
			if(lightShaftsSample != null) {
				lightShaftsSample.free();
			}
		}
	}

	private PipelineShaderLightShaftsSample(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inverseViewProjMatrix4f = null;
		public IUniformGL u_sampleStep1f = null;
		public IUniformGL u_eyePosition3f = null;
		public IUniformGL u_ditherScale2f = null;
		public IUniformGL u_sunShadowMatrixLOD04f = null;
		public IUniformGL u_sunShadowMatrixLOD14f = null;
		public IUniformGL u_sunShadowMatrixLOD24f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_sampleStep1f = _wglGetUniformLocation(prog, "u_sampleStep1f");
			u_eyePosition3f = _wglGetUniformLocation(prog, "u_eyePosition3f");
			u_ditherScale2f = _wglGetUniformLocation(prog, "u_ditherScale2f");
			u_sunShadowMatrixLOD04f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD04f");
			u_sunShadowMatrixLOD14f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD14f");
			u_sunShadowMatrixLOD24f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD24f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowDepthTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ditherTexture"), 2);
		}

	}

}

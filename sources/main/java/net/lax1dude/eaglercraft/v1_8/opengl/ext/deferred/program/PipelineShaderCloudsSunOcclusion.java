package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

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
public class PipelineShaderCloudsSunOcclusion extends ShaderProgram<PipelineShaderCloudsSunOcclusion.Uniforms> {

	public static PipelineShaderCloudsSunOcclusion compile() {
		IShaderGL cloudsOcclusion = ShaderCompiler.compileShader("clouds_sun_occlusion", GL_FRAGMENT_SHADER,
				ShaderSource.clouds_sun_occlusion_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("clouds_sun_occlusion", SharedPipelineShaders.deferred_local, cloudsOcclusion);
			return new PipelineShaderCloudsSunOcclusion(prog);
		}finally {
			if(cloudsOcclusion != null) {
				cloudsOcclusion.free();
			}
		}
	}
	private PipelineShaderCloudsSunOcclusion(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleMatrix4x3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sampleMatrix4x3f = _wglGetUniformLocation(prog, "u_sampleMatrix4x3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_cloudsTexture"), 0);
		}

	}

}

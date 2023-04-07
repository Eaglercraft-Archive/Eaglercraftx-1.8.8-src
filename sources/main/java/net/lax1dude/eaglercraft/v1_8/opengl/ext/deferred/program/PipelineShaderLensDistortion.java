package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;

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
public class PipelineShaderLensDistortion extends ShaderProgram<PipelineShaderLensDistortion.Uniforms> {

	public static PipelineShaderLensDistortion compile() throws ShaderException {
		IShaderGL lensDistort = ShaderCompiler.compileShader("post_lens_distort", GL_FRAGMENT_SHADER,
					ShaderSource.post_lens_distort_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_lens_distort", SharedPipelineShaders.deferred_local, lensDistort);
			return new PipelineShaderLensDistortion(prog);
		}finally {
			if(lensDistort != null) {
				lensDistort.free();
			}
		}
	}

	private PipelineShaderLensDistortion(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
		}

	}

}

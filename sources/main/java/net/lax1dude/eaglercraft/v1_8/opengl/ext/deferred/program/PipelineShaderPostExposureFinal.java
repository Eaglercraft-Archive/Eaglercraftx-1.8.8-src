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
public class PipelineShaderPostExposureFinal extends ShaderProgram<PipelineShaderPostExposureFinal.Uniforms> {

	public static PipelineShaderPostExposureFinal compile() throws ShaderException {
		IShaderGL postExposureFinal = ShaderCompiler.compileShader("post_exposure_final", GL_FRAGMENT_SHADER,
					ShaderSource.post_exposure_final_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_exposure_final", SharedPipelineShaders.deferred_local, postExposureFinal);
			return new PipelineShaderPostExposureFinal(prog);
		}finally {
			if(postExposureFinal != null) {
				postExposureFinal.free();
			}
		}
	}

	private PipelineShaderPostExposureFinal(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inputSize2f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_exposureValue"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunOcclusionValue"), 2);
			u_inputSize2f = _wglGetUniformLocation(prog, "u_inputSize2f");
		}

	}

}

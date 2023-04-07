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
public class PipelineShaderPostExposureAvg extends ShaderProgram<PipelineShaderPostExposureAvg.Uniforms> {

	public static PipelineShaderPostExposureAvg compile(boolean luma) throws ShaderException {
		List<String> compileFlags = new ArrayList(1);
		if(luma) {
			compileFlags.add("CALCULATE_LUMINANCE");
		}
		IShaderGL postExposureAvg = ShaderCompiler.compileShader("post_exposure_avg", GL_FRAGMENT_SHADER,
					ShaderSource.post_exposure_avg_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_exposure_avg", SharedPipelineShaders.deferred_local, postExposureAvg);
			return new PipelineShaderPostExposureAvg(prog);
		}finally {
			if(postExposureAvg != null) {
				postExposureAvg.free();
			}
		}
	}

	private PipelineShaderPostExposureAvg(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleOffset4f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
			u_sampleOffset4f = _wglGetUniformLocation(prog, "u_sampleOffset4f");
		}
		
	}

}

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
public class PipelineShaderLensSunOcclusion extends ShaderProgram<PipelineShaderLensSunOcclusion.Uniforms> {

	public static PipelineShaderLensSunOcclusion compile() throws ShaderException {
		IShaderGL sunOcclusion = ShaderCompiler.compileShader("lens_sun_occlusion", GL_FRAGMENT_SHADER,
					ShaderSource.lens_sun_occlusion_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("lens_sun_occlusion", SharedPipelineShaders.deferred_local, sunOcclusion);
			return new PipelineShaderLensSunOcclusion(prog);
		}finally {
			if(sunOcclusion != null) {
				sunOcclusion.free();
			}
		}
	}

	private PipelineShaderLensSunOcclusion(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleMatrix3f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sampleMatrix3f = _wglGetUniformLocation(prog, "u_sampleMatrix3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_depthBufferTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_cloudsSunOcclusion"), 1);
		}

	}

}

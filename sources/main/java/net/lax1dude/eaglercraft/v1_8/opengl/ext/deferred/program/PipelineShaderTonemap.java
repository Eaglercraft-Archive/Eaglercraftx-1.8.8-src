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
public class PipelineShaderTonemap extends ShaderProgram<PipelineShaderTonemap.Uniforms> {

	public static PipelineShaderTonemap compile() throws ShaderException {
		IShaderGL tonemapOperator = ShaderCompiler.compileShader("post_tonemap", GL_FRAGMENT_SHADER,
					ShaderSource.post_tonemap_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_tonemap", SharedPipelineShaders.deferred_local, tonemapOperator);
			return new PipelineShaderTonemap(prog);
		}finally {
			if(tonemapOperator != null) {
				tonemapOperator.free();
			}
		}
	}

	private PipelineShaderTonemap(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_exposure3f;
		public IUniformGL u_ditherScale2f;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_exposure3f = _wglGetUniformLocation(prog, "u_exposure3f");
			u_ditherScale2f = _wglGetUniformLocation(prog, "u_ditherScale2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lightingHDRFramebufferTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_framebufferLumaAvgInput"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ditherTexture"), 2);
		}

	}

}

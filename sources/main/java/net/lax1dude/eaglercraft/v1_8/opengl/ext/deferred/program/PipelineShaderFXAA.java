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
public class PipelineShaderFXAA extends ShaderProgram<PipelineShaderFXAA.Uniforms> {

	public static PipelineShaderFXAA compile() throws ShaderException {
		IShaderGL postFXAA = ShaderCompiler.compileShader("post_fxaa", GL_FRAGMENT_SHADER,
					ShaderSource.post_fxaa_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_fxaa", SharedPipelineShaders.deferred_local, postFXAA);
			return new PipelineShaderFXAA(prog);
		}finally {
			if(postFXAA != null) {
				postFXAA.free();
			}
		}
	}

	private PipelineShaderFXAA(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_screenSize2f;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_screenTexture"), 0);
			u_screenSize2f = _wglGetUniformLocation(prog, "u_screenSize2f");
		}

	}

}

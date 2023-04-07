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
public class PipelineShaderSSAOGenerate extends ShaderProgram<PipelineShaderSSAOGenerate.Uniforms> {

	public static PipelineShaderSSAOGenerate compile() throws ShaderException {
		IShaderGL ssaoGenerate = ShaderCompiler.compileShader("ssao_generate", GL_FRAGMENT_SHADER,
					ShaderSource.ssao_generate_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("ssao_generate", SharedPipelineShaders.deferred_local, ssaoGenerate);
			return new PipelineShaderSSAOGenerate(prog);
		}finally {
			if(ssaoGenerate != null) {
				ssaoGenerate.free();
			}
		}
	}

	private PipelineShaderSSAOGenerate(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_projectionMatrix4f;
		public IUniformGL u_inverseProjectionMatrix4f;
		public IUniformGL u_randomizerDataMatrix2f;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_randomizerDataMatrix2f = _wglGetUniformLocation(prog, "u_randomizerDataMatrix2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_noiseConstantTexture"), 2);
		}

	}

}

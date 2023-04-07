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
public class PipelineShaderBloomBrightPass extends ShaderProgram<PipelineShaderBloomBrightPass.Uniforms> {

	public static PipelineShaderBloomBrightPass compile() throws ShaderException {
		IShaderGL bloomBrightPass = ShaderCompiler.compileShader("post_bloom_bright", GL_FRAGMENT_SHADER,
					ShaderSource.post_bloom_bright_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_bloom_bright", SharedPipelineShaders.deferred_local, bloomBrightPass);
			return new PipelineShaderBloomBrightPass(prog);
		}finally {
			if(bloomBrightPass != null) {
				bloomBrightPass.free();
			}
		}
	}

	private PipelineShaderBloomBrightPass(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_outputSize4f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_outputSize4f = _wglGetUniformLocation(prog, "u_outputSize4f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lightingHDRFramebufferTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_framebufferLumaAvgInput"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 3);
		}

	}

}

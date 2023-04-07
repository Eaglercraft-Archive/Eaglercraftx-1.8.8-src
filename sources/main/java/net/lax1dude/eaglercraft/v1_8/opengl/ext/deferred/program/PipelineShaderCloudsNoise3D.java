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
public class PipelineShaderCloudsNoise3D extends ShaderProgram<PipelineShaderCloudsNoise3D.Uniforms> {

	public static PipelineShaderCloudsNoise3D compile() {
		IShaderGL cloudsNoise3d = ShaderCompiler.compileShader("clouds_noise3d", GL_FRAGMENT_SHADER,
				ShaderSource.clouds_noise3d_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("clouds_noise3d", SharedPipelineShaders.deferred_local, cloudsNoise3d);
			return new PipelineShaderCloudsNoise3D(prog);
		}finally {
			if(cloudsNoise3d != null) {
				cloudsNoise3d.free();
			}
		}
	}

	private PipelineShaderCloudsNoise3D(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_textureSlice1f = null;
		public IUniformGL u_textureSize2f = null;
		public IUniformGL u_sampleOffsetMatrix4f = null;
		public IUniformGL u_cloudMovement3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_textureSlice1f = _wglGetUniformLocation(prog, "u_textureSlice1f");
			u_textureSize2f = _wglGetUniformLocation(prog, "u_textureSize2f");
			u_sampleOffsetMatrix4f = _wglGetUniformLocation(prog, "u_sampleOffsetMatrix4f");
			u_cloudMovement3f = _wglGetUniformLocation(prog, "u_cloudMovement3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_noiseTexture"), 0);
		}

	}

}

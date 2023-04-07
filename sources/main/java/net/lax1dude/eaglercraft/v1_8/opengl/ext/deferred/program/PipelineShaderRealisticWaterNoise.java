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
public class PipelineShaderRealisticWaterNoise extends ShaderProgram<PipelineShaderRealisticWaterNoise.Uniforms> {

	public static PipelineShaderRealisticWaterNoise compile() throws ShaderException {
		IShaderGL realisticWaterNoise = ShaderCompiler.compileShader("realistic_water_noise", GL_FRAGMENT_SHADER,
					ShaderSource.realistic_water_noise_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("realistic_water_noise", SharedPipelineShaders.deferred_local, realisticWaterNoise);
			return new PipelineShaderRealisticWaterNoise(prog);
		}finally {
			if(realisticWaterNoise != null) {
				realisticWaterNoise.free();
			}
		}
	}

	private PipelineShaderRealisticWaterNoise(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_waveTimer4f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_noiseTexture"), 0);
			u_waveTimer4f = _wglGetUniformLocation(prog, "u_waveTimer4f");
		}

	}

}

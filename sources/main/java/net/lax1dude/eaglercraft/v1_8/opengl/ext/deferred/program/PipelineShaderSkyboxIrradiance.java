package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Arrays;

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
public class PipelineShaderSkyboxIrradiance extends ShaderProgram<PipelineShaderSkyboxIrradiance.Uniforms> {

	public static PipelineShaderSkyboxIrradiance compile(int phase) throws ShaderException {
		IShaderGL skyboxIrradiance = ShaderCompiler.compileShader("skybox_irradiance", GL_FRAGMENT_SHADER,
					ShaderSource.skybox_irradiance_fsh, Arrays.asList("PHASE_" + phase));
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("skybox_irradiance", SharedPipelineShaders.deferred_local, skyboxIrradiance);
			return new PipelineShaderSkyboxIrradiance(prog);
		}finally {
			if(skyboxIrradiance != null) {
				skyboxIrradiance.free();
			}
		}
	}

	private PipelineShaderSkyboxIrradiance(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_paraboloidSkyboxTexture"), 0);
		}

	}

}

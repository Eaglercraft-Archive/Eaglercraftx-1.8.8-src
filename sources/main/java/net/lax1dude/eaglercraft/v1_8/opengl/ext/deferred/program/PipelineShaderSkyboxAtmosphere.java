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
public class PipelineShaderSkyboxAtmosphere extends ShaderProgram<PipelineShaderSkyboxAtmosphere.Uniforms> {

	public static PipelineShaderSkyboxAtmosphere compile() throws ShaderException {
		IShaderGL skyboxAtmosphere = ShaderCompiler.compileShader("skybox_atmosphere", GL_FRAGMENT_SHADER,
					ShaderSource.skybox_atmosphere_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("skybox_atmosphere", SharedPipelineShaders.deferred_local, skyboxAtmosphere);
			return new PipelineShaderSkyboxAtmosphere(prog);
		}finally {
			if(skyboxAtmosphere != null) {
				skyboxAtmosphere.free();
			}
		}
	}

	private PipelineShaderSkyboxAtmosphere(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sunDirectionIntensity4f = null;
		public IUniformGL u_altitude1f = null;
		public IUniformGL u_blendColor4f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sunDirectionIntensity4f = _wglGetUniformLocation(prog, "u_sunDirectionIntensity4f");
			u_altitude1f = _wglGetUniformLocation(prog, "u_altitude1f");
			u_blendColor4f = _wglGetUniformLocation(prog, "u_blendColor4f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_skyNormals"), 0);
		}

	}
}

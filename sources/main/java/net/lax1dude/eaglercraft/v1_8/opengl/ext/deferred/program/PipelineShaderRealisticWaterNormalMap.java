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
public class PipelineShaderRealisticWaterNormalMap extends ShaderProgram<PipelineShaderRealisticWaterNormalMap.Uniforms> {

	public static PipelineShaderRealisticWaterNormalMap compile() throws ShaderException {
		IShaderGL realisticWaterNormals = ShaderCompiler.compileShader("realistic_water_normals", GL_FRAGMENT_SHADER,
					ShaderSource.realistic_water_normals_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("realistic_water_normals", SharedPipelineShaders.deferred_local, realisticWaterNormals);
			return new PipelineShaderRealisticWaterNormalMap(prog);
		}finally {
			if(realisticWaterNormals != null) {
				realisticWaterNormals.free();
			}
		}
	}

	private PipelineShaderRealisticWaterNormalMap(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleOffset2f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_displacementTexture"), 0);
			u_sampleOffset2f = _wglGetUniformLocation(prog, "u_sampleOffset2f");
		}

	}

}

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
public class PipelineShaderCloudsShapes extends ShaderProgram<PipelineShaderCloudsShapes.Uniforms> {

	public static PipelineShaderCloudsShapes compile() {
		IShaderGL cloudsShapesVSH = ShaderCompiler.compileShader("clouds_shapes", GL_VERTEX_SHADER,
				ShaderSource.clouds_shapes_vsh);
		IShaderGL cloudsShapesFSH = null;
		try {
			cloudsShapesFSH = ShaderCompiler.compileShader("clouds_shapes", GL_FRAGMENT_SHADER,
					ShaderSource.clouds_shapes_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("clouds_shapes", cloudsShapesVSH, cloudsShapesFSH);
			return new PipelineShaderCloudsShapes(prog);
		}finally {
			if(cloudsShapesVSH != null) {
				cloudsShapesVSH.free();
			}
			if(cloudsShapesFSH != null) {
				cloudsShapesFSH.free();
			}
		}
	}

	private PipelineShaderCloudsShapes(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_textureLevel1f = null;
		public IUniformGL u_textureLod1f = null;
		public IUniformGL u_transformMatrix3x2f = null;
		public IUniformGL u_sampleWeights2f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_textureLevel1f = _wglGetUniformLocation(prog, "u_textureLevel1f");
			u_textureLod1f = _wglGetUniformLocation(prog, "u_textureLod1f");
			u_transformMatrix3x2f = _wglGetUniformLocation(prog, "u_transformMatrix3x2f");
			u_sampleWeights2f = _wglGetUniformLocation(prog, "u_sampleWeights2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
		}

	}

}

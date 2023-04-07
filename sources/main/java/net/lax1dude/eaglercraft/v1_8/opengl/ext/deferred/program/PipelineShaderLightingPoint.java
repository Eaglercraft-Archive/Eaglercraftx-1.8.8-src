package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

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
public class PipelineShaderLightingPoint extends ShaderProgram<PipelineShaderLightingPoint.Uniforms> {

	public static PipelineShaderLightingPoint compile(boolean shadows)
			throws ShaderException {
		List<String> compileFlags = new ArrayList(2);
		if(shadows) {
			compileFlags.add("COMPILE_PARABOLOID_SHADOW");
		}
		IShaderGL lightingPoint = ShaderCompiler.compileShader("lighting_point", GL_FRAGMENT_SHADER,
				ShaderSource.lighting_point_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("lighting_point", SharedPipelineShaders.lighting_mesh, lightingPoint);
			return new PipelineShaderLightingPoint(prog, shadows);
		}finally {
			if(lightingPoint != null) {
				lightingPoint.free();
			}
		}
	}

	private PipelineShaderLightingPoint(IProgramGL program, boolean shadows) {
		super(program, new Uniforms(shadows));
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_viewportSize2f = null;
		public IUniformGL u_modelViewProjMatrix4f = null;
		public IUniformGL u_inverseProjectionMatrix4f = null;
		public IUniformGL u_inverseViewMatrix4f = null;
		public IUniformGL u_lightPosition3f = null;
		public IUniformGL u_lightColor3f = null;

		public final boolean shadows;

		private Uniforms(boolean shadows) {
			this.shadows = shadows;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferColorTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_metalsLUT"), 5);
			u_viewportSize2f = _wglGetUniformLocation(prog, "u_viewportSize2f");
			u_modelViewProjMatrix4f = _wglGetUniformLocation(prog, "u_modelViewProjMatrix4f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_lightPosition3f = _wglGetUniformLocation(prog, "u_lightPosition3f");
			u_lightColor3f = _wglGetUniformLocation(prog, "u_lightColor3f");
		}

	}

}

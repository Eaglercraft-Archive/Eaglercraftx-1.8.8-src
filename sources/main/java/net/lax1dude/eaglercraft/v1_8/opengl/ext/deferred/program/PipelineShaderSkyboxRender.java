package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

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
public class PipelineShaderSkyboxRender extends ShaderProgram<PipelineShaderSkyboxRender.Uniforms> {

	public static PipelineShaderSkyboxRender compile(boolean paraboloid, boolean clouds) throws ShaderException {
		List<String> compileFlags = new ArrayList();
		if(paraboloid) {
			compileFlags.add("COMPILE_PARABOLOID_SKY");
		}
		if(clouds) {
			compileFlags.add("COMPILE_CLOUDS");
		}
		IShaderGL vertexShader = ShaderCompiler.compileShader("skybox_render", GL_VERTEX_SHADER,
					ShaderSource.skybox_render_vsh, compileFlags);
		IShaderGL fragmentShader = null;
		try {
			fragmentShader = ShaderCompiler.compileShader("skybox_render", GL_FRAGMENT_SHADER,
					ShaderSource.skybox_render_fsh, compileFlags);
			IProgramGL prog = ShaderCompiler.linkProgram("skybox_render", vertexShader, fragmentShader);
			return new PipelineShaderSkyboxRender(prog, paraboloid);
		}finally {
			if(vertexShader != null) {
				vertexShader.free();
			}
			if(fragmentShader != null) {
				fragmentShader.free();
			}
		}
	}

	private PipelineShaderSkyboxRender(IProgramGL program, boolean paraboloid) {
		super(program, new Uniforms(paraboloid));
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_viewMatrix4f = null;
		public IUniformGL u_projMatrix4f = null;
		public IUniformGL u_sunDirection3f = null;
		public IUniformGL u_sunColor3f = null;
		public IUniformGL u_lightningColor4f = null;
		public IUniformGL u_farPlane1f = null;

		public final boolean paraboloid;

		private Uniforms(boolean paraboloid) {
			this.paraboloid = paraboloid;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_viewMatrix4f = _wglGetUniformLocation(prog, "u_viewMatrix4f");
			u_projMatrix4f = _wglGetUniformLocation(prog, "u_projMatrix4f");
			u_sunDirection3f = _wglGetUniformLocation(prog, "u_sunDirection3f");
			u_sunColor3f = _wglGetUniformLocation(prog, "u_sunColor3f");
			u_lightningColor4f = _wglGetUniformLocation(prog, "u_lightningColor4f");
			u_farPlane1f = _wglGetUniformLocation(prog, "u_farPlane1f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_renderedAtmosphere"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_cloudsTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunOcclusion"), 2);
		}
		
	}

}

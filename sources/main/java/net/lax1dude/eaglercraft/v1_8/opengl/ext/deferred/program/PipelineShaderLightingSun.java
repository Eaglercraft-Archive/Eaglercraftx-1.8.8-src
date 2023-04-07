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
public class PipelineShaderLightingSun extends ShaderProgram<PipelineShaderLightingSun.Uniforms> {

	public static PipelineShaderLightingSun compile(int shadowsSun, boolean coloredShadows) throws ShaderException {
		IShaderGL sunShader = null;
		List<String> compileFlags = new ArrayList(1);
		if(shadowsSun > 0) {
			compileFlags.add("COMPILE_SUN_SHADOW");
		}
		if(coloredShadows) {
			compileFlags.add("COMPILE_COLORED_SHADOW");
		}
		sunShader = ShaderCompiler.compileShader("lighting_sun", GL_FRAGMENT_SHADER,
				ShaderSource.lighting_sun_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("lighting_sun", SharedPipelineShaders.deferred_local, sunShader);
			return new PipelineShaderLightingSun(prog, shadowsSun);
		}finally {
			if(sunShader != null) {
				sunShader.free();
			}
		}
	}

	private PipelineShaderLightingSun(IProgramGL program, int shadowsSun) {
		super(program, new Uniforms(shadowsSun));
	}

	public static class Uniforms implements IProgramUniforms {

		public final int shadowsSun;
		public IUniformGL u_inverseViewMatrix4f;
		public IUniformGL u_inverseProjectionMatrix4f;
		public IUniformGL u_sunDirection3f;
		public IUniformGL u_sunColor3f;

		private Uniforms(int shadowsSun) {
			this.shadowsSun = shadowsSun;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferColorTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowTexture"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_metalsLUT"), 5);
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_sunDirection3f = _wglGetUniformLocation(prog, "u_sunDirection3f");
			u_sunColor3f = _wglGetUniformLocation(prog, "u_sunColor3f");
		}

	}

}

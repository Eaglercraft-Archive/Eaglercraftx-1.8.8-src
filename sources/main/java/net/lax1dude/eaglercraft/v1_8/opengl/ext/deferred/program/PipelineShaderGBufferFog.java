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
public class PipelineShaderGBufferFog extends ShaderProgram<PipelineShaderGBufferFog.Uniforms> {

	public static PipelineShaderGBufferFog compile(boolean linear, boolean atmosphere, boolean lightShafts) {
		List<String> macros = new ArrayList(3);
		if(linear) {
			macros.add("COMPILE_FOG_LINEAR");
		}
		if(atmosphere) {
			macros.add("COMPILE_FOG_ATMOSPHERE");
		}
		if(lightShafts) {
			macros.add("COMPILE_FOG_LIGHT_SHAFTS");
		}
		IShaderGL deferredFog = ShaderCompiler.compileShader("deferred_fog", GL_FRAGMENT_SHADER,
				ShaderSource.deferred_fog_fsh, macros);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("deferred_fog", SharedPipelineShaders.deferred_local, deferredFog);
			return new PipelineShaderGBufferFog(prog);
		}finally {
			if(deferredFog != null) {
				deferredFog.free();
			}
		}
	}

	private PipelineShaderGBufferFog(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inverseViewProjMatrix4f = null;
		public IUniformGL u_linearFogParam2f = null;
		public IUniformGL u_expFogDensity1f = null;
		public IUniformGL u_fogColorLight4f = null;
		public IUniformGL u_fogColorDark4f = null;
		public IUniformGL u_sunColorAdd3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_linearFogParam2f = _wglGetUniformLocation(prog, "u_linearFogParam2f");
			u_expFogDensity1f = _wglGetUniformLocation(prog, "u_expFogDensity1f");
			u_fogColorLight4f = _wglGetUniformLocation(prog, "u_fogColorLight4f");
			u_fogColorDark4f = _wglGetUniformLocation(prog, "u_fogColorDark4f");
			u_sunColorAdd3f = _wglGetUniformLocation(prog, "u_sunColorAdd3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_fogDepthTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_environmentMap"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lightShaftsTexture"), 4);
		}

	}

}

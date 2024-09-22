package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class PipelineShaderGBufferFog extends ShaderProgram<PipelineShaderGBufferFog.Uniforms> {

	public static PipelineShaderGBufferFog compile(boolean linear, boolean atmosphere, boolean lightShafts) {
		List<String> macros = new ArrayList<>(3);
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

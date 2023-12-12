package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.Arrays;

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
public class PipelineShaderLightShaftsSample extends ShaderProgram<PipelineShaderLightShaftsSample.Uniforms> {

	public static PipelineShaderLightShaftsSample compile(int shadowsSun) {
		if(shadowsSun == 0) {
			throw new IllegalStateException("Enable shadows to compile this shader");
		}
		int lods = shadowsSun - 1;
		if(lods > 2) {
			lods = 2;
		}
		IShaderGL lightShaftsSample = ShaderCompiler.compileShader("light_shafts_sample", GL_FRAGMENT_SHADER,
				ShaderSource.light_shafts_sample_fsh, Arrays.asList("COMPILE_SUN_SHADOW_LOD" + lods));
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("light_shafts_sample", SharedPipelineShaders.deferred_local, lightShaftsSample);
			return new PipelineShaderLightShaftsSample(prog);
		}finally {
			if(lightShaftsSample != null) {
				lightShaftsSample.free();
			}
		}
	}

	private PipelineShaderLightShaftsSample(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inverseViewProjMatrix4f = null;
		public IUniformGL u_sampleStep1f = null;
		public IUniformGL u_eyePosition3f = null;
		public IUniformGL u_ditherScale2f = null;
		public IUniformGL u_sunShadowMatrixLOD04f = null;
		public IUniformGL u_sunShadowMatrixLOD14f = null;
		public IUniformGL u_sunShadowMatrixLOD24f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_sampleStep1f = _wglGetUniformLocation(prog, "u_sampleStep1f");
			u_eyePosition3f = _wglGetUniformLocation(prog, "u_eyePosition3f");
			u_ditherScale2f = _wglGetUniformLocation(prog, "u_ditherScale2f");
			u_sunShadowMatrixLOD04f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD04f");
			u_sunShadowMatrixLOD14f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD14f");
			u_sunShadowMatrixLOD24f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD24f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowDepthTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ditherTexture"), 2);
		}

	}

}

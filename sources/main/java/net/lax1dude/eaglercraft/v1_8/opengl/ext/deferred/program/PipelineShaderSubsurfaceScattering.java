/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL._wglGetUniformLocation;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL._wglUniform1i;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

public class PipelineShaderSubsurfaceScattering extends ShaderProgram<PipelineShaderSubsurfaceScattering.Uniforms> {

	public static PipelineShaderSubsurfaceScattering compile(int shadowsSun, float texW, float texH) throws ShaderException {
		IShaderGL shadowShader = null;
		List<String> compileFlags = new ArrayList<>(3);
		if(shadowsSun == 0) {
			throw new IllegalStateException("Enable shadows to compile this shader");
		}
		int lods = shadowsSun - 1;
		if(lods > 2) {
			lods = 2;
		}
		compileFlags.add("COMPILE_SUN_SHADOW_LOD" + lods);
		compileFlags.add("SUN_SHADOW_DEPTH_SIZE_2F_X " + texW);
		compileFlags.add("SUN_SHADOW_DEPTH_SIZE_2F_Y " + texH);
		shadowShader = ShaderCompiler.compileShader("subsurface_scattering", GL_FRAGMENT_SHADER,
				ShaderSource.subsurface_scattering_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("subsurface_scattering", SharedPipelineShaders.deferred_local, shadowShader);
			return new PipelineShaderSubsurfaceScattering(prog, shadowsSun, texW, texH);
		}finally {
			if(shadowShader != null) {
				shadowShader.free();
			}
		}
	}

	private PipelineShaderSubsurfaceScattering(IProgramGL program, int shadowsSun, float texW, float texH) {
		super(program, new Uniforms(shadowsSun, texW, texH));
	}

	public static class Uniforms implements IProgramUniforms {

		public final int shadowsSun;
		public final float texW;
		public final float texH;
		public IUniformGL u_inverseViewMatrix4f;
		public IUniformGL u_inverseViewProjMatrix4f;
		public IUniformGL u_sunShadowMatrixLOD04f;
		public IUniformGL u_sunShadowMatrixLOD14f;
		public IUniformGL u_sunShadowMatrixLOD24f;
		public IUniformGL u_sunDirection3f;

		private Uniforms(int shadowsSun, float texW, float texH) {
			this.shadowsSun = shadowsSun;
			this.texW = texW;
			this.texH = texH;
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunShadowDepthTexture"), 3);
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_sunShadowMatrixLOD04f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD04f");
			u_sunShadowMatrixLOD14f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD14f");
			u_sunShadowMatrixLOD24f = _wglGetUniformLocation(prog, "u_sunShadowMatrixLOD24f");
			u_sunDirection3f = _wglGetUniformLocation(prog, "u_sunDirection3f");
		}

	}

}

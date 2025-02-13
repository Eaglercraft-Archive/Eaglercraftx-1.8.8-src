/*
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

public class PipelineShaderRealisticWaterControl extends ShaderProgram<PipelineShaderRealisticWaterControl.Uniforms> {

	public static PipelineShaderRealisticWaterControl compile() throws ShaderException {
		IShaderGL realisticWaterControl = ShaderCompiler.compileShader("realistic_water_control", GL_FRAGMENT_SHADER,
					ShaderSource.realistic_water_control_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("realistic_water_control", SharedPipelineShaders.deferred_local, realisticWaterControl);
			return new PipelineShaderRealisticWaterControl(prog);
		}finally {
			if(realisticWaterControl != null) {
				realisticWaterControl.free();
			}
		}
	}

	public PipelineShaderRealisticWaterControl(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inverseProjectionMatrix4f = null;
		public IUniformGL u_inverseViewProjMatrix4f = null;
		public IUniformGL u_reprojectionMatrix4f = null;
		public IUniformGL u_lastInverseProjMatrix4f = null;
		public IUniformGL u_reprojectionInverseViewMatrix4f = null;
		public IUniformGL u_projectionMatrix4f = null;
		public IUniformGL u_viewToPreviousProjMatrix4f = null;
		public IUniformGL u_nearFarPlane4f = null;
		public IUniformGL u_pixelAlignment4f = null;
		public IUniformGL u_refractFogColor4f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferColorTexture4f"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_realisticWaterMaskNormal"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_realisticWaterDepthTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameReflectionInput4f"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameHitVectorInput4f"), 5);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameColorTexture"), 6);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameDepthTexture"), 7);
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_reprojectionMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionMatrix4f");
			u_lastInverseProjMatrix4f = _wglGetUniformLocation(prog, "u_lastInverseProjMatrix4f");
			u_reprojectionInverseViewMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionInverseViewMatrix4f");
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_viewToPreviousProjMatrix4f = _wglGetUniformLocation(prog, "u_viewToPreviousProjMatrix4f");
			u_nearFarPlane4f = _wglGetUniformLocation(prog, "u_nearFarPlane4f");
			u_pixelAlignment4f = _wglGetUniformLocation(prog, "u_pixelAlignment4f");
			u_refractFogColor4f = _wglGetUniformLocation(prog, "u_refractFogColor4f");
		}

	}

}
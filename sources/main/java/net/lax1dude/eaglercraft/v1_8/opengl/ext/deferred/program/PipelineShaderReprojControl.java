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

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

public class PipelineShaderReprojControl extends ShaderProgram<PipelineShaderReprojControl.Uniforms> {

	public static PipelineShaderReprojControl compile(boolean ssao, boolean ssr) throws ShaderException {
		List<String> compileFlags = new ArrayList<>(2);
		if(ssao) {
			compileFlags.add("COMPILE_REPROJECT_SSAO");
		}
		if(ssr) {
			compileFlags.add("COMPILE_REPROJECT_SSR");
		}
		IShaderGL reprojControl = ShaderCompiler.compileShader("reproj_control", GL_FRAGMENT_SHADER,
					ShaderSource.reproject_control_fsh, compileFlags);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("reproj_control", SharedPipelineShaders.deferred_local, reprojControl);
			return new PipelineShaderReprojControl(prog, ssao, ssr);
		}finally {
			if(reprojControl != null) {
				reprojControl.free();
			}
		}
	}

	private PipelineShaderReprojControl(IProgramGL prog, boolean ssao, boolean ssr) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_inverseViewProjMatrix4f = null;
		public IUniformGL u_projectionMatrix4f = null;
		public IUniformGL u_reprojectionMatrix4f = null;
		public IUniformGL u_inverseProjectionMatrix4f = null;
		public IUniformGL u_lastInverseProjMatrix4f = null;
		public IUniformGL u_reprojectionInverseViewMatrix4f = null;
		public IUniformGL u_viewToPreviousProjMatrix4f = null;
		public IUniformGL u_nearFarPlane4f = null;
		public IUniformGL u_pixelAlignment4f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ssaoSampleTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionSSAOInput4f"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionReflectionInput4f"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionHitVectorInput4f"), 5);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameColorInput4f"), 6);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionDepthTexture"), 7);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferMaterialTexture"), 8);
			u_inverseViewProjMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewProjMatrix4f");
			u_projectionMatrix4f = _wglGetUniformLocation(prog, "u_projectionMatrix4f");
			u_reprojectionMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionMatrix4f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_lastInverseProjMatrix4f = _wglGetUniformLocation(prog, "u_lastInverseProjMatrix4f");
			u_reprojectionInverseViewMatrix4f = _wglGetUniformLocation(prog, "u_reprojectionInverseViewMatrix4f");
			u_viewToPreviousProjMatrix4f = _wglGetUniformLocation(prog, "u_viewToPreviousProjMatrix4f");
			u_nearFarPlane4f = _wglGetUniformLocation(prog, "u_nearFarPlane4f");
			u_pixelAlignment4f = _wglGetUniformLocation(prog, "u_pixelAlignment4f");
		}

	}

}
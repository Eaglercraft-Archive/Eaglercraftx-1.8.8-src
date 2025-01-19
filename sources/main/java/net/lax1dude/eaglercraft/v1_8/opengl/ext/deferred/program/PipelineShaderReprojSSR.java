package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

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
public class PipelineShaderReprojSSR extends ShaderProgram<PipelineShaderReprojSSR.Uniforms> {

	public static PipelineShaderReprojSSR compile() throws ShaderException {
		IShaderGL reprojSSR = ShaderCompiler.compileShader("reproj_ssr", GL_FRAGMENT_SHADER,
					ShaderSource.reproject_ssr_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("reproj_ssr", SharedPipelineShaders.deferred_local, reprojSSR);
			return new PipelineShaderReprojSSR(prog);
		}finally {
			if(reprojSSR != null) {
				reprojSSR.free();
			}
		}
	}

	private PipelineShaderReprojSSR(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_lastProjectionMatrix4f;
		public IUniformGL u_lastInverseProjMatrix4x2f;
		public IUniformGL u_inverseProjectionMatrix4f;
		public IUniformGL u_sampleStep1f;
		public IUniformGL u_pixelAlignment4f = null;
		public IUniformGL u_sampleDelta1i;

		@Override
		public void loadUniforms(IProgramGL prog) {
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferDepthTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_gbufferNormalTexture"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionReflectionInput4f"), 2);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_reprojectionHitVectorInput4f"), 3);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameColorInput4f"), 4);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lastFrameDepthInput"), 5);
			u_lastProjectionMatrix4f = _wglGetUniformLocation(prog, "u_lastProjectionMatrix4f");
			u_lastInverseProjMatrix4x2f = _wglGetUniformLocation(prog, "u_lastInverseProjMatrix4x2f");
			u_inverseProjectionMatrix4f = _wglGetUniformLocation(prog, "u_inverseProjectionMatrix4f");
			u_sampleStep1f = _wglGetUniformLocation(prog, "u_sampleStep1f");
			u_pixelAlignment4f = _wglGetUniformLocation(prog, "u_pixelAlignment4f");
			u_sampleDelta1i = _wglGetUniformLocation(prog, "u_sampleDelta1i");
		}

	}

}

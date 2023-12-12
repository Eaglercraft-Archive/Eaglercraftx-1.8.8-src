package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

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
public class PipelineShaderTonemap extends ShaderProgram<PipelineShaderTonemap.Uniforms> {

	public static PipelineShaderTonemap compile() throws ShaderException {
		IShaderGL tonemapOperator = ShaderCompiler.compileShader("post_tonemap", GL_FRAGMENT_SHADER,
					ShaderSource.post_tonemap_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("post_tonemap", SharedPipelineShaders.deferred_local, tonemapOperator);
			return new PipelineShaderTonemap(prog);
		}finally {
			if(tonemapOperator != null) {
				tonemapOperator.free();
			}
		}
	}

	private PipelineShaderTonemap(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_exposure3f;
		public IUniformGL u_ditherScale2f;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_exposure3f = _wglGetUniformLocation(prog, "u_exposure3f");
			u_ditherScale2f = _wglGetUniformLocation(prog, "u_ditherScale2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_lightingHDRFramebufferTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_framebufferLumaAvgInput"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_ditherTexture"), 2);
		}

	}

}

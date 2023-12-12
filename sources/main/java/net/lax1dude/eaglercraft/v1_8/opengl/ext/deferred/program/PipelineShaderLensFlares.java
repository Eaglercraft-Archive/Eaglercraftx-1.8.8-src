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
public class PipelineShaderLensFlares extends ShaderProgram<PipelineShaderLensFlares.Uniforms> {

	public static PipelineShaderLensFlares compileStreaks() {
		IShaderGL vertexShader = ShaderCompiler.compileShader("post_lens_streaks", GL_VERTEX_SHADER,
					ShaderSource.post_lens_streaks_vsh);
		IShaderGL fragmentShader = null;
		try {
			fragmentShader = ShaderCompiler.compileShader("post_lens_streaks", GL_FRAGMENT_SHADER,
					ShaderSource.post_lens_streaks_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("post_lens_streaks", vertexShader, fragmentShader);
			return new PipelineShaderLensFlares(prog);
		}finally {
			if(vertexShader != null) {
				vertexShader.free();
			}
			if(fragmentShader != null) {
				fragmentShader.free();
			}
		}
	}

	public static PipelineShaderLensFlares compileGhosts() {
		IShaderGL vertexShader = ShaderCompiler.compileShader("post_lens_ghosts", GL_VERTEX_SHADER,
				ShaderSource.post_lens_ghosts_vsh);
		IShaderGL fragmentShader = null;
		try {
			fragmentShader = ShaderCompiler.compileShader("post_lens_ghosts", GL_FRAGMENT_SHADER,
					ShaderSource.post_lens_ghosts_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("post_lens_ghosts", vertexShader, fragmentShader);
			return new PipelineShaderLensFlares(prog);
		}finally {
			if(vertexShader != null) {
				vertexShader.free();
			}
			if(fragmentShader != null) {
				fragmentShader.free();
			}
		}
	}

	private PipelineShaderLensFlares(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sunFlareMatrix3f = null;
		public IUniformGL u_flareColor3f = null;
		public IUniformGL u_sunPosition2f = null;
		public IUniformGL u_aspectRatio1f = null;
		public IUniformGL u_baseScale1f = null;

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sunFlareMatrix3f = _wglGetUniformLocation(prog, "u_sunFlareMatrix3f");
			u_flareColor3f = _wglGetUniformLocation(prog, "u_flareColor3f");
			u_sunPosition2f = _wglGetUniformLocation(prog, "u_sunPosition2f");
			u_aspectRatio1f = _wglGetUniformLocation(prog, "u_aspectRatio1f");
			u_baseScale1f = _wglGetUniformLocation(prog, "u_baseScale1f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_flareTexture"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_exposureValue"), 1);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_sunOcclusionValue"), 2);
		}

	}

}

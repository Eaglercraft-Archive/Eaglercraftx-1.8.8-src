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
public class PipelineShaderMoonRender extends ShaderProgram<PipelineShaderMoonRender.Uniforms> {

	public static PipelineShaderMoonRender compile() {
		IShaderGL moonRenderVSH = ShaderCompiler.compileShader("moon_render", GL_VERTEX_SHADER,
				ShaderSource.moon_render_vsh);
		IShaderGL moonRenderFSH = null;
		try {
			moonRenderFSH = ShaderCompiler.compileShader("moon_render", GL_FRAGMENT_SHADER,
					ShaderSource.moon_render_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("moon_render", moonRenderVSH, moonRenderFSH);
			return new PipelineShaderMoonRender(prog);
		}finally {
			if(moonRenderVSH != null) {
				moonRenderVSH.free();
			}
			if(moonRenderFSH != null) {
				moonRenderFSH.free();
			}
		}
	}

	private PipelineShaderMoonRender(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_modelMatrix4f = null;
		public IUniformGL u_viewMatrix4f = null;
		public IUniformGL u_projMatrix4f = null;
		public IUniformGL u_moonColor3f = null;
		public IUniformGL u_lightDir3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_modelMatrix4f = _wglGetUniformLocation(prog, "u_modelMatrix4f");
			u_viewMatrix4f = _wglGetUniformLocation(prog, "u_viewMatrix4f");
			u_projMatrix4f = _wglGetUniformLocation(prog, "u_projMatrix4f");
			u_moonColor3f = _wglGetUniformLocation(prog, "u_moonColor3f");
			u_lightDir3f = _wglGetUniformLocation(prog, "u_lightDir3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_moonTextures"), 0);
			_wglUniform1i(_wglGetUniformLocation(prog, "u_cloudsTexture"), 1);
		}

	}

}

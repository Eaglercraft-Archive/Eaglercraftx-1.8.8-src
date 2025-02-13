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

public class PipelineShaderCloudsShapes extends ShaderProgram<PipelineShaderCloudsShapes.Uniforms> {

	public static PipelineShaderCloudsShapes compile() {
		IShaderGL cloudsShapesVSH = ShaderCompiler.compileShader("clouds_shapes", GL_VERTEX_SHADER,
				ShaderSource.clouds_shapes_vsh);
		IShaderGL cloudsShapesFSH = null;
		try {
			cloudsShapesFSH = ShaderCompiler.compileShader("clouds_shapes", GL_FRAGMENT_SHADER,
					ShaderSource.clouds_shapes_fsh);
			IProgramGL prog = ShaderCompiler.linkProgram("clouds_shapes", cloudsShapesVSH, cloudsShapesFSH);
			return new PipelineShaderCloudsShapes(prog);
		}finally {
			if(cloudsShapesVSH != null) {
				cloudsShapesVSH.free();
			}
			if(cloudsShapesFSH != null) {
				cloudsShapesFSH.free();
			}
		}
	}

	private PipelineShaderCloudsShapes(IProgramGL prog) {
		super(prog, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_textureLevel1f = null;
		public IUniformGL u_textureLod1f = null;
		public IUniformGL u_transformMatrix3x2f = null;
		public IUniformGL u_sampleWeights2f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_textureLevel1f = _wglGetUniformLocation(prog, "u_textureLevel1f");
			u_textureLod1f = _wglGetUniformLocation(prog, "u_textureLod1f");
			u_transformMatrix3x2f = _wglGetUniformLocation(prog, "u_transformMatrix3x2f");
			u_sampleWeights2f = _wglGetUniformLocation(prog, "u_sampleWeights2f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_inputTexture"), 0);
		}

	}

}
package net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.program;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.IProgramUniforms;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderProgram;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class DynamicLightsExtPipelineShader extends ShaderProgram<DynamicLightsExtPipelineShader.Uniforms> {

	public final int coreState;

	public DynamicLightsExtPipelineShader(IProgramGL program, int coreState) {
		super(program, new Uniforms());
		this.coreState = coreState;
	}

	public static class Uniforms implements IProgramUniforms {

		public int u_chunkLightingDataBlockBinding = -1;

		public int inverseViewMatrixSerial = -1;
		public IUniformGL u_inverseViewMatrix4f = null;

		Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_inverseViewMatrix4f = _wglGetUniformLocation(prog, "u_inverseViewMatrix4f");
			int blockIndex = _wglGetUniformBlockIndex(prog, "u_chunkLightingData");
			if(blockIndex != -1) {
				_wglUniformBlockBinding(prog, blockIndex, 0);
				u_chunkLightingDataBlockBinding = 0;
			}else {
				u_chunkLightingDataBlockBinding = -1;
			}
		}

	}

}

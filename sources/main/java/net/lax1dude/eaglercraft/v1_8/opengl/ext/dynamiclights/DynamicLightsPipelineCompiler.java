package net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionState;
import net.lax1dude.eaglercraft.v1_8.opengl.IExtPipelineCompiler;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderSource;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.program.DynamicLightsExtPipelineShader;
import net.minecraft.client.renderer.GLAllocation;

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
public class DynamicLightsPipelineCompiler implements IExtPipelineCompiler {

	static FloatBuffer matrixCopyBuffer = null;

	private static class PipelineInstance {

		private final int coreBits;
		private final int extBits;

		private DynamicLightsExtPipelineShader shader;

		public PipelineInstance(int coreBits, int extBits) {
			this.coreBits = coreBits;
			this.extBits = extBits;
		}

	}

	@Override
	public String[] getShaderSource(int stateCoreBits, int stateExtBits, Object[] userPointer) {
		if(matrixCopyBuffer == null) {
			matrixCopyBuffer = GLAllocation.createDirectFloatBuffer(16);
		}
		userPointer[0] = new PipelineInstance(stateCoreBits, stateExtBits);
		return new String[] {
			ShaderSource.getSourceFor(ShaderSource.core_dynamiclights_vsh),
			ShaderSource.getSourceFor(ShaderSource.core_dynamiclights_fsh)
		};
	}

	@Override
	public int getExtensionStatesCount() {
		return 0;
	}

	@Override
	public int getCurrentExtensionStateBits(int stateCoreBits) {
		return 0;
	}

	@Override
	public int getCoreStateMask(int stateExtBits) {
		return 0xFFFFFFFF;
	}

	@Override
	public void initializeNewShader(IProgramGL compiledProg, int stateCoreBits, int stateExtBits,
			Object[] userPointer) {
		DynamicLightsExtPipelineShader newShader = new DynamicLightsExtPipelineShader(compiledProg, stateCoreBits);
		((PipelineInstance)userPointer[0]).shader = newShader;
		newShader.loadUniforms();
	}

	@Override
	public void updatePipeline(IProgramGL compiledProg, int stateCoreBits, int stateExtBits, Object[] userPointer) {
		if((stateCoreBits & FixedFunctionState.STATE_ENABLE_LIGHTMAP) != 0) {
			DynamicLightsExtPipelineShader.Uniforms uniforms = ((PipelineInstance)userPointer[0]).shader.uniforms;
			if(uniforms.u_inverseViewMatrix4f != null) {
				int serial = DynamicLightsStateManager.inverseViewMatrixSerial;
				if(uniforms.inverseViewMatrixSerial != serial) {
					uniforms.inverseViewMatrixSerial = serial;
					FloatBuffer buf = matrixCopyBuffer;
					buf.clear();
					DynamicLightsStateManager.inverseViewMatrix.store(buf);
					buf.flip();
					_wglUniformMatrix4fv(uniforms.u_inverseViewMatrix4f, false, buf);
				}
			}
		}
	}

	@Override
	public void destroyPipeline(IProgramGL shaderProgram, int stateCoreBits, int stateExtBits, Object[] userPointer) {
		
	}

}

package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class DrawUtils {

	public static final String vertexShaderPath = "/assets/eagler/glsl/local.vsh";
	public static final String vertexShaderPrecision = "precision highp float;\n";

	public static IBufferArrayGL standardQuad2DVAO = null;
	public static IBufferArrayGL standardQuad3DVAO = null;
	public static IBufferGL standardQuadVBO = null;

	public static IShaderGL vshLocal = null;
	public static List<VSHInputLayoutParser.ShaderInput> vshLocalLayout = null;

	static void init() {
		if(standardQuad2DVAO == null) {
			standardQuad2DVAO = EaglercraftGPU.createGLBufferArray();
			standardQuad3DVAO = EaglercraftGPU.createGLBufferArray();
			standardQuadVBO = _wglGenBuffers();

			FloatBuffer verts = EagRuntime.allocateFloatBuffer(18);
			verts.put(new float[] {
					-1.0f, -1.0f, 0.0f,  1.0f, -1.0f, 0.0f,  -1.0f, 1.0f, 0.0f,
					1.0f, -1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  -1.0f, 1.0f, 0.0f
			});
			verts.flip();

			EaglercraftGPU.bindVAOGLArrayBufferNow(standardQuadVBO);
			_wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
			EagRuntime.freeFloatBuffer(verts);

			EaglercraftGPU.bindGLBufferArray(standardQuad2DVAO);

			EaglercraftGPU.enableVertexAttribArray(0);
			EaglercraftGPU.vertexAttribPointer(0, 2, GL_FLOAT, false, 12, 0);

			EaglercraftGPU.bindGLBufferArray(standardQuad3DVAO);

			EaglercraftGPU.enableVertexAttribArray(0);
			EaglercraftGPU.vertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
		}

		if(vshLocal == null) {
			String vertexSource = EagRuntime.getRequiredResourceString(vertexShaderPath);

			vshLocalLayout = VSHInputLayoutParser.getShaderInputs(vertexSource);

			vshLocal = _wglCreateShader(GL_VERTEX_SHADER);

			_wglShaderSource(vshLocal, GLSLHeader.getVertexHeaderCompat(vertexSource, vertexShaderPrecision));
			_wglCompileShader(vshLocal);
	
			if(_wglGetShaderi(vshLocal, GL_COMPILE_STATUS) != GL_TRUE) {
				EaglercraftGPU.logger.error("Failed to compile GL_VERTEX_SHADER \"" + vertexShaderPath + "\"!");
				String log = _wglGetShaderInfoLog(vshLocal);
				if(log != null) {
					String[] lines = log.split("(\\r\\n|\\r|\\n)");
					for(int i = 0; i < lines.length; ++i) {
						EaglercraftGPU.logger.error("[VERT] {}", lines[i]);
					}
				}
				throw new IllegalStateException("Vertex shader \"" + vertexShaderPath + "\" could not be compiled!");
			}
		}
	}

	public static void drawStandardQuad2D() {
		EaglercraftGPU.bindGLBufferArray(standardQuad2DVAO);
		EaglercraftGPU.doDrawArrays(GL_TRIANGLES, 0, 6);
	}

	public static void drawStandardQuad3D() {
		EaglercraftGPU.bindGLBufferArray(standardQuad3DVAO);
		EaglercraftGPU.doDrawArrays(GL_TRIANGLES, 0, 6);
	}

	public static void destroy() {
		if(standardQuad2DVAO != null) {
			EaglercraftGPU.destroyGLBufferArray(standardQuad2DVAO);
			standardQuad2DVAO = null;
		}
		if(standardQuad3DVAO != null) {
			EaglercraftGPU.destroyGLBufferArray(standardQuad3DVAO);
			standardQuad3DVAO = null;
		}
		if(standardQuadVBO != null) {
			_wglDeleteBuffers(standardQuadVBO);
			standardQuadVBO = null;
		}
		if(vshLocal != null) {
			vshLocal.free();
			vshLocal = null;
			vshLocalLayout = null;
		}
	}

}

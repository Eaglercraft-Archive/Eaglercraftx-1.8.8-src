/*
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

package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL;

public class GLSLHeader {

	public static final String GLES2_COMPAT_FILE_NAME = "/assets/eagler/glsl/gles2_compat.glsl";

	private static String header = null;
	private static String gles2CompatFile = null;

	static void init() {
		gles2CompatFile = EagRuntime.getRequiredResourceString(GLES2_COMPAT_FILE_NAME);
		int glesVersion = EaglercraftGPU.checkOpenGLESVersion();
		StringBuilder headerBuilder;
		if(glesVersion >= 310) {
			headerBuilder = new StringBuilder("#version 310 es");
			boolean oes5 = PlatformOpenGL.checkOESGPUShader5Capable();
			boolean ext5 = !oes5 && PlatformOpenGL.checkEXTGPUShader5Capable();
			if(oes5) {
				headerBuilder.append("\n#extension GL_OES_gpu_shader5 : enable");
			}else if(ext5) {
				headerBuilder.append("\n#extension GL_EXT_gpu_shader5 : enable");
			}
			headerBuilder.append("\n#define EAGLER_IS_GLES_310");
			headerBuilder.append("\n#define EAGLER_HAS_GLES_310");
			headerBuilder.append("\n#define EAGLER_HAS_GLES_300");
			headerBuilder.append("\n#define EAGLER_HAS_GLES_200");
			if(oes5 || ext5) {
				headerBuilder.append("\n#define EAGLER_HAS_GLES_310_SHADER_5");
			}
		}else if(glesVersion == 300) {
			headerBuilder = new StringBuilder("#version 300 es");
			headerBuilder.append("\n#define EAGLER_IS_GLES_300");
			headerBuilder.append("\n#define EAGLER_HAS_GLES_300");
			headerBuilder.append("\n#define EAGLER_HAS_GLES_200");
		}else if(glesVersion == 200) {
			boolean texLOD = PlatformOpenGL.checkTextureLODCapable();
			headerBuilder = new StringBuilder("#version 100");
			if(texLOD) {
				headerBuilder.append("\n#extension GL_EXT_shader_texture_lod : enable");
			}
			headerBuilder.append("\n#define EAGLER_HAS_GLES_200");
			headerBuilder.append("\n#define EAGLER_IS_GLES_200");
			if(texLOD) {
				headerBuilder.append("\n#define EAGLER_HAS_GLES_200_SHADER_TEXTURE_LOD");
			}
		}else {
			throw new IllegalStateException("Unsupported OpenGL ES version: " + glesVersion);
		}
		header = headerBuilder.append('\n').toString();
	}

	static void destroy() {
		header = null;
	}

	public static String getHeader() {
		if(header == null) throw new IllegalStateException();
		return header;
	}

	public static String getVertexHeader(String shaderSrc) {
		if(header == null) throw new IllegalStateException();
		return header + "#define EAGLER_IS_VERTEX_SHADER\n" + shaderSrc;
	}

	public static String getFragmentHeader(String shaderSrc) {
		if(header == null) throw new IllegalStateException();
		return header + "#define EAGLER_IS_FRAGMENT_SHADER\n" + shaderSrc;
	}

	public static String getVertexHeaderCompat(String shaderSrc, String precisions) {
		if(header == null || gles2CompatFile == null) throw new IllegalStateException();
		return header + "#define EAGLER_IS_VERTEX_SHADER\n" + (precisions == null ? "" : precisions + "\n") + gles2CompatFile + "\n" + shaderSrc;
	}

	public static String getFragmentHeaderCompat(String shaderSrc, String precisions) {
		if(header == null || gles2CompatFile == null) throw new IllegalStateException();
		return header + "#define EAGLER_IS_FRAGMENT_SHADER\n"+ (precisions == null ? "" : precisions + "\n") + gles2CompatFile + "\n" + shaderSrc;
	}

}
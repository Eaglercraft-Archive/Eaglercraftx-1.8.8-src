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

import java.util.Arrays;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.GLSLHeader;
import net.minecraft.util.ResourceLocation;

public class ShaderCompiler {

	private static final Logger logger = LogManager.getLogger("DeferredPipelineCompiler");

	public static IShaderGL compileShader(String name, int stage, ResourceLocation filename, String... compileFlags) throws ShaderCompileException {
		String src = ShaderSource.getSourceFor(filename);
		if(src == null) {
			throw new ShaderMissingException(name, "File not found: " + filename);
		}
		return compileShader(name, stage, filename.toString(), src, Arrays.asList(compileFlags));
	}

	public static IShaderGL compileShader(String name, int stage, String filename, String source, String... compileFlags) throws ShaderCompileException {
		return compileShader(name, stage, filename, source, Arrays.asList(compileFlags));
	}

	public static IShaderGL compileShader(String name, int stage, ResourceLocation filename, List<String> compileFlags) throws ShaderCompileException {
		String src = ShaderSource.getSourceFor(filename);
		if(src == null) {
			throw new ShaderMissingException(name, "File not found: " + filename);
		}
		return compileShader(name, stage, filename.toString(), src, compileFlags);
	}

	public static IShaderGL compileShader(String name, int stage, String filename, String source, List<String> compileFlags) throws ShaderCompileException {
		logger.info("Compiling Shader: " + filename);
		StringBuilder srcCat = new StringBuilder();
		srcCat.append(GLSLHeader.getHeader()).append('\n');
		
		if(compileFlags != null && compileFlags.size() > 0) {
			for(int i = 0, l = compileFlags.size(); i < l; ++i) {
				srcCat.append("#define ").append(compileFlags.get(i)).append('\n');
			}
		}
		
		IShaderGL ret = _wglCreateShader(stage);
		_wglShaderSource(ret, srcCat.append(source).toString());
		_wglCompileShader(ret);
		
		if(_wglGetShaderi(ret, GL_COMPILE_STATUS) != GL_TRUE) {
			Display.checkContextLost();
			logger.error("Failed to compile {} \"{}\" of program \"{}\"!", getStageName(stage), filename, name);
			String log = _wglGetShaderInfoLog(ret);
			if(log != null) {
				String s2 = getStageNameV2(stage);
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[{}] [{}] [{}] {}", name, s2, filename, lines[i]);
				}
			}
			_wglDeleteShader(ret);
			throw new ShaderCompileException(name, stage, filename, "Compile status for " + getStageName(stage) + " \"" + filename + "\" of \"" + name + "\" is not GL_TRUE!");
		}
		
		return ret;
	}

	public static IProgramGL linkProgram(String name, IShaderGL vert, IShaderGL frag) throws ShaderLinkException {
		IProgramGL ret = _wglCreateProgram();
		
		_wglAttachShader(ret, vert);
		_wglAttachShader(ret, frag);
		_wglLinkProgram(ret);
		_wglDetachShader(ret, vert);
		_wglDetachShader(ret, frag);
		
		if(_wglGetProgrami(ret, GL_LINK_STATUS) != GL_TRUE) {
			Display.checkContextLost();
			logger.error("Failed to link program \"{}\"!", name);
			String log = _wglGetProgramInfoLog(ret);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[{}] [LINK] {}", name, lines[i]);
				}
			}
			_wglDeleteProgram(ret);
			throw new ShaderLinkException(name, "Link status for program \"" + name + "\" is not GL_TRUE!");
		}
		
		return ret;
	}

	private static String getStageName(int stage) {
		switch(stage) {
		case GL_VERTEX_SHADER:
			return "GL_VERTEX_SHADER";
		case GL_FRAGMENT_SHADER:
			return "GL_FRAGMENT_SHADER";
		default:
			return "stage_" + stage;
		}
	}

	private static String getStageNameV2(int stage) {
		switch(stage) {
		case GL_VERTEX_SHADER:
			return "VERT";
		case GL_FRAGMENT_SHADER:
			return "FRAG";
		default:
			return "stage_" + stage;
		}
	}
}
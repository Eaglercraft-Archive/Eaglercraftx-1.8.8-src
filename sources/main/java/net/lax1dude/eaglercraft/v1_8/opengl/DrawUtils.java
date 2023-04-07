package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionConstants;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class DrawUtils {

	public static final String vertexShaderPath = "/assets/eagler/glsl/local.vsh";

	public static IBufferArrayGL standardQuad2DVAO = null;
	public static IBufferArrayGL standardQuad3DVAO = null;
	public static IBufferGL standardQuadVBO = null;

	public static IShaderGL vshLocal = null;

	static void init() {
		if(standardQuad2DVAO == null) {
			standardQuad2DVAO = _wglGenVertexArrays();
			standardQuad3DVAO = _wglGenVertexArrays();
			standardQuadVBO = _wglGenBuffers();

			FloatBuffer verts = EagRuntime.allocateFloatBuffer(18);
			verts.put(new float[] {
					-1.0f, -1.0f, 0.0f,  1.0f, -1.0f, 0.0f,  -1.0f, 1.0f, 0.0f,
					1.0f, -1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  -1.0f, 1.0f, 0.0f
			});
			verts.flip();

			EaglercraftGPU.bindGLArrayBuffer(standardQuadVBO);
			_wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
			EagRuntime.freeFloatBuffer(verts);

			EaglercraftGPU.bindGLBufferArray(standardQuad2DVAO);

			_wglEnableVertexAttribArray(0);
			_wglVertexAttribPointer(0, 2, GL_FLOAT, false, 12, 0);

			EaglercraftGPU.bindGLBufferArray(standardQuad3DVAO);

			_wglEnableVertexAttribArray(0);
			_wglVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
		}

		if(vshLocal == null) {
			String vertexSource = EagRuntime.getResourceString(vertexShaderPath);
			if(vertexSource == null) {
				throw new RuntimeException("vertex shader \"" + vertexShaderPath + "\" is missing!");
			}
	
			vshLocal = _wglCreateShader(GL_VERTEX_SHADER);
	
			_wglShaderSource(vshLocal, FixedFunctionConstants.VERSION + "\n" + vertexSource);
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
		_wglDrawArrays(GL_TRIANGLES, 0, 6);
	}

	public static void drawStandardQuad3D() {
		EaglercraftGPU.bindGLBufferArray(standardQuad3DVAO);
		_wglDrawArrays(GL_TRIANGLES, 0, 6);
	}

}

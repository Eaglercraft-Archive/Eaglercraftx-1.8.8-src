package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionConstants;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
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
public class InstancedFontRenderer {

	private static final Logger logger = LogManager.getLogger("InstancedFontRenderer");

	public static final String vertexShaderPath = "/assets/eagler/glsl/accel_font.vsh";
	public static final String fragmentShaderPath = "/assets/eagler/glsl/accel_font.fsh";

	private static final int BYTES_PER_CHARACTER = 10;
	private static final int CHARACTER_LIMIT = 6553;

	private static IProgramGL shaderProgram = null;
	private static IUniformGL u_matrixTransform = null;
	private static FloatBuffer matrixCopyBuffer = null;
	private static IUniformGL u_charSize2f = null;
	private static IUniformGL u_charCoordSize2f = null;
	private static IUniformGL u_color4f = null;
	private static IUniformGL u_colorBias4f = null;

	private static IBufferArrayGL vertexArray = null;
	private static IBufferGL vertexBuffer = null;

	private static IBufferGL instancesBuffer = null;

	private static float stateColorR = -999.0f;
	private static float stateColorG = -999.0f;
	private static float stateColorB = -999.0f;
	private static float stateColorA = -999.0f;
	private static int stateColorSerial = -1;

	private static float stateColorBiasR = -999.0f;
	private static float stateColorBiasG = -999.0f;
	private static float stateColorBiasB = -999.0f;
	private static float stateColorBiasA = -999.0f;
	
	private static final Matrix4f tmpMatrix = new Matrix4f();
	private static final Vector4f tmpVector = new Vector4f();
	private static int stateModelMatrixSerial = -1;
	private static int stateProjectionMatrixSerial = -1;
	
	private static float charWidthValue = -1;
	private static float charHeightValue = -1;
	private static float charCoordWidthValue = -1.0f;
	private static float charCoordHeightValue = -1.0f;
	
	static void initialize() {
		
		String vertexSource = EagRuntime.getResourceString(vertexShaderPath);
		if(vertexSource == null) {
			throw new RuntimeException("InstancedFontRenderer shader \"" + vertexShaderPath + "\" is missing!");
		}

		String fragmentSource = EagRuntime.getResourceString(fragmentShaderPath);
		if(fragmentSource == null) {
			throw new RuntimeException("InstancedFontRenderer shader \"" + fragmentShaderPath + "\" is missing!");
		}

		IShaderGL vert = _wglCreateShader(GL_VERTEX_SHADER);
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(vert, FixedFunctionConstants.VERSION + "\n" + vertexSource);
		_wglCompileShader(vert);

		if(_wglGetShaderi(vert, GL_COMPILE_STATUS) != GL_TRUE) {
			logger.error("Failed to compile GL_VERTEX_SHADER \"" + vertexShaderPath + "\" for InstancedFontRenderer!");
			String log = _wglGetShaderInfoLog(vert);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[VERT] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Vertex shader \"" + vertexShaderPath + "\" could not be compiled!");
		}

		_wglShaderSource(frag, FixedFunctionConstants.VERSION + "\n" + fragmentSource);
		_wglCompileShader(frag);

		if(_wglGetShaderi(frag, GL_COMPILE_STATUS) != GL_TRUE) {
			logger.error("Failed to compile GL_FRAGMENT_SHADER \"" + fragmentShaderPath + "\" for InstancedFontRenderer!");
			String log = _wglGetShaderInfoLog(frag);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[FRAG] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Fragment shader \"" + fragmentShaderPath + "\" could not be compiled!");
		}

		shaderProgram = _wglCreateProgram();

		_wglAttachShader(shaderProgram, vert);
		_wglAttachShader(shaderProgram, frag);

		_wglLinkProgram(shaderProgram);

		_wglDetachShader(shaderProgram, vert);
		_wglDetachShader(shaderProgram, frag);

		_wglDeleteShader(vert);
		_wglDeleteShader(frag);

		if(_wglGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
			logger.error("Failed to link shader program for InstancedFontRenderer!");
			String log = _wglGetProgramInfoLog(shaderProgram);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					logger.error("[LINK] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Shader program for InstancedFontRenderer could not be linked!");
		}

		matrixCopyBuffer = EagRuntime.allocateFloatBuffer(16);
		fontDataBuffer = EagRuntime.allocateByteBuffer(CHARACTER_LIMIT * BYTES_PER_CHARACTER);
		fontBoldDataBuffer = EagRuntime.allocateByteBuffer(CHARACTER_LIMIT * BYTES_PER_CHARACTER);

		EaglercraftGPU.bindGLShaderProgram(shaderProgram);

		u_matrixTransform = _wglGetUniformLocation(shaderProgram, "u_matrixTransform");
		u_charSize2f = _wglGetUniformLocation(shaderProgram, "u_charSize2f");
		u_charCoordSize2f = _wglGetUniformLocation(shaderProgram, "u_charCoordSize2f");
		u_color4f = _wglGetUniformLocation(shaderProgram, "u_color4f");
		u_colorBias4f = _wglGetUniformLocation(shaderProgram, "u_colorBias4f");

		_wglUniform1i(_wglGetUniformLocation(shaderProgram, "u_inputTexture"), 0);

		vertexArray = _wglGenVertexArrays();
		vertexBuffer = _wglGenBuffers();
		instancesBuffer = _wglGenBuffers();

		FloatBuffer verts = EagRuntime.allocateFloatBuffer(108);
		verts.put(new float[] {
				
				// (0 - 6 - 12) regular:
				
				0.0f, 0.0f, 0.25f,  0.0f, 1.0f, 0.25f,  1.0f, 0.0f, 0.25f,
				1.0f, 0.0f, 0.25f,  0.0f, 1.0f, 0.25f,  1.0f, 1.0f, 0.25f,
				0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,

				// (12 - 24 - 36) bold shadow:

				0.0f, 0.0f, 0.25f,  0.0f, 1.0f, 0.25f,  1.0f, 0.0f, 0.25f,
				1.0f, 0.0f, 0.25f,  0.0f, 1.0f, 0.25f,  1.0f, 1.0f, 0.25f,
				0.0f, 0.0f, 0.75f,  0.0f, 1.0f, 0.75f,  1.0f, 0.0f, 0.75f,
				1.0f, 0.0f, 0.75f,  0.0f, 1.0f, 0.75f,  1.0f, 1.0f, 0.75f,

				0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 0.5f,  0.0f, 1.0f, 0.5f,  1.0f, 0.0f, 0.5f,
				1.0f, 0.0f, 0.5f,  0.0f, 1.0f, 0.5f,  1.0f, 1.0f, 0.5f

		});
		verts.flip();

		EaglercraftGPU.bindGLBufferArray(vertexArray);

		EaglercraftGPU.bindGLArrayBuffer(vertexBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);

		EagRuntime.freeFloatBuffer(verts);

		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
		_wglVertexAttribDivisor(0, 0);

		EaglercraftGPU.bindGLArrayBuffer(instancesBuffer);
		_wglBufferData(GL_ARRAY_BUFFER, fontDataBuffer.remaining(), GL_STATIC_DRAW);

		_wglEnableVertexAttribArray(1);
		_wglVertexAttribPointer(1, 2, GL_SHORT, false, 10, 0);
		_wglVertexAttribDivisor(1, 1);

		_wglEnableVertexAttribArray(2);
		_wglVertexAttribPointer(2, 2, GL_UNSIGNED_BYTE, false, 10, 4);
		_wglVertexAttribDivisor(2, 1);

		_wglEnableVertexAttribArray(3);
		_wglVertexAttribPointer(3, 4, GL_UNSIGNED_BYTE, true, 10, 6);
		_wglVertexAttribDivisor(3, 1);
		
	}

	private static ByteBuffer fontDataBuffer = null;
	private static int charactersDrawn = 0;
	private static ByteBuffer fontBoldDataBuffer = null;
	private static int boldCharactersDrawn = 0;
	private static boolean hasOverflowed = false;
	private static boolean hasBoldOverflowed = false;

	private static boolean fogEnabled = false;
	private static int widthCalcLeast = Integer.MAX_VALUE;
	private static int heightCalcLeast = Integer.MAX_VALUE;
	private static int widthCalcMost = Integer.MAX_VALUE;
	private static int heightCalcMost = Integer.MAX_VALUE;

	public static void begin() {
		fontDataBuffer.clear();
		charactersDrawn = 0;
		fontBoldDataBuffer.clear();
		boldCharactersDrawn = 0;
		hasOverflowed = false;
		hasBoldOverflowed = false;
		fogEnabled = GlStateManager.stateFog && GlStateManager.stateFogDensity > 0.0f;
		if(fogEnabled) {
			widthCalcLeast = Integer.MAX_VALUE;
			heightCalcLeast = Integer.MAX_VALUE;
			widthCalcMost = Integer.MAX_VALUE;
			heightCalcMost = Integer.MAX_VALUE;
		}
	}

	public static void render(float charWidth, float charHeight, float charCoordWidth, float charCoordHeight, boolean shadow) {
		if(charactersDrawn == 0 && boldCharactersDrawn == 0) {
			return;
		}
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		
		if(charWidth != charWidthValue || charHeight != charHeightValue) {
			charWidthValue = charWidth;
			charHeightValue = charHeight;
			_wglUniform2f(u_charSize2f, (float)charWidth, (float)charHeight);
		}
		
		if(charCoordWidth != charCoordWidthValue || charCoordHeight != charCoordHeightValue) {
			charCoordWidthValue = charCoordWidth;
			charCoordHeightValue = charCoordHeight;
			_wglUniform2f(u_charCoordSize2f, charCoordWidth, charCoordHeight);
		}
		
		int ptr1 = GlStateManager.modelMatrixStackPointer;
		int serial1 = GlStateManager.modelMatrixStackAccessSerial[ptr1];
		int ptr2 = GlStateManager.projectionMatrixStackPointer;
		int serial2 = GlStateManager.projectionMatrixStackAccessSerial[ptr2];
		if(stateModelMatrixSerial != serial1 || stateProjectionMatrixSerial != serial2) {
			stateModelMatrixSerial = serial1;
			stateProjectionMatrixSerial = serial2;
			Matrix4f.mul(GlStateManager.projectionMatrixStack[ptr2], GlStateManager.modelMatrixStack[ptr1], tmpMatrix);
			matrixCopyBuffer.clear();
			tmpMatrix.store(matrixCopyBuffer);
			matrixCopyBuffer.flip();
			_wglUniformMatrix4fv(u_matrixTransform, false, matrixCopyBuffer);
		}
		
		if(!fogEnabled || DeferredStateManager.isInDeferredPass()) {
			int serial = GlStateManager.stateColorSerial;
			if(stateColorSerial != serial) {
				stateColorSerial = serial;
				float r = GlStateManager.stateColorR;
				float g = GlStateManager.stateColorG;
				float b = GlStateManager.stateColorB;
				float a = GlStateManager.stateColorA;
				if(stateColorR != r || stateColorG != g ||
					stateColorB != b || stateColorA != a) {
					_wglUniform4f(u_color4f, r, g, b, a);
					stateColorR = r;
					stateColorG = g;
					stateColorB = b;
					stateColorA = a;
				}
			}
			if(stateColorBiasR != 0.0f || stateColorBiasG != 0.0f ||
					stateColorBiasB != 0.0f || stateColorBiasA != 0.0f) {
				_wglUniform4f(u_colorBias4f, 0.0f, 0.0f, 0.0f, 0.0f);
				stateColorBiasR = 0.0f;
				stateColorBiasG = 0.0f;
				stateColorBiasB = 0.0f;
				stateColorBiasA = 0.0f;
			}
		}else {
			stateColorSerial = -1;
			Vector4f vec4 = tmpVector;
			vec4.x = (float)(widthCalcLeast + (widthCalcMost - widthCalcLeast + 1.0f) * 0.5f) * charWidth;
			vec4.y = (float)(heightCalcLeast + (heightCalcMost - heightCalcLeast + 1.0f) * 0.5f)* charHeight;
			vec4.z = 0.0f;
			vec4.w = 1.0f;

			Matrix4f.transform(GlStateManager.modelMatrixStack[ptr1], vec4, vec4);

			vec4.x /= vec4.w;
			vec4.y /= vec4.w;
			vec4.z /= vec4.w;
			vec4.w = 1.0f;

			vec4.x *= vec4.x;
			vec4.y *= vec4.y;
			vec4.z *= vec4.z;

			float fogFactor = (float) Math.sqrt(vec4.x + vec4.y + vec4.z);
			if(GlStateManager.stateFogEXP) {
				fogFactor = 1.0f - (float) Math.pow(2.718, -(GlStateManager.stateFogDensity * fogFactor));
			}else {
				fogFactor = (fogFactor - GlStateManager.stateFogStart) / (GlStateManager.stateFogEnd - GlStateManager.stateFogStart);
			}

			if(fogFactor > 1.0f) fogFactor = 1.0f;
			if(fogFactor < 0.0f) fogFactor = 0.0f;

			float r = GlStateManager.stateColorR;
			float g = GlStateManager.stateColorG;
			float b = GlStateManager.stateColorB;
			float a = GlStateManager.stateColorA;

			float fogFactor2 = (1.0f - fogFactor) * GlStateManager.stateFogColorA;
			r *= fogFactor2;
			g *= fogFactor2;
			b *= fogFactor2;

			if(stateColorR != r || stateColorG != g ||
				stateColorB != b || stateColorA != a) {
				_wglUniform4f(u_color4f, r, g, b, a);
				stateColorR = r;
				stateColorG = g;
				stateColorB = b;
				stateColorA = a;
			}

			fogFactor *= GlStateManager.stateFogColorA;
			float biasR = GlStateManager.stateFogColorR * fogFactor;
			float biasG = GlStateManager.stateFogColorG * fogFactor;
			float biasB = GlStateManager.stateFogColorB * fogFactor;
			float biasA = 0.0f;

			if(stateColorBiasR != biasR || stateColorBiasG != biasG ||
				stateColorBiasB != biasB || stateColorBiasA != biasA) {
				_wglUniform4f(u_colorBias4f, biasR, biasG, biasB, biasA);
				stateColorBiasR = biasR;
				stateColorBiasG = biasG;
				stateColorBiasB = biasB;
				stateColorBiasA = biasA;
			}
		}

		EaglercraftGPU.bindGLArrayBuffer(instancesBuffer);
		EaglercraftGPU.bindGLBufferArray(vertexArray);

		if(charactersDrawn > 0) {
			int p = fontDataBuffer.position();
			int l = fontDataBuffer.limit();

			fontDataBuffer.flip();
			_wglBufferSubData(GL_ARRAY_BUFFER, 0, fontDataBuffer);

			fontDataBuffer.position(p);
			fontDataBuffer.limit(l);

			_wglDrawArraysInstanced(GL_TRIANGLES, shadow ? 0 : 6, shadow ? 12 : 6, charactersDrawn);
		}

		if(boldCharactersDrawn > 0) {
			int p = fontBoldDataBuffer.position();
			int l = fontBoldDataBuffer.limit();

			fontBoldDataBuffer.flip();
			_wglBufferSubData(GL_ARRAY_BUFFER, 0, fontBoldDataBuffer);

			fontBoldDataBuffer.position(p);
			fontBoldDataBuffer.limit(l);

			_wglDrawArraysInstanced(GL_TRIANGLES, shadow ? 12 : 24, shadow ? 24 : 12, boldCharactersDrawn);
		}
	}

	public static void appendQuad(int x, int y, int cx, int cy, int color, boolean italic) {
		if(hasOverflowed) {
			return;
		}
		if(charactersDrawn >= CHARACTER_LIMIT) {
			hasOverflowed = true;
			logger.error("Font renderer buffer has overflowed! Exceeded {} regular characters, no more regular characters will be rendered.", CHARACTER_LIMIT);
			return;
		}
		++charactersDrawn;
		ByteBuffer buf = fontDataBuffer;
		buf.putShort((short)x);
		buf.putShort((short)y);
		buf.put((byte)cx);
		buf.put((byte)cy);
		color = ((color >> 1) & 0x7F000000) | (color & 0xFFFFFF);
		if(italic) {
			color |= 0x80000000;
		}
		buf.putInt(color);
		if(fogEnabled) {
			updateBounds(x, y);
		}
	}

	public static void appendBoldQuad(int x, int y, int cx, int cy, int color, boolean italic) {
		if(hasBoldOverflowed) {
			return;
		}
		if(boldCharactersDrawn >= CHARACTER_LIMIT) {
			hasBoldOverflowed = true;
			logger.error("Font renderer buffer has overflowed! Exceeded {} bold characters, no more bold characters will be rendered.", CHARACTER_LIMIT);
			return;
		}
		++boldCharactersDrawn;
		ByteBuffer buf = fontBoldDataBuffer;
		buf.putShort((short)x);
		buf.putShort((short)y);
		buf.put((byte)cx);
		buf.put((byte)cy);
		color = ((color >> 1) & 0x7F000000) | (color & 0xFFFFFF);
		if(italic) {
			color |= 0x80000000;
		}
		buf.putInt(color);
		if(fogEnabled) {
			updateBounds(x, y);
		}
	}

	private static final void updateBounds(int x, int y) {
		if(x < widthCalcLeast || widthCalcLeast == Integer.MAX_VALUE) widthCalcLeast = x;
		if(x > widthCalcMost || widthCalcMost == Integer.MAX_VALUE) widthCalcMost = x;
		if(y < heightCalcLeast || heightCalcLeast == Integer.MAX_VALUE) heightCalcLeast = y;
		if(y > heightCalcMost || heightCalcMost == Integer.MAX_VALUE) heightCalcMost = y;
	}

}

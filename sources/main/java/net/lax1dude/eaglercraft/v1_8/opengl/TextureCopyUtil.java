package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
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
public class TextureCopyUtil {

	private static final Logger LOGGER = LogManager.getLogger("TextureCopyUtil");

	public static final String vertexShaderPath = "/assets/eagler/glsl/texture_blit.vsh";
	public static final String fragmentShaderPath = "/assets/eagler/glsl/texture_blit.fsh";

	private static String vshSource = null;
	private static String fshSource = null;

	private static IShaderGL vshShader = null;

	private static class TextureCopyShader {
		private IProgramGL shaderProgram = null;
		private IUniformGL u_srcCoords4f = null;
		private IUniformGL u_dstCoords4f = null;
		private IUniformGL u_textureLod1f = null;
		private IUniformGL u_pixelAlignmentSizes4f = null;
		private IUniformGL u_pixelAlignmentOffset2f = null;
		private TextureCopyShader(IProgramGL shaderProgram) {
			this.shaderProgram = shaderProgram;
			EaglercraftGPU.bindGLShaderProgram(shaderProgram);
			this.u_srcCoords4f = _wglGetUniformLocation(shaderProgram, "u_srcCoords4f");
			this.u_dstCoords4f = _wglGetUniformLocation(shaderProgram, "u_dstCoords4f");
			this.u_textureLod1f = _wglGetUniformLocation(shaderProgram, "u_textureLod1f");
			this.u_pixelAlignmentSizes4f = _wglGetUniformLocation(shaderProgram, "u_pixelAlignmentSizes4f");
			this.u_pixelAlignmentOffset2f = _wglGetUniformLocation(shaderProgram, "u_pixelAlignmentOffset2f");
		}
	}

	private static TextureCopyShader textureBlit = null;
	private static TextureCopyShader textureBlitAligned = null;
	private static TextureCopyShader textureBlitDepth = null;
	private static TextureCopyShader textureBlitDepthAligned = null;

	private static float srcViewW = 100.0f;
	private static float srcViewH = 100.0f;

	private static float dstViewW = 50.0f;
	private static float dstViewH = 50.0f;

	private static boolean isAligned = false;
	private static int alignW = 0;
	private static int alignH = 0;
	private static float alignOffsetX = 0.0f;
	private static float alignOffsetY = 0.0f;

	static void initialize() {
		vshSource = EagRuntime.getResourceString(vertexShaderPath);
		if(vshSource == null) {
			throw new RuntimeException("TextureCopyUtil shader \"" + vertexShaderPath + "\" is missing!");
		}

		fshSource = EagRuntime.getResourceString(fragmentShaderPath);
		if(fshSource == null) {
			throw new RuntimeException("TextureCopyUtil shader \"" + fragmentShaderPath + "\" is missing!");
		}

		vshShader = _wglCreateShader(GL_VERTEX_SHADER);

		_wglShaderSource(vshShader, FixedFunctionConstants.VERSION + "\n" + vshSource);
		_wglCompileShader(vshShader);

		if(_wglGetShaderi(vshShader, GL_COMPILE_STATUS) != GL_TRUE) {
			LOGGER.error("Failed to compile GL_VERTEX_SHADER \"" + vertexShaderPath + "\" for TextureCopyUtil!");
			String log = _wglGetShaderInfoLog(vshShader);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					LOGGER.error("[VERT] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Vertex shader \"" + vertexShaderPath + "\" could not be compiled!");
		}
	}

	private static TextureCopyShader compileShader(boolean align, boolean depth) {
		IShaderGL frag = _wglCreateShader(GL_FRAGMENT_SHADER);

		_wglShaderSource(frag,
				FixedFunctionConstants.VERSION + "\n" + (align ? "#define COMPILE_PIXEL_ALIGNMENT\n" : "")
						+ (depth ? "#define COMPILE_BLIT_DEPTH\n" : "") + fshSource);
		_wglCompileShader(frag);

		if(_wglGetShaderi(frag, GL_COMPILE_STATUS) != GL_TRUE) {
			LOGGER.error("Failed to compile GL_FRAGMENT_SHADER \"" + fragmentShaderPath + "\" for TextureCopyUtil!");
			String log = _wglGetShaderInfoLog(frag);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					LOGGER.error("[FRAG] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Fragment shader \"" + fragmentShaderPath + "\" could not be compiled!");
		}

		IProgramGL shaderProgram = _wglCreateProgram();

		_wglAttachShader(shaderProgram, vshShader);
		_wglAttachShader(shaderProgram, frag);

		_wglLinkProgram(shaderProgram);

		_wglDetachShader(shaderProgram, vshShader);
		_wglDetachShader(shaderProgram, frag);

		_wglDeleteShader(frag);

		if(_wglGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
			LOGGER.error("Failed to link shader program for TextureCopyUtil!");
			String log = _wglGetProgramInfoLog(shaderProgram);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					LOGGER.error("[LINK] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Shader program for TextureCopyUtil could not be linked!");
		}

		return new TextureCopyShader(shaderProgram);
	}

	private static TextureCopyShader getShaderObj(boolean align, boolean depth) {
		if(align) {
			if(depth) {
				if(textureBlitDepthAligned == null) textureBlitDepthAligned = compileShader(true, true);
				return textureBlitDepthAligned;
			}else {
				if(textureBlitAligned == null) textureBlitAligned = compileShader(true, false);
				return textureBlitAligned;
			}
		}else {
			if(depth) {
				if(textureBlitDepth == null) textureBlitDepth = compileShader(false, true);
				return textureBlitDepth;
			}else {
				if(textureBlit == null) textureBlit = compileShader(false, false);
				return textureBlit;
			}
		}
	}

	public static void srcSize(int w, int h) {
		srcViewW = w;
		srcViewH = h;
	}

	public static void dstSize(int w, int h) {
		dstViewW = w * 0.5f;
		dstViewH = h * 0.5f;
	}

	public static void srcDstSize(int w, int h) {
		srcViewW = w;
		srcViewH = h;
		dstViewW = w * 0.5f;
		dstViewH = h * 0.5f;
	}

	/**
	 * this is reset after every blit
	 */
	public static void alignPixels(int dstW, int dstH, float alignX, float alignY) {
		isAligned = true;
		alignW = dstW;
		alignH = dstH;
		alignOffsetX = alignX;
		alignOffsetY = alignY;
	}

	/**
	 * this is reset after every blit
	 */
	public static void alignPixelsTopLeft(int srcW, int srcH, int dstW, int dstH) {
		alignPixels(dstW, dstH, (0.5f * dstW) / srcW, (0.5f * dstH) / srcH);
	}

	public static void disableAlign() {
		isAligned = false;
	}

	public static void blitTexture(int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTexture(0, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTexture(int lvl, int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTexture(lvl, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTexture(int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		blitTexture(0, srcX, srcY, srcW, srcH, dstX, dstY, dstW, dstH);
	}

	public static void blitTexture(int lvl, int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		TextureCopyShader shaderObj = getShaderObj(isAligned, false);
		EaglercraftGPU.bindGLShaderProgram(shaderObj.shaderProgram);
		_wglUniform4f(shaderObj.u_srcCoords4f, (float)srcX / srcViewW, (float)srcY / srcViewH, (float)srcW / srcViewW, (float)srcH / srcViewH);
		_wglUniform4f(shaderObj.u_dstCoords4f, (float) dstX / dstViewW - 1.0f, (float) dstY / dstViewH - 1.0f,
				(float) dstW / dstViewW, (float) dstH / dstViewH);
		_wglUniform1f(shaderObj.u_textureLod1f, lvl);
		if(isAligned) {
			_wglUniform4f(shaderObj.u_pixelAlignmentSizes4f, alignW, alignH, 1.0f / alignW, 1.0f / alignH);
			_wglUniform2f(shaderObj.u_pixelAlignmentOffset2f, alignOffsetX, alignOffsetY);
			isAligned = false;
		}
		DrawUtils.drawStandardQuad2D();
	}

	public static void blitTexture() {
		blitTexture(0);
	}

	public static void blitTexture(int lvl) {
		TextureCopyShader shaderObj = getShaderObj(isAligned, false);
		EaglercraftGPU.bindGLShaderProgram(shaderObj.shaderProgram);
		_wglUniform4f(shaderObj.u_srcCoords4f, 0.0f, 0.0f, 1.0f, 1.0f);
		_wglUniform4f(shaderObj.u_dstCoords4f, -1.0f, -1.0f, 2.0f, 2.0f);
		_wglUniform1f(shaderObj.u_textureLod1f, lvl);
		if(isAligned) {
			_wglUniform4f(shaderObj.u_pixelAlignmentSizes4f, alignW, alignH, 1.0f / alignW, 1.0f / alignH);
			_wglUniform2f(shaderObj.u_pixelAlignmentOffset2f, alignOffsetX, alignOffsetY);
			isAligned = false;
		}
		DrawUtils.drawStandardQuad2D();
	}

	public static void blitTextureUsingViewports(int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTextureUsingViewports(0, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTextureUsingViewports(int lvl, int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTextureUsingViewports(lvl, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTextureUsingViewports(int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		blitTextureUsingViewports(0, srcX, srcY, srcW, srcH, dstX, dstY, dstW, dstH);
	}

	public static void blitTextureUsingViewports(int lvl, int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		TextureCopyShader shaderObj = getShaderObj(isAligned, false);
		EaglercraftGPU.bindGLShaderProgram(shaderObj.shaderProgram);
		GlStateManager.viewport(dstX, dstY, dstW, dstH);
		_wglUniform4f(shaderObj.u_srcCoords4f, (float)srcX / srcViewW, (float)srcY / srcViewH, (float)srcW / srcViewW, (float)srcH / srcViewH);
		_wglUniform4f(shaderObj.u_dstCoords4f, -1.0f, -1.0f, 2.0f, 2.0f);
		_wglUniform1f(shaderObj.u_textureLod1f, lvl);
		if(isAligned) {
			_wglUniform4f(shaderObj.u_pixelAlignmentSizes4f, alignW, alignH, 1.0f / alignW, 1.0f / alignH);
			_wglUniform2f(shaderObj.u_pixelAlignmentOffset2f, alignOffsetX, alignOffsetY);
			isAligned = false;
		}
		DrawUtils.drawStandardQuad2D();
	}

	public static void blitTextureDepth(int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTextureDepth(0, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTextureDepth(int lvl, int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTextureDepth(lvl, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTextureDepth(int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		blitTextureDepth(0, srcX, srcY, srcW, srcH, dstX, dstY, dstW, dstH);
	}

	public static void blitTextureDepth(int lvl, int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		TextureCopyShader shaderObj = getShaderObj(isAligned, true);
		EaglercraftGPU.bindGLShaderProgram(shaderObj.shaderProgram);
		_wglUniform4f(shaderObj.u_srcCoords4f, (float)srcX / srcViewW, (float)srcY / srcViewH, (float)srcW / srcViewW, (float)srcH / srcViewH);
		_wglUniform4f(shaderObj.u_dstCoords4f, (float) dstX / dstViewW - 1.0f, (float) dstY / dstViewH - 1.0f,
				(float) dstW / dstViewW, (float) dstH / dstViewH);
		_wglUniform1f(shaderObj.u_textureLod1f, lvl);
		if(isAligned) {
			_wglUniform4f(shaderObj.u_pixelAlignmentSizes4f, alignW, alignH, 1.0f / alignW, 1.0f / alignH);
			_wglUniform2f(shaderObj.u_pixelAlignmentOffset2f, alignOffsetX, alignOffsetY);
			isAligned = false;
		}
		DrawUtils.drawStandardQuad2D();
	}

	public static void blitTextureDepth() {
		blitTextureDepth(0);
	}

	public static void blitTextureDepth(int lvl) {
		TextureCopyShader shaderObj = getShaderObj(isAligned, true);
		EaglercraftGPU.bindGLShaderProgram(shaderObj.shaderProgram);
		_wglUniform4f(shaderObj.u_srcCoords4f, 0.0f, 0.0f, 1.0f, 1.0f);
		_wglUniform4f(shaderObj.u_dstCoords4f, -1.0f, -1.0f, 2.0f, 2.0f);
		_wglUniform1f(shaderObj.u_textureLod1f, lvl);
		if(isAligned) {
			_wglUniform4f(shaderObj.u_pixelAlignmentSizes4f, alignW, alignH, 1.0f / alignW, 1.0f / alignH);
			_wglUniform2f(shaderObj.u_pixelAlignmentOffset2f, alignOffsetX, alignOffsetY);
			isAligned = false;
		}
		DrawUtils.drawStandardQuad2D();
	}

	public static void blitTextureDepthUsingViewports(int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTextureDepthUsingViewports(0, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTextureDepthUsingViewports(int lvl, int srcX, int srcY, int dstX, int dstY, int w, int h) {
		blitTextureDepthUsingViewports(lvl, srcX, srcY, w, h, dstX, dstY, w, h);
	}

	public static void blitTextureDepthUsingViewports(int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		blitTextureDepthUsingViewports(0, srcX, srcY, srcW, srcH, dstX, dstY, dstW, dstH);
	}

	public static void blitTextureDepthUsingViewports(int lvl, int srcX, int srcY, int srcW, int srcH, int dstX, int dstY, int dstW, int dstH) {
		TextureCopyShader shaderObj = getShaderObj(isAligned, true);
		EaglercraftGPU.bindGLShaderProgram(shaderObj.shaderProgram);
		GlStateManager.viewport(dstX, dstY, dstW, dstH);
		_wglUniform4f(shaderObj.u_srcCoords4f, (float)srcX / srcViewW, (float)srcY / srcViewH, (float)srcW / srcViewW, (float)srcH / srcViewH);
		_wglUniform4f(shaderObj.u_dstCoords4f, -1.0f, -1.0f, 2.0f, 2.0f);
		_wglUniform1f(shaderObj.u_textureLod1f, lvl);
		if(isAligned) {
			_wglUniform4f(shaderObj.u_pixelAlignmentSizes4f, alignW, alignH, 1.0f / alignW, 1.0f / alignH);
			_wglUniform2f(shaderObj.u_pixelAlignmentOffset2f, alignOffsetX, alignOffsetY);
			isAligned = false;
		}
		DrawUtils.drawStandardQuad2D();
	}
}

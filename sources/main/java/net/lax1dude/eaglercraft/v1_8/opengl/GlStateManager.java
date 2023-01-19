package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

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
public class GlStateManager {
	
	static final Logger logger = LogManager.getLogger("GlStateManager");

	static boolean stateDepthTest = false;
	static int stateDepthFunc = -1;
	static boolean stateDepthMask = true;

	static boolean stateCull = false;
	static int stateCullFace = GL_BACK;

	static boolean statePolygonOffset = false;
	static float statePolygonOffsetFactor = 0.0f;
	static float statePolygonOffsetUnits = 0.0f;

	static float stateColorR = 1.0f;
	static float stateColorG = 1.0f;
	static float stateColorB = 1.0f;
	static float stateColorA = 1.0f;
	static int stateColorSerial = 0;

	static float stateShaderBlendSrcColorR = 1.0f;
	static float stateShaderBlendSrcColorG = 1.0f;
	static float stateShaderBlendSrcColorB = 1.0f;
	static float stateShaderBlendSrcColorA = 1.0f;
	static float stateShaderBlendAddColorR = 0.0f;
	static float stateShaderBlendAddColorG = 0.0f;
	static float stateShaderBlendAddColorB = 0.0f;
	static float stateShaderBlendAddColorA = 0.0f;
	static int stateShaderBlendColorSerial = 0;
	static boolean stateEnableShaderBlendColor = false;
	
	static boolean stateBlend = false;
	static int stateBlendEquation = -1;
	static int stateBlendSRC = -1;
	static int stateBlendDST = -1;
	static boolean stateEnableOverlayFramebufferBlending = false;

	static boolean stateAlphaTest = false;
	static float stateAlphaTestRef = 0.1f;

	static boolean stateMaterial = false;
	static boolean stateLighting = false;
	static int stateLightsStackPointer = 0;
	static final boolean[][] stateLightsEnabled = new boolean[4][8];
	static final Vector4f[][] stateLightsStack = new Vector4f[4][8];
	static final int[] stateLightingSerial = new int[4];

	static float stateLightingAmbientR = 0.0f;
	static float stateLightingAmbientG = 0.0f;
	static float stateLightingAmbientB = 0.0f;
	static int stateLightingAmbientSerial = 0;
	
	static float stateNormalX = 0.0f;
	static float stateNormalY = 0.0f;
	static float stateNormalZ = -1.0f;
	static int stateNormalSerial = 0;

	static boolean stateFog = false;
	static boolean stateFogEXP = false;
	static float stateFogDensity = 1.0f;
	static float stateFogStart = 0.0f;
	static float stateFogEnd = 1.0f;
	static float stateFogColorR = 1.0f;
	static float stateFogColorG = 1.0f;
	static float stateFogColorB = 1.0f;
	static float stateFogColorA = 1.0f;
	static int stateFogSerial = 0;
	
	static int activeTexture = 0;
	static final boolean[] stateTexture = new boolean[16];
	static final int[] boundTexture = new int[] {
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1
	};

	static float stateAnisotropicFixW = -999.0f;
	static float stateAnisotropicFixH = -999.0f;
	static int stateAnisotropicFixSerial = 0;

	static boolean stateTexGen = false;

	static int viewportX = -1;
	static int viewportY = -1;
	static int viewportW = -1;
	static int viewportH = -1;
	
	static int colorMaskBits = 15;

	static float clearColorR = 0.0f;
	static float clearColorG = 0.0f;
	static float clearColorB = 0.0f;
	static float clearColorA = 1.0f;
	
	static float clearDepth = -999.0f;
	
	public static enum TexGen {
		S, T, R, Q;

		int source = GL_OBJECT_LINEAR;
		int plane = GL_OBJECT_PLANE;
		Vector4f vector = new Vector4f();
		
	}
	
	static int stateTexGenSerial = 0;

	static int stateMatrixMode = GL_MODELVIEW;
	
	static final Matrix4f[] modelMatrixStack = new Matrix4f[48];
	static final int[] modelMatrixStackAccessSerial = new int[48];
	private static int modelMatrixAccessSerial = 0;
	static int modelMatrixStackPointer = 0;
	
	static final Matrix4f[] projectionMatrixStack = new Matrix4f[8];
	static final int[] projectionMatrixStackAccessSerial = new int[8];
	private static int projectionMatrixAccessSerial = 0;
	static int projectionMatrixStackPointer = 0;

	static final float[] textureCoordsX = new float[8];
	static final float[] textureCoordsY = new float[8];
	static final int[] textureCoordsAccessSerial = new int[8];
	
	static final Matrix4f[][] textureMatrixStack = new Matrix4f[8][8];
	static final int[][] textureMatrixStackAccessSerial = new int[8][8];
	static int[] textureMatrixAccessSerial = new int[8];
	static int[] textureMatrixStackPointer = new int[8];
	
	private static final Matrix4f tmpInvertedMatrix = new Matrix4f();
	
	static {
		populateStack(modelMatrixStack);
		populateStack(projectionMatrixStack);
		populateStack(textureMatrixStack);
		populateStack(stateLightsStack);
	}
	
	static void populateStack(Matrix4f[] stack) {
		for(int i = 0; i < stack.length; ++i) {
			stack[i] = new Matrix4f();
		}
	}
	
	static void populateStack(Matrix4f[][] stack) {
		for(int i = 0; i < stack.length; ++i) {
			populateStack(stack[i]);
		}
	}
	
	static void populateStack(Vector4f[][] stack) {
		for(int i = 0; i < stack.length; ++i) {
			for(int j = 0; j < stack[i].length; ++j) {
				stack[i][j] = new Vector4f(0.0f, -1.0f, 0.0f, 0.0f);
			}
		}
	}
	
	public static final void pushLightCoords() {
		int push = stateLightsStackPointer + 1;
		if(push < stateLightsStack.length) {
			Vector4f[] copyFrom = stateLightsStack[stateLightsStackPointer];
			boolean[] copyFrom2 = stateLightsEnabled[stateLightsStackPointer];
			Vector4f[] copyTo = stateLightsStack[push];
			boolean[] copyTo2 = stateLightsEnabled[push];
			for(int i = 0; i < copyFrom.length; ++i) {
				if(copyFrom2[i]) {
					copyTo[i].set(copyFrom[i]);
					copyTo2[i] = true;
				}else {
					copyTo2[i] = false;
				}
			}
			stateLightingSerial[push] = stateLightingSerial[stateLightsStackPointer];
			stateLightsStackPointer = push;
		}else {
			Throwable t = new IndexOutOfBoundsException("GL_LIGHT direction stack overflow!" +
					" Exceeded " + stateLightsStack.length + " calls to GlStateManager.pushLightCoords");
			logger.error(t);
		}
	}

	public static final void popLightCoords() {
		if(stateLightsStackPointer > 0) {
			--stateLightsStackPointer;
		}else {
			Throwable t = new IndexOutOfBoundsException("GL_LIGHT direction stack underflow!" +
					" Called GlStateManager.popLightCoords on an empty light stack");
			logger.error(t);
		}
	}

	public static final void disableAlpha() {
		stateAlphaTest = false;
	}

	public static final void enableAlpha() {
		stateAlphaTest = true;
	}

	public static final void alphaFunc(int func, float ref) {
		if(func != GL_GREATER) {
			throw new UnsupportedOperationException("Only GL_GREATER alphaFunc is supported");
		}else {
			stateAlphaTestRef = ref;
		}
	}

	public static final void enableLighting() {
		stateLighting = true;
	}

	public static final void disableLighting() {
		stateLighting = false;
	}
	
	private static final Vector4f paramVector4 = new Vector4f();
	public static final void enableMCLight(int light, float diffuse, double dirX,
			double dirY, double dirZ, double dirW) {
		paramVector4.x = (float)dirX;
		paramVector4.y = (float)dirY;
		paramVector4.z = (float)dirZ;
		paramVector4.w = (float)dirW;
		Matrix4f.transform(modelMatrixStack[modelMatrixStackPointer], paramVector4, paramVector4);
		paramVector4.normalise();
		Vector4f dest = stateLightsStack[stateLightsStackPointer][light];
		dest.x = paramVector4.x;
		dest.y = paramVector4.y;
		dest.z = paramVector4.z;
		dest.w = diffuse;
		stateLightsEnabled[stateLightsStackPointer][light] = true;
		++stateLightingSerial[stateLightsStackPointer];
	}

	public static final void disableMCLight(int light) {
		stateLightsEnabled[stateLightsStackPointer][light] = false;
		++stateLightingSerial[stateLightsStackPointer];
	}
	
	public static final void setMCLightAmbient(float r, float g, float b) {
		stateLightingAmbientR = r;
		stateLightingAmbientG = g;
		stateLightingAmbientB = b;
		++stateLightingAmbientSerial;
	}

	public static final void enableColorMaterial() {
		stateMaterial = true;
	}

	public static final void disableColorMaterial() {
		stateMaterial = false;
	}

	public static final void disableDepth() {
		if(stateDepthTest) {
			_wglDisable(GL_DEPTH_TEST);
			stateDepthTest = false;
		}
	}

	public static final void enableDepth() {
		if(!stateDepthTest) {
			_wglEnable(GL_DEPTH_TEST);
			stateDepthTest = true;
		}
	}

	public static final void depthFunc(int depthFunc) {
		int rev = depthFunc;
		switch(depthFunc) {
		case GL_GREATER:
			rev = GL_LESS;
			break;
		case GL_GEQUAL:
			rev = GL_LEQUAL;
			break;
		case GL_EQUAL:
			rev = GL_EQUAL;
			break;
		case GL_LEQUAL:
			rev = GL_GEQUAL;
			break;
		case GL_LESS:
			rev = GL_GREATER;
			break;
		}
		if(rev != stateDepthFunc) {
			_wglDepthFunc(rev);
			stateDepthFunc = rev;
		}
	}

	public static final void depthMask(boolean flagIn) {
		if(flagIn != stateDepthMask) {
			_wglDepthMask(flagIn);
			stateDepthMask = flagIn;
		}
	}

	public static final void disableBlend() {
		if(stateBlend) {
			_wglDisable(GL_BLEND);
			stateBlend = false;
		}
	}

	public static final void enableBlend() {
		if(!stateBlend) {
			_wglEnable(GL_BLEND);
			stateBlend = true;
		}
	}

	public static final void blendFunc(int srcFactor, int dstFactor) {
		if(stateEnableOverlayFramebufferBlending) {
			tryBlendFuncSeparate(srcFactor, dstFactor, 0, 1);
			return;
		}
		int srcBits = (srcFactor | (srcFactor << 16));
		int dstBits = (dstFactor | (dstFactor << 16));
		if(srcBits != stateBlendSRC || dstBits != stateBlendDST) {
			_wglBlendFunc(srcFactor, dstFactor);
			stateBlendSRC = srcBits;
			stateBlendDST = dstBits;
		}
	}

	public static final void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
		if(stateEnableOverlayFramebufferBlending) { // game overlay framebuffer in EntityRenderer.java
			srcFactorAlpha = GL_ONE;
			dstFactorAlpha = GL_ONE_MINUS_SRC_ALPHA;
		}
		int srcBits = (srcFactor | (srcFactorAlpha << 16));
		int dstBits = (dstFactor | (dstFactorAlpha << 16));
		if(srcBits != stateBlendSRC || dstBits != stateBlendDST) {
			_wglBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
			stateBlendSRC = srcBits;
			stateBlendDST = dstBits;
		}
	}

	public static final void enableOverlayFramebufferBlending() {
		stateEnableOverlayFramebufferBlending = true;
	}

	public static final void disableOverlayFramebufferBlending() {
		stateEnableOverlayFramebufferBlending = false;
	}

	public static final void setShaderBlendSrc(float r, float g, float b, float a) {
		stateShaderBlendSrcColorR = r;
		stateShaderBlendSrcColorG = g;
		stateShaderBlendSrcColorB = b;
		stateShaderBlendSrcColorA = a;
		++stateShaderBlendColorSerial;
	}

	public static final void setShaderBlendAdd(float r, float g, float b, float a) {
		stateShaderBlendAddColorR = r;
		stateShaderBlendAddColorG = g;
		stateShaderBlendAddColorB = b;
		stateShaderBlendAddColorA = a;
		++stateShaderBlendColorSerial;
	}

	public static final void enableShaderBlendAdd() {
		stateEnableShaderBlendColor = true;
	}

	public static final void disableShaderBlendAdd() {
		stateEnableShaderBlendColor = false;
	}

	public static final void enableFog() {
		stateFog = true;
	}

	public static final void disableFog() {
		stateFog = false;
	}

	public static final void setFog(int param) {
		stateFogEXP = param == GL_EXP;
		++stateFogSerial;
	}

	public static final void setFogDensity(float param) {
		stateFogDensity = param;
		++stateFogSerial;
	}

	public static final void setFogStart(float param) {
		stateFogStart = param;
		++stateFogSerial;
	}

	public static final void setFogEnd(float param) {
		stateFogEnd = param;
		++stateFogSerial;
	}

	public static final void enableCull() {
		if(!stateCull) {
			_wglEnable(GL_CULL_FACE);
			stateCull = true;
		}
	}

	public static final void disableCull() {
		if(stateCull) {
			_wglDisable(GL_CULL_FACE);
			stateCull = false;
		}
	}

	public static final void cullFace(int mode) {
		if(stateCullFace != mode) {
			_wglCullFace(mode);
			stateCullFace = mode;
		}
	}

	public static final void enablePolygonOffset() {
		if(!statePolygonOffset) {
			_wglEnable(GL_POLYGON_OFFSET_FILL);
			statePolygonOffset = true;
		}
	}

	public static final void disablePolygonOffset() {
		if(statePolygonOffset) {
			_wglDisable(GL_POLYGON_OFFSET_FILL);
			statePolygonOffset = false;
		}
	}

	public static final void doPolygonOffset(float factor, float units) {
		if(factor != statePolygonOffsetFactor || units != statePolygonOffsetUnits) {
			_wglPolygonOffset(-factor, units);
			statePolygonOffsetFactor = factor;
			statePolygonOffsetUnits = units;
		}
	}

	public static final void enableColorLogic() {
		System.err.println("TODO: rewrite text field cursor to use blending");
	}

	public static final void disableColorLogic() {
		
	}

	public static final void colorLogicOp(int opcode) {
		
	}

	public static final void enableTexGen() {
		stateTexGen = true;
	}

	public static final void disableTexGen() {
		stateTexGen = false;
	}

	public static final void texGen(GlStateManager.TexGen coord, int source) {
		coord.source = source;
		++stateTexGenSerial;
	}

	public static final void func_179105_a(GlStateManager.TexGen coord, int plane, FloatBuffer vector) {
		coord.plane = plane;
		coord.vector.load(vector);
		if(plane == GL_EYE_PLANE) {
			tmpInvertedMatrix.load(GlStateManager.modelMatrixStack[GlStateManager.modelMatrixStackPointer]).invert().transpose();
			Matrix4f.transform(tmpInvertedMatrix, coord.vector, coord.vector);
		}
		++stateTexGenSerial;
	}

	public static final void setActiveTexture(int texture) {
		int textureIdx = texture - GL_TEXTURE0;
		if(textureIdx != activeTexture) {
			_wglActiveTexture(texture);
			activeTexture = textureIdx;
		}
	}

	public static final void enableTexture2D() {
		stateTexture[activeTexture] = true;
	}

	public static final void disableTexture2D() {
		stateTexture[activeTexture] = false;
	}
	
	public static final void texCoords2D(float x, float y) {
		textureCoordsX[activeTexture] = x;
		textureCoordsY[activeTexture] = y;
		++textureCoordsAccessSerial[activeTexture];
	}

	public static final int generateTexture() {
		return EaglercraftGPU.mapTexturesGL.register(_wglGenTextures());
	}

	public static final void deleteTexture(int texture) {
		_wglDeleteTextures(EaglercraftGPU.mapTexturesGL.free(texture));
	}

	public static final void bindTexture(int texture) {
		if(texture != boundTexture[activeTexture]) {
			_wglBindTexture(GL_TEXTURE_2D, EaglercraftGPU.mapTexturesGL.get(texture));
			boundTexture[activeTexture] = texture;
		}
	}

	public static final int getTextureBound() {
		return boundTexture[activeTexture];
	}

	public static final void shadeModel(int mode) {

	}

	public static final void enableRescaleNormal() {
		// still not sure what this is for
	}

	public static final void disableRescaleNormal() {
		
	}

	public static final void viewport(int x, int y, int w, int h) {
		if(viewportX != x || viewportY != y || viewportW != w || viewportH != h) {
			_wglViewport(x, y, w, h);
			viewportX = x;
			viewportY = y;
			viewportW = w;
			viewportH = h;
		}
	}

	public static final void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		int bits = (red ? 1 : 0) | (green ? 2 : 0) | (blue ? 4 : 0) | (alpha ? 8 : 0);
		if(bits != colorMaskBits) {
			_wglColorMask(red, green, blue, alpha);
			colorMaskBits = bits;
		}
	}

	public static final void clearDepth(float depth) {
		depth = 1.0f - depth;
		if(depth != clearDepth) {
			_wglClearDepth(depth);
			clearDepth = depth;
		}
	}

	public static final void clearColor(float red, float green, float blue, float alpha) {
		if(red != clearColorR || green != clearColorG || blue != clearColorB || alpha != clearColorA) {
			_wglClearColor(red, green, blue, alpha);
			clearColorR = red;
			clearColorG = green;
			clearColorB = blue;
			clearColorA = alpha;
		}
	}

	public static final void clear(int mask) {
		_wglClear(mask);
	}

	public static final void matrixMode(int mode) {
		stateMatrixMode = mode;
	}

	public static final void loadIdentity() {
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			modelMatrixStack[modelMatrixStackPointer].setIdentity();
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			projectionMatrixStack[projectionMatrixStackPointer].setIdentity();
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]].setIdentity();
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
	}

	public static final void pushMatrix() {
		int push;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			push = modelMatrixStackPointer + 1;
			if(push < modelMatrixStack.length) {
				modelMatrixStack[push].load(modelMatrixStack[modelMatrixStackPointer]);
				modelMatrixStackAccessSerial[push] = modelMatrixStackAccessSerial[modelMatrixStackPointer];
				modelMatrixStackPointer = push;
			}else {
				Throwable t = new IndexOutOfBoundsException("GL_MODELVIEW matrix stack overflow!" +
						" Exceeded " + modelMatrixStack.length + " calls to GlStateManager.pushMatrix");
				logger.error(t);
			}
			break;
		case GL_PROJECTION:
			push = projectionMatrixStackPointer + 1;
			if(push < projectionMatrixStack.length) {
				projectionMatrixStack[push].load(projectionMatrixStack[projectionMatrixStackPointer]);
				projectionMatrixStackAccessSerial[push] = projectionMatrixStackAccessSerial[projectionMatrixStackPointer];
				projectionMatrixStackPointer = push;
			}else {
				Throwable t = new IndexOutOfBoundsException("GL_PROJECTION matrix stack overflow!" +
						" Exceeded " + projectionMatrixStack.length + " calls to GlStateManager.pushMatrix");
				logger.error(t);
			}
			break;
		case GL_TEXTURE:
			push = textureMatrixStackPointer[activeTexture] + 1;
			if(push < textureMatrixStack.length) {
				int ptr = textureMatrixStackPointer[activeTexture];
				textureMatrixStack[activeTexture][push].load(textureMatrixStack[activeTexture][ptr]);
				textureMatrixStackAccessSerial[activeTexture][push] = textureMatrixStackAccessSerial[activeTexture][ptr];
				textureMatrixStackPointer[activeTexture] = push;
			}else {
				Throwable t = new IndexOutOfBoundsException("GL_TEXTURE #" + activeTexture + " matrix stack overflow!" +
						" Exceeded " + textureMatrixStack.length + " calls to GlStateManager.pushMatrix");
				logger.error(t);
			}
			break;
		}
	}

	public static final void popMatrix() {
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			if(modelMatrixStackPointer > 0) {
				--modelMatrixStackPointer;
			}else {
				Throwable t = new IndexOutOfBoundsException("GL_MODELVIEW matrix stack underflow!" +
						" Called GlStateManager.popMatrix on an empty matrix stack");
				logger.error(t);
			}
			break;
		case GL_PROJECTION:
			if(projectionMatrixStackPointer > 0) {
				--projectionMatrixStackPointer;
			}else {
				Throwable t = new IndexOutOfBoundsException("GL_PROJECTION matrix stack underflow!" +
						" Called GlStateManager.popMatrix on an empty matrix stack");
				logger.error(t);
			}
			break;
		case GL_TEXTURE:
			if(textureMatrixStackPointer[activeTexture] > 0) {
				--textureMatrixStackPointer[activeTexture];
			}else {
				Throwable t = new IndexOutOfBoundsException("GL_TEXTURE #" + activeTexture +
						" matrix stack underflow!  Called GlStateManager.popMatrix on an empty matrix stack");
				logger.error(t);
			}
			break;
		}
	}

	public static final void getFloat(int pname, float[] params) {
		switch(pname) {
		case GL_MODELVIEW_MATRIX:
			modelMatrixStack[modelMatrixStackPointer].store(params);
			break;
		case GL_PROJECTION_MATRIX:
			projectionMatrixStack[projectionMatrixStackPointer].store(params);
			break;
		case GL_TEXTURE_MATRIX:
			textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]].store(params);
			break;
		default:
			throw new UnsupportedOperationException("glGetFloat can only be used to retrieve matricies!");
		}
	}

	public static final void ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
		Matrix4f matrix;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
			matrix = modelMatrixStack[modelMatrixStackPointer];
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
		default:
			matrix = projectionMatrixStack[projectionMatrixStackPointer];
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			matrix = textureMatrixStack[activeTexture][ptr];
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
		matrix.m00 = 2.0f / (float)(right - left);
		matrix.m01 = 0.0f;
		matrix.m02 = 0.0f;
		matrix.m03 = 0.0f;
		matrix.m10 = 0.0f;
		matrix.m11 = 2.0f / (float)(top - bottom);
		matrix.m12 = 0.0f;
		matrix.m13 = 0.0f;
		matrix.m20 = 0.0f;
		matrix.m21 = 0.0f;
		matrix.m22 = 2.0f / (float)(zFar - zNear);
		matrix.m23 = 0.0f;
		matrix.m30 = (float)(-(right + left) / (right - left));
		matrix.m31 = (float)(-(top + bottom) / (top - bottom));
		matrix.m32 = (float)((zFar + zNear) / (zFar - zNear));
		matrix.m33 = 1.0f;
	}

	private static final Vector3f paramVector = new Vector3f();
	private static final float toRad = 0.0174532925f;
	public static final void rotate(float angle, float x, float y, float z) {
		paramVector.x = x;
		paramVector.y = y;
		paramVector.z = z;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			modelMatrixStack[modelMatrixStackPointer].rotate(angle * toRad, paramVector);
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			projectionMatrixStack[projectionMatrixStackPointer].rotate(angle * toRad, paramVector);
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			textureMatrixStack[activeTexture][ptr].rotate(angle * toRad, paramVector);
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
	}

	public static final void scale(float x, float y, float z) {
		paramVector.x = x;
		paramVector.y = y;
		paramVector.z = z;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			modelMatrixStack[modelMatrixStackPointer].scale(paramVector);
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			projectionMatrixStack[projectionMatrixStackPointer].scale(paramVector);
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			textureMatrixStack[activeTexture][ptr].scale(paramVector);
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
	}

	public static final void scale(double x, double y, double z) {
		paramVector.x = (float)x;
		paramVector.y = (float)y;
		paramVector.z = (float)z;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			modelMatrixStack[modelMatrixStackPointer].scale(paramVector);
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			projectionMatrixStack[projectionMatrixStackPointer].scale(paramVector);
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			textureMatrixStack[activeTexture][ptr].scale(paramVector);
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
	}

	public static final void translate(float x, float y, float z) {
		paramVector.x = x;
		paramVector.y = y;
		paramVector.z = z;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			modelMatrixStack[modelMatrixStackPointer].translate(paramVector);
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			projectionMatrixStack[projectionMatrixStackPointer].translate(paramVector);
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			textureMatrixStack[activeTexture][ptr].translate(paramVector);
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
	}

	public static final void translate(double x, double y, double z) {
		paramVector.x = (float)x;
		paramVector.y = (float)y;
		paramVector.z = (float)z;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			modelMatrixStack[modelMatrixStackPointer].translate(paramVector);
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			projectionMatrixStack[projectionMatrixStackPointer].translate(paramVector);
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			textureMatrixStack[activeTexture][ptr].translate(paramVector);
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
	}

	private static final Matrix4f paramMatrix = new Matrix4f();
	public static final void multMatrix(float[] matrix) {
		Matrix4f modeMatrix;
		
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
		default:
			modeMatrix = modelMatrixStack[modelMatrixStackPointer];
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			modeMatrix = projectionMatrixStack[projectionMatrixStackPointer];
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			modeMatrix = textureMatrixStack[activeTexture][ptr];
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
		
		paramMatrix.load(matrix);
		
		Matrix4f.mul(modeMatrix, paramMatrix, modeMatrix);
	}

	public static final void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
		stateColorR = colorRed;
		stateColorG = colorGreen;
		stateColorB = colorBlue;
		stateColorA = colorAlpha;
		++stateColorSerial;
	}

	public static final void color(float colorRed, float colorGreen, float colorBlue) {
		stateColorR = colorRed;
		stateColorG = colorGreen;
		stateColorB = colorBlue;
		stateColorA = 1.0f;
		++stateColorSerial;
	}

	public static final void resetColor() {
		stateColorR = 1.0f;
		stateColorG = 1.0f;
		stateColorB = 1.0f;
		stateColorA = 1.0f;
		++stateColorSerial;
	}

	public static final void callList(int list) {
		EaglercraftGPU.glCallList(list);
	}

	public static final void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
		Matrix4f matrix;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
			matrix = modelMatrixStack[modelMatrixStackPointer];
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
		default:
			matrix = projectionMatrixStack[projectionMatrixStackPointer];
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			int ptr = textureMatrixStackPointer[activeTexture];
			matrix = textureMatrixStack[activeTexture][ptr];
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] =
					++textureMatrixAccessSerial[activeTexture];
			break;
		}
		float cotangent = (float) Math.cos(fovy * toRad * 0.5f) / (float) Math.sin(fovy * toRad * 0.5f);
		matrix.m00 = cotangent / aspect;
		matrix.m01 = 0.0f;
		matrix.m02 = 0.0f;
		matrix.m03 = 0.0f;
		matrix.m10 = 0.0f;
		matrix.m11 = cotangent;
		matrix.m12 = 0.0f;
		matrix.m13 = 0.0f;
		matrix.m20 = 0.0f;
		matrix.m21 = 0.0f;
		matrix.m22 = (zFar + zNear) / (zFar - zNear);
		matrix.m23 = -1.0f;
		matrix.m30 = 0.0f;
		matrix.m31 = 0.0f;
		matrix.m32 = 2.0f * zFar * zNear / (zFar - zNear);
		matrix.m33 = 0.0f;
	}

	private static final Matrix4f unprojA = new Matrix4f();
	private static final Matrix4f unprojB = new Matrix4f();
	private static final Vector4f unprojC = new Vector4f();
	public static final void gluUnProject(float p1, float p2, float p3, float[] modelview, float[] projection,
			int[] viewport, float[] objectcoords) {
		unprojA.load(modelview);
		unprojB.load(projection);
		Matrix4f.mul(unprojA, unprojB, unprojB);
		unprojB.invert();
		unprojC.set(((p1 - (float)viewport[0]) / (float)viewport[2]) * 2f - 1f,
				((p2 - (float)viewport[1]) / (float)viewport[3]) * 2f - 1f, p3, 1.0f);
		Matrix4f.transform(unprojB, unprojC, unprojC);
		objectcoords[0] = unprojC.x / unprojC.w;
		objectcoords[1] = unprojC.y / unprojC.w;
		objectcoords[2] = unprojC.z / unprojC.w;
	}

	public static void recompileShaders() {
		FixedFunctionPipeline.flushCache();
	}
}
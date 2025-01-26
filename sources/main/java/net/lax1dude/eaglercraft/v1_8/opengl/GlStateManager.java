package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.ITextureGL;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.minecraft.util.MathHelper;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

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
public class GlStateManager {
	
	static final Logger logger = LogManager.getLogger("GlStateManager");

	static boolean stateDepthTest = false;
	static boolean stateDepthTestStash = false;
	static int stateDepthFunc = -1;
	static boolean stateDepthMask = true;

	static boolean stateCull = false;
	static boolean stateCullStash = false;
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
	static boolean stateBlendStash = false;
	static boolean stateGlobalBlend = true;
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

	static float blendConstantR = -999.0f;
	static float blendConstantG = -999.0f;
	static float blendConstantB = -999.0f;
	static float blendConstantA = -999.0f;

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
	static final int[] textureMatrixAccessSerial = new int[8];
	static final int[] textureMatrixStackPointer = new int[8];
	
	static boolean stateUseExtensionPipeline = false;
	
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

	public static final void enableExtensionPipeline() {
		stateUseExtensionPipeline = true;
	}

	public static final void disableExtensionPipeline() {
		stateUseExtensionPipeline = false;
	}

	public static final boolean isExtensionPipeline() {
		return stateUseExtensionPipeline;
	}

	private static final Vector4f paramVector4 = new Vector4f();
	public static final void enableMCLight(int light, float diffuse, double dirX,
			double dirY, double dirZ, double dirW) {
		if(dirW != 0.0) throw new IllegalArgumentException("dirW must be 0.0!");
		paramVector4.x = (float)dirX;
		paramVector4.y = (float)dirY;
		paramVector4.z = (float)dirZ;
		paramVector4.w = (float)0.0f;
		Matrix4f.transform(modelMatrixStack[modelMatrixStackPointer], paramVector4, paramVector4);
		Vector4f dest = stateLightsStack[stateLightsStackPointer][light];
		float len = MathHelper.sqrt_float(paramVector4.x * paramVector4.x
				+ paramVector4.y * paramVector4.y
				+ paramVector4.z * paramVector4.z);
		dest.x = paramVector4.x / len;
		dest.y = paramVector4.y / len;
		dest.z = paramVector4.z / len;
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

	public static final void eagPushStateForGLES2BlitHack() {
		stateDepthTestStash = stateDepthTest;
		stateCullStash = stateCull;
		stateBlendStash = stateBlend;
	}

	public static final void eagPopStateForGLES2BlitHack() {
		if(stateDepthTestStash) {
			enableDepth();
		}else {
			disableDepth();
		}
		if(stateCullStash) {
			enableCull();
		}else {
			disableCull();
		}
		if(stateBlendStash) {
			enableBlend();
		}else {
			disableBlend();
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
			if(stateGlobalBlend) _wglDisable(GL_BLEND);
			stateBlend = false;
		}
	}

	public static final void enableBlend() {
		if(!stateBlend) {
			if(stateGlobalBlend) _wglEnable(GL_BLEND);
			stateBlend = true;
		}
	}

	public static final void globalDisableBlend() {
		if(stateBlend) {
			_wglDisable(GL_BLEND);
		}
		stateGlobalBlend = false;
	}

	public static final void globalEnableBlend() {
		if(stateBlend) {
			_wglEnable(GL_BLEND);
		}
		stateGlobalBlend = true;
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

	public static final void setBlendConstants(float r, float g, float b, float a) {
		if(r != blendConstantR || g != blendConstantG || b != blendConstantB || a != blendConstantA) {
			_wglBlendColor(r, g, b, a);
			blendConstantR = r;
			blendConstantG = g;
			blendConstantB = b;
			blendConstantA = a;
		}
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
		throw new UnsupportedOperationException("Color logic op is not supported in OpenGL ES!");
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
	
	public static final void texCoords2DDirect(int tex, float x, float y) {
		textureCoordsX[tex] = x;
		textureCoordsY[tex] = y;
		++textureCoordsAccessSerial[tex];
	}

	public static final float getTexCoordX(int tex) {
		return textureCoordsX[tex];
	}

	public static final float getTexCoordY(int tex) {
		return textureCoordsY[tex];
	}

	public static final int generateTexture() {
		return EaglercraftGPU.mapTexturesGL.register(_wglGenTextures());
	}

	public static final void deleteTexture(int texture) {
		unbindTextureIfCached(texture);
		_wglDeleteTextures(EaglercraftGPU.mapTexturesGL.free(texture));
	}

	static final void unbindTextureIfCached(int texture) {
		boolean f1, f2 = false;
		for(int i = 0; i < boundTexture.length; ++i) {
			if(boundTexture[i] == texture) {
				f1 = i != activeTexture;
				if(f2 || f1) {
					_wglActiveTexture(GL_TEXTURE0 + i);
					f2 = f1;
				}
				_wglBindTexture(GL_TEXTURE_2D, null);
				if(EaglercraftGPU.checkOpenGLESVersion() >= 300) {
					_wglBindTexture(GL_TEXTURE_3D, null);
				}
				boundTexture[i] = -1;
			}
		}
		if(f2) {
			_wglActiveTexture(GL_TEXTURE0 + activeTexture);
		}
	}

	public static final void bindTexture(int texture) {
		if(texture != boundTexture[activeTexture]) {
			_wglBindTexture(GL_TEXTURE_2D, EaglercraftGPU.mapTexturesGL.get(texture));
			boundTexture[activeTexture] = texture;
		}
	}

	public static final void bindTexture3D(int texture) {
		if(texture != boundTexture[activeTexture]) {
			_wglBindTexture(GL_TEXTURE_3D, EaglercraftGPU.mapTexturesGL.get(texture));
			boundTexture[activeTexture] = texture;
		}
	}

	public static final void quickBindTexture(int unit, int texture) {
		int unitBase = unit - GL_TEXTURE0;
		if(texture != boundTexture[unitBase]) {
			if(unitBase != activeTexture) {
				_wglActiveTexture(unit);
			}
			_wglBindTexture(GL_TEXTURE_2D, EaglercraftGPU.mapTexturesGL.get(texture));
			boundTexture[unitBase] = texture;
			if(unitBase != activeTexture) {
				_wglActiveTexture(GL_TEXTURE0 + activeTexture);
			}
		}
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

	private static Matrix4f getMatrixIncr() {
		Matrix4f mat;
		int _i, _j;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
			_j = modelMatrixStackPointer;
			mat = modelMatrixStack[_j];
			modelMatrixStackAccessSerial[_j] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
			_j = projectionMatrixStackPointer;
			mat = projectionMatrixStack[_j];
			projectionMatrixStackAccessSerial[_j] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			_i = activeTexture;
			_j = textureMatrixStackPointer[_i];
			mat = textureMatrixStack[_i][_j];
			textureMatrixStackAccessSerial[_i][_j] = ++textureCoordsAccessSerial[_i];
			break;
		default:
			throw new IllegalStateException();
		}
		return mat;
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

	public static final void getFloat(int pname, FloatBuffer params) {
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
		Matrix4f matrix = getMatrixIncr();
		paramMatrix.m00 = 2.0f / (float)(right - left);
		paramMatrix.m01 = 0.0f;
		paramMatrix.m02 = 0.0f;
		paramMatrix.m03 = 0.0f;
		paramMatrix.m10 = 0.0f;
		paramMatrix.m11 = 2.0f / (float)(top - bottom);
		paramMatrix.m12 = 0.0f;
		paramMatrix.m13 = 0.0f;
		paramMatrix.m20 = 0.0f;
		paramMatrix.m21 = 0.0f;
		paramMatrix.m22 = 2.0f / (float)(zFar - zNear);
		paramMatrix.m23 = 0.0f;
		paramMatrix.m30 = (float)(-(right + left) / (right - left));
		paramMatrix.m31 = (float)(-(top + bottom) / (top - bottom));
		paramMatrix.m32 = (float)((zFar + zNear) / (zFar - zNear));
		paramMatrix.m33 = 1.0f;
		Matrix4f.mul(matrix, paramMatrix, matrix);
	}

	private static final Vector3f paramVector = new Vector3f();
	private static final float toRad = 0.0174532925f;
	public static final void rotate(float angle, float x, float y, float z) {
		Matrix4f matrix = getMatrixIncr();
		if(x == 0.0f) {
			if(y == 0.0f) {
				if(z == 1.0f || z == -1.0f) {
					_glRotatefZ(matrix, toRad * angle * z);
					return;
				}
			}else if((y == 1.0f || y == -1.0f) && z == 0.0f) {
				_glRotatefY(matrix, toRad * angle * y);
				return;
			}
		}else if((x == 1.0f || x == -1.0f) && y == 0.0f && z == 0.0f) {
			_glRotatefX(matrix, toRad * angle * x);
			return;
		}
		_glRotatef(matrix, toRad * angle, x, y, z);
	}

	public static final void rotateXYZ(float x, float y, float z) {
		Matrix4f matrix = getMatrixIncr();
		if(x != 0.0f) _glRotatefX(matrix, toRad * x);
		if(y != 0.0f) _glRotatefY(matrix, toRad * y);
		if(z != 0.0f) _glRotatefZ(matrix, toRad * z);
	}

	public static final void rotateZYX(float x, float y, float z) {
		Matrix4f matrix = getMatrixIncr();
		if(z != 0.0f) _glRotatefZ(matrix, toRad * z);
		if(y != 0.0f) _glRotatefY(matrix, toRad * y);
		if(x != 0.0f) _glRotatefX(matrix, toRad * x);
	}

	public static final void rotateXYZRad(float x, float y, float z) {
		Matrix4f matrix = getMatrixIncr();
		if(x != 0.0f) _glRotatefX(matrix, x);
		if(y != 0.0f) _glRotatefY(matrix, y);
		if(z != 0.0f) _glRotatefZ(matrix, z);
	}

	public static final void rotateZYXRad(float x, float y, float z) {
		Matrix4f matrix = getMatrixIncr();
		if(z != 0.0f) _glRotatefZ(matrix, z);
		if(y != 0.0f) _glRotatefY(matrix, y);
		if(x != 0.0f) _glRotatefX(matrix, x);
	}

	private static void _glRotatefX(Matrix4f mat, float angle) {
		float sin = MathHelper.sin(angle);
		float cos = MathHelper.cos(angle);
		float lm10 = mat.m10, lm11 = mat.m11, lm12 = mat.m12, lm13 = mat.m13, lm20 = mat.m20, lm21 = mat.m21,
				lm22 = mat.m22, lm23 = mat.m23;
		mat.m20 = lm10 * -sin + lm20 * cos;
		mat.m21 = lm11 * -sin + lm21 * cos;
		mat.m22 = lm12 * -sin + lm22 * cos;
		mat.m23 = lm13 * -sin + lm23 * cos;
		mat.m10 = lm10 * cos + lm20 * sin;
		mat.m11 = lm11 * cos + lm21 * sin;
		mat.m12 = lm12 * cos + lm22 * sin;
		mat.m13 = lm13 * cos + lm23 * sin;
	}

	private static void _glRotatefY(Matrix4f mat, float angle) {
		float sin = MathHelper.sin(angle);
		float cos = MathHelper.cos(angle);
		float nm00 = mat.m00 * cos + mat.m20 * -sin;
		float nm01 = mat.m01 * cos + mat.m21 * -sin;
		float nm02 = mat.m02 * cos + mat.m22 * -sin;
		float nm03 = mat.m03 * cos + mat.m23 * -sin;
		mat.m20 = mat.m00 * sin + mat.m20 * cos;
		mat.m21 = mat.m01 * sin + mat.m21 * cos;
		mat.m22 = mat.m02 * sin + mat.m22 * cos;
		mat.m23 = mat.m03 * sin + mat.m23 * cos;
		mat.m00 = nm00;
		mat.m01 = nm01;
		mat.m02 = nm02;
		mat.m03 = nm03;
	}

	private static void _glRotatefZ(Matrix4f mat, float angle) {
		float dirX = MathHelper.sin(angle);
		float dirY = MathHelper.cos(angle);
		float nm00 = mat.m00 * dirY + mat.m10 * dirX;
		float nm01 = mat.m01 * dirY + mat.m11 * dirX;
		float nm02 = mat.m02 * dirY + mat.m12 * dirX;
		float nm03 = mat.m03 * dirY + mat.m13 * dirX;
		mat.m10 = mat.m00 * -dirX + mat.m10 * dirY;
		mat.m11 = mat.m01 * -dirX + mat.m11 * dirY;
		mat.m12 = mat.m02 * -dirX + mat.m12 * dirY;
		mat.m13 = mat.m03 * -dirX + mat.m13 * dirY;
		mat.m00 = nm00;
		mat.m01 = nm01;
		mat.m02 = nm02;
		mat.m03 = nm03;
	}

	private static void _glRotatef(Matrix4f mat, float angle, float x, float y, float z) {
		float s = MathHelper.sin(angle);
		float c = MathHelper.cos(angle);
		float C = 1.0f - c;
		float xx = x * x, xy = x * y, xz = x * z;
		float yy = y * y, yz = y * z;
		float zz = z * z;
		float rm00 = xx * C + c;
		float rm01 = xy * C + z * s;
		float rm02 = xz * C - y * s;
		float rm10 = xy * C - z * s;
		float rm11 = yy * C + c;
		float rm12 = yz * C + x * s;
		float rm20 = xz * C + y * s;
		float rm21 = yz * C - x * s;
		float rm22 = zz * C + c;
		float nm00 = mat.m00 * rm00 + mat.m10 * rm01 + mat.m20 * rm02;
		float nm01 = mat.m01 * rm00 + mat.m11 * rm01 + mat.m21 * rm02;
		float nm02 = mat.m02 * rm00 + mat.m12 * rm01 + mat.m22 * rm02;
		float nm03 = mat.m03 * rm00 + mat.m13 * rm01 + mat.m23 * rm02;
		float nm10 = mat.m00 * rm10 + mat.m10 * rm11 + mat.m20 * rm12;
		float nm11 = mat.m01 * rm10 + mat.m11 * rm11 + mat.m21 * rm12;
		float nm12 = mat.m02 * rm10 + mat.m12 * rm11 + mat.m22 * rm12;
		float nm13 = mat.m03 * rm10 + mat.m13 * rm11 + mat.m23 * rm12;
		mat.m20 = mat.m00 * rm20 + mat.m10 * rm21 + mat.m20 * rm22;
		mat.m21 = mat.m01 * rm20 + mat.m11 * rm21 + mat.m21 * rm22;
		mat.m22 = mat.m02 * rm20 + mat.m12 * rm21 + mat.m22 * rm22;
		mat.m23 = mat.m03 * rm20 + mat.m13 * rm21 + mat.m23 * rm22;
		mat.m00 = nm00;
		mat.m01 = nm01;
		mat.m02 = nm02;
		mat.m03 = nm03;
		mat.m10 = nm10;
		mat.m11 = nm11;
		mat.m12 = nm12;
		mat.m13 = nm13;
	}

	public static final void scale(float x, float y, float z) {
		Matrix4f matrix = getMatrixIncr();
		matrix.m00 *= x;
		matrix.m01 *= x;
		matrix.m02 *= x;
		matrix.m03 *= x;
		matrix.m10 *= y;
		matrix.m11 *= y;
		matrix.m12 *= y;
		matrix.m13 *= y;
		matrix.m20 *= z;
		matrix.m21 *= z;
		matrix.m22 *= z;
		matrix.m23 *= z;
	}

	public static final void scale(double x, double y, double z) {
		Matrix4f matrix = getMatrixIncr();
		matrix.m00 *= x;
		matrix.m01 *= x;
		matrix.m02 *= x;
		matrix.m03 *= x;
		matrix.m10 *= y;
		matrix.m11 *= y;
		matrix.m12 *= y;
		matrix.m13 *= y;
		matrix.m20 *= z;
		matrix.m21 *= z;
		matrix.m22 *= z;
		matrix.m23 *= z;
	}

	public static final void translate(float x, float y, float z) {
		Matrix4f matrix = getMatrixIncr();
		matrix.m30 = matrix.m00 * x + matrix.m10 * y + matrix.m20 * z + matrix.m30;
		matrix.m31 = matrix.m01 * x + matrix.m11 * y + matrix.m21 * z + matrix.m31;
		matrix.m32 = matrix.m02 * x + matrix.m12 * y + matrix.m22 * z + matrix.m32;
		matrix.m33 = matrix.m03 * x + matrix.m13 * y + matrix.m23 * z + matrix.m33;
	}

	public static final void translate(double x, double y, double z) {
		float _x = (float)x;
		float _y = (float)y;
		float _z = (float)z;
		Matrix4f matrix = getMatrixIncr();
		matrix.m30 = matrix.m00 * _x + matrix.m10 * _y + matrix.m20 * _z + matrix.m30;
		matrix.m31 = matrix.m01 * _x + matrix.m11 * _y + matrix.m21 * _z + matrix.m31;
		matrix.m32 = matrix.m02 * _x + matrix.m12 * _y + matrix.m22 * _z + matrix.m32;
		matrix.m33 = matrix.m03 * _x + matrix.m13 * _y + matrix.m23 * _z + matrix.m33;
	}

	private static final Matrix4f paramMatrix = new Matrix4f();
	public static final void multMatrix(float[] matrix) {
		paramMatrix.load(matrix);
		Matrix4f mat = getMatrixIncr();
		Matrix4f.mul(mat, paramMatrix, mat);
	}

	public static final void multMatrix(Matrix4f matrix) {
		Matrix4f mat = getMatrixIncr();
		Matrix4f.mul(mat, matrix, mat);
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
		Matrix4f matrix = getMatrixIncr();
		float cotangent = (float) Math.cos(fovy * toRad * 0.5f) / (float) Math.sin(fovy * toRad * 0.5f);
		paramMatrix.m00 = cotangent / aspect;
		paramMatrix.m01 = 0.0f;
		paramMatrix.m02 = 0.0f;
		paramMatrix.m03 = 0.0f;
		paramMatrix.m10 = 0.0f;
		paramMatrix.m11 = cotangent;
		paramMatrix.m12 = 0.0f;
		paramMatrix.m13 = 0.0f;
		paramMatrix.m20 = 0.0f;
		paramMatrix.m21 = 0.0f;
		paramMatrix.m22 = (zFar + zNear) / (zFar - zNear);
		paramMatrix.m23 = -1.0f;
		paramMatrix.m30 = 0.0f;
		paramMatrix.m31 = 0.0f;
		paramMatrix.m32 = 2.0f * zFar * zNear / (zFar - zNear);
		paramMatrix.m33 = 0.0f;
		Matrix4f.mul(matrix, paramMatrix, matrix);
	}

	public static final void gluLookAt(Vector3f eye, Vector3f center, Vector3f up) {
		Matrix4f matrix = getMatrixIncr();
		float x = center.x - eye.x;
		float y = center.y - eye.y;
		float z = center.z - eye.z;
		float xyzLen = (float) Math.sqrt(x * x + y * y + z * z);
		x /= xyzLen;
		y /= xyzLen;
		z /= xyzLen;
		float ux = up.x;
		float uy = up.y;
		float uz = up.z;
		xyzLen = (float) Math.sqrt(ux * ux + uy * uy + uz * uz);
		ux /= xyzLen;
		uy /= xyzLen;
		uz /= xyzLen;
		float lxx = y * uz - z * uy;
		float lxy = ux * z - uz * x;
		float lxz = x * uy - y * ux;
		float lyx = lxy * z - lxz * y;
		float lyy = x * lxz - z * lxx;
		float lyz = lxx * y - lxy * x;
		paramMatrix.m00 = lxx;
		paramMatrix.m01 = lyx;
		paramMatrix.m02 = -x;
		paramMatrix.m03 = 0.0f;
		paramMatrix.m10 = lxy;
		paramMatrix.m11 = lyy;
		paramMatrix.m12 = -y;
		paramMatrix.m13 = 0.0f;
		paramMatrix.m20 = lxz;
		paramMatrix.m21 = lyz;
		paramMatrix.m22 = -z;
		paramMatrix.m23 = 0.0f;
		paramMatrix.m30 = -eye.x;
		paramMatrix.m31 = -eye.y;
		paramMatrix.m32 = -eye.z;
		paramMatrix.m33 = 1.0f;
		Matrix4f.mul(matrix, paramMatrix, matrix);
	}

	public static final void transform(Vector4f vecIn, Vector4f vecOut) {
		Matrix4f matrix;
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
			matrix = modelMatrixStack[modelMatrixStackPointer];
			break;
		case GL_PROJECTION:
		default:
			matrix = projectionMatrixStack[projectionMatrixStackPointer];
			break;
		case GL_TEXTURE:
			matrix = textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]];
			break;
		}
		Matrix4f.transform(matrix, vecIn, vecOut);
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

	public static final void getMatrix(Matrix4f mat) {
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
			mat.load(modelMatrixStack[modelMatrixStackPointer]);
			break;
		case GL_PROJECTION:
		default:
			mat.load(projectionMatrixStack[projectionMatrixStackPointer]);
			break;
		case GL_TEXTURE:
			mat.load(textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]]);
			break;
		}
	}

	public static final void loadMatrix(Matrix4f mat) {
		switch(stateMatrixMode) {
		case GL_MODELVIEW:
			modelMatrixStack[modelMatrixStackPointer].load(mat);
			modelMatrixStackAccessSerial[modelMatrixStackPointer] = ++modelMatrixAccessSerial;
			break;
		case GL_PROJECTION:
		default:
			projectionMatrixStack[projectionMatrixStackPointer].load(mat);
			projectionMatrixStackAccessSerial[projectionMatrixStackPointer] = ++projectionMatrixAccessSerial;
			break;
		case GL_TEXTURE:
			textureMatrixStack[activeTexture][textureMatrixStackPointer[activeTexture]].load(mat);
			textureMatrixStackAccessSerial[activeTexture][textureMatrixStackPointer[activeTexture]] = ++textureMatrixAccessSerial[activeTexture];
			break;
		}
	}

	public static final int getModelViewSerial() {
		return modelMatrixStackAccessSerial[modelMatrixStackPointer];
	}

	public static final Matrix4f getModelViewReference() {
		return modelMatrixStack[modelMatrixStackPointer];
	}

	public static final Matrix4f getProjectionReference() {
		return projectionMatrixStack[projectionMatrixStackPointer];
	}

	public static void recompileShaders() {
		FixedFunctionPipeline.flushCache();
	}

	public static int getBoundTexture() {
		return boundTexture[activeTexture];
	}

	static void setTextureCachedSize(int target, int w, int h) {
		if(target == GL_TEXTURE_2D) {
			ITextureGL tex = EaglercraftGPU.getNativeTexture(boundTexture[activeTexture]);
			if(tex != null) {
				tex.setCacheSize(w, h);
			}
		}
	}

}
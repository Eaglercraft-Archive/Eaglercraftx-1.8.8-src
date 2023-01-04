package net.lax1dude.eaglercraft.v1_8.opengl;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionConstants;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.minecraft.util.MathHelper;

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
class FixedFunctionPipeline {
	
	private static final Logger LOGGER = LogManager.getLogger("FixedFunctionPipeline");

	static final int STATE_HAS_ATTRIB_TEXTURE = 1;
	static final int STATE_HAS_ATTRIB_COLOR = 2;
	static final int STATE_HAS_ATTRIB_NORMAL = 4;
	static final int STATE_HAS_ATTRIB_LIGHTMAP = 8;
	static final int STATE_ENABLE_TEXTURE2D = 16;
	static final int STATE_ENABLE_LIGHTMAP = 32;
	static final int STATE_ENABLE_ALPHA_TEST = 64;
	static final int STATE_ENABLE_MC_LIGHTING = 128;
	static final int STATE_ENABLE_END_PORTAL = 256;
	static final int STATE_ENABLE_ANISOTROPIC_FIX = 512;
	static final int STATE_ENABLE_FOG = 1024;
	static final int STATE_ENABLE_BLEND_ADD = 2048;
	
	static final int getFragmentState() {
		return (GlStateManager.stateTexture[0] ? STATE_ENABLE_TEXTURE2D : 0) |
				(GlStateManager.stateTexture[1] ? STATE_ENABLE_LIGHTMAP : 0) |
				(GlStateManager.stateAlphaTest ? STATE_ENABLE_ALPHA_TEST : 0) |
				((GlStateManager.stateLighting && GlStateManager.stateMaterial)
						? STATE_ENABLE_MC_LIGHTING : 0) |
				((GlStateManager.stateTexture[0] && GlStateManager.stateTexGen)
						? STATE_ENABLE_END_PORTAL : 0) |
				/* TODO: (GlStateManager.??? ? STATE_ENABLE_ANISOTROPIC_FIX : 0) | */
				((GlStateManager.stateFog && GlStateManager.stateFogDensity > 0.0f)
						? STATE_ENABLE_FOG : 0) |
				(GlStateManager.stateEnableShaderBlendColor ? STATE_ENABLE_BLEND_ADD : 0);
	}
	
	static FixedFunctionPipeline setupDirect(ByteBuffer buffer, int attrib) {
		FixedFunctionPipeline self = getPipelineInstance(attrib | getFragmentState());
		
		EaglercraftGPU.bindGLBufferArray(self.vertexArray);
		EaglercraftGPU.bindGLArrayBuffer(self.vertexBuffer);
		
		int wantSize = buffer.remaining();
		if(self.vertexBufferSize < wantSize) {
			int newSize = (wantSize & 0xFFFFF000) + 0x2000;
			_wglBufferData(GL_ARRAY_BUFFER, newSize, GL_DYNAMIC_DRAW);
			self.vertexBufferSize = newSize;
		}
		
		_wglBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		
		return self;
	}
	
	static void setupDisplayList(DisplayList list) {
		FixedFunctionPipeline self = getPipelineInstance(list.attribs | getFragmentState());
		
		EaglercraftGPU.bindGLBufferArray(list.vertexArray);
		EaglercraftGPU.bindGLArrayBuffer(list.vertexBuffer);
		
		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, VertexFormat.COMPONENT_POSITION_SIZE,
				VertexFormat.COMPONENT_POSITION_FORMAT, false, self.attribStride, 0);

		if(self.attribTextureIndex != -1) {
			_wglEnableVertexAttribArray(self.attribTextureIndex);
			_wglVertexAttribPointer(self.attribTextureIndex, VertexFormat.COMPONENT_TEX_SIZE,
					VertexFormat.COMPONENT_TEX_FORMAT, false, self.attribStride, self.attribTextureOffset);
		}
		
		if(self.attribColorIndex != -1) {
			_wglEnableVertexAttribArray(self.attribColorIndex);
			_wglVertexAttribPointer(self.attribColorIndex, VertexFormat.COMPONENT_COLOR_SIZE,
					VertexFormat.COMPONENT_COLOR_FORMAT, true, self.attribStride, self.attribColorOffset);
		}
		
		if(self.attribNormalIndex != -1) {
			_wglEnableVertexAttribArray(self.attribNormalIndex);
			_wglVertexAttribPointer(self.attribNormalIndex, VertexFormat.COMPONENT_NORMAL_SIZE,
					VertexFormat.COMPONENT_NORMAL_FORMAT, true, self.attribStride, self.attribNormalOffset);
		}
		
		if(self.attribLightmapIndex != -1) {
			_wglEnableVertexAttribArray(self.attribLightmapIndex);
			_wglVertexAttribPointer(self.attribLightmapIndex, VertexFormat.COMPONENT_LIGHTMAP_SIZE,
					VertexFormat.COMPONENT_LIGHTMAP_FORMAT, false, self.attribStride, self.attribLightmapOffset);
		}
		
	}
	
	static FixedFunctionPipeline setupRenderDisplayList(int attribs) {
		return getPipelineInstance(attribs | getFragmentState());
	}
	
	void drawArrays(int mode, int offset, int count) {
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		PlatformOpenGL._wglDrawArrays(mode, offset, count);
	}
	
	void drawDirectArrays(int mode, int offset, int count) {
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		if(mode == GL_QUADS) {
			if(count > 0xFFFF) {
				if(!bindQuad32) {
					bindQuad16 = false;
					bindQuad32 = true;
					EaglercraftGPU.attachQuad32EmulationBuffer(count, true);
				}else {
					EaglercraftGPU.attachQuad32EmulationBuffer(count, false);
				}
				PlatformOpenGL._wglDrawElements(GL_TRIANGLES, count + (count >> 1),
						GL_UNSIGNED_INT, 0);
			}else {
				if(!bindQuad16) {
					bindQuad16 = true;
					bindQuad32 = false;
					EaglercraftGPU.attachQuad16EmulationBuffer(count, true);
				}else {
					EaglercraftGPU.attachQuad16EmulationBuffer(count, false);
				}
				PlatformOpenGL._wglDrawElements(GL_TRIANGLES, count + (count >> 1),
						GL_UNSIGNED_SHORT, 0);
			}
		}else {
			PlatformOpenGL._wglDrawArrays(mode, offset, count);
		}
	}
	
	void drawElements(int mode, int count, int type, int offset) {
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		PlatformOpenGL._wglDrawElements(mode, count, type, offset);
	}
	
	private static final FixedFunctionPipeline[] pipelineStateCache = new FixedFunctionPipeline[4096];

	private static String shaderSourceCacheVSH = null;
	private static String shaderSourceCacheFSH = null;
	
	private static FixedFunctionPipeline getPipelineInstance(int bits) {
		FixedFunctionPipeline pp = pipelineStateCache[bits];
		if(pp == null) {
			if(shaderSourceCacheVSH == null) {
				shaderSourceCacheVSH = EagRuntime.getResourceString(FixedFunctionConstants.FILENAME_VSH);
				if(shaderSourceCacheVSH == null) {
					throw new RuntimeException("Could not load: " + FixedFunctionConstants.FILENAME_VSH);
				}
			}
			if(shaderSourceCacheFSH == null) {
				shaderSourceCacheFSH = EagRuntime.getResourceString(FixedFunctionConstants.FILENAME_FSH);
				if(shaderSourceCacheFSH == null) {
					throw new RuntimeException("Could not load: " + FixedFunctionConstants.FILENAME_FSH);
				}
			}
			
			String macros = FixedFunctionConstants.VERSION + "\n";
			if((bits & STATE_HAS_ATTRIB_TEXTURE) == STATE_HAS_ATTRIB_TEXTURE) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ATTRIB_TEXTURE + "\n");
			}
			if((bits & STATE_HAS_ATTRIB_COLOR) == STATE_HAS_ATTRIB_COLOR) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ATTRIB_COLOR + "\n");
			}
			if((bits & STATE_HAS_ATTRIB_NORMAL) == STATE_HAS_ATTRIB_NORMAL) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ATTRIB_NORMAL + "\n");
			}
			if((bits & STATE_HAS_ATTRIB_LIGHTMAP) == STATE_HAS_ATTRIB_LIGHTMAP) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ATTRIB_LIGHTMAP + "\n");
			}
			if((bits & STATE_ENABLE_TEXTURE2D) == STATE_ENABLE_TEXTURE2D) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_TEXTURE2D + "\n");
			}
			if((bits & STATE_ENABLE_LIGHTMAP) == STATE_ENABLE_LIGHTMAP) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_LIGHTMAP + "\n");
			}
			if((bits & STATE_ENABLE_ALPHA_TEST) == STATE_ENABLE_ALPHA_TEST) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_ALPHA_TEST + "\n");
			}
			if((bits & STATE_ENABLE_MC_LIGHTING) == STATE_ENABLE_MC_LIGHTING) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_MC_LIGHTING + "\n");
			}
			if((bits & STATE_ENABLE_END_PORTAL) == STATE_ENABLE_END_PORTAL) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_END_PORTAL + "\n");
			}
			if((bits & STATE_ENABLE_ANISOTROPIC_FIX) == STATE_ENABLE_ANISOTROPIC_FIX) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_ANISOTROPIC_FIX + "\n");
			}
			if((bits & STATE_ENABLE_FOG) == STATE_ENABLE_FOG) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_FOG + "\n");
			}
			if((bits & STATE_ENABLE_BLEND_ADD) == STATE_ENABLE_BLEND_ADD) {
				macros += ("#define " + FixedFunctionConstants.MACRO_ENABLE_BLEND_ADD + "\n");
			}

			macros += ("precision " + FixedFunctionConstants.PRECISION_INT + " int;\n");
			macros += ("precision " + FixedFunctionConstants.PRECISION_FLOAT + " float;\n");
			macros += ("precision " + FixedFunctionConstants.PRECISION_SAMPLER + " sampler2D;\n\n");
			
			IShaderGL vsh = _wglCreateShader(GL_VERTEX_SHADER);
			
			_wglShaderSource(vsh, macros + shaderSourceCacheVSH);
			_wglCompileShader(vsh);
			
			if(_wglGetShaderi(vsh, GL_COMPILE_STATUS) != GL_TRUE) {
				LOGGER.error("Failed to compile GL_VERTEX_SHADER for state {} !", getStateBits(bits, 11));
				String log = _wglGetShaderInfoLog(vsh);
				if(log != null) {
					String[] lines = log.split("(\\r\\n|\\r|\\n)");
					for(int i = 0; i < lines.length; ++i) {
						LOGGER.error("[VERT] {}", lines[i]);
					}
				}
				_wglDeleteShader(vsh);
				throw new IllegalStateException("Vertex shader could not be compiled!");
			}
			
			IShaderGL fsh = _wglCreateShader(GL_FRAGMENT_SHADER);
			
			_wglShaderSource(fsh, macros + shaderSourceCacheFSH);
			_wglCompileShader(fsh);
			
			if(_wglGetShaderi(fsh, GL_COMPILE_STATUS) != GL_TRUE) {
				LOGGER.error("Failed to compile GL_FRAGMENT_SHADER for state {} !", getStateBits(bits, 11));
				String log = _wglGetShaderInfoLog(fsh);
				if(log != null) {
					String[] lines = log.split("(\\r\\n|\\r|\\n)");
					for(int i = 0; i < lines.length; ++i) {
						LOGGER.error("[FRAG] {}", lines[i]);
					}
				}
				_wglDeleteShader(fsh);
				_wglDeleteShader(vsh);
				throw new IllegalStateException("Fragment shader could not be compiled!");
			}
			
			IProgramGL prog = _wglCreateProgram();

			_wglAttachShader(prog, vsh);
			_wglAttachShader(prog, fsh);
			
			IllegalStateException err = null;
			try {
				pp = new FixedFunctionPipeline(bits, prog);
			}catch(IllegalStateException t) {
				err = t;
			}
			
			_wglDetachShader(prog, vsh);
			_wglDetachShader(prog, fsh);
			_wglDeleteShader(fsh);
			_wglDeleteShader(vsh);
			
			if(err != null) {
				_wglDeleteProgram(prog);
				throw err;
			}else {
				pipelineStateCache[bits] = pp;
			}
		}
		return pp;
	}
	
	private static String getStateBits(int input, int bits) {
		String out = "";
		for(int i = bits - 1; i >= 0; --i) {
			out += (input >> bits) & 1;
		}
		return out;
	}

	private final int stateBits;
	private final boolean stateHasAttribTexture;
	private final boolean stateHasAttribColor;
	private final boolean stateHasAttribNormal;
	private final boolean stateHasAttribLightmap;
	private final boolean stateEnableTexture2D;
	private final boolean stateEnableLightmap;
	private final boolean stateEnableAlphaTest;
	private final boolean stateEnableMCLighting;
	private final boolean stateEnableEndPortal;
	private final boolean stateEnableAnisotropicFix;
	private final boolean stateEnableFog;
	private final boolean stateEnableBlendAdd;

	private final int attribTextureIndex;
	private final int attribTextureOffset;
	private final int attribColorIndex;
	private final int attribColorOffset;
	private final int attribNormalIndex;
	private final int attribNormalOffset;
	private final int attribLightmapIndex;
	private final int attribLightmapOffset;
	
	private final int attribStride;

	private final IProgramGL shaderProgram;
	
	private final IUniformGL stateColorUniform4f;
	private float stateColorR = -999.0f;
	private float stateColorG = -999.0f;
	private float stateColorB = -999.0f;
	private float stateColorA = -999.0f;
	private int stateColorSerial = -1;

	private final IUniformGL stateShaderBlendSrcColorUniform4f;
	private float stateShaderBlendSrcColorR = -999.0f;
	private float stateShaderBlendSrcColorG = -999.0f;
	private float stateShaderBlendSrcColorB = -999.0f;
	private float stateShaderBlendSrcColorA = -999.0f;
	private final IUniformGL stateShaderBlendAddColorUniform4f;
	private float stateShaderBlendAddColorR = -999.0f;
	private float stateShaderBlendAddColorG = -999.0f;
	private float stateShaderBlendAddColorB = -999.0f;
	private float stateShaderBlendAddColorA = -999.0f;
	private int stateShaderBlendColorSerial = -1;

	private final IUniformGL stateAlphaTestUniform1f;
	private float stateAlphaTestRef = -999.0f;

	private final IUniformGL stateLightsEnabledUniform1i;
	private final IUniformGL[] stateLightsVectorsArrayUniform4f = new IUniformGL[4];
	private int stateLightsEnabled = -1;
	private final Vector4f[] stateLightsVectors = new Vector4f[4];
	private int stateLightingSerial = -1;

	private final IUniformGL stateLightingAmbientUniform3f;
	private float stateLightingAmbientR = -999.0f;
	private float stateLightingAmbientG = -999.0f;
	private float stateLightingAmbientB = -999.0f;
	private int stateLightingAmbientSerial = -1;
	
	private final IUniformGL stateNormalUniform3f;
	private float stateNormalX = -999.0f;
	private float stateNormalY = -999.0f;
	private float stateNormalZ = -999.0f;
	private int stateNormalSerial = -1;

	// X = Linear or Exp, Y = Density, Z = start, W = end
	private final IUniformGL stateFogParamUniform4f;
	private boolean stateFogEXP = false;
	private float stateFogDensity = -999.0f;
	private float stateFogStart = -999.0f;
	private float stateFogEnd = -999.0f;
	private final IUniformGL stateFogColorUniform4f;
	private float stateFogColorR = -999.0f;
	private float stateFogColorG = -999.0f;
	private float stateFogColorB = -999.0f;
	private float stateFogColorA = -999.0f;
	private int stateFogSerial = -1;

	private final IUniformGL stateTexGenPlaneUniform4i;
	private int stateTexGenSPlane = -1;
	private final IUniformGL stateTexGenSVectorUniform4f;
	private final Vector4f stateTexGenSVector = new Vector4f();
	private int stateTexGenTPlane = -1;
	private final IUniformGL stateTexGenTVectorUniform4f;
	private final Vector4f stateTexGenTVector = new Vector4f();
	private int stateTexGenRPlane = -1;
	private final IUniformGL stateTexGenRVectorUniform4f;
	private final Vector4f stateTexGenRVector = new Vector4f();
	private int stateTexGenQPlane = -1;
	private final IUniformGL stateTexGenQVectorUniform4f;
	private final Vector4f stateTexGenQVector = new Vector4f();
	private int stateTexGenSerial = -1;

	private final IUniformGL stateModelMatrixUniformMat4f;
	private int stateModelMatrixSerial = -1;

	private static final Matrix4f tmpMatrixForInv = new Matrix4f();
	private static final Vector4f tmpVec4ForTex = new Vector4f();

	private final IUniformGL stateProjectionMatrixUniformMat4f;
	private int stateProjectionMatrixSerial = -1;

	// implement only 2 textures
	private final IUniformGL stateTextureMatrix01UniformMat4f;
	private final IUniformGL stateTextureMatrix02UniformMat4f;
	private final int[] stateTextureMatrixSerial = new int[8];
	
	private final IUniformGL stateTextureCoords01Uniform2f;
	private final IUniformGL stateTextureCoords02Uniform2f;
	private final float[] stateTextureCoordsX = new float[8];
	private final float[] stateTextureCoordsY = new float[8];
	private final int[] stateTextureCoordsAccessSerial = new int[8];
	private final int[] stateTextureCoordsMatrixSerial = new int[8];

	private final IUniformGL stateAnisotropicFix2f;
	private float stateAnisotropicFixW = -999.0f;
	private float stateAnisotropicFixH = -999.0f;
	private float stateAnisotropicFixSerial = 0;

	private final IBufferArrayGL vertexArray;
	private final IBufferGL vertexBuffer;
	private int vertexBufferSize = -1;
	
	private boolean bindQuad16 = false;
	private boolean bindQuad32 = false;
	
	private static FloatBuffer matrixCopyBuffer = null;
	
	private FixedFunctionPipeline(int bits, IProgramGL compiledProg) {
		shaderProgram = compiledProg;
		
		stateBits = bits;
		stateHasAttribTexture = (bits & STATE_HAS_ATTRIB_TEXTURE) == STATE_HAS_ATTRIB_TEXTURE;
		stateHasAttribColor = (bits & STATE_HAS_ATTRIB_COLOR) == STATE_HAS_ATTRIB_COLOR;
		stateHasAttribNormal = (bits & STATE_HAS_ATTRIB_NORMAL) == STATE_HAS_ATTRIB_NORMAL;
		stateHasAttribLightmap = (bits & STATE_HAS_ATTRIB_LIGHTMAP) == STATE_HAS_ATTRIB_LIGHTMAP;
		
		int index = 0;
		int stride = 0;
		
		_wglBindAttribLocation(compiledProg, index, FixedFunctionConstants.ATTRIB_POSITION);
		
		stride += VertexFormat.COMPONENT_POSITION_STRIDE; // vec3f
		if(stateHasAttribColor) {
			attribColorIndex = ++index;
			attribColorOffset = stride;
			_wglBindAttribLocation(compiledProg, index, FixedFunctionConstants.ATTRIB_COLOR);
			stride += VertexFormat.COMPONENT_COLOR_STRIDE; // vec4b
		}else {
			attribColorIndex = -1;
			attribColorOffset = -1;
		}
		if(stateHasAttribTexture) {
			attribTextureIndex = ++index;
			attribTextureOffset = stride;
			_wglBindAttribLocation(compiledProg, index, FixedFunctionConstants.ATTRIB_TEXTURE);
			stride += VertexFormat.COMPONENT_TEX_STRIDE; // vec2f
		}else {
			attribTextureIndex = -1;
			attribTextureOffset = -1;
		}
		if(stateHasAttribNormal) {
			attribNormalIndex = ++index;
			attribNormalOffset = stride;
			_wglBindAttribLocation(compiledProg, index, FixedFunctionConstants.ATTRIB_NORMAL);
			stride += VertexFormat.COMPONENT_NORMAL_STRIDE; // vec4b
		}else {
			attribNormalIndex = -1;
			attribNormalOffset = -1;
		}
		if(stateHasAttribLightmap) {
			attribLightmapIndex = ++index;
			attribLightmapOffset = stride;
			_wglBindAttribLocation(compiledProg, index, FixedFunctionConstants.ATTRIB_LIGHTMAP);
			stride += VertexFormat.COMPONENT_LIGHTMAP_STRIDE; // vec2s
		}else {
			attribLightmapIndex = -1;
			attribLightmapOffset = -1;
		}
		
		attribStride = stride;
		
		_wglLinkProgram(compiledProg);
		
		if(_wglGetProgrami(compiledProg, GL_LINK_STATUS) != GL_TRUE) {
			LOGGER.error("Program could not be linked for state {} !", getStateBits(bits, 11));
			String log = _wglGetProgramInfoLog(compiledProg);
			if(log != null) {
				String[] lines = log.split("(\\r\\n|\\r|\\n)");
				for(int i = 0; i < lines.length; ++i) {
					LOGGER.error("[LINK] {}", lines[i]);
				}
			}
			throw new IllegalStateException("Program could not be linked!");
		}
		
		vertexArray = PlatformOpenGL._wglGenVertexArrays();
		vertexBuffer = PlatformOpenGL._wglGenBuffers();

		EaglercraftGPU.bindGLBufferArray(vertexArray);
		EaglercraftGPU.bindGLArrayBuffer(vertexBuffer);
		
		_wglEnableVertexAttribArray(0);
		_wglVertexAttribPointer(0, VertexFormat.COMPONENT_POSITION_SIZE,
				VertexFormat.COMPONENT_POSITION_FORMAT, false, attribStride, 0);

		if(attribTextureIndex != -1) {
			_wglEnableVertexAttribArray(attribTextureIndex);
			_wglVertexAttribPointer(attribTextureIndex, VertexFormat.COMPONENT_TEX_SIZE,
					VertexFormat.COMPONENT_TEX_FORMAT, false, attribStride, attribTextureOffset);
		}
		
		if(attribColorIndex != -1) {
			_wglEnableVertexAttribArray(attribColorIndex);
			_wglVertexAttribPointer(attribColorIndex, VertexFormat.COMPONENT_COLOR_SIZE,
					VertexFormat.COMPONENT_COLOR_FORMAT, true, attribStride, attribColorOffset);
		}
		
		if(attribNormalIndex != -1) {
			_wglEnableVertexAttribArray(attribNormalIndex);
			_wglVertexAttribPointer(attribNormalIndex, VertexFormat.COMPONENT_NORMAL_SIZE,
					VertexFormat.COMPONENT_NORMAL_FORMAT, true, attribStride, attribNormalOffset);
		}
		
		if(attribLightmapIndex != -1) {
			_wglEnableVertexAttribArray(attribLightmapIndex);
			_wglVertexAttribPointer(attribLightmapIndex, VertexFormat.COMPONENT_LIGHTMAP_SIZE,
					VertexFormat.COMPONENT_LIGHTMAP_FORMAT, false, attribStride, attribLightmapOffset);
		}
		
		stateEnableTexture2D = (bits & STATE_ENABLE_TEXTURE2D) == STATE_ENABLE_TEXTURE2D;
		stateEnableLightmap = (bits & STATE_ENABLE_LIGHTMAP) == STATE_ENABLE_LIGHTMAP;
		stateEnableAlphaTest = (bits & STATE_ENABLE_ALPHA_TEST) == STATE_ENABLE_ALPHA_TEST;
		stateEnableMCLighting = (bits & STATE_ENABLE_MC_LIGHTING) == STATE_ENABLE_MC_LIGHTING;
		stateEnableEndPortal = (bits & STATE_ENABLE_END_PORTAL) == STATE_ENABLE_END_PORTAL;
		stateEnableAnisotropicFix = (bits & STATE_ENABLE_ANISOTROPIC_FIX) == STATE_ENABLE_ANISOTROPIC_FIX;
		stateEnableFog = (bits & STATE_ENABLE_FOG) == STATE_ENABLE_FOG;
		stateEnableBlendAdd = (bits & STATE_ENABLE_BLEND_ADD) == STATE_ENABLE_BLEND_ADD;
		
		for(int i = 0; i < stateLightsVectors.length; ++i) { 
			stateLightsVectors[i] = new Vector4f(-999.0f, -999.0f, -999.0f, 0.0f);
		}
		
		for(int i = 0; i < stateTextureMatrixSerial.length; ++i) {
			stateTextureMatrixSerial[i] = -1;
		}

		stateColorUniform4f = _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_COLOR_NAME);
		
		stateAlphaTestUniform1f = stateEnableAlphaTest ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_ALPHA_TEST_NAME) : null;
		
		stateLightsEnabledUniform1i = stateEnableMCLighting ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_LIGHTS_ENABLED_NAME) : null;
		
		if(stateEnableMCLighting) {
			for(int i = 0; i < stateLightsVectorsArrayUniform4f.length; ++i) {
				stateLightsVectorsArrayUniform4f[i] =_wglGetUniformLocation(compiledProg,
						FixedFunctionConstants.UNIFORM_LIGHTS_VECTORS_NAME + "[" + i + "]");
			}
		}
		
		stateLightingAmbientUniform3f = stateEnableMCLighting ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_LIGHTS_AMBIENT_NAME) : null;
		
		stateNormalUniform3f = (!stateHasAttribNormal && stateEnableMCLighting) ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_CONSTANT_NORMAL_NAME) : null;
		
		stateFogParamUniform4f = stateEnableFog ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_FOG_PARAM_NAME) : null;
		
		stateFogColorUniform4f = stateEnableFog ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_FOG_COLOR_NAME) : null;
		
		stateTexGenPlaneUniform4i = stateEnableEndPortal ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEX_GEN_PLANE_NAME) : null;
		
		stateTexGenSVectorUniform4f = stateEnableEndPortal ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEX_GEN_S_NAME) : null;
		
		stateTexGenTVectorUniform4f = stateEnableEndPortal ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEX_GEN_T_NAME) : null;
		
		stateTexGenRVectorUniform4f = stateEnableEndPortal ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEX_GEN_R_NAME) : null;
		
		stateTexGenQVectorUniform4f = stateEnableEndPortal ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEX_GEN_Q_NAME) : null;
		
		stateModelMatrixUniformMat4f = _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_MODEL_MATRIX_NAME);
		
		stateProjectionMatrixUniformMat4f = _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_PROJECTION_MATRIX_NAME);
		
		stateTextureMatrix01UniformMat4f = (stateEnableEndPortal || stateHasAttribTexture) ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEXTURE_MATRIX_01_NAME) : null;
		
		stateTextureMatrix02UniformMat4f = stateHasAttribLightmap ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEXTURE_MATRIX_02_NAME) : null;
		
		stateTextureCoords01Uniform2f = (!stateHasAttribTexture && stateEnableTexture2D) ? _wglGetUniformLocation(
				compiledProg, FixedFunctionConstants.UNIFORM_TEXTURE_COORDS_01_NAME) : null;
		
		stateTextureCoords02Uniform2f = (!stateHasAttribLightmap && stateEnableLightmap) ? _wglGetUniformLocation(
				compiledProg, FixedFunctionConstants.UNIFORM_TEXTURE_COORDS_02_NAME) : null;
		
		stateAnisotropicFix2f = stateEnableAnisotropicFix ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_TEXTURE_ANISOTROPIC_FIX) : null;
		
		stateShaderBlendSrcColorUniform4f = stateEnableBlendAdd ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_BLEND_SRC_COLOR_NAME) : null;
		
		stateShaderBlendAddColorUniform4f = stateEnableBlendAdd ? _wglGetUniformLocation(compiledProg,
				FixedFunctionConstants.UNIFORM_BLEND_ADD_COLOR_NAME) : null;
		
		if(stateEnableTexture2D) {
			EaglercraftGPU.bindGLShaderProgram(compiledProg);
			_wglUniform1i(_wglGetUniformLocation(compiledProg, FixedFunctionConstants.UNIFORM_TEXTURE_UNIT_01_NAME), 0);
		}
		
		if(stateEnableLightmap) {
			EaglercraftGPU.bindGLShaderProgram(compiledProg);
			_wglUniform1i(_wglGetUniformLocation(compiledProg, FixedFunctionConstants.UNIFORM_TEXTURE_UNIT_02_NAME), 1);
		}
		
	}
	
	public FixedFunctionPipeline update() {
		
		EaglercraftGPU.bindGLShaderProgram(shaderProgram);
		
		int serial = GlStateManager.stateColorSerial;
		if(stateColorSerial != serial) {
			stateColorSerial = serial;
			float r = GlStateManager.stateColorR;
			float g = GlStateManager.stateColorG;
			float b = GlStateManager.stateColorB;
			float a = GlStateManager.stateColorA;
			if(stateColorR != r || stateColorG != g ||
				stateColorB != b || stateColorA != a) {
				_wglUniform4f(stateColorUniform4f, r, g, b, a);
				stateColorR = r;
				stateColorG = g;
				stateColorB = b;
				stateColorA = a;
			}
		}
		
		if(matrixCopyBuffer == null) {
			matrixCopyBuffer = PlatformRuntime.allocateFloatBuffer(16);
		}
		
		int ptr = GlStateManager.modelMatrixStackPointer;
		serial = GlStateManager.modelMatrixStackAccessSerial[ptr];
		if(stateModelMatrixSerial != serial) {
			stateModelMatrixSerial = serial;
			matrixCopyBuffer.clear();
			GlStateManager.modelMatrixStack[ptr].store(matrixCopyBuffer);
			matrixCopyBuffer.flip();
			_wglUniformMatrix4fv(stateModelMatrixUniformMat4f, false, matrixCopyBuffer);
		}
		
		ptr = GlStateManager.projectionMatrixStackPointer;
		serial = GlStateManager.projectionMatrixStackAccessSerial[ptr];
		if(stateProjectionMatrixSerial != serial) {
			stateProjectionMatrixSerial = serial;
			matrixCopyBuffer.clear();
			GlStateManager.projectionMatrixStack[ptr].store(matrixCopyBuffer);
			matrixCopyBuffer.flip();
			_wglUniformMatrix4fv(stateProjectionMatrixUniformMat4f, false, matrixCopyBuffer);
		}
		
		if(stateEnableAlphaTest) {
			float v = GlStateManager.stateAlphaTestRef;
			if(stateAlphaTestRef != v) {
				stateAlphaTestRef = v;
				_wglUniform1f(stateAlphaTestUniform1f, v);
			}
		}
		
		if(stateEnableTexture2D) {
			ptr = GlStateManager.textureMatrixStackPointer[0];
			serial = GlStateManager.textureMatrixStackAccessSerial[0][ptr];
			if(stateHasAttribTexture || stateEnableEndPortal) {
				if(stateTextureMatrixSerial[0] != serial) {
					stateTextureMatrixSerial[0] = serial;
					matrixCopyBuffer.clear();
					GlStateManager.textureMatrixStack[0][ptr].store(matrixCopyBuffer);
					matrixCopyBuffer.flip();
					_wglUniformMatrix4fv(stateTextureMatrix01UniformMat4f, false, matrixCopyBuffer);
				}
			}
			if(!stateHasAttribTexture && !stateEnableEndPortal) {
				int serial2 = GlStateManager.textureCoordsAccessSerial[0];
				if(stateTextureCoordsAccessSerial[0] != serial2 || stateTextureCoordsMatrixSerial[0] != serial) {
					stateTextureCoordsAccessSerial[0] = serial2;
					stateTextureCoordsMatrixSerial[0] = serial;
					tmpVec4ForTex.x = GlStateManager.textureCoordsX[0];
					tmpVec4ForTex.y = GlStateManager.textureCoordsY[0];
					tmpVec4ForTex.z = 0.0f;
					tmpVec4ForTex.w = 1.0f;
					Matrix4f.transform(GlStateManager.textureMatrixStack[0][ptr], tmpVec4ForTex, tmpVec4ForTex);
					float x = tmpVec4ForTex.x / tmpVec4ForTex.w;
					float y = tmpVec4ForTex.y / tmpVec4ForTex.w;
					if(x != stateTextureCoordsX[0] || y != stateTextureCoordsY[0]) {
						stateTextureCoordsX[0] = x;
						stateTextureCoordsY[0] = y;
						_wglUniform2f(stateTextureCoords01Uniform2f, x, y);
					}
				}
			}
		}
		
		if(stateEnableLightmap) {
			ptr = GlStateManager.textureMatrixStackPointer[1];
			serial = GlStateManager.textureMatrixStackAccessSerial[1][ptr];
			if(!stateHasAttribLightmap) {
				int serial2 = GlStateManager.textureCoordsAccessSerial[1];
				if(stateTextureCoordsAccessSerial[1] != serial2 || stateTextureCoordsMatrixSerial[1] != serial) {
					stateTextureCoordsAccessSerial[1] = serial2;
					stateTextureCoordsMatrixSerial[1] = serial;
					tmpVec4ForTex.x = GlStateManager.textureCoordsX[1];
					tmpVec4ForTex.y = GlStateManager.textureCoordsY[1];
					tmpVec4ForTex.z = 0.0f;
					tmpVec4ForTex.w = 1.0f;
					Matrix4f.transform(GlStateManager.textureMatrixStack[1][ptr], tmpVec4ForTex, tmpVec4ForTex);
					float x = tmpVec4ForTex.x / tmpVec4ForTex.w;
					float y = tmpVec4ForTex.y / tmpVec4ForTex.w;
					if(x != stateTextureCoordsX[1] || y != stateTextureCoordsY[1]) {
						stateTextureCoordsX[1] = x;
						stateTextureCoordsY[1] = y;
						_wglUniform2f(stateTextureCoords02Uniform2f, x, y);
					}
				}
			}else {
				if(stateTextureMatrixSerial[1] != serial) {
					stateTextureMatrixSerial[1] = serial;
					matrixCopyBuffer.clear();
					GlStateManager.textureMatrixStack[1][ptr].store(matrixCopyBuffer);
					matrixCopyBuffer.flip();
					_wglUniformMatrix4fv(stateTextureMatrix02UniformMat4f, false, matrixCopyBuffer);
				}
			}
		}
		
		if(stateEnableMCLighting) {
			ptr = GlStateManager.stateLightsStackPointer;
			serial = GlStateManager.stateLightingSerial[ptr];
			if(stateLightingSerial != serial) {
				stateLightingSerial = serial;

				boolean[] en = GlStateManager.stateLightsEnabled[ptr];
				int lightsCounter = 0;
				for(int i = 0; i < en.length; ++i) {
					if(en[i]) {
						Vector4f lightDirOld = stateLightsVectors[lightsCounter];
						Vector4f lightDirNew = GlStateManager.stateLightsStack[ptr][i];
						float x = lightDirNew.x;
						float y = lightDirNew.y;
						float z = lightDirNew.z;
						float w = lightDirNew.w;
						if(lightDirOld.x != x || lightDirOld.y != y || lightDirOld.z != z || lightDirOld.w != w) {
							lightDirOld.x = x;
							lightDirOld.y = y;
							lightDirOld.z = z;
							lightDirOld.w = w;
							_wglUniform4f(stateLightsVectorsArrayUniform4f[lightsCounter], x, y, z, w);
						}
						if(++lightsCounter >= stateLightsVectors.length) {
							break;
						}
					}
				}
				
				if(stateLightsEnabled != lightsCounter) {
					stateLightsEnabled = lightsCounter;
					_wglUniform1i(stateLightsEnabledUniform1i, lightsCounter);
				}
				
			}
			
			serial = GlStateManager.stateLightingAmbientSerial;
			if(stateLightingAmbientSerial != serial) {
				stateLightingAmbientSerial = serial;
				float r = GlStateManager.stateLightingAmbientR;
				float g = GlStateManager.stateLightingAmbientG;
				float b = GlStateManager.stateLightingAmbientB;
				if(stateLightingAmbientR != r || stateLightingAmbientG != g ||
						stateLightingAmbientB != b) {
					stateLightingAmbientR = r;
					stateLightingAmbientG = g;
					stateLightingAmbientB = b;
					_wglUniform3f(stateLightingAmbientUniform3f, r, g, b);
				}
			}
			
			if(!stateHasAttribNormal) {
				serial = GlStateManager.stateNormalSerial;
				if(stateNormalSerial != serial) {
					stateNormalSerial = serial;
					float x = GlStateManager.stateNormalX;
					float y = GlStateManager.stateNormalY;
					float z = GlStateManager.stateNormalZ;
					float c = 1.0f / MathHelper.sqrt_float(x * x + y * y + z * z);
					x *= c; y *= c; z *= c;
					if(stateNormalX != x || stateNormalY != y || stateNormalZ != z) {
						stateNormalX = x;
						stateNormalY = y;
						stateNormalZ = z;
						_wglUniform3f(stateNormalUniform3f, x, y, z);
					}
				}
			}
		}
		
		if(stateEnableFog) {
			serial = GlStateManager.stateFogSerial;
			if(stateFogSerial != serial) {
				stateFogSerial = serial;
				boolean fogEXP = GlStateManager.stateFogEXP;
				float fogDensity = GlStateManager.stateFogDensity;
				float fogStart = GlStateManager.stateFogStart;
				float fogEnd = GlStateManager.stateFogEnd;
				if(stateFogEXP != fogEXP || stateFogDensity != fogDensity ||
						stateFogStart != fogStart || stateFogEnd != fogEnd) {
					stateFogEXP = fogEXP;
					stateFogDensity = fogDensity;
					stateFogStart = fogStart;
					stateFogEnd = fogEnd;
					_wglUniform4f(stateFogParamUniform4f, fogEXP ? 1.0f : 0.0f, fogDensity, fogStart, fogEnd);
				}
				float r = GlStateManager.stateFogColorR;
				float g = GlStateManager.stateFogColorG;
				float b = GlStateManager.stateFogColorB;
				float a = GlStateManager.stateFogColorA;
				if(stateFogColorR != r || stateFogColorG != g ||
						stateFogColorB != b || stateFogColorA != a) {
					stateFogColorR = r;
					stateFogColorG = g;
					stateFogColorB = b;
					stateFogColorA = a;
					_wglUniform4f(stateFogColorUniform4f, r, g, b, a);
				}
			}
		}
		
		if(stateEnableAnisotropicFix) {
			serial = GlStateManager.stateAnisotropicFixSerial;
			if(stateAnisotropicFixSerial != serial) {
				stateAnisotropicFixSerial = serial;
				float w = GlStateManager.stateAnisotropicFixW;
				float h = GlStateManager.stateAnisotropicFixH;
				if(stateAnisotropicFixW != w || stateAnisotropicFixH != h) {
					stateAnisotropicFixW = w;
					stateAnisotropicFixH = h;
					_wglUniform2f(stateAnisotropicFix2f, w, h);
				}
			}
		}
		
		if(stateEnableEndPortal) {
			serial = GlStateManager.stateTexGenSerial;
			if(stateTexGenSerial != serial) {
				stateTexGenSerial = serial;
				int planeS = GlStateManager.TexGen.S.plane;
				int planeT = GlStateManager.TexGen.T.plane;
				int planeR = GlStateManager.TexGen.R.plane;
				int planeQ = GlStateManager.TexGen.Q.plane;
				if(stateTexGenSPlane != planeS || stateTexGenTPlane != planeT ||
						stateTexGenRPlane != planeR || stateTexGenQPlane != planeQ) {
					stateTexGenSPlane = planeS;
					stateTexGenTPlane = planeT;
					stateTexGenRPlane = planeR;
					stateTexGenQPlane = planeQ;
					_wglUniform4i(stateTexGenPlaneUniform4i, planeS == GL_EYE_PLANE ? 1 : 0,
							planeT == GL_EYE_PLANE ? 1 : 0, planeR == GL_EYE_PLANE ? 1 : 0,
							planeQ == GL_EYE_PLANE ? 1 : 0);
				}
				Vector4f vecS = GlStateManager.TexGen.S.vector;
				if (stateTexGenSVector.x != vecS.x || stateTexGenSVector.y != vecS.y ||
						stateTexGenSVector.z != vecS.z || stateTexGenSVector.w != vecS.w) {
					stateTexGenSVector.x = vecS.x;
					stateTexGenSVector.y = vecS.y;
					stateTexGenSVector.z = vecS.z;
					stateTexGenSVector.w = vecS.w;
					_wglUniform4f(stateTexGenSVectorUniform4f, vecS.x, vecS.y, vecS.z, vecS.w);
				}
				Vector4f vecT = GlStateManager.TexGen.T.vector;
				if (stateTexGenTVector.x != vecT.x || stateTexGenTVector.y != vecT.y ||
						stateTexGenTVector.z != vecT.z || stateTexGenTVector.w != vecT.w) {
					stateTexGenTVector.x = vecT.x;
					stateTexGenTVector.y = vecT.y;
					stateTexGenTVector.z = vecT.z;
					stateTexGenTVector.w = vecT.w;
					_wglUniform4f(stateTexGenTVectorUniform4f, vecT.x, vecT.y, vecT.z, vecT.w);
				}
				Vector4f vecR = GlStateManager.TexGen.R.vector;
				if (stateTexGenRVector.x != vecR.x || stateTexGenRVector.y != vecR.y ||
						stateTexGenRVector.z != vecR.z || stateTexGenRVector.w != vecR.w) {
					stateTexGenRVector.x = vecR.x;
					stateTexGenRVector.y = vecR.y;
					stateTexGenRVector.z = vecR.z;
					stateTexGenRVector.w = vecR.w;
					_wglUniform4f(stateTexGenRVectorUniform4f, vecR.x, vecR.y, vecR.z, vecR.w);
				}
				Vector4f vecQ = GlStateManager.TexGen.Q.vector;
				if (stateTexGenQVector.x != vecQ.x || stateTexGenQVector.y != vecQ.y ||
						stateTexGenQVector.z != vecQ.z || stateTexGenQVector.w != vecQ.w) {
					stateTexGenQVector.x = vecQ.x;
					stateTexGenQVector.y = vecQ.y;
					stateTexGenQVector.z = vecQ.z;
					stateTexGenQVector.w = vecQ.w;
					_wglUniform4f(stateTexGenQVectorUniform4f, vecQ.x, vecQ.y, vecQ.z, vecQ.w);
				}
			}
		}
		
		if(stateEnableBlendAdd) {
			serial = GlStateManager.stateShaderBlendColorSerial;
			if(stateShaderBlendColorSerial != serial) {
				stateShaderBlendColorSerial = serial;
				float r = GlStateManager.stateShaderBlendSrcColorR;
				float g = GlStateManager.stateShaderBlendSrcColorG;
				float b = GlStateManager.stateShaderBlendSrcColorB;
				float a = GlStateManager.stateShaderBlendSrcColorA;
				if(stateShaderBlendSrcColorR != r || stateShaderBlendSrcColorG != g ||
						stateShaderBlendSrcColorB != b || stateShaderBlendSrcColorA != a) {
					_wglUniform4f(stateShaderBlendSrcColorUniform4f, r, g, b, a);
					stateShaderBlendSrcColorR = r;
					stateShaderBlendSrcColorG = g;
					stateShaderBlendSrcColorB = b;
					stateShaderBlendSrcColorA = a;
				}
				r = GlStateManager.stateShaderBlendAddColorR;
				g = GlStateManager.stateShaderBlendAddColorG;
				b = GlStateManager.stateShaderBlendAddColorB;
				a = GlStateManager.stateShaderBlendAddColorA;
				if(stateShaderBlendAddColorR != r || stateShaderBlendAddColorG != g ||
						stateShaderBlendAddColorB != b || stateShaderBlendAddColorA != a) {
					_wglUniform4f(stateShaderBlendAddColorUniform4f, r, g, b, a);
					stateShaderBlendAddColorR = r;
					stateShaderBlendAddColorG = g;
					stateShaderBlendAddColorB = b;
					stateShaderBlendAddColorA = a;
				}
			}
		}
		
		return this;
	}
	
	public static void flushCache() {
		shaderSourceCacheVSH = null;
		shaderSourceCacheFSH = null;
		for(int i = 0; i < pipelineStateCache.length; ++i) {
			if(pipelineStateCache[i] != null) {
				pipelineStateCache[i].destroy();
				pipelineStateCache[i] = null;
			}
		}
	}
	
	public void destroy() {
		PlatformOpenGL._wglDeleteProgram(shaderProgram);
		PlatformOpenGL._wglDeleteVertexArrays(vertexArray);
		PlatformOpenGL._wglDeleteBuffers(vertexBuffer);
	}
	
	public IBufferArrayGL getDirectModeBufferArray() {
		return vertexArray;
	}
}

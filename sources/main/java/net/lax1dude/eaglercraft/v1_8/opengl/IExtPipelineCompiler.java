package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;

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
public interface IExtPipelineCompiler {

	/**
	 * @return new String[] { vshSource, fshSource }
	 */
	String[] getShaderSource(int stateCoreBits, int stateExtBits, Object[] userPointer);

	int getExtensionStatesCount();

	int getCurrentExtensionStateBits(int stateCoreBits);

	int getCoreStateMask(int stateExtBits);

	void initializeNewShader(IProgramGL compiledProg, int stateCoreBits, int stateExtBits, Object[] userPointer);

	void updatePipeline(IProgramGL compiledProg, int stateCoreBits, int stateExtBits, Object[] userPointer);

	void destroyPipeline(IProgramGL shaderProgram, int stateCoreBits, int stateExtBits, Object[] userPointer);

}

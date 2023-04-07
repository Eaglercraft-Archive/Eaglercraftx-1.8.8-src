package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.GBufferExtPipelineShader;

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
public class GBufferPipelineProgramInstance {

	public final int coreState;
	public final int extState;

	public GBufferExtPipelineShader shaderObject = null;

	public GBufferPipelineProgramInstance(int coreState, int extState) {
		this.coreState = coreState;
		this.extState = extState;
	}

}

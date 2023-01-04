package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

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
public class WorldVertexBufferUploader {
	public void func_181679_a(WorldRenderer parWorldRenderer) {
		int cunt = parWorldRenderer.getVertexCount();
		if (cunt > 0) {
			VertexFormat fmt = parWorldRenderer.getVertexFormat();
			ByteBuffer buf = parWorldRenderer.getByteBuffer();
			buf.position(0).limit(cunt * fmt.attribStride);
			EaglercraftGPU.renderBuffer(buf, fmt.eaglercraftAttribBits,
					parWorldRenderer.getDrawMode(), cunt);
			parWorldRenderer.reset();
		}
	}
}
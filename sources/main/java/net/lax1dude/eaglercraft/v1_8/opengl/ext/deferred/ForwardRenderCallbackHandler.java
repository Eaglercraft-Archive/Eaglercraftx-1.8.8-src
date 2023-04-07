package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class ForwardRenderCallbackHandler {

	public final List<ShadersRenderPassFuture> renderPassList = new ArrayList(1024);

	public void push(ShadersRenderPassFuture f) {
		renderPassList.add(f);
	}

	public void reset() {
		renderPassList.clear();
	}

	public void sort(float x, float y, float z) {
		if(renderPassList.size() == 0) return;
		ShadersRenderPassFuture rp;
		float dx, dy, dz;
		for(int i = 0, l = renderPassList.size(); i < l; ++i) {
			rp = renderPassList.get(i);
			dx = rp.getX() - x;
			dy = rp.getY() - y;
			dz = rp.getZ() - z;
			rp.tmpValue()[0] = dx * dx + dy * dy + dz * dz;
		}
		Collections.sort(renderPassList, new Comparator<ShadersRenderPassFuture>() {
			@Override
			public int compare(ShadersRenderPassFuture o1, ShadersRenderPassFuture o2) {
				float a = o1.tmpValue()[0], b = o2.tmpValue()[0];
				return a < b ? 1 : (a > b ? -1 : 0);
			}
		});
	}
}

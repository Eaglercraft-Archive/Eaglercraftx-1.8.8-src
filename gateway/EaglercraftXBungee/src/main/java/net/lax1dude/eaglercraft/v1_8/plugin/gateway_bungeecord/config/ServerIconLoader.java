package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

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
class ServerIconLoader {

	static int[] createServerIcon(BufferedImage awtIcon) {
		BufferedImage icon = awtIcon;
		boolean gotScaled = false;
		if(icon.getWidth() != 64 || icon.getHeight() != 64) {
			icon = new BufferedImage(64, 64, awtIcon.getType());
			Graphics2D g = (Graphics2D) icon.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, (awtIcon.getWidth() < 64 || awtIcon.getHeight() < 64) ?
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR : RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setBackground(new Color(0, true));
			g.clearRect(0, 0, 64, 64);
			int ow = awtIcon.getWidth();
			int oh = awtIcon.getHeight();
			int nw, nh;
			float aspectRatio = (float)oh / (float)ow;
			if(aspectRatio >= 1.0f) {
				nw = (int)(64 / aspectRatio);
				nh = 64;
			}else {
				nw = 64;
				nh = (int)(64 * aspectRatio);
			}
			g.drawImage(awtIcon, (64 - nw) / 2, (64 - nh) / 2, (64 - nw) / 2 + nw, (64 - nh) / 2 + nh, 0, 0, awtIcon.getWidth(), awtIcon.getHeight(), null);
			g.dispose();
			gotScaled = true;
		}
		int[] pxls = icon.getRGB(0, 0, 64, 64, new int[4096], 0, 64);
		if(gotScaled) {
			for(int i = 0; i < pxls.length; ++i) {
				if((pxls[i] & 0xFFFFFF) == 0) {
					pxls[i] = 0;
				}
			}
		}
		return pxls;
	}
	
	static int[] createServerIcon(InputStream f) {
		try {
			return createServerIcon(ImageIO.read(f));
		}catch(Throwable t) {
			return null;
		}
	}
	
	static int[] createServerIcon(File f) {
		try {
			return createServerIcon(ImageIO.read(f));
		}catch(Throwable t) {
			return null;
		}
	}

}

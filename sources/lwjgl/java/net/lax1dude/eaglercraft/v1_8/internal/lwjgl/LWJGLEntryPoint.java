package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformANGLE;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.minecraft.client.main.Main;

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
public class LWJGLEntryPoint {

	public static void main_(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Could not set system look and feel: " + e.toString());
		}
		
		LaunchRenderDocDialog lr = new LaunchRenderDocDialog();
		lr.setLocationRelativeTo(null);
		lr.setVisible(true);
		
		while(lr.isVisible()) {
			EagUtils.sleep(100l);
		}
		
		lr.dispose();
		
		getANGLEPlatformFromArgs(args);
		
		EagRuntime.create();
		
		Main.appMain(new String[0]);
		
	}
	
	private static void getANGLEPlatformFromArgs(String[] args) {
		for(int i = 0; i < args.length; ++i) {
			EnumPlatformANGLE angle = EnumPlatformANGLE.fromId(args[i]);
			if(angle != EnumPlatformANGLE.DEFAULT) {
				PlatformRuntime.requestANGLE(angle);
				break;
			}
		}
	}

}

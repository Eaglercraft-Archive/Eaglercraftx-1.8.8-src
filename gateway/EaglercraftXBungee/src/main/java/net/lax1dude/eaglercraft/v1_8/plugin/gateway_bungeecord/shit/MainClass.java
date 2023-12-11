package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.shit;

import java.awt.GraphicsEnvironment;

import javax.swing.JOptionPane;

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
public class MainClass {

	public static void main(String[] args) {
		System.err.println();
		System.err.println("ERROR: The EaglerXBungee 1.8 jar file is a PLUGIN intended to be used with BungeeCord!");
		System.err.println("Place this file in the \"plugins\" directory of your BungeeCord installation");
		System.err.println();
		try {
			tryShowPopup();
		}catch(Throwable t) {
		}
		System.exit(0);
	}

	private static void tryShowPopup() throws Throwable {
		if(!GraphicsEnvironment.isHeadless()) {
			JOptionPane.showMessageDialog(null, "ERROR: The EaglerXBungee 1.8 jar file is a PLUGIN intended to be used with BungeeCord!\nPlace this file in the \"plugins\" directory of your BungeeCord installation", "EaglerXBungee", JOptionPane.ERROR_MESSAGE);
		}
	}
}

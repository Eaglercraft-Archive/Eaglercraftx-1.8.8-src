package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

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
public class FileChooserTool {
	
	public static final JFileChooser fc = new JFileChooser();
	
	public static File load(boolean directory) {
		fc.setFileSelectionMode(directory ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setFileHidingEnabled(false);
		fc.setDialogTitle("Eaglercraft Buildtools");
		JFrame parent = new JFrame();
		parent.setBounds(0, 0, 50, 50);
		parent.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		parent.setAlwaysOnTop(true);
		parent.setTitle("File Chooser");
		parent.setLocationRelativeTo(null);
		parent.setVisible(true);
		if(fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			parent.dispose();
			return fc.getSelectedFile();
		}else {
			parent.dispose();
			return null;
		}
	}
	
}

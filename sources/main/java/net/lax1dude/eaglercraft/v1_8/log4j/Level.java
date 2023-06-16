package net.lax1dude.eaglercraft.v1_8.log4j;

import java.io.PrintStream;

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
public enum Level {
	
	TRACE(0, "TRACE", false), DEBUG(1, "DEBUG", false), INFO(2, "INFO", false),
	WARN(3, "WARN", false), ERROR(4, "ERROR", true), FATAL(5, "FATAL", true),
	OFF(Integer.MAX_VALUE, "DISABLED", false);

	public final int levelInt;
	public final String levelName;
	public final PrintStream stdout;
	
	private Level(int levelInt, String levelName, boolean stderr) {
		this.levelInt = levelInt;
		this.levelName = levelName;
		this.stdout = stderr ? System.err : System.out;
	}
	
	PrintStream getPrintStream() {
		return stdout;
	}
	
}

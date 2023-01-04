package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
public class Lines {

	public static final Pattern splitPattern = Pattern.compile("(\\r\\n|\\n|\\r)");

	public static String[] linesArray(String input) {
		return splitPattern.split(input);
	}
	
	public static List<String> linesList(String input) {
		return Arrays.asList(splitPattern.split(input));
	}
	
}

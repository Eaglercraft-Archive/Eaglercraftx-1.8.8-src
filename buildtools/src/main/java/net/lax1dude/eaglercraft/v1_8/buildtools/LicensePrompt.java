package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
public class LicensePrompt {

	public static void main(String[] args) {
		System.out.println();
		display();
	}

	public static void display() {
		System.out.println("WARNING: You must agree to the LICENSE before running this command");
		System.out.println();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(LicensePrompt.class.getResourceAsStream("/lang/LICENSE_console_wrapped.txt"), StandardCharsets.UTF_8))) {
			String line;
			while((line = reader.readLine()) != null) {
				if(line.equals("<press enter>")) {
					pressEnter();
				}else {
					System.out.println(line);
				}
			}
		}catch(IOException ex) {
			System.err.println();
			System.err.println("ERROR: could not display license text");
			System.err.println("Please read the \"LICENSE\" file before using this software");
			System.err.println();
			pressEnter();
		}
	}

	private static void pressEnter() {
		System.out.println();
		System.out.println("(press ENTER to continue)");
		while(true) {
			try {
				if(System.in.read() == '\n') {
					break;
				}
			}catch(IOException ex) {
				throw new RuntimeException("Unexpected IOException reading STDIN", ex);
			}
		}
	}

}

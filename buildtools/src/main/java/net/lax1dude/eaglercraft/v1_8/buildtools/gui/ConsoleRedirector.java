package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.IOException;
import java.io.OutputStream;
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
public class ConsoleRedirector extends OutputStream {

	private final OutputStream stdout;
	private final boolean err;

	public ConsoleRedirector(boolean err) {
		stdout = err ? System.err : System.out;
		this.err = err;
	}

	@Override
	public void write(byte[] b, int o, int l) throws IOException {
		stdout.write(b, o, l);
		String append = new String(b, o, l, StandardCharsets.US_ASCII);
		if(err) {
			CompileLatestClientGUI.frame.logError(append);
		}else {
			CompileLatestClientGUI.frame.logInfo(append);
		}
	}

	@Override
	public void write(int b) throws IOException {
		stdout.write(b);
		write0(b);
	}

	private void write0(int b) throws IOException {
		char c = (char)b;
		if(c != '\r') {
			if(err && c != '\n') {
				CompileLatestClientGUI.frame.logError(new String(new char[] { c }));
			}else {
				CompileLatestClientGUI.frame.logInfo(new String(new char[] { c }));
			}
		}
	}

}

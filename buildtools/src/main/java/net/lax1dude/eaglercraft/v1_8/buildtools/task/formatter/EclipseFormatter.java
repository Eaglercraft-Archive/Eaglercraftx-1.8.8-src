package net.lax1dude.eaglercraft.v1_8.buildtools.task.formatter;

import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatterOptions;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

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
public class EclipseFormatter {
	
	private static final DefaultCodeFormatter formatter = new DefaultCodeFormatter(DefaultCodeFormatterOptions.getEclipseDefaultSettings());
	
	public static String processSource(String input, String lineSeparator) {
		try {
			IDocument doc = new Document();
			doc.set(input);
			TextEdit edit = formatter.format(CodeFormatter.K_COMPILATION_UNIT |
				CodeFormatter.F_INCLUDE_COMMENTS, input, 0, input.length(), 0, lineSeparator);
			edit.apply(doc);
			return doc.get();
		}catch(Throwable t) {
			System.err.println("Code formatting failed!");
			t.printStackTrace();
			return null;
		}
	}
	
}

package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.CSVMappings.Symbol;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileReaderUTF;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileWriterUTF;

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
public class GenerateSRGs {
	
	public static boolean generate(File mcpDataTMP, File srgOut, CSVMappings csv) throws Throwable {
		System.out.println();
		System.out.println("Generating \"" + srgOut.getName() + "\" from \"" + mcpDataTMP.getName() + "\"...");
		
		File methodsCSV = new File(mcpDataTMP, "methods.csv");
		try(FileReaderUTF fr = new FileReaderUTF(methodsCSV)) {
			csv.loadMethodsFile(fr);
		}catch(IOException ex) {
			System.err.println("ERROR: failed to read \"" + methodsCSV.getAbsolutePath() + "\"!");
			ex.printStackTrace();
			return false;
		}
		
		File fieldsCSV = new File(mcpDataTMP, "fields.csv");
		try(FileReaderUTF fr = new FileReaderUTF(fieldsCSV)) {
			csv.loadFieldsFile(fr);
		}catch(IOException ex) {
			System.err.println("ERROR: failed to read \"" + fieldsCSV.getAbsolutePath() + "\"!");
			ex.printStackTrace();
			return false;
		}
		
		int ccount = 0;
		int mcount = 0;
		int fcount = 0;
		
		try(BufferedReader is = new BufferedReader(new FileReaderUTF(new File(mcpDataTMP, "joined.srg")));
				PrintWriter os = new PrintWriter(new FileWriterUTF(srgOut));) {
			String s;
			while((s = is.readLine()) != null) {
				if(s.startsWith("MD:")) {
					int lastSpace = s.lastIndexOf(' ');
					String sig = s.substring(lastSpace + 1);
					s = s.substring(0, lastSpace);
					int lastSlash = s.lastIndexOf('/');
					String fd = s.substring(lastSlash + 1);
					s = s.substring(0, lastSlash);
					Symbol sm = csv.csvMethodsMappings.get(fd);
					if(sm != null) {
						++mcount;
						fd = sm.name;
					}
					os.println(s + "/" + fd + " " + sig);
				}else if(s.startsWith("FD:")) {
					int lastSlash = s.lastIndexOf('/');
					String fd = s.substring(lastSlash + 1);
					s = s.substring(0, lastSlash);
					Symbol sm = csv.csvFieldsMappings.get(fd);
					if(sm != null) {
						++fcount;
						fd = sm.name;
					}
					os.println(s + "/" + fd);
				}else if(s.startsWith("CL:")) {
					++ccount;
					os.println(s);
				}else {
					os.println(s);
				}
			}
		}catch(IOException ex) {
			System.err.println("ERROR: failed to write \"" + srgOut.getName() + "\" from \"joined.srg\"!");
			ex.printStackTrace();
			return false;
		}

		System.out.println("   - Deobf " + ccount + " classes to \"" + srgOut.getName() + "\"");
		System.out.println("   - Deobf " + mcount + " methods to \"" + srgOut.getName() + "\"");
		System.out.println("   - Deobf " + fcount + " fields to \"" + srgOut.getName() + "\"");
		
		return true;
	}

}

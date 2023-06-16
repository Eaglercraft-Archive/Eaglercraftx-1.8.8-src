package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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
public class CSVMappings {

	public final Map<String, Symbol> csvFieldsMappings = new HashMap();
	public final Map<String, Symbol> csvMethodsMappings = new HashMap();
	public final Map<String, Param> csvParamsMappings = new HashMap();
	public final Map<String, Param[]> csvParamsForFunction = new HashMap();
	
	public static class Symbol {
		
		public final String name;
		public final int mod;
		public final String comment;
		
		public Symbol(String name, int mod, String comment) {
			this.name = name;
			this.mod = mod;
			this.comment = comment;
		}
		
	}
	
	public static class Param {
		
		public final String name;
		public final int mod;
		
		public Param(String name, int mod) {
			this.name = name;
			this.mod = mod;
		}
		
	}
	
	public void loadMethodsFile(Reader reader) throws IOException {
		loadSymbols(reader, csvMethodsMappings, "methods.csv");
	}
	
	public void loadFieldsFile(Reader reader) throws IOException {
		loadSymbols(reader, csvFieldsMappings, "fields.csv");
	}
	
	private void loadSymbols(Reader reader, Map<String, Symbol> map, String debugFileName) throws IOException {
		try {
			CSVParser ps = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
			Iterator<CSVRecord> rows = ps.iterator();
			while(rows.hasNext()) {
				CSVRecord rec = rows.next();
				String srgName = rec.get("searge");
				String deobfName = rec.get("name");
				int mod = Integer.parseInt(rec.get("side"));
				String comment = rec.get("desc");
				map.put(srgName, new Symbol(deobfName, mod, comment));
			}
			System.out.println("   Loaded " + map.size() + " symbols from " + debugFileName);
		}catch(Throwable t) {
			t.printStackTrace();
			throw new IOException("Invalid " + debugFileName + " file!");
		}
	}
	
	public void loadParamsFile(Reader reader) throws IOException {
		try {
			CSVParser ps = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
			Iterator<CSVRecord> rows = ps.iterator();
			while(rows.hasNext()) {
				CSVRecord rec = rows.next();
				String srgName = rec.get("param");
				String deobfName = rec.get("name");
				int mod = Integer.parseInt(rec.get("side"));
				csvParamsMappings.put(srgName, new Param(deobfName, mod));
				String fName = srgName.substring(srgName.indexOf('_') + 1);
				if(!fName.startsWith("i")) { 
					int i2 = fName.indexOf('_');
					if(i2 != -1) {
						int ordinal = -1;
						String ordStr = fName.substring(i2 + 1);
						if(ordStr.length() >= 2) {
							try {
								ordinal = Integer.parseInt(ordStr.substring(0, ordStr.length() - 1));
							}catch(NumberFormatException ex) {
							}
						}
						if(ordinal >= 0) {
							fName = "func_" + fName.substring(0, i2);
							Param[] prm = csvParamsForFunction.get(fName);
							if(prm == null || prm.length <= ordinal) {
								Param[] prm2 = new Param[ordinal + 1];
								if(prm != null) {
									System.arraycopy(prm, 0, prm2, 0, prm.length);
								}
								prm = prm2;
							}
							prm[ordinal] = new Param(deobfName, mod);
							csvParamsForFunction.put(fName, prm);
						}
					}
				}
			}
			System.out.println("   Loaded " + csvParamsMappings.size() + " symbols from params.csv");
		}catch(Throwable t) {
			throw new IOException("Invalid params.csv file!");
		}
	}

}

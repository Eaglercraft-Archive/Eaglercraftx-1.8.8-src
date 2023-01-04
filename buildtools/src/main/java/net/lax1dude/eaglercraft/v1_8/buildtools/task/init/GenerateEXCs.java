package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.buildtools.decompiler.ParameterSplitter;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.CSVMappings.Param;
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
public class GenerateEXCs {

	public static boolean generateEXCs(File mcpDataTMP, File excOut, CSVMappings params) {
		System.out.println();
		System.out.println("Generating \"" + excOut.getName() + "\" from \"" + mcpDataTMP.getName() + "\"...");
		
		File paramsCSV = new File(mcpDataTMP, "params.csv");
		try(FileReaderUTF fr = new FileReaderUTF(paramsCSV)) {
			params.loadParamsFile(fr);
		}catch(IOException ex) {
			System.err.println("ERROR: failed to read \"" + paramsCSV.getAbsolutePath() + "\"!");
			ex.printStackTrace();
			return false;
		}
		
		Map<Integer,String> paramsTmp = new HashMap();
		Set<String> definedFunctions = new HashSet();

		int pcount = 0;
		int mcount = 0;
		int pgcount = 0;
		try(BufferedReader is = new BufferedReader(new FileReaderUTF(new File(mcpDataTMP, "joined.exc")));
				PrintWriter os = new PrintWriter(new FileWriterUTF(excOut));) {
			String s;
			while((s = is.readLine()) != null) {
				int idx = s.lastIndexOf('|');
				if(idx != -1) {
					String pfx = s.substring(0, idx);
					String func = null;
					int p1 = pfx.indexOf('(');
					if(p1 != -1) {
						func = pfx.substring(0, p1);
						func = pfx.substring(func.lastIndexOf('.') + 1);
					}
					if(func != null) {
						definedFunctions.add(func);
					}
					if(idx != s.length() - 1) {
						paramsTmp.clear();
						String[] prms = s.substring(idx + 1).split(",");
						String[] nprms = new String[prms.length];
						int lpc = 0;
						for(int i = 0; i < prms.length; ++i) {
							Param p = params.csvParamsMappings.get(prms[i]);
							if(p != null) {
								nprms[i] = p.name;
								++pcount;
								++lpc;
							}
						}
						if(lpc != prms.length) {
							if(p1 != -1) {
								String sig = pfx.substring(p1);
								sig = sig.substring(0, sig.indexOf('='));
								pgcount += ParameterSplitter.getParameterArray(sig, nprms);
							}
							for(int i = 0; i < nprms.length; ++i) {
								if(nprms[i] == null) {
									nprms[i] = "param0" + i;
								}
							}
						}
						s = pfx + "|" + String.join(",", nprms);
					}else if(func != null) {
						int idxx = func.indexOf('_');
						int idxx2 = func.lastIndexOf('_');
						if(idxx2 > idxx) {
							func = func.substring(0, idxx2 - 1);
						}
						Param[] pars = params.csvParamsForFunction.get(func);
						String sig = null;
						if(p1 != -1) {
							sig = pfx.substring(p1);
							sig = sig.substring(0, sig.indexOf('='));
						}
						if(pars == null) {
							if(sig != null) {
								String[] sg = ParameterSplitter.getParameterSigArray(sig, "par");
								if(sg != null) {
									s = pfx + "|" + String.join(",", sg);
								}
								pgcount += sg.length;
							}
						}else {
							int notNullLen = 0;
							for(int i = 0; i < pars.length; ++i) {
								if(pars[i] != null) {
									++notNullLen;
								}
							}
							String[] sg = new String[notNullLen];
							notNullLen = 0;
							for(int i = 0; i < pars.length; ++i) {
								if(pars[i] != null) {
									sg[notNullLen++] = pars[i].name;
									++pcount;
								}
							}
							s = pfx + "|" + String.join(",", sg);
						}
					}
				}
				int idx3 = s.indexOf('(');
				if(idx3 != -1) {
					int idx4 = s.lastIndexOf('.', idx3);
					if(idx4 != -1) {
						String func = s.substring(idx4 + 1, idx3);
						Symbol rp = params.csvMethodsMappings.get(func);
						if(rp != null) {
							String pfx = s.substring(0, idx4);
							String pofx = s.substring(idx3);
							s = pfx + "." + rp.name + pofx;
							++mcount;
						}
					}
				}
				os.println(s);
			}
			os.println();
			os.println("# auto generated entries start here:");
			try(BufferedReader iss = new BufferedReader(new FileReaderUTF(new File(mcpDataTMP, "joined.srg")))) {
				while((s = iss.readLine()) != null) {
					if(s.startsWith("MD:")) {
						int idx = s.lastIndexOf(' ');
						if(idx > 0) {
							int idx2 = s.lastIndexOf(' ', idx - 1);
							String fname = s.substring(idx2 + 1, idx);
							String fnameShort = fname;
							String fsig = s.substring(idx + 1);
							fnameShort = fname.substring(fname.lastIndexOf('/') + 1);
							int idx3 = fnameShort.lastIndexOf('_');
							if(idx3 != -1 && fnameShort.lastIndexOf('_', idx3 - 1) > 0) {
								fnameShort = fnameShort.substring(0, idx3);
							}
							if(definedFunctions.add(fnameShort)) {
								String[] sg = ParameterSplitter.getParameterSigArray(fsig, "par");
								Param[] pars = params.csvParamsForFunction.get(fnameShort);
								if(pars != null) {
									int notNullLen = 0;
									for(int i = 0; i < pars.length; ++i) {
										if(pars[i] != null) {
											++notNullLen;
										}
									}
									if(notNullLen > 0) {
										notNullLen = 0;
										for(int i = 0; i < pars.length; ++i) {
											if(pars[i] != null) {
												int ii = notNullLen++;
												if(ii < sg.length) {
													sg[ii] = pars[i].name;
													++pcount;
												}
											}
										}
										int idx4 = fname.lastIndexOf('/');
										String ppfx = fname.substring(0, idx4);
										String ppfunc = fname.substring(idx4 + 1);
										Symbol rp = params.csvMethodsMappings.get(ppfunc);
										if(rp != null) {
											ppfunc = rp.name;
											++mcount;
										}
										fname = ppfx + "." + ppfunc;
										os.println(fname + fsig + "=|" + String.join(",", sg));
									}
								}else {
									if(sg != null) {
										if(sg.length > 0) {
											pgcount += sg.length;
											int idx4 = fname.lastIndexOf('/');
											String ppfx = fname.substring(0, idx4);
											String ppfunc = fname.substring(idx4 + 1);
											Symbol rp = params.csvMethodsMappings.get(ppfunc);
											if(rp != null) {
												ppfunc = rp.name;
												++mcount;
											}
											fname = ppfx + "." + ppfunc;
											os.println(fname + fsig + "=|" + String.join(",", sg));
										}
									}
								}
							}
						}
					}
				}
			}
		}catch(IOException ex) {
			System.err.println("ERROR: failed to write \"" + excOut.getName() + "\" from \"joined.exc\"!");
			ex.printStackTrace();
			return false;
		}

		System.out.println("   - Deobf " + pcount + " params to \"" + excOut.getName() + "\"");
		System.out.println("   - Generate " + pgcount + " params to \"" + excOut.getName() + "\"");
		
		return true;
	}
	
}

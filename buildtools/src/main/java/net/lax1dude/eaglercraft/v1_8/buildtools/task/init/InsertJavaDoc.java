package net.lax1dude.eaglercraft.v1_8.buildtools.task.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import net.lax1dude.eaglercraft.v1_8.buildtools.EaglerBuildTools;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.diff.Lines;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.CSVMappings.Symbol;
import net.lax1dude.eaglercraft.v1_8.buildtools.util.FileReaderUTF;

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
public class InsertJavaDoc {
	
	public static final String enumImport = "import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;";

	private static final String[] typeModifiersFields = new String[] { 
			"public", "private", "protected", "static", 
			"final", "volatile", "transient"
	};
	
	private static boolean isTypeModifierField(String tk) {
		for(int i = 0; i < typeModifiersFields.length; ++i) {
			if(typeModifiersFields[i].equals(tk)) {
				return true;
			}
		}
		return false;
	}
	
	private static final String[] typeModifiersMethods = new String[] { 
			"public", "private", "protected", "static", 
			"final", "synchronized", "abstract", "default"
	};
	
	private static final Pattern illegalCharactersNotATypeName = Pattern.compile("[^a-zA-Z0-9_\\-\\$\\[\\]<>\\.]");
	
	private static boolean isTypeModifierMethod(String tk) {
		for(int i = 0; i < typeModifiersMethods.length; ++i) {
			if(typeModifiersMethods[i].equals(tk)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean processSource(File fileIn, File fileOut, File mcpDataTMP, CSVMappings csv) throws Throwable {
		return processSource(fileIn, fileOut, mcpDataTMP, csv, true);
	}
		
	public static boolean processSource(File fileIn, File fileOut, File mcpDataTMP, CSVMappings csv, boolean compress) throws Throwable {
		System.out.println("Adding javadoc...");
		
		if(csv == null) {
			System.out.println("(writing enums only, skipping field/method annotations)");
		}
		
		//RealOpenGLEnums.initEnums();
		//System.out.println("Loaded " + RealOpenGLEnums.enumNames.size() + " OpenGL enums");
		
		List<String> copyrightComment = null;
		try(BufferedReader is = new BufferedReader(new FileReaderUTF(new File(EaglerBuildTools.repositoryRoot, "patches/minecraft/output_license.txt")))) {
			copyrightComment = new ArrayList();
			copyrightComment.add("/**+");
			String ln;
			while((ln = is.readLine()) != null) {
				copyrightComment.add(" * " + ln);
			}
			copyrightComment.add(" * ");
			copyrightComment.add(" */");
		}
		
		Map<String, List<Symbol>> methodsInClasses = new HashMap();
		Map<String, List<Symbol>> fieldsInClasses = new HashMap();
		
		if(csv != null) {
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
			
			try(BufferedReader is = new BufferedReader(new FileReaderUTF(new File(mcpDataTMP, "joined.srg")))) {
				String s;
				while((s = is.readLine()) != null) {
					if(s.startsWith("MD:")) {
						s = s.trim();
						int idxx = s.lastIndexOf(' ');
						int idxx2 = s.lastIndexOf(' ', idxx - 1);
						s = s.substring(idxx2 + 1, idxx);
						idxx = s.lastIndexOf('/');
						String s1 = s.substring(0, idxx);
						String s2 = s.substring(idxx + 1);
						Symbol sm = csv.csvMethodsMappings.get(s2);
						if(sm != null && sm.comment != null && sm.comment.length() > 0) {
							List<Symbol> sbls = methodsInClasses.get(s1);
							if(sbls == null) {
								methodsInClasses.put(s1, sbls = new ArrayList());
							}
							sbls.add(sm);
						}
					}else if(s.startsWith("FD:")) {
						s = s.trim();
						int idxx = s.lastIndexOf(' ');
						s = s.substring(idxx + 1);
						idxx = s.lastIndexOf('/');
						String s1 = s.substring(0, idxx);
						String s2 = s.substring(idxx + 1);
						Symbol sm = csv.csvFieldsMappings.get(s2);
						if(sm != null && sm.comment != null && sm.comment.length() > 0) {
							List<Symbol> sbls = fieldsInClasses.get(s1);
							if(sbls == null) {
								fieldsInClasses.put(s1, sbls = new ArrayList());
							}
							sbls.add(sm);
						}
					}
				}
			}
		}
		
		OpenGLEnumManager.loadEnumMap();
		
		System.out.print("   ");
		int xt = 0;
		int modm = 0;
		int modf = 0;
		final int[] enums = new int[1];
		
		Consumer<Integer> enumCounter = new Consumer<Integer>() {
			@Override
			public void accept(Integer t) {
				enums[0] += t.intValue();
			}
		};
		
		try(ZipInputStream jarIn = new ZipInputStream(new FileInputStream(fileIn)); 
				ZipOutputStream jarOut = new ZipOutputStream(new FileOutputStream(fileOut))) {
			jarOut.setLevel(compress ? 5 : 0);
			jarOut.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			jarOut.write("Manifest-Version: 1.0\nCreated-By: Eaglercraft BuildTools\n".getBytes(StandardCharsets.UTF_8));
			ZipEntry et;
			String nm;
			while((et = jarIn.getNextEntry()) != null) {
				if(et.isDirectory()) {
					continue;
				}
				nm = et.getName();
				if(nm.endsWith(".java")) {
					String fs = IOUtils.toString(jarIn, "UTF-8");
					List<String> linesLst = new ArrayList();
					linesLst.addAll(Lines.linesList(fs));
					if(copyrightComment != null) {
						for(int i = 0; i < linesLst.size(); ++i) {
							String ln = linesLst.get(i);
							if(!ln.startsWith("import")) {
								if(ln.startsWith("public")) {
									ln = ln.substring(6).trim();
								}
								if(ln.startsWith("class") || ln.startsWith("enum") || ln.startsWith("interface") || ln.startsWith("@interface")) {
									linesLst.addAll(i, copyrightComment);
									i += copyrightComment.size();
									break;
								}
							}
						}
					}
					
					String cnm = nm.substring(0, nm.length() - 5);
					List<Symbol> meths = csv == null ? null : methodsInClasses.get(cnm);
					List<Symbol> fields = csv == null ? null : fieldsInClasses.get(cnm);
					if(meths != null || fields != null) {
						for(int i = 0; i < linesLst.size(); ++i) {
							String ln2 = linesLst.get(i);
							boolean notMethod = ln2.endsWith(";");
							String ln = ln2;
							String indent = "";
							while(ln.length() > 0 && Character.isWhitespace(ln.charAt(0))) {
								indent += ln.charAt(0);
								ln = ln.substring(1);
							}
							String[] tokens = ln.split("\\s+");
							boolean hasTypeDecl = false;
							boolean hasMethodDecl = false;
							boolean hasType = false;
							for(int j = 0; j < tokens.length; ++j) {
								if(tokens[j].length() > 0) {
									boolean b1 = false;
									boolean b2 = false;
									if(isTypeModifierField(tokens[j])) {
										b1 = true;
										hasTypeDecl = true;
									}
									if(!notMethod && isTypeModifierMethod(tokens[j])) {
										b2 = true;
										hasMethodDecl = true;
									}
									if(b1 || b2) {
										continue;
									}else if(!hasType) {
										if(illegalCharactersNotATypeName.matcher(tokens[j]).find()) {
											break;
										}else {
											hasType = true;
											continue;
										}
									}
									int idx = 0;
									if(hasTypeDecl && j < tokens.length - 1 && tokens[j + 1].equals("=")) {
										if(fields != null) {
											for(int k = 0, l = fields.size(); k < l; ++k) {
												Symbol ss = fields.get(k);
												if(ss.name.equals(tokens[j])) {
													List<String> lines = wordWrapComment(ss.comment, indent);
													linesLst.addAll(i, lines);
													i += lines.size();
													++modf;
													break;
												}
											}
										}
									}else if(((idx = tokens[j].indexOf('(')) != -1 && j > 0) || hasMethodDecl) {
										if(meths != null) {
											if(idx > 0) {
												String sss = tokens[j].substring(0, idx);
												for(int k = 0, l = meths.size(); k < l; ++k) {
													Symbol ss = meths.get(k);
													if(ss.name.equals(sss)) {
														List<String> lines = wordWrapComment(ss.comment, indent);
														linesLst.addAll(i, lines);
														i += lines.size();
														++modm;
														break;
													}
												}
											}
										}
									}
									break;
								}
							}
						}
					}
					int cnt0 = enums[0];
					for(int i = 0, l = linesLst.size(); i < l; ++i) {
						linesLst.set(i, OpenGLEnumManager.insertIntoLine(linesLst.get(i), enumCounter));
					}
					
					if(cnt0 != enums[0]) {
						for(int i = 0, l = linesLst.size(); i < l; ++i) {
							String line = linesLst.get(i);
							if(line.startsWith("package")) {
								linesLst.addAll(i + 1, Arrays.asList("", enumImport));
								break;
							}
						}
					}
					
					ZipEntry z2 = new ZipEntry(nm);
					jarOut.putNextEntry(z2);
					IOUtils.write(String.join(System.lineSeparator(), linesLst), jarOut, "UTF-8");
					++xt;
					if(xt % 75 == 74) {
						System.out.print(".");
					}
				}else {
					if(!nm.startsWith("META-INF")) {
						ZipEntry z2 = new ZipEntry(nm);
						jarOut.putNextEntry(z2);
						IOUtils.copy(jarIn, jarOut, 4096);
					}
				}
			}
		}catch(IOException ex) {
			System.err.println("Failed to process jar '" + fileIn.getName() + "' and write it to '" + fileOut.getName() + "!");
			ex.printStackTrace();
			return false;
		}
		
		System.out.println();
		System.out.println("Added " + enums[0] + " OpenGL enums");
		if(csv != null) {
			System.out.println("Added " + modm + " comments to methods");
			System.out.println("Added " + modf + " comments to fields");
		}
		System.out.println();
		
		return true;
	}
	
	private static List<String> wordWrapComment(String strIn, String indent) {
		String[] wds = strIn.split("\\s+");
		List<String> ret = new ArrayList();
		ret.add(indent + "/**+");
		String ln = "";
		for(int i = 0; i < wds.length; ++i) {
			if(ln.length() > 0 && wds[i].length() + ln.length() > 60) {
				ret.add(indent + " * " + ln);
				ln = "";
			}
			ln += ln.length() > 0 ? " " + wds[i] : wds[i];
		}
		if(ln.length() > 0) {
			ret.add(indent + " * " + ln);
		}
		ret.add(indent + " */");
		return ret;
	}
	
	public static String stripDocForDiff(String fileIn) {
		List<String> linesIn = Lines.linesList(fileIn);
		OpenGLEnumManager.loadEnumMap();
		List<String> linesOut = new ArrayList();
		boolean addOpenGLImport = false;
		for(int i = 0, l = linesIn.size(); i < l; ++i) {
			String line = linesIn.get(i);
			if(line.trim().startsWith("/**+")) {
				for(; i < l; ++i) {
					if(linesIn.get(i).endsWith("*/")) {
						break;
					}
				}
			}else {
				String line2 = OpenGLEnumManager.stripFromLine(line);
				if(line2 != null) {
					linesOut.add(line2);
					addOpenGLImport = true;
				}else {
					linesOut.add(line);
				}
			}
		}
		
		if(addOpenGLImport) {
			int idx = linesOut.indexOf(enumImport);
			if(idx != -1) {
				if(idx - 1 >= 0 && linesOut.get(idx - 1).trim().length() == 0 && linesOut.size() > 1) {
					idx -= 1;
					linesOut.remove(idx);
					linesOut.remove(idx);
				}else {
					linesOut.remove(idx);
				}
			}
		}
		
		return String.join(System.lineSeparator(), linesOut);
	}
	
}

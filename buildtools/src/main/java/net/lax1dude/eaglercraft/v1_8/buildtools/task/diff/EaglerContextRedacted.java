package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.ChangeDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.DeleteDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.InsertDelta;
import com.github.difflib.patch.Patch;

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
public class EaglerContextRedacted {

	public static void writeContextRedacted(Patch<String> patch, PrintWriter output) {
		Date theDate = new Date();
		
		output.println();
		output.println("# Eagler Context Redacted Diff");
		output.println("# Copyright (c) " + (new SimpleDateFormat("yyyy")).format(theDate) + " lax1dude. All rights reserved.");
		output.println();
		output.println("# Version: 1.0");
		output.println("# Author: lax1dude");
		output.println();

		int lastSourcePos = 0;
		int lastTargetPos = 0;
		List<AbstractDelta<String>> deltas = patch.getDeltas();
		delta_itr: for(int i = 0, l = deltas.size(); i < l; ++i) {
			AbstractDelta<String> delta = deltas.get(i);
			
			DeltaType type = delta.getType();
			String blockType;
			String blockPrefix;
			switch(type) {
			case CHANGE:
				blockType = "> CHANGE";
				blockPrefix = "~ ";
				break;
			case DELETE:
				blockType = "> DELETE";
				blockPrefix = "- ";
				break;
			case EQUAL:
				continue delta_itr;
			case INSERT:
				blockType = "> INSERT";
				blockPrefix = "+ ";
				break;
			default:
				throw new IllegalArgumentException("Invalid type " + type + " for delta " + i);
			}
			
			Chunk<String> source = delta.getSource();
			int sourcePos = source.getPosition();
			int sourceLen = source.getLines().size();
			
			int sourcePosRelative = sourcePos - lastSourcePos;
			
			Chunk<String> target = delta.getTarget();
			int targetPos = target.getPosition();
			List<String> linesToWrite = target.getLines();
			int targetLen = linesToWrite.size();
			
			int targetPosRelative = targetPos - lastTargetPos;
			
			output.println(blockType + "  " + targetPosRelative + (targetLen > 0 ? " : " + (targetPosRelative + targetLen) : "") + "  @  "
					+ sourcePosRelative + (sourceLen > 0 ? " : " + (sourcePosRelative + sourceLen) : ""));
			
			output.println();
			
			if(targetLen > 0) {
				for(int j = 0, ll = linesToWrite.size(); j < ll; ++j) {
					output.println(blockPrefix + linesToWrite.get(j));
				}
				
				output.println();
			}
			
			lastSourcePos = sourcePos + sourceLen;
			lastTargetPos = targetPos + targetLen;
		}
		
		output.println("> EOF");
	}

	public static Patch<String> readContextRestricted(List<String> context, BufferedReader reader) throws IOException {
		Patch<String> newPatch = new Patch();
		
		DeltaType currentDeltaType = null;
		int sourceStart = 0;
		int sourceLen = 0;
		int targetStart = 0;
		int targetLen = 0;
		List<String> targetLines = null;
		
		int lastSourcePos = 0;
		int lastTargetPos = 0;
		String line;
		readLinesLoop: while((line = reader.readLine()) != null) {
			if(line.length() < 2) {
				continue;
			}
			
			if(line.charAt(1) != ' ') {
				throw new IOException("Unknown line type: " + line.substring(0, 2));
			}
			
			char lineType = line.charAt(0);
			String value = line.substring(2);
			
			switch(lineType) {
			case '#':
				int idx = value.indexOf(':');
				if(idx > 0) {
					String k = value.substring(0, idx).trim().toLowerCase();
					if(k.equals("version")) {
						String v = value.substring(idx + 1).trim();
						if(!v.equals("1.0")) {
							throw new IOException("Unsupported format version: " + v);
						}
					}
				}
				break;
			case '>':
				
				String[] split = value.trim().split("[\\s]+");
				
				if(split.length == 1 && split[0].equals("EOF")) {
					break readLinesLoop;
				}
				
				if(split.length < 4 ||
						!((split[2].equals("@") && (split.length == 4 || (split.length == 6 && split[4].equals(":")))) ||
						(split[2].equals(":") && ((split.length == 6 && split[4].equals("@")) || (split.length == 8 && split[4].equals("@") && split[6].equals(":")))))) {
					throw new IOException("Invalid block: [ " + String.join(" ", split) + " ]");
				}
				
				if(currentDeltaType != null) {
					lastSourcePos += sourceStart;
					lastTargetPos += targetStart;
					newPatch.addDelta(makeDelta(currentDeltaType, lastSourcePos, sourceLen, lastTargetPos, targetLen, context, targetLines));
					lastSourcePos += sourceLen;
					lastTargetPos += targetLen;
				}
				
				switch(split[0]) {
				case "CHANGE":
					currentDeltaType = DeltaType.CHANGE;
					break;
				case "DELETE":
					currentDeltaType = DeltaType.DELETE;
					break;
				case "INSERT":
					currentDeltaType = DeltaType.INSERT;
					break;
				default:
					throw new IOException("Unknown line block: " + split[0]);
				}
				
				targetLines = null;
				
				targetStart = parseInt(split[1]);
				
				if(split[2].equals(":")) {
					targetLen = parseInt(split[3]) - targetStart;
					sourceStart = parseInt(split[5]);
					if(split.length == 8) {
						sourceLen = parseInt(split[7]) - sourceStart;
					}else {
						sourceLen = 0;
					}
				}else {
					targetLen = 0;
					sourceStart = parseInt(split[3]);
					if(split.length == 6) {
						sourceLen = parseInt(split[5]) - sourceStart;
					}else {
						sourceLen = 0;
					}
				}
				break;
			case '~':
				if(currentDeltaType != DeltaType.CHANGE) {
					throw new IOException("Read an unexpected CHANGE line in a " + currentDeltaType + " block: " + line);
				}else {
					if(targetLines == null) targetLines = new ArrayList();
					targetLines.add(value);
				}
				break;
			case '-':
				if(currentDeltaType != DeltaType.DELETE) {
					throw new IOException("Read an unexpected DELETE line in a " + currentDeltaType + " block: " + line);
				}else {
					if(targetLines == null) targetLines = new ArrayList();
					targetLines.add(value);
				}
				break;
			case '+':
				if(currentDeltaType != DeltaType.INSERT) {
					throw new IOException("Read an unexpected INSERT line in a " + currentDeltaType + " block: " + line);
				}else {
					if(targetLines == null) targetLines = new ArrayList();
					targetLines.add(value);
				}
				break;
			default:
				throw new IOException("Unknown line type: " + lineType);
			}
		}
		
		if(currentDeltaType != null) {
			lastSourcePos += sourceStart;
			lastTargetPos += targetStart;
			newPatch.addDelta(makeDelta(currentDeltaType, lastSourcePos, sourceLen, lastTargetPos, targetLen, context, targetLines));
			lastSourcePos += sourceLen;
			lastTargetPos += targetLen;
		}
		
		return newPatch;
	}

	private static int parseInt(String str) throws IOException {
		try {
			return Integer.parseInt(str);
		}catch(NumberFormatException ex) {
			throw new IOException("Value is not a valid integer: \"" + str + "\"");
		}
	}

	private static AbstractDelta<String> makeDelta(DeltaType deltaType, int sourceStart, int sourceLen,
			int targetStart, int targetLen, List<String> context, List<String> targetLines) throws IOException {
		List<String> sourceLines = new ArrayList(sourceLen);
		for(int i = 0; i < sourceLen; ++i) {
			sourceLines.add(context.get(sourceStart + i));
		}
		if(targetLines == null) {
			targetLines = new ArrayList(0);
		}
		if(targetLen != targetLines.size()) {
			throw new IOException("" + deltaType + " block at sourceStart " + sourceStart + " is " + targetLen
					+ " lines long but only " + targetLines.size() + " lines were read!");
		}
		switch(deltaType) {
		case CHANGE:
			return new ChangeDelta(new Chunk(sourceStart, sourceLines), new Chunk(targetStart, targetLines));
		case DELETE:
			return new DeleteDelta(new Chunk(sourceStart, sourceLines), new Chunk(targetStart, targetLines));
		case INSERT:
			return new InsertDelta(new Chunk(sourceStart, sourceLines), new Chunk(targetStart, targetLines));
		default:
			throw new IllegalArgumentException("Invalid delta type: " + deltaType);
		}
	}

}

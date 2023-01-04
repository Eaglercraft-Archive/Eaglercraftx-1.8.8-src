package net.lax1dude.eaglercraft.v1_8.buildtools.decompiler;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

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
public class ParameterSplitter extends SignatureVisitor {
	
	protected ParameterSplitter() {
		super(Opcodes.ASM5);
	}
	
	protected static final ArrayList<LocalVariableGenerator> ret = new ArrayList();
	protected static final HashMap<String,Integer> usedLocals = new HashMap();
	
	public static int getParameterArray(String sig, String[] input) {
		SignatureReader rd = new SignatureReader(sig);
		ParameterSplitter pms = new ParameterSplitter();
		ret.clear();
		usedLocals.clear();
		rd.accept(pms);
		int l = ret.size();
		if(l > input.length) {
			l = input.length;
		}
		int c = 0;
		for(int i = 0; i < l; ++i) {
			if(input[i] == null) {
				input[i] = LocalVariableGenerator.nextLocalVariableName(usedLocals, ret.get(i), "par");
				++c;
			}
		}
		return c;
	}
	
	public static String[] getParameterSigArray(String sig, String pfx) {
		SignatureReader rd = new SignatureReader(sig);
		ParameterSplitter pms = new ParameterSplitter();
		ret.clear();
		usedLocals.clear();
		rd.accept(pms);
		String[] r = new String[ret.size()];
		for(int i = 0; i < r.length; ++i) {
			r[i] = LocalVariableGenerator.nextLocalVariableName(usedLocals, ret.get(i), pfx);
		}
		return r;
	}
	
	@Override
	public SignatureVisitor visitParameterType() {
		LocalVariableGenerator lv = new LocalVariableGenerator();
		ret.add(lv);
		return lv;
	}
	
	@Override public SignatureVisitor visitClassBound() { return LocalVariableGenerator.nopVisitor; }
	@Override public SignatureVisitor visitExceptionType() { return LocalVariableGenerator.nopVisitor; }
	@Override public SignatureVisitor visitInterface() { return LocalVariableGenerator.nopVisitor; }
	@Override public SignatureVisitor visitInterfaceBound() { return LocalVariableGenerator.nopVisitor; }
	@Override public SignatureVisitor visitTypeArgument(char wildcard) { return LocalVariableGenerator.nopVisitor; }
	@Override public SignatureVisitor visitReturnType() { return LocalVariableGenerator.nopVisitor; }
	@Override public SignatureVisitor visitArrayType() { return LocalVariableGenerator.nopVisitor; }
	@Override public void visitBaseType(char descriptor) { }
	@Override public void visitClassType(String name) { }
	@Override public void visitEnd() { }
	@Override public void visitFormalTypeParameter(String name) { }
	@Override public void visitInnerClassType(String name) { }
	@Override public SignatureVisitor visitSuperclass() { return LocalVariableGenerator.nopVisitor; }
	@Override public void visitTypeArgument() { }
	@Override public void visitTypeVariable(String name) { }
	
}

package net.lax1dude.eaglercraft.v1_8.buildtools.decompiler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class LocalVariableGenerator extends SignatureVisitor {
	
	public static final Map<Character,String> primitiveNames = new HashMap();
	public static final Set<String> illegalVariableNames = new HashSet();
	
	static {
		
		primitiveNames.put('Z', "Flag");
		primitiveNames.put('C', "Char");
		primitiveNames.put('B', "Byte");
		primitiveNames.put('S', "Short");
		primitiveNames.put('I', "Int");
		primitiveNames.put('F', "Float");
		primitiveNames.put('J', "Long");
		primitiveNames.put('D', "Double");
		primitiveNames.put('V', "Void");
		
		illegalVariableNames.addAll(Arrays.asList(
			"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
			"continue", "const", "default", "do", "double", "else", "enum", "exports", "extends",
			"final", "finally", "float", "for", "goto", "if", "implements", "import",
			"instanceof", "int", "interface", "long", "native", "new", "package", "private",
			"protected", "public", "return", "short", "static", "strictfp", "super", "switch",
			"synchronized", "this", "throw", "throws", "transient", "try", "var", "void",
			"volatile", "while", "string"
		));
		
	}

	private String baseClass = null;
	private boolean isArray = false;
	private String typeParam1 = null;
	private boolean typeParam1IsArray = false;
	private String typeParam2 = null;
	private boolean typeParam2IsArray = false;
	
	public static final SignatureVisitor nopVisitor = new SignatureVisitor(Opcodes.ASM5) {};
	
	LocalVariableGenerator() {
		super(Opcodes.ASM5);
	}

	public static String createName(String sig) {
		SignatureReader rd = new SignatureReader(sig);
		LocalVariableGenerator gen = new LocalVariableGenerator();
		rd.acceptType(gen);
		return gen.getResult();
	}
	
	private String removePath(String in) {
		int idx = in.lastIndexOf('/');
		int idx2 = in.lastIndexOf('$');
		if(idx2 > idx && idx2 != in.length() - 1) {
			idx = idx2;
		}
		if(idx != -1) {
			in = in.substring(idx + 1);
		}
		if(in.length() == 0 || Character.isDigit(in.charAt(0))) {
			in = "obj" + in;
		}
		return in;
	}
	
	String getResult() {
		String rt;
		if(baseClass == null) {
			rt = "Object";
		}else {
			rt = removePath(baseClass);
		}
		if(typeParam1 == null && typeParam2 == null) {
			if(isArray) {
				rt = "ArrayOf" + rt;
			}
		}else {
			if(isArray) {
				rt = rt + "Array";
			}
		}
		if(typeParam1 != null && typeParam2 == null) {
			if(typeParam1IsArray) {
				typeParam1 = typeParam1 + "Array";
			}
			rt = rt + "Of" + removePath(typeParam1);
		}else if(typeParam1 != null && typeParam2 != null) {
			if(typeParam1IsArray) {
				typeParam1 = typeParam1 + "Array";
			}
			if(typeParam2IsArray) {
				typeParam2 = typeParam2 + "Array";
			}
			rt = rt + "Of" + removePath(typeParam1) + "And" + removePath(typeParam2);
		}
		return rt;
	}

	@Override
	public SignatureVisitor visitArrayType() {
		if(baseClass == null) {
			isArray = true;
			return new ArrayTypeVisitor();
		}else {
			return nopVisitor;
		}
	}
	
	private class ArrayTypeVisitor extends SignatureVisitor {
		
		protected ArrayTypeVisitor() {
			super(Opcodes.ASM5);
		}
		
		@Override
		public void visitBaseType(char descriptor) {
			if(baseClass == null) {
				baseClass = primitiveNames.get(descriptor);
			}
		}
		
		@Override
		public void visitClassType(String name) {
			if(baseClass == null) {
				baseClass = name;
			}
		}
		
		@Override
		public SignatureVisitor visitArrayType() {
			if(baseClass == null) {
				baseClass = "array";
			}
			return nopVisitor;
		}
		
		@Override public SignatureVisitor visitClassBound() { return nopVisitor; }
		@Override public SignatureVisitor visitExceptionType() { return nopVisitor; }
		@Override public SignatureVisitor visitInterface() { return nopVisitor; }
		@Override public SignatureVisitor visitInterfaceBound() { return nopVisitor; }
		@Override public SignatureVisitor visitParameterType() { return nopVisitor; }
		@Override public SignatureVisitor visitTypeArgument(char wildcard) { return nopVisitor; }
		@Override public SignatureVisitor visitReturnType() { return nopVisitor; }
		
	}

	@Override
	public void visitBaseType(char descriptor) {
		if(baseClass == null) {
			baseClass = primitiveNames.get(descriptor);
		}
	}

	@Override
	public SignatureVisitor visitClassBound() {
		//System.out.println("class: " + this);
		return nopVisitor;
	}

	@Override
	public void visitClassType(String name) {
		//System.out.println("classType: " + name);
		if(baseClass == null) {
			baseClass = name;
		}
	}

	@Override
	public void visitEnd() {
		
	}

	@Override
	public SignatureVisitor visitExceptionType() {
		return nopVisitor;
	}

	@Override
	public void visitFormalTypeParameter(String name) {
		//System.out.println("formalTypeParam: " + name);
	}

	@Override
	public void visitInnerClassType(String name) {
		//System.out.println("innerClassType: " + name);
		
	}

	@Override
	public SignatureVisitor visitInterface() {
		return nopVisitor;
	}

	@Override
	public SignatureVisitor visitInterfaceBound() {
		return nopVisitor;
	}

	@Override
	public SignatureVisitor visitParameterType() {
		return nopVisitor;
	}
	
	private class TypeParamVisitor extends SignatureVisitor {
		
		private boolean hasVisited = false;
		private final int firstOrSecond;

		protected TypeParamVisitor(int firstOrSecond) {
			super(Opcodes.ASM5);
			this.firstOrSecond = firstOrSecond;
		}
		
		@Override
		public void visitBaseType(char descriptor) {
			if(!hasVisited) {
				if(firstOrSecond == 1) {
					if(typeParam1 == null) {
						typeParam1 = primitiveNames.get(descriptor);
					}
				}else if(firstOrSecond == 2) {
					if(typeParam2 == null) {
						typeParam2 = primitiveNames.get(descriptor);
					}
				}
				hasVisited = true;
			}
		}
		
		@Override
		public void visitClassType(String name) {
			if(!hasVisited) {
				if(firstOrSecond == 1) {
					if(typeParam1 == null) {
						typeParam1 = name;
					}
				}else if(firstOrSecond == 2) {
					if(typeParam2 == null) {
						typeParam2 = name;
					}
				}
				hasVisited = true;
			}
		}

		@Override
		public SignatureVisitor visitArrayType() {
			if(!hasVisited) {
				if(firstOrSecond == 1) {
					if(typeParam1 == null) {
						typeParam1IsArray = true;
						return new TypeParamArrayVisitor();
					}
				}else if(firstOrSecond == 2) {
					if(typeParam2 == null) {
						typeParam2IsArray = true;
						return new TypeParamArrayVisitor();
					}
				}
			}
			return nopVisitor;
		}
		
		private class TypeParamArrayVisitor extends SignatureVisitor {

			protected TypeParamArrayVisitor() {
				super(Opcodes.ASM5);
			}
			
			@Override
			public void visitBaseType(char descriptor) {
				if(!hasVisited) {
					if(firstOrSecond == 1) {
						if(typeParam1 == null) {
							typeParam1 = primitiveNames.get(descriptor);
						}
					}else if(firstOrSecond == 2) {
						if(typeParam2 == null) {
							typeParam2 = primitiveNames.get(descriptor);
						}
					}
					hasVisited = true;
				}
			}
			
			@Override
			public void visitClassType(String name) {
				if(!hasVisited) {
					if(firstOrSecond == 1) {
						if(typeParam1 == null) {
							typeParam1 = name;
						}
					}else if(firstOrSecond == 2) {
						if(typeParam2 == null) {
							typeParam2 = name;
						}
					}
					hasVisited = true;
				}
			}
			
			@Override
			public SignatureVisitor visitArrayType() {
				if(!hasVisited) {
					if(firstOrSecond == 1) {
						if(typeParam1 == null) {
							typeParam1 = "array";
						}
					}else if(firstOrSecond == 2) {
						if(typeParam2 == null) {
							typeParam2 = "array";
						}
					}
					hasVisited = true;
				}
				return nopVisitor;
			}
			
			@Override public SignatureVisitor visitClassBound() { return nopVisitor; }
			@Override public SignatureVisitor visitExceptionType() { return nopVisitor; }
			@Override public SignatureVisitor visitInterface() { return nopVisitor; }
			@Override public SignatureVisitor visitInterfaceBound() { return nopVisitor; }
			@Override public SignatureVisitor visitParameterType() { return nopVisitor; }
			@Override public SignatureVisitor visitTypeArgument(char wildcard) { return nopVisitor; }
			@Override public SignatureVisitor visitReturnType() { return nopVisitor; }
			
		}
		
	}

	@Override
	public SignatureVisitor visitReturnType() {
		return nopVisitor;
	}

	@Override
	public SignatureVisitor visitSuperclass() {
		return nopVisitor;
	}

	@Override
	public void visitTypeArgument() {
		
	}

	@Override
	public SignatureVisitor visitTypeArgument(char wildcard) {
		if(typeParam1 == null) {
			return new TypeParamVisitor(1);
		}else if(typeParam2 == null) {
			return new TypeParamVisitor(2);
		}else {
			return nopVisitor;
		}
	}

	@Override
	public void visitTypeVariable(String name) {
		
	}
	
	public static String nextLocalVariableNameFromString(Map<String,Integer> tmpLocalsMap, String signature, String pfx) {
		String str = signature.length() == 1 ? primitiveNames.get(signature.charAt(0)) : null;
		if(str == null) {
			str = LocalVariableGenerator.createName(signature);
		}
		String ls = str.toLowerCase();
		Integer i = tmpLocalsMap.get(ls);
		if(i == null) {
			tmpLocalsMap.put(ls, 1);
			if(Character.isDigit(str.charAt(str.length() - 1))) {
				str = str + "_1";
			}else {
				str = illegalVariableNames.contains(str.toLowerCase()) ? str + "1" : str;
			}
		}else {
			int ii = i.intValue() + 1;
			tmpLocalsMap.put(ls, ii);
			if(Character.isDigit(str.charAt(str.length() - 1)) || str.contains("And") || str.length() > 16) {
				str = str + "_" + ii;
			}else {
				str = str + ii;
			}
		}
		return pfx == null ? camelCase(str) : pfx + str;
	}
	
	public static String nextLocalVariableName(Map<String,Integer> tmpLocalsMap, LocalVariableGenerator signature, String pfx) {
		String str = signature.getResult();
		String ls = str.toLowerCase();
		Integer i = tmpLocalsMap.get(ls);
		if(i == null) {
			tmpLocalsMap.put(ls, 1);
			if(Character.isDigit(str.charAt(str.length() - 1))) {
				str = str + "_1";
			}else {
				str = illegalVariableNames.contains(str.toLowerCase()) ? str + "1" : str;
			}
		}else {
			int ii = i.intValue() + 1;
			tmpLocalsMap.put(ls, ii);
			if(Character.isDigit(str.charAt(str.length() - 1)) || str.contains("And") || str.length() > 16) {
				str = str + "_" + ii;
			}else {
				str = str + ii;
			}
		}
		return pfx == null ? camelCase(str) : pfx + str;
	}
	
	public static String camelCase(String in) {
		if(in == null || in.length() <= 0) {
			return "name";
		}else {
			if(in.length() > 1 && Character.isUpperCase(in.charAt(0)) && Character.isUpperCase(in.charAt(1))) {
				return "var" + in;
			}else {
				return in.substring(0, 1).toLowerCase() + in.substring(1);
			}
		}
	}

}

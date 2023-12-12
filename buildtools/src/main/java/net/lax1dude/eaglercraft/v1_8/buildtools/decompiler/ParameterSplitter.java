package net.lax1dude.eaglercraft.v1_8.buildtools.decompiler;

import java.util.ArrayList;
import java.util.HashMap;

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

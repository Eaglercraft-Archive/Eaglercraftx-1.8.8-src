package net.lax1dude.eaglercraft.v1_8.internal.teavm.generators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.teavm.backend.javascript.codegen.ScopedName;
import org.teavm.backend.javascript.codegen.SourceWriter;
import org.teavm.backend.javascript.spi.Generator;
import org.teavm.backend.javascript.spi.GeneratorContext;
import org.teavm.model.MethodReference;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.Base64VarIntArray;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class TeaVMRuntimeDeobfuscatorGenerator implements Generator {

	private int indexIntoSet(String name, Map<String,Integer> namesSet, List<String> namesList) {
		Integer ret = namesSet.get(name);
		if(ret != null) {
			return ret.intValue();
		}
		int i = namesList.size();
		namesList.add(name);
		namesSet.put(name, i);
		return i;
	}

	@Override
	public void generate(GeneratorContext context, SourceWriter writer, MethodReference methodRef) throws IOException {
		Map<String,List<Integer>> map = new HashMap<>();
		List<String> classNamesPartsList = new ArrayList<>();
		Map<String,Integer> classNamesPartsSet = new HashMap<>();
		List<String> namesList = new ArrayList<>();
		Map<String,Integer> namesSet = new HashMap<>();
		Map<String,List<Integer>> namesEncSet = new HashMap<>();
		for(MethodReference method : context.getDependency().getReachableMethods()) {
			ScopedName name = writer.getNaming().getFullNameFor(method);
			if(name.scoped) {
				continue;
			}
			String clsName = method.getClassName();
			List<Integer> lst = map.get(clsName);
			if(lst == null) {
				map.put(clsName, lst = new ArrayList<>());
			}
			lst.add(indexIntoSet(name.value, namesSet, namesList));
			lst.add(indexIntoSet(method.getName(), namesSet, namesList));
		}
		for(String str : map.keySet()) {
			List<Integer> builder = new ArrayList<>();
			boolean b = false;
			for(String strr : str.split("\\.")) {
				builder.add(indexIntoSet(strr, classNamesPartsSet, classNamesPartsList));
				b = true;
			}
			namesEncSet.put(str, builder);
		}
		writer.append("return [").ws().append('[').ws();
		boolean b = false;
		for(String str : classNamesPartsList) {
			if(b) {
				writer.append(',').ws();
			}
			writer.append('\"').append(str).append('\"');
			b = true;
		}
		writer.append("],").ws().append('[').ws();
		b = false;
		for(String str : namesList) {
			if(b) {
				writer.append(',').ws();
			}
			writer.append('\"').append(str).append('\"');
			b = true;
		}
		writer.ws().append("],").ws();
		b = false;
		for (Entry<String,List<Integer>> name : map.entrySet()) {
			if(b) {
				writer.append(',').ws();
			}
			writer.append('\"').append(Base64VarIntArray.encodeVarIntArray(namesEncSet.get(name.getKey()))).append("\",").ws();
			writer.append('\"').appendClass(name.getKey()).append("\",").ws().append('\"');
			writer.append(Base64VarIntArray.encodeVarIntArray(name.getValue())).append('\"').ws();
			b = true;
		}
		writer.ws().append("];").softNewLine();
	}

}

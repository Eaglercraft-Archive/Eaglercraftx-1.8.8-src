/*
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

package net.lax1dude.eaglercraft.v1_8.internal.teavm.generators;

import java.io.IOException;

import org.teavm.backend.javascript.codegen.SourceWriter;
import org.teavm.backend.javascript.spi.Generator;
import org.teavm.backend.javascript.spi.GeneratorContext;
import org.teavm.backend.javascript.spi.Injector;
import org.teavm.backend.javascript.spi.InjectorContext;
import org.teavm.model.MethodReference;

public class TeaVMUtilsUnwrapGenerator {

	// WARNING: This code uses internal TeaVM APIs that may not have
	// been intended for end users of the compiler to program with

	public static class UnwrapArrayBuffer implements Injector {

		@Override
		public void generate(InjectorContext context, MethodReference methodRef) throws IOException {
			context.writeExpr(context.getArgument(0));
			context.getWriter().append(".data.buffer");
		}

	}

	public static class UnwrapTypedArray implements Injector {

		@Override
		public void generate(InjectorContext context, MethodReference methodRef) throws IOException {
			context.writeExpr(context.getArgument(0));
			context.getWriter().append(".data");
		}

	}

	public static class WrapArrayBuffer implements Generator {

		@Override
		public void generate(GeneratorContext context, SourceWriter writer, MethodReference methodRef)
				throws IOException {
	        String parName = context.getParameterName(1);
			switch (methodRef.getName()) {
			case "wrapByteArrayBuffer":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_bytecls(),").ws().append("new Int8Array(").append(parName).append("))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapIntArrayBuffer":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_intcls(),").ws().append("new Int32Array(").append(parName).append("))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapFloatArrayBuffer":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_floatcls(),").ws().append("new Float32Array(").append(parName).append("))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapShortArrayBuffer":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_shortcls(),").ws().append("new Int16Array(").append(parName).append("))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			default:
				break;
			}
		}

	}

	public static class WrapArrayBufferView implements Generator {

		@Override
		public void generate(GeneratorContext context, SourceWriter writer, MethodReference methodRef)
				throws IOException {
	        String parName = context.getParameterName(1);
			switch (methodRef.getName()) {
			case "wrapByteArrayBufferView":
			case "wrapUnsignedByteArray":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_bytecls(),").ws().append("new Int8Array(").append(parName).append(".buffer))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapIntArrayBufferView":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_intcls(),").ws().append("new Int32Array(").append(parName).append(".buffer))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapFloatArrayBufferView":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_floatcls(),").ws().append("new Float32Array(").append(parName).append(".buffer))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapShortArrayBufferView":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_shortcls(),").ws().append("new Int16Array(").append(parName).append(".buffer))").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			default:
				break;
			}
		}

	}

	public static class WrapTypedArray implements Generator {

		@Override
		public void generate(GeneratorContext context, SourceWriter writer, MethodReference methodRef)
				throws IOException {
	        String parName = context.getParameterName(1);
			switch (methodRef.getName()) {
			case "wrapByteArray":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_bytecls(),").ws().append(parName).append(")").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapIntArray":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_intcls(),").ws().append(parName).append(")").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapFloatArray":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_floatcls(),").ws().append(parName).append(")").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			case "wrapShortArray":
				writer.append("return ").append(parName).ws().append('?').ws();
				writer.append("$rt_createNumericArray($rt_shortcls(),").ws().append(parName).append(")").ws();
				writer.append(':').ws().append("null;").softNewLine();
				break;
			default:
				break;
			}
		}

	}

	public static class UnwrapUnsignedTypedArray implements Injector {

		@Override
		public void generate(InjectorContext context, MethodReference methodRef) throws IOException {
			context.getWriter().append("new Uint8Array(");
			context.writeExpr(context.getArgument(0));
			context.getWriter().append(".data.buffer)");
		}

	}

}
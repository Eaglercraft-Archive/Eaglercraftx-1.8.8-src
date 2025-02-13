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

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

public enum EnumES6Shims {
	SHIM_CLASS_MAP(ES6ShimStatusJS.SHIM_MAP, "Map"),
	SHIM_CLASS_WEAKMAP(ES6ShimStatusJS.SHIM_WEAKMAP, "WeakMap"),
	SHIM_CLASS_SET(ES6ShimStatusJS.SHIM_SET, "Set"),
	SHIM_CLASS_WEAKSET(ES6ShimStatusJS.SHIM_WEAKSET, "WeakSet"),
	SHIM_CLASS_PROMISE(ES6ShimStatusJS.SHIM_PROMISE, "Promise"),
	SHIM_STRING_FROM_CODE_POINT(ES6ShimStatusJS.SHIM_STRING_FROM_CODE_POINT, "String.fromCodePoint"),
	SHIM_STRING_PROTO_CODE_POINT_AT(ES6ShimStatusJS.SHIM_STRING_CODE_POINT_AT, "String.prototype.codePointAt"),
	SHIM_STRING_PROTO_STARTS_WITH(ES6ShimStatusJS.SHIM_STRING_STARTS_WITH, "String.prototype.startsWith"),
	SHIM_STRING_PROTO_ENDS_WITH(ES6ShimStatusJS.SHIM_STRING_ENDS_WITH, "String.prototype.endsWith"),
	SHIM_STRING_PROTO_INCLUDES(ES6ShimStatusJS.SHIM_STRING_INCLUDES, "String.prototype.includes"),
	SHIM_STRING_PROTO_REPEAT(ES6ShimStatusJS.SHIM_STRING_REPEAT, "String.prototype.repeat"),
	SHIM_ARRAY_PROTO_FILL(ES6ShimStatusJS.SHIM_ARRAY_FILL, "Array.prototype.fill"),
	SHIM_OBJECT_IS(ES6ShimStatusJS.SHIM_OBJECT_IS, "Object.is"),
	SHIM_OBJECT_SET_PROTOTYPE_OF(ES6ShimStatusJS.SHIM_OBJECT_SET_PROTOTYPE_OF, "Object.setPrototypeOf"),
	SHIM_FUNCTION_NAME(ES6ShimStatusJS.SHIM_FUNCTION_NAME, "Function.prototype.name"),
	SHIM_MATH_SIGN(ES6ShimStatusJS.SHIM_MATH_SIGN, "Math.sign"),
	SHIM_FAKE_SYMBOL(ES6ShimStatusJS.SHIM_SYMBOL, "Symbol (sham)");

	public final int shimId;
	public final String shimDesc;

	private EnumES6Shims(int shimId, String shimDesc) {
		this.shimId = shimId;
		this.shimDesc = shimDesc;
	}

	public static EnumES6Shims getShimById(int id) {
		return (id >= 0 && id < lookup.length) ? lookup[id] : null;
	}

	private static final EnumES6Shims[] lookup = new EnumES6Shims[20];

	static {
		EnumES6Shims[] _values = values();
		for(int i = 0; i < _values.length; ++i) {
			lookup[_values[i].shimId] = _values[i];
		}
	}

}
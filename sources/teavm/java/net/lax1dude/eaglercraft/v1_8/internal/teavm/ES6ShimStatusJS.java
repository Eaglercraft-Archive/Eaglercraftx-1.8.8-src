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

import org.teavm.jso.JSObject;

public interface ES6ShimStatusJS extends JSObject {

	public static final int INIT_STATUS_ERROR = -1;
	public static final int INIT_STATUS_DISABLED = 0;
	public static final int INIT_STATUS_ENABLED = 1;
	public static final int INIT_STATUS_DISABLED_ERRORS = 2;
	public static final int INIT_STATUS_ENABLED_ERRORS = 3;

	public static final int SHIM_MAP = 0;
	public static final int SHIM_WEAKMAP = 1;
	public static final int SHIM_SET = 2;
	public static final int SHIM_WEAKSET = 3;
	public static final int SHIM_PROMISE = 4;
	public static final int SHIM_STRING_FROM_CODE_POINT = 5;
	public static final int SHIM_STRING_CODE_POINT_AT = 6;
	public static final int SHIM_STRING_STARTS_WITH = 7;
	public static final int SHIM_STRING_ENDS_WITH = 8;
	public static final int SHIM_STRING_INCLUDES = 9;
	public static final int SHIM_STRING_REPEAT = 10;
	public static final int SHIM_ARRAY_FILL = 11;
	public static final int SHIM_OBJECT_IS = 12;
	public static final int SHIM_OBJECT_SET_PROTOTYPE_OF = 13;
	public static final int SHIM_FUNCTION_NAME = 14;
	public static final int SHIM_MATH_SIGN = 15;
	public static final int SHIM_SYMBOL = 16;

	int getShimInitStatus();

	int getEnabledShimCount();

	int getEnabledShimID(int idx);

}
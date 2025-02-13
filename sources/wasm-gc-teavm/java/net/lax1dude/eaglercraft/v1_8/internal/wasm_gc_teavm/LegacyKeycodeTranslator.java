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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

public class LegacyKeycodeTranslator {

	public static class LegacyKeycode {

		public final int keyCode;
		public final int location;

		private LegacyKeycode(int keyCode, int location) {
			this.keyCode = keyCode;
			this.location = location;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof LegacyKeycode))
				return false;
			LegacyKeycode other = (LegacyKeycode) obj;
			if (keyCode != other.keyCode)
				return false;
			if (location != other.location)
				return false;
			return true;
		}

	}

	private static final Set<String> numpadVolatile = Sets.newHashSet(
			"Comma", "Minus", "Period", "Slash", "Equal", "Enter", "Digit0", "Digit1", "Digit2", "Digit3",
			"Digit4", "Digit5", "Digit6", "Digit7", "Digit8", "Digit9", "IntlYen");

	private final Map<String,LegacyKeycode> codeLookupBase = new HashMap<>();
	private final Map<String,LegacyKeycode> codeLookupLayout = new HashMap<>();

	public LegacyKeycodeTranslator() {
		codeLookupBase.put("Digit0", new LegacyKeycode(0x30, 0));
		codeLookupBase.put("Digit1", new LegacyKeycode(0x31, 0));
		codeLookupBase.put("Digit2", new LegacyKeycode(0x32, 0));
		codeLookupBase.put("Digit3", new LegacyKeycode(0x33, 0));
		codeLookupBase.put("Digit4", new LegacyKeycode(0x34, 0));
		codeLookupBase.put("Digit5", new LegacyKeycode(0x35, 0));
		codeLookupBase.put("Digit6", new LegacyKeycode(0x36, 0));
		codeLookupBase.put("Digit7", new LegacyKeycode(0x37, 0));
		codeLookupBase.put("Digit8", new LegacyKeycode(0x38, 0));
		codeLookupBase.put("Digit9", new LegacyKeycode(0x39, 0));
		codeLookupBase.put("KeyA", new LegacyKeycode(0x41, 0));
		codeLookupBase.put("KeyB", new LegacyKeycode(0x42, 0));
		codeLookupBase.put("KeyC", new LegacyKeycode(0x43, 0));
		codeLookupBase.put("KeyD", new LegacyKeycode(0x44, 0));
		codeLookupBase.put("KeyE", new LegacyKeycode(0x45, 0));
		codeLookupBase.put("KeyF", new LegacyKeycode(0x46, 0));
		codeLookupBase.put("KeyG", new LegacyKeycode(0x47, 0));
		codeLookupBase.put("KeyH", new LegacyKeycode(0x48, 0));
		codeLookupBase.put("KeyI", new LegacyKeycode(0x49, 0));
		codeLookupBase.put("KeyJ", new LegacyKeycode(0x4A, 0));
		codeLookupBase.put("KeyK", new LegacyKeycode(0x4B, 0));
		codeLookupBase.put("KeyL", new LegacyKeycode(0x4C, 0));
		codeLookupBase.put("KeyM", new LegacyKeycode(0x4D, 0));
		codeLookupBase.put("KeyN", new LegacyKeycode(0x4E, 0));
		codeLookupBase.put("KeyO", new LegacyKeycode(0x4F, 0));
		codeLookupBase.put("KeyP", new LegacyKeycode(0x50, 0));
		codeLookupBase.put("KeyQ", new LegacyKeycode(0x51, 0));
		codeLookupBase.put("KeyR", new LegacyKeycode(0x52, 0));
		codeLookupBase.put("KeyS", new LegacyKeycode(0x53, 0));
		codeLookupBase.put("KeyT", new LegacyKeycode(0x54, 0));
		codeLookupBase.put("KeyU", new LegacyKeycode(0x55, 0));
		codeLookupBase.put("KeyV", new LegacyKeycode(0x56, 0));
		codeLookupBase.put("KeyW", new LegacyKeycode(0x57, 0));
		codeLookupBase.put("KeyX", new LegacyKeycode(0x58, 0));
		codeLookupBase.put("KeyY", new LegacyKeycode(0x59, 0));
		codeLookupBase.put("KeyZ", new LegacyKeycode(0x5A, 0));
		codeLookupBase.put("Comma", new LegacyKeycode(0xBC, 0));
		codeLookupBase.put("Period", new LegacyKeycode(0xBE, 0));
		codeLookupBase.put("Semicolon", new LegacyKeycode(0xBA, 0));
		codeLookupBase.put("Quote", new LegacyKeycode(0xDE, 0));
		codeLookupBase.put("BracketLeft", new LegacyKeycode(0xDB, 0));
		codeLookupBase.put("BracketRight", new LegacyKeycode(0xDD, 0));
		codeLookupBase.put("Backquote", new LegacyKeycode(0xC0, 0));
		codeLookupBase.put("Backslash", new LegacyKeycode(0xDC, 0));
		codeLookupBase.put("IntlBackslash", new LegacyKeycode(0xDC, 0));
		codeLookupBase.put("Minus", new LegacyKeycode(0xBD, 0));
		codeLookupBase.put("Equal", new LegacyKeycode(0xBB, 0));
		codeLookupBase.put("Slash", new LegacyKeycode(0xBF, 0));
		codeLookupBase.put("IntlRo", new LegacyKeycode(0xC1, 0));
		codeLookupBase.put("IntlYen", new LegacyKeycode(0xFF, 0));
		codeLookupBase.put("AltLeft", new LegacyKeycode(0x12, 1));
		codeLookupBase.put("AltRight", new LegacyKeycode(0x12, 2));
		codeLookupBase.put("CapsLock", new LegacyKeycode(0x14, 0));
		codeLookupBase.put("ControlLeft", new LegacyKeycode(0x11, 1));
		codeLookupBase.put("ControlRight", new LegacyKeycode(0x11, 2));
		codeLookupBase.put("MetaLeft", new LegacyKeycode(0x5B, 1));
		codeLookupBase.put("MetaRight", new LegacyKeycode(0x5C, 2));
		codeLookupBase.put("ShiftLeft", new LegacyKeycode(0x10, 1));
		codeLookupBase.put("ShiftRight", new LegacyKeycode(0x10, 2));
		codeLookupBase.put("ContextMenu", new LegacyKeycode(0x5D, 0));
		codeLookupBase.put("Enter", new LegacyKeycode(0x0D, 0));
		codeLookupBase.put("Space", new LegacyKeycode(0x20, 0));
		codeLookupBase.put("Backspace", new LegacyKeycode(0x08, 0));
		codeLookupBase.put("Tab", new LegacyKeycode(0x09, 0));
		codeLookupBase.put("Delete", new LegacyKeycode(0x2E, 0));
		codeLookupBase.put("End", new LegacyKeycode(0x23, 0));
		codeLookupBase.put("Help", new LegacyKeycode(0x2D, 0));
		codeLookupBase.put("Home", new LegacyKeycode(0x24, 0));
		codeLookupBase.put("Insert", new LegacyKeycode(0x2D, 0));
		codeLookupBase.put("PageDown", new LegacyKeycode(0x22, 0));
		codeLookupBase.put("PageUp", new LegacyKeycode(0x21, 0));
		codeLookupBase.put("ArrowDown", new LegacyKeycode(0x28, 0));
		codeLookupBase.put("ArrowLeft", new LegacyKeycode(0x25, 0));
		codeLookupBase.put("ArrowRight", new LegacyKeycode(0x27, 0));
		codeLookupBase.put("ArrowUp", new LegacyKeycode(0x26, 0));
		codeLookupBase.put("Escape", new LegacyKeycode(0x1B, 0));
		codeLookupBase.put("PrintScreen", new LegacyKeycode(0x2C, 0));
		codeLookupBase.put("ScrollLock", new LegacyKeycode(0x91, 0));
		codeLookupBase.put("Pause", new LegacyKeycode(0x13, 0));
		codeLookupBase.put("F1", new LegacyKeycode(0x70, 0));
		codeLookupBase.put("F2", new LegacyKeycode(0x71, 0));
		codeLookupBase.put("F3", new LegacyKeycode(0x72, 0));
		codeLookupBase.put("F4", new LegacyKeycode(0x73, 0));
		codeLookupBase.put("F5", new LegacyKeycode(0x74, 0));
		codeLookupBase.put("F6", new LegacyKeycode(0x75, 0));
		codeLookupBase.put("F7", new LegacyKeycode(0x76, 0));
		codeLookupBase.put("F8", new LegacyKeycode(0x77, 0));
		codeLookupBase.put("F9", new LegacyKeycode(0x78, 0));
		codeLookupBase.put("F10", new LegacyKeycode(0x79, 0));
		codeLookupBase.put("F11", new LegacyKeycode(0x7A, 0));
		codeLookupBase.put("F12", new LegacyKeycode(0x7B, 0));
		codeLookupBase.put("F13", new LegacyKeycode(0x7C, 0));
		codeLookupBase.put("F14", new LegacyKeycode(0x7D, 0));
		codeLookupBase.put("F15", new LegacyKeycode(0x7E, 0));
		codeLookupBase.put("F16", new LegacyKeycode(0x7F, 0));
		codeLookupBase.put("F17", new LegacyKeycode(0x80, 0));
		codeLookupBase.put("F18", new LegacyKeycode(0x81, 0));
		codeLookupBase.put("F19", new LegacyKeycode(0x82, 0));
		codeLookupBase.put("F20", new LegacyKeycode(0x83, 0));
		codeLookupBase.put("F21", new LegacyKeycode(0x84, 0));
		codeLookupBase.put("F22", new LegacyKeycode(0x85, 0));
		codeLookupBase.put("F23", new LegacyKeycode(0x86, 0));
		codeLookupBase.put("F24", new LegacyKeycode(0x87, 0));
		codeLookupBase.put("NumLock", new LegacyKeycode(0x90, 3));
		codeLookupBase.put("Numpad0", new LegacyKeycode(0x60, 3));
		codeLookupBase.put("Numpad1", new LegacyKeycode(0x61, 3));
		codeLookupBase.put("Numpad2", new LegacyKeycode(0x62, 3));
		codeLookupBase.put("Numpad3", new LegacyKeycode(0x63, 3));
		codeLookupBase.put("Numpad4", new LegacyKeycode(0x64, 3));
		codeLookupBase.put("Numpad5", new LegacyKeycode(0x65, 3));
		codeLookupBase.put("Numpad6", new LegacyKeycode(0x66, 3));
		codeLookupBase.put("Numpad7", new LegacyKeycode(0x67, 3));
		codeLookupBase.put("Numpad8", new LegacyKeycode(0x68, 3));
		codeLookupBase.put("Numpad9", new LegacyKeycode(0x69, 3));
		codeLookupBase.put("NumpadAdd", new LegacyKeycode(0x6B, 3));
		codeLookupBase.put("NumpadComma", new LegacyKeycode(0xC2, 3));
		codeLookupBase.put("NumpadDecimal", new LegacyKeycode(0x6E, 3));
		codeLookupBase.put("NumpadDivide", new LegacyKeycode(0x6F, 3));
		codeLookupBase.put("NumpadEnter", new LegacyKeycode(0x0D, 3));
		codeLookupBase.put("NumpadEqual", new LegacyKeycode(0x0C, 3));
		codeLookupBase.put("NumpadMultiply", new LegacyKeycode(0x6A, 3));
		codeLookupBase.put("NumpadSubtract", new LegacyKeycode(0x6D, 3));
	}

	public LegacyKeycodeTranslator addBrowserLayoutMapping(String keyChar, String codeStr) {
		LegacyKeycode mapTo = codeLookupBase.get(codeStr);
		if(mapTo != null) {
			String keyCode = getCodeFromLayoutChar(keyChar);
			if(keyCode != null && !keyCode.equals(codeStr) && !(codeStr.startsWith("Numpad") && numpadVolatile.contains(keyCode)) && !mapTo.equals(codeLookupBase.get(keyCode))) {
				codeLookupLayout.put(keyCode, mapTo);
			}
		}
		return this;
	}

	public int getRemappedKeyCount() {
		return codeLookupLayout.size();
	}

	public Map<String,LegacyKeycode> buildLayoutTable() {
		if(codeLookupLayout.isEmpty()) {
			return codeLookupBase;
		}
		Map<String,LegacyKeycode> ret = new HashMap<>();
		ret.putAll(codeLookupBase);
		ret.putAll(codeLookupLayout);
		return ret;
	}

	public static String getCodeFromLayoutChar(String keyChar) {
		if(keyChar.length() != 1) {
			return null;
		}
		char c = keyChar.charAt(0);
		String ret = getCodeFromLayoutChar0(c);
		if(ret == null) {
			ret = getCodeFromLayoutChar0(Character.toLowerCase(c));
		}
		return ret;
	}

	private static String getCodeFromLayoutChar0(char keyChar) {
		switch(keyChar) {
		case 'e':
			return "KeyE";
		case 'd':
			return "KeyD";
		case 'u':
			return "KeyU";
		case '-':
			return "Minus";
		case 'h':
			return "KeyH";
		case 'z':
			return "KeyZ";
		case '=':
			return "Equal";
		case 'p':
			return "KeyP";
		case ';':
			return "Semicolon";
		case ']':
			return "BracketRight";
		case '/':
			return "Slash";
		case '[':
			return "BracketLeft";
		case 'l':
			return "KeyL";
		case '8':
			return "Digit8";
		case 'w':
			return "KeyW";
		case 's':
			return "KeyS";
		case '5':
			return "Digit5";
		case '9':
			return "Digit9";
		case 'o':
			return "KeyO";
		case '.':
			return "Period";
		case '6':
			return "Digit6";
		case 'v':
			return "KeyV";
		case '3':
			return "Digit3";
		case '`':
			return "Backquote";
		case 'g':
			return "KeyG";
		case 'j':
			return "KeyJ";
		case 'q':
			return "KeyQ";
		case '1':
			return "Digit1";
		case 't':
			return "KeyT";
		case 'y':
			return "KeyY";
		case '\'':
			return "Quote";
		case '\\':
			return "Backslash";
		case 'k':
			return "KeyK";
		case 'f':
			return "KeyF";
		case 'i':
			return "KeyI";
		case 'r':
			return "KeyR";
		case 'x':
			return "KeyX";
		case 'a':
			return "KeyA";
		case '2':
			return "Digit2";
		case '7':
			return "Digit7";
		case 'm':
			return "KeyM";
		case '4':
			return "Digit4";
		case '0':
			return "Digit0";
		case 'n':
			return "KeyN";
		case 'b':
			return "KeyB";
		case 'c':
			return "KeyC";
		case ',':
			return "Comma";
		case '*':
			return "NumpadMultiply";
		case 0xA5:
			return "IntlYen";
		default:
			return null;
		}
	}

}
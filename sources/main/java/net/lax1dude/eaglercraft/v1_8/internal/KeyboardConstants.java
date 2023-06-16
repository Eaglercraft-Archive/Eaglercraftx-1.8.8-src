package net.lax1dude.eaglercraft.v1_8.internal;

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
public class KeyboardConstants {

	private static final String[] keyboardNames = new String[256];
	private static final int[] keyboardGLFWToEagler = new int[384];
	private static final int[] keyboardEaglerToGLFW = new int[256];
	private static final int[] keyboardBrowserToEagler = new int[384 * 4];
	private static final int[] keyboardEaglerToBrowser = new int[256];
	private static final char[] keyboardChars = new char[256];
	
	public static final int KEY_NONE = 0x00;
	public static final int KEY_ESCAPE = 0x01;
	public static final int KEY_1 = 0x02;
	public static final int KEY_2 = 0x03;
	public static final int KEY_3 = 0x04;
	public static final int KEY_4 = 0x05;
	public static final int KEY_5 = 0x06;
	public static final int KEY_6 = 0x07;
	public static final int KEY_7 = 0x08;
	public static final int KEY_8 = 0x09;
	public static final int KEY_9 = 0x0A;
	public static final int KEY_0 = 0x0B;
	public static final int KEY_MINUS = 0x0C; /* - on main keyboard */
	public static final int KEY_EQUALS = 0x0D;
	public static final int KEY_BACK = 0x0E; /* backspace */
	public static final int KEY_TAB = 0x0F;
	public static final int KEY_Q = 0x10;
	public static final int KEY_W = 0x11;
	public static final int KEY_E = 0x12;
	public static final int KEY_R = 0x13;
	public static final int KEY_T = 0x14;
	public static final int KEY_Y = 0x15;
	public static final int KEY_U = 0x16;
	public static final int KEY_I = 0x17;
	public static final int KEY_O = 0x18;
	public static final int KEY_P = 0x19;
	public static final int KEY_LBRACKET = 0x1A;
	public static final int KEY_RBRACKET = 0x1B;
	public static final int KEY_RETURN = 0x1C; /* Enter on main keyboard */
	public static final int KEY_LCONTROL = 0x1D;
	public static final int KEY_A = 0x1E;
	public static final int KEY_S = 0x1F;
	public static final int KEY_D = 0x20;
	public static final int KEY_F = 0x21;
	public static final int KEY_G = 0x22;
	public static final int KEY_H = 0x23;
	public static final int KEY_J = 0x24;
	public static final int KEY_K = 0x25;
	public static final int KEY_L = 0x26;
	public static final int KEY_SEMICOLON = 0x27;
	public static final int KEY_APOSTROPHE = 0x28;
	public static final int KEY_GRAVE = 0x29; /* accent grave */
	public static final int KEY_LSHIFT = 0x2A;
	public static final int KEY_BACKSLASH = 0x2B;
	public static final int KEY_Z = 0x2C;
	public static final int KEY_X = 0x2D;
	public static final int KEY_C = 0x2E;
	public static final int KEY_V = 0x2F;
	public static final int KEY_B = 0x30;
	public static final int KEY_N = 0x31;
	public static final int KEY_M = 0x32;
	public static final int KEY_COMMA = 0x33;
	public static final int KEY_PERIOD = 0x34; /* . on main keyboard */
	public static final int KEY_SLASH = 0x35; /* / on main keyboard */
	public static final int KEY_RSHIFT = 0x36;
	public static final int KEY_MULTIPLY = 0x37; /* * on numeric keypad */
	public static final int KEY_LMENU = 0x38; /* left Alt */
	public static final int KEY_SPACE = 0x39;
	public static final int KEY_CAPITAL = 0x3A;
	public static final int KEY_F1 = 0x3B;
	public static final int KEY_F2 = 0x3C;
	public static final int KEY_F3 = 0x3D;
	public static final int KEY_F4 = 0x3E;
	public static final int KEY_F5 = 0x3F;
	public static final int KEY_F6 = 0x40;
	public static final int KEY_F7 = 0x41;
	public static final int KEY_F8 = 0x42;
	public static final int KEY_F9 = 0x43;
	public static final int KEY_F10 = 0x44;
	public static final int KEY_NUMLOCK = 0x45;
	public static final int KEY_SCROLL = 0x46; /* Scroll Lock */
	public static final int KEY_NUMPAD7 = 0x47;
	public static final int KEY_NUMPAD8 = 0x48;
	public static final int KEY_NUMPAD9 = 0x49;
	public static final int KEY_SUBTRACT = 0x4A; /* - on numeric keypad */
	public static final int KEY_NUMPAD4 = 0x4B;
	public static final int KEY_NUMPAD5 = 0x4C;
	public static final int KEY_NUMPAD6 = 0x4D;
	public static final int KEY_ADD = 0x4E; /* + on numeric keypad */
	public static final int KEY_NUMPAD1 = 0x4F;
	public static final int KEY_NUMPAD2 = 0x50;
	public static final int KEY_NUMPAD3 = 0x51;
	public static final int KEY_NUMPAD0 = 0x52;
	public static final int KEY_DECIMAL = 0x53; /* . on numeric keypad */
	public static final int KEY_F11 = 0x57;
	public static final int KEY_F12 = 0x58;
	public static final int KEY_F13 = 0x64; /* (NEC PC98) */
	public static final int KEY_F14 = 0x65; /* (NEC PC98) */
	public static final int KEY_F15 = 0x66; /* (NEC PC98) */
	public static final int KEY_F16 = 0x67; /* Extended Function keys - (Mac) */
	public static final int KEY_F17 = 0x68;
	public static final int KEY_F18 = 0x69;
	public static final int KEY_KANA = 0x70; /* (Japanese keyboard) */
	public static final int KEY_F19 = 0x71; /* Extended Function keys - (Mac) */
	public static final int KEY_CONVERT = 0x79; /* (Japanese keyboard) */
	public static final int KEY_NOCONVERT = 0x7B; /* (Japanese keyboard) */
	public static final int KEY_YEN = 0x7D; /* (Japanese keyboard) */
	public static final int KEY_NUMPADEQUALS = 0x8D; /* = on numeric keypad (NEC PC98) */
	public static final int KEY_CIRCUMFLEX = 0x90; /* (Japanese keyboard) */
	public static final int KEY_AT = 0x91; /* (NEC PC98) */
	public static final int KEY_COLON = 0x92; /* (NEC PC98) */
	public static final int KEY_UNDERLINE = 0x93; /* (NEC PC98) */
	public static final int KEY_KANJI = 0x94; /* (Japanese keyboard) */
	public static final int KEY_STOP = 0x95; /* (NEC PC98) */
	public static final int KEY_AX = 0x96; /* (Japan AX) */
	public static final int KEY_UNLABELED = 0x97; /* (J3100) */
	public static final int KEY_NUMPADENTER = 0x9C; /* Enter on numeric keypad */
	public static final int KEY_RCONTROL = 0x9D;
	public static final int KEY_SECTION = 0xA7; /* Section symbol (Mac) */
	public static final int KEY_NUMPADCOMMA = 0xB3; /* , on numeric keypad (NEC PC98) */
	public static final int KEY_DIVIDE = 0xB5; /* / on numeric keypad */
	public static final int KEY_SYSRQ = 0xB7;
	public static final int KEY_RMENU = 0xB8; /* right Alt */
	public static final int KEY_FUNCTION = 0xC4; /* Function (Mac) */
	public static final int KEY_PAUSE = 0xC5; /* Pause */
	public static final int KEY_HOME = 0xC7; /* Home on arrow keypad */
	public static final int KEY_UP = 0xC8; /* UpArrow on arrow keypad */
	public static final int KEY_PRIOR = 0xC9; /* PgUp on arrow keypad */
	public static final int KEY_LEFT = 0xCB; /* LeftArrow on arrow keypad */
	public static final int KEY_RIGHT = 0xCD; /* RightArrow on arrow keypad */
	public static final int KEY_END = 0xCF; /* End on arrow keypad */
	public static final int KEY_DOWN = 0xD0; /* DownArrow on arrow keypad */
	public static final int KEY_NEXT = 0xD1; /* PgDn on arrow keypad */
	public static final int KEY_INSERT = 0xD2; /* Insert on arrow keypad */
	public static final int KEY_DELETE = 0xD3; /* Delete on arrow keypad */
	public static final int KEY_CLEAR = 0xDA; /* Clear key (Mac) */
	public static final int KEY_LMETA = 0xDB; /* Left Windows/Option key */
	public static final int KEY_RMETA = 0xDC; /* Right Windows/Option key */
	public static final int KEY_APPS = 0xDD; /* AppMenu key */
	public static final int KEY_POWER = 0xDE;
	public static final int KEY_SLEEP = 0xDF;
	
	private static final int GLFW_KEY_SPACE = 32, GLFW_KEY_APOSTROPHE = 39, GLFW_KEY_COMMA = 44, GLFW_KEY_MINUS = 45,
			GLFW_KEY_PERIOD = 46, GLFW_KEY_SLASH = 47, GLFW_KEY_0 = 48, GLFW_KEY_1 = 49, GLFW_KEY_2 = 50,
			GLFW_KEY_3 = 51, GLFW_KEY_4 = 52, GLFW_KEY_5 = 53, GLFW_KEY_6 = 54, GLFW_KEY_7 = 55, GLFW_KEY_8 = 56,
			GLFW_KEY_9 = 57, GLFW_KEY_SEMICOLON = 59, GLFW_KEY_EQUAL = 61, GLFW_KEY_A = 65, GLFW_KEY_B = 66,
			GLFW_KEY_C = 67, GLFW_KEY_D = 68, GLFW_KEY_E = 69, GLFW_KEY_F = 70, GLFW_KEY_G = 71, GLFW_KEY_H = 72,
			GLFW_KEY_I = 73, GLFW_KEY_J = 74, GLFW_KEY_K = 75, GLFW_KEY_L = 76, GLFW_KEY_M = 77, GLFW_KEY_N = 78,
			GLFW_KEY_O = 79, GLFW_KEY_P = 80, GLFW_KEY_Q = 81, GLFW_KEY_R = 82, GLFW_KEY_S = 83, GLFW_KEY_T = 84,
			GLFW_KEY_U = 85, GLFW_KEY_V = 86, GLFW_KEY_W = 87, GLFW_KEY_X = 88, GLFW_KEY_Y = 89, GLFW_KEY_Z = 90,
			GLFW_KEY_LEFT_BRACKET = 91, GLFW_KEY_BACKSLASH = 92, GLFW_KEY_RIGHT_BRACKET = 93,
			GLFW_KEY_GRAVE_ACCENT = 96, GLFW_KEY_WORLD_1 = 161, GLFW_KEY_WORLD_2 = 162;
	
	private static final int GLFW_KEY_ESCAPE = 256, GLFW_KEY_ENTER = 257, GLFW_KEY_TAB = 258, GLFW_KEY_BACKSPACE = 259,
			GLFW_KEY_INSERT = 260, GLFW_KEY_DELETE = 261, GLFW_KEY_RIGHT = 262, GLFW_KEY_LEFT = 263,
			GLFW_KEY_DOWN = 264, GLFW_KEY_UP = 265, GLFW_KEY_PAGE_UP = 266, GLFW_KEY_PAGE_DOWN = 267,
			GLFW_KEY_HOME = 268, GLFW_KEY_END = 269, GLFW_KEY_CAPS_LOCK = 280, GLFW_KEY_SCROLL_LOCK = 281,
			GLFW_KEY_NUM_LOCK = 282, GLFW_KEY_PRINT_SCREEN = 283, GLFW_KEY_PAUSE = 284, GLFW_KEY_F1 = 290,
			GLFW_KEY_F2 = 291, GLFW_KEY_F3 = 292, GLFW_KEY_F4 = 293, GLFW_KEY_F5 = 294, GLFW_KEY_F6 = 295,
			GLFW_KEY_F7 = 296, GLFW_KEY_F8 = 297, GLFW_KEY_F9 = 298, GLFW_KEY_F10 = 299, GLFW_KEY_F11 = 300,
			GLFW_KEY_F12 = 301, GLFW_KEY_F13 = 302, GLFW_KEY_F14 = 303, GLFW_KEY_F15 = 304, GLFW_KEY_F16 = 305,
			GLFW_KEY_F17 = 306, GLFW_KEY_F18 = 307, GLFW_KEY_F19 = 308, GLFW_KEY_F20 = 309, GLFW_KEY_F21 = 310,
			GLFW_KEY_F22 = 311, GLFW_KEY_F23 = 312, GLFW_KEY_F24 = 313, GLFW_KEY_F25 = 314, GLFW_KEY_KP_0 = 320,
			GLFW_KEY_KP_1 = 321, GLFW_KEY_KP_2 = 322, GLFW_KEY_KP_3 = 323, GLFW_KEY_KP_4 = 324, GLFW_KEY_KP_5 = 325,
			GLFW_KEY_KP_6 = 326, GLFW_KEY_KP_7 = 327, GLFW_KEY_KP_8 = 328, GLFW_KEY_KP_9 = 329,
			GLFW_KEY_KP_DECIMAL = 330, GLFW_KEY_KP_DIVIDE = 331, GLFW_KEY_KP_MULTIPLY = 332, GLFW_KEY_KP_SUBTRACT = 333,
			GLFW_KEY_KP_ADD = 334, GLFW_KEY_KP_ENTER = 335, GLFW_KEY_KP_EQUAL = 336, GLFW_KEY_LEFT_SHIFT = 340,
			GLFW_KEY_LEFT_CONTROL = 341, GLFW_KEY_LEFT_ALT = 342, GLFW_KEY_LEFT_SUPER = 343, GLFW_KEY_RIGHT_SHIFT = 344,
			GLFW_KEY_RIGHT_CONTROL = 345, GLFW_KEY_RIGHT_ALT = 346, GLFW_KEY_RIGHT_SUPER = 347, GLFW_KEY_MENU = 348,
			GLFW_KEY_LAST = GLFW_KEY_MENU;
	
	private static final int DOM_KEY_LOCATION_STANDARD = 0, DOM_KEY_LOCATION_LEFT = 1, DOM_KEY_LOCATION_RIGHT = 2,
			DOM_KEY_LOCATION_NUMPAD = 3;

	private static void register(int eaglerId, int glfwId, int browserId, int browserLocation, String name, char character) {
		if(keyboardEaglerToGLFW[eaglerId] != 0) throw new IllegalArgumentException("Duplicate keyboardEaglerToGLFW entry: " + eaglerId + " -> " + glfwId);
		keyboardEaglerToGLFW[eaglerId] = glfwId;
		if(keyboardGLFWToEagler[glfwId] != 0) throw new IllegalArgumentException("Duplicate keyboardGLFWToEagler entry: " + glfwId + " -> " + eaglerId);
		keyboardGLFWToEagler[glfwId] = eaglerId;
		if(browserLocation == 0) {
			if(keyboardEaglerToBrowser[eaglerId] != 0) throw new IllegalArgumentException("Duplicate keyboardEaglerToBrowser entry: " + eaglerId + " -> " + browserId + "(0)");
			keyboardEaglerToBrowser[eaglerId] = browserId;
			if(keyboardBrowserToEagler[browserId] != 0) throw new IllegalArgumentException("Duplicate keyboardBrowserToEagler entry: " + browserId + "(0) -> " + eaglerId);
			keyboardBrowserToEagler[browserId] = eaglerId;
		}else {
			browserLocation *= 384;
			if(keyboardEaglerToBrowser[eaglerId] != 0) throw new IllegalArgumentException("Duplicate keyboardEaglerToBrowser entry: " + eaglerId + " -> " + browserId + "(" + browserLocation + ")");
			keyboardEaglerToBrowser[eaglerId] = browserId + browserLocation;
			if(keyboardBrowserToEagler[browserId + browserLocation] != 0) throw new IllegalArgumentException("Duplicate keyboardBrowserToEagler entry: " + browserId + "(" + browserLocation + ") -> " + eaglerId);
			keyboardBrowserToEagler[browserId + browserLocation] = eaglerId;
		}
		if(keyboardNames[eaglerId] != null) throw new IllegalArgumentException("Duplicate keyboardNames entry: " + eaglerId + " -> " + name);
		keyboardNames[eaglerId] = name;
		if(keyboardChars[eaglerId] != '\0') throw new IllegalArgumentException("Duplicate keyboardChars entry: " + eaglerId + " -> " + character);
		keyboardChars[eaglerId] = character;
	}
	
	private static void registerAlt(int eaglerId, int browserId, int browserLocation) {
		if(browserLocation == 0) {
			if(keyboardBrowserToEagler[browserId] != 0) throw new IllegalArgumentException("Duplicate (alt) keyboardBrowserToEagler entry: " + browserId + " -> " + eaglerId);
			keyboardBrowserToEagler[browserId] = eaglerId;
		}else {
			browserLocation *= 384;
			if(keyboardBrowserToEagler[browserId + browserLocation] != 0) throw new IllegalArgumentException("Duplicate (alt) keyboardBrowserToEagler entry: " + browserId + "(" + browserLocation + ") -> " + eaglerId);
			keyboardBrowserToEagler[browserId + browserLocation] = eaglerId;
		}
	}
	
	static {
		register(KEY_SPACE, GLFW_KEY_SPACE, 32, DOM_KEY_LOCATION_STANDARD, "Space", ' ');
		register(KEY_APOSTROPHE, GLFW_KEY_APOSTROPHE, 222, DOM_KEY_LOCATION_STANDARD, "Quote", '\'');
		register(KEY_COMMA, GLFW_KEY_COMMA, 188, DOM_KEY_LOCATION_STANDARD, "Comma", ',');
		register(KEY_MINUS, GLFW_KEY_MINUS, 189, DOM_KEY_LOCATION_STANDARD, "Minus", '-');
		register(KEY_PERIOD, GLFW_KEY_PERIOD, 190, DOM_KEY_LOCATION_STANDARD, "Period", '.');
		register(KEY_SLASH, GLFW_KEY_SLASH, 191, DOM_KEY_LOCATION_STANDARD, "Slash", '/');
		register(KEY_0, GLFW_KEY_0, 48, DOM_KEY_LOCATION_STANDARD, "0", '0');
		register(KEY_1, GLFW_KEY_1, 49, DOM_KEY_LOCATION_STANDARD, "1", '1');
		register(KEY_2, GLFW_KEY_2, 50, DOM_KEY_LOCATION_STANDARD, "2", '2');
		register(KEY_3, GLFW_KEY_3, 51, DOM_KEY_LOCATION_STANDARD, "3", '3');
		register(KEY_4, GLFW_KEY_4, 52, DOM_KEY_LOCATION_STANDARD, "4", '4');
		register(KEY_5, GLFW_KEY_5, 53, DOM_KEY_LOCATION_STANDARD, "5", '5');
		register(KEY_6, GLFW_KEY_6, 54, DOM_KEY_LOCATION_STANDARD, "6", '6');
		register(KEY_7, GLFW_KEY_7, 55, DOM_KEY_LOCATION_STANDARD, "7", '7');
		register(KEY_8, GLFW_KEY_8, 56, DOM_KEY_LOCATION_STANDARD, "8", '8');
		register(KEY_9, GLFW_KEY_9, 57, DOM_KEY_LOCATION_STANDARD, "9", '9');
		register(KEY_SEMICOLON, GLFW_KEY_SEMICOLON, 186, DOM_KEY_LOCATION_STANDARD, "Semicolon", ';');
		register(KEY_EQUALS, GLFW_KEY_EQUAL, 187, DOM_KEY_LOCATION_STANDARD, "Equals", '=');
		register(KEY_A, GLFW_KEY_A, 65, DOM_KEY_LOCATION_STANDARD, "A", 'a');
		register(KEY_B, GLFW_KEY_B, 66, DOM_KEY_LOCATION_STANDARD, "B", 'b');
		register(KEY_C, GLFW_KEY_C, 67, DOM_KEY_LOCATION_STANDARD, "C", 'c');
		register(KEY_D, GLFW_KEY_D, 68, DOM_KEY_LOCATION_STANDARD, "D", 'd');
		register(KEY_E, GLFW_KEY_E, 69, DOM_KEY_LOCATION_STANDARD, "E", 'e');
		register(KEY_F, GLFW_KEY_F, 70, DOM_KEY_LOCATION_STANDARD, "F", 'f');
		register(KEY_G, GLFW_KEY_G, 71, DOM_KEY_LOCATION_STANDARD, "G", 'g');
		register(KEY_H, GLFW_KEY_H, 72, DOM_KEY_LOCATION_STANDARD, "H", 'h');
		register(KEY_I, GLFW_KEY_I, 73, DOM_KEY_LOCATION_STANDARD, "I", 'i');
		register(KEY_J, GLFW_KEY_J, 74, DOM_KEY_LOCATION_STANDARD, "J", 'j');
		register(KEY_K, GLFW_KEY_K, 75, DOM_KEY_LOCATION_STANDARD, "K", 'k');
		register(KEY_L, GLFW_KEY_L, 76, DOM_KEY_LOCATION_STANDARD, "L", 'l');
		register(KEY_M, GLFW_KEY_M, 77, DOM_KEY_LOCATION_STANDARD, "M", 'm');
		register(KEY_N, GLFW_KEY_N, 78, DOM_KEY_LOCATION_STANDARD, "N", 'n');
		register(KEY_O, GLFW_KEY_O, 79, DOM_KEY_LOCATION_STANDARD, "O", 'o');
		register(KEY_P, GLFW_KEY_P, 80, DOM_KEY_LOCATION_STANDARD, "P", 'p');
		register(KEY_Q, GLFW_KEY_Q, 81, DOM_KEY_LOCATION_STANDARD, "Q", 'q');
		register(KEY_R, GLFW_KEY_R, 82, DOM_KEY_LOCATION_STANDARD, "R", 'r');
		register(KEY_S, GLFW_KEY_S, 83, DOM_KEY_LOCATION_STANDARD, "S", 's');
		register(KEY_T, GLFW_KEY_T, 84, DOM_KEY_LOCATION_STANDARD, "T", 't');
		register(KEY_U, GLFW_KEY_U, 85, DOM_KEY_LOCATION_STANDARD, "U", 'u');
		register(KEY_V, GLFW_KEY_V, 86, DOM_KEY_LOCATION_STANDARD, "V", 'v');
		register(KEY_W, GLFW_KEY_W, 87, DOM_KEY_LOCATION_STANDARD, "W", 'w');
		register(KEY_X, GLFW_KEY_X, 88, DOM_KEY_LOCATION_STANDARD, "X", 'x');
		register(KEY_Y, GLFW_KEY_Y, 89, DOM_KEY_LOCATION_STANDARD, "Y", 'y');
		register(KEY_Z, GLFW_KEY_Z, 90, DOM_KEY_LOCATION_STANDARD, "Z", 'z');
		register(KEY_LBRACKET, GLFW_KEY_LEFT_BRACKET, 219, DOM_KEY_LOCATION_STANDARD, "L. Bracket", '[');
		register(KEY_BACKSLASH, GLFW_KEY_BACKSLASH, 220, DOM_KEY_LOCATION_STANDARD, "Backslash", '\\');
		register(KEY_RBRACKET, GLFW_KEY_RIGHT_BRACKET, 221, DOM_KEY_LOCATION_STANDARD, "R. Bracket", ']');
		register(KEY_GRAVE, GLFW_KEY_GRAVE_ACCENT, 192, DOM_KEY_LOCATION_STANDARD, "Backtick", '`');
		register(KEY_ESCAPE, GLFW_KEY_ESCAPE, 27, DOM_KEY_LOCATION_STANDARD, "Escape", '\0');
		register(KEY_RETURN, GLFW_KEY_ENTER, 13, DOM_KEY_LOCATION_STANDARD, "Enter", '\n');
		register(KEY_TAB, GLFW_KEY_TAB, 9, DOM_KEY_LOCATION_STANDARD, "Tab", '\t');
		register(KEY_BACK, GLFW_KEY_BACKSPACE, 8, DOM_KEY_LOCATION_STANDARD, "Backspace", '\0');
		register(KEY_INSERT, GLFW_KEY_INSERT, 45, DOM_KEY_LOCATION_STANDARD, "Insert", '\0');
		register(KEY_DELETE, GLFW_KEY_DELETE, 46, DOM_KEY_LOCATION_STANDARD, "Delete", '\0');
		register(KEY_RIGHT, GLFW_KEY_RIGHT, 39, DOM_KEY_LOCATION_STANDARD, "Right", '\0');
		register(KEY_LEFT, GLFW_KEY_LEFT, 37, DOM_KEY_LOCATION_STANDARD, "Left", '\0');
		register(KEY_DOWN, GLFW_KEY_DOWN, 40, DOM_KEY_LOCATION_STANDARD, "Down", '\0');
		register(KEY_UP, GLFW_KEY_UP, 38, DOM_KEY_LOCATION_STANDARD, "Up", '\0');
		register(KEY_PRIOR, GLFW_KEY_PAGE_UP, 33, DOM_KEY_LOCATION_STANDARD, "Page Up", '\0');
		register(KEY_NEXT, GLFW_KEY_PAGE_DOWN, 34, DOM_KEY_LOCATION_STANDARD, "Page Down", '\0');
		register(KEY_HOME, GLFW_KEY_HOME, 36, DOM_KEY_LOCATION_STANDARD, "Home", '\0');
		register(KEY_END, GLFW_KEY_END, 35, DOM_KEY_LOCATION_STANDARD, "End", '\0');
		register(KEY_CAPITAL, GLFW_KEY_CAPS_LOCK, 20, DOM_KEY_LOCATION_STANDARD, "Caps Lock", '\0');
		register(KEY_SCROLL, GLFW_KEY_SCROLL_LOCK, 145, DOM_KEY_LOCATION_STANDARD, "Scroll Lock", '\0');
		register(KEY_NUMLOCK, GLFW_KEY_NUM_LOCK, 144, DOM_KEY_LOCATION_STANDARD, "Num Lock", '\0');
		register(KEY_PAUSE, GLFW_KEY_PAUSE, 19, DOM_KEY_LOCATION_STANDARD, "Pause", '\0');
		register(KEY_F1, GLFW_KEY_F1, 112, DOM_KEY_LOCATION_STANDARD, "F1", '\0');
		register(KEY_F2, GLFW_KEY_F2, 113, DOM_KEY_LOCATION_STANDARD, "F2", '\0');
		register(KEY_F3, GLFW_KEY_F3, 114, DOM_KEY_LOCATION_STANDARD, "F3", '\0');
		register(KEY_F4, GLFW_KEY_F4, 115, DOM_KEY_LOCATION_STANDARD, "F4", '\0');
		register(KEY_F5, GLFW_KEY_F5, 116, DOM_KEY_LOCATION_STANDARD, "F5", '\0');
		register(KEY_F6, GLFW_KEY_F6, 117, DOM_KEY_LOCATION_STANDARD, "F6", '\0');
		register(KEY_F7, GLFW_KEY_F7, 118, DOM_KEY_LOCATION_STANDARD, "F7", '\0');
		register(KEY_F8, GLFW_KEY_F8, 119, DOM_KEY_LOCATION_STANDARD, "F8", '\0');
		register(KEY_F9, GLFW_KEY_F9, 120, DOM_KEY_LOCATION_STANDARD, "F9", '\0');
		register(KEY_F10, GLFW_KEY_F10, 121, DOM_KEY_LOCATION_STANDARD, "F10", '\0');
		register(KEY_F11, GLFW_KEY_F11, 122, DOM_KEY_LOCATION_STANDARD, "F11", '\0');
		register(KEY_F12, GLFW_KEY_F12, 123, DOM_KEY_LOCATION_STANDARD, "F12", '\0');
		register(KEY_NUMPAD0, GLFW_KEY_KP_0, 96, DOM_KEY_LOCATION_NUMPAD, "Keypad 0", '0');
		register(KEY_NUMPAD1, GLFW_KEY_KP_1, 97, DOM_KEY_LOCATION_NUMPAD, "Keypad 1", '1');
		register(KEY_NUMPAD2, GLFW_KEY_KP_2, 98, DOM_KEY_LOCATION_NUMPAD, "Keypad 2", '2');
		register(KEY_NUMPAD3, GLFW_KEY_KP_3, 99, DOM_KEY_LOCATION_NUMPAD, "Keypad 3", '3');
		register(KEY_NUMPAD4, GLFW_KEY_KP_4, 100, DOM_KEY_LOCATION_NUMPAD, "Keypad 4", '4');
		register(KEY_NUMPAD5, GLFW_KEY_KP_5, 101, DOM_KEY_LOCATION_NUMPAD, "Keypad 5", '5');
		register(KEY_NUMPAD6, GLFW_KEY_KP_6, 102, DOM_KEY_LOCATION_NUMPAD, "Keypad 6", '6');
		register(KEY_NUMPAD7, GLFW_KEY_KP_7, 103, DOM_KEY_LOCATION_NUMPAD, "Keypad 7", '7');
		register(KEY_NUMPAD8, GLFW_KEY_KP_8, 104, DOM_KEY_LOCATION_NUMPAD, "Keypad 8", '8');
		register(KEY_NUMPAD9, GLFW_KEY_KP_9, 105, DOM_KEY_LOCATION_NUMPAD, "Keypad 9", '9');
		register(KEY_DECIMAL, GLFW_KEY_KP_DECIMAL, 110, DOM_KEY_LOCATION_NUMPAD, "Decimal", '.');
		register(KEY_DIVIDE, GLFW_KEY_KP_DIVIDE, 111, DOM_KEY_LOCATION_NUMPAD, "Divide", '/');
		register(KEY_MULTIPLY, GLFW_KEY_KP_MULTIPLY, 106, DOM_KEY_LOCATION_NUMPAD, "Multiply", '*');
		register(KEY_SUBTRACT, GLFW_KEY_KP_SUBTRACT, 109, DOM_KEY_LOCATION_NUMPAD, "Subtract", '-');
		register(KEY_ADD, GLFW_KEY_KP_ADD, 107, DOM_KEY_LOCATION_NUMPAD, "Add", '+');
		register(KEY_NUMPADENTER, GLFW_KEY_KP_ENTER, 13, DOM_KEY_LOCATION_NUMPAD, "Enter", '\n');
		register(KEY_NUMPADEQUALS, GLFW_KEY_KP_EQUAL, 187, DOM_KEY_LOCATION_NUMPAD, "Equals", '=');
		register(KEY_LSHIFT, GLFW_KEY_LEFT_SHIFT, 16, DOM_KEY_LOCATION_LEFT, "L. Shift", '\0');
		register(KEY_LCONTROL, GLFW_KEY_LEFT_CONTROL, 17, DOM_KEY_LOCATION_LEFT, "L. Control", '\0');
		register(KEY_LMENU, GLFW_KEY_LEFT_ALT, 18, DOM_KEY_LOCATION_LEFT, "L. Alt", '\0');
		registerAlt(KEY_LSHIFT, 16, DOM_KEY_LOCATION_STANDARD);
		registerAlt(KEY_LCONTROL, 17, DOM_KEY_LOCATION_STANDARD);
		registerAlt(KEY_LMENU, 18, DOM_KEY_LOCATION_STANDARD);
		register(KEY_RSHIFT, GLFW_KEY_RIGHT_SHIFT, 16, DOM_KEY_LOCATION_RIGHT, "R. Shift", '\0');
		register(KEY_RCONTROL, GLFW_KEY_RIGHT_CONTROL, 17, DOM_KEY_LOCATION_RIGHT, "R. Control", '\0');
		register(KEY_RMENU, GLFW_KEY_RIGHT_ALT, 18, DOM_KEY_LOCATION_RIGHT, "R. Alt", '\0');
	}

	public static String getKeyName(int key) {
		if (key < 0 || key >= 256 || keyboardNames[key] == null) {
			return "Unknown";
		} else {
			return keyboardNames[key];
		}
	}

	public static int getEaglerKeyFromGLFW(int key) {
		if (key < 0 || key >= 384) {
			return 0;
		} else {
			return keyboardGLFWToEagler[key];
		}
	}

	public static int getGLFWKeyFromEagler(int key) {
		if (key < 0 || key >= 256) {
			return 0;
		} else {
			return keyboardEaglerToGLFW[key];
		}
	}
	
	public static int getEaglerKeyFromBrowser(int key) {
		return getEaglerKeyFromBrowser(key, 0);
	}
	
	public static int getEaglerKeyFromBrowser(int key, int location) {
		if (key < 0 || key >= 384) {
			return 0;
		} else {
			if(location <= 0 || location >= 4) {
				return keyboardBrowserToEagler[key];
			}else {
				int i = keyboardBrowserToEagler[key + location * 384];
				if(i == 0) {
					i = keyboardBrowserToEagler[key];
				}
				return i;
			}
		}
	}

	public static int getBrowserKeyFromEagler(int key) {
		if (key < 0 || key >= 256) {
			return 0;
		} else {
			return keyboardEaglerToBrowser[key] % 384;
		}
	}

	public static int getBrowserLocationFromEagler(int key) {
		if (key < 0 || key >= 384) {
			return 0;
		} else {
			return keyboardEaglerToBrowser[key] / 384;
		}
	}
	
	public static char getKeyCharFromEagler(int key) {
		if (key < 0 || key >= 256) {
			return '\0';
		} else {
			return keyboardChars[key];
		}
	}
	
}

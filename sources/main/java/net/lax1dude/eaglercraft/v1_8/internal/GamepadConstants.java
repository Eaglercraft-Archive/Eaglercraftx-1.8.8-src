package net.lax1dude.eaglercraft.v1_8.internal;

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
public class GamepadConstants {

	private static final String[] buttonNames = new String[24];
	private static final String[] axisNames = new String[4];
	private static final int[] eaglerButtonsToGLFW = new int[24];
	private static final int[] glfwButtonsToEagler = new int[24];

	public static final int GAMEPAD_NONE = -1;
	public static final int GAMEPAD_A = 0;
	public static final int GAMEPAD_B = 1;
	public static final int GAMEPAD_X = 2;
	public static final int GAMEPAD_Y = 3;
	public static final int GAMEPAD_LEFT_BUTTON = 4;
	public static final int GAMEPAD_RIGHT_BUTTON = 5;
	public static final int GAMEPAD_LEFT_TRIGGER = 6;
	public static final int GAMEPAD_RIGHT_TRIGGER = 7;
	public static final int GAMEPAD_BACK = 8;
	public static final int GAMEPAD_START = 9;
	public static final int GAMEPAD_LEFT_STICK_BUTTON = 10;
	public static final int GAMEPAD_RIGHT_STICK_BUTTON = 11;
	public static final int GAMEPAD_DPAD_UP = 12;
	public static final int GAMEPAD_DPAD_DOWN = 13;
	public static final int GAMEPAD_DPAD_LEFT = 14;
	public static final int GAMEPAD_DPAD_RIGHT = 15;
	public static final int GAMEPAD_GUIDE = 16;

	public static final int GAMEPAD_AXIS_NONE = -1;
	public static final int GAMEPAD_AXIS_LEFT_STICK_X = 0;
	public static final int GAMEPAD_AXIS_LEFT_STICK_Y = 1;
	public static final int GAMEPAD_AXIS_RIGHT_STICK_X = 2;
	public static final int GAMEPAD_AXIS_RIGHT_STICK_Y = 3;

	private static final int GLFW_GAMEPAD_BUTTON_A = 0, GLFW_GAMEPAD_BUTTON_B = 1, GLFW_GAMEPAD_BUTTON_X = 2,
			GLFW_GAMEPAD_BUTTON_Y = 3, GLFW_GAMEPAD_BUTTON_LEFT_BUMPER = 4, GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER = 5,
			GLFW_GAMEPAD_BUTTON_BACK = 6, GLFW_GAMEPAD_BUTTON_START = 7, GLFW_GAMEPAD_BUTTON_GUIDE = 8,
			GLFW_GAMEPAD_BUTTON_LEFT_THUMB = 9, GLFW_GAMEPAD_BUTTON_RIGHT_THUMB = 10, GLFW_GAMEPAD_BUTTON_DPAD_UP = 11,
			GLFW_GAMEPAD_BUTTON_DPAD_RIGHT = 12, GLFW_GAMEPAD_BUTTON_DPAD_DOWN = 13, GLFW_GAMEPAD_BUTTON_DPAD_LEFT = 14;

	private static void registerBtn(int eaglerBtn, int glfwBtn, String name) {
		if(eaglerButtonsToGLFW[eaglerBtn] != 0) throw new IllegalArgumentException("Duplicate eaglerButtonsToGLFW entry: " + eaglerBtn + " -> " + glfwBtn);
		if(glfwBtn != -1 && glfwButtonsToEagler[glfwBtn] != 0) throw new IllegalArgumentException("Duplicate glfwButtonsToEAGLER entry: " + glfwBtn + " -> " + eaglerBtn);
		eaglerButtonsToGLFW[eaglerBtn] = glfwBtn;
		if(glfwBtn != -1) glfwButtonsToEagler[glfwBtn] = eaglerBtn;
		if(buttonNames[eaglerBtn] != null) throw new IllegalArgumentException("Duplicate buttonNames entry: " + eaglerBtn);
		buttonNames[eaglerBtn] = name;
	}

	private static void registerAxis(int eaglerAxis, String name) {
		if(axisNames[eaglerAxis] != null) throw new IllegalArgumentException("Duplicate axisNames entry: " + eaglerAxis);
		axisNames[eaglerAxis] = name;
	}

	static {
		registerBtn(GAMEPAD_A, GLFW_GAMEPAD_BUTTON_A, "A");
		registerBtn(GAMEPAD_B, GLFW_GAMEPAD_BUTTON_B, "B");
		registerBtn(GAMEPAD_X, GLFW_GAMEPAD_BUTTON_X, "X");
		registerBtn(GAMEPAD_Y, GLFW_GAMEPAD_BUTTON_Y, "Y");
		registerBtn(GAMEPAD_LEFT_BUTTON, GLFW_GAMEPAD_BUTTON_LEFT_BUMPER, "Left Button");
		registerBtn(GAMEPAD_LEFT_TRIGGER, -1, "Left Trigger");
		registerBtn(GAMEPAD_RIGHT_BUTTON, GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER, "Right Button");
		registerBtn(GAMEPAD_RIGHT_TRIGGER, -1, "Right Trigger");
		registerBtn(GAMEPAD_BACK, GLFW_GAMEPAD_BUTTON_BACK, "Back");
		registerBtn(GAMEPAD_START, GLFW_GAMEPAD_BUTTON_START, "Start");
		registerBtn(GAMEPAD_LEFT_STICK_BUTTON, GLFW_GAMEPAD_BUTTON_LEFT_THUMB, "L. Stick Button");
		registerBtn(GAMEPAD_RIGHT_STICK_BUTTON, GLFW_GAMEPAD_BUTTON_RIGHT_THUMB, "R. Stick Button");
		registerBtn(GAMEPAD_DPAD_UP, GLFW_GAMEPAD_BUTTON_DPAD_UP, "D-Pad Up");
		registerBtn(GAMEPAD_DPAD_DOWN, GLFW_GAMEPAD_BUTTON_DPAD_DOWN, "D-Pad Down");
		registerBtn(GAMEPAD_DPAD_LEFT, GLFW_GAMEPAD_BUTTON_DPAD_LEFT, "D-Pad Left");
		registerBtn(GAMEPAD_DPAD_RIGHT, GLFW_GAMEPAD_BUTTON_DPAD_RIGHT, "D-Pad Right");
		registerBtn(GAMEPAD_GUIDE, GLFW_GAMEPAD_BUTTON_GUIDE, "Guide");
		registerAxis(GAMEPAD_AXIS_LEFT_STICK_X, "Left Stick X");
		registerAxis(GAMEPAD_AXIS_LEFT_STICK_Y, "Left Stick Y");
		registerAxis(GAMEPAD_AXIS_RIGHT_STICK_X, "Right Stick X");
		registerAxis(GAMEPAD_AXIS_RIGHT_STICK_Y, "Right Stick Y");
	}

	public static int getEaglerButtonFromBrowser(int button) {
		return button;
	}

	public static int getBrowserButtonFromEagler(int button) {
		return button;
	}

	public static int getEaglerButtonFromGLFW(int button) {
		if(button >= 0 && button < glfwButtonsToEagler.length) {
			return glfwButtonsToEagler[button];
		}else {
			return -1;
		}
	}

	public static int getGLFWButtonFromEagler(int button) {
		if(button >= 0 && button < eaglerButtonsToGLFW.length) {
			return eaglerButtonsToGLFW[button];
		}else {
			return -1;
		}
	}

	public static String getButtonName(int button) {
		if(button >= 0 && button < buttonNames.length) {
			return buttonNames[button];
		}else {
			return "Button " + button;
		}
	}

	public static String getAxisName(int button) {
		if(button >= 0 && button < axisNames.length) {
			return axisNames[button];
		}else {
			return "Axis " + button;
		}
	}

}

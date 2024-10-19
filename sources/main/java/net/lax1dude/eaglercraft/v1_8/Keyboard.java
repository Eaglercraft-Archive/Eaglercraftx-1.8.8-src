package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.internal.EnumFireKeyboardEvent;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class Keyboard {
	
	public static void enableRepeatEvents(boolean b) {
		PlatformInput.keyboardEnableRepeatEvents(b);
	}

	public static boolean isCreated() {
		return true;
	}

	public static boolean next() {
		return PlatformInput.keyboardNext();
	}

	public static boolean getEventKeyState() {
		return PlatformInput.keyboardGetEventKeyState();
	}

	public static char getEventCharacter() {
		return PlatformInput.keyboardGetEventCharacter();
	}

	public static int getEventKey() {
		return PlatformInput.keyboardGetEventKey();
	}

	public static void setFunctionKeyModifier(int key) {
		PlatformInput.setFunctionKeyModifier(key);
	}

	public static boolean isKeyDown(int key) {
		return PlatformInput.keyboardIsKeyDown(key);
	}

	public static String getKeyName(int key) {
		return KeyboardConstants.getKeyName(key);
	}

	public static boolean isRepeatEvent() {
		return PlatformInput.keyboardIsRepeatEvent();
	}

	public static void fireEvent(EnumFireKeyboardEvent eventType, int eagKey, char keyChar) {
		PlatformInput.keyboardFireEvent(eventType, eagKey, keyChar);
	}

	public static boolean areKeysLocked() {
		return PlatformInput.keyboardAreKeysLocked();
	}

}

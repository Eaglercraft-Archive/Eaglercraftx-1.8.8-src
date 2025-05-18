/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal;

public class PlatformInput {

	public static native int getWindowWidth();

	public static native int getWindowHeight();

	public static native int getVisualViewportX();

	public static native int getVisualViewportY();

	public static native int getVisualViewportW();

	public static native int getVisualViewportH();

	public static native boolean getWindowFocused();

	public static native boolean isCloseRequested();

	public static native void setVSync(boolean enable);

	public static native void update();

	public static native void update(int fpsLimit);

	public static native boolean isVSyncSupported();

	public static native boolean wasResized();

	public static native boolean wasVisualViewportResized();

	public static native boolean keyboardNext();

	public static native void keyboardFireEvent(EnumFireKeyboardEvent eventType, int eagKey, char keyChar);

	public static native boolean keyboardGetEventKeyState();

	public static native int keyboardGetEventKey();

	public static native char keyboardGetEventCharacter();

	public static native boolean keyboardIsKeyDown(int key);

	public static native boolean keyboardIsRepeatEvent();

	public static native void keyboardEnableRepeatEvents(boolean b);

	public static native boolean keyboardAreKeysLocked();

	public static native boolean mouseNext();

	public static native void mouseFireMoveEvent(EnumFireMouseEvent eventType, int posX, int posY);

	public static native void mouseFireButtonEvent(EnumFireMouseEvent eventType, int posX, int posY, int button);

	public static native void mouseFireWheelEvent(EnumFireMouseEvent eventType, int posX, int posY, float wheel);

	public static native boolean mouseGetEventButtonState();

	public static native int mouseGetEventButton();

	public static native int mouseGetEventX();

	public static native int mouseGetEventY();

	public static native int mouseGetEventDWheel();

	public static native int mouseGetX();

	public static native int mouseGetY();

	public static native boolean mouseIsButtonDown(int i);

	public static native int mouseGetDWheel();

	public static native boolean mouseGrabSupported();

	public static native void mouseSetGrabbed(boolean grab);

	public static native boolean isMouseGrabbed();

	public static native boolean isPointerLocked();

	public static native int mouseGetDX();

	public static native int mouseGetDY();

	public static native void mouseSetCursorPosition(int x, int y);

	public static native boolean mouseIsInsideWindow();

	public static native boolean contextLost();

	public static native void setFunctionKeyModifier(int key);

	public static native boolean supportsFullscreen();

	public static native void toggleFullscreen();

	public static native boolean isFullscreen();

	public static native void showCursor(EnumCursorType cursor);

	public static native boolean touchNext();

	public static native EnumTouchEvent touchGetEventType();

	public static native int touchGetEventTouchPointCount();

	public static native int touchGetEventTouchX(int pointId);

	public static native int touchGetEventTouchY(int pointId);

	public static native float touchGetEventTouchRadiusMixed(int pointId);

	public static native float touchGetEventTouchForce(int pointId);

	public static native int touchGetEventTouchPointUID(int pointId);

	public static native int touchPointCount();

	public static native int touchPointX(int pointId);

	public static native int touchPointY(int pointId);

	public static native float touchRadiusMixed(int pointId);

	public static native float touchForce(int pointId);

	public static native int touchPointUID(int pointId);

	public static native String touchGetPastedString();

	public static native void touchSetOpenKeyboardZone(int x, int y, int w, int h);

	public static native void touchCloseDeviceKeyboard();

	public static native boolean touchIsDeviceKeyboardOpenMAYBE();

	public static native int gamepadGetValidDeviceCount();

	public static native String gamepadGetDeviceName(int deviceId);

	public static native void gamepadSetSelectedDevice(int deviceId);

	public static native void gamepadUpdate();

	public static native boolean gamepadIsValid();

	public static native String gamepadGetName();

	public static native boolean gamepadGetButtonState(int button);

	public static native float gamepadGetAxis(int axis);

	public static native float getDPI();

}
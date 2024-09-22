package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.internal.EnumTouchEvent;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

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
public class Touch {

	public static boolean next() {
		return PlatformInput.touchNext();
	}

	public static EnumTouchEvent getEventType() {
		return PlatformInput.touchGetEventType();
	}

	public static int getEventTouchPointCount() {
		return PlatformInput.touchGetEventTouchPointCount();
	}

	public static int getEventTouchX(int pointId) {
		return PlatformInput.touchGetEventTouchX(pointId);
	}

	public static int getEventTouchY(int pointId) {
		return PlatformInput.touchGetEventTouchY(pointId);
	}

	public static float getEventTouchRadiusX(int pointId) {
		return PlatformInput.touchGetEventTouchRadiusX(pointId);
	}

	public static float getEventTouchRadiusY(int pointId) {
		return PlatformInput.touchGetEventTouchRadiusY(pointId);
	}

	public static float getEventTouchRadiusMixed(int pointId) {
		return PlatformInput.touchGetEventTouchRadiusMixed(pointId);
	}

	public static float getEventTouchForce(int pointId) {
		return PlatformInput.touchGetEventTouchForce(pointId);
	}

	public static int getEventTouchPointUID(int pointId) {
		return PlatformInput.touchGetEventTouchPointUID(pointId);
	}

	public static int touchPointCount() {
		return PlatformInput.touchPointCount();
	}

	public static int touchPointX(int pointId) {
		return PlatformInput.touchPointX(pointId);
	}

	public static int touchPointY(int pointId) {
		return PlatformInput.touchPointY(pointId);
	}

	public static float touchPointRadiusX(int pointId) {
		return PlatformInput.touchRadiusX(pointId);
	}

	public static float touchPointRadiusY(int pointId) {
		return PlatformInput.touchRadiusY(pointId);
	}

	public static float touchPointRadiusMixed(int pointId) {
		return PlatformInput.touchRadiusMixed(pointId);
	}

	public static float touchPointForce(int pointId) {
		return PlatformInput.touchForce(pointId);
	}

	public static int touchPointUID(int pointId) {
		return PlatformInput.touchPointUID(pointId);
	}

	public static void touchSetOpenKeyboardZone(int x, int y, int w, int h) {
		PlatformInput.touchSetOpenKeyboardZone(x, y, w, h);
	}

	public static void closeDeviceKeyboard() {
		PlatformInput.touchCloseDeviceKeyboard();
	}

	public static boolean isDeviceKeyboardOpenMAYBE() {
		return PlatformInput.touchIsDeviceKeyboardOpenMAYBE();
	}

	public static String getPastedString() {
		return PlatformInput.touchGetPastedString();
	}

	public static boolean checkPointTouching(int uid) {
		int cnt = PlatformInput.touchPointCount();
		if(cnt > 0) {
			for(int i = 0; i < cnt; ++i) {
				if(PlatformInput.touchPointUID(i) == uid) {
					return true;
				}
			}
		}
		return false;
	}

	public static int fetchPointIdx(int uid) {
		int cnt = PlatformInput.touchPointCount();
		if(cnt > 0) {
			for(int i = 0; i < cnt; ++i) {
				if(PlatformInput.touchPointUID(i) == uid) {
					return i;
				}
			}
		}
		return -1;
	}
}

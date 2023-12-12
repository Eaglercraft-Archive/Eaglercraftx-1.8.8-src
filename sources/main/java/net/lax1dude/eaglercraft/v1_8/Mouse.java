package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class Mouse {

	public static int getEventDWheel() {
		return PlatformInput.mouseGetEventDWheel();
	}

	public static int getX() {
		return PlatformInput.mouseGetX();
	}

	public static int getY() {
		return PlatformInput.mouseGetY();
	}

	public static boolean getEventButtonState() {
		return PlatformInput.mouseGetEventButtonState();
	}

	public static boolean isCreated() {
		return true;
	}

	public static boolean next() {
		return PlatformInput.mouseNext();
	}

	public static int getEventX() {
		return PlatformInput.mouseGetEventX();
	}

	public static int getEventY() {
		return PlatformInput.mouseGetEventY();
	}

	public static int getEventButton() {
		return PlatformInput.mouseGetEventButton();
	}

	public static boolean isButtonDown(int i) {
		return PlatformInput.mouseIsButtonDown(i);
	}

	public static int getDWheel() {
		return PlatformInput.mouseGetDWheel();
	}

	public static void setGrabbed(boolean grab) {
		PlatformInput.mouseSetGrabbed(grab);
	}

	public static int getDX() {
		return PlatformInput.mouseGetDX();
	}

	public static int getDY() {
		return PlatformInput.mouseGetDY();
	}

	public static void setCursorPosition(int x, int y) {
		PlatformInput.mouseSetCursorPosition(x, y);
	}

	public static boolean isInsideWindow() {
		return PlatformInput.mouseIsInsideWindow();
	}

	public static boolean isActuallyGrabbed() {
		return PlatformInput.isPointerLocked();
	}

	public static boolean isMouseGrabbed() {
		return PlatformInput.isMouseGrabbed();
	}

	private static int customCursorCounter = 0;
	private static EnumCursorType currentCursorType = EnumCursorType.DEFAULT;

	public static void showCursor(EnumCursorType cursor) {
		if(EagRuntime.getConfiguration().useSpecialCursors()) {
			customCursorCounter = 2;
			if(currentCursorType != cursor) {
				PlatformInput.showCursor(cursor);
				currentCursorType = cursor;
			}
		}
	}

	public static void tickCursorShape() {
		if(EagRuntime.getConfiguration().useSpecialCursors()) {
			if(customCursorCounter > 0) {
				if(--customCursorCounter == 0) {
					if(currentCursorType != EnumCursorType.DEFAULT) {
						PlatformInput.showCursor(EnumCursorType.DEFAULT);
						currentCursorType = EnumCursorType.DEFAULT;
					}
				}
			}
		}
	}
}

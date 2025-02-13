/*
 * Copyright (c) 2024 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchControls;
import net.minecraft.client.Minecraft;

public class PointerInputAbstraction {

	protected static Minecraft mc;
	protected static int oldMX = -1;
	protected static int oldMY = -1;
	protected static int oldTX = -1;
	protected static int oldTY = -1;
	protected static int dragStartX = -1;
	protected static int dragStartY = -1;
	protected static int dragStepStartX = -1;
	protected static int dragStepStartY = -1;
	protected static int dragUID = -1;

	protected static int cursorX = -1;
	protected static int cursorY = -1;
	protected static int cursorDX = 0;
	protected static int cursorDY = 0;
	protected static boolean touchingScreen = false;
	protected static boolean touchingScreenNotButton = false;
	protected static boolean draggingNotTouching = false;
	protected static boolean touchMode = false;

	public static void init(Minecraft mcIn) {
		mc = mcIn;
		oldMX = -1;
		oldMY = -1;
		oldTX = -1;
		oldTY = -1;
		dragStartX = -1;
		dragStartY = -1;
		dragStepStartX = -1;
		dragStepStartY = -1;
		dragUID = -1;
		cursorX = -1;
		cursorY = -1;
		cursorDX = 0;
		cursorDY = 0;
		touchingScreen = false;
		touchingScreenNotButton = false;
		draggingNotTouching = false;
		touchMode = !mcIn.mouseGrabSupported;
	}

	public static void runGameLoop() {
		if(touchMode) {
			runTouchUpdate();
		}else {
			oldTX = -1;
			oldTY = -1;
			cursorX = oldMX = Mouse.getX();
			cursorY = oldMY = Mouse.getY();
			cursorDX += Mouse.getDX();
			cursorDY += Mouse.getDY();
		}
	}

	private static void runTouchUpdate() {
		int tc = Touch.touchPointCount();
		if (tc > 0) {
			TouchControls.update(true);
			touchingScreen = true;
			for(int i = 0; i < tc; ++i) {
				int uid = Touch.touchPointUID(i);
				if(TouchControls.touchControls.containsKey(uid)) {
					continue;
				}
				int tx = Touch.touchPointX(i);
				int ty = Touch.touchPointY(i);
				if(TouchControls.overlappingControl(tx, ty) != null) {
					continue;
				}
				if(mc.currentScreen == null && mc.ingameGUI.isTouchOverlapEagler(uid, tx, ty)) {
					continue;
				}
				cursorX = oldTX = tx;
				cursorY = oldTY = ty;
				oldMX = Mouse.getX();
				oldMY = Mouse.getY();
				touchingScreenNotButton = true;
				runTouchDeltaUpdate(uid);
				return;
			}
			touchingScreenNotButton = false;
		} else {
			TouchControls.update(false);
			touchingScreen = false;
			touchingScreenNotButton = false;
			dragStepStartX = -1;
			dragStepStartY = -1;
			dragStartX = -1;
			dragStartY = -1;
			dragUID = -1;
			final int tmp = Mouse.getX();
			final int tmp2 = Mouse.getY();
			if(oldTX == -1 || oldTY == -1) {
				cursorX = oldMX = tmp;
				cursorY = oldMY = tmp2;
				cursorDX += Mouse.getDX();
				cursorDY += Mouse.getDY();
				return;
			}
			if (oldMX == -1 || oldMY == -1) {
				oldMX = tmp;
				oldMY = tmp2;
			}
			if (oldMX == tmp && oldMY == tmp2) {
				cursorX = oldTX;
				cursorY = oldTY;
			}else {
				cursorX = oldMX = tmp;
				cursorY = oldMY = tmp2;
				cursorDX += Mouse.getDX();
				cursorDY += Mouse.getDY();
			}
		}
	}

	private static void runTouchDeltaUpdate(int uid) {
		if(uid != dragUID) {
			dragStartX = oldTX;
			dragStartY = oldTY;
			dragStepStartX = -1;
			dragStepStartY = -1;
			dragUID = uid;
			draggingNotTouching = false;
			return;
		}
		if(dragStepStartX != -1) {
			cursorDX += oldTX - dragStepStartX;
		}
		dragStepStartX = oldTX;
		if(dragStepStartY != -1) {
			cursorDY += oldTY - dragStepStartY;
		}
		dragStepStartY = oldTY;
		if(dragStartX != -1 && dragStartY != -1) {
			int dx = oldTX - dragStartX;
			int dy = oldTY - dragStartY;
			int len = dx * dx + dy * dy;
			int dm = Math.max((int)(6 * Display.getDPI()), 2);
			if(len > dm * dm) {
				draggingNotTouching = true;
			}
		}
	}

	public static boolean isTouchMode() {
		return touchMode;
	}

	public static boolean isTouchingScreen() {
		return touchingScreen;
	}

	public static boolean isTouchingScreenNotButton() {
		return touchingScreenNotButton;
	}

	public static boolean isDraggingNotTouching() {
		return draggingNotTouching;
	}

	public static void enterTouchModeHook() {
		if(!touchMode) {
			touchMode = true;
			if(mc.mouseGrabSupported) {
				mc.mouseHelper.ungrabMouseCursor();
			}
		}
	}

	public static void enterMouseModeHook() {
		if(touchMode) {
			touchMode = false;
			touchingScreen = false;
			touchingScreenNotButton = false;
			if(mc.inGameHasFocus && mc.mouseGrabSupported) {
				mc.mouseHelper.grabMouseCursor();
			}
		}
	}

	public static int getVCursorX() {
		return cursorX;
	}

	public static int getVCursorY() {
		return cursorY;
	}

	public static int getVCursorDX() {
		int tmp = cursorDX;
		cursorDX = 0;
		return tmp;
	}

	public static int getVCursorDY() {
		int tmp = cursorDY;
		cursorDY = 0;
		return tmp;
	}

	public static boolean getVCursorButtonDown(int bt) {
		return (touchingScreenNotButton && bt == 0) || Mouse.isButtonDown(bt);
	}

}
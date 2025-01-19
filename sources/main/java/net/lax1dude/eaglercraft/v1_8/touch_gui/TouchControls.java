package net.lax1dude.eaglercraft.v1_8.touch_gui;

import net.lax1dude.eaglercraft.v1_8.Touch;
import net.lax1dude.eaglercraft.v1_8.touch_gui.EnumTouchControl.TouchAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.*;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;

/**
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
public class TouchControls {

	public static final IntObjectMap<TouchControlInput> touchControls = new IntObjectHashMap<>();
	protected static Set<EnumTouchControl> touchControlPressed = EnumSet.noneOf(EnumTouchControl.class);

	protected static boolean isSneakToggled = false;

	public static void update(boolean screenTouched) {
		Minecraft mc = Minecraft.getMinecraft();
		int h = mc.displayHeight;
		final ScaledResolution sr = mc.scaledResolution;
		int fac = sr.getScaleFactor();
		if(screenTouched) {
			int touchPoints = Touch.touchPointCount();
			int[] loc;
			for(int i = 0; i < touchPoints; ++i) {
				int x = Touch.touchPointX(i);
				int y = h - Touch.touchPointY(i) - 1;
				int uid = Touch.touchPointUID(i);
				TouchControlInput input = touchControls.get(uid);
				if(input != null) {
					EnumTouchControl ctrl = input.control;
					loc = ctrl.getLocation(sr, TouchOverlayRenderer._fuck);
					loc[0] *= fac;
					loc[1] *= fac;
					int size = ctrl.getSize() * fac;
					if (x >= loc[0] && y >= loc[1] && x < loc[0] + size && y < loc[1] + size) {
						continue;
					}
					EnumTouchControl[] en = EnumTouchControl._VALUES;
					for (int j = 0; j < en.length; ++j) {
						EnumTouchControl control = en[j];
						if(!control.visible) continue;
						loc = control.getLocation(sr, TouchOverlayRenderer._fuck);
						loc[0] *= fac;
						loc[1] *= fac;
						size = control.getSize() * fac;
						if (x >= loc[0] && y >= loc[1] && x < loc[0] + size && y < loc[1] + size) {
							touchControls.put(uid, new TouchControlInput(x / fac, y / fac, control));
							break;
						}
					}
				}
			}
			mc.ingameGUI.updateTouchEagler(mc.currentScreen == null);
		}else {
			touchControls.clear();
			touchControlPressed.clear();
			mc.ingameGUI.updateTouchEagler(false);
		}
	}

	public static boolean handleTouchBegin(int uid, int pointX, int pointY) {
		Minecraft mc = Minecraft.getMinecraft();
		pointY = mc.displayHeight - pointY - 1;
		EnumTouchControl control = overlappingControl0(pointX, pointY, mc.scaledResolution);
		if(control != null) {
			int fac = mc.scaledResolution.getScaleFactor();
			touchControls.put(uid, new TouchControlInput(pointX / fac, pointY / fac, control));
			return true;
		}else {
			return mc.currentScreen == null && Minecraft.getMinecraft().ingameGUI.handleTouchBeginEagler(uid, pointX, pointY);
		}
	}

	public static boolean handleTouchEnd(int uid, int pointX, int pointY) {
		if(touchControls.remove(uid) != null) {
			return true;
		}else {
			Minecraft mc = Minecraft.getMinecraft();
			return mc.currentScreen == null && mc.ingameGUI.handleTouchEndEagler(uid, pointX, mc.displayHeight - pointY - 1);
		}
	}

	public static void resetSneak() {
		isSneakToggled = false;
	}

	public static void resetSneakInvalidate() {
		if(isSneakToggled) {
			isSneakToggled = false;
			EnumTouchControl.SNEAK.invalid = true;
			Minecraft.getMinecraft().touchOverlayRenderer.invalidate();
		}
	}

	public static void handleInput() {
		if(!touchControls.isEmpty()) {
			Set<EnumTouchControl> newPressed = EnumSet.noneOf(EnumTouchControl.class);
			TouchOverlayRenderer renderer = Minecraft.getMinecraft().touchOverlayRenderer;
			for (ObjectCursor<TouchControlInput> input_ : touchControls.values()) {
				TouchControlInput input = input_.value;
				TouchAction action = input.control.getAction();
				if(action != null) {
					action.call(input.control, input.x, input.y);
				}
				if(input.control.invalid) {
					renderer.invalidate();
				}
				newPressed.add(input.control);
			}
			touchControlPressed = newPressed;
		}else {
			touchControlPressed.clear();
		}
	}

	public static boolean isPressed(EnumTouchControl control) {
		return touchControlPressed.contains(control);
	}

	public static boolean getSneakToggled() {
		return isSneakToggled;
	}

	public static EnumTouchControl overlappingControl(int tx, int ty) {
		Minecraft mc = Minecraft.getMinecraft();
		ty = mc.displayHeight - ty - 1;
		return overlappingControl0(tx, ty, mc.scaledResolution);
	}

	private static EnumTouchControl overlappingControl0(int pointX, int pointY, ScaledResolution sr) {
		EnumTouchControl[] en = EnumTouchControl._VALUES;
		int[] loc;
		int fac = sr.getScaleFactor();
		int size;
		for (int j = 0; j < en.length; ++j) {
			EnumTouchControl control = en[j];
			if(!control.visible) continue;
			loc = control.getLocation(sr, TouchOverlayRenderer._fuck);
			loc[0] *= fac;
			loc[1] *= fac;
			size = control.getSize() * fac;
			if (pointX >= loc[0] && pointY >= loc[1] && pointX < loc[0] + size && pointY < loc[1] + size) {
				return control;
			}
		}
		return null;
	}

}

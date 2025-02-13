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

package net.lax1dude.eaglercraft.v1_8;

import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.GamepadConstants;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

public class Gamepad {

	private static final boolean[] buttonsLastState = new boolean[24];
	private static final List<VirtualButtonEvent> buttonEvents = new LinkedList<>();
	private static VirtualButtonEvent currentVEvent = null;

	private static class VirtualButtonEvent {

		private final int button;
		private final boolean state;

		public VirtualButtonEvent(int button, boolean state) {
			this.button = button;
			this.state = state;
		}

	}

	public static int getValidDeviceCount() {
		return PlatformInput.gamepadGetValidDeviceCount();
	}

	public static String getDeviceName(int deviceId) {
		return PlatformInput.gamepadGetDeviceName(deviceId);
	}

	public static void setSelectedDevice(int deviceId) {
		PlatformInput.gamepadSetSelectedDevice(deviceId);
	}

	public static void update() {
		PlatformInput.gamepadUpdate();
		if(isValid()) {
			for(int i = 0; i < buttonsLastState.length; ++i) {
				boolean b = PlatformInput.gamepadGetButtonState(i);
				if(b != buttonsLastState[i]) {
					buttonsLastState[i] = b;
					buttonEvents.add(new VirtualButtonEvent(i, b));
					if(buttonEvents.size() > 64) {
						buttonEvents.remove(0);
					}
				}
			}
		}else {
			for(int i = 0; i < buttonsLastState.length; ++i) {
				buttonsLastState[i] = false;
			}
		}
	}

	public static boolean next() {
		currentVEvent = null;
		return !buttonEvents.isEmpty() && (currentVEvent = buttonEvents.remove(0)) != null;
	}

	public static int getEventButton() {
		return currentVEvent != null ? currentVEvent.button : -1;
	}

	public static boolean getEventButtonState() {
		return currentVEvent != null ? currentVEvent.state : false;
	}

	public static boolean isValid() {
		return PlatformInput.gamepadIsValid();
	}

	public static String getName() {
		return PlatformInput.gamepadGetName();
	}

	public static boolean getButtonState(int button) {
		return PlatformInput.gamepadGetButtonState(button);
	}

	public static String getButtonName(int button) {
		return GamepadConstants.getButtonName(button);
	}

	public static float getAxis(int axis) {
		return PlatformInput.gamepadGetAxis(axis);
	}

	public static String getAxisName(int button) {
		return GamepadConstants.getAxisName(button);
	}

	public static void clearEventBuffer() {
		buttonEvents.clear();
	}

}
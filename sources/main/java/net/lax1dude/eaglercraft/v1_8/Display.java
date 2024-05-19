package net.lax1dude.eaglercraft.v1_8;

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
public class Display {

	private static long lastSwap = 0l;

	public static int getWidth() {
		return PlatformInput.getWindowWidth();
	}
	
	public static int getHeight() {
		return PlatformInput.getWindowHeight();
	}

	public static boolean isActive() {
		return PlatformInput.getWindowFocused();
	}

	public static void create() {
		
	}

	public static void setTitle(String string) {
		
	}

	public static boolean isCloseRequested() {
		return PlatformInput.isCloseRequested();
	}

	public static void setVSync(boolean enable) {
		PlatformInput.setVSync(enable);
	}

	public static boolean isVSyncSupported() {
		return PlatformInput.isVSyncSupported();
	}

	public static void update() {
		PlatformInput.update();
	}

	public static void sync(int limitFramerate) {
		boolean limitFPS = limitFramerate > 0 && limitFramerate < 1000;
		
		if(limitFPS) {
			long millis = System.currentTimeMillis();
			long frameMillis = (1000l / limitFramerate) - (millis - lastSwap);
			if(frameMillis > 0l) {
				EagUtils.sleep(frameMillis);
			}
		}
		
		lastSwap = System.currentTimeMillis();
	}

	public static boolean contextLost() {
		return PlatformInput.contextLost();
	}
	
	public static boolean wasResized() {
		return PlatformInput.wasResized();
	}

	public static boolean isFullscreen() {
		return PlatformInput.isFullscreen();
	}

	public static void toggleFullscreen() {
		PlatformInput.toggleFullscreen();
	}

}

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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import java.util.function.Consumer;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapterHooks;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts.JSEaglercraftXOptsHooks;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class WASMGCClientConfigAdapterHooks implements IClientConfigAdapterHooks {

	private static final Logger logger = LogManager.getLogger("TeaVMClientConfigAdapterHooks");

	private LocalStorageSaveHook saveHook = null;
	private LocalStorageLoadHook loadHook = null;
	private CrashReportHook crashHook = null;
	private ScreenChangeHook screenChangedHook = null;

	@JSFunctor
	private static interface LocalStorageSaveHook extends JSObject {
		void call(String key, String base64);
	}

	@Override
	public void callLocalStorageSavedHook(String key, String base64) {
		if(saveHook != null) {
			try {
				saveHook.call(key, base64);
			}catch(Throwable t) {
				logger.error("Caught exception while invoking eaglercraftXOpts \"localStorageSaved\" hook!");
				logger.error(t);
			}
		}
	}

	@JSFunctor
	private static interface LocalStorageLoadHook extends JSObject {
		String call(String key);
	}

	@Override
	public String callLocalStorageLoadHook(String key) {
		if(loadHook != null) {
			try {
				return loadHook.call(key);
			}catch(Throwable t) {
				logger.error("Caught exception while invoking eaglercraftXOpts \"localStorageLoaded\" hook!");
				logger.error(t);
				return null;
			}
		}else {
			return null;
		}
	}

	@JSFunctor
	private static interface ScreenChangeHook extends JSObject {
		String call(String screenName, int scaledWidth, int scaledHeight, int realWidth, int realHeight,
				int scaleFactor);
	}

	@Override
	public void callScreenChangedHook(String screenName, int scaledWidth, int scaledHeight, int realWidth,
			int realHeight, int scaleFactor) {
		if(screenChangedHook != null) {
			try {
				screenChangedHook.call(screenName, scaledWidth, scaledHeight, realWidth, realHeight, scaleFactor);
			}catch(Throwable t) {
				logger.error("Caught exception while invoking eaglercraftXOpts \"screenChanged\" hook!");
				logger.error(t);
			}
		}
	}

	@JSFunctor
	private static interface CrashReportHook extends JSObject {
		void call(String crashReport, CustomMessageCB customMessageCB);
	}

	@JSFunctor
	private static interface CustomMessageCB extends JSObject {
		void call(String msg);
	}

	@Override
	public void callCrashReportHook(String crashReport, Consumer<String> customMessageCB) {
		if(crashHook != null) {
			try {
				crashHook.call(crashReport, (msg) -> customMessageCB.accept(msg));
			}catch(Throwable t) {
				logger.error("Caught exception while invoking eaglercraftXOpts \"screenChanged\" hook!");
				logger.error(t);
			}
		}
	}

	public void loadHooks(JSEaglercraftXOptsHooks hooks) {
		saveHook = (LocalStorageSaveHook)hooks.getLocalStorageSavedHook();
		loadHook = (LocalStorageLoadHook)hooks.getLocalStorageLoadedHook();
		crashHook = (CrashReportHook)hooks.getCrashReportHook();
		screenChangedHook = (ScreenChangeHook)hooks.getScreenChangedHook();
	}

}
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

import java.io.PrintStream;

import org.teavm.interop.Import;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.ContextLostError;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.opts.JSEaglercraftXOptsRoot;
import net.minecraft.client.main.Main;

public class ClientMain {

	private static final PrintStream systemOut = System.out;
	private static final PrintStream systemErr = System.err;
	public static String configLocalesFolder = null;

	public static void _main() {
		try {
			systemOut.println("ClientMain: [INFO] eaglercraftx wasm gc is starting...");
			JSObject opts = getEaglerXOpts();

			if(opts == null) {
				systemErr.println("ClientMain: [ERROR] the \"window.eaglercraftXOpts\" variable is undefined");
				systemErr.println("ClientMain: [ERROR] eaglercraftx cannot start");
				Window.alert("ERROR: game cannot start, the \"window.eaglercraftXOpts\" variable is undefined");
				return;
			}

			try {
				JSEaglercraftXOptsRoot eaglercraftOpts = (JSEaglercraftXOptsRoot)opts;
				
				configLocalesFolder = eaglercraftOpts.getLocalesURI("lang");
				if(configLocalesFolder.endsWith("/")) {
					configLocalesFolder = configLocalesFolder.substring(0, configLocalesFolder.length() - 1);
				}
				
				((WASMGCClientConfigAdapter)WASMGCClientConfigAdapter.instance).loadNative(eaglercraftOpts);
				
				systemOut.println("ClientMain: [INFO] configuration was successful");
			}catch(Throwable t) {
				systemErr.println("ClientMain: [ERROR] the \"window.eaglercraftXOpts\" variable is invalid");
				EagRuntime.debugPrintStackTraceToSTDERR(t);
				systemErr.println("ClientMain: [ERROR] eaglercraftx cannot start");
				Window.alert("ERROR: game cannot start, the \"window.eaglercraftXOpts\" variable is invalid: " + t.toString());
				return;
			}

			systemOut.println("ClientMain: [INFO] initializing eaglercraftx runtime");

			try {
				EagRuntime.create();
			}catch(ContextLostError t) {
				systemErr.println("ClientMain: [ERROR] webgl context lost during initialization!");
				PlatformRuntime.showContextLostScreen(EagRuntime.getStackTrace(t));
				return;
			}catch(Throwable t) {
				systemErr.println("ClientMain: [ERROR] eaglercraftx's runtime could not be initialized!");
				EagRuntime.debugPrintStackTraceToSTDERR(t);
				PlatformRuntime.writeCrashReport("EaglercraftX's runtime could not be initialized!\n\n" + EagRuntime.getStackTrace(t));
				systemErr.println("ClientMain: [ERROR] eaglercraftx cannot start");
				return;
			}

			systemOut.println("ClientMain: [INFO] launching eaglercraftx main thread");

			try {
				Main.appMain();
			}catch(ContextLostError t) {
				systemErr.println("ClientMain: [ERROR] webgl context lost!");
				PlatformRuntime.showContextLostScreen(EagRuntime.getStackTrace(t));
			}catch(Throwable t) {
				systemErr.println("ClientMain: [ERROR] unhandled exception caused main thread to exit");
				EagRuntime.debugPrintStackTraceToSTDERR(t);
				PlatformRuntime.writeCrashReport("Unhandled exception caused main thread to exit!\n\n" + EagRuntime.getStackTrace(t));
			}
		}finally {
			systemErr.println("ClientMain: [ERROR] eaglercraftx main thread has exited");
		}
	}


	@Import(module = "platformRuntime", name = "getEaglercraftXOpts")
	private static native JSObject getEaglerXOpts();

}
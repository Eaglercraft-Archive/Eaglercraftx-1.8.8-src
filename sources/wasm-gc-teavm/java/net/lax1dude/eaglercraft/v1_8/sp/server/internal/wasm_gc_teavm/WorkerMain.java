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

package net.lax1dude.eaglercraft.v1_8.sp.server.internal.wasm_gc_teavm;

import java.io.PrintStream;

import org.teavm.interop.Import;
import org.teavm.jso.JSObject;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WASMGCClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket15Crashed;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacketFFProcessKeepAlive;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.lax1dude.eaglercraft.v1_8.sp.server.internal.ServerPlatformSingleplayer;

public class WorkerMain {

	public static void _main() {
		PrintStream systemOut = System.out;
		PrintStream systemErr = System.err;
		try {
			systemOut.println("WorkerMain: [INFO] eaglercraftx worker thread is starting...");
			JSObject startArgs = getEaglerXOpts();
			systemOut.println("WorkerMain: [INFO] reading configuration");
			((WASMGCClientConfigAdapter)WASMGCClientConfigAdapter.instance).loadNative(startArgs);
			systemOut.println("WorkerMain: [INFO] initializing server runtime");
			ServerPlatformSingleplayer.initializeContext();
			systemOut.println("WorkerMain: [INFO] starting worker thread");
			PlatformRuntime.setThreadName("IntegratedServer");
			EaglerIntegratedServerWorker.serverMain();
		}catch(Throwable t) {
			System.setOut(systemOut);
			System.setErr(systemErr);
			systemErr.println("WorkerMain: [ERROR] uncaught exception thrown!");
			EagRuntime.debugPrintStackTraceToSTDERR(t);
			EaglerIntegratedServerWorker.sendIPCPacket(new IPCPacket15Crashed("UNCAUGHT EXCEPTION CAUGHT IN WORKER PROCESS!\n\n" + EagRuntime.getStackTrace(t)));
			EaglerIntegratedServerWorker.sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacketFFProcessKeepAlive.EXITED));
		}finally {
			systemErr.println("WorkerMain: [ERROR] eaglercraftx worker thread has exited");
		}
	}

	@Import(module = "platformRuntime", name = "getEaglercraftXOpts")
	private static native JSObject getEaglerXOpts();

}
package net.lax1dude.eaglercraft.v1_8.sp.server.internal.teavm;

import java.io.PrintStream;

import org.json.JSONObject;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket15Crashed;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacketFFProcessKeepAlive;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.lax1dude.eaglercraft.v1_8.sp.server.internal.ServerPlatformSingleplayer;

/**
 * Copyright (c) 2023-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class WorkerMain {

	public static void _main() {
		PrintStream systemOut = System.out;
		PrintStream systemErr = System.err;
		try {
			__println(systemOut, false, "WorkerMain: [INFO] eaglercraftx worker thread is starting...");
			String startArgs = getStartArgs();
			__println(systemOut, false, "WorkerMain: [INFO] reading configuration");
			if(startArgs == null) {
				throw new NullPointerException("startup arguments is null!");
			}
			((TeaVMClientConfigAdapter)TeaVMClientConfigAdapter.instance).loadJSON(new JSONObject(startArgs));
			__println(systemOut, false, "WorkerMain: [INFO] initializing server runtime");
			EaglerIntegratedServerWorker.enableLoggingRedirector(true);
			ServerPlatformSingleplayer.initializeContext();
			__println(systemOut, false, "WorkerMain: [INFO] starting worker thread");
			PlatformRuntime.setThreadName("IntegratedServer");
			EaglerIntegratedServerWorker.serverMain();
		}catch(Throwable t) {
			System.setOut(systemOut);
			System.setErr(systemErr);
			__println(systemErr, true, "WorkerMain: [ERROR] uncaught exception thrown!");
			EaglerIntegratedServerWorker.sendLogMessagePacket(EagRuntime.getStackTrace(t), true);
			EagRuntime.debugPrintStackTraceToSTDERR(t);
			EaglerIntegratedServerWorker.sendIPCPacket(new IPCPacket15Crashed("UNCAUGHT EXCEPTION CAUGHT IN WORKER PROCESS!\n\n" + EagRuntime.getStackTrace(t)));
			EaglerIntegratedServerWorker.sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacketFFProcessKeepAlive.EXITED));
		}finally {
			__println(systemErr, true, "WorkerMain: [ERROR] eaglercraftx worker thread has exited");
		}
	}

	private static void __println(PrintStream stream, boolean err, String msg) {
		stream.println(msg);
		try {
			EaglerIntegratedServerWorker.sendLogMessagePacket(msg, err);
		}catch(Throwable t) {
		}
	}

	@JSFunctor
	private static interface WorkerArgumentsPacketHandler extends JSObject {
		public void onMessage(String msg);
	}

	@JSBody(params = { "wb" }, script = "onmessage = function(o) { wb(o.data.msg); };")
	private static native void setOnMessage(WorkerArgumentsPacketHandler cb);

	@Async
	private static native String getStartArgs();

	private static void getStartArgs(final AsyncCallback<String> cb) {
		setOnMessage(new WorkerArgumentsPacketHandler() {

			@Override
			public void onMessage(String msg) {
				ServerPlatformSingleplayer.register();
				cb.complete(msg);
			}

		});
	}

}

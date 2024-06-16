package net.lax1dude.eaglercraft.v1_8.internal;

import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.ArrayBuffer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUpdateThread;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;
import net.lax1dude.eaglercraft.v1_8.update.UpdateProgressStruct;

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
public class PlatformUpdateSvc {

	private static final Logger logger = LogManager.getLogger("PlatformUpdateSvc");

	private static byte[] eaglercraftXClientSignature = null;
	private static byte[] eaglercraftXClientBundle = null;

	private static final UpdateProgressStruct progressStruct = new UpdateProgressStruct();

	@JSBody(params = { }, script = "if(typeof window.eaglercraftXClientSignature !== \"string\") return null; var ret = window.eaglercraftXClientSignature; window.eaglercraftXClientSignature = null; return ret;")
	private static native String grabEaglercraftXClientSignature();

	@JSBody(params = { }, script = "if(typeof window.eaglercraftXClientBundle !== \"string\") return null; var ret = window.eaglercraftXClientBundle; window.eaglercraftXClientBundle = null; return ret;")
	private static native String grabEaglercraftXClientBundle();

	public static Thread updateThread = null;

	public static boolean supported() {
		return true;
	}

	public static void initialize() {
		eaglercraftXClientSignature = loadClientData(grabEaglercraftXClientSignature());
		eaglercraftXClientBundle = loadClientData(grabEaglercraftXClientBundle());
	}

	private static byte[] loadClientData(String url) {
		if(url == null) {
			return null;
		}
		ArrayBuffer buf = PlatformRuntime.downloadRemoteURI(url);
		if(buf == null) {
			logger.error("Failed to download client bundle or signature URL!");
			return null;
		}
		return TeaVMUtils.wrapByteArrayBuffer(buf);
	}

	public static byte[] getClientSignatureData() {
		return eaglercraftXClientSignature;
	}

	public static byte[] getClientBundleData() {
		return eaglercraftXClientBundle;
	}

	public static void startClientUpdateFrom(UpdateCertificate clientUpdate) {
		if(updateThread == null || !updateThread.isAlive()) {
			updateThread = new Thread(new TeaVMUpdateThread(clientUpdate, progressStruct), "EaglerUpdateThread");
			updateThread.setDaemon(true);
			updateThread.start();
		}else {
			logger.error("Tried to start a new download while the current download thread was still alive!");
		}
	}

	public static UpdateProgressStruct getUpdatingStatus() {
		return progressStruct;
	}

	public static void quine(String filename, byte[] cert, byte[] data, String date) {
		EagRuntime.downloadFileWithName(filename, TeaVMUpdateThread.generateSignedOffline(cert, data, date));
	}

	public static void quine(UpdateCertificate clientUpdate, byte[] data) {
		TeaVMUpdateThread.downloadSignedOffline(clientUpdate, data);
	}
}

package net.lax1dude.eaglercraft.v1_8.internal;

import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.ArrayBuffer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuEntryPoint;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUpdateThread;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;
import net.lax1dude.eaglercraft.v1_8.update.UpdateProgressStruct;
import net.lax1dude.eaglercraft.v1_8.update.UpdateResultObj;

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
	private static UpdateResultObj updateResult = null;

	private static final UpdateProgressStruct progressStruct = new UpdateProgressStruct();

	@JSBody(params = { }, script = "if(typeof eaglercraftXClientSignature !== \"string\") return null; var ret = eaglercraftXClientSignature; eaglercraftXClientSignature = null; return ret;")
	private static native String grabEaglercraftXClientSignature();

	@JSBody(params = { }, script = "if(typeof eaglercraftXClientBundle !== \"string\") return null; var ret = eaglercraftXClientBundle; eaglercraftXClientBundle = null; return ret;")
	private static native String grabEaglercraftXClientBundle();

	public static Thread updateThread = null;

	private static boolean hasInitialized = false;

	public static boolean supported() {
		return true;
	}

	public static void initialize() {
		if(!hasInitialized) {
			hasInitialized = true;
			eaglercraftXClientSignature = loadClientData(grabEaglercraftXClientSignature());
			eaglercraftXClientBundle = loadClientData(grabEaglercraftXClientBundle());
		}
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
		if(!hasInitialized) {
			initialize();
		}
		return eaglercraftXClientSignature;
	}

	public static byte[] getClientBundleData() {
		if(!hasInitialized) {
			initialize();
		}
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

	public static UpdateResultObj getUpdateResult() {
		UpdateResultObj ret = updateResult;
		if(ret != null) {
			updateResult = null;
			return ret;
		}else {
			return null;
		}
	}

	public static void setUpdateResultTeaVM(UpdateResultObj obj) {
		updateResult = obj;
	}

	public static void installSignedClient(UpdateCertificate clientCert, byte[] clientPayload, boolean setDefault,
			boolean setTimeout) {
		BootMenuEntryPoint.installSignedClientAtRuntime(
				clientCert.bundleDisplayName + " " + clientCert.bundleDisplayVersion, PlatformRuntime.win,
				clientCert.rawCertData, clientPayload, setDefault, setTimeout);
	}

	public static void quine(String filename, byte[] cert, byte[] data, String date) {
		EagRuntime.downloadFileWithName(filename, TeaVMUpdateThread.generateSignedOffline(cert, data, date));
	}

	public static void quine(UpdateCertificate clientUpdate, byte[] data) {
		TeaVMUpdateThread.downloadSignedOffline(clientUpdate, data);
	}
}

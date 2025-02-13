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

package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.teavm.jso.browser.Window;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class SignedClientInstaller {

	private static final Logger logger = LogManager.getLogger("SignedClientInstaller");

	public static void installSignedClientAtRuntime(String displayName, Window win, byte[] clientCert,
			byte[] clientPayload, boolean setDefault, boolean setTimeout) {
		logger.info("Enabling boot menu...");
		int f = BootMenuDataManager.getBootMenuFlags(win);
		if(f == -1) f = 0;
		f |= 1;
		BootMenuDataManager.setBootMenuFlags(win, f);
		logger.info("Loading datastore...");
		BootMenuDatastore dstore = BootMenuDatastore.openDatastore();
		try {
			logger.info("Loading manifest...");
			BootMenuDataManager dmgr = new BootMenuDataManager(dstore);
			logger.info("Generating client data...");
			EaglercraftUUID certUUID = EaglercraftUUID.nameUUIDFromBytes(clientCert);
			EaglercraftUUID payloadUUID = EaglercraftUUID.nameUUIDFromBytes(clientPayload);
			Map<EaglercraftUUID, byte[]> blobs = new HashMap<>(2);
			blobs.put(certUUID, clientCert);
			blobs.put(payloadUUID, clientPayload);
			ClientDataEntry clientData = new ClientDataEntry(EnumClientFormatType.EAGLER_SIGNED_OFFLINE,
					EaglercraftUUID.randomUUID(), payloadUUID, null, certUUID, null);
			JSONObject launchOptsJSON = BootMenuEntryPoint.getOriginLaunchOptsJSON();
			launchOptsJSON.put("bootMenuBlocksUnsignedClients", false);
			RelayRandomizeHelper.makeOptsJSONHaveMacroHack(launchOptsJSON);
			String launchOpts = launchOptsJSON.toString(4);
			LaunchConfigEntry launchData = new LaunchConfigEntry(EaglercraftUUID.randomUUID(), clientData.uuid, displayName,
					EnumClientLaunchType.EAGLERX_SIGNED_V1, null, null, null, null, null, launchOpts, false);
			logger.info("Installing client data...");
			dmgr.installNewClientData(launchData, clientData, blobs, false);
			if(setDefault) {
				logger.info("Setting boot order...");
				while(dmgr.launchOrderList.remove(launchData.uuid));
				dmgr.launchOrderList.add(0, launchData.uuid);
				dmgr.writeManifest();
				if(setTimeout) {
					logger.info("Setting boot timeout...");
					dmgr.confBootTimeout = 5;
					dmgr.saveAdditionalConf();
				}
			}
		}finally {
			logger.info("Cleaning up...");
			dstore.closeDatastore();
		}
	}

}
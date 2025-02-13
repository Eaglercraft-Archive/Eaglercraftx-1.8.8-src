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

import org.json.JSONObject;

public class EaglerXBungeeVersion {

	public static final String pluginFileEPK = "plugin_download.zip";

	private static String pluginName = null;
	private static String pluginVersion = null;
	private static long pluginVersionLong = 0l;
	private static String pluginButton = null;
	private static String pluginFilename = null;

	public static void initialize() {
		String pluginVersionJson = EagRuntime.getRequiredResourceString("plugin_version.json");
		JSONObject json = new JSONObject(pluginVersionJson);
		pluginName = json.getString("pluginName");
		pluginVersion = json.getString("pluginVersion");
		pluginVersionLong = getVersionAsLong(pluginVersion);
		pluginButton = json.getString("pluginButton");
		pluginFilename = json.getString("pluginFilename");
	}

	public static String getPluginName() {
		return pluginName;
	}

	public static String getPluginVersion() {
		return pluginVersion;
	}

	public static long getPluginVersionLong() {
		return pluginVersionLong;
	}

	public static String getPluginButton() {
		return pluginButton;
	}

	public static String getPluginFilename() {
		return pluginFilename;
	}

	public static long getVersionAsLong(String vers) {
		try {
			String[] verz = vers.split("\\.");
			long ret = 0;
			long div = 1000000000000l;
			for(int i = 0; i < verz.length; ++i) {
				ret += div * Long.parseLong(verz[i]);
				div /= 10000l;
			}
			return ret;
		}catch(Throwable t) {
			return -1l;
		}
	}

	public static byte[] getPluginDownload() {
		return EagRuntime.getRequiredResourceBytes(pluginFileEPK);
	}

	public static void startPluginDownload() {
		EagRuntime.downloadFileWithName(pluginFilename, getPluginDownload());
	}

	public static boolean isUpdateToPluginAvailable(String brand, String vers) {
		if(pluginVersionLong == -1l || !pluginName.equals(brand)) {
			return false;
		}
		long verz = getVersionAsLong(vers);
		return verz != -1l && verz < pluginVersionLong;
	}
}
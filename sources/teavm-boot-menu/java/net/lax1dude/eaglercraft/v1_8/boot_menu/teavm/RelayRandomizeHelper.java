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

import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;

public class RelayRandomizeHelper {

	public static int countRelayMacro(String launchOpts) {
		int i = 0;
		while(launchOpts.contains("\"$random_relay_primary_" + i + "\"")) {
			++i;
		}
		return i;
	}

	public static String replaceRelayMacroWithConstant(String launchOpts) {
		int i = countRelayMacro(launchOpts);
		if(i == 0) {
			return launchOpts;
		}
		int randomRelay = ThreadLocalRandom.current().nextInt(i);
		for(int j = 0; j < i; ++j) {
			launchOpts = launchOpts.replace("\"$random_relay_primary_" + j + "\"", randomRelay == j ? "true" : "false");
		}
		return launchOpts;
	}

	public static String replaceRelayMacroWithEqRelayId(String launchOpts) {
		int i = countRelayMacro(launchOpts);
		if(i == 0) {
			return launchOpts;
		}
		for(int j = 0; j < i; ++j) {
			launchOpts = launchOpts.replace("\"$random_relay_primary_" + j + "\"", "relayId === " + j);
		}
		return launchOpts;
	}

	public static void makeOptsJSONHaveMacroHack(JSONObject optsDump) {
		int i = 0;
		JSONArray arr = optsDump.optJSONArray("relays");
		if(arr != null) {
			for(int j = 0, l = arr.length(); j < l; ++j) {
				JSONObject relay = arr.optJSONObject(j);
				if(relay != null) {
					if(relay.has("primary")) {
						relay.put("primary", "$random_relay_primary_" + i++);
					}
				}
			}
		}
	}

}
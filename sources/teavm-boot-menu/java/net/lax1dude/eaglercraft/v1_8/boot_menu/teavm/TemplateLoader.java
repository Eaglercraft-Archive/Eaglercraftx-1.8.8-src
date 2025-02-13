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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

public class TemplateLoader {

	public static final Map<String, String> baseGlobals;
	
	static {
		baseGlobals = new HashMap<>();
		baseGlobals.put("client_name", BootMenuConstants.client_projectForkName);
		baseGlobals.put("client_vendor", BootMenuConstants.client_projectForkVendor);
		baseGlobals.put("client_version", BootMenuConstants.client_projectForkVersion);
		baseGlobals.put("game_version", BootMenuConstants.client_projectOriginRevision);
		baseGlobals.put("client_fork_name", BootMenuConstants.client_projectForkName);
		baseGlobals.put("client_fork_vendor", BootMenuConstants.client_projectForkVendor);
		baseGlobals.put("client_fork_version", BootMenuConstants.client_projectForkVersion);
		baseGlobals.put("client_origin_name", BootMenuConstants.client_projectOriginName);
		baseGlobals.put("client_origin_vendor", BootMenuConstants.client_projectOriginAuthor);
		baseGlobals.put("client_origin_version", BootMenuConstants.client_projectOriginVersion);
		baseGlobals.put("client_origin_revision", BootMenuConstants.client_projectOriginRevision);
		EaglercraftRandom randomCharGenerator = new EaglercraftRandom();
		char[] vigg = new char[16];
		String charSel = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i = 0; i < vigg.length; ++i) {
			vigg[i] = charSel.charAt(randomCharGenerator.nextInt(charSel.length()));
		}
		baseGlobals.put("root_class_gen", BootMenuConstants.cssClassPrefix + (new String(vigg)));
	}

	public static String loadTemplate(String path) throws IOException {
		return loadTemplate(path, null);
	}

	public static String loadTemplate(String path, Map<String, String> globals) throws IOException {
		String basePath;
		int i = path.lastIndexOf('/');
		if(i != -1) {
			basePath = path.substring(0, i);
		}else {
			basePath = "";
		}
		if(globals != null) {
			Map<String, String> newGlobals = new HashMap<>();
			newGlobals.putAll(baseGlobals);
			newGlobals.putAll(globals);
			globals = newGlobals;
		}else {
			globals = baseGlobals;
		}
		String templateContent = BootMenuAssets.loadResourceString(path);
		if(templateContent == null) {
			throw new IOException("Could not load template: \"" + path + "\"");
		}
		return TemplateParser.loadTemplate(templateContent, basePath, true, globals);
	}

}
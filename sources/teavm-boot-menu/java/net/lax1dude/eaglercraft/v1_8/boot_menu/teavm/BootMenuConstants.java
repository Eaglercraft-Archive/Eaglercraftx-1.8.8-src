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

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;

public class BootMenuConstants {

	public static final EaglercraftUUID UUID_ORIGIN_UNSIGNED_CLASSES_JS = new EaglercraftUUID(0x738248F88FF1446EL, 0xA834D40120DD8EB5L);
	public static final EaglercraftUUID UUID_ORIGIN_SIGNED_SIGNATURE = new EaglercraftUUID(0xB55252A38A6F4291L, 0x8DB68BF94B3E6FEDL);
	public static final EaglercraftUUID UUID_ORIGIN_SIGNED_BUNDLE = new EaglercraftUUID(0xCE298D98E9084597L, 0x9EB2501EAC6D720BL);

	public static final EaglercraftUUID UUID_CLIENT_DATA_ORIGIN = new EaglercraftUUID(0xB673DAD0EF4407BL, 0xBE12C8E5BD5A2CBDL);
	public static final EaglercraftUUID UUID_CLIENT_LAUNCH_ORIGIN = new EaglercraftUUID(0x74FB063984A24D1AL, 0x8E1D2FC39C21EA1EL);

	public static final String client_projectForkName = EaglercraftVersion.projectForkName;
	public static final String client_projectForkVendor = EaglercraftVersion.projectForkVendor;
	public static final String client_projectForkVersion = EaglercraftVersion.projectForkVersion;

	public static final String client_projectOriginName = EaglercraftVersion.projectOriginName;
	public static final String client_projectOriginAuthor = EaglercraftVersion.projectOriginAuthor;
	public static final String client_projectOriginVersion = EaglercraftVersion.projectOriginVersion;
	public static final String client_projectOriginRevision = EaglercraftVersion.projectOriginRevision;

	public static final String cssClassPrefix = "_eaglercraftX_";
	public static final String cssClassPrefixBootMenu = cssClassPrefix + "boot_menu_";

	public static final String bootMenuDatabaseName = "_net_lax1dude_eaglercraft_v1_8_boot_menu_BootMenuDatastore_1_8_8_main";

	public static String getBootMenuFlagsKeyName() {
		String pfx = EagRuntime.getConfiguration().getLocalStorageNamespace();
		if(pfx == null) {
			pfx = EaglercraftVersion.localStorageNamespace;
		}
		return pfx + ".showBootMenu";
	}

}
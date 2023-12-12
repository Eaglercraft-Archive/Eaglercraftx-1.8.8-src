package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayEntry;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class DesktopClientConfigAdapter implements IClientConfigAdapter {

	public static final IClientConfigAdapter instance = new DesktopClientConfigAdapter();

	public final List<DefaultServer> defaultServers = new ArrayList();

	@Override
	public String getDefaultLocale() {
		return "en_US";
	}

	@Override
	public List<DefaultServer> getDefaultServerList() {
		return defaultServers;
	}

	@Override
	public String getServerToJoin() {
		return null;
	}

	@Override
	public String getWorldsDB() {
		return "desktop";
	}

	@Override
	public JSONObject dumpConfig() {
		return new JSONObject("{\"container\":null,\"worldsDB\":\"desktop\"}");
	}

	@Override
	public List<RelayEntry> getRelays() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public boolean checkShaderGLErrors() {
		return true;
	}

	@Override
	public boolean isDemo() {
		return EaglercraftVersion.forceDemoMode;
	}

	@Override
	public boolean allowUpdateSvc() {
		return false;
	}

	@Override
	public boolean allowUpdateDL() {
		return false;
	}

	@Override
	public boolean isEnableDownloadOfflineButton() {
		return false;
	}

	@Override
	public String getDownloadOfflineButtonLink() {
		return null;
	}

	@Override
	public boolean useSpecialCursors() {
		return false;
	}

	@Override
	public boolean isLogInvalidCerts() {
		return false;
	}

	@Override
	public boolean isCheckRelaysForUpdates() {
		return false;
	}

	@Override
	public boolean isEnableSignatureBadge() {
		return false;
	}

}

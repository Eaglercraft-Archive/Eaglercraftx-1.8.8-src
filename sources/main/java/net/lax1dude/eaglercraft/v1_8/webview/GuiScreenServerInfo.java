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

package net.lax1dude.eaglercraft.v1_8.webview;

import java.net.URI;
import java.net.URISyntaxException;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.PauseMenuCustomizeState;
import net.lax1dude.eaglercraft.v1_8.internal.EnumWebViewContentMode;
import net.lax1dude.eaglercraft.v1_8.internal.WebViewOptions;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketDisplayWebViewURLV5EAG;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

public class GuiScreenServerInfo extends GuiScreen {

	private static final Logger logger = LogManager.getLogger("GuiScreenServerInfo");

	private final GuiScreen parent;
	private final WebViewOptions opts;
	private boolean isShowing = false;

	public GuiScreenServerInfo(GuiScreen parent, WebViewOptions opts) {
		this.parent = parent;
		this.opts = opts;
	}

	public static GuiScreen createForCurrentState(GuiScreen parent, String url) {
		URI urlObj;
		try {
			urlObj = new URI(url);
		}catch(URISyntaxException ex) {
			logger.error("Refusing to iframe an invalid URL: {}", url);
			logger.error(ex);
			return new GuiScreenGenericErrorMessage("webviewInvalidURL.title", "webviewInvalidURL.desc", parent);
		}
		return createForCurrentState(parent, urlObj);
	}

	public static GuiScreen createForCurrentState(GuiScreen parent, URI url) {
		boolean support = WebViewOverlayController.supported();
		boolean fallbackSupport = WebViewOverlayController.fallbackSupported();
		if(!support && !fallbackSupport) {
			return new GuiScreenGenericErrorMessage("webviewNotSupported.title", "webviewNotSupported.desc", parent);
		}
		WebViewOptions opts = new WebViewOptions();
		opts.contentMode = EnumWebViewContentMode.URL_BASED;
		opts.url = url;
		setupState(opts);
		opts.permissionsOriginUUID = WebViewOptions.getURLOriginUUID(url);
		return support ? new GuiScreenServerInfo(parent, opts) : new GuiScreenServerInfoDesktop(parent, opts);
	}

	public static GuiScreen createForCurrentState(GuiScreen parent, byte[] blob, EaglercraftUUID permissionsOriginUUID) {
		boolean support = WebViewOverlayController.supported();
		boolean fallbackSupport = WebViewOverlayController.fallbackSupported();
		if(!support && !fallbackSupport) {
			return new GuiScreenGenericErrorMessage("webviewNotSupported.title", "webviewNotSupported.desc", parent);
		}
		WebViewOptions opts = new WebViewOptions();
		opts.contentMode = EnumWebViewContentMode.BLOB_BASED;
		opts.blob = blob;
		setupState(opts);
		opts.permissionsOriginUUID = permissionsOriginUUID;
		return support ? new GuiScreenServerInfo(parent, opts) : new GuiScreenServerInfoDesktop(parent, opts);
	}

	public static void setupState(WebViewOptions opts) {
		opts.scriptEnabled = (PauseMenuCustomizeState.serverInfoEmbedPerms & PauseMenuCustomizeState.SERVER_INFO_EMBED_PERMS_JAVASCRIPT) != 0;
		opts.strictCSPEnable = (PauseMenuCustomizeState.serverInfoEmbedPerms & PauseMenuCustomizeState.SERVER_INFO_EMBED_PERMS_STRICT_CSP) != 0;
		opts.serverMessageAPIEnabled = (PauseMenuCustomizeState.serverInfoEmbedPerms & PauseMenuCustomizeState.SERVER_INFO_EMBED_PERMS_MESSAGE_API) != 0;
		opts.fallbackTitle = PauseMenuCustomizeState.serverInfoEmbedTitle;
	}

	public static GuiScreen createForDisplayRequest(GuiScreen parent, int perms, String title, String url) {
		URI urlObj;
		try {
			urlObj = new URI(url);
		}catch(URISyntaxException ex) {
			logger.error("Refusing to iframe an invalid URL: {}", url);
			logger.error(ex);
			return new GuiScreenGenericErrorMessage("webviewInvalidURL.title", "webviewInvalidURL.desc", parent);
		}
		return createForDisplayRequest(parent, perms, title, urlObj);
	}

	public static GuiScreen createForDisplayRequest(GuiScreen parent, int perms, String title, URI url) {
		boolean support = WebViewOverlayController.supported();
		boolean fallbackSupport = WebViewOverlayController.fallbackSupported();
		if(!support && !fallbackSupport) {
			return new GuiScreenGenericErrorMessage("webviewNotSupported.title", "webviewNotSupported.desc", parent);
		}
		WebViewOptions opts = new WebViewOptions();
		opts.contentMode = EnumWebViewContentMode.URL_BASED;
		opts.url = url;
		setupState(opts, perms, title);
		opts.permissionsOriginUUID = WebViewOptions.getURLOriginUUID(url);
		return support ? new GuiScreenServerInfo(parent, opts) : new GuiScreenServerInfoDesktop(parent, opts);
	}

	public static GuiScreen createForDisplayRequest(GuiScreen parent, int perms, String title, byte[] blob,
			EaglercraftUUID permissionsOriginUUID) {
		boolean support = WebViewOverlayController.supported();
		boolean fallbackSupport = WebViewOverlayController.fallbackSupported();
		if(!support && !fallbackSupport) {
			return new GuiScreenGenericErrorMessage("webviewNotSupported.title", "webviewNotSupported.desc", parent);
		}
		WebViewOptions opts = new WebViewOptions();
		opts.contentMode = EnumWebViewContentMode.BLOB_BASED;
		opts.blob = blob;
		setupState(opts, perms, title);
		opts.permissionsOriginUUID = permissionsOriginUUID;
		return support ? new GuiScreenServerInfo(parent, opts) : new GuiScreenServerInfoDesktop(parent, opts);
	}

	public static void setupState(WebViewOptions opts, int perms, String title) {
		opts.scriptEnabled = (perms & SPacketDisplayWebViewURLV5EAG.FLAG_PERMS_JAVASCRIPT) != 0;
		opts.strictCSPEnable = (perms & SPacketDisplayWebViewURLV5EAG.FLAG_PERMS_STRICT_CSP) != 0;
		opts.serverMessageAPIEnabled = (perms & SPacketDisplayWebViewURLV5EAG.FLAG_PERMS_MESSAGE_API) != 0;
		opts.fallbackTitle = title;
	}

	public void initGui() {
		ScaledResolution res = mc.scaledResolution;
		if(!isShowing) {
			isShowing = true;
			WebViewOverlayController.beginShowingSmart(opts, res, 30, 30, width - 60, height - 60);
		}else {
			WebViewOverlayController.resizeSmart(res, 30, 30, width - 60, height - 60);
		}
		buttonList.clear();
		buttonList.add(new GuiButton(0, (width - 200) / 2, height - 25, I18n.format("gui.done")));
	}

	public void onGuiClosed() {
		if(isShowing) {
			isShowing = false;
			WebViewOverlayController.endShowing();
		}
	}

	public void actionPerformed(GuiButton btn) {
		if(btn.id == 0) {
			mc.displayGuiScreen(parent);
		}
	}

	public void drawScreen(int mx, int my, float pt) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, opts.fallbackTitle == null ? "Server Info" : opts.fallbackTitle, width / 2, 13, 0xFFFFFF);
		super.drawScreen(mx, my, pt);
	}

	protected boolean isPartOfPauseMenu() {
		return true;
	}
}
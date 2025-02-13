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

import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebView;
import net.lax1dude.eaglercraft.v1_8.internal.WebViewOptions;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketWebViewMessageV4EAG;
import net.minecraft.client.gui.ScaledResolution;

public class WebViewOverlayController {

	public static boolean supported() {
		return PlatformWebView.supported();
	}

	public static boolean isShowing() {
		return PlatformWebView.isShowing();
	}

	public static void beginShowing(WebViewOptions options, int x, int y, int w, int h) {
		PlatformWebView.beginShowing(options, x, y, w, h);
	}

	public static void resize(int x, int y, int w, int h) {
		PlatformWebView.resize(x, y, w, h);
	}

	public static void beginShowingSmart(WebViewOptions options, ScaledResolution res, int x, int y, int w, int h) {
		int fac = res.getScaleFactor();
		PlatformWebView.beginShowing(options, x * fac, y * fac, w * fac, h * fac);
	}

	public static void resizeSmart(ScaledResolution res, int x, int y, int w, int h) {
		int fac = res.getScaleFactor();
		PlatformWebView.resize(x * fac, y * fac, w * fac, h * fac);
	}

	public static void endShowing() {
		PlatformWebView.endShowing();
	}

	public static boolean fallbackSupported() {
		return PlatformWebView.fallbackSupported();
	}

	public static void launchFallback(WebViewOptions options) {
		PlatformWebView.launchFallback(options);
	}

	public static boolean fallbackRunning() {
		return PlatformWebView.fallbackRunning();
	}

	public static String getFallbackURL() {
		return PlatformWebView.getFallbackURL();
	}

	public static void endFallbackServer() {
		PlatformWebView.endFallbackServer();
	}

	public static void handleMessagePacket(SPacketWebViewMessageV4EAG packet) {
		PlatformWebView.handleMessageFromServer(packet);
	}

	public static interface IPacketSendCallback {
		boolean sendPacket(GameMessagePacket packet);
	}

	public static void setPacketSendCallback(IPacketSendCallback callback) {
		PlatformWebView.setPacketSendCallback(callback);
	}

	public static void runTick() {
		PlatformWebView.runTick();
	}

}
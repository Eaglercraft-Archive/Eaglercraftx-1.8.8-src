package net.lax1dude.eaglercraft.v1_8.profile;

import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.v1_8.socket.ConnectionHandshake;
import net.lax1dude.eaglercraft.v1_8.socket.HandshakePacketTypes;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class GuiAuthenticationScreen extends GuiScreen {

	private final GuiConnecting retAfterAuthScreen;
	private final GuiScreen parent;
	private GuiButton continueButton;
	private final String message;

	private GuiPasswordTextField password;
	private int authTypeForWarning = Integer.MAX_VALUE;
	private boolean allowPlaintext = false;

	public GuiAuthenticationScreen(GuiConnecting retAfterAuthScreen, GuiScreen parent, String message) {
		this.retAfterAuthScreen = retAfterAuthScreen;
		this.parent = parent;
		String authRequired = HandshakePacketTypes.AUTHENTICATION_REQUIRED;
		if(message.startsWith(authRequired)) {
			message = message.substring(authRequired.length()).trim();
		}
		if(message.length() > 0 && message.charAt(0) == '[') {
			int idx = message.indexOf(']', 1);
			if(idx != -1) {
				String authType = message.substring(1, idx);
				int type = Integer.MAX_VALUE;
				try {
					type = Integer.parseInt(authType);
				}catch(NumberFormatException ex) {
				}
				if(type != Integer.MAX_VALUE) {
					authTypeForWarning = type;
					message = message.substring(idx + 1).trim();
				}
			}
		}
		this.message = message;
	}

	public void initGui() {
		if(authTypeForWarning != Integer.MAX_VALUE) {
			GuiScreen scr = ConnectionHandshake.displayAuthProtocolConfirm(authTypeForWarning, parent, this);
			authTypeForWarning = Integer.MAX_VALUE;
			if(scr != null) {
				mc.displayGuiScreen(scr);
				allowPlaintext = true;
				return;
			}
		}
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(continueButton = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 80 + 12,
				I18n.format("auth.continue", new Object[0])));
		continueButton.enabled = false;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 80 + 37,
				I18n.format("gui.cancel", new Object[0])));
		this.password = new GuiPasswordTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 40, 200, 20); // 116
		this.password.setFocused(true);
		this.password.setCanLoseFocus(false);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiConnecting(retAfterAuthScreen, password.getText()));
		}else {
			this.mc.displayGuiScreen(parent);
			if (!PlatformNetworking.playConnectionState().isClosed()) {
				PlatformNetworking.playDisconnect();
			}
		}
	}

	public void drawScreen(int i, int j, float var3) {
		drawBackground(0);
		this.password.drawTextBox();
		this.drawCenteredString(this.fontRendererObj, I18n.format("auth.required", new Object[0]), this.width / 2,
				this.height / 4 - 5, 16777215);
		this.drawCenteredString(this.fontRendererObj, message, this.width / 2, this.height / 4 + 15, 0xAAAAAA);
		super.drawScreen(i, j, var3);
	}

	protected void keyTyped(char parChar1, int parInt1) {
		String pass = password.getText();
		if(parInt1 == KeyboardConstants.KEY_RETURN && pass.length() > 0) {
			this.mc.displayGuiScreen(new GuiConnecting(retAfterAuthScreen, pass, allowPlaintext));
		}else {
			this.password.textboxKeyTyped(parChar1, parInt1);
			this.continueButton.enabled = password.getText().length() > 0;
		}
	}

	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		this.password.mouseClicked(parInt1, parInt2, parInt3);
	}

}

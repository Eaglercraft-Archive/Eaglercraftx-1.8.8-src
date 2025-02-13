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

package net.lax1dude.eaglercraft.v1_8.cookie;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore.ServerCookie;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiScreenInspectSessionToken extends GuiScreen {

	private final GuiScreen parent;
	private final ServerCookie cookie;

	public GuiScreenInspectSessionToken(GuiScreenRevokeSessionToken parent, ServerCookie cookie) {
		this.parent = parent;
		this.cookie = cookie;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 6 + 106, I18n.format("gui.done")));
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		String[][] toDraw = new String[][] {
			{
				I18n.format("inspectSessionToken.details.server"),
				I18n.format("inspectSessionToken.details.expires"),
				I18n.format("inspectSessionToken.details.length")
			},
			{
				cookie.server.length() > 32 ? cookie.server.substring(0, 30) + "..." : cookie.server,
				(new SimpleDateFormat("M/d/yyyy h:mm aa")).format(new Date(cookie.expires)),
				Integer.toString(cookie.cookie.length)
			}
		};
		int[] maxWidth = new int[2];
		for(int i = 0; i < 2; ++i) {
			String[] strs = toDraw[i];
			int w = 0;
			for(int j = 0; j < strs.length; ++j) {
				int k = fontRendererObj.getStringWidth(strs[j]);
				if(k > w) {
					w = k;
				}
			}
			maxWidth[i] = w + 10;
		}
		int totalWidth = maxWidth[0] + maxWidth[1];
		this.drawCenteredString(fontRendererObj, I18n.format("inspectSessionToken.title"), this.width / 2, 70, 16777215);
		this.drawString(fontRendererObj, toDraw[0][0], (this.width - totalWidth) / 2, 90, 11184810);
		this.drawString(fontRendererObj, toDraw[0][1], (this.width - totalWidth) / 2, 104, 11184810);
		this.drawString(fontRendererObj, toDraw[0][2], (this.width - totalWidth) / 2, 118, 11184810);
		this.drawString(fontRendererObj, toDraw[1][0], (this.width - totalWidth) / 2 + maxWidth[0], 90, 11184810);
		this.drawString(fontRendererObj, toDraw[1][1], (this.width - totalWidth) / 2 + maxWidth[0], 104, 11184810);
		this.drawString(fontRendererObj, toDraw[1][2], (this.width - totalWidth) / 2 + maxWidth[0], 118, 11184810);
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(parent);
		}
	}

}
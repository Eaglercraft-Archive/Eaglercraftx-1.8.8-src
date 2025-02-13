/*
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiScreenRelay extends GuiScreen implements GuiYesNoCallback {

	private final GuiScreen screen;
	private GuiSlotRelay slots;
	private boolean hasPinged;
	private boolean addingNew = false;
	private boolean deleting = false;
	int selected;

	private GuiButton deleteRelay;
	private GuiButton setPrimary;

	private String tooltipString = null;

	private long lastRefresh = 0l;

	public GuiScreenRelay(GuiScreen screen) {
		this.screen = screen;
	}

	public void initGui() {
		selected = -1;
		buttonList.clear();
		buttonList.add(new GuiButton(0, this.width / 2 + 54, this.height - 28, 100, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("networkSettings.add")));
		buttonList.add(deleteRelay = new GuiButton(2, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("networkSettings.delete")));
		buttonList.add(setPrimary = new GuiButton(3, this.width / 2 + 54, this.height - 52, 100, 20, I18n.format("networkSettings.default")));
		buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, I18n.format("networkSettings.refresh")));
		buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 28, 100, 20, I18n.format("networkSettings.loadDefaults")));
		buttonList.add(new GuiButton(6, this.width - 100, 0, 100, 20, I18n.format("networkSettings.downloadRelay")));
		updateButtons();
		this.slots = new GuiSlotRelay(this);
		if(!hasPinged) {
			hasPinged = true;
			slots.relayManager.ping();
		}
	}

	void updateButtons() {
		if(selected < 0) {
			deleteRelay.enabled = false;
			setPrimary.enabled = false;
		}else {
			deleteRelay.enabled = true;
			setPrimary.enabled = true;
		}
	}

	public void actionPerformed(GuiButton btn) {
		if(btn.id == 0) {
			RelayManager.relayManager.save();
			mc.displayGuiScreen(screen);
		} else if(btn.id == 1) {
			addingNew = true;
			mc.displayGuiScreen(new GuiScreenAddRelay(this));
		} else if(btn.id == 2) {
			if(selected >= 0) {
				RelayServer srv = RelayManager.relayManager.get(selected);
				mc.displayGuiScreen(new GuiYesNo(this, I18n.format("networkSettings.delete"), I18n.format("addRelay.removeText1") +
						EnumChatFormatting.GRAY + " '" + srv.comment + "' (" + srv.address + ")", selected));
				deleting = true;
			}
		} else if(btn.id == 3) {
			if(selected >= 0) {
				slots.relayManager.setPrimary(selected);
				selected = 0;
			}
		} else if(btn.id == 4) {
			long millis = EagRuntime.steadyTimeMillis();
			if(millis - lastRefresh > 700l) {
				lastRefresh = millis;
				slots.relayManager.ping();
			}
			lastRefresh += 60l;
		} else if(btn.id == 5) {
			slots.relayManager.loadDefaults();
			long millis = EagRuntime.steadyTimeMillis();
			if(millis - lastRefresh > 700l) {
				lastRefresh = millis;
				slots.relayManager.ping();
			}
			lastRefresh += 60l;
		} else if(btn.id == 6) {
			EagRuntime.downloadFileWithName("EaglerSPRelay.zip", EagRuntime.getRequiredResourceBytes("relay_download.zip"));
		}
	}

	public void updateScreen() {
		slots.relayManager.update();
	}

	private int mx = 0;
	private int my = 0;

	int getFrameMouseX() {
		return mx;
	}

	int getFrameMouseY() {
		return my;
	}

	public void drawScreen(int par1, int par2, float par3) {
		mx = par1;
		my = par2;
		slots.drawScreen(par1, par2, par3);

		if(tooltipString != null) {
			int ww = mc.fontRendererObj.getStringWidth(tooltipString);
			Gui.drawRect(par1 + 1, par2 - 14, par1 + ww + 7, par2 - 2, 0xC0000000);
			screen.drawString(mc.fontRendererObj, tooltipString, par1 + 4, par2 - 12, 0xFF999999);
			tooltipString = null;
		}

		this.drawCenteredString(fontRendererObj, I18n.format("networkSettings.title"), this.width / 2, 16, 16777215);

		String str = I18n.format("networkSettings.relayTimeout") + " " + mc.gameSettings.relayTimeout;
		int w = fontRendererObj.getStringWidth(str);
		this.drawString(fontRendererObj, str, 3, 3, 0xDDDDDD);

		GlStateManager.pushMatrix();
		GlStateManager.translate(w + 7, 4, 0.0f);
		GlStateManager.scale(0.75f, 0.75f, 0.75f);
		str = EnumChatFormatting.UNDERLINE + I18n.format("networkSettings.relayTimeoutChange");
		int w2 = fontRendererObj.getStringWidth(str);
		boolean b = par1 > w + 5 && par1 < w + 7 + w2 * 3 / 4 && par2 > 3 && par2 < 11;
		if(b) Mouse.showCursor(EnumCursorType.HAND);
		this.drawString(fontRendererObj, EnumChatFormatting.UNDERLINE + I18n.format("networkSettings.relayTimeoutChange"), 0, 0, b ? 0xCCCCCC : 0x999999);
		GlStateManager.popMatrix();

		super.drawScreen(par1, par2, par3);
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if(par3 == 0) {
			String str = I18n.format("networkSettings.relayTimeout") + " " + mc.gameSettings.relayTimeout;
			int w = fontRendererObj.getStringWidth(str);
			str = I18n.format("networkSettings.relayTimeoutChange");
			int w2 = fontRendererObj.getStringWidth(str);
			if(par1 > w + 5 && par1 < w + 7 + w2 * 3 / 4 && par2 > 3 && par2 < 11) {
				this.mc.displayGuiScreen(new GuiScreenChangeRelayTimeout(this));
				this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}
		}
	}

	void setToolTip(String str) {
		tooltipString = str;
	}

	String addNewName;
	String addNewAddr;
	boolean addNewPrimary;

	public void confirmClicked(boolean par1, int par2) {
		if(par1) {
			if(addingNew) {
				RelayManager.relayManager.addNew(addNewAddr, addNewName, addNewPrimary);
				addNewAddr = null;
				addNewName = null;
				addNewPrimary = false;
				selected = -1;
				updateButtons();
			}else if(deleting) {
				RelayManager.relayManager.remove(par2);
				selected = -1;
				updateButtons();
			}
		}
		addingNew = false;
		deleting = false;
		this.mc.displayGuiScreen(this);
	}

	static Minecraft getMinecraft(GuiScreenRelay screen) {
		return screen.mc;
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.slots.handleMouseInput();
	}

	@Override
	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		this.slots.handleTouchInput();
	}

}
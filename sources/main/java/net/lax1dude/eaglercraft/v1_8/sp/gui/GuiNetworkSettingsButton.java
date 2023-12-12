package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

/**
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
public class GuiNetworkSettingsButton extends Gui {

	private final GuiScreen screen;
	private final String text;
	private final Minecraft mc;

	public GuiNetworkSettingsButton(GuiScreen screen) {
		this.screen = screen;
		this.text = I18n.format("directConnect.lanWorldRelay");
		this.mc = Minecraft.getMinecraft();
	}

	public void drawScreen(int xx, int yy) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75f, 0.75f, 0.75f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		int w = mc.fontRendererObj.getStringWidth(text);
		boolean hover = xx > 1 && yy > 1 && xx < (w * 3 / 4) + 7 && yy < 12;
		if(hover) {
			Mouse.showCursor(EnumCursorType.HAND);
		}

		drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, 5, 5, hover ? 0xFFEEEE22 : 0xFFCCCCCC);

		GlStateManager.popMatrix();
	}

	public void mouseClicked(int xx, int yy, int btn) {
		int w = mc.fontRendererObj.getStringWidth(text);
		if(xx > 2 && yy > 2 && xx < (w * 3 / 4) + 5 && yy < 12) {
			if(LANServerController.supported()) {
				mc.displayGuiScreen(GuiScreenLANInfo.showLANInfoScreen(new GuiScreenRelay(screen)));
			}else {
				mc.displayGuiScreen(new GuiScreenLANNotSupported(screen));
			}
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
		}
	}

}
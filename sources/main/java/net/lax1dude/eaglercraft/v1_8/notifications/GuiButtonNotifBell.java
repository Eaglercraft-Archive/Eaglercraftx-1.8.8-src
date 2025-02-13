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

package net.lax1dude.eaglercraft.v1_8.notifications;

import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonNotifBell extends GuiButton {

	private static final ResourceLocation eaglerTextures = new ResourceLocation("eagler:gui/eagler_gui.png");

	private int unread = 0;

	public GuiButtonNotifBell(int buttonID, int xPos, int yPos) {
		super(buttonID, xPos, yPos, 20, 20, "");
	}

	public void setUnread(int num) {
		unread = num;
	}

	public void drawButton(Minecraft minecraft, int i, int j) {
		if (this.visible) {
			minecraft.getTextureManager().bindTexture(eaglerTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width
					&& j < this.yPosition + this.height;
			int k = 0;
			int c = 14737632;
			if (flag) {
				k += this.height;
				c = 16777120;
				Mouse.showCursor(EnumCursorType.HAND);
			}

			drawTexturedModalRect(xPosition, yPosition, unread > 0 ? 116 : 136, k, width, height);
			
			if(unread > 0) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(xPosition + 15.5f, yPosition + 11.0f, 0.0f);
				if(unread >= 10) {
					GlStateManager.translate(0.0f, 1.0f, 0.0f);
					GlStateManager.scale(0.5f, 0.5f, 0.5f);
				}else {
					GlStateManager.scale(0.75f, 0.75f, 0.75f);
				}
				drawCenteredString(minecraft.fontRendererObj, Integer.toString(unread), 0, 0, c);
				GlStateManager.popMatrix();
			}
		}
	}
}
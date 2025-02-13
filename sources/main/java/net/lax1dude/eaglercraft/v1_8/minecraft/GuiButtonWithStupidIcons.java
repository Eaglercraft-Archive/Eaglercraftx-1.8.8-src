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

package net.lax1dude.eaglercraft.v1_8.minecraft;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonWithStupidIcons extends GuiButton {

	protected ResourceLocation leftIcon;
	protected float leftIconAspect;
	protected ResourceLocation rightIcon;
	protected float rightIconAspect;

	public GuiButtonWithStupidIcons(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}

	public GuiButtonWithStupidIcons(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, buttonText);
	}

	public GuiButtonWithStupidIcons(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,
			ResourceLocation leftIcon, float leftIconAspect, ResourceLocation rightIcon, float rightIconAspect) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.leftIcon = leftIcon;
		this.leftIconAspect = leftIconAspect;
		this.rightIcon = rightIcon;
		this.rightIconAspect = rightIconAspect;
	}

	public GuiButtonWithStupidIcons(int buttonId, int x, int y, String buttonText, ResourceLocation leftIcon,
			float leftIconAspect, ResourceLocation rightIcon, float rightIconAspect) {
		super(buttonId, x, y, buttonText);
		this.leftIcon = leftIcon;
		this.leftIconAspect = leftIconAspect;
		this.rightIcon = rightIcon;
		this.rightIconAspect = rightIconAspect;
	}

	public ResourceLocation getLeftIcon() {
		return leftIcon;
	}

	public ResourceLocation getRightIcon() {
		return rightIcon;
	}

	public void setLeftIcon(ResourceLocation leftIcon, float aspectRatio) {
		this.leftIcon = leftIcon;
		this.leftIconAspect = aspectRatio;
	}

	public void setRightIcon(ResourceLocation rightIcon, float aspectRatio) {
		this.rightIcon = rightIcon;
		this.rightIconAspect = aspectRatio;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
					&& mouseY < this.yPosition + this.height;
			if (this.enabled && this.hovered) {
				Mouse.showCursor(EnumCursorType.HAND);
			}
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2,
					46 + i * 20, this.width / 2, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;
			if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}

			int strWidth = fontrenderer.getStringWidth(displayString);
			int strWidthAdj = strWidth - (leftIcon != null ? (int) (16 * leftIconAspect) : 0)
					+ (rightIcon != null ? (int) (16 * rightIconAspect) : 0);
			this.drawString(fontrenderer, this.displayString, this.xPosition + (this.width - strWidthAdj) / 2,
					this.yPosition + (this.height - 8) / 2, j);
			if(leftIcon != null) {
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				mc.getTextureManager().bindTexture(leftIcon);
				GlStateManager.pushMatrix();
				GlStateManager.translate(this.xPosition + (this.width - strWidthAdj) / 2 - 3 - 16 * leftIconAspect, this.yPosition + 2, 0.0f);
				float f = 16.0f / 256.0f;
				GlStateManager.scale(f * leftIconAspect, f, f);
				this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
				GlStateManager.popMatrix();
			}
			if(rightIcon != null) {
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				mc.getTextureManager().bindTexture(rightIcon);
				GlStateManager.pushMatrix();
				GlStateManager.translate(this.xPosition + (this.width - strWidthAdj) / 2 + strWidth + 3, this.yPosition + 2, 0.0f);
				float f = 16.0f / 256.0f;
				GlStateManager.scale(f * rightIconAspect, f, f);
				this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
				GlStateManager.popMatrix();
			}
		}
	}

}
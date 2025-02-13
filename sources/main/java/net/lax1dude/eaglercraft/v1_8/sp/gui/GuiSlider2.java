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

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiSlider2 extends GuiButton {
	/** The value of this slider control. */
	public float sliderValue = 1.0F;
	public float sliderMax = 1.0F;

	/** Is this slider control being dragged. */
	public boolean dragging = false;

	public GuiSlider2(int buttonId, int x, int y, int widthIn, int heightIn, float sliderValue, float sliderMax) {
		super(buttonId, x, y, widthIn, heightIn, null);
		this.sliderValue = sliderValue;
		this.sliderMax = sliderMax;
		this.displayString = updateDisplayString();
	}

	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this
	 * button and 2 if it IS hovering over this button.
	 */
	protected int getHoverState(boolean par1) {
		return 0;
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
		if (this.visible) {
			if (this.dragging) {
				float oldValue = sliderValue;
				this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

				if (this.sliderValue < 0.0F) {
					this.sliderValue = 0.0F;
				}

				if (this.sliderValue > 1.0F) {
					this.sliderValue = 1.0F;
				}

				if(oldValue != sliderValue) {
					onChange();
				}

				this.displayString = updateDisplayString();
			}

			if(this.enabled) {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
				this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
			}
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			float oldValue = sliderValue;
			this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

			if (this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}

			if (this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}

			if(oldValue != sliderValue) {
				onChange();
			}

			this.displayString = updateDisplayString();
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int par1, int par2) {
		this.dragging = false;
	}

	protected String updateDisplayString() {
		return (int)(this.sliderValue * this.sliderMax * 100.0F) + "%";
	}

	protected void onChange() {
		
	}

	public boolean isSliderTouchEvents() {
		return true;
	}

}
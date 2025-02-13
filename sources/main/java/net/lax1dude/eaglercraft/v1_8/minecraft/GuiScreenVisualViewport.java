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

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenVisualViewport extends GuiScreen {

	protected int offsetX;
	protected int offsetY;

	@Override
	public final void setWorldAndResolution(Minecraft mc, int width, int height) {
		Display.wasVisualViewportResized(); // clear state
		offsetX = Display.getVisualViewportX() * width / mc.displayWidth;
		offsetY = Display.getVisualViewportY() * height / mc.displayHeight;
		setWorldAndResolution0(mc, Display.getVisualViewportW() * width / mc.displayWidth,
				Display.getVisualViewportH() * height / mc.displayHeight);
	}

	protected void setWorldAndResolution0(Minecraft mc, int width, int height) {
		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	public final void updateScreen() {
		if(Display.wasVisualViewportResized()) {
			setWorldAndResolution(mc, mc.scaledResolution.getScaledWidth(), mc.scaledResolution.getScaledHeight());
		}
		updateScreen0();
	}

	protected void updateScreen0() {
		super.updateScreen();
	}

	@Override
	public final void drawScreen(int i, int j, float var3) {
		i -= offsetX;
		j -= offsetY;
		GlStateManager.pushMatrix();
		GlStateManager.translate(offsetX, offsetY, 0.0f);
		drawScreen0(i, j, var3);
		GlStateManager.popMatrix();
	}

	protected void drawScreen0(int i, int j, float var3) {
		super.drawScreen(i, j, var3);
	}

	@Override
	protected final void mouseClicked(int parInt1, int parInt2, int parInt3) {
		parInt1 -= offsetX;
		parInt2 -= offsetY;
		mouseClicked0(parInt1, parInt2, parInt3);
	}

	protected void mouseClicked0(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
	}

	@Override
	protected final void mouseReleased(int i, int j, int k) {
		i -= offsetX;
		j -= offsetY;
		mouseReleased0(i, j, k);
	}

	protected void mouseReleased0(int i, int j, int k) {
		super.mouseReleased(i, j, k);
	}

	@Override
	protected final void mouseClickMove(int var1, int var2, int var3, long var4) {
		var1 -= offsetX;
		var2 -= offsetY;
		mouseClickMove0(var1, var2, var3, var4);
	}

	protected void mouseClickMove0(int var1, int var2, int var3, long var4) {
		super.mouseClickMove(var1, var2, var3, var4);
	}

	@Override
	protected final void touchEndMove(int parInt1, int parInt2, int parInt3) {
		parInt1 -= offsetX;
		parInt2 -= offsetY;
		touchEndMove0(parInt1, parInt2, parInt3);
	}

	protected void touchEndMove0(int parInt1, int parInt2, int parInt3) {
		super.touchEndMove(parInt1, parInt2, parInt3);
	}

	@Override
	protected final void touchMoved(int parInt1, int parInt2, int parInt3) {
		parInt1 -= offsetX;
		parInt2 -= offsetY;
		touchMoved0(parInt1, parInt2, parInt3);
	}

	protected void touchMoved0(int parInt1, int parInt2, int parInt3) {
		super.touchMoved(parInt1, parInt2, parInt3);
	}

	@Override
	protected final void touchStarted(int parInt1, int parInt2, int parInt3) {
		parInt1 -= offsetX;
		parInt2 -= offsetY;
		touchStarted0(parInt1, parInt2, parInt3);
	}

	protected void touchStarted0(int parInt1, int parInt2, int parInt3) {
		super.touchStarted(parInt1, parInt2, parInt3);
	}

	@Override
	protected void touchTapped(int parInt1, int parInt2, int parInt3) {
		parInt1 -= offsetX;
		parInt2 -= offsetY;
		touchTapped0(parInt1, parInt2, parInt3);
	}

	protected void touchTapped0(int parInt1, int parInt2, int parInt3) {
		super.touchTapped(parInt1, parInt2, parInt3);
	}

}
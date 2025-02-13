/*
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiShadersNotSupported extends GuiScreen {

	private GuiScreen parent;
	private String reason;

	public GuiShadersNotSupported(GuiScreen parent, String reason) {
		this.parent = parent;
		this.reason = reason;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 10, I18n.format("gui.back")));
	}

	public void drawScreen(int i, int j, float var3) {
		this.drawBackground(0);
		drawCenteredString(fontRendererObj, I18n.format("shaders.gui.unsupported.title"), width / 2, height / 2 - 30, 0xFFFFFF);
		drawCenteredString(fontRendererObj, reason, width / 2, height / 2 - 10, 11184810);
		super.drawScreen(i, j, var3);
	}

	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 0) {
			mc.displayGuiScreen(parent);
		}
	}

}
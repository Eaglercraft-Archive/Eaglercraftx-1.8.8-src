package net.lax1dude.eaglercraft.v1_8.recording;

import net.minecraft.client.gui.GuiSlot;

/**
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
public class GuiSlotSelectCodec extends GuiSlot {

	protected final GuiScreenSelectCodec screen;

	public GuiSlotSelectCodec(GuiScreenSelectCodec screen, int topIn, int bottomIn) {
		super(GuiScreenSelectCodec.getMC(screen), screen.width, screen.height, topIn, bottomIn, 18);
		this.screen = screen;
	}

	@Override
	protected int getSize() {
		return screen.codecs.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		if(var1 < screen.codecs.size()) {
			screen.selectedCodec = var1;
		}
	}

	@Override
	protected boolean isSelected(int var1) {
		return screen.selectedCodec == var1;
	}

	@Override
	protected void drawBackground() {
		screen.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int id, int xx, int yy, int width, int height, int ii) {
		if(id < screen.codecs.size()) {
			this.screen.drawString(mc.fontRendererObj, screen.codecs.get(id).name, xx + 4, yy + 3, 0xFFFFFF);
		}
	}

}

package net.lax1dude.eaglercraft.v1_8.recording;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

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
public class GuiScreenSelectCodec extends GuiScreen {

	protected final GuiScreenRecordingSettings parent;
	protected List<EnumScreenRecordingCodec> codecs;
	protected int selectedCodec;
	protected GuiSlotSelectCodec slots;
	protected GuiButton showAllButton;
	protected boolean showAll;
	protected EnumScreenRecordingCodec codec;

	public GuiScreenSelectCodec(GuiScreenRecordingSettings parent, EnumScreenRecordingCodec codec) {
		this.parent = parent;
		this.codec = codec;
	}

	public void initGui() {
		showAll = codec.advanced;
		codecs = showAll ? ScreenRecordingController.advancedCodecsOrdered : ScreenRecordingController.simpleCodecsOrdered;
		selectedCodec = codecs.indexOf(codec);
		buttonList.clear();
		buttonList.add(showAllButton = new GuiButton(0, this.width / 2 - 154, this.height - 38, 150, 20,
				I18n.format("options.recordingCodec.showAdvancedCodecs", I18n.format(showAll ? "gui.yes" : "gui.no"))));
		buttonList.add(new GuiButton(1, this.width / 2 + 4, this.height - 38, 150, 20, I18n.format("gui.done")));
		slots = new GuiSlotSelectCodec(this, 32, height - 45);
	}

	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 0) {
			changeStateShowAll(!showAll);
			showAllButton.displayString = I18n.format("options.recordingCodec.showAdvancedCodecs", I18n.format(showAll ? "gui.yes" : "gui.no"));
		}else if(parGuiButton.id == 1) {
			if(selectedCodec >= 0 && selectedCodec < codecs.size()) {
				parent.handleCodecCallback(codecs.get(selectedCodec));
			}
			mc.displayGuiScreen(parent);
		}
	}

	protected void changeStateShowAll(boolean newShowAll) {
		if(newShowAll == showAll) return;
		EnumScreenRecordingCodec oldCodec = codecs.get(selectedCodec >= 0 && selectedCodec < codecs.size() ? selectedCodec : 0);
		codecs = newShowAll ? ScreenRecordingController.advancedCodecsOrdered : ScreenRecordingController.simpleCodecsOrdered;
		showAll = newShowAll;
		selectedCodec = codecs.indexOf(oldCodec);
	}

	public void drawScreen(int i, int j, float var3) {
		slots.drawScreen(i, j, var3);
		this.drawCenteredString(this.fontRendererObj, I18n.format("options.recordingCodec.title"), this.width / 2, 16, 16777215);
		super.drawScreen(i, j, var3);
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		slots.handleMouseInput();
	}

	public void handleTouchInput() throws IOException {
		super.handleTouchInput();
		slots.handleTouchInput();
	}

	static Minecraft getMC(GuiScreenSelectCodec screen) {
		return screen.mc;
	}

}

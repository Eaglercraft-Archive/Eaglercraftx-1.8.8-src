package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.WorkerStartupFailedException;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket15Crashed;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.resources.I18n;

/**
 * Copyright (c) 2023-2024 lax1dude. All Rights Reserved.
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
public class GuiScreenIntegratedServerStartup extends GuiScreen {

	private final GuiScreen backScreen;
	private static final String[] dotDotDot = new String[] { "", ".", "..", "..." };

	private int counter = 0;

	public GuiScreenIntegratedServerStartup(GuiScreen backScreen) {
		this.backScreen = backScreen;
	}

	protected void keyTyped(char parChar1, int parInt1) {
	}

	public void initGui() {
		this.buttonList.clear();
	}

	public void updateScreen() {
		++counter;
		if(counter == 2) {
			try {
				SingleplayerServerController.startIntegratedServerWorker();
			}catch(WorkerStartupFailedException ex) {
				mc.displayGuiScreen(new GuiScreenIntegratedServerFailed(ex.getMessage(), new GuiMainMenu()));
				return;
			}
		}else if(counter > 2) {
			IPCPacket15Crashed[] crashReport = SingleplayerServerController.worldStatusErrors();
			if(crashReport != null) {
				mc.displayGuiScreen(GuiScreenIntegratedServerBusy.createException(new GuiMainMenu(), "singleplayer.failed.notStarted", crashReport));
			}else if(SingleplayerServerController.isIntegratedServerWorkerStarted()) {
				GuiScreen cont = new GuiSelectWorld(backScreen);
				if(SingleplayerServerController.isRunningSingleThreadMode()) {
					cont = new GuiScreenIntegratedServerFailed("singleplayer.failed.singleThreadWarning.1", "singleplayer.failed.singleThreadWarning.2", cont);
				}
				mc.displayGuiScreen(cont);
			}
		}
	}

	public void drawScreen(int i, int j, float f) {
		this.drawBackground(0);
		String txt = I18n.format("singleplayer.integratedStartup");
		int w = this.fontRendererObj.getStringWidth(txt);
		this.drawString(this.fontRendererObj, txt + dotDotDot[(int)((System.currentTimeMillis() / 300L) % 4L)], (this.width - w) / 2, this.height / 2 - 50, 16777215);
		super.drawScreen(i, j, f);
	}

}

package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.WorkerStartupFailedException;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket15Crashed;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket1CIssueDetected;
import net.minecraft.client.gui.GuiButton;
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
public class GuiScreenDemoIntegratedServerStartup extends GuiScreen {

	private final GuiScreen contScreen;
	private final boolean singleThread;
	private static final String[] dotDotDot = new String[] { "", ".", "..", "..." };

	private int counter = 0;

	private GuiButton cancelButton;

	public GuiScreenDemoIntegratedServerStartup(GuiScreen contScreen) {
		this.contScreen = contScreen;
		this.singleThread = false;
	}

	public GuiScreenDemoIntegratedServerStartup(GuiScreen contScreen, boolean singleThread) {
		this.contScreen = contScreen;
		this.singleThread = singleThread;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(cancelButton = new GuiButton(0, this.width / 2 - 100, this.height / 3 + 50, I18n.format("singleplayer.busy.killTask")));
		cancelButton.visible = false;
	}

	public void updateScreen() {
		++counter;
		if(counter == 2) {
			try {
				SingleplayerServerController.startIntegratedServerWorker(singleThread);
			}catch(WorkerStartupFailedException ex) {
				mc.displayGuiScreen(new GuiScreenIntegratedServerFailed(ex.getMessage(), new GuiScreenDemoIntegratedServerFailed()));
				return;
			}
		}else if(counter > 2) {
			if(counter > 100 && SingleplayerServerController.canKillWorker() && !singleThread) {
				cancelButton.visible = true;
			}
			IPCPacket15Crashed[] crashReport = SingleplayerServerController.worldStatusErrors();
			if(crashReport != null) {
				mc.displayGuiScreen(GuiScreenIntegratedServerBusy.createException(new GuiScreenDemoIntegratedServerFailed(), "singleplayer.failed.notStarted", crashReport));
			}else if(SingleplayerServerController.isIntegratedServerWorkerStarted()) {
				GuiScreen cont = contScreen;
				if(SingleplayerServerController.isRunningSingleThreadMode()) {
					cont = new GuiScreenIntegratedServerFailed("singleplayer.failed.singleThreadWarning.1", "singleplayer.failed.singleThreadWarning.2", cont);
				} else if (!EagRuntime.getConfiguration().isRamdiskMode()
						&& SingleplayerServerController.isIssueDetected(IPCPacket1CIssueDetected.ISSUE_RAMDISK_MODE)
						&& SingleplayerServerController.canKillWorker()) {
					cont = new GuiScreenRAMDiskModeDetected(cont);
				}
				mc.displayGuiScreen(cont);
			}
		}
	}

	protected void actionPerformed(GuiButton parGuiButton) {
		if(parGuiButton.id == 0) {
			SingleplayerServerController.killWorker();
			mc.displayGuiScreen(new GuiScreenDemoIntegratedServerStartup(contScreen, true));
		}
	}

	public void drawScreen(int i, int j, float f) {
		this.drawBackground(0);
		String txt = I18n.format("singleplayer.integratedStartup");
		int w = this.fontRendererObj.getStringWidth(txt);
		this.drawString(this.fontRendererObj, txt + dotDotDot[(int)((EagRuntime.steadyTimeMillis() / 300L) % 4L)], (this.width - w) / 2, this.height / 2 - 50, 16777215);
		super.drawScreen(i, j, f);
	}

	public boolean canCloseGui() {
		return false;
	}

}

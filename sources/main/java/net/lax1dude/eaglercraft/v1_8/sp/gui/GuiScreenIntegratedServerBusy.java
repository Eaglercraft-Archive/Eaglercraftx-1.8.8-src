package net.lax1dude.eaglercraft.v1_8.sp.gui;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket15Crashed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class GuiScreenIntegratedServerBusy extends GuiScreen {

	public final GuiScreen menu;
	private GuiButton killTask;
	public final String failMessage;
	private BooleanSupplier checkTaskComplete;
	private Runnable taskKill;
	private String lastStatus;
	private String currentStatus;
	private BiConsumer<GuiScreen, IPCPacket15Crashed[]> onException;
	private int areYouSure;
	
	private long startStartTime;
	
	private static final Runnable defaultTerminateAction = () -> {
		if(SingleplayerServerController.canKillWorker()) {
			SingleplayerServerController.killWorker();
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenIntegratedServerFailed("singleplayer.failed.killed", new GuiMainMenu()));
		}else {
			EagRuntime.showPopup("Cannot kill worker tasks on desktop runtime!");
		}
	};
	
	public static GuiScreen createException(GuiScreen ok, String msg, IPCPacket15Crashed[] exceptions) {
		ok = new GuiScreenIntegratedServerFailed(msg, ok);
		if(exceptions != null) {
			for(int i = exceptions.length - 1; i >= 0; --i) {
				ok = new GuiScreenIntegratedServerCrashed(ok, exceptions[i].crashReport);
			}
		}
		return ok;
	}
	
	private static final BiConsumer<GuiScreen, IPCPacket15Crashed[]> defaultExceptionAction = (t, u) -> {
		GuiScreenIntegratedServerBusy tt = (GuiScreenIntegratedServerBusy) t;
		Minecraft.getMinecraft().displayGuiScreen(createException(tt.menu, tt.failMessage, u));
	};
	
	public GuiScreenIntegratedServerBusy(GuiScreen menu, String progressMessage, String failMessage, BooleanSupplier checkTaskComplete) {
		this(menu, progressMessage, failMessage, checkTaskComplete, defaultExceptionAction, defaultTerminateAction);
	}
	
	public GuiScreenIntegratedServerBusy(GuiScreen menu, String progressMessage, String failMessage, BooleanSupplier checkTaskComplete, BiConsumer<GuiScreen, IPCPacket15Crashed[]> exceptionAction) {
		this(menu, progressMessage, failMessage, checkTaskComplete, exceptionAction, defaultTerminateAction);
	}
	
	public GuiScreenIntegratedServerBusy(GuiScreen menu, String progressMessage, String failMessage, BooleanSupplier checkTaskComplete, Runnable onTerminate) {
		this(menu, progressMessage, failMessage, checkTaskComplete, defaultExceptionAction, onTerminate);
	}
	
	public GuiScreenIntegratedServerBusy(GuiScreen menu, String progressMessage, String failMessage, BooleanSupplier checkTaskComplete, BiConsumer<GuiScreen, IPCPacket15Crashed[]> onException, Runnable onTerminate) {
		this.menu = menu;
		this.failMessage = failMessage;
		this.checkTaskComplete = checkTaskComplete;
		this.onException = onException;
		this.taskKill = onTerminate;
		this.lastStatus = SingleplayerServerController.worldStatusString();
		this.currentStatus = progressMessage;
	}
	
	public void initGui() {
		if(startStartTime == 0) this.startStartTime = System.currentTimeMillis();
		areYouSure = 0;
		this.buttonList.add(killTask = new GuiButton(0, this.width / 2 - 100, this.height / 3 + 50, I18n.format("singleplayer.busy.killTask")));
		killTask.enabled = false;
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		int top = this.height / 3;
		
		long millis = System.currentTimeMillis();
		
		String str = I18n.format(currentStatus);
		
		long dots = (millis / 500l) % 4l;
		this.drawString(fontRendererObj, str + (dots > 0 ? "." : "") + (dots > 1 ? "." : "") + (dots > 2 ? "." : ""), (this.width - this.fontRendererObj.getStringWidth(str)) / 2, top + 10, 0xFFFFFF);
		
		if(areYouSure > 0) {
			this.drawCenteredString(fontRendererObj, I18n.format("singleplayer.busy.cancelWarning"), this.width / 2, top + 25, 0xFF8888);
		}else {
			float prog = SingleplayerServerController.worldStatusProgress();
			if(this.currentStatus.equals(this.lastStatus) && prog > 0.01f) {
				this.drawCenteredString(fontRendererObj, (prog > 1.0f ? ("(" + (prog > 1000000.0f ? "" + (int)(prog / 1000000.0f) + "MB" :
					(prog > 1000.0f ? "" + (int)(prog / 1000.0f) + "kB" : "" + (int)prog + "B")) + ")") : "" + (int)(prog * 100.0f) + "%"), this.width / 2, top + 25, 0xFFFFFF);
			}else {
				long elapsed = (millis - startStartTime) / 1000l;
				if(elapsed > 3) {
					this.drawCenteredString(fontRendererObj, "(" + elapsed + "s)", this.width / 2, top + 25, 0xFFFFFF);
				}
			}
		}
		
		super.drawScreen(par1, par2, par3);
	}
	
	public void updateScreen() {
		long millis = System.currentTimeMillis();
		if(millis - startStartTime > 6000l && SingleplayerServerController.canKillWorker()) {
			killTask.enabled = true;
		}
		if(SingleplayerServerController.didLastCallFail() || !SingleplayerServerController.isIntegratedServerWorkerAlive()) {
			onException.accept(this, SingleplayerServerController.worldStatusErrors());
			return;
		}
		if(checkTaskComplete.getAsBoolean()) {
			this.mc.displayGuiScreen(menu);
		}
		String str = SingleplayerServerController.worldStatusString();
		if(!lastStatus.equals(str)) {
			lastStatus = str;
			currentStatus = str;
		}
		killTask.displayString = I18n.format(areYouSure > 0 ? "singleplayer.busy.confirmCancel" : "singleplayer.busy.killTask");
		if(areYouSure > 0) {
			--areYouSure;
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			if(areYouSure <= 0) {
				areYouSure = 80;
			}else if(areYouSure <= 65) {
				taskKill.run();
			}
		}
	}

	public boolean shouldHangupIntegratedServer() {
		return false;
	}

}

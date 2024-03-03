package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket05RequestData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiRenameWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;

/**
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
public class GuiScreenBackupWorldSelection extends GuiScreen {

	private GuiScreen selectWorld;

	private GuiButton worldRecreate = null;
	private GuiButton worldDuplicate = null;
	private GuiButton worldExport = null;
	private GuiButton worldConvert = null;
	private GuiButton worldBackup = null;
	private long worldSeed;
	private NBTTagCompound levelDat;
	
	private String worldName;
	
	public GuiScreenBackupWorldSelection(GuiScreen selectWorld, String worldName, NBTTagCompound levelDat) {
		this.selectWorld = selectWorld;
		this.worldName = worldName;
		this.levelDat = levelDat;
		this.worldSeed = levelDat.getCompoundTag("Data").getLong("RandomSeed");
	}
	
	public void initGui() {
		this.buttonList.add(worldRecreate = new GuiButton(1, this.width / 2 - 100, this.height / 5 + 5, I18n.format("singleplayer.backup.recreate")));
		this.buttonList.add(worldDuplicate = new GuiButton(2, this.width / 2 - 100, this.height / 5 + 30, I18n.format("singleplayer.backup.duplicate")));
		this.buttonList.add(worldExport = new GuiButton(3, this.width / 2 - 100, this.height / 5 + 80, I18n.format("singleplayer.backup.export")));
		this.buttonList.add(worldConvert = new GuiButton(4, this.width / 2 - 100, this.height / 5 + 105, I18n.format("singleplayer.backup.vanilla")));
		this.buttonList.add(worldBackup = new GuiButton(5, this.width / 2 - 100, this.height / 5 + 136, I18n.format("singleplayer.backup.clearPlayerData")));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 155, I18n.format("gui.cancel")));
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();

		this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.backup.title", worldName), this.width / 2, this.height / 5 - 35, 16777215);
		this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.backup.seed") + " " + worldSeed, this.width / 2, this.height / 5 + 62, 0xAAAAFF);
		
		int toolTipColor = 0xDDDDAA;
		if(worldRecreate.isMouseOver()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.backup.recreate.tooltip"), this.width / 2, this.height / 5 - 12, toolTipColor);
		}else if(worldDuplicate.isMouseOver()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.backup.duplicate.tooltip"), this.width / 2, this.height / 5 - 12, toolTipColor);
		}else if(worldExport.isMouseOver()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.backup.export.tooltip"), this.width / 2, this.height / 5 - 12, toolTipColor);
		}else if(worldConvert.isMouseOver()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.backup.vanilla.tooltip"), this.width / 2, this.height / 5 - 12, toolTipColor);
		}else if(worldBackup.isMouseOver()) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("singleplayer.backup.clearPlayerData.tooltip"), this.width / 2, this.height / 5 - 12, toolTipColor);
		}
		
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(selectWorld);
		}else if(par1GuiButton.id == 1) {
			GuiCreateWorld cw = new GuiCreateWorld(selectWorld);
			cw.func_146318_a(new WorldInfo(this.levelDat.getCompoundTag("Data")));
			this.mc.displayGuiScreen(cw);
		}else if(par1GuiButton.id == 2) {
			this.mc.displayGuiScreen(new GuiRenameWorld(this.selectWorld, this.worldName, true));
		}else if(par1GuiButton.id == 3) {
			SingleplayerServerController.exportWorld(worldName, IPCPacket05RequestData.REQUEST_LEVEL_EAG);
			this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(selectWorld, "singleplayer.busy.exporting.1", "singleplayer.failed.exporting.1", () -> {
				byte[] b = SingleplayerServerController.getExportResponse();
				if(b != null) {
					EagRuntime.downloadFileWithName(worldName + ".epk", b);
					return true;
				}
				return false;
			}));
		}else if(par1GuiButton.id == 4) {
			SingleplayerServerController.exportWorld(worldName, IPCPacket05RequestData.REQUEST_LEVEL_MCA);
			this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(selectWorld, "singleplayer.busy.exporting.2", "singleplayer.failed.exporting.2", () -> {
				byte[] b = SingleplayerServerController.getExportResponse();
				if(b != null) {
					EagRuntime.downloadFileWithName(worldName + ".zip", b);
					return true;
				}
				return false;
			}));
		}else if(par1GuiButton.id == 5) {
			this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("singleplayer.backup.clearPlayerData.warning1"),
					I18n.format("singleplayer.backup.clearPlayerData.warning2", worldName, EaglerProfile.getName()), 0));
		}
	}
	
	public void confirmClicked(boolean par1, int par2) {
		if(par1) {
			SingleplayerServerController.clearPlayerData(worldName);
			this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(this, "singleplayer.busy.clearplayers", "singleplayer.failed.clearplayers", SingleplayerServerController::isReady));
		}else {
			mc.displayGuiScreen(this);
		}
	}

}

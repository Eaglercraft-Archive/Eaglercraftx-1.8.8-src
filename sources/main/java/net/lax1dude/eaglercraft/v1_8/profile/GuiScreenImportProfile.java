package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
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
public class GuiScreenImportProfile extends GuiScreen {

	private GuiScreen back;
	private ProfileImporter importer;

	private GuiButton importProfile;
	private boolean doImportProfile;
	private GuiButton importSettings;
	private boolean doImportSettings;
	private GuiButton importServers;
	private boolean doImportServers;
	private GuiButton importResourcePacks;
	private boolean doImportResourcePacks;

	public GuiScreenImportProfile(ProfileImporter importer, GuiScreen back) {
		this.back = back;
		this.importer = importer;
		this.doImportProfile = importer.hasProfile();
		this.doImportSettings = importer.hasSettings();
		this.doImportServers = importer.hasSettings();
		this.doImportResourcePacks = importer.hasResourcePacks();
	}

	public void initGui() {
		this.buttonList.add(importProfile = new GuiButton(2, this.width / 2 - 100, this.height / 4, I18n.format("settingsBackup.import.option.profile") + " " + I18n.format(doImportProfile ? "gui.yes" : "gui.no")));
		importProfile.enabled = importer.hasProfile();
		this.buttonList.add(importSettings = new GuiButton(3, this.width / 2 - 100, this.height / 4 + 25, I18n.format("settingsBackup.import.option.settings") + " " + I18n.format(doImportSettings ? "gui.yes" : "gui.no")));
		importSettings.enabled = importer.hasProfile();
		this.buttonList.add(importServers = new GuiButton(4, this.width / 2 - 100, this.height / 4 + 50, I18n.format("settingsBackup.import.option.servers") + " " + I18n.format(doImportServers ? "gui.yes" : "gui.no")));
		importServers.enabled = importer.hasServers();
		this.buttonList.add(importResourcePacks = new GuiButton(5, this.width / 2 - 100, this.height / 4 + 75, I18n.format("settingsBackup.import.option.resourcePacks") + " " + I18n.format(doImportResourcePacks ? "gui.yes" : "gui.no")));
		importResourcePacks.enabled = importer.hasResourcePacks() && EaglerFolderResourcePack.isSupported();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 115, I18n.format("settingsBackup.import.option.import")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 140, I18n.format("gui.cancel")));
	}

	@Override
	public void onGuiClosed() {
		try {
			importer.close();
		} catch (IOException e) {
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			if(!doImportProfile && !doImportSettings && !doImportServers && !doImportResourcePacks) {
				mc.displayGuiScreen(back);
			}else {
				mc.loadingScreen.eaglerShow(I18n.format("settingsBackup.importing.1"), I18n.format("settingsBackup.importing.2"));
				try {
					List<String> list1 = new ArrayList<>(mc.gameSettings.resourcePacks); 
					List<String> list2 = new ArrayList<>(mc.gameSettings.field_183018_l);
					importer.importProfileAndSettings(doImportProfile, doImportSettings, doImportServers, doImportResourcePacks);
					boolean resourcePacksChanged = !mc.gameSettings.resourcePacks.equals(list1) || !mc.gameSettings.field_183018_l.equals(list2);
					if(resourcePacksChanged || (doImportResourcePacks && (list1.size() > 0 || list2.size() > 0))) {
						mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
								I18n.format("resourcePack.load.pleaseWait"));
						mc.getResourcePackRepository().reconstruct(mc.gameSettings);
						mc.refreshResources();
					}
					mc.displayGuiScreen(back);
				} catch (IOException e) {
					EagRuntime.debugPrintStackTrace(e);
					mc.displayGuiScreen(new GuiScreenGenericErrorMessage("settingsBackup.importing.failed.1", "settingsBackup.importing.failed.2", back));
				}
			}
		}else if(par1GuiButton.id == 1) {
			mc.displayGuiScreen(back);
		}else if(par1GuiButton.id == 2) {
			doImportProfile = !doImportProfile;
			importProfile.displayString = I18n.format("settingsBackup.import.option.profile") + " " + I18n.format(doImportProfile ? "gui.yes" : "gui.no");
		}else if(par1GuiButton.id == 3) {
			doImportSettings = !doImportSettings;
			importSettings.displayString = I18n.format("settingsBackup.import.option.settings") + " " + I18n.format(doImportSettings ? "gui.yes" : "gui.no");
		}else if(par1GuiButton.id == 4) {
			doImportServers = !doImportServers;
			importServers.displayString = I18n.format("settingsBackup.import.option.servers") + " " + I18n.format(doImportServers ? "gui.yes" : "gui.no");
		}else if(par1GuiButton.id == 5) {
			doImportResourcePacks = !doImportResourcePacks;
			importResourcePacks.displayString = I18n.format("settingsBackup.import.option.resourcePacks") + " " + I18n.format(doImportResourcePacks ? "gui.yes" : "gui.no");
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("settingsBackup.import.title"), this.width / 2, this.height / 4 - 25, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}

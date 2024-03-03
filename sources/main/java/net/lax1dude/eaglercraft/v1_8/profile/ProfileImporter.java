package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.EPKDecompiler;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.EPKDecompiler.FileEntry;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;

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
public class ProfileImporter {

	private static final Logger logger = LogManager.getLogger("ProfileImporter");

	private byte[] data;
	private EPKDecompiler epkDecompiler;
	private boolean headerHasProfile;
	private boolean headerHasSettings;
	private boolean headerHasServers;
	private boolean headerHasResourcePacks;

	public ProfileImporter(byte[] data) {
		this.data = data;
	}

	public void readHeader() throws IOException {
		logger.info("Reading EPK file header...");
		epkDecompiler = new EPKDecompiler(data);
		
		FileEntry etr = epkDecompiler.readFile();
		if (etr == null || !etr.type.equals("HEAD") || !etr.name.equals("file-type")
				|| !EPKDecompiler.readASCII(etr.data).equals("epk/profile188")) {
			throw new IOException("EPK file is not a profile backup!");
		}
		
		etr = epkDecompiler.readFile();
		if (etr == null || !etr.type.equals("HEAD") || !etr.name.equals("file-exports") || etr.data.length != 1) {
			throw new IOException("EPK file is not a profile backup!");
		}

		headerHasProfile = (etr.data[0] & 1) != 0;
		headerHasSettings = (etr.data[0] & 2) != 0;
		headerHasServers = (etr.data[0] & 4) != 0;
		headerHasResourcePacks = (etr.data[0] & 8) != 0;
	}

	public boolean hasProfile() {
		return headerHasProfile;
	}

	public boolean hasSettings() {
		return headerHasSettings;
	}

	public boolean hasServers() {
		return headerHasServers;
	}

	public boolean hasResourcePacks() {
		return headerHasResourcePacks;
	}

	/**
	 * Note: this function is sensitive to the order file appear in the EPK
	 */
	public void importProfileAndSettings(boolean doImportProfile, boolean doImportSettings,
			boolean doImportServers, boolean doImportResourcePacks) throws IOException {
		doImportProfile &= headerHasProfile;
		doImportSettings &= headerHasSettings;
		doImportServers &= headerHasServers;
		doImportResourcePacks &= headerHasResourcePacks && EaglerFolderResourcePack.isSupported();
		FileEntry etr;
		vigg: while((etr = epkDecompiler.readFile()) != null) {
			if(etr.type.equals("FILE")) {
				switch(etr.name) {
				case "_eaglercraftX.p":
					if(doImportProfile) {
						logger.info("Importing profile...");
						EaglerProfile.read(etr.data);
						EagRuntime.setStorage("p", etr.data);
					}
					break;
				case "_eaglercraftX.g":
					if(doImportSettings) {
						logger.info("Importing settings...");
						Minecraft.getMinecraft().gameSettings.loadOptions(etr.data);
						EagRuntime.setStorage("g", etr.data);
					}
					break;
				case "_eaglercraftX.r":
					if(doImportSettings) {
						logger.info("Importing relays...");
						RelayManager.relayManager.load(etr.data);
						EagRuntime.setStorage("r", etr.data);
					}
					break;
				case "_eaglercraftX.s":
					if(doImportServers) {
						logger.info("Importing servers...");
						ServerList.getServerList().loadServerList(etr.data);
						EagRuntime.setStorage("s", etr.data);
					}
					break;
				default:
					if(etr.name.startsWith("certs/")) {
						UpdateService.addCertificateToSet(etr.data);
					}else if(etr.name.startsWith(EaglerFolderResourcePack.RESOURCE_PACKS + "/")) {
						if(doImportResourcePacks) {
							logger.info("Deleting old resource packs...");
							(new VFile2(EaglerFolderResourcePack.RESOURCE_PACKS)).listFiles(true).forEach(VFile2::delete);
							logger.info("Importing resource packs...");
							int counter = 0;
							do {
								if(etr.name.startsWith(EaglerFolderResourcePack.RESOURCE_PACKS + "/")) {
									(new VFile2(etr.name)).setAllBytes(etr.data);
									if(++counter % 100 == 0) {
										logger.info("Imported {} files", counter);
									}
								}
							}while((etr = epkDecompiler.readFile()) != null);
						}
						break vigg;
					}
					break;
				}
			}
		}
		logger.info("Import complete!");
	}
}

package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.update.UpdateCertificate;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;

import static net.lax1dude.eaglercraft.v1_8.sp.server.export.EPKCompiler.*;

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
public class ProfileExporter {

	private static final Logger logger = LogManager.getLogger("ProfileExporter");

	public static void exportProfileAndSettings(boolean doExportProfile, boolean doExportSettings,
			boolean doExportServers, boolean doExportResourcePacks) throws IOException {
		doExportResourcePacks &= EaglerFolderResourcePack.isSupported();
		EaglerOutputStream osb = new EaglerOutputStream();
		osb.write(new byte[]{(byte)69,(byte)65,(byte)71,(byte)80,(byte)75,(byte)71,(byte)36,(byte)36}); // EAGPKG$$
		osb.write(new byte[]{(byte)6,(byte)118,(byte)101,(byte)114,(byte)50,(byte)46,(byte)48}); // 6 + ver2.0
		Date d = new Date();
		
		byte[] filename = "profile.epk".getBytes(StandardCharsets.UTF_8);
		osb.write(filename.length);
		osb.write(filename);
		
		byte[] comment = ("\n\n #  Eaglercraft profile backup - \"" + EaglerProfile.getName() + "\""
				+ "\n #  Contains: " + (doExportProfile ? "profile " : "") + (doExportSettings ? "settings " : "")
				+ (doExportServers ? "servers " : "") + (doExportResourcePacks ? "resourcePacks" : "") + "\n\n")
				.getBytes(StandardCharsets.UTF_8);

		osb.write((comment.length >> 8) & 255);
		osb.write(comment.length & 255);
		osb.write(comment);
		
		writeLong(d.getTime(), osb);
		
		int lengthIntegerOffset = osb.size();
		osb.write(new byte[]{(byte)255,(byte)255,(byte)255,(byte)255}); // this will be replaced with the file count
		
		osb.write('G');
		OutputStream os = EaglerZLIB.newGZIPOutputStream(osb);
		
		os.write(new byte[]{(byte)72,(byte)69,(byte)65,(byte)68}); // HEAD
		os.write(new byte[]{(byte)9,(byte)102,(byte)105,(byte)108,(byte)101,(byte)45,(byte)116,(byte)121,
				(byte)112,(byte)101}); // 9 + file-type
		os.write(new byte[]{(byte)0,(byte)0,(byte)0,(byte)14,(byte)101,(byte)112,(byte)107,(byte)47,(byte)112,(byte)114,(byte)111,
				(byte)102,(byte)105,(byte)108,(byte)101,(byte)49,(byte)56,(byte)56}); // 14 + epk/profile188
		os.write('>');
		
		os.write(new byte[]{(byte)72,(byte)69,(byte)65,(byte)68}); // HEAD
		os.write(new byte[]{(byte)12,(byte)102,(byte)105,(byte)108,(byte)101,(byte)45,(byte)101,(byte)120,
				(byte)112,(byte)111,(byte)114,(byte)116,(byte)115,(byte)0,(byte)0,(byte)0,(byte)1}); // 12 + file-exports + 1
		os.write((doExportProfile ? 1 : 0) | (doExportSettings ? 2 : 0) | (doExportServers ? 4 : 0) | (doExportResourcePacks ? 8 : 0));
		os.write('>');
		
		int fileCount = 2;
		
		if(doExportProfile) {
			byte[] profileData = EaglerProfile.write();
			if(profileData == null) {
				throw new IOException("Could not write profile data!");
			}
			exportFileToEPK("_eaglercraftX.p", profileData, os);
			++fileCount;
		}
		
		if(doExportSettings) {
			logger.info("Exporting game settings...");
			byte[] gameSettings = Minecraft.getMinecraft().gameSettings.writeOptions();
			if(gameSettings == null) {
				throw new IOException("Could not write game settings!");
			}
			exportFileToEPK("_eaglercraftX.g", gameSettings, os);
			++fileCount;
			logger.info("Exporting relay settings...");
			byte[] relays = RelayManager.relayManager.write();
			if(relays == null) {
				throw new IOException("Could not write relay settings!");
			}
			exportFileToEPK("_eaglercraftX.r", relays, os);
			++fileCount;
		}
		
		if(doExportServers) {
			logger.info("Exporting server list...");
			byte[] servers = ServerList.getServerList().writeServerList();
			if(servers == null) {
				throw new IOException("Could not write server list!");
			}
			exportFileToEPK("_eaglercraftX.s", servers, os);
			++fileCount;
		}
		
		logger.info("Exporting certificates...");
		UpdateCertificate cert = UpdateService.getClientCertificate();
		if(cert != null) {
			exportFileToEPK("certs/main.cert", cert.rawCertData, os);
			++fileCount;
		}
		Collection<UpdateCertificate> updatesExport = UpdateService.getAvailableUpdates();
		int cc = 0;
		for(UpdateCertificate cert2 : updatesExport) {
			exportFileToEPK("certs/c" + (cc++) + ".cert", cert2.rawCertData, os);
			++fileCount;
		}
		
		if(doExportResourcePacks) {
			logger.info("Exporting resource packs...");
			byte[] packManifest = (new VFile2(EaglerFolderResourcePack.RESOURCE_PACKS + "/manifest.json")).getAllBytes();
			if(packManifest != null) {
				exportFileToEPK(EaglerFolderResourcePack.RESOURCE_PACKS + "/manifest.json", packManifest, os);
				++fileCount;
				VFile2 baseDir = new VFile2(EaglerFolderResourcePack.RESOURCE_PACKS);
				List<VFile2> files = baseDir.listFiles(true);
				logger.info("({} files to export)", files.size());
				for(int i = 0, l = files.size(); i < l; ++i) {
					VFile2 f = files.get(i);
					if(f.getPath().equals(EaglerFolderResourcePack.RESOURCE_PACKS + "/manifest.json")) {
						continue;
					}
					exportFileToEPK(f.getPath(), f.getAllBytes(), os);
					++fileCount;
					if(i > 0 && i % 100 == 0) {
						logger.info("Exported {} files", i);
					}
				}
			}
		}
		
		os.write(new byte[]{(byte)69,(byte)78,(byte)68,(byte)36}); // END$
		os.close();
		
		osb.write(new byte[]{(byte)58,(byte)58,(byte)58,(byte)89,(byte)69,(byte)69,(byte)58,(byte)62}); // :::YEE:>
		
		byte[] ret = osb.toByteArray();

		ret[lengthIntegerOffset] = (byte)((fileCount >> 24) & 0xFF);
		ret[lengthIntegerOffset + 1] = (byte)((fileCount >> 16) & 0xFF);
		ret[lengthIntegerOffset + 2] = (byte)((fileCount >> 8) & 0xFF);
		ret[lengthIntegerOffset + 3] = (byte)(fileCount & 0xFF);
		
		logger.info("Export complete!");
		
		EagRuntime.downloadFileWithName(EaglerProfile.getName() + "-backup.epk", ret);
	}

	private static void exportFileToEPK(String name, byte[] contents, OutputStream os) throws IOException {
		CRC32 checkSum = new CRC32();
		checkSum.update(contents);
		long sum = checkSum.getValue();
		
		os.write(new byte[]{(byte)70,(byte)73,(byte)76,(byte)69}); // FILE
		
		byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
		os.write(nameBytes.length);
		os.write(nameBytes);
		writeInt(contents.length + 5, os);
		writeInt((int)sum, os);
		
		os.write(contents);
		os.write(':');
		os.write('>');
	}
}

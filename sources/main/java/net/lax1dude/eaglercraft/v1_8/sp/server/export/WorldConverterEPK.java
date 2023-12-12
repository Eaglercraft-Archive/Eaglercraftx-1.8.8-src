package net.lax1dude.eaglercraft.v1_8.sp.server.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerSaveFormat;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

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
public class WorldConverterEPK {

	private static final Logger logger = LogManager.getLogger("WorldConverterEPK");

	public static void importWorld(byte[] archiveContents, String newName) throws IOException {
		logger.info("Importing world \"{}\" from EPK", newName);
		String folderName = newName.replaceAll("[\\./\"]", "_");
		VFile2 worldDir = EaglerIntegratedServerWorker.saveFormat.getSaveLoader(folderName, false).getWorldDirectory();
		while((new VFile2(worldDir, "level.dat")).exists() || (new VFile2(worldDir, "level.dat_old")).exists()) {
			folderName += "_";
			worldDir = EaglerIntegratedServerWorker.saveFormat.getSaveLoader(folderName, false).getWorldDirectory();
		}
		EPKDecompiler dc = new EPKDecompiler(archiveContents);
		EPKDecompiler.FileEntry f = null;
		int lastProgUpdate = 0;
		int prog = 0;
		String hasReadType = null;
		boolean has152Format = false;
		int cnt = 0;
		while((f = dc.readFile()) != null) {
			byte[] b = f.data;
			if(hasReadType == null) {
				if (f.type.equals("HEAD") && f.name.equals("file-type")
						&& ((hasReadType = EPKDecompiler.readASCII(f.data)).equals("epk/world188")
								|| (has152Format = hasReadType.equals("epk/world152")))) {
					if(has152Format) {
						logger.warn("World type detected as 1.5.2, it will be converted to 1.8.8 format");
					}
					continue;
				}else {
					throw new IOException("file does not contain a singleplayer 1.5.2 or 1.8.8 world!");
				}
			}
			if(f.type.equals("FILE")) {
				if(f.name.equals("level.dat") || f.name.equals("level.dat_old")) {
					NBTTagCompound worldDatNBT = CompressedStreamTools.readCompressed(new ByteArrayInputStream(b));
					worldDatNBT.getCompoundTag("Data").setString("LevelName", newName);
					worldDatNBT.getCompoundTag("Data").setLong("LastPlayed", System.currentTimeMillis());
					ByteArrayOutputStream tmp = new ByteArrayOutputStream();
					CompressedStreamTools.writeCompressed(worldDatNBT, tmp);
					b = tmp.toByteArray();
				}
				VFile2 ff = new VFile2(worldDir, f.name);
				ff.setAllBytes(b);
				prog += b.length;
				++cnt;
				if(prog - lastProgUpdate > 25000) {
					lastProgUpdate = prog;
					logger.info("Extracted {} files, {} bytes from EPK...", cnt, prog);
					EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.importing.1", prog);
				}
			}
		}
		logger.info("EPK was successfully extracted into directory \"{}\"", worldDir.getPath());
		String[] worldsTxt = EaglerSaveFormat.worldsList.getAllLines();
		if(worldsTxt == null || worldsTxt.length <= 0 || (worldsTxt.length == 1 && worldsTxt[0].trim().length() <= 0)) {
			worldsTxt = new String[] { folderName };
		}else {
			String[] tmp = worldsTxt;
			worldsTxt = new String[worldsTxt.length + 1];
			System.arraycopy(tmp, 0, worldsTxt, 0, tmp.length);
			worldsTxt[worldsTxt.length - 1] = folderName;
		}
		EaglerSaveFormat.worldsList.setAllChars(String.join("\n", worldsTxt));
	}

	public static byte[] exportWorld(String worldName) {
		String realWorldName = worldName;
		String worldOwner = "UNKNOWN";
		String splitter = new String(new char[] { (char)253, (char)233, (char)233 });
		if(worldName.contains(splitter)) {
			int i = worldName.lastIndexOf(splitter);
			worldOwner = worldName.substring(i + 3);
			realWorldName = worldName.substring(0, i);
		}
		VFile2 worldDir = EaglerIntegratedServerWorker.saveFormat.getSaveLoader(realWorldName, false).getWorldDirectory();
		logger.info("Exporting world directory \"{}\" as EPK", worldDir.getPath());
		final int[] bytesWritten = new int[1];
		final int[] filesWritten = new int[1];
		final int[] lastUpdate = new int[1];
		EPKCompiler c = new EPKCompiler(realWorldName, worldOwner, "epk/world188");
		String pfx = worldDir.getPath();
		for(VFile2 vf : worldDir.listFiles(true)) {
			++filesWritten[0];
			byte[] b = vf.getAllBytes();
			c.append(vf.getPath().substring(pfx.length() + 1), b);
			bytesWritten[0] += b.length;
			if (bytesWritten[0] - lastUpdate[0] > 25000) {
				lastUpdate[0] = bytesWritten[0];
				logger.info("Exporting {} files, {} bytes to EPK...", filesWritten[0], bytesWritten[0]);
				EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.exporting.1", bytesWritten[0]);
			}
		}
		byte[] r = c.complete();
		logger.info("World directory \"{}\" was successfully exported as EPK", worldDir.getPath());
		return r;
	}

}

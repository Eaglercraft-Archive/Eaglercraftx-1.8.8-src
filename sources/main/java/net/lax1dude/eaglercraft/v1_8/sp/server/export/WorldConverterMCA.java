package net.lax1dude.eaglercraft.v1_8.sp.server.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerChunkLoader;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerSaveFormat;
import net.minecraft.world.chunk.storage.RegionFile;
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
public class WorldConverterMCA {

	private static final Logger logger = LogManager.getLogger("WorldConverterMCA");

	public static void importWorld(byte[] archiveContents, String newName, byte gameRules) throws IOException {
		logger.info("Importing world \"{}\" from MCA", newName);
		String folderName = newName.replaceAll("[\\./\"]", "_");
		VFile2 worldDir = EaglerIntegratedServerWorker.saveFormat.getSaveLoader(folderName, false).getWorldDirectory();
		while((new VFile2(worldDir, "level.dat")).exists() || (new VFile2(worldDir, "level.dat_old")).exists()) {
			folderName += "_";
			worldDir = EaglerIntegratedServerWorker.saveFormat.getSaveLoader(folderName, false).getWorldDirectory();
		}
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(archiveContents));
		ZipEntry folderNameFile = null;
		List<char[]> fileNames = new ArrayList<>();
		while((folderNameFile = zis.getNextEntry()) != null) {
			if (folderNameFile.getName().contains("__MACOSX/")) continue;
			if (folderNameFile.isDirectory()) continue;
			String lowerName = folderNameFile.getName().toLowerCase();
			if (!(lowerName.endsWith(".dat") || lowerName.endsWith(".dat_old") || lowerName.endsWith(".mca") || lowerName.endsWith(".mcr"))) continue;
			fileNames.add(folderNameFile.getName().toCharArray());
		}
		final int[] i = new int[] { 0 };
		while(fileNames.get(0).length > i[0] && fileNames.stream().allMatch(w -> w[i[0]] == fileNames.get(0)[i[0]])) i[0]++;
		int folderPrefixOffset = i[0];
		zis = new ZipInputStream(new ByteArrayInputStream(archiveContents));
		ZipEntry f = null;
		int lastProgUpdate = 0;
		int prog = 0;
		byte[] bb = new byte[16384];
		while ((f = zis.getNextEntry()) != null) {
			if (f.getName().contains("__MACOSX/")) continue;
			if (f.isDirectory()) continue;
			String lowerName = f.getName().toLowerCase();
			if (!(lowerName.endsWith(".dat") || lowerName.endsWith(".dat_old") || lowerName.endsWith(".mca") || lowerName.endsWith(".mcr") || lowerName.endsWith(".bmp"))) continue;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len;
			while ((len = zis.read(bb)) != -1) {
				baos.write(bb, 0, len);
			}
			baos.close();
			byte[] b = baos.toByteArray();
			String fileName = f.getName().substring(folderPrefixOffset);
			if (fileName.equals("level.dat") || fileName.equals("level.dat_old")) {
				NBTTagCompound worldDatNBT = CompressedStreamTools.readCompressed(new ByteArrayInputStream(b));

				NBTTagCompound gameRulesNBT = worldDatNBT.getCompoundTag("Data").getCompoundTag("GameRules");
				gameRulesNBT.setString("loadSpawnChunks", (gameRules & 2) != 0 ? "true" : "false");
				String s = (gameRules & 1) != 0 ? "true" : "false";
				gameRulesNBT.setString("bedSpawnPoint", s);
				gameRulesNBT.setString("clickToRide", "false");
				gameRulesNBT.setString("clickToSit", s);
				gameRulesNBT.setString("colorCodes", s);
				gameRulesNBT.setString("doSignEditing", s);
				worldDatNBT.getCompoundTag("Data").setTag("GameRules", gameRulesNBT);

				worldDatNBT.getCompoundTag("Data").setString("LevelName", newName);
				worldDatNBT.getCompoundTag("Data").setLong("LastPlayed", System.currentTimeMillis());
				ByteArrayOutputStream bo = new ByteArrayOutputStream();
				CompressedStreamTools.writeCompressed(worldDatNBT, bo);
				b = bo.toByteArray();
				VFile2 ff = new VFile2(worldDir, fileName);
				ff.setAllBytes(b);
				prog += b.length;
			} else if ((fileName.endsWith(".mcr") || fileName.endsWith(".mca")) && (fileName.startsWith("region/") || fileName.startsWith("DIM1/region/") || fileName.startsWith("DIM-1/region/"))) {
				VFile2 chunkFolder = new VFile2(worldDir, fileName.startsWith("DIM1") ? "level1" : (fileName.startsWith("DIM-1") ? "level-1" : "level0"));
				RegionFile mca = new RegionFile(new RandomAccessMemoryFile(b, b.length));
				int loadChunksCount = 0;
				for(int j = 0; j < 32; ++j) {
					for(int k = 0; k < 32; ++k) {
						if(mca.isChunkSaved(j, k)) {
							NBTTagCompound chunkNBT;
							NBTTagCompound chunkLevel;
							try {
								chunkNBT = CompressedStreamTools.read(mca.getChunkDataInputStream(j, k));
								if(!chunkNBT.hasKey("Level", 10)) {
									throw new IOException("Chunk is missing level data!");
								}
								chunkLevel = chunkNBT.getCompoundTag("Level");
							}catch(Throwable t) {
								logger.error("{}: Could not read chunk: {}, {}", fileName, j, k);
								logger.error(t);
								continue;
							}
							int chunkX = chunkLevel.getInteger("xPos");
							int chunkZ = chunkLevel.getInteger("zPos");
							VFile2 chunkOut = new VFile2(chunkFolder, EaglerChunkLoader.getChunkPath(chunkX, chunkZ) + ".dat");
							if(chunkOut.exists()) {
								logger.error("{}: Chunk already exists: {}", fileName, chunkOut.getPath());
								continue;
							}
							ByteArrayOutputStream bao = new ByteArrayOutputStream();
							CompressedStreamTools.writeCompressed(chunkNBT, bao);
							b = bao.toByteArray();
							chunkOut.setAllBytes(b);
							prog += b.length;
							if (prog - lastProgUpdate > 25000) {
								lastProgUpdate = prog;
								EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.importing.2", prog);
							}
							++loadChunksCount;
						}
					}
				}
				logger.info("{}: Imported {} chunks successfully ({} bytes)", fileName, loadChunksCount, prog);
			} else if (fileName.startsWith("playerdata/") || fileName.startsWith("stats/")) {
				//TODO: LAN player inventories
			} else if (fileName.startsWith("data/") || fileName.startsWith("players/") || fileName.startsWith("eagler/skulls/")) {
				VFile2 ff = new VFile2(worldDir, fileName);
				ff.setAllBytes(b);
				prog += b.length;
			} else if (!fileName.equals("level.dat_mcr") && !fileName.equals("session.lock")) {
				logger.info("Skipping file: {}", fileName);
			}
			if (prog - lastProgUpdate > 25000) {
				lastProgUpdate = prog;
				EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.importing.2", prog);
			}
		}
		logger.info("MCA was successfully extracted into directory \"{}\"", worldDir.getPath());
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

	public static byte[] exportWorld(String folderName) throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bao);
		zos.setComment("contains backup of world '" + folderName + "'");
		VFile2 worldFolder = EaglerIntegratedServerWorker.saveFormat.getSaveLoader(folderName, false).getWorldDirectory();
		logger.info("Exporting world directory \"{}\" as MCA", worldFolder.getPath());
		VFile2 vf = new VFile2(worldFolder, "level.dat");
		byte[] b;
		int lastProgUpdate = 0;
		int prog = 0;
		boolean safe = false;
		if(vf.exists()) {
			zos.putNextEntry(new ZipEntry(folderName + "/level.dat"));
			b = vf.getAllBytes();
			zos.write(b);
			prog += b.length;
			safe = true;
		}
		vf = new VFile2(worldFolder, "level.dat_old");
		if(vf.exists()) {
			zos.putNextEntry(new ZipEntry(folderName + "/level.dat_old"));
			b = vf.getAllBytes();
			zos.write(b);
			prog += b.length;
			safe = true;
		}
		if (prog - lastProgUpdate > 25000) {
			lastProgUpdate = prog;
			EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.exporting.2", prog);
		}
		String[] srcFolderNames = new String[] { "level0", "level-1", "level1" };
		String[] dstFolderNames = new String[] { "/region/", "/DIM-1/region/", "/DIM1/region/" };
		List<VFile2> fileList;
		for(int i = 0; i < 3; ++i) {
			vf = new VFile2(worldFolder, srcFolderNames[i]);
			fileList = vf.listFiles(true);
			String regionFolder = folderName + dstFolderNames[i];
			logger.info("Converting chunks in \"{}\" as MCA to \"{}\"...", vf.getPath(), regionFolder);
			Map<String,RegionFile> regionFiles = new HashMap();
			for(VFile2 chunkFile : fileList) {
				NBTTagCompound chunkNBT;
				NBTTagCompound chunkLevel;
				try {
					b = chunkFile.getAllBytes();
					chunkNBT = CompressedStreamTools.readCompressed(new ByteArrayInputStream(b));
					if(!chunkNBT.hasKey("Level", 10)) {
						throw new IOException("Chunk is missing level data!");
					}
					chunkLevel = chunkNBT.getCompoundTag("Level");
				}catch(IOException t) {
					logger.error("Could not read chunk: {}", chunkFile.getPath());
					logger.error(t);
					continue;
				}
				int chunkX = chunkLevel.getInteger("xPos");
				int chunkZ = chunkLevel.getInteger("zPos");
				String regionFileName = "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca";
				RegionFile rf = regionFiles.get(regionFileName);
				if(rf == null) {
					rf = new RegionFile(new RandomAccessMemoryFile(new byte[65536], 0));
					regionFiles.put(regionFileName, rf);
				}
				try(DataOutputStream dos = rf.getChunkDataOutputStream(chunkX & 31, chunkZ & 31)) {
					CompressedStreamTools.write(chunkNBT, dos);
				}catch(IOException t) {
					logger.error("Could not write chunk to {}: {}", regionFileName, chunkFile.getPath());
					logger.error(t);
					continue;
				}
				prog += b.length;
				if (prog - lastProgUpdate > 25000) {
					lastProgUpdate = prog;
					EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.exporting.2", prog);
				}
			}
			if(regionFiles.isEmpty()) {
				logger.info("No region files were generated");
				continue;
			}
			for(Entry<String,RegionFile> etr : regionFiles.entrySet()) {
				String regionPath = regionFolder + etr.getKey();
				logger.info("Writing region file: {}", regionPath);
				zos.putNextEntry(new ZipEntry(regionPath));
				zos.write(etr.getValue().getFile().getByteArray());
			}
		}
		logger.info("Copying extra world data...");
		fileList = (new VFile2(worldFolder, "data")).listFiles(false);
		for(VFile2 dataFile : fileList) {
			zos.putNextEntry(new ZipEntry(folderName + "/data/" + dataFile.getName()));
			b = dataFile.getAllBytes();
			zos.write(b);
			prog += b.length;
			if (prog - lastProgUpdate > 25000) {
				lastProgUpdate = prog;
				EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.exporting.2", prog);
			}
		}
		fileList = (new VFile2(worldFolder, "players")).listFiles(false);
		for(VFile2 dataFile : fileList) {
			zos.putNextEntry(new ZipEntry(folderName + "/players/" + dataFile.getName()));
			b = dataFile.getAllBytes();
			zos.write(b);
			prog += b.length;
			if (prog - lastProgUpdate > 25000) {
				lastProgUpdate = prog;
				EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.exporting.2", prog);
			}
		}
		fileList = (new VFile2(worldFolder, "eagler/skulls")).listFiles(false);
		for(VFile2 dataFile : fileList) {
			zos.putNextEntry(new ZipEntry(folderName + "/eagler/skulls/" + dataFile.getName()));
			b = dataFile.getAllBytes();
			zos.write(b);
			prog += b.length;
			if (prog - lastProgUpdate > 25000) {
				lastProgUpdate = prog;
				EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.exporting.2", prog);
			}
		}
		zos.close();
		logger.info("World directory \"{}\" was successfully exported as MCA", worldFolder.getPath());
		return bao.toByteArray();
	}

}

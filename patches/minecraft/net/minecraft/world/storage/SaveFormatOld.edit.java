
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 6

~ 
~ import java.io.InputStream;
~ import java.io.OutputStream;

> DELETE  2  @  2 : 3

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;

> CHANGE  6 : 9  @  6 : 8

~ import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  2 : 3  @  2

+ 

> CHANGE  1 : 2  @  1 : 2

~ 	protected final VFile2 savesDirectory;

> CHANGE  1 : 2  @  1 : 6

~ 	public SaveFormatOld(VFile2 parFile) {

> CHANGE  7 : 8  @  7 : 8

~ 	public List<SaveFormatComparator> getSaveList() {

> CHANGE  8 : 9  @  8 : 9

~ 						worldinfo.areCommandsAllowed(), null));

> CHANGE  10 : 11  @  10 : 11

~ 		VFile2 file1 = new VFile2(this.savesDirectory, saveName);

> CHANGE  3 : 4  @  3 : 4

~ 			VFile2 file2 = new VFile2(file1, "level.dat");

> CHANGE  2 : 6  @  2 : 3

~ 					NBTTagCompound nbttagcompound2;
~ 					try (InputStream is = file2.getInputStream()) {
~ 						nbttagcompound2 = CompressedStreamTools.readCompressed(is);
~ 					}

> CHANGE  7 : 8  @  7 : 8

~ 			file2 = new VFile2(file1, "level.dat_old");

> CHANGE  2 : 6  @  2 : 3

~ 					NBTTagCompound nbttagcompound;
~ 					try (InputStream is = file2.getInputStream()) {
~ 						nbttagcompound = CompressedStreamTools.readCompressed(is);
~ 					}

> CHANGE  11 : 15  @  11 : 15

~ 	public boolean renameWorld(String dirName, String newName) {
~ 		VFile2 file1 = new VFile2(this.savesDirectory, dirName);
~ 		VFile2 file2 = new VFile2(file1, "level.dat");
~ 		{

> CHANGE  2 : 6  @  2 : 3

~ 					NBTTagCompound nbttagcompound;
~ 					try (InputStream is = file2.getInputStream()) {
~ 						nbttagcompound = CompressedStreamTools.readCompressed(is);
~ 					}

> CHANGE  2 : 9  @  2 : 5

~ 					try (OutputStream os = file2.getOutputStream()) {
~ 						CompressedStreamTools.writeCompressed(nbttagcompound, os);
~ 					}
~ 					return true;
~ 				} catch (Throwable exception) {
~ 					logger.error("Failed to rename world \"{}\"!", dirName);
~ 					logger.error(exception);

> INSERT  4 : 5  @  4

+ 		return false;

> CHANGE  3 : 4  @  3 : 16

~ 		return !canLoadWorld(parString1);

> CHANGE  3 : 5  @  3 : 8

~ 		VFile2 file1 = new VFile2(this.savesDirectory, parString1);
~ 		logger.info("Deleting level " + parString1);

> CHANGE  1 : 6  @  1 : 6

~ 		for (int i = 1; i <= 5; ++i) {
~ 			logger.info("Attempt " + i + "...");
~ 			if (deleteFiles(file1.listFiles(true), "singleplayer.busy.deleting")) {
~ 				return true;
~ 			}

> CHANGE  1 : 7  @  1 : 8

~ 			logger.warn("Unsuccessful in deleting contents.");
~ 			if (i < 5) {
~ 				try {
~ 					Thread.sleep(500L);
~ 				} catch (InterruptedException var5) {
~ 					;

> DELETE  2  @  2 : 4

> INSERT  1 : 3  @  1

+ 
+ 		return false;

> CHANGE  2 : 13  @  2 : 9

~ 	protected static boolean deleteFiles(List<VFile2> files, String progressString) {
~ 		long totalSize = 0l;
~ 		long lastUpdate = 0;
~ 		for (int i = 0, l = files.size(); i < l; ++i) {
~ 			VFile2 file1 = files.get(i);
~ 			if (progressString != null) {
~ 				totalSize += file1.length();
~ 				if (totalSize - lastUpdate > 10000) {
~ 					lastUpdate = totalSize;
~ 					EaglerIntegratedServerWorker.sendProgress(progressString, totalSize);
~ 				}

> DELETE  1  @  1 : 2

> CHANGE  10 : 11  @  10 : 11

~ 		return new SaveHandler(this.savesDirectory, s);

> CHANGE  15 : 17  @  15 : 17

~ 		return (new VFile2(this.savesDirectory, parString1, "level.dat")).exists()
~ 				|| (new VFile2(this.savesDirectory, parString1, "level.dat_old")).exists();

> EOF

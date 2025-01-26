
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 6  @  3 : 6

~ 
~ import java.io.InputStream;
~ import java.io.OutputStream;

> DELETE  2  @  2 : 3

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;
+ import net.lax1dude.eaglercraft.v1_8.sp.server.WorldsDB;

> CHANGE  1 : 5  @  1 : 8

~ import net.lax1dude.eaglercraft.v1_8.EagUtils;
~ import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  3 : 4  @  3 : 4

~ 	protected final VFile2 savesDirectory;

> CHANGE  1 : 2  @  1 : 6

~ 	public SaveFormatOld(VFile2 parFile) {

> CHANGE  7 : 8  @  7 : 8

~ 	public List<SaveFormatComparator> getSaveList() {

> CHANGE  8 : 9  @  8 : 9

~ 						worldinfo.areCommandsAllowed(), null));

> CHANGE  10 : 17  @  10 : 22

~ 		VFile2 file1 = WorldsDB.newVFile(this.savesDirectory, saveName);
~ 		VFile2 file2 = WorldsDB.newVFile(file1, "level.dat");
~ 		if (file2.exists()) {
~ 			try {
~ 				NBTTagCompound nbttagcompound2;
~ 				try (InputStream is = file2.getInputStream()) {
~ 					nbttagcompound2 = CompressedStreamTools.readCompressed(is);

> INSERT  1 : 6  @  1

+ 				NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
+ 				return new WorldInfo(nbttagcompound3);
+ 			} catch (Exception exception1) {
+ 				logger.error("Exception reading " + file2);
+ 				logger.error(exception1);

> INSERT  1 : 2  @  1

+ 		}

> CHANGE  1 : 7  @  1 : 9

~ 		file2 = WorldsDB.newVFile(file1, "level.dat_old");
~ 		if (file2.exists()) {
~ 			try {
~ 				NBTTagCompound nbttagcompound;
~ 				try (InputStream is = file2.getInputStream()) {
~ 					nbttagcompound = CompressedStreamTools.readCompressed(is);

> INSERT  1 : 6  @  1

+ 				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
+ 				return new WorldInfo(nbttagcompound1);
+ 			} catch (Exception exception) {
+ 				logger.error("Exception reading " + file2);
+ 				logger.error(exception);

> DELETE  1  @  1 : 3

> INSERT  1 : 3  @  1

+ 
+ 		return null;

> CHANGE  2 : 6  @  2 : 6

~ 	public boolean renameWorld(String dirName, String newName) {
~ 		VFile2 file1 = WorldsDB.newVFile(this.savesDirectory, dirName);
~ 		VFile2 file2 = WorldsDB.newVFile(file1, "level.dat");
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

~ 		VFile2 file1 = WorldsDB.newVFile(this.savesDirectory, parString1);
~ 		logger.info("Deleting level " + parString1);

> CHANGE  1 : 5  @  1 : 15

~ 		for (int i = 1; i <= 5; ++i) {
~ 			logger.info("Attempt " + i + "...");
~ 			if (deleteFiles(file1.listFiles(true), "singleplayer.busy.deleting")) {
~ 				return true;

> CHANGE  2 : 6  @  2 : 3

~ 			logger.warn("Unsuccessful in deleting contents.");
~ 			if (i < 5) {
~ 				EagUtils.sleep(500);
~ 			}

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

~ 		return (WorldsDB.newVFile(this.savesDirectory, parString1, "level.dat")).exists()
~ 				|| (WorldsDB.newVFile(this.savesDirectory, parString1, "level.dat_old")).exists();

> EOF

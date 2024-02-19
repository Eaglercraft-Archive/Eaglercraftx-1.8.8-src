
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  7 : 10  @  7 : 10

~ import java.io.InputStream;
~ import java.io.OutputStream;
~ import java.util.HashMap;

> INSERT  6 : 10  @  6

+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
+ import net.minecraft.scoreboard.ScoreboardSaveData;
+ import net.minecraft.village.VillageCollection;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.world.gen.structure.MapGenStructureData;

> INSERT  7 : 20  @  7

+ 	public static interface MapStorageProvider {
+ 		WorldSavedData createInstance(String mapFileName);
+ 	}
+ 
+ 	public static final Map<Class<? extends WorldSavedData>, MapStorageProvider> storageProviders = new HashMap();
+ 
+ 	static {
+ 		storageProviders.put(MapData.class, (s) -> new MapData(s));
+ 		storageProviders.put(MapGenStructureData.class, (s) -> new MapGenStructureData(s));
+ 		storageProviders.put(ScoreboardSaveData.class, (s) -> new ScoreboardSaveData(s));
+ 		storageProviders.put(VillageCollection.class, (s) -> new VillageCollection(s));
+ 	}
+ 

> CHANGE  12 : 13  @  12 : 13

~ 					VFile2 file1 = this.saveHandler.getMapFileFromName(s);

> CHANGE  2 : 3  @  2 : 4

~ 							worldsaveddata = (WorldSavedData) storageProviders.get(oclass).createInstance(s);

> CHANGE  3 : 7  @  3 : 8

~ 						try (InputStream is = file1.getInputStream()) {
~ 							NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(is);
~ 							worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
~ 						}

> CHANGE  2 : 3  @  2 : 3

~ 					EagRuntime.debugPrintStackTrace(exception1);

> CHANGE  35 : 36  @  35 : 36

~ 				VFile2 file1 = this.saveHandler.getMapFileFromName(parWorldSavedData.mapName);

> CHANGE  5 : 9  @  5 : 8

~ 
~ 					try (OutputStream fileoutputstream = file1.getOutputStream()) {
~ 						CompressedStreamTools.writeCompressed(nbttagcompound1, fileoutputstream);
~ 					}

> CHANGE  2 : 3  @  2 : 3

~ 				EagRuntime.debugPrintStackTrace(exception);

> CHANGE  12 : 13  @  12 : 13

~ 			VFile2 file1 = this.saveHandler.getMapFileFromName("idcounts");

> CHANGE  1 : 5  @  1 : 4

~ 				NBTTagCompound nbttagcompound;
~ 				try (DataInputStream datainputstream = new DataInputStream(file1.getInputStream())) {
~ 					nbttagcompound = CompressedStreamTools.read(datainputstream);
~ 				}

> CHANGE  11 : 12  @  11 : 12

~ 			EagRuntime.debugPrintStackTrace(exception);

> CHANGE  17 : 18  @  17 : 18

~ 				VFile2 file1 = this.saveHandler.getMapFileFromName("idcounts");

> CHANGE  8 : 11  @  8 : 11

~ 					try (DataOutputStream dataoutputstream = new DataOutputStream(file1.getOutputStream())) {
~ 						CompressedStreamTools.write(nbttagcompound, (DataOutput) dataoutputstream);
~ 					}

> CHANGE  2 : 3  @  2 : 3

~ 				EagRuntime.debugPrintStackTrace(exception);

> EOF


# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 3  @  1 : 16

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ 

> DELETE  1  @  1 : 4

> CHANGE  1 : 2  @  1 : 4

~ import java.util.LinkedList;

> DELETE  2  @  2 : 4

> CHANGE  1 : 6  @  1 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.futures.FutureTask;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;

> DELETE  8  @  8 : 11

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 2

> DELETE  7  @  7 : 8

> CHANGE  10 : 11  @  10 : 12

~ import net.minecraft.world.chunk.Chunk;

> DELETE  1  @  1 : 2

> CHANGE  2 : 4  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  1 : 2  @  1 : 2

~ public abstract class MinecraftServer implements Runnable, ICommandSender, IThreadListener {

> DELETE  1  @  1 : 2

> CHANGE  1 : 2  @  1 : 5

~ 	protected final List<ITickable> playersOnline = Lists.newArrayList();

> CHANGE  2 : 3  @  2 : 5

~ 	private final EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  3 : 4  @  3 : 4

~ 	protected boolean serverRunning = false;

> DELETE  2  @  2 : 3

> DELETE  12  @  12 : 13

> DELETE  1  @  1 : 2

> CHANGE  7 : 8  @  7 : 8

~ 	protected long timeOfLastWarning;

> DELETE  3  @  3 : 5

> CHANGE  1 : 2  @  1 : 4

~ 	protected final Queue<FutureTask<?>> futureTaskQueue = new LinkedList();

> CHANGE  1 : 4  @  1 : 2

~ 	protected long currentTime = getCurrentTimeMillis();
~ 	private boolean paused = false;
~ 	private boolean isSpawnChunksLoaded = false;

> CHANGE  1 : 2  @  1 : 3

~ 	public MinecraftServer(String worldName) {

> CHANGE  1 : 3  @  1 : 9

~ 		this.worldName = worldName;
~ 		this.commandManager = new ServerCommandManager();

> DELETE  2  @  2 : 15

> DELETE  7  @  7 : 12

> DELETE  1  @  1 : 23

> CHANGE  10 : 11  @  10 : 12

~ 	protected void loadAllWorlds(ISaveHandler isavehandler, String s1, WorldSettings worldsettings) {

> DELETE  3  @  3 : 5

> DELETE  1  @  1 : 2

> CHANGE  1 : 2  @  1 : 2

~ 			if (this.isDemo() || worldsettings == null) {

> DELETE  1  @  1 : 8

> DELETE  1  @  1 : 2

> CHANGE  39 : 44  @  39 : 40

~ 		this.isSpawnChunksLoaded = this.worldServers[0].getWorldInfo().getGameRulesInstance()
~ 				.getBoolean("loadSpawnChunks");
~ 		if (this.isSpawnChunksLoaded) {
~ 			this.initialWorldChunkLoad();
~ 		}

> CHANGE  15 : 17  @  15 : 17

~ 		for (int k = -192; k <= 192; k += 16) {
~ 			for (int l = -192; l <= 192; l += 16) {

> CHANGE  14 : 29  @  14 : 18

~ 	protected void unloadSpawnChunks() {
~ 		WorldServer worldserver = this.worldServers[0];
~ 		BlockPos blockpos = worldserver.getSpawnPoint();
~ 		int cnt = 0;
~ 
~ 		for (int k = -192; k <= 192 && this.isServerRunning(); k += 16) {
~ 			for (int l = -192; l <= 192 && this.isServerRunning(); l += 16) {
~ 				Chunk chunk = worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + k >> 4,
~ 						blockpos.getZ() + l >> 4);
~ 				if (chunk != null
~ 						&& !worldserver.getPlayerManager().hasPlayerInstance(chunk.xPosition, chunk.zPosition)) {
~ 					worldserver.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
~ 					++cnt;
~ 				}
~ 			}

> INSERT  2 : 3  @  2

+ 		logger.info("Dropped {} spawn chunks with no players in them", cnt);

> INSERT  20 : 21  @  20

+ 		EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.startingIntegratedServer", parInt1 * 0.01f);

> CHANGE  7 : 8  @  7 : 8

~ 	public void saveAllWorlds(boolean dontLog) {

> CHANGE  1 : 3  @  1 : 2

~ 			for (int i = 0; i < this.worldServers.length; ++i) {
~ 				WorldServer worldserver = this.worldServers[i];

> DELETE  20  @  20 : 23

> CHANGE  16 : 22  @  16 : 21

~ 		} else {
~ 			logger.info("Stopping server without saving");
~ 			String str = getFolderName();
~ 			logger.info("Deleting world \"{}\"...", str);
~ 			EaglerIntegratedServerWorker.saveFormat.deleteWorldDirectory(str);
~ 			logger.info("Deletion successful!");

> INSERT  3 : 8  @  3

+ 	public void deleteWorldAndStopServer() {
+ 		this.worldIsBeingDeleted = true;
+ 		this.initiateShutdown();
+ 	}
+ 

> DELETE  17  @  17 : 21

> DELETE  45  @  45 : 53

> DELETE  15  @  15 : 40

> DELETE  17  @  17 : 24

> CHANGE  1 : 8  @  1 : 4

~ 		boolean loadSpawnChunks = this.worldServers[0].getWorldInfo().getGameRulesInstance()
~ 				.getBoolean("loadSpawnChunks");
~ 		if (this.isSpawnChunksLoaded != loadSpawnChunks) {
~ 			if (loadSpawnChunks) {
~ 				this.initialWorldChunkLoad();
~ 			} else {
~ 				this.unloadSpawnChunks();

> CHANGE  1 : 2  @  1 : 4

~ 			this.isSpawnChunksLoaded = loadSpawnChunks;

> DELETE  13  @  13 : 16

> DELETE  1  @  1 : 5

> CHANGE  58 : 59  @  58 : 59

~ 		EaglerIntegratedServerWorker.tick();

> DELETE  20  @  20 : 24

> CHANGE  29 : 30  @  29 : 30

~ 		return "eagler";

> CHANGE  28 : 29  @  28 : 29

~ 			List<String> list = this.commandManager.getTabCompletionOptions(sender, input, pos);

> CHANGE  1 : 3  @  1 : 2

~ 				for (int i = 0, l = list.size(); i < l; ++i) {
~ 					String s2 = list.get(i);

> CHANGE  12 : 15  @  12 : 14

~ 			String[] unames = this.serverConfigManager.getAllUsernames();
~ 			for (int i = 0; i < unames.length; ++i) {
~ 				String s1 = unames[i];

> DELETE  13  @  13 : 17

> DELETE  16  @  16 : 20

> CHANGE  13 : 14  @  13 : 14

~ 		return this.worldName;

> DELETE  2  @  2 : 10

> DELETE  4  @  4 : 8

> DELETE  35  @  35 : 55

> DELETE  13  @  13 : 64

> DELETE  85  @  85 : 89

> DELETE  18  @  18 : 22

> DELETE  28  @  28 : 32

> DELETE  20  @  20 : 36

> CHANGE  4 : 7  @  4 : 6

~ 	public Entity getEntityFromUuid(EaglercraftUUID uuid) {
~ 		for (int i = 0; i < this.worldServers.length; ++i) {
~ 			WorldServer worldserver = this.worldServers[i];

> CHANGE  22 : 24  @  22 : 37

~ 	public boolean isCallingFromMinecraftThread() {
~ 		return true;

> CHANGE  2 : 4  @  2 : 5

~ 	public int getNetworkCompressionTreshold() {
~ 		return 256;

> CHANGE  2 : 4  @  2 : 4

~ 	public void setPaused(boolean pause) {
~ 		this.paused = pause;

> CHANGE  2 : 4  @  2 : 4

~ 	public boolean getPaused() {
~ 		return paused;

> EOF

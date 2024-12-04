
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

> CHANGE  1 : 8  @  1 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EagUtils;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.futures.FutureTask;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerIntegratedServerWorker;

> DELETE  8  @  8 : 11

> CHANGE  1 : 2  @  1 : 5

~ import net.minecraft.network.play.server.S41PacketServerDifficulty;

> DELETE  7  @  7 : 8

> DELETE  4  @  4 : 5

> CHANGE  5 : 6  @  5 : 7

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

> CHANGE  1 : 2  @  1 : 5

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

~ 	protected final Queue<FutureTask<?>> futureTaskQueue = new LinkedList<>();

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

> INSERT  6 : 11  @  6

+ 		if (worldinfo.isOldEaglercraftRandom()) {
+ 			LogManager.getLogger("EaglerMinecraftServer")
+ 					.info("Detected a pre-u34 world, using old EaglercraftRandom implementation for world generation");
+ 		}
+ 

> CHANGE  12 : 14  @  12 : 14

~ 					this.worldServers[j] = (WorldServer) (new DemoWorldServer(this, isavehandler, worldinfo, b0))
~ 							.init();

> CHANGE  1 : 2  @  1 : 3

~ 					this.worldServers[j] = (WorldServer) (new WorldServer(this, isavehandler, worldinfo, b0)).init();

> CHANGE  4 : 6  @  4 : 6

~ 				this.worldServers[j] = (WorldServer) (new WorldServerMulti(this, isavehandler, b0,
~ 						this.worldServers[0])).init();

> DELETE  3  @  3 : 6

> CHANGE  3 : 11  @  3 : 5

~ 		if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
~ 			this.setDifficultyForAllWorlds(this.getDifficulty());
~ 		}
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

> CHANGE  6 : 7  @  6 : 11

~ 					worldserver.saveAllChunks(true, (IProgressUpdate) null);

> DELETE  9  @  9 : 12

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

> CHANGE  29 : 30  @  29 : 30

~ 					EagUtils.sleep(Math.max(1L, 50L - i));

> DELETE  15  @  15 : 23

> DELETE  15  @  15 : 40

> CHANGE  7 : 8  @  7 : 8

~ 		long i = EagRuntime.nanoTime();

> DELETE  3  @  3 : 5

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 8

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

> DELETE  3  @  3 : 4

> DELETE  2  @  2 : 3

> CHANGE  2 : 3  @  2 : 16

~ 		this.tickTimeArray[this.tickCounter % 100] = EagRuntime.nanoTime() - i;

> DELETE  3  @  3 : 4

> DELETE  6  @  6 : 8

> CHANGE  1 : 2  @  1 : 2

~ 			long i = EagRuntime.nanoTime();

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 5

> DELETE  2  @  2 : 4

> DELETE  17  @  17 : 19

> DELETE  1  @  1 : 3

> CHANGE  2 : 3  @  2 : 3

~ 			this.timeOfLastDimensionTick[j][this.tickCounter % 100] = EagRuntime.nanoTime() - i;

> CHANGE  2 : 3  @  2 : 5

~ 		EaglerIntegratedServerWorker.tick();

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 6

> DELETE  11  @  11 : 15

> CHANGE  29 : 30  @  29 : 30

~ 		return "eagler";

> CHANGE  5 : 6  @  5 : 8

~ 				return "N/A (disabled)";

> CHANGE  20 : 21  @  20 : 21

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

> INSERT  16 : 19  @  16

+ 		this.getConfigurationManager().sendPacketToAllPlayers(new S41PacketServerDifficulty(
+ 				this.worldServers[0].getDifficulty(), this.worldServers[0].getWorldInfo().isDifficultyLocked()));
+ 	}

> INSERT  1 : 9  @  1

+ 	public void setDifficultyLockedForAllWorlds(boolean locked) {
+ 		for (int i = 0; i < this.worldServers.length; ++i) {
+ 			WorldServer worldserver = this.worldServers[i];
+ 			if (worldserver != null) {
+ 				worldserver.getWorldInfo().setDifficultyLocked(locked);
+ 			}
+ 		}
+ 

> DELETE  18  @  18 : 38

> DELETE  13  @  13 : 64

> DELETE  85  @  85 : 89

> DELETE  18  @  18 : 22

> DELETE  28  @  28 : 32

> CHANGE  1 : 2  @  1 : 2

~ 		return EagRuntime.steadyTimeMillis();

> DELETE  18  @  18 : 34

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

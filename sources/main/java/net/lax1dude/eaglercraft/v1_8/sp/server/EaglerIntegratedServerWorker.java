package net.lax1dude.eaglercraft.v1_8.sp.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.ILogRedirector;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.*;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.WorldConverterEPK;
import net.lax1dude.eaglercraft.v1_8.sp.server.export.WorldConverterMCA;
import net.lax1dude.eaglercraft.v1_8.sp.server.internal.ServerPlatformSingleplayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Copyright (c) 2023-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class EaglerIntegratedServerWorker {

	public static final Logger logger = LogManager.getLogger("EaglerIntegratedServer");

	private static EaglerMinecraftServer currentProcess = null;
	private static WorldSettings newWorldSettings = null;

	public static final EaglerSaveFormat saveFormat = new EaglerSaveFormat(EaglerSaveFormat.worldsFolder);

	private static final Map<String, IntegratedServerPlayerNetworkManager> openChannels = new HashMap();

	private static final IPCPacketManager packetManagerInstance = new IPCPacketManager();

	private static void processAsyncMessageQueue() {
		List<IPCPacketData> pktList = ServerPlatformSingleplayer.recieveAllPacket();
		if(pktList != null) {
			IPCPacketData packetData;
			for(int i = 0, l = pktList.size(); i < l; ++i) {
				packetData = pktList.get(i);
				if(packetData.channel.equals(SingleplayerServerController.IPC_CHANNEL)) {
					IPCPacketBase ipc;
					try {
						ipc = packetManagerInstance.IPCDeserialize(packetData.contents);
					}catch(IOException ex) {
						throw new RuntimeException("Failed to deserialize IPC packet", ex);
					}
					handleIPCPacket(ipc);
				}else {
					IntegratedServerPlayerNetworkManager netHandler = openChannels.get(packetData.channel);
					if(netHandler != null) {
						netHandler.addRecievedPacket(packetData.contents);
					}else {
						logger.error("Recieved packet on channel that does not exist: \"{}\"", packetData.channel);
					}
				}
			}
		}
	}

	public static void tick() {
		List<IntegratedServerPlayerNetworkManager> ocs = new ArrayList<>(openChannels.values());
		for(int i = 0, l = ocs.size(); i < l; ++i) {
			ocs.get(i).tick();
		}
	}

	public static EaglerMinecraftServer getServer() {
		return currentProcess;
	}

	public static boolean getChannelExists(String channel) {
		return openChannels.containsKey(channel);
	}

	public static void closeChannel(String channel) {
		IntegratedServerPlayerNetworkManager netmanager = openChannels.remove(channel);
		if(netmanager != null) {
			netmanager.closeChannel(new ChatComponentText("End of stream"));
			sendIPCPacket(new IPCPacket0CPlayerChannel(channel, false));
		}
	}

	private static void startPlayerConnnection(String channel) {
		if(openChannels.containsKey(channel)) {
			logger.error("Tried opening player channel that already exists: {}", channel);
			return;
		}
		if(currentProcess == null) {
			logger.error("Tried opening player channel while server is stopped: {}", channel);
			return;
		}
		IntegratedServerPlayerNetworkManager networkmanager = new IntegratedServerPlayerNetworkManager(channel);
		networkmanager.setConnectionState(EnumConnectionState.LOGIN);
		networkmanager.setNetHandler(new NetHandlerLoginServer(currentProcess, networkmanager));
		openChannels.put(channel, networkmanager);
	}

	private static void handleIPCPacket(IPCPacketBase ipc) {
		int id = ipc.id();
		try {
			switch(id) {
			case IPCPacket00StartServer.ID: {
				IPCPacket00StartServer pkt = (IPCPacket00StartServer)ipc;
				
				if(!isServerStopped()) {
					currentProcess.stopServer();
				}
				
				currentProcess = new EaglerMinecraftServer(pkt.worldName, pkt.ownerName, pkt.initialViewDistance, newWorldSettings, pkt.demoMode);
				currentProcess.setBaseServerProperties(EnumDifficulty.getDifficultyEnum(pkt.initialDifficulty), newWorldSettings == null ? GameType.SURVIVAL : newWorldSettings.getGameType());
				currentProcess.startServer();
				
				String[] worlds = EaglerSaveFormat.worldsList.getAllLines();
				if(worlds == null || (worlds.length == 1 && worlds[0].trim().length() <= 0)) {
					worlds = null;
				}
				if(worlds == null) {
					EaglerSaveFormat.worldsList.setAllChars(pkt.worldName);
				}else {
					boolean found = false;
					for(int i = 0; i < worlds.length; ++i) {
						if(worlds[i].equals(pkt.worldName)) {
							found = true;
							break;
						}
					}
					if(!found) {
						String[] s = new String[worlds.length + 1];
						s[0] = pkt.worldName;
						System.arraycopy(worlds, 0, s, 1, worlds.length);
						EaglerSaveFormat.worldsList.setAllChars(String.join("\n", s));
					}
				}
				
				sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket00StartServer.ID));
				break;
			}
			case IPCPacket01StopServer.ID: {
				if(currentProcess != null) {
					currentProcess.stopServer();
					currentProcess = null;
				}
				sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket01StopServer.ID));
				break;
			}
			case IPCPacket02InitWorld.ID: {
				tryStopServer();
				IPCPacket02InitWorld pkt = (IPCPacket02InitWorld)ipc;
				newWorldSettings = new WorldSettings(pkt.seed, GameType.getByID(pkt.gamemode), pkt.structures,
						pkt.hardcore, WorldType.worldTypes[pkt.worldType]);
				newWorldSettings.setWorldName(pkt.worldArgs); // "setWorldName" is actually for setting generator arguments, MCP fucked up
				if(pkt.bonusChest) {
					newWorldSettings.enableBonusChest();
				}
				if(pkt.cheats) {
					newWorldSettings.enableCommands();
				}
				break;
			}
			case IPCPacket03DeleteWorld.ID: {
				tryStopServer();
				IPCPacket03DeleteWorld pkt = (IPCPacket03DeleteWorld)ipc;
				if(!saveFormat.deleteWorldDirectory(pkt.worldName)) {
					sendTaskFailed();
					break;
				}
				String[] worldsTxt = EaglerSaveFormat.worldsList.getAllLines();
				if(worldsTxt != null) {
					List<String> newWorlds = new ArrayList();
					for(int i = 0; i < worldsTxt.length; ++i) {
						String str = worldsTxt[i];
						if(!str.equalsIgnoreCase(pkt.worldName)) {
							newWorlds.add(str);
						}
					}
					EaglerSaveFormat.worldsList.setAllChars(String.join("\n", newWorlds));
				}
				sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket03DeleteWorld.ID));
				break;
			}
			case IPCPacket05RequestData.ID: {
				tryStopServer();
				IPCPacket05RequestData pkt = (IPCPacket05RequestData)ipc;
				if(pkt.request == IPCPacket05RequestData.REQUEST_LEVEL_EAG) {
					sendIPCPacket(new IPCPacket09RequestResponse(WorldConverterEPK.exportWorld(pkt.worldName)));
				}else if(pkt.request == IPCPacket05RequestData.REQUEST_LEVEL_MCA) {
					sendIPCPacket(new IPCPacket09RequestResponse(WorldConverterMCA.exportWorld(pkt.worldName)));
				}else {
					logger.error("Unknown IPCPacket05RequestData type {}", ((int)pkt.request & 0xFF));
					sendTaskFailed();
				}
				break;
			}
			case IPCPacket06RenameWorldNBT.ID: {
				tryStopServer();
				IPCPacket06RenameWorldNBT pkt = (IPCPacket06RenameWorldNBT)ipc;
				boolean b = false;
				if(pkt.duplicate) {
					b = saveFormat.duplicateWorld(pkt.worldName, pkt.displayName);
				}else {
					b = saveFormat.renameWorld(pkt.worldName, pkt.displayName);
				}
				if(!b) {
					sendTaskFailed();
					break;
				}
				sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket06RenameWorldNBT.ID));
				break;
			}
			case IPCPacket07ImportWorld.ID: {
				tryStopServer();
				IPCPacket07ImportWorld pkt = (IPCPacket07ImportWorld)ipc;
				try {
					if(pkt.worldFormat == IPCPacket07ImportWorld.WORLD_FORMAT_EAG) {
						WorldConverterEPK.importWorld(pkt.worldData, pkt.worldName);
					}else if(pkt.worldFormat == IPCPacket07ImportWorld.WORLD_FORMAT_MCA) {
						WorldConverterMCA.importWorld(pkt.worldData, pkt.worldName, pkt.gameRules);
					}else {
						throw new IOException("Client requested an unsupported export format!");
					}
					sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket07ImportWorld.ID));
				}catch(IOException ex) {
					sendIPCPacket(new IPCPacket15Crashed("COULD NOT IMPORT WORLD \"" + pkt.worldName + "\"!!!\n\n" + EagRuntime.getStackTrace(ex) + "\n\nFile is probably corrupt, try a different world"));
					sendTaskFailed();
				}
				break;
			}
			case IPCPacket0ASetWorldDifficulty.ID: {
				IPCPacket0ASetWorldDifficulty pkt = (IPCPacket0ASetWorldDifficulty)ipc;
				if(!isServerStopped()) {
					if(pkt.difficulty == (byte)-1) {
						currentProcess.setDifficultyLockedForAllWorlds(true);
					}else {
						currentProcess.setDifficultyForAllWorlds(EnumDifficulty.getDifficultyEnum(pkt.difficulty));
					}
				}else {
					logger.warn("Client tried to set difficulty while server was stopped");
				}
				break;
			}
			case IPCPacket0BPause.ID: {
				IPCPacket0BPause pkt = (IPCPacket0BPause)ipc;
				if(!isServerStopped()) {
					currentProcess.setPaused(pkt.pause);
					sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket0BPause.ID));
				}else {
					logger.error("Client tried to {} while server was stopped", pkt.pause ? "pause" : "unpause");
					sendTaskFailed();
				}
				break;
			}
			case IPCPacket0CPlayerChannel.ID: {
				IPCPacket0CPlayerChannel pkt = (IPCPacket0CPlayerChannel)ipc;
				if(!isServerStopped()) {
					if(pkt.open) {
						startPlayerConnnection(pkt.channel);
					}else {
						closeChannel(pkt.channel);
					}
				}else {
					logger.error("Client tried to {} channel server was stopped", pkt.open ? "open" : "close");
				}
				break;
			}
			case IPCPacket0EListWorlds.ID: {
				IPCPacket0EListWorlds pkt = (IPCPacket0EListWorlds)ipc;
				if(!isServerStopped()) {
					logger.error("Client tried to list worlds while server was running");
					sendTaskFailed();
				}else {
					String[] worlds = EaglerSaveFormat.worldsList.getAllLines();
					if(worlds == null) {
						sendIPCPacket(new IPCPacket16NBTList(IPCPacket16NBTList.WORLD_LIST, new LinkedList<NBTTagCompound>()));
						break;
					}
					LinkedHashSet<String> updatedList = new LinkedHashSet();
					LinkedList<NBTTagCompound> sendListNBT = new LinkedList();
					boolean rewrite = false;
					for(int i = 0; i < worlds.length; ++i) {
						String w = worlds[i].trim();
						if(w.length() > 0) {
							VFile2 vf = new VFile2(EaglerSaveFormat.worldsFolder, w, "level.dat");
							if(!vf.exists()) {
								vf = new VFile2(EaglerSaveFormat.worldsFolder, w, "level.dat_old");
							}
							if(vf.exists()) {
								try(InputStream dat = vf.getInputStream()) {
									if(updatedList.add(w)) {
										NBTTagCompound worldDatNBT = CompressedStreamTools.readCompressed(dat);
										worldDatNBT.setString("folderNameEagler", w);
										sendListNBT.add(worldDatNBT);
									}else {
										rewrite = true;
									}
									continue;
								}catch(IOException e) {
									// shit fuck
								}
							}
							rewrite = true;
							logger.error("World level.dat for '{}' was not found, attempting to delete", w);
							if(!saveFormat.deleteWorldDirectory(w)) {
								logger.error("Failed to delete '{}'! It will be removed from the worlds list anyway", w);
							}
						}else {
							rewrite = true;
						}
					}
					if(rewrite) {
						EaglerSaveFormat.worldsList.setAllChars(String.join("\n", updatedList));
					}
					sendIPCPacket(new IPCPacket16NBTList(IPCPacket16NBTList.WORLD_LIST, sendListNBT));
				}
				break;
			}
			case IPCPacket14StringList.ID: {
				IPCPacket14StringList pkt = (IPCPacket14StringList)ipc;
				switch(pkt.opCode) {
				case IPCPacket14StringList.LOCALE:
					StringTranslate.initServer(pkt.stringList);
					break;
				//case IPCPacket14StringList.STAT_GUID:
				//	AchievementMap.init(pkt.stringList);
				//	AchievementList.init();
				//	break;
				default:
					logger.error("Strange string list 0x{} with length{} recieved", Integer.toHexString(pkt.opCode), pkt.stringList.size());
					break;
				}
				break;
			}
			case IPCPacket17ConfigureLAN.ID: {

				IPCPacket17ConfigureLAN pkt = (IPCPacket17ConfigureLAN)ipc;
				if(!pkt.iceServers.isEmpty() && ServerPlatformSingleplayer.getClientConfigAdapter().isAllowVoiceClient()) {
					currentProcess.enableVoice(pkt.iceServers.toArray(new String[pkt.iceServers.size()]));
				}
				currentProcess.getConfigurationManager().configureLAN(pkt.gamemode, pkt.cheats); // don't use iceServers

				break;
			}
			case IPCPacket18ClearPlayers.ID: {
				if(!isServerStopped()) {
					logger.error("Client tried to clear players while server was running");
					sendTaskFailed();
				}else {
					saveFormat.clearPlayers(((IPCPacket18ClearPlayers)ipc).worldName);
					sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket18ClearPlayers.ID));
				}
				break;
			}
			case IPCPacket19Autosave.ID: {
				if(!isServerStopped()) {
					currentProcess.getConfigurationManager().saveAllPlayerData();
					currentProcess.saveAllWorlds(false);
					sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket19Autosave.ID));
				}else {
					logger.error("Client tried to autosave while server was stopped");
					sendTaskFailed();
				}
				break;
			}
			case IPCPacket21EnableLogging.ID: {
				enableLoggingRedirector(((IPCPacket21EnableLogging)ipc).enable);
				break;
			}
			default: 
				logger.error("IPC packet type 0x{} class \"{}\" was not handled", Integer.toHexString(id), ipc.getClass().getSimpleName());
				sendTaskFailed();
				break;
			}
		}catch(Throwable t) {
			logger.error("IPC packet type 0x{} class \"{}\" was not processed correctly", Integer.toHexString(id), ipc.getClass().getSimpleName());
			logger.error(t);
			sendIPCPacket(new IPCPacket15Crashed("IPC packet type 0x" + Integer.toHexString(id) + " class \"" + ipc.getClass().getSimpleName() + "\" was not processed correctly!\n\n" + EagRuntime.getStackTrace(t)));
			sendTaskFailed();
		}
	}

	public static void enableLoggingRedirector(boolean en) {
		LogManager.logRedirector = en ? new ILogRedirector() {
			@Override
			public void log(String txt, boolean err) {
				sendLogMessagePacket(txt, err);
			}
		} : null;
	}

	public static void sendLogMessagePacket(String txt, boolean err) {
		sendIPCPacket(new IPCPacket20LoggerMessage(txt, err));
	}

	public static void sendIPCPacket(IPCPacketBase ipc) {
		byte[] pkt;
		try {
			pkt = packetManagerInstance.IPCSerialize(ipc);
		}catch (IOException ex) {
			throw new RuntimeException("Failed to serialize IPC packet", ex);
		}
		ServerPlatformSingleplayer.sendPacket(new IPCPacketData(SingleplayerServerController.IPC_CHANNEL, pkt));
	}

	public static void reportTPS(List<String> texts) {
		sendIPCPacket(new IPCPacket14StringList(IPCPacket14StringList.SERVER_TPS, texts));
	}

	public static void sendTaskFailed() {
		sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacketFFProcessKeepAlive.FAILURE));
	}

	public static void sendProgress(String updateMessage, float updateProgress) {
		sendIPCPacket(new IPCPacket0DProgressUpdate(updateMessage, updateProgress));
	}

	private static boolean isServerStopped() {
		return currentProcess == null || !currentProcess.isServerRunning();
	}

	private static void tryStopServer() {
		if(!isServerStopped()) {
			currentProcess.stopServer();
		}
		currentProcess = null;
	}

	private static void mainLoop() {
		processAsyncMessageQueue();
		
		if(currentProcess != null) {
			if(currentProcess.isServerRunning()) {
				currentProcess.mainLoop();
			}
			if(!currentProcess.isServerRunning()) {
				currentProcess.stopServer();
				currentProcess = null;
				sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacket01StopServer.ID));
			}
		}else {
			EagUtils.sleep(50l);
		}
	}

	public static void serverMain() {
		try {
			currentProcess = null;
			logger.info("Starting EaglercraftX integrated server worker...");
			
			// signal thread startup successful
			sendIPCPacket(new IPCPacketFFProcessKeepAlive(0xFF));
			
			while(true) {
				mainLoop();
				EagUtils.sleep(0l);
			}
		}catch(Throwable tt) {
			if(tt instanceof ReportedException) {
				String fullReport = ((ReportedException)tt).getCrashReport().getCompleteReport();
				logger.error(fullReport);
				sendIPCPacket(new IPCPacket15Crashed(fullReport));
			}else {
				logger.error("Server process encountered a fatal error!");
				logger.error(tt);
				sendIPCPacket(new IPCPacket15Crashed("SERVER PROCESS EXITED!\n\n" + EagRuntime.getStackTrace(tt)));
			}
		}finally {
			if(!isServerStopped()) {
				try {
					currentProcess.stopServer();
				}catch(Throwable t) {
					logger.error("Encountered exception while stopping server!");
					logger.error(t);
				}
			}
			logger.error("Server process exited!");
			sendIPCPacket(new IPCPacketFFProcessKeepAlive(IPCPacketFFProcessKeepAlive.EXITED));
		}
	}
}

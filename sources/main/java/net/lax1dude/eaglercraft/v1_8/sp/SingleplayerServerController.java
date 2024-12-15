package net.lax1dude.eaglercraft.v1_8.sp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebRTC;

import org.apache.commons.lang3.StringUtils;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.sp.internal.ClientPlatformSingleplayer;
import net.lax1dude.eaglercraft.v1_8.sp.ipc.*;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.lax1dude.eaglercraft.v1_8.sp.socket.ClientIntegratedServerNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.WorldInfo;

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
public class SingleplayerServerController implements ISaveFormat {

	public static final String IPC_CHANNEL = "~!IPC";
	public static final String PLAYER_CHANNEL = "~!LOCAL_PLAYER";

	private static int statusState = IntegratedServerState.WORLD_WORKER_NOT_RUNNING;
	private static boolean loggingState = true;
	private static String worldStatusString = "";
	private static float worldStatusProgress = 0.0f;
	private static final LinkedList<IPCPacket15Crashed> exceptions = new LinkedList<>();
	private static final Set<Integer> issuesDetected = new HashSet<>();

	public static final SingleplayerServerController instance = new SingleplayerServerController();
	public static final Logger logger = LogManager.getLogger("SingleplayerServerController");
	public static final List<SaveFormatComparator> saveListCache = new ArrayList<>();
	public static final Map<String, WorldInfo> saveListMap = new HashMap<>();
	public static final List<NBTTagCompound> saveListNBT = new ArrayList<>();

	private static boolean isPaused = false;
	private static List<String> integratedServerTPS = new ArrayList<>();
	private static long integratedServerLastTPSUpdate = 0;
	public static final ClientIntegratedServerNetworkManager localPlayerNetworkManager = new ClientIntegratedServerNetworkManager(PLAYER_CHANNEL);
	private static final List<String> openLANChannels = new ArrayList<>();

	private static final IPCPacketManager packetManagerInstance = new IPCPacketManager();

	private SingleplayerServerController() {
	}

	public static void startIntegratedServerWorker(boolean forceSingleThread) {
		if(statusState == IntegratedServerState.WORLD_WORKER_NOT_RUNNING) {
			exceptions.clear();
			issuesDetected.clear();
			statusState = IntegratedServerState.WORLD_WORKER_BOOTING;
			loggingState = true;
			callFailed = false;
			boolean singleThreadSupport = ClientPlatformSingleplayer.isSingleThreadModeSupported();
			if(!singleThreadSupport && forceSingleThread) {
				throw new UnsupportedOperationException("Single thread mode is not supported!");
			}
			if(forceSingleThread || !singleThreadSupport) {
				ClientPlatformSingleplayer.startIntegratedServer(forceSingleThread);
			}else {
				try {
					ClientPlatformSingleplayer.startIntegratedServer(forceSingleThread);
				}catch(Throwable t) {
					logger.error("Failed to start integrated server worker");
					logger.error(t);
					logger.error("Attempting to use single thread mode");
					exceptions.clear();
					issuesDetected.clear();
					statusState = IntegratedServerState.WORLD_WORKER_BOOTING;
					loggingState = true;
					ClientPlatformSingleplayer.startIntegratedServer(true);
				}
			}
		}
	}

	public static boolean isIssueDetected(int issue) {
		return issuesDetected.contains(issue);
	}

	public static boolean isIntegratedServerWorkerStarted() {
		return statusState != IntegratedServerState.WORLD_WORKER_NOT_RUNNING && statusState != IntegratedServerState.WORLD_WORKER_BOOTING;
	}

	public static boolean isIntegratedServerWorkerAlive() {
		return statusState != IntegratedServerState.WORLD_WORKER_NOT_RUNNING;
	}

	public static boolean isRunningSingleThreadMode() {
		return ClientPlatformSingleplayer.isRunningSingleThreadMode();
	}

	public static boolean isReady() {
		return statusState == IntegratedServerState.WORLD_NONE;
	}

	public static boolean isWorldNotLoaded() {
		return statusState == IntegratedServerState.WORLD_NONE || statusState == IntegratedServerState.WORLD_WORKER_NOT_RUNNING ||
				statusState == IntegratedServerState.WORLD_WORKER_BOOTING;
	}
	
	public static boolean isWorldRunning() {
		return statusState == IntegratedServerState.WORLD_LOADED || statusState == IntegratedServerState.WORLD_PAUSED ||
				statusState == IntegratedServerState.WORLD_LOADING || statusState == IntegratedServerState.WORLD_SAVING;
	}
	
	public static boolean isWorldReady() {
		return statusState == IntegratedServerState.WORLD_LOADED || statusState == IntegratedServerState.WORLD_PAUSED ||
				statusState == IntegratedServerState.WORLD_SAVING;
	}
	
	public static int getStatusState() {
		return statusState;
	}
	
	public static boolean isChannelOpen(String ch) {
		return openLANChannels.contains(ch);
	}
	
	public static boolean isChannelNameAllowed(String ch) {
		return !ch.startsWith("~!");
	}
	
	public static void openPlayerChannel(String ch) {
		if(openLANChannels.contains(ch)) {
			logger.error("Tried to open channel that already exists: \"{}\"", ch);
		} else if (!isChannelNameAllowed(ch)) {
			logger.error("Tried to open disallowed channel name: \"{}\"", ch);
		}else {
			openLANChannels.add(ch);
			sendIPCPacket(new IPCPacket0CPlayerChannel(ch, true));
		}
	}
	
	public static void closePlayerChannel(String ch) {
		if(!openLANChannels.remove(ch)) {
			logger.error("Tried to close channel that doesn't exist: \"{}\"", ch);
		}else {
			sendIPCPacket(new IPCPacket0CPlayerChannel(ch, false));
			PlatformWebRTC.serverLANDisconnectPeer(ch);
		}
	}
	
	public static void openLocalPlayerChannel() {
		localPlayerNetworkManager.isPlayerChannelOpen = true;
		sendIPCPacket(new IPCPacket0CPlayerChannel(PLAYER_CHANNEL, true));
	}
	
	public static void closeLocalPlayerChannel() {
		localPlayerNetworkManager.isPlayerChannelOpen = false;
		sendIPCPacket(new IPCPacket0CPlayerChannel(PLAYER_CHANNEL, false));
	}
	
	private static void ensureReady() {
		if(!isReady()) {
			String msg = "Server is in state " + statusState + " '" + IntegratedServerState.getStateName(statusState) + "' which is not the 'WORLD_NONE' state for the requested IPC operation";
			throw new IllegalStateException(msg);
		}
	}
	
	private static void ensureWorldReady() {
		if(!isWorldReady()) {
			String msg = "Server is in state " + statusState + " '" + IntegratedServerState.getStateName(statusState) + "' which is not the 'WORLD_LOADED' state for the requested IPC operation";
			throw new IllegalStateException(msg);
		}
	}

	public static void launchEaglercraftServer(String folderName, int difficulty, int viewDistance, WorldSettings settings) {
		ensureReady();
		clearTPS();
		if(settings != null) {
			sendIPCPacket(new IPCPacket02InitWorld(folderName, settings.getGameType().getID(),
					settings.getTerrainType().getWorldTypeID(), settings.getWorldName(), settings.getSeed(),
					settings.areCommandsAllowed(), settings.isMapFeaturesEnabled(), settings.isBonusChestEnabled(),
					settings.getHardcoreEnabled()));
		}
		statusState = IntegratedServerState.WORLD_LOADING;
		worldStatusProgress = 0.0f;
		sendIPCPacket(new IPCPacket00StartServer(folderName, EaglerProfile.getName(), difficulty, viewDistance, EagRuntime.getConfiguration().isDemo()));
	}

	public static void clearTPS() { 
		integratedServerTPS.clear();
		integratedServerLastTPSUpdate = 0l;
	}

	public static List<String> getTPS() {
		return integratedServerTPS;
	}

	public static long getTPSAge() {
		return EagRuntime.steadyTimeMillis() - integratedServerLastTPSUpdate;
	}

	public static boolean hangupEaglercraftServer() {
		LANServerController.closeLAN();
		if(isWorldRunning()) {
			logger.error("Shutting down integrated server due to unexpected client hangup, this is a memleak");
			statusState = IntegratedServerState.WORLD_UNLOADING;
			sendIPCPacket(new IPCPacket01StopServer());
			return true;
		}else {
			return false;
		}
	}

	public static boolean shutdownEaglercraftServer() {
		LANServerController.closeLAN();
		if(isWorldRunning()) {
			logger.info("Shutting down integrated server");
			statusState = IntegratedServerState.WORLD_UNLOADING;
			sendIPCPacket(new IPCPacket01StopServer());
			return true;
		}else {
			return false;
		}
	}

	public static void autoSave() {
		if(!isPaused) {
			statusState = IntegratedServerState.WORLD_SAVING;
			sendIPCPacket(new IPCPacket19Autosave());
		}
	}

	public static void setPaused(boolean pause) {
		if(statusState != IntegratedServerState.WORLD_LOADED && statusState != IntegratedServerState.WORLD_PAUSED && statusState != IntegratedServerState.WORLD_SAVING) {
			return;
		}
		if(isPaused != pause) {
			sendIPCPacket(new IPCPacket0BPause(pause));
			isPaused = pause;
		}
	}

	public static void runTick() {
		List<IPCPacketData> pktList = ClientPlatformSingleplayer.recieveAllPacket();
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
				}else if(packetData.channel.equals(SingleplayerServerController.PLAYER_CHANNEL)) {
					if(localPlayerNetworkManager.getConnectStatus() != EnumEaglerConnectionState.CLOSED) {
						localPlayerNetworkManager.addRecievedPacket(packetData.contents);
					}else {
						logger.warn("Recieved {} byte packet on closed local player connection", packetData.contents.length);
					}
				}else {
					//logger.warn("Recieved packet on IPC channel '{}', forwarding to PlatformWebRTC even though the channel should be mapped", packetData.channel);
					// just to be safe
					PlatformWebRTC.serverLANWritePacket(packetData.channel, packetData.contents);
				}
			}
		}

		if(EagRuntime.getPlatformType() == EnumPlatformType.JAVASCRIPT) {
			boolean logWindowState = PlatformApplication.isShowingDebugConsole();
			if(loggingState != logWindowState) {
				loggingState = logWindowState;
				sendIPCPacket(new IPCPacket1BEnableLogging(logWindowState));
			}
		}

		if(ClientPlatformSingleplayer.isRunningSingleThreadMode()) {
			ClientPlatformSingleplayer.updateSingleThreadMode();
		}

		LANServerController.updateLANServer();
	}

	private static void handleIPCPacket(IPCPacketBase ipc) {
		switch(ipc.id()) {
		case IPCPacketFFProcessKeepAlive.ID: {
			IPCPacketFFProcessKeepAlive pkt = (IPCPacketFFProcessKeepAlive)ipc;
			IntegratedServerState.assertState(pkt.ack, statusState);
			switch(pkt.ack) {
				case 0xFF:
					logger.info("Integrated server signaled a successful boot");
					sendIPCPacket(new IPCPacket14StringList(IPCPacket14StringList.LOCALE, StringTranslate.dump()));
					statusState = IntegratedServerState.WORLD_NONE;
					break;
				case IPCPacket00StartServer.ID:
					statusState = IntegratedServerState.WORLD_LOADED;
					isPaused = false;
					break;
				case IPCPacket0BPause.ID:
				case IPCPacket19Autosave.ID:
					if(statusState != IntegratedServerState.WORLD_UNLOADING) {
						statusState = isPaused ? IntegratedServerState.WORLD_PAUSED : IntegratedServerState.WORLD_LOADED;
					}
					break;
				case IPCPacketFFProcessKeepAlive.FAILURE:
					logger.error("Server signaled 'FAILURE' response in state '{}'", IntegratedServerState.getStateName(statusState));
					statusState = IntegratedServerState.WORLD_NONE;
					callFailed = true;
					break;
				case IPCPacket01StopServer.ID:
					LANServerController.closeLAN();
					localPlayerNetworkManager.isPlayerChannelOpen = false;
					statusState = IntegratedServerState.WORLD_NONE;
					break;
				case IPCPacket06RenameWorldNBT.ID:
					statusState = IntegratedServerState.WORLD_NONE;
					break;
				case IPCPacket03DeleteWorld.ID:
				case IPCPacket07ImportWorld.ID:
				case IPCPacket12FileWrite.ID:
				case IPCPacket13FileCopyMove.ID:
				case IPCPacket18ClearPlayers.ID:
					statusState = IntegratedServerState.WORLD_NONE;
					break;
				case IPCPacketFFProcessKeepAlive.EXITED:
					logger.error("Server signaled 'EXITED' response in state '{}'", IntegratedServerState.getStateName(statusState));
					if(ClientPlatformSingleplayer.canKillWorker()) {
						ClientPlatformSingleplayer.killWorker();
					}
					LANServerController.closeLAN();
					localPlayerNetworkManager.isPlayerChannelOpen = false;
					statusState = IntegratedServerState.WORLD_WORKER_NOT_RUNNING;
					callFailed = true;
					break;
				default:
					logger.error("IPC acknowledge packet type 0x{} was not handled", Integer.toHexString(pkt.ack));
					break;
			}
			break;
		}
		case IPCPacket09RequestResponse.ID: {
			IPCPacket09RequestResponse pkt = (IPCPacket09RequestResponse)ipc;
			if(statusState == IntegratedServerState.WORLD_EXPORTING) {
				statusState = IntegratedServerState.WORLD_NONE;
				exportResponse = pkt.response;
			}else {
				logger.error("IPCPacket09RequestResponse was recieved but statusState was '{}' instead of 'WORLD_EXPORTING'", IntegratedServerState.getStateName(statusState));
			}
			break;
		}
		case IPCPacket0DProgressUpdate.ID: {
			IPCPacket0DProgressUpdate pkt = (IPCPacket0DProgressUpdate)ipc;
			worldStatusString = pkt.updateMessage;
			worldStatusProgress = pkt.updateProgress;
			break;
		}
		case IPCPacket15Crashed.ID: {
			exceptions.add((IPCPacket15Crashed)ipc);
			if(exceptions.size() > 64) {
				exceptions.remove(0);
			}
			break;
		}
		case IPCPacket16NBTList.ID: {
			IPCPacket16NBTList pkt = (IPCPacket16NBTList)ipc;
			if(pkt.opCode == IPCPacket16NBTList.WORLD_LIST && statusState == IntegratedServerState.WORLD_LISTING) {
				statusState = IntegratedServerState.WORLD_NONE;
				saveListNBT.clear();
				saveListNBT.addAll(pkt.nbtTagList);
				loadSaveComparators();
			}else {
				logger.error("IPC packet type 0x{} class '{}' contained invalid opCode {} in state {} '{}'", Integer.toHexString(ipc.id()), ipc.getClass().getSimpleName(), pkt.opCode, statusState, IntegratedServerState.getStateName(statusState));
			}
			break;
		}
		case IPCPacket0CPlayerChannel.ID: {
			IPCPacket0CPlayerChannel pkt = (IPCPacket0CPlayerChannel)ipc;
			if(!pkt.open) {
				if(pkt.channel.equals(PLAYER_CHANNEL)) {
					LANServerController.closeLAN();
					localPlayerNetworkManager.isPlayerChannelOpen = false;
					logger.error("Local player channel was closed");
				}else {
					PlatformWebRTC.serverLANDisconnectPeer(pkt.channel);
				}
			}
			break;
		}
		case IPCPacket14StringList.ID: {
			IPCPacket14StringList pkt = (IPCPacket14StringList)ipc;
			if(pkt.opCode == IPCPacket14StringList.SERVER_TPS) {
				integratedServerTPS.clear();
				integratedServerTPS.addAll(pkt.stringList);
				integratedServerLastTPSUpdate = EagRuntime.steadyTimeMillis();
			}else {
				logger.warn("Strange string list type {} recieved!", pkt.opCode);
			}
			break;
		}
		case IPCPacket1ALoggerMessage.ID: {
			IPCPacket1ALoggerMessage pkt = (IPCPacket1ALoggerMessage)ipc;
			PlatformApplication.addLogMessage(pkt.logMessage, pkt.isError);
			break;
		}
		case IPCPacket1CIssueDetected.ID: {
			IPCPacket1CIssueDetected pkt = (IPCPacket1CIssueDetected)ipc;
			issuesDetected.add(pkt.issueID);
			break;
		}
		default:
			throw new RuntimeException("Unexpected IPC packet type recieved on client: " + ipc.id());
		}
	}

	public static void sendIPCPacket(IPCPacketBase ipc) {
		byte[] pkt;
		try {
			pkt = packetManagerInstance.IPCSerialize(ipc);
		}catch (IOException ex) {
			throw new RuntimeException("Failed to serialize IPC packet", ex);
		}
		ClientPlatformSingleplayer.sendPacket(new IPCPacketData(IPC_CHANNEL, pkt));
	}
	

	private static boolean callFailed = false;
	
	public static boolean didLastCallFail() {
		boolean c = callFailed;
		callFailed = false;
		return c;
	}

	public static void importWorld(String name, byte[] data, int format, byte gameRules) {
		ensureReady();
		statusState = IntegratedServerState.WORLD_IMPORTING;
		sendIPCPacket(new IPCPacket07ImportWorld(name, data, (byte)format, gameRules));
	}
	
	public static void exportWorld(String name, int format) {
		ensureReady();
		statusState = IntegratedServerState.WORLD_EXPORTING;
		if(format == IPCPacket05RequestData.REQUEST_LEVEL_EAG) {
			name = name + (new String(new char[] { (char)253, (char)233, (char)233 })) + EaglerProfile.getName();
		}
		sendIPCPacket(new IPCPacket05RequestData(name, (byte)format));
	}
	
	private static byte[] exportResponse = null;

	public static byte[] getExportResponse() {
		byte[] dat = exportResponse;
		exportResponse = null;
		return dat;
	}
	
	public static String worldStatusString() {
		return worldStatusString;
	}
	
	public static float worldStatusProgress() {
		return worldStatusProgress;
	}

	public static IPCPacket15Crashed worldStatusError() {
		return exceptions.size() > 0 ? exceptions.remove(0) : null;
	}

	public static IPCPacket15Crashed[] worldStatusErrors() {
		int l = exceptions.size();
		if(l == 0) {
			return null;
		}
		IPCPacket15Crashed[] pkts = exceptions.toArray(new IPCPacket15Crashed[l]);
		exceptions.clear();
		return pkts;
	}

	public static void clearPlayerData(String worldName) {
		ensureReady();
		statusState = IntegratedServerState.WORLD_CLEAR_PLAYERS;
		sendIPCPacket(new IPCPacket18ClearPlayers(worldName));
	}

	private static void loadSaveComparators() {
		saveListMap.clear();
		saveListCache.clear();
		for(int j = 0, l = saveListNBT.size(); j < l; ++j) {
			NBTTagCompound nbt = saveListNBT.get(j);
			String folderName = nbt.getString("folderNameEagler");
			if(!StringUtils.isEmpty(folderName)) {
				WorldInfo worldinfo = new WorldInfo(nbt.getCompoundTag("Data"));
				saveListMap.put(folderName, worldinfo);
				String s1 = worldinfo.getWorldName();
				if (StringUtils.isEmpty(s1)) {
					s1 = folderName;
				}

				long i = 0L;
				saveListCache.add(new SaveFormatComparator(folderName, s1, worldinfo.getLastTimePlayed(), i,
						worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(),
						worldinfo.areCommandsAllowed(), nbt));
			}
		}
	}

	@Override
	public String getName() {
		return "eaglercraft";
	}

	@Override
	public ISaveHandler getSaveLoader(String var1, boolean var2) {
		return new SingleplayerSaveHandler(saveListMap.get(var1));
	}

	@Override
	public List<SaveFormatComparator> getSaveList() {
		return saveListCache;
	}

	@Override
	public void flushCache() {
		sendIPCPacket(new IPCPacket0EListWorlds());
		statusState = IntegratedServerState.WORLD_LISTING;
	}

	@Override
	public WorldInfo getWorldInfo(String var1) {
		return saveListMap.get(var1);
	}

	@Override
	public boolean func_154335_d(String var1) {
		return false;
	}

	@Override
	public boolean deleteWorldDirectory(String var1) {
		sendIPCPacket(new IPCPacket03DeleteWorld(var1));
		statusState = IntegratedServerState.WORLD_DELETING;
		return false;
	}

	@Override
	public boolean renameWorld(String var1, String var2) {
		sendIPCPacket(new IPCPacket06RenameWorldNBT(var1, var2, false));
		statusState = IntegratedServerState.WORLD_RENAMING;
		return true;
	}

	public static void duplicateWorld(String var1, String var2) {
		sendIPCPacket(new IPCPacket06RenameWorldNBT(var1, var2, true));
		statusState = IntegratedServerState.WORLD_DUPLICATING;
	}

	@Override
	public boolean func_154334_a(String var1) {
		return false;
	}

	@Override
	public boolean isOldMapFormat(String var1) {
		return false;
	}

	@Override
	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		return false;
	}

	@Override
	public boolean canLoadWorld(String var1) {
		return saveListMap.containsKey(var1);
	}

	public static boolean canKillWorker() {
		return ClientPlatformSingleplayer.canKillWorker();
	}

	public static void killWorker() {
		statusState = IntegratedServerState.WORLD_WORKER_NOT_RUNNING;
		ClientPlatformSingleplayer.killWorker();
		LANServerController.closeLAN();
	}

	public static void updateLocale(List<String> dump) {
		if(statusState != IntegratedServerState.WORLD_WORKER_NOT_RUNNING) {
			sendIPCPacket(new IPCPacket14StringList(IPCPacket14StringList.LOCALE, dump));
		}
	}

	public static void setDifficulty(int difficultyId) {
		if(isWorldRunning()) {
			sendIPCPacket(new IPCPacket0ASetWorldDifficulty((byte)difficultyId));
		}
	}

	public static void configureLAN(net.minecraft.world.WorldSettings.GameType enumGameType, boolean allowCommands) {
		sendIPCPacket(new IPCPacket17ConfigureLAN(enumGameType.getID(), allowCommands, LANServerController.currentICEServers));
	}

	public static boolean isClientInEaglerSingleplayerOrLAN() {
		Minecraft mc = Minecraft.getMinecraft();
		return mc != null && mc.thePlayer != null && mc.thePlayer.sendQueue.isClientInEaglerSingleplayerOrLAN();
	}
}

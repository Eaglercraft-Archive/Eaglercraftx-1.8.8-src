package net.lax1dude.eaglercraft.v1_8.sp;

import net.lax1dude.eaglercraft.v1_8.sp.ipc.*;

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
public class IntegratedServerState {

	public static final int WORLD_WORKER_NOT_RUNNING = -2;
	public static final int WORLD_WORKER_BOOTING = -1;
	public static final int WORLD_NONE = 0;
	public static final int WORLD_LOADING = 2;
	public static final int WORLD_LOADED = 3;
	public static final int WORLD_UNLOADING = 4;
	public static final int WORLD_DELETING = 5;
	public static final int WORLD_RENAMING = 6;
	public static final int WORLD_DUPLICATING = 7;
	public static final int WORLD_PAUSED = 9;
	public static final int WORLD_LISTING = 10;
	public static final int WORLD_SAVING = 11;
	public static final int WORLD_IMPORTING = 12;
	public static final int WORLD_EXPORTING = 13;
	public static final int WORLD_GET_NBT = 14;

	public static final int WORLD_LIST_FILE = 15;
	public static final int WORLD_FILE_READ = 16;
	public static final int WORLD_FILE_WRITE = 17;
	public static final int WORLD_FILE_MOVE = 18;
	public static final int WORLD_FILE_COPY = 19;
	public static final int WORLD_CLEAR_PLAYERS = 20;
	
	public static String getStateName(int i) {
		switch(i) {
		case WORLD_WORKER_NOT_RUNNING: return "WORLD_WORKER_NOT_RUNNING";
		case WORLD_WORKER_BOOTING: return "WORLD_WORKER_BOOTING";
		case WORLD_NONE: return "WORLD_NONE";
		case WORLD_LOADING: return "WORLD_LOADING";
		case WORLD_LOADED: return "WORLD_LOADED";
		case WORLD_UNLOADING: return "WORLD_UNLOADING";
		case WORLD_DELETING: return "WORLD_DELETING";
		case WORLD_RENAMING: return "WORLD_RENAMING";
		case WORLD_DUPLICATING: return "WORLD_DUPLICATING";
		case WORLD_PAUSED: return "WORLD_PAUSED";
		case WORLD_LISTING: return "WORLD_LISTING";
		case WORLD_SAVING: return "WORLD_SAVING";
		case WORLD_IMPORTING: return "WORLD_IMPORTING";
		case WORLD_EXPORTING: return "WORLD_EXPORTING";
		case WORLD_GET_NBT: return "WORLD_GET_NBT";
		case WORLD_LIST_FILE: return "WORLD_LIST_FILE";
		case WORLD_FILE_READ: return "WORLD_FILE_READ";
		case WORLD_FILE_WRITE: return "WORLD_FILE_WRITE";
		case WORLD_FILE_MOVE: return "WORLD_FILE_MOVE";
		case WORLD_FILE_COPY: return "WORLD_FILE_COPY";
		case WORLD_CLEAR_PLAYERS: return "WORLD_CLEAR_PLAYERS";
		default: return "INVALID";
		}
	}
	
	public static boolean isACKValidInState(int ack, int state) {
		switch(ack) {
		case 0xFF: return state == WORLD_WORKER_BOOTING;
		case IPCPacketFFProcessKeepAlive.EXITED: return true;
		case IPCPacketFFProcessKeepAlive.FAILURE: return true;
		case IPCPacket01StopServer.ID: return true;
		case IPCPacket00StartServer.ID: return state == WORLD_LOADING;
		case IPCPacket03DeleteWorld.ID: return state == WORLD_DELETING;
		case IPCPacket06RenameWorldNBT.ID: return (state == WORLD_DUPLICATING || state == WORLD_RENAMING);
		case IPCPacket07ImportWorld.ID: return state == WORLD_IMPORTING;
		case IPCPacket0BPause.ID:
		case IPCPacket19Autosave.ID: return (state == WORLD_SAVING || state == WORLD_PAUSED || state == WORLD_LOADED || state == WORLD_UNLOADING);
		case IPCPacket12FileWrite.ID: return state == WORLD_FILE_WRITE;
		case IPCPacket13FileCopyMove.ID: return (state == WORLD_FILE_MOVE || state == WORLD_FILE_COPY);
		case IPCPacket18ClearPlayers.ID: return state == WORLD_CLEAR_PLAYERS;
		default: return false;
		}
	}
	
	public static void assertState(int ack, int state) {
		if(!isACKValidInState(ack, state)) {
			String msg = "Recieved ACK " + ack + " while the client state was " + state + " '" + getStateName(state) + "'";
			throw new IllegalStateException(msg);
		}
	}

}

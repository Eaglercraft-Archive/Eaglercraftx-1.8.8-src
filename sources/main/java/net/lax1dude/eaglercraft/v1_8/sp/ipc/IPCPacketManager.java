package net.lax1dude.eaglercraft.v1_8.sp.ipc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Copyright (c) 2023-2024 lax1dude. All Rights Reserved.
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
public class IPCPacketManager {
	
	public static final HashMap<Integer, Supplier<IPCPacketBase>> mappings = new HashMap();

	public static final IPCInputStream IPC_INPUT_STREAM = new IPCInputStream();
	public static final IPCOutputStream IPC_OUTPUT_STREAM = new IPCOutputStream();

	public static final DataInputStream IPC_DATA_INPUT_STREAM = new DataInputStream(IPC_INPUT_STREAM);
	public static final DataOutputStream IPC_DATA_OUTPUT_STREAM = new DataOutputStream(IPC_OUTPUT_STREAM);
	
	static {
		mappings.put(IPCPacket00StartServer.ID, () -> new IPCPacket00StartServer());
		mappings.put(IPCPacket01StopServer.ID, () -> new IPCPacket01StopServer());
		mappings.put(IPCPacket02InitWorld.ID, () -> new IPCPacket02InitWorld());
		mappings.put(IPCPacket03DeleteWorld.ID, () -> new IPCPacket03DeleteWorld());
		mappings.put(IPCPacket05RequestData.ID, () -> new IPCPacket05RequestData());
		mappings.put(IPCPacket06RenameWorldNBT.ID, () -> new IPCPacket06RenameWorldNBT());
		mappings.put(IPCPacket07ImportWorld.ID, () -> new IPCPacket07ImportWorld());
		mappings.put(IPCPacket09RequestResponse.ID, () -> new IPCPacket09RequestResponse());
		mappings.put(IPCPacket0ASetWorldDifficulty.ID, () -> new IPCPacket0ASetWorldDifficulty());
		mappings.put(IPCPacket0BPause.ID, () -> new IPCPacket0BPause());
		mappings.put(IPCPacket0CPlayerChannel.ID, () -> new IPCPacket0CPlayerChannel());
		mappings.put(IPCPacket0DProgressUpdate.ID, () -> new IPCPacket0DProgressUpdate());
		mappings.put(IPCPacket0EListWorlds.ID, () -> new IPCPacket0EListWorlds());
		mappings.put(IPCPacket0FListFiles.ID, () -> new IPCPacket0FListFiles());
		mappings.put(IPCPacket10FileRead.ID, () -> new IPCPacket10FileRead());
		mappings.put(IPCPacket12FileWrite.ID, () -> new IPCPacket12FileWrite());
		mappings.put(IPCPacket13FileCopyMove.ID, () -> new IPCPacket13FileCopyMove());
		mappings.put(IPCPacket14StringList.ID, () -> new IPCPacket14StringList());
		mappings.put(IPCPacket15Crashed.ID, () -> new IPCPacket15Crashed());
		mappings.put(IPCPacket16NBTList.ID, () -> new IPCPacket16NBTList());
		mappings.put(IPCPacket17ConfigureLAN.ID, () -> new IPCPacket17ConfigureLAN());
		mappings.put(IPCPacket18ClearPlayers.ID, () -> new IPCPacket18ClearPlayers());
		mappings.put(IPCPacket19Autosave.ID, () -> new IPCPacket19Autosave());
		mappings.put(IPCPacket20LoggerMessage.ID, () -> new IPCPacket20LoggerMessage());
		mappings.put(IPCPacket21EnableLogging.ID, () -> new IPCPacket21EnableLogging());
		mappings.put(IPCPacketFFProcessKeepAlive.ID, () -> new IPCPacketFFProcessKeepAlive());
	}
	
	public static byte[] IPCSerialize(IPCPacketBase pkt) throws IOException {
		
		IPC_OUTPUT_STREAM.feedBuffer(new byte[pkt.size() + 1], pkt.getClass().getSimpleName());
		IPC_OUTPUT_STREAM.write(pkt.id());
		pkt.serialize(IPC_DATA_OUTPUT_STREAM);
		
		return IPC_OUTPUT_STREAM.returnBuffer();
	}
	
	public static IPCPacketBase IPCDeserialize(byte[] pkt) throws IOException {
		
		IPC_INPUT_STREAM.feedBuffer(pkt);
		int i = IPC_INPUT_STREAM.read();
		
		Supplier<IPCPacketBase> pk = mappings.get(Integer.valueOf(i));
		if(pk == null) {
			throw new IOException("Packet type 0x" + Integer.toHexString(i) + " doesn't exist");
		}
		
		IPCPacketBase p = pk.get();
		
		IPC_INPUT_STREAM.nameBuffer(p.getClass().getSimpleName());
		
		p.deserialize(IPC_DATA_INPUT_STREAM);
		
		int lo = IPC_INPUT_STREAM.getLeftoverCount();
		if(lo > 0) {
			System.err.println("Packet type 0x" + Integer.toHexString(i) + " class '" + p.getClass().getSimpleName() + "' was size " + (pkt.length - 1) + " but only " + (pkt.length - 1 - lo) + " bytes were read");
		}
		
		return p;
	}
	
}

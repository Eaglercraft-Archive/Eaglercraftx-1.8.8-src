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
	
	public static final HashMap<Integer, Supplier<IPCPacketBase>> mappings = new HashMap<>();

	public final IPCInputStream IPC_INPUT_STREAM = new IPCInputStream();
	public final IPCOutputStream IPC_OUTPUT_STREAM = new IPCOutputStream();

	public final DataInputStream IPC_DATA_INPUT_STREAM = new DataInputStream(IPC_INPUT_STREAM);
	public final DataOutputStream IPC_DATA_OUTPUT_STREAM = new DataOutputStream(IPC_OUTPUT_STREAM);
	
	static {
		mappings.put(IPCPacket00StartServer.ID, IPCPacket00StartServer::new);
		mappings.put(IPCPacket01StopServer.ID, IPCPacket01StopServer::new);
		mappings.put(IPCPacket02InitWorld.ID, IPCPacket02InitWorld::new);
		mappings.put(IPCPacket03DeleteWorld.ID, IPCPacket03DeleteWorld::new);
		mappings.put(IPCPacket05RequestData.ID, IPCPacket05RequestData::new);
		mappings.put(IPCPacket06RenameWorldNBT.ID, IPCPacket06RenameWorldNBT::new);
		mappings.put(IPCPacket07ImportWorld.ID, IPCPacket07ImportWorld::new);
		mappings.put(IPCPacket09RequestResponse.ID, IPCPacket09RequestResponse::new);
		mappings.put(IPCPacket0ASetWorldDifficulty.ID, IPCPacket0ASetWorldDifficulty::new);
		mappings.put(IPCPacket0BPause.ID, IPCPacket0BPause::new);
		mappings.put(IPCPacket0CPlayerChannel.ID, IPCPacket0CPlayerChannel::new);
		mappings.put(IPCPacket0DProgressUpdate.ID, IPCPacket0DProgressUpdate::new);
		mappings.put(IPCPacket0EListWorlds.ID, IPCPacket0EListWorlds::new);
		mappings.put(IPCPacket0FListFiles.ID, IPCPacket0FListFiles::new);
		mappings.put(IPCPacket10FileRead.ID, IPCPacket10FileRead::new);
		mappings.put(IPCPacket12FileWrite.ID, IPCPacket12FileWrite::new);
		mappings.put(IPCPacket13FileCopyMove.ID, IPCPacket13FileCopyMove::new);
		mappings.put(IPCPacket14StringList.ID, IPCPacket14StringList::new);
		mappings.put(IPCPacket15Crashed.ID, IPCPacket15Crashed::new);
		mappings.put(IPCPacket16NBTList.ID, IPCPacket16NBTList::new);
		mappings.put(IPCPacket17ConfigureLAN.ID, IPCPacket17ConfigureLAN::new);
		mappings.put(IPCPacket18ClearPlayers.ID, IPCPacket18ClearPlayers::new);
		mappings.put(IPCPacket19Autosave.ID, IPCPacket19Autosave::new);
		mappings.put(IPCPacket1ALoggerMessage.ID, IPCPacket1ALoggerMessage::new);
		mappings.put(IPCPacket1BEnableLogging.ID, IPCPacket1BEnableLogging::new);
		mappings.put(IPCPacket1CIssueDetected.ID, IPCPacket1CIssueDetected::new);
		mappings.put(IPCPacketFFProcessKeepAlive.ID, IPCPacketFFProcessKeepAlive::new);
	}
	
	public byte[] IPCSerialize(IPCPacketBase pkt) throws IOException {
		
		IPC_OUTPUT_STREAM.feedBuffer(new byte[pkt.size() + 1], pkt.getClass().getSimpleName());
		IPC_OUTPUT_STREAM.write(pkt.id());
		pkt.serialize(IPC_DATA_OUTPUT_STREAM);
		
		return IPC_OUTPUT_STREAM.returnBuffer();
	}
	
	public IPCPacketBase IPCDeserialize(byte[] pkt) throws IOException {
		
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

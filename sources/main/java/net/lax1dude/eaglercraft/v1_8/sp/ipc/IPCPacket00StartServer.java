package net.lax1dude.eaglercraft.v1_8.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

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
public class IPCPacket00StartServer implements IPCPacketBase {
	
	public static final int ID = 0x00;

	public String worldName;
	public String ownerName;
	public int initialDifficulty;
	public int initialViewDistance;
	public boolean demoMode;
	
	public IPCPacket00StartServer() {
	}
	
	public IPCPacket00StartServer(String worldName, String ownerName, int initialDifficulty, int initialViewDistance, boolean demoMode) {
		this.worldName = worldName;
		this.ownerName = ownerName;
		this.initialDifficulty = initialDifficulty;
		this.initialViewDistance = initialViewDistance;
		this.demoMode = demoMode;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		ownerName = bin.readUTF();
		initialDifficulty = bin.readByte();
		initialViewDistance = bin.readByte();
		demoMode = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeUTF(ownerName);
		bin.writeByte(initialDifficulty);
		bin.writeByte(initialViewDistance);
		bin.writeBoolean(demoMode);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + IPCPacketBase.strLen(ownerName) + 3;
	}

}

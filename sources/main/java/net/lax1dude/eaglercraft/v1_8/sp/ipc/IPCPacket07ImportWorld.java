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
public class IPCPacket07ImportWorld implements IPCPacketBase {
	
	public static final int ID = 0x07;
	public byte gameRules;

	public String worldName;
	public byte[] worldData;
	public byte worldFormat;
	
	public static final byte WORLD_FORMAT_EAG = 0x00;
	public static final byte WORLD_FORMAT_MCA = 0x01;
	
	public IPCPacket07ImportWorld() {
	}
	
	public IPCPacket07ImportWorld(String worldName, byte[] worldData, byte worldFormat, byte gameRules) {
		this.worldName = worldName;
		this.worldData = worldData;
		this.worldFormat = worldFormat;
		this.gameRules = gameRules;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		worldData = new byte[bin.readInt()];
		worldFormat = bin.readByte();
		gameRules = bin.readByte();
		bin.readFully(worldData);
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeInt(worldData.length);
		bin.writeByte(worldFormat);
		bin.writeByte(gameRules);
		bin.write(worldData);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + worldData.length + 6;
	}

}

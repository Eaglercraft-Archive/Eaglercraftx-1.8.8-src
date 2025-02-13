/*
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

package net.lax1dude.eaglercraft.v1_8.sp.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IPCPacket02InitWorld implements IPCPacketBase {
	
	public static final int ID = 0x02;

	public String worldName;
	public byte gamemode;
	public byte worldType;
	public String worldArgs;
	public long seed;
	public boolean cheats;
	public boolean structures;
	public boolean bonusChest;
	public boolean hardcore;
	
	public IPCPacket02InitWorld() {
	}
	
	public IPCPacket02InitWorld(String worldName, int gamemode, int worldType, String worldArgs, long seed, boolean cheats, boolean structures, boolean bonusChest, boolean hardcore) {
		this.worldName = worldName;
		this.gamemode = (byte)gamemode;
		this.worldType = (byte)worldType;
		this.worldArgs = worldArgs;
		this.seed = seed;
		this.cheats = cheats;
		this.structures = structures;
		this.bonusChest = bonusChest;
		this.hardcore = hardcore;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		gamemode = bin.readByte();
		worldType = bin.readByte();
		worldArgs = bin.readUTF();
		seed = bin.readLong();
		cheats = bin.readBoolean();
		structures = bin.readBoolean();
		bonusChest = bin.readBoolean();
		hardcore = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeByte(gamemode);
		bin.writeByte(worldType);
		bin.writeUTF(worldArgs);
		bin.writeLong(seed);
		bin.writeBoolean(cheats);
		bin.writeBoolean(structures);
		bin.writeBoolean(bonusChest);
		bin.writeBoolean(hardcore);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + 1 + 1 + IPCPacketBase.strLen(worldArgs) + 8 + 4;
	}

}
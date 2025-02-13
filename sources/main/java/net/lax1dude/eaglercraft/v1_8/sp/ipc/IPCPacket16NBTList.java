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
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class IPCPacket16NBTList implements IPCPacketBase {

	public static final int ID = 0x16;
	
	public static final int WORLD_LIST = 0x0;

	public int opCode;
	public final List<byte[]> tagList;
	public final List<NBTTagCompound> nbtTagList;
	
	public IPCPacket16NBTList() {
		tagList = new LinkedList<>();
		nbtTagList = new LinkedList<>();
	}
	
	public IPCPacket16NBTList(int opcode, NBTTagCompound[] list) {
		this(opcode, Arrays.asList(list));
	}
	
	public IPCPacket16NBTList(int opcode, List<NBTTagCompound> list) {
		tagList = new LinkedList<>();
		nbtTagList = list;
		for(int i = 0, size = list.size(); i < size; ++i) {
			NBTTagCompound tag = list.get(i);
			try {
				EaglerOutputStream bao = new EaglerOutputStream();
				CompressedStreamTools.write(tag, new DataOutputStream(bao));
				tagList.add(bao.toByteArray());
			}catch(IOException e) {
				System.err.println("Failed to write tag '" + tag.getId() + "' (#" + i + ") in IPCPacket16NBTList");
			}
		}
		opCode = opcode;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		tagList.clear();
		nbtTagList.clear();
		opCode = bin.readInt();
		int count = bin.readInt();
		for(int i = 0; i < count; ++i) {
			byte[] toRead = new byte[bin.readInt()];
			bin.readFully(toRead);
			tagList.add(toRead);
			try {
				nbtTagList.add(CompressedStreamTools.read(new DataInputStream(new EaglerInputStream(toRead))));
			}catch(IOException e) {
				System.err.println("Failed to read tag #" + i + " in IPCPacket16NBTList");
			}
		}
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeInt(opCode);
		int l = tagList.size();
		bin.writeInt(l);
		for(int i = 0; i < l; ++i) {
			byte[] str = tagList.get(i);
			bin.writeInt(str.length);
			bin.write(str);
		}
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		int len = 8;
		for(int i = 0, l = tagList.size(); i < l; ++i) {
			len += 4;
			len += tagList.get(i).length;
		}
		return len;
	}

}
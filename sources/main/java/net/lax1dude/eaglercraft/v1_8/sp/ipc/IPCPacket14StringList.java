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
import java.util.ArrayList;
import java.util.List;

public class IPCPacket14StringList implements IPCPacketBase {
	
	public static final int ID = 0x14;

	public static final int FILE_LIST = 0x0;
	public static final int LOCALE = 0x1;
	public static final int STAT_GUID = 0x2;
	public static final int SERVER_TPS = 0x3;

	public int opCode;
	public final List<String> stringList;
	
	public IPCPacket14StringList() {
		stringList = new ArrayList<>();
	}
	
	public IPCPacket14StringList(int opcode, String[] list) {
		stringList = new ArrayList<>(list.length);
		for(int i = 0; i < list.length; ++i) {
			String s = list[i].trim();
			if(s.length() > 0) {
				stringList.add(s);
			}
		}
		this.opCode = opcode;
	}
	
	public IPCPacket14StringList(int opcode, List<String> list) {
		stringList = new ArrayList<>(list.size());
		for(int i = 0, l = list.size(); i < l; ++i) {
			String s = list.get(i).trim();
			if(s.length() > 0) {
				stringList.add(s);
			}
		}
		this.opCode = opcode;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		stringList.clear();
		opCode = bin.readByte();
		int len = bin.readInt();
		for(int i = 0; i < len; ++i) {
			stringList.add(bin.readUTF());
		}
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeByte(opCode);
		int l = stringList.size();
		bin.writeInt(l);
		for(int i = 0; i < l; ++i) {
			bin.writeUTF(stringList.get(i));
		}
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		int len = 5;
		for(int i = 0, l = stringList.size(); i < l; ++i) {
			len += IPCPacketBase.strLen(stringList.get(i));
		}
		return len;
	}

}
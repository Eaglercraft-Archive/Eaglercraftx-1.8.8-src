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

public class IPCPacket05RequestData implements IPCPacketBase {
	
	public static final int ID = 0x05;

	public static final byte REQUEST_LEVEL_DAT = 0x00;
	public static final byte REQUEST_LEVEL_EAG = 0x01;
	public static final byte REQUEST_LEVEL_MCA = 0x02;

	public String worldName;
	public byte request;
	
	public IPCPacket05RequestData() {
	}
	
	public IPCPacket05RequestData(String worldName, byte request) {
		this.worldName = worldName;
		this.request = request;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		worldName = bin.readUTF();
		request = bin.readByte();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(worldName);
		bin.writeByte(request);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(worldName) + 1;
	}

}
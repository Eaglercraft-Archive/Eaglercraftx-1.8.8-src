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

public class IPCPacket13FileCopyMove implements IPCPacketBase {
	
	public static final int ID = 0x13;

	public String fileOldName;
	public String fileNewName;
	public boolean copy;
	
	public IPCPacket13FileCopyMove() {
	}
	
	public IPCPacket13FileCopyMove(String fileOldName, String fileNewName, boolean copy) {
		this.fileOldName = fileOldName;
		this.fileNewName = fileNewName;
		this.copy = copy;
	}

	@Override
	public void deserialize(DataInput bin) throws IOException {
		fileOldName = bin.readUTF();
		fileNewName = bin.readUTF();
		copy = bin.readBoolean();
	}

	@Override
	public void serialize(DataOutput bin) throws IOException {
		bin.writeUTF(fileOldName);
		bin.writeUTF(fileNewName);
		bin.writeBoolean(copy);
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int size() {
		return IPCPacketBase.strLen(fileOldName) + IPCPacketBase.strLen(fileNewName) + 1;
	}

}
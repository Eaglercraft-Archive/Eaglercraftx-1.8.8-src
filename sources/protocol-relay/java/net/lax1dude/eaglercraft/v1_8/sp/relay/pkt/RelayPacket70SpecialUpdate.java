/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RelayPacket70SpecialUpdate extends RelayPacket {

	public static final int OPERATION_UPDATE_CERTIFICATE = 0x69;

	public int operation;
	public byte[] updatePacket;

	public RelayPacket70SpecialUpdate() {
	}

	public RelayPacket70SpecialUpdate(int operation, byte[] updatePacket) {
		this.operation = operation;
		this.updatePacket = updatePacket;
	}

	@Override
	public void read(DataInputStream input) throws IOException {
		operation = input.read();
		updatePacket = new byte[input.readUnsignedShort()];
		input.read(updatePacket);
	}

	@Override
	public void write(DataOutputStream output) throws IOException {
		output.write(operation);
		output.writeShort(updatePacket.length);
		output.write(updatePacket);
	}

	@Override
	public int packetLength() {
		return 3 + updatePacket.length;
	}
}
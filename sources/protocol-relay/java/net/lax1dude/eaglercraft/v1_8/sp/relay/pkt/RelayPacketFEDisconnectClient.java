/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
import java.nio.ByteBuffer;

public class RelayPacketFEDisconnectClient extends RelayPacket {

	public static final int TYPE_FINISHED_SUCCESS = 0x00;
	public static final int TYPE_FINISHED_FAILED = 0x01;
	public static final int TYPE_TIMEOUT = 0x02;
	public static final int TYPE_INVALID_OPERATION = 0x03;
	public static final int TYPE_INTERNAL_ERROR = 0x04;
	public static final int TYPE_SERVER_DISCONNECT = 0x05;
	public static final int TYPE_UNKNOWN = 0xFF;
	
	public String clientId;
	public int code;
	public String reason;
	
	public RelayPacketFEDisconnectClient() {
	}
	
	public RelayPacketFEDisconnectClient(String clientId, int code, String reason) {
		this.clientId = clientId;
		this.code = code;
		this.reason = reason;
	}
	
	public void read(DataInputStream input) throws IOException {
		clientId = readASCII8(input);
		code = input.read();
		reason = readASCII16(input);
	}
	
	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, clientId);
		output.write(code);
		writeASCII16(output, reason);
	}
	
	public int packetLength() {
		return 1 + 1 + 2 + clientId.length() + (reason != null ? reason.length() : 0);
	}
	
	public static final ByteBuffer ratelimitPacketTooMany = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x00 });
	public static final ByteBuffer ratelimitPacketBlock = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x01 });
	public static final ByteBuffer ratelimitPacketBlockLock = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x02 });
	public static final ByteBuffer ratelimitPacketLocked = ByteBuffer.wrap(new byte[] { (byte)0xFC, (byte)0x03 });
	
}
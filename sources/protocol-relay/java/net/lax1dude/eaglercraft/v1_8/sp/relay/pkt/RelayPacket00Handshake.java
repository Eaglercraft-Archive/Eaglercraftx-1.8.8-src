package net.lax1dude.eaglercraft.v1_8.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
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
public class RelayPacket00Handshake extends RelayPacket {

	public int connectionType = 0;
	public int connectionVersion = 1;
	public String connectionCode = null;
	
	public RelayPacket00Handshake() {
	}
	
	public RelayPacket00Handshake(int connectionType, int connectionVersion,
			String connectionCode) {
		this.connectionType = connectionType;
		this.connectionVersion = connectionVersion;
		this.connectionCode = connectionCode;
	}
	
	@Override
	public void read(DataInputStream input) throws IOException {
		connectionType = input.read();
		connectionVersion = input.read();
		connectionCode = RelayPacket.readASCII8(input);
	}
	
	@Override
	public void write(DataOutputStream output) throws IOException {
		output.write(connectionType);
		output.write(connectionVersion);
		RelayPacket.writeASCII8(output, connectionCode);
	}
	
	@Override
	public int packetLength() {
		return 1 + 1 + (connectionCode != null ? 1 + connectionCode.length() : 0);
	}

}

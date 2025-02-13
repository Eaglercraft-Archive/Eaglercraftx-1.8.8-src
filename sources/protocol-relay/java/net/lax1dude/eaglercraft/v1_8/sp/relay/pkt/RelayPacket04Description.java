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

public class RelayPacket04Description extends RelayPacket {

	public String peerId;
	public byte[] description;
	
	public RelayPacket04Description() {
	}
	
	public RelayPacket04Description(String peerId, String desc) {
		this.peerId = peerId;
		this.description = toASCIIBin(desc);
	}
	
	public RelayPacket04Description(String peerId, byte[] desc) {
		this.peerId = peerId;
		this.description = desc;
	}
	
	public void read(DataInputStream input) throws IOException {
		peerId = readASCII8(input);
		description = readBytes16(input);
	}
	
	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, peerId);
		writeBytes16(output, description);
	}
	
	public String getDescriptionString() {
		return description == null ? null : toASCIIStr(description);
	}
	
	public int packetLength() {
		return 1 + peerId.length() + 2 + description.length;
	}

}
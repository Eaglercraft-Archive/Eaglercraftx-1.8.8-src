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
public class RelayPacket03ICECandidate extends RelayPacket {

	public String peerId;
	public byte[] candidate;
	
	public RelayPacket03ICECandidate() {
	}
	
	public RelayPacket03ICECandidate(String peerId, String desc) {
		this.peerId = peerId;
		this.candidate = toASCIIBin(desc);
	}
	
	public RelayPacket03ICECandidate(String peerId, byte[] desc) {
		this.peerId = peerId;
		this.candidate = desc;
	}
	
	public void read(DataInputStream input) throws IOException {
		peerId = readASCII8(input);
		candidate = readBytes16(input);
	}
	
	public void write(DataOutputStream output) throws IOException {
		writeASCII8(output, peerId);
		writeBytes16(output, candidate);
	}
	
	public String getCandidateString() {
		return candidate == null ? null : toASCIIStr(candidate);
	}
	
	public int packetLength() {
		return 1 + peerId.length() + 2 + candidate.length;
	}

}
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
public class RelayPacket69Pong extends RelayPacket {

	public int protcolVersion;
	public String comment;
	public String brand;
	
	public RelayPacket69Pong(int protcolVersion, String comment, String brand) {
		if(comment.length() > 255) {
			comment = comment.substring(0, 256);
		}
		this.protcolVersion = protcolVersion;
		this.comment = comment;
		this.brand = brand;
	}

	public RelayPacket69Pong() {
	}
	
	public void write(DataOutputStream output) throws IOException {
		output.write(protcolVersion);
		writeASCII8(output, comment);
		writeASCII8(output, brand);
	}
	
	public void read(DataInputStream output) throws IOException {
		protcolVersion = output.read();
		comment = readASCII8(output);
		brand = readASCII8(output);
	}
	
	public int packetLength() {
		return 3 + comment.length() + brand.length();
	}
	
}

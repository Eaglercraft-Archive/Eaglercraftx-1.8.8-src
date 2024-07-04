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
public class RelayPacketFFErrorCode extends RelayPacket {

	public static final int TYPE_INTERNAL_ERROR = 0x00;
	public static final int TYPE_PROTOCOL_VERSION = 0x01;
	public static final int TYPE_INVALID_PACKET = 0x02;
	public static final int TYPE_ILLEGAL_OPERATION = 0x03;
	public static final int TYPE_CODE_LENGTH = 0x04;
	public static final int TYPE_INCORRECT_CODE = 0x05;
	public static final int TYPE_SERVER_DISCONNECTED = 0x06;
	public static final int TYPE_UNKNOWN_CLIENT = 0x07;

	public static final String[] packetTypes = new String[0x08];

	static {
		packetTypes[TYPE_INTERNAL_ERROR] = "TYPE_INTERNAL_ERROR";
		packetTypes[TYPE_PROTOCOL_VERSION] = "TYPE_PROTOCOL_VERSION";
		packetTypes[TYPE_INVALID_PACKET] = "TYPE_INVALID_PACKET";
		packetTypes[TYPE_ILLEGAL_OPERATION] = "TYPE_ILLEGAL_OPERATION";
		packetTypes[TYPE_CODE_LENGTH] = "TYPE_CODE_LENGTH";
		packetTypes[TYPE_INCORRECT_CODE] = "TYPE_INCORRECT_CODE";
		packetTypes[TYPE_SERVER_DISCONNECTED] = "TYPE_SERVER_DISCONNECTED";
		packetTypes[TYPE_UNKNOWN_CLIENT] = "TYPE_UNKNOWN_CLIENT";
	}

	public static String code2string(int i) {
		if(i >= 0 || i < packetTypes.length) {
			return packetTypes[i];
		}else {
			return "UNKNOWN";
		}
	}

	public int code;
	public String desc;

	public RelayPacketFFErrorCode() {
	}

	public RelayPacketFFErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public void read(DataInputStream input) throws IOException {
		code = input.read();
		desc = readASCII16(input);
	}

	@Override
	public void write(DataOutputStream input) throws IOException {
		input.write(code);
		writeASCII16(input, desc);
	}

	@Override
	public int packetLength() {
		return 1 + 2 + desc.length();
	}

}

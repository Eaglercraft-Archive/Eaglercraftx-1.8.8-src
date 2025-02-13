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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RelayPacket {

	private static final Map<Integer,Class<? extends RelayPacket>> definedPacketClasses = new HashMap<>();
	private static final Map<Integer,Supplier<? extends RelayPacket>> definedPacketCtors = new HashMap<>();
	private static final Map<Class<? extends RelayPacket>,Integer> definedPacketIds = new HashMap<>();

	private static void register(int id, Class<? extends RelayPacket> clazz, Supplier<? extends RelayPacket> ctor) {
		definedPacketClasses.put(id, clazz);
		definedPacketCtors.put(id, ctor);
		definedPacketIds.put(clazz, id);
	}

	static {
		register(0x00, RelayPacket00Handshake.class, RelayPacket00Handshake::new);
		register(0x01, RelayPacket01ICEServers.class, RelayPacket01ICEServers::new);
		register(0x02, RelayPacket02NewClient.class, RelayPacket02NewClient::new);
		register(0x03, RelayPacket03ICECandidate.class, RelayPacket03ICECandidate::new);
		register(0x04, RelayPacket04Description.class, RelayPacket04Description::new);
		register(0x05, RelayPacket05ClientSuccess.class, RelayPacket05ClientSuccess::new);
		register(0x06, RelayPacket06ClientFailure.class, RelayPacket06ClientFailure::new);
		register(0x07, RelayPacket07LocalWorlds.class, RelayPacket07LocalWorlds::new);
		register(0x69, RelayPacket69Pong.class, RelayPacket69Pong::new);
		register(0x70, RelayPacket70SpecialUpdate.class, RelayPacket70SpecialUpdate::new);
		register(0xFE, RelayPacketFEDisconnectClient.class, RelayPacketFEDisconnectClient::new);
		register(0xFF, RelayPacketFFErrorCode.class, RelayPacketFFErrorCode::new);
	}

	public static RelayPacket readPacket(DataInputStream input, IRelayLogger logger) throws IOException {
		int i = input.read();
		Supplier<? extends RelayPacket> ctor = definedPacketCtors.get(i);
		if(ctor == null) {
			throw new IOException("Unknown packet type: " + i);
		}
		RelayPacket pkt = ctor.get();
		pkt.read(input);
		int j = input.available();
		if(j > 0) {
			throw new IOException("Packet type " + i + " had " + j + " remaining bytes");
		}
		return pkt;
	}

	public static byte[] writePacket(RelayPacket packet, IRelayLogger logger) throws IOException {
		Integer i = definedPacketIds.get(packet.getClass());
		if(i != null) {
			int len = packet.packetLength();
			ByteArrayOutputStream bao = len == -1 ? new ByteArrayOutputStream() :
				new ByteArrayOutputStream(len + 1);
			bao.write(i);
			packet.write(new DataOutputStream(bao));
			byte[] ret = bao.toByteArray();
			if(len != -1 && ret.length != len + 1) {
				logger.debug("writePacket buffer for packet {} {} by {} bytes", packet.getClass().getSimpleName(),
						len + 1 < ret.length ? "overflowed" : "underflowed",
						len + 1 < ret.length ? ret.length - len - 1 : len + 1 - ret.length);
			}
			return ret;
		}else {
			throw new IOException("Unknown packet type: " + packet.getClass().getSimpleName());
		}
	}

	public void read(DataInputStream input) throws IOException {
	}

	public void write(DataOutputStream output) throws IOException {
	}

	public int packetLength() {
		return -1;
	}

	public static String readASCII(InputStream is, int len) throws IOException {
		char[] ret = new char[len];
		for(int i = 0; i < len; ++i) {
			int j = is.read();
			if(j < 0) {
				throw new EOFException();
			}
			ret[i] = (char)j;
		}
		return new String(ret);
	}

	public static void writeASCII(OutputStream is, String txt) throws IOException {
		for(int i = 0, l = txt.length(); i < l; ++i) {
			is.write((int)txt.charAt(i));
		}
	}

	public static String readASCII8(InputStream is) throws IOException {
		int i = is.read();
		if(i < 0) {
			throw new EOFException();
		}else {
			return readASCII(is, i);
		}
	}

	public static void writeASCII8(OutputStream is, String txt) throws IOException {
		if(txt == null) {
			is.write(0);
		}else {
			int l = txt.length();
			is.write(l);
			for(int i = 0; i < l; ++i) {
				is.write((int)txt.charAt(i));
			}
		}
	}

	public static String readASCII16(InputStream is) throws IOException {
		int hi = is.read();
		int lo = is.read();
		if(hi < 0 || lo < 0) {
			throw new EOFException();
		}else {
			return readASCII(is, (hi << 8) | lo);
		}
	}

	public static void writeASCII16(OutputStream is, String txt) throws IOException {
		if(txt == null) {
			is.write(0);
			is.write(0);
		}else {
			int l = txt.length();
			is.write((l >>> 8) & 0xFF);
			is.write(l & 0xFF);
			for(int i = 0; i < l; ++i) {
				is.write((int)txt.charAt(i));
			}
		}
	}

	public static byte[] readBytes16(InputStream is) throws IOException {
		int hi = is.read();
		int lo = is.read();
		if(hi < 0 || lo < 0) {
			throw new EOFException();
		}else {
			byte[] ret = new byte[(hi << 8) | lo];
			is.read(ret);
			return ret;
		}
	}

	public static void writeBytes16(OutputStream is, byte[] arr) throws IOException {
		if(arr == null) {
			is.write(0);
			is.write(0);
		}else {
			is.write((arr.length >>> 8) & 0xFF);
			is.write(arr.length & 0xFF);
			for(int i = 0; i < arr.length; ++i) {
				is.write(arr[i]);
			}
		}
	}

	public static byte[] toASCIIBin(String txt) {
		if(txt == null) {
			return new byte[0];
		}else {
			byte[] ret = new byte[txt.length()];
			for(int i = 0; i < ret.length; ++i) {
				ret[i] = (byte)txt.charAt(i);
			}
			return ret;
		}
	}

	public static String toASCIIStr(byte[] bin) {
		char[] charRet = new char[bin.length];
		for(int i = 0; i < charRet.length; ++i) {
			charRet[i] = (char)((int)bin[i] & 0xFF);
		}
		return new String(charRet);
	}
}
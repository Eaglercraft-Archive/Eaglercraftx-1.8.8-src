package net.lax1dude.eaglercraft.v1_8.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class IPacket {

	private static final Logger logger = LogManager.getLogger("RelayPacket");

	private static final Map<Integer,Class<? extends IPacket>> definedPacketClasses = new HashMap();
	private static final Map<Class<? extends IPacket>,Integer> definedPacketIds = new HashMap();
	
	private static void register(int id, Class<? extends IPacket> clazz) {
		definedPacketClasses.put(id, clazz);
		definedPacketIds.put(clazz, id);
	}
	
	static {
		register(0x00, IPacket00Handshake.class);
		register(0x01, IPacket01ICEServers.class);
		register(0x02, IPacket02NewClient.class);
		register(0x03, IPacket03ICECandidate.class);
		register(0x04, IPacket04Description.class);
		register(0x05, IPacket05ClientSuccess.class);
		register(0x06, IPacket06ClientFailure.class);
		register(0x07, IPacket07LocalWorlds.class);
		register(0x69, IPacket69Pong.class);
		register(0x70, IPacket70SpecialUpdate.class);
		register(0xFE, IPacketFEDisconnectClient.class);
		register(0xFF, IPacketFFErrorCode.class);
	}
	
	public static IPacket readPacket(DataInputStream input) throws IOException {
		int i = input.read();
		try {
			Class<? extends IPacket> clazz = definedPacketClasses.get(i);
			if(clazz == null) {
				throw new IOException("Unknown packet type: " + i);
			}
			IPacket pkt = clazz.newInstance();
			pkt.read(input);
			return pkt;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IOException("Unknown packet type: " + i);
		}
	}
	
	public static byte[] writePacket(IPacket packet) throws IOException {
		Integer i = definedPacketIds.get(packet.getClass());
		if(i != null) {
			int len = packet.packetLength();
			EaglerOutputStream bao = len == -1 ? new EaglerOutputStream() :
				new EaglerOutputStream(len + 1);
			bao.write(i);
			packet.write(new DataOutputStream(bao));
			byte[] ret = bao.toByteArray();
			if(len != -1 && ret.length != len + 1) {
				logger.error("writePacket buffer for packet {} {} by {} bytes", packet.getClass().getSimpleName(),
						(len + 1 < ret.length ? "overflowed" : "underflowed"),
						(len + 1 < ret.length ? ret.length - len - 1 : len + 1 - ret.length));
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
				return null;
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
			return null;
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
			return null;
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
	
}

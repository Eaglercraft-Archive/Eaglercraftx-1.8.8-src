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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RelayPacket01ICEServers extends RelayPacket {

	public final Collection<RelayServer> servers;

	public static class RelayServer {

		public String address;
		public RelayType type;
		public String username;
		public String password;

		public RelayServer(String address, RelayType type, String username, String password) {
			this.address = address;
			this.type = type;
			this.username = username;
			this.password = password;
		}

		public String getICEString() {
			if(username == null) {
				return address;
			}else {
				return address + ";" + username + ";" + password;
			}
		}

	}

	public static enum RelayType {
		NO_PASSWD, PASSWD;
	}

	public RelayPacket01ICEServers() {
		this.servers = new ArrayList<>();
	}

	public RelayPacket01ICEServers(Collection<RelayServer> servers) {
		this.servers = servers;
	}

	public void write(DataOutputStream output) throws IOException {
		int l = servers.size();
		output.writeShort(l);
		Iterator<RelayServer> itr = servers.iterator();
		while(itr.hasNext()) {
			RelayServer srv = itr.next();
			if(srv.type == RelayType.NO_PASSWD) {
				output.write('S');
			}else if(srv.type == RelayType.PASSWD) {
				output.write('T');
			}else {
				throw new IOException("Unknown/Unsupported Relay Type: " + srv.type.name());
			}
			writeASCII16(output, srv.address);
			writeASCII8(output, srv.username);
			writeASCII8(output, srv.password);
		}
	}

	public void read(DataInputStream input) throws IOException {
		servers.clear();
		int l = input.readUnsignedShort();
		for(int i = 0; i < l; ++i) {
			char type = (char)input.read();
			RelayType typeEnum;
			if(type == 'S') {
				typeEnum = RelayType.NO_PASSWD;
			}else if(type == 'T') {
				typeEnum = RelayType.PASSWD;
			}else {
				throw new IOException("Unknown/Unsupported Relay Type: '" + type + "'");
			}
			servers.add(new RelayServer(
					readASCII16(input),
					typeEnum,
					readASCII8(input),
					readASCII8(input)
			));
		}
	}
}
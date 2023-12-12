package net.lax1dude.eaglercraft.v1_8.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
public class IPacket01ICEServers extends IPacket {
	
	public final Collection<ICEServerSet.RelayServer> servers;
	
	public IPacket01ICEServers() {
		servers = new ArrayList();
	}
	
	public void read(DataInputStream input) throws IOException {
		servers.clear();
		int l = input.readUnsignedShort();
		for(int i = 0; i < l; ++i) {
			char type = (char)input.read();
			ICEServerSet.RelayType typeEnum;
			if(type == 'S') {
				typeEnum = ICEServerSet.RelayType.STUN;
			}else if(type == 'T') {
				typeEnum = ICEServerSet.RelayType.TURN;
			}else {
				throw new IOException("Unknown/Unsupported Relay Type: '" + type + "'");
			}
			servers.add(new ICEServerSet.RelayServer(
					typeEnum,
					readASCII16(input),
					readASCII8(input),
					readASCII8(input)
			));
		}
	}
	
}

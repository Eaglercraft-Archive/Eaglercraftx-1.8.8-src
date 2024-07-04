package net.lax1dude.eaglercraft.v1_8.sp.relay.pkt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class RelayPacket07LocalWorlds extends RelayPacket {
	
	public static class LocalWorld {
		
		public final String worldName;
		public final String worldCode;
		
		public LocalWorld(String worldName, String worldCode) {
			this.worldName = worldName;
			this.worldCode = worldCode;
		}
		
	}
	
	public List<LocalWorld> worldsList;
	
	public RelayPacket07LocalWorlds() {
	}
	
	public RelayPacket07LocalWorlds(List<LocalWorld> worldsList) {
		this.worldsList = worldsList;
	}

	public void write(DataOutputStream output) throws IOException {
		if(worldsList == null) {
			output.write(0);
		}else {
			int i = worldsList.size();
			if(i > 255) {
				i = 255;
			}
			output.write(i);
			for(int j = 0; j < i; ++j) {
				LocalWorld w = worldsList.get(j);
				writeASCII8(output, w.worldName);
				writeASCII8(output, w.worldCode);
			}
		}
	}

	public void read(DataInputStream input) throws IOException {
		int l = input.read();
		if(worldsList == null) {
			worldsList = new ArrayList(l);
		}else {
			worldsList.clear();
		}
		for(int i = 0; i < l; ++i) {
			worldsList.add(new LocalWorld(readASCII8(input), readASCII8(input)));
		}
	}

	public int packetLength() {
		int accum = 1;
		if(worldsList != null) {
			for(int i = 0, l = worldsList.size(); i < l; ++i) {
				LocalWorld j = worldsList.get(i);
				accum += 2 + j.worldName.length() + j.worldCode.length();
			}
		}
		return accum;
	}
}

package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket01ICEServers;

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
public class EaglerSPRelayConfigRelayList {

	public static final Collection<RelayPacket01ICEServers.RelayServer> relayServers = new ArrayList<>();
	
	public static void loadRelays(File list) throws IOException {
		ArrayList<RelayPacket01ICEServers.RelayServer> loading = new ArrayList<>();
		
		if(!list.isFile()) {
			EaglerSPRelay.logger.info("Creating new {}...", list.getName());
			try(InputStream is = EaglerSPRelayConfigRelayList.class.getResourceAsStream("/relays.txt");
					FileOutputStream os = new FileOutputStream(list)) {
				byte[] buffer = new byte[4096];
				int i;
				while((i = is.read(buffer)) != -1) {
					os.write(buffer, 0, i);
				}
			}
		}

		EaglerSPRelay.logger.info("Loading STUN/TURN relays from: {}", list.getName());
		
		RelayPacket01ICEServers.RelayType addType = null;
		String addAddress = null;
		String addUsername = null;
		String addPassword = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(list))) {
			String line;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.length() == 0 || line.startsWith("#")) {
					continue;
				}
				boolean isNOPASSHead = line.equals("[NO_PASSWD]") || line.equals("[STUN]");
				boolean isPASSHead = line.equals("[PASSWD]") || line.equals("[TURN]");
				if(isNOPASSHead || isPASSHead) {
					if(addType != null) {
						add(list.getName(), loading, addType, addAddress, addUsername, addPassword);
					}
					addAddress = null;
					addUsername = null;
					addPassword = null;
					addType = null;
				}
				if(isNOPASSHead) {
					addType = RelayPacket01ICEServers.RelayType.NO_PASSWD;
				}else if(isPASSHead) {
					addType = RelayPacket01ICEServers.RelayType.PASSWD;
				}else if(line.startsWith("url")) {
					int spidx = line.indexOf('=') + 1;
					if(spidx < 3) {
						EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
					}else {
						line = line.substring(spidx).trim();
						if(line.length() < 1) {
							EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
						}else {
							addAddress = line;
						}
					}
				}else if(line.startsWith("username")) {
					int spidx = line.indexOf('=') + 1;
					if(spidx < 8) {
						EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
					}else {
						line = line.substring(spidx).trim();
						if(line.length() < 1) {
							EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
						}else {
							addUsername = line;
						}
					}
				}else if(line.startsWith("password")) {
					int spidx = line.indexOf('=') + 1;
					if(spidx < 8) {
						EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
					}else {
						line = line.substring(spidx).trim();
						if(line.length() < 1) {
							EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
						}else {
							addPassword = line;
						}
					}
				}else {
					EaglerSPRelay.logger.error("Error: Invalid line in {}: ", line);
				}
			}
		}
		
		if(addType != null) {
			add(list.getName(), loading, addType, addAddress, addUsername, addPassword);
		}
		
		if(loading.size() == 0) {
			throw new IOException(list.getName() + ": no servers loaded");
		}else {
			relayServers.clear();
			relayServers.addAll(loading);
		}
		
	}
	
	private static void add(String filename, Collection<RelayPacket01ICEServers.RelayServer> loading,
			RelayPacket01ICEServers.RelayType type, String url, String user, String pass) {
		if(url == null) {
			EaglerSPRelay.logger.error("Error: Invalid relay in {}, missing 'url'", filename);
		}else {
			loading.add(new RelayPacket01ICEServers.RelayServer(url, type, user, pass));
		}
	}
	
}

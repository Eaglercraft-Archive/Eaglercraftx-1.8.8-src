/*
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.sp.lan;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebRTC;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayQueryDispatch;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServer;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayWorldsQuery;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket07LocalWorlds;

public class LANServerList {
	
	private final List<LanServer> lanServersList = new LinkedList<>();
	private final Map<String, RelayWorldsQuery> lanServersQueryList = new LinkedHashMap<>();
	private final Set<String> deadURIs = new HashSet<>();
	
	private long lastRefresh = 0l;
	private int refreshCounter = 0;
	
	public boolean update() {
		long millis = EagRuntime.steadyTimeMillis();
		if(millis - lastRefresh > 20000l) {
			if(++refreshCounter < 10) {
				refresh();
			}else {
				lastRefresh = millis;
			}
		}else {
			boolean changed = false;
			Iterator<Entry<String,RelayWorldsQuery>> itr = lanServersQueryList.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<String,RelayWorldsQuery> etr = itr.next();
				String uri = etr.getKey();
				RelayWorldsQuery q = etr.getValue();
				q.update();
				if(!q.isQueryOpen()) {
					itr.remove();
					if(q.isQueryFailed()) {
						deadURIs.add(uri);
						Iterator<LanServer> itr2 = lanServersList.iterator();
						while(itr2.hasNext()) {
							if(itr2.next().lanServerRelay.address.equals(uri)) {
								itr2.remove();
								changed = true;
							}
						}
					}else {
						RelayServer rl = RelayManager.relayManager.getByURI(uri);
						Iterator<LanServer> itr2 = lanServersList.iterator();
						while(itr2.hasNext()) {
							LanServer l = itr2.next();
							if(l.lanServerRelay.address.equals(uri)) {
								l.flagged = false;
							}
						}
						if(rl != null) {
							Iterator<RelayPacket07LocalWorlds.LocalWorld> itr3 = q.getWorlds().iterator();
							yee: while(itr3.hasNext()) {
								RelayPacket07LocalWorlds.LocalWorld l = itr3.next();
								itr2 = lanServersList.iterator();
								while(itr2.hasNext()) {
									LanServer l2 = itr2.next();
									if(l2.lanServerRelay.address.equals(uri) && l2.lanServerCode.equals(l.worldCode)) {
										l2.lanServerMotd = l.worldName;
										l2.flagged = true;
										continue yee;
									}
								}
								lanServersList.add(new LanServer(l.worldName, rl, l.worldCode));
								changed = true;
							}
						}
						itr2 = lanServersList.iterator();
						while(itr2.hasNext()) {
							LanServer l = itr2.next();
							if(l.lanServerRelay.address.equals(uri)) {
								if(!l.flagged) {
									itr2.remove();
									changed = true;
								}
							}
						}
					}
				}
			}
			return changed;
		}
		return false;
	}
	
	public void forceRefresh() {
		deadURIs.clear();
		refreshCounter = 0;
		refresh();
	}

	private void refresh() {
		lastRefresh = EagRuntime.steadyTimeMillis();
		if(PlatformWebRTC.supported()) {
			for(int i = 0, l = RelayManager.relayManager.count(); i < l; ++i) {
				RelayServer srv = RelayManager.relayManager.get(i);
				if(!lanServersQueryList.containsKey(srv.address) && !deadURIs.contains(srv.address)) {
					lanServersQueryList.put(srv.address, RelayQueryDispatch.openRelayWorldsQuery(srv.address));
				}
			}
		}
	}

	public LanServer getServer(int idx) {
		return lanServersList.get(idx);
	}

	public int countServers() {
		return lanServersList.size();
	}
	
	public class LanServer {
		
		private String lanServerMotd;
		private RelayServer lanServerRelay;
		private String lanServerCode;
		
		protected boolean flagged = true;
		
		protected LanServer(String lanServerMotd, RelayServer lanServerRelay, String lanServerCode) {
			this.lanServerMotd = lanServerMotd;
			this.lanServerRelay = lanServerRelay;
			this.lanServerCode = lanServerCode;
		}
		
		public String getLanServerMotd() {
			return lanServerMotd;
		}
		
		public RelayServer getLanServerRelay() {
			return lanServerRelay;
		}
		
		public String getLanServerCode() {
			return lanServerCode;
		}
		
	}
	
}
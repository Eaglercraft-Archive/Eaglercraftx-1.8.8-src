package net.lax1dude.eaglercraft.v1_8.sp.relay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.IPacket;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.IPacket00Handshake;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.IPacketFFErrorCode;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
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
public class RelayManager {

	public static final Logger logger = LogManager.getLogger("RelayManager");

	public static final RelayManager relayManager = new RelayManager();
	public static final int preferredRelayVersion = 1;
	
	private final List<RelayServer> relays = new ArrayList();
	private long lastPingThrough = 0l;

	public void load(byte[] relayConfig) {
		NBTTagCompound relays = null;
		if(relayConfig != null) {
			try {
				relays = CompressedStreamTools.readCompressed(new EaglerInputStream(relayConfig));
			} catch (IOException ex) {
			}
		}
		if(relays != null && relays.hasKey("relays", 9)) {
			load(relays.getTagList("relays", 10));
			if(!relays.getBoolean("f")) {
				fixBullshit();
			}
		}else {
			sort(); // loads defaults
			save();
		}
	}

	// versions pre-u24 always have "relay.deev.is" as primary due to a glitch
	// this function is intended to randomize the list if that is detected
	private void fixBullshit() {
		if(!relays.isEmpty()) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer rs = relays.get(i);
				if(rs.address.equalsIgnoreCase("wss://relay.deev.is/") && !rs.isPrimary()) {
					return;
				}
			}
			for(int i = 0, l = relays.size(); i < l; ++i) {
				relays.get(i).setPrimary(false);
			}
			relays.get(ThreadLocalRandom.current().nextInt(relays.size())).setPrimary(true);
			sort();
			save();
		}
	}

	private void load(NBTTagList relayConfig) {
		relays.clear();
		if(relayConfig != null && relayConfig.tagCount() > 0) {
			boolean gotAPrimary = false;
			for(int i = 0, l = relayConfig.tagCount(); i < l; ++i) {
				NBTTagCompound relay = relayConfig.getCompoundTagAt(i);
				boolean p = relay.getBoolean("primary");
				if(p) {
					if(gotAPrimary) {
						p = false;
					}else {
						gotAPrimary = true;
					}
				}
				relays.add(new RelayServer(relay.getString("addr"), relay.getString("comment"), p));
			}
		}
		sort();
	}
	
	public void save() {
		if(relays.isEmpty()) {
			return;
		}
		byte[] data = write();
		if(data != null) {
			EagRuntime.setStorage("r", data);
		}
	}
	
	public byte[] write() {
		try {
			NBTTagList lst = new NBTTagList();
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				NBTTagCompound etr = new NBTTagCompound();
				etr.setString("addr", srv.address);
				etr.setString("comment", srv.comment);
				etr.setBoolean("primary", srv.isPrimary());
				lst.appendTag(etr);
			}

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("relays", lst);
			nbttagcompound.setBoolean("f", true);

			EaglerOutputStream bao = new EaglerOutputStream();
			CompressedStreamTools.writeCompressed(nbttagcompound, bao);
			return bao.toByteArray();
		} catch (Exception exception) {
			logger.error("Couldn\'t save relay list!");
			logger.error(exception);
			return null;
		}
	}
	
	private void sort() {
		if(relays.isEmpty()) {
			List<RelayEntry> defaultRelays = EagRuntime.getConfiguration().getRelays();
			for(int i = 0, l = defaultRelays.size(); i < l; ++i) {
				relays.add(new RelayServer(defaultRelays.get(i)));
			}
		}
		if(relays.isEmpty()) {
			return;
		}
		int j = -1;
		for(int i = 0, l = relays.size(); i < l; ++i) {
			if(relays.get(i).isPrimary()) {
				if(j == -1) {
					j = i;
				}else {
					relays.get(i).setPrimary(false);
				}
			}
		}
		if(j == -1) {
			boolean found = false;
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(srv.getPing() > 0l) {
					found = true;
					srv.setPrimary(true);
					break;
				}
			}
			if(!found) {
				relays.get(0).setPrimary(true);
			}
		}else {
			RelayServer srv = relays.remove(j);
			relays.add(0, srv);
		}
	}
	
	public void ping() {
		lastPingThrough = System.currentTimeMillis();
		for(int i = 0, l = relays.size(); i < l; ++i) {
			relays.get(i).ping();
		}
	}
	
	public void update() {
		for(int i = 0, l = relays.size(); i < l; ++i) {
			relays.get(i).update();
		}
	}
	
	public void close() {
		for(int i = 0, l = relays.size(); i < l; ++i) {
			relays.get(i).close();
		}
	}
	
	public int count() {
		return relays.size();
	}
	
	public RelayServer get(int idx) {
		return relays.get(idx);
	}
	
	public void add(String addr, String comment, boolean primary) {
		lastPingThrough = 0l;
		int i = relays.size();
		relays.add(new RelayServer(addr, comment, false));
		if(primary) {
			setPrimary0(i);
		}
		save();
	}
	
	public void addNew(String addr, String comment, boolean primary) {
		lastPingThrough = 0l;
		int i = relays.size();
		int j = primary || i == 0 ? 0 : 1;
		RelayServer newServer = new RelayServer(addr, comment, false);
		relays.add(j, newServer);
		newServer.ping();
		if(primary) {
			setPrimary0(j);
		}
		save();
	}
	
	public void setPrimary(int idx) {
		setPrimary0(idx);
		save();
	}

	private void setPrimary0(int idx) {
		if(idx >= 0 && idx < relays.size()) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(srv.isPrimary()) {
					srv.setPrimary(false);
				}
			}
			RelayServer pr = relays.remove(idx);
			pr.setPrimary(true);
			relays.add(0, pr);
		}
	}
	
	public void remove(int idx) {
		RelayServer srv = relays.remove(idx);
		srv.close();
		sort();
		save();
	}
	
	public RelayServer getPrimary() {
		if(!relays.isEmpty()) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(srv.isPrimary()) {
					return srv;
				}
			}
			sort();
			save();
			return getPrimary();
		}else {
			return null;
		}
	}
	
	public RelayServerSocket connectHandshake(RelayServer relay, int type, String code) {
		RelayServerSocket sock = relay.openSocket();
		while(!sock.isClosed()) {
			if(sock.isOpen()) {
				sock.writePacket(new IPacket00Handshake(type, preferredRelayVersion, code));
				while(!sock.isClosed()) {
					IPacket pkt = sock.nextPacket();
					if(pkt != null) {
						if(pkt instanceof IPacket00Handshake) {
							return sock;
						}else if(pkt instanceof IPacketFFErrorCode) {
							IPacketFFErrorCode ipkt = (IPacketFFErrorCode) pkt;
							logger.error("Relay [{}] failed: {}({}): {}", relay.address, IPacketFFErrorCode.code2string(ipkt.code), ipkt.code, ipkt.desc);
							Throwable t;
							while((t = sock.getException()) != null) {
								logger.error(t);
							}
							sock.close();
							return null;
						}else {
							logger.error("Relay [{}] unexpected packet: {}", relay.address, pkt.getClass().getSimpleName());
							sock.close();
							return null;
						}
					}
					EagUtils.sleep(20l);
				}
			}
			EagUtils.sleep(20l);
		}
		logger.error("Relay [{}] connection failed!", relay.address);
		Throwable t;
		while((t = sock.getException()) != null) {
			logger.error(t);
		}
		return null;
	}
	
	private final List<RelayServer> brokenServers = new LinkedList();

	public RelayServerSocket getWorkingRelay(Consumer<String> progressCallback, int type, String code) {
		brokenServers.clear();
		if(!relays.isEmpty()) {
			long millis = System.currentTimeMillis();
			if(millis - lastPingThrough < 10000l) {
				RelayServer relay = getPrimary();
				if(relay.getPing() > 0l && relay.getPingCompatible().isCompatible()) {
					progressCallback.accept(relay.address);
					RelayServerSocket sock = connectHandshake(relay, type, code);
					if(sock != null) {
						if(!sock.isFailed()) {
							return sock;
						}
					}else {
						brokenServers.add(relay);
					}
				}
				for(int i = 0, l = relays.size(); i < l; ++i) {
					RelayServer relayEtr = relays.get(i);
					if(relayEtr != relay) {
						if(relayEtr.getPing() > 0l && relayEtr.getPingCompatible().isCompatible()) {
							progressCallback.accept(relayEtr.address);
							RelayServerSocket sock = connectHandshake(relayEtr, type, code);
							if(sock != null) {
								if(!sock.isFailed()) {
									return sock;
								}
							}else {
								brokenServers.add(relayEtr);
							}
						}
					}
				}
			}
			return getWorkingCodeRelayActive(progressCallback, type, code);
		}else {
			return null;
		}
	}
	
	private RelayServerSocket getWorkingCodeRelayActive(Consumer<String> progressCallback, int type, String code) {
		if(!relays.isEmpty()) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				RelayServer srv = relays.get(i);
				if(!brokenServers.contains(srv)) {
					progressCallback.accept(srv.address);
					RelayServerSocket sock = connectHandshake(srv, type, code);
					if(sock != null) {
						if(!sock.isFailed()) {
							return sock;
						}
					}else {
						brokenServers.add(srv);
					}
				}
			}
			return null;
		}else {
			return null;
		}
	}
	
	public void loadDefaults() {
		List<RelayEntry> defaultRelays = EagRuntime.getConfiguration().getRelays();
		eee: for(int i = 0, l = defaultRelays.size(); i < l; ++i) {
			RelayEntry etr = defaultRelays.get(i);
			for(int j = 0, l2 = relays.size(); j < l2; ++j) {
				if(relays.get(j).address.equalsIgnoreCase(etr.address)) {
					continue eee;
				}
			}
			relays.add(new RelayServer(etr));
		}
		sort();
	}
	
	public String makeNewRelayName() {
		String str = "Relay Server #" + (relays.size() + 1);
		for(int i = relays.size() + 2, l = relays.size() + 50; i < l; ++i) {
			if(str.equalsIgnoreCase("Relay Server #" + i)) {
				str = "Relay Server #" + (i + 1);
			}
		}
		eee: while(true) {
			for(int i = 0, l = relays.size(); i < l; ++i) {
				if(str.equalsIgnoreCase(relays.get(i).comment)) {
					str = str + "_";
					continue eee;
				}
			}
			break;
		}
		return str;
	}
	
	public RelayServer getByURI(String uri) {
		Iterator<RelayServer> itr = relays.iterator();
		while(itr.hasNext()) {
			RelayServer rl = itr.next();
			if(rl.address.equals(uri)) {
				return rl;
			}
		}
		return null;
	}
	
}

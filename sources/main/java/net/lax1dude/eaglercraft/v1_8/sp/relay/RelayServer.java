package net.lax1dude.eaglercraft.v1_8.sp.relay;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebRTC;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayQuery.VersionMismatch;
import net.minecraft.client.Minecraft;

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
public class RelayServer {
	
	public final String address;
	public final String comment;
	private boolean primary;
	
	private RelayQuery query = null;
	private int queriedVersion = -1;
	private String queriedComment;
	private String queriedVendor;
	private VersionMismatch queriedCompatible;
	private long ping = 0l;
	private long workingPing = 0l;
	public long lastPing = 0l;

	public RelayServer(String address, String comment, boolean primary) {
		this.address = address;
		this.comment = comment;
		this.primary = primary;
	}
	
	public RelayServer(RelayEntry etr) {
		this(etr.address, etr.comment, etr.primary);
	}
	
	public boolean isPrimary() {
		return primary;
	}
	
	public void setPrimary(boolean primaryee) {
		primary = primaryee;
	}

	public long getPing() {
		return ping;
	}

	public long getWorkingPing() {
		return workingPing;
	}
	
	public int getPingVersion() {
		return queriedVersion;
	}
	
	public String getPingComment() {
		return queriedComment == null ? "" : queriedComment;
	}
	
	public String getPingVendor() {
		return queriedVendor == null ? "" : queriedVendor;
	}
	
	public VersionMismatch getPingCompatible() {
		return queriedCompatible;
	}
	
	public void pingBlocking() {
		ping();
		while(getPing() < 0l) {
			EagUtils.sleep(250);
			update();
		}
	}
	
	public void ping() {
		if(PlatformWebRTC.supported()) {
			close();
			query = RelayQueryDispatch.openRelayQuery(address);
			queriedVersion = -1;
			queriedComment = null;
			queriedVendor = null;
			queriedCompatible = VersionMismatch.UNKNOWN;
			ping = -1l;
		}else {
			query = null;
			queriedVersion = 1;
			queriedComment = "LAN NOT SUPPORTED";
			queriedVendor = "NULL";
			queriedCompatible = VersionMismatch.CLIENT_OUTDATED;
			ping = -1l;
		}
	}
	
	public void update() {
		if(query != null) {
			query.update();
			if(!query.isQueryOpen()) {
				if(query.isQueryFailed()) {
					queriedVersion = -1;
					queriedComment = null;
					queriedVendor = null;
					queriedCompatible = VersionMismatch.UNKNOWN;
					ping = 0l;
				}else {
					queriedVersion = query.getVersion();
					queriedComment = query.getComment();
					queriedVendor = query.getBrand();
					ping = query.getPing();
					queriedCompatible = query.getCompatible();
					workingPing = ping;
				}
				lastPing = EagRuntime.steadyTimeMillis();
				query = null;
			}
		}
	}
	
	public void close() {
		if(query != null && query.isQueryOpen()) {
			query.close();
			query = null;
			queriedVersion = -1;
			queriedComment = null;
			queriedVendor = null;
			queriedCompatible = VersionMismatch.UNKNOWN;
			ping = 0l;
		}
	}
	
	public RelayServerSocket openSocket() {
		return RelayQueryDispatch.openRelayConnection(address, Minecraft.getMinecraft().gameSettings.relayTimeout * 1000);
	}
	
}

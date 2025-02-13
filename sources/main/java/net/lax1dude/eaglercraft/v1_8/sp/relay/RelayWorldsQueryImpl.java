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

package net.lax1dude.eaglercraft.v1_8.sp.relay;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayQuery.RateLimit;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket00Handshake;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket07LocalWorlds;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket70SpecialUpdate;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacketFFErrorCode;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;

public class RelayWorldsQueryImpl implements RelayWorldsQuery {

	private static final Logger logger = LogManager.getLogger("RelayWorldsQuery");
	private static final RelayLoggerImpl loggerImpl = new RelayLoggerImpl(LogManager.getLogger("RelayPacket"));

	private final IWebSocketClient sock;
	private final String uri;

	private boolean failed;

	private long openedAt;

	private boolean hasSentHandshake = false;
	private boolean hasRecievedAnyData = false;
	private RelayQuery.RateLimit rateLimitStatus = RelayQuery.RateLimit.NONE;

	private RelayQuery.VersionMismatch versError = RelayQuery.VersionMismatch.UNKNOWN;

	private List<RelayPacket07LocalWorlds.LocalWorld> worlds = null;

	public RelayWorldsQueryImpl(String uri) {
		this.uri = uri;
		IWebSocketClient s;
		try {
			openedAt = EagRuntime.steadyTimeMillis();
			s = PlatformNetworking.openWebSocketUnsafe(uri);
		}catch(Throwable t) {
			sock = null;
			failed = true;
			return;
		}
		sock = s;
	}

	@Override
	public void update() {
		if(sock == null) return;
		if(sock.availableStringFrames() > 0) {
			logger.warn("[{}] discarding {} string frames recieved on a binary connection", uri, sock.availableStringFrames());
			sock.clearStringFrames();
		}
		List<IWebSocketFrame> frames = sock.getNextBinaryFrames();
		if(frames != null) {
			for(int i = 0, l = frames.size(); i < l; ++i) {
				hasRecievedAnyData = true;
				byte[] arr = frames.get(i).getByteArray();
				if(arr.length == 2 && arr[0] == (byte)0xFC) {
					if(arr[1] == (byte)0x00 || arr[1] == (byte)0x01) {
						rateLimitStatus = RateLimit.BLOCKED;
						RelayServerRateLimitTracker.setLimited(RelayWorldsQueryImpl.this.uri);
					}else if(arr[1] == (byte)0x02) {
						rateLimitStatus = RateLimit.NOW_LOCKED;
						RelayServerRateLimitTracker.setLimitedLocked(RelayWorldsQueryImpl.this.uri);
					}else {
						rateLimitStatus = RateLimit.LOCKED;
						RelayServerRateLimitTracker.setLocked(RelayWorldsQueryImpl.this.uri);
					}
					failed = true;
					sock.close();
				}else {
					try {
						RelayPacket pkt = RelayPacket.readPacket(new DataInputStream(new EaglerInputStream(arr)), loggerImpl);
						if(pkt instanceof RelayPacket07LocalWorlds) {
							worlds = ((RelayPacket07LocalWorlds)pkt).worldsList;
							sock.close();
							failed = false;
							return;
						}else if(pkt instanceof RelayPacket70SpecialUpdate) {
							RelayPacket70SpecialUpdate ipkt = (RelayPacket70SpecialUpdate)pkt;
							if(ipkt.operation == RelayPacket70SpecialUpdate.OPERATION_UPDATE_CERTIFICATE) {
								UpdateService.addCertificateToSet(ipkt.updatePacket);
							}
						}else if(pkt instanceof RelayPacketFFErrorCode) {
							RelayPacketFFErrorCode ipkt = (RelayPacketFFErrorCode)pkt;
							if(ipkt.code == RelayPacketFFErrorCode.TYPE_PROTOCOL_VERSION) {
								String s1 = ipkt.desc.toLowerCase();
								if(s1.contains("outdated client") || s1.contains("client outdated")) {
									versError = RelayQuery.VersionMismatch.CLIENT_OUTDATED;
								}else if(s1.contains("outdated server") || s1.contains("server outdated") ||
										s1.contains("outdated relay") || s1.contains("server relay")) {
									versError = RelayQuery.VersionMismatch.RELAY_OUTDATED;
								}else {
									versError = RelayQuery.VersionMismatch.UNKNOWN;
								}
							}
							logger.error("[{}] Recieved query error code {}: {}", uri, ipkt.code, ipkt.desc);
							failed = true;
							sock.close();
							return;
						}else {
							throw new IOException("Unexpected packet '" + pkt.getClass().getSimpleName() + "'");
						}
					} catch (IOException e) {
						logger.error("Relay World Query Error: {}", e.toString());
						EagRuntime.debugPrintStackTrace(e);
						failed = true;
						sock.close();
						return;
					}
				}
			}
		}
		if(sock.isOpen() && !hasSentHandshake) {
			hasSentHandshake = true;
			try {
				sock.send(RelayPacket.writePacket(new RelayPacket00Handshake(0x04, RelayManager.preferredRelayVersion, ""), loggerImpl));
			} catch (IOException e) {
				logger.error("Failed to write handshake: {}", e.toString());
				logger.error(e);
				sock.close();
				failed = true;
			}
		}
		if(sock.isClosed()) {
			if(!hasRecievedAnyData) {
				failed = true;
				rateLimitStatus = RelayServerRateLimitTracker.isLimitedLong(uri);
			}
		}else {
			if(EagRuntime.steadyTimeMillis() - openedAt > 10000l) {
				logger.error("Terminating connection that was open for too long: {}", uri);
				sock.close();
				failed = true;
			}
		}
	}

	@Override
	public boolean isQueryOpen() {
		return sock != null && !sock.isClosed();
	}

	@Override
	public boolean isQueryFailed() {
		return failed || sock == null || sock.getState() == EnumEaglerConnectionState.FAILED;
	}

	@Override
	public RelayQuery.RateLimit isQueryRateLimit() {
		return rateLimitStatus;
	}

	@Override
	public void close() {
		if(sock != null) {
			sock.close();
		}
	}

	@Override
	public List<RelayPacket07LocalWorlds.LocalWorld> getWorlds() {
		return worlds;
	}

	@Override
	public RelayQuery.VersionMismatch getCompatible() {
		return versError;
	}

}
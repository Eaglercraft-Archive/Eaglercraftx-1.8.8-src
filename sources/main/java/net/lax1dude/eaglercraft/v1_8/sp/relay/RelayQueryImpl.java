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
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket00Handshake;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket69Pong;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket70SpecialUpdate;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacketFFErrorCode;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;

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
public class RelayQueryImpl implements RelayQuery {

	private static final Logger logger = LogManager.getLogger("RelayQuery");
	private static final RelayLoggerImpl loggerImpl = new RelayLoggerImpl(LogManager.getLogger("RelayPacket"));

	private final IWebSocketClient sock;
	private final String uri;

	private boolean failed;

	private boolean hasSentHandshake = false;
	private boolean hasRecievedAnyData = false;

	private int vers = -1;
	private String comment = "<no comment>";
	private String brand = "<no brand>";

	private long connectionOpenedAt;
	private long connectionPingStart = -1;
	private long connectionPingTimer = -1;

	private RateLimit rateLimitStatus = RateLimit.NONE;

	private VersionMismatch versError = VersionMismatch.UNKNOWN;

	public RelayQueryImpl(String uri) {
		this.uri = uri;
		IWebSocketClient s;
		try {
			connectionOpenedAt = EagRuntime.steadyTimeMillis();
			s = PlatformNetworking.openWebSocketUnsafe(uri);
		}catch(Throwable t) {
			connectionOpenedAt = 0l;
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
						RelayServerRateLimitTracker.setLimited(RelayQueryImpl.this.uri);
					}else if(arr[1] == (byte)0x02) {
						rateLimitStatus = RateLimit.NOW_LOCKED;
						RelayServerRateLimitTracker.setLimitedLocked(RelayQueryImpl.this.uri);
					}else {
						rateLimitStatus = RateLimit.LOCKED;
						RelayServerRateLimitTracker.setLocked(RelayQueryImpl.this.uri);
					}
					failed = true;
					sock.close();
				}else {
					try {
						RelayPacket pkt = RelayPacket.readPacket(new DataInputStream(new EaglerInputStream(arr)), loggerImpl);
						if(pkt instanceof RelayPacket69Pong) {
							RelayPacket69Pong ipkt = (RelayPacket69Pong)pkt;
							versError = VersionMismatch.COMPATIBLE;
							if(connectionPingTimer == -1) {
								connectionPingTimer = frames.get(i).getTimestamp() - connectionPingStart;
							}
							vers = ipkt.protcolVersion;
							comment = ipkt.comment;
							brand = ipkt.brand;
							failed = false;
							sock.close();
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
									versError = VersionMismatch.CLIENT_OUTDATED;
								}else if(s1.contains("outdated server") || s1.contains("server outdated") ||
										s1.contains("outdated relay") || s1.contains("server relay")) {
									versError = VersionMismatch.RELAY_OUTDATED;
								}else {
									versError = VersionMismatch.UNKNOWN;
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
						logger.error("Relay query error: {}", e.toString());
						logger.error(e);
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
				connectionPingStart = EagRuntime.steadyTimeMillis();
				sock.send(RelayPacket.writePacket(new RelayPacket00Handshake(0x03, RelayManager.preferredRelayVersion, ""), loggerImpl));
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
		}
		if(EagRuntime.steadyTimeMillis() - connectionOpenedAt > 10000l) {
			logger.error("Terminating connection that was open for too long: {}", uri);
			sock.close();
			failed = true;
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
	public RateLimit isQueryRateLimit() {
		return rateLimitStatus;
	}

	@Override
	public void close() {
		if(sock != null) {
			sock.close();
		}
	}

	@Override
	public int getVersion() {
		return vers;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public String getBrand() {
		return brand;
	}

	@Override
	public long getPing() {
		return connectionPingTimer < 1 ? 1 : connectionPingTimer;
	}

	@Override
	public VersionMismatch getCompatible() {
		return versError;
	}

}

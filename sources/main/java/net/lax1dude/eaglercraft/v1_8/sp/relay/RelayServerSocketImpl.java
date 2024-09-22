package net.lax1dude.eaglercraft.v1_8.sp.relay;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket70SpecialUpdate;
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
public class RelayServerSocketImpl implements RelayServerSocket {

	private static final Logger logger = LogManager.getLogger("RelayServerSocket");
	private static final RelayLoggerImpl loggerImpl = new RelayLoggerImpl(LogManager.getLogger("RelayPacket"));

	private final IWebSocketClient sock;
	private final String uri;

	private boolean hasRecievedAnyData = false;
	private boolean failed = false;

	private final List<Throwable> exceptions = new LinkedList<>();
	private final List<RelayPacket> packets = new LinkedList<>();

	public RelayServerSocketImpl(String uri, int timeout) {
		this.uri = uri;
		IWebSocketClient s;
		try {
			s = PlatformNetworking.openWebSocketUnsafe(uri);
		}catch(Throwable t) {
			exceptions.add(t);
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
				try {
					RelayPacket pkt = RelayPacket.readPacket(new DataInputStream(frames.get(i).getInputStream()), loggerImpl);
					if(pkt instanceof RelayPacket70SpecialUpdate) {
						RelayPacket70SpecialUpdate ipkt = (RelayPacket70SpecialUpdate)pkt;
						if(ipkt.operation == RelayPacket70SpecialUpdate.OPERATION_UPDATE_CERTIFICATE) {
							UpdateService.addCertificateToSet(ipkt.updatePacket);
						}
					}else {
						packets.add(pkt);
					}
				} catch (IOException e) {
					exceptions.add(e);
					logger.error("[{}] Relay Socket Error: {}", uri, e.toString());
					EagRuntime.debugPrintStackTrace(e);
					failed = true;
					sock.close();
					return;
				}
			}
		}
		if(sock.isClosed()) {
			if (!hasRecievedAnyData) {
				failed = true;
			}
		}
	}

	@Override
	public boolean isOpen() {
		return sock != null && sock.isOpen();
	}

	@Override
	public boolean isClosed() {
		return sock == null || sock.isClosed();
	}

	@Override
	public void close() {
		if(sock != null) {
			sock.close();
		}
	}

	@Override
	public boolean isFailed() {
		return failed || sock == null || sock.getState() == EnumEaglerConnectionState.FAILED;
	}

	@Override
	public Throwable getException() {
		if(!exceptions.isEmpty()) {
			return exceptions.remove(0);
		}else {
			return null;
		}
	}

	@Override
	public void writePacket(RelayPacket pkt) {
		if(sock != null) {
			try {
				sock.send(RelayPacket.writePacket(pkt, loggerImpl));
			} catch (Throwable e) {
				logger.error("Relay connection error: {}", e.toString());
				EagRuntime.debugPrintStackTrace(e);
				exceptions.add(e);
				sock.close();
			}
		}
	}

	@Override
	public RelayPacket readPacket() {
		if(!packets.isEmpty()) {
			return packets.remove(0);
		}else {
			return null;
		}
	}

	@Override
	public RelayPacket nextPacket() {
		if(!packets.isEmpty()) {
			return packets.get(0);
		}else {
			return null;
		}
	}

	@Override
	public RelayQuery.RateLimit getRatelimitHistory() {
		return RelayServerRateLimitTracker.isLimitedEver(uri);
	}

	@Override
	public String getURI() {
		return uri;
	}

}

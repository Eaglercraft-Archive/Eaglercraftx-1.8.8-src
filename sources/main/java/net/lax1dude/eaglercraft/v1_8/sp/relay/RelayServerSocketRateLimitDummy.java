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

import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket;

public class RelayServerSocketRateLimitDummy implements RelayServerSocket {

	private final RelayQuery.RateLimit limit;

	public RelayServerSocketRateLimitDummy(RelayQuery.RateLimit limit) {
		this.limit = limit;
	}

	@Override
	public void update() {
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public boolean isClosed() {
		return true;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isFailed() {
		return true;
	}

	@Override
	public Throwable getException() {
		return null;
	}

	@Override
	public void writePacket(RelayPacket pkt) {
	}

	@Override
	public RelayPacket readPacket() {
		return null;
	}

	@Override
	public RelayPacket nextPacket() {
		return null;
	}

	@Override
	public RelayQuery.RateLimit getRatelimitHistory() {
		return limit;
	}

	@Override
	public String getURI() {
		return "<disconnected>";
	}

}
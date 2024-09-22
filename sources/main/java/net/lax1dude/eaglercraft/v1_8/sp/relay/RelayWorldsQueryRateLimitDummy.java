package net.lax1dude.eaglercraft.v1_8.sp.relay;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket07LocalWorlds;

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
public class RelayWorldsQueryRateLimitDummy implements RelayWorldsQuery {

	private final RelayQuery.RateLimit rateLimit;

	public RelayWorldsQueryRateLimitDummy(RelayQuery.RateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	@Override
	public void update() {
	}

	@Override
	public boolean isQueryOpen() {
		return false;
	}

	@Override
	public boolean isQueryFailed() {
		return true;
	}

	@Override
	public RelayQuery.RateLimit isQueryRateLimit() {
		return rateLimit;
	}

	@Override
	public void close() {
	}

	@Override
	public List<RelayPacket07LocalWorlds.LocalWorld> getWorlds() {
		return new ArrayList<>(0);
	}

	@Override
	public RelayQuery.VersionMismatch getCompatible() {
		return RelayQuery.VersionMismatch.COMPATIBLE;
	}

}

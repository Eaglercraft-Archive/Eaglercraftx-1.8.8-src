/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

public class RelayQueryDispatch {

	public static RelayQuery openRelayQuery(String addr) {
		RelayQuery.RateLimit limit = RelayServerRateLimitTracker.isLimited(addr);
		if(limit == RelayQuery.RateLimit.LOCKED || limit == RelayQuery.RateLimit.BLOCKED) {
			return new RelayQueryRateLimitDummy(limit);
		}
		return new RelayQueryImpl(addr);
	}

	public static RelayWorldsQuery openRelayWorldsQuery(String addr) {
		RelayQuery.RateLimit limit = RelayServerRateLimitTracker.isLimited(addr);
		if(limit == RelayQuery.RateLimit.LOCKED || limit == RelayQuery.RateLimit.BLOCKED) {
			return new RelayWorldsQueryRateLimitDummy(limit);
		}
		return new RelayWorldsQueryImpl(addr);
	}

	public static RelayServerSocket openRelayConnection(String addr, int timeout) {
		RelayQuery.RateLimit limit = RelayServerRateLimitTracker.isLimited(addr);
		if(limit == RelayQuery.RateLimit.LOCKED || limit == RelayQuery.RateLimit.BLOCKED) {
			return new RelayServerSocketRateLimitDummy(limit);
		}
		return new RelayServerSocketImpl(addr, timeout);
	}

}
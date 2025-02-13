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

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

public class RelayServerRateLimitTracker {

	private static final Map<String,Long> relayQueryLimited = new HashMap<>();
	private static final Map<String,Long> relayQueryLocked = new HashMap<>();

	public static void setLimited(String str) {
		synchronized(relayQueryLimited) {
			relayQueryLimited.put(str, EagRuntime.steadyTimeMillis());
		}
	}

	public static void setLocked(String str) {
		synchronized(relayQueryLocked) {
			relayQueryLocked.put(str, EagRuntime.steadyTimeMillis());
		}
	}

	public static void setLimitedLocked(String str) {
		long now = EagRuntime.steadyTimeMillis();
		synchronized(relayQueryLimited) {
			relayQueryLimited.put(str, now);
		}
		synchronized(relayQueryLocked) {
			relayQueryLocked.put(str, now);
		}
	}

	public static RelayQuery.RateLimit isLimited(String str) {
		long now = EagRuntime.steadyTimeMillis();
		synchronized(relayQueryLocked) {
			Long l = relayQueryLocked.get(str);
			if(l != null && now - l.longValue() < 60000l) {
				return RelayQuery.RateLimit.LOCKED;
			}
		}
		synchronized(relayQueryLimited) {
			Long l = relayQueryLimited.get(str);
			if(l != null && now - l.longValue() < 10000l) {
				return RelayQuery.RateLimit.BLOCKED;
			}
		}
		return RelayQuery.RateLimit.NONE;	
	}

	public static RelayQuery.RateLimit isLimitedLong(String str) {
		long now = EagRuntime.steadyTimeMillis();
		synchronized(relayQueryLocked) {
			Long l = relayQueryLocked.get(str);
			if(l != null && now - l.longValue() < 400000l) {
				return RelayQuery.RateLimit.LOCKED;
			}
		}
		synchronized(relayQueryLimited) {
			Long l = relayQueryLimited.get(str);
			if(l != null && now - l.longValue() < 900000l) {
				return RelayQuery.RateLimit.BLOCKED;
			}
		}
		return RelayQuery.RateLimit.NONE;	
	}

	public static RelayQuery.RateLimit isLimitedEver(String str) {
		synchronized(relayQueryLocked) {
			if(relayQueryLocked.containsKey(str)) {
				return RelayQuery.RateLimit.LOCKED;
			}
		}
		synchronized(relayQueryLimited) {
			if(relayQueryLimited.containsKey(str)) {
				return RelayQuery.RateLimit.BLOCKED;
			}
		}
		return RelayQuery.RateLimit.NONE;	
	}

}
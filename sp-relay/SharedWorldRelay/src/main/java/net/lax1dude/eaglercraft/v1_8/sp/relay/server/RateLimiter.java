package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class RateLimiter {
	
	private final int period;
	private final int limit;
	private final int lockoutLimit;
	private final int lockoutDuration;
	
	private class RateLimitEntry {
		
		protected long timer;
		protected int count;
		protected long lockedTimer;
		protected boolean locked;
		
		protected RateLimitEntry() {
			timer = Util.millis();
			count = 0;
			lockedTimer = 0l;
			locked = false;
		}
		
		protected void update() {
			long millis = Util.millis();
			if(locked) {
				if(millis - lockedTimer > RateLimiter.this.lockoutDuration) {
					timer = millis;
					count = 0;
					lockedTimer = 0l;
					locked = false;
				}
			}else {
				long p = RateLimiter.this.period / RateLimiter.this.limit;
				int breaker = 0;
				while(millis - timer > p) {
					timer += p;
					--count;
					if(count < 0 || ++breaker > 100) {
						timer = millis;
						count = 0;
						break;
					}
				}
			}
		}
		
	}
	
	public static enum RateLimit {
		NONE, LIMIT, LIMIT_NOW_LOCKOUT, LOCKOUT;
	}
	
	private final Map<String, RateLimitEntry> limiters = new HashMap<>();
	
	public RateLimiter(int period, int limit, int lockoutLimit, int lockoutDuration) {
		this.period = period;
		this.limit = limit;
		this.lockoutLimit = lockoutLimit;
		this.lockoutDuration = lockoutDuration;
	}
	
	public synchronized RateLimit limit(String addr) {
		RateLimitEntry etr = limiters.get(addr);
		
		if(etr == null) {
			etr = new RateLimitEntry();
			limiters.put(addr, etr);
		}else {
			etr.update();
		}
		
		if(etr.locked) {
			return RateLimit.LOCKOUT;
		}
		
		++etr.count;
		if(etr.count >= lockoutLimit) {
			etr.count = 0;
			etr.locked = true;
			etr.lockedTimer = Util.millis();
			return RateLimit.LIMIT_NOW_LOCKOUT;
		}else if(etr.count > limit) {
			return RateLimit.LIMIT;
		}else {
			return RateLimit.NONE;
		}
	}
	
	public synchronized void update() {
		Iterator<RateLimitEntry> itr = limiters.values().iterator();
		while(itr.hasNext()) {
			if(itr.next().count == 0) {
				itr.remove();
			}
		}
	}
	
	public synchronized void reset() {
		limiters.clear();
	}

}

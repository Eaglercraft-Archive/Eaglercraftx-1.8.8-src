package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.config.Configuration;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class EaglerRateLimiter {
	
	private final int period;
	private final int limit;
	private final int limitLockout;
	private int effectiveLimit;
	private int effectiveLimitLockout;
	private final int lockoutDuration;
	private final List<String> exceptions;
	
	private EaglerRateLimiter(int period, int limit, int limitLockout, int lockoutDuration, List<String> exceptions) {
		this.period = period * 1000 / limit;
		this.limit = this.effectiveLimit = limit;
		this.limitLockout = this.effectiveLimitLockout = limitLockout;
		this.lockoutDuration = lockoutDuration * 1000;
		this.exceptions = exceptions;
	}

	public void setDivisor(int d) {
		this.effectiveLimit = this.limit * d;
		this.effectiveLimitLockout = this.limitLockout * d;
	}

	public int getPeriod() {
		return period;
	}

	public int getLimit() {
		return effectiveLimit;
	}

	public int getLimitLockout() {
		return effectiveLimitLockout;
	}

	public int getLockoutDuration() {
		return lockoutDuration;
	}

	public List<String> getExceptions() {
		return exceptions;
	}

	public boolean isException(String addr) {
		for(int i = 0, l = exceptions.size(); i < l; ++i) {
			String str = exceptions.get(i);
			int ll = str.length() - 1;
			if(str.indexOf('*') == 0) {
				if(addr.endsWith(str.substring(1))) {
					return true;
				}
			}else if(str.lastIndexOf('*') == ll) {
				if(addr.startsWith(str.substring(ll))) {
					return true;
				}
			}else {
				if(addr.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	protected class RateLimiter {
		
		protected int requestCounter = 0;
		protected long lockoutTimestamp = 0l;
		protected long cooldownTimestamp = 0l;
		
		protected RateLimitStatus rateLimit() {
			long millis = System.currentTimeMillis();
			tick(millis);
			if(lockoutTimestamp != 0l) {
				return RateLimitStatus.LOCKED_OUT;
			}else {
				if(++requestCounter > EaglerRateLimiter.this.effectiveLimitLockout) {
					lockoutTimestamp = millis;
					requestCounter = 0;
					return RateLimitStatus.LIMITED_NOW_LOCKED_OUT;
				}else if(requestCounter > EaglerRateLimiter.this.effectiveLimit) {
					return RateLimitStatus.LIMITED;
				}else {
					return RateLimitStatus.OK;
				}
			}
		}
		
		protected void tick(long millis) {
			if(lockoutTimestamp != 0l) {
				if(millis - lockoutTimestamp > EaglerRateLimiter.this.lockoutDuration) {
					requestCounter = 0;
					lockoutTimestamp = 0l;
					cooldownTimestamp = millis;
				}
			}else {
				long delta = millis - cooldownTimestamp;
				long decr = delta / EaglerRateLimiter.this.period;
				if(decr >= requestCounter) {
					requestCounter = 0;
					cooldownTimestamp = millis;
				}else {
					requestCounter -= decr;
					cooldownTimestamp += decr * EaglerRateLimiter.this.period;
					if(requestCounter < 0) {
						requestCounter = 0;
					}
				}
			}
		}
	}

	private final Map<String, RateLimiter> ratelimiters = new HashMap();

	public RateLimitStatus rateLimit(String addr) {
		addr = addr.toLowerCase();
		if(isException(addr)) {
			return RateLimitStatus.OK;
		}else {
			RateLimiter limiter;
			synchronized(ratelimiters) {
				limiter = ratelimiters.get(addr);
				if(limiter == null) {
					limiter = new RateLimiter();
					ratelimiters.put(addr, limiter);
				}
			}
			return limiter.rateLimit();
		}
	}

	public void tick() {
		long millis = System.currentTimeMillis();
		synchronized(ratelimiters) {
			Iterator<RateLimiter> itr = ratelimiters.values().iterator();
			while(itr.hasNext()) {
				RateLimiter i = itr.next();
				i.tick(millis);
				if(i.requestCounter <= 0 && i.lockoutTimestamp <= 0l) {
					itr.remove();
				}
			}
		}
	}

	public void reset() {
		synchronized(ratelimiters) {
			ratelimiters.clear();
		}
	}

	static EaglerRateLimiter loadConfig(Configuration config) {
		int period = config.getInt("period", -1);
		int limit = config.getInt("limit", -1);
		int limitLockout = config.getInt("limit_lockout", -1);
		int lockoutDuration = config.getInt("lockout_duration", -1);
		Collection<String> exc = (Collection<String>) config.getList("exceptions");
		List<String> exceptions = new ArrayList();
		for(String str : exc) {
			exceptions.add(str.toLowerCase());
		}
		if(period != -1 && limit != -1 && limitLockout != -1 && lockoutDuration != -1) {
			return new EaglerRateLimiter(period, limit, limitLockout, lockoutDuration, exceptions);
		}else {
			return null;
		}
	}

}

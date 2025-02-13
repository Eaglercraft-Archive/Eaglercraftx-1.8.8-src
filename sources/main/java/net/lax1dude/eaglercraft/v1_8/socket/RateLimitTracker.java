/*
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.socket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

public class RateLimitTracker {

	private static long lastTickUpdate = 0l;

	private static final Map<String, Long> blocks = new HashMap<>();
	private static final Map<String, Long> lockout = new HashMap<>();

	public static boolean isLockedOut(String addr) {
		Long lockoutStatus = lockout.get(addr);
		return lockoutStatus != null && EagRuntime.steadyTimeMillis() - lockoutStatus.longValue() < 300000l;
	}

	public static boolean isProbablyLockedOut(String addr) {
		return blocks.containsKey(addr) || lockout.containsKey(addr);
	}

	public static void registerBlock(String addr) {
		blocks.put(addr, EagRuntime.steadyTimeMillis());
	}

	public static void registerLockOut(String addr) {
		long millis = EagRuntime.steadyTimeMillis();
		blocks.put(addr, millis);
		lockout.put(addr, millis);
	}

	public static void tick() {
		long millis = EagRuntime.steadyTimeMillis();
		if(millis - lastTickUpdate > 5000l) {
			lastTickUpdate = millis;
			Iterator<Long> blocksItr = blocks.values().iterator();
			while(blocksItr.hasNext()) {
				if(millis - blocksItr.next().longValue() > 900000l) {
					blocksItr.remove();
				}
			}
			blocksItr = lockout.values().iterator();
			while(blocksItr.hasNext()) {
				if(millis - blocksItr.next().longValue() > 900000l) {
					blocksItr.remove();
				}
			}
		}
	}

}
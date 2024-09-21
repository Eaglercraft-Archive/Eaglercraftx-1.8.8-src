package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class SimpleRateLimiter {

	private long timer;
	private int count;

	public SimpleRateLimiter() {
		timer = EaglerXBungeeAPIHelper.steadyTimeMillis();
		count = 0;
	}

	public boolean rateLimit(int maxPerMinute) {
		int t = 60000 / maxPerMinute;
		long millis = EaglerXBungeeAPIHelper.steadyTimeMillis();
		int decr = (int)(millis - timer) / t;
		if(decr > 0) {
			timer += decr * t;
			count -= decr;
			if(count < 0) {
				count = 0;
			}
		}
		if(count >= maxPerMinute) {
			return false;
		}else {
			++count;
			return true;
		}
	}

}

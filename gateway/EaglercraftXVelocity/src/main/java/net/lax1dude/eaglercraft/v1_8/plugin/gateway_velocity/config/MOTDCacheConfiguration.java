package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config;

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
public class MOTDCacheConfiguration {
	
	public final int cacheTTL;
	public final boolean cacheServerListAnimation;
	public final boolean cacheServerListResults;
	public final boolean cacheServerListTrending;
	public final boolean cacheServerListPortfolios;
	
	public MOTDCacheConfiguration(int cacheTTL, boolean cacheServerListAnimation, boolean cacheServerListResults,
			boolean cacheServerListTrending, boolean cacheServerListPortfolios) {
		this.cacheTTL = cacheTTL;
		this.cacheServerListAnimation = cacheServerListAnimation;
		this.cacheServerListResults = cacheServerListResults;
		this.cacheServerListTrending = cacheServerListTrending;
		this.cacheServerListPortfolios = cacheServerListPortfolios;
	}

}

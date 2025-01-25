package net.lax1dude.eaglercraft.v1_8.minecraft;

import java.util.Collection;

/**
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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
public abstract class ResourceIndex {

	protected Collection<String> propertiesCache = null;
	protected Collection<String> citPotionsCache = null;

	public Collection<String> getPropertiesFiles() {
		if(propertiesCache == null) {
			propertiesCache = getPropertiesFiles0();
		}
		return propertiesCache;
	}

	protected abstract Collection<String> getPropertiesFiles0();

	public Collection<String> getCITPotionsFiles() {
		if(citPotionsCache == null) {
			citPotionsCache = getCITPotionsFiles0();
		}
		return citPotionsCache;
	}

	protected abstract Collection<String> getCITPotionsFiles0();

}

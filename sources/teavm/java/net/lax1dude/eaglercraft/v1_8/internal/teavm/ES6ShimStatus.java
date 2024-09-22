package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.EnumSet;
import java.util.Set;

import org.teavm.jso.JSBody;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
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
public class ES6ShimStatus {

	private static final Logger logger = LogManager.getLogger("ES6ShimStatus");

	private static ES6ShimStatus instance = null;

	@JSBody(params = { }, script = "return (typeof __eaglercraftXES6ShimStatus === \"object\") ? __eaglercraftXES6ShimStatus : null;")
	private static native ES6ShimStatusJS getRuntimeStatus0();

	public static ES6ShimStatus getRuntimeStatus() {
		if(instance == null) {
			return instance = new ES6ShimStatus(getRuntimeStatus0());
		}
		ES6ShimStatusJS jsImpl = getRuntimeStatus0();
		if(instance.impl != jsImpl) {
			instance = new ES6ShimStatus(jsImpl);
		}
		return instance;
	}

	private final ES6ShimStatusJS impl;
	private final EnumES6ShimStatus status;
	private final Set<EnumES6Shims> shims;

	public ES6ShimStatus(ES6ShimStatusJS impl) {
		this.impl = impl;
		if(impl != null && TeaVMUtils.isTruthy(impl)) {
			this.status = EnumES6ShimStatus.getStatusById(impl.getShimInitStatus());
			this.shims = EnumSet.noneOf(EnumES6Shims.class);
			for(int i = 0, id, l = impl.getEnabledShimCount(); i < l; ++i) {
				id = impl.getEnabledShimID(i);
				EnumES6Shims theShim = EnumES6Shims.getShimById(id);
				if(theShim != null) {
					this.shims.add(theShim);
				}else {
					logger.warn("Ignoring unknown shim id: {}", id);
				}
			}
		}else {
			this.status = EnumES6ShimStatus.STATUS_NOT_PRESENT;
			this.shims = EnumSet.noneOf(EnumES6Shims.class);
		}
	}

	public ES6ShimStatusJS getImpl() {
		return impl;
	}

	public EnumES6ShimStatus getStatus() {
		return status;
	}

	public Set<EnumES6Shims> getShims() {
		return shims;
	}

}

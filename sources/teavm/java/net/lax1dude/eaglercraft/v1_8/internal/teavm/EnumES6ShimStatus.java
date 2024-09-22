package net.lax1dude.eaglercraft.v1_8.internal.teavm;

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
public enum EnumES6ShimStatus {
	STATUS_NOT_PRESENT(Integer.MIN_VALUE, "Not present"),
	STATUS_ERROR(ES6ShimStatusJS.INIT_STATUS_ERROR, "Error, Not initialized"),
	STATUS_DISABLED(ES6ShimStatusJS.INIT_STATUS_DISABLED, "Disabled"),
	STATUS_ENABLED(ES6ShimStatusJS.INIT_STATUS_ENABLED, "Enabled"),
	STATUS_DISABLED_ERRORS(ES6ShimStatusJS.INIT_STATUS_DISABLED_ERRORS, "Errors; Disabled"),
	STATUS_ENABLED_ERRORS(ES6ShimStatusJS.INIT_STATUS_ENABLED_ERRORS, "Errors; Enabled");

	public final int statusId;
	public final String statusDesc;

	private EnumES6ShimStatus(int statusId, String statusDesc) {
		this.statusId = statusId;
		this.statusDesc = statusDesc;
	}

	public static EnumES6ShimStatus getStatusById(int id) {
		id = id + 1;
		return (id >= 0 && id < lookup.length) ? lookup[id] : null;
	}

	public boolean isEnabled() {
		return (statusId != -1) && (statusId & 1) != 0;
	}

	public boolean isErrored() {
		return (statusId == -1) || (statusId & 2) != 0;
	}

	private static final EnumES6ShimStatus[] lookup = new EnumES6ShimStatus[5];

	static {
		EnumES6ShimStatus[] _values = values();
		for(int i = 0; i < _values.length; ++i) {
			lookup[_values[i].statusId + 1] = _values[i];
		}
	}

}

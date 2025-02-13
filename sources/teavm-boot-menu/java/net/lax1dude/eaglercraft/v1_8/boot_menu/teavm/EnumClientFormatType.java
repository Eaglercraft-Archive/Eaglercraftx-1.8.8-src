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

package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.Set;

import com.google.common.collect.Sets;

public enum EnumClientFormatType {
	/**
	 * Eagler 1.8, b1.3, or pre-singleplayer 1.5 offline
	 */
	EAGLER_STANDARD_OFFLINE(0, "Standard Offline", Sets.newHashSet(EnumClientLaunchType.EAGLERX_V1, EnumClientLaunchType.EAGLER_1_5_V1,
					EnumClientLaunchType.EAGLER_BETA_V1, EnumClientLaunchType.PEYTON_V2, EnumClientLaunchType.PEYTON_V1,
					EnumClientLaunchType.STANDARD_OFFLINE_V1)),

	/**
	 * Eagler 1.5 offline with integrated server
	 */
	EAGLER_STANDARD_1_5_OFFLINE(1, "Standard 1.5 Offline", Sets.newHashSet(EnumClientLaunchType.EAGLER_1_5_V2)),
	
	/**
	 * Eagler 1.8 with certificate
	 */
	EAGLER_SIGNED_OFFLINE(2, "Signed Offline", Sets.newHashSet(EnumClientLaunchType.EAGLERX_SIGNED_V1));

	public final int id;
	public final String displayName;
	public final Set<EnumClientLaunchType> launchTypes;

	private EnumClientFormatType(int id, String displayName, Set<EnumClientLaunchType> launchTypes) {
		this.id = id;
		this.displayName = displayName;
		this.launchTypes = launchTypes;
	}

	private static final EnumClientFormatType[] lookup = new EnumClientFormatType[3];

	public static EnumClientFormatType getById(int id) {
		if(id >= 0 && id < lookup.length) {
			return lookup[id];
		}else {
			return null;
		}
	}

	static {
		EnumClientFormatType[] _values = values();
		for(int i = 0; i < _values.length; ++i) {
			lookup[_values[i].id] = _values[i];
		}
	}

}
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

public enum EnumClientLaunchType {
	/**
	 * Configuable Eagler 1.8-like offline
	 */
	STANDARD_OFFLINE_V1(0),
	
	/**
	 * Eagler 1.8 standard
	 */
	EAGLERX_V1(1),
	
	/**
	 * Eagler 1.8 with certificate
	 */
	EAGLERX_SIGNED_V1(2),
	
	/**
	 * Eagler 1.5 array "window.minecraftOpts" of element id, epk file, servers, server to join
	 */
	EAGLER_1_5_V1(3),
	
	/**
	 * Eagler 1.5 array "window.eaglercraftOpts" with "container" and "assetsURI" and "serverWorkerURI"
	 */
	EAGLER_1_5_V2(4),
	
	/**
	 * Eagler beta array "window.minecraftOpts" of element id, epk file, server to join
	 */
	EAGLER_BETA_V1(5),
	
	/**
	 * Peyton format with "window.classicConfig" array of root element id and epk file
	 */
	PEYTON_V1(6),
	
	/**
	 * Peyton format with "window.config" JSON object with "gameContainer" and "assetsLocation"
	 */
	PEYTON_V2(7);

	public final int id;

	private EnumClientLaunchType(int id) {
		this.id = id;
	}

	private static final EnumClientLaunchType[] lookup = new EnumClientLaunchType[8];

	public static EnumClientLaunchType getById(int id) {
		if(id >= 0 && id < lookup.length) {
			return lookup[id];
		}else {
			return null;
		}
	}

	public static EnumClientLaunchType[] _values() {
		return lookup;
	}

	static {
		EnumClientLaunchType[] _values = values();
		for(int i = 0; i < _values.length; ++i) {
			lookup[_values[i].id] = _values[i];
		}
	}

}
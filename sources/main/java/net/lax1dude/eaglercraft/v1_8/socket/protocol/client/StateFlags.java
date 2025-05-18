/*
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

package net.lax1dude.eaglercraft.v1_8.socket.protocol.client;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

public class StateFlags {

	public static final EaglercraftUUID EAGLER_PLAYER_FLAG_PRESENT = new EaglercraftUUID(0x55F63601694140D9L,
			0xB77BCE7B99A62E52L);

	public static final EaglercraftUUID LEGACY_EAGLER_PLAYER_FLAG_PRESENT = new EaglercraftUUID(0xEEEEA64771094C4EL,
			0x86E55B81D17E67EBL);

	public static final EaglercraftUUID DISABLE_SKIN_URL_LOOKUP = new EaglercraftUUID(0xC41D641BE2DA4094L,
			0xB1B2DFF2E9D08180L);

	public static boolean eaglerPlayerFlag = false;

	public static boolean eaglerPlayerFlagSupervisor = false;

	public static boolean disableSkinURLLookup = false;

	public static void setFlag(EaglercraftUUID flag, int value) {
		if (flag.equals(EAGLER_PLAYER_FLAG_PRESENT)) {
			eaglerPlayerFlag = (value & 1) != 0;
			eaglerPlayerFlagSupervisor = (value & 2) != 0;
		} else if (flag.equals(DISABLE_SKIN_URL_LOOKUP)) {
			disableSkinURLLookup = value != 0;
		}
	}

	public static void reset() {
		eaglerPlayerFlag = false;
		eaglerPlayerFlagSupervisor = false;
		disableSkinURLLookup = false;
	}

}

/*
 * Copyright (c) 2022-2024 ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelReadyState;

public class PlatformVoiceClient {

	public static void initialize() {
		
	}

	public static void initializeDevices() {
		
	}

	public static boolean isSupported() {
		return false;
	}

	public static void setVoiceListenVolume(float f) {
		
	}

	public static void setVoiceSpeakVolume(float f) {
		
	}

	public static void activateVoice(boolean talk) {
		
	}

	public static void setICEServers(String[] servs) {
		
	}

	public static void signalConnect(EaglercraftUUID user, boolean offer) {
		
	}

	public static void signalDisconnect(EaglercraftUUID user, boolean b) {
		
	}

	public static void signalICECandidate(EaglercraftUUID user, String ice) {
		
	}

	public static void signalDescription(EaglercraftUUID user, String desc) {
		
	}

	public static void tickVoiceClient() {
		
	}

	public static void updateVoicePosition(EaglercraftUUID uuid, double x, double y, double z) {
		
	}

	public static void resetPeerStates() {
		
	}

	public static void setVoiceProximity(int prox) {
		
	}

	public static void setMicVolume(float f) {
		
	}

	public static void mutePeer(EaglercraftUUID uuid, boolean mute) {
		
	}

	public static EnumVoiceChannelReadyState getReadyState() {
		return EnumVoiceChannelReadyState.NONE;
	}

}
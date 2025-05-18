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

package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.voice.EnumVoiceChannelReadyState;

public class PlatformVoiceClient {

	public static native boolean isSupported();

	public static native void setICEServers(String[] urls);

	public static native void activateVoice(boolean talk);

	public static native void initializeDevices();

	public static native void tickVoiceClient();

	public static native void setMicVolume(float val);

	public static native EnumVoiceChannelReadyState getReadyState();

	public static native void signalConnect(EaglercraftUUID peerId, boolean offer);

	public static native void signalDescription(EaglercraftUUID peerId, String descJSON);

	public static native void signalDisconnect(EaglercraftUUID peerId, boolean quiet);

	public static native void makePeerGlobal(EaglercraftUUID peerId);

	public static native void makePeerProximity(EaglercraftUUID peerId);

	public static native void setVoiceProximity(int prox);

	public static native void updateVoicePosition(EaglercraftUUID uuid, double x, double y, double z);

	public static native void mutePeer(EaglercraftUUID peerId, boolean muted);

	public static native void signalICECandidate(EaglercraftUUID peerId, String candidate);

	public static native void setVoiceListenVolume(float f);

}
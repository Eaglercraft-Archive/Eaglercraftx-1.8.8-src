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

package net.lax1dude.eaglercraft.v1_8.sp.internal;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;

public class ClientPlatformSingleplayer {

	public static native void startIntegratedServer(boolean singleThreadMode);

	public static native void sendPacket(IPCPacketData packet);

	public static native List<IPCPacketData> recieveAllPacket();

	public static native boolean canKillWorker();

	public static native void killWorker();

	public static native boolean isRunningSingleThreadMode();

	public static native boolean isSingleThreadModeSupported();

	public static native void updateSingleThreadMode();

	public static native void showCrashReportOverlay(String report, int x, int y, int w, int h);

	public static native void hideCrashReportOverlay();

}
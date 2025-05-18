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

public class PlatformApplication {

	public static native void openLink(String url);

	public static native void setClipboard(String text);

	public static native String getClipboard();

	public static native void setLocalStorage(String name, byte[] data);

	public static native void setLocalStorage(String name, byte[] data, boolean hooks);

	public static native byte[] getLocalStorage(String name);

	public static native byte[] getLocalStorage(String name, boolean hooks);

	public static native void displayFileChooser(String mime, String ext);

	public static native boolean fileChooserHasResult();

	public static native FileChooserResult getFileChooserResult();

	public static native void clearFileChooserResult();

	public static native void showPopup(String msg);

	public static native void openCreditsPopup(String text);

	public static native void downloadFileWithName(String str, byte[] dat);

	public static native String saveScreenshot();

	public static native void showDebugConsole();

	public static native void addLogMessage(String text, boolean err);

	public static native boolean isShowingDebugConsole();

	public static native void setMCServerWindowGlobal(String str);

}
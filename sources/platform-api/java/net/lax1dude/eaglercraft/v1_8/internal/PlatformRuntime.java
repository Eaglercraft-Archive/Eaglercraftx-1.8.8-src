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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

public class PlatformRuntime {

	public static native void create();

	public static native void destroy();

	public static native EnumPlatformType getPlatformType();

	public static native EnumPlatformAgent getPlatformAgent();

	public static native String getUserAgentString();

	public static native EnumPlatformOS getPlatformOS();

	public static native boolean requireSSL();

	public static native boolean isOfflineDownloadURL();

	public static native EnumPlatformANGLE getPlatformANGLE();

	public static native String getGLVersion();

	public static native String getGLRenderer();

	public static native ByteBuffer allocateByteBuffer(int length);

	public static native IntBuffer allocateIntBuffer(int length);

	public static native FloatBuffer allocateFloatBuffer(int length);

	public static native ByteBuffer castPrimitiveByteArray(byte[] array);

	public static native IntBuffer castPrimitiveIntArray(int[] array);

	public static native FloatBuffer castPrimitiveFloatArray(float[] array);

	public static native byte[] castNativeByteBuffer(ByteBuffer buffer);

	public static native int[] castNativeIntBuffer(IntBuffer buffer);

	public static native float[] castNativeFloatBuffer(FloatBuffer buffer);

	public static native void freeByteBuffer(ByteBuffer byteBuffer);

	public static native void freeIntBuffer(IntBuffer intBuffer);

	public static native void freeFloatBuffer(FloatBuffer floatBuffer);

	public static native void downloadRemoteURIByteArray(String assetPackageURI, final Consumer<byte[]> cb);

	public static native void downloadRemoteURIByteArray(String assetPackageURI, boolean useCache,
			final Consumer<byte[]> cb);

	public static native byte[] downloadRemoteURIByteArray(String assetPackageURI);

	public static native byte[] downloadRemoteURIByteArray(String assetPackageURI, boolean forceCache);

	public static native boolean isDebugRuntime();

	public static native void writeCrashReport(String crashDump);

	public static native void showContextLostScreen(String crashDump);

	public static native void getStackTrace(Throwable t, Consumer<String> ret);

	public static native boolean printJSExceptionIfBrowser(Throwable t);

	public static native String currentThreadName();

	public static native void setThreadName(String string);

	public static native long maxMemory();

	public static native long totalMemory();

	public static native long freeMemory();

	public static native String getCallingClass(int i);

	public static native long randomSeed();

	public static native void exit();

	public static native IClientConfigAdapter getClientConfigAdapter();

	public static native long steadyTimeMillis();

	public static native long nanoTime();

	public static native void sleep(int millis);

	public static native void immediateContinue();

	public static native boolean immediateContinueSupported();

	public static native void postCreate();

	public static native void setDisplayBootMenuNextRefresh(boolean en);

	public static native OutputStream newDeflaterOutputStream(OutputStream os) throws IOException;

	public static native int deflateFull(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff,
			int outputLen) throws IOException;

	public static native OutputStream newGZIPOutputStream(OutputStream os) throws IOException;

	public static native InputStream newInflaterInputStream(InputStream is) throws IOException;

	public static native int inflateFull(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff,
			int outputLen) throws IOException;

	public static native InputStream newGZIPInputStream(InputStream is) throws IOException;

}
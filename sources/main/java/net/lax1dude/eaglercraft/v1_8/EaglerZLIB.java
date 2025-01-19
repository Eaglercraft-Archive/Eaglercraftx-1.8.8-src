package net.lax1dude.eaglercraft.v1_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class EaglerZLIB {

	public static OutputStream newDeflaterOutputStream(OutputStream os) throws IOException {
		return PlatformRuntime.newDeflaterOutputStream(os);
	}

	public static int deflateFull(byte[] input, byte[] output) throws IOException {
		return PlatformRuntime.deflateFull(input, 0, input.length, output, 0, output.length);
	}

	public static int deflateFull(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff,
			int outputLen) throws IOException {
		return PlatformRuntime.deflateFull(input, inputOff, inputLen, output, outputOff, outputLen);
	}

	public static OutputStream newGZIPOutputStream(OutputStream os) throws IOException {
		return PlatformRuntime.newGZIPOutputStream(os);
	}

	public static InputStream newInflaterInputStream(InputStream is) throws IOException {
		return PlatformRuntime.newInflaterInputStream(is);
	}

	public static int inflateFull(byte[] input, byte[] output) throws IOException {
		return PlatformRuntime.inflateFull(input, 0, input.length, output, 0, output.length);
	}

	public static int inflateFull(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff,
			int outputLen) throws IOException {
		return PlatformRuntime.inflateFull(input, inputOff, inputLen, output, outputOff, outputLen);
	}

	public static InputStream newGZIPInputStream(InputStream is) throws IOException {
		return PlatformRuntime.newGZIPInputStream(is);
	}

}

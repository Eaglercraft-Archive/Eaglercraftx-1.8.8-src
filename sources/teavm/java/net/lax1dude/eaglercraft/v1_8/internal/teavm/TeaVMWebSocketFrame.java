package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.io.InputStream;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;

import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

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
public class TeaVMWebSocketFrame implements IWebSocketFrame {

	private JSObject data;
	private boolean str;

	private String cachedStrContent = null;
	private byte[] cachedByteContent = null;

	private int cachedLen = -1;

	private final long timestamp;

	@JSBody(params = { "obj" }, script = "return (typeof obj === \"string\");")
	private static native boolean isStr(JSObject obj);

	public TeaVMWebSocketFrame(JSObject data) {
		this.data = data;
		this.str = isStr(data);
		this.timestamp = PlatformRuntime.steadyTimeMillis();
	}

	@Override
	public boolean isString() {
		return str;
	}

	@JSBody(params = { "obj" }, script = "return obj;")
	private static native String toStr(JSObject obj);

	@Override
	public String getString() {
		if(str) {
			if(cachedStrContent == null) {
				return (cachedStrContent = toStr(data));
			}else {
				return cachedStrContent;
			}
		}else {
			return null;
		}
	}

	@Override
	public byte[] getByteArray() {
		if(!str) {
			if(cachedByteContent == null) {
				return (cachedByteContent = TeaVMUtils.wrapByteArrayBuffer((ArrayBuffer)data));
			}else {
				return cachedByteContent;
			}
		}else {
			return null;
		}
	}

	@Override
	public InputStream getInputStream() {
		if(!str) {
			return new ArrayBufferInputStream((ArrayBuffer)data);
		}else {
			return null;
		}
	}

	@JSBody(params = { "obj" }, script = "return obj.length;")
	private static native int strLen(JSObject obj);

	@JSBody(params = { "obj" }, script = "return obj.byteLength;")
	private static native int arrLen(JSObject obj);

	@Override
	public int getLength() {
		if(cachedLen == -1) {
			if(str) {
				cachedLen = strLen(data);
			}else {
				cachedLen = arrLen(data);
			}
		}
		return cachedLen;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

}

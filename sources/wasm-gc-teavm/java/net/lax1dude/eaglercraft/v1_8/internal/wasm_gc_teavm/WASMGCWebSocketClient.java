package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCDirectArrayConverter;

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
public class WASMGCWebSocketClient implements IWebSocketClient {

	public interface JSWebSocketClientHandle extends JSObject {

		@JSProperty
		int getState();

		void closeSocket();

		void sendStringFrame(String str);

		void sendBinaryFrame(Uint8Array arr);

		int availableFrames();

		WASMGCWebSocketFrame.JSWebSocketFrame getNextFrame();

		WASMGCWebSocketFrame.JSWebSocketFrame[] getAllFrames();

		void clearFrames();

		int availableStringFrames();

		WASMGCWebSocketFrame.JSWebSocketFrame getNextStringFrame();

		WASMGCWebSocketFrame.JSWebSocketFrame[] getAllStringFrames();

		void clearStringFrames();

		int availableBinaryFrames();

		WASMGCWebSocketFrame.JSWebSocketFrame getNextBinaryFrame();

		WASMGCWebSocketFrame.JSWebSocketFrame[] getAllBinaryFrames();

		void clearBinaryFrames();

	}

	private final JSWebSocketClientHandle handle;
	private final String uri;

	public WASMGCWebSocketClient(JSWebSocketClientHandle handle, String uri) {
		this.handle = handle;
		this.uri = uri;
	}

	@Override
	public EnumEaglerConnectionState getState() {
		int state = handle.getState();
		switch(state) {
		case 0:
		default:
			return EnumEaglerConnectionState.CLOSED;
		case 1:
			return EnumEaglerConnectionState.CONNECTING;
		case 2:
			return EnumEaglerConnectionState.CONNECTED;
		case 3:
			return EnumEaglerConnectionState.FAILED;
		}
	}

	@Override
	public boolean connectBlocking(int timeoutMS) {
		long startTime = PlatformRuntime.steadyTimeMillis();
		int state;
		for(;;) {
			state = handle.getState();
			if(state != 1) { // CONNECTING
				break;
			}
			EagUtils.sleep(50);
			if(PlatformRuntime.steadyTimeMillis() - startTime > timeoutMS) {
				state = 3;
				break;
			}
		}
		return state == 2;
	}

	@Override
	public boolean isOpen() {
		return handle.getState() == 2;
	}

	@Override
	public boolean isClosed() {
		int state = handle.getState();
		return state != 1 && state != 2;
	}

	@Override
	public void close() {
		handle.closeSocket();
	}

	@Override
	public int availableFrames() {
		return handle.availableFrames();
	}

	@Override
	public IWebSocketFrame getNextFrame() {
		WASMGCWebSocketFrame.JSWebSocketFrame nextFrame = handle.getNextFrame();
		return nextFrame != null ? new WASMGCWebSocketFrame(nextFrame) : null;
	}

	@Override
	public List<IWebSocketFrame> getNextFrames() {
		WASMGCWebSocketFrame.JSWebSocketFrame[] arrJS = handle.getAllFrames();
		if(arrJS == null) {
			return null;
		}
		int len = arrJS.length;
		IWebSocketFrame[] arr = new IWebSocketFrame[len];
		for(int i = 0; i < len; ++i) {
			arr[i] = new WASMGCWebSocketFrame(arrJS[i]);
		}
		return Arrays.asList(arr);
	}

	@Override
	public void clearFrames() {
		handle.clearFrames();
	}

	@Override
	public int availableStringFrames() {
		return handle.availableStringFrames();
	}

	@Override
	public IWebSocketFrame getNextStringFrame() {
		WASMGCWebSocketFrame.JSWebSocketFrame nextFrame = handle.getNextStringFrame();
		return nextFrame != null ? new WASMGCWebSocketFrame(nextFrame) : null;
	}

	@Override
	public List<IWebSocketFrame> getNextStringFrames() {
		WASMGCWebSocketFrame.JSWebSocketFrame[] arrJS = handle.getAllStringFrames();
		if(arrJS == null) {
			return null;
		}
		int len = arrJS.length;
		IWebSocketFrame[] arr = new IWebSocketFrame[len];
		for(int i = 0; i < len; ++i) {
			arr[i] = new WASMGCWebSocketFrame(arrJS[i]);
		}
		return Arrays.asList(arr);
	}

	@Override
	public void clearStringFrames() {
		handle.clearStringFrames();
	}

	@Override
	public int availableBinaryFrames() {
		return handle.availableBinaryFrames();
	}

	@Override
	public IWebSocketFrame getNextBinaryFrame() {
		WASMGCWebSocketFrame.JSWebSocketFrame nextFrame = handle.getNextBinaryFrame();
		return nextFrame != null ? new WASMGCWebSocketFrame(nextFrame) : null;
	}

	@Override
	public List<IWebSocketFrame> getNextBinaryFrames() {
		WASMGCWebSocketFrame.JSWebSocketFrame[] arrJS = handle.getAllBinaryFrames();
		if(arrJS == null) {
			return null;
		}
		int len = arrJS.length;
		IWebSocketFrame[] arr = new IWebSocketFrame[len];
		for(int i = 0; i < len; ++i) {
			arr[i] = new WASMGCWebSocketFrame(arrJS[i]);
		}
		return Arrays.asList(arr);
	}

	@Override
	public void clearBinaryFrames() {
		handle.clearStringFrames();
	}

	@Override
	public void send(String str) {
		handle.sendStringFrame(str);
	}

	@Override
	public void send(byte[] bytes) {
		ByteBuffer buf = WASMGCDirectArrayConverter.byteArrayToBuffer(bytes);
		try {
			handle.sendBinaryFrame(WASMGCBufferAllocator.getUnsignedByteBufferView(buf));
		}finally {
			PlatformRuntime.freeByteBuffer(buf);
		}
	}

	@Override
	public String getCurrentURI() {
		return uri;
	}

}

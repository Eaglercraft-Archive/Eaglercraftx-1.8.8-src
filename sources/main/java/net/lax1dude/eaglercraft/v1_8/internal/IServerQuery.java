/*
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

package net.lax1dude.eaglercraft.v1_8.internal;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;

public interface IServerQuery {

	public static final long defaultTimeout = 10000l;

	public static enum QueryReadyState {
		CONNECTING(true, false), OPEN(true, false), CLOSED(false, true), FAILED(false, true);
		
		private final boolean open;
		private final boolean closed;
		
		private QueryReadyState(boolean open, boolean closed) {
			this.open = open;
			this.closed = closed;
		}
		
		public boolean isOpen() {
			return open;
		}
		
		public boolean isClosed() {
			return closed;
		}

	}

	void update();

	void send(String str);

	default void send(JSONObject json) {
		send(json.toString());
	}

	void send(byte[] bytes);

	int responsesAvailable();

	QueryResponse getResponse();

	int binaryResponsesAvailable();

	byte[] getBinaryResponse();

	QueryReadyState readyState();

	default boolean isOpen() {
		return readyState().isOpen();
	}

	default boolean isClosed() {
		return readyState().isClosed();
	}

	void close();

	EnumServerRateLimit getRateLimit();

	default boolean awaitResponseAvailable(long timeout) {
		long start = EagRuntime.steadyTimeMillis();
		while(isOpen() && responsesAvailable() <= 0 && (timeout <= 0l || EagRuntime.steadyTimeMillis() - start < timeout)) {
			EagUtils.sleep(5);
		}
		return responsesAvailable() > 0;
	}
	
	default boolean awaitResponseAvailable() {
		return awaitResponseAvailable(defaultTimeout);
	}
	
	default boolean awaitResponseBinaryAvailable(long timeout) {
		long start = EagRuntime.steadyTimeMillis();
		while(isOpen() && binaryResponsesAvailable() <= 0 && (timeout <= 0l || EagRuntime.steadyTimeMillis() - start < timeout)) {
			EagUtils.sleep(5);
		}
		return binaryResponsesAvailable() > 0;
	}

	default boolean awaitResponseBinaryAvailable() {
		return awaitResponseBinaryAvailable(defaultTimeout);
	}

	default QueryResponse awaitResponse(long timeout) {
		return awaitResponseAvailable(timeout) ? getResponse() : null;
	}
	
	default QueryResponse awaitResponse() {
		return awaitResponseAvailable() ? getResponse() : null;
	}
	
	default byte[] awaitResponseBinary(long timeout) {
		return awaitResponseBinaryAvailable(timeout) ? getBinaryResponse() : null;
	}

	default byte[] awaitResponseBinary() {
		return awaitResponseBinaryAvailable() ? getBinaryResponse() : null;
	}

}
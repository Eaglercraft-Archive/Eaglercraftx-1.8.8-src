/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.MessageEvent;
import org.teavm.jso.websocket.WebSocket;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.AbstractWebSocketClient;
import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

public class TeaVMWebSocketClient extends AbstractWebSocketClient {

	private final WebSocket sock;
	private boolean sockIsConnecting = true;
	private boolean sockIsConnected = false;
	private boolean sockIsFailed = false;

	public TeaVMWebSocketClient(String socketURI) {
		super(socketURI);
		sock = WebSocket.create(socketURI);
		sock.setBinaryType("arraybuffer");
		TeaVMUtils.addEventListener(sock, "open", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				sockIsConnecting = false;
				sockIsConnected = true;
			}
		});
		TeaVMUtils.addEventListener(sock, "close", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				sockIsConnecting = false;
				sockIsConnected = false;
			}
		});
		TeaVMUtils.addEventListener(sock, "message", new EventListener<MessageEvent>() {
			@Override
			public void handleEvent(MessageEvent evt) {
				addRecievedFrame(new TeaVMWebSocketFrame(evt.getData()));
			}
		});
		TeaVMUtils.addEventListener(sock, "error", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(sockIsConnecting) {
					sockIsFailed = true;
					sockIsConnecting = false;
				}
			}
		});
	}

	@Override
	public boolean connectBlocking(int timeoutMS) {
		long startTime = PlatformRuntime.steadyTimeMillis();
		while(!sockIsConnected && !sockIsFailed) {
			EagUtils.sleep(50);
			if(PlatformRuntime.steadyTimeMillis() - startTime > timeoutMS * 1000) {
				break;
			}
		}
		return sockIsConnected;
	}

	@Override
	public EnumEaglerConnectionState getState() {
		return sockIsConnected ? EnumEaglerConnectionState.CONNECTED
				: (sockIsFailed ? EnumEaglerConnectionState.FAILED
						: (sockIsConnecting ? EnumEaglerConnectionState.CONNECTING : EnumEaglerConnectionState.CLOSED));
	}

	@Override
	public boolean isOpen() {
		return sockIsConnected;
	}

	@Override
	public boolean isClosed() {
		return !sockIsConnecting && !sockIsConnected;
	}

	@Override
	public void close() {
		sockIsConnecting = false;
		sockIsConnected = false;
		sock.close();
	}

	@Override
	public void send(String str) {
		if(sockIsConnected) {
			sock.send(str);
		}
	}

	@Override
	public void send(byte[] bytes) {
		if(sockIsConnected) {
			sock.send(TeaVMUtils.unwrapArrayBuffer(bytes));
		}
	}

}
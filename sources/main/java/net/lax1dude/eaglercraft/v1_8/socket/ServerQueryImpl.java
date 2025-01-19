package net.lax1dude.eaglercraft.v1_8.socket;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.v1_8.internal.EnumServerRateLimit;
import net.lax1dude.eaglercraft.v1_8.internal.IServerQuery;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketFrame;
import net.lax1dude.eaglercraft.v1_8.internal.QueryResponse;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
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
class ServerQueryImpl implements IServerQuery {

	public static final Logger logger = LogManager.getLogger("WebSocketQuery");

	private final List<QueryResponse> queryResponses = new LinkedList<>();
	private final List<byte[]> queryResponsesBytes = new LinkedList<>();

	protected final IWebSocketClient websocketClient;
	protected final String uri;
	protected final String accept;
	protected boolean hasSentAccept = false;
	protected boolean open = true;
	protected boolean alive = false;
	protected long pingStart = -1l;
	protected long pingTimer = -1l;
	private EnumServerRateLimit rateLimit = EnumServerRateLimit.OK;

	ServerQueryImpl(IWebSocketClient websocketClient, String accept) {
		this.websocketClient = websocketClient;
		this.uri = websocketClient.getCurrentURI();
		this.accept = accept;
	}

	@Override
	public void update() {
		if(!hasSentAccept && websocketClient.getState() == EnumEaglerConnectionState.CONNECTED) {
			hasSentAccept = true;
			websocketClient.send("Accept: " + accept);
		}
		List<IWebSocketFrame> lst = websocketClient.getNextFrames();
		if(lst != null) {
			for(int i = 0, l = lst.size(); i < l; ++i) {
				IWebSocketFrame frame = lst.get(i);
				alive = true;
				if(pingTimer == -1) {
					pingTimer = frame.getTimestamp() - pingStart;
					if(pingTimer < 1) {
						pingTimer = 1;
					}
				}
				if(frame.isString()) {
					String str = frame.getString();
					if(str.equalsIgnoreCase("BLOCKED")) {
						logger.error("Reached full IP ratelimit for {}!", uri);
						rateLimit = EnumServerRateLimit.BLOCKED;
						return;
					}
					if(str.equalsIgnoreCase("LOCKED")) {
						logger.error("Reached full IP ratelimit lockout for {}!", uri);
						rateLimit = EnumServerRateLimit.LOCKED_OUT;
						return;
					}
					try {
						JSONObject obj = new JSONObject(str);
						if("blocked".equalsIgnoreCase(obj.optString("type", null))) {
							logger.error("Reached query ratelimit for {}!", uri);
							rateLimit = EnumServerRateLimit.BLOCKED;
						}else if("locked".equalsIgnoreCase(obj.optString("type", null))) {
							logger.error("Reached query ratelimit lockout for {}!", uri);
							rateLimit = EnumServerRateLimit.LOCKED_OUT;
						}else {
							queryResponses.add(new QueryResponse(obj, pingTimer));
						}
					}catch(Throwable t) {
						logger.error("Exception thrown parsing websocket query response from \"" + uri + "\"!");
						logger.error(t);
					}
				}else {
					queryResponsesBytes.add(frame.getByteArray());
				}
			}
		}
		if(websocketClient.isClosed()) {
			open = false;
		}
	}

	@Override
	public void send(String str) {
		if(websocketClient.getState() == EnumEaglerConnectionState.CONNECTED) {
			websocketClient.send(str);
		}
	}

	@Override
	public void send(byte[] bytes) {
		if(websocketClient.getState() == EnumEaglerConnectionState.CONNECTED) {
			websocketClient.send(bytes);
		}
	}

	@Override
	public int responsesAvailable() {
		synchronized(queryResponses) {
			return queryResponses.size();
		}
	}

	@Override
	public QueryResponse getResponse() {
		synchronized(queryResponses) {
			if(queryResponses.size() > 0) {
				return queryResponses.remove(0);
			}else {
				return null;
			}
		}
	}

	@Override
	public int binaryResponsesAvailable() {
		synchronized(queryResponsesBytes) {
			return queryResponsesBytes.size();
		}
	}

	@Override
	public byte[] getBinaryResponse() {
		synchronized(queryResponsesBytes) {
			if(queryResponsesBytes.size() > 0) {
				return queryResponsesBytes.remove(0);
			}else {
				return null;
			}
		}
	}

	@Override
	public QueryReadyState readyState() {
		return open ? (alive ? QueryReadyState.OPEN : QueryReadyState.CONNECTING)
				: (alive ? QueryReadyState.CLOSED : QueryReadyState.FAILED);
	}

	@Override
	public void close() {
		if(open) {
			open = false;
			websocketClient.close();
		}
	}

	@Override
	public EnumServerRateLimit getRateLimit() {
		return rateLimit;
	}

}

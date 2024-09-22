package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
public abstract class AbstractWebSocketClient implements IWebSocketClient {

	protected volatile int availableStringFrames = 0;
	protected volatile int availableBinaryFrames = 0;
	protected final List<IWebSocketFrame> recievedPacketBuffer = new LinkedList<>();
	protected String currentURI;

	protected AbstractWebSocketClient(String currentURI) {
		this.currentURI = currentURI;
	}

	protected void addRecievedFrame(IWebSocketFrame frame) {
		boolean str = frame.isString();
		synchronized(recievedPacketBuffer) {
			recievedPacketBuffer.add(frame);
			if(str) {
				++availableStringFrames;
			}else {
				++availableBinaryFrames;
			}
		}
	}

	@Override
	public int availableFrames() {
		synchronized(recievedPacketBuffer) {
			return availableStringFrames + availableBinaryFrames;
		}
	}

	@Override
	public IWebSocketFrame getNextFrame() {
		synchronized(recievedPacketBuffer) {
			if(!recievedPacketBuffer.isEmpty()) {
				IWebSocketFrame frame = recievedPacketBuffer.remove(0);
				if(frame.isString()) {
					--availableStringFrames;
				}else {
					--availableBinaryFrames;
				}
				return frame;
			}else {
				return null;
			}
		}
	}

	@Override
	public List<IWebSocketFrame> getNextFrames() {
		synchronized(recievedPacketBuffer) {
			if(!recievedPacketBuffer.isEmpty()) {
				List<IWebSocketFrame> ret = new ArrayList<>(recievedPacketBuffer);
				recievedPacketBuffer.clear();
				availableStringFrames = 0;
				availableBinaryFrames = 0;
				return ret;
			}else {
				return null;
			}
		}
	}

	@Override
	public void clearFrames() {
		synchronized(recievedPacketBuffer) {
			recievedPacketBuffer.clear();
		}
	}

	@Override
	public int availableStringFrames() {
		synchronized(recievedPacketBuffer) {
			return availableStringFrames;
		}
	}

	@Override
	public IWebSocketFrame getNextStringFrame() {
		synchronized(recievedPacketBuffer) {
			if(availableStringFrames > 0) {
				Iterator<IWebSocketFrame> itr = recievedPacketBuffer.iterator();
				while(itr.hasNext()) {
					IWebSocketFrame r = itr.next();
					if(r.isString()) {
						itr.remove();
						--availableStringFrames;
						return r;
					}
				}
				availableStringFrames = 0;
				return null;
			}else {
				return null;
			}
		}
	}

	@Override
	public List<IWebSocketFrame> getNextStringFrames() {
		synchronized(recievedPacketBuffer) {
			if(availableStringFrames > 0) {
				List<IWebSocketFrame> ret = new ArrayList<>(availableStringFrames);
				Iterator<IWebSocketFrame> itr = recievedPacketBuffer.iterator();
				while(itr.hasNext()) {
					IWebSocketFrame r = itr.next();
					if(r.isString()) {
						itr.remove();
						ret.add(r);
					}
				}
				availableStringFrames = 0;
				return ret;
			}else {
				return null;
			}
		}
	}

	@Override
	public void clearStringFrames() {
		synchronized(recievedPacketBuffer) {
			if(availableStringFrames > 0) {
				Iterator<IWebSocketFrame> itr = recievedPacketBuffer.iterator();
				while(itr.hasNext()) {
					IWebSocketFrame r = itr.next();
					if(r.isString()) {
						itr.remove();
					}
				}
				availableStringFrames = 0;
			}
		}
	}

	@Override
	public int availableBinaryFrames() {
		synchronized(recievedPacketBuffer) {
			return availableBinaryFrames;
		}
	}

	@Override
	public IWebSocketFrame getNextBinaryFrame() {
		synchronized(recievedPacketBuffer) {
			if(availableBinaryFrames > 0) {
				Iterator<IWebSocketFrame> itr = recievedPacketBuffer.iterator();
				while(itr.hasNext()) {
					IWebSocketFrame r = itr.next();
					if(!r.isString()) {
						itr.remove();
						--availableBinaryFrames;
						return r;
					}
				}
				availableBinaryFrames = 0;
				return null;
			}else {
				return null;
			}
		}
	}

	@Override
	public List<IWebSocketFrame> getNextBinaryFrames() {
		synchronized(recievedPacketBuffer) {
			if(availableBinaryFrames > 0) {
				List<IWebSocketFrame> ret = new ArrayList<>(availableBinaryFrames);
				Iterator<IWebSocketFrame> itr = recievedPacketBuffer.iterator();
				while(itr.hasNext()) {
					IWebSocketFrame r = itr.next();
					if(!r.isString()) {
						itr.remove();
						ret.add(r);
					}
				}
				availableBinaryFrames = 0;
				return ret;
			}else {
				return null;
			}
		}
	}

	@Override
	public void clearBinaryFrames() {
		synchronized(recievedPacketBuffer) {
			if(availableBinaryFrames > 0) {
				Iterator<IWebSocketFrame> itr = recievedPacketBuffer.iterator();
				while(itr.hasNext()) {
					IWebSocketFrame r = itr.next();
					if(!r.isString()) {
						itr.remove();
					}
				}
				availableBinaryFrames = 0;
			}
		}
	}

	@Override
	public String getCurrentURI() {
		return currentURI;
	}

}

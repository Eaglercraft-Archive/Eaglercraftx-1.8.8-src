/*
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

package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.List;

public interface IWebSocketClient {

	EnumEaglerConnectionState getState();

	boolean connectBlocking(int timeoutMS);

	boolean isOpen();

	boolean isClosed();

	void close();

	int availableFrames();

	IWebSocketFrame getNextFrame();

	List<IWebSocketFrame> getNextFrames();

	void clearFrames();

	int availableStringFrames();

	IWebSocketFrame getNextStringFrame();

	List<IWebSocketFrame> getNextStringFrames();

	void clearStringFrames();

	int availableBinaryFrames();

	IWebSocketFrame getNextBinaryFrame();

	List<IWebSocketFrame> getNextBinaryFrames();

	void clearBinaryFrames();

	void send(String str);

	void send(byte[] bytes);

	String getCurrentURI();

	void setEnableStringFrames(boolean enable);

	void setEnableBinaryFrames(boolean enable);

}
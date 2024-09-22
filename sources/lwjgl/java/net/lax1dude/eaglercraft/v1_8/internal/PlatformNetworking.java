package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URI;
import java.net.URISyntaxException;

import net.lax1dude.eaglercraft.v1_8.internal.lwjgl.DesktopWebSocketClient;
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
public class PlatformNetworking {
	
	private static final Logger logger = LogManager.getLogger("PlatformNetworking");
	
	public static IWebSocketClient openWebSocket(String socketURI) {
		try {
			URI uri = new URI(socketURI);
			return new DesktopWebSocketClient(uri);
		}catch(Throwable t) {
			logger.error("Could not open WebSocket to \"{}\"!", socketURI);
			logger.error(t);
			return null;
		}
	}
	
	public static IWebSocketClient openWebSocketUnsafe(String socketURI) throws URISyntaxException {
		URI uri = new URI(socketURI);
		return new DesktopWebSocketClient(uri);
	}
	
}

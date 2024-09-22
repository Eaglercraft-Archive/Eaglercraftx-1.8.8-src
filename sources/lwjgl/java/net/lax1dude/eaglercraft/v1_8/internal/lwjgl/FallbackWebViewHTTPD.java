package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
class FallbackWebViewHTTPD extends NanoHTTPD {

	static final Logger logger = FallbackWebViewServer.logger;

	private String index;

	FallbackWebViewHTTPD(String hostname, int port, String index) {
		super(hostname, port);
		this.index = index;
	}

	@Override
    public Response serve(IHTTPSession session) {
		if("/RTWebViewClient".equals(session.getUri())) {
			return newFixedLengthResponse(Status.OK, MIME_HTML, index);
		}else {
			return newFixedLengthResponse(Status.NOT_FOUND, MIME_HTML, "<!DOCTYPE html><html><head><title>Eaglercraft Desktop Runtime</title></head><body><h1>404 Not Found</h1></body></html>");
		}
	}
}

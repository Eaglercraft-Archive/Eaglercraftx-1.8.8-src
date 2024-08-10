package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response;

import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerXBukkitAPI;

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
public class ResponseBytes implements IEaglerRPCResponse {

	protected final IEaglerXBukkitAPI source;
	protected final int requestID;
	public final byte[] bytes;

	public ResponseBytes(IEaglerXBukkitAPI source, int requestID, byte[] bytes) {
		this.source = source;
		this.requestID = requestID;
		this.bytes = bytes;
	}

	@Override
	public IEaglerXBukkitAPI getSource() {
		return source;
	}

	@Override
	public int getRequestID() {
		return requestID;
	}

}

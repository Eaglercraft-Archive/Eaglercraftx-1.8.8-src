/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.sp.server.socket.protocol;

import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
import net.minecraft.network.NetHandlerPlayServer;

public abstract class ServerMessageHandler implements GameMessageHandler {

	protected final NetHandlerPlayServer netHandler;
	protected final EaglerMinecraftServer server;

	public ServerMessageHandler(NetHandlerPlayServer netHandler) {
		this.netHandler = netHandler;
		this.server = (EaglerMinecraftServer)netHandler.serverController;
	}

	public static ServerMessageHandler createServerHandler(int version, NetHandlerPlayServer netHandler) {
		switch(version) {
		case 3:
			return new ServerV3MessageHandler(netHandler);
		case 4:
			return new ServerV4MessageHandler(netHandler);
		case 5:
			return new ServerV5MessageHandler(netHandler);
		default:
			throw new UnsupportedOperationException();
		}
	}

}

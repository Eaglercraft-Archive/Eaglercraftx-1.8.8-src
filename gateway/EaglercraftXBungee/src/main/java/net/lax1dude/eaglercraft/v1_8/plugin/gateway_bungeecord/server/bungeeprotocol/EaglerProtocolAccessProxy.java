package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.bungeeprotocol;

import net.md_5.bungee.protocol.DefinedPacket;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class EaglerProtocolAccessProxy {
	
	public static int getPacketId(EaglerBungeeProtocol protocol, int protocolVersion, DefinedPacket pkt, boolean server) {
		final EaglerBungeeProtocol.DirectionData prot = server ? protocol.TO_CLIENT : protocol.TO_SERVER;
		return prot.getId((Class) pkt.getClass(), protocolVersion);
	}

	public static DefinedPacket createPacket(EaglerBungeeProtocol protocol, int protocolVersion, int packetId, boolean server) {
		final EaglerBungeeProtocol.DirectionData prot = server ? protocol.TO_CLIENT : protocol.TO_SERVER;
		return prot.createPacket(packetId, protocolVersion);
	}
	
}

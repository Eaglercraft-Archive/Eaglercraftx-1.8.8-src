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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;

public class EaglerProtocolAccessProxy {

	private static final Field fieldToClient;
	private static final Field fieldToServer;
	private static final Method methodGetId;
	private static final Method methodCreatePacket;
	
	static {
		try {
			fieldToClient = Protocol.class.getDeclaredField("TO_CLIENT");
			fieldToClient.setAccessible(true);
			fieldToServer = Protocol.class.getDeclaredField("TO_SERVER");
			fieldToServer.setAccessible(true);
			methodGetId = Protocol.DirectionData.class.getDeclaredMethod("getId", Class.class, int.class);
			methodGetId.setAccessible(true);
			methodCreatePacket = Protocol.DirectionData.class.getDeclaredMethod("createPacket", int.class, int.class);
			methodCreatePacket.setAccessible(true);
		}catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	public static int getPacketId(Protocol protocol, int protocolVersion, DefinedPacket pkt, boolean server) {
		try {
			Object prot = server ? fieldToClient.get(protocol) : fieldToServer.get(protocol);
			return (int)methodGetId.invoke(prot, pkt.getClass(), protocolVersion);
		}catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public static DefinedPacket createPacket(Protocol protocol, int protocolVersion, int packetId, boolean server) {
		try {
			Object prot = server ? fieldToClient.get(protocol) : fieldToServer.get(protocol);
			return (DefinedPacket) methodCreatePacket.invoke(prot, packetId, protocolVersion);
		}catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
}
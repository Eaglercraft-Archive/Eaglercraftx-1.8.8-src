package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.CPacketRPCEnabled;

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
public class HelloPacketFactory {

	public static final byte[] BASE_HELLO_PACKET;

	static {
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			DataOutputStream dao = new DataOutputStream(bao);
			CPacketRPCEnabled pkt = new CPacketRPCEnabled(new int[] { EaglerBackendRPCProtocol.V1.vers });
			EaglerBackendRPCProtocol.INIT.writePacket(dao, EaglerBackendRPCProtocol.CLIENT_TO_SERVER, pkt);
			BASE_HELLO_PACKET = bao.toByteArray();
		}catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}

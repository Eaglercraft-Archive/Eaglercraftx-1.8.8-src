/*
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.socket;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;

public class AddressResolver {

	public static String resolveURI(ServerData input) {
		return resolveURI(input.serverIP);
	}
	
	public static String resolveURI(String input) {
		String lc = input.toLowerCase();
		if(!lc.startsWith("ws://") && !lc.startsWith("wss://")) {
			if(EagRuntime.requireSSL()) {
				input = "wss://" + input;
			}else {
				input = "ws://" + input;
			}
		}
		return input;
	}

	public static ServerAddress resolveAddressFromURI(String input) {
		String uri = resolveURI(input);
		String lc = input.toLowerCase();
		if(lc.startsWith("ws://")) {
			input = input.substring(5);
		}else if(lc.startsWith("wss://")) {
			input = input.substring(6);
		}
		int port = EagRuntime.requireSSL() ? 443: 80;
		int i = input.indexOf('/');
		if(i != -1) {
			input = input.substring(0, i);
		}
		i = input.lastIndexOf(':');
		if(i != -1) {
			try {
				port = Integer.parseInt(input.substring(i + 1));
			}catch(Throwable t) {
			}
		}
		return new ServerAddress(uri, port);
	}
	
}
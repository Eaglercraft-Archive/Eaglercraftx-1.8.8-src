package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.event;

import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.EnumSubscribeEvents;

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
public class EventWebViewMessage implements IEaglerRPCEvent {

	public static enum MessageType {
		STRING, BINARY;
	}

	public final String channelName;
	public final MessageType messageType;
	protected final byte[] messageContent;
	protected String asString = null;

	public EventWebViewMessage(String channelName, MessageType messageType, byte[] messageContent) {
		this.channelName = channelName;
		this.messageType = messageType;
		this.messageContent = messageContent;
	}

	public String getContentStr() {
		if(messageType == MessageType.STRING) {
			if(asString == null) {
				asString = new String(messageContent, StandardCharsets.UTF_8);
			}
			return asString;
		}else {
			return null;
		}
	}

	public byte[] getContentBytes() {
		return messageType == MessageType.BINARY ? messageContent : null;
	}

	@Override
	public EnumSubscribeEvents getType() {
		return EnumSubscribeEvents.EVENT_WEBVIEW_MESSAGE;
	}

}

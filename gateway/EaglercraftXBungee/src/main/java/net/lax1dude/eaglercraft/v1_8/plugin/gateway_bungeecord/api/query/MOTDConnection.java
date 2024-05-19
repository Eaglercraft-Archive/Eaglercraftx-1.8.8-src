package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query;

import java.net.InetAddress;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;

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
public interface MOTDConnection {

	boolean isClosed();
	void close();

	String getAccept();
	InetAddress getAddress();
	EaglerListenerConfig getListener();
	long getConnectionTimestamp();
	
	public default long getConnectionAge() {
		return System.currentTimeMillis() - getConnectionTimestamp();
	}
	
	void sendToUser();
	void setKeepAlive(boolean enable);
	
	String getLine1();
	String getLine2();
	List<String> getPlayerList();
	int[] getBitmap();
	int getOnlinePlayers();
	int getMaxPlayers();
	String getSubType();
	
	void setLine1(String p);
	void setLine2(String p);
	void setPlayerList(List<String> p);
	void setPlayerList(String... p);
	void setBitmap(int[] p);
	void setOnlinePlayers(int i);
	void setMaxPlayers(int i);

}

/*
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

public class QueryResponse {

	public final String responseType;
	private final Object responseData;
	public final String serverVersion;
	public final String serverBrand;
	public final String serverName;
	public final long serverTime;
	public final long clientTime;
	public final boolean serverCracked;
	public final long ping;
	
	public QueryResponse(JSONObject obj, long ping) {
		this.responseType = obj.getString("type").toLowerCase();
		this.ping = ping;
		this.responseData = obj.get("data");
		this.serverVersion = obj.getString("vers");
		this.serverBrand = obj.getString("brand");
		this.serverName = obj.getString("name");
		this.serverTime = obj.getLong("time");
		this.clientTime = EagRuntime.steadyTimeMillis();
		this.serverCracked = obj.optBoolean("cracked", false);
	}
	
	public boolean isResponseString() {
		return responseData instanceof String;
	}
	
	public boolean isResponseJSON() {
		return responseData instanceof JSONObject;
	}
	
	public String getResponseString() {
		return (String)responseData;
	}
	
	public JSONObject getResponseJSON() {
		return (JSONObject)responseData;
	}

}
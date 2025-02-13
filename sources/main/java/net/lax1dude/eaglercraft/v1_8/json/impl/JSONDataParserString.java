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

package net.lax1dude.eaglercraft.v1_8.json.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONDataParserImpl;

public class JSONDataParserString implements JSONDataParserImpl {

	public boolean accepts(Object type) {
		return type instanceof String;
	}

	@Override
	public Object parse(Object data) {
		String s = ((String)data).trim();
		try {
			if(s.indexOf('{') == 0 && s.lastIndexOf('}') == s.length() - 1) {
				return new JSONObject(s);
			}else if(s.indexOf('[') == 0 && s.lastIndexOf(']') == s.length() - 1) {
				return new JSONArray(s);
			}else if ((s.indexOf('\"') == 0 && s.lastIndexOf('\"') == s.length() - 1)
					|| (s.indexOf('\'') == 0 && s.lastIndexOf('\'') == s.length() - 1)) {
				return (new JSONObject("{\"E\":" + s + "}")).getString("E");
			}else {
				return (String)data;
			}
		}catch(JSONException ex) {
			return (String)data;
		}
	}

}
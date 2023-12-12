package net.lax1dude.eaglercraft.v1_8.json.impl;

import java.io.IOException;
import java.io.Reader;

import org.json.JSONException;

import net.lax1dude.eaglercraft.v1_8.json.JSONDataParserImpl;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

/**
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
public class JSONDataParserReader implements JSONDataParserImpl {
	
	public boolean accepts(Object type) {
		return type instanceof Reader;
	}

	@Override
	public Object parse(Object data) {
		Reader r = (Reader)data;
		StringBuilder builder = new StringBuilder();
		char[] copyBuffer = new char[2048];
		int i;
		try {
			try {
				while((i = r.read(copyBuffer)) != -1) {
					builder.append(copyBuffer, 0, i);
				}
			}finally {
				r.close();
			}
		}catch(IOException ex) {
			throw new JSONException("Could not deserialize from " + data.getClass().getSimpleName());
		}
		return JSONTypeProvider.parse(builder.toString());
	}

}

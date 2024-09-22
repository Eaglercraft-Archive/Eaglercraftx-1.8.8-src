package net.lax1dude.eaglercraft.v1_8.opengl;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;

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
public class VSHInputLayoutParser {

	public static class ShaderInput {

		public final int index;
		public final String type;
		public final String name;

		public ShaderInput(int index, String type, String name) {
			this.index = index;
			this.type = type;
			this.name = name;
		}

	}

	public static class ShaderLayoutParseException extends RuntimeException {

		public ShaderLayoutParseException(String message, Throwable cause) {
			super(message, cause);
		}

		public ShaderLayoutParseException(String message) {
			super(message);
		}

	}

	public static List<ShaderInput> getShaderInputs(String vshSource) {
		int idx1 = vshSource.indexOf("EAGLER_VSH_LAYOUT_BEGIN()");
		if(idx1 == -1) {
			throw new ShaderLayoutParseException("Could not find \"EAGLER_VSH_LAYOUT_BEGIN()\" delimiter!");
		}
		int idx2 = vshSource.indexOf("EAGLER_VSH_LAYOUT_END()", idx1 + 25);
		if(idx2 == -1) {
			throw new ShaderLayoutParseException("Could not find \"EAGLER_VSH_LAYOUT_END()\" delimiter!");
		}
		List<String> lines = EagUtils.linesList(vshSource.substring(idx1 + 25, idx2));
		List<ShaderInput> ret = new ArrayList<>();
		for(int i = 0, l = lines.size(); i < l; ++i) {
			String ln = lines.get(i);
			ln = ln.trim();
			if(ln.startsWith("EAGLER_IN(") && ln.endsWith(")")) {
				String[] tokens = ln.substring(10, ln.length() - 1).split(",", 3);
				if(tokens.length == 3) {
					int idx;
					try {
						idx = Integer.parseInt(tokens[0].trim());
					}catch(NumberFormatException ex) {
						continue;
					}
					ret.add(new ShaderInput(idx, tokens[1].trim(), tokens[2].trim()));
				}
			}
		}
		return ret;
	}

	public static void applyLayout(IProgramGL program, List<ShaderInput> layout) {
		for(int i = 0, l = layout.size(); i < l; ++i) {
			ShaderInput itm = layout.get(i);
			_wglBindAttribLocation(program, itm.index, itm.name);
		}
	}

}

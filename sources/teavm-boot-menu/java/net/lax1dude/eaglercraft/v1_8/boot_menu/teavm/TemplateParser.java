package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.text.StrTokenizer;
import org.json.JSONObject;

import com.google.common.html.HtmlEscapers;

import net.lax1dude.eaglercraft.v1_8.Base64;

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
public class TemplateParser {

	private static class State {
		private boolean evalAllowed;
		private String baseDir;
		private Map<String, String> globals;
		private boolean htmlEscape;
		private boolean strEscape;
		private boolean disableMacros;
		private boolean enableEval;
		private State(String baseDir, boolean evalAllowed, Map<String, String> globals) {
			this.baseDir = baseDir;
			this.evalAllowed = evalAllowed;
			this.globals = globals;
		}
		private State push() {
			return new State(baseDir, evalAllowed, globals);
		}
	}

	public static String loadTemplate(String content, String baseDir, boolean evalAllowed, Map<String, String> globals) throws IOException {
		return loadTemplate(content, new State(baseDir, evalAllowed, globals));
	}

	private static String loadTemplate(String content, State state) throws IOException {
		StringBuilder ret = new StringBuilder();
		int i = 0, j = 0;
		while((i = content.indexOf("{%", j)) != -1) {
			ret.append(content, j, i);
			j = i;
			i = content.indexOf("%}", j + 2);
			if(i != -1) {
				ret.append(processMacro(content.substring(j + 2, i), state));
				j = i + 2;
			}else {
				break;
			}
		}
		ret.append(content, j, content.length());
		return ret.toString();
	}

	public static class InvalidMacroException extends RuntimeException {

		public InvalidMacroException(String message, Throwable cause) {
			super(message, cause);
		}

		public InvalidMacroException(String message) {
			super(message);
		}

	}

	private static String processMacro(String content, State state) throws IOException {
		String trimmed = content.trim();
		try {
			String[] strs = (new StrTokenizer(trimmed, ' ', '`')).getTokenArray();
			if(strs.length < 1) {
				return "{%" + content + "%}";
			}
			if(strs[0].equals("disablemacros") && strs.length == 2) {
				switch(strs[1]) {
				case "on":
					if(state.disableMacros) {
						return "{%" + content + "%}";
					}else {
						state.disableMacros = true;
						return "";
					}
				case "off":
					state.disableMacros = false;
					return "";
				default:
					if(state.disableMacros) {
						return "{%" + content + "%}";
					}else {
						throw new InvalidMacroException("Unknown disablemacros mode: " + strs[1] + " (Expected: on, off)");
					}
				}
			}else if(!state.disableMacros) {
				switch(strs[0]) {
				case "embed":
					argCheck(3, strs.length);
					switch(strs[1]) {
					case "base64":
						return Base64.encodeBase64String(loadResourceBytes(state.baseDir + "/" + strs[2]));
					case "text":
						return escapeMacroResult(loadResourceString(state.baseDir + "/" + strs[2]), state);
					case "eval":
						if(state.evalAllowed) {
							return escapeMacroResult(loadTemplate(loadResourceString(state.baseDir + "/" + strs[2]), state.push()), state);
						}else {
							throw new InvalidMacroException("Template tried to eval file \"" + strs[2] + "\"! (eval is disabled)");
						}
					default:
						throw new InvalidMacroException("Unknown embed mode: " + strs[1] + " (Expected: base64, text, eval)");
					}
				case "htmlescape":
					argCheck(2, strs.length);
					switch(strs[1]) {
					case "on":
						state.htmlEscape = true;
						return "";
					case "off":
						state.htmlEscape = false;
						return "";
					default:
						throw new InvalidMacroException("Unknown htmlescape mode: " + strs[1] + " (Expected: on, off)");
					}
				case "strescape":
					argCheck(2, strs.length);
					switch(strs[1]) {
					case "on":
						state.strEscape = true;
						return "";
					case "off":
						state.strEscape = false;
						return "";
					default:
						throw new InvalidMacroException("Unknown strescape mode: " + strs[1] + " (Expected: on, off)");
					}
				case "eval":
					argCheck(2, strs.length);
					switch(strs[1]) {
					case "on":
						if(!state.evalAllowed) {
							throw new InvalidMacroException("Template tried to enable eval! (eval is disabled)");
						}
						state.enableEval = true;
						return "";
					case "off":
						state.enableEval = false;
						return "";
					default:
						throw new InvalidMacroException("Unknown eval mode: " + strs[1] + " (Expected: on, off)");
					}
				case "global":
					argCheck(2, 3, strs.length);
					String ret = state.globals.get(strs[1]);
					if(ret == null) {
						if(strs.length == 3) {
							ret = strs[2];
						}else {
							throw new InvalidMacroException("Unknown global \"" + strs[1] + "\"! (Available: " + String.join(", ", state.globals.keySet()) + ")");
						}
					}
					return escapeMacroResult(ret, state);
				case "property":
					argCheck(2, 3, strs.length);
					ret = System.getProperty(strs[1]);
					if(ret == null) {
						if(strs.length == 3) {
							ret = strs[2];
						}else {
							throw new InvalidMacroException("Unknown system property \"" + strs[1] + "\"!");
						}
					}
					return escapeMacroResult(ret, state);
				case "text":
					argCheck(2, strs.length);
					return escapeMacroResult(strs[1], state);
//				case "translate":
//					argCheckMin(2, strs.length);
//					String[] additionalArgs = new String[strs.length - 2];
//					System.arraycopy(strs, 2, additionalArgs, 0, additionalArgs.length);
//					return escapeMacroResult(BungeeCord.getInstance().getTranslation(strs[1], (Object[])additionalArgs), state);
				default:
					return "{%" + content + "%}";
				}
			}else {
				return "{%" + content + "%}";
			}
		}catch(InvalidMacroException ex) {
			throw new IOException("Invalid macro: {% " + trimmed + " %}, message: " + ex.getMessage(), ex);
		}catch(Throwable th) {
			throw new IOException("Error processing: {% " + trimmed + " %}, raised: " + th.toString(), th);
		}
	}

	private static String escapeMacroResult(String str, State state) throws IOException {
		if(str.length() > 0) {
			if(state.evalAllowed && state.enableEval) {
				str = loadTemplate(str, state.push());
			}
			if(state.strEscape) {
				str = (new JSONObject()).put("e", str).toString(); //rip
				if(str.length() >= 8) {
					str = str.substring(6, str.length() - 2);
				}
			}
			if(state.htmlEscape) {
				str = HtmlEscapers.htmlEscaper().escape(str);
			}
		}
		return str;
	}

	private static void argCheck(int expect, int actual) {
		if(expect != actual) {
			throw new InvalidMacroException("Wrong number of arguments (" + actual + ", expected " + expect + ")");
		}
	}

	private static void argCheck(int expectMin, int expectMax, int actual) {
		if(expectMin > actual || expectMax < actual) {
			throw new InvalidMacroException("Wrong number of arguments (" + actual + ", expected " + expectMin + " to " + expectMax + ")");
		}
	}

	private static void argCheckMin(int expectMin, int actual) {
		if(expectMin > actual) {
			throw new InvalidMacroException("Wrong number of arguments (expected " + expectMin + " or more, got " + actual + ")");
		}
	}

	private static byte[] loadResourceBytes(String path) {
		byte[] res = BootMenuAssets.loadResourceBytes(path);
		if(res == null) {
			throw new InvalidMacroException("Unknown file: " + path);
		}
		return res;
	}

	private static String loadResourceString(String path) {
		String res = BootMenuAssets.loadResourceString(path);
		if(res == null) {
			throw new InvalidMacroException("Unknown file: " + path);
		}
		return res;
	}

}

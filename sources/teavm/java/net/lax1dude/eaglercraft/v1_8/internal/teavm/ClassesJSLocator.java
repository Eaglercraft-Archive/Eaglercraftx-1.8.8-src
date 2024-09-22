package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.jso.dom.html.HTMLScriptElement;
import org.teavm.jso.dom.xml.Element;
import org.teavm.jso.dom.xml.NodeList;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class ClassesJSLocator {

	private static final Logger logger = LogManager.getLogger("ClassesJSLocator");

	public static String resolveClassesJSFromThrowable() {
		String str = resolveClassesJSFromThrowable0();
		if(str != null && str.equalsIgnoreCase(PlatformRuntime.win.getLocation().getFullURL())) {
			return null;
		}
		return str;
	}

	private static String resolveClassesJSFromThrowable0() {
		String str = TeaVMUtils.dumpJSStackTrace();
		String[] frames = EagUtils.splitPattern.split(str);
		if("Error".equals(frames[0])) {
			// V8 stack trace
			if(frames.length > 1) {
				String framesTrim = frames[1].trim();
				if(framesTrim.startsWith("at")) {
					//definitely V8
					int i = framesTrim.indexOf('(');
					int j = framesTrim.indexOf(')');
					if(i != -1 && j != -1 && i < j) {
						return tryResolveClassesSourceFromFrame(framesTrim.substring(i + 1, j));
					}
				}
			}
		}else {
			// Mozilla/WebKit stack trace
			String framesTrim = frames[0].trim();
			int i = framesTrim.indexOf('@');
			if(i != -1) {
				return tryResolveClassesSourceFromFrame(framesTrim.substring(i + 1));
			}
		}
		return null;
	}

	private static String tryResolveClassesSourceFromFrame(String fileLineCol) {
		int i = fileLineCol.lastIndexOf(':');
		if(i > 0) {
			i = fileLineCol.lastIndexOf(':', i - 1);
		}
		if(i != -1) {
			return fileLineCol.substring(0, i);
		}
		return null;
	}

	public static HTMLScriptElement resolveClassesJSFromInline() {
		NodeList<Element> elements = PlatformRuntime.doc.getElementsByTagName("script");
		for(int i = 0, l = elements.getLength(); i < l; ++i) {
			HTMLScriptElement tag = (HTMLScriptElement)elements.get(i);
			String scriptSrc = tag.getText();
			if(scriptSrc != null && scriptSrc.length() > 1024 * 1024) {
				// I'm not feeling very creative tonight
				int j = scriptSrc.indexOf("var $rt_seed=2463534242;");
				if(j > 0 && j < 2048 && scriptSrc.indexOf("$rt_createNumericArray(") != -1) {
					logger.warn("Could not locate classes.js through conventional means, however an inline script tag was found on the page that (probably) contains a TeaVM program");
					return tag;
				}
			}
		}
		return null;
	}

}

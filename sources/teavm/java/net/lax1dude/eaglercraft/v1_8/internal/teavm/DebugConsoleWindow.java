package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.LinkedList;
import java.util.List;

import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLBodyElement;
import org.teavm.jso.dom.html.HTMLCollection;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;

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
public class DebugConsoleWindow {

	private static class LogMessage {
		
		private final boolean err;
		private final String msg;
		
		public LogMessage(boolean err, String msg) {
			this.err = err;
			this.msg = msg;
		}
		
	}

	private static final int bufferSpoolSize = 256;
	private static final int windowMaxMessages = 2048;

	private static final List<LogMessage> messageBuffer = new LinkedList();

	public static Window parent = null;
	public static Window logger = null;
	private static HTMLDocument loggerDoc = null;
	private static HTMLBodyElement loggerBody = null;
	private static HTMLElement loggerMessageContainer = null;

	public static void initialize(Window parentWindow) {
		parent = parentWindow;
		parent.addEventListener("unload", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				destroyWindow();
			}
		});
		if("true".equals(parent.getLocalStorage().getItem("_eaglercraftX.showDebugConsole"))) {
			showDebugConsole0();
		}
	}

	public static void showDebugConsole() {
		parent.getLocalStorage().setItem("_eaglercraftX.showDebugConsole", "true");
		showDebugConsole0();
	}

	@JSBody(params = { "doc", "str" }, script = "doc.write(str);doc.close();")
	private static native void documentWrite(HTMLDocument doc, String str);

	private static void showDebugConsole0() {
		if(logger == null) {
			int w = (int)(1000 * parent.getDevicePixelRatio());
			int h = (int)(400 * parent.getDevicePixelRatio());
			int x = (parent.getScreen().getWidth() - w) / 2;
			int y = (parent.getScreen().getHeight() - h) / 2;
			logger = parent.open("", "_blank", "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + ",menubar=0,status=0,titlebar=0,toolbar=0");
			if(logger == null) {
				LogManager.getLogger("DebugConsoleWindow").error("Logger popup was blocked!");
				Window.alert("ERROR: Popup blocked!\n\nPlease make sure you have popups enabled for this site!");
				return;
			}
			logger.focus();
			documentWrite(logger.getDocument(), "<!DOCTYPE html><html><head><meta charset=\"UTF-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
					+ "<title>Debug Console</title><link type=\"image/png\" rel=\"shortcut icon\" href=\"" + PlatformApplication.faviconURLTeaVM() + "\" />"
					+ "</head><body style=\"overflow-x:hidden;overflow-y:scroll;padding:0px;\"><p id=\"loggerMessageContainer\" style=\"overflow-wrap:break-word;white-space:pre-wrap;font:14px monospace;padding:10px;\"></p></body></html>");
			loggerDoc = logger.getDocument();
			loggerBody = loggerDoc.getBody();
			loggerMessageContainer = loggerDoc.getElementById("loggerMessageContainer");
			synchronized(messageBuffer) {
				for(LogMessage msg : messageBuffer) {
					appendLogMessage(msg.msg + "\n", msg.err ? "#DD0000" : "#000000");
				}
				messageBuffer.clear();
			}
			scrollToEnd0(logger, loggerDoc);
			EventListener<Event> unloadListener = new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if(logger != null) {
						logger = null;
						parent.getLocalStorage().setItem("_eaglercraftX.showDebugConsole", "false");
					}
				}
			};
			logger.addEventListener("beforeunload", unloadListener);
			logger.addEventListener("unload", unloadListener);
		}else {
			logger.focus();
		}
	}

	public static void addLogMessage(String text, boolean isErr) {
		if(logger == null) {
			synchronized(messageBuffer) {
				if(logger == null) {
					messageBuffer.add(new LogMessage(isErr, text));
					while(messageBuffer.size() > bufferSpoolSize) {
						messageBuffer.remove(0);
					}
					return;
				}
			}
		}
		appendLogMessageAndScroll(text + "\n", isErr ? "#DD0000" : "#000000");
	}

	private static void appendLogMessageAndScroll(String text, String color) {
		boolean b = isScrollToEnd(logger, loggerDoc);
		appendLogMessage(text, color);
		if(b) {
			scrollToEnd0(logger, loggerDoc);
		}
	}

	private static void appendLogMessage(String text, String color) {
		HTMLElement el = loggerDoc.createElement("span");
		el.setInnerText(text);
		el.getStyle().setProperty("color", color);
		loggerMessageContainer.appendChild(el);
		HTMLCollection children = loggerMessageContainer.getChildren();
		while(children.getLength() > windowMaxMessages) {
			children.get(0).delete();
		}
	}

	@JSBody(params = { "win", "doc" }, script = "return (win.innerHeight + win.pageYOffset) >= doc.body.offsetHeight;")
	private static native boolean isScrollToEnd(Window win, HTMLDocument doc);

	@JSBody(params = { "win", "doc" }, script = "setTimeout(function(){win.scrollTo(0, doc.body.scrollHeight || doc.body.clientHeight);}, 1);")
	private static native void scrollToEnd0(Window win, HTMLDocument doc);

	public static void destroyWindow() {
		if(logger != null) {
			Window w = logger;
			logger = null;
			w.close();
		}
	}

	public static boolean isShowingDebugConsole() {
		return logger != null;
	}

}

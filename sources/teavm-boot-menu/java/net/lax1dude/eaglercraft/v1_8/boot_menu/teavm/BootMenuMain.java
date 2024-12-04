package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import com.google.common.collect.Lists;
import com.google.common.html.HtmlEscapers;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.LegacyKeycodeTranslator;
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
public class BootMenuMain {

	private static final Logger logger = LogManager.getLogger("BootMenuMain");

	public static Window win = null;
	public static HTMLDocument doc = null;
	public static HTMLElement parent = null;
	public static BootMenuDOM bootMenuDOM = null;
	public static MenuState currentState = null;
	public static BootMenuMetadata bootMenuMetadata = null;
	public static BootMenuDatastore bootMenuDatastore = null;
	public static BootMenuDataManager bootMenuDataManager = null;
	public static BootMenuFatOfflineLoader bootMenuFatOfflineLoader = null;

	private static EventListener<KeyboardEvent> windowKeyDownListener = null;
	private static EventListener<KeyboardEvent> windowKeyUpListener = null;

	private static final List<Runnable> eventQueue = new LinkedList<>();

	private static boolean runUpdateLoop = true;

	@JSBody(params = { "e" }, script = "return (typeof e.which === \"number\") ? e.which : ((typeof e.keyCode === \"number\") ? e.keyCode : 0);")
	private static native int getWhich(KeyboardEvent e);

	@JSBody(params = { "evt" }, script = "return (typeof evt.key === \"string\");")
	private static native boolean hasKeyVar(KeyboardEvent evt);

	@JSBody(params = { "evt" }, script = "return (typeof evt.code === \"string\");")
	private static native boolean hasCodeVar(KeyboardEvent evt);

	public static void launchMenu(Window parentWindow, HTMLElement parentElement) {
		win = parentWindow;
		doc = parentWindow.getDocument();
		parent = parentElement;
		logger.info("Integrated boot menu is loading");
		String renderedMarkup;
		try {
			renderedMarkup = TemplateLoader.loadTemplate("/assets/eagler/boot_menu/boot_menu_markup.html");
		}catch(IOException ex) {
			logger.error("Failed to render template!");
			logger.error(ex);
			parentElement.setInnerHTML("<div style=\"padding:10px;\"><h1>Failed to render template!</h1><h3>"
					+ HtmlEscapers.htmlEscaper().escape(ex.toString())
					+ "</h3><p>Check the console for more details</p></div>");
			return;
		}
		parentElement.setInnerHTML(renderedMarkup);
		bootMenuDOM = new BootMenuDOM(parentElement);
		logger.info("Registering event handlers");
		win.addEventListener("keydown", windowKeyDownListener = (evt) -> {
			if(currentState != null) {
				LegacyKeycodeTranslator.LegacyKeycode keyCode = null;
				Map<String,LegacyKeycodeTranslator.LegacyKeycode> keyCodeTranslatorMap = PlatformInput.getKeyCodeTranslatorMapTeaVM();
				if(keyCodeTranslatorMap != null && hasCodeVar(evt)) {
					keyCode = keyCodeTranslatorMap.get(evt.getCode());
				}
				final int which;
				if(keyCode != null) {
					which = keyCode.keyCode;
				}else {
					which = getWhich(evt);
				}
				if(!evt.isRepeat()) {
					runLater(() -> {
						if(currentState != null) {
							currentState.doHandleKeyDown(which);
						}
					});
				}else {
					runLater(() -> {
						if(currentState != null) {
							currentState.doHandleKeyRepeat(which);
						}
					});
				}
			}
		});
		win.addEventListener("keyup", windowKeyUpListener = (evt) -> {
			if(currentState != null) {
				if(!evt.isRepeat()) {
					LegacyKeycodeTranslator.LegacyKeycode keyCode = null;
					Map<String,LegacyKeycodeTranslator.LegacyKeycode> keyCodeTranslatorMap = PlatformInput.getKeyCodeTranslatorMapTeaVM();
					if(keyCodeTranslatorMap != null && hasCodeVar(evt)) {
						keyCode = keyCodeTranslatorMap.get(evt.getCode());
					}
					final int which;
					if(keyCode != null) {
						which = keyCode.keyCode;
					}else {
						which = getWhich(evt);
					}
					runLater(() -> {
						if(currentState != null) {
							currentState.doHandleKeyUp(which);
						}
					});
				}
			}
		});
		bootMenuDOM.registerEventHandlers();
		bootMenuMetadata = new BootMenuMetadata("/assets/eagler/boot_menu/");
		bootMenuDatastore = BootMenuDatastore.openDatastore(); //TODO: error handling
		bootMenuDataManager = new BootMenuDataManager(bootMenuDatastore);
		bootMenuDOM.header_title.setInnerText(bootMenuDataManager.confMenuTitle);
		bootMenuFatOfflineLoader = new BootMenuFatOfflineLoader(parentWindow.getDocument().getHead());
		logger.info("Entering boot menu display state");
		eventQueue.clear();
		changeState(new MenuStateBoot(!BootMenuEntryPoint.wasManuallyInvoked));
		enterUpdateLoop();
	}

	private static void enterUpdateLoop() {
		runUpdateLoop = true;
		while(runUpdateLoop) {
			if(currentState != null) {
				currentState.doUpdate();
			}
			List<Runnable> eq = null;
			synchronized(eventQueue) {
				if(!eventQueue.isEmpty()) {
					eq = Lists.newArrayList(eventQueue);
					eventQueue.clear();
				}
			}
			if(eq != null) {
				for(Runnable run : eq) {
					try {
						run.run();
					}catch(Throwable t) {
						logger.error("Caught error in event queue!");
						logger.error(t);
					}
				}
			}
			EagUtils.sleep(50);
		}
	}

	public static void runLater(Runnable run) {
		if(runUpdateLoop) {
			synchronized(eventQueue) {
				eventQueue.add(run);
			}
		}
	}

	public static void runLaterMS(Runnable run, int millis) {
		Window.setTimeout(() -> runLater(run), millis);
	}

	public static void unregisterEventHandlers() {
		if(windowKeyDownListener != null) {
			win.removeEventListener("keydown", windowKeyDownListener);
			windowKeyDownListener = null;
		}
		if(windowKeyUpListener != null) {
			win.removeEventListener("keyup", windowKeyUpListener);
			windowKeyUpListener = null;
		}
	}

	public static void changeState(MenuState newState) {
		if(currentState != null) {
			currentState.doExitState();
			currentState = null;
		}
		currentState = newState;
		if(newState != null) {
			newState.doEnterState();
		}
	}

	public static void continueBootToOriginClient() {
		continueBootToOriginClient(BootMenuMain::sanitizeEaglercraftXOpts);
	}

	public static void continueBootToOriginClient(Runnable doBeforeBoot) {
		destroyBootMenuRuntime();
		stopEventLoop();
		BootMenuEntryPoint.bootOriginClient(doBeforeBoot);
	}

	@JSBody(params = { }, script = "try { window.eaglercraftXOptsHints.bootMenuBlocksUnsignedClients = true; }catch(_ex){} try { window.eaglercraftXOpts.bootMenuBlocksUnsignedClients = true; }catch(_ex){}")
	private static native void doSanitizeSignatureRequired();

	public static void sanitizeEaglercraftXOpts() {
		if(IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients()) {
			doSanitizeSignatureRequired();
		}
	}

	public static String createRootElementForClient() {
		EaglercraftRandom randomCharGenerator = new EaglercraftRandom();
		char[] randomChars = new char[16];
		String charSel = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i = 0; i < randomChars.length; ++i) {
			randomChars[i] = charSel.charAt(randomCharGenerator.nextInt(charSel.length()));
		}
		String randomId = "game_frame_" + new String(randomChars);
		HTMLElement parentTemp = parent;
		HTMLElement newRoot = doc.createElement("div");
		newRoot.getStyle().setProperty("width", "100%");
		newRoot.getStyle().setProperty("height", "100%");
		newRoot.setAttribute("id", randomId);
		destroyBootMenuRuntime();
		parentTemp.appendChild(newRoot);
		return randomId;
		
	}

	public static void destroyBootMenuRuntime() {
		unregisterEventHandlers();
		bootMenuDOM = null;
		currentState = null;
		bootMenuMetadata = null;
		if(bootMenuDatastore != null) {
			bootMenuDatastore.closeDatastore();
			bootMenuDatastore = null;
		}
		bootMenuDataManager = null;
		bootMenuFatOfflineLoader = null;
		BootMenuAssets.freeBootMenuResourceRepo();
		win = null;
		doc = null;
		while(parent.getLastChild() != null) {
			parent.removeChild(parent.getLastChild());
		}
		parent = null;
	}

	public static void stopEventLoop() {
		runUpdateLoop = false;
	}

	public static void fireChangeEvent(HTMLElement element) {
		if(currentState != null) {
			runLater(() -> {
				if(currentState != null) {
					currentState.doHandleOnChange(element);
				}
			});
		}
	}

	public static void fireClickEvent(HTMLElement element) {
		if(currentState != null) {
			runLater(() -> {
				if(currentState != null) {
					currentState.doHandleOnClick(element);
				}
			});
		}
	}

	public static void fireMouseOverEvent(HTMLElement element) {
		if(currentState != null) {
			runLater(() -> {
				if(currentState != null) {
					currentState.doHandleOnMouseOver(element);
				}
			});
		}
	}

}

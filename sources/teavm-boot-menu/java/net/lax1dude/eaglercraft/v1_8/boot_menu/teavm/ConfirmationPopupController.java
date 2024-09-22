package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.dom.html.HTMLElement;

import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;

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
public abstract class ConfirmationPopupController<T extends ConfirmationPopupController.SelectionOption> {

	public static interface SelectionOption {

		String getName();
		
		default boolean getEnabled() {
			return true;
		}

	}

	public static class SelectionOptionEnum<E> implements SelectionOption {

		protected final String displayName;
		protected final boolean enabled;
		protected final E itemEnum;

		public SelectionOptionEnum(String displayName, boolean enabled, E itemEnum) {
			this.displayName = displayName;
			this.enabled = enabled;
			this.itemEnum = itemEnum;
		}

		public SelectionOptionEnum(String displayName, E itemEnum) {
			this.displayName = displayName;
			this.enabled = true;
			this.itemEnum = itemEnum;
		}

		@Override
		public String getName() {
			return displayName;
		}

		@Override
		public boolean getEnabled() {
			return enabled;
		}

		public E getEnum() {
			return itemEnum;
		}

	}

	protected static class ConfirmationOptionInstance<E extends ConfirmationPopupController.SelectionOption> {
		
		protected final E listItem;
		protected final HTMLElement element;
		
		protected ConfirmationOptionInstance(E listItem, HTMLElement element) {
			this.listItem = listItem;
			this.element = element;
		}
		
	}

	protected final HTMLElement parent;
	protected final List<T> optionList;
	protected final List<ConfirmationOptionInstance<T>> optionEnableList;
	protected int currentSelected = -1;

	public ConfirmationPopupController(HTMLElement parent, List<T> optionList) {
		this.parent = parent;
		this.optionList = optionList;
		this.optionEnableList = new ArrayList<>(optionList.size());
	}

	public void setup() {
		optionEnableList.clear();
		parent.setInnerHTML("");
		StringBuilder htmlBuilder = new StringBuilder();
		Escaper escaper = HtmlEscapers.htmlEscaper();
		currentSelected = -1;
		for(int i = 0, l = optionList.size(); i < l; ++i) {
			T itm = optionList.get(i);
			if(i > 0) {
				htmlBuilder.append(" &emsp; ");
			}
			htmlBuilder.append(
					"<span class=\"_eaglercraftX_boot_menu_popup_confirm_opt _eaglercraftX_boot_menu_popup_confirm_opt"
							+ i + "\">&nbsp;&lt;&nbsp;");
			htmlBuilder.append(escaper.escape(itm.getName()));
			htmlBuilder.append("&nbsp;&gt;&nbsp;</span>");
		}
		parent.setInnerHTML(htmlBuilder.toString());
		for(int i = 0, l = optionList.size(); i < l; ++i) {
			T itm = optionList.get(i);
			HTMLElement el = parent.querySelector("._eaglercraftX_boot_menu_popup_confirm_opt" + i);
			if(el == null) {
				throw new RuntimeException("Failed to select element from page: ._eaglercraftX_boot_menu_popup_confirm_opt" + i);
			}
			if(itm.getEnabled()) {
				if(currentSelected == -1) {
					currentSelected = 0;
					el.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "popup_confirm_opt_selected");
				}
				final ConfirmationOptionInstance<T> newInstance = new ConfirmationOptionInstance<T>(itm, el);
				final int ii = optionEnableList.size();
				el.addEventListener("mouseover", (evt) -> {
					BootMenuMain.runLater(() -> {
						setSelected(ii);
					});
				});
				el.addEventListener("click", (evt) -> {
					BootMenuMain.runLater(() -> {
						optionSelected(newInstance.listItem);
					});
				});
				optionEnableList.add(newInstance);
			}else {
				el.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "popup_confirm_opt_disabled");
			}
		}
	}

	public void destroy() {
		parent.setInnerHTML("");
		currentSelected = -1;
		optionEnableList.clear();
	}

	public void setSelected(int idx) {
		int listLen = optionEnableList.size();
		if(listLen == 0) {
			idx = -1;
		}else if(idx >= listLen) {
			idx = listLen - 1;
		}else if(idx < 0) {
			idx = 0;
		}
		if(idx == currentSelected) {
			return;
		}
		if(currentSelected >= 0 && currentSelected < optionEnableList.size()) {
			optionEnableList.get(currentSelected).element.getClassList().remove(BootMenuConstants.cssClassPrefixBootMenu + "popup_confirm_opt_selected");
		}
		currentSelected = idx;
		if(idx != -1) {
			optionEnableList.get(idx).element.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "popup_confirm_opt_selected");
		}
	}

	public void handleKeyDown(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ARROW_LEFT) {
			setSelected(currentSelected - 1);
		}else if(keyCode == KeyCodes.DOM_KEY_ARROW_RIGHT) {
			setSelected(currentSelected + 1);
		}else if(keyCode == KeyCodes.DOM_KEY_ENTER) {
			if(currentSelected >= 0 && currentSelected < optionEnableList.size()) {
				optionSelected(optionEnableList.get(currentSelected).listItem);
			}
		}
	}

	public void handleKeyRepeat(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ARROW_LEFT) {
			setSelected(currentSelected - 1);
		}else if(keyCode == KeyCodes.DOM_KEY_ARROW_RIGHT) {
			setSelected(currentSelected + 1);
		}
	}

	protected abstract void optionSelected(T item);
}

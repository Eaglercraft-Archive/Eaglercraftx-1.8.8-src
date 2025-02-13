/*
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

package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.teavm.jso.dom.html.HTMLElement;

public abstract class SelectionListController<T extends SelectionListController.ListItem> {

	public static interface ListItem {
		
		String getName();
		
		default boolean getEnabled() {
			return true;
		}
		
		default boolean getAlwaysSelected() {
			return true;
		}
		
	}

	public static class ListItemEnum<E> implements ListItem {

		protected final String displayName;
		protected final boolean enabled;
		protected final E itemEnum;

		public ListItemEnum(String displayName, boolean enabled, E itemEnum) {
			this.displayName = displayName;
			this.enabled = enabled;
			this.itemEnum = itemEnum;
		}

		public ListItemEnum(String displayName, E itemEnum) {
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

	protected static class ListItemInstance<E extends SelectionListController.ListItem> {
		
		protected final E listItem;
		protected final HTMLElement element;
		protected int index;
		protected boolean userVal = false;
		
		protected ListItemInstance(E listItem, HTMLElement element, int index) {
			this.listItem = listItem;
			this.element = element;
			this.index = index;
		}
		
	}

	protected final HTMLElement parent;
	protected final List<T> selectionList;
	protected final List<ListItemInstance<T>> selectionEnableList;
	protected int currentSelected = -1;
	protected boolean cursorEventsSuspended = false;

	public SelectionListController(HTMLElement parent, List<T> selectionList) {
		this.parent = parent;
		this.selectionList = selectionList;
		this.selectionEnableList = new ArrayList<>(selectionList.size());
	}

	public void setup() {
		selectionEnableList.clear();
		parent.setInnerHTML("");
		currentSelected = -1;
		for(int i = 0, l = selectionList.size(); i < l; ++i) {
			T itm = selectionList.get(i);
			HTMLElement el = BootMenuMain.doc.createElement("p");
			el.setInnerText(itm.getName());
			el.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "content_item");
			if(itm.getEnabled()) {
				if(currentSelected == -1) {
					currentSelected = 0;
					el.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "content_item_selected");
				}
				final int ii = selectionEnableList.size();
				final ListItemInstance<T> newInstance = new ListItemInstance<T>(itm, el, ii);
				el.addEventListener("mouseover", (evt) -> {
					BootMenuMain.runLater(() -> {
						if(!cursorEventsSuspended) {
							setSelected(newInstance.index);
						}
					});
				});
				el.addEventListener("click", (evt) -> {
					BootMenuMain.runLater(() -> {
						if(!cursorEventsSuspended) {
							itemSelectedLow(newInstance);
						}
					});
				});
				selectionEnableList.add(newInstance);
			}else {
				el.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "content_item_disabled");
			}
			parent.appendChild(el);
		}
	}

	public void destroy() {
		parent.setInnerHTML("");
		currentSelected = -1;
		selectionEnableList.clear();
	}

	public void setSelected(int idx) {
		int listLen = selectionEnableList.size();
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
		if(currentSelected >= 0 && currentSelected < selectionEnableList.size()) {
			selectionEnableList.get(currentSelected).element.getClassList().remove(BootMenuConstants.cssClassPrefixBootMenu + "content_item_selected");
		}
		currentSelected = idx;
		if(idx != -1) {
			selectionEnableList.get(idx).element.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "content_item_selected");
		}
	}

	public T getSelected() {
		if(currentSelected >= 0 && currentSelected < selectionEnableList.size()) {
			return selectionEnableList.get(currentSelected).listItem;
		}else {
			return null;
		}
	}

	public void moveEntryUp(int index) {
		if(index < 1 || index >= selectionEnableList.size()) return;
		if(currentSelected == index) {
			--currentSelected;
		}
		ListItemInstance<T> etr = selectionEnableList.get(index);
		ListItemInstance<T> etr2 = selectionEnableList.get(index - 1);
		Collections.swap(selectionEnableList, index, index - 1);
		etr.index--;
		etr2.index++;
		parent.removeChild(etr.element);
		parent.insertBefore(etr.element, etr2.element);
	}

	public void moveEntryDown(int index) {
		if(index < 0 || index >= selectionEnableList.size() - 1) return;
		if(currentSelected == index) {
			++currentSelected;
		}
		ListItemInstance<T> etr = selectionEnableList.get(index);
		Collections.swap(selectionEnableList, index, index + 1);
		etr.index++;
		selectionEnableList.get(index + 1).index--;
		parent.removeChild(etr.element);
		if(index >= selectionEnableList.size() - 2) {
			parent.appendChild(etr.element);
		}else {
			ListItemInstance<T> etr2 = selectionEnableList.get(index + 2);
			parent.insertBefore(etr.element, etr2.element);
		}
	}

	public void handleKeyDown(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ARROW_UP) {
			setSelected(currentSelected - 1);
		}else if(keyCode == KeyCodes.DOM_KEY_ARROW_DOWN) {
			setSelected(currentSelected + 1);
		}else if(keyCode == KeyCodes.DOM_KEY_ENTER) {
			fireSelect();
		}
	}

	protected void fireSelect() {
		if(currentSelected >= 0 && currentSelected < selectionEnableList.size()) {
			itemSelectedLow(selectionEnableList.get(currentSelected));
		}
	}

	public void handleKeyRepeat(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ARROW_UP) {
			setSelected(currentSelected - 1);
		}else if(keyCode == KeyCodes.DOM_KEY_ARROW_DOWN) {
			setSelected(currentSelected + 1);
		}
	}

	public void setCursorEventsSuspended(boolean sus) {
		cursorEventsSuspended = sus;
	}

	protected void itemSelectedLow(ListItemInstance<T> item) {
		itemSelected(item.listItem);
	}

	protected abstract void itemSelected(T item);

}
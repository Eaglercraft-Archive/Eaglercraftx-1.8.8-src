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

import java.util.List;
import java.util.function.Consumer;

import org.teavm.jso.dom.html.HTMLElement;

public abstract class MenuPopupStateSelection<T> extends MenuState {

	public static class SelectionItem<E> implements SelectionListController.ListItem {

		protected final String name;
		protected final E enumValue;

		public SelectionItem(E enumValue, String name) {
			this.name = name;
			this.enumValue = enumValue;
		}

		public SelectionItem(E enumValue) {
			this.name = enumValue.toString();
			this.enumValue = enumValue;
		}

		@Override
		public String getName() {
			return name;
		}

	}

	protected final String title;
	protected final List<SelectionItem<T>> items;
	protected SelectionListController<SelectionItem<T>> selectionController;

	public MenuPopupStateSelection(String title, List<SelectionItem<T>> items) {
		this.title = title;
		this.items = items;
		this.selectionController = new SelectionListController<SelectionItem<T>>(BootMenuMain.bootMenuDOM.popup_selection, items) {
			@Override
			protected void itemSelected(SelectionItem<T> item) {
				MenuPopupStateSelection.this.itemSelected(item.enumValue);
			}
		};
	}

	public static <T> MenuPopupStateSelection<T> createHelper(String title, List<T> items, Consumer<T> selectCallback) {
		return new MenuPopupStateSelection<T>(title, items.stream().map(SelectionItem<T>::new).toList()) {
			@Override
			protected void itemSelected(T item) {
				selectCallback.accept(item);
			}
		};
	}

	@Override
	protected void enterState() {
		selectionController.setup();
		BootMenuMain.bootMenuDOM.popup_selection_title.setInnerText(title);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup_view_selection);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup);
	}

	@Override
	protected void exitState() {
		selectionController.destroy();
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.popup_view_selection);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.popup);
	}

	@Override
	protected void enterPopupBlockingState() {
		throw new IllegalStateException();
	}

	@Override
	protected void exitPopupBlockingState() {
		throw new IllegalStateException();
	}

	@Override
	protected void handleKeyDown(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ESCAPE) {
			BootMenuMain.currentState.changePopupState(null);
			return;
		}
		selectionController.handleKeyDown(keyCode);
	}

	@Override
	protected void handleKeyUp(int keyCode) {
		
	}

	@Override
	protected void handleKeyRepeat(int keyCode) {
		selectionController.handleKeyRepeat(keyCode);
	}

	@Override
	protected void handleOnChanged(HTMLElement htmlElement) {
		
	}

	@Override
	protected void handleOnClick(HTMLElement htmlElement) {
		
	}

	@Override
	protected void handleOnMouseOver(HTMLElement htmlElement) {
		
	}

	@Override
	protected void update() {
		
	}

	protected abstract void itemSelected(T item);

}
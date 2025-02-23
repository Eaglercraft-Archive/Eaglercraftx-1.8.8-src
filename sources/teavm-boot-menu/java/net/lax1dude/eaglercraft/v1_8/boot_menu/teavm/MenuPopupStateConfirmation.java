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

import org.teavm.jso.dom.html.HTMLElement;

public abstract class MenuPopupStateConfirmation<E> extends MenuState {

	public static enum EnumYesNoHelper {
		YES("Yes"),
		NO("No");
		
		private final String str;
		
		private EnumYesNoHelper(String str) {
			this.str = str;
		}
		
		@Override
		public String toString() {
			return str;
		}
	}

	protected class SelectionItem implements ConfirmationPopupController.SelectionOption {

		protected E enumValue;

		protected SelectionItem(E enumValue) {
			this.enumValue = enumValue;
		}

		@Override
		public String getName() {
			return enumValue.toString();
		}

	}

	protected final String title;
	protected final List<E> options;
	protected final ConfirmationPopupController<SelectionItem> popupController;

	public MenuPopupStateConfirmation(String title, List<E> options) {
		this.title = title;
		this.options = options;
		this.popupController = new ConfirmationPopupController<SelectionItem>(
				BootMenuMain.bootMenuDOM.popup_confirm_opts,
				options.stream().map(SelectionItem::new).toList()) {
			@Override
			protected void optionSelected(SelectionItem item) {
				MenuPopupStateConfirmation.this.selectCallback(item.enumValue);
			}
		};
	}

	@Override
	protected void enterState() {
		popupController.setup();
		BootMenuMain.bootMenuDOM.popup_confirm_title.setInnerText(title);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup_view_confirm);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup);
	}

	@Override
	protected void exitState() {
		popupController.destroy();
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.popup_view_confirm);
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
		popupController.handleKeyDown(keyCode);
	}

	@Override
	protected void handleKeyUp(int keyCode) {
		
	}

	@Override
	protected void handleKeyRepeat(int keyCode) {
		popupController.handleKeyRepeat(keyCode);
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

	protected abstract void selectCallback(E enumValue);

}
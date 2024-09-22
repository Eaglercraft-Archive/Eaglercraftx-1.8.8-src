package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

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
public abstract class MenuPopupStateEditInteger extends MenuState {

	protected final InputPopupController inputPopupController;
	protected final String menuTitle;
	protected final int defaultValue;

	public MenuPopupStateEditInteger(String menuTitle, int defaultValue) {
		this.inputPopupController = new InputPopupController((HTMLInputElement) BootMenuMain.bootMenuDOM.popup_input_val,
				true, BootMenuMain.bootMenuDOM.popup_input_opt_cancel, BootMenuMain.bootMenuDOM.popup_input_opt_done) {

					@Override
					protected void onSave(HTMLInputElement inputField) {
						int i = 0;
						try {
							i = Integer.parseInt(inputField.getValue().trim());
						}catch(NumberFormatException ex) {
						}
						MenuPopupStateEditInteger.this.onSave(i);
					}

					@Override
					protected void onCancel() {
						MenuPopupStateEditInteger.this.onCancel();
					}

		};
		this.menuTitle = menuTitle;
		this.defaultValue = defaultValue;
	}

	@Override
	protected void enterState() {
		BootMenuMain.bootMenuDOM.popup_input_title.setInnerText(menuTitle);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup_view_input);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup);
		inputPopupController.setup(Integer.toString(defaultValue));
	}

	@Override
	protected void exitState() {
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.popup_view_input);
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
		inputPopupController.handleKeyDown(keyCode);
	}

	@Override
	protected void handleKeyUp(int keyCode) {
		
	}

	@Override
	protected void handleKeyRepeat(int keyCode) {
		
	}

	@Override
	protected void handleOnChanged(HTMLElement htmlElement) {
		inputPopupController.handleOnChanged(htmlElement);
	}

	@Override
	protected void handleOnClick(HTMLElement htmlElement) {
		inputPopupController.handleOnClick(htmlElement);
	}

	@Override
	protected void handleOnMouseOver(HTMLElement htmlElement) {
		inputPopupController.handleOnMouseOver(htmlElement);
	}

	@Override
	protected void update() {
		
	}

	protected abstract void onSave(int i);

	protected abstract void onCancel();

}

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

import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

public abstract class InputPopupController {

	public final HTMLInputElement inputField;
	public final boolean intValue;
	public final HTMLElement cancelButton;
	public final  HTMLElement doneButton;
	protected boolean cancelSelected = true;

	public InputPopupController(HTMLInputElement inputField, boolean intValue, HTMLElement cancelButton, HTMLElement doneButton) {
		this.inputField = inputField;
		this.intValue = intValue;
		this.cancelButton = cancelButton;
		this.doneButton = doneButton;
	}

	public void setup(String initialValue) {
		cancelSelected = true;
		inputField.setValue(initialValue);
		cancelButton.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "popup_input_opt_selected");
		doneButton.getClassList().remove(BootMenuConstants.cssClassPrefixBootMenu + "popup_input_opt_selected");
	}

	public void handleOnChanged(HTMLElement htmlElement) {
		if(inputField == htmlElement) {
			if(intValue) {
				int i;
				try {
					i = Integer.parseInt(inputField.getValue().trim());
				}catch(NumberFormatException ex) {
					inputField.setValue("0");
					return;
				}
				if(i < 0) {
					inputField.setValue("0");
				}
			}else {
				inputField.setValue(inputField.getValue().trim());
			}
		}
	}

	public void handleOnClick(HTMLElement htmlElement) {
		if(doneButton == htmlElement) {
			onSave(inputField);
		}else if(cancelButton == htmlElement) {
			onCancel();
		}
	}

	public void handleOnMouseOver(HTMLElement htmlElement) {
		if(doneButton == htmlElement) {
			setCancelSelected(false);
		}else if(cancelButton == htmlElement) {
			setCancelSelected(true);
		}
	}

	public void handleKeyDown(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ARROW_RIGHT) {
			setCancelSelected(false);
		}else if(keyCode == KeyCodes.DOM_KEY_ARROW_LEFT) {
			setCancelSelected(true);
		}else if(keyCode == KeyCodes.DOM_KEY_ENTER) {
			handleOnChanged(inputField);
			onSave(inputField);
		}else if(keyCode == KeyCodes.DOM_KEY_ESCAPE) {
			onCancel();
		}
	}

	public void setCancelSelected(boolean sel) {
		if(sel) {
			if(!cancelSelected) {
				cancelSelected = true;
				cancelButton.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "popup_input_opt_selected");
				doneButton.getClassList().remove(BootMenuConstants.cssClassPrefixBootMenu + "popup_input_opt_selected");
			}
		}else {
			if(cancelSelected) {
				cancelSelected = false;
				doneButton.getClassList().add(BootMenuConstants.cssClassPrefixBootMenu + "popup_input_opt_selected");
				cancelButton.getClassList().remove(BootMenuConstants.cssClassPrefixBootMenu + "popup_input_opt_selected");
			}
		}
	}

	protected abstract void onSave(HTMLInputElement inputField);

	protected abstract void onCancel();

}
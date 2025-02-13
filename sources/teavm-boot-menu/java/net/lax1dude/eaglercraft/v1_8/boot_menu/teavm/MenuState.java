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

public abstract class MenuState {

	protected MenuState currentPopup = null;

	public void changePopupState(MenuState popup) {
		if(popup == null) {
			if(currentPopup != null) {
				currentPopup.exitState();
				exitPopupBlockingState();
				currentPopup = null;
			}
		}else {
			if(currentPopup != null) {
				currentPopup.exitState();
			}
			currentPopup = popup;
			enterPopupBlockingState();
			popup.enterState();
		}
	}

	public void doEnterState() {
		enterState();
		if(currentPopup != null) {
			enterPopupBlockingState();
			currentPopup.enterState();
		}
	}

	public void doExitState() {
		if(currentPopup != null) {
			currentPopup.exitState();
			exitPopupBlockingState();
		}
		exitState();
	}

	protected abstract void enterState();

	protected abstract void exitState();

	protected abstract void enterPopupBlockingState();

	protected abstract void exitPopupBlockingState();

	public void doHandleKeyDown(int keyCode) {
		if(currentPopup != null) {
			currentPopup.doHandleKeyDown(keyCode);
		}else {
			handleKeyDown(keyCode);
		}
	}

	public void doHandleKeyUp(int keyCode) {
		if(currentPopup != null) {
			currentPopup.doHandleKeyUp(keyCode);
		}else {
			handleKeyUp(keyCode);
		}
	}

	public void doHandleKeyRepeat(int keyCode) {
		if(currentPopup != null) {
			currentPopup.doHandleKeyRepeat(keyCode);
		}else {
			handleKeyRepeat(keyCode);
		}
	}

	public void doHandleOnChange(HTMLElement element) {
		if(currentPopup != null) {
			currentPopup.doHandleOnChange(element);
		}else {
			handleOnChanged(element);
		}
	}

	public void doHandleOnClick(HTMLElement element) {
		if(currentPopup != null) {
			currentPopup.doHandleOnClick(element);
		}else {
			handleOnClick(element);
		}
	}

	public void doHandleOnMouseOver(HTMLElement element) {
		if(currentPopup != null) {
			currentPopup.doHandleOnMouseOver(element);
		}else {
			handleOnMouseOver(element);
		}
	}

	public void doUpdate() {
		if(currentPopup != null) {
			currentPopup.doUpdate();
		}else {
			update();
		}
	}

	protected abstract void handleKeyDown(int keyCode);

	protected abstract void handleKeyUp(int keyCode);

	protected abstract void handleKeyRepeat(int keyCode);

	protected abstract void handleOnChanged(HTMLElement htmlElement);

	protected abstract void handleOnClick(HTMLElement htmlElement);

	protected abstract void handleOnMouseOver(HTMLElement htmlElement);

	protected abstract void update();

}
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

import org.apache.commons.lang3.StringUtils;
import org.teavm.jso.dom.html.HTMLElement;

import net.lax1dude.eaglercraft.v1_8.EagUtils;

public class MenuPopupStateLoading extends MenuState implements IProgressMsgCallback {

	protected final String text;
	protected String msg;

	public MenuPopupStateLoading(String text) {
		this.text = text;
	}

	@Override
	protected void enterState() {
		BootMenuMain.bootMenuDOM.popup_confirm_opts.setInnerHTML("");
		BootMenuMain.bootMenuDOM.popup_confirm_title.setInnerText(!StringUtils.isAllEmpty(msg) ? (text + "\n\n" + msg) : text);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup_view_confirm);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup);
	}

	@Override
	protected void exitState() {
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
		
	}

	@Override
	protected void handleKeyUp(int keyCode) {
		
	}

	@Override
	protected void handleKeyRepeat(int keyCode) {
		
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
	public void updateMessage(String msg) {
		this.msg = msg;
		BootMenuMain.bootMenuDOM.popup_confirm_title.setInnerText(!StringUtils.isAllEmpty(msg) ? (text + "\n\n" + msg) : text);
		EagUtils.sleep(50);
	}

	@Override
	protected void update() {
		
	}

}
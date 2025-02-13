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

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;

public abstract class MenuPopupStateFileChooser extends MenuState {

	protected final String text;
	protected final String mime;
	protected final String ext;
	protected String msg;
	private boolean waitingForFile = false;

	public MenuPopupStateFileChooser(String text, String mime, String ext) {
		this.text = text;
		this.mime = mime;
		this.ext = ext;
	}

	@Override
	protected void enterState() {
		BootMenuMain.bootMenuDOM.popup_confirm_opts.setInnerHTML("");
		BootMenuMain.bootMenuDOM.popup_confirm_title.setInnerText(text + "\n\nPress ESC to cancel");
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup_view_confirm);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.popup);
		EagRuntime.displayFileChooser(mime, ext);
		waitingForFile = true;
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
		if(keyCode == KeyCodes.DOM_KEY_ESCAPE) {
			waitingForFile = false;
			onResult(null);
		}
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
	protected void update() {
		if(waitingForFile && EagRuntime.fileChooserHasResult()) {
			waitingForFile = false;
			onResult(EagRuntime.getFileChooserResult());
		}
	}

	protected abstract void onResult(FileChooserResult res);

}
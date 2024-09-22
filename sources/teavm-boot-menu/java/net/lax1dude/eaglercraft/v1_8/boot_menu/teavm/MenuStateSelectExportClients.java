package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.teavm.jso.dom.html.HTMLElement;

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
public class MenuStateSelectExportClients extends MenuState {

	private static final Logger logger = LogManager.getLogger("MenuStateSelectExportClients");

	private static class BootItem implements SelectionListController.ListItem {

		private final BootableClientEntry bootEntry;
		private final Consumer<BootableClientEntry> onSelected;

		public BootItem(BootableClientEntry bootEntry, Consumer<BootableClientEntry> onSelected) {
			this.bootEntry = bootEntry;
			this.onSelected = onSelected;
		}

		@Override
		public String getName() {
			return bootEntry.bootAdapter.getDisplayName();
		}

	}

	protected final boolean exportEPK;
	protected final MenuState parentState;
	protected SelectionListController<BootItem> selectionController;

	public MenuStateSelectExportClients(boolean exportEPK, MenuStateBoot parentState, List<BootableClientEntry> filteredList) {
		this.exportEPK = exportEPK;
		this.parentState = parentState;
		List<BootItem> lst = new ArrayList<>(filteredList.size());
		for(int i = 0, l = filteredList.size(); i < l; ++i) {
			lst.add(new BootItem(filteredList.get(i), (etr) -> {
				MenuPopupStateLoading popupState = new MenuPopupStateLoading("Downloading: '" + etr.bootAdapter.getDisplayName() + "'...");
				MenuStateSelectExportClients.this.changePopupState(popupState);
				BootMenuMain.runLaterMS(() -> {
					try {
						if(exportEPK) {
							etr.bootAdapter.downloadEPK(popupState);
						}else {
							etr.bootAdapter.downloadOffline(popupState);
						}
					}catch(Throwable t) {
						logger.error("Failed to download client!");
						logger.error(t);
						changePopupState(new MenuPopupStateConfirmation<String>("Error: Failed to download client!\n\n" + t.toString(),
								Arrays.asList("OK")) {
							@Override
							protected void selectCallback(String enumValue) {
								BootMenuMain.changeState(parentState);
							}
						});
						return;
					}
					BootMenuMain.changeState(parentState);
				}, 250);
			}));
		}
		selectionController = new SelectionListController<BootItem>(BootMenuMain.bootMenuDOM.content_selection, lst) {
			@Override
			protected void itemSelected(BootItem item) {
				item.onSelected.accept(item.bootEntry);
			}
		};
	}

	@Override
	protected void enterState() {
		selectionController.setup();
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.content_view_selection);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.footer_text_menu_select);
	}

	@Override
	protected void exitState() {
		selectionController.destroy();
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.content_view_selection);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.footer_text_menu_select);
	}

	@Override
	protected void enterPopupBlockingState() {
		selectionController.setCursorEventsSuspended(true);
	}

	@Override
	protected void exitPopupBlockingState() {
		selectionController.setCursorEventsSuspended(false);
	}

	@Override
	protected void handleKeyDown(int keyCode) {
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

}

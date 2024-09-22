package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.teavm.jso.dom.html.HTMLElement;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

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
public class MenuStateEnterSetup extends MenuState {

	private static enum EnumListMultiSelectType {
		CHECKBOX_ALWAYS_SHOW, SUBMENU_BOOT_ORDER, SUBMENU_DELETE_ITEM, AUTOMATIC_BOOT_TIMEOUT, SUBMENU_CHANGE_TITLE, DONE;
	}

	private static class MenuItem implements SelectionListController.ListItem {

		private final EnumListMultiSelectType type;
		private final String displayName;

		private MenuItem(EnumListMultiSelectType type, String displayName) {
			this.type = type;
			this.displayName = displayName;
		}

		@Override
		public String getName() {
			return displayName;
		}

	}

	protected SelectionListController<MenuItem> selectionController;

	public MenuStateEnterSetup(List<BootableClientEntry> bootableClients) {
		int bootFlagsRet = BootMenuDataManager.getBootMenuFlags(BootMenuMain.win);
		final int bootFlags = bootFlagsRet == -1 ? 0 : bootFlagsRet;
		final boolean[] alwaysShowState = new boolean[] { (bootFlags & 1) != 0 };
		int timeout = BootMenuMain.bootMenuDataManager.confBootTimeout;
		selectionController = new SelectionListController<MenuItem>(BootMenuMain.bootMenuDOM.content_selection, Arrays.asList(
					new MenuItem(EnumListMultiSelectType.CHECKBOX_ALWAYS_SHOW, (alwaysShowState[0] ? "[X]" : "[ ]") + " Always show boot menu on launch"),
					new MenuItem(EnumListMultiSelectType.SUBMENU_BOOT_ORDER, "Change Boot Order"),
					new MenuItem(EnumListMultiSelectType.SUBMENU_DELETE_ITEM, "Delete Boot Item"),
					new MenuItem(EnumListMultiSelectType.AUTOMATIC_BOOT_TIMEOUT, "Automatic Boot Timeout: " + (timeout == 0 ? "Disabled" : timeout)),
					new MenuItem(EnumListMultiSelectType.SUBMENU_CHANGE_TITLE, "Change Menu Title"),
					new MenuItem(EnumListMultiSelectType.DONE, "Done")
				)) {

			@Override
			public void handleKeyDown(int keyCode) {
				if(keyCode == KeyCodes.DOM_KEY_SPACE) {
					if(currentSelected == 0) { // the checkbox
						fireSelect();
					}
				}else {
					super.handleKeyDown(keyCode);
				}
			}

			@Override
			protected void itemSelectedLow(ListItemInstance<MenuItem> item) {
				switch(item.listItem.type) {
				case CHECKBOX_ALWAYS_SHOW:
					alwaysShowState[0] = !alwaysShowState[0];
					BootMenuDataManager.setBootMenuFlags(BootMenuMain.win, (bootFlags & ~1) | (alwaysShowState[0] ? 1 : 0));
					item.element.setInnerText((alwaysShowState[0] ? "[X]" : "[ ]") + " Always show boot menu on launch");
					break;
				case SUBMENU_BOOT_ORDER:
					BootMenuMain.changeState(new MenuStateEditBootOrder(MenuStateEnterSetup.this, bootableClients) {

						@Override
						protected void handleSave(List<EaglercraftUUID> reorderedList) {
							BootMenuMain.bootMenuDataManager.launchOrderList.clear();
							BootMenuMain.bootMenuDataManager.launchOrderList.addAll(reorderedList);
							BootableClientEntry.applyClientOrdering(BootMenuMain.bootMenuDataManager.launchOrderList, bootableClients);
							BootMenuMain.bootMenuDataManager.writeManifest();
							BootMenuMain.changeState(MenuStateEnterSetup.this);
						}

						@Override
						protected void handleCancel() {
							BootMenuMain.changeState(MenuStateEnterSetup.this);
						}

					});
					break;
				case SUBMENU_DELETE_ITEM:
					List<BootableClientEntry> deletableClients = new ArrayList<>(bootableClients.size() - 1);
					for(int i = 0, l = bootableClients.size(); i < l; ++i) {
						BootableClientEntry etr = bootableClients.get(i);
						if(etr.dataType == BootableClientEntry.EnumDataType.LOCAL_STORAGE) {
							deletableClients.add(etr);
						}
					}
					if(!deletableClients.isEmpty()) {
						BootMenuMain.changeState(new MenuStateClientMultiSelect(MenuStateEnterSetup.this, deletableClients) {
							@Override
							protected void onDone(List<BootableClientEntry> entries) {
								if(entries != null && !entries.isEmpty()) {
									for(int i = 0, l = entries.size(); i < l; ++i) {
										EaglercraftUUID toDelete = entries.get(i).bootAdapter.getLaunchConfigEntry().uuid;
										BootMenuMain.bootMenuDataManager.deleteLaunchConfig(toDelete);
									}
									List<BootableClientEntry> newEnum = BootableClientEntry.enumerateBootableClients();
									if(BootableClientEntry.applyClientOrdering(BootMenuMain.bootMenuDataManager.launchOrderList, newEnum)) {
										BootMenuMain.bootMenuDataManager.writeManifest();
									}
									BootMenuMain.changeState(new MenuStateEnterSetup(newEnum));
								}else {
									BootMenuMain.changeState(MenuStateEnterSetup.this);
								}
							}
						});
					}else {
						changePopupState(new MenuPopupStateConfirmation<String>(
								"Error: No deletable clients!",
								Arrays.asList("OK")) {
							@Override
							protected void selectCallback(String enumValue) {
								MenuStateEnterSetup.this.changePopupState(null);
							}
						});
					}
					break;
				case AUTOMATIC_BOOT_TIMEOUT:
					MenuStateEnterSetup.this.changePopupState(new MenuPopupStateEditInteger("Enter the number of seconds, or 0 to disable:", BootMenuMain.bootMenuDataManager.confBootTimeout) {

						@Override
						protected void onSave(int i) {
							if(i < 0) i = 0;
							if(i != BootMenuMain.bootMenuDataManager.confBootTimeout) {
								BootMenuMain.bootMenuDataManager.confBootTimeout = i;
								BootMenuMain.bootMenuDataManager.saveAdditionalConf();
								item.element.setInnerText("Automatic Boot Timeout: " + (i == 0 ? "Disabled" : i));
							}
							MenuStateEnterSetup.this.changePopupState(null);
						}

						@Override
						protected void onCancel() {
							MenuStateEnterSetup.this.changePopupState(null);
						}

					});
					break;
				case SUBMENU_CHANGE_TITLE:
					MenuStateEnterSetup.this.changePopupState(new MenuPopupStateEditString(
							"Enter the title to display on the menu:", BootMenuMain.bootMenuDataManager.confMenuTitle) {

						@Override
						protected void onSave(String str) {
							str = str.trim();
							if(!StringUtils.isEmpty(str) && !str.equals(BootMenuMain.bootMenuDataManager.confMenuTitle)) {
								BootMenuMain.bootMenuDataManager.confMenuTitle = str;
								BootMenuMain.bootMenuDataManager.saveAdditionalConf();
								BootMenuMain.bootMenuDOM.header_title.setInnerText(str);
							}
							MenuStateEnterSetup.this.changePopupState(null);
						}

						@Override
						protected void onCancel() {
							MenuStateEnterSetup.this.changePopupState(null);
						}

					});
					break;
				case DONE:
					BootMenuMain.changeState(new MenuStateBoot(false));
					break;
				default:
					break;
				}
			}
			@Override
			protected void itemSelected(MenuItem item) {
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
		if(keyCode == KeyCodes.DOM_KEY_ESCAPE) {
			BootMenuMain.changeState(new MenuStateBoot(false));
		}else {
			selectionController.handleKeyDown(keyCode);
		}
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

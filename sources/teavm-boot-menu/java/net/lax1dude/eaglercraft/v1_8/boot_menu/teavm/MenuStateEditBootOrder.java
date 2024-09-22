package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public abstract class MenuStateEditBootOrder extends MenuState {

	private static final EaglercraftUUID MAGIC_UUID_CANCEL = new EaglercraftUUID(0xD13983F5B764B3DL, 0xBF2C5157DEFDB5F9L);
	private static final EaglercraftUUID MAGIC_UUID_DONE = new EaglercraftUUID(0x1F2BBFD548DD4818L, 0xB00B79D143BBF607L);

	private static final BootOrderItem CANCEL_ITEM = new BootOrderItem("Cancel", MAGIC_UUID_CANCEL);
	private static final BootOrderItem DONE_ITEM = new BootOrderItem("Done", MAGIC_UUID_DONE);

	public static class BootOrderItem implements SelectionListController.ListItem {

		public final String displayName;
		public final EaglercraftUUID uuid;

		public BootOrderItem(String displayName, EaglercraftUUID uuid) {
			this.displayName = displayName;
			this.uuid = uuid;
		}

		@Override
		public String getName() {
			return displayName;
		}

	}

	public static class BootOrderItemContainer {

		public final EaglercraftUUID uuid;
		public final BootOrderItem exists;

		public BootOrderItemContainer(EaglercraftUUID uuid, BootOrderItem exists) {
			this.uuid = uuid;
			this.exists = exists;
		}

	}

	protected final MenuState parent;
	protected final SelectionListController<BootOrderItem> selectionController;
	protected final List<BootOrderItem> existingBootItems;
	protected final List<BootOrderItemContainer> allBootItems;
	protected boolean controlDown = false;

	public MenuStateEditBootOrder(MenuState parent) {
		this(parent, helper());
	}

	private static List<BootableClientEntry> helper() {
		List<BootableClientEntry> enumBootItems = BootableClientEntry.enumerateBootableClients();
		if(BootableClientEntry.applyClientOrdering(BootMenuMain.bootMenuDataManager.launchOrderList, enumBootItems)) {
			BootMenuMain.bootMenuDataManager.writeManifest();
		}
		return enumBootItems;
	}

	public MenuStateEditBootOrder(MenuState parent, List<BootableClientEntry> enumBootItems) {
		this.parent = parent;
		Map<EaglercraftUUID,BootableClientEntry> enumBootItemsMap = new HashMap<>(enumBootItems.size());
		for(int i = 0, l = enumBootItems.size(); i < l; ++i) {
			BootableClientEntry etr = enumBootItems.get(i);
			enumBootItemsMap.put(etr.bootAdapter.getLaunchConfigEntry().uuid, etr);
		}
		List<BootOrderItem> lst = new ArrayList<>();
		List<BootOrderItemContainer> lst2 = new ArrayList<>();
		List<EaglercraftUUID> lstSrc = BootMenuMain.bootMenuDataManager.launchOrderList;
		for(int i = 0, l = lstSrc.size(); i < l; ++i) {
			EaglercraftUUID uuid = lstSrc.get(i);
			BootableClientEntry etr =  enumBootItemsMap.get(uuid);
			if(etr != null) {
				BootOrderItem itm = new BootOrderItem(etr.bootAdapter.getDisplayName(), uuid);
				lst.add(itm);
				lst2.add(new BootOrderItemContainer(uuid, itm));
			}else {
				lst2.add(new BootOrderItemContainer(uuid, null));
			}
		}
		existingBootItems = lst;
		allBootItems = lst2;
		List<BootOrderItem> lst3 = new ArrayList<>(lst.size() + 2);
		lst3.addAll(lst);
		lst3.add(CANCEL_ITEM);
		lst3.add(DONE_ITEM);
		selectionController = new SelectionListController<BootOrderItem>(BootMenuMain.bootMenuDOM.content_selection, lst3) {
			@Override
			protected void itemSelected(BootOrderItem item) {
				if(item == DONE_ITEM) {
					MenuStateEditBootOrder.this.fireHandleSave();
				}else if(item == CANCEL_ITEM) {
					MenuStateEditBootOrder.this.handleCancel();
				}
			}
		};
		selectionController.setCursorEventsSuspended(true); // mouse over events might make it hard to reorder
	}

	protected void fireHandleSave() {
		List<EaglercraftUUID> retList = new ArrayList<>(allBootItems.size());
		for(int i = 0, l = allBootItems.size(); i < l; ++i) {
			retList.add(allBootItems.get(i).uuid);
		}
		handleSave(retList);
	}

	@Override
	protected void enterState() {
		selectionController.setup();
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.footer_text_boot_order);
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.content_view_selection);
	}

	@Override
	protected void exitState() {
		selectionController.destroy();
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.footer_text_boot_order);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.content_view_selection);
	}

	@Override
	protected void enterPopupBlockingState() {
		
	}

	@Override
	protected void exitPopupBlockingState() {
		
	}

	@Override
	protected void handleKeyDown(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ESCAPE) {
			handleCancel();
		}else if(keyCode == KeyCodes.DOM_KEY_CONTROL) {
			controlDown = true;
		}else if(controlDown && keyCode == KeyCodes.DOM_KEY_ARROW_UP) {
			moveEntryUp();
		}else if(controlDown && keyCode == KeyCodes.DOM_KEY_ARROW_DOWN) {
			moveEntryDown();
		}else {
			selectionController.handleKeyDown(keyCode);
		}
	}

	protected void moveEntryUp() {
		BootOrderItem itm = selectionController.getSelected();
		if(itm == null || itm == DONE_ITEM || itm == CANCEL_ITEM) {
			return;
		}
		int index = selectionController.currentSelected;
		if(index <= 0) {
			return;
		}
		EaglercraftUUID currentUUID = itm.uuid;
		int index2 = -1;
		for(int i = 0, l = allBootItems.size(); i < l; ++i) {
			BootOrderItemContainer itm2 = allBootItems.get(i);
			if(itm2.uuid.equals(currentUUID)) {
				index2 = i;
				break;
			}
		}
		if(index2 == -1) {
			throw new IllegalStateException();
		}
		int newIndex = index - 1;
		int newIndex2 = index2 - 1;
		while(newIndex2 > 0 && allBootItems.get(newIndex2).exists == null) {
			--newIndex2;
		}
		Collections.swap(existingBootItems, index, newIndex);
		Collections.swap(allBootItems, index2, newIndex2);
		selectionController.moveEntryUp(index);
	}

	protected void moveEntryDown() {
		BootOrderItem itm = selectionController.getSelected();
		if(itm == null || itm == DONE_ITEM || itm == CANCEL_ITEM) {
			return;
		}
		int index = selectionController.currentSelected;
		if(index >= existingBootItems.size() - 1) {
			return;
		}
		EaglercraftUUID currentUUID = itm.uuid;
		int index2 = -1;
		for(int i = 0, l = allBootItems.size(); i < l; ++i) {
			BootOrderItemContainer itm2 = allBootItems.get(i);
			if(itm2.uuid.equals(currentUUID)) {
				index2 = i;
				break;
			}
		}
		if(index2 == -1) {
			throw new IllegalStateException();
		}
		int newIndex = index + 1;
		int newIndex2 = index2 + 1;
		while(newIndex2 < allBootItems.size() - 1 && allBootItems.get(newIndex2).exists == null) {
			++newIndex2;
		}
		Collections.swap(existingBootItems, index, newIndex);
		Collections.swap(allBootItems, index2, newIndex2);
		selectionController.moveEntryDown(index);
	}

	@Override
	protected void handleKeyUp(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_CONTROL) {
			controlDown = false;
		}
	}

	@Override
	protected void handleKeyRepeat(int keyCode) {
		selectionController.handleKeyDown(keyCode);
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

	protected abstract void handleSave(List<EaglercraftUUID> reorderedList);

	protected abstract void handleCancel();

}

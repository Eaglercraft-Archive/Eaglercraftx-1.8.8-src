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

import java.util.Arrays;
import java.util.List;

import org.teavm.jso.dom.html.HTMLElement;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public abstract class CheckboxListController<T extends SelectionListController.ListItem>
		extends SelectionListController<SelectionListController.ListItem> {

	protected static final SelectionListController.ListItem LIST_ITEM_CANCEL = new SelectionListController.ListItem() {
		@Override
		public String getName() {
			return "Cancel";
		}
	};

	protected static final SelectionListController.ListItem LIST_ITEM_DONE = new SelectionListController.ListItem() {
		@Override
		public String getName() {
			return "Done";
		}
	};

	protected static class ListItemWrapper implements SelectionListController.ListItem {

		protected final SelectionListController.ListItem parent;

		protected ListItemWrapper(SelectionListController.ListItem parent) {
			this.parent = parent;
		}

		@Override
		public String getName() {
			return (parent.getAlwaysSelected() ? "[X] " : "[ ] ") + parent.getName();
		}

		@Override
		public boolean getAlwaysSelected() {
			return parent.getAlwaysSelected();
		}

	}

	public CheckboxListController(HTMLElement parent, List<T> selectionList) {
		super(parent, Lists.newArrayList(Iterators.concat(Iterators.transform(selectionList.iterator(), ListItemWrapper::new),
						Arrays.asList(LIST_ITEM_CANCEL, LIST_ITEM_DONE).iterator())));
	}

	public List<T> getSelectedItems() {
		return selectionEnableList.stream().filter((e) -> (e.userVal && !e.listItem.getAlwaysSelected()))
				.map((e) -> (T) ((ListItemWrapper) e.listItem).parent).toList();
	}

	protected void itemSelectedLow(ListItemInstance<SelectionListController.ListItem> item) {
		if(item.listItem == LIST_ITEM_CANCEL) {
			cancelSelected();
		}else if(item.listItem == LIST_ITEM_DONE) {
			doneSelected(getSelectedItems());
		}else {
			if(!item.listItem.getAlwaysSelected()) {
				item.userVal = !item.userVal;
				item.element.setInnerText((item.userVal ? "[X]" : "[ ]") + item.element.getInnerText().substring(3));
			}
		}
	}

	public void handleKeyDown(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_SPACE) {
			if(currentSelected >= 0 && currentSelected < selectionEnableList.size()) {
				if(!selectionEnableList.get(currentSelected).listItem.getAlwaysSelected()) {
					fireSelect();
				}
			}
			return;
		}
		super.handleKeyDown(keyCode);
	}

	@Override
	protected final void itemSelected(SelectionListController.ListItem item) {
	}

	protected abstract void cancelSelected();

	protected abstract void doneSelected(List<T> selectedItems);

}
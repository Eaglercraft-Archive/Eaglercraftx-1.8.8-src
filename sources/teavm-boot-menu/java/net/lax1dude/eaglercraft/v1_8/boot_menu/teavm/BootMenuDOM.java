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

import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

public class BootMenuDOM {

	public final HTMLElement content_view_selection;
	public final HTMLElement content_selection;
	public final HTMLElement content_view_editor;
	public final HTMLElement header_title;
	public final HTMLElement launch_conf_val_profile_name;
	public final HTMLElement launch_conf_val_data_format;
	public final HTMLElement launch_conf_val_launch_type;
	public final Map<EnumClientLaunchType,HTMLElement> launch_conf_val_launch_type_opts;
	public final HTMLElement launch_conf_join_server;
	public final HTMLElement launch_conf_val_join_server;
	public final HTMLElement launch_conf_opts_name;
	public final HTMLElement launch_conf_val_opts_name;
	public final HTMLElement launch_conf_assetsURI;
	public final HTMLElement launch_conf_val_assetsURI;
	public final HTMLElement launch_conf_container;
	public final HTMLElement launch_conf_val_container;
	public final HTMLElement launch_conf_main_func;
	public final HTMLElement launch_conf_val_main_func;
	public final HTMLElement launch_conf_clear_cookies;
	public final HTMLElement launch_conf_val_clear_cookies;
	public final HTMLElement launch_opt_editor;
	public final HTMLElement popup;
	public final HTMLElement popup_view_confirm;
	public final HTMLElement popup_confirm_title;
	public final HTMLElement popup_confirm_opts;
	public final HTMLElement popup_view_selection;
	public final HTMLElement popup_selection_title;
	public final HTMLElement popup_selection;
	public final HTMLElement popup_view_input;
	public final HTMLElement popup_input_title;
	public final HTMLElement popup_input_val;
	public final HTMLElement popup_input_opt_cancel;
	public final HTMLElement popup_input_opt_done;
	public final HTMLElement footer_text_boot_select;
	public final HTMLElement footer_text_boot_select_count;
	public final HTMLElement footer_text_boot_countdown;
	public final HTMLElement footer_text_menu_select;
	public final HTMLElement footer_text_opts_editor;
	public final HTMLElement footer_text_opts_editor_alt;
	public final HTMLElement footer_text_boot_order;

	public BootMenuDOM(HTMLElement parentElement) {
		content_view_selection = selectHelper(parentElement, "content_view_selection");
		content_selection = selectHelper(parentElement, "content_selection");
		content_view_editor = selectHelper(parentElement, "content_view_editor");
		header_title = selectHelper(parentElement, "header_title");
		launch_conf_val_profile_name = selectHelper(parentElement, "launch_conf_val_profile_name");
		launch_conf_val_data_format = selectHelper(parentElement, "launch_conf_val_data_format");
		launch_conf_val_launch_type = selectHelper(parentElement, "launch_conf_val_launch_type");
		launch_conf_val_launch_type_opts = new HashMap<>();
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.EAGLERX_V1, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=EAGLERX_V1]"));
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.EAGLERX_SIGNED_V1, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=EAGLERX_SIGNED_V1]"));
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.EAGLER_1_5_V2, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=EAGLER_1_5_V2]"));
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.EAGLER_1_5_V1, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=EAGLER_1_5_V1]"));
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.EAGLER_BETA_V1, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=EAGLER_BETA_V1]"));
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.PEYTON_V1, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=PEYTON_V1]"));
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.PEYTON_V2, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=PEYTON_V2]"));
		launch_conf_val_launch_type_opts.put(EnumClientLaunchType.STANDARD_OFFLINE_V1, selectHelper(parentElement, "launch_conf_val_launch_type_opt[value=STANDARD_OFFLINE_V1]"));
		launch_conf_join_server = selectHelper(parentElement, "launch_conf_join_server");
		launch_conf_val_join_server = selectHelper(parentElement, "launch_conf_val_join_server");
		launch_conf_opts_name = selectHelper(parentElement, "launch_conf_opts_name");
		launch_conf_val_opts_name = selectHelper(parentElement, "launch_conf_val_opts_name");
		launch_conf_assetsURI = selectHelper(parentElement, "launch_conf_assetsURI");
		launch_conf_val_assetsURI = selectHelper(parentElement, "launch_conf_val_assetsURI");
		launch_conf_container = selectHelper(parentElement, "launch_conf_container");
		launch_conf_val_container = selectHelper(parentElement, "launch_conf_val_container");
		launch_conf_main_func = selectHelper(parentElement, "launch_conf_main_func");
		launch_conf_val_main_func = selectHelper(parentElement, "launch_conf_val_main_func");
		launch_conf_clear_cookies = selectHelper(parentElement, "launch_conf_clear_cookies");
		launch_conf_val_clear_cookies = selectHelper(parentElement, "launch_conf_val_clear_cookies");
		launch_opt_editor = selectHelper(parentElement, "launch_opt_editor");
		popup = selectHelper(parentElement, "popup");
		popup_view_confirm = selectHelper(parentElement, "popup_view_confirm");
		popup_confirm_title = selectHelper(parentElement, "popup_confirm_title");
		popup_confirm_opts = selectHelper(parentElement, "popup_confirm_opts");
		popup_view_selection = selectHelper(parentElement, "popup_view_selection");
		popup_selection_title = selectHelper(parentElement, "popup_selection_title");
		popup_selection = selectHelper(parentElement, "popup_selection");
		popup_view_input = selectHelper(parentElement, "popup_view_input");
		popup_input_title = selectHelper(parentElement, "popup_input_title");
		popup_input_val = selectHelper(parentElement, "popup_input_val");
		popup_input_opt_cancel = selectHelper(parentElement, "popup_input_opt_cancel");
		popup_input_opt_done = selectHelper(parentElement, "popup_input_opt_done");
		footer_text_boot_select = selectHelper(parentElement, "footer_text_boot_select");
		footer_text_boot_select_count = selectHelper(parentElement, "footer_text_boot_select_count");
		footer_text_boot_countdown = selectHelper(parentElement, "footer_text_boot_countdown");
		footer_text_menu_select = selectHelper(parentElement, "footer_text_menu_select");
		footer_text_opts_editor = selectHelper(parentElement, "footer_text_opts_editor");
		footer_text_opts_editor_alt = selectHelper(parentElement, "footer_text_opts_editor_alt");
		footer_text_boot_order = selectHelper(parentElement, "footer_text_boot_order");
	}

	public void registerEventHandlers() {
		launch_conf_val_profile_name.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_profile_name);
		});
		launch_conf_val_data_format.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_data_format);
		});
		launch_conf_val_launch_type.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_launch_type);
		});
		launch_conf_val_join_server.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_join_server);
		});
		launch_conf_val_opts_name.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_opts_name);
		});
		launch_conf_val_assetsURI.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_assetsURI);
		});
		launch_conf_val_container.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_container);
		});
		launch_conf_val_main_func.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_main_func);
		});
		launch_conf_val_clear_cookies.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_conf_val_clear_cookies);
		});
		launch_opt_editor.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(launch_opt_editor);
		});
		popup_input_val.addEventListener("change", (evt) -> {
			BootMenuMain.fireChangeEvent(popup_input_val);
		});
		popup_input_opt_cancel.addEventListener("click", (evt) -> {
			BootMenuMain.fireClickEvent(popup_input_opt_cancel);
		});
		popup_input_opt_done.addEventListener("click", (evt) -> {
			BootMenuMain.fireClickEvent(popup_input_opt_done);
		});
		popup_input_opt_cancel.addEventListener("mouseover", (evt) -> {
			BootMenuMain.fireMouseOverEvent(popup_input_opt_cancel);
		});
		popup_input_opt_done.addEventListener("mouseover", (evt) -> {
			BootMenuMain.fireMouseOverEvent(popup_input_opt_done);
		});
	}

	public static void show(HTMLElement el) {
		el.getStyle().setProperty("display", "block");
	}

	public static void hide(HTMLElement el) {
		el.getStyle().setProperty("display", "none");
	}

	public static void setValue(HTMLElement el, String value) {
		((HTMLInputElement)el).setValue(value);
	}

	public static String getValue(HTMLElement el) {
		return ((HTMLInputElement)el).getValue();
	}

	public static void setChecked(HTMLElement el, boolean checked) {
		((HTMLInputElement)el).setChecked(checked);
	}

	public static boolean getChecked(HTMLElement el) {
		return ((HTMLInputElement)el).isChecked();
	}

	public static void setDisabled(HTMLElement el, boolean disabled) {
		((HTMLInputElement)el).setDisabled(disabled);
	}

	private static HTMLElement selectHelper(HTMLElement parent, String name) {
		name = "." + BootMenuConstants.cssClassPrefixBootMenu + name;
		HTMLElement ret = parent.querySelector(name);
		if(ret == null) {
			throw new RuntimeException("Failed to select \"" + name + "\" from boot menu!");
		}
		return ret;
	}

}
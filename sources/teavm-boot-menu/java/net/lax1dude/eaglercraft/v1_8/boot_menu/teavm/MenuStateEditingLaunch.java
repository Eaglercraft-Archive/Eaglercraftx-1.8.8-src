package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.json.JSONException;
import org.json.JSONObject;
import org.teavm.jso.dom.html.HTMLElement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuMetadata.DefaultLaunchTemplate;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuMetadata.LaunchTemplate;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

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
public class MenuStateEditingLaunch extends MenuState {

	private static final Logger logger = LogManager.getLogger("MenuStateEditingLaunch");

	protected final MenuState previousMenu;
	protected final LaunchConfigEntry launchConf;
	protected final ClientDataEntry clientData;
	protected final boolean isImporting;
	protected final Map<EaglercraftUUID, Supplier<byte[]>> importClientBlobs;
	protected boolean controlDown = false;

	public static MenuStateEditingLaunch createHelper(MenuState previousMenu, LaunchConfigEntry launchConf,
			ClientDataEntry clientData, boolean isImporting, Map<EaglercraftUUID, byte[]> importClientBlobs) {
		return new MenuStateEditingLaunch(previousMenu, launchConf, clientData, isImporting,
				Maps.transformValues(importClientBlobs, (blob) -> {
			return () -> blob;
		}));
	}

	public MenuStateEditingLaunch(MenuState previousMenu, LaunchConfigEntry launchConf, ClientDataEntry clientData,
			boolean isImporting, Map<EaglercraftUUID, Supplier<byte[]>> importClientBlobs) {
		this.previousMenu = previousMenu;
		this.launchConf = launchConf;
		this.clientData = clientData;
		this.isImporting = isImporting;
		this.importClientBlobs = importClientBlobs;
	}

	private void setVisibleControlSet() {
		EnumClientLaunchType launchType = launchConf.type;
		switch(launchType) {
		case EAGLERX_V1:
		case EAGLERX_SIGNED_V1:
		case EAGLER_1_5_V2:
		case PEYTON_V2:
		case PEYTON_V1:
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_join_server);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_opts_name);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_assetsURI);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_container);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_main_func);
			break;
		case EAGLER_1_5_V1:
		case EAGLER_BETA_V1:
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.launch_conf_join_server);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_opts_name);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_assetsURI);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_container);
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_main_func);
			break;
		case STANDARD_OFFLINE_V1:
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_join_server);
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.launch_conf_opts_name);
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.launch_conf_assetsURI);
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.launch_conf_container);
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.launch_conf_main_func);
			break;
		}
	}

	private void setControlSetValues() {
		BootMenuMain.bootMenuDOM.launch_conf_val_data_format.setInnerText(clientData.type.displayName);
		EnumClientLaunchType launchType = launchConf.type;
		BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_launch_type, launchType.toString());
		BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_profile_name, launchConf.displayName);
		BootMenuDOM.setChecked(BootMenuMain.bootMenuDOM.launch_conf_val_clear_cookies, launchConf.clearCookiesBeforeLaunch);
		switch(launchType) {
		case EAGLERX_V1:
		case EAGLERX_SIGNED_V1:
		case EAGLER_1_5_V2:
		case PEYTON_V2:
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_opt_editor, tryJSONFormat(launchConf.launchOpts));
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_container, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, "");
			break;
		case EAGLER_1_5_V1:
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_opt_editor, launchConf.launchOpts);
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, launchConf.joinServer);
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_container, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, "");
			break;
		case EAGLER_BETA_V1:
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_opt_editor, "JSON opts are not supported for Eaglercraft b1.3!");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, launchConf.joinServer);
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_container, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, "");
			break;
		case PEYTON_V1:
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_opt_editor, "JSON opts are not supported for Indev!");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_container, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, "");
			break;
		case STANDARD_OFFLINE_V1:
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_opt_editor, tryJSONFormat(launchConf.launchOpts));
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, "");
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, launchConf.launchOptsVar);
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, launchConf.launchOptsAssetsURIVar);
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_container, launchConf.launchOptsContainerVar);
			BootMenuDOM.setValue(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, launchConf.mainFunction);
			break;
		}
	}

	private void setEnabledControlSet() {
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_launch_type, false);
		setEnabledLaunchTypes();
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_profile_name, false);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_clear_cookies, false);
		EnumClientLaunchType launchType = launchConf.type;
		switch(launchType) {
		case EAGLERX_V1:
		case EAGLERX_SIGNED_V1:
		case EAGLER_1_5_V2:
		case PEYTON_V2:
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_opt_editor, false);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_container, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, true);
			break;
		case EAGLER_1_5_V1:
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_opt_editor, false);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, false);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_container, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, true);
			break;
		case EAGLER_BETA_V1:
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_opt_editor, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, false);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_container, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, true);
			break;
		case PEYTON_V1:
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_opt_editor, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_container, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, true);
			break;
		case STANDARD_OFFLINE_V1:
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_opt_editor, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, true);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, false);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, false);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_container, false);
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, false);
			break;
		}
	}

	private void readAllValues() {
		EnumClientLaunchType newLaunchType = EnumClientLaunchType.valueOf(BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_launch_type));
		if(!clientData.type.launchTypes.contains(newLaunchType)) {
			logger.error("nope!");
			throw new IllegalStateException("nope!");
		}
		EnumClientLaunchType launchType = launchConf.type;
		launchConf.type = newLaunchType;
		launchConf.displayName = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_profile_name).trim();
		launchConf.clearCookiesBeforeLaunch = BootMenuDOM.getChecked(BootMenuMain.bootMenuDOM.launch_conf_val_clear_cookies);
		switch(launchType) {
		case EAGLERX_V1:
		case EAGLERX_SIGNED_V1:
		case EAGLER_1_5_V2:
		case PEYTON_V2:
			launchConf.launchOpts = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_opt_editor).trim();
			break;
		case EAGLER_1_5_V1:
			launchConf.launchOpts = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_opt_editor).trim();
			launchConf.joinServer = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_join_server).trim();
			break;
		case EAGLER_BETA_V1:
			launchConf.joinServer = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_join_server).trim();
			break;
		case PEYTON_V1:
			break;
		case STANDARD_OFFLINE_V1:
			launchConf.launchOpts = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_opt_editor).trim();
			launchConf.launchOptsVar = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name).trim();
			launchConf.launchOptsAssetsURIVar = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI).trim();
			launchConf.launchOptsContainerVar = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_container).trim();
			launchConf.mainFunction = BootMenuDOM.getValue(BootMenuMain.bootMenuDOM.launch_conf_val_main_func).trim();
			break;
		}
	}

	private void setEnabledLaunchTypes() {
		EnumClientFormatType clientType = clientData.type;
		Set<EnumClientLaunchType> launchTypes = clientType.launchTypes;
		EnumClientLaunchType[] itr = EnumClientLaunchType._values();
		for(int i = 0; i < itr.length; ++i) {
			EnumClientLaunchType enumType = itr[i];
			BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_launch_type_opts.get(enumType),
					!launchTypes.contains(enumType));
		}
	}

	private static String tryJSONFormat(String input) {
		try {
			return (new JSONObject(input)).toString(4);
		}catch(JSONException ex) {
			logger.warn("This client's JSON is corrupt! Failed to format");
			logger.warn(ex);
			return input;
		}
	}

	@Override
	protected void enterState() {
		if(isImporting) {
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.footer_text_opts_editor_alt);
		}else {
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.footer_text_opts_editor);
		}
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.content_view_editor);
		setEnabledControlSet();
		setControlSetValues();
		setVisibleControlSet();
	}

	@Override
	protected void exitState() {
		if(isImporting) {
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.footer_text_opts_editor_alt);
		}else {
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.footer_text_opts_editor);
		}
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.content_view_editor);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_join_server);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_opts_name);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_assetsURI);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_container);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.launch_conf_main_func);
	}

	@Override
	protected void enterPopupBlockingState() {
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_opt_editor, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_launch_type, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_profile_name, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_join_server, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_opts_name, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_assetsURI, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_container, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_main_func, true);
		BootMenuDOM.setDisabled(BootMenuMain.bootMenuDOM.launch_conf_val_clear_cookies, true);
	}

	@Override
	protected void exitPopupBlockingState() {
		setEnabledControlSet();
	}

	public static enum EnumEditorMenu {
		BOOT("Boot configuration"),
		SAVE("Save configuration"),
		SAVE_COPY("Save a copy"),
		LOAD_DEFAULTS("Load defaults"),
		LOAD_TEMPLATE("Load template"),
		EXPORT_OFFLINE("Export Offline"),
		DELETE("Delete configuration"),
		RETURN("Return to menu"),
		CANCEL("Cancel");
		
		private final String str;
		
		private EnumEditorMenu(String str) {
			this.str = str;
		}
		
		@Override
		public String toString() {
			return str;
		}
	}

	protected static final List<EnumEditorMenu> EDITOR_MENU = Arrays.asList(EnumEditorMenu.BOOT, EnumEditorMenu.SAVE,
			EnumEditorMenu.SAVE_COPY, EnumEditorMenu.LOAD_DEFAULTS, EnumEditorMenu.LOAD_TEMPLATE,
			EnumEditorMenu.EXPORT_OFFLINE, EnumEditorMenu.DELETE, EnumEditorMenu.RETURN, EnumEditorMenu.CANCEL);

	protected static final List<EnumEditorMenu> IMPORTER_MENU = Arrays.asList(EnumEditorMenu.SAVE,
			EnumEditorMenu.LOAD_DEFAULTS, EnumEditorMenu.LOAD_TEMPLATE, EnumEditorMenu.EXPORT_OFFLINE,
			EnumEditorMenu.RETURN, EnumEditorMenu.CANCEL);

	@Override
	protected void handleKeyDown(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_ESCAPE) {
			returnToMenu();
		}else if(keyCode == KeyCodes.DOM_KEY_CONTROL) {
			controlDown = true;
		}else if(keyCode == KeyCodes.DOM_KEY_SHIFT) {
			if(controlDown) {
				changePopupState(MenuPopupStateSelection.createHelper("What do you wanna do?", isImporting ? IMPORTER_MENU : EDITOR_MENU, (opt) -> {
					switch(opt) {
					case BOOT:
						bootCurrentConfig();
						break;
					case SAVE:
						saveAndExit();
						break;
					case SAVE_COPY:
						saveCopy();
						break;
					case LOAD_DEFAULTS:
						loadDefaults();
						break;
					case LOAD_TEMPLATE:
						loadTemplate();
						break;
					case EXPORT_OFFLINE:
						exportOffline();
						break;
					case DELETE:
						deleteConfig();
						break;
					case RETURN:
						returnToMenu();
						break;
					case CANCEL:
					default:
						changePopupState(null);
						break;
					}
				}));
			}
		}else if(keyCode == KeyCodes.DOM_KEY_ENTER) {
			if(controlDown) {
				if(isImporting) {
					saveAndExit();
				}else {
					bootCurrentConfig();
				}
			}
		}
	}

	private String validateLaunchOpts() {
		EnumClientLaunchType launchType = launchConf.type;
		switch(launchType) {
		case EAGLERX_V1:
		case EAGLERX_SIGNED_V1:
		case EAGLER_1_5_V2:
		case PEYTON_V2:
		case STANDARD_OFFLINE_V1:
			try {
				new JSONObject(launchConf.launchOpts);
				return null;
			}catch(JSONException ex) {
				return ex.toString();
			}
		case EAGLER_1_5_V1:
			if(!launchConf.launchOpts.startsWith("[NBT]") || !launchConf.launchOpts.endsWith("[/NBT]")) {
				return "Content does not begin/end with \"[NBT]\" and \"[/NBT]\"";
			}
			try {
				String str = launchConf.launchOpts;
				JsonToNBT.getTagFromJson(str.substring(5, str.length() - 6).trim());
				return null;
			}catch(NBTException ex) {
				return ex.toString();
			}
		case EAGLER_BETA_V1:
		case PEYTON_V1:
		default:
			return null;
		}
	}

	private void bootCurrentConfig() {
		readAllValues();
		String err = validateLaunchOpts();
		if(err != null) {
			changePopupState(new MenuPopupStateConfirmation<String>("Error: Invalid syntax in launch opts!\n\n" + err,
					Arrays.asList("OK")) {
				@Override
				protected void selectCallback(String enumValue) {
					MenuStateEditingLaunch.this.changePopupState(null);
				}
			});
			return;
		}
		MenuPopupStateLoading popupState = new MenuPopupStateLoading("Booting: '" + launchConf.displayName + "'...");
		changePopupState(popupState);
		BootMenuMain.runLaterMS(() -> {
			try {
				ClientBootFactory.bootClient(launchConf, clientData, importClientBlobs, popupState);
			}catch(UnsignedBootException ex) {
				MenuStateBoot.displayUnsignedError(MenuStateEditingLaunch.this, (cb) -> {
					MenuStateEditingLaunch.this.exportOffline();
				}, () -> {
					MenuStateEditingLaunch.this.changePopupState(null);
				});
				return;
			}catch(Throwable t) {
				logger.error("Failed to boot client!");
				logger.error(t);
				changePopupState(new MenuPopupStateConfirmation<String>("Error: Failed to boot client!\n\n" + t.toString(),
						Arrays.asList("OK")) {
					@Override
					protected void selectCallback(String enumValue) {
						MenuStateEditingLaunch.this.changePopupState(null);
					}
				});
				return;
			}
			try {
				changePopupState(null);
			}catch(Throwable t) {
			}
		}, 250);
	}

	private void saveAndExit() {
		readAllValues();
		String err = validateLaunchOpts();
		if(err != null) {
			changePopupState(new MenuPopupStateConfirmation<String>("Error: Invalid syntax in launch opts!\n\n" + err,
					Arrays.asList("OK")) {
				@Override
				protected void selectCallback(String enumValue) {
					MenuStateEditingLaunch.this.changePopupState(null);
				}
			});
			return;
		}
		MenuPopupStateLoading popupState = new MenuPopupStateLoading("Saving: '" + launchConf.displayName + "'...");
		changePopupState(popupState);
		BootMenuMain.runLaterMS(() -> {
			try {
				BootMenuMain.bootMenuDataManager.installNewLaunchConfig(launchConf, clientData,
						Maps.transformValues(importClientBlobs, (e) -> e.get()), false);
			}catch(Throwable t) {
				logger.error("Error: could not save launch config!");
				logger.error(t);
				changePopupState(new MenuPopupStateConfirmation<String>("Error: Could not save launch config!\n\n" + t.toString(),
						Arrays.asList("OK")) {
					@Override
					protected void selectCallback(String enumValue) {
						MenuStateEditingLaunch.this.changePopupState(null);
					}
				});
				return;
			}
			BootMenuMain.changeState(new MenuStateBoot(false));
		}, 250);
	}

	private void saveCopy() {
		readAllValues();
		String err = validateLaunchOpts();
		if(err != null) {
			changePopupState(new MenuPopupStateConfirmation<String>("Error: Invalid syntax in launch opts!\n\n" + err,
					Arrays.asList("OK")) {
				@Override
				protected void selectCallback(String enumValue) {
					MenuStateEditingLaunch.this.changePopupState(null);
				}
			});
			return;
		}
		final LaunchConfigEntry launchConfCopy = launchConf.rotateUUIDs(EaglercraftUUID.randomUUID(), launchConf.clientDataUUID);
		MenuPopupStateLoading popupState = new MenuPopupStateLoading("Saving: '" + launchConfCopy.displayName + "'...");
		changePopupState(popupState);
		BootMenuMain.runLaterMS(() -> {
			try {
				BootMenuMain.bootMenuDataManager.installNewLaunchConfig(launchConfCopy, clientData,
						Maps.transformValues(importClientBlobs, (e) -> e.get()), false);
			}catch(Throwable t) {
				logger.error("Error: could not save launch config!");
				logger.error(t);
				changePopupState(new MenuPopupStateConfirmation<String>("Error: Could not save launch config!\n\n" + t.toString(),
						Arrays.asList("OK")) {
					@Override
					protected void selectCallback(String enumValue) {
						MenuStateEditingLaunch.this.changePopupState(null);
					}
				});
				return;
			}
			BootMenuMain.changeState(new MenuStateBoot(false));
		}, 250);
	}

	private void loadDefaults() {
		LaunchTemplate def = BootMenuMain.bootMenuMetadata.formatDefaultOptsMap.get(launchConf.type);
		if(def != null) {
			def.configureLaunchConfig(launchConf);
			setControlSetValues();
			changePopupState(null);
		}else {
			changePopupState(new MenuPopupStateConfirmation<String>("Error: Could not find default config!",
					Arrays.asList("OK")) {
				@Override
				protected void selectCallback(String enumValue) {
					MenuStateEditingLaunch.this.changePopupState(null);
				}
			});
		}
	}

	private void loadTemplate() {
		List<DefaultLaunchTemplate> templates = BootMenuMain.bootMenuMetadata.getTemplatesForParseType(EnumOfflineParseType.inferFromClientFormat(launchConf.type));
		if(templates != null && !templates.isEmpty()) {
			List<Object> listWithCancel = Lists.newArrayList(templates);
			listWithCancel.add("Cancel");
			changePopupState(MenuPopupStateSelection.createHelper("Select the template you would like to load:", listWithCancel, (template) -> {
				if(template != null) {
					if(!"Cancel".equals(template)) {
						((DefaultLaunchTemplate)template).templateState.configureLaunchConfig(launchConf);
						setControlSetValues();
						changePopupState(null);
					}else {
						changePopupState(null);
					}
				}
			}));
		}else {
			changePopupState(new MenuPopupStateConfirmation<String>("Error: Could not find any templates!",
					Arrays.asList("OK")) {
				@Override
				protected void selectCallback(String enumValue) {
					MenuStateEditingLaunch.this.changePopupState(null);
				}
			});
		}
	}

	private void exportOffline() {
		readAllValues();
		String err = validateLaunchOpts();
		if(err != null) {
			changePopupState(new MenuPopupStateConfirmation<String>("Error: Invalid syntax in launch opts!\n\n" + err,
					Arrays.asList("OK")) {
				@Override
				protected void selectCallback(String enumValue) {
					MenuStateEditingLaunch.this.changePopupState(null);
				}
			});
			return;
		}
		MenuPopupStateLoading popupState = new MenuPopupStateLoading("Exporting: '" + launchConf.displayName + "'...");
		changePopupState(popupState);
		BootMenuMain.runLaterMS(() -> {
			try {
				OfflineDownloadFactory.downloadOffline(launchConf, clientData, importClientBlobs, popupState);
			}catch(Throwable t) {
				logger.error("Failed to export offline!");
				logger.error(t);
				changePopupState(new MenuPopupStateConfirmation<String>("Error: Failed to export offline!\n\n" + t.toString(),
						Arrays.asList("OK")) {
					@Override
					protected void selectCallback(String enumValue) {
						MenuStateEditingLaunch.this.changePopupState(null);
					}
				});
				return;
			}
			MenuStateEditingLaunch.this.changePopupState(null);
		}, 250);
		
	}

	private void deleteConfig() {
		changePopupState(new MenuPopupStateLoading("Deleting: '" + launchConf.displayName + "'..."));
		BootMenuMain.runLaterMS(() -> {
			try {
				BootMenuMain.bootMenuDataManager.deleteLaunchConfig(launchConf.uuid);
			}finally {
				BootMenuMain.changeState(new MenuStateBoot(false));
			}
		}, 250);
	}

	private void returnToMenu() {
		if(previousMenu != null) {
			BootMenuMain.changeState(previousMenu);
		}
	}

	@Override
	protected void handleKeyUp(int keyCode) {
		if(keyCode == KeyCodes.DOM_KEY_CONTROL) {
			controlDown = false;
		}
	}

	@Override
	protected void handleKeyRepeat(int keyCode) {
		
	}

	@Override
	protected void handleOnChanged(HTMLElement htmlElement) {
		if(BootMenuMain.bootMenuDOM.launch_conf_val_launch_type == htmlElement) {
			EnumClientLaunchType launchType = launchConf.type;
			readAllValues();
			if(launchConf.type != launchType) {
				setEnabledControlSet();
				setControlSetValues();
				setVisibleControlSet();
			}
		}
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

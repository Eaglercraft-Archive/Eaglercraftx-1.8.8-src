package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.teavm.jso.dom.html.HTMLElement;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuMetadata.DefaultLaunchTemplate;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.OfflineDownloadParser.ParsedOfflineAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
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
public class MenuStateBoot extends MenuState {

	private static final Logger logger = LogManager.getLogger("MenuStateBoot");

	private static class BootItem implements SelectionListController.ListItem, Runnable {

		private final String name;
		private final Consumer<BootItem> runnable;
		private final Consumer<BootItem> onEdit;

		private BootItem(String name, Consumer<BootItem> runnable, Consumer<BootItem> onEdit) {
			this.name = name;
			this.runnable = runnable;
			this.onEdit = onEdit;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void run() {
			runnable.accept(this);
		}

	}

	protected SelectionListController<BootItem> selectionController;

	private static enum EnumImportExportMenu {
		IMPORT_CLIENT("Import Client"),
		EXPORT_CLIENT("Export Client"),
		EXPORT_OFFLINE_BUNDLE("Export Offline Bundle"),
		CANCEL("Cancel");
		
		private final String str;
		
		private EnumImportExportMenu(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}

	}

	private static final List<EnumImportExportMenu> OPTIONS_NO_BUNDLE = Arrays.asList(
			EnumImportExportMenu.IMPORT_CLIENT, EnumImportExportMenu.EXPORT_CLIENT, EnumImportExportMenu.CANCEL);

	private static final List<EnumImportExportMenu> OPTIONS_BUNDLE = Arrays.asList(EnumImportExportMenu.IMPORT_CLIENT,
			EnumImportExportMenu.EXPORT_CLIENT, EnumImportExportMenu.EXPORT_OFFLINE_BUNDLE,
			EnumImportExportMenu.CANCEL);

	private static enum EnumImportModeMenu {
		AUTO_DETECT("Auto Detect"),
		EAGLERCRAFT_EPK_FILE(".EPK Client Archive"),
		EAGLERCRAFTX_1_8_OFFLINE("EaglercraftX 1.8 Offline .HTML"),
		EAGLERCRAFTX_1_8_SIGNED("EaglercraftX 1.8 Signed .HTML"),
		EAGLERCRAFTX_1_8_FAT_OFFLINE("EaglercraftX 1.8 Fat Offline .HTML"),
		EAGLERCRAFTX_1_8_FAT_SIGNED("EaglercraftX 1.8 Fat Signed .HTML"),
		EAGLERCRAFT_1_5_NEW_OFFLINE("Eaglercraft 1.5.2 Offline .HTML (post-22w34a)"),
		EAGLERCRAFT_1_5_OLD_OFFLINE("Eaglercraft 1.5.2 Offline .HTML (pre-22w34a)"),
		EAGLERCRAFT_BETA_B1_3_OFFLINE("Eaglercraft Beta 1.3 Offline .HTML"),
		PEYTONPLAYZ585_BETA_1_7_3("PeytonPlayz585 Beta 1.7.3 Offline .HTML"),
		PEYTONPLAYZ585_ALPHA_1_2_6("PeytonPlayz585 Alpha 1.2.6 Offline .HTML"),
		PEYTONPLAYZ585_INDEV("PeytonPlayz585 Indev Offline .HTML"),
		CANCEL("Cancel");
		
		private final String str;
		
		private EnumImportModeMenu(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}

	}

	private static enum EnumUnsignedClientAction {
		DOWNLOAD_OFFLINE("Download Client"),
		DOWNLOAD_EAGLERX_OFFLINE("Download EaglercraftX"),
		CANCEL("Cancel");
		
		private final String str;
		
		private EnumUnsignedClientAction(String str) {
			this.str = str;
		}
		
		@Override
		public String toString() {
			return str;
		}
	}

	public static void displayUnsignedError(final MenuState parentState, final Consumer<IProgressMsgCallback> onDownloadOffline, final Runnable onDone) {
		parentState.changePopupState(new MenuPopupStateConfirmation<EnumUnsignedClientAction>(
				"Error: This client does not have a valid signature!\n\nUnsigned clients are disabled on this website",
				Arrays.asList(EnumUnsignedClientAction.values())) {
			@Override
			protected void selectCallback(EnumUnsignedClientAction enumValue) {
				switch(enumValue) {
				case DOWNLOAD_OFFLINE:
					MenuPopupStateLoading loadingScreen = new MenuPopupStateLoading("Downloading client...");
					parentState.changePopupState(loadingScreen);
					try {
						onDownloadOffline.accept(loadingScreen);
					}catch(Throwable t) {
						logger.error("Failed to invoke download offline function!");
						logger.error(t);
						parentState.changePopupState(new MenuPopupStateConfirmation<String>(
								"Error: Failed to download!\n\n" + t.toString(),
								Arrays.asList("OK")) {
							@Override
							protected void selectCallback(String enumValue) {
								onDone.run();
							}
						});
						return;
					}
					onDone.run();
					break;
				case DOWNLOAD_EAGLERX_OFFLINE:
					loadingScreen = new MenuPopupStateLoading("Downloading client...");
					parentState.changePopupState(loadingScreen);
					try {
						BootableClientEntry.getOriginClient().bootAdapter.downloadOffline(loadingScreen);
					}catch(Throwable t) {
						logger.error("Failed to invoke download offline function!");
						logger.error(t);
						parentState.changePopupState(new MenuPopupStateConfirmation<String>(
								"Error: Failed to download!\n\n" + t.toString(),
								Arrays.asList("OK")) {
							@Override
							protected void selectCallback(String enumValue) {
								onDone.run();
							}
						});
						return;
					}
					onDone.run();
					break;
				case CANCEL:
				default:
					onDone.run();
					break;
				}
			}
		});
	}

	protected int bootCountDown = 0;
	protected int bootCountDownCur = 0;
	protected long bootCountDownStart = 0l;
	protected boolean doCountDown;

	public MenuStateBoot(boolean doCountDown) {
		this.doCountDown = doCountDown;
		List<BootItem> list = new ArrayList<>();
		final List<BootableClientEntry> bootableClients = BootableClientEntry.enumerateBootableClients();
		if(BootableClientEntry.applyClientOrdering(BootMenuMain.bootMenuDataManager.launchOrderList, bootableClients)) {
			BootMenuMain.bootMenuDataManager.writeManifest();
		}
		for(int i = 0, l = bootableClients.size(); i < l; ++i) {
			final BootableClientEntry etr = bootableClients.get(i);
			list.add(new BootItem(etr.bootAdapter.getDisplayName(),
					(itm) -> {
						MenuPopupStateLoading popupState = new MenuPopupStateLoading("Booting: '" + itm.name + "'...");
						MenuStateBoot.this.changePopupState(popupState);
						BootMenuMain.runLaterMS(() -> {
							try {
								etr.bootAdapter.bootClient(popupState);
							}catch(UnsignedBootException ex) {
								displayUnsignedError(MenuStateBoot.this, etr.bootAdapter::downloadOffline, () -> {
									MenuStateBoot.this.changePopupState(null);
								});
								return;
							}catch(Throwable t) {
								logger.error("Failed to boot client!");
								logger.error(t);
								changePopupState(new MenuPopupStateConfirmation<String>("Error: Failed to boot client!\n\n" + t.toString(),
										Arrays.asList("OK")) {
									@Override
									protected void selectCallback(String enumValue) {
										MenuStateBoot.this.changePopupState(null);
									}
								});
								return;
							}
						}, 250);
					}, (itm) -> {
						ClientDataEntry clientData = etr.bootAdapter.getClientDataEntry();
						LaunchConfigEntry launchConf = etr.bootAdapter.getLaunchConfigEntry();
						if(clientData != null && launchConf != null) {
							BootMenuMain.changeState(new MenuStateEditingLaunch(MenuStateBoot.this, launchConf.fork(),
									clientData, false, etr.bootAdapter.getBlobLoaders()));
						}
					}));
		}
		list.add(new BootItem("Import/Export",
				(itm) -> {
					MenuStateBoot.this.changePopupState(MenuPopupStateSelection.createHelper("What do you wanna do?",
							bootableClients.size() > 1 ? OPTIONS_BUNDLE : OPTIONS_NO_BUNDLE, (enumValue) -> {
								switch(enumValue) {
								case IMPORT_CLIENT:
									MenuStateBoot.this.changePopupState(MenuPopupStateSelection.createHelper("Select the format of the client:",
											Arrays.asList(EnumImportModeMenu.values()), (enumValue2) -> {
												EnumOfflineParseType parseType = null;
												if(enumValue2 == EnumImportModeMenu.CANCEL) {
													MenuStateBoot.this.changePopupState(null);
													return;
												}else if(enumValue2 != EnumImportModeMenu.AUTO_DETECT) {
													switch(enumValue2) {
													case EAGLERCRAFTX_1_8_OFFLINE: parseType = EnumOfflineParseType.EAGLERCRAFTX_1_8_OFFLINE; break;
													case EAGLERCRAFTX_1_8_SIGNED: parseType = EnumOfflineParseType.EAGLERCRAFTX_1_8_SIGNED; break;
													case EAGLERCRAFTX_1_8_FAT_OFFLINE: parseType = EnumOfflineParseType.EAGLERCRAFTX_1_8_FAT_OFFLINE; break;
													case EAGLERCRAFTX_1_8_FAT_SIGNED: parseType = EnumOfflineParseType.EAGLERCRAFTX_1_8_FAT_SIGNED; break;
													case EAGLERCRAFT_1_5_NEW_OFFLINE: parseType = EnumOfflineParseType.EAGLERCRAFT_1_5_NEW_OFFLINE; break;
													case EAGLERCRAFT_1_5_OLD_OFFLINE: parseType = EnumOfflineParseType.EAGLERCRAFT_1_5_OLD_OFFLINE; break;
													case EAGLERCRAFT_BETA_B1_3_OFFLINE: parseType = EnumOfflineParseType.EAGLERCRAFT_BETA_B1_3_OFFLINE; break;
													case PEYTONPLAYZ585_BETA_1_7_3: parseType = EnumOfflineParseType.PEYTONPLAYZ585_ALPHA_BETA; break;
													case PEYTONPLAYZ585_ALPHA_1_2_6: parseType = EnumOfflineParseType.PEYTONPLAYZ585_ALPHA_BETA; break;
													case PEYTONPLAYZ585_INDEV: parseType = EnumOfflineParseType.PEYTONPLAYZ585_INDEV; break;
													case EAGLERCRAFT_EPK_FILE: parseType = EnumOfflineParseType.EAGLERCRAFT_EPK_FILE; break;
													default:
														MenuStateBoot.this.changePopupState(null);
														return;
													}
												}
												String mime = parseType == EnumOfflineParseType.EAGLERCRAFT_EPK_FILE ? null : "text/html";
												String ext = parseType == EnumOfflineParseType.EAGLERCRAFT_EPK_FILE ? "epk" : "html";
												final EnumOfflineParseType parseTypeF = parseType;
												MenuStateBoot.this.changePopupState(new MenuPopupStateFileChooser("Importing client...", mime, ext) {
													@Override
													protected void onResult(FileChooserResult res) {
														if(res != null) {
															MenuPopupStateLoading loadingScreen = new MenuPopupStateLoading("Importing client...");
															MenuStateBoot.this.changePopupState(loadingScreen);
															EagUtils.sleep(50l);
															String offlineData = new String(res.fileData, StandardCharsets.UTF_8).replace("\r\n", "\n");
															EnumOfflineParseType parseType2 = parseTypeF;
															if(parseType2 == null) {
																loadingScreen.updateMessage("Detecting type...");
																try {
																	parseType2 = OfflineDownloadParser.detectOfflineType(offlineData);
																	if(parseType2 == null) {
																		throw new IllegalStateException("Failed to detect offline download type!");
																	}
																}catch(Throwable t) {
																	MenuStateBoot.this.changePopupState(new MenuPopupStateConfirmation<EnumImportModeMenu>(
																			t.toString(), Arrays.asList(EnumImportModeMenu.CANCEL)) {
																		@Override
																		protected void selectCallback(EnumImportModeMenu enumValue) {
																			MenuStateBoot.this.changePopupState(null);
																			return;
																		}
																	});
																	return;
																}
															}
															loadingScreen.updateMessage("Parsing file...");
															List<ParsedOfflineAdapter> parsedOfflines;
															try {
																if(parseType2 == EnumOfflineParseType.EAGLERCRAFT_EPK_FILE) {
																	parsedOfflines = EPKClientParser.parseEPKClient(res.fileData);
																}else {
																	parsedOfflines = OfflineDownloadParser.parseOffline(parseType2, offlineData);
																}
																if(parsedOfflines == null || parsedOfflines.size() == 0) {
																	throw new IllegalStateException("Failed to parse the file as \"" + parseType2 + "\"!");
																}
															}catch(Throwable t) {
																MenuStateBoot.this.changePopupState(new MenuPopupStateConfirmation<EnumImportModeMenu>(
																		t.toString(), Arrays.asList(EnumImportModeMenu.CANCEL)) {
																	@Override
																	protected void selectCallback(EnumImportModeMenu enumValue) {
																		MenuStateBoot.this.changePopupState(null);
																	}
																});
																return;
															}
															if(parsedOfflines.size() == 1) {
																ParsedOfflineAdapter pp = parsedOfflines.get(0);
																if(pp.launchData == null) {
																	List<DefaultLaunchTemplate> templates = BootMenuMain.bootMenuMetadata.getTemplatesForParseType(pp.parseType);
																	if(templates.size() == 0) {
																		throw new IllegalStateException("Missing default launch type for parse type: " + pp.parseType);
																	}
																	if(templates.size() == 1) {
																		EaglercraftUUID rotatedLaunchUUID = EaglercraftUUID.randomUUID();
																		EaglercraftUUID rotatedClientUUID = EaglercraftUUID.randomUUID();
																		LaunchConfigEntry launchConfig = templates.get(0).createLaunchConfig(rotatedLaunchUUID, rotatedClientUUID);
																		ClientDataEntry clientData = pp.clientData.rotateUUID(rotatedClientUUID);
																		MenuStateBoot.this.changePopupState(null);
																		BootMenuMain.changeState(MenuStateEditingLaunch.createHelper(MenuStateBoot.this, launchConfig, clientData, true, pp.blobs));
																	}else {
																		MenuStateBoot.this.changePopupState(
																				MenuPopupStateSelection.createHelper(
																						"Please select the template launch configuration to use:",
																						templates, (template) -> {
																			if(template != null) {
																				EaglercraftUUID rotatedLaunchUUID = EaglercraftUUID.randomUUID();
																				EaglercraftUUID rotatedClientUUID = EaglercraftUUID.randomUUID();
																				LaunchConfigEntry launchConfig = template.createLaunchConfig(rotatedLaunchUUID, rotatedClientUUID);
																				ClientDataEntry clientData = pp.clientData.rotateUUID(rotatedClientUUID);
																				MenuStateBoot.this.changePopupState(null);
																				BootMenuMain.changeState(MenuStateEditingLaunch.createHelper(MenuStateBoot.this, launchConfig, clientData, true, pp.blobs));
																			}else {
																				MenuStateBoot.this.changePopupState(null);
																			}
																		}));
																	}
																}else {
																	EaglercraftUUID rotatedLaunchUUID = EaglercraftUUID.randomUUID();
																	EaglercraftUUID rotatedClientUUID = EaglercraftUUID.randomUUID();
																	LaunchConfigEntry launchConfig = pp.launchData.rotateUUIDs(rotatedLaunchUUID, rotatedClientUUID);
																	ClientDataEntry clientData = pp.clientData.rotateUUID(rotatedClientUUID);
																	MenuStateBoot.this.changePopupState(null);
																	BootMenuMain.changeState(MenuStateEditingLaunch.createHelper(MenuStateBoot.this, launchConfig, clientData, true, pp.blobs));
																}
															}else {
																MenuStateBoot.this.changePopupState(null);
																BootMenuMain.changeState(new MenuStateImportMultiSelect(MenuStateBoot.this, parsedOfflines));
															}
														}else {
															MenuStateBoot.this.changePopupState(null);
															return;
														}
													}
												});
												
											}));
									break;
								case EXPORT_CLIENT:
									final MenuState[] formatSelState = new MenuState[1];
									formatSelState[0] = MenuPopupStateSelection.createHelper("Select the format of the client:",
											Arrays.asList(EnumImportModeMenu.AUTO_DETECT, EnumImportModeMenu.EAGLERCRAFT_EPK_FILE,
													EnumImportModeMenu.EAGLERCRAFTX_1_8_OFFLINE,
													EnumImportModeMenu.EAGLERCRAFTX_1_8_SIGNED,
													EnumImportModeMenu.EAGLERCRAFT_1_5_NEW_OFFLINE,
													EnumImportModeMenu.EAGLERCRAFT_1_5_OLD_OFFLINE,
													EnumImportModeMenu.EAGLERCRAFT_BETA_B1_3_OFFLINE,
													EnumImportModeMenu.PEYTONPLAYZ585_BETA_1_7_3,
													EnumImportModeMenu.PEYTONPLAYZ585_ALPHA_1_2_6,
													EnumImportModeMenu.PEYTONPLAYZ585_INDEV,
													EnumImportModeMenu.CANCEL), (enumValue2) -> {
												List<BootableClientEntry> filteredList;
												if(enumValue2 == EnumImportModeMenu.CANCEL) {
													MenuStateBoot.this.changePopupState(null);
													return;
												}else if(enumValue2 == EnumImportModeMenu.AUTO_DETECT || enumValue2 == EnumImportModeMenu.EAGLERCRAFT_EPK_FILE) {
													filteredList = bootableClients;
												}else {
													filteredList = Lists.newArrayList(Collections2.filter(bootableClients, (etr) -> {
														switch(enumValue2) {
														case EAGLERCRAFTX_1_8_OFFLINE:
														case EAGLERCRAFT_1_5_OLD_OFFLINE:
														case EAGLERCRAFT_BETA_B1_3_OFFLINE:
														case PEYTONPLAYZ585_BETA_1_7_3:
														case PEYTONPLAYZ585_ALPHA_1_2_6:
														case PEYTONPLAYZ585_INDEV:
															return etr.bootAdapter.getClientDataEntry().type == EnumClientFormatType.EAGLER_STANDARD_OFFLINE;
														case EAGLERCRAFTX_1_8_SIGNED:
															return etr.bootAdapter.getClientDataEntry().type == EnumClientFormatType.EAGLER_SIGNED_OFFLINE;
														case EAGLERCRAFT_1_5_NEW_OFFLINE:
															return etr.bootAdapter.getClientDataEntry().type == EnumClientFormatType.EAGLER_STANDARD_1_5_OFFLINE;
														default:
															return false;
														}
													}));
												}
												if(filteredList.size() > 0) {
													MenuStateBoot.this.changePopupState(null);
													BootMenuMain.changeState(new MenuStateSelectExportClients(enumValue2 == EnumImportModeMenu.EAGLERCRAFT_EPK_FILE, MenuStateBoot.this, filteredList));
												}else {
													changePopupState(new MenuPopupStateConfirmation<String>("Error: No clients available to export!",
															Arrays.asList("OK")) {
														@Override
														protected void selectCallback(String enumValue) {
															MenuStateBoot.this.changePopupState(formatSelState[0]);
														}
													});
												}
											});
									MenuStateBoot.this.changePopupState(formatSelState[0]);
									break;
								case EXPORT_OFFLINE_BUNDLE:
									MenuStateBoot.this.changePopupState(null);
									BootMenuMain.changeState(new MenuStateClientMultiSelect(MenuStateBoot.this, bootableClients) {
										@Override
										protected void onDone(List<BootableClientEntry> entries) {
											if(entries.size() > 0) {
												MenuPopupStateLoading popupState = new MenuPopupStateLoading("Exporting Fat Offline...");
												this.changePopupState(popupState);
												try {
													FatOfflineDownloadFactory.downloadOffline(entries, popupState);
												}catch(Throwable t) {
													logger.error("Failed to export fat offline!");
													logger.error(t);
													changePopupState(new MenuPopupStateConfirmation<String>(
															"Error: Failed to export!\n\n" + t.toString(),
															Arrays.asList("OK")) {
														@Override
														protected void selectCallback(String enumValue) {
															BootMenuMain.changeState(MenuStateBoot.this);
														}
													});
													return;
												}
											}
											BootMenuMain.changeState(MenuStateBoot.this);
										}
									});
									break;
								case CANCEL:
									MenuStateBoot.this.changePopupState(null);
									break;
								default:
									break;
								}
							}));
				}, null));
		list.add(new BootItem("Enter Setup",
				(itm) -> {
					BootMenuMain.changeState(new MenuStateEnterSetup(bootableClients));
				}, null));
		selectionController = new SelectionListController<BootItem>(BootMenuMain.bootMenuDOM.content_selection, list) {
				@Override
				protected void itemSelected(BootItem item) {
					item.run();
				}
		};
	}

	@Override
	protected void enterState() {
		if(doCountDown) {
			bootCountDownStart = EagRuntime.steadyTimeMillis();
			bootCountDown = BootMenuMain.bootMenuDataManager.confBootTimeout;
			bootCountDownCur = bootCountDown;
		}
		selectionController.setup();
		if(bootCountDown > 0) {
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.footer_text_boot_select_count);
			BootMenuMain.bootMenuDOM.footer_text_boot_countdown.setInnerText(Integer.toString(bootCountDown));
		}else {
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.footer_text_boot_select);
		}
		BootMenuDOM.show(BootMenuMain.bootMenuDOM.content_view_selection);
	}

	@Override
	protected void exitState() {
		selectionController.destroy();
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.footer_text_boot_select);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.footer_text_boot_select_count);
		BootMenuDOM.hide(BootMenuMain.bootMenuDOM.content_view_selection);
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
		boolean cancelEsc = bootCountDown > 0;
		cancelCountdown();
		if(keyCode == KeyCodes.DOM_KEY_ESCAPE) {
			if(!cancelEsc) {
				BootItem def = selectionController.selectionEnableList.get(0).listItem;
				def.runnable.accept(def);
			}
		}else if(keyCode == KeyCodes.DOM_KEY_E) {
			BootItem itm = selectionController.getSelected();
			if(itm.onEdit != null) {
				itm.onEdit.accept(itm);
			}
		}else {
			selectionController.handleKeyDown(keyCode);
		}
	}

	@Override
	protected void handleKeyUp(int keyCode) {
		
	}

	@Override
	protected void handleKeyRepeat(int keyCode) {
		cancelCountdown();
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
		if(bootCountDown > 0) {
			int countDownCur = bootCountDown - (int)((EagRuntime.steadyTimeMillis() - bootCountDownStart) / 1000l);
			if(countDownCur < 0) {
				BootItem def = selectionController.selectionEnableList.get(0).listItem;
				def.runnable.accept(def);
				bootCountDown = 0;
				return;
			}
			if(bootCountDownCur != countDownCur) {
				bootCountDownCur = countDownCur;
				BootMenuMain.bootMenuDOM.footer_text_boot_countdown.setInnerText(Integer.toString(countDownCur));
			}
		}
	}

	public void cancelCountdown() {
		if(bootCountDown > 0) {
			bootCountDown = 0;
			BootMenuDOM.hide(BootMenuMain.bootMenuDOM.footer_text_boot_select_count);
			BootMenuDOM.show(BootMenuMain.bootMenuDOM.footer_text_boot_select);
		}
	}

}

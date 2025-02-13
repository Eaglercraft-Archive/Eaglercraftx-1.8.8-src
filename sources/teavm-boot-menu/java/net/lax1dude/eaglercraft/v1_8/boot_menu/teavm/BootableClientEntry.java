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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.boot_menu.teavm.BootMenuEntryPoint.UnsignedClientEPKLoader;

public class BootableClientEntry {

	public static enum EnumDataType {
		SITE_ORIGIN, EMBEDDED, LOCAL_STORAGE;
	}

	public static interface BootAdapter {
		String getDisplayName();
		void bootClient(IProgressMsgCallback cb);
		void downloadOffline(IProgressMsgCallback cb);
		void downloadEPK(IProgressMsgCallback cb);
		ClientDataEntry getClientDataEntry();
		LaunchConfigEntry getLaunchConfigEntry();
		Map<EaglercraftUUID,Supplier<byte[]>> getBlobLoaders();
	}

	public final EnumDataType dataType;
	public final BootAdapter bootAdapter;

	public BootableClientEntry(EnumDataType dataType, BootAdapter bootAdapter) {
		this.dataType = dataType;
		this.bootAdapter = bootAdapter;
	}

	public static BootableClientEntry getOriginClient() {
		final ClientDataEntry originClientDat;
		final LaunchConfigEntry originLaunchDat;
		final Map<EaglercraftUUID, Supplier<byte[]>> siteOriginLoader = new HashMap<>(2);
		if(BootMenuEntryPoint.isSignedClient()) {
			siteOriginLoader.put(BootMenuConstants.UUID_ORIGIN_SIGNED_SIGNATURE, BootMenuEntryPoint::getSignedClientSignature);
			siteOriginLoader.put(BootMenuConstants.UUID_ORIGIN_SIGNED_BUNDLE, BootMenuEntryPoint::getSignedClientBundle);
			originClientDat = new ClientDataEntry(EnumClientFormatType.EAGLER_SIGNED_OFFLINE,
					BootMenuConstants.UUID_CLIENT_DATA_ORIGIN, BootMenuConstants.UUID_ORIGIN_SIGNED_BUNDLE, null,
					BootMenuConstants.UUID_ORIGIN_SIGNED_SIGNATURE, null);
			originLaunchDat = new LaunchConfigEntry(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN,
					BootMenuConstants.UUID_CLIENT_DATA_ORIGIN,
					BootMenuConstants.client_projectForkName + " " + BootMenuConstants.client_projectOriginRevision
							+ " " + BootMenuConstants.client_projectOriginVersion,
					EnumClientLaunchType.EAGLERX_SIGNED_V1, null, null, null, null, null,
					BootMenuEntryPoint.getOriginLaunchOpts(), false);
		}else {
			siteOriginLoader.put(BootMenuConstants.UUID_ORIGIN_UNSIGNED_CLASSES_JS, BootMenuEntryPoint::getUnsignedClientClassesJS);
			UnsignedClientEPKLoader loader = BootMenuEntryPoint.getUnsignedClientAssetsEPK();
			siteOriginLoader.putAll(loader.loaders);
			originClientDat = new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE,
					BootMenuConstants.UUID_CLIENT_DATA_ORIGIN, BootMenuConstants.UUID_ORIGIN_UNSIGNED_CLASSES_JS, null,
					null, loader.list);
			originLaunchDat = new LaunchConfigEntry(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN,
						BootMenuConstants.UUID_CLIENT_DATA_ORIGIN,
						BootMenuConstants.client_projectForkName + " " + BootMenuConstants.client_projectOriginRevision
								+ " " + BootMenuConstants.client_projectOriginVersion,
						EnumClientLaunchType.EAGLERX_V1, null, null, null, null, null,
						BootMenuEntryPoint.getOriginLaunchOpts(), false);
		}
		return new BootableClientEntry(EnumDataType.SITE_ORIGIN, new BootAdapter() {

			private final String displayName = BootMenuConstants.client_projectForkName + " " + BootMenuConstants.client_projectOriginRevision + " "
					+ BootMenuConstants.client_projectOriginVersion + " (site origin)";

			@Override
			public String getDisplayName() {
				return displayName;
			}

			@Override
			public void bootClient(IProgressMsgCallback cb) {
				BootMenuMain.continueBootToOriginClient(BootMenuMain::sanitizeEaglercraftXOpts);
			}

			@Override
			public void downloadOffline(IProgressMsgCallback cb) {
				OfflineDownloadFactory.downloadOffline(originLaunchDat, originClientDat, siteOriginLoader, cb);
			}

			@Override
			public void downloadEPK(IProgressMsgCallback cb) {
				EPKClientFactory.downloadEPKClient(originLaunchDat, originClientDat, siteOriginLoader, cb);
			}

			@Override
			public ClientDataEntry getClientDataEntry() {
				return originClientDat;
			}

			@Override
			public LaunchConfigEntry getLaunchConfigEntry() {
				return originLaunchDat;
			}

			@Override
			public Map<EaglercraftUUID, Supplier<byte[]>> getBlobLoaders() {
				return siteOriginLoader;
			}

		});
	}

	public static List<BootableClientEntry> enumerateBootableClients() {
		List<BootableClientEntry> ret = new ArrayList<>(16);
		final Map<EaglercraftUUID, Supplier<byte[]>> siteOriginLoader = new HashMap<>(2);
		final ClientDataEntry originClientDat;
		final LaunchConfigEntry originLaunchDat;
		if(BootMenuEntryPoint.isSignedClient()) {
			siteOriginLoader.put(BootMenuConstants.UUID_ORIGIN_SIGNED_SIGNATURE, BootMenuEntryPoint::getSignedClientSignature);
			siteOriginLoader.put(BootMenuConstants.UUID_ORIGIN_SIGNED_BUNDLE, BootMenuEntryPoint::getSignedClientBundle);
			originClientDat = new ClientDataEntry(EnumClientFormatType.EAGLER_SIGNED_OFFLINE,
					BootMenuConstants.UUID_CLIENT_DATA_ORIGIN, BootMenuConstants.UUID_ORIGIN_SIGNED_BUNDLE, null,
					BootMenuConstants.UUID_ORIGIN_SIGNED_SIGNATURE, null);
			originLaunchDat = new LaunchConfigEntry(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN,
					BootMenuConstants.UUID_CLIENT_DATA_ORIGIN,
					BootMenuConstants.client_projectForkName + " " + BootMenuConstants.client_projectOriginRevision
							+ " " + BootMenuConstants.client_projectOriginVersion,
					EnumClientLaunchType.EAGLERX_SIGNED_V1, null, null, null, null, null,
					BootMenuEntryPoint.getOriginLaunchOpts(), false);
		}else {
			siteOriginLoader.put(BootMenuConstants.UUID_ORIGIN_UNSIGNED_CLASSES_JS, BootMenuEntryPoint::getUnsignedClientClassesJS);
			UnsignedClientEPKLoader loader = BootMenuEntryPoint.getUnsignedClientAssetsEPK();
			siteOriginLoader.putAll(loader.loaders);
			originClientDat = new ClientDataEntry(EnumClientFormatType.EAGLER_STANDARD_OFFLINE,
					BootMenuConstants.UUID_CLIENT_DATA_ORIGIN, BootMenuConstants.UUID_ORIGIN_UNSIGNED_CLASSES_JS, null,
					null, loader.list);
			originLaunchDat = new LaunchConfigEntry(BootMenuConstants.UUID_CLIENT_LAUNCH_ORIGIN,
						BootMenuConstants.UUID_CLIENT_DATA_ORIGIN,
						BootMenuConstants.client_projectForkName + " " + BootMenuConstants.client_projectOriginRevision
								+ " " + BootMenuConstants.client_projectOriginVersion,
						EnumClientLaunchType.EAGLERX_V1, null, null, null, null, null,
						BootMenuEntryPoint.getOriginLaunchOpts(), false);
		}
		ret.add(new BootableClientEntry(EnumDataType.SITE_ORIGIN, new BootAdapter() {

			private final String displayName = BootMenuConstants.client_projectForkName + " " + BootMenuConstants.client_projectOriginRevision + " "
					+ BootMenuConstants.client_projectOriginVersion + " (site origin)";

			@Override
			public String getDisplayName() {
				return displayName;
			}

			@Override
			public void bootClient(IProgressMsgCallback cb) {
				BootMenuMain.continueBootToOriginClient(BootMenuMain::sanitizeEaglercraftXOpts);
			}

			@Override
			public void downloadOffline(IProgressMsgCallback cb) {
				OfflineDownloadFactory.downloadOffline(originLaunchDat, originClientDat, siteOriginLoader, cb);
			}

			@Override
			public void downloadEPK(IProgressMsgCallback cb) {
				EPKClientFactory.downloadEPKClient(originLaunchDat, originClientDat, siteOriginLoader, cb);
			}

			@Override
			public ClientDataEntry getClientDataEntry() {
				return originClientDat;
			}

			@Override
			public LaunchConfigEntry getLaunchConfigEntry() {
				return originLaunchDat;
			}

			@Override
			public Map<EaglercraftUUID, Supplier<byte[]>> getBlobLoaders() {
				return siteOriginLoader;
			}

		}));
		for(final LaunchConfigEntry etr : BootMenuMain.bootMenuFatOfflineLoader.launchDatas) {
			final ClientDataEntry etr2 = BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(etr.clientDataUUID)
					? originClientDat
					: BootMenuMain.bootMenuFatOfflineLoader.clientDatas.get(etr.clientDataUUID);
			if(etr2 != null) {
				Collection<EaglercraftUUID> refBlobs = etr2.getReferencedBlobs();
				final Map<EaglercraftUUID, Supplier<byte[]>> loaderMap = new HashMap<>(2);
				for(final EaglercraftUUID uuid : refBlobs) {
					loaderMap.put(uuid, () -> {
						Supplier<byte[]> sup = siteOriginLoader.get(uuid);
						if(sup != null) {
							byte[] ret2 = sup.get();
							if(ret2 != null) {
								return ret2;
							}
						}
						return BootMenuMain.bootMenuFatOfflineLoader.loadDataBinary(uuid.toString());
					});
				}
				ret.add(new BootableClientEntry(EnumDataType.EMBEDDED, new BootAdapter() {

					private final String displayName = etr.displayName + " (embedded"
							+ (etr.type == EnumClientLaunchType.EAGLERX_SIGNED_V1 ? ", signed)" : ")");

					@Override
					public String getDisplayName() {
						return displayName;
					}

					@Override
					public void bootClient(IProgressMsgCallback cb) {
						ClientBootFactory.bootClient(etr, etr2, loaderMap, cb);
					}

					@Override
					public void downloadOffline(IProgressMsgCallback cb) {
						OfflineDownloadFactory.downloadOffline(etr, etr2, loaderMap, cb);
					}

					@Override
					public void downloadEPK(IProgressMsgCallback cb) {
						EPKClientFactory.downloadEPKClient(etr, etr2, loaderMap, cb);
					}

					@Override
					public ClientDataEntry getClientDataEntry() {
						return etr2;
					}

					@Override
					public LaunchConfigEntry getLaunchConfigEntry() {
						return etr;
					}

					@Override
					public Map<EaglercraftUUID, Supplier<byte[]>> getBlobLoaders() {
						return loaderMap;
					}

				}));
			}
		}
		for(EaglercraftUUID etrUUID : BootMenuMain.bootMenuDataManager.launchDatasList) {
			final LaunchConfigEntry etr = BootMenuMain.bootMenuDataManager.launchDatas.get(etrUUID);
			if(etr != null) {
				final ClientDataEntry etr2 = BootMenuConstants.UUID_CLIENT_DATA_ORIGIN.equals(etr.clientDataUUID)
						? originClientDat
						: BootMenuMain.bootMenuDataManager.clientDatas.get(etr.clientDataUUID);
				if(etr2 != null) {
					Collection<EaglercraftUUID> refBlobs = etr2.getReferencedBlobs();
					final Map<EaglercraftUUID, Supplier<byte[]>> loaderMap = new HashMap<>(2);
					for(final EaglercraftUUID uuid : refBlobs) {
						loaderMap.put(uuid, () -> {
							Supplier<byte[]> sup = siteOriginLoader.get(uuid);
							if(sup != null) {
								byte[] ret2 = sup.get();
								if(ret2 != null) {
									return ret2;
								}
							}
							return BootMenuMain.bootMenuDatastore.getItem("blobs/" + uuid);
						});
					}
					ret.add(new BootableClientEntry(EnumDataType.LOCAL_STORAGE, new BootAdapter() {

						private final String displayName = etr.displayName + " (local storage"
								+ (etr.type == EnumClientLaunchType.EAGLERX_SIGNED_V1 ? ", signed)" : ")");

						@Override
						public String getDisplayName() {
							return displayName;
						}

						@Override
						public void bootClient(IProgressMsgCallback cb) {
							ClientBootFactory.bootClient(etr, etr2, loaderMap, cb);
						}

						@Override
						public void downloadOffline(IProgressMsgCallback cb) {
							OfflineDownloadFactory.downloadOffline(etr, etr2, loaderMap, cb);
						}

						@Override
						public void downloadEPK(IProgressMsgCallback cb) {
							EPKClientFactory.downloadEPKClient(etr, etr2, loaderMap, cb);
						}

						@Override
						public ClientDataEntry getClientDataEntry() {
							return etr2;
						}

						@Override
						public LaunchConfigEntry getLaunchConfigEntry() {
							return etr;
						}

						@Override
						public Map<EaglercraftUUID, Supplier<byte[]>> getBlobLoaders() {
							return loaderMap;
						}

					}));
				}
			}
		}
		return ret;
	}

	/**
	 * returns true if uuidsList has changed
	 */
	public static boolean applyClientOrdering(List<EaglercraftUUID> uuidsList, List<BootableClientEntry> bootableClients) {
		if(bootableClients.isEmpty()) return false;
		
		List<BootableClientEntry> bootableClientsCopy = new ArrayList<>(bootableClients);
		Set<EaglercraftUUID> uuidsSet = new HashSet<>(uuidsList);
		Map<EaglercraftUUID,BootableClientEntry> bootableClientsLookup = new HashMap<>(bootableClients.size());
		bootableClients.clear();
		
		for(int i = 0, l = bootableClientsCopy.size(); i < l; ++i) {
			BootableClientEntry etr = bootableClientsCopy.get(i);
			bootableClientsLookup.put(etr.bootAdapter.getLaunchConfigEntry().uuid, etr);
		}
		
		for(int i = 0, l = uuidsList.size(); i < l; ++i) {
			BootableClientEntry etr = bootableClientsLookup.get(uuidsList.get(i));
			if(etr != null) {
				bootableClients.add(etr);
			}
		}
		
		boolean flag = false;
		for(int i = 0, l = bootableClientsCopy.size(); i < l; ++i) {
			BootableClientEntry etr = bootableClientsCopy.get(i);
			EaglercraftUUID uuid = etr.bootAdapter.getLaunchConfigEntry().uuid;
			if(!uuidsSet.contains(uuid)) {
				bootableClients.add(etr);
				uuidsList.add(uuid);
				flag = true;
			}
		}
		
		return flag;
	}

}
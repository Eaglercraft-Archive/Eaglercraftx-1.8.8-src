package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.crypto.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.ArrayBufferInputStream;
import net.lax1dude.eaglercraft.v1_8.internal.vfs.VirtualFilesystem.VFSHandle;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class SYS {

	public static final VirtualFilesystem VFS;
	
	static {
		VFSHandle vh = VirtualFilesystem.openVFS("_net_lax1dude_eaglercraft_v1_8_VirtualFilesystem_");
		
		if(vh.vfs == null) {
			System.err.println("Could not init filesystem!");
		}
		
		VFS = vh.vfs;

		List<String> srp = getResourcePackNames(true);
		for (String name : srp) {
			if (System.currentTimeMillis() - Long.parseLong(name.substring(name.lastIndexOf('_') + 1)) >= 604800000L) {
				deleteResourcePack(name, true);
			}
		}
	}

	public static final void loadRemoteResourcePack(String url, String hash, Consumer<String> cb, Consumer<Runnable> ast, Runnable loading) {
		if (!hash.matches("^[a-f0-9]{40}$")) {
			cb.accept(null);
			return;
		}
		List<String> srpPre = getResourcePackNames(true);
		String alreadyHere = srpPre.stream().filter(s -> s.startsWith(hash + "_")).findFirst().orElse(null);
		if (alreadyHere != null) {
			cb.accept(alreadyHere);
			return;
		}
		PlatformRuntime.downloadRemoteURI(url, ab -> {
			ast.accept(() -> {
				if (ab == null) {
					cb.accept(null);
					return;
				}
				List<String> srp = getResourcePackNames(true);
				// delete old server resource packs - todo: test
				if (srp.size() > 5) {
					srp.sort(Comparator.comparingLong(val -> Long.parseLong(val.substring(val.lastIndexOf('_') + 1))));
					for (int i = 0; i < srp.size() - 5; i++) {
						deleteResourcePack(srp.get(i), true);
					}
				}
				String packName = hash + "_" + System.currentTimeMillis();
				loading.run();
				boolean success = loadResourcePack(packName + ".zip", new ArrayBufferInputStream(ab), hash);
				if (success) {
					cb.accept(packName);
					return;
				}
				cb.accept(null);
			});
		});
	}

	public static final boolean loadResourcePack(String name, InputStream is, String hash) {
		BufferedInputStream bis = new BufferedInputStream(is);

		bis.mark(Integer.MAX_VALUE);

		if (hash != null) {
			try {
				SHA1Digest digest = new SHA1Digest();
				byte[] buffer = new byte[16000];
				int read = 0;

				while ((read = bis.read(buffer)) > 0) {
					digest.update(buffer, 0, read);
				}

				byte[] sha1sum = new byte[20];
				digest.doFinal(sha1sum, 0);
				bis.reset();
				if (!hash.equals((new BigInteger(1, sha1sum)).toString(16))) {
					return false;
				}
			} catch (IOException e) {
				EagRuntime.debugPrintStackTrace(e);
				return false;
			}
		}

		String packName = name.substring(0, name.lastIndexOf('.')).replace('/', '_');
		try {
			int prefixLen = Integer.MAX_VALUE;
			ZipInputStream ziss = new ZipInputStream(bis);
			ZipEntry zipEntryy;
			while ((zipEntryy = ziss.getNextEntry()) != null) {
				String zn;
				if (!zipEntryy.isDirectory() && ((zn = zipEntryy.getName()).equals("pack.mcmeta") || zn.endsWith("/pack.mcmeta"))) {
					int currPrefixLen = zn.length() - 11;
					if (prefixLen > currPrefixLen) {
						prefixLen = currPrefixLen;
					}
				}
			}
			if (prefixLen == Integer.MAX_VALUE) {
				prefixLen = 0;
			}

			bis.reset();

			ZipInputStream zis = new ZipInputStream(bis);
			byte[] bb = new byte[16000];
			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) continue;
				if (zipEntry.getName().length() <= prefixLen) continue;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len;
				while ((len = zis.read(bb)) != -1) {
					baos.write(bb, 0, len);
				}
				baos.close();
				SYS.VFS.getFile((hash == null ? "resourcepacks/" : "srp/") + packName + "/" + zipEntry.getName().substring(prefixLen)).setAllBytes(baos.toByteArray());
			}
			zis.closeEntry();
			zis.close();
			return true;
		} catch (IOException e) {
			EagRuntime.debugPrintStackTrace(e);
			return false;
		}
	}

	public static final List<String> getResourcePackNames() {
		return getResourcePackNames(false);
	}

	private static final List<String> getResourcePackNames(boolean srp) {
		List<String> res = new ArrayList<>();
		List<String> resourcePackFiles = SYS.VFS.listFiles(srp ? "srp/" : "resourcepacks/");
		for (String path : resourcePackFiles) {
			String trimmed = path.substring(srp ? 4 : 14);
			trimmed = trimmed.substring(0, trimmed.indexOf('/'));
			boolean hasIt = false;
			for (String alreadyHas : res) {
				if (trimmed.equals(alreadyHas)) {
					hasIt = true;
					break;
				}
			}
			if (hasIt) continue;
			res.add(trimmed);
		}
		return res;
	}

	public static final void deleteResourcePack(String packName) {
		deleteResourcePack(packName, false);
	}

	private static final void deleteResourcePack(String packName, boolean srp) {
		SYS.VFS.deleteFiles((srp ? "srp/" : "resourcepacks/") + packName);
	}
}

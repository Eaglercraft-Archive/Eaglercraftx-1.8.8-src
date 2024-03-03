package net.lax1dude.eaglercraft.v1_8.sp.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;

/**
 * Copyright (c) 2023-2024 lax1dude. All Rights Reserved.
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
public class EaglerSaveFormat extends SaveFormatOld {

	public static final VFile2 worldsList = new VFile2("worlds_list.txt");
	public static final VFile2 worldsFolder = new VFile2("worlds");

	public EaglerSaveFormat(VFile2 parFile) {
		super(parFile);
	}

	public String getName() {
		return "eagler";
	}

	public ISaveHandler getSaveLoader(String s, boolean flag) {
		return new EaglerSaveHandler(this.savesDirectory, s);
	}

	public List<SaveFormatComparator> getSaveList() {
		ArrayList arraylist = Lists.newArrayList();
		if(worldsList.exists()) {
			String[] lines = worldsList.getAllLines();
			for (int i = 0; i < lines.length; ++i) {
				String s = lines[i];
				WorldInfo worldinfo = this.getWorldInfo(s);
				if (worldinfo != null
						&& (worldinfo.getSaveVersion() == 19132 || worldinfo.getSaveVersion() == 19133)) {
					boolean flag = worldinfo.getSaveVersion() != this.getSaveVersion();
					String s1 = worldinfo.getWorldName();
					if (StringUtils.isEmpty(s1)) {
						s1 = s;
					}

					arraylist.add(new SaveFormatComparator(s, s1, worldinfo.getLastTimePlayed(), 0l,
							worldinfo.getGameType(), flag, worldinfo.isHardcoreModeEnabled(),
							worldinfo.areCommandsAllowed(), null));
				}
			}
		}
		return arraylist;
	}

	public void clearPlayers(String worldFolder) {
		VFile2 file1 = new VFile2(this.savesDirectory, worldFolder, "player");
		deleteFiles(file1.listFiles(true), null);
	}

	protected int getSaveVersion() {
		return 19133; // why notch?
	}

	public boolean duplicateWorld(String worldFolder, String displayName) {
		String newFolderName = displayName.replaceAll("[\\./\"]", "_");
		VFile2 newFolder = new VFile2(savesDirectory, newFolderName);
		while((new VFile2(newFolder, "level.dat")).exists() || (new VFile2(newFolder, "level.dat_old")).exists()) {
			newFolderName += "_";
			newFolder = new VFile2(savesDirectory, newFolderName);
		}
		VFile2 oldFolder = new VFile2(this.savesDirectory, worldFolder);
		String oldPath = oldFolder.getPath();
		int totalSize = 0;
		int lastUpdate = 0;
		final VFile2 finalNewFolder = newFolder;
		List<VFile2> vfl = oldFolder.listFiles(true);
		for(int i = 0, l = vfl.size(); i < l; ++i) {
			VFile2 vf = vfl.get(i);
			String fileNameRelative = vf.getPath().substring(oldPath.length() + 1);
			totalSize += VFile2.copyFile(vf, new VFile2(finalNewFolder, fileNameRelative));
			if (totalSize - lastUpdate > 10000) {
				lastUpdate = totalSize;
				EaglerIntegratedServerWorker.sendProgress("singleplayer.busy.duplicating", totalSize);
			}
		}
		String[] worldsTxt = worldsList.getAllLines();
		if(worldsTxt == null || worldsTxt.length <= 0) {
			worldsTxt = new String[] { newFolderName };
		}else {
			String[] tmp = worldsTxt;
			worldsTxt = new String[worldsTxt.length + 1];
			System.arraycopy(tmp, 0, worldsTxt, 0, tmp.length);
			worldsTxt[worldsTxt.length - 1] = newFolderName;
		}
		worldsList.setAllChars(String.join("\n", worldsTxt));
		return renameWorld(newFolderName, displayName);
	}
}

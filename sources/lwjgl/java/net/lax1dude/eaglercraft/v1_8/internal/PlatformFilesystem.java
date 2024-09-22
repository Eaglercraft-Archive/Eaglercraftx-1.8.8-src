package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.File;

import net.lax1dude.eaglercraft.v1_8.internal.lwjgl.DebugFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.lwjgl.JDBCFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.lwjgl.JDBCFilesystemConverter;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class PlatformFilesystem {

	public static final Logger logger = LogManager.getLogger("PlatformFilesystem");

	@Deprecated
	public static final File debugFilesystemRoot = (new File("filesystem/sp")).getAbsoluteFile();

	public static final File filesystemsRoot = (new File("filesystem")).getAbsoluteFile();

	private static final boolean isLegacyFolder = checkLegacy();

	private static boolean checkLegacy() {
		if(!debugFilesystemRoot.isDirectory()) return false;
		String[] str = debugFilesystemRoot.list();
		return str != null && str.length > 0;
	}

	public static IEaglerFilesystem initializePersist(String dbName) {
		String jdbcUri = System.getProperty("eagler.jdbc." + dbName + ".uri");
		String jdbcDriver = System.getProperty("eagler.jdbc." + dbName + ".driver");
		if(jdbcUri != null && jdbcDriver != null) {
			try {
				IEaglerFilesystem provider = JDBCFilesystem.initialize(dbName, jdbcUri, jdbcDriver);
				if(((JDBCFilesystem)provider).isNewFilesystem() && debugFilesystemRoot.isDirectory() && debugFilesystemRoot.list().length > 0) {
					JDBCFilesystemConverter.convertFilesystem("Converting filesystem, please wait...", debugFilesystemRoot, provider, true);
				}
				return provider;
			}catch(Throwable t) {
				logger.error("Could not open jdbc-based filesystem: {}", dbName);
				logger.error(t);
				return null;
			}
		}else {
			File f;
			if(isLegacyFolder && (dbName.equals("worlds") || dbName.equals("resourcePacks"))) {
				f = debugFilesystemRoot;
				logger.info("Note: filesystem \"{}\" will be stored in the legacy \"sp\" folder because it exists and is not empty", dbName);
			}else {
				f = new File(filesystemsRoot, dbName);
			}
			try {
				return DebugFilesystem.initialize(dbName, f);
			}catch(Throwable t) {
				logger.error("Could not open folder-based filesystem: {}", dbName);
				logger.error(t);
				return null;
			}
		}
	}

}

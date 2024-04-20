package net.lax1dude.eaglercraft.v1_8.internal;

import java.io.File;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
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

	public static final File debugFilesystemRoot = (new File("filesystem/sp")).getAbsoluteFile();

	private static IFilesystemProvider provider = null;

	public static String jdbcUri = null;
	public static String jdbcDriver = null;

	public static void initialize() {
		if(provider == null) {
			if(jdbcUri != null && jdbcDriver != null) {
				provider = JDBCFilesystem.initialize(jdbcUri, jdbcDriver);
				if(((JDBCFilesystem)provider).isNewFilesystem() && debugFilesystemRoot.isDirectory() && debugFilesystemRoot.list().length > 0) {
					JDBCFilesystemConverter.convertFilesystem("Converting filesystem, please wait...", debugFilesystemRoot, provider, true);
				}
			}else {
				provider = DebugFilesystem.initialize(debugFilesystemRoot);
			}
		}
	}

	public static void setUseJDBC(String uri) {
		jdbcUri = uri;
	}

	public static void setJDBCDriverClass(String driver) {
		jdbcDriver = driver;
	}

	public static interface IFilesystemProvider {

		boolean eaglerDelete(String pathName);

		ByteBuffer eaglerRead(String pathName);

		void eaglerWrite(String pathName, ByteBuffer data);

		boolean eaglerExists(String pathName);

		boolean eaglerMove(String pathNameOld, String pathNameNew);

		int eaglerCopy(String pathNameOld, String pathNameNew);

		int eaglerSize(String pathName);

		void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive);

	}

	private static void throwNotInitialized() {
		throw new UnsupportedOperationException("Filesystem has not been initialized!");
	}

	public static boolean eaglerDelete(String pathName) {
		if(provider == null) throwNotInitialized();
		return provider.eaglerDelete(pathName);
	}

	public static ByteBuffer eaglerRead(String pathName) {
		if(provider == null) throwNotInitialized();
		return provider.eaglerRead(pathName);
	}

	public static void eaglerWrite(String pathName, ByteBuffer data) {
		if(provider == null) throwNotInitialized();
		provider.eaglerWrite(pathName, data);
	}

	public static boolean eaglerExists(String pathName) {
		if(provider == null) throwNotInitialized();
		return provider.eaglerExists(pathName);
	}

	public static boolean eaglerMove(String pathNameOld, String pathNameNew) {
		if(provider == null) throwNotInitialized();
		return provider.eaglerMove(pathNameOld, pathNameNew);
	}

	public static int eaglerCopy(String pathNameOld, String pathNameNew) {
		if(provider == null) throwNotInitialized();
		return provider.eaglerCopy(pathNameOld, pathNameNew);
	}

	public static int eaglerSize(String pathName) {
		if(provider == null) throwNotInitialized();
		return provider.eaglerSize(pathName);
	}

	public static void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive) {
		if(provider == null) throwNotInitialized();
		provider.eaglerIterate(pathName, itr, recursive);
	}

	public static void platformShutdown() {
		if(provider != null) {
			if(provider instanceof JDBCFilesystem) {
				((JDBCFilesystem)provider).shutdown();
			}
			provider = null;
		}
	}
}

package net.lax1dude.eaglercraft.v1_8.sp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.ChunkCoordIntPair;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.MinecraftException;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.chunk.Chunk;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

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
public class EaglerChunkLoader extends AnvilChunkLoader {

	private static final String hex = "0123456789ABCDEF";
	private static final Logger logger = LogManager.getLogger("EaglerChunkLoader");

	public static String getChunkPath(int x, int z) {
		int unsignedX = x + 1900000;
		int unsignedZ = z + 1900000;
		
		char[] path = new char[12];
		for(int i = 5; i >= 0; --i) {
			path[i] = hex.charAt((unsignedX >> (i * 4)) & 0xF);
			path[i + 6] = hex.charAt((unsignedZ >> (i * 4)) & 0xF);
		}
		
		return new String(path);
	}

	public static ChunkCoordIntPair getChunkCoords(String filename) {
		String strX = filename.substring(0, 6);
		String strZ = filename.substring(6);

		int retX = 0;
		int retZ = 0;

		for(int i = 0; i < 6; ++i) {
			retX |= hex.indexOf(strX.charAt(i)) << (i << 2);
			retZ |= hex.indexOf(strZ.charAt(i)) << (i << 2);
		}

		return new ChunkCoordIntPair(retX - 1900000, retZ - 1900000);
	}

	public final VFile2 chunkDirectory;

	public EaglerChunkLoader(VFile2 chunkDirectory) {
		this.chunkDirectory = chunkDirectory;
	}

	@Override
	public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
		VFile2 file = new VFile2(chunkDirectory, getChunkPath(var2, var3) + ".dat");
		if(!file.exists()) {
			return null;
		}
		try {
			NBTTagCompound nbt;
			try(InputStream is = file.getInputStream()) {
				nbt = CompressedStreamTools.readCompressed(is);
			}
			return checkedReadChunkFromNBT(var1, var2, var3, nbt);
		}catch(Throwable t) {
			
		}
		return null;
	}

	@Override
	public void saveChunk(World var1, Chunk var2) throws IOException, MinecraftException {
		NBTTagCompound chunkData = new NBTTagCompound();
		this.writeChunkToNBT(var2, var1, chunkData);
		NBTTagCompound fileData = new NBTTagCompound();
		fileData.setTag("Level", chunkData);
		VFile2 file = new VFile2(chunkDirectory, getChunkPath(var2.xPosition, var2.zPosition) + ".dat");
		try(OutputStream os = file.getOutputStream()) {
			CompressedStreamTools.writeCompressed(fileData, os);
		}
	}

	@Override
	public void saveExtraChunkData(World var1, Chunk var2) throws IOException {
		// ?
	}

	@Override
	public void chunkTick() {
		// ?
	}

	@Override
	public void saveExtraData() {
		// ?
	}

}

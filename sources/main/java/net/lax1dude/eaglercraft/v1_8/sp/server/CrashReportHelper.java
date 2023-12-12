package net.lax1dude.eaglercraft.v1_8.sp.server;

import java.util.concurrent.Callable;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.Block;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.IBlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.minecraft.crash.CrashReportCategory;

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
public class CrashReportHelper {

	public static void addIntegratedServerBlockInfo(CrashReportCategory category, final BlockPos pos, final Block blockIn,
			final int blockData) {
		final int i = Block.getIdFromBlock(blockIn);
		category.addCrashSectionCallable("Block type", new Callable<String>() {
			public String call() throws Exception {
				try {
					return HString.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i),
							blockIn.getUnlocalizedName(), blockIn.getClass().getName() });
				} catch (Throwable var2) {
					return "ID #" + i;
				}
			}
		});
		category.addCrashSectionCallable("Block data value", new Callable<String>() {
			public String call() throws Exception {
				if (blockData < 0) {
					return "Unknown? (Got " + blockData + ")";
				} else {
					String s = HString.format("%4s", new Object[] { Integer.toBinaryString(blockData) }).replace(" ",
							"0");
					return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(blockData), s });
				}
			}
		});
		category.addCrashSectionCallable("Block location", new Callable<String>() {
			public String call() throws Exception {
				return CrashReportCategory.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));
			}
		});
	}

	public static void addIntegratedServerBlockInfo(CrashReportCategory category, final BlockPos pos, final IBlockState state) {
		category.addCrashSectionCallable("Block", new Callable<String>() {
			public String call() throws Exception {
				return state.toString();
			}
		});
		category.addCrashSectionCallable("Block location", new Callable<String>() {
			public String call() throws Exception {
				return CrashReportCategory.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));
			}
		});
	}
}

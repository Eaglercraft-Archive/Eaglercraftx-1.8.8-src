package net.optifine.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;

public class TileEntityUtils {
	public static String getTileEntityName(IBlockAccess blockAccess, BlockPos blockPos) {
		TileEntity tileentity = blockAccess.getTileEntity(blockPos);
		return getTileEntityName(tileentity);
	}

	public static String getTileEntityName(TileEntity te) {
		if (!(te instanceof IWorldNameable)) {
			return null;
		} else {
			IWorldNameable iworldnameable = (IWorldNameable) te;
			return !iworldnameable.hasCustomName() ? null : iworldnameable.getName();
		}
	}
}

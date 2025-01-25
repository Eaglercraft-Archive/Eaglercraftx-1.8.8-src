package net.optifine;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.optifine.util.TextureUtils;

public class Config {

	private static final Logger logger = LogManager.getLogger("OptiFine");

	private static Minecraft mc;
	private static GameSettings gameSettings;

	public static void setGameObj(Minecraft mc) {
		Config.mc = mc;
		Config.gameSettings = mc.gameSettings;
	}

	public static String[] tokenize(String p_tokenize_0_, String p_tokenize_1_) {
		StringTokenizer stringtokenizer = new StringTokenizer(p_tokenize_0_, p_tokenize_1_);
		List list = new ArrayList();

		while (stringtokenizer.hasMoreTokens()) {
			String s = stringtokenizer.nextToken();
			list.add(s);
		}

		String[] astring = (String[]) list.toArray(new String[list.size()]);
		return astring;
	}

	public static boolean isCustomSky() {
		return gameSettings.customSkyOF;
	}

	public static boolean isBetterGrass() {
		return gameSettings.betterGrassOF != 0;
	}

	public static boolean isBetterGrassFancy() {
		return gameSettings.betterGrassOF == 2;
	}

	public static boolean isBetterSnow() {
		return gameSettings.betterGrassOF != 0;
	}

	public static boolean isConnectedTextures() {
		return gameSettings.connectedTexturesOF != 0;
	}

	public static boolean isConnectedTexturesFancy() {
		return gameSettings.connectedTexturesOF == 2;
	}

	public static boolean isTreesFancy() {
		return gameSettings.fancyGraphics;
	}

	public static boolean isTreesSmart() {
		return gameSettings.fancyGraphics && gameSettings.smartLeavesOF;
	}

	public static boolean isCullFacesLeaves() {
		return !gameSettings.fancyGraphics;
	}

	public static boolean isCustomItems() {
		return gameSettings.customItemsOF;
	}

	public static void dbg(String string) {
		logger.debug(string);
	}

	public static void warn(String string) {
		logger.warn(string);
	}

	public static void error(String string) {
		logger.error(string);
	}

	public static int limit(int p_limit_0_, int p_limit_1_, int p_limit_2_) {
		return p_limit_0_ < p_limit_1_ ? p_limit_1_ : (p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_);
	}

	public static float limit(float p_limit_0_, float p_limit_1_, float p_limit_2_) {
		return p_limit_0_ < p_limit_1_ ? p_limit_1_ : (p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_);
	}

	public static double limit(double p_limit_0_, double p_limit_2_, double p_limit_4_) {
		return p_limit_0_ < p_limit_2_ ? p_limit_2_ : (p_limit_0_ > p_limit_4_ ? p_limit_4_ : p_limit_0_);
	}

	public static int parseInt(String p_parseInt_0_, int p_parseInt_1_) {
		try {
			if (p_parseInt_0_ == null) {
				return p_parseInt_1_;
			} else {
				p_parseInt_0_ = p_parseInt_0_.trim();
				return Integer.parseInt(p_parseInt_0_);
			}
		} catch (NumberFormatException var3) {
			return p_parseInt_1_;
		}
	}

	public static float parseFloat(String p_parseFloat_0_, float p_parseFloat_1_) {
		try {
			if (p_parseFloat_0_ == null) {
				return p_parseFloat_1_;
			} else {
				p_parseFloat_0_ = p_parseFloat_0_.trim();
				return Float.parseFloat(p_parseFloat_0_);
			}
		} catch (NumberFormatException var3) {
			return p_parseFloat_1_;
		}
	}

	public static boolean parseBoolean(String p_parseBoolean_0_, boolean p_parseBoolean_1_) {
		try {
			if (p_parseBoolean_0_ == null) {
				return p_parseBoolean_1_;
			} else {
				p_parseBoolean_0_ = p_parseBoolean_0_.trim();
				return Boolean.parseBoolean(p_parseBoolean_0_);
			}
		} catch (NumberFormatException var3) {
			return p_parseBoolean_1_;
		}
	}

	public static int[] addIntToArray(int[] p_addIntToArray_0_, int p_addIntToArray_1_) {
		if (p_addIntToArray_0_ != null) {
			int[] ret = new int[p_addIntToArray_0_.length + 1];
			System.arraycopy(p_addIntToArray_0_, 0, ret, 0, p_addIntToArray_0_.length);
			ret[p_addIntToArray_0_.length] = p_addIntToArray_1_;
			return ret;
		} else {
			throw new NullPointerException("The given array is NULL");
		}
	}

	public static int[] addIntsToArray(int[] p_addIntsToArray_0_, int[] p_addIntsToArray_1_) {
		if (p_addIntsToArray_0_ != null && p_addIntsToArray_1_ != null) {
			int i = p_addIntsToArray_0_.length;
			int j = i + p_addIntsToArray_1_.length;
			int[] aint = new int[j];
			System.arraycopy(p_addIntsToArray_0_, 0, aint, 0, i);

			for (int k = 0; k < p_addIntsToArray_1_.length; ++k) {
				aint[k + i] = p_addIntsToArray_1_[k];
			}

			return aint;
		} else {
			throw new NullPointerException("The given array is NULL");
		}
	}

	public static String arrayToString(Object[] p_arrayToString_0_) {
		return arrayToString(p_arrayToString_0_, ", ");
	}

	public static String arrayToString(Object[] p_arrayToString_0_, String p_arrayToString_1_) {
		if (p_arrayToString_0_ == null) {
			return "";
		} else {
			StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);

			for (int i = 0; i < p_arrayToString_0_.length; ++i) {
				Object object = p_arrayToString_0_[i];

				if (i > 0) {
					stringbuffer.append(p_arrayToString_1_);
				}

				stringbuffer.append(String.valueOf(object));
			}

			return stringbuffer.toString();
		}
	}

	public static String arrayToString(int[] p_arrayToString_0_) {
		return arrayToString(p_arrayToString_0_, ", ");
	}

	public static String arrayToString(int[] p_arrayToString_0_, String p_arrayToString_1_) {
		if (p_arrayToString_0_ == null) {
			return "";
		} else {
			StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);

			for (int i = 0; i < p_arrayToString_0_.length; ++i) {
				int j = p_arrayToString_0_[i];

				if (i > 0) {
					stringbuffer.append(p_arrayToString_1_);
				}

				stringbuffer.append(String.valueOf(j));
			}

			return stringbuffer.toString();
		}
	}

	public static int[] toPrimitive(Integer[] p_toPrimitive_0_) {
		if (p_toPrimitive_0_ == null) {
			return null;
		} else if (p_toPrimitive_0_.length == 0) {
			return new int[0];
		} else {
			int[] aint = new int[p_toPrimitive_0_.length];

			for (int i = 0; i < aint.length; ++i) {
				aint[i] = p_toPrimitive_0_[i].intValue();
			}

			return aint;
		}
	}

	public static boolean isSameOne(Object p_isSameOne_0_, Object[] p_isSameOne_1_) {
		if (p_isSameOne_1_ == null) {
			return false;
		} else {
			for (int i = 0; i < p_isSameOne_1_.length; ++i) {
				Object object = p_isSameOne_1_[i];

				if (p_isSameOne_0_ == object) {
					return true;
				}
			}

			return false;
		}
	}

	public static boolean equals(Object p_equals_0_, Object p_equals_1_) {
		return p_equals_0_ == p_equals_1_ ? true : (p_equals_0_ == null ? false : p_equals_0_.equals(p_equals_1_));
	}

	public static boolean isFromDefaultResourcePack(ResourceLocation p_isFromDefaultResourcePack_0_) {
		IResourcePack iresourcepack = getDefiningResourcePack(p_isFromDefaultResourcePack_0_);
		return iresourcepack == mc.getDefaultResourcePack();
	}

	public static IResourcePack getDefiningResourcePack(ResourceLocation p_getDefiningResourcePack_0_) {
		ResourcePackRepository resourcepackrepository = mc.getResourcePackRepository();
		IResourcePack iresourcepack = resourcepackrepository.getResourcePackInstance();

		if (iresourcepack != null && iresourcepack.resourceExists(p_getDefiningResourcePack_0_)) {
			return iresourcepack;
		} else {
			List<ResourcePackRepository.Entry> list = resourcepackrepository.getRepositoryEntries();

			for (int i = list.size() - 1; i >= 0; --i) {
				ResourcePackRepository.Entry resourcepackrepository$entry = (ResourcePackRepository.Entry) list.get(i);
				IResourcePack iresourcepack1 = resourcepackrepository$entry.getResourcePack();

				if (iresourcepack1.resourceExists(p_getDefiningResourcePack_0_)) {
					return iresourcepack1;
				}
			}

			DefaultResourcePack res = mc.getDefaultResourcePack();
			if (res.resourceExists(p_getDefiningResourcePack_0_)) {
				return res;
			} else {
				return null;
			}
		}
	}

	public static boolean hasResource(ResourceLocation p_hasResource_0_) {
		if (p_hasResource_0_ == null) {
			return false;
		} else {
			IResourcePack iresourcepack = getDefiningResourcePack(p_hasResource_0_);
			return iresourcepack != null;
		}
	}

	public static IResourcePack[] getResourcePacks() {
		ResourcePackRepository resourcepackrepository = mc.getResourcePackRepository();
		List list = resourcepackrepository.getRepositoryEntries();
		List list1 = new ArrayList();

		for (Object resourcepackrepository$entry0 : list) {
			ResourcePackRepository.Entry resourcepackrepository$entry = (ResourcePackRepository.Entry) resourcepackrepository$entry0;
			list1.add(resourcepackrepository$entry.getResourcePack());
		}

		if (resourcepackrepository.getResourcePackInstance() != null) {
			list1.add(resourcepackrepository.getResourcePackInstance());
		}

		IResourcePack[] airesourcepack = (IResourcePack[]) ((IResourcePack[]) list1
				.toArray(new IResourcePack[list1.size()]));
		return airesourcepack;
	}

	public static int intHash(int p_intHash_0_) {
		p_intHash_0_ = p_intHash_0_ ^ 61 ^ p_intHash_0_ >> 16;
		p_intHash_0_ = p_intHash_0_ + (p_intHash_0_ << 3);
		p_intHash_0_ = p_intHash_0_ ^ p_intHash_0_ >> 4;
		p_intHash_0_ = p_intHash_0_ * 668265261;
		p_intHash_0_ = p_intHash_0_ ^ p_intHash_0_ >> 15;
		return p_intHash_0_;
	}

	public static int getRandom(BlockPos p_getRandom_0_, int p_getRandom_1_) {
		int i = intHash(p_getRandom_1_ + 37);
		i = intHash(i + p_getRandom_0_.getX());
		i = intHash(i + p_getRandom_0_.getZ());
		i = intHash(i + p_getRandom_0_.getY());
		return i;
	}

	public static void frameInitHook() {
		TextureUtils.registerResourceListener();
	}

}

package net.lax1dude.eaglercraft.v1_8.sp.server;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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
public class GenLayerEaglerRivers extends GenLayer {

	private static final int[] pattern = new int[] {
			0b111000011100001110000111,
			0b111000111110011111000111,
			0b011100011100001110001110,
			0b011100000000000000001110,
			0b001110000000000000011100,
			0b001110000000000000011100,
			0b000111000000000000111000,
			0b000111000000000000111000,
			0b000011100000000001110000,
			0b000011100000000001110000,
			0b000001110000000011100000,
			0b000001110000000011100000,
			0b000000111000000111000000,
			0b000000111000000111000000,
			0b000000011100001110000000,
			0b000000011100001110000000,
			0b000000001110011100000000,
			0b000000001110011100000000,
			0b000000000111111000000000,
			0b000000000111111000000000,
			0b000000000011110000000000,
			0b000000000011110000000000,
			0b000000000001100000000000,
			0b000000000001100000000000,
	};

	private static final int patternSize = 24;

	public GenLayerEaglerRivers(long parLong1, GenLayer p) {
		super(parLong1);
		this.parent = p;
	}

	@Override
	public int[] getInts(int x, int y, int w, int h) {
		int[] aint = this.parent.getInts(x, y, w, h);
		int[] aint1 = IntCache.getIntCache(w * h);

		long a = worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		long b = ((a & 112104l) == 0) ? (((a & 534l) == 0) ? 1l : 15l) : 746l;
		for (int yy = 0; yy < h; ++yy) {
			for (int xx = 0; xx < w; ++xx) {
				int i = xx + yy * w;
				aint1[i] = aint[i];
				long xxx = (long)(x + xx) & 0xFFFFFFFFl;
				long yyy = (long)(y + yy) & 0xFFFFFFFFl;
				long hash = a + (xxx / patternSize);
				hash *= hash * 6364136223846793005L + 1442695040888963407L;
				hash += (yyy / patternSize);
				hash *= hash * 6364136223846793005L + 1442695040888963407L;
				hash += a;
				if ((hash & b) == 0l) {
					xxx %= (long)patternSize;
					yyy %= (long)patternSize;
					long tmp;
					switch((int)((hash >>> 16l) & 3l)) {
					case 1:
						tmp = xxx;
						xxx = yyy;
						yyy = (long)patternSize - tmp - 1l;
						break;
					case 2:
						tmp = xxx;
						xxx = (long)patternSize - yyy - 1l;
						yyy = tmp;
						break;
					case 3:
						tmp = xxx;
						xxx = (long)patternSize - yyy - 1l;
						yyy = (long)patternSize - tmp - 1l;
						break;
					}
					if((pattern[(int)yyy] & (1 << (int)xxx)) != 0) {
						aint1[i] = BiomeGenBase.river.biomeID;
					}
				}
			}
		}

		return aint1;
	}

}

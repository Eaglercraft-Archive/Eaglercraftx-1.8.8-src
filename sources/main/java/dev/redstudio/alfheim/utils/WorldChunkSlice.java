package dev.redstudio.alfheim.utils;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Represents a slice of a world containing a collection of chunks.
 *
 * @author Luna Lage (Desoroxxx)
 * @author Angeline (@jellysquid)
 * @since 1.0
 */
public class WorldChunkSlice {

	private static final int DIAMETER = 5;
	private static final int RADIUS = DIAMETER / 2;

	private final int x, z;

	private final Chunk[] chunks;

	/**
	 * Initializes a {@link WorldChunkSlice} object using a given chunk provider and
	 * coordinates.
	 *
	 * @param chunkProvider The chunk provider to get chunks from
	 * @param x             The X-coordinate of the center chunk
	 * @param z             The Z-coordinate of the center chunk
	 */
	public WorldChunkSlice(final IChunkProvider chunkProvider, final int x, final int z) {
		chunks = new Chunk[DIAMETER * DIAMETER];

		for (int xDiff = -RADIUS; xDiff <= RADIUS; xDiff++)
			for (int zDiff = -RADIUS; zDiff <= RADIUS; zDiff++)
				chunks[((xDiff + RADIUS) * DIAMETER) + (zDiff + RADIUS)] = chunkProvider.getLoadedChunk(x + xDiff,
						z + zDiff);

		this.x = x - RADIUS;
		this.z = z - RADIUS;
	}

	/**
	 * Checks if all chunks within a radius around a coordinate are loaded.
	 *
	 * @param x      The X-coordinate to check around
	 * @param z      The Z-coordinate to check around
	 * @param radius The radius around the coordinates to check
	 *
	 * @return true if all chunks are loaded, false otherwise
	 */
	public boolean isLoaded(final int x, final int z, final int radius) {
		final int xStart = ((x - radius) >> 4) - this.x;
		final int zStart = ((z - radius) >> 4) - this.z;
		final int xEnd = ((x + radius) >> 4) - this.x;
		final int zEnd = ((z + radius) >> 4) - this.z;

		for (int currentX = xStart; currentX <= xEnd; ++currentX)
			for (int currentZ = zStart; currentZ <= zEnd; ++currentZ)
				if (getChunk(currentX, currentZ) == null)
					return false;

		return true;
	}

	/**
	 * Retrieves the chunk that includes the provided world coordinates.
	 *
	 * @param x The X-coordinate in the world
	 * @param z The Z-coordinate in the world
	 *
	 * @return The Chunk object that includes these coordinates
	 */
	public Chunk getChunkFromWorldCoords(final int x, final int z) {
		return getChunk((x >> 4) - this.x, (z >> 4) - this.z);
	}

	/**
	 * Retrieves the chunk located at the given coordinates within this chunk slice.
	 *
	 * @param x The X-coordinate within the slice
	 * @param z The Z-coordinate within the slice
	 *
	 * @return The Chunk object at these coordinates
	 */
	private Chunk getChunk(final int x, final int z) {
		return chunks[(x * DIAMETER) + z];
	}
}

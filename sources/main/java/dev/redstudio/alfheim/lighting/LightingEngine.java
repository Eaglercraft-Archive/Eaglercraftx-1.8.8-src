package dev.redstudio.alfheim.lighting;

import dev.redstudio.alfheim.utils.DeduplicatedLongQueue;
import dev.redstudio.redcore.math.ClampUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

/**
 * Modified by lax1dude not to abuse interfaces
 * 
 * @author Luna Lage (Desoroxxx)
 * @author kappa-maintainer
 * @author embeddedt
 * @author Angeline (@jellysquid)
 * @since 1.0
 */
public final class LightingEngine {

	private static final byte MAX_LIGHT_LEVEL = 15;

	private final World world;

	// Layout of longs: [padding(4)] [y(8)] [x(26)] [z(26)]
	private final DeduplicatedLongQueue[] lightUpdateQueues = new DeduplicatedLongQueue[EnumSkyBlock.values().length];

	// Layout of longs: see above
	private final DeduplicatedLongQueue[] darkeningQueues = new DeduplicatedLongQueue[MAX_LIGHT_LEVEL + 1];
	private final DeduplicatedLongQueue[] brighteningQueues = new DeduplicatedLongQueue[MAX_LIGHT_LEVEL + 1];

	// Layout of longs: [newLight(4)] [pos(60)]
	private final DeduplicatedLongQueue initialBrightenings;
	// Layout of longs: [padding(4)] [pos(60)]
	private final DeduplicatedLongQueue initialDarkenings;

	private boolean updating = false;

	// Layout parameters
	// Length of bit segments
	private static final int L_X = 26, L_Y = 8, L_Z = 26, L_L = 4;

	// Bit segment shifts/positions
	private static final int S_Z = 0, S_X = S_Z + L_Z, S_Y = S_X + L_X, S_L = S_Y + L_Y;

	// Bit segment masks
	private static final long M_X = (1L << L_X) - 1, M_Y = (1L << L_Y) - 1, M_Z = (1L << L_Z) - 1,
			M_L = (1L << L_L) - 1, M_POS = (M_Y << S_Y) | (M_X << S_X) | (M_Z << S_Z);

	// Bit to check whether y had overflow
	private static final long Y_CHECK = 1L << (S_Y + L_Y);

	private static final long[] neighborShifts = new long[6];

	static {
		for (byte i = 0; i < 6; ++i) {
			final Vec3i offset = EnumFacing._VALUES[i].getDirectionVec();
			neighborShifts[i] = ((long) offset.getY() << S_Y) | ((long) offset.getX() << S_X)
					| ((long) offset.getZ() << S_Z);
		}
	}

	// Mask to extract chunk identifier
	private static final long M_CHUNK = ((M_X >> 4) << (4 + S_X)) | ((M_Z >> 4) << (4 + S_Z));

	// Iteration state data
	// Cache position to avoid allocation of new object each time
	private final BlockPos currentPos = new BlockPos();
	private Chunk currentChunk;
	private long currentChunkIdentifier;
	private long currentData;

	// Cached data about neighboring blocks (of tempPos)
	private boolean isNeighborDataValid = false;

	private final NeighborInfo[] neighborInfos = new NeighborInfo[6];
	private DeduplicatedLongQueue currentQueue;

	public LightingEngine(final World world) {
		this.world = world;

		initialBrightenings = new DeduplicatedLongQueue(16384);
		initialDarkenings = new DeduplicatedLongQueue(16384);

		for (int i = 0; i < EnumSkyBlock.values().length; ++i)
			lightUpdateQueues[i] = new DeduplicatedLongQueue(16384);

		for (int i = 0; i < darkeningQueues.length; ++i)
			darkeningQueues[i] = new DeduplicatedLongQueue(16384);

		for (int i = 0; i < brighteningQueues.length; ++i)
			brighteningQueues[i] = new DeduplicatedLongQueue(16384);

		for (int i = 0; i < neighborInfos.length; ++i)
			neighborInfos[i] = new NeighborInfo();
	}

	/**
	 * Schedules a light update for the specified light type and position to be
	 * processed later by
	 * {@link LightingEngine#processLightUpdatesForType(EnumSkyBlock)}
	 */
	public void scheduleLightUpdate(final EnumSkyBlock lightType, final BlockPos pos) {
		scheduleLightUpdate(lightType, encodeWorldCoord(pos));
	}

	/**
	 * Schedules a light update for the specified light type and position to be
	 * processed later by {@link LightingEngine#processLightUpdates()}
	 */
	private void scheduleLightUpdate(final EnumSkyBlock lightType, final long blockPos) {
		lightUpdateQueues[lightType.ordinal()].enqueue(blockPos);
	}

	/**
	 * Calls {@link LightingEngine#processLightUpdatesForType(EnumSkyBlock)} for
	 * both light types
	 */
	public void processLightUpdates() {
		processLightUpdatesForType(EnumSkyBlock.SKY);
		processLightUpdatesForType(EnumSkyBlock.BLOCK);
	}

	/**
	 * Processes light updates of the given light type
	 */
	public void processLightUpdatesForType(final EnumSkyBlock lightType) {
		final DeduplicatedLongQueue queue = lightUpdateQueues[lightType.ordinal()];

		// Quickly check if the queue is empty before we acquire a more expensive lock.
		if (queue.isEmpty())
			return;

		processLightUpdatesForTypeInner(lightType, queue);
	}

	private void processLightUpdatesForTypeInner(final EnumSkyBlock lightType, final DeduplicatedLongQueue queue) {
		// Avoid nested calls
		if (updating)
			throw new IllegalStateException("Already processing updates!");

		updating = true;

		currentChunkIdentifier = -1; // Reset chunk cache

		currentQueue = queue;

		if (currentQueue != null)
			currentQueue.newDeduplicationSet();

		// Process the queued updates and enqueue them for further processing
		while (nextItem()) {
			if (currentChunk == null)
				continue;

			final byte oldLight = getCursorCachedLight(lightType);
			final byte newLight = calculateNewLightFromCursor(lightType);

			if (oldLight < newLight)
				initialBrightenings.enqueue(((long) newLight << S_L) | currentData); // Don't enqueue directly for
																						// brightening to avoid
																						// duplicate scheduling
			else if (oldLight > newLight)
				initialDarkenings.enqueue(currentData); // Don't enqueue directly for darkening to avoid duplicate
														// scheduling
		}

		currentQueue = initialBrightenings;

		if (currentQueue != null)
			currentQueue.newDeduplicationSet();

		while (nextItem()) {
			final byte newLight = (byte) (currentData >> S_L & M_L);

			if (newLight > getCursorCachedLight(lightType))
				enqueueBrightening(currentPos, currentData & M_POS, newLight, currentChunk, lightType); // Sets the
																										// light to
																										// newLight to
																										// only schedule
																										// once. Clear
																										// leading bits
																										// of curData
																										// for later
		}

		currentQueue = initialDarkenings;

		if (currentQueue != null)
			currentQueue.newDeduplicationSet();

		while (nextItem()) {
			final byte oldLight = getCursorCachedLight(lightType);

			if (oldLight != 0)
				enqueueDarkening(currentPos, currentData, oldLight, currentChunk, lightType); // Sets the light to zero
																								// to only schedule once
		}

		// Iterate through enqueued updates (brightening and darkening in parallel) from
		// brightest to darkest so that we only need to iterate once
		for (byte currentLight = MAX_LIGHT_LEVEL; currentLight >= 0; --currentLight) {
			currentQueue = darkeningQueues[currentLight];

			if (currentQueue != null)
				currentQueue.newDeduplicationSet();

			while (nextItem()) {
				// Don't darken if we got brighter due to some other change
				if (getCursorCachedLight(lightType) >= currentLight)
					continue;

				final IBlockState blockState = currentChunk.getBlockState(currentPos);
				final byte luminosity = getCursorLuminosity(blockState, lightType);
				final byte opacity; // If luminosity is high enough, opacity is irrelevant

				if (luminosity >= MAX_LIGHT_LEVEL - 1)
					opacity = 1;
				else
					opacity = getPosOpacity(currentPos, blockState);

				// Only darken neighbors if we indeed became darker
				if (calculateNewLightFromCursor(luminosity, opacity, lightType) < currentLight) {
					// Need to calculate new light value from neighbors IGNORING neighbors which are
					// scheduled for darkening
					byte newLight = luminosity;

					fetchNeighborDataFromCursor(lightType);

					for (final NeighborInfo neighborInfo : neighborInfos) {
						final Chunk neighborChunk = neighborInfo.chunk;

						if (neighborChunk == null)
							continue;

						final byte neighborLight = neighborInfo.light;

						if (neighborLight == 0)
							continue;

						final BlockPos neighborPos = neighborInfo.mutableBlockPos;

						if (currentLight - getPosOpacity(neighborPos, neighborChunk
								.getBlockState(neighborPos)) >= neighborLight) /*
																				 * Schedule neighbor for darkening if we
																				 * possibly light it
																				 */ {
							enqueueDarkening(neighborPos, neighborInfo.key, neighborLight, neighborChunk, lightType);
						} else /* Only use for new light calculation if not */ {
							// If we can't darken the neighbor, no one else can (because of processing
							// order) -> safe to let us be illuminated by it
							newLight = (byte) Math.max(newLight, neighborLight - opacity);
						}
					}

					// Schedule brightening since light level was set to 0
					enqueueBrighteningFromCursor(newLight, lightType);
				} else /*
						 * We didn't become darker, so we need to re-set our initial light value (was
						 * set to zero) and notify neighbors
						 */ {
					enqueueBrighteningFromCursor(currentLight, lightType); // Do not spread to neighbors immediately to
																			// avoid scheduling multiple times
				}
			}

			currentQueue = brighteningQueues[currentLight];

			if (currentQueue != null)
				currentQueue.newDeduplicationSet();

			while (nextItem()) {
				final byte oldLight = getCursorCachedLight(lightType);

				// Only process this if nothing else has happened at this position since
				// scheduling
				if (oldLight == currentLight) {
					world.notifyLightSet(currentPos);

					if (currentLight > 1)
						spreadLightFromCursor(currentLight, lightType);
				}
			}
		}

		updating = false;
	}

	/**
	 * Gets data for neighbors of {@link #currentPos} and saves the results into
	 * neighbor state data members. If a neighbor can't be accessed/doesn't exist,
	 * the corresponding entry in neighborChunks is null - others are not reset
	 */
	private void fetchNeighborDataFromCursor(final EnumSkyBlock lightType) {
		// Only update if curPos was changed
		if (isNeighborDataValid)
			return;

		isNeighborDataValid = true;

		for (int i = 0; i < neighborInfos.length; ++i) {
			final NeighborInfo neighborInfo = neighborInfos[i];
			final long neighborLongPos = neighborInfo.key = currentData + neighborShifts[i];

			if ((neighborLongPos & Y_CHECK) != 0) {
				neighborInfo.chunk = null;
				continue;
			}

			final BlockPos neighborPos = decodeWorldCoord(neighborInfo.mutableBlockPos, neighborLongPos);

			final Chunk neighborChunk;

			if ((neighborLongPos & M_CHUNK) == currentChunkIdentifier)
				neighborChunk = neighborInfo.chunk = currentChunk;
			else
				neighborChunk = neighborInfo.chunk = getChunk(neighborPos);

			if (neighborChunk != null) {
				final ExtendedBlockStorage neighborSection = neighborChunk
						.getBlockStorageArray()[neighborPos.getY() >> 4];

				neighborInfo.light = getCachedLightFor(neighborChunk, neighborSection, neighborPos, lightType);
			}
		}
	}

	private static byte getCachedLightFor(final Chunk chunk, final ExtendedBlockStorage storage,
			final BlockPos blockPos, final EnumSkyBlock type) {
		final int x = blockPos.getX() & 15;
		final int y = blockPos.getY();
		final int z = blockPos.getZ() & 15;

		if (storage == null)
			return type == EnumSkyBlock.SKY && chunk.canSeeSky(blockPos) ? (byte) type.defaultLightValue : 0;
		else if (type == EnumSkyBlock.SKY)
			return chunk.getWorld().provider.getHasNoSky() ? 0 : (byte) storage.getExtSkylightValue(x, y & 15, z);
		else
			return type == EnumSkyBlock.BLOCK ? (byte) storage.getExtBlocklightValue(x, y & 15, z)
					: (byte) type.defaultLightValue;
	}

	private byte calculateNewLightFromCursor(final EnumSkyBlock lightType) {
		final IBlockState blockState = currentChunk.getBlockState(currentPos);

		final byte luminosity = getCursorLuminosity(blockState, lightType);
		final byte opacity;

		if (luminosity >= MAX_LIGHT_LEVEL - 1)
			opacity = 1;
		else
			opacity = getPosOpacity(currentPos, blockState);

		return calculateNewLightFromCursor(luminosity, opacity, lightType);
	}

	private byte calculateNewLightFromCursor(final byte luminosity, final byte opacity, final EnumSkyBlock lightType) {
		if (luminosity >= MAX_LIGHT_LEVEL - opacity)
			return luminosity;

		byte newLight = luminosity;

		fetchNeighborDataFromCursor(lightType);

		for (final NeighborInfo neighborInfo : neighborInfos) {
			if (neighborInfo.chunk == null)
				continue;

			newLight = (byte) Math.max(neighborInfo.light - opacity, newLight);
		}

		return newLight;
	}

	private void spreadLightFromCursor(final byte currentLight, final EnumSkyBlock lightType) {
		fetchNeighborDataFromCursor(lightType);

		for (final NeighborInfo neighborInfo : neighborInfos) {
			final Chunk neighborChunk = neighborInfo.chunk;

			if (neighborChunk == null || currentLight < neighborInfo.light)
				continue;

			final BlockPos neighborBlockPos = neighborInfo.mutableBlockPos;

			final byte newLight = (byte) (currentLight
					- getPosOpacity(neighborBlockPos, neighborChunk.getBlockState(neighborBlockPos)));

			if (newLight > neighborInfo.light)
				enqueueBrightening(neighborBlockPos, neighborInfo.key, newLight, neighborChunk, lightType);
		}
	}

	private void enqueueBrighteningFromCursor(final byte newLight, final EnumSkyBlock lightType) {
		enqueueBrightening(currentPos, currentData, newLight, currentChunk, lightType);
	}

	/**
	 * Enqueues the blockPos for brightening and sets its light value to newLight
	 */
	private void enqueueBrightening(final BlockPos blockPos, final long longPos, final byte newLight, final Chunk chunk,
			final EnumSkyBlock lightType) {
		brighteningQueues[newLight].enqueue(longPos);

		chunk.setLightFor(lightType, blockPos, newLight);
	}

	/**
	 * Enqueues the blockPos for darkening and sets its light value to 0
	 */
	private void enqueueDarkening(final BlockPos blockPos, final long longPos, final byte oldLight, final Chunk chunk,
			final EnumSkyBlock lightType) {
		darkeningQueues[oldLight].enqueue(longPos);

		chunk.setLightFor(lightType, blockPos, 0);
	}

	private static BlockPos decodeWorldCoord(final BlockPos mutableBlockPos, final long longPos) {
		return mutableBlockPos.func_181079_c((int) (longPos >> S_X & M_X) - (1 << L_X - 1), (int) (longPos >> S_Y & M_Y),
				(int) (longPos >> S_Z & M_Z) - (1 << L_Z - 1));
	}

	private static long encodeWorldCoord(final BlockPos pos) {
		return ((long) pos.getY() << S_Y) | ((long) pos.getX() + (1 << L_X - 1) << S_X)
				| ((long) pos.getZ() + (1 << L_Z - 1) << S_Z);
	}

	/**
	 * Polls a new item from {@link #currentQueue} and fills in state data members
	 *
	 * @return If there was an item to poll
	 */
	private boolean nextItem() {
		if (currentQueue.isEmpty()) {
			currentQueue = null;

			return false;
		}

		currentData = currentQueue.dequeue();
		isNeighborDataValid = false;

		decodeWorldCoord(currentPos, currentData);

		final long chunkIdentifier = currentData & M_CHUNK;

		if (currentChunkIdentifier != chunkIdentifier) {
			currentChunk = getChunk(currentPos);
			currentChunkIdentifier = chunkIdentifier;
		}

		return true;
	}

	private byte getCursorCachedLight(final EnumSkyBlock lightType) {
		return currentChunk.alfheim$getCachedLightFor(lightType, currentPos);
	}

	/**
	 * Calculates the luminosity for {@link #currentPos}, taking into account the
	 * light type
	 */
	private byte getCursorLuminosity(final IBlockState state, final EnumSkyBlock lightType) {
		if (lightType == EnumSkyBlock.SKY) {
			if (currentChunk.canSeeSky(currentPos))
				return (byte) EnumSkyBlock.SKY.defaultLightValue;
			else
				return 0;
		}

		return (byte) ClampUtil.clampMinFirst(state.getBlock().getLightValue(), 0, MAX_LIGHT_LEVEL);
	}

	private byte getPosOpacity(final BlockPos blockPos, final IBlockState blockState) {
		return (byte) ClampUtil.clampMinFirst(blockState.getBlock().getLightOpacity(), 1, MAX_LIGHT_LEVEL);
	}

	private Chunk getChunk(final BlockPos blockPos) {
		return world.getChunkProvider().getLoadedChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
	}

	private static final class NeighborInfo {

		public final BlockPos mutableBlockPos = new BlockPos();

		public Chunk chunk;

		public byte light;

		public long key;
	}
}

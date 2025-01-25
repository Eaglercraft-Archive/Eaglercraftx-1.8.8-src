package dev.redstudio.redcore.math;

public class ClampUtil {
	/**
	 * Clamps a value within a specified range [min, max], checking for the minimum
	 * value first.
	 * <p>
	 * If the input is less than min, it returns min. If the input is greater than
	 * max, it returns max. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 *
	 * @return The clamped value
	 */
	public static byte clampMinFirst(final byte input, final byte min, final byte max) {
		return input < min ? min : input > max ? max : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the minimum
	 * value first.
	 * <p>
	 * If the input is less than min, it returns min. If the input is greater than
	 * max, it returns max. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 *
	 * @return The clamped value
	 */
	public static short clampMinFirst(final short input, final short min, final short max) {
		return input < min ? min : input > max ? max : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the minimum
	 * value first.
	 * <p>
	 * If the input is less than min, it returns min. If the input is greater than
	 * max, it returns max. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 *
	 * @return The clamped value
	 */
	public static int clampMinFirst(final int input, final int min, final int max) {
		return input < min ? min : input > max ? max : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the minimum
	 * value first.
	 * <p>
	 * If the input is less than min, it returns min. If the input is greater than
	 * max, it returns max. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 *
	 * @return The clamped value
	 */
	public static long clampMinFirst(final long input, final long min, final long max) {
		return input < min ? min : input > max ? max : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the minimum
	 * value first.
	 * <p>
	 * If the input is less than min, it returns min. If the input is greater than
	 * max, it returns max. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 *
	 * @return The clamped value
	 */
	public static float clampMinFirst(final float input, final float min, final float max) {
		return input < min ? min : input > max ? max : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the minimum
	 * value first.
	 * <p>
	 * If the input is less than min, it returns min. If the input is greater than
	 * max, it returns max. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 *
	 * @return The clamped value
	 */
	public static double clampMinFirst(final double input, final double min, final double max) {
		return input < min ? min : input > max ? max : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the maximum
	 * value first.
	 * <p>
	 * If the input is greater than max, it returns max. If the input is less than
	 * min, it returns min. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 * @return The clamped value
	 */
	public static byte clampMaxFirst(final byte input, final byte min, final byte max) {
		return input > max ? max : input < min ? min : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the maximum
	 * value first.
	 * <p>
	 * If the input is greater than max, it returns max. If the input is less than
	 * min, it returns min. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 * @return The clamped value
	 */
	public static short clampMaxFirst(final short input, final short min, final short max) {
		return input > max ? max : input < min ? min : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the maximum
	 * value first.
	 * <p>
	 * If the input is greater than max, it returns max. If the input is less than
	 * min, it returns min. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 * @return The clamped value
	 */
	public static int clampMaxFirst(final int input, final int min, final int max) {
		return input > max ? max : input < min ? min : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the maximum
	 * value first.
	 * <p>
	 * If the input is greater than max, it returns max. If the input is less than
	 * min, it returns min. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 * @return The clamped value
	 */
	public static long clampMaxFirst(final long input, final long min, final long max) {
		return input > max ? max : input < min ? min : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the maximum
	 * value first.
	 * <p>
	 * If the input is greater than max, it returns max. If the input is less than
	 * min, it returns min. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 * @return The clamped value
	 */
	public static float clampMaxFirst(final float input, final float min, final float max) {
		return input > max ? max : input < min ? min : input;
	}

	/**
	 * Clamps a value within a specified range [min, max], checking for the maximum
	 * value first.
	 * <p>
	 * If the input is greater than max, it returns max. If the input is less than
	 * min, it returns min. Otherwise, it returns the input.
	 *
	 * @param input The input value to clamp
	 * @param min   The minimum value to clamp to
	 * @param max   The maximum value to clamp to
	 * @return The clamped value
	 */
	public static double clampMaxFirst(final double input, final double min, final double max) {
		return input > max ? max : input < min ? min : input;
	}
}

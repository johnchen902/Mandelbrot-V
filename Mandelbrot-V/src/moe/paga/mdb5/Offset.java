package moe.paga.mdb5;

import java.util.Objects;

/**
 * An immutable representation of offsets.
 * 
 * @author johnchen902
 */
public class Offset {
	private final int x, y;

	/**
	 * Create an offset that is still at the original location.
	 */
	public Offset() {
		this(0, 0);
	}

	/**
	 * Create an offset with specified components.
	 * 
	 * @param x
	 *            the X component of the offset
	 * @param y
	 *            the Y component of the offset
	 */
	public Offset(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the X component of the offset.
	 * 
	 * @return the X component of the offset
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the Y component of the offset.
	 * 
	 * @return the Y component of the offset
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the offset that each component is non-negative and smaller than
	 * the corresponding component of {@code size}. Moreover, each component of
	 * this offset and the returned offset are congruent modulo the
	 * corresponding component of {@code size}.
	 * 
	 * @param size
	 *            {@code size} described above
	 * @return the normalized offset
	 * @see Offset#normalize(Location, Size)
	 */
	public Offset normalize(Size size) {
		return new Offset(Math.floorMod(x, size.getWidth()), Math.floorMod(y, size.getHeight()));
	}

	/**
	 * Returns the location that is logically equals to this offset and the
	 * specified location when combined with the result of
	 * {@code this.normalize(size)}.
	 * 
	 * @param location
	 *            the location to be normalize
	 * @param size
	 *            the size that the above description under
	 * @return the normalized location
	 * @see Offset#normalize(Size)
	 */
	public Location normalize(Location location, Size size) {
		Objects.requireNonNull(location);
		int x = this.x, y = this.y;
		while (x < 0) {
			x += size.getWidth();
			location = location.decreaseX();
		}
		while (x >= size.getWidth()) {
			x -= size.getWidth();
			location = location.increaseX();
		}
		while (y < 0) {
			y += size.getHeight();
			location = location.decreaseY();
		}
		while (y >= size.getHeight()) {
			y -= size.getHeight();
			location = location.increaseY();
		}
		return location;
	}

	@Override
	public int hashCode() {
		return 31 * (31 + x) + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Offset) {
			Offset o = (Offset) obj;
			return x == o.x && y == o.y;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Offset [(" + x + ", " + y + ")]";
	}
}
